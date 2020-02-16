import java.util.*;

class Province {
	
	private String name;//省份的名字
	private int ip;//感染患者
	private int sp;//疑似患者
	private int cure;//治愈
	private int dead;//死亡
	private int isinlog;//当天是否被改变
	
	public Province(String name)//构造函数
	{
		this.name=name;
		ip=0;
		sp=0;
		cure=0;
		dead=0;
		isinlog=0;
	}
	
	public int getIsinlog()
	{
		return isinlog;
	}
	
	public void setIsinlog()
	{
		isinlog=1;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getIp()
	{
		return ip;
	}
	
	public int getSp()
	{
		return sp;
	}
	
	public int getCure()
	{
		return cure;
	}
	
	public int getDead()
	{
		return dead;
	}
	
	public void addIp(int x)//增加感染者
	{
		ip=ip+x;
	}
	
	public void addSp(int x)//增加疑似患者
	{
		sp=sp+x;
	}
	
	public void addCure(int x)
	{
		cure=cure+x;
	}
	
	public void addDead(int x)
	{
		dead=dead+x;
	}
	
	public void moveIp(int x)
	{
		ip=ip-x;
		if(ip<0)
			ip=0;
	}
	
	public void moveSp(int x)
	{
		sp=sp-x;
		if(sp<0)
			sp=0;
	}
	
	public String ToString()
	{
		return name+" 感染患者"+ip+"人 疑似患者"+sp
				+"人 治愈"+cure+"人 死亡"+dead+"人\n";
	}
	
	public String outIp()
	{
		return "感染患者"+ip+"人";
	}
	
	public String outSp()
	{
		return "疑似患者"+sp+"人";
	}
	
	public String outCure()
	{
		return "治愈"+cure+"人";
	}
	
	public String outDead()
	{
		return "死亡"+dead+"人";
	}
}

public class Lib {
	String date;
	Vector<Province> provinces;
	
	public Lib(String date)
	{
		this.date=date;
		provinces=new Vector<Province>();
		
		provinces.addElement(new Province("全国"));
		provinces.addElement(new Province("安徽"));
		provinces.addElement(new Province("北京"));
		provinces.addElement(new Province("重庆"));
		provinces.addElement(new Province("福建"));
		provinces.addElement(new Province("甘肃"));
		provinces.addElement(new Province("广东"));
		provinces.addElement(new Province("广西"));
		provinces.addElement(new Province("贵州"));
		provinces.addElement(new Province("海南"));
		provinces.addElement(new Province("河北"));
		provinces.addElement(new Province("河南"));
		provinces.addElement(new Province("黑龙江"));
		provinces.addElement(new Province("湖北"));
		provinces.addElement(new Province("湖南"));
		provinces.addElement(new Province("吉林"));
		provinces.addElement(new Province("江苏"));
		provinces.addElement(new Province("江西"));
		provinces.addElement(new Province("辽宁"));
		provinces.addElement(new Province("内蒙古"));
		provinces.addElement(new Province("宁夏"));
		provinces.addElement(new Province("青海"));
		provinces.addElement(new Province("山东"));
		provinces.addElement(new Province("山西"));
		provinces.addElement(new Province("陕西"));
		provinces.addElement(new Province("上海"));
		provinces.addElement(new Province("四川"));
		provinces.addElement(new Province("天津"));
		provinces.addElement(new Province("西藏"));
		provinces.addElement(new Province("新疆"));
		provinces.addElement(new Province("云南"));
		provinces.addElement(new Province("浙江"));
	}
	
	public String getDate()
	{
		return date;
	}
}
