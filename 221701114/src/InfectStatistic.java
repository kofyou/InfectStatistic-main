/*
* FileName:InfectStatistic.java
* Author:zyl
* Version:1.0
* Date:2020-2-16
*/


import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

class InfectStatistic 
{
	public static String pathOfLog=null;  //用于记录日志目录的位置
	public static String pathOfOutput=null;  //用于记录输出文件路径和文件名
	public static String dateString=null;  //用于记录日期
	public static String[] paramentersOfType = new String[4];  //用于记录参数type的参数值
	public static String[] paramentersOfProvince = new String[34];  //用于记录参数province的参数值
	
	//记录list命令各参数是否出现，分别对应参数log、out、date、type、province,出现后相应元素值置为1
    public static int commandArgsFlag[] = {0,0,0,0,0};
    
    public static String[] allLogFilesName;  //记录所有日志文件的文件名
    public static String maxDate=null; //日志目录下的最大日期
    
    public static ArrayList<Province> allProvince = new ArrayList<Province>();
    
    /*
    * Description:分析命令行穿给主函数的命令
    * Input:命令行传个主函数的字符串数组args
    * Return:无
    * Others:无
    */ 
	public static void analysisOfCommand(String[] args)
	{
		for (int i = 0;i < args.length;i++)
        {
        	if (args[i].equals("-log"))
        	{
        		commandArgsFlag[0]++;
        		pathOfLog = args[i + 1];
        	}
        	else if (args[i].equals("-out"))
        	{
        		commandArgsFlag[1]++;
        		pathOfOutput = args[i + 1];
        	}
        	else if (args[i].equals("-date"))
            {
        		commandArgsFlag[2]++;
                dateString = args[i + 1];
            }
        	else if (args[i].equals("-type"))
            {
        		commandArgsFlag[3]++;
                int cnt = 0;
                for (int j = i + 1;j < args.length && args[j].startsWith("-") == false;j++)
                {
                	paramentersOfType[cnt++] = args[j];
                }
                
            }
        	else if (args[i].equals("-province"))
            {
                commandArgsFlag[4]++;
                int count = 0;
                for (int j = i + 1;j < args.length && args[j].startsWith("-") == false;j++)
                {
                	paramentersOfProvince[count++] = args[j];
                }
            }
        }
	}
	
	/*
	* Description:根据命令中指定的日志目录，获取该日志目录下所有文件的名字
	* Input:无
	* Return:无
	* Others:无
	*/ 
	public static void getAllLogFilesName()
	{
		File file = new File(pathOfLog);
        File[] allLogFiles = file.listFiles();   
        allLogFilesName = new String[allLogFiles.length];
        for(int i = 0;i < allLogFiles.length;i++)
        {
        	allLogFilesName[i] = allLogFiles[i].getName();
        }
	}
	
	/*
	* Description:获取所提供日志最新的一天
	* Input:无
	* Return:最新一天日期字符串
	* Others:无
	*/ 
	public static String getMaxDate()
	{
		String theMaxDate =allLogFilesName[0];
		for (int i = 0;i < allLogFilesName.length;i++)
		{
			if (theMaxDate.compareTo(allLogFilesName[i]) < 0)
			{
				theMaxDate = allLogFilesName[i];
			}
		}
		
		int index = theMaxDate.indexOf('.');
		theMaxDate = theMaxDate.substring(0, index);
		
		return theMaxDate;
	}
	
	/*
	* Description:统计各个省份的感染人数，疑似人数，治愈人数和死亡人数
	* Input:从日志文件读取出的一行信息，该行信息保存为字符串
	* Return:无
	* Others:无
	*/ 
	public static void getProvincialInformation(String oneLineOfFile)
	{
		String[] splitString = oneLineOfFile.split(" ");
		int countOfSplitString = splitString.length;
		int countOfPeople = getStringNumber(splitString[splitString.length - 1]); 
		boolean isExist = false;  //用于标志动态数组中是否有记录某省
		
		//检验动态数组中是否有该省份，
        for (int i = 0;i < allProvince.size();i++)
        {
        	if (allProvince.get(i).provinceName.equals(splitString[0]) == true)
        	{
        		isExist = true;
        	    break;
        	}
        }
        if (isExist == false)//无该省则把该省份加到动态数组中
        {
        	allProvince.add(new Province(splitString[0]));
        }
	} 
	
	/*
	* Description:提取传入的字符串当中的数字并转换成整数返回
	* Input:一个包含了数字字符的字符串
	* Return:一个整数
	* Others:无
	*/ 
	public static int getStringNumber(String str)
	{
		str = str.trim();
		String numString = "";
		
		if(str != null && !"".equals(str))
		{
			for(int i = 0;i < str.length();i++)
			{
				if(str.charAt(i) >= 48 && str.charAt(i) <= 57)
				{
					numString += str.charAt(i);
				}
			}
		}
		
		return Integer.parseInt(numString);
	} 
	
    public static void main(String[] args) throws IOException 
    {
    	analysisOfCommand(args);
    	getAllLogFilesName();
    	maxDate=getMaxDate();
    	
    	//检验命令行中是否有输入日期
    	if (dateString == null)  
    	{
    		dateString = maxDate;
    	}
    	
    	//检验日期是否超出范围
    	if (dateString.compareTo(maxDate+".log.txt") > 0)  
    	{
    		System.out.println("日期超出范围！");
    	}
    	else 
    	{
    		//System.out.println("日期没有超出范围");
    		for (int i = 0;i < allLogFilesName.length;i++)
    		{
    			//读取满足日期小于或等于指定日期的文件的每行
    			if (allLogFilesName[i].compareTo(dateString+".log.txt") <= 0)
    			{
    		    	String oneLineOfFile = null;
    		    	try 
    		    	{
    		    		InputStreamReader isr = new InputStreamReader(
    		    			new FileInputStream(pathOfLog+allLogFilesName[i]), "UTF-8");
    	    			BufferedReader br = new BufferedReader(isr);
    		    		while ((oneLineOfFile = br.readLine()) != null 
    		    			&& oneLineOfFile.length() != 0  //不读取空行
    		    			&& oneLineOfFile.startsWith("//") == false)  //不读取注释行
    		    		{
    		    			//System.out.println(oneLineOfFile);
    		    			getProvincialInformation(oneLineOfFile);
    		    			
    		    		}
    		    	} 
    		    	catch (FileNotFoundException e) {
    		    		e.printStackTrace();
    		    	}
    			}
    		}
    		
    		
    	}
    	
    	


    }
}

class Province
{
	String provinceName;  //省份名字
	int ip;  //感染患者数目
    int sp;  //疑似患者数目
    int cure;  //治愈数目
    int dead;  //死亡数目
    
    Province(String provinceName)
    {
    	this.provinceName=provinceName;
    	ip=0;
    	sp=0;
    	cure=0;
    	dead=0;
    }
	
}