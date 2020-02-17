import java.util.*;
import java.io.*;

public class InfectStatistic {
	
	public static int i;   //用于读取命令参数时计数使用
	public static int typeCount; 
	public static int provinceCount;   //统计多参数的个数用，如后面跟了几个省份
	public static String fileDirect;   //保存所有日志文件文件目录的路径
	public static String outputFilepath;   //保存输出文件的路径
	public static String dateTime;   //保存-date的参数
	public static String typePeople[];   //保存要输出的类型
	public static String province[];   //保存要输出的省份
	public static ArrayList<String> fileContent;    //保存从文件中读取的内容
	public static Map<String , String> statistic;    //保存统计的结果
	public static File fileArray[];          //保存所有日志文件的路径
	
	public static boolean judgeList (String str)
	/*  
	 * 该方法判定args数组里的参数是否是命令行参数 
	*/
	{
		str = str.substring(0,1);
		if(str.equals("-")) return true;
		else return false;
	}
	
	public static void judgeType (String str[])	
	/*  
	 * 该方法判定是什么类型的命令行参数 
	 */
	{
		if(str[i].equalsIgnoreCase("-log"))
		{
			readLog(str);
		}
		
		else if(str[i].equalsIgnoreCase("-out"))
		{
			readOutputPath(str);
		}
		
		else if(str[i].equalsIgnoreCase("-date"))
		{
			readDateTime(str);
		}
		
		else if(str[i].equalsIgnoreCase("-type"))
		{
			readType(str);
		}
		
		else if(str[i].equalsIgnoreCase("-type"))
		{
			readType(str);
		}
		
		else if(str[i].equalsIgnoreCase("-province"))
		{
			readProvince(str);
		}
	}
	
	public static void readDirect()
	/*  
	 * 该方法用于读取目录下的日志文件并保存 
	 */
	{

		File file = new File(fileDirect);
		fileArray = file.listFiles();			
		statistic = new HashMap<String,String>();
		for(int i=0; i < fileArray.length; i++)
		{
			fileContent = new ArrayList<String>();
			readFile(i);
			getStatistic();
		}



	}
	
	public static void readFile(int fileCount)
	/*  
	 * 该方法用于读取日志文件内容并保存 
	 */
	{
		try 
		{
			Scanner sc  = new Scanner(fileArray[fileCount],"UTF-8");
			while(sc.hasNext())
			{
				String str = sc.next();
				if (str.equals("//"))
					break;
				
				else fileContent.add(str);
			}
			
			
			for(int i=0;i <fileContent.size();i++)
			{
				System.out.println(fileContent.get(i));
			}
			
			File file =new File(outputFilepath);
			
            if(!file.exists())
            {
	        	file.createNewFile();
	        }
            
            FileWriter fileWritter = new FileWriter(file,true);
            for(int i=0;i <fileContent.size();i++)
			{
            	fileWritter.write(fileContent.get(i));
			}
            fileWritter.close();
			sc.close();
		} 
		
		catch (Exception e) 
		{
			System.out.println("读取文件出错，请检查日志目录是否合理");
		}
		
	}
	
	public static void getStatistic()
	/*
	 * 该方法用于计算统计一个日志文件的各项数据
	 */
	{
		for(int i = 0; i < fileContent.size() - 2; i++)
		{
			if(fileContent.get(i + 1).equals("新增") )  //判别新增
			{
				increamentState(i);
			}
			
			else if(fileContent.get(i + 2).equals("流入")) //判别为流入的情况
			{
				flowState(i);
			}
		}
	}

	public static void increamentState(int count)
	/*
	 * 该方法统计情况为新增的日志数据
	 */
	{
		String provin = fileContent.get(count);  //获取这条信息关联的省份
		String type = fileContent.get(count + 2); //感染患者或者疑似患者
		String str = fileContent.get(count + 3); ;
		str = str.substring(0 , str.length() - 1); //截取人数
		
		if(!statistic.containsKey(provin + type)) //检查哈希表中是否已经存在该省份的数据了
		{
			initStatistic(provin);
		}
		
		//新增情况举例:福建 新增 感染患者 23人
		
		int sum = Integer.parseInt(str) + Integer.parseInt(statistic.get(provin + type));
		
		statistic.put(provin + type , String.valueOf(sum));
	
	}
	
