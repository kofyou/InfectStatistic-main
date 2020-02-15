

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;
public class Homework2 
{
	
	public String[] allType = {"感染患者","疑似患者","治愈","死亡患者"};
	public String[] allProvinces = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北"
									,"河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏"
									,"青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
	public int[][] people = new int[32][4];//三十一个省份（加全国），四种类型
	ArrayList<String> allLog = new ArrayList<String>();//存储所有的日志文件
	
	
	/*
	 * 命令行处理
	 */
	public void orderHandle(String[] inputOrder) throws IOException
	{
		if(!(inputOrder[0].equals("list")))
		{
			System.out.println("命令行仅支持list");
			return ;
		}
		for(int i = 1;i < inputOrder.length;i++)
		{
			if(inputOrder[i].equals("-log"))//指定日志目录位置
			{
				allLog = queryFileNames(inputOrder[i+1]);
				for(int j = 0;j < allLog.size();j++)
				{
					readLog(allLog.get(j));
				}
			}
			else if(inputOrder[i].equals("-out"))//指定输出文件路径和文件名
			{
				writeOut(inputOrder[i+1]);
			}
			else if(inputOrder[i].equals("-date"))//指定日志日期
			{
				
			}
			else if(inputOrder[i].equals("-type"))//指定列出患者类型
			{
				
			}
			else if(inputOrder[i].equals("-province"))//指定列出的省
			{
				
			}
		}
		for(int i = 0;i<allLog.size();i++)
		{
			readLog(allLog.get(i));
		}
	}
	
	/*
	 * 获得指定目录的所有文件
	 */
	public static ArrayList<String> queryFileNames(String filePath)
	{
		ArrayList<String> es = new ArrayList<String>();
		File f = new File(filePath);
		File[] ts = f.listFiles();
		File[] fs = f.listFiles();
		for(int i = 0;i < fs.length;i++)
		{
			if(ts[i].isFile())
			{
				es.add(ts[i].toString());
			}
		}
		return es;
	}
	
