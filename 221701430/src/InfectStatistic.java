import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import javafx.beans.binding.When;




/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
	//一个命令行所对应的实体类
	//一个命令行包括一个命令和众多参数
	static class CommandLine{
		public Command command;
		public Arguments arguments;
		
		//测试用，之后记得删除！！！
		public void test() {
			System.out.println("command:" + command.type );
			System.out.println("arguments.log:" + arguments.log + "," + arguments.log_value);	
			System.out.println("arguments.date:" + arguments.date + "," + arguments.date_value);	
			System.out.println("arguments.out:" + arguments.out + "," + arguments.out_value);
			System.out.println("arguments.type:" + arguments.type + "," + arguments.type_value);
			System.out.println("arguments.province:" + arguments.province + "," + arguments.province_value);
		}
		
		//一个命令所对应的实体类，本次作业只有一种命令list
		static class Command{
			//命令的类型，目前只有list
			String type;
			
			//判断是否为list命令
			public boolean is_list() {
				if(this.type.equals("list")) {
					return true;
				}
				else {
					return false;
				}	
			}
		}
		
		//一组参数对应的实体类，共有五种，分别为：
		//-log，-date，-out，-type，-province
		static class Arguments{
			//五种参数，若命令行中有某参数则对应参数设为true表示激活
			public boolean log;
			public String log_value;
			public boolean out;
			public String out_value;
			public boolean date;
			public String date_value;
			public boolean type;
			public ArrayList<String> type_value;
			public boolean province;
			public ArrayList<String> province_value;
			
			//五种函数用于判断五种参数是否激活，返回值为对应Boolean型参数
			public boolean is_log() {
				return log;
			}
			
			public boolean is_out() {
				return out;
			}
			
			public boolean is_date() {
				return date;
			}
			
			public boolean is_type() {
				return type;
			}
			
			public boolean is_province() {
				return province;
			}
		}
	
	
	}
	
	//命令行解析类，可以使用此类进行命令行解析形成命令行对象，即一个CommandLine对象
	static class CommandLineAnalysis{
		//命令行解析函数，将命令行解析为一个对应的CommandLine对象
		public CommandLine analysis(ArrayList<String> commandline) {
			//先实例化一个commandline对象
			CommandLine command_line = new CommandLine();
			command_line.command = new CommandLine.Command();
			command_line.arguments = new CommandLine.Arguments();
			command_line.arguments.type_value = new ArrayList<String>();
			command_line.arguments.province_value = new ArrayList<String>();
			
			//进行命令行解析
			for(int i = 0;i < commandline.size();i++) {
				String temp = commandline.get(i);
				
				switch (temp) {
				case "list":
					command_line.command.type = "list";					
					break;
				case "-log":
					command_line.arguments.log = true;
					command_line.arguments.log_value = commandline.get(i + 1);
					break;
				case "-date":
					command_line.arguments.date = true;
					command_line.arguments.date_value = commandline.get(i + 1);
					break;
				case "-out":
					command_line.arguments.out = true;
					command_line.arguments.out_value = commandline.get(i + 1);
					break;
				case "-type":
					//由于type和province都有可能有多个参数所以特殊处理
					command_line.arguments.type = true;
					for(int j = i + 1;j < commandline.size();j++) {
						char temp_char = commandline.get(j).charAt(0);
						//如果开头不是-，则代表还是参数值;若是-，则说明已经到了下一个参数
						if(temp_char != '-') {
							command_line.arguments.type_value.add(commandline.get(j));
						}else {
							//由于此时commandline.get(j)已经是下一个参数了
							//所以把i设为j-1，那么下一轮switch的temp就是下一个参数
							i = j - 1;
							break;
						}
					}
					break;
				case "-province":
					command_line.arguments.province = true;
					for(int j = i + 1;j < commandline.size();j++) {
						char temp_char = commandline.get(j).charAt(0);
						//如果开头不是-，则代表还是参数值;若是-，则说明已经到了下一个参数
						if(temp_char != '-') {
							command_line.arguments.province_value.add(commandline.get(j));
						}else {
							//由于此时commandline.get(j)已经是下一个参数了
							//所以把i设为j-1，那么下一轮switch的temp就是下一个参数
							i = j - 1;
							break;
						}
					}
					break;
				default:
					break;
				}
			}
			
			//测试看看解析是否成功
			command_line.test();
			return command_line;
		}
	}
	
	//命令行运行类,使用此类进行命令行的运行,可以考虑加一个省类分别有省名以及其他数据
	static class CommandLineRun{
		public CommandLine commandline;
		public File file_test;
		public int ip;
		public int sp;
		public int cure;
		public int dead;
		
		public CommandLineRun(CommandLine cmdline) throws IOException {
			commandline = new CommandLine();
			commandline = cmdline;
			//这里记得要改成参数值！！！！！！！！！！
			file_test = new File("D:\\InfectStatistic-main\\221701430\\log\\2020-01-22.log.txt");
			get_data(file_test);
		}
		public void get_data(File f) throws IOException {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
			String temp;
			
			//用于正则表达式匹配
			String type_1 = "(\\W+) (新增 感染患者) (\\d+)(人)";
	        String type_2 = "(\\W+) (新增 疑似患者) (\\d+)(人)";
	        String type_3 = "(\\W+) (感染患者 流入) (\\W+) (\\d+)(人)";
	        String type_4 = "(\\W+) (疑似患者 流入) (\\W+) (\\d+)(人)";
	        String type_5 = "(\\W+) (死亡) (\\d+)(人)";
	        String type_6 = "(\\W+) (治愈) (\\d+)(人)";
	        String type_7 = "(\\W+) (疑似患者 确诊感染) (\\d+)(人)";
	        String type_8 = "(\\W+) (排除 疑似患者) (\\d+)(人)";
	        
	        ArrayList<String> type_list = new ArrayList<String>();
	        type_list.add(type_1);
	        type_list.add(type_2);
	        type_list.add(type_3);
	        type_list.add(type_4);
	        type_list.add(type_5);
	        type_list.add(type_6);
	        type_list.add(type_7);
	        type_list.add(type_8);
	        
	        
	        while((temp = reader.readLine()) != null) {
	        	if(temp.charAt(0) == '/') {
	        		continue;
	        	}
	        	
	        	char flag = '0';
	        	Pattern pattern;
		        Matcher matcher;
	        	for(int i = 0;i < 8;i++) {
	        		pattern = Pattern.compile(type_list.get(i));
	        		matcher = pattern.matcher(temp);
	        		if(matcher.find()) {
	        			flag = (char) (48 + (i + 1));
	        			break;
	        		}else {
						continue;
					}
	        	}
	        	System.out.println(temp);
	        	System.out.println("this is p" + flag);
	        	
	        }
	        
	        /*
	        if (m1.find( )) {
	            System.out.println("Found value: " + m1.group(0) );
	            System.out.println("Found value: " + m1.group(1) );
	            System.out.println("Found value: " + m1.group(2) );
	            System.out.println("Found value: " + m1.group(3) ); 
	         } else {
	            System.out.println("NO MATCH");
	         }*/
		}
	}
	
	
	
	
	
	
	
	
	
    public static void main(String[] args) throws IOException {
    	String command = "";   	
    	//提取命令行
        for(int i=3;i<args.length;i++){
            command += args[i] + " ";
        }
        //以上也许没用，先写着
        
        //测试用
        CommandLine commandline = new CommandLine();
         
        //将命令行连成一个List方便之后进行命令行分析生成命令行实例
        ArrayList<String> cmd_line = new ArrayList<String>();
        for (String temp : args) {
        	cmd_line.add(temp);
        }
        //用于测试用的自组命令行，记得注释掉再把commandline_analysis.analysis(commandline_test);
        //改成commandline_analysis.analysis(cmdline);
        ArrayList<String> commandline_test = new ArrayList<String>();
        commandline_test.add("list");
        commandline_test.add("-log");
        commandline_test.add("123");
        commandline_test.add("-type");
        commandline_test.add("444");
        commandline_test.add("555");
        commandline_test.add("666");
        commandline_test.add("-province");
        commandline_test.add("7");
        commandline_test.add("8");
        
        CommandLineAnalysis commandline_analysis = new CommandLineAnalysis();
        commandline = commandline_analysis.analysis(commandline_test);
        
        //测试用
        CommandLineRun cmd_run = new CommandLineRun(commandline);
    }
}