	public static void flowState(int count)
	/*
	 * 该方法统计情况为流入的日志数据
	 */
	{
		String provin2 = fileContent.get(count);  //获取有患者流出的省份
		String provin1 = fileContent.get(count + 3);  //获取有患者流入的省份
		String type = fileContent.get(count + 1); //感染患者或者疑似患者
		String str = fileContent.get(count + 4);
		str = str.substring(0 , str.length() - 1); //截取人数
		
		if(!statistic.containsKey(provin1 + type))  //检查哈希表中是否已经存在该省份的数据了
		{
			initStatistic(provin1);
		}
		
		if(!statistic.containsKey(provin2 + type)) 
		{
			initStatistic(provin2);
		}
		
		
		int sum1 = Integer.parseInt(str) + Integer.parseInt(statistic.get(provin1 + type)); //统计有患者流入的省份
		int sum2 = Integer.parseInt(statistic.get(provin2 + type)) - Integer.parseInt(str); //统计有患者流出的省份
		
		statistic.put(provin1 + type , String.valueOf(sum1));
		statistic.put(provin2 + type , String.valueOf(sum2));

		System.out.println(statistic.get(provin1 + type));	
		System.out.println(statistic.get(provin2 + type));	
	}
	
	public static void initStatistic(String provin)
	/*
	 * 该方法用于初始化统计数据的哈希表，如果日志中出现了该省的信息，则进行初始化
	 */
	{
		statistic.put(provin + "感染患者", "0");  //如果哈希表中并没有存放该省份，就初始化该省份
		statistic.put(provin + "疑似患者", "0");
		statistic.put(provin + "治愈", "0");
		statistic.put(provin + "死亡", "0");
	}
	
	public static void readOutputPath (String str[])
	/*  
	 * 该方法用于保存输出文件路径 
	 */
	{
		
		if(i != str.length - 1) 
		{
			i++; //读取-out地址
			outputFilepath = str[i]; //保存路径
		}
		
		System.out.print(outputFilepath);
	}
	
	public static void readLog (String str[])
	/*  
	 * 该方法用于保存日志文件目录路径 
	 */
	{
		if(i != str.length - 1) 
		{
			i++; //读取-log地址
			fileDirect = str[i]; //保存路径
		}
		
		System.out.print(fileDirect);
	}
	
	public static void readDateTime (String str[])
	/*  
	 * 该方法用于保存-date的参数值
	 */
	{
		if(i == str.length - 1 || str[i+1].substring(0,1).equals("-"))
		{
			dateTime = "latest";
		}
		
		else
		{
			i++;
			dateTime = str[i];
		}
		
		System.out.print(dateTime);
	}
	
	public static void readType (String str[])
	/*  
	 * 该方法用于保存-type的参数值
	 */
	{
		typePeople = new String[4];
		if(i == str.length - 1 || str[i+1].substring(0,1).equals("-"))
		{
			typePeople[0] = "all";
			typeCount++;
		}
		
		else
		{
			i++;			
			typeCount = 0;
			for(  ; i < str.length ; i++)
			{	
				if(str[i].equalsIgnoreCase("ip") 
					|| str[i].equalsIgnoreCase("sp") 
					|| str[i].equalsIgnoreCase("cure") 
					|| str[i].equalsIgnoreCase("dead") )
				typePeople[typeCount++] = str[i];
				
				if(i == str.length - 1 || str[i+1].substring(0,1).equals("-"))  break;
			}
		}
		
		for(int k=0;k<typeCount;k++)
		System.out.println(typePeople[k]);
	}
	
	public static void readProvince (String str[])
	/*  
	 * 该方法用于保存-province的参数值
	 */
	{
		province = new String[31];
		if(i == str.length - 1 || str[i+1].substring(0,1).equals("-"))
		{
			province[0] = "全国";
		    provinceCount++;
		}
		
		else
		{
			i++;			
			provinceCount = 0;
			for(  ; i < str.length ; i++)
			{	
				province[provinceCount++] = str[i];
				
				if(i == str.length - 1 || str[i+1].substring(0,1).equals("-"))  break;
			}
		}
		
		for(int k=0;k<provinceCount;k++)
		System.out.println(province[k]);
	}
	
	public static void main(String args[])
    {
		i = 0;
		//if(args[i].equalsIgnoreCase("list"))
    	for(i = 0;i < args.length; i++)
    	{
    		if(judgeList(args[i]))
    		{
    			judgeType(args);
    		}
    	}
		
		readDirect();
    	return;
    }

}