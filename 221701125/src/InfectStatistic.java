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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


class InfectStatistic {
	
	 static String [] areas= {"安徽","北京","重庆","福建","甘肃","广东","广西","贵州",
			 "海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁",
			 "内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏",
			 "新疆","云南","浙江",};
	 
	 static String [] parameters= {"-log","-out","-date","-type","-province"};
	 
	 static String [] types= {"ip","sp","cure","dead"};
	
	public static void infectInto(Area source,Area destination, int num)
	{
		source.decreaseInfect(num);
		destination.addInfect(num);
	}
	
	public static void suspectInto(Area source,Area destination, int num)
	{
		source.decreaseSuspect(num);
		destination.addSuspect(num);
	}
	
	
    public static void main(String[] args) {
    	
    	Map<String, Area> map = new HashMap<String,Area>(); 
    	
    	String logPath=null;
		String outputPath=null;
		String dateString=null;
		
		boolean setProvince=false;
		boolean setType=false;
		
		File directory=null;
		File output=null;
		
		Date date=null;
		
		List<String> type = new ArrayList<>();

        int infectSum=0;
        int suspectSum=0;
        int cureSum=0;
        int deathSum=0;
        
        Area country=null;
        
        map.put("安徽", new Area());
        map.put("北京", new Area());
        map.put("重庆", new Area());
        map.put("福建", new Area());
        map.put("甘肃", new Area());
        map.put("广东", new Area());
        map.put("广西", new Area());
        map.put("贵州", new Area());
        map.put("海南", new Area());
        map.put("河北", new Area());
        map.put("河南", new Area());
        map.put("黑龙江", new Area());
        map.put("湖北", new Area());
        map.put("湖南", new Area());
        map.put("吉林", new Area());
        map.put("江苏", new Area());
        map.put("江西", new Area());
        map.put("辽宁", new Area());
        map.put("内蒙古", new Area());
        map.put("宁夏", new Area());
        map.put("青海", new Area());
        map.put("山东", new Area());
        map.put("山西", new Area());
        map.put("陕西", new Area());
        map.put("上海", new Area());
        map.put("四川", new Area());
        map.put("天津", new Area());
        map.put("西藏", new Area());
        map.put("新疆", new Area());
        map.put("云南", new Area());
        map.put("浙江", new Area());
        
        
        for (int i=0;i<args.length;i++)
        {
        	if (args[i].equals("-log"))
        	{
        		logPath=args[i+1];
        		int position=logPath.lastIndexOf('/');
                
                logPath.substring(0, position);
                directory = new File(logPath);
	            if (!directory.isDirectory())
	            {
	                System.out.println("日志文件错误或不存在日志文件");
	                System.exit(1);
	            }
        		i++;
        	}
        	else if (args[i].equals("-out"))
        	{
        		outputPath=args[i+1];
        		output= new File(outputPath);
    		    if(!outputPath.matches("[A-z]:/(.+?/)*.+?.txt"))
    	        {
    				System.out.println("输出文件路径错误");
    				System.exit(1);
    			}
        		i++;
        	}
        	else if (args[i].equals("-date"))
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
        	else if (args[i].equals("-type"))
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
        	else if (args[i].equals("-province"))
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
        	else
        	{
        		if (!args[i].equals("list"))
        		{
        			System.out.println("无法识别参数："+args[i]);
        			System.exit(1);
        		}
        	}
        }
        
        File[] logs=directory.listFiles();
        
        if (date!=null)
        {
        	 String log=logs[0].getName();
             Date latestDate=new Date(Integer.parseInt(log.substring(0, 4))-1900, 
             		Integer.parseInt(log.substring(5, 7))-1, 
             		Integer.parseInt(log.substring(8,10)));
             for (int i=1;i<logs.length;i++)
             {
             	log=logs[i].getName();
             	Date temp=new Date(Integer.parseInt(log.substring(0, 4))-1900, 
                 		Integer.parseInt(log.substring(5, 7))-1, 
                 		Integer.parseInt(log.substring(8,10)));
             	 if (temp.compareTo(latestDate)>0)
                  {
             		 latestDate=temp;
                  }
             }
             if (date.compareTo(latestDate)>0)
             {
             	System.out.println("-date超出范围");
             	System.exit(1);
             }
        }
       
        
        try {
        	
        	for (int i=0;i<logs.length;i++)
        	{
        		if (date!=null)
        		{
            		String logName=logs[i].getName();
            		int logYear=Integer.parseInt(logName.substring(0, 4));
                    int logMonth=Integer.parseInt(logName.substring(5, 7));
                    int logDay=Integer.parseInt(logName.substring(8,10));
                    
                    Date logDate=new Date(logYear-1900, logMonth-1, logDay);
                    
                    if (logDate.compareTo(date)>0)
                    {
                    	continue;
                    }
        		}
        		
        		
        		InputStreamReader reader=new InputStreamReader(new FileInputStream(logs[i]),"utf-8");
    			BufferedReader bufferedReader = new BufferedReader(reader);
    	        String str;
    	        
    			while ((str = bufferedReader.readLine()) != null) {
    				    				
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
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        for (Map.Entry<String, Area> entry : map.entrySet()) 
        {
        		infectSum+=entry.getValue().getInfectNum();
        		suspectSum+=entry.getValue().getSuspectNum();
        		cureSum+=entry.getValue().getCureNum();
        		deathSum+=entry.getValue().getDeathNum();
        		
        }
        
        country=new Area(infectSum,suspectSum,cureSum,deathSum);
        
        
        

      
        
        
        if (!output.exists())
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
        
        try {
			FileWriter fileWriter = new  FileWriter(output);			
			
        	OutputStreamWriter bufferedWriter=new OutputStreamWriter(new FileOutputStream(output),"utf-8");

			if (type.size()==0)
			{
				bufferedWriter.write("全国 感染患者"+country.getInfectNum()
				+"人 疑似患者"+country.getSuspectNum()
				+"人 治愈"+country.getCureNum()
				+"人 死亡"+country.getDeathNum()+"人\n");
		
				for (int i=0;i<areas.length;i++)
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
			else
			{				
				bufferedWriter.write("全国");
				for (String item : type) 
				{
					bufferedWriter.write(country.outputType(item));
		        }
				for (int i=0;i<areas.length;i++)
				{
					if (map.get(areas[i]).getIsRelate()==true)
					{
						bufferedWriter.write("\n"+areas[i]);
						for (String item : type) 
						{
							bufferedWriter.write(map.get(areas[i]).outputType(item));
				        }
					}
				}
			}
			
			bufferedWriter.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        System.out.println("");
    }
}


class Area{
	private int infectNum=0;
	private int suspectNum=0;
	private int cureNum=0;
	private int deathNum=0;
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
	
	public void addInfect(int num)
	{
		infectNum+=num;
	}
	
	public void decreaseInfect(int num)
	{
		infectNum-=num;
	}
	
	public void addSuspect(int num)
	{
		suspectNum+=num;
	}
	
	public void decreaseSuspect(int num)
	{
		suspectNum-=num;
	}
	
	public void cure(int num)
	{
		cureNum+=num;
		infectNum-=num;
	}
	
	public void death(int num)
	{
		deathNum+=num;
		infectNum-=num;
	}
	
	public void suspectToInfect(int num)
	{
		suspectNum-=num;
		infectNum+=num;
	}
	
	public void exclude(int num)
	{
		suspectNum-=num;
	}
	
	public String outputType(String type)
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
	





