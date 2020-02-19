import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
	public static String log = "";//指定日志目录位置
	public static String outpath = "";//指定输出位置
	public static String date = "";//指定统计截至日期
	public static List<String> type = new ArrayList<String>();//指定输出的患者类型
	public static List<String> outputprovince = new ArrayList<String>();//指定输出的省份名称 
	public static Hashtable<String, Province> map =new Hashtable<String, InfectStatistic.Province>();//存放省份的哈希表
	public static String[] provincename = {
			"全国",
			"安徽","北京","重庆","福建","甘肃",
			"广东","广西","贵州","海南","河北",
			"河南","黑龙江","湖北","湖南","吉林",
			"江苏","江西","辽宁","内蒙古","宁夏",
			"青海","山东","山西","陕西","上海",
			"四川","天津","西藏","新疆","云南",
			"浙江"};
	
	public static class Province {
		private String name;//省份名称 
		private long ip;//感染患者
		private long sp;//疑似患者
		private long cure;//治愈患者
		private long dead;//死亡患者
		/////////////////初始化相关变量///////////////////
		Province(String name, long ip, long sp, long cure, long dead) {
			this.name = name;
			this.ip = ip;
			this.sp = sp;
			this.cure = cure;
			this.dead = dead;
		}
		////////////////数量变化函数//////////////////////
		public void ipadd(long add) {this.ip += add;}//感染增加
		public void spadd(long add) {this.sp += add;}//疑似增加
		public void cureadd(long add) {this.cure += add;}//治愈增加
		public void deadadd(long add) {this.dead += add;}//死亡增加
		public void ipsub(long sub) {this.ip -= sub;}//感染减少
		public void spsub(long sub) {this.sp -= sub;}//疑似减少
		////////////////获取省份数据的函数////////////////
		public String getname() {return name;}
		public long getip() {return ip;}
		public long getsp() {return sp;}
		public long getcure() {return cure;}
		public long getdead() {return dead;}
	}
	
	public static void inItHashMap() {//初始化省份哈希表
		type.add("true");
		outputprovince.add("ture");
		for (int i = 0; i<provincename.length; i++) {
			map.put(provincename[i], new Province(provincename[i], 0, 0, 0, 0));
		}
	}
	
	public static void getLogFile(String log) {//获取指定目录下的文件
		File logfile = new File(log);
		List<String> fileprovince = new ArrayList<String>();//指定省份涉及的省份
		if (logfile.isDirectory()) {
			String list[] = logfile.list();//获取文件列表
			if (!date.equals("") && !lastDateCheck(list)) {//如果date有参数，且超过最晚日期
				System.out.println("超出最晚日期");
				return ;
			} else {
				for (int i=0; i<list.length; i++) {
					File file = new File(log + "/" +list[i]);//合成目标文件路径
					if (file.isFile() && checkDate(file)) {//判断指定目录的文件下是否为标准文件，且日期是否不大于指定日期
						try {
							FileReader fr = new FileReader(file);
							BufferedReader br = new BufferedReader(fr);
							String str = null;
							while((str = br.readLine())!=null){
								System.out.println(str);
								String[] arr = str.split(" ");
								if (arr[0].equals("//"))
									break;
								if (outputprovince.size() == 1) {//如果没有outputprovince参数则获取日志中涉及的省作参数
									fileprovince.add(arr[0]);
								}
								if (arr.length == 5) {//湖北 感染患者 流入 福建 2人//湖北 疑似患者 流入 福建 5人
									if (outputprovince.size() == 1) {//如果没有outputprovince参数则获取日志中涉及的省作参数
										fileprovince.add(arr[3]);
									}
									switch (arr[1]) {
									case "感染患者" :
										map.get(arr[3]).ipadd(getLongFromStr(arr[4]));//流入省增加
										map.get(arr[0]).ipsub(getLongFromStr(arr[4]));//流出省减少
										break;
									case "疑似患者" :
										map.get(arr[3]).spadd(getLongFromStr(arr[4]));//流入省增加
										map.get(arr[0]).spsub(getLongFromStr(arr[4]));//流出省减少
										break;
									}
								} else if (arr.length == 4) {//福建 新增 感染患者 2人//福建 新增 疑似患者 5人//湖北 排除 疑似患者 2人//湖北 疑似患者 确诊感染 3人
									switch (arr[1]) {
									case "新增" :
										if (arr[2].equals("感染患者")) {
											map.get(arr[0]).ipadd(getLongFromStr(arr[3]));//感染患者增加
										} else {
											map.get(arr[0]).spadd(getLongFromStr(arr[3]));//疑似患者增加
										}
										break;
									case "排除" :
										map.get(arr[0]).spsub(getLongFromStr(arr[3]));//疑似患者减少
										break;
									case "疑似患者" :
										map.get(arr[0]).spsub(getLongFromStr(arr[3]));//疑似患者减少
										map.get(arr[0]).ipadd(getLongFromStr(arr[3]));//感染患者增加
										break;
									}
								} else if (arr.length == 3) {//湖北 死亡 1人//湖北 治愈 2人		
									switch (arr[1]) {
									case "治愈" :
										map.get(arr[0]).ipsub(getLongFromStr(arr[2]));//感染患者减少
										map.get(arr[0]).cureadd(getLongFromStr(arr[2]));//治愈患者增加
										break;
									case "死亡" :
										map.get(arr[0]).ipsub(getLongFromStr(arr[2]));//感染患者减少
										map.get(arr[0]).deadadd(getLongFromStr(arr[2]));//死亡患者增加
										break;
									}
								}
							}
							br.close();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
	                } else { System.out.println(log + "/" +list[i] + "不为标准文件。"); }
				}
				if (outputprovince.size() == 1) 
	    			outputprovince.add("全国");
				for (int i = 0; i<fileprovince.size(); i++) {
					int n = 0;
					for (n = 0; n<outputprovince.size(); n++) {
						if (fileprovince.get(i).equals(outputprovince.get(n))) {
							break;
						}
					}
					if (n == outputprovince.size()) {
						outputprovince.add(fileprovince.get(i));	
					}
					else continue;
				}
				//////////////////////////计算全国情况/////////////////////////////
				for (int i = 0; i<provincename.length; i++) {
					map.get(provincename[0]).ipadd(map.get(provincename[i]).getip());
					map.get(provincename[0]).spadd(map.get(provincename[i]).getsp());
					map.get(provincename[0]).cureadd(map.get(provincename[i]).getcure());
					map.get(provincename[0]).deadadd(map.get(provincename[i]).getdead());
				}
			}
		} else { System.out.println("输入的log地址不为目录。"); }
	}
	
	public static String getFilePreName(String filepath) {//获取文件前缀名
		String temp[] = filepath.split("\\\\");
        String filename = temp[temp.length-1]; //包括后缀名
        String fileprename = filename.substring(0,filename.indexOf(".")); //获取前缀名
        return fileprename;
	}
	
	public static boolean lastDateCheck(String[] list) {//检测命令行参数日期是否超过最晚日期
		int num = 0;
		for (int i = 0; i<list.length; i++) {
			String filedate = getFilePreName(list[i]);
			if (date.compareTo(filedate) >= 1)
				num++;
		}
		if (num == list.length)
			return false;
		else return true;
	}
	
	public static boolean checkDate(File file) {
		if (date.equals("")) {
			return true;
		}
		else {
			String logfilename = file.getName();
		    String filedate = getFilePreName(logfilename);
		    if (filedate.compareTo(date) >= 1)
		    	return false;
	    	else return true;
		}
	}
	
	public static long getLongFromStr(String str) {//从字符串中获取long类型
		String num = "";
		str = str.trim();
		for (int i = 0; i<str.length(); i++) {
			if (str.charAt(i)>=48 && str.charAt(i)<=57)
				num += str.charAt(i);
		}
		return Long.parseLong(num);
	}
	
	public static boolean checkprovince(String str) {
		for (int i = 0; i<provincename.length; i++) {
			if (provincename[i].equals(str))
				return true;
		}
		return false;
	}
	
	public static void readArgs(String[] args) {//读取命令行参数
		for (int i = 1; i<args.length; i++) {
			switch (args[i]) {
			case "-log":
				log = args[i+1];
				i++;//跳过log后跟的参数 
				break;//获取日志目录路径
			case "-out":
				outpath = args[i+1];//获取输出路径
				i++;//跳过out后面的参数
				break;
			case "-date"://获取截止日期
				date = args[i+1];
				i++;//跳过date后面跟的参数
				break;
			case "-type"://获取相关输出类型
				for (int j = i+1; j<i+1+4 && j<args.length; j++) {
//					System.out.println("1");
					switch (args[j]) {
					case "ip":
						type.add("ip"); break;
					case "sp":
						type.add("sp"); break;
					case "cure":
						type.add("cure"); break;
					case "dead":
						type.add("dead"); break;
					}//使用type数组之后记得要初始化
				}
				break;
			case "-province"://获取输出省份
				for (int j = i+1; j<args.length && j<args.length; j++) {
					if (checkprovince(args[j]))
						outputprovince.add(args[j]);
					else break;
				}
				break;
			}
		}
//		System.out.println("log = " + log);
//		System.out.println("outpath = " + outpath);
//		System.out.println("date = " + date);
		return;
	}
	
	public static void outPut() {
//		for (int i = 0; i<outputprovince.size(); i++)
//			System.out.println("output开始：" + outputprovince.get(i));
		String str = new String("");
		if (type.size() == 1) {
			type.add("ip");
			type.add("sp");
			type.add("cure");
			type.add("dead");
		}
		try {
			for (int i = 1; i<outputprovince.size(); i++) {
			str += map.get(outputprovince.get(i)).getname();
//			System.out.println("name");
			for (int n = 0; n<type.size(); n++) {
				switch (type.get(n)) {
			    case "ip":
//			    	System.out.println("ip");
		    		str += " 感染患者" + map.get(outputprovince.get(i)).getip() + "人"; break;
		    	case "sp":
//			    	System.out.println("sp");
			    	str += " 疑似患者" + map.get(outputprovince.get(i)).getsp() + "人"; break;
			    case "cure":
//			    	System.out.println("cure");
			    	str += " 治愈" + map.get(outputprovince.get(i)).getcure() + "人"; break;
		    	case "dead":
//			    	System.out.println("dead");
			    	str += " 死亡" + map.get(outputprovince.get(i)).getdead() + "人"; break;
			    }
			}
//			System.out.println(str);
			str += "\r\n";
		    }
		}catch (Exception e) {
			// TODO: handle exception
		}
		try {
			FileOutputStream fos = new FileOutputStream(outpath);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(str);
			bw.newLine();
			bw.close();
			osw.close();
			fos.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(str);
		str = "";
	}
	
	
    public static void main(String[] args) {
    	inItHashMap();
        readArgs(args);
        getLogFile(log);
//    	for (int i=0; i<args.length; i++)
//    		System.out.println("arg[" + i + "] = " + args[i]);
    	outPut();
        return;
    }
}
