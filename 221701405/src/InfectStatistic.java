import java.io.*;
import java.util.*;


/**
 * InfectStatistic
 * TODO
 *
 * @author 221701405_叶如茵
 * @version 1.0.0
 * @since 2020-02-10
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
		
   	}
	
	//文件处理类
	static class FileControl{
		
		//待完善
		
	}
	    
	public static void main(String[] args) throws IOException {
		
        ArrayList<String> list=new ArrayList<String>();
        for(String i:args) {
        	list.add(i);//把命令加入list
        }
        
    }
}
	
	    
