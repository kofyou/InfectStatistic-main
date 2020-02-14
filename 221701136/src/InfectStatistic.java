/**
 * InfectStatistic
 * TODO
 *
 * @author 唐小熊
 * @version 1.0
 * @since 2.13
 * @function 统计疫情数据
 */

import java.io.*;
import java.text.SimpleDateFormat;

import com.sun.org.apache.xpath.internal.operations.String;
import java.util.*;

class InfectStatistic {
	
	string date;
	
	//获取当前的系统时间并格式化输出
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    String currentDate = dateFormat.format(date);
	
	
    
	/*
	 *函数功能：解析命令行
	 *输入参数：命令行字符串
	 *输出参数：-data、-type、-province、-log、-out
	 **/
	public void analyseCommandLine(String args[])throws IOException {
		//使用BufferedReader从控制台读取字符
	/*	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		string str;
		str=br.readLine();
*/
		String date,inputAddress,outputAddress,type,province;
		//type和province的类型可能不止一种，故创建其字符串数组
		List<string> typeList=new List<string>();
		List<string> provinceList=new List<string>();
		
		
		if(!args[0].equals("list")) {
			System.out.println("命令行的格式有误");
		}
		
		
		else if{
		for(int commandOrder=1;commandOrder<args.length;commandOrder++) {
				if(args[commandOrder].equals("-data")) {
					date=args[++commandOrder];
				}
				if(args[commandOrder].equals("-log")) {
					inputAddress=args[++commandOrder];
				}
				if(args[commandOrder].equals("-out")) {
					outputAdderss=args[++commandOrder];
				}
				
				if(args[commandOrder].equals("-type")) {
					type=args[++commandOrder];
					
					//若类型是不以-开头的，则不断添加到类型列表中
					while(!type.startsWith("-")&&commandOrder<args.length-1) {
						typeList.add(type);
						type=args[++commandOrder];
					}
				}
				if(args[commandOrder].equals("-province")) {
					province=args[commandOrder++];
					while(province.startwith("-")&&commandOrder<args.length-1) {
						provinceList.add(province);
						province=args[commandList++];
					}
				}
		}
		}
		}	
	}
	
	/*
	 *函数功能：读取文件名称
	 *输入参数：-log路径
	 *输出参数：文件名称
	 **/
	public string getFileName(string inputAddress) {
	File file = new File(inputAddress);
	string fileName;
	
	//获取inputAddress路径下的所有文件和文件目录
	File[] tempList = file.listFiles();
	
	//获取当前日志的最晚更新日期
	SimpleDateFormat format = new SimpleDataFormat("yyyy-MM-dd");
	
	//设置日志最晚更新时间为tempList[0]
	string latestFileName = tempList[0].getName();
	Date latestDate = format.parse(latetFileName);
	for(int i=1;i<tempList.length();i++) {
		fileName = tempList[i].getName();
		Date fileDate = format.parse(fileName);
		if(fileDate.after(latestDate)) {
			latestDate = fileDate;
		}
	}
	
	//当前时间
	Date commandDate = format.parse(date);
	
	//若提供的日期大于当前时间，则报错
	if(commandDate.after(currentDate)) {
		System.out.println("日期超出范围")；
	}
	
	//若commandDate在日期中不存在，则代表疫情不发生变化
	//
	if()
	
	
	
	for(int i=0;i<tempList.length();i++) {
		fileName = tempList[i].getName();
		
		SimpleDateFormat format = new SimpleDataFormat("yyyy-MM-dd");
		Date commandDate = format.parse(date)
		Date fileDate = format.parse(fileName)
		Date latestDate = format.parse(currentDate);
		
		
		
		//如果提供的日期大于当前日期，则报错
		if(commandDate.after(Date)) {
			System.out.println("日期超出范围");
		}
		
		//如果找到命令行对应的日志，则返回日志名
		else if(commandDate.equals(fileDate)) {
			return commandDate;
		}
		else {
		
			
		}
		
	}
	
		
	}
	}
	
	
	/*
	 *函数功能：获取文件内容
	 *输入参数：文件路径
	 *输出参数：
	 **/
	public void readFile(string inputAddress) {
		string filePath=inputAddress+getFileName(inputAddress);
		
		
		
	}
	
	
	
	
	/*
	 *函数功能：统计省份疫情人数
	 *输入参数：
	 *输出参数：
	 **/
	public void countData() {
		
		
	}
	
	/*
	 *函数功能：输出统计结果到文件中
	 *输入参数：
	 *输出参数：
	 **/
	public void outputData() {
		
		
	}
	
	
    public static void main(String[] args) {
        System.out.println("helloworld");
    }
}
