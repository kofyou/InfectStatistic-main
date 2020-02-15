import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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


class DateOutOfBoundsException extends Exception{
	public DateOutOfBoundsException(String s){
        super(s);
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



enum ListKey{
	LOG(0,"读取路径"), DATE(1,"限定日期"), PROVINCE(2,"限定省份"), 
	TYPE(3,"限定类型"), OUT(4,"输出路径");
	private int key;
	private String text;
	private ListKey(int key,String text){
		this.key = key;
		this.text = text;
	}
	
	private static HashMap<Integer,String> map = new HashMap<Integer,String>();
	static {
        for(ListKey d : ListKey.values()){
            map.put(d.key,d.text);
        }
    }

	public static ListKey valueOf(int ordinal) {
		if (ordinal < 0 || ordinal >= values().length) {
			throw new IndexOutOfBoundsException("Invalid ordinal");
		} 
		return values()[ordinal];
	}
}

//命令模式的command类
interface Command{
	void execute(Map<String,List<String>> map);
}

class ListCommand implements Command{
	private ListKey listKey;
	private List<String[]> result = new LinkedList<String[]>();
	private List<String> logLine = new LinkedList<String>();
	
	/**
     * 传入参数和值的map并进行处理
     * 当前处理顺序：log->date->province->type->out
     * 若要新增功能请确认处理顺序符合逻辑并将功能添加在ListKey枚举类中
     * @param map
     */
	@Override
	public void execute(Map<String, List<String>> map) {
		for(int i = 0; i < ListKey.values().length ;i++) {
			listKey = ListKey.valueOf(i);
			switch(listKey) {
				case DATE:
					dateKey(map);
					break;
				case LOG:
					logKey(map);
					break;
				case OUT:
					outKey(map);
					break;
				case TYPE:
					typeKey(map);
					break;
				case PROVINCE:
					provinceKey(map);
					break;
				}
			}
		}
//		Set<String> keySet = map.keySet();
//		Iterator<String> it =keySet.iterator();
//		while(it.hasNext()) {
//			String key = it.next();
//			if(key.matches("date")) {
//				
//			}
//		}

	
	private void provinceKey(Map<String, List<String>> map) {
		//System.out.println("now is in ProvinceKey Handler");
		List<String> provinceList = map.get("province");
		for(int i = 0; i < logLine.size();i++) {
			boolean flag = false;
			for(int j = 0; j < provinceList.size(); j++) {
				if(logLine.get(i).matches(".*" + provinceList.get(j) + ".*")) {
					flag = true;
					break;
				}
			}
			if(flag == false) {
				logLine.remove(i);
			}
		}
		for(int i = 0;i < logLine.size();i++) {
			System.out.println(logLine.get(i));
		}
	}

	private void typeKey(Map<String, List<String>> map) {
		//System.out.println("nowType");
		
	}

	private void outKey(Map<String, List<String>> map) {
		//System.out.println("nowOut");
		
	}

	private void logKey(Map<String, List<String>> map) {
		//System.out.println("now is in LogKey Handler");
		//List<String> logList = map.get("log");
		//System.out.println("log is " + logList.get(0));
	}

	private void dateKey(Map<String, List<String>> map) {

		//System.out.println("now is in DateKey Handler");
		List<String> logList = map.get("log");
		List<String> dateList = map.get("date");
		//System.out.println("date deadline is " + dateList.get(0));
		String date = dateList.get(0);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dateObj = dateFormat.parse(date);
			List<File> fileList = TxtTool.getFileList(logList.get(0));
			
			//获取文件列表的最后一个文件判断参数值是否超过当前最大日期
			String pathStr = fileList.get(fileList.size() - 1).toString();
			String[] fileStr = pathStr.split("\\\\");
			String[] dateStr = fileStr[fileStr.length - 1].split("\\.");
			date = dateStr[0];
			try {
				Date dateObj2 = dateFormat.parse(date);
				if(dateObj2.before(dateObj)) {
					throw new DateOutOfBoundsException("date out of bounds.");
				}
			}
			catch(DateOutOfBoundsException e){
				e.printStackTrace();
			}
			
			//System.out.println("处理前的文件列表  " + fileList);
			TxtTool.dateScreen(fileList,dateObj);
			//System.out.println("处理后的文件列表  " + fileList);
			String result = "";
			for(int i = 0; i < fileList.size(); i++) {
				result += TxtTool.txt2String(fileList.get(i));
			}
			String[] line = result.split("\\n");
			for(int i = 0;i < line.length;i++) {
				if(!line[i].matches("^\\s$")) {
					logLine.add(line[i].trim());
				}
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

class TxtTool {
	static List<File> filelist = new ArrayList<File>(); 
	
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
    
    public static List<File> getFileList(String strPath) {
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
        	//System.out.println("loading the files...");
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
                } 
                else { 
                    //System.out.println("-" + strFileName);
                    filelist.add(files[i]);
                }
            }
        	//System.out.println("loading completed.");
        }
        return filelist;
    }
    
    public static void dateScreen(List<File> fileList, Date dateObj) {			
		for(int i = 0; i < fileList.size(); i++) {
			String pathStr = fileList.get(i).toString();
			String[] fileStr = pathStr.split("\\\\");
			String[] dateStr = fileStr[fileStr.length - 1].split("\\.");
			String date = dateStr[0];
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date dateObj2 = dateFormat.parse(date);
				if(dateObj.before(dateObj2)) {
					fileList.remove(i);
					i--;//重点
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
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
