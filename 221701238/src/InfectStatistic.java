import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic 主程序存在的类
 * TODO
 *
 * @author 221701238_周宇靖
 * @version 1.9
 * @since 2020-02-08
 */
public class InfectStatistic {
    //常量PROVINCE_ARRAY，用于存储所有的省份名字，已按拼音排序
    public static String[] PROVINCE_ARRAY = {"安徽", "澳门", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州",
            "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海",
            "山东", "山西", "陕西", "上海", "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
    //全局变量statisticsInformationArrayList，存储所有省份的疾病信息
    public static ArrayList<StatisticsInformation> statisticsInformationArrayList = initStatisticsInformation();
    //存储指定文件夹下所有日志文件的文件名
    public static ArrayList<String> fileList = new ArrayList<String>();

    //获取选项后的参数并保存
    //-log 读取
    public static String readfile = null;
    //-out 输出
    public static String writefile = null;
    //-date 指定日期
    public static String datetime = null;
    //-type 指定类型
    public static ArrayList<String> typelist = null;
    //-province 指定省份
    public static ArrayList<String> provincelist = null;

    //记录选项出现的次数
    public int logNum = 0;
    public int outNum = 0;
    public int dateNum = 0;
    public int typeNum = 0;
    public int provinceNum = 0;

    //定位选项在命令行字符串中的位置
    public int logPosition = -1;
    public int outPosition = -1;
    public int datePosition = -1;
    public int typePosition = -1;
    public int provincePosition = -1;
    /**
     * 初始化各省份信息数组的方法
     * @return ArrayList<StatisticsInformation>    返回一个ArrayList<StatisticsInformation>数组
     */
    public static ArrayList<StatisticsInformation> initStatisticsInformation() {
        ArrayList<StatisticsInformation> statisticsInformationArrayList = new ArrayList<StatisticsInformation>();
        for (int i = 0; i < PROVINCE_ARRAY.length; i++) {
            statisticsInformationArrayList.add(new StatisticsInformation(PROVINCE_ARRAY[i],
                    0, 0, 0, 0));
        }
        return statisticsInformationArrayList;
    }

    /**
     * 读取一个日志文件的方法
     * @param filepath
     */
    public void readFile(String filepath) {
        File file = new File(filepath);
        BufferedReader reader = null;
        if (file.exists()) {
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String tempString = null;
                while ((tempString = reader.readLine()) != null) {
                    String opString = tempString.trim();
                    if (opString.startsWith("//")) {
                        //如果内容带有“//”注释开头，则不读取
                    }else {
                        //把读取的每行字符串转换成字符串数组，并进行相应的操作
                        String[] strArray = convertStrToArray(opString);
                        if (strArray.length == 3) {
                            operateArrayThree(strArray);
                        } else if (strArray.length == 4) {
                            operateArrayFour(strArray);
                        } else if (strArray.length == 5) {
                            operateArrayFive(strArray);
                        }
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (reader != null) {
                    try {
                        reader.close();
                    }catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }else {
            System.out.println("错误，文件不存在");
            System.exit(0);
        }
    }

    /**
     * 将读取的字符串按空格分割成数组
     * @param str              读入的字符串
     * @return String[]        返回一个字符串数组
     */
    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(" ");
        return strArray;
    }

    /**
     * 对长度为3的数组操作的方法
     * @param strArray    字符串数组
     */
    public void operateArrayThree(String[] strArray) {
        if (strArray[1].equals("死亡")) {
            //<省> 死亡 n人
            addDead(strArray);
        }else if (strArray[1].equals("治愈")) {
            //<省> 治愈 n人
            addCure(strArray);
        }
    }

    /**
     * 对长度为4的数组操作的方法
     * @param strArray    字符串数组
     */
    public void operateArrayFour(String[] strArray) {
        if (strArray[1].equals("新增")) {
            if (strArray[2].equals("感染患者")) {
                //<省> 新增 感染患者 n人
                addInfection(strArray);
            }else if (strArray[2].equals("疑似患者")) {
                //<省> 新增 疑似患者 n人
                addSuspect(strArray);
            }
        }else if (strArray[1].equals("疑似患者")) {
            //<省> 疑似患者 确诊感染 n人
            confirmSuspect(strArray);
        }else if (strArray[1].equals("排除")) {
            //<省> 排除 疑似患者 n人
            removeSuspect(strArray);
        }
    }

    /**
     * 对长度为5的数组操作的方法
     * @param strArray    字符串数组
     */
    public void operateArrayFive(String[] strArray) {
        if (strArray[1].equals("感染患者")) {
            //<省1> 感染患者 流入 <省2> n人
            moveInfection(strArray);
        }else if (strArray[1].equals("疑似患者")) {
            //<省1> 疑似患者 流入 <省2> n人
            moveSuspect(strArray);
        }
    }

    /**
     * 感染患者增加的操作方法
     * <省> 新增 感染患者 n人
     * @param strArray    字符串数组
     */
    public void addInfection(String[] strArray) {
        int i;
        String example = "[0-9]";
        //找到相应的省份，获得对应的数组下标
        for (i = 0;i < PROVINCE_ARRAY.length;i ++) {
            if (PROVINCE_ARRAY[i].equals(strArray[0])) {
                break;
            }
        }
        //获得感染人数
        String str = matchResult(Pattern.compile(example), strArray[3]);
        int num = Integer.parseInt(str);
        statisticsInformationArrayList.get(i).infection += num;
    }

    /**
     * 疑似患者增加的操作方法
     * <省> 新增 疑似患者 n人
     * @param strArray    字符串数组
     */
    public void addSuspect(String[] strArray) {
        int i;
        String example = "[0-9]";
        for (i = 0;i < PROVINCE_ARRAY.length;i ++) {
            if (PROVINCE_ARRAY[i].equals(strArray[0])) {
                break;
            }
        }
        String str = matchResult(Pattern.compile(example), strArray[3]);
        int num = Integer.parseInt(str);
        statisticsInformationArrayList.get(i).suspect += num;
    }

    /**
     * 感染患者流动的操作方法
     * <省1> 感染患者 流入 <省2> n人
     * @param strArray    字符串数组
     */
    public void moveInfection(String[] strArray) {
        int i;
        int j;
        String example = "[0-9]";
        for (i = 0;i < PROVINCE_ARRAY.length;i ++) {
            if (PROVINCE_ARRAY[i].equals(strArray[0])) {
                break;
            }
        }
        for (j = 0;j < PROVINCE_ARRAY.length;j ++) {
            if (PROVINCE_ARRAY[j].equals(strArray[3])) {
                break;
            }
        }
        String str = matchResult(Pattern.compile(example), strArray[4]);
        int num = Integer.parseInt(str);
        statisticsInformationArrayList.get(i).infection -= num;
        statisticsInformationArrayList.get(j).infection += num;
    }

    /**
     * 疑似患者流动的操作方法
     * <省1> 疑似患者 流入 <省2> n人
     * @param strArray    字符串数组
     */
    public void moveSuspect(String[] strArray) {
        int i;
        int j;
        String example = "[0-9]";
        for (i = 0;i < PROVINCE_ARRAY.length;i ++) {
            if (PROVINCE_ARRAY[i].equals(strArray[0])) {
                break;
            }
        }
        for (j = 0;j < PROVINCE_ARRAY.length;j ++) {
            if (PROVINCE_ARRAY[j].equals(strArray[3])) {
                break;
            }
        }
        String str = matchResult(Pattern.compile(example), strArray[4]);
        int num = Integer.parseInt(str);
        statisticsInformationArrayList.get(i).suspect -= num;
        statisticsInformationArrayList.get(j).suspect += num;
    }

    /**
     * 患者死亡的操作方法
     * <省> 死亡 n人
     * @param strArray    字符串数组
     */
    public void addDead(String[] strArray) {
        int i;
        String example = "[0-9]";
        for (i = 0;i < PROVINCE_ARRAY.length;i ++) {
            if (PROVINCE_ARRAY[i].equals(strArray[0])) {
                break;
            }
        }
        String str = matchResult(Pattern.compile(example), strArray[2]);
        int num = Integer.parseInt(str);
        statisticsInformationArrayList.get(i).dead += num;
        statisticsInformationArrayList.get(i).infection -= num;
    }

    /**
     * 患者治愈的操作方法
     * <省> 治愈 n人
     * @param strArray    字符串数组
     */
    public void addCure(String[] strArray) {
        int i;
        String example = "[0-9]";
        for (i = 0;i < PROVINCE_ARRAY.length;i ++) {
            if (PROVINCE_ARRAY[i].equals(strArray[0])) {
                break;
            }
        }
        String str = matchResult(Pattern.compile(example), strArray[2]);
        int num = Integer.parseInt(str);
        statisticsInformationArrayList.get(i).cure += num;
        statisticsInformationArrayList.get(i).infection -= num;
    }

    /**
     * 疑似患者确诊的操作方法
     * <省> 疑似患者 确诊感染 n人
     * @param strArray    字符串数组
     */
    public void confirmSuspect(String[] strArray) {
        int i;
        String example = "[0-9]";
        for (i = 0;i < PROVINCE_ARRAY.length;i ++) {
            if (PROVINCE_ARRAY[i].equals(strArray[0])) {
                break;
            }
        }
        String str = matchResult(Pattern.compile(example), strArray[3]);
        int num = Integer.parseInt(str);
        statisticsInformationArrayList.get(i).suspect -= num;
        statisticsInformationArrayList.get(i).infection += num;
    }
    /**
     * 疑似患者排除的操作方法
     * <省> 排除 疑似患者 n人
     * @param strArray    字符串数组
     */
    public void removeSuspect(String[] strArray) {
        int i;
        String example = "[0-9]";
        for (i = 0;i < PROVINCE_ARRAY.length;i ++) {
            if (PROVINCE_ARRAY[i].equals(strArray[0])) {
                break;
            }
        }
        String str = matchResult(Pattern.compile(example), strArray[3]);
        int num = Integer.parseInt(str);
        statisticsInformationArrayList.get(i).suspect -= num;
    }

    /**
     * 匹配并提取相应的字符串的方法
     * @param p                 Pattern类
     * @param str               原字符串
     * @return sb.toString()    新字符串
     */
    public static String matchResult(Pattern p, String str) {
        StringBuilder sb = new StringBuilder();
        Matcher m = p.matcher(str);
        while (m.find())
            for (int i = 0;i <= m.groupCount();i ++) {
                sb.append(m.group());
            }
        return sb.toString();
    }

    /**
     * 读取指定文件夹下的所有文件的方法
     * @param filepath    //文件路径
     */
    public void readDirectory(String filepath) {
        File file = new File(filepath);
        if (file == null){
            System.out.println("找不到该文件夹");
            System.exit(0);
        }
        File[] tempLists = file.listFiles();
        for (int i = 0; i < tempLists.length; i++) {
            if (tempLists[i].isFile() && tempLists[i].getName().endsWith(".log.txt")) {
                fileList.add(tempLists[i].getName());
            }
        }
        Collections.sort(fileList);
        if (fileList.size() == 0){
            System.out.println("该文件夹下无相关文件");
            System.exit(0);
        }
    }

    /**
     * 将统计好的日志信息写入文件的方法
     * @param filepath    指定文件路径
     */
    public void writeFile(String filepath) {
        File file = new File(filepath);
        BufferedWriter writer = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer = new BufferedWriter(new FileWriter(filepath));
            StatisticsInformation nation = new StatisticsInformation("全国",
                    0, 0, 0, 0);
            //统计目前全国的感染情况
            for (int i = 0;i < statisticsInformationArrayList.size();i ++) {
                nation.infection += statisticsInformationArrayList.get(i).infection;
                nation.suspect += statisticsInformationArrayList.get(i).suspect;
                nation.dead += statisticsInformationArrayList.get(i).dead;
                nation.cure += statisticsInformationArrayList.get(i).cure;
            }
            //判断需要打印的情况
            int ipNum = 0;
            int spNum = 0;
            int cureNum = 0;
            int deadNum = 0;
            //判断需要打印的省份，存储对应provinceList的下标
            ArrayList<Integer> provinceCountList = new ArrayList<Integer>();
            //判断是否打印“全国”，含有“全国”则设为1
            int nationNum = 0;
            //存储要写到文件里的一行内容
            String writeContent = "";
            //只有type选项没有province选项的情况
            if (typelist != null && provincelist == null){
                for (int i = 0;i < typelist.size();i ++ ){
                    if (typelist.get(i).equals("ip")){
                        ipNum = 1;
                    }else if (typelist.get(i).equals("sp")){
                        spNum = 1;
                    }else if (typelist.get(i).equals("cure")){
                        cureNum = 1;
                    }else{
                        deadNum = 1;
                    }
                }
                writeContent = nation.name;
                if (ipNum == 1){
                    writeContent += " 感染患者" + nation.infection +"人";
                }
                if (spNum ==1){
                    writeContent += " 疑似患者" + nation.suspect + "人";
                }
                if (cureNum == 1){
                    writeContent += " 治愈" + nation.cure + "人";
                }
                if (deadNum == 1){
                    writeContent += " 死亡" + nation.dead + "人";
                }
                writer.write(writeContent);
                writer.newLine();
                for (int i = 0; i < statisticsInformationArrayList.size(); i++) {
                    if (statisticsInformationArrayList.get(i).infection == 0
                            && statisticsInformationArrayList.get(i).suspect == 0
                            && statisticsInformationArrayList.get(i).dead == 0
                            && statisticsInformationArrayList.get(i).cure == 0) {

                    }else {
                        writeContent = statisticsInformationArrayList.get(i).name;
                        if (ipNum == 1){
                            writeContent += " 感染患者" + statisticsInformationArrayList.get(i).infection +"人";
                        }
                        if (spNum ==1){
                            writeContent += " 疑似患者" + statisticsInformationArrayList.get(i).suspect + "人";
                        }
                        if (cureNum == 1){
                            writeContent += " 治愈" + statisticsInformationArrayList.get(i).cure + "人";
                        }
                        if (deadNum == 1){
                            writeContent += " 死亡" + statisticsInformationArrayList.get(i).dead + "人";
                        }
                        writer.write(writeContent);
                        writer.newLine();
                    }
                }
            }else if (typelist == null && provincelist != null){
                //只有province选项没有type选项的情况
                for (int i = 0;i < provincelist.size();i ++){
                    if (provincelist.get(i).equals("全国")){
                        nationNum = 1;
                    }else{
                        for (int j = 0;j < PROVINCE_ARRAY.length;j ++){
                            if (provincelist.get(i).equals(PROVINCE_ARRAY[j])){
                                provinceCountList.add(j);
                                break;
                            }
                        }
                    }
                }
                Collections.sort(provinceCountList);
                if (nationNum == 1){
                    writer.write(nation.name + " 感染患者" + nation.infection + "人 疑似患者" + nation.suspect
                            + "人 治愈" + nation.cure + "人 死亡" + nation.dead + "人");
                    writer.newLine();
                }
                for (int i = 0; i < provinceCountList.size(); i++){
                    writer.write(statisticsInformationArrayList.get(provinceCountList.get(i)).name + " 感染患者"
                            + statisticsInformationArrayList.get(provinceCountList.get(i)).infection + "人 疑似患者"
                            + statisticsInformationArrayList.get(provinceCountList.get(i)).suspect + "人 治愈"
                            + statisticsInformationArrayList.get(provinceCountList.get(i)).cure + "人 死亡"
                            + statisticsInformationArrayList.get(provinceCountList.get(i)).dead + "人");
                    writer.newLine();
                }
            }else if (typelist != null && provincelist != null){
                //type选项和province选项都存在的情况
                for (int i = 0;i < typelist.size();i ++ ){
                    if (typelist.get(i).equals("ip")){
                        ipNum = 1;
                    }else if (typelist.get(i).equals("sp")){
                        spNum = 1;
                    }else if (typelist.get(i).equals("cure")){
                        cureNum = 1;
                    }else{
                        deadNum = 1;
                    }
                }
                for (int i = 0;i < provincelist.size();i ++){
                    if (provincelist.get(i).equals("全国")){
                        nationNum = 1;
                    }else{
                        for (int j = 0;j < PROVINCE_ARRAY.length;j ++){
                            if (provincelist.get(i).equals(PROVINCE_ARRAY[j])){
                                provinceCountList.add(j);
                                break;
                            }
                        }
                    }
                }
                Collections.sort(provinceCountList);
                if (nationNum == 1){
                    writeContent = nation.name;
                    if (ipNum == 1){
                        writeContent += " 感染患者" + nation.infection +"人";
                    }
                    if (spNum ==1){
                        writeContent += " 疑似患者" + nation.suspect + "人";
                    }
                    if (cureNum == 1){
                        writeContent += " 治愈" + nation.cure + "人";
                    }
                    if (deadNum == 1){
                        writeContent += " 死亡" + nation.dead + "人";
                    }
                    writer.write(writeContent);
                    writer.newLine();
                }
                for (int i = 0; i < provinceCountList.size(); i++) {
                    writeContent = statisticsInformationArrayList.get(provinceCountList.get(i)).name;
                    if (ipNum == 1){
                        writeContent += " 感染患者"
                                + statisticsInformationArrayList.get(provinceCountList.get(i)).infection +"人";
                    }
                    if (spNum ==1){
                        writeContent += " 疑似患者"
                                + statisticsInformationArrayList.get(provinceCountList.get(i)).suspect + "人";
                    }
                    if (cureNum == 1){
                        writeContent += " 治愈"
                                + statisticsInformationArrayList.get(provinceCountList.get(i)).cure + "人";
                    }
                    if (deadNum == 1){
                        writeContent += " 死亡"
                                + statisticsInformationArrayList.get(provinceCountList.get(i)).dead + "人";
                    }
                    writer.write(writeContent);
                    writer.newLine();
                }
            }else {
                //type选项和province选项都不存在的情况
                writer.write(nation.name + " 感染患者" + nation.infection + "人 疑似患者" + nation.suspect + "人 治愈"
                        + nation.cure + "人 死亡" + nation.dead + "人");
                writer.newLine();
                for (int i = 0; i < statisticsInformationArrayList.size(); i++) {
                    if (statisticsInformationArrayList.get(i).infection == 0
                            && statisticsInformationArrayList.get(i).suspect == 0
                            && statisticsInformationArrayList.get(i).dead == 0
                            && statisticsInformationArrayList.get(i).cure == 0) {

                    }else {
                        writer.write(statisticsInformationArrayList.get(i).name + " 感染患者"
                                + statisticsInformationArrayList.get(i).infection + "人 疑似患者"
                                + statisticsInformationArrayList.get(i).suspect + "人 治愈"
                                + statisticsInformationArrayList.get(i).cure + "人 死亡"
                                + statisticsInformationArrayList.get(i).dead + "人");
                        writer.newLine();
                    }
                }
                writer.write("// 该文档并非真实数据，仅供测试使用");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断是否为合法命令的方法
     * @param str          含有命令的字符串数组
     * @return boolean     合法命令则返回true，非法命令则返回false
     */
    public Boolean isRightCommand(String[] str) {
        //没有list，则为错误命令
        if (!str[0].equalsIgnoreCase("list")) {
            System.out.println("错误的命令");
            return false;
        }else {
            for (int i = 0;i < str.length;i ++) {
                //判断在命令行字符串数组中是否有出现相应选项，有则记录
                if (str[i].equals("-log")) {
                    logNum++;
                    logPosition = i;
                }else if (str[i].equals("-out")) {
                    outNum++;
                    outPosition = i;
                }else if (str[i].equals("-date")) {
                    dateNum++;
                    datePosition = i;
                }else if (str[i].equals("-type")) {
                    typeNum++;
                    typePosition = i;
                }else if (str[i].equals("-province")) {
                    provinceNum++;
                    provincePosition = i;
                }else if (str[i].startsWith("-")) {
                    //除了以上5种选项，其余选项都是有问题的
                    System.out.println("错误，" + str[i] + "选项不存在");
                    return false;
                }
            }
            //判断选项是否存在及选项后参数是否正确存在
            if (logNum == 0) {
                System.out.println("错误，log是必选选项，不可缺少");
                return false;
            }else if (outNum == 0) {
                System.out.println("错误，out是必选选项，不可缺少");
                return false;
            }else if (logNum > 1 || outNum > 1 || dateNum > 1 || typeNum > 1 || provinceNum > 1) {
                System.out.println("错误，选项不可重复");
                return false;
            }else {
                if (logPosition + 2 < str.length && !str[logPosition + 2].startsWith("-")) {
                    System.out.println("错误，log选项后只能有一个参数");
                    return false;
                }
                if (outPosition + 2 < str.length && !str[outPosition + 2].startsWith("-")) {
                    System.out.println("错误，out选项后只能有一个参数");
                    return false;
                }
                if (logPosition + 1 < str.length && str[logPosition + 1].startsWith("-")) {
                    System.out.println("错误，log选项后必须要有一个参数");
                    return false;
                }
                if (outPosition + 1 < str.length && str[outPosition + 1].startsWith("-")) {
                    System.out.println("错误，out选项后必须要有一个参数");
                    return false;
                }
                readfile = str[logPosition + 1];
                writefile = str[outPosition + 1];
                if (!readfile.endsWith("\\") && !readfile.endsWith("/")){
                    System.out.println("目录名错误");
                    return false;
                }
                if (dateNum == 1) {
                    if (datePosition + 2 < str.length && !str[datePosition + 2].startsWith("-")) {
                        System.out.println("错误，date选项后只能有一个参数");
                        return false;
                    }else {
                        datetime = str[datePosition + 1] + ".log.txt";
                    }
                }
                if (typeNum == 1) {
                    typelist = new ArrayList<String>();
                    int num = getNearLargeNum(typePosition, logPosition, outPosition, datePosition, provincePosition);
                    //如果type选项不是命令种的最后一个选项
                    if (num != typePosition) {
                        for (int i = typePosition + 1; i < num;i ++) {
                            if (str[i].equals("ip") || str[i].equals("sp") || str[i].equals("cure")
                                    || str[i].equals("dead")) {
                                typelist.add(str[i]);
                            } else {
                                System.out.println("错误，不存在" + str[i] + "选项");
                                return false;
                            }
                        }
                    }else {
                        for (int i = typePosition + 1;i < str.length;i ++) {
                            if (str[i].equals("ip") || str[i].equals("sp") || str[i].equals("cure")
                                    || str[i].equals("dead")) {
                                typelist.add(str[i]);
                            }else {
                                System.out.println("错误，不存在" + str[i] + "选项");
                                return false;
                            }
                        }
                        //判断ip,sp,cure,dead是否重复出现
                        if (isRepeatValue(typelist)) {
                            System.out.println("错误，存在重复选项");
                            return false;
                        }
                    }
                }
                if (provinceNum == 1) {
                    provincelist = new ArrayList<String>();
                    int num = getNearLargeNum(provincePosition, logPosition, outPosition, datePosition, typeNum);
                    //如果province选项不是命令种的最后一个选项
                    if (num != provincePosition) {
                        for (int i = provincePosition + 1; i < num;i ++) {
                            if (isRightProvince(str[i])) {
                                provincelist.add(str[i]);
                            } else {
                                System.out.println("错误，不存在该省份");
                                return false;
                            }
                        }
                    }else {
                        for (int i = provincePosition + 1;i < str.length;i ++) {
                            if (isRightProvince(str[i])) {
                                provincelist.add(str[i]);
                            }else {
                                System.out.println("错误，不存在该省份");
                                return false;
                            }
                        }
                        //判断省份是否重复出现
                        if (isRepeatValue(provincelist)) {
                            System.out.println("错误，存在重复选项");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 用一个数与其他四个数比较，找到最接近比它大的那个数的方法
     * 用指定选项的下标与其他四个选项的下标进行比较，判断该选项是否是命令的最后一个选项
     * 如果是，返回该选项的下标，不是则返回在它后面离它最近的下标
     * @param num       指定数
     * @param a         整型数a
     * @param b         整形数b
     * @param c         整形数c
     * @param d         整形数d
     * @return int      返回一个整数
     */
    public int getNearLargeNum(int num, int a, int b, int c, int d) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(a);
        arrayList.add(b);
        arrayList.add(c);
        arrayList.add(d);
        Collections.sort(arrayList);
        for (int i = 0;i < arrayList.size();i ++) {
            if (arrayList.get(i) > num) {
                return arrayList.get(i);
            }
        }
        return num;
    }

    /**
     * 判断字符串是否是正确的省份名字的方法
     * @param str         待判断字符串
     * @return boolean    一个boolean值，是正确的省份和全国返回true，否则返回false
     */
    public boolean isRightProvince(String str) {
        if (str.equals("全国")) {
            return true;
        }
        for (int i = 0;i < PROVINCE_ARRAY.length;i ++) {
            if (PROVINCE_ARRAY[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断ArrayList数组里是否有重复的字符串的方法
     * @param arrayList    一个arraylist数组
     * @return boolean     一个Boolean值，重复返回true，否则返回false
     */
    public boolean isRepeatValue(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size() - 1; i++) {
            for (int j = i + 1; j < arrayList.size(); j++) {
                if (arrayList.get(i).equals(arrayList.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 执行所有操作的方法
     * @param str    含有命令的字符串数组
     */
    public void allOperate(String[] str){
        boolean b = isRightCommand(str);
        //如果输入的命令有误，错误退出
        if (b == false) {
            System.exit(0);
        }
        //读取指定文件夹下所有文件
        readDirectory(readfile);
        if (datetime == null){
            //如果date选项不存在
            for (int i = 0;i < fileList.size();i ++) {
                readFile(readfile + fileList.get(i));
            }
        }else{
            //如果date选项存在，判断该日期与该文件夹下以日期为命名的所有文件的关系，通过filelist的下标定位
            int pos = -1;
            for (int i = 0;i < fileList.size();i ++) {
                if (datetime.compareTo(fileList.get(i)) < 0){
                    pos = i;
                    break;
                }
            }
            if (pos == -1){
                pos = fileList.size();
            }
            for (int i = 0;i < pos;i ++) {
                readFile(readfile + fileList.get(i));
            }
        }
        writeFile(writefile);
    }

    /**
     * 主程序执行的函数方法
     * @param args     读取命令行参数的数组
     * @return null
     */
    public static void main(String[] args) {
        InfectStatistic infectStatistic = new InfectStatistic();
        infectStatistic.allOperate(args);
    }
}

/**
 * 统计全国和所有省的情况的类
 */
class StatisticsInformation{
    //省份名字
    public String name;
    //感染患者人数
    public int infection;
    //疑似患者人数
    public int suspect;
    //已治愈人数
    public int cure;
    //已死亡人数
    public int dead;

    /**
     * 构造函数，用于给类成员赋值
     * @param name          省份名字
     * @param infection     感染患者人数
     * @param suspect       疑似患者人数
     * @param cure          已治愈人数
     * @param dead          已死亡人数
     */
    public StatisticsInformation(String name, int infection, int suspect, int cure, int dead){
        this.name = name;
        this.infection = infection;
        this.suspect = suspect;
        this.cure = cure;
        this.dead = dead;
    }
}