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
	public static int[] type= {0,0,0,0};
	/*
	 * ip代表感染患者，sp代表疑似患者，cure代表治愈，dead代表死亡
	 */
	public static String[] type_symbol= {"ip","sp","cure","dead"};
	
	public int[] province_num = new int[35];
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
					else if(str[i+j].equals("ip"))
					{
						
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

    
    
    
public static void processingText(String lineTxt) {
	
	
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
    	judgeCommandLine(args);
    	for(int i=0;i<log_list.length;i++)
    	{
    		if(log_need.compareTo(log_list[i])>=0)
    		{
    			getTextContent(log_list[i]);
    		}
    		else break;
    	}
    	
    }

}
