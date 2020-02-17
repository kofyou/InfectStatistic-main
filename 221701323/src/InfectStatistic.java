import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jdk.javadoc.internal.doclets.formats.html.markup.StringContent;

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
        String str="list -log C:\\Users\\dell\\Desktop\\InfectStatistic-main\\221701323\\log -out C:\\Users\\dell\\Desktop\\InfectStatistic-main\\221701323\\result\\ListOut.txt -province 福建 浙江 -type ip dead";
        String[] strs=str.split(" ");
        String log=null;
        String out=null;
        Date date=new Date();
        String[] provinces=null;
        String[] types=null;
        for(int i=1;i<strs.length;i++){
            if(strs[i].equals("-log")){
                log=strs[++i];
            }else if(strs[i].equals("-out")){
                out=strs[++i];
            }
            else if(strs[i].equals("-date")){
                String[] s=strs[++i].split("-");
                Calendar cal = GregorianCalendar.getInstance();
                cal.set(Integer.parseInt(s[0]),Integer.parseInt(s[1])-1, Integer.parseInt(s[2]));
                date=cal.getTime();
            }else if(strs[i].equals("-type")){
                int n=i++;
                i++;
                while(strs[i].charAt(0)!='-'){
                    i++;
                    if(i==strs.length)break;
                }
                i--;
                int m=i;
                String[] news=new String[m-n];
                for(int p=n+1;p<=m;p++){
                    news[p-n-1]=strs[p];
                }
                types=news;
            }else if(strs[i].equals("-province")){
                int n=i++;
                i++;
                while(strs[i].charAt(0)!='-'){
                    i++;
                    if(i<strs.length)break;
                }
                i--;
                int m=i;
                String[] news=new String[m-n];
                for(int p=n+1;p<=m;p++){
                    news[p-n-1]=strs[p];
                }
                // for (String string : news) {
                //     System.out.println(string);
                // }
                provinces=news;
                
            }else continue;
        }
        // for (String string : provinces) {
        //     System.out.println(string);
        // }
        List list=new List(log, out, date, provinces, types);
        // list.printList();
        Work work=new Work(list);
        work.dealData();
        // work.printout();
        // work.PL.printout();
        work.Show();
        // work.list.Provinces.printout();
    }
}


//创建省份类
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
    public int GiveNumber(String str){
        if(str.equals("ip"))return ip;
        else if(str.equals("sp"))return sp;
        else if(str.equals("cure"))return cure;
        else if(str.equals("dead"))return dead;
        else return 0;
    }
}


//创建城市列表类


class ProvinceList{
    static String[] povinces={"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江","全国"};
    public Province[] List;

    //城市列表
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
            if(p.ip!=0||p.sp!=0||p.cure!=0||p.dead!=0){
                System.out.println("城市："+p.ProvinceName);
                System.out.println("ip="+p.ip);
                System.out.println("sp="+p.sp);
                System.out.println("cure="+p.cure);
                System.out.println("dead="+p.dead);
            }
        }
    }
    public Province Select(String name){
        for (Province p : List) {
            if(p.ProvinceName.equals(name))return p;
        }
        return new Province("");
    }
}


