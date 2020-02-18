

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
public class Homework2 
{
	public String[] allTypes = {"sp","ip","cure","dead"};
	public String[] allType = {"感染患者","疑似患者","治愈","死亡患者"};
	public String[] allProvinces = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北"
									,"河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏"
									,"青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
	public int[][] people = new int[32][4];//三十一个省份（加全国），四种类型
	ArrayList<String> allLog = new ArrayList<String>();//存储所有的日志文件
	public int[] provinceFlag = new int[32];//根据参数-province确定输出内容
	public int[] typeFlag = new int[4];//根据参数-type确定输出内容
	String date = "";//根据参数date确定输出日期
	String outOrder = "";//全局变量存储-out指令
	String dateOrder = "";//全局变量存储-date指令
	String provinceOrder = "";//全局变量存储-province指令
	String typeOrder = "";//全局变量存储-type指令
	
	/*
	 * 命令行处理
	 */
	public void orderHandle(String[] inputOrder) throws IOException
	{
		for(int j = 0;j < allType.length;j++)
		{
			typeFlag[j] = 0;
		}
		for(int j = 0;j < allProvinces.length;j++)
		{
			provinceFlag[j] = 0;
		}
		if(!(inputOrder[0].equals("list")))
		{
			System.out.println("命令行仅支持list");
			return ;
		}
		for(int i = 1;i < inputOrder.length;i++)
		{
			if(inputOrder[i].equals("-log"))//指定日志目录位置
			{
				if(isValidLogAddress(inputOrder[i + 1]))
				{
					allLog = queryFileNames(inputOrder[i + 1]);
				}
				else 
				{
					System.out.print("日志目录位置有误，请重新输入。");
					return ;
				}
			}
			else if(inputOrder[i].equals("-out"))//指定输出文件路径和文件名
			{
				if(isValidOutAddress(inputOrder[i + 1]))
				{
					outOrder = inputOrder[i + 1];
				}
				else 
				{
					System.out.print("输出文件路径有误，请重新输入。");
					return ;
				}
				
			}
			else if(inputOrder[i].equals("-date"))//指定日志日期
			{
				dateOrder = inputOrder[i + 1];
				date = inputOrder[i + 1] + ".log.txt";
			}
			else if(inputOrder[i].equals("-type"))//指定列出患者类型
			{
				typeOrder = "get -type";
				int k = i + 1;
				while(k < inputOrder.length)
				{
					for(int j = 0;j < allType.length;j++)
					{
						if(inputOrder[k].equals(allTypes[j]))
						{
							typeFlag[j] = 1;
						}
					}
					k++;
				}
			}
			else if(inputOrder[i].equals("-province"))//指定列出的省
			{
				provinceOrder = "get -province";
				int k = i + 1;
				while(k < inputOrder.length)
				{
					for(int j = 0;j < allProvinces.length;j++)
					{
						if(inputOrder[k].equals(allProvinces[j]))
						{
							provinceFlag[j] = 1;
						}
					}
					k++;
				}
			}
		}
		if(!(isValidDate(dateOrder)))
		{
			System.out.print("日期不合法，请重新输入。");
			return ;
		}
		else if(!(isDateOutOfRange(dateOrder)))
		{
			System.out.print("日期超出范围，请重新输入。");
			return ;
		}
		for(int k = 0;k < allLog.size();k++)
		{
			readLog(allLog.get(k));
			String[] log = allLog.get(k).split("\\\\");
			if(log[log.length - 1].equals(date))
			{
				break;
			}
		}
		writeOut(outOrder);
	}
	
	/*
	 * 判断日志文件目录路径名是否合法
	 */
	public boolean isValidLogAddress(String address)
	{
		if(address.matches("^[A-z]:\\\\(.+?\\\\)*$"))
		{
			return true;
		}
		return false;
	}
	
	/*
	 * 判断输出文件路径是否合法
	 */
	public boolean isValidOutAddress(String address)
	{
		if(address.matches("^[A-z]:\\\\(.+?\\\\)*(.+?.txt)$"))
		{
			return true;
		}
		return false;
	}
	
	/*
	 * 判断日期是否合法
	 */
	public boolean isValidDate(String inputDate)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try 
        {
            format.setLenient(false);//此处指定日期/时间解析是否不严格，在true是不严格，false时为严格
            format.parse(inputDate);//从给定字符串的开始解析文本，以生成一个日期
            
            String[] sArray = inputDate.split("-");
            for (String s : sArray) 
            {
                boolean isNum = s.matches("[0-9]+");
                if (!isNum)
                    return false;
            }
        } 
        catch (Exception e) 
        {
            return false;
        }
        return true;
	}
	
	/*
	 * 判断日期是否超出范围
	 */
	public boolean isDateOutOfRange(String inputDate)
	{
		String[] endLog = allLog.get(allLog.size() - 1).split("\\\\");
		String[] beginLog = allLog.get(0).split("\\\\");
		if((inputDate.compareTo(endLog[endLog.length - 1])) == 1)
		{
			return false;
		}
		else if((inputDate.compareTo(beginLog[endLog.length - 1])) == -1)
		{
			return false;
		}
		return true;
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
		if(typeOrder.equals(""))
		{
			for(int i = 0;i < allType.length;i++)
			{
				typeFlag[i] = 1;
			}
		}
		if(provinceOrder.equals(""))
		{
			for(int i = 0;i < allProvinces.length;i++)
			{
				if(people[i][0] != 0 || people[i][1] != 0 || people[i][2] != 0 || people[i][3] != 0)
				{
					provinceFlag[i] = 1;
				}
			}
		}
	    FileWriter writer = null;
	    try 
	    {
	        writer = new FileWriter(filePath);
	        for(int i = 0;i < allProvinces.length;i++)
	        {
	        	if(provinceFlag[i] == 1)
	        	{
	        		writer.write(allProvinces[i] + " ");
		        	for(int j = 0;j < allType.length;j++)
		        	{
		        		if(typeFlag[j] == 1)
		        		{
			        		writer.write(allType[j] + people[i][j] + "人 ");
		        		}
		        	}
		        	writer.write("\n");
	        	}
	        }
	        writer.write("// 该文档并非真实数据，仅供测试使用");
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    } 
	    finally 
	    {
            try 
            {
                writer.flush();
                writer.close();
            } 
            catch(IOException ex) 
            {
                ex.printStackTrace();
            }
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
