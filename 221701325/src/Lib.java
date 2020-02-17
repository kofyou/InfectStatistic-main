import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

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
public class Lib {}

class DataManager{
	public static List<int[]> solveData(List<String>data){
		List<int[]> result = new LinkedList<int[]>();
		ListInit(result);
		System.out.println("建立责任链");
		AddipHandler addip = new AddipHandler(result);
		AddSpHandler addSp = new AddSpHandler(result);
		ChangeHandler change = new ChangeHandler(result);
		CureHandler cure = new CureHandler(result);
		DeathHandler death = new DeathHandler(result);
		ExcludeHandler exclude = new ExcludeHandler(result);
		SwapIpHandler swapIp = new SwapIpHandler(result);
		SwapSpHandler swapSp = new SwapSpHandler(result);
		addip.nextHandler = addSp;
		addSp.nextHandler = change;
		change.nextHandler = cure;
		cure.nextHandler = death;
		death.nextHandler = exclude;
		exclude.nextHandler = swapIp;
		swapIp.nextHandler = swapSp;
		swapSp.nextHandler = null;
		System.out.println("建立责任链完成，开始处理文本");
		Iterator<String> it = data.iterator();
    	while(it.hasNext()) {
    		String s = it.next();
    		addip.handleRequest(s);
    	}
    	System.out.println("---文本处理完成");
		return result;
	}

	private static void ListInit(List<int[]> result) {
		System.out.println("输出结果集初始化");
		for(int i = 0; i < ProvinceValue.values().length ;i++) {
			int[] vals = {i,0,0,0,0};
			result.add(vals);
		}
	}

	public static void mergeData(List<int[]> result) {
		System.out.println("---正在简化结果集");
		Iterator<int[]> it = result.iterator();
    	while(it.hasNext()) {
    		int[] t = it.next();
    		if(t[1] == 0 &&  t[2] == 0 && t[3] == 0 && t[4] ==0) {
    			it.remove();
    		}
    	}
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
			map.put(key, value);
		}
		return map;
	}

	/**
     * 获取命令中的一个参数
     * @return
     */
    String argKey() {
    	if(index<args.length && args[index].matches(Constant.COMMAND_REG)) {
    		//index 作为现在处理到的位置
    		String[] key = args[index].split(Constant.SPILT_COMMAND);
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
    	while(index<args.length && (!args[index].matches(Constant.COMMAND_REG))) {
    		//将参数所有的值放入values中
    		values.add(args[index]);
    		index++;
    	}
		Iterator<String> it = values.iterator();
    	if(!it.hasNext()) {
    		values.add(Constant.DEFAULT);
    	}
    	return values;
    }
}
//命令模式的command类
interface Command{
	void execute(Map<String,List<String>> map);
}

class ListCommand implements Command{
	private ListKey listKey;
	private List<int[]> result = new LinkedList<int[]>();
	private List<String> logLine = new LinkedList<String>();
	
	/**
     * 传入参数和值的map并进行处理
     * 当前处理顺序：log->date->province->type->out
     * 若要新增功能请确认处理顺序符合逻辑并将功能添加在ListKey枚举类中
     * @param map
     */
	@Override
	public void execute(Map<String, List<String>> map) {
		System.out.println("---现在开始执行list命令");
		String proString = null;
		List<String> typeString = new LinkedList<String>();
		for(int i = 0; i < ListKey.values().length ;i++) {
			listKey = ListKey.valueOf(i);
			switch(listKey) {
				case DATE:
					dateKey(map);
					result = DataManager.solveData(logLine);
					DataManager.mergeData(result);
					System.out.println("简化后结果集为：");
					Iterator<int[]> it = result.iterator();
			    	while(it.hasNext()) {
			    		int[] t = it.next();
			    		System.out.println(t[1]+Constant.SPACE + t[2] + Constant.SPACE 
			    				+ t[3] + Constant.SPACE + t[4]);
			    		}
					break;
				case LOG:
					logKey(map);
					break;
				case OUT:
					outKey(map,proString,typeString);
					break;
				case TYPE:
					typeString = typeKey(map);
					break;
				case PROVINCE:
					proString = provinceKey(map,result);
					break;
			}
		}
	}

