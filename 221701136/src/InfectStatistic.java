/**
 * InfectStatistic
 * TODO
 *
 * @author 唐小熊
 * @version 1.1
 * @since 2.13
 * @function 统计疫情数据
 */

import java.io.*;
import java.text.SimpleDateFormat;
import java.lang.*;
import com.sun.org.apache.xpath.internal.operations.String;
import java.util.*;
import java.util.HashMap;;
import java.util.Map;

class InfectStatistic {
	
	string commandDate;
	
	//获取当前的系统时间并格式化输出
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    String currentDate = dateFormat.format(date);
	
    //创建哈希表
	HashMap<String,Integer> TypeToNumMap = new HashMap<String,Integer>();
	HashMap<String,TypeToNumMap> ProvinceToNumMap = new HashMap<String,TypeToNumMap>();
	
	//初始化
	TypeToNumMap.put("感染患者",0);
    TypeTnNumMap.put("疑似患者",0);
    TypeToNumMap.put("治愈",0);
    TypeToNumMap.put("死亡",0);
    
    ProvinceToNumMap("全国",TypeToNumMap);
    
    
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
					currentDate = changeToValidDate(args[++commandOrder]);
				}
				if(args[commandOrder].equals("-log")) {
					inputAddress = args[++commandOrder];
					if(!isValidAddress(inputAddress)) {
						System.out.println("log路径错误！");
						return;
					}
				}
				if(args[commandOrder].equals("-out")) {
					outputAdderss = args[++commandOrder];
					if(!isValidAddress(outputAddress)) {
						System.out.println("out路径错误！");
						return;
					}
				}
				if(args[commandOrder].equals("-type")) {
					type = args[++commandOrder];
					
					//若类型是不以-开头的，则不断添加到类型列表中
					while(!type.startsWith("-")&&commandOrder<args.length-1) {
						typeList.add(type);
						type = args[++commandOrder];
					}
				}
				if(args[commandOrder].equals("-province")) {
					province = args[commandOrder++];
					while(!province.startwith("-")&&commandOrder<args.length-1) {
						provinceList.add(province);
						province = args[commandList++];
					}
				}
		}
		}
		}	
	}
	
/*
 *函数功能：判断命令行格式是否有错误
 *输入参数：args[]
 *输出参数：true,false
 **/
    public boolean isValidCommand(String args[]) {
	    if(!args[0].equals("list")) {
		    System.out.println("命令行的格式有误");
		    return false;
	    }
    }

    /*
     *函数功能：获取合法的日志日期
     *输入参数：args[]
     *输出参数：Date
     **/
    public Date changeToValidDate(String date) {
    	SimpleDateFormat format = new SimpleDataFormat("yyyy-MM-dd");
    	Date validDate = format.parse(date);
    	return validDate;
    }
    
    /*
     *函数功能：判断是否是合法的路径
     *输入参数：string
     *输出参数：false,true
     **/
    public boolean isValidAddress(String address) {
    	//用正则表达式判断输入的路径是否正确
    	if(address.matches("^[A-z]:\\\\(.+?\\\\)*$")) {
    	ruturn true;
    	}
    	else
    		return false;
    }
   
    
	/*
	 *函数功能：获取文件
	 *输入参数：-log路径
	 *输出参数：文件名称
	 **/
	public void getFile(string inputAddress) {
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
	
	Date commandDate = format.parse(date);
	
	//若提供的日期大于当前时间，则报错
	if(commandDate.after(currentDate)) {
		System.out.println("日期超出范围")；
	}
	
	//获取所有小于commandDate的日志,并读取内容
	for(int j=0;j<tempList.length;j++) {
		fileName = tempList[i].getName();
		Date fileDate = format.parse(fileName);
		if(fileDate.before(commandDate)) {
			readFile(inputAddress+fileName+".log.txt");
		}	
	}
	}
	
	
	/*
	 *函数功能：获取文件内容
	 *输入参数：文件路径
	 *输出参数：无
	 **/
	public void readFile(String address) throws IOException {
	FileInputStream fiStream = new FileInputStream(address);
	InputStreamReader isReader = new InputStreamReader(fiStream,"UTF-8");
	BufferedReader bufferedReader = new BufferedReader(isReader);
	String line = null;
	while((line=bufferedReader.readLine())! = null) {
		if(!line.startWith("//")) {
			handleInformation(line);
		}	
	}
	}
	
	
	
	
	/*
	 *函数功能：统计省份疫情人数
	 *输入参数：
	 *输出参数：
	 **/
	public void handleInformation(String lineInformation) {
		//先将每一行的字符串分隔成字符串数组
		String[] linePart = lineInformation.split(" ");
		String province = linePart[0];
		
		if(lineInformation.contains("新增 感染患者"))
	
		
		
			
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
