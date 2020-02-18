import net.sourceforge.pinyin4j.PinyinHelper;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Lib {
    static  String[] allType ={"ip","sp","cure","dead"};
    static String unit = "人 ";
    static String explain = "// 该文档并非真实数据，仅供测试使用\n" + "//命令：" ;
    static String nullRecord = "感染患者0人 疑似患者0人 治愈0人 死亡0人\n";//


    /**
     * @param outLocate 要创建的文件路径，包含文件名字
     */
    public static void creatFile(String outLocate) {
        String filePath=null,fileName=null;
        char divideChar = '/';
        int divideIndex = outLocate.lastIndexOf(divideChar);
        filePath = outLocate.substring(0,divideIndex+1);
        fileName = outLocate.substring(divideIndex+1,outLocate.length());
        File folder = new File(filePath);
        if (!folder.exists() && !folder.isDirectory()) {  //文件夹路径不存在
            folder.mkdirs();//创建路径
        }
        File file = new File(filePath + fileName); // 如果文件不存在就创建
        if (!file.exists()) {//文件不存在，创建文件
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static String toPinyin(String str){
        String tmpStr=null;
       for(int i=0;i<str.length();i++){
           String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i));
           if(str.equals("重庆") && i==0){//i==0是为了防止在轮到字符"庆"时 庆 不是多音字，没有 pinyin[1]
               tmpStr += pinyin[1];
           }
           else {
               tmpStr += pinyin[0];
           }
       }
        return tmpStr;
    }


    public static String toChinese(String type){
        if(type.equals("ip"))
            return "感染患者";
        else if(type.equals("sp"))
            return "疑似患者";
        else if(type.equals("cure"))
            return "治愈";
        else
            return "死亡";
    }



    public static int getNumber(String str){
        int number=0;
        char[] strNumber=str.toCharArray();
        int index;
        for(index=0;index<strNumber.length;index++){
           if( !(strNumber[index]>='0' && strNumber[index]<='9') )
               break;
        }
        String realNumber = str.substring(0,index);
        number = Integer.valueOf(realNumber);
        return  number;
    }


    public static void setDate(InfectStatistic statistic){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        statistic.strDate = dateNowStr;
    }

}