	private String provinceKey(Map<String, List<String>> map, List<int[]> result) {
		List<String> provinceList = map.get(Constant.PROVINCE);
		if(provinceList == null || provinceList.get(0) == Constant.DEFAULT) {
			System.out.println("---未指定provinc参数，默认输出全部省份");
			String proIndex = new String(Constant.SPACE);
			for(int i = 0; i < ProvinceValue.values().length; i++) {
				proIndex += ProvinceValue.valueOf(i).getText();
				proIndex += Constant.SPACE;
			}
			return proIndex;
		}
		System.out.println("分析-provinc参数");
		String proIndex = new String(Constant.SPACE);
		for(int i = 0; i < provinceList.size(); i++) {
			proIndex += provinceList.get(i).toString();
			proIndex += Constant.SPACE;
		}
		System.out.println("---已获得province的正则表达式");
		System.out.println("province分析完成");
		return proIndex;
	}

	private List<String> typeKey(Map<String, List<String>> map) {
		List<String> typeList = map.get(Constant.TYPE);
		if(typeList == null || typeList.get(0) == Constant.DEFAULT) {
			typeList = new LinkedList<String>();
			System.out.println("---未指定type参数，默认输出全部类型");
			for(int i = 0; i < TypeValue.values().length; i++) {
				typeList.add(TypeValue.values()[i].getEng());
			}
			return typeList;
		}
		System.out.println("分析-type参数");
		System.out.println("---已获得type列表");
		System.out.println("type分析完成");
		return typeList;
	}

