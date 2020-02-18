/*
 * InfectStatistic
 * TODO
 *
 * @author 唐小熊
 * @version 1.1
 * @since 2.13
 * @function 统计疫情数据
 */


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.lang.String;
import java.util.*;
import java.util.HashMap;
import java.util.regex.Pattern;
import javax.annotation.Resource;

class InfectStatistic {
	
	
	private static final HashMap<String, Integer> TypeToNumMap = null;
	private static String commandDate;
	//获取当前的系统时间并格式化输出
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static Date date1 = new Date(System.currentTimeMillis());
    static String currentDate = dateFormat.format(date1);
	 	//构造一个双层嵌套的哈希表
	static HashMap <String,HashMap<String,Integer>> ProvinceToNumMap;

	static boolean isEmptyCommandProvince = true;
	 static String provinceList[] = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南",
	    		"黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海",
	    		"四川","天津","西藏","新疆","云南","浙江"};              
  public InfectStatistic() {
	  
    	ProvinceToNumMap
        = new HashMap<String,HashMap<String,Integer>>();
    	
      /*  String provinceList[] = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南",
    		"黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海",
    		"四川","天津","西藏","新疆","云南","浙江"};
	 	 */
          for(int i=0;i<provinceList.length;i++) {
        	  HashMap<String,Integer> TypeToNumMap = new HashMap<String,Integer>();
    	//初始化TypeToNum哈希表
    	TypeToNumMap.put("感染患者",0);
        TypeToNumMap.put("疑似患者",0);
        TypeToNumMap.put("治愈",0);
        TypeToNumMap.put("死亡",0);
        
    	ProvinceToNumMap.put(provinceList[i], TypeToNumMap);
    }
      }
	 
          
          
	static String inputAddress;
	static String outputAddress;
    
	//type和province的类型可能不止一种，故创建其字符串数组
	static List<String> typeList=new LinkedList<>();
			
	static List<String> commandProvinceList=new LinkedList<String>();
	
	/*
	 *函数功能：解析命令行
	 *输入参数：命令行字符串
	 *输出参数：无
	 **/
	public void analyseCommandLine(String args[]) {
		String province,type;
		int commandOrder=0;
		if(!args[0].equals("list")) {
			System.out.println("命令行的格式有误");
		}
		else {
		while(commandOrder<args.length) {
				if(args[commandOrder].equals("-date")) {
				//commandDate = changeToValidDate(args[++commandOrder]);
					commandDate = args[++commandOrder];
				}
				else if(args[commandOrder].equals("-log")) {
					inputAddress = args[++commandOrder];
					if(!isValidInputAddress(inputAddress)) {
						System.out.println("log路径错误！");
						//return;
					}
				}
				else if(args[commandOrder].equals("-out")) {
					outputAddress = args[++commandOrder];
					if(!isValidOutputAddress(outputAddress)) {
						System.out.println("out路径错误！");
						//return;
					}
				}
				else if(args[commandOrder].equals("-type")) {
					
					type = args[++commandOrder];
					
					//若类型是不以-开头的，则不断添加到类型列表中
					while(!type.startsWith("-")) {
						switch(type) {
						case "ip":typeList.add("感染患者");
						break;
						case "sp":typeList.add("疑似患者");
						break;
						case "cure":typeList.add("治愈");
						break;
						case "dead":typeList.add("死亡");
						break;
						}
						
						if(commandOrder==args.length-1) 
							break;
						type = args[++commandOrder];
					}
				}
				else if(args[commandOrder].equals("-province")) {
					isEmptyCommandProvince = false;
					province = args[++commandOrder];
					while(!province.startsWith("-")) {
						commandProvinceList.add(province);
						if(commandOrder==args.length-1) 
							break;
						province = args[++commandOrder];
					}
				}
				else commandOrder++;
		}
	}
		if(isEmptyCommandProvince == true) {
		commandProvinceList.add("全国");	
		}
		}
	
		
	
	
