import java.io.*;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Lib
 * TODO
 * @author Kvc
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
        Date date1 = stringToDate(d1);
        Date date2 = stringToDate(d2);
        return date1.compareTo(date2);
    }

    /** tranform "yyyy-MM-dd" to Date **/
    public static Date stringToDate(String dateStr)
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
        // comfirm now date >= date
        Date now = new Date();
        if (now.compareTo(date) < 0)
        {
            /** now >= date **/
            System.out.println("now's date < date, " + dateStr + " is invalid" );
            return null;
        }
        return date;
    }

    /** read out put file **/
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

    /** read one line in output file **/
    public static boolean readOneLineOfOutFile(Record record, String line)
    {
        String[] data = line.split(" ");
        if (data[0].equals(Lib.Skip))
        {
            return false;
        }
        record.SetProvinceName(data[0]);
        for (int i = 1; i < data.length; ++i)
        {
            String type = parserStringGetType(data[i]);
            if (type.equals(Lib.Ip))
            {
                record.SetIpNum(Common.parserStringToInt(data[i]));
            }
            else if (type.equals(Lib.Sp))
            {
                record.SetSpNum(Common.parserStringToInt(data[i]));
            }
            else if (type.equals(Lib.Cure))
            {
                record.SetCureNum(Common.parserStringToInt(data[i]));
            }
            else if (type.equals(Lib.Dead))
            {
                record.SetDeadNum(Common.parserStringToInt(data[i]));
            }
        }
        return true;
    }

    /**
     * parser a string to get type
     * type ： 感染患者(Ip) 疑似患者(Sp) 治愈(Cure) 死亡(Dead)
     * example ： 感染患者5人 -> type is 感染患者
     */
    public static String parserStringGetType(String string)
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

    /**
     * parser date from log file name
     * log file name : XX/XX/XX/YYYY-MM-dd.log.txt
     */
    public static Date parserDateFromLogFileName(String fileName)
    {
        String[] strs1 = null;
        if (isWindows())
        {
            strs1 = fileName.split("\\\\");
        }
        else
        {
            strs1 = fileName.split("/");
        }
        String[] strs2 = strs1[strs1.length - 1].split("\\.");
        return stringToDate(strs2[0]);
    }

    /** parser number from string **/
    public static int parserStringToInt(String string)
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

    /** sort map by key **/
    public static <A,B> Map<A, B> sortMapByKey(Map<A, B> map, Comparator<A> comparator)
    {
        if (map == null || map.isEmpty())
        {
            return null;
        }
        Map<A, B> sortMap = new TreeMap<A, B>(comparator);
        sortMap.putAll(map);
        return sortMap;
    }

    /** get files from path and only get file which date < date **/
    public static Map<String, File> GetFiles(String path, String date, String regex) throws ParseException
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
                    if (Common.isWindows())
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
        // sort file by date
        FileComparator fileComparator = new FileComparator();
        fileMap = Common.sortMapByKey(fileMap, fileComparator);
        return fileMap;
    }

    public static boolean isWindows()
    {
        boolean isWindows = System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
        return isWindows;
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
        Date date1 = Common.parserDateFromLogFileName(str1);
        Date date2 = Common.parserDateFromLogFileName(str2);
        return date2.compareTo(date1);
    }
}


