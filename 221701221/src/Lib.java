import java.io.*;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Lib
 * TODO
 * Lib
 * Common
 * @author 岳逾先
 * @version 1.1.1
 * @since 1.8
 */
public class Lib {
    public static String LogFileRegex = "2020-[0-1][0-9]-[0-3][0-9].log.txt";
    public static String Ip = "感染患者";
    public static String Sp = "疑似患者";
    public static String Cure = "治愈";
    public static String Dead = "死亡";
    public static String Exclude = "排除";
    public static String Increase = "新增";
    public static String National = "全国";
    public static String Skip = "//";
    public static String[] AllType = {"Ip", "Sp", "Cure", "Dead"};
    public static String[] Provinces = {"安徽", "北京","重庆","福建","甘肃","广东","广西","贵州",
            "海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏",
            "江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西",
            "上海","四川","天津","西藏","新疆","云南","浙江"};
}

class Common {

    /** new BufferReader br **/
    public static BufferedReader newBufferReader(String path) throws FileNotFoundException
    {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        return br;
    }
    /** new BufferWriter bw **/
    public static BufferedWriter NBWriter(String path) throws IOException
    {
        File file = new File(path);
        if (file.isDirectory())
        {
            file = new File(path + "output.txt");
        }
        if(!file.exists())
        {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        return bw;
    }

    public static int CompareDate(String d1, String d2)
    {
        Date date1 = StringToDate(d1);
        Date date2 = StringToDate(d2);
        return date1.compareTo(date2);
    }

    public static Date StringToDate(String dateStr)
    {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            date = format.parse(dateStr);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        if (date == null)
        {
            System.out.println("args's date is not invalid");
            return null;
        }
        Date today = new Date();
        if (today.compareTo(date) < 0)
        {
            System.out.println("today's date < date, " + dateStr + " is invalid" );
            return null;
        }
        return date;
    }

    public static Container ReadOutFile(String OutFilePath) throws IOException
    {
        Container container = new Container();
        File file = new File(OutFilePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        while((line = br.readLine()) != null)
        {
            Record record = new Record();
            if (ReadOutFile(record, line))
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

    public static boolean ReadOutFile(Record record, String line)
    {
        String[] data = line.split(" ");
        if (data[0].equals(Lib.Skip))
        {
            return false;
        }
        record.SetProvinceName(data[0]);
        for (int i = 1; i < data.length; ++i)
        {
            String type = ParserStringGetType(data[i]);
            if (type.equals(Lib.Ip))
            {
                record.SetIpNum(Common.ParserStringToInt(data[i]));
            }
            else if (type.equals(Lib.Sp))
            {
                record.SetSpNum(Common.ParserStringToInt(data[i]));
            }
            else if (type.equals(Lib.Cure))
            {
                record.SetCureNum(Common.ParserStringToInt(data[i]));
            }
            else if (type.equals(Lib.Dead))
            {
                record.SetDeadNum(Common.ParserStringToInt(data[i]));
            }
        }
        return true;
    }

    public static String ParserStringGetType(String string)
    {
        String str1 = string.trim();
        String str2 = "";
        for (int i = 0; i < str1.length(); ++i)
        {
            if(str1.charAt(i) >= '0' && str1.charAt(i) <= '9')
            {
                break;
            }
            else
            {
                str2 += str1.charAt(i);
            }
        }
        return str2;
    }

    public static Date ParserDateFromLogFileName(String fileName)
    {
        String[] strs1 = null;
        if (IsWindows())
        {
            strs1 = fileName.split("\\\\");
        }
        else
        {
            strs1 = fileName.split("/");
        }
        String[] strs2 = strs1[strs1.length - 1].split("\\.");
        return StringToDate(strs2[0]);
    }

    public static int ParserStringToInt(String string)
    {
        String str1 = string.trim();
        String str2 = "";
        for (int i = 0; i < str1.length(); ++i)
        {
            if(str1.charAt(i) >= '0' && str1.charAt(i) <= '9')
            {
                str2 += str1.charAt(i);
            }
            else
            {
                continue;
            }
        }
        if (str2.length() == 0)
        {
            return 0;
        }
        int number = Integer.parseInt(str2);
        return number;
    }

    public static <A,B> Map<A, B> SortMap(Map<A, B> map, Comparator<A> comparator)
    {
        if (map == null || map.isEmpty())
        {
            return null;
        }
        Map<A, B> sortMap = new TreeMap<A, B>(comparator);
        sortMap.putAll(map);
        return sortMap;
    }

    public static Map<String, File> GetFiles(String path, String date, String regex)
    {
        File file = new File(path);
        File[] fileList = file.listFiles();
        Map<String, File> fileMap = new HashMap<>();
        if (fileList != null)
        {
            for (int i = 0; i < fileList.length; ++i)
            {
                if (fileList[i].isFile())
                {
                    String[] temp = null;
                    if (Common.IsWindows())
                    {
                        temp = fileList[i].toString().split("\\\\");
                    }
                    else
                    {
                        temp = fileList[i].toString().split("/");
                    }
                    // "2020-[0-1][0-9]-[0-3][0-9].log.txt"
                    if (!temp[temp.length - 1].matches(regex))
                    {
                        System.out.println("invalid file : " + temp[temp.length - 1] +" Skip ");
                        continue;
                    }
                    // support args -date
                    if (date != null && Common.CompareDate(temp[temp.length - 1], date) > 0)
                    {
                        continue;
                    }
                    fileMap.put(fileList[i].toString(), fileList[i]);
                }
            }
        }
        FileComparator fileComparator = new FileComparator();
        fileMap = Common.SortMap(fileMap, fileComparator);
        return fileMap;
    }

    public static boolean IsWindows()
    {
        boolean IsWindows = System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
        return IsWindows;
    }
}

class ChinaComparator implements Comparator<String>
{
    public static Map<String, Integer> provinceMap = new HashMap<>();
    static
    {
        int index = 0;
        for (String str : Lib.Provinces)
        {
            provinceMap.put(str, index);
            index++;
        }
    }

    @Override
    public int compare(String str1, String str2)
    {
        return (provinceMap.get(str1) - provinceMap.get(str2));
    }
}

class FileComparator implements Comparator<String>
{
    @Override
    public int compare(String str1, String str2)
    {
        Date date1 = Common.ParserDateFromLogFileName(str1);
        Date date2 = Common.ParserDateFromLogFileName(str2);
        return date2.compareTo(date1);
    }
}