/*
 *函数功能：判断命令行格式是否有错误
 *输入参数：args[]
 *输出参数：true,false
 **/
  /*  public boolean isValidCommand(String args[]) {
	    if(!args[0].equals("list")) {
		    System.out.println("命令行的格式有误");
		    return false;
	    }
    }
    */

    /*
     *函数功能：获取合法的日志日期
     *输入参数：args[]
     *输出参数：Date
     **/
   /* public String changeToValidDate(String date) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	Date validDate = format.parse(date);
    	return validDate;
    }
    */
    /*
     *函数功能：判断是否是合法的路径
     *输入参数：string
     *输出参数：false,true
     **/
    public static boolean isValidInputAddress(String address) {
    	//用正则表达式判断输入的路径是否正确
    	if (address.matches("^[A-z]:\\\\(.+?\\\\)*$")) {
    	return true;
    	}
    	else
    		return false;
    }
    /*
     *函数功能：判断是否是合法的路径
     *输入参数：string
     *输出参数：false,true
     **/
    public static boolean isValidOutputAddress(String address) {
    	//用正则表达式判断输入的路径是否正确
    	if (address.matches("^[A-z]:\\\\(\\S+)+(\\.txt)$")) {
    	return true;
    	}
    	else
    		return false;
    }
    
   
    
	/*
	 *函数功能：查询路径文件
	 *输入参数：-log路径
	 *输出参数：文件名称
	 **/
	public void searchFile(String address) throws ParseException, IOException {
	File file = new File(inputAddress);
	String fileName;
	
	//获取inputAddress路径下的所有文件和文件目录
	File[] tempList ;
	tempList = file.listFiles();
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	String latestDate = tempList[0].getName();
		Date latestDay = format.parse(latestDate);
	if(commandDate.isEmpty()) {
	for(int i=0;i<tempList.length;i++) {
		fileName = tempList[i].getName();
		Date fileDay = format.parse(fileName);
		if(fileDay.after(latestDay)) {
			latestDay = fileDay;
		}
	}
	commandDate = latestDay.toString();
	}
	
	Date commandDay = format.parse(commandDate);
	Date currentDay = format.parse(currentDate);
	
	//若提供的日期大于当前时间，则报错
	if(commandDay.after(currentDay)) {
		System.out.println("日期超出范围");
	}
	
	//获取所有小于commandDate的日志,并读取内容
	for(int j=0;j<tempList.length;j++) {
		fileName = tempList[j].getName();
		Date fileDay = format.parse(fileName);
		if(fileDay.before(commandDay)||fileDay.equals(commandDay)) {
			
				readFile(inputAddress+fileName);
			}
		}	
	}
	
	
	
	
	/*
	 *函数功能：获取文件内容
	 *输入参数：文件路径
	 *输出参数：无
	 **/
	public void readFile(String address) throws IOException {
		try {
	        BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(address)),"UTF-8"));
	        String lineTxt = null;
	        
	                //按行读取文本内容
	        while ((lineTxt = bfr.readLine()) != null) { 
	        	
	        	//遇到“//”不读取
	            if(!lineTxt.startsWith("//")) {
	            	 
	            		//先将每一行的字符串分隔成字符串数组
	            	if(isEmptyCommandProvince == true) {
	            		
	            	String[] linePart = lineTxt.split(" ");
	            	String province = linePart[0];	
	            	if(!commandProvinceList.contains(province)) {
	            	commandProvinceList.add(province);
	            	}
	            	}
	             handleInformation(lineTxt);
	            // System.out.println("x");
	           
	        }
	        
	    
	    }bfr.close();
	        }catch (Exception e) {
	        e.printStackTrace();
	}
	}
	
	/*
	 *函数功能：统计省份疫情人数
	 *输入参数：
	 *输出参数：
	 **/
	public void handleInformation(String lineInformation) {
		String lineTypeOne = "(\\S+) 新增 感染患者 (\\d+)人";
		String lineTypeTwo = "(\\S+) 新增 疑似患者 (\\d+)人";
		String lineTypeThree = "(\\S+) 治愈 (\\d+)人";
		String lineTypeFour = "(\\S+) 死亡 (\\d+)人";
		String lineTypeFive = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
		String lineTypeSix = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
		String lineTypeSeven = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
		String lineTypeEight = "(\\S+) 排除 疑似患者 (\\d+)人";
		
		
		if(Pattern.matches(lineTypeOne, lineInformation)) {
			addInfectionPatients(lineInformation);
		}
		if(Pattern.matches(lineTypeTwo, lineInformation)) {
			
			addSuspectedPatients(lineInformation);
			
		}
		if(Pattern.matches(lineTypeThree, lineInformation)) {
			addCurePatients(lineInformation);
			
		}
		if(Pattern.matches(lineTypeFour, lineInformation)) {
			addDeadPatients(lineInformation);
			
		}
		if(Pattern.matches(lineTypeFive, lineInformation)) {
			infectionPatientsMove(lineInformation);
			
		}
		if(Pattern.matches(lineTypeSix, lineInformation)) {
			suspectedPatientsMove(lineInformation);
			
			}
		if(Pattern.matches(lineTypeSeven, lineInformation)) {
			suspectedToInfection(lineInformation);
			
		}
		if(Pattern.matches(lineTypeEight, lineInformation)) {
			suspectedToNormal(lineInformation);
			
		}
		
		
	}
		
	/*
	 *函数功能：获取对应省份对应类型的患者previousNum
	 *输入参数：
	 *输出参数：
	 **/
	public int searchProvinceToTypeNum(String province,String type) {
		//获取省份对应类型下的患者数量
		int previousNum=0;
				Set<String> thisSet = ProvinceToNumMap.keySet();
				for(String str:thisSet) {
					if(str.equals(province)) {
						HashMap<String, Integer> thisMap = ProvinceToNumMap.get(province);
						Set<String> typeKeys = thisMap.keySet();
						for(String strTwo:typeKeys) {		
							if(strTwo.equals(type)) {
								previousNum = thisMap.get(type);
							}
						}
					}
					}
				
				return previousNum;
	}
	
	
	
	/*
	 *函数功能：增加感染患者
	 *输入参数：
	 *输出参数：
	 **/
	public void addInfectionPatients(String lineInformation) {
		
		
		//先将每一行的字符串分隔成字符串数组
		String[] linePart = lineInformation.split(" ");
		String province = linePart[0];				
		//新增感染患者的数量
		int num;
		
		//去除数字后面的“人”，取出单独的数字
		num = Integer.valueOf(linePart[3].replaceAll("人",""));
		
		int previousNum,countryPreviousNum,currentNum,countryCurrentNum;
		
		previousNum = searchProvinceToTypeNum(province,"感染患者");	
		countryPreviousNum = searchProvinceToTypeNum("全国","感染患者");
	
		
		currentNum = num+previousNum;
		countryCurrentNum = num+countryPreviousNum;
		
	    ProvinceToNumMap.get(province).replace("感染患者",currentNum);
	    ProvinceToNumMap.get("全国").replace("感染患者",countryCurrentNum);
	    
	    
	    
	}
	
	
	/*
	 *函数功能：增加疑似患者
	 *输入参数：
	 *输出参数：
	 **/
     public void addSuspectedPatients(String lineInformation) {
 		//先将每一行的字符串分隔成字符串数组
 		String[] linePart = lineInformation.split(" ");
 		String province = linePart[0];
 				
 		//新增感染患者的数量
 		int num;
 		
 		//去除数字后面的“人”，取出单独的数字
 		num = Integer.valueOf(linePart[3].replaceAll("人",""));
 		
 		int previousNum,countryPreviousNum,currentNum,countryCurrentNum;
 		
 		previousNum = searchProvinceToTypeNum(province,"疑似患者");
 		countryPreviousNum = searchProvinceToTypeNum("全国","疑似患者");
 			
 		currentNum = num + previousNum;
 		countryCurrentNum = num+countryPreviousNum;
 		
		
 	    ProvinceToNumMap.get(province).replace("疑似患者",currentNum);
 	    ProvinceToNumMap.get("全国").replace("疑似患者",countryCurrentNum);
		}
		
		
		
	
     /*
 	 *函数功能：感染患者流动
 	 *输入参数：
 	 *输出参数：
 	 **/
      public void infectionPatientsMove(String lineInformation) {
 		
 		//先将每一行的字符串分隔成字符串数组
 		String[] linePart = lineInformation.split(" ");
 		
 		String flowOutProvince = linePart[0];
 		String flowInProvince = linePart[3];
 		
 		
 		//流动患者数量
 		int flowNum;
 		
 		//去除数字后面的“人”，取出单独的数字
 		flowNum = Integer.valueOf(linePart[4].replaceAll("人",""));
 		
 		int flowOutPreviousNum,flowInPreviousNum,flowOutCurrentNum,flowInCurrentNum;
 		
 		flowOutPreviousNum = searchProvinceToTypeNum(flowOutProvince,"感染患者");
 		flowInPreviousNum = searchProvinceToTypeNum(flowInProvince,"感染患者");
 		
 		flowOutCurrentNum = flowOutPreviousNum-flowNum;
 		flowInCurrentNum = flowInPreviousNum+flowNum;
 		
 	    ProvinceToNumMap.get(flowOutProvince).replace("感染患者",flowOutCurrentNum);
 	   ProvinceToNumMap.get(flowInProvince).replace("感染患者",flowInCurrentNum);
 	}
 	
    

	/*
   	 *函数功能：疑似患者流动
   	 *输入参数：
   	 *输出参数：
   	 **/
     public void suspectedPatientsMove(String lineInformation) {
   		
  		//先将每一行的字符串分隔成字符串数组
  		String[] linePart = lineInformation.split(" ");
  		String flowOutProvince = linePart[0];
  		String flowInProvince = linePart[3];
  		
  		//流动疑似患者数量
  		int flowNum;
  		
  		//去除数字后面的“人”，取出单独的数字
  		flowNum = Integer.valueOf(linePart[4].replaceAll("人",""));
  		
  		int flowOutPreviousNum,flowInPreviousNum,flowOutCurrentNum,flowInCurrentNum;
  		
  		flowOutPreviousNum = searchProvinceToTypeNum(flowOutProvince,"疑似患者");
  		flowInPreviousNum = searchProvinceToTypeNum(flowInProvince,"疑似患者");
  		
  		flowOutCurrentNum = flowOutPreviousNum-flowNum;
  		flowInCurrentNum = flowInPreviousNum+flowNum;
  		
  		
  	    ProvinceToNumMap.get(flowOutProvince).replace("疑似患者",flowOutCurrentNum);
  	   ProvinceToNumMap.get(flowInProvince).replace("疑似患者",flowInCurrentNum);
   	}
   	

        /*
        *函数功能：统计死亡人数
       	 *输入参数：
       	 *输出参数：
       	 **/
     public void addDeadPatients(String lineInformation) {
    	//先将每一行的字符串分隔成字符串数组
  		String[] linePart = lineInformation.split(" ");
  		String province = linePart[0];
  				
  		//新增感染患者的数量
  		int num;
  		
  		//去除数字后面的“人”，取出单独的数字
  		num = Integer.valueOf(linePart[2].replaceAll("人",""));
  		
  		int previousNum,countryPreviousNum,currentNum,countryCurrentNum;
  		int previousInfectionNum,countryPreviousInfectionNum,currentInfectionNum,countryCurrentInfectionNum;
  		
  		previousNum = searchProvinceToTypeNum(province,"死亡患者");
  		previousInfectionNum = searchProvinceToTypeNum(province,"感染患者");
  		countryPreviousNum = searchProvinceToTypeNum("全国","死亡");
  		countryPreviousInfectionNum = searchProvinceToTypeNum("全国","感染患者");
			
  		currentNum = num + previousNum;
  		countryCurrentNum = num+countryPreviousNum;
  		currentInfectionNum = previousInfectionNum-num;
   		countryCurrentInfectionNum = countryPreviousInfectionNum-num;
  		
  	    ProvinceToNumMap.get(province).replace("死亡",currentNum);
  	    ProvinceToNumMap.get("全国").replace("死亡",countryCurrentNum);
  	  ProvinceToNumMap.get(province).replace("感染患者",currentInfectionNum);
	    ProvinceToNumMap.get("全国").replace("感染患者",countryCurrentInfectionNum);
       	}
       	
     /*函数功能：统计治愈人数
          *输入参数：
         *输出参数：
          **/
     public void addCurePatients(String lineInformation) {
    	//先将每一行的字符串分隔成字符串数组
   		String[] linePart = lineInformation.split(" ");
   		String province = linePart[0];
   				
   		//新增治愈患者的数量
   		int num;
   		
   		//去除数字后面的“人”，取出单独的数字
   		num = Integer.valueOf(linePart[2].replaceAll("人",""));
   		
   		int previousNum,countryPreviousNum,currentNum,countryCurrentNum;
   		int previousInfectionNum,countryPreviousInfectionNum,currentInfectionNum,countryCurrentInfectionNum;
   		
   		previousNum = searchProvinceToTypeNum(province,"治愈");
   		previousInfectionNum = searchProvinceToTypeNum(province,"感染患者");
   		
   		countryPreviousNum = searchProvinceToTypeNum("全国","治愈");
   		countryPreviousInfectionNum = searchProvinceToTypeNum("全国","感染患者");
   			
   		currentNum = num + previousNum;
   		countryCurrentNum = num+countryPreviousNum;
   		currentInfectionNum = previousInfectionNum-num;
   		countryCurrentInfectionNum = countryPreviousInfectionNum-num;
   		
   	    ProvinceToNumMap.get(province).replace("治愈",currentNum);
   	    ProvinceToNumMap.get("全国").replace("治愈",countryCurrentNum);
   	 ProvinceToNumMap.get(province).replace("感染患者",currentInfectionNum);
	    ProvinceToNumMap.get("全国").replace("感染患者",countryCurrentInfectionNum);
           	}
           	
                
     /*函数功能：疑似患者确认感染
               	 *输入参数：
               	 *输出参数：
               	 **/
      public void suspectedToInfection(String lineInformation) {
               		
               		//先将每一行的字符串分隔成字符串数组
               		String[] linePart = lineInformation.split(" ");
               		String province = linePart[0];
               		
               		//疑似患者确认感染数量
               		int num;
               		
               		//去除数字后面的“人”，取出单独的数字
               		num = Integer.valueOf(linePart[3].replaceAll("人",""));
               		
               		int previousSuspectedNum,previousInfectionNum;
               		int currentSuspectedNum,currentInfectionNum;
               		int countryPreviousSuspectedNum,countryPreviousInfectionNum;
               		int countryCurrentSuspectedNum,countryCurrentInfectionNum;
               		
               		previousSuspectedNum = searchProvinceToTypeNum(province,"疑似患者");
               		previousInfectionNum = searchProvinceToTypeNum(province,"感染患者");
               		
               		countryPreviousSuspectedNum = searchProvinceToTypeNum("全国","疑似患者");
               		countryPreviousInfectionNum = searchProvinceToTypeNum("全国","感染患者");		
               	
               		currentSuspectedNum = previousSuspectedNum-num;
               		currentInfectionNum = previousInfectionNum+num;
               		
               		countryCurrentSuspectedNum = countryPreviousSuspectedNum-num;
               		countryCurrentInfectionNum = countryPreviousInfectionNum+num;
               		
           
               	    ProvinceToNumMap.get(province).replace("疑似患者",currentSuspectedNum);
               	 ProvinceToNumMap.get(province).replace("感染患者",currentInfectionNum);
               	 ProvinceToNumMap.get("全国").replace("疑似患者",countryCurrentSuspectedNum);
               	 ProvinceToNumMap.get("全国").replace("感染患者",countryCurrentInfectionNum);
               	}
                    
     
      /*函数功能：排除疑似感染患者
    	 *输入参数：
    	 *输出参数：
    	 **/
         public void suspectedToNormal(String lineInformation) {

        		//先将每一行的字符串分隔成字符串数组
        		String[] linePart = lineInformation.split(" ");
        		String province = linePart[0];
        	
        		//疑似患者确认感染数量
        		int num;
        		
        		//去除数字后面的“人”，取出单独的数字
        		num = Integer.valueOf(linePart[3].replaceAll("人",""));
        		
        		int previousSuspectedNum,currentSuspectedNum;
        	
        		int countryPreviousSuspectedNum,countryCurrentSuspectedNum;
        		
        		previousSuspectedNum = searchProvinceToTypeNum(province,"疑似患者");
        		countryPreviousSuspectedNum = searchProvinceToTypeNum("全国","疑似患者");
        		
        		currentSuspectedNum = previousSuspectedNum-num;
        		countryCurrentSuspectedNum = countryPreviousSuspectedNum-num;
        				
        	    ProvinceToNumMap.get(province).replace("疑似患者",currentSuspectedNum);
        	 ProvinceToNumMap.get("全国").replace("疑似患者",countryCurrentSuspectedNum);
    	}

                   		
                  	 
                        
                    
	/*
	 *函数功能：输出统计结果到文件中
	 *输入参数：
	 *输出参数：
	 **/

	public void outputData(String path) {
		
		try {
			File file = new File(path);
			if(!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream foStream = new FileOutputStream(file);
			OutputStreamWriter opStream = new OutputStreamWriter(foStream);
			BufferedWriter writer = new BufferedWriter(opStream);
			
			if(typeList.isEmpty()) {
					typeList.add("感染患者");
					typeList.add("疑似患者");
	                typeList.add("治愈");
					typeList.add("死亡");
					
				}
			//若-province包含全国或者命令行不包含-province
		/*	if(isEmptyCommandProvince==true) {
				for(int i=0;i<provinceList.length;i++) {
				Set<String> thisSet = ProvinceToNumMap.keySet();
				for(String strKey:thisSet) {	
					if(strKey.equals(provinceList[i])) {	
						writer.write(strKey+" ");
					HashMap<String,Integer> TypeToNumValue = ProvinceToNumMap.get(strKey);
					for(int j=0;j<typeList.size();j++) {
					Set<String> set = TypeToNumValue.keySet();
					for(String integerKey:set) {
							
							if(integerKey.equals(typeList.get(j))) {			
							switch(typeList.get(j)) {
							case "感染患者":Integer value = TypeToNumValue.get("感染患者");
							writer.write("感染患者"+value+"人"+" ");
							break;
							case "疑似患者":Integer value1 = TypeToNumValue.get("疑似患者");
							writer.write("疑似患者"+value1+"人"+" ");
							break;
							case "治愈":Integer value2 = TypeToNumValue.get("治愈");	
							writer.write("治愈"+value2+"人"+" ");
							break;
							case "死亡":Integer value3 = TypeToNumValue.get("死亡");
							writer.write("死亡"+value3+"人"+" ");
							break;
							}
						}	
							}
						}
					writer.write("\n");
					}
				}
				}
				}
			else {*/
				for(int i=0;i<commandProvinceList.size();i++) {
				Set<String> thisSet = ProvinceToNumMap.keySet();
				for(String strKey:thisSet) {			
						if(strKey.equals(commandProvinceList.get(i))) {
							writer.write(strKey);
							HashMap<String,Integer> TypeToNumValue = ProvinceToNumMap.get(strKey);
							for(int j=0;j<typeList.size();j++) {
						Set<String> set = TypeToNumValue.keySet();
						for(String integerKey:set) {
							
								if(integerKey.equals(typeList.get(j))) {
								switch(typeList.get(j)) {
								case "感染患者":Integer value = TypeToNumValue.get("感染患者");
								writer.write("感染患者"+value+"人"+" ");
								break;
								case "疑似患者":Integer value1 = TypeToNumValue.get("疑似患者");
								writer.write("疑似患者"+value1+"人"+" ");
								break;
								case "治愈":Integer value2 = TypeToNumValue.get("治愈");	
								writer.write("治愈"+value2+"人"+" ");
								break;
								case "死亡":Integer value3 = TypeToNumValue.get("死亡");
								writer.write("死亡"+value3+"人"+" ");
								break;
								}
						}}
							}writer.write("\n");
						}
						
						
					}
				}
		//	} 
			writer.write("// 该文档并非真实数据，仅供测试使用");
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void main(String[] args) throws ParseException, IOException, InterruptedException {
     InfectStatistic infectStatistic = new InfectStatistic();
    
       String[] str3= {"list","-log","D:\\221701136\\log\\","-out",
				"D:\\ListOut1.txt","-date","2020-01-23","-province","全国","福建","浙江","-type","cure","dead","ip"};
       String[] str1= {"list","-log","D:\\221701136\\log\\","-out",
				"D:\\ListOut1.txt","-date","2020-01-22"};
       String[] str2= {"list","-log","D:\\221701136\\log\\","-out",
				"D:\\ListOut1.txt","-date","2020-01-22","-province","福建","河北"};
       infectStatistic.analyseCommandLine(str2);
      // System.out.println("你好"+"111");
       System.out.println(inputAddress);
       System.out.println(outputAddress);
       
     // System.out.println(commandDate);
       infectStatistic.searchFile(inputAddress);
       infectStatistic.outputData(outputAddress);
       
     //  String lineTypeOne = "(\\S+) 新增 感染患者 (\\d+)人";
     //  System.out.println(Pattern.matches(lineTypeOne, "湖南 新增 感染患者 5人"));
    //  System.out.println(commandProvinceList.size());
      
    /*   for(int i=0;i<commandProvinceList.size();i++) {
       System.out.println(commandProvinceList.get(i));
       }
       
       for(int i=0;i<typeList.size();i++) {
           System.out.println(typeList.get(i));
           }
           */
	}
 
   
}
