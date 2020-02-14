import javax.naming.CompoundName;
import javax.naming.directory.Attributes;
import javax.xml.xpath.XPath;
import java.io.File;
import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    private static ArrayList<String> provincelist = new ArrayList<String>(); //需要的的省份集
    private static  ArrayList<String> typelist = new ArrayList<String>(); //需要的状态集,
    private static ArrayList<String> datelist = new ArrayList<String>(); //已处理的日志时间集
    private  static  String[] need = new String[]{"0","0","0"};
    private static String[] province = new String[]{"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林",
            "江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};//省份集
    private static String[] type = new String[]{"ip","sp","cure","dead"};//状态集，{"ip","感染患者"},{"sp","疑似患者"},{"cure","治愈患者"},{"dead","死亡患者"}

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
        filename = text.substring(text.lastIndexOf("/")+1);
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

    public static void main(String[] args)
    {//java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt

        list.put("-log",Inpath);
        args= new String[]{ "list","-date","2020-01-39","-log","D:/log/","-out","D:/output.txt","-type","ip","ap"};

        if(InputLog(args)){
            for (HashMap.Entry<String, String> entry : list.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                System.out.println(key);
                System.out.println(val);
            }
            System.out.println(Outpath);
            System.out.println(Inpath);
            System.out.println(filename);
            System.out.println(date);
            System.out.println(provincelist);
            System.out.println(typelist);
        }else{
            System.out.println("无法执行！");
        }
    }
}
