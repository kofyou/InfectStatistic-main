import java.io.*;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * InfectStatistic TODO
 *
 * @author 张嘉伟
 * @version 5.0
 * @since 2020-02-20 23:40
 */
class InfectStatistic
{
    static Province province[] = new Province[100];
    static int number = 0;
    static String log;
    static String out;
    static String date = null;
    static String type[] = new String[5];
    static int typeNumber = 0;
    static String outputProvince[] = new String[100];
    static int outputNumber = 0;
    static TreeSet<String> fileDate = new TreeSet<String>();
    static Province nation = new Province("全国",0,0,0,0);

    public static void main(String[] args)
    {
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
                    type[typeNumber] = "ip";
                    typeNumber++;
                    break;
                case "sp":
                    type[typeNumber] = "sp";
                    typeNumber++;
                    break;
                case "cure":
                    type[typeNumber] = "cure";
                    typeNumber++;
                    break;
                case "dead":
                    type[typeNumber] = "dead";
                    typeNumber++;
                    break;
                case "-province":
                    while(i < args.length - 1)
                    {
                        if(!args[i + 1].contains("-"))
                        {
                            outputProvince[outputNumber] = args[i + 1];
                            outputNumber++;
                        }
                        else
                        {
                            break;
                        }
                        i++;
                    }
                default:
                    break;
            }
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

    /**读取资源文件，根据命令参数处理后将结果写入到目标文件 */
    public static void writeALLProvince()
    {
        clearFile(out);
        for(String string : fileDate)
        {
            if(dateCompare(string))
                readFile(log + string + ".log.txt");
        }
        addNationToProvinceList();
        //如果没有指定-province，直接输出nation，否则判断参数里是否含"全国"
        if(outputNumber == 0)
        {
            writeFile(out,
                    getOutputTypeString(nation.getName(), nation.getInfectNumber(),
                    nation.getSuspectNumber(),nation.getCure(), nation.getDead())
                );
            //将已存的省份名添加进outputProovince[]
            ProvinceListIntoOutputProvince();
        }
        else
        {
            for(int i = 0; i < outputNumber; i++)
            {
                if(outputProvince[i].equals("全国"))
                {
                    writeFile(out,
                        getOutputTypeString(nation.getName(),nation.getInfectNumber(),
                        nation.getSuspectNumber(),nation.getCure(), nation.getDead())
                    );
                    break;
                }
            }
        }
        provinceSort();
        for(int i = 0; i < outputNumber; i++)
        {
            int j = 0;
            for(j = 0; j < number; j++)
            {
                if(outputProvince[i].equals(province[j].getName()))
                {
                    writeFile(out,
                        getOutputTypeString(province[j].getName(), province[j].getInfectNumber(),
                        province[j].getSuspectNumber(),province[j].getCure(), province[j].getDead())
                    );
                    break;
                }
            }
            if(j == number && !outputProvince[i].equals("全国"))
            {
                Province newProvince = new Province(outputProvince[i]);
                writeFile(out,
                    getOutputTypeString(newProvince.getName(), newProvince.getInfectNumber(),
                    newProvince.getSuspectNumber(),newProvince.getCure(), newProvince.getDead())
                );
            }
        }
    }

    public static void addNationToProvinceList()
    {
        for(int i = 0; i < number; i++)
        {
            nation.add("感染患者", province[i].getInfectNumber());
            nation.add("疑似患者", province[i].getSuspectNumber());
            nation.add("治愈", province[i].getCure());
            nation.add("死亡", province[i].getDead());
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

    public static void ProvinceListIntoOutputProvince()
    {
        for(int i = 0; i < number; i++)
        {
            outputProvince[outputNumber] = province[i].getName();
            outputNumber++;
        }
    }

    /**将要输出的省份按拼音排序 */
    public static void provinceSort()
    {
        //数据交换防止Array.sort因存在空指针的报错
        String string[] = new String[outputNumber];
        for(int i = 0; i < outputNumber; i++)
        {
            string[i] = outputProvince[i];
        }
        Comparator<Object> cmp = Collator.getInstance(java.util.Locale.CHINA);
        Arrays.sort(string, cmp);
        for(int i = 0; i < outputNumber; i++)
        {
            outputProvince[i] = string[i];
        }
    }

    public static String getOutputTypeString(String name, int infect, int suspect, int cured, int deaded)
    {
        String string = name;
        if(typeNumber == 0)
        {
            string = string + " 感染患者" + infect + "人";
            string = string + " 疑似患者" + suspect+ "人";
            string = string + " 治愈" + cured + "人";
            string = string + " 死亡" + deaded + "人";
        }
        for(int i = 0; i < typeNumber; i++)
        {
            if(type[i].equals("ip"))
                string = string + " 感染患者" + infect + "人";
            else if(type[i].equals("sp"))
                string = string + " 疑似患者" + suspect + "人";
            else if(type[i].equals("cure"))
                string = string + " 治愈" + cured + "人";
            else if(type[i].equals("dead"))
                string = string + " 死亡" + deaded + "人";
        }
        string = string + "\n";
        return string;
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
            while((tempString = reader.readLine()) != null)
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

    public static void clearFile(String fileName)
    {
        File file =new File(fileName);
        try
        {
            if(!file.exists())
            {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
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
