import java.util.Date;
import java.io.File;
import java.io.*;
import java.util.regex.*;
import java.text.SimpleDateFormat;
import java.io.FileNotFoundException;

/**
 * CmdAndFile
 *
 * @author Dai_Yining
 * @date 2020-2-11
 */
class CmdAndFile
{
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date currentDate = new Date(System.currentTimeMillis());
    public String date = dateFormat.format(currentDate);   //默认日期(当前日期)

    public String logFileName = date+".log.txt";  //默认日志文件名(当前日期log.txt)

    public String log;  //日志文件位置

    public String out;  //输出文件位置

    /*
     * type：感染类型列表
     */
    public String[] type = {"感染患者", "疑似患者", "治愈", "死亡患者"};

    /*
     * typeOrder：确认-type输出顺序
     * ip：type[x]=0      sp：type[x]=1
     * cure：type[x]=2   dead：type[x]=3
     *
     */
    public int[] typeOrder = {-1,-1,-1,-1};
    public int checkType = 0;


    /*
     * province：各省份列表 除去港澳台 31个省份 + "全国"
     * 全国0 , 安徽1 , 北京2 , 重庆3 , 福建4 , 甘肃5, 广东6 , 广西7 ,
     * 贵州8 , 海南9 , 河北10 , 河南11 , 黑龙江12 , 湖北13 , 湖南14 , 吉林15 ,
     * 江苏16 , 江西17 , 辽宁18 , 内蒙古19 , 宁夏20 , 青海21 , 山东22 , 山西23,
     * 陕西24 , 上海25 , 四川26 , 天津27 , 西藏28 , 新疆29 , 云南30 , 浙江31
     */
    public String[] province = { "全国","安徽","北京","重庆","福建","甘肃","广东","广西",
            "贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林",
            "江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西",
            "陕西","上海","四川","天津","西藏","新疆","云南","浙江"};

    /*
     * statistics：各省份各感染类型统计列表
     * statistics[32][]：除去港澳台31个省份 + "全国"
     * statistics[][5]：-type四种可选值 + -provice查询确认位
     *
     */
    public int[][] statistics = new int[32][5];

    public int checkProvince = 0;

    public String[] strCmd;  //当前输入cmd命令

    CmdAndFile(String[] arg)
    {
        strCmd = arg;
    }

    //解析命令
    /*
     * getValidLog：获取有效日志文件路径
     * itemLog：cmd命令中-log路径在arg[]的索引值
     */
    public int getValidLog(int itemLog)
    {
        /*  if (strCmd[itemLog].matches("^[A-z]:\\\\(.+?s\\\\)*$"))
                    log = strCmd[itemLog];
            else
                return -1;
            return itemLog;
         */
        if (strCmd[itemLog].startsWith("-"))
            return -1;
        else
        {
            log = strCmd[itemLog];
            return itemLog;
        }
    }

    /*
     * getValidOut：获取有效输出文件路径
     * itemOut：cmd命令中-out路径在arg[]的索引值
     */
    public int getValidOut(int itemOut)
    {

        /*  if (strCmd[itemOut].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$"))
                out = strCmd[itemOut];
            else
                return -1;
            return itemOut;
         */
        if (strCmd[itemOut].startsWith("-"))
            return -1;
        else
        {
            out = strCmd[itemOut];
            return itemOut;
        }
    }

