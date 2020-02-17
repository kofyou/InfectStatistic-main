package InfectStatistic;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.Arrays;

//import InfectStatistic.test.line;
/**
 * InfectStatistic
 * TODO
 *
 * @author 代铭杰
 * @version 1.0
 */
class InfectStatistic {
    static int count=0;//已有所有数据的条数
	static int selcount=0;//所筛选指定省份的个数
	static int seltypecount=0;////所筛选指定类型的个数
	static Province[] all=new Province[34];//初始化结果
	static Province[] result=new Province[34];//总的排序后结果
    static Province[] proresult=new Province[34];//筛选省份后的排序后结果
    static String topath=new String();//输出文档路径
    static String frompath=new String();//log文件路径
    static int index=0;//控制是否输入日期比日志最早一天还早，若是则值为-2
    static int isWrong=0;//输入日期是否出错（输入日期比最新的日志还晚）
     	static class Province {

            /** 省份名称 **/
            String provinceName; 
            /** 感染患者 **/
            int ip; 
            /** 疑似患者* */
            int sp;
            /** 治愈 **/
            int cure;
            /** 死亡 **/
            int dead;

            Province(String provinceName, int ip, int sp, int cure, int dead) {
                this.provinceName = provinceName;
                this.ip = ip;
                this.sp = sp;
                this.cure = cure;
                this.dead = dead;
            }
/********** 功能：打印一条统计疫情信息 输入参数：无 返回值：信息字符串***************/
            String printline() {
            return(provinceName+" 感染患者"+ip+"人 疑似患者"+sp+"人 治愈"+cure+"人 死亡"+dead+"人");
            }

        }
	public static void main(String[] args) throws IOException {
    	if(args.length==0) {
    		System.out.println("输入命令行为空，请重新输入！");
    		return;
    	}
    	if(!args[0].equals("list")) {//命令错误
    		System.out.println("未输入命令‘list’，则不可以带参数，请重新输入！");
    		return;
    	}
	}
		static class cmdArgs {//获取使用命令
	    String[] args;
	    
