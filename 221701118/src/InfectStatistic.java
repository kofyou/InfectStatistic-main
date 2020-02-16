import java.io.*;

/**
 * InfectStatistic TODO
 *
 * @author 张嘉伟
 * @version 1.0
 * @since 2020-02-16 23:53
 */
class InfectStatistic
{
    public static void main(String[] args)
    {
        
    }

    public static void recordProcess(String string)
    {
        //处理每行记录
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
