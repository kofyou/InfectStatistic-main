/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


class InfectStatistic {
	
	 static String [] areas= {"安徽","北京","重庆","福建","甘肃","广东","广西","贵州",
			 "海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁",
			 "内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏",
			 "新疆","云南","浙江",};
	 	 
	 static String [] types= {"ip","sp","cure","dead"};
	
	public static void infectInto(Area source,Area destination, int num)//确诊患者从source流入destination
	{
		source.decreaseInfect(num);
		destination.addInfect(num);
	}
	
	public static void suspectInto(Area source,Area destination, int num)//疑似患者从source流入destination
	{
		source.decreaseSuspect(num);
		destination.addSuspect(num);
	}
	
	public static Date getLatestDate(File directory)
	{
		File[] logs=directory.listFiles();
        
        Date latestDate=getLogDate(logs[0].getName());
        for (int i=1,length=logs.length;i<length;i++)//求出日志中最晚的一天
        {
         	
         	Date temp=getLogDate(logs[i].getName());
         	if (temp.compareTo(latestDate)>0)
            {
         		latestDate=temp;
            }
        }
        return latestDate;
	}
	
	public static Date getLogDate(String logName)
	{
		int logYear=Integer.parseInt(logName.substring(0, 4));
        int logMonth=Integer.parseInt(logName.substring(5, 7));
        int logDay=Integer.parseInt(logName.substring(8,10));
        
        return (new Date(logYear-1900, logMonth-1, logDay));
	}
	
