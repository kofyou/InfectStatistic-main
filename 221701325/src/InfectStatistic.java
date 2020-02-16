import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InfectStatistic
 * 利用CmdArgs类对命令行进行剖析
 * 利用ListCommand类和Command类的命令模式保证程序可拓展性
 * 使用Map对list命令的参数和值进行储存并处理
 *
 * @author Benjamin_Gnep
 * @version 1.0
 * @since 2020/2/11
 */
class InfectStatistic {
    public static void main(String[] args) {
    	String[] test = {"list","-date"
    			,"2020-01-23"
    			,"-log","C:/Users/jhuy/Documents/GitHub/InfectStatistic-main/221701325/log",
    			"-province","福建","新疆","全国",
    			"-type","ip","sp",
    			"-out","C:/Users/jhuy/Documents/GitHub/InfectStatistic-main/221701325/out.txt"};
    	CmdArgs cmdArgs = new CmdArgs(test,"benjamin");
    	Map<String,List<String>> map = new HashMap<String,List<String>>();
    	
    	/*
    	 * 这里可能省略一些命令的选择，可以添加list之外的命令进行筛选
    	 */

    	ListCommand listCommand = new ListCommand();//目前默认只有list命令
    	cmdArgs.setCommand(listCommand);
    	cmdArgs.fillMap(map);
    	cmdArgs.command.execute(map);
    	
    }
    
}
