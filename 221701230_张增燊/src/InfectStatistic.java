import java.io.*;
import java.util.*;
import java.lang.String;


public class InfectStatistic {
	
	
	public static void list(String log,String out,String date,
			String type,String province)
	{
		//读取文件
		GetFileList(log);
	}
	//读文件的函数
	public static void GetFileList(String log)
	{
		File file = new File(log);
		File[] fileList = file.listFiles();
		//开始读文件
		for (int i = 0; i < fileList.length; i++)
		{
			if (fileList[i].isFile())
			{
				//是文件就按行读取
				String fileName = fileList[i].getName();
				//读文件
				readFileByLines(log+"\\"+fileName,i);         
			} 
			if (fileList[i].isDirectory())
			{
				//是目录就递归读取
				String fileName = fileList[i].getName();
				GetFileList(log+"\\"+fileName);
            }
	    }
	}
	//按行读取文件
	public static void readFileByLines(String fileName,int order) 
	{  
        File file = new File(fileName);  
        BufferedReader reader = null;  
        try {    
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;  
            //读行  
            while ((tempString = reader.readLine()) != null)
            {  
                System.out.println(tempString);
            }
            reader.close();  
        } 
        catch (IOException e)
        {  
            e.printStackTrace();  
        }
        finally
        {  
            if (reader != null)
            {  
                try {  
                    reader.close();  
                } 
                catch (IOException e1) {}  
            }  
        }  
    }  
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in); 
		
		System.out.print("输入log\n");
		String log=s.nextLine();
		System.out.print("输入out\n");
		String out=s.nextLine();
		System.out.print("输入date\n");
		String date=s.nextLine();
		System.out.print("输入type\n");
		String type=s.nextLine();
		System.out.print("输入province\n");
		String province=s.nextLine();
		System.out.print("\n");
		list(log,out,date,type,province);
		
	}

	
}
