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

    public String log;  //日志文件位置

    public String out;  //输出文件位置

    /*
     * type：感染类型列表
     * ip：感染患者 ，sp：疑似患者，
     * cure：治愈 ，dead：死亡患者
     */
    public String[] type = {"ip", "sp", "cure", "dead"};

    /*
     * province：各省份列表 除去港澳台 31个省份 + "全国"
     * 全国0 , 安徽1 , 北京2 , 重庆3 , 福建4 , 甘肃5, 广东6 , 广西7 ,
     * 贵州8 , 海南9 , 河北10 , 河南11 , 黑龙江12 , 湖北13 , 湖南14 , 吉林15 ,
     * 江苏16 , 江西17 , 辽宁18 , 内蒙古19 , 宁夏20 , 青海21 , 山东22 , 山西23,
     * 陕西24 , 上海25 , 四川26 , 天津27 , 西藏28 , 新疆29 , 云南30 , 浙江31
     */
    public String[] province = { "全国", "安徽","北京", "重庆", "福建","甘肃", "广东", "广西",
                                 "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
                                 "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西",
                                 "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};

    /*
     * statistics：各省份各感染类型统计列表
     */
    public int[][] statistics = new int[32][4];

    class CommondAnalysis
    {
        public String[] strCmd;  //当前输入的命令

        CommondAnalysis(String[] arg)
        {
            strCmd = arg;
        }

        public int validLog(int itemLog)    //validLog：日志文件路径有效
        {
            int itemCurrent;
            if (strCmd[itemLog].matches("^[A-z]:\\\\(.+?\\\\)*$"))
                log = strCmd[itemLog];
            else
                return -1;
            return itemLog;
        }

        public int validOut(int itemOut)  //validOut：输出文件路径有效
        {
            int itemCurrent;
            if (strCmd[itemOut].matches("^[A-z]:\\\\(.+?\\\\)*$"))
                out = strCmd[itemOut];
            else
                return -1;
            return itemOut;
        }

        public int validDate(int itemDate)    //validDate：日期有效
        {
            return itemDate;
        }

        public int validType(int itemType)  //validType：感染类型有效
        {
            return itemType;
        }

        public int validProvince(int itemProvince)    //validProvince：日志文件路径有效
        {
            return itemProvince;
        }

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
                    i = validLog(++i);
                    if (i < 0)
                    {
                        System.out.println("日志文件路径无效！请重新输入！");
                        return false;
                    }
                }
                else if (strCmd[i].equals("-out"))
                {
                    i = validOut(++i);
                    if (i < 0)
                    {
                        System.out.println("输出文件路径无效！请重新输入！");
                        return false;
                    }
                }
                else if (strCmd[i].equals("-date"))
                {
                    i = validDate(++i);
                    if (i < 0)
                    {
                        System.out.println("查询日期无效！请重新输入！");
                        return false;
                    }
                }
                else if (strCmd[i].equals("-type"))
                {
                    i = validType(++i);
                    if (i < 0)
                    {
                        System.out.println("查询类型无效！请重新输入！");
                        return false;
                    }
                }
                else if (strCmd[i].equals("-province"))
                {
                    i = validProvince(++i);
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

    public static void main(String[] arg)
    {
        InfectStatistic infectStatistic = new InfectStatistic();
        InfectStatistic.CommondAnalysis commondAnalysis= infectStatistic.new CommondAnalysis(arg);

        for(int i=0;i<arg.length;i++)
        {
            System.out.println(arg[i]);
        }

        //infectStatistic.validCmd()
    }
}
