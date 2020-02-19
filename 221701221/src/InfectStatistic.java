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
}
class Container {

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
}