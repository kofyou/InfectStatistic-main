import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;


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
            System.out.println("文件夹路径不存在，创建路径:" + filePath);
            folder.mkdirs();
        } else {
            System.out.println("文件夹路径存在:" + filePath);
        }
        File file = new File(filePath + fileName); // 如果文件不存在就创建
        if (!file.exists()) {
            System.out.println("文件不存在，创建文件:" + filePath + fileName);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("文件已存在，文件为:" + filePath + fileName);
        }
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





}
