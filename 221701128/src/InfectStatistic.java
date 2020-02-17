import java.util.*;
import java.io.*;
import java.text.Collator;

public class InfectStatistic {
	
	public static int cmdCount;   //用于读取命令参数时计数使用，在main函数中的循环中第一次使用
	public static int typeCount; 
	public static int provinceCount;   //统计多参数的个数用，如后面跟了几个省份
	public static String fileDirect;   //保存所有日志文件文件目录的路径
	public static String outputFilepath;   //保存输出文件的路径
	public static String dateTime;   //保存-date的参数
	public static String typePeople[];   //保存要输出的类型
	public static String province[];   //保存要输出的省份
	public static ArrayList<String> fileContent;    //保存从文件中读取的内容
	public static Map<String , String> statistic;    //保存统计的结果
	public static Map<String, Object> sortMap;  //用于保存经过排序的统计结果
	public static File fileArray[];          //保存所有日志文件的路径
	
	public static boolean judgeList (String str)
	/*  
	 * 该方法判定args数组元素是否是命令行参数 
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
		if(str[cmdCount].equalsIgnoreCase("-log"))
		{
			readLog(str);
		}
		
		else if(str[cmdCount].equalsIgnoreCase("-out"))
		{
			readOutputPath(str);
		}
		
		else if(str[cmdCount].equalsIgnoreCase("-date"))
		{
			readDateTime(str);
		}
		
		else if(str[cmdCount].equalsIgnoreCase("-type"))
		{
			readType(str);
		}
		
		else if(str[cmdCount].equalsIgnoreCase("-type"))
		{
			readType(str);
		}
		
		else if(str[cmdCount].equalsIgnoreCase("-province"))
		{
			readProvince(str);
		}
	}
	
	public static Map<String, Object> sortHashkey()
	/*
	 * 将HashMap的key值按照中文拼音排序
	 */
	{
		Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);
		Map<String, Object> map = new TreeMap<String, Object>(CHINA_COMPARE);
		map.putAll(statistic);
		return map;
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
			if(fileArray[i].getName().equals(dateTime + ".log.txt")) break; //如果dateTime中规定了统计的日志日期，那就在统计完那个日志后结束统计
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
            
            FileWriter fileWritter = new FileWriter(file);
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
			
			else if(fileContent.get(i + 1).equals("死亡") || fileContent.get(i + 1).equals("治愈")) //判别为死亡或者治愈的情况
			{
				deadCureState(i);
			}
			
			else if(fileContent.get(i + 2).equals("确诊感染") || fileContent.get(i + 1).equals("排除")) //判别为确诊感染或排除的情况
			{
				confirmState(i);
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

	}
	
	public static void deadCureState(int count)
	/*
	 * 该方法统计情况为死亡或者治愈的日志数据
	 */
	{
		String provin = fileContent.get(count);  //获取数据相关的省份
		String type = fileContent.get(count + 1); //获取是治愈了还是死亡了
		String str = fileContent.get(count + 2);
		str = str.substring(0 , str.length() - 1); //截取人数
		
		if(!statistic.containsKey(provin + type))  //检查哈希表中是否已经存在该省份的数据了
		{
			initStatistic(provin);
		}
				
		int sum = Integer.parseInt(str) + Integer.parseInt(statistic.get(provin + type));			
		int infect = Integer.parseInt(statistic.get(provin + "感染患者")) - Integer.parseInt(str);  //无论是治愈了还是死亡了，感染患者现存数都要减去
		
		statistic.put(provin + type , String.valueOf(sum));
		statistic.put(provin + "感染患者" , String.valueOf(infect));

	}
	
	public static void confirmState(int count)
	/*
	 * 该方法统计情况为确诊感染的日志数据
	 */
	{
		String provin = fileContent.get(count);  //获取数据相关的省份
		String str = fileContent.get(count + 3);
		str = str.substring(0 , str.length() - 1); //截取人数
		
		if(!statistic.containsKey(provin + "疑似患者"))  //检查哈希表中是否已经存在该省份的数据了
		{
			initStatistic(provin);
		}
		
		int infect = 0;
		int unknownInfect = 0;
		
		if(fileContent.get(count + 2).equals("确诊感染")) //如果是确认感染，那感染患者数量要加上
		{
			infect = Integer.parseInt(statistic.get(provin + "感染患者")) + Integer.parseInt(str);
			statistic.put(provin + "感染患者" , String.valueOf(infect));
		}
		

		unknownInfect = Integer.parseInt(statistic.get(provin + "疑似患者")) - Integer.parseInt(str);  //无论是排除还是确诊，疑似患者现存数都要减去
		
		statistic.put(provin + "疑似患者" , String.valueOf(unknownInfect));

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
		
		if(cmdCount != str.length - 1) 
		{
			cmdCount++; //读取-out地址
			outputFilepath = str[cmdCount]; //保存路径
		}
		
		System.out.print(outputFilepath);
	}
	
	public static void readLog (String str[])
	/*  
	 * 该方法用于保存日志文件目录路径 
	 */
	{
		if(cmdCount != str.length - 1) 
		{
			cmdCount++; //读取-log地址
			fileDirect = str[cmdCount]; //保存路径
		}
		
		System.out.print(fileDirect);
	}
	
	public static void readDateTime (String str[])
	/*  
	 * 该方法用于保存-date的参数值
	 */
	{
		if(cmdCount == str.length - 1 || str[cmdCount+1].substring(0,1).equals("-"))
		{
			dateTime = "latest";
		}
		
		else
		{
			cmdCount++;
			dateTime = str[cmdCount];
		}
		
		System.out.print(dateTime);
	}
	
	public static void readType (String str[])
	/*  
	 * 该方法用于保存-type的参数值
	 */
	{
		typePeople = new String[4];
		if(cmdCount == str.length - 1 || str[cmdCount+1].substring(0,1).equals("-"))
		{
			typePeople[0] = "all";
			typeCount++;
		}
		
		else
		{
			cmdCount++;			
			typeCount = 0;
			for(  ; cmdCount < str.length ; cmdCount++)
			{	
				if(str[cmdCount].equalsIgnoreCase("ip") 
					|| str[cmdCount].equalsIgnoreCase("sp") 
					|| str[cmdCount].equalsIgnoreCase("cure") 
					|| str[cmdCount].equalsIgnoreCase("dead") )
				typePeople[typeCount++] = str[cmdCount];
				
				if(cmdCount == str.length - 1 || str[cmdCount+1].substring(0,1).equals("-"))  break;
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
		if(cmdCount == str.length - 1 || str[cmdCount+1].substring(0,1).equals("-"))
		{
			province[0] = "全国";
		    provinceCount++;
		}
		
		else
		{
			cmdCount++;			
			provinceCount = 0;
			for(  ; cmdCount < str.length ; cmdCount++)
			{	
				province[provinceCount++] = str[cmdCount];
				
				if(cmdCount == str.length - 1 || str[cmdCount+1].substring(0,1).equals("-"))  break;
			}
		}
		
		for(int k=0;k<provinceCount;k++)
		System.out.println(province[k]);
	}
	
	
	public static void main(String args[])
    {
		cmdCount = 0;
		if(args[cmdCount].equalsIgnoreCase("list"))
    	for(cmdCount = 0;cmdCount < args.length; cmdCount++)
    	{
    		if(judgeList(args[cmdCount]))
    		{
    			judgeType(args);
    		}
    	}
		
		readDirect();
		sortMap = sortHashkey();
		for (String key : sortMap.keySet()) {  //通过foreach方法来遍历
		       System.out.println("key= "+ key + " and value= " + sortMap.get(key));
		      }

    	return;
    }

}