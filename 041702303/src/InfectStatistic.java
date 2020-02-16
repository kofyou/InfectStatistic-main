/**
 * InfectStatistic
 * TODO
 *
 * @author wjchen
 * @version 1.0
 * @since 2020-02-15
 */



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	//主函数
	public static void main(String args[]) throws ParseException{
		Date date=new Date();
		String type[]={};
		String province[]={};
		String outString="";
		infectStatistic test=new infectStatistic(date, type, province, outString);
		System.out.println(test.getProvinceIndex("安徽"));
		System.out.println(test.getProvinceIndex("福建"));
		System.out.println(test.getProvinceIndex("北京"));
		System.out.println(test.getProvinceIndex("全国"));
		System.out.println(test.getTypeIndex("ip"));
		System.out.println(test.getTypeIndex("sp"));
		System.out.println(test.getTypeIndex("cure"));
		System.out.println(test.getTypeIndex("dead"));
		System.out.println(test.getTypeIndex("adad"));
		System.out.println(infectStatistic.isValidDate("2017-08-29"));
		System.out.println(infectStatistic.isValidDate("2017-08-31"));
		System.out.println(infectStatistic.isValidDate("2012-02-28"));
		System.out.println(infectStatistic.isValidDate("2017-08-32"));
		System.out.println(infectStatistic.isValidDate("2017-00-29"));
		System.out.println(infectStatistic.isValidDate("2017-00-32"));
		System.out.println(infectStatistic.isValidDate("2017*08-29"));
		System.out.println(infectStatistic.isValidDate("2017-08*29"));
		System.out.println(infectStatistic.isValidDate("2012-02-29"));
		System.out.println(infectStatistic.isValidDate("2012-aa-29"));
	}
}

