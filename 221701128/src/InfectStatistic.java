
public class InfectStatistic {
	
	public static int i;
	public static int typeCount;
	public static int provinceCount;
	public static String fileDirect;
	public static String outputFilepath;
	public static String dateTime;
	public static String typePeople[];
	public static String province[];
	
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
		
		else if(str[i].equalsIgnoreCase("-type"))
		{
			readType(str);
		}
		
		else if(str[i].equalsIgnoreCase("-type"))
		{
			readType(str);
		}
		
		else if(str[i].equalsIgnoreCase("-province"))
		{
			readProvince(str);
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
	
	public static void readType (String str[])
	{
		typePeople = new String[4];
		if(i == str.length - 1 || str[i+1].substring(0,1).equals("-"))
		{
			typePeople[0] = "all";
			typeCount++;
		}
		
		else
		{
			i++;			
			typeCount = 0;
			for(  ; i < str.length ; i++)
			{	
				if(str[i].equalsIgnoreCase("ip") 
					|| str[i].equalsIgnoreCase("sp") 
					|| str[i].equalsIgnoreCase("cure") 
					|| str[i].equalsIgnoreCase("dead") )
				typePeople[typeCount++] = str[i];
				
				if(i == str.length - 1 || str[i+1].substring(0,1).equals("-"))  break;
			}
		}
		
		for(int k=0;k<typeCount;k++)
		System.out.println(typePeople[k]);
	}
	
	public static void readProvince (String str[])
	{
		province = new String[31];
		if(i == str.length - 1 || str[i+1].substring(0,1).equals("-"))
		{
			province[0] = "全国";
		    provinceCount++;
		}
		
		else
		{
			i++;			
			provinceCount = 0;
			for(  ; i < str.length ; i++)
			{	
				province[provinceCount++] = str[i];
				
				if(i == str.length - 1 || str[i+1].substring(0,1).equals("-"))  break;
			}
		}
		
		for(int k=0;k<provinceCount;k++)
		System.out.println(province[k]);
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