	    	cmdArgs(String[] passargs) {
	    		args=passargs;
	    	}
/**************功能：判断是否存在某命令   输入参数：需要查验的命令  返回值：该命令的索引int值，如果没有该命令则返回-1***************/
	    	int hasParam(String key) {    	
	    		for(int i=0;i<args.length;i++) {
	    			if(args[i].equals(key)) {
	    				return i;
	    			}
	    		}
	    		return -1;
	    	}    
		}
		/*
          	功能：判断日期的合法性
          	输入参数：最新更新日志的时间，待验证日期字符串
          	返回值：true,false
         */
		static boolean isCorrectdate(String lastdate,String date) {
			if(isBefore(lastdate,date)) {
	    		return false;
	    	}
	    	else {
	    		return true;
	    	}
		}
/*************功能：获取日志文档位置 输入参数：命令行String数组，-log命令所在索引 返回值：无***************************/
	    static void getFrompath(String[] args,int pos) {
	    	int i=pos+1;//获得路径所在索引   
			frompath=args[i];
	    } 
/************功能：检验日志文件所在文档的路径的正确性 输入参数：String日志文件的路径 返回值：boolean********************/
	    static boolean isCorformpath(String path) {
	    	//System.out.println(path.matches("^[A-z]:\\\\(.+?\\\\)*$"));
	    	if(path.matches("^[A-z]:\\\\(.+?\\\\)*$")){//格式正确
	    		File file = new File(path);
	    		if(!file.exists()) {//输入文件夹不存在
	    			System.out.println("输入的日志文件路径不存在，请重新输入命令！");
	    			return false; 
	    		}
	    		else {
	    			String[] filename = file.list();//获取所有日志文件名     	
	    			if(filename.length==0) {//文件夹里没日志
	    				System.out.println("输入的日志文件夹内无内容，请重新输入命令！");
	    				return false;
	    			}
	    			else {
	    				return true;
	    			}
	    		}
	    	}
	    	else {
	    		System.out.println("输入的日志文件路径格式错误，请重新输入命令！");
	    		return false;
	    	}
	    }   
/*******************功能：获取输出文档位置 输入参数：命令行String数组，-out命令所在索引 返回值：无*****************************/
	    static void getTopath(String[] args,int pos) {
	    	int i=pos+1;//获得路径所在索引   
			topath=args[i];
			//System.out.print(topath);
	    }     
	    
/******************功能：比较日期大小 输入参数：两个需要比较的日期字符串 返回值：前<后返回true，前>后返回false*****************************/
		    static boolean isBefore(String date1,String date2) {
		    	if(date1.compareTo(date2)>=0) {
		    		return false;
		    	}
		    	else {
		    		return true;
		    	}
		    }
/*******************功能：获取最新日志的时间  输入参数：无 返回值：日期字符串**************************************/
		    static String getLastdate() {
		    	String date="";
		    	File file = new File(frompath);
		    	String[] filename = file.list();//获取所有日志文件名    
		    	date=filename[filename.length-1].substring(0,10);   
		    	//System.out.print(date);
		    	return date;
   }
/****************** 功能：获取指定日期文件在所有日志中的索引  输入参数：指定日期字符串 返回值：索引int值************/
		    static int findPot(String date) {
		    	File file = new File(frompath);
		        String[] filename = file.list();//获取所有日志文件名      
		        int mid=-1;//中间存储变量，暂存返回值
		        if(isBefore(date,filename[0].substring(0,10))) {//输入日期比日志最早还早
		        	return -2;
		        }
		    	for(int i=0;i<filename.length-1;i++) {
		    		String datecut1=filename[i].substring(0,10);//只获取文件名前的日期
		    		String datecut2=filename[i+1].substring(0,10);//前后两个日期
		    		if(date.equals(datecut1)) {   	   			
		    			mid=i;
		    			return mid;
		    		}
		    		else if(date.equals(datecut2)) {
		    			mid=i+1;
		    			return mid;
		    		}
		    		else if(isBefore(datecut1,date)&&isBefore(date,datecut2)) {//所给日期在两天有记录的日志之间
		    			mid=i;
		    			return mid;
		    		}   		
		    	}    	
		    	return -1;   	
		    }
/*************** 功能：读取log文件  输入参数：指定的输出日期，是否指定输出日期 返回值：无"d:/log/"**********************/
		    static void readLog(String date,boolean hasDate) throws IOException {
		    	//System.out.print("1");
		    	if(hasDate==true) {    			
		    		if(isCorrectdate(getLastdate(),date)) {//检验输入日期正确性
		    			int i=0;//控制日志读取索引
		    			File file = new File(frompath);
		    			String[] filename = file.list();//获取所有日志文件名     	
		    			index=findPot(date);
		    			if(index==-2) {//比最新的日期还早
		    				File f = new File(topath);
		    		        BufferedWriter output = new BufferedWriter(new FileWriter(f,false));
		    		        output.write("无");  
		    		        output.close();
		    			}
		    			else {
			    			while(i<=index) { 
			    				//System.out.print(findPot(date));			
								FileInputStream fs=new FileInputStream(frompath+filename[i]);
							    InputStreamReader is=new InputStreamReader(fs,"UTF-8");
							    BufferedReader br=new BufferedReader(is);
							    String s="";				    
							    while((s=br.readLine())!=null){//一行一行读
							    	if(s.charAt(0)=='/'&&s.charAt(1)=='/') {//排除注释掉的内容
							    		continue;
							    	}
							    	else {
							    		String[] sp =s.split(" ");//分隔开的字符串
							    		statistics(sp,all);
							    	}
							    	//System.out.print(s+"\n");
					    	    }
							    br.close();
							    i++;
					    	}
		    			}
		    		}
		    		else {//日期不正确
		    			isWrong=1;
		    			System.out.print("输入的日期超出范围，请重新命令！");
		    		}
		    	}
		    	else {//没输入指定日期
		    		int i=0;//控制日志读取索引
					File file = new File(frompath);
					String[] filename = file.list();//获取所有日志文件名  
					while(i<filename.length) {   			
						FileInputStream fs=new FileInputStream(frompath+filename[i]);
					    InputStreamReader is=new InputStreamReader(fs,"UTF-8");
					    BufferedReader br=new BufferedReader(is);
					    String s="";				    
					    while((s=br.readLine())!=null){//一行一行读
					    	if(s.charAt(0)=='/'&&s.charAt(1)=='/') {//排除注释掉的内容
					    		continue;
					    	}
					    	else {
					    		String[] sp =s.split(" ");//分隔开的字符串
							    statistics(sp,all);			    		
					    	}
			    	    }
					    br.close();
					    i++;
			    	}   		
		    	}
		    	if(index!=-2&&isWrong!=1) {
		    		printtxt(sortline(all,count));
		    	}
		    }
/******************功能：找出指定地址是否已经存在记录 输入参数：省的名字，总的记录数组 返回值：true,false*************/
		    static boolean isExistlocation(String location,Province[] all) {
		    for(int i=0;i<count;i++) {
		    if(location.equals(all[i].provinceName)) {
		    	return true;
		    }
		    }
		    return false;    	
		    }

/***************** 功能：找出指定地址的记录 输入参数：省的名字，总的记录数组 返回值：一条记录***************/  
		    static Province getLine(String location,Province[] all) {
		    for(int i=0;i<count;i++) {
		    if(location.equals(all[i].provinceName)) {
		    	return all[i];
		    }
		    }
		    return null;//不会用到
		    }

static void statistics(String[] ssp,Province[] all) {   	
	String location="";    	
	location=ssp[0];
	Province Province1;
	if(!isExistlocation(location,all)) {//不存在对应该省的记录
		Province1=new Province(location,0,0,0,0);//新建数据条   		
		all[count]=Province1;
		count++;
	}
	else {
		Province1=getLine(location,all);//获得原有的数据条
	}
	if(ssp[1].equals("新增")) {
		if(ssp[2].equals("感染患者")) {//获得感染人数
			Province1.ip+=Integer.valueOf(ssp[3].substring(0,ssp[3].length()-1));
			
		}
		else {//疑似患者
			Province1.sp+=Integer.valueOf(ssp[3].substring(0,ssp[3].length()-1));
		}
	}
	else if(ssp[1].equals("死亡")) {
		Province1.dead+=Integer.valueOf(ssp[2].substring(0,ssp[2].length()-1));
		Province1.ip-=Integer.valueOf(ssp[2].substring(0,ssp[2].length()-1));
	}
	else if(ssp[1].equals("治愈")) {
		Province1.cure+=Integer.valueOf(ssp[2].substring(0,ssp[2].length()-1));
		Province1.ip-=Integer.valueOf(ssp[2].substring(0,ssp[2].length()-1));
	}
	else if(ssp[1].equals("疑似患者")) {
		if(ssp[2].equals("确诊感染")){
			int change=Integer.valueOf(ssp[3].substring(0,ssp[3].length()-1));//改变人数
			Province1.ip+=change;
			Province1.sp-=change; 			
		}
		else {//流入情况
			String tolocation=ssp[3];//流入省
			int change=Integer.valueOf(ssp[4].substring(0,ssp[4].length()-1));//改变人数
			Province Province2;
	    	if(!isExistlocation(tolocation,all)) {//不存在对应该省的记录
	    		Province2=new Province(tolocation,0,0,0,0);//新建数据条
	    		all[count]=Province2;
	    		count++;
	    	}
	    	else {
	    		Province2=getLine(tolocation,all);//获得原有的数据条
	    	}
	    	Province1.sp-=change;
	    	Province2.sp+=change;
		}
	}
	else if(ssp[1].equals("排除")) {
		Province1.sp-=Integer.valueOf(ssp[3].substring(0,ssp[3].length()-1));   		
	}
	else {//感染患者流入情况
		String tolocation=ssp[3];//流入省
		//System.out.print(ssp[0]);
		int change=Integer.valueOf(ssp[4].substring(0,ssp[4].length()-1));//改变人数
		Province Province2;
    	if(!isExistlocation(tolocation,all)) {//不存在对应该省的记录
    		Province2=new Province(tolocation,0,0,0,0);//新建数据条
    		all[count]=Province2;
    		count++;
    	}
    	else {
    		Province2=getLine(tolocation,all);//获得原有的数据条
    	}
    	Province1.ip-=change;
    	Province2.ip+=change;   		
	}
	}
/************* 功能：将全国信息加在result总数组中 输入参数：无  返回值：无********************/
	static void addAll() {
		Province[] mid=new Province[count+1];//暂存信息
		mid[0]=calAll(result,count);
		for(int i=1;i<count+1;i++) {
			mid[i]=result[i-1];
		}
		result=mid;
	}
/************ 功能：把所有记录输出到txt文件 输入参数：总的记录数组all 返回值：无*************/
static void printtxt(Province[] result) throws IOException {
	File f = new File(topath);
    BufferedWriter output = new BufferedWriter(new FileWriter(f,false));
    output.write(calAll(result,count).printline()+"\n");
    for(int i=0;i<count;i++) {//写入统计数据
    	output.write(result[i].printline()+"\n");
    }
    output.write("//该文档并非真实数据，仅供测试使用");
	output.close();
}
/************* 功能：拼音顺序排序line数组 输入参数：记录数组,排序数组个数 返回值：排序好的数组**********/
static Province[] sortline(Province[] wannasort,int num) {
	String[] location=new String[num];
	for(int i=0;i<num;i++) {
		location[i]=wannasort[i].provinceName;    		
	}    	
    Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
    Arrays.sort(location, cmp);
    int i=0; 
    int j=0;//控制省份拼音顺序索引
    while(j<num) {       	
    	while(i<num) {
        	if(wannasort[i].provinceName.equals(location[j])) {
        		result[j]=wannasort[i];
        		j++;
        		if(j>=num) {
        			break;
        		}
        	}
        	i++;
    	}
    } 
    return result;
}


}

