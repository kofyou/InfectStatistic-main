import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author 221701405_叶如茵
 * @version 1.0.0
 * @since 2020-02
 */

class InfectStatistic {
	
	//命令行类
	static class Command{
		
		private boolean list;//list命令
		
		//参数，若命令行中有某参数则对应参数设为true
    	private boolean date;
    	private boolean type;
    	private boolean province;
    	private boolean log;
    	private boolean out;
    	
    	//参数值
    	private String date_value;
    	private String log_value;
    	private String out_value;
    	private ArrayList<String> type_value;//type支持多个参数
    	private ArrayList<String> province_value;//province支持多个参数
    	
    	//构造函数
    	public Command() {
    		list = false;
    	   	date = false;
    		type = false;
    		province = false;
    		log = false;
    		out = false;
    		date_value = null;
    		log_value = null;
    		out_value = null;
    		type_value = new ArrayList<String>();
    		province_value = new ArrayList<String>();
    	}
    	
    	//判断命令是否为list
    	public boolean isList() {
			return log;
		}
    	 
    	//判断是否有这五个参数，返回布尔值
		public boolean isLog() {
			return log;
		}
			
		public boolean isOut() {
			return out;
		}
			
		public boolean isDate() {
			return date;
		}
			
		public boolean isType() {
			return type;
		}
			
		public boolean isProvince() {
			return province;
		}
		
		//参数值
		public void setDateValue(String argument) {
	    	this.date_value = argument;
	    }
		
		public void setLogValue(String argument) {
	    	this.log_value = argument;
	    }
	    	
	    public void setOutValue(String argument) {
	    	this.out_value = argument;
	    }
	    
	    public void setTypeValue(ArrayList<String> argument) {
	    	this.type_value = argument;
	    }
	    
	    public void setProvinceValue(ArrayList<String> argument) {
	    	this.province_value = argument;
	    }
	    
	}
			
	//命令行解析类
	static class CommandAnalysis{
		
		//解析命令行
		static public Command analysis(List<String> list) {
			Command command = new Command();//实例化一个command对象
			
			//进行命令行解析
			for(int i=0;i<list.size();i++) {
				String temp = list.get(i);
				//获取command
				switch(temp) {
				case "list":
					command.list = true;//命令行包含list命令
					break;
				case "-date":
					command.date = true;
					command.setDateValue(list.get(i+1));
					break;
				case "-log":
					command.log = true;
					command.setLogValue(list.get(i+1));
					break;
				case "-out":
					command.out = true;
					command.setOutValue(list.get(i+1));
					break;	
				case "-type":
					command.type = true;
					ArrayList<String> type = new ArrayList<String>();
					for(int j = i+1;j < list.size();j++) {
						String type_value = list.get(j);
						if(!type_value.substring(0,1).equals("-")) {//如果开头不是-，则代表还是参数值
							type.add(type_value);
						}else {
							break;
						}
					}
					command.setTypeValue(type);					
					break;	
				case "-province":
					command.province = true;
					ArrayList<String> prov = new ArrayList<String>();
					for(int j = i + 1;j < list.size();j++) {
						String province = list.get(j);
						if(!province.substring(0,1).equals("-")) {//如果开头不是-，则代表还是参数值
							prov.add(province);
						}else {
							break;
						}
					}
					command.setProvinceValue(prov);
					break;		
				default:
					break;
				}
				
			}
			return command;
		}
		
		//执行命令
		public void commandRun(ArrayList<String> list) throws IOException {
	 	    Command cmd = new Command();
			cmd = CommandAnalysis.analysis(list);
			
			FileControl file = new FileControl();
	        //file.readLog(cmd.log_value,cmd.date_value);
	        //file.writeTxt(cmd,cmd.type_value,cmd.province_value);
     	}
	}
	
	//文件处理类
	static class FileControl{	
		public String[] provinces={"全国","安徽","澳门","北京","重庆","福建","甘肃","广东","广西",
	            "贵州", "海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏",
	            "江西", "辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海",
	            "四川", "台湾","天津","西藏","香港","新疆","云南","浙江"};
		public boolean[] is_province = new boolean[31];//判断是否有这个省份
	    public int[] ip= new int[31];//各省感染患者人数
	    public int[] sp=new int[31];//各省疑似患者人数
	    public int[] cure=new int[31];//各省治愈人数
	    public int[] dead=new int[31];//各省死亡人数
	    public int all_ip=0,all_sp=0,all_cure=0,all_dead=0;//全国数据
	    ArrayList<String> file_list = new ArrayList<>();//需要处理的文件列表
	    
	    //处理单行日志文件
	    public void dealLog(String line) throws IOException {
	    	
	    	//正则表达式匹配
			String pattern1 = "(\\S+) 新增 感染患者 (\\d+)人";
	        String pattern2 = "(\\S+) 新增 疑似患者 (\\d+)人";
	        String pattern3 = "(\\S+) 治愈 (\\d+)人";
	        String pattern4 = "(\\S+) 死亡 (\\d+)人";
	        String pattern5 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
	        String pattern6 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
	        String pattern7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
	        String pattern8 = "(\\S+) 排除 疑似患者 (\\d+)人";
	        boolean match1 = Pattern.matches(pattern1, line);
	        boolean match2 = Pattern.matches(pattern2, line);
	        boolean match3 = Pattern.matches(pattern3, line);
	        boolean match4 = Pattern.matches(pattern4, line);
	        boolean match5 = Pattern.matches(pattern5, line);
	        boolean match6 = Pattern.matches(pattern6, line);
	        boolean match7 = Pattern.matches(pattern7, line);
	        boolean match8 = Pattern.matches(pattern8, line);
	        
	        //对八种情况进行处理
	        if(match1) 
	            addIp(line);
	        else if(match2) 
	            addSp(line) ;
	        else if(match3) 
	            addCure(line);
	        else if(match4) 
	            addDead(line);
	        else if(match5) 
	            mobilityIp(line);
	        else if(match6) 
	            mobilitySp(line);
	        else if(match7) 
	            spToIp(line);
	        else if(match8) 
	            reduceSp(line);
	    }
	    
