import java.io.*;
import java.util.TreeSet;

/**
 * InfectStatistic TODO
 *
 * @author 张嘉伟
 * @version 3.0
 * @since 2020-02-19 15:51
 */
class InfectStatistic
{
    static Province province[] = new Province[100];
    static int number = 0;
    static String log;
    static String out;
    static String date = null;
    static boolean ip = false;
    static boolean sp = false;
    static boolean cure = false;
    static boolean dead = false;
    static String outputProvince[] = new String[100];
    static int outputNumber = 0;
    static TreeSet<String> fileDate = new TreeSet<String>();

    public static void main(String[] args) {
        for(int i = 0; i < args.length; i++)
        {
            switch(args[i])
            {
                case "-log":
                    log = args[i + 1];
                    break;
                case "-out":
                    out = args[i + 1];
                    break;
                case "-date":
                    date = args[i + 1];
                    break;
                case "ip":
                    ip = true;
                    break;
                case "sp":
                    sp = true;
                    break;
                case "cure":
                    cure = true;
                    break;
                case "dead":
                    dead = true;
                    break;
                case "-province":
                    while(!args[i + 1].contains("-") && i < args.length - 1)
                    {
                        outputProvince[outputNumber] = args[i + 1];
                        outputNumber++;
                        i++;
                    }
                default:
                    break;
            }
        }
        if(!(ip || sp || cure || dead))
        {
            ip = sp = cure = dead =true;
        }
        File list = new File(log);
        getFileName(list);
        writeALLProvince();
    }

    public static void recordProcess(String string)
    {
        //处理每行记录，获取首个空格前的字符串也就是省名，并且获取字符串里的数字
        String provinceName=string.substring(0, string.indexOf(" "));
        int numberOfPeople = getNumber(string);
        int itemOfProvinceList = exit(provinceName);

        if (string.contains("新增"))
        {
            if(string.contains("感染患者"))
            {
                province[itemOfProvinceList].add("感染患者", numberOfPeople);
            }
            else if(string.contains("疑似患者"))
            {
                province[itemOfProvinceList].add("疑似患者", numberOfPeople);
            }
        }
        else if(string.contains("流入"))
        {
            //获取字符串里流入的目的地省名
            String aimProvince = string.substring(string.indexOf("流入"), string.length());
            aimProvince = aimProvince.replaceAll("流入", "");
            aimProvince = aimProvince.replaceAll(" ", "");
            aimProvince = aimProvince.replaceAll("人", "");
            aimProvince = aimProvince.replaceAll("\\d+", "");
            if(string.contains("感染患者"))
            {
                province[itemOfProvinceList].minus("感染患者", numberOfPeople);
                province[exit(aimProvince)].add("感染患者", numberOfPeople);
            }
            else if(string.contains("疑似患者"))
            {
                province[itemOfProvinceList].minus("疑似患者", numberOfPeople);
                province[exit(aimProvince)].add("疑似患者", numberOfPeople);
            }
        }
        else if(string.contains("死亡"))
        {
            province[itemOfProvinceList].add("死亡", numberOfPeople);
            province[itemOfProvinceList].minus("感染患者", numberOfPeople);
        }
        else if(string.contains("治愈"))
        {
            province[itemOfProvinceList].add("治愈", numberOfPeople);
            province[itemOfProvinceList].minus("感染患者", numberOfPeople);
        }
        else if(string.contains("确诊"))
        {
            province[itemOfProvinceList].add("感染患者", numberOfPeople);
            province[itemOfProvinceList].minus("疑似患者", numberOfPeople);
        }
        else if(string.contains("排除"))
        {
            province[itemOfProvinceList].minus("疑似患者", numberOfPeople);
        }
    }

    /* 检查该省是否首次出现,返回其位置，没有则创建 */
    public static int exit(String provinceName)
    {
        for (int i = 0; i < number; i++)
        {
            if (provinceName.equals(province[i].getName()))
                return i;
        }
        Province newProvince = new Province(provinceName);
        province[number] = newProvince;
        number++;
        return number - 1;
    }

    public static int getNumber(String string)
    {
        // 这里的 \\D 等同于 [^0-9]
        // 把非0 - 9 的值替换为空字符串
        String number = string.replaceAll("\\D", "");
        return Integer.parseInt(number);
    }

