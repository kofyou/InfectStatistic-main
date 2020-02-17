
/**
 * InfectStatistic
 * TODO
 *
 * @author wjchen
 * @version 1.0
 * @since 2020-02-15
 */

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class infectStatistic {
	//省份数
	final int provinceNumber=34;
	//类型数
	final int typeNumber=4;
	//各省各类型统计数
	int StatisticsNumber[][];
	//类型参数
	String parameterType[];
	//省份参数
	String parameterProvince[];
	//日志日期
	Date date;
	//输入输出路径
	String logPath,outPath;
	//省份名字
	String provinceName[]= {
			"安徽","澳门","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北",
			"河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海",
			"山东","山西","陕西","上海","四川","天津","西藏","香港","新疆","台湾","云南","浙江"
			};
	//类型中文名
	String typeName[]= {"感染患者","疑似患者","治愈","死亡"};
	//类型英文名
	String enTypeName[]= {"ip","sp","cure","dead"};
	
	//构造函数
	infectStatistic(Date date,String type[],String province[],String out){
		StatisticsNumber=new int[provinceNumber][typeNumber];
		for(int i=0;i<provinceNumber;i++) {
			for(int j=0;j<typeNumber;j++) {
				StatisticsNumber[i][j]=0;
			}
		}
		this.date=date;
		this.parameterType=type;
		this.parameterProvince=province;
		this.outPath=out;
	}
	
	//获取文件名对应时间
	public Date getFileNameDate(File file) throws ParseException {
		String date=file.getName();
		int index=date.indexOf(".");
		date=date.substring(0, index);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(date);
	}
	
	
	//通过日期参数获取所需处理日志文件
	public File[] getFilesByDate(Date parameterDate,File folder) throws ParseException {
		ArrayList<File> fileList=new ArrayList<File>();
		if(isExistValidDateFile(folder, parameterDate)) {
			File files[]=folder.listFiles();
			for(File file : files) {
				int result=getFileNameDate(file).compareTo(parameterDate);
				//判断日期，处理小于等于命令行日期的日志文件
				if(result<0||result==0) {
						fileList.add(file);
				}
			}
		}
		else{ 
			System.out.println("查找日志文件非法");
		}
		return (File[]) fileList.toArray(new File[fileList.size()]);
	}
	
	//获取对应省份的下标
	public int getProvinceIndex(String province) {
		for(int i=0;i<provinceNumber;i++) {
			if(province.equals("全国")){
				return -2;
			}
			if(province.equals(provinceName[i])) {
				return i;
			}
		}
		return -1;
	}
	
	//获取对应类型的下标
	public int getTypeIndex(String type) {
		for(int i=0;i<typeNumber;i++) {
			if(type.equals(enTypeName[i])) {
				return i;
			}
		}
		return -1;
	}

	//判断合法日期
	public static boolean isValidDate(String dateString) throws ParseException {
		char[] testDateArray=dateString.toCharArray();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		//去除日期格式化
		if(testDateArray[4]=='-'&&testDateArray[7]=='-') {
			for(int i=0;i<testDateArray.length;i++) {
				if((testDateArray[i]<'0'||testDateArray[i]>'9')&&i!=4&&i!=7) {
					return false;
				}
			}
			//分离年月日
			String time[]=dateString.split("-");
			int year=Integer.parseInt(time[0]);
			int month=Integer.parseInt(time[1]);
			int day=Integer.parseInt(time[2]);
			//常规的日期合法性判断
			if(month>12||month<1) {
				return false;
			}
			else {
				if(day>31||day<1) {
					return false;
				}
				else {
					if((month==4||month==6||month==9||month==11)) {
						if(day>30||day<0) {
							return false;
						}
					}
					if(year%4==0&&month==2&&day>28) {
						return false;
					}
					long parameterDateTime=sdf.parse(dateString).getTime();
					//比较命令行日期和当前时间,检测合法性
					if(parameterDateTime<System.currentTimeMillis()) {
						return true;
					}
					else {
						return false;
					}
				}
			}
		}
		else {
			return false;
		}
	}

	//判断合法疫情类型参数
	public boolean isValidParameterType() {
		int typeExist[]=new int[typeNumber];
		for(int i=0;i<typeNumber;i++) {
			typeExist[i]=0;
		}
		for(int i=0;i<parameterType.length;i++) {
			if(getTypeIndex(parameterType[i])==-1) {
				return false;
			}
			else {
				//检测命令行type参数值重复
				if(typeExist[getTypeIndex(parameterType[i])]==0) {
					typeExist[getTypeIndex(parameterType[i])]=1;
				}
				else {
					return false;
				}
			}
		}
		return true;
	}
	
	//判断合法省份参数
	public boolean isValidParameterProvince() {
		int provinceExist[]=new int[provinceNumber];
		int country=0;
		for(int i=0;i<provinceNumber;i++) {
			provinceExist[i]=0;
		}
		for(int i=0;i<parameterProvince.length;i++) {
			if(getProvinceIndex(parameterProvince[i])==-1) {
				return false;
			}
			else {
				if(getProvinceIndex(parameterProvince[i])==-2) {
					if(country==0) {
						country=1;
					}
					else {
						return false;
					}
					continue;
				}
				//检测命令行参数province值重复
				if(provinceExist[getProvinceIndex(parameterProvince[i])]==0) {
					provinceExist[getProvinceIndex(parameterProvince[i])]=1;
				}
				else {
					return false;
				}
			}
		}
		return true;
	}

	//判断是否存在合法日期日志文件
	public boolean isExistValidDateFile(File folder,Date parameterDate) throws ParseException {
		File files[]=folder.listFiles();
		for(File file : files) {
			int result=getFileNameDate(file).compareTo(parameterDate);
			//判断存在大于日期参数的文件
			if(result>0||result==0) {
					return true;
			}
		}
		return false;
	}

	//提示命令函数
	public static void validCommandTips(){
		System.out.println("reference:");
		System.out.println("	list<required and first>:");
		System.out.println("		-date<required>		must be valid date and lower now");
		System.out.println("		-log<required>		must be valid folder path");
		System.out.println("		-out<required>		must be valid file path");
		System.out.println("		-type<optional>		must be ip/sp/cure/dead");
		System.out.print("		-province<optional>	must be Chinese province(北京、上海...)/country");
	}

	//主函数
	public static void main(String args[]) throws ParseException{
		//检测命令行头部list
		if(args.length!=0&&args[0].equals("list")&&args.length!=1) {
			String dateString="";
			String logString="";
			String outString="";
			ArrayList<String> provinceArrayList=new ArrayList<String>();
			ArrayList<String> typeArrayList=new ArrayList<String>();
			//用于存储命令行参数位于数组的下标
			TreeMap<Integer, String> parameterTreeMap=new TreeMap<Integer, String>();
			//用于判断命令行参数有且仅有出现一次
			int parameterExist[]= {0,0,0,0,0};
			//初始化命令集合，提取参数下标
			for(int i=1;i<args.length;i++) {
				if(args[i].equals("-date")) {
					parameterTreeMap.put(i, "-date");
					//记录命令行参数出现次数
					parameterExist[0]++;
				}
				else if(args[i].equals("-log")) {
					parameterTreeMap.put(i, "-log");
					parameterExist[1]++;
				}
				else if(args[i].equals("-out")) {
					parameterTreeMap.put(i, "-out");
					parameterExist[2]++;
				}
				else if(args[i].equals("-province")) {
					parameterTreeMap.put(i, "-province");
					parameterExist[3]++;
				}
				else if(args[i].equals("-type")) {
					parameterTreeMap.put(i, "-type");
					parameterExist[4]++;
				}
			}
			//命令行参数的首位参数下标
			int maxParameterIndex=parameterTreeMap.lastKey();
			int minParameterIndex=parameterTreeMap.firstKey();
			int invalidCommand=0;
			//检测list命令头部的合法性
			if(minParameterIndex==1) {
				//解析参数，获取参数值
				for (int i : parameterTreeMap.keySet()) {
					//检测参数值存在，下标相邻为空，下标越界为空
					if((!parameterTreeMap.containsKey(i+1))&&i+1!=args.length) {
						if(parameterTreeMap.get(i).equals("-date")) {
							dateString=args[i+1];
						}
						else if(parameterTreeMap.get(i).equals("-log")) {
							logString=args[i+1];
						}
						else if(parameterTreeMap.get(i).equals("-out")) {
							outString=args[i+1];
						}
						else if(parameterTreeMap.get(i).equals("-province")) {
							if(i<maxParameterIndex) {
								for(int j=i+1;j<parameterTreeMap.higherKey(i);j++) {
									provinceArrayList.add(args[j]);
								}
							}
							else {
								for(int j=i+1;j<args.length;j++) {
									provinceArrayList.add(args[j]);
								}
							}
						}
						else if(parameterTreeMap.get(i).equals("-type")) {
							if(i<maxParameterIndex) {
								for(int j=i+1;j<parameterTreeMap.higherKey(i);j++) {
									typeArrayList.add(args[j]);
								}
							}
							else {
								for(int j=i+1;j<args.length;j++) {
									typeArrayList.add(args[j]);
								}
							}
						}
					}
					else {
						invalidCommand=1;
					}
				}
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String type[]= {};
				String province[]= {};
				//检测命令行参数值为空,0--非空 1--空
				if(invalidCommand==0) {
					//检测date/log/out参数必须存在且命令行参数唯一性
					if(parameterExist[1]==1&&parameterExist[2]==1&&
							parameterExist[0]==1&&parameterExist[3]<2&&parameterExist[4]<2) {
						type=typeArrayList.toArray(new String[typeArrayList.size()]);
						province=provinceArrayList.toArray(new String[provinceArrayList.size()]);
						//检测命令行日期参数的合法性
						if(infectStatistic.isValidDate(dateString.trim())) {
							Date date=sdf.parse(dateString.trim());
							infectStatistic t=new infectStatistic(date,type,province,outString);
							File folder=new File(logString.trim());
							//检测命令行type/province参数值的合法性
							if(t.isValidParameterType()&&t.isValidParameterProvince()) {
								if(folder.isDirectory()) {
									System.out.println("get");
								}
								else {
										System.out.println("路径非法，不存在该文件夹");
										infectStatistic.validCommandTips();
								}								
							}
							else {
									System.out.println("省份或者类型参数值非法");
									infectStatistic.validCommandTips();
							}
						}
						else {
							System.out.println("命令行日期参数非法");
							infectStatistic.validCommandTips();
						}
					}
					else {
						System.out.println("缺少或重复date/log/out参数");
						infectStatistic.validCommandTips();
					}
				}
				else {
					System.out.println("date/log/out/type/province参数值为空");
					infectStatistic.validCommandTips();
				}
			}
			else {
				System.out.println("list后未紧接参数");
				infectStatistic.validCommandTips();
			}
		}
		else {
			System.out.println("list命令格式错误");
			infectStatistic.validCommandTips();
		}
	}
}