import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InfectStatistic 
{
	public static String inputPath = "";
	public static String outputPath = "";
	public static String [] provinceList = null;
	public static String [] typeList = null;
	public static Date inputDate = new Date();
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static String initMaxDateString = "1900-01-01";
	public static String now = "";
	public static int nowIndex = 0;
	
	
	public class Province
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
	public void ipIncreace(Province p, int num)
	{
		p.ip += num;
	}
	
	//新增疑似
	public void spIncreace(Province p, int num)
	{
		p.sp += num;
	}
	
	//治愈患者
	public void cureIncreace(Province p, int num)
	{
		p.cure += num;
		p.ip -= num;
	}
	
	//患者死亡
	public void deadIncreace(Province p, int num)
	{
		p.dead += num;
		p.ip -= num;
	}
	
	//疑似患者确诊
	public void spToIp(Province p, int num)
	{
		p.ip += num;
		p.sp -= num;
	}
	
	//排除疑似患者
	public void spDecrease(Province p, int num)
	{
		p.sp -= num;
	}

	//p1省确诊患者流入p2省
	public void ipMoveTo(Province p1, Province p2, int num)
	{
		p1.ip -= num;
		p2.ip += num;
	}
	
	//p1省疑似患者流入p2省
	public void spMoveTo(Province p1, Province p2, int num)
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
	public static void readInfo(String path, Date date)
	{
		String [] list = new File(path).list();
		int compareTo = 0;
		for(int i=0;i<list.length;i++)
		{
			try 
			{
				Date current = format.parse(list[i].substring(0, 10));
				compareTo = date.compareTo(current);
				if (compareTo > 0)
				{
					String filePath = path + list[i];
					File file = new File(filePath);
					try (BufferedReader br = new BufferedReader(new FileReader(file));)
					{
				        String line = null;
				        String [] info = null;
				        while ((line = br.readLine()) != null)
				        {
				            line = line.trim();
				            info = line.split(" ");
				            if(info[0].substring(0, 2).equals("//"))
				            {
				            	break;
				            }
				            else if(info.length==3)
				            {
				            	
				            }
				            else if(info.length==4)
				            {
				            	
				            }
				            else if(info.length==5)
				            {
				            	
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
			}
		}
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		int number = args.length;
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
			
		}
		
		
		
		
		
		
	}
}












