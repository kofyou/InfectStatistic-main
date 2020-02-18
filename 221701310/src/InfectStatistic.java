import java.util.HashMap;
import java.util.Map;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */


class InfectStatistic {
	
	class MyProvince {
		
		private String provinceName;//“全国”，或省、直辖市的名称
		
		private int ip;//确诊患者数量
		private int sp;//疑似患者数量
		private int cure;//治愈患者数量
		private int dead;//死亡患者数量
		
		private boolean isMentioned;//是否在日志中被提到过
		private boolean isNeedPrint;//是否被操作员指明需要列出
		
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
	
    public static void main(String[] args) {
    	InfectStatistic infectStatistic = new InfectStatistic();
    	infectStatistic.init();
    	for(String i:args) {
    		System.out.println(i);
    	}
    }
    
    public void init() {
    	provinceMap.clear();
    	for(String strProvinceName:AllProvinceName) {
    		MyProvince myProvince = new MyProvince(strProvinceName);
    		provinceMap.put(strProvinceName, myProvince);
    	}
    	
    }
}