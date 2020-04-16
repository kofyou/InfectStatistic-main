/**
 * InfectStatistic
 * TODO
 *
 * @author shenmw
 * @version final
 * @since 2.18
 */

import java.util.List;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;


class InfectStatistic 
{
    public String date;    //日期

    public String logPath;    //日志传入路径

    public String outputPath;    //output文件传入路径

    public List<String> name;    //省份名称    
    
    public String[] provinceName;//所有省份名称
    
    public Province country;    //全国的情况   
    
    public String[] arg; //命令行参数

    public boolean isRead;    //读取所有日志文件

    public boolean isOutput;    //所有参数

    public String[] output;    //输出顺序

    public boolean isOutputAll;    //输出全国和所有省的情况

    public List<String> provinces;    //-province要输出的省份

    public HashMap<String,Province> map;//省份名称与其具体情况的映射

    public boolean isFinish;
        

    public InfectStatistic(String[] args)
    {        
    	arg = args;
        isRead = true;
        logPath = "G:\\example\\log\\";
        outputPath = "G:\\example\\result\\output3.txt";
        provinceName = new String[]{"安徽","北京","重庆","福建","甘肃","广东",
            "广西","贵州","海南","河北","河南","黑龙江","湖北","湖南",
            "江西","吉林","江苏","辽宁","内蒙古","宁夏","青海","山西","山东","陕西","上海",
            "四川","天津","西藏","新疆","云南","浙江"};
        name = new ArrayList<>();       
        map = new HashMap<String,Province>();
        country = new Province("全国");
        isOutput = true;
        isOutputAll = true;
        provinces = new ArrayList<>();
        isFinish = false;
        output = new String[4];     

        for (int i = 0; i < provinceName.length; i ++ )
        {
            name.add(provinceName[i]);
            map.put(name.get(i), null);
        }
        
        for (int i = 0; i < 4; i ++ )
        {
            output[i] = "";
        }
        this.init();
    }
    
    //初始化
    public void init()
    {
        for (int i = 0; i < arg.length; i ++ )
        {           
            switch (arg[i])
            {
                case "-date":
                    date = new String(arg[i+1]);
                    isRead = false; 
                    break;
                case "-log":
                    logPath = new String(arg[i+1]);
                    break;
                case "-out":
                    outputPath = new String(arg[i+1]);
                    break;
                case "-type":
                    isOutput = false;
                    dealType(i+1);
                    break;
                case "-province":
                    isOutputAll = false;
                    dealProvince(i+1);
                default:    
                    break;
            }           
        }
    }
    
    //-province参数
    public void dealProvince(int index) 
    {
        while (index < arg.length)
        {
            switch (arg[index])
            {
                case "-date":
                    return;
                case "-log":
                    return;
                case "-out":
                    return;
                case "-type":
                    return;
                default:
                    provinces.add(arg[index]);
                    map.put(arg[index], null);
            }   
            index ++ ;
        }               
    }
    
    //-type参数
    public void dealType(int index)
    {       
        for (int i = 0; index < arg.length && i < 4; i ++ )
        {
            switch (arg[index])
            {
                case "ip":
                    output[i] = arg[index];
                    break;
                case "sp":
                    output[i] = arg[index];
                    break;
                case "cure":
                    output[i] = arg[index];
                    break;
                case "dead":
                    output[i] = arg[index];
                    break;                
                default:
                    break;
            }
            index ++ ;
        }
    }
    
        
    //日志文件
    public void deal() throws IOException
    {               
        String logDate;     
        String[] sArray;
        File file = new File(logPath);
        File[] tempList = file.listFiles();
        
        if (!isRead)
        {
            logDate = new String(tempList[tempList.length-1].getName());                      
            sArray = logDate.split("\\.");                    
            logDate = new String(sArray[0]);
            if ((logDate.compareTo(date)) < 0)
            {
                isFinish = true;
                System.out.println("日期超出范围!");
                return;
            }
        }

        for (int i = 0; i < tempList.length; i ++ )                   
        {                
            logDate = new String(tempList[i].getName());                      
            sArray = logDate.split("\\.");                    
            logDate = new String(sArray[0]);
                                                        
            if (isRead || (logDate.compareTo(date)) <= 0)                      
            {                 
                BufferedReader br = null;               
                String line = null;         
                br = new BufferedReader(new InputStreamReader(new FileInputStream(tempList[i].toString()), "UTF-8"));  
                
                while ((line = br.readLine()) != null)
                {
                    String[] array = line.split(" ");
                    dealOneLine(array);
                }          
                br.close();
            }                               
        }
        allStatistic();
    }
        