	public static Area getCountry(Map<String, Area> map)
	{
		
		int infectSum=0;//全国所有的感染患者
        int suspectSum=0;//全国所有疑似患者
        int cureSum=0;//全国所有治愈
        int deathSum=0;//全国所有死亡
		
		for (Map.Entry<String, Area> entry : map.entrySet()) //统计全国疫情
        {
    		infectSum+=entry.getValue().getInfectNum();
    		suspectSum+=entry.getValue().getSuspectNum();
    		cureSum+=entry.getValue().getCureNum();
    		deathSum+=entry.getValue().getDeathNum();
        }
		return (new Area(infectSum,suspectSum,cureSum,deathSum));
	}
	
	
    public static void main(String[] args) 
    {
    	
    	Map<String, Area> map = new HashMap<String,Area>(); 
    	
    	String logPath=null;
		String outputPath=null;
		String dateString=null;
		
		boolean setProvince=false;//记录指令是否有-province参数
		boolean setType=false;//记录指令是否有-type参数
		boolean setCountry=false;//记录-province参数是否包含全国
		
		File directory=null;//日志文件夹
		File output=null;//输出文件
		
		Date date=null;//记录指令是否含有-date参数，若有则date为该参数的日期
		
		List<String> type = new ArrayList<>();//指令中若含有-type参数则记录需要统计那些疫情

       
        
        Area country=null;
        
        for (int i=0,length=areas.length;i<length;i++)
        {
        	map.put(areas[i], new Area());
        }
        
      
        for (int i=0,length=args.length;i<length;i++)
        {
        	if (args[i].equals("-log"))//-log参数
        	{
        		logPath=args[i+1];
        		int position=logPath.lastIndexOf('\\');
                
                logPath.substring(0, position);
                directory = new File(logPath);
	            if (!directory.isDirectory())
	            {
	                System.out.println("日志文件错误或不存在日志文件");
	                System.exit(1);
	            }
        		i++;
        	}
        	else if (args[i].equals("-out"))//-out参数
        	{
        		outputPath=args[i+1];
        		output= new File(outputPath);
    		    if(!outputPath.matches("[A-z]:\\\\(.+?\\\\)*.+?.txt"))//用正则表达式判断是否合法
    	        {
    				System.out.println("输出文件路径错误");
    				System.exit(1);
    			}
    		    if (!output.exists())//如果输出文件不存在则创建它
    	        {
    	        	try 
    	        	{
    					output.createNewFile();
    				} 
    	        	catch (IOException e) 
    	        	{
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}        
    	        }
        		i++;
        	}
        	else if (args[i].equals("-date"))//-date参数
        	{
        		dateString=args[i+1];
        		
        		try
        		{
        			int year=Integer.parseInt(dateString.substring(0, 4));
                    int month=Integer.parseInt(dateString.substring(5, 7));
                    int day=Integer.parseInt(dateString.substring(8,10));
                    
                    date=new Date(year-1900, month-1, day);
        		}
        		catch(StringIndexOutOfBoundsException e)
        		{
        			System.out.println("-date参数错误");
        			System.exit(1);
        		}
        		
                i++;
        	}
        	else if (args[i].equals("-type"))//-type参数
        	{
        			setType=true;
        	}
        	else if (Arrays.asList(types).contains(args[i]))
        	{
        		if (setType==true)
        		{
        			type.add(args[i]);
        		}
        		else
        		{
        			System.out.println("请使用-type参数");
        			System.exit(1);
        		}
        	}
        	else if (args[i].equals("-province"))//-province参数
        	{
        		setProvince=true;
        	}
        	else if (Arrays.asList(areas).contains(args[i]))
        	{
        		if (setProvince==true)
        		{
        			map.get(args[i]).setIsRelate();
        		}
        		else
        		{
        			System.out.println("请使用-province参数");
        			System.exit(1);
        		}
        	}
        	else if (args[i].equals("全国"))
        	{
        		if (setProvince==true)
        		{
        			setCountry=true;
        		}
        		else
        		{
        			System.out.println("请使用-province参数");
        			System.exit(1);
        		}
        	}
        	else
        	{
        		if (!args[i].equals("list"))
        		{
        			System.out.println("无法识别参数："+args[i]);
        			System.exit(1);
        		}
        	}
        }
        
        if (setProvince==false)
		{
			setCountry=true;
		}
        
        
        File[] logs=directory.listFiles();
        
        if (date!=null)
        {
             Date latestDate=getLatestDate(directory);
             if (date.compareTo(latestDate)>0)//如果-date日期比日志中最晚一天还要晚则报错
             {
             	System.out.println("-date超出范围");
             	System.exit(1);
             }
        }
       
        try 
        {
        	for (int i=0,length=logs.length;i<length;i++)
        	{
        		if (date!=null)
        		{
                    Date logDate=getLogDate(logs[i].getName());
                    
                    if (logDate.compareTo(date)>0)//如果某一日志文件比-date日期晚，则跳过不统计该日志
                    {
                    	continue;
                    }
        		}
        		
        		InputStreamReader reader=new InputStreamReader(new FileInputStream(logs[i]),"utf-8");
    			BufferedReader bufferedReader = new BufferedReader(reader);
    	        String str;
    	        
    			while ((str = bufferedReader.readLine()) != null) 
    			{
    				
    				if (str.startsWith("//")||str.equals(""))//改行为空或者是注释则跳过
    				{
    					continue;
    				}
    				    				
    				String[] splitLine=str.split(" "); 
    				
    				if (setProvince==false)
    				{
    					map.get(splitLine[0]).setIsRelate();
    				}
    				
    				int num=Integer.parseInt(splitLine[splitLine.length-1].substring(0, splitLine[splitLine.length-1].length()-1));			
    				
    				if (splitLine[1].equals("新增"))
    				{
    					if (splitLine[2].equals("感染患者"))
    					{
    						map.get(splitLine[0]).addInfect(num);
    					}
    					else
    					{
    						map.get(splitLine[0]).addSuspect(num);
    					}
    				}
    				else if (splitLine[1].equals("感染患者"))
    				{
    					infectInto(map.get(splitLine[0]),map.get(splitLine[3]),num);
    				}
    				else if (splitLine[1].equals("疑似患者"))
    				{
    					if (splitLine[2].equals("流入"))
    					{
    						suspectInto(map.get(splitLine[0]),map.get(splitLine[3]),num);
    					}
    					else
    					{
    						map.get(splitLine[0]).suspectToInfect(num);
    					}
    				}
    				else if (splitLine[1].equals("治愈"))
    				{
    					map.get(splitLine[0]).cure(num);
    				}
    				else if (splitLine[1].equals("死亡"))
    				{
    					map.get(splitLine[0]).death(num);
    				}
    				else if (splitLine[1].equals("排除"))
    				{
    					map.get(splitLine[0]).exclude(num);
    				}
    				
    			}
    	        bufferedReader.close();
        	}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        country=getCountry(map);
        
        try {
			
        	OutputStreamWriter bufferedWriter=new OutputStreamWriter(new FileOutputStream(output),"utf-8");

			if (setType==false)//如果指令没有-type参数则输出所有类型疫情
			{
				if (setCountry==true)
				{
					bufferedWriter.write("全国 感染患者"+country.getInfectNum()
					+"人 疑似患者"+country.getSuspectNum()
					+"人 治愈"+country.getCureNum()
					+"人 死亡"+country.getDeathNum()+"人\n");
				}
				
				for (int i=0,length=areas.length;i<length;i++)
				{
					if (map.get(areas[i]).getIsRelate()==true)
					{
						bufferedWriter.write(areas[i]+" 感染患者"+map.get(areas[i]).getInfectNum()
								+"人 疑似患者"+map.get(areas[i]).getSuspectNum()
								+"人 治愈"+map.get(areas[i]).getCureNum()
								+"人 死亡"+map.get(areas[i]).getDeathNum()+"人\n");
					}
				}
			}
			else//如果指令有-type参数则输出指定类型疫情
			{				
				if (setCountry==true)
				{
					bufferedWriter.write("全国");
					for (String item : type) 
					{
						bufferedWriter.write(country.outputType(item));
			        }
					bufferedWriter.write("\n");
				}
				
				for (int i=0,length=areas.length;i<length;i++)
				{
					if (map.get(areas[i]).getIsRelate()==true)
					{
						bufferedWriter.write(areas[i]);
						for (String item : type) 
						{
							bufferedWriter.write(map.get(areas[i]).outputType(item));
				        }
						bufferedWriter.write("\n");
					}
				}
			}
			
			bufferedWriter.write("// 该文档并非真实数据，仅供测试使用");
			
			bufferedWriter.flush();
			bufferedWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        System.out.println("");
    }
}


class Area{
	private int infectNum=0;//感染患者
	private int suspectNum=0;//疑似患者
	private int cureNum=0;//治愈
	private int deathNum=0;//死亡
	private boolean isRelate=false;
	
	Area(int infect,int suspect,int cure,int death)
	{
		infectNum=infect;
		suspectNum=suspect;
		cureNum=cure;
		deathNum=death;
	}
	
	Area()
	{
		infectNum=0;
		suspectNum=0;
		cureNum=0;
		deathNum=0;
	}
	
	public int getInfectNum()
	{
		return infectNum;
	}
	
	public int getSuspectNum()
	{
		return suspectNum;
	}
	
	public int getCureNum()
	{
		return cureNum;
	}
	
	public int getDeathNum()
	{
		return deathNum;
	}
	
	public boolean getIsRelate()
	{
		return isRelate;
	}
	
	public void setIsRelate()
	{
		if (isRelate==false)
		isRelate=true;
	}
	
	public void addInfect(int num)//新增感染患者
	{
		infectNum+=num;
	}
	
	public void decreaseInfect(int num)//减少感染患者
	{
		infectNum-=num;
	}
	
	public void addSuspect(int num)//新增疑似患者
	{
		suspectNum+=num;
	}
	
	public void decreaseSuspect(int num)//减少疑似患者
	{
		suspectNum-=num;
	}
	
	public void cure(int num)//治愈
	{
		cureNum+=num;
		infectNum-=num;
	}
	
	public void death(int num)//死亡
	{
		deathNum+=num;
		infectNum-=num;
	}
	
	public void suspectToInfect(int num)//疑似转为确诊感染
	{
		suspectNum-=num;
		infectNum+=num;
	}
	
	public void exclude(int num)//排除疑似
	{
		suspectNum-=num;
	}
	
	public String outputType(String type)//输出type类型的疫情
	{
		if (type.equals("ip"))
		{
			return " 感染患者"+infectNum+"人";
		}
		else if (type.equals("sp"))
		{
			return " 疑似患者"+suspectNum+"人";
		}
		else if (type.equals("cure"))
		{
			return " 治愈"+cureNum+"人";
		}
		else
		{
			return " 死亡"+deathNum+"人";
		}
	}
}
	





