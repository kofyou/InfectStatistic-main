import javax.naming.CompoundName;
import javax.naming.directory.Attributes;
import javax.xml.xpath.XPath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.Key;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */

class InfectStatistic {

    private static String proflag = "0";//省份是否为空的标志，0为空；1不空，参数保存在provincelist中。
    private static String typeflag = "0";//类型是否为空的标志，0为空；1不空，参数保存在typelist中。
    private static String dateflag = "0";//日期是否为空的标志，0为空；1不空，为最新日期。
    private static boolean flag = true;//传入参数是否全部正确
    private static String Inpath = null;//日志文件所在位置
    private static String Outpath = null;//输出文件的位置
    private static String filename = null;//输出的的文件名
    private static String named = null;//输出的的文件名后缀
    private static String date = null;//需求的日期
    private static int num = 0;//-type与-province参数个数

    static HashMap<String,String> list = new HashMap<>();
    private static String [][] commandlist ={{"-log",Inpath},{"-out",Outpath},{"-date",dateflag},{"-type",typeflag},{"-province",proflag}}; //命令集
    private static  ArrayList<String> provincelist = new ArrayList<String>();//需要的的省份集
    private static HashMap<String,Integer[]> prolist = new HashMap<String,Integer[]>(); //处理的的的省份集
    private static  ArrayList<String> typelist = new ArrayList<String>(); //需要的状态集,
    private static ArrayList<String> datelist = new ArrayList<String>(); //已处理的日志时间集
    private static ArrayList<String> dlist = new ArrayList<>(); //符合格式的日志文件名列表
    private  static  String[] need = new String[]{"0","0","0"}; //三个必要参数是否存在
    private static String[] province = new String[]{"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林",
            "江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};//省份集
    private static String[] type = new String[]{"ip","sp","cure","dead"};//状态集，{"ip","感染患者"},{"sp","疑似患者"},{"cure","治愈患者"},{"dead","死亡患者"}
    private static String[] status = new String[]{"新增 感染患者","新增 疑似患者","感染患者 流入","疑似患者 流入","死亡","治愈","疑似患者 确诊感染","排除 疑似患者"};//每日情况
    /*
    1、<省> 新增 感染患者 n人
2、<省> 新增 疑似患者 n人
3、<省1> 感染患者 流入 <省2> n人
4、<省1> 疑似患者 流入 <省2> n人
5、<省> 死亡 n人
6、<省> 治愈 n人
7、<省> 疑似患者 确诊感染 n人
8、<省> 排除 疑似患者 n人
     */

    /*处理传递进来的参数*/
    public static boolean InputLog(String[] Mes)
    {
        int i;
        for(i = 0;i<Mes.length;i++){
            if(Mes[i].equals("list")){
                need[0] = "1";
                continue;
            }
            else if(Mes[i].equals("-log")){
                i++;
                if(!(Mes[i].startsWith("-"))){
                    Inpath = Mes[i];
                    if(CheckPos(Inpath)){
                        need[1] = "1";
                        list.put("-log",Inpath);
                        continue;
                    }else{
                        System.out.println("请输入完整的日志所在位置！");
                        flag = false;
                        break;
                    }
                }else{
                    System.out.println("请输入日志文件所在位置！");
                    flag = false;
                    break;
                }
            }
            else if(Mes[i].equals("-out")){
                i++;
                if(!(Mes[i].startsWith("-"))){
                    GetPathandName(Mes[i]);
                    if(CheckPos(Outpath)){
                        if(!named.equals("txt")){
                            System.out.println("只允许输出txt文件！");
                            flag = false;
                            break;
                        }else{
                            need[2] = "1";
                            list.put("-out",Outpath);
                            continue;
                        }
                    }else{
                        System.out.println("请输入完整的文件输出位置！");
                        flag = false;
                        break;
                    }
                }else{
                    System.out.println("请输入文件输出位置！");
                    break;
                }
            }
            else if(Mes[i].equals("-date")){
                i++;
                if(!(Mes[i].startsWith("-"))){
                    dateflag = "1";
                    date = Mes[i];
                    if(CheckTime(date)){
                        list.put("-date",date);
                    }else{
                        System.out.println("请输入正确的日期格式'xxxx-xx-xx'！");
                        flag = false;
                        break;
                    }
                }else{
                    System.out.println("请输入需要查看的日期！");
                    flag = false;
                    break;
                }
            }
            else if(Mes[i].equals("-type")){
                i++;
                num=0;
                for(;i<Mes.length;i++){
                    if(!(Mes[i].substring(0,1).equals("-"))){
                        num++;
                        if(CheckType(Mes[i])){
                            typelist.add(Mes[i]);
                        }else{
                            System.out.println("请输入正确的类型！");
                            flag = false;
                            break;
                        }
                    }
                }
                typeflag = String.valueOf(num);
                list.put("-type",typeflag);
            }
            else if(Mes[i].equals("-province")){
                i++;
                num=0;
                for(;i<Mes.length;i++){
                    if(!(Mes[i].substring(0,1).equals("-"))){
                        num++;
                        if(CheckName(Mes[i])){
                            provincelist.add(Mes[i]);
                        }else{
                            System.out.println("请输入正确省份名称！");
                            flag = false;
                            break;
                        }
                    }
                }
                proflag = String.valueOf(num);
                list.put("-province",proflag);
            }else{
                System.out.println("无效命令，请输入正确的命令！");
                flag = false;
                break;
            }
        }

        for (String a:need
        ) {
            if(a.equals("0")){
                System.out.println("缺少必要参数'list'、'-log'或'-out'，请输入正确的命令！");
                flag = false;
                break;
            }else{
                flag = flag;
            }
        }
        if(proflag.equals("0")){
            provincelist.add("全国");
        }
        if(typeflag.equals("0")){
            typelist.add(type[0]);
            typelist.add(type[1]);
            typelist.add(type[2]);
            typelist.add(type[3]);
        }
        return flag;
    }

    /*检查路径是否完整*/
    public static boolean CheckPos(String pa)
    {
        String test = ":/";
        if(pa.indexOf(test) != -1){
            return true;
        }else{
            return false;
        }
    }

    /*获取输出路径、输出文件名以及文件后缀*/
    public static void GetPathandName(String text)
    {
        Outpath = text.substring(0,text.lastIndexOf("/")+1);
        filename = text.substring(text.lastIndexOf("/")+1,text.lastIndexOf("."));
        named = text.substring(text.lastIndexOf(".")+1);
    }

    /*检查日期格式是否正确*/
    public static boolean CheckTime(String text)
    {
        boolean a = text.matches("[0-9][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]");
        return a;
    }

    /*检查省份是否正确*/
    public static boolean CheckName(String text){
        boolean a = Arrays.asList(province).contains(text);
        return a;
    }

    /*检查状态是否正确*/
    public static boolean CheckType(String text){
        boolean a = Arrays.asList(type).contains(text);
        return a;
    }

    public static boolean ReadLog(){
        int count = 0;
        try {
            File file = new File(Inpath);
            if (!file.isDirectory()) {
                System.out.println("文件");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());

            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                String pattern = "[0-9][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9].log.txt";
                Pattern r = Pattern.compile(pattern);
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(Inpath + "\\" + filelist[i]);
                    Matcher m = r.matcher(readfile.getName());
                    if(m.find()){
                        count++;
                        dlist.add(readfile.getName());
                    }else{
                        continue;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        boolean a = true;
        if(count==0){
            a = false;

        }else {
            a=true;
        }
        return a;
    }

    public static void ReadData(){
        String path;
        Integer[] m1,m2;
        for(String n:dlist){
            if(n.compareTo(date) < 0) {
                path = Inpath + n;
                try (FileReader reader = new FileReader(path);
                     BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
                ) {
                    String line;
                    //网友推荐更加简洁的写法
                    while ((line = br.readLine()) != null && (!line.startsWith("//"))) {
                        // 一次读入一行数据
                        for (int i = 0; i < status.length; i++) {
                            if (line.contains(status[i])) {
                                String[] lined = line.split("\\s+");
                                String str = "";
                                String str1 = lined[lined.length - 1];
                                for (int j = 0; j < str1.length(); j++) {
                                    if (str1.charAt(j) >= 48 && str1.charAt(j) <= 57) {
                                        str += str1.charAt(j);
                                    }
                                }
                                str = str.trim();
                                Integer num = 0;
                                num = Integer.parseInt(str);
                                switch (i) {
                                    case 0: {
                                        if (prolist.containsKey(lined[0])) {
                                            m1 = prolist.get(lined[0]);
                                            m1[0] += num;
                                            prolist.put(lined[0], m1);
                                        }
                                        break;
                                    }
                                    case 1: {
                                        if (prolist.containsKey(lined[0])) {
                                            m1 = prolist.get(lined[0]);
                                            m1[1] += num;
                                            prolist.put(lined[0], m1);
                                        }
                                        break;
                                    }
                                    case 2: {
                                        if (prolist.containsKey(lined[0]) && prolist.containsKey(lined[3])) {
                                            m1 = prolist.get(lined[0]);
                                            m2 = prolist.get(lined[3]);
                                            m1[0] -= num;
                                            m2[0] += num;
                                            prolist.put(lined[0], m1);
                                            prolist.put(lined[3], m2);
                                        }
                                        break;
                                    }
                                    case 3: {
                                        if (prolist.containsKey(lined[0]) && prolist.containsKey(lined[3])) {
                                            m1 = prolist.get(lined[0]);
                                            m2 = prolist.get(lined[3]);
                                            m1[1] -= num;
                                            m2[1] += num;
                                            prolist.put(lined[0], m1);
                                            prolist.put(lined[3], m2);
                                        }
                                        break;
                                    }
                                    case 4: {
                                        if (prolist.containsKey(lined[0])) {
                                            m1 = prolist.get(lined[0]);
                                            m1[0] -= num;
                                            m1[3] += num;
                                            prolist.put(lined[0], m1);
                                        }
                                        break;
                                    }
                                    case 5: {
                                        if (prolist.containsKey(lined[0])) {
                                            m1 = prolist.get(lined[0]);
                                            m1[0] -= num;
                                            m1[2] += num;
                                            prolist.put(lined[0], m1);
                                        }
                                        break;
                                    }
                                    case 6: {
                                        if (prolist.containsKey(lined[0])) {
                                            m1 = prolist.get(lined[0]);
                                            m1[1] -= num;
                                            m1[0] += num;
                                            prolist.put(lined[0], m1);
                                        }
                                        break;
                                    }
                                    case 7: {
                                        if (prolist.containsKey(lined[0])) {
                                            m1 = prolist.get(lined[0]);
                                            m1[1] -= num;
                                            prolist.put(lined[0], m1);
                                        }
                                        break;
                                    }
                                }
                            }

                        }

                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                break;
            }
        }
    }

    public static void main(String[] args)
    {//java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
        Integer[] inte = new Integer[]{0,0,0,0};
        for(String p:province){
            prolist.put(p,new Integer[]{0,0,0,0});
        }
        args= new String[]{ "list","-date","2019-02-30","-log","D:/log/","-out","D:/output.txt","-type","ip","sp"};

        if(InputLog(args)){
            if(ReadLog()) {
                Collections.sort(dlist);
                if(dateflag.equals("0")){
                    date=dlist.get(dlist.size());
                }
                ReadData();
                for (HashMap.Entry<String, Integer[]> entry : prolist.entrySet()) {
                    String key = entry.getKey();
                    Integer[] val = entry.getValue();
                    System.out.println(key);
                    for(int i:val){
                        System.out.println(i);
                    }
                }
            }else {
                System.out.println("无日志文件！");
            }
        }else{
            System.out.println("无法执行！");
        }
    }
}
