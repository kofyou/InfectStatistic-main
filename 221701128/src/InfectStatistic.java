
public class InfectStatistic {
	
	public static int i;
	public static String fileDirect;
	public static String outputFilepath;
	public static String dateTime;
	
	public static boolean judgeList (String str)
	{
		str = str.substring(0,1);
		if(str.equals("-")) return true;
		else return false;
	}
	
	public static void judgeType (String str[])
	{
		if(str[i].equalsIgnoreCase("-log"))
		{
			readLog(str);
		}
		
		else if(str[i].equalsIgnoreCase("-out"))
		{
			readOutputPath(str);
		}
		
		else if(str[i].equalsIgnoreCase("-date"))
		{
			readDateTime(str);
		}
	}
	
	public static void readOutputPath (String str[])
	{
		if(i != str.length - 1) 
		{
			i++; //读取-out地址
			outputFilepath = str[i]; //保存路径
		}
		
		System.out.print(outputFilepath);
	}
	
	public static void readLog (String str[])
	{
		if(i != str.length - 1) 
		{
			i++; //读取-log地址
			fileDirect = str[i]; //保存路径
		}
		
		System.out.print(fileDirect);
	}
	
	public static void readDateTime (String str[])
	{
		if(i == str.length - 1 || str[i+1].substring(0,1).equals("-"))
		{
			dateTime = "latest";
		}
		
		else
		{
			i++;
			dateTime = str[i];
		}
		
		System.out.print(dateTime);
	}
	
	public static void main(String args[])
    {
    	for(i = 0;i < args.length; i++)
    	{
    		if(judgeList(args[i]))
    		{
    			judgeType(args);
    		}
    	}
    	return;
    }

}
