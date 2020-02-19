/**
 * InfectStatistic
 * TODO
 *
 * @author Kvc
 * @version 1.1.0
 * @since xxx
 */

import java.io.*;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;
class InfectStatistic
{
    private CmdArgs cmdArgs;
    private Map<String  , File> LogsMap;
    private Container container;
    private Record nation;


    public CmdArgs GetCmdArgs()
    {
        return cmdArgs;
    }

    public Map<String , File> GetLogsMap()
    {
        return LogsMap;
    }

    public void SetLogsMap(Map<String , File> LogsMap)
    {
        this.LogsMap = LogsMap;
    }

    public Container GetContainer()
    {
        return container;
    }

    public void AddIpNum(Record record , int num)
    {
        record.UpIpNum(num);
        nation.UpIpNum(num);
    }

    public void AddSpNum(Record record , int num)
    {
        record.UpSpNum(num);
        nation.UpSpNum(num);
    }

    public void AddDeadNum(Record record , int num)
    {
        record.UpDeadNum(num);
        record.DownIpNum(num);
        nation.UpDeadNum(num);
        nation.DownIpNum(num);
    }

    public void AddCureNum(Record record , int num)
    {
        record.UpCureNum(num);
        record.DownIpNum(num);
        nation.UpCureNum(num);
        nation.DownIpNum(num);
    }



    public void Sp2Ip(Record record , int num)
    {
        record.UpIpNum(num);
        record.DownSpNum(num);
        nation.UpIpNum(num);
        nation.DownSpNum(num);
    }

    public void ExcludeSp(Record record , int num)
    {
        record.DownSpNum(num);
        nation.DownSpNum(num);
    }

    public void Move(Record FromRecord , Record InfectedRecord , String type , int num)
    {
        if (type.equals(Lib.Ip))
        {
            FromRecord.DownIpNum(num);
            InfectedRecord.UpIpNum(num);
        }
        else if (type.equals(Lib.Sp))
        {
            FromRecord.DownSpNum(num);
            InfectedRecord.UpSpNum(num);
        }
    }
    //构造函数
    public InfectStatistic()
    {
        cmdArgs = new CmdArgs();
        nation = new Record();
        nation.SetProvinceName("全国");
        container = new Container();
        LogsMap = new HashMap<>();
    }

    //处理文件中的一行
    public boolean ManageLine(String line)
    {
        String[] data = line.split(" ");
        String beginStr = data[0].substring(0 ,2);
        if (beginStr.equals(Lib.Skip))
        {
            return false;
        }
        // Get Province record
        Record record = container.GetRecord(data[0]);
        if (record == null)
        {
            record = new Record();
            record.SetProvinceName(data[0]);
            container.AddRecord(record);
        }
        //获取数量
        int num = Common.parserStringToInt(data[data.length - 1]);
        int[] dataLength = {3 , 4 , 5};
        // 处理数据
        if (data.length == dataLength[0])
        {
            if (data[1].equals(Lib.Dead))
            {
                this.AddDeadNum(record , num);
            }
            else if (data[1].equals(Lib.Cure))
            {
                this.AddCureNum(record , num);
            }
        }
        else if (data.length == dataLength[1])
        {
            if (data[1].equals(Lib.Increase))
            {
                if (data[2].equals(Lib.Ip))
                {
                    this.AddIpNum(record , num);
                }
                else if (data[2].equals(Lib.Sp))
                {
                    this.AddSpNum(record , num);
                }
            }
            else if (data[1].equals(Lib.Sp))
            {
                this.Sp2Ip(record , num);
            }
            else if (data[1].equals(Lib.Exclude))
            {
                this.ExcludeSp(record , num);
            }
        }
        else if (data.length == dataLength[2])
        {
            Record InfectedRecord = container.GetRecord(data[3]);
            if (InfectedRecord == null)
            {
                InfectedRecord = new Record();
                InfectedRecord.SetProvinceName(data[3]);
                container.AddRecord(InfectedRecord);
            }
            this.Move(record , InfectedRecord , data[1] , num);
        }
        return true;
    }
    //读取日志文件
    public void ReadLogFile() throws IOException
    {
        int index = 0;
        for (Map.Entry<String , File> entry : LogsMap.entrySet())
        {
            index++;
            System.out.println("Handle file : " + entry.getKey());
            BufferedReader br = null;
            try{
                br = Common.newBufferReader(entry.getKey());
                String line  = null;
                while((line = br.readLine()) != null)
                {
                    if (!ManageLine(line))
                    {
                        break;
                    }
                }
            }catch (IOException e)
            {
                e.printStackTrace();
            }finally
            {
                br.close();
            }
        }
    }

