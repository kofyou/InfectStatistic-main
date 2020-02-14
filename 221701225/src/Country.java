import java.util.HashMap;

public class Country {
    HashMap<String,Province> provincesMap;
    public static String[] PROVINCES={"安徽","北京","重庆","福建","甘肃","广东","广西",
        "贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁",
        "内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆",
        "云南","浙江"};
    //HashMap<String,Boolean> hasOccured;
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

    public static Country getInstance(){
        return country;
    }

    public Province getProvince(String name){
        return provincesMap.get(name);
    }
    public boolean getOccured(String province){
        return provincesMap.get(province).hasOccured;
    }

    public void setOccured(String province){
        provincesMap.get(province).hasOccured=true;
    }

    public void logData(HashMap<String,DailyInfo> dailyInfos){
        for(String provinceName:PROVINCES){
            Province province=provincesMap.get(provinceName);
            DailyInfo info=dailyInfos.get(provinceName);
            province.addDailyInfo(info);
        }
    }
}
