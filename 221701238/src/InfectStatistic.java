import java.util.ArrayList;

/**
 * InfectStatistic 主程序存在的类
 * TODO
 *
 * @author 221701238_周宇靖
 * @version 1.1
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
     * 初始化各省份信息数组
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
     * 主程序执行的函数方法
     * @param args     读取命令行参数的数组
     * @return null
     */
    public static void main(String[] args) {
        System.out.println("helloworld");
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