import java.io.File;
import java.io.*;
import java.util.regex.*;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * InfectStatistic
 *
 * @author Dai_Yining
 * @date 2020-2-11
 */
public class InfectStatistic
{
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date currentDate = new Date(System.currentTimeMillis());
    public String date = dateFormat.format(currentDate);   //设置默认日期

    public String journalFileName = date+".log.txt";  //日志文件名

    public String log;  //日志文件位置

    public String out;  //输出文件位置

    /*
     * type：感染类型列表
     * ip：感染患者 ，sp：疑似患者，
     * cure：治愈 ，dead：死亡患者
     */
    public String[] type = {"ip", "sp", "cure", "dead"};

    /*
     * typeCheck：确认-type输出顺序
     * type[0]:ip  type[1]:sp
     * type[2]:cure   iptype[3]:dead
     */
    public int[] typeCheck = {0,0,0,0};

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
     */
    public int[][] statistics = new int[32][5];

    /*
     * 解析cmd命令
     */
    class CommondAnalysis
    {
        public String[] strCmd;  //当前输入cmd命令

        CommondAnalysis(String[] arg)
        {
            strCmd = arg;
        }

        /*
         * getValidLog：获取有效日志文件路径
         * itemLog：cmd命令中-log路径在arg[]的索引值
         */
        public int getValidLog(int itemLog)
        {
        /*    if (strCmd[itemLog].matches("^[A-z]:\\\\(.+?s\\\\)*$"))
                    log = strCmd[itemLog];
            else
                return -1;
            return itemLog;
         */
            log = strCmd[itemLog];
            return itemLog;
        }

        /*
         * getValidOut：获取有效输出文件路径
         * itemOut：cmd命令中-out路径在arg[]的索引值
         */
        public int getValidOut(int itemOut)
        {

        /*    if (strCmd[itemOut].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$"))
                out = strCmd[itemOut];
            else
                return -1;
            return itemOut;
         */
            out = strCmd[itemOut];
            return itemOut;
        }

        /*
         * isDate：判断输入日期格式正确
         * dateStr：cmd命令中-date的值
         */
        public boolean isDate (String dateStr)
        {
            // boolean validDate = true;
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
                if (date.compareTo(strCmd[itemDate]) >= 0)
                {
                    journalFile = strCmd[itemDate]+".log.txt";
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
            int typeOrder = 1;
            int itemTypeFlag = itemType;
            while (itemType < strCmd.length)
            {
                if(strCmd[itemType].equals("ip"))
                {
                    typeCheck[0] = typeOrder;
                    itemType++;
                    typeOrder++;
                }
                else if(strCmd[itemType].equals("sp"))
                {
                    typeCheck[1] = typeOrder;
                    itemType++;
                    typeOrder++;
                }
                else if(strCmd[itemType].equals("cure"))
                {
                    typeCheck[2] = typeOrder;
                    itemType++;
                    typeOrder++;
                }
                else if(strCmd[itemType].equals("dead"))
                {
                    typeCheck[3] = typeOrder;
                    itemType++;
                    typeOrder++;
                }
                else
                    break;
            }
            if (itemType == itemTypeFlag)
                return -1;
            else
                return (itemType-1);
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
        public boolean validCmd()  //validCmd：输入命令有效
        {
            int i;
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
                        System.out.println("日志文件路径无效！请重新输入！");
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
                    i = getValidDate(++i);
                    if (i < 0)
                    {
                        System.out.println("查询日期无效！请重新输入！");
                        return false;
                    }
                }
                else if (strCmd[i].equals("-type"))
                {
                    i = getValidType(++i);
                    if (i < 0)
                    {
                        System.out.println("查询类型无效！请重新输入！");
                        return false;
                    }
                }
                else if (strCmd[i].equals("-province"))
                {
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
            return true;
        }
    }

    class FileOpration
    {
        FileOpration()
        {

        }

        /*
         * getLogList：获取指定目录下的所有文件
         * log：-log指定日志文件目录
         */
        public void getLogList (String log)
        {

        }

        /*
         * readJournalFile：按行读取所需日志文件
         * journalFileName：所需日志文件名
         */
        public void readJournalFile (String journalFileName)
        {

        }

        /*
         * infectedCount：统计各省感染情况
         * journalFileContent：每行日志文件内容
         */
        public void infectedCount (String journalFileContent)
        {

        }

        /*
         * writeOutFile：输出统计结果
         * journalFileContent：-out指定输出文件路径
         */
        public void writeOutFile (String journalFileName)
        {

        }
    }

    public static void main(String[] arg)
    {
        InfectStatistic infectStatistic = new InfectStatistic();
        InfectStatistic.CommondAnalysis commondAnalysis= infectStatistic.new CommondAnalysis(arg);
        commondAnalysis.validCmd();



        //infectStatistic.validCmd()
    }
}
