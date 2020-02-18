/**
 * 
 * @author wzzzq
 *
 */
import java.io.File;

import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.text.SimpleDateFormat;


public class InfectStatistic {
	public String[] args;		//保存命令
	public String logPath;		//保存日志路径
	public String outPath;		//保存输出路径
	public int[] type = {1,2,3,4};		  //标记类型输出及顺序
	public int[] province = new int[35];  //标记输出省份
	public String[] typeStr = {"感染患者","疑似患者","治愈","死亡"};	//保存类型（ip，sp，cure，dead）
	public String[] provinceStr = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃",
			"广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
			"江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
			"四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};	//保存省份
	public LinkedHashMap<String,Integer> ip = new LinkedHashMap<String,Integer>();	//保存各省的感染患者人数
	public LinkedHashMap<String,Integer> sp = new LinkedHashMap<String,Integer>(); 	//保存各省的疑似患者人数
	public LinkedHashMap<String,Integer> cure = new LinkedHashMap<String,Integer>();	//保存各省的治愈人数
	public LinkedHashMap<String,Integer> dead = new LinkedHashMap<String,Integer>();	//保存各省的死亡人数
	 // 定义正则表达式，表达式内的空格不可随意修该，否则会影响读取处理
    String s1 = "\\s*\\S+ 新增 感染患者 \\d+人\\s*";
    String s2 = "\\s*\\S+ 新增 疑似患者 \\d+人\\s*";
    String s3 = "\\s*\\S+ 感染患者 流入 \\S+ \\d+人\\s*";
    String s4 = "\\s*\\S+ 疑似患者 流入 \\S+ \\d+人\\s*";
    String s5 = "\\s*\\S+ 死亡 \\d+人\\s*";
    String s6 = "\\s*\\S+ 治愈 \\d+人\\s*";
    String s7 = "\\s*\\S+ 疑似患者 确诊感染 \\d+人\\s*";
    String s8 = "\\s*\\S+ 排除 疑似患者 \\d+人\\s*";
	//设定日期格式
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	Date d = new Date(System.currentTimeMillis());
	public String date = dateFormat.format(d);
	
	public InfectStatistic() {
		for(int i = 0;i < provinceStr.length;i++) {
			ip.put(provinceStr[i], 0);
			sp.put(provinceStr[i], 0);
			cure.put(provinceStr[i], 0);
			dead.put(provinceStr[i], 0);
		}
	}
	//检验参数函数
	public boolean inspectParameter(String [] argsStr) {
		int j;
		args = argsStr;
		if(!argsStr[0].equals("list")) {
			System.out.println("Command line format error.");
			return false;
		}
		for(j = 1;j < argsStr.length;j++) {
			switch(argsStr[j]) {
				case "-log":
					j = inspectPath(++j);
					if(j == -1) {
						System.out.println("Command line format error.('-log'parameter)");
						return false;
					}
					else{
						logPath = argsStr[j];
						break;
					}
				case "-date":
					j = inspectDate(++j);
					if(j == -1) {
						System.out.println("Command line format error.('-date'parameter)");
						return false;
					}
					else{
						date = args[j] + ".log.txt";
						break;
					}
				case "-out":
					j = inspectPath(++j);
					if(j == -1) {
						System.out.println("Command line format error.('-out'parameter)");
						return false;
					}
					else{
						outPath = argsStr[j];
						break;
					}
				case "-type":
					j = inspectType(++j);
					if(j == -1) {
						System.out.println("Command line format error.('-type'parameter)");
						return false;
					}
					else
						break;
				case "-province":
					j = inspectProvince(++j); 
					if(j == -1) {
						System.out.println("Command line format error.('-province'parameter)");
						return false;
					}
					else  
						break;
				 default:
					 System.out.println("Unknown error.");
					 return false;
			}
			
		}
		return true;
	}
	
	//检验路径
	public int inspectPath(int j) {
		if(j < args.length) {
			if(args[j].matches("^[A-z]:\\\\(.+?\\\\)*$")) 
				return j;
			else
				return -1;
		}
		else
			return -1;	
	}
	