//状态类

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
//list类存储命令

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
        Provinces.printout();
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

    //将string转化为date
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


    //处理日志列表，选取符合Date的日志文件

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


    //根据日志列表读取文件，遍历文件每一行进行数据处理
    public void dealData(){
        this.Select();
        this.FileSort();
        for (String str : FileList) {
            String fileName=list.Log+"\\"+str;
            File file = new File(fileName);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String tempStr;
                while ((tempStr = reader.readLine()) != null) {
                    tempStr.trim();
                    
                    if(tempStr.charAt(0)!='/'){
                        // System.out.println(tempStr);
                        String[] lineList=tempStr.split(" ");
                        // for (String s : lineList) {
                        //     System.out.println(s);
                        //     System.out.println(s.equals("治愈"));
                        
                        // }
                        Deal(lineList);
                    }
                    
                }
                reader.close();
            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        

    }


    // 对每一行的string数组进行分析处理
    public void Deal(String[] lineList){
        // System.out.println(lineList[0]+lineList.length);
        if(lineList.length==3){
            if(lineList[1].equals("死亡")){
                //省死亡数+ 感染者数-
                Add(lineList[0],"dead",FindNumber(lineList[2]));
                Add(lineList[0],"ip",-1*FindNumber(lineList[2]));

            }
            else if(lineList[1].equals("治愈")){
                //治愈数+ 感染数-
                Add(lineList[0],"cure",FindNumber(lineList[2]));
                Add(lineList[0],"ip",-1*FindNumber(lineList[2]));

            }
        }
        else if(lineList.length==4){
            if(lineList[1].equals("新增")){
                if(lineList[2].equals("感染患者")){
                    
                    //感染患者+
                    Add(lineList[0],"ip",FindNumber(lineList[3]));
                }
                else if(lineList[2].equals("疑似患者")){
                    //疑似患者+
                    Add(lineList[0],"sp",FindNumber(lineList[3]));
                }
            }
            else if(lineList[1].equals("排除")){
                //疑似患者-
                Add(lineList[0],"sp",-1*FindNumber(lineList[3]));
            }
            else if(lineList[1].equals("疑似患者")&&lineList[2].equals("确诊感染")){
                //疑似患者- 感染患者+
                Add(lineList[0],"sp",-1*FindNumber(lineList[3]));
                Add(lineList[0],"ip",FindNumber(lineList[3]));


            }
        }
        else if(lineList.length==5){
            if(lineList[1].equals("感染患者")){
                //省1感染患者-  省2感染患者+
                Add(lineList[0],"ip",-1*FindNumber(lineList[4]));
                Add(lineList[3],"ip",FindNumber(lineList[4]));


            }
            else if(lineList[1].equals("疑似患者")){
                //省1疑似患者-  省2疑似患者+
                Add(lineList[0],"sp",-1*FindNumber(lineList[4]));
                Add(lineList[3],"sp",FindNumber(lineList[4]));

            }
        }
    }

    //需要对province类进行操作 
    //1、需要传入省名 
    //2、需要传入类型
    //3、需要传入加减
    //因此确定函数的形式为 
    public void Add(String name ,String type,int number){
        for ( Province por : PL.List) {
            if(por.ProvinceName.equals(name)){
                if(type.equals("ip")){
                    por.ip+=number;
                }
                else if(type.equals("cure")){
                    por.cure+=number;
                }
                else if(type.equals("sp")){
                    por.sp+=number;
                }
                else if(type.equals("dead")){
                    por.dead+=number;
                }
            }
        }

    }
    //需要从字符串中提去数字的函数
    public int FindNumber(String str){
        String regEx="[^0-9]";  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(str);  
        return Integer.parseInt(m.replaceAll("").trim()) ;
    }


    //根据list的provinces输出结果
    //1、
    public void Show(){
        for (int i=0;i<list.Provinces.List.length;i++){
            if(list.Provinces.List[i].ProvinceName.equals("全国")){
                for (Province p : PL.List) {
                    list.Provinces.List[i].ip+=p.ip;
                    list.Provinces.List[i].sp+=p.sp;
                    list.Provinces.List[i].cure+=p.cure;
                    list.Provinces.List[i].dead+=p.dead;
                }
            }
            else {
                Province ps=PL.Select(list.Provinces.List[i].ProvinceName);
                list.Provinces.List[i]=ps;
            }

        }
        // String[] li=list.Out.split("//");
        // String OutName=li[li.length-1];
        File file=new File(list.Out);
        if(file.exists()){
            System.out.println("文件已经存在了");
        }else{
            try{
                file.createNewFile();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            

        }
        //控制只输入types的累类型
        try {
            FileWriter fw = new FileWriter(file);
            for (Province p : list.Provinces.List) {
                fw.write(p.ProvinceName+" ");
                for (Entry<String, String> entry : list.Types.Types.entrySet()) {
                    
                    fw.write(entry.getValue()+p.GiveNumber(entry.getKey())+"人 "); 
                }
                fw.write("\n");
            }
            fw.write("// 该文档并非真实数据，仅供测试使用\n");
            fw.flush();
            fw.close();
            System.out.println("写入成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}