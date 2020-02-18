/**
 * InfectStatistic
 * TODO
 *
 * @author yangmingwei
 * @version 1.0
 * @since 2020/2/15
 */
import java.util.Vector;
import java.io.InputStream;
import java.text.Collator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.io.*;
public class InfectStatistic {
    public static void main(String[] args) {
    	DocFormatter doc=new DocFormatter(args);  //获取命令的字符串不同参数的值的类。
    	Excute excute=new Excute(doc);            //执行命令的类，封装根据doc类读写文件的方法
    	excute.run();							//调用读写的函数。
    }
	/* 用于单元测试
	 * public static Set<String> SortSet(Set<String> set){
		 Set<String> sortSet = new TreeSet<String>(new MapKeyComparator());
		 sortSet.addAll(set);
		 return sortSet;
	}
	public static  Vector<String> getFiles(String date,String source){
		Vector<String> vec =new Vector<>();
		String basePath=source;
		String[] list=new File(basePath).list();
		Vector<String> dates=new Vector<>();
		for(int i=0;i<list.length;i++) dates.add(list[i]);
		dates.add(date+".log.txt");
		Collections.sort(dates);
		int index=dates.lastIndexOf(date+".log.txt");
		if(index==dates.size() - 1&&!(dates.get(index).equals(dates.get(index -1)))) {
			return vec;
		}
		for(int i=0;i<dates.lastIndexOf(date+".log.txt");i++) {
			vec.add(source+dates.get(i));
		}
		return vec;
	}*/

}
/*接收命令字符串数组，通过不同的函数接口返回不同的参数值，保存不同参数的索引号*/
class DocFormatter{
	private String[] cmd;
	private int logPathInd;
	private int outPathInd;
	private int dateInd;
	private int typeInd;
	private int provinceInd;
	private Vector<String> pros;
	DocFormatter(String[] arg){
		pros=new Vector<>();
		init_pro(pros);
		cmd=arg;
        exchange();
	}
	/*获取-log，-type在字符串数组中的索引位置*/
	public void exchange() {           
		dateInd=-1;
		typeInd=-1;
		provinceInd=-1;
		for(int i=0;i<cmd.length;i++) {
			if(cmd[i].equals("-log"))  logPathInd=++i;
			if(cmd[i].equals("-out"))  outPathInd=++i;
			if(cmd[i].equals("-date"))  dateInd=++i;
			if(cmd[i].equals("-province"))  provinceInd=++i;
			if(cmd[i].equals("-type"))  typeInd=++i;
		}
	}
	private void init_pro(Vector<String> vec) {                      
		String[] pros= {"北京","天津","上海","重庆","河北","山西","辽宁","吉林","黑龙江"
				,"江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东"
				,"海南","四川","贵州","云南","陕西","甘肃","青海","台湾","内蒙古","广西"
				,"西藏","宁夏","新疆","香港","澳门","全国"};
		for(int i=0;i<35;i++) {
			vec.add(pros[i]);
		}
	}
	/*获取log文件位置*/
	public String getlogPath() {							
		return cmd[logPathInd];
	}
	/*获取输出文件位置*/
	public String getoutPath() {						
		return cmd[outPathInd];
	}
	/*获取日期*/
	public String getDate() {               
		if(dateInd==-1) return "Latest";
		return cmd[dateInd];
	}
	/*获取-type的参数值*/
	public Vector<String> getType() {						
		Vector<String> type= new Vector<String>();
		Vector<String> vec =new Vector<>();
		vec.add("-log");vec.add("-out");
		vec.add("-date");vec.add("-province");
		if(typeInd!=-1) {
			int i=typeInd;
			while( i<cmd.length &&(!vec.contains(cmd[i]))) {	
				type.add(cmd[i]);		
				i++;
			}	
		}
		return type;
	}
	/*获取-province的参数值,并检验省份的输入是否合法*/
	public Vector<String> getPro() {				
		Vector<String> vec= new Vector<String>();
		Vector<String> vecs =new Vector<>();
		vecs.add("-log");vecs.add("-out");
		vecs.add("-date");vecs.add("-type");
		if(provinceInd!=-1) {
			int i=provinceInd;
			while( i<cmd.length &&(!vecs.contains(cmd[i]))) {	
				if(!pros.contains(cmd[i]))  {
					System.out.println("省份输入错误，为默认输出!");
					vec.clear();
					return vec;
				}
				vec.add(cmd[i]);	
				i++;
			}	
		}
		return vec;
	}	
}
/*保存各个省份的疫情信息，保存DocFormatter类，封装执行读写文件函数和相关的调用函数。*/
 class Excute{
	/*使用map保存疫情信息，ke为省份名称，value为Status记录人数*/
	private Map<String,Status> map;						
	private DocFormatter Formatter;
	private String[] tyChecked;
	private boolean TyChecked;
	private boolean ProChecked;
	private Status Total;
	private Vector<String> pro;
	public Excute(DocFormatter d) {
		Formatter =d;
		ProChecked=false;
		TyChecked=false;
		tyChecked= new String[]{"","","",""};
		Total =new Status("全国");		
		pro=new Vector<>();
		map=new HashMap<String,Status>();
		map.put("全国",Total);
	}
	public void run() {                                    
		for(int i=0;i<Formatter.getType().size();i++) {                   //按-type参数初始化输出格式，并检验-type参数的合法性
			TyChecked=true;
			if(Formatter.getType().get(i).equals("ip")) tyChecked[i]="感染患者";
			else if(Formatter.getType().get(i).equals("sp")) tyChecked[i]="疑似患者";
			else if(Formatter.getType().get(i).equals("dead")) tyChecked[i]="死亡";
			else if(Formatter.getType().get(i).equals("cure")) tyChecked[i]="治愈";
			else {
				System.out.println("测试"+Formatter.getType().get(i));
				System.out.println("-type参数错误，请输入ip、sp、dead或者cure！");
				return ;
			}
		
		}
		for(int i=0;i<Formatter.getPro().size();i++) {            
			ProChecked=true;
			pro.add(Formatter.getPro().get(i));
			
		}
		try{
			String date=Formatter.getDate();                                     //读取文件
			String source=Formatter.getlogPath();
			Vector<String> vec =getFiles(date,source);							//根据日期返回要打开的文件
			if(vec.size()<=0) { 
					System.out.println("打开文件错误，请检查入是否正确！");
					return ;
				}
			for(int i=0;i<vec.size();i++) {
				openFile(vec.get(i));
			}
			
		}
		catch(IOException e) {
			System.out.println("打开文件错误，请检查入是否正确！");
			return ;
		}
		try {
			String dest=Formatter.getoutPath();
			WriteFile(dest);                                       //写入文件
		}
		catch(IOException e) {
			System.out.println("写入文件错误，请检查输入是否正确！");
		}
	}
	private String openFile(String path) throws IOException  {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
		String line=null;
		while((line=br.readLine())!=null) {								//按行读取文件
			String[] arr = line.split(" "); 							// 用  分割文件的每一行，并保存在arr数组中
				if(!map.containsKey(arr[0]) ) {
					map.put(arr[0],new Status(arr[0]));					//根据每行开头的省份创建Status类并放入map中
				}
				Status s=map.get(arr[0]);								//读取arr，检查是否匹配“新增”，“死亡”等情况，修改Status相应变量
				if(arr[1].equals("新增")) {
					if(arr[2].equals("感染患者"))  addIP(s,arr[3]);
					else  addSP(s,arr[3]);
				}
				else if(arr[1].equals("死亡")) {
					addDead(s,arr[2]);
				}
				else if(arr[1].equals("治愈")) {
					addCure(s,arr[2]);
				}
				else if(arr[1].equals("排除")) {
					delSp(s,arr[3]);
				}
				else if(arr[1].equals("疑似患者")) {
					if(arr[2].equals("确诊感染"))  spToip(s,arr[3]);
					else {
						if(!map.containsKey(arr[3]) ) {
							map.put(arr[3],new Status(arr[3]));
						}
						Status t=map.get(arr[3]);
						spRunTo(s,t,arr[4]);
					} 
				}
				else if(arr[1].equals("感染患者")){   
					if(!map.containsKey(arr[3]) ) {
						map.put(arr[3],new Status(arr[3]));
					}
					Status t=map.get(arr[3]);
					ipRunTo(s,t,arr[4]);    }
				else {}
			
		}
		MakeSumUp();
		br.close();					//关闭文件
        return "";
	};
	/*将map写入文件,根据docformatter参数，写入指定文件中*/
	private void WriteFile(String dest) throws IOException{			
		OutputStream os = new FileOutputStream(dest);
		PrintWriter pw=new PrintWriter(os);
		if(ProChecked) {                        			  //检查是否有指定身份
			for(int i=0;i<pro.size();i++) {        			 //补充缺漏
				String s=pro.get(i);
				if(!map.containsKey(s))
					map.put(s, new Status(s));
			}
		}
		Set<String> set=SortSet(map.keySet());
		for(String key : set){
			Status status=map.get(key);
			if(key.equals("//")) continue;
			if(ProChecked) {                        			//检查是否有指定身份
				if(!pro.contains(key)) continue;        		 //非指定身份，continue
			}
			
			if(TyChecked) {
				pw.print(key);
				for(int i=0;i<4;i++) {
					if(tyChecked[i].equals("感染患者")) pw.print(" 感染患者"+status.ip +"人");
					else if(tyChecked[i].equals("疑似患者"))   pw.print(" 疑似患者"+status.sp +"人");
					else if(tyChecked[i].equals("治愈"))   pw.print(" 治愈"+status.cure +"人");
					else if(tyChecked[i].equals("死亡"))   pw.print(" 死亡"+status.dead +"人");
					else 					{}
					
				};
				   
			    pw.print("\n");
			}
			else
		        pw.print(key +" 感染患者"+status.ip+"人 疑似患者"+status.sp+
		 		   "人 治愈"+status.cure+"人 死亡"+status.dead+"人 \n");
		}
		pw.close();
	}
	private Vector<String> getFiles(String date,String source){
		Vector<String> vec =new Vector<>();
		String basePath=source;
		String[] list=new File(basePath).list();
		Vector<String> dates=new Vector<>();
		for(int i=0;i<list.length;i++) dates.add(list[i]);
		dates.add(date+".log.txt");
		Collections.sort(dates);
		int index=dates.lastIndexOf(date+".log.txt");
		if(index==dates.size() - 1&&!(dates.get(index).equals(dates.get(index -1)))) {
			return vec;
		}
		for(int i=0;i<dates.lastIndexOf(date+".log.txt");i++) {
			vec.add(source+dates.get(i));
		}
		return vec;
	}

		
	private void addIP(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.ip+=num;
	}
	private void addSP(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.sp+=num;
	}
	private void ipRunTo(Status from,Status to,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		from.ip-=num;
		to.ip+=num;
	}
	private void spRunTo(Status from,Status to,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		from.sp-=num;
		to.sp+=num;
	}
	private void addDead(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.dead+=num;
		s.ip-=num;
	}
	private void addCure(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.cure+=num;
		s.ip-=num;
	}
	private void spToip(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.ip+=num;
		s.sp-=num;
	}
	private void delSp(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.sp-=num;
	}
	private void add(Status s,Status t) {
		s.ip+=t.ip;
		s.sp+=t.sp;
		s.dead+=t.dead;
		s.cure+=t.cure;
	}
	private void MakeSumUp() {				//统计全国
		map.get("全国").cure=0;
		map.get("全国").ip=0;
		map.get("全国").sp=0;
		map.get("全国").dead=0;
		
		for(String key : map.keySet()) {
			if(key.equals("全国")) continue;
				add(Total,map.get(key));
		}
	}
	public Set<String> SortSet(Set<String> set){
		 Set<String> sortSet = new TreeSet<String>(new MapKeyComparator());
		 sortSet.addAll(set);
		 return sortSet;
	}
	public static Map<String,Status> sortMapByKey(Map<String,Status> map) {
			if (map == null || map.isEmpty()) {
					return null;
				}
			
			Map<String, Status> sortMap = new TreeMap<String, Status>(
			new MapKeyComparator());
			sortMap.putAll(map);
			return sortMap;
			}
	/*封装ip，sp，cure，dead，省份名。*/
	class Status{					
		private String pro;
		private int ip;
		private int sp;
		private int cure;
		private int dead;
		public Status(String s) {
			pro=s;
			ip=sp=cure=dead=0;
		}
	}
}
/*比较器，按拼音顺序返回，“全国”为最优先值*/
class MapKeyComparator implements Comparator<String>{
		public int compare(String str1, String str2) {
			if(str1.equals("全国")) return -99;
			if(str2.equals("全国")) return 99;	
			Collator instance = Collator.getInstance(Locale.CHINA);
			return instance.compare(str1,str2);
        
          }

} 