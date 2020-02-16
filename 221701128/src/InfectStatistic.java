
public class InfectStatistic {
	
	public static int i;
	public static String fileDirect;
	public static String outputFilepath;
	
	public static boolean judgeList (String str)
	{
		str=str.substring(0,1);
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
	}
	
	public static void readOutputPath (String str[])
	{
		i++; //读取-out地址
		outputFilepath=str[i]; //保存路径
		System.out.print(outputFilepath);
	}
	
	public static void readLog (String str[])
	{
		i++; //读取-log地址
		fileDirect=str[i]; //保存路径
		System.out.print(fileDirect);
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