	/*
	 * 读取日志文件的内容
	 * 输入参数：日志路径
	 * 返回值：无
	 */
	public void readLog(String filePath) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
        		new FileInputStream(new File(filePath)),"UTF-8"));
		String line = null;
		while((line = bufferedReader.readLine()) != null)
		{
			if(!line.startsWith("//"))
			{
				logHandle(line);
			}
		}
		bufferedReader.close();
	}
	
	/*
	 * 处理日志中的每一行
	 * 输入参数：未注释的日志行
	 * 返回值：无
	 */
	public void logHandle(String inputLine)
	{
		int patternNum=10;
		String[] patterns =
		{
			"(\\W+) 新增 感染患者 (\\d+)人",
			"(\\W+) 新增 疑似患者 (\\d+)人",
			"(\\W+) 感染患者 流入 (\\W+) (\\d+)人",
			"(\\W+) 疑似患者 流入 (\\W+) (\\d+)人",
			"(\\W+) 死亡 (\\d+)人",
			"(\\W+) 治愈 (\\d+)人",
			"(\\W+) 疑似患者 确诊感染 (\\d+)人",
			"(\\W+) 排除 疑似患者 (\\d+)人"
		};
		boolean[] matchPattern = new boolean[8];		
		for(int i = 0;i < 8;i++)
		{
			matchPattern[i] = false;
		}
		for(int i = 0;i < 8;i++)
		{
			matchPattern[i] = Pattern.matches(patterns[i], inputLine);
		}
		for(int i = 0;i < 8;i++)
		{
			if(matchPattern[i])
			{
				patternNum=i;
				break;
			}
		}
		//System.out.print(patternNum);
		switch(patternNum)
		{
			case 0 :
				addIp(inputLine);//新增感染患者
				break;
			case 1 :
				addSp(inputLine);//新增疑似患者
				break;
			case 2 :
				inflowIp(inputLine);//确诊患者流入
				break;
			case 3 :
				inflowSp(inputLine);//疑似患者流入
				break;
			case 4 :
				deadIp(inputLine);//患者死亡
				break;
			case 5 :
				cureIp(inputLine);//患者治愈
				break;
			case 6 :
				diagnoseSp(inputLine);//疑似患者确诊
				break;
			case 7 :
				removeSp(inputLine);//排除疑似患者
				break;
			default :
				System.out.println("日志语句出错");
		}
	}
	
	/*
	 * 新增感染患者
	 */
	public void addIp(String line)
	{
		String[] words = line.split(" ");
		String addNum = "";
		for(int i = 0;i < words[3].length();i++)
		{
			if(words[3].charAt(i) >= 48 && words[3].charAt(i) <= 57)
			{
				addNum += words[3].charAt(i);
			}
		}
		int ipNum = Integer.valueOf(addNum).intValue();
		for(int j = 0;j < allProvinces.length;j++)
		{
			if(allProvinces[j].equals(words[0]))
			{
				people[j][0] += ipNum;
				people[0][0] += ipNum;
			}
		}
	}
	
	/*
	 * 新增疑似患者
	 */
	public void addSp(String line)
	{
		String[] words = line.split(" ");
		String addNum = "";
		for(int i = 0;i < words[3].length();i++)
		{
			if(words[3].charAt(i) >= 48 && words[3].charAt(i) <= 57)
			{
				addNum += words[3].charAt(i);
			}
		}
		int spNum = Integer.valueOf(addNum).intValue();
		for(int j = 0;j < allProvinces.length;j++)
		{
			if(allProvinces[j].equals(words[0]))
			{
				people[j][1] += spNum;
				people[0][1] += spNum;
			}
		}
	}
	
	/*
	 * 感染患者流入
	 */
	public void inflowIp(String line)
	{
		String[] words = line.split(" ");
		String addNum = "";
		for(int i = 0;i < words[4].length();i++)
		{
			if(words[4].charAt(i) >= 48 && words[4].charAt(i) <= 57)
			{
				addNum += words[4].charAt(i);
			}
		}
		int ipNum = Integer.valueOf(addNum).intValue();
		for(int j = 0;j < allProvinces.length;j++)
		{
			if(allProvinces[j].equals(words[0]))
			{
				people[j][0] -= ipNum;
			}
			if(allProvinces[j].equals(words[3]))
			{
				people[j][0] += ipNum;
			}
		}
	}
	/*
	 * 疑似患者流入
	 */
	public void inflowSp(String line)
	{
		String[] words = line.split(" ");
		String addNum = "";
		for(int i = 0;i < words[4].length();i++)
		{
			if(words[4].charAt(i) >= 48 && words[4].charAt(i) <= 57)
			{
				addNum += words[4].charAt(i);
			}
		}
		int spNum = Integer.valueOf(addNum).intValue();
		for(int j = 0;j < allProvinces.length;j++)
		{
			if(allProvinces[j].equals(words[0]))
			{
				people[j][1] -= spNum;
			}
			if(allProvinces[j].equals(words[3]))
			{
				people[j][1] += spNum;
			}
		}
	}
	/*
	 * 患者治愈
	 */
	public void cureIp(String line)
	{
		String[] words = line.split(" ");
		String addNum = "";
		for(int i = 0;i < words[2].length();i++)
		{
			if(words[2].charAt(i) >= 48 && words[2].charAt(i) <= 57)
			{
				addNum += words[2].charAt(i);
			}
		}
		int cureNum = Integer.valueOf(addNum).intValue();
		for(int j = 0;j < allProvinces.length;j++)
		{
			if(allProvinces[j].equals(words[0]))
			{
				people[j][2] += cureNum;
				people[0][2] += cureNum;
				people[j][0] -= cureNum;
				people[0][0] -= cureNum;
			}
		}
	}
	/*
	 * 患者死亡
	 */
	public void deadIp(String line)
	{
		String[] words = line.split(" ");
		String addNum = "";
		for(int i = 0;i < words[2].length();i++)
		{
			if(words[2].charAt(i) >= 48 && words[2].charAt(i) <= 57)
			{
				addNum += words[2].charAt(i);
			}
		}
		int deadNum = Integer.valueOf(addNum).intValue();
		for(int j = 0;j < allProvinces.length;j++)
		{
			if(allProvinces[j].equals(words[0]))
			{
				people[j][3] += deadNum;
				people[0][3] += deadNum;
				people[j][0] -= deadNum;
				people[0][0] -= deadNum;
			}
		}
	}
	/*
	 * 疑似患者确诊
	 */
	public void diagnoseSp(String line)
	{
		String[] words = line.split(" ");
		String addNum = "";
		for(int i = 0;i < words[3].length();i++)
		{
			if(words[3].charAt(i) >= 48 && words[3].charAt(i) <= 57)
			{
				addNum += words[3].charAt(i);
			}
		}
		int spNum = Integer.valueOf(addNum).intValue();
		for(int j = 0;j < allProvinces.length;j++)
		{
			if(allProvinces[j].equals(words[0]))
			{
				people[j][0] += spNum;
				people[j][1] -= spNum;
				people[0][0] += spNum;
				people[0][1] -= spNum;
			}
		}
	}
	/*
	 * 排除疑似患者
	 */
	public void removeSp(String line)
	{
		String[] words = line.split(" ");
		String subNum = "";
		for(int i = 0;i < words[3].length();i++)
		{
			if(words[3].charAt(i) >= 48 && words[3].charAt(i) <= 57)
			{
				subNum += words[3].charAt(i);
			}
		}
		int spNum = Integer.valueOf(subNum).intValue();
		for(int j = 0;j < allProvinces.length;j++)
		{
			if(allProvinces[j].equals(words[0]))
			{
				people[j][1] -= spNum;
				people[0][1] -= spNum;
			}
		}
	}
	/*
	 * 输出文件
	 * 输入参数：文件路径
	 * 返回值：无
	 */
	public void writeOut(String filePath) throws IOException
	{
		/*
		for(int i = 0;i < allProvinces.length;i++)
        {
        	for(int j = 0;j < allType.length;j++)
        	{
        		System.out.print(people[i][j]);
        	}
        	System.out.println();
        }
        */
		File dir = new File(filePath);
	    // 一、检查放置文件的文件夹路径是否存在，不存在则创建
	    if(!dir.exists()) 
	    {
	        dir.mkdirs();//创建多级目录
	    }
	    String[] fileName = filePath.split("\\");
	    File checkFile = new File(filePath + fileName[fileName.length-1]);
	    FileWriter writer = null;
	    try 
	    {
	        // 二、检查目标文件是否存在，不存在则创建
	        if(!checkFile.exists()) 
	        {
	            checkFile.createNewFile();// 创建目标文件
	        }
	        // 三、向目标文件中写入内容
	        writer = new FileWriter(checkFile, true);
	        for(int i = 0;i < allProvinces.length;i++)
	        {
	        	writer.write(allProvinces[i] + " ");
	        	for(int j = 0;j < allType.length;j++)
	        	{
	        		writer.write(allType[j] + people[i][j] + "人 ");
	        	}
	        }
	        writer.flush();
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    } 
	    finally 
	    {
	        if (null != writer)
	            writer.close();
	    }
	}
	public static void main(String args[]) throws IOException
	{
		if(args.length == 0)
		{
			System.out.println("未输入命令行参数");
			return ;
		}
		Homework2 hw2 = new Homework2();
		hw2.orderHandle(args);
		
	}
}
