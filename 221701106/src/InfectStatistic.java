//package homework; 
/**
 * InfectStatistic
 * TODO
 *
 * @author 刘星雨
 * @version 1.0
 * @since 2020-02-14
 */
/**
 * Copyright @ 221701106_刘星雨
 */
import java.io.*;
import java.lang.String;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;


class InfectStatistic {
	public String[] province= {
			"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南",
			"河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁",
			"内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津",
			"西藏","新疆","云南","浙江","全国"
	};
	public int[] illnessarray=new int[32];			//用于存放各省感染患者人数
	public int[] doubtfulillnessarray=new int[32];	//用于存放各省疑似患者人数
	public int[] secureillnessarray=new int[32];	//用于存放各省治愈患者人数
	public int[] deadillnessarray=new int[32];		//用于存放各省死亡人数
	public int[] type={1,2,3,4};					//用于判断-type类型的输出顺序
	public int[] provinceflag=new int[32];			//用于存放输出省份的标准
    public static void main(String args[]) throws IOException {
    	//String[] args= {"list","-log","D:\\game\\221701106\\example\\log","-out","D:\\output.txt","-date","2020-01-25"};
    	String[] argstring=args;
		int df=0,tf=0,pf=0;							//-date,-type,-province标志
		String[] typestr={"感染患者","疑似患者","治愈","死亡"};
		
		InfectStatistic useInfectStatistic=new InfectStatistic();
		OutProcess outprocess = useInfectStatistic.new OutProcess();
		LogProcess logprocess =useInfectStatistic.new LogProcess();
		TypeHandle typehandle = useInfectStatistic.new TypeHandle();
		ProvinceHandle provincehandle = useInfectStatistic.new ProvinceHandle();
		
		String logpath=null;				//日志文件路径
		String outpath=null;				//输入文件路径
		String strdate=null;
		int ilog=0,idate=0,itype=0,iprovince=0;
		int di=0,ti=0,pi=0;
		
		for (int i=0;i<args.length;i++) {		//循环读取命令行参数
			if (args[i].equals("-log")&&ilog!=1) {
				ilog=1;
				logpath=args[i+1];				//-log参数的下一个参数即为日志文件路径名
			}
			if (args[i].equals("-out")) {	
				outpath=args[i+1];				//-out参数的下一个参数即为输出文件路径名
			}
			if (args[i].equals("-date")) {		//保证-date参数是第二个处理的
				idate=1;
				di=1;
				df=1;
				strdate=args[i+1];
			}
			if (args[i].equals("-type")) {
				itype=1;
				tf=1;
				ti=i;
			}
			if (args[i].equals("-province")) {
				iprovince=1;
				pf=1;
				pi=i;
			}
		}
		
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		if (df==0) {								//未指定日期即用当然日期进行处理
			logprocess.LogProcess(logpath,date.format(formatter));
		}
		if (df!=0) {
			if (date.format(formatter).compareTo(strdate)<0)
			{
				System.out.println("日期超出范围");
				System.exit(0);
			}
			logprocess.LogProcess(logpath,strdate);					//日期处理
		}
		if (tf!=0)
		typehandle.TypeHandle(argstring,ti);						//处理-type参数
		if (pf!=0)
		provincehandle.ProvinceHandle(argstring,pi);				//处理-type参数
		
		outprocess.OutProcess(logpath,outpath,typestr,df,tf,pf);	//对-out参数进行处理
    }
	public class OutProcess {
		public void OutProcess(String logpath,String outpath,String[] typestr,int df,int tf,int pf) throws IOException {	//输出到指定文件中
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outpath),"UTF-8"));
			
			if (tf==0) {						//未指定输出类型即按顺序输出
				for (int i=0;i<4;i++) {
					type[i]=i+1;
				}
			}
			if (pf==0) {						//未指定省份则输出全国和所有省份
				for (int i=0;i<32;i++) {
					provinceflag[i]=1;
				}
			}
			
			for (int i=0;i<32;i++) {
				int ftype=0;
				if (provinceflag[((i+31)%32)]==1&&tf==0) {
					bw.write(province[(i+31)%32]+" 感染患者"+illnessarray[(i+31)%32]+"人 "+"疑似患者"+
						doubtfulillnessarray[(i+31)%32]+"人 "+"治愈"+secureillnessarray[(i+31)%32]+"人 "+"死亡"+
						deadillnessarray[(i+31)%32]+"人"+"\n");
				}
				if (provinceflag[(i+31)%32]==1&&tf==1) {
					bw.write(province[(i+31)%32]+" ");
					for (int k=0;k<4;k++) {
						for (int j=0;j<4;j++) {
							if (type[j]==1&&ftype==0) {
								ftype++;
								if (j==0)
									bw.write("感染患者"+illnessarray[(i+31)%32]+"人 ");
								if (j==1)
									bw.write("疑似患者"+doubtfulillnessarray[(i+31)%32]+"人 ");
								if (j==2)
									bw.write("治愈"+secureillnessarray[(i+31)%32]+"人 ");
								if (j==3)
									bw.write("死亡"+deadillnessarray[(i+31)%32]+"人 ");
							}
							if (type[j]==2&&ftype==1) {
								ftype++;
								if (j==0)
									bw.write("感染患者"+illnessarray[(i+31)%32]+"人 ");
								if (j==1)
									bw.write("疑似患者"+doubtfulillnessarray[(i+31)%32]+"人 ");
								if (j==2)
									bw.write("治愈"+secureillnessarray[(i+31)%32]+"人 ");
								if (j==3)
									bw.write("死亡"+deadillnessarray[(i+31)%32]+"人 ");
							}
							if (type[j]==3&&ftype==2) {
								ftype++;
								if (j==0)
									bw.write("感染患者"+illnessarray[(i+31)%32]+"人 ");
								if (j==1)
									bw.write("疑似患者"+doubtfulillnessarray[(i+31)%32]+"人 ");
								if (j==2)
									bw.write("治愈"+secureillnessarray[(i+31)%32]+"人 ");
								if (j==3)
									bw.write("死亡"+deadillnessarray[(i+31)%32]+"人 ");
							}
							if (type[j]==4&&ftype==3) {
								ftype++;
								if (j==0)
									bw.write("感染患者"+illnessarray[(i+31)%32]+"人 ");
								if (j==1)
									bw.write("疑似患者"+doubtfulillnessarray[(i+31)%32]+"人 ");
								if (j==2)
									bw.write("治愈"+secureillnessarray[(i+31)%32]+"人 ");
								if (j==3)
									bw.write("死亡"+deadillnessarray[(i+31)%32]+"人 ");
							}
						}
					}
					bw.write("\n");
				}
			}
	        bw.close();
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
    	String match8="(\\S+) 排除 疑似患者 (\\d+)人";		//匹配<省> 排除 疑似患者 n人
    	
    	boolean isMatch1,isMatch2,isMatch3,isMatch4,isMatch5,isMatch6,isMatch7,isMatch8;
    	
    	if (isMatch1=Pattern.matches(match1,lineText)) {
    		AddIllness(lineText);
    	}
    	if (isMatch2=Pattern.matches(match2,lineText)) {
    		AddDoubtfulIllness(lineText);
    	}
    	if (isMatch3=Pattern.matches(match3,lineText)) {
    		ChangeIllness(lineText);
    	}
    	if (isMatch4=Pattern.matches(match4,lineText)) {
    		ChangeDoubtfulIllness(lineText);
    	}
    	if (isMatch5=Pattern.matches(match5,lineText)) {
    		DeadIllness(lineText);
    	}
    	if (isMatch6=Pattern.matches(match6,lineText)) {
    		SecureIllness(lineText);
    	}
    	if (isMatch7=Pattern.matches(match7,lineText)) {
    		DiagnoseDoutbfulIllness(lineText);
    	}
    	if (isMatch8=Pattern.matches(match8,lineText)) {
    		RemoveDoutbfulIllness(lineText);
    	}
    }   
    public class LogProcess {
    	public void LogProcess(String logpath,String concretedate) throws IOException {
    		File logfilepath=new File(logpath);
    		File[] list=logfilepath.listFiles();					//获取日志文件列表
    		for (int i=0;i<list.length;i++) {						//循环获得日志文件夹中的每一个日志文件
    			String logsname=list[i].getName();					//获得日志文件名
    			String[] cutlineText=logsname.split("\\.");			//按"."分割
    			if (!(cutlineText[0].compareTo(concretedate)>0)) {		//如果在指定的日期内则处理该文本的信息
    				File logfile=new File(list[i].getAbsolutePath());
					BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(logfile),"UTF-8"));
					String lineText=null;
					while ((lineText=buff.readLine())!=null) {	//按行读取文本内容
						if(!lineText.startsWith("//")) {		//行开头是"//"则不读取
							LogContentHandle(GetLineText(lineText));	//返回单行文本
						}
					}
				}
			}
    	}
    }
    public String GetLineText (String lineText) {
    	return lineText;
    }
    public void AddIllness(String lineText) {					//<省> 新增 感染患者 n人
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[3].replace("人",""));	//将n人从字符串类型转化为int类型
    	
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配省份
    			illnessarray[i]+=n;								//该省感染患者人数增加n
    			illnessarray[31]+=n;							//全国感染患者人数增加n
    			break;
    		}
    	}
    }
    public void AddDoubtfulIllness(String lineText) {			//<省> 新增 疑似患者 n人
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[3].replace("人",""));	//将n人从字符串类型转化为int类型
    	
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配省份
    			doubtfulillnessarray[i]+=n;						//该省疑似患者人数增加n
    			doubtfulillnessarray[31]+=n;					//全国疑似患者人数增加n
    			break;
    		}
    	}
    }
    public void ChangeIllness(String lineText) {				//<省1> 感染患者 流入 <省2> n人
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[4].replace("人",""));	//将n人从字符串类型转化为int类型
    	
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配流出感染患者省份
    			illnessarray[i]-=n;								//流出患者省感染患者人数减少n
    			break;
    		}
    	}
    	
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[3].equals(province[i])) {			//匹配流入感染患者省份
    			illnessarray[i]+=n;								//流入患者省感染患者人数增加n
    			break;
    		}
    	}
    }
    public void ChangeDoubtfulIllness(String lineText) {		//<省1> 疑似患者 流入 <省2> n人
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[4].replace("人",""));	//将n人从字符串类型转化为int类型
    	
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配流出疑似患者省份
    			doubtfulillnessarray[i]-=n;						//流出患者省疑似患者人数减少n
    			break;
    		}
    	}
    	
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[3].equals(province[i])) {			//匹配流入疑似患者省份
    			doubtfulillnessarray[i]+=n;						//流入患者省疑似患者人数增加n
    			break;
    		}
    	}
    }
    public void DeadIllness(String lineText) {					//<省> 死亡 n人
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[2].replace("人",""));	//将n人从字符串类型转化为int类型
    	
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配死亡患者省份
    			deadillnessarray[i]+=n;							//该省死亡人数增加n
    			deadillnessarray[31]+=n;						//全国死亡人数增加n
    			illnessarray[i]-=n;								//该省感染患者人数减少n
    			illnessarray[31]-=n;							//全国感染患者人数减少n
    			break;
    		}
    	}
    }
    public void SecureIllness(String lineText) {				//<省> 治愈 n人
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[2].replace("人",""));	//将n人从字符串类型转化为int类型
    	
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配治愈患者省份
    			secureillnessarray[i]+=n;						//该省治愈人数增加n
    			secureillnessarray[31]+=n;						//全国治愈人数增加n
    			illnessarray[i]-=n;								//该省感染患者人数减少n
    			illnessarray[31]-=n;							//全国感染患者人数减少n
    			break;
    		}
    	}
    }
    public void DiagnoseDoutbfulIllness(String lineText) {		//<省> 疑似患者 确诊感染 n人
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[3].replace("人",""));	//将n人从字符串类型转化为int类型
    	
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配疑似患者确诊省份
    			doubtfulillnessarray[i]-=n;						//该省的疑似患者减少n
    			doubtfulillnessarray[31]-=n;					//全国的疑似患者减少n
    			illnessarray[i]+=n;								//该省感染患者人数增加n
    			illnessarray[31]+=n;							//全国感染患者人数增加n
    			break;
    		}
    	}
    }
    public void RemoveDoutbfulIllness(String lineText) {		//<省> 排除 疑似患者 n人
    	String[] cutlineText=lineText.split(" ");
    	int n=Integer.valueOf(cutlineText[3].replace("人",""));	//将n人从字符串类型转化为int类型
    	
    	for (int i=0;i<province.length;i++) {
    		if (cutlineText[0].equals(province[i])) {			//匹配排除疑似患者省份
    			doubtfulillnessarray[i]-=n;						//该省的疑似患者减少n
    			doubtfulillnessarray[31]-=n;					//全国的疑似患者减少n
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
    			if (args[i].equals("ip")) {				//只输出感染患者
    				type[0]=j;
    				j++;
    			}
    			if (args[i].equals("sp")) {				//只输出疑似患者
    				type[1]=j;
    				j++;
    			}
    			if (args[i].equals("cure")) {			//只输出治愈患者
    				type[2]=j;
    				j++;
    			}
    			if (args[i].equals("dead")) {			//只输出治愈患者
    				type[3]=j;
    				j++;
    			}
    		}
    	}
    }
    public class ProvinceHandle {
    	public void ProvinceHandle(String[] args,int i) {
    		for(;i<args.length;i++) {
    			for (int j=0;j<province.length;j++) {
    					if (args[i].equals(province[j])) {		//参数中的省份如果匹配的话，讲省份的标志置为1
    						provinceflag[j]=1;			
    				}
    			}
    		}
    	}
    }
}
