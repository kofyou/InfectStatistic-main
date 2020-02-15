import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public String TargetPath; //目标路径
    public String OriginPath; //原路径
    public int Var;
    /*
     * 获取当前时间
     * 将默认日期设为当前时间
     */
    static Date date = new Date();
    static SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
    public static String AcqTime = dateFormat.format(date);
    /*
     * 设置省份列表
     * 0代表未列出，1代表列出
     */
    public int [] Area = {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public String[] AreaStr = new String[]{"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南",
    		"吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
    /*
     * 设置患者类型（ip,sp,cure,dead）
     * 0代表无需列出，1代表感染，2代表疑似，3代表治愈，4代表死亡
     * 默认排序为1,2,3,4
     */
    public int [] Patients = {1,2,3,4};
    public String[] PatientsStr = new String[] {"感染","疑似","治愈","死亡"};
    /*
     * 设置全国和各地各个患者类型的人数情况
     * 数组的一维代表地区（包括全国），二维代表患者类型（ip,sp,cure,dead）
     */
    public int [][] TotalNumber = new int [32][4];
    
    /*
     * 解析命令行参数
     */
    class CLA {
    	public String [] Args ; //储存传入参数
    	
    	/*
    	 * CLA重载
    	 */
    	public CLA (String [] ArgsStr) {
    		Args = ArgsStr;
    	}
    	
    	/*
    	 * 判断命令行参数是否有误
    	 */
    	public boolean JudgCLA(){
    		if (Args[0]!="list") {
    			System.out.println("未输入命令");
    			return false;
    		}
    		int i=0;
    		for (i=1;i<Args.length;i++) {
    			if (Args[i]=="-log") {
    				if (getOriPath(++i)==false) {
    					System.out.println("日志目录路径格式错误");
    					return false;
    				}
    			}
    			else if (Args[i]=="-out") {
    				if (getTarPath(++i)==false) {
    					System.out.println("输出文件路径格式错误");
    					return false;
    				}
    			}
    			else if (Args[i]=="-date") {
    				if (getDate(++i)==false) {
    					System.out.println("日期参数错误");
    					return false;
    				}
    			}
    			else if (Args[i]=="-type") {
    				int n=0;
    				for (n=0;n<4;n++) {
    					Patients[n] = 0;
    				}
    				if (getType(++i)==false) {
    					System.out.println("患者类型参数错误");
    					return false;
    				}
    				i=Var;
    			}
    			else if (Args[i]=="-province") {
    				Area[0] = 0;
    				if (getArea(++i)==false) {
    					System.out.println("省份参数错误");
    					return false;
    				}
    				i=Var;
    			}
    			else {
    				System.out.println("无命令参数输入");
    				return false;
    			}
    		}
    		return true;
    	}
    	/*
    	 * 获取输出文件路径
    	 * 判断输出文件路径是否格式正确
    	 */
    	public boolean getTarPath (int i) {
    		if (i<Args.length) {
    			if (Args[i].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$")) {
    				TargetPath = Args[i];
    				return true;
    			}
    			else {
    				return false;
    			}
    		}
    		else{
    			return false;
    		}
    	}
    	/*
    	 * 获取日志目录路径
    	 * 判断日志目录路径是否格式正确
    	 */
    	public boolean getOriPath (int i) {
    		if (i<Args.length) {
    			if (Args[i].matches("^[A-z]:\\\\(.+?\\\\)*$")) {
    				OriginPath = Args[i];
    				return true;
    			}
    			else {
    				return false;
    			}
    		}
    		else{
    			return false;
    		}
    	}
    	/*
    	 * 获取输入的日期
    	 * 判断日期是否超过当前日期
    	 */
    	public boolean getDate (int i) {
    		boolean result = true;
    		if (i<Args.length) {
    			if (isDate(Args[i])) {
    				if (AcqTime.compareTo(Args[i])>0) {
    					AcqTime = Args[i];
    					result = true;
    				}
    				else {
    					result = false;
    				}
    			}
    			else {
    				result = false;
    			}
    		}
    		else {
    			result = false;
    		}
    		return result;
    	}
    	/*
    	 *  判断日期格式是否正确
    	 */
    	public boolean isDate(String str) {
            try {
            	dateFormat.setLenient(false);
            	dateFormat.parse(str);
            	return true;
            }
            catch (Exception e){
                return false;
            }
        }
    	/*
    	 * 获取患者类型
    	 */
    	public boolean getType(int i) {
    		boolean result = true;
    		int n=0,m=0;
    		for (n=0;n<4;n++) {
    			m=n+i;
    			if (m<Args.length) {
    				if (Args[m]=="ip") {
    					Patients[0] = 1;
    				}
    				else if (Args[m]=="sp") {
    					Patients[1] = 2;
    				}
    				else if (Args[m]=="cure") {
    					Patients[2] = 3;
    				}
    				else if (Args[m]=="dead") {
    					Patients[3] = 4;
    				}
	    		}
    			else {
    				break;
    			}
	    	}
    		if (m==i) {
    			result=false;
    		}
    		Var=m;
    		return result;
    	}
    	/*
    	 * 获取要列出的省份（包括全国）
    	 */
    	public boolean getArea(int i) {
    		boolean result = true;
    		int n=0,m=0,k=0;
    		for (n=0;n<32;n++) {
    			m=n+i;
    			if (m<Args.length) {
    				int j=0;
    				for (j=0;j<32;j++) {
    					if (AreaStr[j]==Args[m]) {
    						Area[j]=1;
    						k++;
    						break;
    					}
    				}
	    		}
    			else {
    				break;
    			}
	    	}
    		m=k+i;
    		if (m==i) {
    			result=false;
    		}
    		Var=m;
    		return result;
    	}
    }
    
    
    
    
    
    public static void main(String[] args) {
        System.out.println("helloworld");
        System.out.println( AcqTime);
        int a=0;
        int d=a++;
        int c=++a;
        System.out.println(c);
        InfectStatistic infectstatistic = new InfectStatistic();
        InfectStatistic.CLA cla = infectstatistic.new CLA(args); 
        boolean b=cla.JudgCLA();
        if (b==false) {
        	System.out.println("helloworld");
            System.out.println( AcqTime);
        }
        System.out.println( AcqTime);
    }
}