    /**该函数供本机测试使用，后期会做修改 */
    public static void writeALLProvince()
    {
        for(String string : fileDate)
        {
            if(dateCompare(string))
                readFile(log + string + ".log.txt");
        }
        int infectNumber = 0;
        int suspectNumber = 0;
        int cure = 0;
        int dead = 0;
        for(int i = 0; i < number; i++)
        {
            infectNumber += province[i].getInfectNumber();
            suspectNumber += province[i].getSuspectNumber();
            cure += province[i].getCure();
            dead += province[i].getDead();
        }
        writeFile(("C:/Users/张嘉伟的电脑/Desktop/demo.txt"),
            "全国 感染患者" + infectNumber
            +"人 疑似患者" + suspectNumber
            + "人 治愈" + cure
            + " 死亡"+ dead
            + "人\n");
        for(int i = 0; i < number; i++)
        {
            writeFile(("C:/Users/张嘉伟的电脑/Desktop/demo.txt"), 
                province[i].getName()
                + " 感染患者" + province[i].getInfectNumber()
                + "人 疑似患者" + province[i].getSuspectNumber()
                + "人 治愈" + province[i].getCure()
                + " 死亡"+ province[i].getDead()
                + "人\n");
        }
    }

    public static boolean dateCompare(String string)
    {
        if(date == null)
        {
            return true;
        }
        String inputDate = date;
        int inputYear = Integer.valueOf(string.substring(0, string.indexOf("-")));
        int fileYear = Integer.valueOf(inputDate.substring(0, inputDate.indexOf("-")));
        string = string.substring(string.indexOf("-") + 1, string.length());
        inputDate = inputDate.substring(inputDate.indexOf("-") + 1, inputDate.length());
        int inputMonth = Integer.valueOf(string.substring(0, string.indexOf("-")));
        int fileMonth = Integer.valueOf(inputDate.substring(0, inputDate.indexOf("-")));
        string = string.substring(string.indexOf("-") + 1, string.length());
        inputDate = inputDate.substring(inputDate.indexOf("-") + 1, inputDate.length());
        int inputDay = Integer.valueOf(string);
        int fileDay = Integer.valueOf(inputDate);
        if(inputYear > fileYear)
            return true;
        else if(inputYear < fileYear)
            return false;
        else
        {
            if(inputMonth > fileMonth)
                return true;
            else if(inputMonth < fileMonth)
                return false;
            else
            {
                if(inputDay <= fileDay)
                    return true;
                return false;
            }
        }
    }

    public static void getFileName(File list)
    {
        if(list != null)
        {
			File[] file = list.listFiles();
            if(file != null)
            {
                for(int i=0;i<file.length;i++)
                {
					getFileName(file[i]);
				}
            }
            else
            {
                String time = list.toString();
                time = time.substring(time.indexOf("log\\") + 4, time.indexOf(".log.txt"));
                fileDate.add(time);
			}
		}
    }

    public static void readFile(String fileName)
    {
        try 
        {
            FileInputStream file = new FileInputStream(fileName);   
            InputStreamReader isr = new InputStreamReader(file, "UTF-8");   
            BufferedReader reader = new BufferedReader(isr);  
            String tempString = null;
            while ((tempString = reader.readLine()) != null)
            {
                //读取文件里不以"//"开头的每行记录，传入recordProcess方法
                if(tempString.indexOf("//") != 0)
                {
                    recordProcess(tempString);
                }
            }
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }  

    public static void writeFile(String fileName, String content)
    {
        try
        {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

class Province
{
    private String name;
    private int infectNumber;
    private int suspectNumber;
    private int cure;
    private int dead;

    public Province(String name)
    {
        this.name = name;
        this.infectNumber = 0;
        this.suspectNumber = 0;
        this.cure = 0;
        this.dead = 0;
    }
    
    public Province(String name, int infectNumber, int suspectNumber, int cure, int dead)
    {
        this.name = name;
        this.infectNumber = infectNumber;
        this.suspectNumber = suspectNumber;
        this.cure = cure;
        this.dead = dead;
    }

    public void add(String object, int number)
    {
        switch(object)
        {
            case "感染患者":
                this.infectNumber += number;
                break;
            case "疑似患者":
                this.suspectNumber += number;
                break;
            case "治愈":
                this.cure += number;
                break;
            case "死亡":
                this.dead += number;
            default:
                break;
        }
    }

    public void minus(String object, int number)
    {
        switch(object)
        {
            case "感染患者":
                this.infectNumber -= number;
                break;
            case "疑似患者":
                this.suspectNumber -= number;
                break;
            case "治愈":
                this.cure -= number;
                break;
            case "死亡":
                this.dead -= number;
            default:
                break;
        }
    }

    public String getName()
    {
        return this.name;
    }

    public int getInfectNumber()
    {
        return this.infectNumber;
    }

    public int getSuspectNumber()
    {
        return this.suspectNumber;
    }

    public int getCure()
    {
        return this.cure;
    }

    public int getDead()
    {
        return this.dead;
    }
}