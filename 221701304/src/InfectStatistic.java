package test;

import java.io.*;
import java.text.Collator;
import java.util.*;
import java.util.regex.Pattern;


public class InfectStatistic {
	static String[] arr=new String[1024];//数组存储日志内容
	static String log_path;//指定日志目录的位置
	static String out_path;//指定输出文件路径和文件名
	static String date;
	static String type="ipspcuredead";
	static String list_province;
	static boolean log_flag=false;
	static boolean out_flag=false;
	static boolean date_flag=false;
	static boolean type_flag=false;
	static boolean province_flag=false;
	static int sum_ip=0;
	static int sum_sp=0;
	static int sum_cure=0;
	static int sum_dead=0;
	
	static Map<String, Province> province=new HashMap<String,Province>();
	static Province qg=new Province();
	static Province ah=new Province();
	static Province am=new Province();
	static Province bj=new Province();
	static Province cq=new Province();
	static Province fj=new Province();
	static Province gs=new Province();
	static Province gd=new Province();
	static Province gx=new Province();
	static Province gz=new Province();
	static Province hainan=new Province();
	static Province hebei=new Province();
	static Province henan=new Province();
	static Province hlj=new Province();
	static Province hubei=new Province();
	static Province hunan=new Province();
	static Province jl=new Province();
	static Province js=new Province();
	static Province jx=new Province();
	static Province ln=new Province();
	static Province nmg=new Province();
	static Province nx=new Province();
	static Province qh=new Province();
	static Province sd=new Province();
	static Province shanxi=new Province();
	static Province shanxi2=new Province();
	static Province sh=new Province();
	static Province sc=new Province();
	static Province tw=new Province();
	static Province tj=new Province();
	static Province xz=new Province();
	static Province xg=new Province();
	static Province xj=new Province();
	static Province yn=new Province();
	static Province zj=new Province();
	public  static void readFile(String str) {
		File file= new File(str);
		File[] filelist=file.listFiles();
		try {
				if(date_flag)
				{
					String max=(filelist[1].getName()).substring(0,10);
					for (int i= 0;i<filelist.length;i++) 
					{
						
						if ((((filelist[i].getName()).substring(0,10)).compareTo(max))>0) {
							max=(filelist[i].getName()).substring(0,10);	
						}
						
						if ((((filelist[i].getName()).substring(0,10)).compareTo(date))<=0) 
						{
							BufferedReader br=new BufferedReader(new FileReader(filelist[i]));
							String line;
							while(!(line=br.readLine()).contains("//"))
							{
								String[]data=line.split(" ");
								if (data[2].equals("流入")) 
								{
									Province pv1=province.get(data[0]);
									Province pv2=province.get(data[3]);
									if(data[1].equals("感染患者"))
									{
										String num=Pattern.compile("[^0-9]").matcher(data[4]).replaceAll("");
										pv1.ip=pv1.ip-Integer.parseInt(num);
										pv2.ip=pv2.ip+Integer.parseInt(num);
									}
									else if(data[1].equals("疑似患者")){
										String num=Pattern.compile("[^0-9]").matcher(data[4]).replaceAll("");
										pv1.sp=pv1.sp-Integer.parseInt(num);
										pv2.sp=pv2.sp+Integer.parseInt(num);
									}	
									
								}
								else {
									Province pv=province.get(data[0]);
									if(data[1].equals("新增"))
									{
										if(data[2].equals("感染患者"))
										{
											String num=Pattern.compile("[^0-9]").matcher(data[3]).replaceAll("");
											pv.ip=pv.ip+Integer.parseInt(num);
										}
										else if(data[2].equals("疑似患者")){
											String num=Pattern.compile("[^0-9]").matcher(data[3]).replaceAll("");
											pv.sp=pv.sp+Integer.parseInt(num);
										}	
									}
									else if (data[1].equals("治愈")) {
										String num=Pattern.compile("[^0-9]").matcher(data[2]).replaceAll("");
										pv.cure=pv.cure+Integer.parseInt(num);
										pv.ip=pv.ip-Integer.parseInt(num);
										
				
									}
									else if (data[1].equals("死亡")) {
										String num=Pattern.compile("[^0-9]").matcher(data[2]).replaceAll("");
										pv.dead=pv.dead+Integer.parseInt(num);
										pv.ip=pv.ip-Integer.parseInt(num);
				
									}
									else if (data[1].equals("疑似患者")) {
										String num=Pattern.compile("[^0-9]").matcher(data[3]).replaceAll("");
										pv.sp=pv.sp-Integer.parseInt(num);
										pv.ip=pv.ip+Integer.parseInt(num);
				
									}
									else if (data[1].equals("排除")) {
										String num=Pattern.compile("[^0-9]").matcher(data[3]).replaceAll("");
										pv.sp=pv.sp-Integer.parseInt(num);
				
									}
								}
							}
							br.close();
							
						}
						
					}
					if (max.compareTo(date)<0) {
						System.out.println("日期超出范围");
						return;
					}	
				}
				else {
					int i=0;
					while(filelist[i].exists())
					{
							BufferedReader br=new BufferedReader(new FileReader(filelist[i]));
							String line;
							while(!(line=br.readLine()).contains("//"))
							{
								String[]data=line.split(" ");
								if (data[2].equals("流入")) 
								{
									Province pv1=province.get(data[0]);
									Province pv2=province.get(data[3]);
									if(data[1].equals("感染患者"))
									{
										String num=Pattern.compile("[^0-9]").matcher(data[4]).replaceAll("");
										pv1.ip=pv1.ip-Integer.parseInt(num);
										pv2.ip=pv2.ip+Integer.parseInt(num);
									}
									else if(data[1].equals("疑似患者")){
										String num=Pattern.compile("[^0-9]").matcher(data[4]).replaceAll("");
										pv1.sp=pv1.sp-Integer.parseInt(num);
										pv2.sp=pv2.sp+Integer.parseInt(num);
									}	
									
								}
								else {
									Province pv=province.get(data[0]);
									if(data[1].equals("新增"))
									{
										if(data[2].equals("感染患者"))
										{
											String num=Pattern.compile("[^0-9]").matcher(data[3]).replaceAll("");
											pv.ip=pv.ip+Integer.parseInt(num);
										}
										else if(data[2].equals("疑似患者")){
											String num=Pattern.compile("[^0-9]").matcher(data[3]).replaceAll("");
											pv.sp=pv.sp+Integer.parseInt(num);
										}	
									}
									else if (data[1].equals("治愈")) {
										String num=Pattern.compile("[^0-9]").matcher(data[2]).replaceAll("");
										pv.cure=pv.cure+Integer.parseInt(num);
										pv.ip=pv.ip-Integer.parseInt(num);
										
				
									}
									else if (data[1].equals("死亡")) {
										String num=Pattern.compile("[^0-9]").matcher(data[2]).replaceAll("");
										pv.dead=pv.dead+Integer.parseInt(num);
										pv.ip=pv.ip-Integer.parseInt(num);
				
									}
									else if (data[1].equals("疑似患者")) {
										String num=Pattern.compile("[^0-9]").matcher(data[3]).replaceAll("");
										pv.sp=pv.sp-Integer.parseInt(num);
										pv.ip=pv.ip+Integer.parseInt(num);
				
									}
									else if (data[1].equals("排除")) {
										String num=Pattern.compile("[^0-9]").matcher(data[3]).replaceAll("");
										pv.sp=pv.sp-Integer.parseInt(num);
				
									}
								}
							}
							br.close();
							i++;
					}
					
					
				}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void saveData() {
		Collection<String> set=province.keySet();
		List<String> list=new ArrayList<String>(set);
		Collections.sort(list);
		Collections.sort(list, new Comparator<String>() {  
            @Override  
            public int compare(String o1, String o2) {  
                Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);  
                return com.compare(o1, o2);  
 
            }  
        });  
		list.add(0, "全国");
		province.put("全国", qg);
		for(int i=0;i<list.size();i++)//计算全国的人数
		{
		
			sum_ip=sum_ip+province.get(list.get(i)).ip;
			sum_sp=sum_sp+province.get(list.get(i)).sp;
			sum_cure=sum_cure+province.get(list.get(i)).cure;
			sum_dead=sum_dead+province.get(list.get(i)).dead;
		}
		qg.ip=sum_ip;
		qg.sp=sum_sp;
		qg.cure=sum_cure;
		qg.dead=sum_dead;
			int k=0;
				for(int i=0;i<list.size();i++)//将信息读入arr数组
				{
					String str=list.get(i);
					if (type.contains("ip")) {
						str=str+" 感染患者"+province.get(list.get(i)).ip+"人";
					}
					if(type.contains("sp"))
					{
						str=str+" 疑似患者"+province.get(list.get(i)).sp+"人";
			
					}
					if(type.contains("cure"))
					{
						str=str+" 治愈"+province.get(list.get(i)).cure+"人";
					}
					if (type.contains("dead")) 
					{
						str=str+" 死亡"+province.get(list.get(i)).dead+"人";
					
					}
					if (((province.get(list.get(i)).ip)>0)||((province.get(list.get(i)).sp)>0)||((province.get(list.get(i)).sp)>0)
							||((province.get(list.get(i)).sp)>0)) 
					{
						arr[k]=str;
						k++;
					}
					
				}
				
		
	}
	public  static void writeFile(String str) {
		try {
			OutputStream os=new FileOutputStream(str);
			PrintWriter pw=new PrintWriter(os);
			for(int i=0;arr[i]!=null;i++)
			{
				pw.println(arr[i]);
			}
			pw.println("// 该文档并非真实数据，仅供测试使用");
			pw.close();
			os.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	public static void main(String[] args){
		province.put("安徽", ah);
		province.put("澳门", am);
		province.put("北京", bj);
		province.put("重庆", cq);
		province.put("福建", fj);
		province.put("甘肃", gs);
		province.put("广东", gd);
		province.put("广西", gx);
		province.put("贵州", gz);
		province.put("海南", hainan);
		province.put("河北", hebei);
		province.put("河南", henan);
		province.put("黑龙江", hlj);
		province.put("湖北", hubei);
		province.put("湖南", hunan);
		province.put("吉林", jl);
		province.put("江苏", js);
		province.put("江西", jx);
		province.put("辽宁", ln);
		province.put("内蒙古", nmg);
		province.put("宁夏", nx);
		province.put("青海", qh);
		province.put("山东", sd);
		province.put("山西", shanxi);
		province.put("陕西", shanxi2);
		province.put("上海", sh);
		province.put("四川", sc);
		province.put("台湾", tw);
		province.put("天津", tj);
		province.put("西藏", xz);
		province.put("香港", xg);
		province.put("新疆", xj);
		province.put("云南", yn);
		province.put("浙江", zj);
		
		if(args.length==0||!args[0].equals("list"))
		{
			System.out.println("请输入list命令 支持-log -out -date -type -type ip -province参数");
			return;
		}
		else
		{
			for(int i=1;i<args.length;i++)
			{
				if(args[i].equals("-type"))
				{
					if (((i+1)<args.length)&&(!(args[i+1].equals("-log")))&&(!(args[i+1].equals("-out")))&&(!(args[i+1].equals("-date")))
							&&(!(args[i+1].equals("-province")))) //越界
					{	
						type_flag=true;
						type=args[i+1];
						if (((i+2)<args.length)&&(!(args[i+2].equals("-log")))&&(!(args[i+2].equals("-out")))&&(!(args[i+2].equals("-date")))
							&&(!(args[i+2].equals("-province")))) 
						{
							type=args[i+1]+args[i+2];
							if (((i+3)<args.length)&&(!(args[i+3].equals("-log")))&&(!(args[i+3].equals("-out")))&&(!(args[i+3].equals("-date")))
									&&(!(args[i+3].equals("-province")))) 
								{
									type=args[i+1]+args[i+2]+args[i+3];
								}
						}
					}
				}
						
				
				if(args[i].equals("-log"))
				{
					log_flag=true;
					log_path=args[i+1];
					
				}
				if(args[i].equals("-out"))
				{
					out_flag=true;
					out_path=args[i+1];
				}
				if(args[i].equals("-date"))
				{
	
					if ((i+1)<args.length) //越界
					{	
						if (args[i+1].contains("20")) 
						{
							date_flag=true;
							date=args[i+1];
								
						}
					}
						
				}
				if(args[i].equals("-province"))
				{
					if (((i+1)<args.length)&&(!(args[i+1].equals("-log")))&&(!(args[i+1].equals("-out")))&&(!(args[i+1].equals("-date")))
							&&(!(args[i+1].equals("-province")))) //越界
					{	
						province_flag=true;
						list_province=args[i+1];
						if (((i+2)<args.length)&&(!(args[i+2].equals("-log")))&&(!(args[i+2].equals("-out")))&&(!(args[i+2].equals("-date")))
							&&(!(args[i+2].equals("-province")))) 
						{
							list_province=args[i+1]+args[i+2];
							if (((i+3)<args.length)&&(!(args[i+3].equals("-log")))&&(!(args[i+3].equals("-out")))&&(!(args[i+3].equals("-date")))
									&&(!(args[i+3].equals("-province")))) 
								{
									
									list_province=args[i+1]+args[i+2]+args[i+3];
								}
							
						}
						
					}
				}
			}
		}
			if(!log_flag||!out_flag)
			{	
				System.out.println("请输入必带参数");
				return;
			}
			readFile(log_path);
			saveData();
			writeFile(out_path);
	
		}
	
}
class Province
{
	int ip=0;
	int sp=0;
	int cure=0;
	int dead=0;	
}



