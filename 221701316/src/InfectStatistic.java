import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class InfectStatistic 
{
	
	String[] provinceName = new String[32]; //省份名称
	Integer[] ip = new Integer[32]; //感染患者
	Integer[] sp = new Integer[32]; //疑似患者
	Integer[] cure = new Integer[32]; //治愈
	Integer[] dead = new Integer[32]; //死亡
	
	static String pathString = "./log";   // log的参数值
    
	static String outPathString = "./result/testOutput.txt";    //out的参数值
	
	static String dateString = new String(); //日期参数值，初始值设为当前日期
	
	static String[] typeString = new String[10]; //type的参数值
    
	static String[] provinceString = new String[35];  //province的参数值
    
	static HashMap<String,Integer> map = new HashMap<>(); //cmd命令行的hashmap
	
	static HashMap<String,Integer> provinceMap = new HashMap<>(); //省名称的hashmap
	
	static String[] tString = {"ip","sp","cure","dead"};
	
	static String[] pString = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南",
    		"河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏",
    		"青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"
    };    //省名称的字符串数组
    
	static HashMap<String,Integer> typeCmdMap = new HashMap<>();     //cmd命令行中-type的hashmap
    //用于输出时，判断是否输出的某个情况，如ip、sp、等等
	
	static HashMap<String,Integer> provinceCmdMap = new HashMap<>(); //cmd命令行中-province的hashmap
    //用于输出时，判断是否输出 该省的情况
	
	//初始化
	public InfectStatistic()   
	{
		for(int i = 0;i < 32;i++)
			ip[i] =sp[i] = cure[i] = dead[i] = 0;
		
		pathString = "./log";
		
		outPathString = "./result/testOutput.txt";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = new Date();
		dateString = dateFormat.format(date); //日期参数值，初始值设为当前日期
		
		//将key和value一起放到hashmap中
	    map.put("-log",0);
	   	map.put("-out",1);
	   	map.put("-date",2);
	   	map.put("-type",3);
	   	map.put("-province",4);
		
	   	for(int i = 0;i < pString.length;i++)
		    provinceMap.put(pString[i],i);
	}
	
	//从字符串中提取数字
	public static int GetInteger(String s)  
	{
		String ss = s.substring(0,s.length()-1);
		int ans = Integer.valueOf(ss); 
		return ans;
	}
	
	//处理命令行
	public static void DealCmd(String[] s)
	{
		String[] cmdString = new String[s.length-1];
		int indexOfCmdString[] = {-1,-1,-1,-1,-1};
	    
		//获取cmd命令行中的命令（包括参数名+参数值），并去掉list
	    for(int i = 0;i< cmdString.length;i++){
	    	cmdString[i] = s[i+1];
	    }
	    
	    //eg、indexOfCmdString[3]=8，表示在cmdString下标为8的字符串，
	    //这个字符串在hashmap中的value为3，即key为"-type"
	    for(int i = 0;i < cmdString.length;i++){
	    	if(map.containsKey(cmdString[i]))
	    		indexOfCmdString[map.get(cmdString[i])] = i;
	    }

	    //接下来开始处理各个参数名后面的参数值
	    for(int i = 0;i < indexOfCmdString.length;i++){
	    	if(indexOfCmdString[i] != -1){
	    		if(i == 0) {       //-log
	    			String pathStringReplace=cmdString[indexOfCmdString[i]+1];
	    			pathString=pathStringReplace.replace('/','\\');
	    			
	    		}
	    		else if(i == 1) {  //-out
	    			outPathString=cmdString[indexOfCmdString[i]+1];
	    		}
	    		
	    		else if(i == 2) {  //-date
	    			dateString=cmdString[indexOfCmdString[i]+1];
	    		}
	    		else if(i == 3) {  //-type
	    			String[] ans = new String[10];
	    			int cnt = 0;
	    			for(int j = indexOfCmdString[i] + 1;j < cmdString.length&&!map.containsKey(cmdString[j]);j++)
	    				ans[cnt++] = cmdString[j];
	    			typeString = ans;    
	    		}
	    		else if(i == 4) {  //-province
	    			String[] ans = new String[25];
	    			int cnt = 0;
	    			for(int j = indexOfCmdString[i] + 1;j < cmdString.length&&!map.containsKey(cmdString[j]);j++)
	    				ans[cnt++] = cmdString[j];
	    			provinceString = ans;    
	    		}
	    	}
	    }
	    for(int i = 0;i < typeString.length;i++)      //存储需要输出的某个情况，如ip、sp、等等	
	    	typeCmdMap.put(typeString[i],i);
	    
	    for(int i = 0;i < provinceString.length;i++)  //存储需要输出的省份
	    	provinceCmdMap.put(provinceString[i],i);
		
	}
	
	//该函数读取cmd命令里-log指定的目录下的所有文件名(包括路径)
	public static List<String> GetFiles(String pathString)  
	{ 
        List<String> files = new ArrayList<String>();
        File file = new File(pathString);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) 
        {
            if (tempList[i].isFile())
                files.add(tempList[i].toString());
        }
        return files;
    }
	
	//分行读取所有log文件
    public static ArrayList<String> ReadLog(String pathString,String dateString)
    {
        ArrayList<String> ans = new ArrayList<>();  //返回按行读取的内容
        List<String> filesName = new ArrayList<>();
        filesName = GetFiles(pathString);
        
        for(int i = 0;i < filesName.size();i++)
        {
        	try 
        	{
        	StringBuilder filePathAndNameSB=new StringBuilder(); //这里是将cmd命令里的-date参数和-log参数结合起来
        	filePathAndNameSB.append(pathString);                //形成一个用于判断的条件
        	filePathAndNameSB.append(dateString);                //eg.E:/log/2020-01-23.log.txt
        	filePathAndNameSB.append(".log.txt");                //
        	String filePathAndName = filePathAndNameSB.toString(); //
        	
        	if(filesName.get(i).compareTo(filePathAndName) <= 0)  //比较在cmd命令行指定目录下的文件日期是否早于
            {                                                   //cmd命令指定的日期
        	    
        	    File f = new File(filesName.get(i));
                if(f.isFile()&&f.exists())
            	{
            		InputStreamReader read = new InputStreamReader(new FileInputStream(f),"utf-8");       
            	    BufferedReader reader=new BufferedReader(read);       
            	    String line;       
            	    while ((line = reader.readLine()) != null)        
            	        ans.add(line);   
            	    reader.close();  
            	    read.close();
            	}
        	}
        	}
        	catch (IOException e) 
        	{
    			e.printStackTrace();
    		}
        }
        
        //删除txt中的注释
        Iterator<String> iterator = ans.iterator();  
        while (iterator.hasNext()) 
        {  
            String s1 = iterator.next();  
            String s2 = s1.substring(0,1);
            if (s2.equals("/")) 
                iterator.remove();//使用迭代器的删除方法删除 
        }
    	return ans;
    }
	
    //处理log文件信息
    public void OpLog(String pathString,String dateString,HashMap<String,Integer> provinceMap)
    {
    	//InfectStatistic infectStatistic=new InfectStatistic();
 		ArrayList<String> conString=ReadLog(pathString,dateString);//conString就是ReadLog里的ans
 		
 		for(int i = 0;i < conString.size();i++)
 		{
            String[] s = conString.get(i).split(" ");
            if(s.length == 3)  //第一种情况eachLine数量为3
            {   
            	if(s[1].equals("死亡")) 
            	{
            		ip[provinceMap.get(s[0])] -= GetInteger(s[2]);
            		dead[provinceMap.get(s[0])] += GetInteger(s[2]);
            	}
            	else if(s[1].equals("治愈")) 
            	{
            		ip[provinceMap.get(s[0])] -= GetInteger(s[2]);
            		cure[provinceMap.get(s[0])] += GetInteger(s[2]);
            	}	
            }
            else if( s.length == 5){   //第二中情况eachLine数量为5
            	if(s[1].equals("感染患者")) 
            	{
            		ip[provinceMap.get(s[0])] -= GetInteger(s[4]);
            		ip[provinceMap.get(s[3])] += GetInteger(s[4]);
            	}
            	else if(s[1].equals("疑似患者")) 
            	{
            		sp[provinceMap.get(s[0])] -= GetInteger(s[4]);
            		sp[provinceMap.get(s[3])] += GetInteger(s[4]);
            	}
            }
            else if(s.length == 4)  //第二中情况eachLine数量为4
            {  
            	if(s[1].equals("新增"))
            	{
            		if(s[2].equals("疑似患者")) 
            			sp[provinceMap.get(s[0])] += GetInteger(s[3]);
            		else if(s[2].equals("感染患者"))
            			ip[provinceMap.get(s[0])] += GetInteger(s[3]);
            	}
            	else if(s[1].equals("排除")) 
            		sp[provinceMap.get(s[0])] -= GetInteger(s[3]);
            	else if(s[1].equals("疑似患者")) 
            	{
            		sp[provinceMap.get(s[0])] -= GetInteger(s[3]);
            		ip[provinceMap.get(s[0])] += GetInteger(s[3]);
            	}
            }
            
 		} 
 		for(int i = 1;i < 32;i++) //累加计算全国的情况
	    {
	    	ip[0] += ip[i];
	    	sp[0] += sp[i];
	    	cure[0] += cure[i];
	    	dead[0] += dead[i];
	    }
    }

    //输出到指定文件
    public void Out(String outPathString,String[] pString,HashMap<String,Integer> provinceCmdMap)
    {
    	for(int i = 0;i < 32;i++)
    	{
    		if(provinceCmdMap.containsKey(pString[i]))
    		{
    			String st = pString[i]+" ";                 // 判断需要输出哪些情况，如ip、sp、等等
    			if(typeCmdMap.containsKey(tString[0]))    //
    				st += "感染患者"+ip[i]+"人 ";                 //
    			if(typeCmdMap.containsKey(tString[1]))    //
    				st += "疑似患者"+sp[i]+"人 ";              //
    			if(typeCmdMap.containsKey(tString[2]))    //
    				st += "治愈"+cure[i]+"人 ";                //
    			if(typeCmdMap.containsKey(tString[3]))    //
    				st += "死亡"+dead[i]+"人 ";                 //
    			st += "\n";                                 // 
    			
    			try
    			{
    				File file = new File(outPathString);
    				if(!file.exists()){
    					file.createNewFile();
    				}
    				FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(),true);
    				BufferedWriter bw = new BufferedWriter(fileWriter);
    				bw.write(st);
    				bw.close();
    			}
    			catch(IOException e)
    			{
    				e.printStackTrace();
    			}
    		}
    	}
    }
    

    public static void main(String[] args){
    	
    	//String[] sss = {"list", "-province", "全国" ,"湖北", "福建", "安徽", "北京", "广东", "广西", "黑龙江", "-out", "E:\\out111.txt", "-date", "2020-01-27", "-type", "ip", "sp", "cure", "dead", "-log", "E:\\log\\"};

	    InfectStatistic infectStatistic=new InfectStatistic();
	    DealCmd(args);
	    
	    infectStatistic.OpLog(pathString, dateString, provinceMap);
	    infectStatistic.Out(outPathString, pString, provinceCmdMap);
	}

}