    //输出文件
    public void OutputFile() throws IOException
    {
        String OutputPath = cmdArgs.GetOutputPath();
        BufferedWriter bw = null;
        try
        {
            bw = Common.NBWriter(OutputPath);
            if (!cmdArgs.HasProvince() || cmdArgs.GetProvinces().contains(Lib.National))
            {
                nation.OutRecord(bw , cmdArgs.GetTypes());
            }
            container.OutContainer(bw , cmdArgs.GetTypes() , cmdArgs.GetProvinces());
            bw.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally
        {
            bw.close();
        }
    }
    public static void main(String[] args) throws ParseException , IOException
    {
        InfectStatistic inf = new InfectStatistic();
        CmdArgs cmdArgs =  inf.GetCmdArgs();
        if (!cmdArgs.ManageArgs(args , inf))
        {
            System.out.println("处理命令行参数失败");
            return;
        }
        inf.SetLogsMap(Common.GetFiles(cmdArgs.GetLogPath() ,cmdArgs.GetDate() ,
                Lib.LogFileRegex));
        inf.ReadLogFile();
        inf.OutputFile();
    }
}
class Container
{
    Map<String, Record> recordMap;

    public Container() {
        recordMap = new HashMap<>();
    }

    public Map<String, Record> GetRecordMap() {
        return recordMap;
    }

    public Record GetRecord(String Province) {
        return recordMap.get(Province);
    }

    public void AddRecord(Record record) {
        recordMap.put(record.GetProvinceName(), record);
    }

    public boolean CompareTo(Container c)
    {
        Map<String , Record> rdMap = c.GetRecordMap();
        if (recordMap.size() != rdMap.size())
        {
            return false;
        }
        for (Map.Entry<String , Record> entry : recordMap.entrySet())
        {
            Record record1 = entry.getValue();
            Record record2 = c.GetRecord(record1.GetProvinceName());
            if (!record1.CompareTo(record2))
            {
                return false;
            }
        }
        return true;
    }

    public void SortByProvince()
    {
        ChinaComparator mapKeyComparator = new ChinaComparator();
        recordMap = Common.sortMapByKey(recordMap , mapKeyComparator);
    }

    //将容器内容输入文件
    public void OutContainer(BufferedWriter bw  , Vector<String> types  , Vector<String> Provinces) throws IOException
    {
        SortByProvince();
        for (Map.Entry<String , Record> entry : recordMap.entrySet())
        {
            Record record = entry.getValue();
            if (Provinces != null && Provinces.size() > 0)
            {
                String Province = entry.getValue().GetProvinceName();
                if (!Provinces.contains(Province))
                {
                    continue;
                }
            }
            record.OutRecord(bw , types);
        }
    }
}
class Record
{
    private String ProvinceName;
    private int IpNum;
    private int SpNum;
    private int CureNum;
    private int DeadNum;

    public String GetProvinceName() {
        return ProvinceName;
    }

    public void SetProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }

    public int GetIpNum() {
        return IpNum;
    }

    public void SetIpNum(int IpNum) {
        this.IpNum = IpNum;
    }

