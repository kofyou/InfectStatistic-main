import java.text.DateFormat;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
/**
 * InfectStatistic 
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */

public class InfectStatistic {

    public static void main(String[] args) {
        String log="C:\\Users\\dell\\Desktop\\InfectStatistic-main\\221701323\\log";
        String out="C:\\Users\\dell\\Desktop\\InfectStatistic-main\\221701323\\result";
        Date date=new Date();
        String[] provinces=null;
        String[] types=null;
        List list=new List(log, out, date, provinces, types);
        list.printList();
        Work work=new Work(list);
        work.printout();

    }
}


//创建城市类，存储城市的个个状况

class Province {
    public int ip;
    public int sp;
    public int cure;
    public int dead;
    public String ProvinceName;
    public Province(String name){
        ip=0;
        sp=0;
        cure=0;
        dead=0;
        ProvinceName=name;
    }
}


//城市列表


class ProvinceList{
    static String[] povinces={"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江","全国"
};
    public Province[] List;

    //构造函数
    public ProvinceList(String[] list){
        if(list==null){
            list=povinces;
        }
        List=new Province[list.length];
        for(int i=0;i<list.length;i++){
            List[i]=new Province(list[i]);
        }
    }
    public void printout(){
        for (Province p : List) {
            System.out.println("城市："+p.ProvinceName);
            System.out.println("ip="+p.ip);
            System.out.println("sp="+p.sp);
            System.out.println("cure="+p.cure);
            System.out.println("dead="+p.dead);
        }
    }
}


//状态类；

class TypeList{
    public Map<String, String> Types = new HashMap<String, String>();
    public TypeList(String[] list)
    {
        Types.put("ip", "感染患者");
        Types.put("sp", "疑似患者");
        Types.put("cure", "治愈");
        Types.put("dead", "死亡");
        Map<String, String> Types2 = new HashMap<String, String>();
        if (list != null)
        {
            for (String str : list) {
                Types2.put(str, Types.get(str));
            }
            Types = Types2;
        }   
    }
    public void printout(){
        for (Entry<String, String> entry : Types.entrySet()) {
            System.out.println(entry.getKey()+"="+entry.getValue()); 
        }
    }
}


//List类，用来存储输入的参数

class List{
    public String Log;
    public String Out;
    public Date DateNow;
    public ProvinceList Provinces;
    public TypeList Types;
    
    public List(String log,String out,Date date,String[] provinces,String[] types){
        Log=log;
        Out=out;
        DateNow=date;
        Provinces=new ProvinceList(provinces);
        Types=new TypeList(types);
    }
    public void printList(){
        System.out.println("log=" + Log + "\nOut=" + Out + "\nDate=" + DateNow.toString()+"\n");
        Provinces.printout();
        Types.printout();
    }
}


//日志处理类
class Work{
    List list;
    String[] FilelList;
    ProvinceList PL;
    public Work(List lis){
        list=lis;
        PL=new ProvinceList(null);
        FilelList=new File(list.Log).list();
    }

    public void printout(){
        for (String name : FilelList) {
            System.out.println("文件名："+name);
        }
        PL.printout();
    }

}