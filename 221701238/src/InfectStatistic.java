/**
 * InfectStatistic 主程序存在的类
 * TODO
 *
 * @author 221701238_周宇靖
 * @version 1.0
 * @since 2020-02-08
 */
public class InfectStatistic {
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