    public void UpIpNum(int num) {
        this.IpNum += num;
    }

    public void DownIpNum(int num) {
        this.IpNum -= num;
    }

    public int GetSpNum() {
        return SpNum;
    }

    public void SetSpNum(int SpNum) {
        this.SpNum = SpNum;
    }

    public void UpSpNum(int num) {
        this.SpNum += num;
    }

    public void DownSpNum(int num) {
        this.SpNum -= num;
    }

    public int GetCureNum() {
        return CureNum;
    }

    public void SetCureNum(int CureNum) {
        this.CureNum = CureNum;
    }

    public void UpCureNum(int num) {
        this.CureNum += num;
    }

    public int GetDeadNum() {
        return DeadNum;
    }

    public void SetDeadNum(int DeadNum) {
        this.DeadNum = DeadNum;
    }

    public void UpDeadNum(int num)
    {
        this.DeadNum += num;
    }

    public void SetAll(String ProvinceName , int Sp , int Ip , int Dead , int Cure)
    {
        this.SetProvinceName(ProvinceName);
        this.SetSpNum(Sp);
        this.SetIpNum(Ip);
        this.SetDeadNum(Dead);
        this.SetCureNum(Cure);
    }
    public boolean CompareTo(Record r2)
    {
        if (r2 == null)
        {
            return false;
        }
        else if (this == r2)
        {
            return true;
        }
        else if (!(this.GetProvinceName().equals(r2.GetProvinceName())))
        {
            return false;
        }
        else if (this.GetDeadNum() != r2.GetDeadNum())
        {
            return false;
        }
        else if (this.GetCureNum() != r2.GetCureNum())
        {
            return false;
        }
        else if (this.GetSpNum() != r2.GetSpNum())
        {
            return false;
        }
        else if (this.GetIpNum() != r2.GetIpNum())
        {
            return false;
        }
        return true;
    }

    //将记录输出到文件中
    public void OutRecord(BufferedWriter bw , Vector<String> types) throws IOException
    {
        String ProvinceRecord = "";
        if (types == null || types.size() == 0)
        {
            ProvinceRecord = this.GetProvinceName() + " 感染患者"
                    + this.GetIpNum() +"人 "
                    + "疑似患者" + this.GetSpNum() + "人 "
                    + "治愈" + this.GetCureNum() + "人 "
                    + "死亡" + this.GetDeadNum() + "人";
        }
        else
        {
            ProvinceRecord = this.GetProvinceName();
            for (String str : types)
            {
                if (str.equals(Lib.AllType[0]))
                {
                    ProvinceRecord += " 感染患者" + this.GetIpNum() + "人 ";
                }
                else if (str.equals(Lib.AllType[1]))
                {
                    ProvinceRecord += " 疑似患者" + this.GetSpNum() + "人 ";
                }
                else if (str.equals(Lib.AllType[2]))
                {
                    ProvinceRecord += " 治愈" + this.GetCureNum() + "人 ";
                }
                else if (str.equals(Lib.AllType[3]))
                {
                    ProvinceRecord += " 死亡" + this.GetDeadNum() + "人 ";
                }
            }
        }
        ProvinceRecord += "\r\n";
        bw.write(ProvinceRecord);
        bw.flush();
    }
    /** show record's message **/
    public void showRecordMessage()
    {
        String ProvinceRecord = this.GetProvinceName() + " 感染患者"
                + this.GetIpNum() +"人 "
                + "疑似患者" + this.GetSpNum() + "人 "
                + "治愈" + this.GetCureNum() + "人 "
                + "死亡" + this.GetDeadNum() + "人";
        ProvinceRecord += "\r\n";
        System.out.println(ProvinceRecord);
    }

    public Record()
    {
        this.ProvinceName = "";
        this.IpNum = 0;
        this.SpNum = 0;
        this.DeadNum = 0;
        this.CureNum = 0;
    }
}
}