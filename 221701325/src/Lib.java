import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Lib
 * 1.利用责任链模式对日志文件进行处理
 * 2.ListCommand类的实现
 * 3.CmdArgs类的实现
 * 4.
 *
 * @author Benjamin_Gnep
 * @version 1.0
 * @since 2020/2/11
 */
public class Lib {
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



enum ListKey{
	LOG, OUT, DATE, TYPE, PROVINCE
}

//命令模式的command类
interface Command{
	void execute(Map<String,List<String>> map);
}

class ListCommand implements Command{
	private ListKey listKey;
	
	public ListCommand() {	
	}
	
	@Override
	public void execute(Map<String, List<String>> map) {
		
		Set<String> keySet = map.keySet();
		Iterator<String> it =keySet.iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			List<String> value = map.get(key);
			switch (key) {
				case "date":
					listKey = ListKey.DATE;
					break;
				case "log":
					listKey = ListKey.LOG;
					break;
				case "out":
					listKey = ListKey.OUT;
					break;
				case "type":
					listKey = ListKey.TYPE;
					break;
				case "province":
					listKey = ListKey.PROVINCE;
					break;
			}
			
			switch(listKey) {
				case DATE:
					dateKey(value);
					break;
				case LOG:
					logKey(value);
					break;
				case OUT:
					outKey(value);
					break;
				case TYPE:
					typeKey(value);
					break;
				case PROVINCE:
					provinceKey(value);
					break;
			}
		}
	}

	private void provinceKey(List<String> value) {
		// TODO Auto-generated method stub
		
	}

	private void typeKey(List<String> value) {
		// TODO Auto-generated method stub
		
	}

	private void outKey(List<String> value) {
		// TODO Auto-generated method stub
		
	}

	private void logKey(List<String> value) {
		// TODO Auto-generated method stub
		
	}

	private void dateKey(List<String> value) {
		// TODO Auto-generated method stub
		
	}

}

class TxtTool {
    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        
        try{
            String s = null;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            	//若本行为'//'开头说明为注释，应忽略
            	if(s.matches("^\\/\\/.*$"))
            		continue;
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return result.toString();
    }
    
    public static void string2Txt(File file) {
        try {
            file.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write("我会写入文件啦1\r\n"); // \r\n即为换行
            bw.write("我会写入文件啦2\r\n"); // \r\n即为换行
            bw.flush(); // 把缓存区内容压入文件
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//责任链模式处理日志文件
abstract class MyHandler{
	public abstract boolean regFit();
	public abstract void handle();
	protected MyHandler nextHandler;
	public final void handleRequest() {
		//如果不符合我的正则表达式，传递给下一个handler
		if(!regFit()) {
			if(nextHandler != null) {
				nextHandler.handleRequest();
			}
		}
		else {
			handle();
		}
	}
}

//新增感染患者
class AddipHandler extends MyHandler{
	private Pattern reg;
	@Override
	public boolean regFit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
	}
}

//新增疑似患者
class AddSpHandler extends MyHandler{
	private Pattern reg;
	@Override
	public boolean regFit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
	}
}

//患者治愈
class CureHandler extends MyHandler{
	private Pattern reg;
	@Override
	public boolean regFit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
	}
}

//感染患者流入
class SwapipHandler extends MyHandler{
	private Pattern reg;
	@Override
	public boolean regFit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
	}
}

//疑似患者流入
class SwapSpHandler extends MyHandler{
	private Pattern reg;
	@Override
	public boolean regFit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
	}
}

//患者死亡
class DeathHandler extends MyHandler{
	private Pattern reg;
	@Override
	public boolean regFit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
	}
}

//疑似确诊感染患者
class ChangeHandler extends MyHandler{
	private Pattern reg;
	@Override
	public boolean regFit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
	}
}

//排除疑似患者
class ExcludeHandler extends MyHandler{
	private Pattern reg;
	@Override
	public boolean regFit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
	}
}
