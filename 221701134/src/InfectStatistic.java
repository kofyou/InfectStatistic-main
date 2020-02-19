import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;


/**
 * InfectStatistic
 * TODO
 *
 * @author VisionWong
 * @version 0.1
 * @since 2020/2/17
 */
class InfectStatistic {
				
    public static void main(String[] _args) {
    	
    	String[] args = {
    			"list","-date","2020-01-22","-log",
    			"D:\\GithubProjects\\InfectStatistic-main\\example\\log\\",
    			"-out",
    			"D:\\GithubProjects\\InfectStatistic-main\\example\\result\\output.txt",
    			
    	};
    	
    	if(args[0].equalsIgnoreCase("list")) {   		
    		//初始化统计类并启动
    		new InfectStatistic(args).start();
    	}
    	else {
    		System.out.println("命令" + args[0] + "不存在！");
    	}
    }
    
     private String[] args;
	 private String date;
	 private String logPath;
	 private String outputPath;
	 /** 传入的类型参数列表 */
	 private ArrayList<String> typeList;
	 /** 传入的需要显示的省份参数列表 */
	 private ArrayList<String> provinceArgsList;	 
	 /** 所有有数据的省份名字列表 */
	 private ArrayList<String> allProvinceList;
	 /** 省份数据映射表 */
	 private Hashtable<String, Province> provinceHashtable;
	 /** 是否读取全部日志文件 */
	 private boolean isReadAll;
	 /** 是否显示全国省份数据 */
	 private boolean isShowAllProvince;
	 /** 是否显示省份全部数据 */
	 private boolean isShowAllData;
	 /** 程序是否出错提前结束 */
	 private boolean isEnd;
     
	 public InfectStatistic(String[] args) {
		    date = new String();
			logPath = new String();
			outputPath = new String();
			typeList = new ArrayList<String>();
			provinceArgsList = new ArrayList<String>();
			allProvinceList = new ArrayList<String>();
			provinceHashtable = new Hashtable<String, Province>();
			this.args = args;
			isReadAll = true;
			isShowAllProvince = true;
			isShowAllData = true;
			isEnd = false;
	}
	 
	 /**
	  * 启动疫情统计程序
	  */
	 private void start() {
		//解析命令行参数列表
		this.parse();
		
		//读取日志文件
		this.readFiles();
		
		//输出统计结果写入文件
		this.writeFile();		
	}
	 
	 /**
	  * description：解析传入的命令行参数列表并执行相关命令设置
	  * @param args  命令行参数数组
	  */
	 private void parse() {
		 for (int i = 0; args[i] != null; i++) {
			switch (args[i]) {
			case Constants.CMD_DATE:
				isReadAll = false;
				date = args[++i];
				break;
			case Constants.CMD_LOG:
				logPath = args[++i];
				break;
			case Constants.CMD_OUT:
				outputPath = args[++i];
				break;
			case Constants.CMD_TYPE:
				isShowAllData = false;
				i = executeTypeCmd(i + 1) - 1;
				break;
			case Constants.CMD_PROVINCE:
				isShowAllProvince = false;
				i = executeProvinceCmd(i + 1) - 1;
				break;
			default:
				break;
			}
		}
	 }   	 
	 
	 /**
	  * description：处理“-type”指令
	  * @param index 开始扫描的索引位置
	  * @return 返回执行结束的索引位置
	  */
	 private int executeTypeCmd(int index) {
		 for (int i = 0; index < args.length && i < Constants.NUM_TYPE; i++) {
			 String type = args[index];
			 if (type.equals(Constants.TYPE_IP) || type.equals(Constants.TYPE_SP)
					 || type.equals(Constants.TYPE_CURE) || type.equals(Constants.TYPE_DEAD)) {
				 typeList.add(type);
				 index++;
			 }
			 else { //后续无类型参数
				 return index;
			 }
		} //到列表尾或者四个类型都加入完成
		 return index;    		 
	}
	 
	 /**
	  * description：处理“-province”指令
	  * @param index 开始扫描的索引位置
	  * @return 返回执行结束的索引位置
	  */
	 private int executeProvinceCmd(int index) {
		for (int i = 0; index < args.length && i < Constants.NUM_PROVINCE; i++) {
			String temp = args[index];
			if (temp.equals(Constants.CMD_DATE) || temp.equals(Constants.CMD_LOG)
					|| temp.equals(Constants.CMD_OUT) || temp.equals(Constants.CMD_TYPE)) {
				return index;
			}
			else { //仍有省份参数
				provinceArgsList.add(args[index]);
				index++;
			}			
		} //到列表尾或者全部省份都加入完成
		return index;
	}
	 
