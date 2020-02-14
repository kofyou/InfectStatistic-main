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
        
        
        int position=args[4].lastIndexOf('/');
        
       
        
        args[4].substring(0, position);
        File dir = new File(args[4]);
        
        
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
        
        File output = new File(args[6]);
        
      
        
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
			
			
			
			if (map.get("安徽").getIsRelate()==true)
			{
				bufferedWriter.write("安徽 感染患者"+map.get("安徽").getInfectNum()
						+"人 疑似患者"+map.get("安徽").getSuspectNum()
						+"人 治愈"+map.get("安徽").getCureNum()
						+"人 死亡"+map.get("安徽").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("北京").getIsRelate()==true)
			{
				bufferedWriter.write("北京 感染患者"+map.get("北京").getInfectNum()
						+"人 疑似患者"+map.get("北京").getSuspectNum()
						+"人 治愈"+map.get("北京").getCureNum()
						+"人 死亡"+map.get("北京").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("重庆").getIsRelate()==true)
			{
				bufferedWriter.write("重庆 感染患者"+map.get("重庆").getInfectNum()
						+"人 疑似患者"+map.get("重庆").getSuspectNum()
						+"人 治愈"+map.get("重庆").getCureNum()
						+"人 死亡"+map.get("重庆").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("福建").getIsRelate()==true)
			{
				bufferedWriter.write("福建 感染患者"+map.get("福建").getInfectNum()
						+"人 疑似患者"+map.get("福建").getSuspectNum()
						+"人 治愈"+map.get("福建").getCureNum()
						+"人 死亡"+map.get("福建").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("甘肃").getIsRelate()==true)
			{
				bufferedWriter.write("甘肃 感染患者"+map.get("甘肃").getInfectNum()
						+"人 疑似患者"+map.get("甘肃").getSuspectNum()
						+"人 治愈"+map.get("甘肃").getCureNum()
						+"人 死亡"+map.get("甘肃").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("广东").getIsRelate()==true)
			{
				bufferedWriter.write("广东 感染患者"+map.get("广东").getInfectNum()
						+"人 疑似患者"+map.get("广东").getSuspectNum()
						+"人 治愈"+map.get("广东").getCureNum()
						+"人 死亡"+map.get("广东").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("广西").getIsRelate()==true)
			{
				bufferedWriter.write("广西 感染患者"+map.get("广西").getInfectNum()
						+"人 疑似患者"+map.get("广西").getSuspectNum()
						+"人 治愈"+map.get("广西").getCureNum()
						+"人 死亡"+map.get("广西").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("贵州").getIsRelate()==true)
			{
				bufferedWriter.write("贵州 感染患者"+map.get("贵州").getInfectNum()
						+"人 疑似患者"+map.get("贵州").getSuspectNum()
						+"人 治愈"+map.get("贵州").getCureNum()
						+"人 死亡"+map.get("贵州").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("海南").getIsRelate()==true)
			{
				bufferedWriter.write("海南 感染患者"+map.get("海南").getInfectNum()
						+"人 疑似患者"+map.get("海南").getSuspectNum()
						+"人 治愈"+map.get("海南").getCureNum()
						+"人 死亡"+map.get("海南").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("河北").getIsRelate()==true)
			{
				bufferedWriter.write("河北 感染患者"+map.get("河北").getInfectNum()
						+"人 疑似患者"+map.get("河北").getSuspectNum()
						+"人 治愈"+map.get("河北").getCureNum()
						+"人 死亡"+map.get("河北").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("河南").getIsRelate()==true)
			{
				bufferedWriter.write("河南 感染患者"+map.get("河南").getInfectNum()
						+"人 疑似患者"+map.get("河南").getSuspectNum()
						+"人 治愈"+map.get("河南").getCureNum()
						+"人 死亡"+map.get("河南").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("黑龙江").getIsRelate()==true)
			{
				bufferedWriter.write("黑龙江 感染患者"+map.get("黑龙江").getInfectNum()
						+"人 疑似患者"+map.get("黑龙江").getSuspectNum()
						+"人 治愈"+map.get("黑龙江").getCureNum()
						+"人 死亡"+map.get("黑龙江").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("湖北").getIsRelate()==true)
			{
				bufferedWriter.write("湖北 感染患者"+map.get("湖北").getInfectNum()
						+"人 疑似患者"+map.get("湖北").getSuspectNum()
						+"人 治愈"+map.get("湖北").getCureNum()
						+"人 死亡"+map.get("湖北").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("湖南").getIsRelate()==true)
			{
				bufferedWriter.write("湖南 感染患者"+map.get("湖南").getInfectNum()
						+"人 疑似患者"+map.get("湖南").getSuspectNum()
						+"人 治愈"+map.get("湖南").getCureNum()
						+"人 死亡"+map.get("湖南").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("吉林").getIsRelate()==true)
			{
				bufferedWriter.write("吉林 感染患者"+map.get("吉林").getInfectNum()
						+"人 疑似患者"+map.get("吉林").getSuspectNum()
						+"人 治愈"+map.get("吉林").getCureNum()
						+"人 死亡"+map.get("吉林").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("江苏").getIsRelate()==true)
			{
				bufferedWriter.write("江苏 感染患者"+map.get("江苏").getInfectNum()
						+"人 疑似患者"+map.get("江苏").getSuspectNum()
						+"人 治愈"+map.get("江苏").getCureNum()
						+"人 死亡"+map.get("江苏").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("江西").getIsRelate()==true)
			{
				bufferedWriter.write("江西 感染患者"+map.get("江西").getInfectNum()
						+"人 疑似患者"+map.get("江西").getSuspectNum()
						+"人 治愈"+map.get("江西").getCureNum()
						+"人 死亡"+map.get("江西").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("辽宁").getIsRelate()==true)
			{
				bufferedWriter.write("辽宁 感染患者"+map.get("辽宁").getInfectNum()
						+"人 疑似患者"+map.get("辽宁").getSuspectNum()
						+"人 治愈"+map.get("辽宁").getCureNum()
						+"人 死亡"+map.get("辽宁").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("内蒙古").getIsRelate()==true)
			{
				bufferedWriter.write("内蒙古 感染患者"+map.get("内蒙古").getInfectNum()
						+"人 疑似患者"+map.get("内蒙古").getSuspectNum()
						+"人 治愈"+map.get("内蒙古").getCureNum()
						+"人 死亡"+map.get("内蒙古").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("宁夏").getIsRelate()==true)
			{
				bufferedWriter.write("宁夏 感染患者"+map.get("宁夏").getInfectNum()
						+"人 疑似患者"+map.get("宁夏").getSuspectNum()
						+"人 治愈"+map.get("宁夏").getCureNum()
						+"人 死亡"+map.get("宁夏").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("青海").getIsRelate()==true)
			{
				bufferedWriter.write("青海 感染患者"+map.get("青海").getInfectNum()
						+"人 疑似患者"+map.get("青海").getSuspectNum()
						+"人 治愈"+map.get("青海").getCureNum()
						+"人 死亡"+map.get("青海").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("山东").getIsRelate()==true)
			{
				bufferedWriter.write("山东 感染患者"+map.get("山东").getInfectNum()
						+"人 疑似患者"+map.get("山东").getSuspectNum()
						+"人 治愈"+map.get("山东").getCureNum()
						+"人 死亡"+map.get("山东").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("山西").getIsRelate()==true)
			{
				bufferedWriter.write("山西 感染患者"+map.get("山西").getInfectNum()
						+"人 疑似患者"+map.get("山西").getSuspectNum()
						+"人 治愈"+map.get("山西").getCureNum()
						+"人 死亡"+map.get("山西").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("陕西").getIsRelate()==true)
			{
				bufferedWriter.write("陕西 感染患者"+map.get("陕西").getInfectNum()
						+"人 疑似患者"+map.get("陕西").getSuspectNum()
						+"人 治愈"+map.get("陕西").getCureNum()
						+"人 死亡"+map.get("陕西").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("上海").getIsRelate()==true)
			{
				bufferedWriter.write("上海 感染患者"+map.get("上海").getInfectNum()
						+"人 疑似患者"+map.get("上海").getSuspectNum()
						+"人 治愈"+map.get("上海").getCureNum()
						+"人 死亡"+map.get("上海").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("四川").getIsRelate()==true)
			{
				bufferedWriter.write("四川 感染患者"+map.get("四川").getInfectNum()
						+"人 疑似患者"+map.get("四川").getSuspectNum()
						+"人 治愈"+map.get("四川").getCureNum()
						+"人 死亡"+map.get("四川").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("天津").getIsRelate()==true)
			{
				bufferedWriter.write("天津 感染患者"+map.get("天津").getInfectNum()
						+"人 疑似患者"+map.get("天津").getSuspectNum()
						+"人 治愈"+map.get("天津").getCureNum()
						+"人 死亡"+map.get("天津").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("西藏").getIsRelate()==true)
			{
				bufferedWriter.write("西藏 感染患者"+map.get("西藏").getInfectNum()
						+"人 疑似患者"+map.get("西藏").getSuspectNum()
						+"人 治愈"+map.get("西藏").getCureNum()
						+"人 死亡"+map.get("西藏").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("新疆").getIsRelate()==true)
			{
				bufferedWriter.write("新疆 感染患者"+map.get("新疆").getInfectNum()
						+"人 疑似患者"+map.get("新疆").getSuspectNum()
						+"人 治愈"+map.get("新疆").getCureNum()
						+"人 死亡"+map.get("新疆").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("云南").getIsRelate()==true)
			{
				bufferedWriter.write("云南 感染患者"+map.get("云南").getInfectNum()
						+"人 疑似患者"+map.get("云南").getSuspectNum()
						+"人 治愈"+map.get("云南").getCureNum()
						+"人 死亡"+map.get("云南").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
			}
			if (map.get("浙江").getIsRelate()==true)
			{
				bufferedWriter.write("浙江 感染患者"+map.get("浙江").getInfectNum()
						+"人 疑似患者"+map.get("浙江").getSuspectNum()
						+"人 治愈"+map.get("浙江").getCureNum()
						+"人 死亡"+map.get("浙江").getDeathNum()+"人\n");
//				bufferedWriter.newLine();
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
	





