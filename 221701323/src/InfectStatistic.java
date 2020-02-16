import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.text.SimpleDateFormat;
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
        Calendar cal = GregorianCalendar.getInstance();
        // System.out.println("请输入年月日：");
        cal.set(2020, 0, 27);
        Date date=cal.getTime();//new Date();
        String[] provinces=null;
        String[] types=null;
        List list=new List(log, out, date, provinces, types);
        list.printList();
        Work work=new Work(list);
        work.Select();
        work.FileSort();
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        //String dateString = formatter.format(currentTime); 
        // DateFormat d1 =java.text.DateFormat.getDateInstance();
        System.out.println("log=" + Log + "\nOut=" + Out + "\nDate=" +formatter.format(DateNow)+"\n");
        // Provinces.printout();
        Types.printout();
    }
}


//日志处理类
class Work{
    List list;
    String[] FileList;
    ProvinceList PL;
    public Work(List lis){
        list=lis;
        PL=new ProvinceList(null);
        FileList=new File(list.Log).list();
    }


    public void printout(){
        for (String name : FileList) {
            System.out.println("文件名："+name);
        }
        // PL.printout();
    }

    //将string类型转化为Date类型好比较
    public Date strTodate(String str){
        Date date;
        str.trim();
        String[] strList=str.split("-");
        String s=strList[2];
        String[] strList1=s.split(".log");
        // System.out.println(strList1[0]);
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Integer.parseInt(strList[0]), Integer.parseInt(strList[1])-1, Integer.parseInt(strList1[0]));
        date = cal.getTime();
        return date;
    }

    

    //日志列表排序
    public void FileSort(){
        for(int i=0;i<FileList.length;i++){
            for(int j=i;j<FileList.length;j++){
                if(strTodate(FileList[i]).compareTo(strTodate(FileList[j]))>0){
                    String t=FileList[i];
                    FileList[i]=FileList[j];
                    FileList[j]=t;
                }
            } 
        }
    }


    //日志名和日期比较
    public boolean Compare(String str){
        // System.out.println(list.DateNow.toString()+"\n"+strTodate(str).toString());
        // System.out.println(strTodate(str).compareTo(list.DateNow));
        // System.out.println(list.DateNow.toString().equals(strTodate(str).toString()));
        if(strTodate(str).compareTo(list.DateNow)<0||list.DateNow.toString().equals(strTodate(str).toString()))
            return true; 
        else
            return false;

    }


    //处理日志列表,选取符合Date的日志文件名

    public void Select(){
        int i=0;
        for (String str : FileList) {
            if(Compare(str)){
                i++;
            }
        }
        String[] newlist=new String[i];
        int n=0;
        for(int j=0;j<FileList.length;j++){
            if(Compare(FileList[j])){
                newlist[n++]=FileList[j];
            }
        }
        FileList=newlist;
    }
}