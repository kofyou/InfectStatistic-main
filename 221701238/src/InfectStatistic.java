import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic 主程序存在的类
 * TODO
 *
 * @author 221701238_周宇靖
 * @version 1.3
 * @since 2020-02-08
 */
public class InfectStatistic {
    //常量PROVINCE_ARRAY，用于存储所有的省份名字，已按拼音排序
    public static String[] PROVINCE_ARRAY = {"安徽", "澳门", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州",
            "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海",
            "山东", "山西", "陕西", "上海", "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
    //全局变量statisticsInformationArrayList，存储所有省份的疾病信息
    public static ArrayList<StatisticsInformation> statisticsInformationArrayList = initStatisticsInformation();

    /**
     * 初始化各省份信息数组的方法
     * @return ArrayList<StatisticsInformation>    返回一个ArrayList<StatisticsInformation>数组
     */
    public static ArrayList<StatisticsInformation> initStatisticsInformation() {
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
     * @param p      Pattern类
     * @param str    原字符串
     * @return       新字符串
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
     * 主程序执行的函数方法
     * @param args     读取命令行参数的数组
     * @return null
     */
    public static void main(String[] args) {

    }
}

/**
 * 统计全国和所有省的情况的类
 */
class StatisticsInformation{
    public String name;    //省份名字
    public int infection;  //感染患者人数
    public int suspect;    //疑似患者人数
    public int cure;       //已治愈人数
    public int dead;       //已死亡人数

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