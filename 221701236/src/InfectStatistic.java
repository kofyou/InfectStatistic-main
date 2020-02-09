import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic 
{
	//接收命令行参数
	private String[] arg;
	//是否读取所有日志文件
	private boolean isRead;
	//日志最新日期
	private String date;
	//默认日志传入路径
	private String logPath;
	//默认output文件传入路径
	private String outputPath;
		
	//构造函数
	public InfectStatistic(String[] args)
	{
		isRead = true;
		arg = args;
		logPath = "G:/log/";
		this.init();
	}
		
	//处理日志文件
	public void deal()
	{	    
			
		//读取日志文件		
	    List<String> files = new ArrayList<String>();
	    File file = new File(logPath);
	    File[] tempList = file.listFiles();
	    
	    for (int i = 0; i < tempList.length; i++) 	                
	    {     	
	                  
	        String logDate = new String(tempList[i].getName());	                  
	        String[] sArray = logDate.split("\\.");	                  
	        logDate = new String(sArray[0]);
	                      	                   
	        if (isRead || (logDate.compareTo(date)) <= 0) 	                   
	        {	                 	
	        	//处理该天的日志	              	                        
	        	//files.add(tempList[i].toString());                                      
	        	System.out.println(logDate);	                    
	        }            	                
	    }
	}
		
	//生成output.txt文件
	public void output()
	{
		
	}
		
	//处理"-date"命令
	public void init()
	{
		for(int i=0;i<arg.length;i++)
		{			
			switch(arg[i])
			{
			    case "-date":
				    date = new String(arg[i+1]);
				    isRead = false;	
				    break;
			    case "-log":
				    logPath = new String(arg[i+1]);
				    break;
			    case "-out":
				    outputPath = new String(arg[i+1]);
				    break;
			    default:					
			}			
		}
	}
				
    public static void main(String[] args) 
    {
    	if(args[0].equalsIgnoreCase("list"))
    	{
    	    InfectStatistic l = new InfectStatistic(args);
    	    l.deal();	
    	}
    	else
    	{
    	    System.out.print("未知的命令：" + args[0]);
    	}    		
    }
}

class Province
{
	
}

