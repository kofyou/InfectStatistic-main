import java.io.*;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

class InfectStatistic {

    private Cmd cmd;
    private Map<String  , File> LogsMap;
    private Container container;
    private Record nation;


    public Cmd GetCmd()
    {
        return cmd;
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
        cmd = new Cmd();
        nation = new Record();
        nation.SetProvinceName("全国");
        container = new Container();
        LogsMap = new HashMap<>();
    }

    //处理文件中的一行
    public boolean ManageLine(String line)
    {
        String[] data = line.split(" ");
        String First = data[0].substring(0 , 2);
        if (First.equals(Lib.Skip))
        {
            return false;
        }
        Record record = container.GetRecord(data[0]);
        if (record == null)
        {
            record = new Record();
            record.SetProvinceName(data[0]);
            container.AddRecord(record);
        }
        int num = Common.ParserStringToInt(data[data.length - 1]);
        int[] dataLength = {3 , 4 , 5};
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
                br = Common.NewBufferReader(entry.getKey());
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
        String OutputPath = cmd.GetOutputPath();
        BufferedWriter bw = null;
        try
        {
            bw = Common.NBWriter(OutputPath);
            if (!cmd.HasProvince() || cmd.GetProvinces().contains(Lib.National))
            {
                nation.OutRecord(bw , cmd.GetTypes());
            }
            container.OutContainer(bw , cmd.GetTypes() , cmd.GetProvinces());
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
        Cmd cmd =  inf.GetCmd();
        if (!cmd.ManageArgs(args , inf))
        {
            System.out.println("处理命令行参数失败");
            return;
        }
        inf.SetLogsMap(Common.GetFiles(cmd.GetLogPath() ,cmd.GetDate() ,
                Lib.LogFileRegex));
        inf.ReadLogFile();
        inf.OutputFile();
    }
}

class Container
{

    Map<String , Record> RecordMap;
    public Container()
    {
        RecordMap = new HashMap<>();
    }

    public Map<String , Record> GetRecordMap()
    {
        return RecordMap;
    }

    public Record GetRecord(String Province)
    {
        return RecordMap.get(Province);
    }

    public void AddRecord(Record record)
    {
        RecordMap.put(record.GetProvinceName() , record);
    }

    public boolean CompareTo(Container c)
    {
        Map<String , Record> rdMap = c.GetRecordMap();
        if (RecordMap.size() != rdMap.size())
        {
            return false;
        }
        for (Map.Entry<String , Record> entry : RecordMap.entrySet())
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
        CComparator MapComparator = new CComparator();
        RecordMap = Common.SortMap(RecordMap , MapComparator);
    }