	    //新增感染患者
	    public void addIp(String line){
	        String[] str = line.split(" "); //将一行以空格分割为多个字符串
            int n = Integer.valueOf(str[3].replace("人", ""));//将人前的数字转化为int类型
            for(int i = 0; i < provinces.length; i++){
                if(str[0].equals(provinces[i])){
                	is_province[i] = true;
                    ip[i]+=n;
                    all_ip+=n;
                    break;
                }
            }
        }
	        
	    //新增疑似患者
        public void addSp(String line){
	        String[] str = line.split(" "); //将一行以空格分割为多个字符串
	        int n = Integer.valueOf(str[3].replace("人", ""));//将人前的数字从字符串类型转化为int类型
	        for(int i = 0; i < provinces.length; i++){
	            if(str[0].equals(provinces[i])){
	            	is_province[i] = true;
	                sp[i]+=n;
	                all_sp+=n;
	                break;
	            }
	        }
        }
        
        //新增治愈患者  
        public void addCure(String line){
            String[] str = line.split(" "); //将一行以空格分割为多个字符串
            int n = Integer.valueOf(str[2].replace("人", ""));//将人前的数字转化为int类型
            for(int i = 0; i < provinces.length; i++){
                if(str[0].equals(provinces[i])){
                	is_province[i] = true;
                    ip[i]-=n;
                    cure[i]+=n;
                    all_ip-=n;
                    all_cure+=n;                 
                    break;
                }
            }
        }
        
        //新增死亡患者
        public void addDead(String line){
	        String[] str = line.split(" "); //将一行以空格分割为多个字符串
	        int n = Integer.valueOf(str[2].replace("人", ""));//将人前的数字转化为int类型
	        for(int i = 0; i < provinces.length; i++){
	            if(str[0].equals(provinces[i])){
	            	is_province[i] = true;
	                ip[i]-=n;
	                dead[i]+=n;
	                all_ip-=n;
	                all_dead+=n;	                
	                break;
	            }
	        }
        }
        
        //外省流入本省感染患者
        public void mobilityIp(String line){
            String[] str = line.split(" "); //将一行以空格分割为多个字符串
            int flag2=0;
            int n = Integer.valueOf(str[4].replace("人", ""));//将人前的数字转化为int类型
            for(int i = 0; i < provinces.length; i++){
                if(str[0].equals(provinces[i])){
                	is_province[i] = true;
                    ip[i]-=n;//外省感染患者人数减少                    
                    flag2+=1;
                    //break;
                }
                if(str[3].equals(provinces[i])){
                	is_province[i] = true;
                    ip[i]+=n;//本省感染患者人数增加                    
                    flag2+=1;
                    //break;
                }
                if(flag2==2) break;
            }
        }
        
        //外省流入本省疑似患者
        public void mobilitySp(String line){
	        String[] str = line.split(" "); //将一行以空格分割为多个字符串
            int flag3=0;
            int n = Integer.valueOf(str[4].replace("人", ""));//将人前的数字转化为int类型
            for(int i = 0; i < provinces.length; i++){
	            if(str[0].equals(provinces[i])){
	            	is_province[i] = true;
	                sp[i]-=n;//外省疑似患者人数减少
	                flag3+=1;
	                //break;
	            }
	            if(str[3].equals(provinces[i])){
	            	is_province[i] = true;
	                sp[i]+=n;//本省疑似患者人数增加
	                flag3+=1;
	                //break;    
	            }
                if(flag3==2) break;
	        }
        }
        
        //疑似患者确诊感染患者
        public void spToIp(String line){
            String[] str = line.split(" "); //将一行以空格分割为多个字符串
            int n = Integer.valueOf(str[3].replace("人", ""));//将人前的数字转化为int类型
            for(int i = 0; i < provinces.length; i++){
                if(str[0].equals(provinces[i])){
                	is_province[i] = true;
                    sp[i]-=n;
                    ip[i]+=n;
                    all_sp-=n;
                    all_ip+=n;
                    break;
                }
            }
        }
        
        //排除疑似患者
        public void reduceSp(String line){
            String[] str = line.split(" "); //将一行以空格分割为多个字符串
            int n = Integer.valueOf(str[3].replace("人", ""));//将人前的数字转化为int类型
            for(int i = 0; i < provinces.length; i++){
                if(str[0].equals(provinces[i])){
                	is_province[i] = true;
                    sp[i]-=n;
                    all_sp-=n;
                    break;
                }
            }
        }
	        
	       		
		//读取的文件列表
		public void readLog(String path,String date) throws IOException {
			//待完善
 		}

 		//按要求输出到文件
 		public void writeTxt(Command command,ArrayList<String> type,ArrayList<String> province) throws IOException {
 			
 			//待完善
 			
 		}	
	}
	    
	public static void main(String[] args) throws IOException {
		
        ArrayList<String> list=new ArrayList<String>();
        for(String i:args) {
        	list.add(i);//把命令加入list
        }
        CommandAnalysis cmd_analysis=new CommandAnalysis();//实例化命令行解析对象
        cmd_analysis.commandRun(list);//执行命令
    }
}
	
	    