    /*
     * isDate：判断输入日期格式正确
     * dateStr：cmd命令中-date的值
     */
    public boolean isDate (String dateStr)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            format.setLenient(false);
            format.parse(dateStr);
            String[] dateArray = dateStr.split("-");
            for ( int i = 0; i < dateArray.length; i++)
            {
                if (!dateArray[i].matches("[0-9]*"))
                    return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /*
     * getValidDate：获取有效日期
     * itemDate：cmd命令中-date值在arg[]的索引值
     */
    public int getValidDate(int itemDate)
    {
        String dateStr = strCmd[itemDate];
        if (isDate(dateStr))
        {
            if (dateStr.compareTo(date) <= 0)  //查询日期 <= 当前日期，查询有效
            {
                logFileName = dateStr + ".log.txt";
                return itemDate;
            }
            else
                return -1;
        }
        else
            return -1;
    }

    /*
     * getValidType：获取有效感染种类
     * itemType：cmd命令中-type值在arg[]的索引值
     */
    public int getValidType(int itemType)
    {
        int typeOrderNum = 0;
        int itemTypeFlag = itemType;
        while (itemType < strCmd.length)
        {
            if(strCmd[itemType].equals("ip"))
            {
                typeOrder[typeOrderNum] = 0;
                itemType++;
                typeOrderNum++;
            }
            else if(strCmd[itemType].equals("sp"))
            {
                typeOrder[typeOrderNum] = 1;
                itemType++;
                typeOrderNum++;
            }
            else if(strCmd[itemType].equals("cure"))
            {
                typeOrder[typeOrderNum] = 2;
                itemType++;
                typeOrderNum++;
            }
            else if(strCmd[itemType].equals("dead"))
            {
                typeOrder[typeOrderNum] = 3;
                itemType++;
                typeOrderNum++;
            }
            else
                break;
        }
        if (itemType == itemTypeFlag)
            return -1;
        else
            return (itemType - 1);
    }

    /*
     * getValidType：获取有效查询省份
     * itemProvince：cmd命令中-province值在arg[]的索引值
     */
    public int getValidProvince(int itemProvince)
    {
        int end;
        int itemProvinceFlag = itemProvince;
        while (itemProvince < strCmd.length)
        {
            end = 1;  //判断是否结束省份查询
            for (int i = 0; i < province.length; i++)
            {
                if (province[i].equals(strCmd[itemProvince]))
                {
                    statistics[i][4] = 1;
                    itemProvince++;
                    end=0;
                    break;
                }
            }
            if (end == 1)
                break;
        }
        if (itemProvince == itemProvinceFlag)
            return -1;
        else
            return (itemProvince - 1);
    }

    /*
     * validCmd：解析cmd命令并判断是否有效
     */
    public String getMinLogName(String filepath)
    {
        //String filepath = log;
        String minLogName;
        File logFile = new File(filepath);
        File[] logFileList = logFile.listFiles();
        minLogName = logFileList[0].getName();
        for (File file : logFileList)
        {
            if (file.getName().compareTo(minLogName) <= 0)
            {
                minLogName = file.getName();
            }
        }
        return minLogName;
    }

    public boolean validCmd()  //validCmd：输入命令有效
    {
        int i;
        String dateCmd = date;
        if (!strCmd[0].equals("list"))
        {
            System.out.println("命令应以'list'开始！请重新输入！");
            return false;
        }
        for (i = 1; i < strCmd.length; i++)
        {
            if (strCmd[i].equals("-log"))
            {
                i = getValidLog(++i);
                if (i == -1)
                {
                    System.out.println("日志文件夹路径无效！请重新输入！");
                    return false;
                }
            }
            else if (strCmd[i].equals("-out"))
            {
                i = getValidOut(++i);
                if (i < 0)
                {
                    System.out.println("输出文件路径无效！请重新输入！");
                    return false;
                }
            }
            else if (strCmd[i].equals("-date"))
            {
                dateCmd = strCmd[i+1]+".log.txt";
                i = getValidDate(++i);
                if (i < 0)
                {
                    System.out.println("查询日期无效！请重新输入！");
                    return false;
                }
            }
            else if (strCmd[i].equals("-type"))
            {
                checkType = 1;
                i = getValidType(++i);
                if (i < 0)
                {
                    System.out.println("查询类型无效！请重新输入！");
                    return false;
                }
            }
            else if (strCmd[i].equals("-province"))
            {
                checkProvince = 1;
                i = getValidProvince(++i);
                if (i < 0)
                {
                    System.out.println("查询省份无效！请重新输入！");
                    return false;
                }
            }
            else
            {
                System.out.println("命令错误！请重新输入！");
                return false;
            }
        }
        if (dateCmd.compareTo(getMinLogName(log)) < 0)
        {
            System.out.println("该日暂无记录！请重新输入！");
            return false;
        }
        if (checkType == 0)
        {
            for (int j=0; j < 4 ;j++)
                typeOrder[j] = j;
        }
        if (checkProvince == 0)
        {
            statistics[0][4] = 1;
        }
        return true;
    }


    //文件操作
    /*
     * getLogList：获取指定目录下需处理文件
     * log：-log指定日志文件目录
     */
    public void getLogList ()
    {
        String filePath;
        File logFile = new File(log);
        File[] logFileList = logFile.listFiles();
        for (File file : logFileList)
        {
            if (file.getName().compareTo(logFileName) <= 0)
            {
                filePath = log + file.getName();
                readLogFile(filePath);
            }
        }
    }

    public int getItemProvince(String provinceName)
    {
        int itemProvince = 1;
        for(; itemProvince < province.length; itemProvince++)
        {
            if (provinceName.equals(province[itemProvince]))
                return itemProvince;
        }
        return 0;
    }

    /*
     * readJournalFile：按行读取所需日志文件
     * journalFileName：所需日志文件名
     */
    public void readLogFile (String logFileName)
    {
        try
        {
            InputStreamReader inputSR =
                    new InputStreamReader(new FileInputStream(logFileName),"UTF-8");
            BufferedReader br=new BufferedReader(inputSR);
            String line = null;
            while ((line = br.readLine()) != null)
            {
                if(!line.startsWith("//"))
                    countLogFile(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void statistics(int itemProvince, int itemType, int count)
    {
        statistics[0][itemType] += count;
        statistics[itemProvince][itemType] += count;
    }

    public void statistics(int itemProvince1, int itemProvince2, int itemType, int count)
    {
        statistics[itemProvince1][itemType] -= count;
        statistics[itemProvince2][itemType] +=  count;
    }

    public void statistics(int itemProvince1, int itemType1, int itemType2, String count)
    {
        int intCount = Integer.valueOf(count);
        statistics[0][itemType1] -= intCount;
        statistics[0][itemType2] += intCount;
        statistics[itemProvince1][itemType1] -= intCount;
        statistics[itemProvince1][itemType2] += intCount;
    }

    /*
     * countLogFile：统计各省感染情况
     * logFileContent：每行日志文件内容
     * statistics[][5]：0:ip  1:sp  2:cure  3:dead  4:check
     */
    public void countLogFile (String logFileContent)
    {
        int itemProvince1;
        int itemProvince2;
        int count;
        Matcher ipIncrease = Pattern.compile("\\W+ 新增 感染患者 \\d+人").matcher(logFileContent);
        Matcher spIncrease = Pattern.compile("\\W+ 新增 疑似患者 \\d+人").matcher(logFileContent);
        Matcher ipInflow = Pattern.compile("\\W+ 感染患者 流入 \\W+ \\d+人").matcher(logFileContent);
        Matcher spInflow = Pattern.compile("\\W+ 疑似患者 流入 \\W+ \\d+人").matcher(logFileContent);
        Matcher ipDead = Pattern.compile("\\W+ 死亡 \\d+人").matcher(logFileContent);
        Matcher ipCure = Pattern.compile("\\W+ 治愈 \\d+人").matcher(logFileContent);
        Matcher spChecked = Pattern.compile("\\W+ 疑似患者 确诊感染 \\d+人").matcher(logFileContent);
        Matcher spRemove = Pattern.compile("\\W+ 排除 疑似患者 \\d+人").matcher(logFileContent);
        String[]  logFileContentArray= logFileContent.split(" ");
        if (ipIncrease.find())
        {
            itemProvince1 = getItemProvince(logFileContentArray[0]);
            if (checkProvince == 0)
            {
                statistics[itemProvince1][4] = 1;
            }
            count = Integer.valueOf(logFileContentArray[3].replace("人", ""));
            statistics(itemProvince1,0,count);
        }
        else if (spIncrease.find())
        {
            itemProvince1 = getItemProvince(logFileContentArray[0]);
            if (checkProvince == 0 )
            {
                statistics[itemProvince1][4] = 1;
            }
            count = Integer.valueOf(logFileContentArray[3].replace("人", ""));
            statistics(itemProvince1,1,count);
        }
        else if (ipInflow.find())
        {
            itemProvince1 = getItemProvince(logFileContentArray[0]);
            itemProvince2 = getItemProvince(logFileContentArray[3]);
            if (checkProvince == 0)
            {
                statistics[itemProvince1][4] = 1;
                statistics[itemProvince2][4] = 1;
            }
            count = Integer.valueOf(logFileContentArray[4].replace("人", ""));
            statistics(itemProvince1, itemProvince2, 0, count);
        }
        else if (spInflow.find())
        {
            itemProvince1 = getItemProvince(logFileContentArray[0]);
            itemProvince2 = getItemProvince(logFileContentArray[3]);
            if (checkProvince == 0)
            {
                statistics[itemProvince1][4] = 1;
                statistics[itemProvince2][4] = 1;
            }
            count = Integer.valueOf(logFileContentArray[4].replace("人", ""));
            statistics(itemProvince1, itemProvince2, 1, count);
        }
        else if (ipDead.find())
        {
            itemProvince1 = getItemProvince(logFileContentArray[0]);
            if (checkProvince == 0)
            {
                statistics[itemProvince1][4] = 1;
            }
            String scount = logFileContentArray[2].replace("人", "");
            statistics(itemProvince1, 0, 3, scount);
        }
        else if (ipCure.find())
        {
            itemProvince1 = getItemProvince(logFileContentArray[0]);
            if (checkProvince == 0)
            {
                statistics[itemProvince1][4] = 1;
            }
            String scount = logFileContentArray[2].replace("人", "");
            statistics(itemProvince1, 0, 2, scount);
        }
        else if (spChecked.find())
        {
            itemProvince1 = getItemProvince(logFileContentArray[0]);
            if (checkProvince == 0)
            {
                statistics[itemProvince1][4] = 1;
            }
            String scount = logFileContentArray[3].replace("人", "");
            statistics(itemProvince1, 1, 0, scount);
        }
        else if (spRemove.find())
        {
            itemProvince1 = getItemProvince(logFileContentArray[0]);
            if (checkProvince == 0)
            {
                statistics[itemProvince1][4] = 1;
            }
            count = Integer.valueOf(logFileContentArray[3].replace("人", ""));
            count = -count;
            statistics(itemProvince1,1,count);
        }
    }

    /*
     * writeOutFile：输出统计结果
     * journalFileContent：-out指定输出文件路径
     */
    public void writeOutFile ()
    {
        int itemProvince;
        int itemType;
        int typeInt;
        String content = "";
        try
        {
            File outFile = new File(out);
            if (!outFile.exists())
                outFile.createNewFile();
            FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (itemProvince = 0; itemProvince < 32; itemProvince++)
            {
                if (statistics[itemProvince][4] == 1)
                {
                    content = province[itemProvince]+"  ";
                    for (itemType = 0; itemType < 4; itemType++)
                    {
                        typeInt = typeOrder[itemType];
                        if (typeInt >= 0)
                        {
                            content += type[typeInt] + statistics[itemProvince][typeInt] + "人  ";
                        }
                    }
                    bw.write(content);
                    bw.write("\n");
                }
            }
            bw.write("// 该文档并非真实数据，仅供测试使用\n");
            String fileTail = "";
            for (int j = 0; j < strCmd.length; j++)
            {
                fileTail += strCmd[j]+" ";
            }
            bw.write("//命令："+fileTail);
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

public class InfectStatistic
{
    public static void main(String[] arg)
    {
        CmdAndFile commondAnalysis=new CmdAndFile(arg);
        if (commondAnalysis.validCmd())
        {
            commondAnalysis.getLogList();
            commondAnalysis.writeOutFile();
        }
    }
}
