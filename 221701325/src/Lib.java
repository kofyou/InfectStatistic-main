import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Lib
 * 1.利用责任链模式对日志文件进行处理
 * 
 *
 * @author Benjamin_Gnep
 * @version 1.0
 * @since 2020/2/11
 */
public class Lib {
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