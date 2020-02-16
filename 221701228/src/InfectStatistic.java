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
	public static String now = "";
	public static int nowIndex = 0;
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
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
	}

}

