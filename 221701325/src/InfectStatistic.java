import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * InfectStatistic
 * 使用Map对list命令的参数进行储存并处理
 *
 * @author Benjamin_Gnep
 * @version 1.0
 * @since 2020/2/11
 */
class InfectStatistic {
    public static void main(String[] args) {
    	String[] test = {"list","-date","2020-01-22","2020-02-14","-log","D:/log/","-out","D:/output.txt"};
    	CmdArgs cmdArgs = new CmdArgs(test,"benjamin");
    	ListCommand listCommand = new ListCommand();//目前默认只有list命令
    	Map<String,List<String>> map = new HashMap<String,List<String>>();
    	
    	
    	cmdArgs.setCommand(listCommand);
    	cmdArgs.fillMap(map);
    	cmdArgs.command.execute(map);
    	
    	System.out.println(map);
    }
    
}




/**
 *存储命令行参数
 *将命令行通过键值对的方式储存至map中
 */
class CmdArgs {
    String[] args;
    Command command;
    int index = 1;//index 作为现在处理到的位置,跳过前面命令list,否则为0
    
    /**
     * 传入命令行参数数组构造
     * @param args
     */
    CmdArgs(String[] args) {
        this.args = args;
    }

	/**
     * 传入命令构造，可以设置无用的前缀，如`groovy Lib`
     * @param argsStr
     * @param noUseInStart
     */
    CmdArgs(String[] argsStr, String noUseInStart) {
    	this.args = argsStr;
    }
    
    /**
     * 确认命令
     * @param command
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * 填满map并返回
     * @param map
     * @return	map
     */
    public Map<String, List<String>> fillMap(Map<String, List<String>> map) {
		String key;
		List<String> value;
		while(index < args.length) {
			key = argKey();
			key = key.toLowerCase().trim();
			
			value = argVals();
//			Iterator<String> it = value.iterator();
//	    	while(it.hasNext()) {
//	    		System.out.println(it.next());
//	    	}
			map.put(key, value);
		}
		return map;
	}
    

	/**
     * 获取命令中的一个参数
     * @return
     */
    String argKey() {
    	if(index<args.length && args[index].matches("^\\-.*$")) {//index 作为现在处理到的位置
    		String[] key = args[index].split("\\-");
    		index++;
    		//System.out.println(key[1]);
    		return key[1];
    	}
		return null;
    }

    /**
     * 获取某个命令行参数的值，返回列表
     * 同时判断该命令是否有对应的参数，若无参数则设为default
     * @param key
     * @return
     */
	List<String> argVals() {
    	List<String> values = new LinkedList<String>();
    	while(index<args.length && (!args[index].matches("^\\-.*$"))) {//将参数所有的值放入values中
    		values.add(args[index]);
    		index++;
    	}
		Iterator<String> it = values.iterator();
    	if(!it.hasNext()) {
    		values.add("default");
    	}
    	return values;
    }
}


