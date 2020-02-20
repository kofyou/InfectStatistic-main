import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author kuiqc
 * @version 0.1.0
 * @since 2020-02-20
 */


class InfectStatistic {
	
	class MyProvince {
		
		public String provinceName;//“全国”，或省、直辖市的名称
		
		public int ip;//确诊患者数量
		public int sp;//疑似患者数量
		public int cure;//治愈患者数量
		public int dead;//死亡患者数量
		
		public boolean isMentioned;//是否在日志中被提到过
		public boolean isNeedPrint;//是否被操作员指明需要列出
		
		public MyProvince(String provinceName) {
			this.provinceName = provinceName;
			this.ip = 0;
			this.sp = 0;
			this.cure = 0;
			this.dead = 0;
			this.isMentioned = false;
			this.isNeedPrint = false;
			if(provinceName.equals("全国")) {
				this.isMentioned = true;
			}
		}
		
		//<省> 新增 感染患者 n人
		public void ipAdd(int n) {
			this.isMentioned = true;
			this.ip += n;
			provinceMap.get("全国").ip += n;
		}
		
		//<省> 新增 疑似患者 n人
		public void spAdd(int n) {
			this.isMentioned = true;
			this.sp += n;
			provinceMap.get("全国").sp +=n;
		}
		
		//<省1> 感染患者 流入 <省2> n人
		public void ipMove(String strProvinceName, int n) {
			this.isMentioned = true;
			this.ip -= n;
			provinceMap.get(strProvinceName).isMentioned = true;
			provinceMap.get(strProvinceName).ip += n;
		}
		
		//<省1> 疑似患者 流入 <省2> n人
		public void spMove(String strProvinceName, int n) {
			this.isMentioned = true;
			this.sp -= n;
			provinceMap.get(strProvinceName).isMentioned = true;
			provinceMap.get(strProvinceName).sp += n;
		
		}
		
		//<省> 死亡 n人
		public void peopleDead(int n) {
			this.isMentioned = true;
			this.ip -= n;
			this.dead += n;
			provinceMap.get("全国").ip -= n;
			provinceMap.get("全国").dead += n;
		}
		
		//<省> 治愈 n人
		public void peopleCured(int n) {
			this.isMentioned = true;
			this.ip -= n;
			this.cure += n;
			provinceMap.get("全国").ip -= n;
			provinceMap.get("全国").cure += n;
		}
		
		//<省> 疑似患者 确诊感染 n人
		public void spDiagnosed(int n) {
			this.isMentioned = true;
			this.sp -= n;
			this.ip += n;
			provinceMap.get("全国").sp -= n;
			provinceMap.get("全国").ip += n;
		}
		
		//<省> 排除 疑似患者 n人
		public void spExclude(int n) {
			this.isMentioned = true;
			this.sp -= n;
			provinceMap.get("全国").sp -= n;
		}
	}
	
	//建立省份名与省份类实例的HashMap
	HashMap<String, MyProvince>provinceMap = new HashMap<String, MyProvince>();
	
	//省份名数组，按首字母顺序排列，“全国”排最前
	String [] AllProvinceName = {
		    "全国","安徽","北京","重庆","福建","甘肃","广东","广西",
		    "贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林",
		    "江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西",
		    "陕西","上海","四川","天津","西藏","新疆","云南","浙江",
	};
	
	//初始化HashMap，建立对应关系
    public void init() {
    	provinceMap.clear();
    	for(String strProvinceName:AllProvinceName) {
    		MyProvince myProvince = new MyProvince(strProvinceName);
    		provinceMap.put(strProvinceName, myProvince);
    	}
    }
    
    //处理输入的命令
    public void processCmd(String[] args) {
    	if(args.length>1 && args[0].equals("list")) {
    		
    		String logPath = new String("");
    		String outPath = new String("");
    		String date = new String("");
    		
    		//int[] type = new int[] {1,1,1,1};//默认输出四种人群。更新：难以按指定顺序输出。已修改
    		
    		//默认依ip,sp,cure,dead顺序输出四种人群，读取-type参数时重置为空
    		ArrayList<String> type = new ArrayList<String>();
    		type.add("ip");
    		type.add("sp");
    		type.add("cure");
    		type.add("dead");
    		
    		boolean isProvinceSpecified = false;//该项为false，输出所有日志中提到的省份，为true输出指定的省份
    		
    		for(int index = 1;index<args.length;index++) {
    			switch(args[index]) {
    				case "-log":
    					logPath = args[index+1];
    					index++;
    					break;
    				case "-out":
    					outPath = args[index+1];
    					index++;
    					break;
    				case "-date":
    					date=args[index+1];
    					index++;
    					break;
    				case "-type":
    					type.clear();
    					while(index+1<args.length && args[index+1].charAt(0)!='-') {
    						if(args[index+1].equals("ip")||args[index+1].equals("sp")
    								||args[index+1].equals("cure")||args[index+1].equals("dead")) {
    							type.add(args[index+1]);
    						}
    						index++;
    					}
    					break;
    				case "-province":
    					isProvinceSpecified = true;
    					while(index+1<args.length && args[index+1].charAt(0)!='-') {
    						provinceMap.get(args[index+1]).isNeedPrint = true;
    						index++;
    					}
    					break;
    			}
    		}
    		excuteCmd(logPath,outPath,date,type,isProvinceSpecified);
    	}
    }
    
