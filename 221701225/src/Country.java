import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 存有各省疫情信息的全国类
 * 使用了单例模式，该类的唯一实例由该类的getInstance()方法获得
 */
public class Country {
    HashMap<String,Province> provincesMap;
    public static String[] PROVINCES={"安徽","北京","重庆","福建","甘肃","广东","广西",
        "贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁",
        "内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆",
        "云南","浙江"};
    private static final Country country=new Country();

    private Country(){
        provincesMap=new HashMap<>();

        //把代表各个省份的Province对象加入map中方便存取
        for(String provinceName:PROVINCES){
            Province province=new Province(provinceName);
            provincesMap.put(provinceName,province);
        }
        System.out.println("Country单例对象已创建");
    }

    /**
     * 获取Country类的唯一实例
     * @return Country类的唯一实例
     */
    public static Country getInstance(){
        return country;
    }

    /**
     * @param name 省份名称
     * @return 对应的Province对象
     */
    public Province getProvince(String name){
        return provincesMap.get(name);
    }

    /**
     *录入各省份的一天中的疫情信息
     * @param dailyInfos 由Log获取的某一天的各省疫情信息
     */
    public void logData(HashMap<String,DailyInfo> dailyInfos){
        for(String provinceName:PROVINCES){
            Province province=provincesMap.get(provinceName);
            DailyInfo info=dailyInfos.get(provinceName);
            province.addDailyInfo(info);
        }
    }

    public DailyInfo getCountryDailyInfo(LocalDate beginDate,LocalDate endDate){
        DailyInfo infectInfo=new DailyInfo(endDate);

        //亿些计算...TODO

        return infectInfo;
    }

    public HashMap<String,DailyInfo> getProvinceDailyInfos(LocalDate beginDate,LocalDate endDate,
                                                           String[] provinceNames){

    }




}
