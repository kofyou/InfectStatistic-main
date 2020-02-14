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
import java.util.HashMap;
import java.util.TreeMap;


class InfectStatistic {
	
	 static String [] areas= {"安徽","北京","重庆","福建","甘肃","广东","广西","贵州",
			 "海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁",
			 "内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏",
			 "新疆","云南","浙江",};
	 
	 static String [] parameters= {"-log","-out","-date","-type","-province"};
	 
	 
	
	
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
		String date=null;

//        if (args.length!=0)
//    	{
//    		for (int i=0;i<args.length;i++)
//    		{
//    			System.out.println(args[i]);
//    		}
//    			
//    	}
//        else
//    	{
//    		System.out.println("没有参数");
//    	}
        
        for (int i=0;i<args.length;i++)
        {
        	if (args[i].equals("-log"))
        	{
        		logPath=args[i+1];
        	}
        	
        	if (args[i].equals("-out"))
        	{
        		outputPath=args[i+1];
        	}
        	
        }
        
        
//        System.out.println(logPath+"    "+outputPath);
        
        
        
        int infectSum=0;
        int suspectSum=0;
        int cureSum=0;
        int deathSum=0;
        
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
        
        
        
        
        for (Map.Entry<String, Area> entry : map.entrySet()) {
//        	if (entry.getValue().isRelate==true)
//        	  System.out.println("Key = " + entry.getKey() + ", Value = " );
        	}
        
        
        int position=logPath.lastIndexOf('/');
        
       
        
        logPath.substring(0, position);
        File dir = new File(logPath);
        
        
//        if (dir.isDirectory())
//        {
//            System.out.println("是目录");
//        }
//        else
//        {
//            System.out.println("不是目录");
//        }
        
        File[] logs=dir.listFiles();
        
//        for(int i=0;i<logs.length;i++)
//        {
//        	System.out.println(logs[i].getName());
//        	
//        }
        
        try {
        	
        	for (int i=0;i<logs.length;i++)
        	{
        		InputStreamReader reader=new InputStreamReader(new FileInputStream(logs[i]),"utf-8");
//        		FileReader fileReader=new FileReader(logs[i]);
    			BufferedReader bufferedReader = new BufferedReader(reader);
    	        String str;
    	        
    			while ((str = bufferedReader.readLine()) != null) {
    				
//    			    System.out.println(str);
    				
    				String[] splitLine=str.split(" "); 
    				
    				 
    				 
    				
    				map.get(splitLine[0]).setIsRelate();
    				
//    				for (int j=0;j<splitLine.length;j++)
//    				{
//    					System.out.print(splitLine[j]);
//    				}
//    				System.out.println();
    				
    				int num=Integer.parseInt(splitLine[splitLine.length-1].substring(0, splitLine[splitLine.length-1].length()-1));
    				
//    				System.out.println(num);
    				
    				
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
        	if (entry.getValue().getIsRelate()==true)
        	{
        		        		
        		
        		infectSum+=entry.getValue().getInfectNum();
        		suspectSum+=entry.getValue().getSuspectNum();
        		cureSum+=entry.getValue().getCureNum();
        		deathSum+=entry.getValue().getDeathNum();
        		

        	}
        }
        
//        System.out.println("Key = 全国, "
//      	  		+ "Value ：确诊" +infectSum
//      	  		+" 疑似" +suspectSum
//      	  		+" 治愈" +cureSum
//      	  		+" 死亡" +deathSum);
        
        
        for (Map.Entry<String, Area> entry : map.entrySet()) 
        {
        	if (entry.getValue().getIsRelate()==true)
        	{

//          	  System.out.println("Key = " + entry.getKey() + ", "
//          	  		+ "Value ：确诊" +entry.getValue().getInfectNum()
//          	  		+" 疑似" +entry.getValue().getSuspectNum()
//          	  		+" 治愈" +entry.getValue().getCureNum()
//          	  		+" 死亡" +entry.getValue().getDeathNum());
        	}
        }
        

//        System.out.println(args[6]);
        
        File output = new File(outputPath);
        
      
        
        if (output.exists())
        {
//        	System.out.println(""+output.getName());
        }
        else
        {
//        	System.out.println("不存在输出文件");
        	try {
				output.createNewFile();
//				System.out.println("新创建的文件"+output.getName());
				
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
        
        
        try {
			FileWriter fileWriter = new  FileWriter(output);
//			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
			
        	OutputStreamWriter bufferedWriter=new OutputStreamWriter(new FileOutputStream(output),"utf-8");

			
			
			bufferedWriter.write("全国 感染患者"+infectSum+"人 疑似患者"+suspectSum
					+"人 治愈"+cureSum+"人 死亡"+deathSum+"人\n");
			
//			bufferedWriter.newLine();
			
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
			
			
			
			
			
			
			
			
			
			
			
			bufferedWriter.flush();
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        
        
        
//        System.out.println("命令错误");
    }
}


class Area{
	private int infectNum=0;
	private int suspectNum=0;
	private int cureNum=0;
	private int deathNum=0;
	private boolean isRelate=false;
	
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
	
}
	





