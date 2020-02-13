import java.util.ArrayList;
import java.util.List;

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
	class CommandLine{
		public Command command;
		public Arguments arguments;
		
		//一个命令所对应的实体类，本次作业只有一种命令list
		class Command{
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
		class Arguments{
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
	class CommandLineAnalysis{
		//命令行解析函数，将命令行解析为一个对应的CommandLine对象
		public CommandLine analysis() {
			CommandLine command_line = null;
			return command_line;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
    public static void main(String[] args) {
    	String commandline = "";
    	
    	//提取命令行
        for(int i=3;i<args.length;i++){
            commandline += args[i] + " ";
        }

        //将命令行连成一个List方便之后进行命令行分析生成命令行实例
        List<String> line = new ArrayList<String>();

        for (String temp : args) {
            line.add(temp);
        }
        
        System.out.println(line.get(0));
    }
}
