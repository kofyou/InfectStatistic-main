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


class InfectStatistic {
	
	private static String commandDate;
	
	//获取当前的系统时间并格式化输出
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static Date date1 = new Date(System.currentTimeMillis());
    static String currentDate = dateFormat.format(date1);
	
	//构造一个双层嵌套的哈希表
	private HashMap<String,HashMap<String,Integer>> ProvinceToNumMap;
	                              
	
	
    public InfectStatistic() {
    	ProvinceToNumMap 
        = new HashMap<String,HashMap<String,Integer>>();
    	HashMap<String,Integer> TypeToNumMap = new HashMap<String,Integer>();
    	//初始化TypeToNum哈希表
    	TypeToNumMap.put("感染患者",0);
        TypeToNumMap.put("疑似患者",0);
        TypeToNumMap.put("治愈",0);
        TypeToNumMap.put("死亡",0);
         String provinceList[] = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南",
    		"黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海",
    		"四川","天津","西藏","新疆","云南","浙江"};
          for(int i=0;i<provinceList.length;i++) {
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
		
		if(!args[0].equals("list")) {
			System.out.println("命令行的格式有误");
		}
		
		for(int commandOrder=1;commandOrder<args.length;commandOrder++) {
				if(args[commandOrder].equals("-data")) {
				//commandDate = changeToValidDate(args[++commandOrder]);
					commandDate = args[++commandOrder];
				}
				if(args[commandOrder].equals("-log")) {
					inputAddress = args[++commandOrder];
					if(!isValidAddress(inputAddress)) {
						System.out.println("log路径错误！");
						return;
					}
				}
				if(args[commandOrder].equals("-out")) {
					outputAddress = args[++commandOrder];
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
					while(!province.startsWith("-")&&commandOrder<args.length-1) {
						commandProvinceList.add(province);
						province = args[commandOrder++];
					}
				}
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
    public static boolean isValidAddress(String address) {
    	//用正则表达式判断输入的路径是否正确
    	if (address.matches("^[A-z]:\\\\(.+?\\\\)*$")) {
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
	public void searchFile(String inputAddress) throws ParseException, IOException {
	File file = new File(inputAddress);
	String fileName;
	
	//获取inputAddress路径下的所有文件和文件目录
	File[] tempList = file.listFiles();
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
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
		if(fileDay.before(commandDay)) {
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
		try {
	        BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(address)),"UTF-8"));
	        String lineTxt = null;
	                
	        while ((lineTxt = bfr.readLine()) != null) { //按行读取文本内容
	            if(!lineTxt.startsWith("//")) //遇到“//”不读取
	               handleInformation(lineTxt);
	        }
	        bfr.close();
	    } catch (Exception e) {
	        e.printStackTrace();
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
		//获取省份对应下的感染患者数量
		int previousNum = 0;
				Set<String> thisSet = ProvinceToNumMap.keySet();
				for(String str:thisSet) {
					if(str.equals(province)) {
						HashMap<String, Integer> thisMap = ProvinceToNumMap.get(str);
						Set<String> typeKeys = thisMap.keySet();
						for(String strTwo:typeKeys) {
							if(strTwo.equals(type)) {
								previousNum = thisMap.get(strTwo);
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
			
		currentNum = num + previousNum;
		countryCurrentNum = num+countryPreviousNum;
		
	    ProvinceToNumMap.get(province).replace(linePart[2],currentNum);
	    ProvinceToNumMap.get("全国").replace(linePart[2],countryCurrentNum);
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
 		
 	    ProvinceToNumMap.get(province).replace(linePart[2],currentNum);
 	    ProvinceToNumMap.get("全国").replace(linePart[2],countryCurrentNum);
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
 		
 	    ProvinceToNumMap.get(flowOutProvince).replace(linePart[1],flowOutCurrentNum);
 	   ProvinceToNumMap.get(flowInProvince).replace(linePart[1],flowInCurrentNum);
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
  		
  		//流动患者数量
  		int flowNum;
  		
  		//去除数字后面的“人”，取出单独的数字
  		flowNum = Integer.valueOf(linePart[4].replaceAll("人",""));
  		
  		int flowOutPreviousNum,flowInPreviousNum,flowOutCurrentNum,flowInCurrentNum;
  		
  		flowOutPreviousNum = searchProvinceToTypeNum(flowOutProvince,"疑似患者");
  		flowInPreviousNum = searchProvinceToTypeNum(flowInProvince,"疑似患者");
  		
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
     public void addDeadPatients(String lineInformation) {
    	//先将每一行的字符串分隔成字符串数组
  		String[] linePart = lineInformation.split(" ");
  		String province = linePart[0];
  				
  		//新增感染患者的数量
  		int num;
  		
  		//去除数字后面的“人”，取出单独的数字
  		num = Integer.valueOf(linePart[2].replaceAll("人",""));
  		
  		int previousNum,countryPreviousNum,currentNum,countryCurrentNum;
  		
  		previousNum = searchProvinceToTypeNum(province,"死亡患者");
  		
  		countryPreviousNum = searchProvinceToTypeNum("全国","死亡");
  			
  		currentNum = num + previousNum;
  		countryCurrentNum = num+countryPreviousNum;
  		
  	    ProvinceToNumMap.get(province).replace(linePart[2],currentNum);
  	    ProvinceToNumMap.get("全国").replace(linePart[2],countryCurrentNum);
       	}
       	
     /*函数功能：统计治愈人数
          *输入参数：
         *输出参数：
          **/
     public void addCurePatients(String lineInformation) {
    	//先将每一行的字符串分隔成字符串数组
   		String[] linePart = lineInformation.split(" ");
   		String province = linePart[0];
   				
   		//新增感染患者的数量
   		int num;
   		
   		//去除数字后面的“人”，取出单独的数字
   		num = Integer.valueOf(linePart[2].replaceAll("人",""));
   		
   		int previousNum,countryPreviousNum,currentNum,countryCurrentNum;
   		
   		previousNum = searchProvinceToTypeNum(province,"治愈");
   		
   		countryPreviousNum = searchProvinceToTypeNum("全国","治愈");
   			
   		currentNum = num + previousNum;
   		countryCurrentNum = num+countryPreviousNum;
   		
   	    ProvinceToNumMap.get(province).replace(linePart[2],currentNum);
   	    ProvinceToNumMap.get("全国").replace(linePart[2],countryCurrentNum);
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
               	 ProvinceToNumMap.get("全国").replace("感染患者",countryCurrentSuspectedNum);
               	 ProvinceToNumMap.get("全国").replace("疑似患者",countryCurrentInfectionNum);
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
			
			
			
			
			//若-province包含全国或者命令行不包含-province
			if(commandProvinceList.contains("全国")||commandProvinceList.isEmpty()) {
				if(typeList.isEmpty()) {
					typeList.add("ip");
					typeList.add("sp");
					typeList.add("cure");
					typeList.add("dead");
				}
				
				Set<String> thisSet = ProvinceToNumMap.keySet();
				for(String strKey:thisSet) {	
					HashMap<String,Integer> TypeToNumValue = ProvinceToNumMap.get(strKey);
					Set<String> set = TypeToNumValue.keySet();
					for(String integerKey:set) {
						for(int j=0;j<typeList.size();j++) {	
							if(integerKey.equals(typeList.get(j))) {
							switch(typeList.get(j)) {
							case "ip":Integer value = TypeToNumValue.get(integerKey);
							writer.write(strKey+" "+"感染患者"+" "+value+"\n");
							case "sp":Integer value1 = TypeToNumValue.get(integerKey);
							writer.write(strKey+" "+"疑似患者"+" "+value1+"\n");
							case "cure":Integer value2 = TypeToNumValue.get(integerKey);
							writer.write(strKey+" "+"治愈"+" "+value2+"\n");
							case "dead":Integer value3 = TypeToNumValue.get(integerKey);
							writer.write(strKey+" "+"死亡"+" "+value3+"\n");
							}
						}	
							}
						}
					}
				}
			
			else {
				Set<String> thisSet = ProvinceToNumMap.keySet();
				for(String strKey:thisSet) {		
					for(int i=0;i<commandProvinceList.size();i++) {
						if(strKey.equals(commandProvinceList.get(i))) {
							HashMap<String,Integer> TypeToNumValue = ProvinceToNumMap.get(strKey);
						Set<String> set = TypeToNumValue.keySet();
						for(String integerKey:set) {
							for(int j=0;j<typeList.size();i++) {
								if(typeList.isEmpty()) {
									typeList.add("ip");
									typeList.add("sp");
									typeList.add("cure");
									typeList.add("dead");
								}
								if(integerKey.equals(typeList.get(j))) {
								switch(typeList.get(j)) {
								case "ip":Integer value = TypeToNumValue.get("感染患者");
								writer.write(strKey+" "+"感染患者"+" "+value+"\n");
								case "sp":Integer value1 = TypeToNumValue.get("疑似患者");
								writer.write(strKey+" "+"疑似患者"+" "+value1+"\n");
								case "cure":Integer value2 = TypeToNumValue.get("治愈");
								writer.write(strKey+" "+"治愈"+" "+value2+"\n");
								case "dead":Integer value3 = TypeToNumValue.get("死亡");
								writer.write(strKey+" "+"死亡"+" "+value3+"\n");
								}
						}}
							}
						}
					}
				}
			} 
			writer.write("// 该文档并非真实数据，仅供测试使用");
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws ParseException, IOException {
       InfectStatistic infectStatistic = new InfectStatistic();
       infectStatistic.analyseCommandLine(args);
       infectStatistic.searchFile(inputAddress);
       infectStatistic.outputData(outputAddress);
    }
   
    
}
 