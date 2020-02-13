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
    
    
    /*****************************
     * 作用:处理日志文件
     * 参数:null
     *****************************/
    private static void processLogFile()
    {
    	//读取文件，读取从2019-12-31到指定日期的所有文件。没有则continue。
    	//定义变量
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = null,dEnd = null;
        String fileName = new String();
        ArrayList<String> dataFile;
        File file;
		try {
			dBegin = sdf.parse("2019-12-31");
			dEnd = sdf.parse(inputEndDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //设置Calendar时间
    	Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(dEnd);
		//在选择的日期内循环。
		while(dEnd.after(calBegin.getTime()))
		{
			 //加一天
			 calBegin.add(Calendar.DAY_OF_MONTH, 1);
			 //System.out.println(sdf.format(calBegin.getTime()));
			 //记得加上前缀，不是在当前文件的目录
			 fileName = inputAddress +  sdf.format(calBegin.getTime()) + ".log.txt";
			 /*			  
			  fileName =  sdf.format(calBegin.getTime()) + ".log.txt";
			  */
			 //System.out.println(fileName);
			 file = new File(fileName);
			 if(!file.exists()) 
			 {
				 //提示文件不存在
				 //不存在文件，则跳过当日，默认趋势保持不变
				 //System.out.println(fileName == "2020-01-22.log.txt");
				 continue;
			 }
			 else 
			 {
				 //System.out.println(fileName);
				 String[] ss ;
				 String st;
				 dataFile = FileReadLine(fileName);
				 for(int i = 0; i < dataFile.size(); i++) {
					 //获取文件行
			         //System.out.println(dataFile.get(i));
			         //按空格分割
					 ss = dataFile.get(i).split("\\s+");
					 //获取对应省份下标
					 int pos = 0;
					 for(int k = 0;k < provinces.size();k++) 
					 {
						 st = provinces.elementAt(k).getName();
						 //System.out.println(st+" "+ss[0]);
						 if(st.contentEquals(ss[0]))
						 {
							 pos = k;
							 break;
						 }
					 }					 
					 //System.out.println(pos);
					 /*
					 for (int j=0;j<ss.length;j++) {
						 System.out.print(ss[j] + "*");						 
					 }
					 System.out.println();
					 */
					 //行分三份
					 if(ss.length == 3) 
					 {
						 String sTemp = ss[2].substring(0,ss[2].length() - 1);
						 if(ss[1].equals("死亡"))
						 {
							 //System.out.println(ss[2].substring(0,ss[2].length() - 1));							 
							 //System.out.println(Integer.parseInt(sTemp));
							 //死亡数+num
							 provinces.elementAt(pos).AddDied(Integer.parseInt(sTemp));
							 //感染数-num
							 provinces.elementAt(pos).DecInfected(Integer.parseInt(sTemp));
							 //全国死亡数+num
							 allCountry.AddDied(Integer.parseInt(sTemp));
							 //全国感染数-num
							 allCountry.DecInfected(Integer.parseInt(sTemp));
						 }
						 if(ss[1].equals("治愈"))
						 {
							 //治愈数+num
							 provinces.elementAt(pos).AddCured(Integer.parseInt(sTemp));
							 //感染数-num
							 provinces.elementAt(pos).DecInfected(Integer.parseInt(sTemp));
							 //全国治愈数+num
							 allCountry.AddCured(Integer.parseInt(sTemp));
							 //全国感染数-num
							 allCountry.DecInfected(Integer.parseInt(sTemp));
						 }
					 }
					//行分四份
					 if(ss.length == 4)
					 {
						 //获取人数
						 String sTemp = ss[3].substring(0,ss[3].length() - 1);
						 if(ss[1].equals("新增"))
						 {
							 if(ss[2].equals("感染患者"))
							 {
								 //感染数+num
								 provinces.elementAt(pos).AddInfected(Integer.parseInt(sTemp));
								 //全国感染数+num
								 allCountry.AddInfected(Integer.parseInt(sTemp));
							 }
							 if(ss[2].equals("疑似患者"))
							 {
								 //疑似数+num
								 provinces.elementAt(pos).AddSuspected(Integer.parseInt(sTemp));
								 //全国疑似数+num
								 allCountry.AddSuspected(Integer.parseInt(sTemp));
							 }
						 }
						 if(ss[1].equals("疑似患者"))
						 {
							 //感染数+num
							 provinces.elementAt(pos).AddInfected(Integer.parseInt(sTemp));
							 //疑似数-num
							 provinces.elementAt(pos).DecSuspected(Integer.parseInt(sTemp));
							 //全国感染数+num
							 allCountry.AddInfected(Integer.parseInt(sTemp));
							 //全国疑似数-num
							 allCountry.DecSuspected(Integer.parseInt(sTemp));
						 }
						 if(ss[1].equals("排除"))
						 {
							 //疑似数-num
							 provinces.elementAt(pos).DecSuspected(Integer.parseInt(sTemp));
							 //全国疑似数-num
							 allCountry.DecSuspected(Integer.parseInt(sTemp));
						 }
					 }
					//行分五份
					 if(ss.length == 5)
					 {
						 String sTemp = ss[4].substring(0,ss[4].length() - 1);
						 //流入的城市的下标
						 int pos_2 = 0;
						 for(int k = 0; k < provinces.size(); k++) 
						 {
							 st = provinces.elementAt(k).getName();
							 //System.out.println(st+" "+ss[0]);
							 if(st.contentEquals(ss[3]))
							 {
								 pos_2 = k;
								 break;
							 }
						 }
						 if(ss[1].equals("感染患者"))
						 {
							 //感染数+num
							 provinces.elementAt(pos_2).AddInfected(Integer.parseInt(sTemp));
							 //疑似数-num
							 provinces.elementAt(pos).DecInfected(Integer.parseInt(sTemp));
						 }
						 if(ss[1].equals("疑似患者"))
						 {
							 //疑似数+num
							 provinces.elementAt(pos_2).AddSuspected(Integer.parseInt(sTemp));
							 //疑似数-num
							 provinces.elementAt(pos).DecSuspected(Integer.parseInt(sTemp));
						 }
					 }
			     }
			 }	  
		}
    }
    
}
