import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class InfectStatistic 
{
	public static String inputPath = "";
	public static String outputPath = "";
	public static String [] provinceList = new String[]{"","","","","","","","","","","","","","","","","","","","","",""
			,"","","","","","","","","","","","",""};
	public static String [] typeList = new String[]{"","","",""};
	public static Date inputDate = new Date();
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static String initMaxDateString = "1900-01-01";
	public static String now = "";
	public static int nowIndex = 0;
	
	
	public static class Province
	{
		int ip = 0;
		int sp = 0;
		int cure = 0;
		int dead = 0;
		String name = "";
		Province(String name)
		{
			this.ip = 0;
			this.sp = 0;
			this.cure = 0;
			this.dead = 0;
			this.name = name;
		}
	}
	
	//新增患者
	public static void ipIncrease(Province p, int num)
	{
		p.ip += num;
	}
	
	//新增疑似
	public static void spIncrease(Province p, int num)
	{
		p.sp += num;
	}
	
	//治愈患者
	public static void cureIncrease(Province p, int num)
	{
		p.cure += num;
		p.ip -= num;
	}
	
	//患者死亡
	public static void deadIncrease(Province p, int num)
	{
		p.dead += num;
		p.ip -= num;
	}
	
	//疑似患者确诊
	public static void spToIp(Province p, int num)
	{
		p.ip += num;
		p.sp -= num;
	}
	
	//排除疑似患者
	public static void spDecrease(Province p, int num)
	{
		p.sp -= num;
	}

	//p1省确诊患者流入p2省
	public static void ipMoveTo(Province p1, Province p2, int num)
	{
		p1.ip -= num;
		p2.ip += num;
	}
	
	//p1省疑似患者流入p2省
	public static void spMoveTo(Province p1, Province p2, int num)
	{
		p1.sp -= num;
		p2.sp += num;
	}
	
	//接收命令行参数
	public static void getParameters(String s)
	{
		if(s.equals("-log"))
		{
			now = "log";
			nowIndex = 0;
		}
		else if(s.equals("-out"))
		{
			now = "out";
			nowIndex = 0;
		}
		else if(s.equals("-date"))
		{
			now = "date";
			nowIndex = 0;
		}
		else if(s.equals("-type"))
		{
			now = "type";
			nowIndex = 0;
		}
		else if(s.equals("-province"))
		{
			now = "province";
			nowIndex = 0;
		}
		else
		{
			if(now.equals("type"))
			{
				typeList[nowIndex] = s;
				nowIndex++;
			}
			else if(now.equals("province"))
			{
				provinceList[nowIndex] = s;
				nowIndex++;
			}
			else if(now.equals("log"))
			{
				inputPath = s;
			}
			else if(now.equals("out"))
			{
				outputPath = s;
			}
			else if(now.equals("date"))
			{
				try 
				{
					inputDate = format.parse(s);
				} 
				catch (ParseException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//判断输入的日期是否超出范围
	public static boolean checkDate(String path, Date date)
	{
		String [] list = new File(path).list();
		int compareTo = 0;
		Date maxDate = null;
		try 
		{
			maxDate = format.parse(initMaxDateString);
		} 
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<list.length;i++)
		{
			try 
			{
				Date current = format.parse(list[i].substring(0, 10));
				compareTo = current.compareTo(maxDate);
				if(compareTo > 0)
				{
					maxDate = current;
				}
			} 
			catch (ParseException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(maxDate.compareTo(date)<0)
		{
			return false;
		}
		return true;
	}
	
	//读取目录下的文件
	public static HashMap<String,Province> readInfo(String path, Date date, HashMap<String,Province> map)
	{
		String [] list = new File(path).list();
		int compareTo = 0;
		for(int i=0;i<list.length;i++)                
		{
			try 
			{
				Date current = format.parse(list[i].substring(0, 10));
				compareTo = date.compareTo(current);
				if (compareTo >= 0)
				{
					String filePath = path + list[i];
					File file = new File(filePath);
					FileInputStream fis = new FileInputStream(filePath);
					InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
					try (BufferedReader br = new BufferedReader(isr))
					{
				        String line = null;
				        String [] info = null;
				        String provinceName1 = null;
				        String provinceName2 = null;
				        int operateNum = 0;
				        while ((line = br.readLine()) != null)
				        {
				            line = line.trim();
				            info = line.split(" ");
				            Province p1 = null;
				            Province p2 = null;
				            if(info[0].substring(0, 2).equals("//"))
				            {
				            	break;
				            }
				            else if(info.length==3)
				            {
				            	provinceName1 = info[0];
				            	operateNum = Integer.parseInt(info[2].substring(0, info[2].length()-1));
				            	if(map.containsKey(provinceName1))
			            		{
			            			p1 = map.get(provinceName1);
			            		}
			            		else
			            		{
			            			p1 = new Province(provinceName1);
			            		}
				            	if(info[1].equals("死亡"))
				            	{
				            		deadIncrease(p1, operateNum);
			            			map.put(provinceName1,p1);
				            	}
				            	else if(info[1].equals("治愈"))
				            	{
				            		cureIncrease(p1, operateNum);
			            			map.put(provinceName1,p1);
				            	}
				            }
				            else if(info.length==4)
				            {
				            	provinceName1 = info[0];
				            	operateNum = Integer.parseInt(info[3].substring(0, info[3].length()-1));
				            	if(map.containsKey(provinceName1))
			            		{
			            			p1 = map.get(provinceName1);
			            		}
			            		else
			            		{
			            			p1 = new Province(provinceName1);
			            		}
				            	if(info[1].equals("新增"))
				            	{
				            		if(info[2].equals("感染患者"))
				            		{
				            			ipIncrease(p1, operateNum);
				            			map.put(provinceName1,p1);
				            		}
				            		else if(info[2].equals("疑似患者"))
				            		{
				            			spIncrease(p1, operateNum);
				            			map.put(provinceName1,p1);
				            		}
				            	}
				            	else if(info[1].equals("排除"))
				            	{
				            		spDecrease(p1, operateNum);
			            			map.put(provinceName1,p1);
				            	}
				            	else if(info[1].equals("疑似患者"))
				            	{
				            		spToIp(p1, operateNum);
				            		map.put(provinceName1,p1);
				            	}
				            }
				            else if(info.length==5)
				            {
				            	provinceName1 = info[0];
				            	provinceName2 = info[3];
				            	operateNum = Integer.parseInt(info[4].substring(0, info[4].length()-1));
				            	if(map.containsKey(provinceName1))
			            		{
			            			p1 = map.get(provinceName1);
			            		}
			            		else
			            		{
			            			p1 = new Province(provinceName1);
			            		}
				            	if(map.containsKey(provinceName2))
			            		{
			            			p2 = map.get(provinceName2);
			            		}
			            		else
			            		{
			            			p2 = new Province(provinceName2);
			            		}
				            	if(info[1].equals("感染患者"))
				            	{
				            		ipMoveTo(p1, p2, operateNum);
				            		map.put(provinceName1,p1);
				            		map.put(provinceName2,p2);
				            	}
				            	else if(info[1].equals("疑似患者"))
			            		{
				            		spMoveTo(p1, p2, operateNum);
				            		map.put(provinceName1,p1);
				            		map.put(provinceName2,p2);
			            		}
				            }
				        }
				    } 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			} 
			catch (ParseException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return map;
	}
	
	//计算全国信息
	public static Province calTotal (HashMap<String,Province> map)
	{
		Province nation = new Province("全国");
		Iterator iter1=map.entrySet().iterator();
        while(iter1.hasNext())
        {
        	Map.Entry<String,Province> entry=(Map.Entry<String,Province>)iter1.next();
        	nation.cure += entry.getValue().cure;
        	nation.ip += entry.getValue().ip;
        	nation.sp += entry.getValue().sp;
        	nation.dead += entry.getValue().dead;
        }
        return nation;
	}
	
	public static void outputInfo (String[] typeList, String[] provinceList, Province nation, String path, HashMap<String,Province> map, Set set)
	{
		FileWriter fileWriter = null;
		String dirPath = "";
		if(path.indexOf("/")>1)
		{
			dirPath = path.substring(0,path.lastIndexOf("/"));
		}
		else 
		{
			dirPath = path.substring(0,path.lastIndexOf("/")+1);
		}
		File dir = new File(dirPath);
		if (!dir.exists()) 
		{
	        dir.mkdirs();
	    }
		File file = new File(path);
		fileWriter = null;
		try 
		{
			if(!file.exists())
			{
				file.createNewFile();
			}
			else 
			{
				file.delete();
				file.createNewFile();
			}
			fileWriter = new FileWriter(file, true);
			if(provinceList[34].equals(""))       //输出全部省份信息
			{
				if(typeList[0].equals(""))
				{
					fileWriter.write(nation.name+" 感染患者"+nation.ip+"人 疑似患者"+nation.sp+"人 治愈"+nation.cure+"人 死亡"+nation.dead+"人\n");
				}
				else 
				{
					fileWriter.write(nation.name);
					for(int i=0;i<typeList.length&&!typeList[i].equals("");i++)
					{
						if(typeList[i].equals("ip"))
						{
							fileWriter.write(" 感染患者"+nation.ip+"人");
						}
						else if(typeList[i].equals("sp"))
						{
							fileWriter.write(" 疑似患者"+nation.sp+"人");
						}
						else if(typeList[i].equals("cure"))
						{
							fileWriter.write(" 治愈"+nation.cure+"人");
						}
						else if(typeList[i].equals("dead"))
						{
							fileWriter.write(" 死亡"+nation.dead+"人");
						}
					}
					fileWriter.write("\n");
				}
				Province p = null;
				for (Object str : set) 
				{  
					String name = (String)str;  
				 	p = map.get(name);
					if(typeList[0].equals(""))
					{
						fileWriter.write(p.name+" 感染患者"+p.ip+"人 疑似患者"+p.sp+"人 治愈"+p.cure+"人 死亡"+p.dead+"人\n");
					}
					else 
					{
						fileWriter.write(p.name);
						for(int i=0;i<typeList.length&&!typeList[0].equals("");i++)
						{
							if(typeList[i].equals("ip"))
							{
								fileWriter.write(" 感染患者"+p.ip+"人");
							}
							else if(typeList[i].equals("sp"))
							{
								fileWriter.write(" 疑似患者"+p.sp+"人");
							}
							else if(typeList[i].equals("cure"))
							{
								fileWriter.write(" 治愈"+p.cure+"人");
							}
							else if(typeList[i].equals("dead"))
							{
								fileWriter.write(" 死亡"+p.dead+"人");
							}
						}
						fileWriter.write("\n");
					}
				} 
			}
			else               //输出部分省份信息
			{
				if(Arrays.asList(provinceList).contains("全国"))
				{
					if(typeList[0].equals(""))
					{
						fileWriter.write(nation.name+" 感染患者"+nation.ip+"人 疑似患者"+nation.sp+"人 治愈"+nation.cure+"人 死亡"+nation.dead+"人\n");
					}
					else 
					{
						fileWriter.write(nation.name);
						for(int j=0;j<typeList.length&&!typeList[j].equals("");j++)
						{
							if(typeList[j].equals("ip"))
							{
								fileWriter.write(" 感染患者"+nation.ip+"人");
							}
							else if(typeList[j].equals("sp"))
							{
								fileWriter.write(" 疑似患者"+nation.sp+"人");
							}
							else if(typeList[j].equals("cure"))
							{
								fileWriter.write(" 治愈"+nation.cure+"人");
							}
							else if(typeList[j].equals("dead"))
							{
								fileWriter.write(" 死亡"+nation.dead+"人");
							}
						}
						fileWriter.write("\n");
					}
				}
				for(int i=0;i<provinceList.length&&!provinceList[34].equals("");i++)
				{
					if(provinceList[i].equals("全国"))
					{
						continue;
					}
					else if(!map.containsKey(provinceList[i])&&!provinceList[i].equals(""))
					{
						if(typeList[0].equals(""))
						{
							fileWriter.write(provinceList[i]+" 感染患者0人 疑似患者0人 治愈0人 死亡0人\n");
						}
						else 
						{
							fileWriter.write(provinceList[i]);
							for(int k=0;k<typeList.length&&!typeList[0].equals("");k++)
							{
								if(typeList[k].equals("ip"))
								{
									fileWriter.write(" 感染患者0人");
								}
								else if(typeList[k].equals("sp"))
								{
									fileWriter.write(" 疑似患者0人");
								}
								else if(typeList[k].equals("cure"))
								{
									fileWriter.write(" 治愈0人");
								}
								else if(typeList[k].equals("dead"))
								{
									fileWriter.write(" 死亡0人");
								}
							}
							fileWriter.write("\n");
						}
					}
					else 
					{
						if(provinceList[i].equals(""))
						{
							continue;
						}
						Province p = null;
						String name = provinceList[i];  
						p = map.get(name);
						if(typeList[0].equals(""))
						{
							fileWriter.write(p.name+" 感染患者"+p.ip+"人 疑似患者"+p.sp+"人 治愈"+p.cure+"人 死亡"+p.dead+"人\n");
						}
						else 
						{
							fileWriter.write(p.name);
							for(int k=0;k<typeList.length&&!typeList[0].equals("");k++)
							{
								if(typeList[k].equals("ip"))
								{
									fileWriter.write(" 感染患者"+p.ip+"人");
								}
								else if(typeList[k].equals("sp"))
								{
									fileWriter.write(" 疑似患者"+p.sp+"人");
								}
								else if(typeList[k].equals("cure"))
								{
									fileWriter.write(" 治愈"+p.cure+"人");
								}
								else if(typeList[k].equals("dead"))
								{
									fileWriter.write(" 死亡"+p.dead+"人");
								}
							}
							fileWriter.write("\n");
						}
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				fileWriter.write("// 该文档并非真实数据，仅供测试使用\n");
				fileWriter.flush();
				fileWriter.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		int number = args.length;
		Province nation = new Province("全国");
		HashMap<String,Province> map =new HashMap<>();
		if(args[0].equals("list"))
		{
			for(int i=1;i<number;i++)
			{
				getParameters(args[i]);
			}
		}
		if(!checkDate(inputPath,inputDate))
		{
			System.out.print("输入的日期超出范围！");
			return ;
		}
		else
		{
			map = readInfo(inputPath, inputDate, map);
			nation = calTotal(map);
		}
		Set set=map.keySet();
	    Object[] arr=set.toArray();
	    Comparator<Object> com=Collator.getInstance(java.util.Locale.CHINA);
	    Arrays.sort(arr);
	    Arrays.sort(provinceList,com);	    
	    outputInfo(typeList, provinceList, nation, outputPath, map, set);
	}
}