	private void outKey(Map<String, List<String>> map,String proString,List<String> typeString) {
		System.out.println("分析-out参数");
		List<String> outList = map.get(Constant.OUT);
		try {
			if(outList == null) {
					throw new NoOutException("---无输出路径");
			}else {
				System.out.println("---输出路径为："+ outList.get(0));
			}
		} catch (NoOutException e) {
			e.printStackTrace();
		}
		String out = outList.get(0);
		System.out.println("out分析完成");
		System.out.println("正在输出...\n...\n...");
		try {
			File file = new File(out);
	        FileWriter writer = new FileWriter(file);
	        BufferedWriter bw = new BufferedWriter(writer);
	        Iterator<int[]> it = result.iterator();
	        
	    	while(it.hasNext()) {
	    		int[] t = it.next();
	    		if(proString.matches(Constant.PREFIX_REG 
	    				+ ProvinceValue.valueOf(t[0]).getText() + Constant.SUFFIX_REG)) {
	    			bw.write(ProvinceValue.valueOf(t[0]).getText() + Constant.SPACE);
	    			for(int i = 0; i < typeString.size(); i++) {
	    				String type = typeString.get(i).toUpperCase().trim();
	    				switch(type) {
	    				case "IP":
	    					bw.write(TypeValue.IP.getText() + t[1] 
	    							+ Constant.PERSON + Constant.SPACE);
	    					break;
	    				case "SP":
	    					bw.write(TypeValue.SP.getText() + t[2] 
	    							+ Constant.PERSON + Constant.SPACE);
	    					break;
	    				case "CURE":
	    					bw.write(TypeValue.CURE.getText() + t[3] 
	    							+ Constant.PERSON + Constant.SPACE);
	    					break;
	    				case "DEAD":
	    					bw.write(TypeValue.DEAD.getText() + t[4] 
	    							+ Constant.PERSON);
	    					break;
	    				}
	    			}
	    			bw.write("\n");
	    		}
	    	}
	        bw.flush(); // 把缓存区内容压入文件
	        bw.close();
	        System.out.println("输出完成！");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	private void logKey(Map<String, List<String>> map) {
		System.out.println("分析-log参数");
		try {
			List<String> logList = map.get(Constant.LOG);
			if(logList == null) {
					throw new NoLogException("---无输入路径");
			}else {
				System.out.println("---输入路径为："+ logList.get(0));
			}
			System.out.println("log分析完成");
		} catch (NoLogException e) {
			e.printStackTrace();
		}
	}

	private void dateKey(Map<String, List<String>> map) {
		System.out.println("分析-date参数");
		List<String> logList = map.get(Constant.LOG);
		List<String> dateList = map.get(Constant.DATE);
		System.out.println("---输入的日期为：" + dateList.get(0));
		
		if(dateList == null || dateList.get(0) == Constant.DEFAULT) {
			System.out.println("---未指定date，设为默认default");
			List<File> fileList = TxtTool.getFileList(logList.get(0));
			String result = new String();
			for(int i = 0; i < fileList.size(); i++) {
				result += TxtTool.txt2String(fileList.get(i));
			}
			String[] line = result.split("\\n");
			for(int i = 0;i < line.length;i++) {
				if(!line[i].matches(Constant.BLANKSPACE_REG)) {
					logLine.add(line[i].trim());
				}
			}
		}
		
		String date = dateList.get(0);
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
		try {
			Date dateObj = dateFormat.parse(date);
			List<File> fileList = TxtTool.getFileList(logList.get(0));
			
			//获取文件列表的最后一个文件判断参数值是否超过当前最大日期
			String pathStr = fileList.get(fileList.size() - 1).toString();
			String[] fileStr = pathStr.split(Constant.SPILT_FILEPATH);
			String[] dateStr = fileStr[fileStr.length - 1].split(Constant.SPILT_DOT);
			date = dateStr[0];
			try {
				Date dateObj2 = dateFormat.parse(date);
				if(dateObj2.before(dateObj)) {
					throw new DateOutOfBoundsException("---输入日期越界.");
				}
			}
			catch(DateOutOfBoundsException e){
				e.printStackTrace();
			}
			System.out.println("---处理前的文件列表  " + fileList);
			TxtTool.dateScreen(fileList,dateObj);
			System.out.println("---处理后的文件列表  " + fileList);
			String result = new String();
			for(int i = 0; i < fileList.size(); i++) {
				result += TxtTool.txt2String(fileList.get(i));
			}
			String[] line = result.split("\\n");
			System.out.println("整合后待处理文本：");
			for(int i = 0;i < line.length;i++) {
				if(!line[i].matches(Constant.BLANKSPACE_REG)) {
					System.out.print(line[i]);
					logLine.add(line[i].trim());
				}
			}
			System.out.println("\n---");
			System.out.println("date分析完成");
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
            	if(s.matches(Constant.ANNOTATION_REG))
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
    
    public static List<File> getFileList(String strPath) {
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
        	System.out.println("读取文件中...");
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
                }else { 
                    filelist.add(files[i]);
                }
            }
        	System.out.println("读取完成");
        }
        return filelist;
    }
    
    public static void dateScreen(List<File> fileList, Date dateObj) {			
		for(int i = 0; i < fileList.size(); i++) {
			String pathStr = fileList.get(i).toString();
			String[] fileStr = pathStr.split(Constant.SPILT_FILEPATH);
			String[] dateStr = fileStr[fileStr.length - 1].split(Constant.SPILT_DOT);
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
	public abstract boolean regFit(String string);
	public abstract void handle();
	protected MyHandler nextHandler;
	public final void handleRequest(String string) {
		//如果不符合我的正则表达式，传递给下一个handler
		if(!regFit(string)) {
			if(nextHandler != null) {
				nextHandler.handleRequest(string);
			}
		}else {
			handle();
		}
	}
}

//新增感染患者
class AddipHandler extends MyHandler{
	private String reg = Constant.ADD_IP_REG;
	private String province;
	private int num;
	
	public List<int[]> result;
	public AddipHandler(List<int[]> result) {
		this.result = result;
	}

	@Override
	public boolean regFit(String string) {
		if(string.matches(reg)) {
			String[] splitString = string.split(Constant.SPILT_SPACE);
			province = splitString[0];
			splitString = splitString[3].split(Constant.PERSON);
			num = Integer.valueOf(splitString[0]);
			return true;
		}
		return false;
	}

	@Override
	public void handle() {
		int index = ProvinceValue.keyOfProvince(province);
		result.get(0)[1] +=num;
		result.get(index)[1] += num;
	}
}

//新增疑似患者
class AddSpHandler extends MyHandler{
	private String reg = Constant.ADD_SP_REG;
	private String province;
	private int num;
	
	public List<int[]> result;
	public AddSpHandler(List<int[]> result) {
		this.result = result;
	}
	@Override
	public boolean regFit(String string) {
		if(string.matches(reg)) {
			String[] splitString = string.split(Constant.SPILT_SPACE);
			province = splitString[0];
			splitString = splitString[3].split(Constant.PERSON);
			num = Integer.valueOf(splitString[0]);
			return true;
		}
		return false;
	}

	@Override
	public void handle() {
		int index = ProvinceValue.keyOfProvince(province);
		result.get(0)[2] +=num;
		result.get(index)[2] += num;
	}
}

//患者治愈
class CureHandler extends MyHandler{
	private String reg = Constant.CURE_REG;
	private String province;
	private int num;
	
	public List<int[]> result;
	public CureHandler(List<int[]> result) {
		this.result = result;
	}
	@Override
	public boolean regFit(String string) {
		if(string.matches(reg)) {
			String[] splitString = string.split(Constant.SPILT_SPACE);
			province = splitString[0];
			splitString = splitString[2].split(Constant.PERSON);
			num = Integer.valueOf(splitString[0]);
			return true;
		}
		return false;
	}

	@Override
	public void handle() {
		int index = ProvinceValue.keyOfProvince(province);
		result.get(index)[1] -= num;
		result.get(0)[1] -=num;
		result.get(index)[3] += num;
		result.get(0)[3] +=num;
	}
}

//感染患者流入
class SwapIpHandler extends MyHandler{
	private String reg = Constant.SWAP_IP_REG;
	private String province1;
	private String province2;
	private int num;
	
	public List<int[]> result;
	public SwapIpHandler(List<int[]> result) {
		this.result = result;
	}
	@Override
	public boolean regFit(String string) {
		if(string.matches(reg)) {
			String[] splitString = string.split(Constant.SPILT_SPACE);
			province1 = splitString[0];
			province2 = splitString[3];
			splitString = splitString[4].split(Constant.PERSON);
			num = Integer.valueOf(splitString[0]);
			return true;
		}
		return false;
	}

	@Override
	public void handle() {
		int index1 = ProvinceValue.keyOfProvince(province1);
		int index2 = ProvinceValue.keyOfProvince(province2);
		result.get(index1)[1] -= num;
		result.get(index2)[1] += num;
	}
}

//疑似患者流入
class SwapSpHandler extends MyHandler{
	private String reg = Constant.SWAP_SP_REG;
	private String province1;
	private String province2;
	private int num;
	
	public List<int[]> result;
	public SwapSpHandler(List<int[]> result) {
		this.result = result;
	}
	@Override
	public boolean regFit(String string) {
		if(string.matches(reg)) {
			String[] splitString = string.split(Constant.SPILT_SPACE);
			province1 = splitString[0];
			province2 = splitString[3];
			splitString = splitString[4].split(Constant.PERSON);
			num = Integer.valueOf(splitString[0]);
			return true;
		}
		return false;
	}

	@Override
	public void handle() {
		int index1 = ProvinceValue.keyOfProvince(province1);
		int index2 = ProvinceValue.keyOfProvince(province2);
		result.get(index1)[2] -= num;
		result.get(index2)[2] += num;
	}
}

//患者死亡
class DeathHandler extends MyHandler{
	private String reg = Constant.DEATH_REG;
	private String province;
	private int num;
	
	public List<int[]> result;
	public DeathHandler(List<int[]> result) {
		this.result = result;
	}
	@Override
	public boolean regFit(String string) {
		if(string.matches(reg)) {
			String[] splitString = string.split(Constant.SPILT_SPACE);
			province = splitString[0];
			splitString = splitString[2].split(Constant.PERSON);
			num = Integer.valueOf(splitString[0]);
			return true;
		}
		return false;
	}

	@Override
	public void handle() {
		int index = ProvinceValue.keyOfProvince(province);
		result.get(index)[1] -= num;
		result.get(0)[1] -=num;
		result.get(index)[4] += num;
		result.get(0)[4] +=num;
	}
}

//疑似确诊感染患者
class ChangeHandler extends MyHandler{
	private String reg = Constant.CHANGE_REG;
	private String province;
	private int num;
	
	public List<int[]> result;
	public ChangeHandler(List<int[]> result) {
		this.result = result;
	}
	@Override
	public boolean regFit(String string) {
		if(string.matches(reg)) {
			String[] splitString = string.split(Constant.SPILT_SPACE);
			province = splitString[0];
			splitString = splitString[3].split(Constant.PERSON);
			num = Integer.valueOf(splitString[0]);
			return true;
		}
		return false;
	}

	@Override
	public void handle() {
		int index = ProvinceValue.keyOfProvince(province);
		result.get(index)[1] += num;
		result.get(index)[2] -= num;
		result.get(0)[1] +=num;
		result.get(0)[2] -=num;
	}
}

//排除疑似患者
class ExcludeHandler extends MyHandler{
	private String reg = Constant.EXCLUDE_REG;
	private String province;
	private int num;
	
	public List<int[]> result;
	public ExcludeHandler(List<int[]> result) {
		this.result = result;
	}
	@Override
	public boolean regFit(String string) {
		if(string.matches(reg)) {
			String[] splitString = string.split(Constant.SPILT_SPACE);
			province = splitString[0];
			splitString = splitString[3].split(Constant.PERSON);
			num = Integer.valueOf(splitString[0]);
			return true;
		}
		return false;
	}

	@Override
	public void handle() {
		int index = ProvinceValue.keyOfProvince(province);
		result.get(0)[2] -=num;
		result.get(index)[2] -= num;
	}
}

class Constant{
    public static final String ADD_IP_REG =  "^.*\\s新增\\s感染患者\\s.*$";
    public static final String ADD_SP_REG = "^.*\\s新增\\s疑似患者\\s.*$";
    public static final String CHANGE_REG = "^.*\\s疑似患者\\s确诊感染\\s.*$";
    public static final String CURE_REG = "^.*\\s治愈\\s.*$";
    public static final String DEATH_REG = "^.*\\s死亡\\s.*$";
    public static final String SWAP_IP_REG = "^.*\\s感染患者\\s流入\\s.*$";
    public static final String SWAP_SP_REG = "^.*\\s疑似患者\\s流入\\s.*$";
    public static final String EXCLUDE_REG = "^.*\\s排除\\s疑似患者\\s.*$";
    public static final String BLANKSPACE_REG = "^\\s$";
    public static final String COMMAND_REG = "^\\-.*$";
    public static final String PREFIX_REG = ".* ";
    public static final String SUFFIX_REG = " .*";
    public static final String ANNOTATION_REG = "^\\/\\/.*$";
    public static final String PERSON = "人";
    public static final String SPACE = " ";
    public static final String DEFAULT = "default";
    public static final String SPILT_SPACE = "\\s";
    public static final String SPILT_FILEPATH = "\\\\";
    public static final String SPILT_DOT = "\\.";
    public static final String SPILT_COMMAND = "\\-";
    public static final String LOG = "log";
    public static final String OUT = "out";
    public static final String TYPE = "type";
    public static final String DATE = "date";
    public static final String PROVINCE = "province";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
}

enum ProvinceValue{
	China(0,"全国"), Anhui(1,"安徽"),Aomen(2,"澳门"), Beijing(3,"北京")
	,Chongqin(4,"重庆"), Fujian(5,"福建"),Gansu(6,"甘肃"), Guangdong(7,"广东")
	,Guangxi(8,"广西"), Guizhou(9,"贵州"), Hainan(10,"海南"), Hebei(11,"河北")
	,Henan(12,"河南"), Heilongjiang(13,"黑龙江"),Hubei(14,"湖北"), Hunan(15,"湖南")
	,Jiangsu(16,"江苏"), Jiangxi(17,"江西"), Jilin(18,"吉林"), Liaoning(19,"辽宁")
	,Neimenggu(20,"内蒙古"), Ningxia(21,"宁夏"),Qinghai(22,"青海")
	,Shandong(23,"山东"),Shanxi(24,"山西"),ShanXi(25,"陕西"),Shanghai(26,"上海")
	,Sichuan(27,"四川"),Taiwan(28,"台湾"),Tianjin(29,"天津"),Xizang(30,"西藏")
	,Xinjiang(31,"新疆"),Xianggang(32,"香港"), Yunnan(33,"云南"),Zhejiang(34,"浙江");
	private int key;
	private String text;
	private ProvinceValue(int key,String text){
		this.key = key;
		this.text = text;
	}

	private static HashMap<Integer,String> map = new HashMap<Integer,String>();
	static {
        for(ProvinceValue d : ProvinceValue.values()){
            map.put(d.key,d.text);
        }
    }

	public static int keyOfProvince(String string) {
		for (Entry<Integer, String> entry : map.entrySet()) {
	        if (entry.getValue().equals(string)) {
	            return entry.getKey();
	        }
	    }
		return -1;
	}
	
	public static ProvinceValue valueOf(int ordinal) {
		if (ordinal < 0 || ordinal >= values().length) {
			throw new IndexOutOfBoundsException("Invalid ordinal");
		} 
		return values()[ordinal];
	}
	
	String getText() {
		return text;
	}
	
	int getKey() {
		return key;
	}
}

enum TypeValue{
	IP(1,"感染患者","ip"), SP(2,"疑似患者","sp"), CURE(3,"治愈","cure")
	,DEAD(4,"死亡","dead");
	private int key;
	private String text;
	private String eng;
	private TypeValue(int key,String text,String eng){
		this.key = key;
		this.text = text;
		this.eng = eng;
	}
	
	String getText() {
		return text;
	}
	
	int getKey() {
		return key;
	}
	
	String getEng() {
		return eng;
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


class DateOutOfBoundsException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DateOutOfBoundsException(String s){
        super(s);
	}
}

class NoLogException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoLogException(String s){
        super(s);
	}
}

class NoOutException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoOutException(String s){
        super(s);
	}
}