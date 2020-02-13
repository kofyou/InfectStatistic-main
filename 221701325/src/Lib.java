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