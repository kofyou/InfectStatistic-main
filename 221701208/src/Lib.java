import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Lib {
    static  String[] allType ={"ip","sp","cure","dead"};
    static String unit = "人 ";
    static String explain = "// 该文档并非真实数据，仅供测试使用\n" + "//命令：" ;

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



    /**
     * @param type ip、dead、sp、cure与中文的对应
     * @return
     */
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


    /**
     * @param str 字符串提取数字并转换为数字
     * @return
     */
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


    /**
     * @param statistic 设置日期
     */
    public static void setDate(InfectStatistic statistic){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        statistic.strDate = dateNowStr;
    }

}


//嘿，终于写完