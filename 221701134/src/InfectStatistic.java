import java.util.ArrayList;
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
    		
    		//初始化统计类
    		InfectStatistic inf = new InfectStatistic(args);
    		inf.parse();
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
	 private ArrayList<String> provinceList;
	 /** 省份数据映射表 */
	 private Hashtable<String, Province> provinceHashtable;
	 /** 是否读取全部日志文件 */
	 private boolean isReadAll;
	 /** 是否显示全国省份数据 */
	 private boolean isShowAllProvince;
	 /** 是否显示省份全部数据 */
	 private boolean isShowAllData;
     
	 public InfectStatistic(String[] args) {
		    date = new String();
			logPath = new String();
			outputPath = new String();
			typeList = new ArrayList<String>();
			provinceList = new ArrayList<String>();
			provinceHashtable = new Hashtable<String, Province>();
			this.args = args;
			isReadAll = true;
			isShowAllProvince = true;
			isShowAllData = true;
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
	 
	 /** 处理“-type”指令 */
	 private int executeTypeCmd(int index) {
		 for (int i = 0; index < args.length && i < Constants.NUM_TYPE; i++) {
			 String type = args[index];
			 if (type.equals(Constants.TYPE_IP) || type.equals(Constants.TYPE_SP)
					 || type.equals(Constants.TYPE_CURE) || type.equals(Constants.TYPE_DEAD)) {
				 typeList.add(type);
				 index++;
			 }
			 else {//后续无类型参数
				 return index;
			 }
		}//到列表尾或者四个类型都加入完成
		 return index;    		 
	}
	 
	 /** 处理“-province”指令 */
	 private int executeProvinceCmd(int index) {
		for (int i = 0; index < args.length && i < Constants.NUM_PROVINCE; i++) {
			String temp = args[index];
			if (temp.equals(Constants.CMD_DATE) || temp.equals(Constants.CMD_LOG)
					|| temp.equals(Constants.CMD_OUT) || temp.equals(Constants.CMD_TYPE)) {
				return index;
			}
			else {//仍有省份参数
				provinceList.add(args[index]);
				index++;
			}			
		}//到列表尾或者全部省份都加入完成
		return index;
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
		public void increaseIp(long changeNum) {
			ip += changeNum;
		}
		
		/** 减少感染患者数量 */
		public void decreaseIp(long changeNum) {
			ip -= changeNum;
		}
		
		/** 增加疑似患者数量 */
		public void increaseSp(long changeNum) {
			sp += changeNum;
		}
		
		/** 减少疑似患者数量 */
		public void decreaseSp(long changeNum) {
			sp -= changeNum;
		}
		
		/** 增加治愈患者数量 */
		public void increaseCure(long changeNum) {
			cure += changeNum;
		}		
		
		/** 增加死亡患者数量 */
		public void increaseDead(long changeNum) {
			dead += changeNum;
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
    }
         
}
