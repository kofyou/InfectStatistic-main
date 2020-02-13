/**
 * InfectStatistic
 * TODO
 *
 * @author yjchen
 * @version 1.0
 * @since 2020-02-08
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
    	allCountry.setName("全国");
    	//保存各省市的感染情况
    	//Vector<J_Province> provinces = new Vector<J_Province>();
    	//省份按拼音首字母顺序排序
    	Comparator<Object> com=Collator.getInstance(java.util.Locale.CHINA);
    	Arrays.sort(arrayProvinces,com);
    	//输出排序结果
    	/*
    	for(String i:arrayProvinces){
    		System.out.print(i+" ");
    	}
    	*/
    	//先前用Vector实现，发现排序麻烦    	
    	//将各省份感染信息加入到Vector中，方便管理
    	for(int i = 0;i < 34;i++) {
    		J_Province temp = new J_Province();
    		temp.setName(arrayProvinces[i]);
    		provinces.add(temp);
    		
    	}
    	//初始化vector
    	initialVector();
		//判断参数类型
    	dealParameter(vector);
    	//处理log文件
    	processLogFile();
    	//输出
		ListProvince(temp);
    }
    
    
    /*******************
     * 作用:初始化向量
     * 参数:null
     *******************/
    private static void initialVector() {
		// TODO Auto-generated method stub    	
    	for(int i = 0;i < vector.size();i ++ )
    	{
    		if(vector.get(i).charAt(0) == '-')
    		{
    			posVec.add(i);
    		}
    		if(vector.get(i).equals("全国")) //如果有全国，则省份个数减一个
    		{
    			isOutCountry = true;
    			countryPos = i;
    		}
       	}
    	if(isOutCountry) //如果有全国，则省份个数减一个
		{
    		posVec.add(vector.size()-1);
		}
    	else
    	{
    		posVec.add(vector.size());
    	}
    	
    	for(int i = 1;i < posVec.size();i ++)
    	{
    		interval.add(posVec.elementAt(i)-posVec.elementAt(i-1)-1);
    		//System.out.println(posVec.elementAt(i)-posVec.elementAt(i-1)-1);
    	}
	}
    
    
    /*******************
     * 作用:判断参数选项
     * 参数:存放命令行参数的Vector数据结构
     *******************/
    private static void dealParameter(Vector<String> vector) {
		// TODO Auto-generated method stub
    	int pos=0;
    	if(vector.get(0).equals("list")) 
    	{
    		for(int i = 0;i < vector.size();i ++ )
        	{
        		if(vector.get(i).charAt(0) == '-') {
        			if(vector.get(i).equals("-log"))
        			{
        				//System.out.print("-log:");
        				//System.out.print("interval.elementAt(pos):"+interval.elementAt(pos));
        				//System.out.print("(pos):"+pos);
        				for(int j = 0;j < interval.elementAt(pos);j ++ )
        				{
        					//System.out.print(vector.get(i+j+1));
        					inputAddress = vector.get(i+j+1);
        				}
        				//System.out.println();
        				i += interval.elementAt(pos);
        				pos ++ ;
        				continue;
        			}
        			if(vector.get(i).equals("-out"))
        			{
        				//System.out.print("-out:");
        				//System.out.print("interval.elementAt(pos):"+interval.elementAt(pos));
        				//System.out.print("(pos):"+pos);
        				for(int j = 0;j < interval.elementAt(pos);j ++ )
        				{
        					//System.out.print(vector.get(i+j+1));
        					outputFileAddress = vector.get(i+j+1);
        					//System.out.print(outputFileAddress);
        				}
        				//System.out.println();
        				i += interval.elementAt(pos);
        				pos ++ ;
        				continue;
        			}
        			if(vector.get(i).equals("-date"))
        			{
        				//System.out.print("-date:");
        				//System.out.print("interval.elementAt(pos):"+interval.elementAt(pos));
        				//System.out.print("(pos):"+pos);
        				for(int j = 0;j < interval.elementAt(pos);j ++ )
        				{
        					//System.out.print(vector.get(i+j+1));
        					inputEndDate = vector.get(i+j+1);
        					//System.out.print("inputEndDate=" + inputEndDate);
        				}
        				//System.out.println();
        				i += interval.elementAt(pos);
        				pos ++ ;
        				continue;
        			}
        			if(vector.get(i).equals("-type"))
        			{
        				//System.out.print("-type:");
        				//System.out.print("interval.elementAt(pos):"+interval.elementAt(pos));
        				//System.out.print("(pos):"+pos);
        				for(int j = 0;j < interval.elementAt(pos);j ++ )
        				{
        					//System.out.print(vector.get(i+j+1));
        					if(vector.get(i+j+1).equals("ip")) {
        						listType[0] = 1;
        					}
        					if(vector.get(i+j+1).equals("sp")) {
        						listType[1] = 1;
        					}
        					if(vector.get(i+j+1).equals("cure")) {
        						listType[2] = 1;
        					}
        					if(vector.get(i+j+1).equals("dead")) {
        						listType[3] = 1;
        					}
        				}
        				//System.out.println();
        				i += interval.elementAt(pos);
        				pos ++ ;
        				//判断输出的列
        				continue;
        			}
        			if(vector.get(i).equals("-province"))
        			{
        				//System.out.print("-province:");
        				//System.out.print("interval.elementAt(pos):"+interval.elementAt(pos));
        				//System.out.print("(pos):"+pos);
        				for(int j = 0;j < interval.elementAt(pos);j ++ )
        				{
        					if(vector.get(i+j+1).equals("全国"))
        					{
        						isOutCountry = true;
        					}
        					//System.out.print(vector.get(i+j+1));
        					if(i+j+1 >= countryPos)
        					{
        						temp.add(vector.get(i+j+2));//跳过“全国”
        					}
        					else
        					{
        						temp.add(vector.get(i+j+1));
        					}
        				}
        				//System.out.println();
        				i += interval.elementAt(pos);
        				pos ++ ;
        				continue;
        			}
        			i += interval.elementAt(pos);
    				pos ++ ;
        			
        		}
        	}
    		
    	}
    	
	}

}
