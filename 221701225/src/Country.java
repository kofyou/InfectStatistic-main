import java.util.HashMap;

public class Country {
    HashMap<String,Province> provincesMap;
    public static String[] provinces={"安徽","北京","重庆","福建","甘肃","广东","广西",
        "贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁",
        "内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆",
        "云南","浙江"};

    public Country(){
        provincesMap=new HashMap<>();

        //把代表各个省份的Province对象加入map中方便存取
        for(String provinceName:provinces){
            Province province=new Province(provinceName);
            provincesMap.put(provinceName,province);
            System.out.println(provinceName+"对象已创立并加入map");
        }
    }
}
