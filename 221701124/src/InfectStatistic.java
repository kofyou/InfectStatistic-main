import java.util.Date;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
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
    public String targetPath; //目标路径
    public String originPath; //原路径
    public int var=0;
    public int preMark=0;
    
    /*
     * 获取当前时间
     * 将默认日期设为当前时间
     */
    static Date date = new Date();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static String acqTime = dateFormat.format(date);
    
    /*
     * 设置省份列表
     * 0代表未列出，1代表列出
     */
    public int [] area = {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public String[] areaStr = new String[]{"全国","安徽","北京","重庆","福建","甘肃","广东",
    		"广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西",
    		"辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏",
    		"新疆","云南","浙江"};
    
    /*
     * 设置患者类型（ip,sp,cure,dead）
     * 0代表无需列出，1代表感染，2代表疑似，3代表治愈，4代表死亡
     * 默认排序为1,2,3,4
     */
    public int [] patients = {1,2,3,4};
    public String[] patientsStr = new String[] {"感染患者","疑似患者","治愈","死亡"};
    
    /*
     * 设置全国和各地各个患者类型的人数情况
     * 数组的一维代表地区（包括全国），二维代表患者类型（ip,sp,cure,dead）
     */
    public int [][] totalNumber = new int [32][4];
    
    /*
     * 解析命令行参数
     */
    class Analysis {
    	public String [] args ; //储存传入参数
    	
    	/*
    	 * CLA重载
    	 */
    	public Analysis (String [] argsStr) {
    		args = argsStr;
    	}
    	
    	/*
    	 * 判断命令行参数是否有误
    	 */
    	public boolean JudgeParameter(){
    		if (args[0] != "list") {
    			System.out.println("未输入命令");
    			return false;
    		}
    		int i = 0;
    		for (i = 1 ; i < args.length ; i++) {
    			if (args[i] == "-log") {
    				if (GetOriginPath(++i) == false) {
    					System.out.println("日志目录路径格式错误");
    					return false;
    				}
    			}
    			else if (args[i] == "-out") {
    				if (GetTargetPath(++i) == false) {
    					System.out.println("输出文件路径格式错误");
    					return false;
    				}
    			}
    			else if (args[i] == "-date") {
    				if (GetDate(++i) == false) {
    					System.out.println("日期参数错误");
    					return false;
    				}
    			}
    			else if (args[i] == "-type") {
    				int n = 0;
    				for (n = 0 ; n < 4 ; n++) {
    					patients[n] = 0;
    				}
    				if (GetType(++i) == false) {
    					System.out.println("患者类型参数错误");
    					return false;
    				}
    				i = var;
    			}
    			else if (args[i] == "-province") {
    				preMark=1;
    				area[0] = 0;
    				if (GetArea(++i) == false) {
    					System.out.println("省份参数错误");
    					return false;
    				}
    				i = var;
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
    	public boolean GetTargetPath (int i) {
    		if (i < args.length) {
    			if (args[i].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$")) {
    				targetPath = args[i];
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
    	public boolean GetOriginPath (int i) {
    		if (i < args.length) {
    			if (args[i].matches("^[A-z]:\\\\(.+?\\\\)*$")) {
    				originPath = args[i];
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
    	public boolean GetDate (int i) {
    		boolean result = true;
    		if (i < args.length) {
    			if (IsDate(args[i])) {
    				if (acqTime.compareTo(args[i]) >= 0) {
    					acqTime = args[i];
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
    	public boolean IsDate(String str) {
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
    	public boolean GetType(int i) {
    		boolean result = true;
    		int n = 0 , m = 0;
    		for (n=0 ; n<4;n++) {
    			m = n + i;
    			if (m < args.length) {
    				if (args[m] == "ip") {
    					patients[0] = 1;
    				}
    				else if (args[m] == "sp") {
    					patients[1] = 2;
    				}
    				else if (args[m] == "cure") {
    					patients[2] = 3;
    				}
    				else if (args[m] == "dead") {
    					patients[3] = 4;
    				}
	    		}
    			else {
    				break;
    			}
	    	}
    		if (m == i) {
    			result = false;
    		}
    		var = m;
    		return result;
    	}
    	
    	/*
    	 * 获取要列出的省份（包括全国）
    	 */
    	public boolean GetArea(int i) {
    		boolean result = true;
    		int n = 0 , m = 0 , k = 0;
    		for (n = 0 ; n < 32 ; n++) {
    			m = n + i;
    			if (m < args.length) {
    				int j = 0;
    				for (j = 0 ; j < 32 ; j++) {
    					if (areaStr[j] == args[m]) {
    						area[j] = 1;
    						k++;
    						break;
    					}
    				}
	    		}
    			else {
    				break;
    			}
	    	}
    		m = k+i;
    		if (m == i) {
    			result = false;
    		}
    		var = m;
    		return result;
    	}
    }
    
    /*
     *  文件处理
     */
    class FileProcess {
    	
    	/*
    	 * 获取符合标准的日志文件路径
    	 */
    	public void ReadLogFile () {
    		File file = new File(originPath);
    		File[] tempList = file.listFiles();
    		int i = 0;
    		for (i = 0 ; i < tempList.length ; i++) {
    			if (tempList[i].isFile()) { 
    				String fileName = tempList[i].getName();
    				String[] names = fileName.split("\\.");
    				if (acqTime.compareTo(names[0]) >= 0) {
    					LogFile(fileName);
    				}
    			} 
    		} 
    	}
    	
    	/*
    	 * 读取日志文件中的内容
    	 */
    	public void LogFile (String fileName) {
    		String filePath = originPath + fileName;
    		try {
    			File tempFile = new File(filePath);
    			InputStreamReader reader = new InputStreamReader(  
    					new FileInputStream(tempFile),"UTF-8"); // 建立一个输入流对象reader  
    			BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            	String line = "";  
            	while ((line = br.readLine()) != null) {  
            		if(!line.startsWith("//")) 
            			LogProcess(line);      			
            	} 
            	br.close();
    		}
    		catch (Exception e) {  
                e.printStackTrace();  
            }  
    	}
    	
    	/*
    	 *对于获取的日志文件内容处理 
    	 */
    	public void LogProcess (String line) {
    		String [] lineSplit = line.split(" ");
    		int n = 0;
    		for (n = 0 ; n < lineSplit.length ; n++) {
    			if (n == lineSplit.length-1) {
    				lineSplit[n] = lineSplit[n].replace("人", "");
    			}
    		}
    		
    		if (lineSplit[1].equals("新增")) 
    			AddPeople(lineSplit);
    		if (lineSplit[1].equals("死亡")) 
    			DeadPeople(lineSplit);
    		if (lineSplit[1].equals("治愈")) 
    			CurePeople(lineSplit);
    		if (lineSplit[1].equals("排除")) 
    			ExcludePeople(lineSplit);
    		if (lineSplit[2].equals("流入")) 
    			InflowPeople(lineSplit);
    		if (lineSplit[2].equals("确诊感染")) 
    			ConfirmPeople(lineSplit);
    	}
    	
    	/*
    	 * 新增
    	 */
    	public void AddPeople (String [] people) {
    		int n = 0 , m = 0;
    		for (n = 0 ; n < 32 ; n++) {
    			if (areaStr[n].equals(people[0])) {
    				if (preMark == 0) {
    					area[n] = 1;
    				}
    				for (m = 0 ; m < 4 ; m++) {
    					if (patientsStr[m].equals(people[2])) {
    						int peopleNum = Integer.parseInt(people[3]);
    						totalNumber[n][m] += peopleNum;
    						totalNumber[0][m] += peopleNum;
    					}
    				}
    			}
    		}
    	}
    	
    	/*
    	 * 死亡
    	 */
    	public void DeadPeople (String [] people) {
    		int n=0;
    		for (n = 0 ; n < 32 ; n++) {
    			if (areaStr[n].equals(people[0])) {
    				if (preMark == 0) {
    					area[n] = 1;
    				}
    				int peopleNum = Integer.parseInt(people[2]);
					totalNumber[n][3] += peopleNum;
					totalNumber[n][0] -= peopleNum;
					totalNumber[0][3] += peopleNum;
					totalNumber[0][0] -= peopleNum;
    			}
    		}
    	}
    	
    	/*
    	 * 治愈
    	 */
    	public void CurePeople (String [] people) {
    		int n=0;
    		for (n = 0 ; n < 32 ; n++) {
    			if (areaStr[n].equals(people[0])) {
    				if (preMark == 0) {
    					area[n] = 1;
    				}
    				int peopleNum = Integer.parseInt(people[2]);
					totalNumber[n][2] += peopleNum;
					totalNumber[n][0] -= peopleNum;
					totalNumber[0][2] += peopleNum;
					totalNumber[0][0] -= peopleNum;
    			}
    		}
    	}
    	
    	/*
    	 * 排除
    	 */
    	public void ExcludePeople (String [] people) {
    		int n = 0 , m = 0;
    		for (n = 0 ; n < 32 ; n++) {
    			if (areaStr[n].equals(people[0])) {
    				if (preMark == 0) {
    					area[n] = 1;
    				}
    				for (m = 0 ; m < 4 ; m++) {
    					if (patientsStr[m].equals(people[2])) {
    						int peopleNum = Integer.parseInt(people[3]);
    						totalNumber[n][m] -= peopleNum;
    						totalNumber[0][m] -= peopleNum;
    					}
    				}
    			}
    		}
    	}
    	
    	/*
    	 * 流入
    	 */
    	public void InflowPeople (String [] people) {
    		int n = 0 , m = 0;
    		for (n = 0 ; n < 32 ; n++) {
    			if (areaStr[n].equals(people[0])) {
    				if (preMark == 0) {
    					area[n] = 1;
    				}
    				for (m = 0 ; m < 4 ; m++) {
    					if (patientsStr[m].equals(people[1])) {
    						int peopleNum = Integer.parseInt(people[4]);
    						totalNumber[n][m] -= peopleNum;
    					}
    				}
    			}
    			if (areaStr[n].equals(people[3])) {
    				if (preMark == 0) {
    					area[n] = 1;
    				}
    				for (m = 0 ; m < 4 ; m++) {
    					if (patientsStr[m].equals(people[1])) {
    						int peopleNum = Integer.parseInt(people[4]);
    						totalNumber[n][m] += peopleNum;
    					}
    				}
    			}
    		}
    	}
    	
    	/*
    	 * 确认感染
    	 */
    	public void ConfirmPeople (String [] people) {
    		int n = 0;
    		for (n = 0 ; n < 32 ; n++) {
    			if (areaStr[n].equals(people[0])) {
    				if (preMark == 0) {
    					area[n] = 1;
    				}
    				int peopleNum = Integer.parseInt(people[3]);
					totalNumber[n][1] -= peopleNum;
					totalNumber[0][1] -= peopleNum;
					totalNumber[n][0] += peopleNum;
					totalNumber[0][0] += peopleNum;
    			}
    		}
    	}
    	
    	/*
    	 * 将数据写入txt文件
    	 */
    	public void WriteOutFile () {
    		try {
    			File writeName = new File(targetPath); // 相对路径，如果没有则要建立一个新的output.txt文件  
                writeName.createNewFile(); // 创建新文件  
                BufferedWriter out = new BufferedWriter(new FileWriter(writeName));  
                int n = 0 , m = 0;
                String outData=" ";
                for (n = 0 ; n < 32 ; n++) {
                	if (area[n] == 1) {
                		outData = areaStr[n]+" ";
                		for (m = 0 ; m < 4 ; m++) {
                			if (patients[m] != 0)
                				outData += patientsStr[m] + totalNumber[n][m] + "人" + " ";
                		}
                		out.write(outData + "\r\n");
                	}
                }
                out.write("// 该文档并非真实数据，仅供测试使用\r\n");
                out.flush(); // 把缓存区内容压入文件  
                out.close(); // 最后记得关闭文件  
    		}
    		catch (Exception e) {  
                e.printStackTrace();  
            }  
    	}
    }
    
    public static void main(String[] args) {
        InfectStatistic infectstatistic = new InfectStatistic();
        InfectStatistic.Analysis cla = infectstatistic.new Analysis(args); 
        boolean b = cla.JudgeParameter();
        if (b == false) {
        	System.out.println("命令行参数存在问题！");
        }
        InfectStatistic.FileProcess fp = infectstatistic.new FileProcess();
        fp.ReadLogFile();
        fp.WriteOutFile();
    }
}