	 /**
	  * description：读取日志文件并解析处理数据
	  */
	 private void readFiles() {
		 File file = new File(logPath);
		 File[] logFiles = file.listFiles();
		 String logDate = new String();
		 
		 if (logFiles.length == 0) {
			 System.out.println("当前文件夹下没有日志文件！路径：" + logPath);
			 isEnd = true;
			 return;
		 }
		 
		 if (isReadAll == false) {
			//比较输入日期与最新日期
			 String lastestDate = logFiles[logFiles.length - 1].getName();
			 //TODO： debug语句 后续删除
			 System.out.println("当前最新的日期文件名为：" + lastestDate);
			 lastestDate = lastestDate.split(".")[0];
			 if (date.compareTo(lastestDate) > 0) {
				 System.out.println("日期超出范围，当前最新日期为：" + lastestDate);
				 isEnd = true;
				 return;
			 }
		 }
		 
		 //读取日志文件
		 for (int i = 0; i < logFiles.length; i++) {
			logDate = logFiles[i].getName().split(".")[0];
			if (isReadAll || date.compareTo(logDate) >= 0) {
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(logFiles[i]), "UTF-8"));;               
		            String line = new String();          
		                
		            while ((line = br.readLine()) != null){
		                String[] datas = line.split(" ");
		                //读取并处理单行数据
		                executeOneLine(datas);
		            } //单个文件读取完毕         
		            br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		} //文件全部读取完毕				 
	 }
	 
	 /**
	  * description：处理文件读取的单行数据
	  * @param datas  分割好的数据数组
	  */
	 private void executeOneLine(String[] datas) {
		 //忽略注释行
		 if(datas[0].equals("//")) {
			 return;
		 }
		 
		String provinceName = datas[0];
		Province prov = getProvinceByKey(provinceName); 
		
		//根据不同情况进行处理
		switch (datas[1]) {
		case "死亡":
			prov.increaseDead(datas[2]);
			break;
		case "治愈":
			prov.increaseCure(datas[2]);
			break;
		case "新增":
			if (datas[2].equals("感染患者")) {
				prov.increaseIp(datas[3]);
			}
			else { //新增疑似患者
				prov.increaseSp(datas[3]);
			}
			break;
		case "排除": //排除疑似患者
			prov.decreaseSp(datas[3]);
			break;
		case "疑似患者": 
			if (datas[2].equals("确诊感染")) { //疑似患者确诊
				prov.increaseIpBySpConfirmed(datas[3]);
			}
			else { //疑似患者流入他省
				Province prov2 = getProvinceByKey(datas[3]);
				prov.decreaseSp(datas[4]);
				prov2.increaseSp(datas[4]);
			}
			break;
		case "感染患者": //感染患者流入他省
			Province prov2 = getProvinceByKey(datas[3]);
			prov.decreaseIp(datas[4]);
			prov2.increaseIp(datas[4]);
			break;
		default:
			System.out.println("日志格式可能出现错误！解析失败！");
			break;
		}		
	}
	 
	 /**
	  * description：判断省份是否已经缓存，没有则新建省份对象，并加入哈希表中
	  * @param key  键值 即省名
	  * @return
	  */
	 private Province getProvinceByKey(String key) {		 
		 Province prov = null;
		 if (provinceHashtable.containsKey(key) == false) {
				prov = new Province(key);
				provinceHashtable.put(key, prov);
				
				//加入有数据列表
				allProvinceList.add(key);
		}
		else {
			prov = provinceHashtable.get(key);
		}
		return prov;	
	}
	 
	 /**
	  * description：将统计结果写入文件输出
	  */
	 private void writeFile() {
		 //若中途出错则直接结束程序
		 if (isEnd) {
			 return;
		 }
		 
		 try {
			 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "UTF-8"));
			 
				//TODO： 判断是否统计全国数据
				 if (isShowAllProvince == false) {
					 //判断输出列表是否包含全国
					 if (isOuputNationwide()) {
						 //统计出全国数据
						 Province nation = getNationStatResult();
						 
					 }
					 else { //仅输出传入参数的省份列表
						
					}
				 }
				 else {
					 //输出全国数据和所有省份数据
					 Province nation = getNationStatResult();
					 
				}
				 
				 bw.write("// 该文档并非真实数据，仅供测试使用");
				 bw.close();
		} 
		 catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	 /** 是否统计全国数据并输出 */
	 private boolean isOuputNationwide() {
		//判断输出列表是否包含全国
		for (String provName : provinceArgsList) {
			if (provName.equals("全国")) {
				return true;
			}
		} //不包含全国
		return false;
	}
	 
	 /**
	  * 获取全国统计数据对象
	  * @return 包含统计数据的省份对象
	  */
	 private Province getNationStatResult() {	 
		long ip = 0;
		long sp = 0;
		long cure = 0;
		long dead = 0;
		
		//遍历所有有数据的省份列表
		for (String name : allProvinceList) {
			Province prov = provinceHashtable.get(name);
			ip += prov.getIp();
			sp += prov.getSp();
			cure += prov.getCure();
			dead += prov.getDead();
		}
		
		return new Province("全国", ip, sp, cure, dead);
	}
	 
	 /**
	  * description：省份名字排序器 按字母先后顺序排列
	  * @author VisionWong
	  *
	  */
	 public class ProvinceCompartor implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}		
	}
	 
    /**
     * description：常量类，储存所有全局常量
     * @author VisionWong
     */
     public static class Constants{
    	 
    	 private static final int NUM_PROVINCE = 34;
    	 private static final int NUM_TYPE = 4;
    	 
    	 private static final String TYPE_IP = "ip";
    	 private static final String TYPE_SP = "sp";
    	 private static final String TYPE_CURE = "cure";
    	 private static final String TYPE_DEAD = "dead";
    	 
         private static final String CMD_LOG = "-log";
         private static final String CMD_OUT = "-out";
         private static final String CMD_DATE = "-date";
         private static final String CMD_TYPE = "-type";
         private static final String CMD_PROVINCE = "-province";
    }
       
    /**
     * description：储存省份信息，提供修改数据的方法
     * @author VisionWong
     */
     public class Province{
    	
    	/** 省份名字 */
    	private String name;
    	/** 感染患者数量 */
    	private long ip;
    	/** 疑似患者数量 */
    	private long sp;
    	/** 治愈患者数量 */
    	private long cure;
    	/** 死亡患者数量 */
    	private long dead;
    	
    	public Province(String name) {
			this.name = name;
			this.ip = 0;
			this.sp = 0;
			this.cure = 0;
			this.dead = 0;
		}
    	
    	public Province(String name, long ip, long sp, long cure, long dead) {
			this.name = name;
			this.ip = ip;
			this.sp = sp;
			this.cure = cure;
			this.dead = dead;
		}
    	   	
		public String getName() {
			return name;
		}
		
		public long getIp() {
			return ip;
		}
		
		public long getSp() {
			return sp;
		}
		
		public long getCure() {
			return cure;
		}
		
		public long getDead() {
			return dead;
		}		

		/** 增加感染患者数量 */
		public void increaseIp(String changeNumStr) {
			long changeNum = getChangeNum(changeNumStr);
			ip += changeNum;
		}	
		
		/** 减少感染患者数量 */
		public void decreaseIp(String changeNumStr) {
			long changeNum = getChangeNum(changeNumStr);
			ip -= changeNum;
		}
		
		/** 增加疑似患者数量 */
		public void increaseSp(String changeNumStr) {
			long changeNum = getChangeNum(changeNumStr);
			sp += changeNum;
		}
		
		/** 减少疑似患者数量 */
		public void decreaseSp(String changeNumStr) {
			long changeNum = getChangeNum(changeNumStr);
			sp -= changeNum;
		}
		
		/** 增加治愈患者数量 */
		public void increaseCure(String changeNumStr) {
			long changeNum = getChangeNum(changeNumStr);
			cure += changeNum;
			ip -= changeNum;
		}		
		
		/** 增加死亡患者数量 */
		public void increaseDead(String changeNumStr) {
			long changeNum = getChangeNum(changeNumStr);
			dead += changeNum;
			ip -= changeNum;
		}
		
		/** 疑似病例确诊 */
		public void increaseIpBySpConfirmed(String changeNumStr) {
			long changeNum = getChangeNum(changeNumStr);
			ip += changeNum;
			sp -= changeNum;
		}
		
		/** 
		 * description：获取要输出的统计数据
		 * @return 要输出的字符串
		 */
		public String getOuputResult() {
			String res = name + ' ' + "感染患者" + ip + "人" + ' ' + "疑似患者" + sp + "人" + ' ' + "治愈" + cure
                    + "人" + ' ' + "死亡" + dead + "人";
			return res;
		}
		
		/**
		 * description：通过命令行指令参数获取要输出的统计结果
		 * @param types  命令行参数类型数组
		 * @return 要输出的字符串
		 */
		public String getOuputResultByTypes(String[] types) {
			String res = name + "";
			for (int i = 0; i < types.length; i++) {
				switch (types[i]) {
				case Constants.TYPE_IP:
					res += "感染患者" + ip + "人 ";
					break;
				case Constants.TYPE_SP:
					res += "疑似患者" + sp + "人 ";
					break;
				case Constants.TYPE_CURE:
					res += "治愈" + cure + "人 ";
					break;
				case Constants.TYPE_DEAD:
					res += "死亡" + dead + "人 ";
					break;
				default:
					break;
				}
			}
			return res;
		}
		
		/** 将字符串解析为数字 */
		private long getChangeNum(String changeNumStr) {
			changeNumStr = changeNumStr.substring(0, changeNumStr.length() - 1);
			return Long.parseLong(changeNumStr);
		}
    }
         
}
