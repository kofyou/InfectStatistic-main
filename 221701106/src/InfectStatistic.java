/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
import java.io.*;
import java.lang.String;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
class InfectStatistic {
	public String[] province= {
			"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南",
			"河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁",
			"内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津",
			"西藏","新疆","云南","浙江"
	};
	public int[] illnessarray=new int[32];			//用于存放各省感染患者人数
	public int[] doubtfulillnessarray=new int[32];	//用于存放各省疑似患者人数
	public int[] secureillnessarray=new int[32];	//用于存放各省治愈患者人数
	public int[] deadillnessarray=new int[32];		//用于存放各省死亡人数
	public int[] type={1,2,3,4};					//用于判断-type类型的输出顺序
	String outputarray;					//用于存放处理过后需要输出的字符串数组
    public static void main(String args[]) throws IOException {
		String[] argstring=args;
		InfectStatistic useInfectStatistic=new InfectStatistic();
		OutProcess outprocess = useInfectStatistic.new OutProcess();
		DateHandle datehandle = useInfectStatistic.new DateHandle();
		TypeHandle typehandle = useInfectStatistic.new TypeHandle();
    	for (int i=0;i<args.length;i++) {		//循环读取命令行参数
    		//System.out.println(args[i]);
			String logpath=null;				//日志文件路径
			String outpath=null;				//输入文件路径
			if (args[i].equals("-log")) {
				logpath=args[i+1];				//-log参数的下一个参数即为日志文件路径名
				//System.out.println(logpath);
				LogProcess(logpath);			//对-log参数进行处理
			}
			if (args[i].equals("-out")) {
				outpath=args[i+1];				//-out参数的下一个参数即为输出文件路径名
				OutProcess(outpath);			//对-out参数进行处理
			}
			if (args[i]=="-date") {				
				concretedate=args[i+1];			//-date参数的下一个参数即为需要输出的具体日期
			}
			if (args[i]=="-type") {				
				typehandle.TypeHandle(argstring,i);			//处理-type参数
			}
		}
    }
    public class OutProcess {
		public void OutProcess(String outpath) throws IOException {	//输出到指定文件中
    		FileWriter fw=new FileWriter(outpath);
    		fw.write(outputarray);									//将最终字符串数组输出到指定文件中
    	}
	}
    public void LogContentHandle(String lineText) {
    	String match1="(\\S+) 新增 感染患者 (\\d+)人";		//匹配<省> 新增 感染患者 n人
    	String match2="(\\S+) 新增 疑似患者 (\\d+)人";		//匹配<省> 新增 疑似患者 n人
    	String match3="(\\S+) 感染患者 流入 (\\S+) (\\d+)人";	//匹配<省1> 感染患者 流入 <省2> n人
    	String match4="(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";	//匹配<省1> 疑似患者 流入 <省2> n人
    	String match5="(\\S+) 死亡 (\\d+)人";				//匹配<省> 死亡 n人
    	String match6="(\\S+) 治愈 (\\d+)人";				//匹配<省> 治愈 n人
    	String match7="(\\S+) 疑似患者 确诊感染 (\\d+)人";	//匹配<省> 疑似患者 确诊感染 n人
    	String match8="(\\S+) 排除 疑似患者(\\d+)人";			//匹配<省> 排除 疑似患者 n人
    	boolean isMatch1,isMatch2,isMatch3,isMatch4,isMatch5,isMatch6,isMatch7,isMatch8;
    	if (isMatch1=Pattern.matches(match1,lineText)) {
    		AddIllness(lineText);
    	}
    	if (isMatch2=Pattern.matches(match2,lineText)) {
    		AddDoubtfulIllness(lineText);
    	}
    	if (isMatch3=Pattern.matches(match2,lineText)) {
    		ChangeIllness(lineText);
    	}
    	if (isMatch4=Pattern.matches(match2,lineText)) {
    		ChangeDoubtfulIllness(lineText);
    	}
    	if (isMatch5=Pattern.matches(match2,lineText)) {
    		DeadIllness(lineText);
    	}
    	if (isMatch6=Pattern.matches(match2,lineText)) {
    		SecureIllness(lineText);
    	}
    	if (isMatch7=Pattern.matches(match2,lineText)) {
    		DiagnoseDoutbfulIllness(lineText);
    	}
    	if (isMatch8=Pattern.matches(match2,lineText)) {
    		RemoveDoutbfulIllness(lineText);
    	}
    }
    public String LogProcess(String logpath) throws IOException {
    	File logfilepath=new File(logpath);
		File list[]=logfilepath.listFiles();		//获取日志文件列表
		for (int logi=0;logi<list.length;logi++) {	//循环获得日志文件夹中的每一个日志文件
			//System.out.println(list[logi].getAbsolutePath());	//输出日志文件的绝对路径
			File logfile=new File(list[logi].getAbsolutePath());	//新建日志文件对象获得文件路径logfile
			//采用字符流读取文件内容
			BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(logfile),"UTF-8"));
			String lineText=null;
			while ((lineText=buff.readLine())!=null) {	//按行读取文本内容
				if(!lineText.startsWith("//"))		//行开头是"//"则不读取
					System.out.println(lineText);
			}
			buff.close();
		}
    }
    public void AddIllness(String lineText) {
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[3].replace("人",""));	//将n人从字符串类型转化为int类型
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配省份
    			illnessarray[i]+=n;								//该省感染患者人数增加n
    			illnessarray[32]+=n;							//全国感染患者人数增加n
    			break;
    		}
    	}
    }
    public void AddDoubtfulIllness(String lineText) {
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[3].replace("人",""));	//将n人从字符串类型转化为int类型
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配省份
    			doubtfulillnessarray[i]+=n;						//该省疑似患者人数增加n
    			doubtfulillnessarray[32]+=n;					//全国疑似患者人数增加n
    			break;
    		}
    	}
    }
    public void ChangeIllness(String lineText) {
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[4].replace("人",""));	//将n人从字符串类型转化为int类型
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配流出感染患者省份
    			illnessarray[i]-=n;								//流出患者省感染患者人数减少n
    			break;
    		}
    	}
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配流入感染患者省份
    			illnessarray[i]+=n;								//流入患者省感染患者人数增加n
    			break;
    		}
    	}
    }
    public void ChangeDoubtfulIllness(String lineText) {
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[4].replace("人",""));	//将n人从字符串类型转化为int类型
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配流出疑似患者省份
    			doubtfulillnessarray[i]-=n;						//流出患者省疑似患者人数减少n
    			break;
    		}
    	}
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配流入疑似患者省份
    			doubtfulillnessarray[i]+=n;						//流入患者省疑似患者人数增加n
    			break;
    		}
    	}
    }
    public void DeadIllness(String lineText) {
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[2].replace("人",""));	//将n人从字符串类型转化为int类型
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配死亡患者省份
    			deadillnessarray[i]+=n;							//该省死亡人数增加n
    			illnessarray[i]-=n;								//该省感染患者人数减少n
    			illnessarray[32]-=n;							//全国感染患者人数减少n
    			break;
    		}
    	}
    }
    public void SecureIllness(String lineText) {
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[2].replace("人",""));	//将n人从字符串类型转化为int类型
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配治愈患者省份
    			secureillnessarray[i]+=n;						//该省治愈人数增加n
    			illnessarray[i]-=n;								//该省感染患者人数减少n
    			illnessarray[32]-=n;							//全国感染患者人数减少n
    			break;
    		}
    	}
    }
    public void DiagnoseDoutbfulIllness(String lineText) {
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[3].replace("人",""));	//将n人从字符串类型转化为int类型
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配治愈患者省份
    			doubtfulillnessarray[i]-=n;						//该省的疑似患者减少n
    			doubtfulillnessarray[32]-=n;					//全国的疑似患者减少n
    			illnessarray[i]+=n;								//该省感染患者人数增加n
    			illnessarray[32]+=n;							//全国感染患者人数增加n
    			break;
    		}
    	}
    }
    public void RemoveDoutbfulIllness(String lineText) {
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[3].replace("人",""));	//将n人从字符串类型转化为int类型
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配治愈患者省份
    			doubtfulillnessarray[i]-=n;						//该省的疑似患者减少n
    			doubtfulillnessarray[32]-=n;					//全国的疑似患者减少n
    			break;
    		}
    	}
    }
    public class TypeHandle {
    	public void TypeHandle(String[] args,int i) {
    		int j;
    		for (j=0;j<4;j++) {
    			type[j]=0;		//-type参数将所有输出优先级置0
    		}
    		j=1;
    		for(;i<args.length;i++) {
    			if (args[i].equals("ip")) {								//只输出感染患者
    				type[0]=j;
    				j++;
    			}
    			if (args[i].equals("sp")) {								//只输出疑似患者
    				type[1]=j;
    				j++;
    			}
    			if (args[i].equals("cure")) {							//只输出治愈患者
    				type[2]=j;
    				j++;
    			}
    			if (args[i].equals("dead")) {							//只输出治愈患者
    				type[3]=j;
    				j++;
    			}
    		}
    	}
    }
    public class DateHandle {
    	public void DateHandle(String concretedate,String logpath) throws IOException {//处理-date参数所输入的具体日期
    		File logfilepath=new File(logpath);
    		File list[]=logfilepath.listFiles();					//获取日志文件列表
    		for (int i=0;i<list.length;i++) {						//循环获得日志文件夹中的每一个日志文件
    			String logsname=list[i].getName();					//获得日志文件名
    			String cutlineText[]=logsname.split("\\.");			//按"."分割
    			//System.out.println(cutlineText[0]);				//输出分割后的日志文件名，测试用
    			if (!(logsname.compareTo(concretedate)>0)) {		//如果在指定的日期内则处理该文本的信息
    				//LogContentHandle(LogProcess(list[i].getAbsolutePath()));
    				LogProcess2(list[i].getAbsolutePath());
    				//System.out.println(list[i].getAbsolutePath());	//测试用，输出指定日期的日志文件绝对路径
    				//System.out.println(logsname);					//输出指定日期内的日志文件名，测试用
				}
			}
    	}
    }
}
