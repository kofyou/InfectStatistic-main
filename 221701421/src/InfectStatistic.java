import java.io.*;
import java.lang.reflect.Array;
import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */

class ProcessParameter {
    private ArrayList<String> provinceList;//省份列表
    private HashMap<String, Integer> typeMap;//查询的类别ip、sp、cure、dead
    private String outputPath;//输出的目标文件路径
    private File logDir;//日志文件夹
    private Date date = null;//查询截止日期

    public static void main(String[] args) {
        ProcessParameter processParameter = new ProcessParameter();
        processParameter.processParameters(args);
        processParameter.statistic();
    }

    public void processParameters(String []args){
        List list = Arrays.asList(args);

        int dateIndex = list.indexOf("-date");//截止日期.
        String dateString = "";
        if (dateIndex > 0) {//如果有传入-date参数
            dateString = args[dateIndex + 1];
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = null;
        if (dateIndex >= 0) {
            try {
                date = simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                System.out.println("时间参数非法");
                System.exit(-1);
            }
        }

        int dirIndex = list.indexOf("-log");//日志文件目录
        if (dirIndex < 0) {
            System.out.println("错误：没有传入日志文件路径");
            System.exit(-1);
        }
        String dirPath = args[dirIndex + 1];//日志文件夹路径
        logDir = new File(dirPath);//日志文件夹

        int outputIndex = list.indexOf("-out");//输出文件目录
        if (outputIndex < 0) {
            System.out.println("错误：没有传入输出文件路径");
            return;
        }
        outputPath = args[outputIndex + 1];

        int provinceListIndex = list.indexOf("-province");//传入参数中-province的下标索引
        int provinceListLastIndex = findLastIndex(args, provinceListIndex);//查询的最后一个省份的下标索引
        provinceList = new ArrayList<String>();//省份列表
        if (provinceListIndex >= 0) {//有传入-province参数
            for (int i = provinceListIndex + 1; i <= provinceListLastIndex; i++) {
                provinceList.add(args[i]);
            }
        }

        typeMap = new HashMap<>();//查询的类别ip、sp、cure、dead
        int typeIndex = list.indexOf("-type");
        int typeLastIndex = findLastIndex(args, typeIndex);
        int t = typeIndex > 0 ? 0 : 1;//是否有传入-type参数 t=0:有  t=1:没有
        typeMap.put("ip", t);
        typeMap.put("sp", t);
        typeMap.put("cure", t);
        typeMap.put("dead", t);
        if (typeIndex >= 0) {//有传入-type参数
            for (int i = typeIndex + 1; i <= typeLastIndex; i++) {
                typeMap.put(args[i], 1);
            }
        }
    }

    public void statistic(){
        //开始统计
        InfectStatistic infectStatistic = new InfectStatistic();
        infectStatistic.statistic(logDir, date);
        infectStatistic.output(outputPath, provinceList, typeMap);
    }


    public int findLastIndex(String[] list, int begin) {
        int len = list.length;
        int index = begin + 1;
        while (index < len && list[index].charAt(0) != '-') {
            index++;
        }
        return index - 1;
    }

    public ArrayList<String> getProvinceList() {
        return provinceList;
    }

    public HashMap<String, Integer> getTypeMap() {
        return typeMap;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public File getLogDir() {
        return logDir;
    }

    public Date getDate() {
        return date;
    }
}


class InfectStatistic {
    private String []provinceList = {
        "安徽", "北京", "重庆","福建", "甘肃", "广东", "广西", "贵州", "海南", "河北", "河南",
            "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海",
            "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"
    };
    private HashMap<String, ProvanceInfo> provinceMap = new HashMap<>();
    private List<String> fileList = new ArrayList<>();
    private int infectTotalNum,suspectedTotalNum,cureTotalNum,diedTotalNum;
    public InfectStatistic(){
        for(String p : provinceList){
            provinceMap.put(p, new ProvanceInfo(p));
        }
        infectTotalNum = suspectedTotalNum = cureTotalNum = diedTotalNum = 0;
    }

    public String[] getProvinceList() {
        return provinceList;
    }

    public HashMap<String, ProvanceInfo> getProvinceMap() {
        return provinceMap;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public int getInfectTotalNum() {
        return infectTotalNum;
    }

    public int getSuspectedTotalNum() {
        return suspectedTotalNum;
    }

    public int getCureTotalNum() {
        return cureTotalNum;
    }

    public int getDiedTotalNum() {
        return diedTotalNum;
    }

    public void processLogDir(File dir){//处理log文件夹的日志文件
        for (File f : dir.listFiles()) {//将目录下所有的文件名加入fileList列表用于排序
            fileList.add(f.getName().split("\\.")[0]);
        }
        Collections.sort(fileList);
    }

    public int processDate(Date date){//时间处理
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String lastDateString = fileList.get(fileList.size() - 1);//最大日期
            Date lastDate = simpleDateFormat.parse(lastDateString);
            if (date != null && date.compareTo(lastDate) > 0) {
                System.out.println("抱歉，日期超出范围");
                return -1;
            }
        }catch (Exception e){
            return -1;
        }
        return 0;
    }

    public int statistic(File dir, Date date){//统计
        try {
            BufferedReader reader = null;
            String line;
            processLogDir(dir);
            processDate(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (String fileDateString : fileList) {
                File file = new File(dir.getPath() + "/" + fileDateString + ".log.txt");
                Date fileDate = simpleDateFormat.parse(fileDateString);//日志日期
                if (date != null && fileDate.compareTo(date) > 0) {
                    break;
                }
                reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine()) != null) {
                    if (line.charAt(0) != '/') {//该行不为注释
                        updateProvinceInfo(line);
                    }
                }
            }
        }catch (Exception e){

        }
        return 0;
    }

    public void updateProvinceInfo(String line){
        String message[] = line.split(" ");
        ProvanceInfo province = provinceMap.get(message[0]);
        String lastMessage = message[message.length-1];
        int num = Integer.parseInt(lastMessage.substring(0,lastMessage.length()-1));//更新人数
        switch (message.length){
            case 3://死亡、治愈
                province.setDoesRefered(true);
                if(message[1].equals("死亡")){
                    province.diedNumAdd(num);
                    province.infectNumSub(num);
                    diedTotalNum += num;
                    infectTotalNum -= num;
                }else{//治愈
                    province.cureNumAdd(num);
                    province.infectNumSub(num);
                    cureTotalNum += num;
                    infectTotalNum -= num;
                }
                break;
            case 4://新增、确诊、排除
                province.setDoesRefered(true);
                if(message[1].equals("新增")){
                    if(message[2].equals("感染患者")){
                        province.infectNumAdd(num);
                        infectTotalNum += num;
                    }else{//疑似患者
                        province.suspectedAdd(num);
                        suspectedTotalNum += num;
                    }
                }else if(message[1].equals("排除")){//排除疑似患者
                    province.suspectedSub(num);
                    suspectedTotalNum -= num;
                }else{//确诊感染
                    province.suspectedSub(num);
                    province.infectNumAdd(num);
                    suspectedTotalNum -= num;
                    infectTotalNum += num;
                }
                break;
            case 5://从A省流入B省
                ProvanceInfo provinceB = provinceMap.get(message[3]);
                province.setDoesRefered(true);
                provinceB.setDoesRefered(true);
                if(message[1].equals("感染患者")){
                    province.infectNumSub(num);
                    provinceB.infectNumAdd(num);
                }else{//疑似患者
                    province.suspectedSub(num);
                    provinceB.suspectedAdd(num);
                }
                break;
        }
    }

    public void output(String outputPath, ArrayList<String>provinceList, HashMap<String, Integer> typeMap){
        try {
            Formatter writer = new Formatter(outputPath);
            int infectNum, suspectedNum, cureNum, diedNum;
            int infectDoesShow, suspectedDoesShow, cureDoesShow, diedDoesShow;
            infectNum = suspectedNum = cureNum = diedNum = 0;
            infectDoesShow = typeMap.get("ip");
            suspectedDoesShow = typeMap.get("sp");
            cureDoesShow = typeMap.get("cure");
            diedDoesShow = typeMap.get("dead");

            if(provinceList.contains("全国") || provinceList.size() == 0){
                writer.format("全国  ");
                if(infectDoesShow > 0){
                    writer.format("感染患者:%d人  ", infectTotalNum);
                }
                if(suspectedDoesShow > 0){
                    writer.format("疑似患者%d人  ", suspectedTotalNum);
                }
                if(cureDoesShow > 0){
                    writer.format("治愈%d人  ", cureTotalNum);
                }
                if(diedDoesShow > 0){
                    writer.format("死亡%d人  ", diedTotalNum);
                }
                writer.format("\n");
            }

            if(provinceList.size() == 0){//指令没有传入-province参数
                for(ProvanceInfo p : provinceMap.values()){
                    if(p.isDoesRefered()){
                        p.output(writer,typeMap);
                    }
                }
            }else{//有传入-province参数
                for(ProvanceInfo p : provinceMap.values()){
                    if(provinceList.contains(p.getName())){
                        p.output(writer,typeMap);
                    }
                    infectNum += p.getInfectNum();
                    suspectedNum += p.getSuspectedNum();
                    cureNum += p.getCureNum();
                    diedNum += p.getDiedNum();
                }
            }

            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class ProvanceInfo{
    private String name;//省名
    private int infectNum = 0;//感染数
    private int suspectedNum = 0;//疑似数
    private int diedNum = 0;//死亡数
    private int cureNum = 0;//治愈数
    private boolean doesRefered = false;//是否有日志提到

    public ProvanceInfo(String name){
        this.name = name;
    }

    public int getInfectNum() {
        return infectNum;
    }

    public int infectNumAdd(int num){
        return infectNum += num;
    }

    public int infectNumSub(int num){
        return infectNum -= num;
    }

    public int getSuspectedNum() {
        return suspectedNum;
    }

    public int suspectedAdd(int num){
        return suspectedNum += num;
    }

    public int suspectedSub(int num){
        return suspectedNum -= num;
    }

    public int getDiedNum() {
        return diedNum;
    }

    public int diedNumAdd(int num){
        return diedNum += num;
    }

    public int diedNumSub(int num){
        return diedNum -= num;
    }

    public int getCureNum() {
        return cureNum;
    }

    public int cureNumAdd(int num){
        return cureNum += num;
    }

    public int cureNumSub(int num){
        return cureNum -= num;
    }

    public String getName() {
        return name;
    }

    public boolean isDoesRefered() {
        return doesRefered;
    }

    public void setDoesRefered(boolean doesRefered) {
        this.doesRefered = doesRefered;
    }

    public void output(Formatter writer, HashMap<String, Integer> typeMap) {
        int infectDoesShow, suspectedDoesShow, cureDoesShow, diedDoesShow;
        infectDoesShow = typeMap.get("ip");
        suspectedDoesShow = typeMap.get("sp");
        cureDoesShow = typeMap.get("cure");
        diedDoesShow = typeMap.get("dead");
        writer.format(name + "  ");
        if (infectDoesShow > 0) {
            writer.format("感染患者:%d人  ", infectNum);
        }
        if (suspectedDoesShow > 0) {
            writer.format("疑似患者%d人  ", suspectedNum);
        }
        if (cureDoesShow > 0) {
            writer.format("治愈%d人  ", cureNum);
        }
        if (diedDoesShow > 0) {
            writer.format("死亡%d人  ", diedNum);
        }
        writer.format("\n");
    }
}