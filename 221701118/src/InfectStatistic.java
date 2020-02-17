import java.io.*;

/**
 * InfectStatistic TODO
 *
 * @author 张嘉伟
 * @version 2.0
 * @since 2020-02-18 01:37
 */
class InfectStatistic
{
    static Province province[] = new Province[100];
    static int number = 0;

    public static void main(String[] args) {
        //本机测试
        readFile("C:/Users/张嘉伟的电脑/Documents/GitHub/InfectStatistic-main/example/log/2020-01-22.log.txt");
        writeALLProvince();
    }

    public static void recordProcess(String string)
    {
        //处理每行记录，获取首个空格前的字符串也就是省名，并且获取字符串里的数字
        String provinceName=string.substring(0, string.indexOf(" "));
        int numberOfPeople = getNumber(string);

        if (string.contains("新增"))
        {
            if(string.contains("感染患者"))
            {
                province[exit(provinceName)].add("感染患者", numberOfPeople);
            }
            else if(string.contains("疑似患者"))
            {
                province[exit(provinceName)].add("疑似患者", numberOfPeople);
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
                province[exit(provinceName)].minus("感染患者", numberOfPeople);
                province[exit(aimProvince)].add("感染患者", numberOfPeople);
            }
            else if(string.contains("疑似患者"))
            {
                province[exit(provinceName)].minus("疑似患者", numberOfPeople);
                province[exit(aimProvince)].add("疑似患者", numberOfPeople);
            }
        }
        else if(string.contains("死亡"))
        {
            province[exit(provinceName)].add("死亡", numberOfPeople);
            province[exit(provinceName)].minus("感染患者", numberOfPeople);
        }
        else if(string.contains("治愈"))
        {
            province[exit(provinceName)].add("治愈", numberOfPeople);
            province[exit(provinceName)].minus("感染患者", numberOfPeople);
        }
        else if(string.contains("确诊"))
        {
            province[exit(provinceName)].add("感染患者", numberOfPeople);
            province[exit(provinceName)].minus("疑似患者", numberOfPeople);
        }
        else if(string.contains("排除"))
        {
            province[exit(provinceName)].minus("疑似患者", numberOfPeople);
        }
    }

    /* 检查该省是否首次出现,返回其位置，没有则创建 */
    public static int exit(String provinceName) {
        for (int i = 0; i < number; i++) {
            if (provinceName.equals(province[i].getName())) {
                return i;
            }
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

    public static void readFile(String fileName)
    {
        File file = new File(fileName);
        BufferedReader reader = null;
        try 
        {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
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