    //执行输入的命令
    public void excuteCmd(String logPath,String outPath,String date,ArrayList<String> type,boolean isProvinceSpecified) {
    	
    	if(logPath.equals("")||outPath.equals("")) {
    		System.out.println("输入、输出路径均不能为空！");
    		System.exit(0);
    	}
    	
    	//读取指定日期以及之前的所有log文件
    	readLogs(logPath,date);
    	
    	try {
    		//带格式输出到指定文件
    		writeLog(outPath,type,isProvinceSpecified);
		} catch(IOException e) {
    		System.out.println("日志写入错误！");
    		System.exit(0);
    	}
    }
	
    //读取指定日期以及之前的所有log文件
    public void readLogs(String logPath,String date) {
    	
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(logPath);
        File[] tempList = file.listFiles();
        
        //isDateAllowed，默认为日期超出范围（false），直到出现不早于date的日期时，置为true
        boolean isDateAllowed = false;
        
        //将所有不晚于date的.log.txt文件的绝对路径添加到files，并判断日期是否超出范围
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
            	
            	//若未指明日期，无需判断，读取所有logs
            	if (date.equals("")) {
            		System.out.println("here!");
            		isDateAllowed = true;
            		files.add(tempList[i].toString());
				} else {//若指明日期，需要判断日期是否合理，选择不晚于该日期的logs
					
                    //fileName，不包含路径、后缀（也就是说只有日期yyyy-mm-dd）
                    String fileName = tempList[i].getName().substring(0,10);
                    
                    //当d1早于d2，dateCompare返回-1，files只记录所有不晚于date的日期
                	if(dateCompare(date,fileName) != -1) {
                		files.add(tempList[i].toString());
                	}
            		if(dateCompare(date, fileName) != 1) {
            			isDateAllowed = true;
            		}
            	}
            }
        }
        
        //日期超出范围提示
        if(!isDateAllowed) {
    		System.out.println("日期超出范围！");
    		System.exit(0);
        }
        
        //逐个读取files中的路径对应的文件
        for(int i = 0;i < files.size();i++) {
        	try{
        		readOneLog(files.get(i));
        	}catch(IOException e) {
        		System.out.println("日志读取错误！");
        		System.exit(0);
        	}
        }
    }
    
    //读取单个文件
    public void readOneLog(String logPathName) throws IOException {
    	
        FileReader fileReader = new FileReader(logPathName);

        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = new String("init");

        while (line!=null){
        	line = bufferedReader.readLine();
        	if(line!=null && !line.startsWith("//")) {//忽略空行和注释
        		
        		//line为当前行
        		String [] wordString = line.split("\\s+");//按空格分割当前行
        		
        		if(wordString[1].equals("新增")) {
        			if(wordString[2].equals("感染患者")) {
        				int num = getPeopleNum(wordString[3]);
        				provinceMap.get(wordString[0]).ipAdd(num);
        			} else if(wordString[2].equals("疑似患者")) {
        				int num = getPeopleNum(wordString[3]);
        				provinceMap.get(wordString[0]).spAdd(num);
        			}
        		} else if(wordString[2].equals("流入")) {
        			if(wordString[1].equals("感染患者")) {
        				int num = getPeopleNum(wordString[4]);
        				provinceMap.get(wordString[0]).ipMove(wordString[3], num);
        			} else if(wordString[1].equals("疑似患者")) {
        				int num = getPeopleNum(wordString[4]);
        				provinceMap.get(wordString[0]).spMove(wordString[3], num);
        			}
        		} else if(wordString[1].equals("死亡")) {
        			int num = getPeopleNum(wordString[2]);
        			provinceMap.get(wordString[0]).peopleDead(num);
        		} else if(wordString[1].equals("治愈")) {
        			int num = getPeopleNum(wordString[2]);
        			provinceMap.get(wordString[0]).peopleCured(num);
        		} else if(wordString[2].equals("确诊感染")) {
        			int num = getPeopleNum(wordString[3]);
        			provinceMap.get(wordString[0]).spDiagnosed(num);
        		} else if(wordString[1].equals("排除")){
        			int num = getPeopleNum(wordString[3]);
        			provinceMap.get(wordString[0]).spExclude(num);
        		}
        	}
        }
        bufferedReader.close();
        fileReader.close();
    }
    
    //带格式输出到指定文件
    public void writeLog(String outPath,ArrayList<String> type,boolean isProvinceSpecified) throws IOException {
    	
    	String outStr = new String();
    	
    	//构建输出字符串
    	//没有"-province"参数时，列出全国的数据，以及日志中涉及到的省的数据
    	if(!isProvinceSpecified) {
        	for(int i = 0;i < AllProvinceName.length;i++) {
        		if(provinceMap.get(AllProvinceName[i]).isMentioned) {
        			outStr += getStrByType(type,provinceMap.get(AllProvinceName[i]));
        		}
        	}
    	}
    	
    	//有"-province"参数时，指定的省必须列出
    	else {
        	for(int i=0;i<AllProvinceName.length;i++) {
        		if(provinceMap.get(AllProvinceName[i]).isNeedPrint) {
        			outStr += getStrByType(type,provinceMap.get(AllProvinceName[i]));
        		}
        	}
    	}
    	
    	//加上注释
    	outStr += "// 该文档并非真实数据，仅供测试使用";
    	
    	File file = new File(outPath);
    	BufferedWriter writer = null;
    	FileOutputStream writerStream = new FileOutputStream(file);
    	writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));

    	StringBuilder strBuild = new StringBuilder(outStr);
    	writer.write(strBuild.toString());
    	
    	writer.flush();
    	writer.close();
    }
    
    //按-type指定的顺序输出curProvince的防疫情况
    public String getStrByType(ArrayList<String> type,MyProvince curProvince) {
    	String str = new String();
    	str += curProvince.provinceName;
    	for(int i = 0;i < type.size();i++) {
    		switch(type.get(i)){
    			case "ip":
    				str += " " + "感染患者" + curProvince.ip + "人";
    				break;
    			case "sp":
    				str += " " + "疑似患者" + curProvince.sp + "人";
    				break;
    			case "cure":
    				str += " " + "治愈" + curProvince.cure + "人";
    				break;
    			case "dead":
    				str += " " + "死亡" + curProvince.dead + "人";
    				break;
    		}
    	}
    	str += "\n";
    	return str;
    }
    
    /*
     * 输入String日期d1，d2，比较两个日期的先后
     * 当d1早于d2，dateCompare返回-1
     * 当d1等于d2，dateCompare返回0
     * 当d1晚于d2，dateCompare返回1
     */
    public int dateCompare(String date1,String date2) {

        int res=date1.compareTo(date2);
        if(res>0)
            return 1;
        else if(res==0)
        	return 0;
        else
        	return -1;

    	/*
    	//将yyyy-mm-dd分解成(int)year,(int)month,(int)day
    	int y1 = Integer.parseInt(date1.substring(0,4));
    	int y2 = Integer.parseInt(date2.substring(0,4));
    	int m1 = Integer.parseInt(date1.substring(5,7));
    	int m2 = Integer.parseInt(date2.substring(5,7));
    	int d1 = Integer.parseInt(date1.substring(8,10));
    	int d2 = Integer.parseInt(date2.substring(8,10));
    	
    	if(y1 > y2) {
    		return 1;
    	}
    	else if(y1 == y2) {
    		if(m1 > m2) {
    			return 1;
    		}
    		else if(m1 == m2) {
    			if(d1 > d2) {
    				return 1;
    			}
    			else if(d1 == d2) {
    				return 0;
    			}
    			else return -1;
    		}
    		else return -1;
    	}
    	else return -1;
    	*/
    }
    
    //输入诸如“24人”的字符串，返回int人数
    public int getPeopleNum(String str) {
    	String strNum = new String();
    	for(int i = 0;i < str.length();i++) {
    		if(str.charAt(i) >= 48 && str.charAt(i) <= 57){
    			strNum += str.charAt(i);
    		}
    	}
    	return Integer.parseInt(strNum);
    }
    
    public static void main(String[] args) {
    	InfectStatistic infectStatistic = new InfectStatistic();
    	infectStatistic.init();
    	infectStatistic.processCmd(args);
    }
}