    //将容器内容输入文件
    public void OutContainer(BufferedWriter bw  , Vector<String> types  , Vector<String> Provinces) throws IOException
    {
        SortByProvince();
        for (Map.Entry<String , Record> entry : RecordMap.entrySet())
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

    public String GetProvinceName()
    {
        return ProvinceName;
    }

    public void SetProvinceName(String ProvinceName)
    {
        this.ProvinceName = ProvinceName;
    }

    public int GetIpNum()
    {
        return IpNum;
    }

    public void SetIpNum(int IpNum)
    {
        this.IpNum = IpNum;
    }

    public void UpIpNum(int num)
    {
        this.IpNum += num;
    }

    public void DownIpNum(int num)
    {
        this.IpNum -= num;
    }

    public int GetSpNum()
    {
        return SpNum;
    }

    public void SetSpNum(int SpNum)
    {
        this.SpNum = SpNum;
    }

    public void UpSpNum(int num)
    {
        this.SpNum += num;
    }

    public void DownSpNum(int num)
    {
        this.SpNum -= num;
    }

    public int GetCureNum()
    {
        return CureNum;
    }

    public void SetCureNum(int CureNum)
    {
        this.CureNum = CureNum;
    }

    public void UpCureNum(int num)
    {
        this.CureNum += num;
    }

    public int GetDeadNum()
    {
        return DeadNum;
    }

    public void SetDeadNum(int DeadNum)
    {
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
    public void ShowRecordMessage()
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

class Cmd
{
    private String LogPath;
    private String OutputPath;
    private String date;
    private Vector<String> types;
    private Vector<String> Provinces;
    private boolean ShowArgs;

    public Cmd()
    {
        this.types = new Vector<String>();
        this.Provinces = new Vector<String>();
        this.LogPath = null;
        this.OutputPath = null;
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.date = sdf.format(today);
        this.ShowArgs = false;
    }

    public String GetLogPath()
    {
        return LogPath;
    }

    public void SetLogPath(String LogPath)
    {
        this.LogPath = LogPath;
    }

    public String GetOutputPath()
    {
        return OutputPath;
    }

    public void SetOutputPath(String OutputPath)
    {
        this.OutputPath = OutputPath;
    }

    public String GetDate()
    {
        return date;
    }

    public boolean SetDate(String date) throws ParseException
    {
        if (Common.StringToDate(date) == null)
        {
            // invalid date
            return false;
        }
        this.date = date;
        return true;
    }

    public Vector<String> GetTypes()
    {
        return types;
    }

    public int typeNumber()
    {
        return this.types.size();
    }

    public void AddType(String type)
    {
        this.types.add(type);
    }


    public Vector<String> GetProvinces()
    {
        return Provinces;
    }

    public void AddProvince(String Province)
    {
        this.Provinces.add(Province);
    }

    public boolean HasProvince()
    {
        return Provinces.size() > 0;
    }

    public int ProvinceNumber()
    {
        return this.Provinces.size();
    }

    public boolean ManageArgs(String[] args , InfectStatistic inf) throws ParseException
    {
        if (args == null)
        {
            return false;
        }
        int i = 0;
        while (i < args.length)
        {
            if ("list".equals(args[i]))
            {
                i++;
                break;
            }
            i++;
        }
        if (i >= args.length)
        {
            ShowRule();
            return false;
        }
        for (; i < args.length; ++i)
        {
            if ("-log".equals(args[i]))
            {
                File file = new File(args[++i]);
                if (file.isDirectory())
                {
                    this.SetLogPath(args[i]);
                }
                else
                {
                    System.out.println("日志文件的路径有误");
                    return false;
                }
            }
            else if ("-out".equals(args[i]))
            {
                this.SetOutputPath(args[++i]);
            }
            else if ("-date".equals(args[i]))
            {
                if (!this.SetDate(args[++i]))
                {
                    return false;
                };
            }
            else if ("-type".equals(args[i]))
            {
                while (i < args.length)
                {
                    if ((i + 1) >= args.length || args[i + 1].charAt(0) == '-')
                    {
                        break;
                    }
                    else
                    {
                        this.AddType(args[++i]);
                    }
                }
            }
            else if ("-Province".equals(args[i]))
            {
                while (i < args.length)
                {
                    if ((i + 1) >= args.length || args[i + 1].charAt(0) == '-')
                    {
                        break;
                    }
                    else
                    {
                        this.AddProvince(args[++i]);
                        if (!"全国".equals(args[i]))
                        {
                            Record record = new Record();
                            record.SetProvinceName(args[i]);
                            inf.GetContainer().AddRecord(record);
                        }
                    }
                }
            }
            else if ("-show".equals(args[i]))
            {
                this.ShowArgs = true;
            }
            else
            {
                System.out.println("未知参数"+ args[i]);
            }
        }
        if (this.LogPath == null || this.OutputPath == null)
        {
            ShowRule();
            return false;
        }
        if (this.ShowArgs)
        {
            ShowArgs();
        }
        return true;
    }

    public void ShowRule()
    {
        System.out.println("请输入形如：java InfectStatistic list -log -out -date -type -Province -show");
    }

    public void ShowArgs()
    {
        System.out.println("日志文件位置:" + this.GetLogPath());
        System.out.println("文件输出位置:" + this.GetOutputPath());
        System.out.println("最新的统计截至日期:" + this.GetDate());
        if (this.GetTypes() != null && this.typeNumber() != 0)
        {
            System.out.println("选择统计的人员类型:");
            for (String tem : this.GetTypes())
            {
                System.out.print(tem + " ");
            }
        }
        if (this.GetProvinces() != null && this.ProvinceNumber() != 0)
        {
            System.out.println("请选择统计的省份:");
            for (String tem : this.GetProvinces())
            {
                System.out.print(tem + " ");
            }
        }
    }
}