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
import java.util.regex.Pattern;


class InfectStatistic {
	
	String commandDate;
	
	//获取当前的系统时间并格式化输出
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    String currentDate = dateFormat.format(date);
	
	//构造一个双层嵌套的哈希表
	HashMap<String,HashMap<String,Integer>> ProvinceToNumMap 
	                               = new HashMap<String,HashMap<String,Integer>>();
	HashMap<String,Integer> TypeToNumMap = new HashMap<String,Integer>();
	
	//初始化TypeToNum哈希表
	TypeToNumMap.put("感染患者",0);
    TypeToNumMap.put("疑似患者",0);
    TypeToNumMap.put("治愈",0);
    TypeToNumMap.put("死亡",0);
    
    String provinceList[] = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南",
    		"黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海",
    		"四川","天津","西藏","新疆","云南","浙江"
}
    
    for(int i=0;i<provinceList.length;i++) {
    	ProvinceToNumMap.put(provinceList[i], TypeToNumMap);
    }
    
    
    String date,inputAddress,outputAddress;
    
	//type和province的类型可能不止一种，故创建其字符串数组
	List<String> typeList=new List<String>();
			
	List<String> commandProvinceList=new List<String>();
	
    
	/*
	 *函数功能：解析命令行
	 *输入参数：命令行字符串
	 *输出参数：无
	 **/
	public static void analyseCommandLine(String args[]) {
		String province,type;
		
		if(!args[0].equals("list")) {
			System.out.println("命令行的格式有误");
		}
		
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
						commandProvinceList.add(province);
						province = args[commandList++];
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
	 *函数功能：查询路径文件
	 *输入参数：-log路径
	 *输出参数：文件名称
	 **/
	public void searchFile(String inputAddress) {
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
		
		
		String lineTypeOne = "(\\s+) 新增 感染患者 (\\d+)人";
		String lineTypeTwo = "(\\s+) 新增 疑似患者 (\\d+)人";
		String lineTypeThree = "(\\s+) 治愈 (\\d+)人";
		String lineTypeFour = "(\\s+) 死亡 (\\d+)人";
		String lineTypeFive = "(\\s+) 感染患者 流入 (\\s+) (\\d+)人";
		String lineTypeSix = "(\\s+) 疑似患者 流入 (\\s+) (\\d+)人";
		String lineTypeSeven = "(\\s+) 疑似患者 确认感染 (\\d+)人";
		String lineTypeEight = "(\\s+) 排除 疑似患者 (\\d+)人";
		
		if(Pattern.matches(lineTypeOne, lineInformation)) {
			addInfectionPatients(linePart);
		}
		if(Pattern.matches(lineTypeTwo, lineInformation)) {
			addSuspectedPatients(linePart);
		}
		if(Pattern.matches(lineTypeThree, lineInformation)) {
			addCurePatients(linePart);
		}
		if(Pattern.matches(lineTypeFour, lineInformation)) {
			addDeadPatients(linePart);
		}
		if(Pattern.matches(lineTypeFive, lineInformation)) {
			infectionPatientsMove(linePart);
		}
		if(Pattern.matches(lineTypeSix, lineInformation)) {
			suspectedPatientsMove(linePart);
		}
		if(Pattern.matches(lineTypeSeven, lineInformation)) {
			suspectedToInfection(linePart);
		}
		if(Pattern.matches(lineTypeEight, lineInformation)) {
			suspectedToNormal(linePart);
		}
		
		
	}
		

	/*
	 *函数功能：增加感染患者
	 *输入参数：
	 *输出参数：
	 **/
	public void addInfectionPatients(String linePart[]) {
		
		//先将每一行的字符串分隔成字符串数组
		String[] linePart = lineInformation.split(" ");
		String province = linePart[0];
				
		//新增感染患者的数量
		int num;
		
		//去除数字后面的“人”，取出单独的数字
		num = Integer.valueOf(linePart[3].replaceAll("人",""));
		
		int previousNum;
		
		//获取省份对应下的感染患者数量
		Set<String> thisSet = ProvinceToNumMap.keySet();
		for(String str:thisSet) {
			if(str.equals(province)) {
				HashMap<String,String> thisMap = ProvinceToNumMap.get(str);
				Set<String> typeKeys = thisMap.keySet();
				for(String strTwo:typeKeys) {
					if(strTwo.equals("感染患者")) {
						previousNum = thisMap.get(strTwo);
					}
				}
			}
		}
		
		
		currentNum = num + previousNum;
		
	    ProvinceToNumMap.get(province).replace(linePart[2],currentNum);
		
	}
	
	
	/*
	 *函数功能：增加疑似患者
	 *输入参数：
	 *输出参数：
	 **/
     public void addSuspectedPatients(String linePart[]) {
		
		//先将每一行的字符串分隔成字符串数组
		String[] linePart = lineInformation.split(" ");
		String province = linePart[0];
				
		//新增疑似感染患者的数量
		int num;
		
		//去除数字后面的“人”，取出单独的数字
		num = Integer.valueOf(linePart[3].replaceAll("人",""));
		
		int previousNum;
		
		//获取省份对应下的感染患者数量
		Set<String> thisSet = ProvinceToNumMap.keySet();
		for(String str:thisSet) {
			if(str.equals(province)) {
				HashMap<String,String> thisMap = ProvinceToNumMap.get(str);
				Set<String> typeKeys = thisMap.keySet();
				for(String strTwo:typeKeys) {
					if(strTwo.equals("疑似患者")) {
						previousNum = thisMap.get(strTwo);
					}
				}
			}
		}
		
		
		currentNum = num + previousNum;
		
	    ProvinceToNumMap.get(province).replace(linePart[2],currentNum);
		
	}
	
     /*
 	 *函数功能：感染患者流动
 	 *输入参数：
 	 *输出参数：
 	 **/
      public void infectionPatientsMove(String linePart[]) {
 		
 		//先将每一行的字符串分隔成字符串数组
 		String[] linePart = lineInformation.split(" ");
 		String flowOutProvince = linePart[0];
 		String flowInProvince = linePart[3];
 		
 		
 		//流动患者数量
 		int flowNum;
 		
 		//去除数字后面的“人”，取出单独的数字
 		flowNum = Integer.valueOf(linePart[4].replaceAll("人",""));
 		
 		int flowOutPreviousNum;
 		int flowInPreviousNum;
 		
 		//获取省份对应下的感染患者数量
 		Set<String> thisSet = ProvinceToNumMap.keySet();
 		for(String str:thisSet) {
 			if(str.equals(flowOutProvince)) {
 				HashMap<String,String> thisMap = ProvinceToNumMap.get(str);
 				Set<String> typeKeys = m.keySet();
 				for(String strTwo:typeKeys) {
 					if(strTwo.equals("感染患者")) {
 						flowOutPreviousNum = m.get(strTwo);
 					}
 				}
 			}
 			if(str.equals(flowInProvince)) {
 				HashMap<String,String> thisMap = ProvinceToNumMap.get(str);
 				Set<String> typeKeys = m.keySet();
 				for(String strTwo:typeKeys) {
 					if(strTwo.equals("感染患者")) {
 						flowInPreviousNum = m.get(strTwo);
 					}
 				}
 			}
 		}
 		
 		
 		flowOutCurrentNum = flowOutPreviousNum-flowNum;
 		flowInCurrentNum = flowInPreviousNum+flowNum;
 		
 	    ProvinceToNumMap.get(flowOutProvince).replace(linePart[1],flowOutCurrentNum);
 	   ProvinceToNumMap.get(flowInProvince).replace(linePart[1],flowInCurrentNum);
 	}
 	
      
      /*
   	 *函数功能：疑似患者流动
   	 *输入参数：
   	 *输出参数：
   	 **/
     public void suspectedPatientsMove(String linePart[]) {
   		
   		//先将每一行的字符串分隔成字符串数组
   		String[] linePart = lineInformation.split(" ");
   		String flowOutProvince = linePart[0];
   		String flowInProvince = linePart[3];
   		
   		
   		//流动疑似患者数量
   		int flowNum;
   		
   		//去除数字后面的“人”，取出单独的数字
   		flowNum = Integer.valueOf(linePart[4].replaceAll("人",""));
   		
   		int flowOutPreviousNum;
   		int flowInPreviousNum;
   		
   		//获取省份对应下的疑似患者数量
   		Set<String> thisSet = ProvinceToNumMap.keySet();
   		for(String str:thisSet) {
   			if(str.equals(flowOutProvince)) {
   				HashMap<String,String> thisMap = ProvinceToNumMap.get(str);
   				Set<String> typeKeys = thisMap.keySet();
   				for(String strTwo:typeKeys) {
   					if(strTwo.equals("疑似患者")) {
   						flowOutPreviousNum = thisMap.get(strTwo);
   					}
   				}
   			}
   			if(str.equals(flowInProvince)) {
   				HashMap<String,String> thisMap = ProvinceToNumMap.get(str);
   				Set<String> typeKeys = thisMap.keySet();
   				for(String strTwo:typeKeys) {
   					if(strTwo.equals("疑似患者")) {
   						flowInPreviousNum = thisMap.get(strTwo);
   					}
   				}
   			}
   		}
   		
   		
   		flowOutCurrentNum = flowOutPreviousNum-flowNum;
   		flowInCurrentNum = flowInPreviousNum+flowNum;
   		
   	    ProvinceToNumMap.get(flowOutProvince).replace(linePart[1],flowOutCurrentNum);
   	   ProvinceToNumMap.get(flowInProvince).replace(linePart[1],flowInCurrentNum);
   	}
   	

        /*
        *函数功能：统计死亡人数
       	 *输入参数：
       	 *输出参数：
       	 **/
     public void addDeadPatients(String linePart[]) {
       		
       		//先将每一行的字符串分隔成字符串数组
       		String[] linePart = lineInformation.split(" ");
       		String province = linePart[0];
       		
       		
       		
       		//死亡患者数量
       		int num;
       		
       		//去除数字后面的“人”，取出单独的数字
       		num = Integer.valueOf(linePart[2].replaceAll("人",""));
       		
       		int previousNum;
       		
       		//获取省份对应下的死亡患者数量
       		Set<String> thisSet = ProvinceToNumMap.keySet();
       		for(String str:thisSet) {
       			if(str.equals(province)) {
       				HashMap<String,String> thisMap = ProvinceToNumMap.get(str);
       				Set<String> typeKeys = thisMap.keySet();
       				for(String strTwo:typeKeys) {
       					if(strTwo.equals("死亡")) {
       						previousNum = thisMap.get(strTwo);
       					}
       				}
       			}
       		}	
       		currentNum = previousNum+num;
       	    ProvinceToNumMap.get(province).replace(linePart[1],currentNum);  
       	}
       	
     /*函数功能：统计治愈人数
          *输入参数：
         *输出参数：
          **/
     public void addCurePatients(String linePart[]) {
           		
           		//先将每一行的字符串分隔成字符串数组
           		String[] linePart = lineInformation.split(" ");
           		String province = linePart[0];
           		
           		
           		
           		//治愈患者数量
           		int num;
           		
           		//去除数字后面的“人”，取出单独的数字
           		num = Integer.valueOf(linePart[2].replaceAll("人",""));
           		int previousNum;
           		
           		//获取省份对应下的治愈患者数量
           		Set<String> thisSet = ProvinceToNumMap.keySet();
           		for(String str:thisSet) {
           			if(str.equals(province)) {
           				HashMap<String,String> thisMap = ProvinceToNumMap.get(str);
           				Set<String> typeKeys = thisMap.keySet();
           				for(String strTwo:typeKeys) {
           					if(strTwo.equals("治愈")) {
           						previousNum = thisMap.get(strTwo);
           					}
           				}
           			}
           		}
           		currentNum = previousNum+num;
           	    ProvinceToNumMap.get(province).replace(linePart[1],currentNum);
           	}
           	
                
     /*函数功能：疑似患者确认感染
               	 *输入参数：
               	 *输出参数：
               	 **/
      public void suspectedToInfection(String linePart[]) {
               		
               		//先将每一行的字符串分隔成字符串数组
               		String[] linePart = lineInformation.split(" ");
               		String province = linePart[0];
               		
               		
               		
               		//疑似患者确认感染数量
               		int num;
               		
               		//去除数字后面的“人”，取出单独的数字
               		num = Integer.valueOf(linePart[3].replaceAll("人",""));
               		int previousNum;
               		
               		//获取省份对应下的感染患者数量
               		Set<String> thisSet = ProvinceToNumMap.keySet();
               		for(String str:thisSet) {
               			if(str.equals(province)) {
               				HashMap<String,String> thisMap = ProvinceToNumMap.get(str);
               				Set<String> typeKeys = thisMap.keySet();
               				for(String strTwo:typeKeys) {
               					if(strTwo.equals("感染患者")) {
               						previousNum = thisMap.get(strTwo);
               					}
               				}
               			}
               		}
               		currentNum = previousNum+num;
               	    ProvinceToNumMap.get(province).replace("感染患者",currentNum);
               	}
                    
     
      /*函数功能：排除疑似感染患者
    	 *输入参数：
    	 *输出参数：
    	 **/
         public void suspectedToNormal(String linePart[]) {
    		
    		//先将每一行的字符串分隔成字符串数组
    		String[] linePart = lineInformation.split(" ");
    		String province = linePart[0];
    		
    		
    		
    		//排除疑似患者数量
    		int num;
    		
    		//去除数字后面的“人”，取出单独的数字
    		num = Integer.valueOf(linePart[3].replaceAll("人",""));
    		int previousNum;
    		
    		//获取省份对应下的疑似患者数量
    		Set<String> thisSet = ProvinceToNumMap.keySet();
    		for(String str:thisSet) {
    			if(str.equals(province)) {
    				HashMap<String,String> thisMap = ProvinceToNumMap.get(str);
    				Set<String> typeKeys = thisMap.keySet();
    				for(String strTwo:typeKeys) {
    					if(strTwo.equals("疑似患者")) {
    						previousNum = thisMap.get(strTwo);
    					}
    				}
    			}
    		}
    		currentNum = previousNum+num;
    	    ProvinceToNumMap.get(province).replace("疑似患者",currentNum);
    	}

                   		
                  	 
                        
                    
	/*
	 *函数功能：输出统计结果到文件中
	 *输入参数：
	 *输出参数：
	 **/
	public void outputData(String path) {
		FileWriter fileWriter = null;
		File file = new File(path);
		try {
			if(!file.exists()) {
				file.createNewFile(path);
			}
			fileWriter = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fileWriter);
			
			//若-province包含全国或者命令行不包含-province
			if(commandProvinceList.contains("全国")||commandProvinceList.isEmpty()) {
				Set<String> thisSet = ProvinceToNumMap.keySet();
				for(String strKey:thisSet) {		
					HashMap<String,Integer> TypeToNumValue = ProvinceToNumMap.get(strKey);
					Set<String> set = TypeToNumValue.keySet();
					for(String integerKey:set) {
						for(int j=0;j<typeList.size();i++) {
							switch(typeList[i]) {
							case "ip":Integer value = TypeToNumValue.get("感染患者");
							out.write(strKey+" "+"感染患者"+" "+value+"\n");
							case "sp":Integer value = TypeToNumValue.get("疑似患者");
							out.write(strKey+" "+"疑似患者"+" "+value+"\n");
							case "cure":Integer value = TypeToNumValue.get("治愈");
							out.write(strKey+" "+"治愈"+" "+value+"\n");
							case "dead":Integer value = TypeToNumValue.get("死亡");
							out.write(strKey+" "+"死亡"+" "+value+"\n");
							}
						}
						
						Integer value = TypeToNumValue.get(integerKey);
					}
					out.write(strKey+integerKey+value);
				}
			}
			else {
				Set<String> thisSet = ProvinceToNumMap.keySet();
				for(String strKey:thisSet) {		
					for(int i=0;i<commandProvinceList.size();i++) {
						if(strKey.equals(commandProvinceList[i])) {
							
							HashMap<String,Integer> TypeToNumValue = ProvinceToNumMap.get(strKey);
						Set<String> set = TypeToNumValue.keySet();
						for(String integerKey:set) {
							for(int j=0;j<typeList.size();i++) {
							switch(typeList[i]) {
							case "ip":Integer value = TypeToNumValue.get("感染患者");
							out.write(strKey+" "+"感染患者"+" "+value+"\n");
							case "sp":Integer value = TypeToNumValue.get("疑似患者");
							out.write(strKey+" "+"疑似患者"+" "+value+"\n");
							case "cure":Integer value = TypeToNumValue.get("治愈");
							out.write(strKey+" "+"治愈"+" "+value+"\n");
							case "dead":Integer value = TypeToNumValue.get("死亡");
							out.write(strKey+" "+"死亡"+" "+value+"\n");
							}
						}
							}
						
						}
					}
					
				}
				
			} 
			
			out.write("// 该文档并非真实数据，仅供测试使用");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
    public static void main(String[] args) {
       InfectStatistic infectStatistic = new InfectStatistic();
       infectStatistic.analyseCommandLine(args);
       infectStatistic.searchFile(inputAddress);
       infectStatistic.outputData(outPutAddesss);
    }
}
