/*
* FileName:InfectStatistic.java
* Author:zyl
* Version:1.0
* Date:2020-2-21
*/


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class InfectStatistic 
{
	public static String pathOfLog=null;  //用于记录日志目录的位置
	public static String pathOfOutput=null;  //用于记录输出文件路径和文件名
	public static String dateString=null;  //用于记录日期
	public static String[] paramentersOfType = new String[4];  //用于记录参数type的参数值
	public static String[] paramentersOfProvince = new String[34];  //用于记录参数province的参数值
	
	//记录list命令各参数是否出现，分别对应参数log、out、date、type、province,出现后相应元素值置为1
    public static int commandArgsFlag[] = {0,0,0,0,0};
    
    public static String[] allLogFilesName;  //记录所有日志文件的文件名
    public static String maxDate=null; //日志目录下的最大日期
    
    public static ArrayList<Province> allProvince = new ArrayList<Province>();  //用于记录省份类的实例对象
    
    public static Nation nation;  //nation的成员变量记录了全国感染人数，疑似人数，治愈人数和死亡人数
    
    /*
    * Description:分析命令行穿给主函数的命令
    * Input:命令行传个主函数的字符串数组args
    * Return:无
    * Others:无
    */ 
	public static void analysisOfCommand(String[] args)
	{
		for (int i = 0;i < args.length;i++)
        {
        	if (args[i].equals("-log"))
        	{
        		commandArgsFlag[0]++;
        		pathOfLog = args[i + 1];
        	}
        	else if (args[i].equals("-out"))
        	{
        		commandArgsFlag[1]++;
        		pathOfOutput = args[i + 1];
        	}
        	else if (args[i].equals("-date"))
            {
        		commandArgsFlag[2]++;
                dateString = args[i + 1];
            }
        	else if (args[i].equals("-type"))
            {
        		commandArgsFlag[3]++;
        		
        		for (int k = 0;k < paramentersOfType.length;k++)
        		{
        			paramentersOfType[k] = new String();
        		}
        		
                int cnt = 0;
                for (int j = i + 1;j < args.length && args[j].startsWith("-") == false;j++)
                {
                	paramentersOfType[cnt++] = args[j];
                }
                
            }
        	else if (args[i].equals("-province"))
            {
                commandArgsFlag[4]++;
                
                for (int k = 0;k < paramentersOfProvince.length;k++)
        		{
                	paramentersOfProvince[k] = new String();
        		}
                
                int count = 0;
                for (int j = i + 1;j < args.length && args[j].startsWith("-") == false;j++)
                {
                	paramentersOfProvince[count++] = args[j];
                }
            }
        }
	}
	
	/*
	* Description:根据命令中指定的日志目录，获取该日志目录下所有文件的名字
	* Input:无
	* Return:无
	* Others:无
	*/ 
	public static void getAllLogFilesName()
	{
		File file = new File(pathOfLog);
        File[] allLogFiles = file.listFiles();   
        allLogFilesName = new String[allLogFiles.length];
        for(int i = 0;i < allLogFiles.length;i++)
        {
        	allLogFilesName[i] = allLogFiles[i].getName();
        }
	}
	
	/*
	* Description:获取所提供日志最新的一天
	* Input:无
	* Return:最新一天日期字符串
	* Others:无
	*/ 
	public static String getMaxDate()
	{
		String theMaxDate =allLogFilesName[0];
		for (int i = 0;i < allLogFilesName.length;i++)
		{
			if (theMaxDate.compareTo(allLogFilesName[i]) < 0)
			{
				theMaxDate = allLogFilesName[i];
			}
		}
		
		int index = theMaxDate.indexOf('.');
		theMaxDate = theMaxDate.substring(0, index);
		
		return theMaxDate;
	}
	
	/*
	* Description:统计省份的感染人数，疑似人数，治愈人数和死亡人数
	* Input:从日志文件读取出的一行信息，该行信息保存为字符串
	* Return:无
	* Others:无
	*/ 
	public static void getProvincialInformation(String oneLineOfFile)
	{
		String[] splitString = oneLineOfFile.split(" ");
		int countOfPeople = getStringNumber(splitString[splitString.length - 1]); 
		
		addToArrayList(splitString[0]);  //将省份加入动态数组
		
		if (splitString.length == 3)
		{
			for (int i = 0;i < allProvince.size();i++)
			{
				if (allProvince.get(i).provinceName.equals(splitString[0]))
				{
					if (splitString[1].equals("死亡"))
					{
						allProvince.get(i).dead += countOfPeople;
						allProvince.get(i).ip -= countOfPeople;
						break;
					}
					else if (splitString[1].equals("治愈"))
					{
						allProvince.get(i).cure += countOfPeople;
						allProvince.get(i).ip -= countOfPeople;
						break;
					}
				}
			}
		}
		else if (splitString.length == 4)
		{
			for (int i = 0;i < allProvince.size();i++)
			{
				if (allProvince.get(i).provinceName.equals(splitString[0]))
				{
					if (splitString[1].equals("新增")
						&& splitString[2].equals("感染患者"))
					{	
						allProvince.get(i).ip += countOfPeople;
						break;
					}
					else if (splitString[1].equals("新增")
						&& splitString[2].equals("疑似患者"))
					{
						allProvince.get(i).sp += countOfPeople;
						break;
					}
					else if (splitString[1].equals("疑似患者")
						&& splitString[2].equals("确诊感染"))
					{
						allProvince.get(i).sp -= countOfPeople;
						allProvince.get(i).ip += countOfPeople;
						break;
					}
					else if (splitString[1].equals("排除")
						&& splitString[2].equals("疑似患者"))
					{
						allProvince.get(i).sp -= countOfPeople;
						break;
					}
				}
			}
		}
		else if (splitString.length == 5)
		{
			addToArrayList(splitString[3]);  //将省份加入动态数组
			
			for (int i = 0;i < allProvince.size();i++)
			{
				if (allProvince.get(i).provinceName.equals(splitString[0]))
				{
					if (splitString[1].equals("感染患者"))
					{
						allProvince.get(i).ip -= countOfPeople;
					}
					else if (splitString[1].equals("疑似患者"))
					{
						allProvince.get(i).sp -= countOfPeople;
					}
				}
				else if (allProvince.get(i).provinceName.equals(splitString[3]))
				{
					if (splitString[1].equals("感染患者"))
					{
						allProvince.get(i).ip += countOfPeople;
					}
					else if (splitString[1].equals("疑似患者"))
					{
						allProvince.get(i).sp += countOfPeople;
					}
				}
			}
		}
	} 
	
	/*
	* Description:提取字符串当中的数字并转换成整数返回
	* Input:一个包含了数字字符的字符串
	* Return:一个整数
	* Others:无
	*/ 
	public static int getStringNumber(String str)
	{
		str = str.trim();
		String numString = "";
		
		if(str != null && !"".equals(str))
		{
			for(int i = 0;i < str.length();i++)
			{
				if(str.charAt(i) >= 48 && str.charAt(i) <= 57)
				{
					numString += str.charAt(i);
				}
			}
		}
		
		return Integer.parseInt(numString);
	} 
	
	/*
	* Description:检验省份是否已存储在动态数组中，如果不存在，则加入
	* Input:记录省份名的字符串
	* Return:无
	* Others:无
	*/ 
	public static void addToArrayList(String provinceName)
	{
		boolean isExist = false;
		
		//检验动态数组中是否有该省份，
        for (int i = 0;i < allProvince.size();i++)
        {
        	if (allProvince.get(i).provinceName.equals(provinceName) == true)
        	{
        		isExist = true;
        	    break;
        	}
        }
        if (isExist == false)//无该省则把该省份加到动态数组中
        {
        	allProvince.add(new Province(provinceName));
        }
	}
	
	/*
	* Description:统计全国的感染人数，疑似人数，治愈人数和死亡人数
	* Input:无
	* Return:无
	* Others:无
	*/ 
	public static void getNationInformation()
	{
		nation = new Nation();
		
		for (int i = 0;i < allProvince.size();i++)
		{
			nation.ip += allProvince.get(i).ip;
			nation.sp += allProvince.get(i).sp;
			nation.cure += allProvince.get(i).cure;
			nation.dead += allProvince.get(i).dead;
		}
	}
	
	/*
	* Description:将统计结果输出到txt文件当中
	* Input:统计结果所在字符串
	* Return:无
	* Others:无
	*/ 
	public static void outputToTxtFile(String resultString)
	{
		File txtFile = new File(pathOfOutput);
		try 
		{
			if(txtFile.exists()) //如果有同名文件，先删除那个文件
			{
				txtFile.delete();
			}
			
			txtFile.createNewFile();
			FileOutputStream writerStream = new FileOutputStream(txtFile);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));

			writer.write(resultString);
			writer.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/*
	* Description:对province的参数值进行升序排序
	* Input:无
	* Return:无
	* Others:无
	*/ 
	public static void paramentersOfProvinceSort()
	{
		int length = 0;
		for (int i = 0;i < paramentersOfProvince.length
			&& paramentersOfProvince[i].equals("") == false;i++)
		{
			length++;
		}
        String[] province= new String[length];
        
        Collator collator = Collator.getInstance(Locale.CHINA);
        
        for (int i = 0;i < length;i++)
        {
        	province[i] = paramentersOfProvince[i];
        }
        List<String> list = Arrays.asList(province);
        Collections.sort(list,collator); 
        
        for (int i = 0;i < length;i++)
        {
        	paramentersOfProvince[i] = list.get(i);
        }
		
	}
	
	/*
	* Description:对存放省份的动态数组按省份进行升序排序
	* Input:无
	* Return:无
	* Others:无
	*/ 
	public static void dynamicSort()
	{
        int length = allProvince.size();
        String[] province= new String[length];
        
        Collator collator = Collator.getInstance(Locale.CHINA);
        
        for (int i = 0;i < length;i++)
        {
        	province[i] = allProvince.get(i).provinceName;
        }
        List<String> list = Arrays.asList(province);
        Collections.sort(list,collator); 
        
        for (int i = 0;i < list.size();i++)
        {
        	for (int j = 0;j < length;j++)
        	{
        		if (allProvince.get(j).provinceName.equals(list.get(i)))
        		{
        			Province temp = allProvince.get(i);
                    allProvince.set(i,allProvince.get(j));
                    allProvince.set(j,temp);
        		}
        	}
        }
        //for(String i:list){  
           // System.out.print(i+"  ");  
        //}


        
        
        //System.out.println(collator.getCollationKey(allProvince.get(0).provinceName));
        
        /*for(int i = 0;i < length-1;i++){
            for(int j = 0;j < length-i-1;j++){
            	CollationKey key1 = collator.getCollationKey(allProvince.get(j).provinceName);
				CollationKey key2 = collator.getCollationKey(allProvince.get(j + 1).provinceName);
                if(key1.compareTo(key2) > 0)
                {
                    //String nameString = allProvince.get(j).provinceName;
                    //int ip = allProvince.get(j).ip;
                    //int sp = allProvince.get(j).sp;
                    //int cure = allProvince.get(j).cure;
                    //int dead = allProvince.get(j).dead;
                    
                    Province temp = allProvince.get(j);
                    allProvince.set(j,allProvince.get(j + 1));
                    allProvince.set(j + 1,temp);
                }
            }
        }*/
    }
	
	
	
    public static void main(String[] args) throws IOException 
    {
    	analysisOfCommand(args);
    	getAllLogFilesName();
    	maxDate=getMaxDate();
    	
    	//检验命令行中是否有输入日期
    	if (dateString == null)  
    	{
    		dateString = maxDate;
    	}
    	
    	//检验日期是否超出范围
    	if (dateString.compareTo(maxDate+".log.txt") > 0)  
    	{
    		System.out.println("日期超出范围！");
    	}
    	else 
    	{
    		//System.out.println("日期没有超出范围");
    		for (int i = 0;i < allLogFilesName.length;i++)
    		{
    			//读取满足日期小于或等于指定日期的文件的每行
    			if (allLogFilesName[i].compareTo(dateString+".log.txt") <= 0)
    			{
    		    	String oneLineOfFile = null;
    		    	try 
    		    	{
    		    		InputStreamReader isr = new InputStreamReader(
    		    			new FileInputStream(pathOfLog+allLogFilesName[i]), "UTF-8");
    	    			BufferedReader br = new BufferedReader(isr);
    		    		while ((oneLineOfFile = br.readLine()) != null 
    		    			&& oneLineOfFile.length() != 0  //不读取空行
    		    			&& oneLineOfFile.startsWith("//") == false)  //不读取注释行
    		    		{
    		    			//System.out.println(oneLineOfFile);
    		    			
    		    			//统计省份的感染人数，疑似人数，治愈人数和死亡人数
    		    			getProvincialInformation(oneLineOfFile);
    		    			
    		    		}
    		    	} 
    		    	catch (FileNotFoundException e) {
    		    		e.printStackTrace();
    		    	}
    			}
    		}
    		
    		getNationInformation(); //统计全国的感染人数，疑似人数，治愈人数和死亡人数
    		
    		if (commandArgsFlag[4] == 1)
    		{
    			for (int i = 0;i < paramentersOfProvince.length 
    				&& paramentersOfProvince[i].equals("") == false;i++)
    			{
    				if (paramentersOfProvince[i].equals("全国") == false)
    				{
    					addToArrayList(paramentersOfProvince[i]);  //将省份加入到动态数组中，如果已存在，则不加入
    				}
    			}
    			paramentersOfProvinceSort();
    		}
    		
    		dynamicSort();  //对动态数组中的省按名字进行升序排序
    		
    		String writeFileString ="";  //用于记录写入输出文件的信息
    		
    		if (commandArgsFlag[4] == 1)  //命令中有带province参数
    		{
    			for (int i = 0;i < paramentersOfProvince.length 
        			&& paramentersOfProvince[i].equals("") == false;i++)
    			{
    				if (paramentersOfProvince[i].equals("全国"))  //province参数值中有全国
    				{
    					writeFileString += "全国";
    					if (commandArgsFlag[3] == 1)  //命令中有带type参数
    					{
    						for (int j = 0;j < paramentersOfType.length 
									&& paramentersOfType[j].equals("") == false;j++)
							{
								if (paramentersOfType[j].equals("ip")) 
								{
									writeFileString += " 感染患者" + nation.ip + "人";
								}
								else if (paramentersOfType[j].equals("sp"))
								{
									writeFileString += " 疑似患者" + nation.sp + "人";
								}
								else if (paramentersOfType[j].equals("cure"))
								{
									writeFileString += " 治愈" + nation.cure + "人";
								}
								else if (paramentersOfType[j].equals("dead"))
								{
									writeFileString += " 死亡" + nation.dead + "人";
								}
							}
    						writeFileString += "\r\n";
    					}
    					else //命令中不带type参数
    					{
        					writeFileString += " 感染患者" + nation.ip + "人"
    			    			+ " 疑似患者" + nation.sp + "人"
    			    			+ " 治愈" + nation.cure + "人"
    			    			+ " 死亡" + nation.dead + "人" + "\r\n";
        				}
    				}
    			}
    			
    			for (int i = 0;i < paramentersOfProvince.length 
            		&& paramentersOfProvince[i].equals("") == false;i++)
    			{
    				if (paramentersOfProvince[i].equals("全国") == false)
    				{
    					for (int j = 0;j < allProvince.size();j++)
    					{
    						if (paramentersOfProvince[i].equals(allProvince.get(j).provinceName))  //在动态数组中找到该省
    						{
    							writeFileString += paramentersOfProvince[i];
    							
    							if (commandArgsFlag[3] == 1)  //命令中有带type参数
    							{
    								for (int k = 0;k < paramentersOfType.length 
    									&& paramentersOfType[k].equals("") == false;k++)
    								{
    									if (paramentersOfType[k].equals("ip")) 
    									{
    										writeFileString += " 感染患者" + allProvince.get(j).ip + "人";
    									}
    									else if (paramentersOfType[k].equals("sp"))
    									{
    										writeFileString += " 疑似患者" + allProvince.get(j).sp + "人";
    									}
    									else if (paramentersOfType[k].equals("cure"))
    									{
    										writeFileString += " 治愈" + allProvince.get(j).cure + "人";
    									}
    									else if (paramentersOfType[k].equals("dead"))
    									{
    										writeFileString += " 死亡" + allProvince.get(j).dead + "人";
    									}
    								}
    								
    								writeFileString += "\r\n";
    							}
    							else  //命令中没有带type参数
    							{
    								writeFileString += " 感染患者" + allProvince.get(j).ip + "人"
    					    			+ " 疑似患者" + allProvince.get(j).sp + "人"
    					    			+ " 治愈" + allProvince.get(j).cure + "人"
    					    			+ " 死亡" + allProvince.get(j).dead + "人" + "\r\n";
								}
    							
    							break;
    						}
    					}
    				}
    			}
    		}
    		else //命令中没带province参数
    		{		
				writeFileString += "全国";
				
    			if (commandArgsFlag[3] == 1)  //命令中有带type参数
				{
					for (int j = 0;j < paramentersOfType.length 
							&& paramentersOfType[j].equals("") == false;j++)
					{
						if (paramentersOfType[j].equals("ip")) 
						{
							writeFileString += " 感染患者" + nation.ip + "人";
						}
						else if (paramentersOfType[j].equals("sp"))
						{
							writeFileString += " 疑似患者" + nation.sp + "人";
						}
						else if (paramentersOfType[j].equals("cure"))
						{
							writeFileString += " 治愈" + nation.cure + "人";
						}
						else if (paramentersOfType[j].equals("dead"))
						{
							writeFileString += " 死亡" + nation.dead + "人";
						}
					}
					writeFileString += "\r\n";
				}
				else //命令中不带type参数
				{
					writeFileString += " 感染患者" + nation.ip + "人"
		    			+ " 疑似患者" + nation.sp + "人"
		    			+ " 治愈" + nation.cure + "人"
		    			+ " 死亡" + nation.dead + "人" + "\r\n";
				}
    			
    			for (int i = 0;i < allProvince.size();i++)
    			{
    				writeFileString += allProvince.get(i).provinceName;
    				if (commandArgsFlag[3] == 1)  //命令中有带type参数
					{
						for (int j = 0;j < paramentersOfType.length 
							&& paramentersOfType[j].equals("") == false;j++)
						{
							if (paramentersOfType[j].equals("ip")) 
							{
								writeFileString += " 感染患者" + allProvince.get(i).ip + "人";
							}
							else if (paramentersOfType[j].equals("sp"))
							{
								writeFileString += " 疑似患者" + allProvince.get(i).sp + "人";
							}
							else if (paramentersOfType[j].equals("cure"))
							{
								writeFileString += " 治愈" + allProvince.get(i).cure + "人";
							}
							else if (paramentersOfType[j].equals("dead"))
							{
								writeFileString += " 死亡" + allProvince.get(i).dead + "人";
							}
						}
						
						writeFileString += "\r\n";
					}
					else  //命令中没有带type参数
					{
						writeFileString += " 感染患者" + allProvince.get(i).ip + "人"
			    			+ " 疑似患者" + allProvince.get(i).sp + "人"
			    			+ " 治愈" + allProvince.get(i).cure + "人"
			    			+ " 死亡" + allProvince.get(i).dead + "人" + "\r\n";
					}
    			}	
			}
    		
    		writeFileString += "// 该文档并非真实数据，仅供测试使用";
    		
    		outputToTxtFile(writeFileString);  //将结果写入指定txt文件
    		
    		System.out.println(writeFileString);
    		
    	}
    }
}

class Province
{
	String provinceName;  //省份名字
	int ip;  //感染患者数目
    int sp;  //疑似患者数目
    int cure;  //治愈数目
    int dead;  //死亡数目
    
    Province(String provinceName)
    {
    	this.provinceName=provinceName;
    	ip=0;
    	sp=0;
    	cure=0;
    	dead=0;
    }
}

class Nation
{
	int ip;  //感染患者数目
    int sp;  //疑似患者数目
    int cure;  //治愈数目
    int dead;  //死亡数目
    
    Nation()
    {
    	ip=0;
    	sp=0;
    	cure=0;
    	dead=0;
    }
}
