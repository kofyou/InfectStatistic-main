import java.util.ArrayList;

/**
 * InfectStatistic
 * TODO
 *
 * @author VisionWong
 * @version 0.1
 * @since 2020/2/17
 */
class InfectStatistic {
				
    public static void main(String[] args) {
    	
        //创建命令行参数解析器 启动程序
    	new InfectStatistic().getArgsParser().parse(args);
    }
       
    /**
     * description：常量类，储存所有全局常量
     * @author VisionWong
     */
    public static class Constants{
    	private static final String NAME_NATION = "全国";
        private static final String CMD_LOG = "log";
        private static final String CMD_OUT = "out";
        private static final String CMD_DATE = "date";
        private static final String CMD_TYPE = "type";
        private static final String CMD_PROVINCE = "province";
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
				case "ip":
					res += "感染患者" + ip + "人 ";
					break;
				case "sp":
					res += "疑似患者" + sp + "人 ";
					break;
				case "cure":
					res += "治愈" + cure + "人 ";
					break;
				case "dead":
					res += "死亡" + dead + "人 ";
					break;
				default:
					break;
				}
			}
			return res;
		}
    }
     
     /**
      * description：命令函参数解析类，负责解析和执行命令行参数数组
      * @author VisionWong
      */
     public class ArgsParser{
    	 
    	 private String date;
    	 private String logPath;
    	 private String outputPath;
    	 private ArrayList<String> typeList;
    	 private ArrayList<String> provinceList;
    	 
    	 
    	 public ArgsParser() {
			date = "";
			logPath = "";
			outputPath = "";
			typeList = new ArrayList<String>();
			provinceList = new ArrayList<String>();
		}
    	 
    	 /**
    	  * description：解析传入的命令行参数列表并执行
    	  * @param args  命令行参数数组
    	  */
    	 public void parse(String[] args) {
    		 
    	 }
    	       	
    	 
     }
     
     /** 获取命令行参数解析器 */
     private ArgsParser getArgsParser() {
		 return new ArgsParser();
	 }
}
