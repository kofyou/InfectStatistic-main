/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

class InfectStatistic {
	public static String log_route="";//日志文件路径
	public static String out_route;//输出文件路径
	public static String log_need;//需要被解析的日志文件路径
	public static String out_name;//
	public static String[] log_list;//读取到的日志文件列表,并且按照日期从小到大排序
	public static int[] type_num= {1,2,3,4};
	/*
	 * ip代表感染患者，sp代表疑似患者，cure代表治愈，dead代表死亡
	 */
	public static String[] type_symbol= {"ip","sp","cure","dead"};
	public static String[] type_name= {"感染患者","疑似患者","治愈","死亡"};
	
	public static int[] province_select = new int[35];
	public static int[][] province_num = new int[35][4];
	public static String[] province_name = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃",
			"广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
			"江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
			"四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
    
public static void judgeCommandLine(String[] str) {
	if(!str[0].equals("list"))
	{
		System.out.println("命令行错误，开头非list错误");
		System.exit(0);
	}
	for(int i=1;i<str.length;i++)
	{
		if(str[i].equals("-log"))
		{
			if(str[++i].matches("^[A-z]:\\\\(.+?\\\\)*$"))
			{
				log_route=str[i];
			}
			else
			{
				System.out.println("命令行错误，文件路径未填写或错误");
				System.exit(0);
			}
		}
		if(str[i].equals("-out"))
		{
			if(str[++i].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$"))
			{
				out_route=str[i];
			}
			else
			{
				System.out.println("命令行错误，文件输出路径未填写或错误");
				System.exit(0);
			}
		}
		if(str[i].equals("-date"))
		{
			//-date如果是最后一个指令，直接
			if((i+1)==str.length)
			{
				getLogList(log_route);
				log_need=log_list[0];
			}
			else if(str[i+1].equals("-log")||str[i+1].equals("-out")
			   ||str[i+1].equals("-type")||str[i+1].equals("-province"))
			{
				getLogList(log_route);
				log_need=log_list[0];
			}
			else
			{
				if(isLegalDate(str[++i]))
				{
					getLogList(log_route);
					log_need=str[i];
							
				}
				else
				{
					System.out.println("命令行错误，日期格式有误");
					System.exit(0);
				}
			}
			
		}
		if(str[i].equals("-type"))
		{
			for(int j=1;j<5;j++)
			{
				//-date如果是最后一个指令，直接
				if((i+j)<str.length)
				{
					if(str[i+j].equals("-log")||str[i+j].equals("-out")
						||str[i+j].equals("-date")||str[i+j].equals("-province"))
					{
						break;
					}
					else if(str[i+j].equals(type_symbol[0]))
					{
						type_num[0]=j;
					}
					else if(str[i+j].equals(type_symbol[1]))
					{
						type_num[1]=j;
					}
					else if(str[i+j].equals(type_symbol[2]))
					{
						type_num[2]=j;
					}
					else if(str[i+j].equals(type_symbol[3]))
					{
						type_num[3]=j;
					}
					else
					{
						System.out.println("命令行错误，-type格式有误！");
						System.exit(0);
					}
				}
				else break;
			}
		}
		if(str[i].equals("-province"))
		{
			for(int j=1;j<=province_name.length;j++)
			{
				//-province如果是最后一个指令，直接
				if((i+j)<str.length)
				{
					if(str[i+j].equals("-log")||str[i+j].equals("-out")
						||str[i+j].equals("-date")||str[i+j].equals("-province"))
					{
						break;
					}
					else if(str[i+j].equals(province_name[j-1]))
					{
						province_select[0]=1;
						province_select[j-1]=1;
					}
					else
					{
						System.out.println("命令行错误，-province格式有误！");
						System.exit(0);
					}
				}
				else break;
			}
			
		}
	}
}
public static boolean isLegalDate(String str) {
	SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
	 try {
		 date_format.setLenient(false);
		 date_format.parse(str);
         String[] date_str = str.split("-");
         for (String s : date_str) 
         {
             boolean isNum = s.matches("[0-9]+");
             if (!isNum)
                 return false;
         }
	 	 } catch (Exception e) {
         return false;
     }
     return true;
}

public static void getLogList(String str) {
	log_list=new File(str).list();
	for (int i=0;i<log_list.length-1;i++){
        for (int j=0;j<log_list.length-i-1;j++) {
        	if(log_list[j].compareTo(log_list[j+1])>0){
                String temp=log_list[j];
                log_list[j]=log_list[j+1];
                log_list[j+1]=temp;
            }
        	
        }
    }

}
/*
 * 读取文本内容
 */
public static void getTextContent(String str) {
	try {
		BufferedReader br = new BufferedReader(new InputStreamReader
				(new FileInputStream(new File(str)), "UTF-8"));
		 String text_content = null;
         
         while ((text_content = br.readLine()) != null) { //按行读取文本内容
         	if(!text_content.startsWith("//")) //遇到“//”不读取
         	processingText(text_content);
         }
         br.close();
	} catch (Exception e) {
		// TODO: handle exception
	}
}

    
    
    
public static void processingText(String str) {
	String situation1 = "(\\S+) 新增 感染患者 (\\d+)人";
	String situation2 = "(\\S+) 新增 疑似患者 (\\d+)人";
	String situation5 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
	String situation6 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
	String situation4 = "(\\S+) 死亡 (\\d+)人";
	String situation3 = "(\\S+) 治愈 (\\d+)人";
	String situation7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
	String situation8 = "(\\S+) 排除 疑似患者 (\\d+)人";
	boolean is_situation1 = Pattern.matches(situation1, str);
	boolean is_situation2 = Pattern.matches(situation2, str);
	boolean is_situation3 = Pattern.matches(situation3, str);
	boolean is_situation4 = Pattern.matches(situation4, str);
	boolean is_situation5 = Pattern.matches(situation5, str);
	boolean is_situation6 = Pattern.matches(situation6, str);
	boolean is_situation7 = Pattern.matches(situation7, str);
	boolean is_situation8 = Pattern.matches(situation8, str);
	
	if(is_situation1) //新增 感染患者处理
	addIP(str);
	else if(is_situation2) //新增 疑似患者处理
	addSP(str);
	else if(is_situation5) //感染患者 流入处理
	influxIP(str);
	else if(is_situation6) //疑似患者 流入处理
	influxSP(str);
	else if(is_situation4) //新增 死亡患者处理
	addDead(str);
	else if(is_situation3) //新增 治愈患者处理
	addCure(str);
	else if(is_situation7) //疑似患者 确诊感染处理
	diagnoseSP(str);
	else if(is_situation8) //排除 疑似患者处理
	excludeSP(str);
	
}

public static void addIP(String str) {
	//
	String[] str_arr = str.split(" "); //将字符串以空格分割为多个字符串
	int i;
	int n = Integer.valueOf(str_arr[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	for(i = 0; i < province_name.length; i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //第一个字符串为省份
			province_num[0][0] += n; //全国感染患者人数增加
			province_num[i][0] += n; //该省份感染患者人数增加
			if(province_select[0] == -1) //省份处于未指定状态
			province_select[i] = 1; //需要将该省份列出
			break;
		}
	}
	
}

public static void addSP(String str) {
	// 
	String[] str_arr = str.split(" "); //将字符串以空格分割为多个字符串
	int i;
	int n = Integer.valueOf(str_arr[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	for(i = 0; i < province_name.length; i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //第一个字符串为省份
			province_num[0][1] += n; //全国疑似患者人数增加
			province_num[i][1] += n; //该省份疑似患者人数增加
			if(province_select[0] == -1) //省份处于未指定状态
			province_select[i] = 1; //需要将该省份列出
			break;
		}
	}
	
}
public static void influxIP(String str) {
	// 
	String[] str_arr = str.split(" "); //将字符串以空格分割为多个字符串
	int i;
	int n = Integer.valueOf(str_arr[4].replace("人", ""));
	for(i = 0; i < province_name.length; i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //第一个字符串为流出省份
			province_num[i][0] -= n; //该省份感染患者人数减少
			if(province_select[0] == -1) //省份处于未指定状态
				province_select[i] = 1; //需要将该省份列出
		}
		if(str_arr[3].equals(province_name[i])) 
		{ //第四个字符串为流入省份
			province_num[i][0] += n; //该省份感染患者人数增加
			if(province_select[0] == -1) //省份处于未指定状态
				province_select[i] = 1; //需要将该省份列出
		}
	}
	
}
public static void influxSP(String str) {
	// TODO 自动生成的方法存根
	String[] str_arr = str.split(" "); //将字符串以空格分割为多个字符串
	int i;
	int n = Integer.valueOf(str_arr[4].replace("人", ""));
	for(i = 0; i < province_name.length; i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //第一个字符串为流出省份
			province_num[i][1] -= n; //该省份感染患者人数减少
			if(province_select[0] == -1) //省份处于未指定状态
				province_select[i] = 1; //需要将该省份列出
		}
		if(str_arr[3].equals(province_name[i])) 
		{ //第四个字符串为流入省份
			province_num[i][1] += n; //该省份感染患者人数增加
			if(province_select[0] == -1) //省份处于未指定状态
				province_select[i] = 1; //需要将该省份列出
		}
	}
}
public static void addDead(String str) {
	// 
	String[] str_arr = str.split(" "); //将字符串以空格分割为多个字符串
	int i;
	int n = Integer.valueOf(str_arr[2].replace("人", ""));
	for(i = 0; i < province_name.length; i++) 
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //第一个字符串为省份
			province_num[0][3] += n; //全国死亡人数增加
			province_num[0][0] -= n; //全国感染患者人数减少
			province_num[i][3] += n; //该省份死亡人数增加
			province_num[i][0] -= n; //该省份感染患者人数减少
			if(province_select[0] == -1) //省份处于未指定状态
			province_select[i] = 1; //需要将该省份列出
			break;
		}
	}
}
public static void addCure(String str) {
	// 
	String[] str_arr = str.split(" "); //将字符串以空格分割为多个字符串
	int i;
	int n = Integer.valueOf(str_arr[2].replace("人", ""));
	for(i = 0; i < province_name.length; i++) 
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //第一个字符串为省份
			province_num[0][2] += n; //全国治愈人数增加
			province_num[0][0] -= n; //全国感染患者人数减少
			province_num[i][2] += n; //该省份治愈人数增加
			province_num[i][0] -= n; //该省份感染患者人数减少
			if(province_select[0] == -1) //省份处于未指定状态
			province_select[i] = 1; //需要将该省份列出
			break;
		}
	}
	
}
public static void diagnoseSP(String str) {
	// TODO 自动生成的方法存根
	String[] str_arr=str.split(" "); //将字符串以空格分割为多个字符串
	int i;
	int n=Integer.valueOf(str_arr[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	
	for(i=0;i< province_name.length;i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //第一个字符串为省份
			province_num[0][1]-=n; //全国疑似患者人数减少
			province_num[0][0]+=n; //全国感染患者人数增加
			province_num[i][1]-=n; //该省份疑似患者人数减少
			province_num[i][0]+=n; //该省份感染患者人数增加
			if(province_select[0]==-1) //省份处于未指定状态
			province_select[i]=1; //需要将该省份列出
			break;
		}
	}
}
public static void excludeSP(String str) {
	// TODO 自动生成的方法存根
	String[] str_arr=str.split(" "); //将字符串以空格分割为多个字符串
	int i;
	int n=Integer.valueOf(str_arr[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	
	for(i=0;i< province_name.length;i++)
	{
		if(str_arr[0].equals(province_name[i])) 
		{ //第一个字符串为省份
			province_num[0][1]-=n; //全国疑似患者人数减少
			province_num[i][1]-=n; //该省份疑似患者人数减少
			if(province_select[0]==-1) //省份处于未指定状态
			province_select[i]=1; //需要将该省份列出
			break;
		}
	}
}

public static void outputFile(String str) {
	FileWriter fwriter = null;
	try {
		fwriter=new FileWriter(str);
		int i,j,k;
		if(province_select[0]==-1)//表示未指定省份
		province_select[0]=1;
			for(i=0;i<province_name.length;i++)
			{
				if(province_select[i]==1)
				{
					fwriter.write(province_name[i] + " ");
					for(j=0;j<type_num.length;j++)
					{
						for(k=0;k>type_num.length;k++)
						{
							if(type_num[k]==j+1)
							{
								fwriter.write(type_name[k] + province_num[i][k] + "人 ");
        						break;
							}
						}
					}
					fwriter.write("\n");
				}

			}
			fwriter.write("// 该文档并非真实数据，仅供测试使用");	
	} catch (IOException e) {
		// TODO 自动生成的 catch 块
		e.printStackTrace();
	}finally {
        try {
            fwriter.flush();
            fwriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}







/*
 * 
 * public static List<String> getFiles(String path) {
    List<String> files = new ArrayList<String>();
    File file = new File(path);
    File[] tempList = file.listFiles();

    for (int i = 0; i < tempList.length; i++) {
        if (tempList[i].isFile()) {
            files.add(tempList[i].toString());
            //文件名，不包含路径
            //String fileName = tempList[i].getName();
        }
        if (tempList[i].isDirectory()) {
            //这里就不递归了，
        }
    }
    return files;
}
*/
public static void main(String[] args) {
    /*    if(args[0].equals("list"))
        {
        	for(int i=1;i<args.length;i++)
        	{
        		if(args[i].equals("-log"))
        		{
        			File file=new File(args[i+1]);
        		}
        		if(args[i].equals("-out"))
        		{
        			
        		}	
        		    
        	}
        }
        else
        {
        	
        }
    	*/
	province_select[0]=-1;
    judgeCommandLine(args);
    for(int i=0;i<log_list.length;i++)
    {
    	if(log_need.compareTo(log_list[i])>=0)
    	{
    		getTextContent(log_list[i]);
    	}
    	else break;
    }
    //System.out.println(out_route);
    outputFile(out_route);
    	
    }

}