	//检验日期
	public int inspectDate(int j) {
		if(j < args.length) {
			if(isValidDate(args[j])) {
				if(date.compareTo(args[j]) >= 0)
					return j;
				else 
					return -1;
			}
			else
				return -1;
		}
		else
			return -1;
	}
	
	//检验类型
	public int inspectType(int j) {
		if(j < args.length) {
			int k,n = j,h = 0;
			if(j < args.length) {
				for(k = 0;k < 4;j++)
					type[k] = 0;
				k = 1;
				while(j < args.length) {
					switch(args[j]){
						case "ip":
							type[0] = k;
							k++;
							j++;
							break;
						case "sp":
							type[1] = k;
							k++;
							j++;
							break;
						case "cure":
							type[2] = k;
							k++;
							j++;
							break;
						case "dead":
							type[3] = k;
							k++;
							j++;
							break;
						default:
							h = 1;
							break;
					}
					if(h == 1) 
						break;
				}
			}
			if(n == j)
				return -1;
		}
		return (j - 1);
	}
	
	//检验省份
	public int inspectProvince(int j) {
		int k, n = j;

		if(j < args.length){
			province[0] = 0; //取消未指定状态标记
			while(j<args.length) {
				for(k = 0; k < provinceStr.length; k++) {
					if(args[j].equals(provinceStr[k])) { //如果参数找到对应省份
						province[k] = 1; //指定该省份需要输出
						j++;
						break;
					}
				}
			}
		}
		if(n == j) //说明-province后无正确参数
			return -1;
		return (j - 1); //接下来不为province的参数，或越界
	}
	
	
	//判断日期是否合法
	public boolean isValidDate(String dateStr) {
        try {
            dateFormat.setLenient(false);
            dateFormat.parse(dateStr);
            String[] sArray = dateStr.split("/");
            for (String s : sArray) {
                boolean isNum = s.matches("[0-9]+");
                if (!isNum)
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
	
	//获取日志文件
	public void execLog() throws Exception{
		File f = new File(logPath);
		String[] logFiles = f.list();
		int l = logFiles.length;
		List<String> legalFiles = new ArrayList<String>();
		for(int i = 0;i < l;i++) {
			if(logFiles[i].matches("\\\\d{4}-\\\\d{2}-\\\\d{2}\\\\.log\\\\.txt"))
				legalFiles.add(logFiles[i]);
		}
		l = legalFiles.size();
		if(l == 0)
			throw new IllegalException("Error, no legal log file exists in the log directory");
		logFiles = new String[1];
		legalFiles.toArray(logFiles);
		Arrays.sort(logFiles);
		for(int i = 0;i < l;i++) {
		execFile(logPath + "/" + logFiles[i]);
		}
		int ipSum = 0;
		int spSum = 0;
		int cureSum = 0;
		int deadSum = 0;
		for(Integer i : ip.values())
			ipSum += i;
		for(Integer i : sp.values())
			spSum += i;
		for(Integer i : cure.values())
			cureSum += i;
		for(Integer i : dead.values())
			deadSum += i;
	}
	
	//处理日志文件
	public void execFile(String path) throws Exception{
		FileInputStream fs = new FileInputStream(new File(path));
		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		String strLine;
		while((strLine = br.readLine()) != null) {
			if(strLine.matches(s1)) {
				String[] strArr = strLine.split(" ");
				int j;
				int m = Integer.valueOf(strArr[3].replace("人", " "));
				
				
			}
		}
	}
	
	class IllegalException extends Exception{
		private String message;
		public  IllegalException(String tMessage) {
			message =tMessage;
		}
		public String toString() {
			return message;
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length == 0) {
			System.out.print("No parameters entered.");
			return;
		}
		InfectStatistic s = new InfectStatistic();
		if(s.inspectParameter(args)) {
			
		}
		else 
			return;
	}

}
