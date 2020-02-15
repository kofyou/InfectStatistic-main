/**
 * InfectStatistic
 * TODO
 *
 * @author 陈朝帏
 * @version 1.0
 * @since 2020.2.14
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

class InfectStatistic
{
	
	public String[] allType = {"感染患者","疑似患者","治愈","死亡患者"};
	
	public String[] allProvinces = {"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北"
									,"河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏"
									,"青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
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
		for(int i=1;i<inputOrder.length;i++)
		{
			if(inputOrder[i].equals("-log"))//指定日志目录位置
			{
				readLog(inputOrder[i+1]);
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
	}
	
	/*
	 * 读取日志文件的内容
	 * 输入参数：日志路径
	 * 返回值：无
	 */
	public void readLog(String filePath) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
        		new FileInputStream(new File(filePath)), "UTF-8"));
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
		for(int i = 0; i < 8; i++)
		{
			matchPattern[i] = false;
		}
		for(int i = 0; i < 8; i++)
		{
			matchPattern[i] = Pattern.matches(patterns[i], inputLine);
		}
		for(int i = 0; i < 8; i++)
		{
			if(matchPattern[i])
			{
				patternNum=i;
				break;
			}
		}
		switch(patternNum)
		{
			case '0' :
				
				break;
			case '1' :
				
				break;
			case '2' :
				
				break;
			case '3' :
	
				break;
			case '4' :
	
				break;
			case '5' :
	
				break;
			case '6' :
	
				break;
			case '7' :
				
				break;
			default :
				System.out.println("日志语句出错");
		}
	}
	
	/*
	 * 输出文件
	 * 输入参数：文件路径
	 * 返回值：无
	 */
	public void writeOut(String filePath) throws IOException
	{
		File dir = new File(filePath);
	    // 一、检查放置文件的文件夹路径是否存在，不存在则创建
	    if (!dir.exists()) {
	        dir.mkdirs();//创建多级目录
	    }
	    String[] fileName = filePath.split("/");
	    File checkFile = new File(filePath + fileName[fileName.length-1]);
	    FileWriter writer = null;
	    try {
	        // 二、检查目标文件是否存在，不存在则创建
	        if (!checkFile.exists()) {
	            checkFile.createNewFile();// 创建目标文件
	        }
	        // 三、向目标文件中写入内容
	        writer = new FileWriter(checkFile, true);
	        writer.append("your content");
	        writer.flush();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (null != writer)
	            writer.close();
	    }
	}
	public static void main(String args[]) throws IOException
	{
		
	}
}
