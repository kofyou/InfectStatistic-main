/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
import java.util.Vector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;  
import infectstatistic_yjchen.J_Province;

public class InfectStatistic
{
	static J_Province allCountry = new J_Province();
	static Vector<J_Province> provinces = new Vector<J_Province>();
	static String[] arrayProvinces={"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南",
			"河北","河南","黑龙江","湖北","湖南","江西","吉林","江苏","辽宁","内蒙古","宁夏",
			"青海","山西","山东","陕西","上海","四川","天津","西藏","新疆","云南","浙江","香港",
			"台湾","澳门"};

	static int[] listType = {0,0,0,0};
	static String inputEndDate;
	static String inputAddress;
	static String outputFileAddress;
	static Vector<Integer> posVec = new Vector<Integer>();
	static Vector<Integer> interval = new Vector<Integer>();
	static Vector<String> temp=new Vector<String>();
	static Vector<String> vector=new Vector<String>();
	static boolean isOutCountry = false;//是否输出全国感染信息
	static int countryPos = 0;//”全国“所处的下标
	
	
    public static void main(String args[ ]) throws IOException
    {
    	//接收命令行参数
    	for(String temp : args)vector.add(temp);
    	//测试函数dealParameter
    	
    	{
    		vector.add("list");
    		vector.add("-log");
    		vector.add("G:\\java\\eclipse\\eclipse-workspace\\hw2_2\\src\\infectstatistic_yjchen\\");
    		vector.add("-out");
    		vector.add("G:\\java\\eclipse\\eclipse-workspace\\hw2_2\\src\\output.txt");
    		vector.add("-date");
    		vector.add("2020-02-22");
    		vector.add("-type");
    		vector.add("ip");
    		vector.add("sp");
    		vector.add("dead");
    		vector.add("-province");
    		vector.add("福建");
    		vector.add("全国");
    		vector.add("湖南");
    	}
    	
    	//保存全国的感染情况
    	//J_Province allCountry = new J_Province();
    	allCountry.setName("全国");
    	//保存各省市的感染情况
    	//Vector<J_Province> provinces = new Vector<J_Province>();
    	//省份按拼音首字母顺序排序
    	Comparator<Object> com=Collator.getInstance(java.util.Locale.CHINA);
    	/*
    	String[] arrayProvinces={"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南",
    			"河北","河南","黑龙江","湖北","湖南","江西","吉林","江苏","辽宁","内蒙古","宁夏",
    			"青海","山西","山东","陕西","上海","四川","天津","西藏","新疆","云南","浙江","香港",
    			"台湾","澳门"};
    	*/
    	Arrays.sort(arrayProvinces,com);
    	//输出排序结果
    	/*
    	for(String i:arrayProvinces){
    		System.out.print(i+" ");
    	}
    	*/
    	//先前用Vector实现，发现排序麻烦
    	/*
    	Vector<String> arrayProvinces = new Vector<String>();
    	arrayProvinces.add("安徽");
    	arrayProvinces.add("北京");
    	arrayProvinces.add("重庆");
    	arrayProvinces.add("福建");
    	arrayProvinces.add("甘肃");
    	arrayProvinces.add("广东");
    	arrayProvinces.add("广西");
    	arrayProvinces.add("贵州");
    	arrayProvinces.add("海南");
    	arrayProvinces.add("河北");
    	arrayProvinces.add("河南");
    	arrayProvinces.add("黑龙江");
    	arrayProvinces.add("湖北");
    	arrayProvinces.add("湖南");
    	arrayProvinces.add("江西");
    	arrayProvinces.add("吉林");
    	arrayProvinces.add("江苏");
    	arrayProvinces.add("辽宁");
    	arrayProvinces.add("内蒙古");
    	arrayProvinces.add("宁夏");
    	arrayProvinces.add("青海");
    	arrayProvinces.add("山西");
    	arrayProvinces.add("山东");
    	arrayProvinces.add("陕西");
    	arrayProvinces.add("上海");
    	arrayProvinces.add("四川");
    	arrayProvinces.add("天津");
    	arrayProvinces.add("西藏");
    	arrayProvinces.add("新疆");
    	arrayProvinces.add("云南");
    	arrayProvinces.add("浙江");
    	arrayProvinces.add("香港");
    	arrayProvinces.add("澳门");
    	arrayProvinces.add("台湾");
    	Collections.sort(arrayProvinces); 
    	*/
    	//将各省份感染信息加入到Vector中，方便管理
    	for(int i = 0;i < 34;i++) {
    		J_Province temp = new J_Province();
    		temp.setName(arrayProvinces[i]);
    		//System.out.println(temp.getName());
    		provinces.add(temp);
    		
    	}
        //System.out.println(arrayProvinces[0]);
    	//初始化vector
    	initialVector();
		//判断参数类型
    	dealParameter(vector);
    	//处理log文件
    	processLogFile();
    	//输出
		ListProvince(temp);
    	//ArrayList<String> dataFile = FileReadLine("");		
		//遍历全国感染结果
		/*
		System.out.println(allCountry.getName() + "\t" + "感染患者" + allCountry.Infected() + "人" +
		"\t" + "疑似患者" + allCountry.Suspected() + "人" + "\t" + "治愈" + allCountry.Cured() + "人" +
				"\t\t" + "死亡" + allCountry.Died() + "人");
		for(J_Province s:provinces) 
    	{
			System.out.println(s.getName() + "\t" + "感染患者" + s.Infected() + "人" + "\t" + "疑似患者" + 
    	s.Suspected() + "人" + "\t" + "治愈" + s.Cured() + "人" + "\t\t" + "死亡" + s.Died() + "人");
    	}
    	*/
    }
}
