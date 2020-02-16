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
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

class InfectStatistic {
	public static String log_route="";//日志文件路径
	public static String out_route;//输出文件路径
	public static String max_date;//
	public static String out_name;//
	public static String[] log_list;//读取到的日志文件列表
    
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
			if(str[i+1].equals("-log")||str[i+1].equals("-out")
			   ||str[i+1].equals("-type")||str[i+1].equals("-province"))
			{
				getMaxDate(log_route);
				System.out.println(log_list[0]);
			}
			else
			{
				if(isLegalDate(str[++i]))
				{
					
				}
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

public static void getMaxDate(String str) {
	log_list=new File(str).list();
	for (int i=0;i<log_list.length-1;i++){
        for (int j=0;j<log_list.length-i-1;j++) {
        	if(log_list[j+1].compareTo(log_list[j])>0){
                String temp=log_list[j];
                log_list[j]=log_list[j+1];
                log_list[j+1]=temp;
            }
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
    	judgeCommandLine(args);
    }

}