    private void dealOneLine(String[] array) 
    {
        if (array[0].equals("//"))
        {
            return;
        }

        if (map.get(array[0]) == null)
        {
            map.put(array[0], new Province(array[0]));  
        }
                        
        switch (array[1])
        {
            case "新增":
                if (array[2].equals("疑似患者"))
                {
                    map.get(array[0]).addSp(array[3]);
                }
                else
                {
                    map.get(array[0]).addIp(array[3]);
                }
                break;
            case "感染患者":
                map.get(array[0]).removeIp(array[4]);
                if (map.get(array[3]) == null)
                    map.put(array[3], new Province(array[3]));
                map.get(array[3]).addIp(array[4]);
                break;
            case "疑似患者":
                if (array[2].equals("流入"))
                {
                    map.get(array[0]).removeSp(array[4]);
                    if (map.get(array[3]) == null)
                        map.put(array[3], new Province(array[3]));
                    map.get(array[3]).addSp(array[4]);
                }
                else
                {
                    map.get(array[0]).addIp(array[3]);
                    map.get(array[0]).removeSp(array[3]);
                }
                break;
            case "死亡":
                map.get(array[0]).dead(array[2]);
                break;
            case "治愈":
                map.get(array[0]).cure(array[2]);
                break;
            case "排除":
                map.get(array[0]).removeSp(array[3]);
                break;
            default:
                break;
        }   
    }
    
    //生成输出文件
    public void output() throws IOException
    {
        if (isFinish)
        {
            return;
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "UTF-8"));
        if (isOutputAll)
        {
            country.output(isOutput,output,bw);
            for (int i = 0; i < name.size(); i ++ )
            {   
                if (map.get(name.get(i)) != null)
                {
                    map.get(name.get(i)).output(isOutput, output, bw);
                }                
            }
        }
        else
        {
            if (provinces.contains("全国"))
            {
                country.output(isOutput,output,bw);
            }
            for (int i = 0; i < provinceName.length; i ++ )
            { 
                if (provinces.contains(provinceName[i]))
                {
                    if (map.get(name.get(i)) == null)
                    {
                        map.put(provinceName[i], new Province(provinceName[i]));
                    }
                    map.get(provinceName[i]).output(isOutput, output, bw);
                }                
            }
        }
        bw.write("// 该文档并非真实数据，仅供测试使用");
        bw.close();
    }
    
    //全国的情况
    public void allStatistic()
    {
        for (int i = 0; i < name.size(); i ++ )
        {
            if (map.get(name.get(i)) != null)
            {
                country.allAdd(map.get(name.get(i)));
            }            
        }
    }                    
    public static void main(String[] args) throws IOException 
    {
        if (args[0].equalsIgnoreCase("list"))
        {
            InfectStatistic l = new InfectStatistic(args);
            l.deal();   
            l.output();         
        }
        else
        {
            System.out.print("命令错误：" + args[0]);
        }           
    }
}

//省份类
class Province
{ 
    private String name;    //省份名称

    private int cure;    //治愈人数

    private int dead;    //死亡人数
    
    private int suspectedPatients;    //疑似人数

    private int infectionPatients;    //感染人数  
    
    public Province(String n)
    {
        name = n;
    }
    //感染人数增长
    public void addIp(String str)
    {
        str = str.substring(0, str.length()-1);
        infectionPatients += Integer.parseInt(str);
    }
    //感染人数减少
    public void removeIp(String str)
    {
        str = str.substring(0, str.length()-1);
        infectionPatients -= Integer.parseInt(str);
    }
    //疑似人数增长
    public void addSp(String str)
    {
        str = str.substring(0, str.length()-1);
        suspectedPatients += Integer.parseInt(str);
    }   
    //疑似人数减少
    public void removeSp(String str)
    {
        str = str.substring(0, str.length()-1);
        suspectedPatients -= Integer.parseInt(str);
    }
    //治愈人数增长
    public void cure(String str)
    {
        str = str.substring(0, str.length()-1);
        cure += Integer.parseInt(str);
        infectionPatients -= Integer.parseInt(str);
    }   
    //死亡人数增长
    public void dead(String str)
    {
        str = str.substring(0, str.length()-1);
        dead += Integer.parseInt(str);
        infectionPatients -= Integer.parseInt(str);
    }
    //省份情况
    public void output(boolean isOutput, String[] output, BufferedWriter bw) throws IOException
    {
        if (isOutput)
        {
            bw.write(name + " 感染患者 " + infectionPatients + "人 "
                + "疑似患者 " + suspectedPatients + "人 "
                + "治愈 " + cure + "人 "
                + "死亡 " + dead + "人");
            bw.newLine();
        }        
        else
        {
            bw.write(name);
            for (int i = 0; i < 4; i ++ )
            {
                switch (output[i])
                {
                    case "ip":                  
                        bw.write(" 感染患者 " + infectionPatients + "人");           
                        break;
                    case "sp":                  
                        bw.write(" 疑似患者 " + suspectedPatients + "人");               
                        break;
                    case "cure":                    
                        bw.write(" 治愈 " + cure + "人");                  
                        break;
                    case "dead":                    
                        bw.write(" 死亡 " + dead + "人");          
                        break;
                    default:                
                        break;
                }
            }
            bw.newLine();
        }       
    }       
    //全国的情况
    public void allAdd(Province p)
    {
        this.infectionPatients += p.infectionPatients;
        this.suspectedPatients += p.suspectedPatients;
        this.cure += p.cure;
        this.dead += p.dead;
    }
}