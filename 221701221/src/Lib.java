import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Lib
 * TODO
 * Common
 * @author Kvc
 * @version 1.1.0
 * @since xxx
 */
public class Lib {
    public static String logFileRegex = "2020-[0-1][0-9]-[0-3][0-9].log.txt";
    public static String IP = "感染患者";
    public static String Sp = "疑似患者";
    public static String Cure = "治愈";
    public static String Dead = "死亡";
    public static String Exclude = "排除";
    public static String Increase = "新增";
    public static String National = "全国";
    public static String Skip = "//";
    public static String[] AllType = {"IP", "Sp", "Cure", "Dead"};
    public static String[] Provinces = {"安徽", "北京","重庆","福建","甘肃","广东","广西","贵州",
            "海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏",
            "江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西",
            "上海","四川","天津","西藏","新疆","云南","浙江"};
}
class Common {


    public static BufferedReader newBufferReader(String path) throws FileNotFoundException {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        return br;
    }

    public static BufferedWriter NBWriter(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            file = new File(path + "output.txt");
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        return bw;
    }

    public static int CompareDate(String d1, String d2) {
        Date date1 = stringToDate(d1);
        Date date2 = stringToDate(d2);
        return date1.compareTo(date2);
    }

    public static Date stringToDate(String dateStr) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            System.out.println("args's date is not invalid");
            return null;
        }
        Date now = new Date();
        if (now.compareTo(date) < 0) {
            System.out.println("now's date < date, " + dateStr + " is invalid");
            return null;
        }
        return date;
    }
    public static Container readOutFile(String outFilePath) throws IOException
    {
        Container container = new Container();
        File file = new File(outFilePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        while((line = br.readLine()) != null)
        {
            Record record = new Record();
            if (readOneLineOfOutFile(record, line))
            {
                container.AddRecord(record);
            }
            else
            {
                break;
            }
        }
        return container;
    }
}