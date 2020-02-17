import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Lib
 * TODO
 *
 * @author wys
 * @version v 1.0.0
 * @since 2020.2.11
 */
public class Lib {
	public static final String str1 = "感染患者";
	public static final String str2 = "疑似患者";
	public static final String str3 = "治愈";
	public static final String str4 = "死亡";
	public static List<String> citiesList = new ArrayList<String>();
	
	/**
	 * 在类中静态初始化
	 */
	static{
		citiesList.add("重庆");
		citiesList.add("福建");
		citiesList.add("甘肃");
		citiesList.add("广东");
		citiesList.add("广西");
		citiesList.add("贵州");
		citiesList.add("海南");
        citiesList.add("河北");
        citiesList.add("江西");
        citiesList.add("江苏");
        citiesList.add("吉林");
        citiesList.add("湖南");
        citiesList.add("黑龙江");
        citiesList.add("河南");
        citiesList.add("山西");
        citiesList.add("山东");
        citiesList.add("青海");
        citiesList.add("宁夏");
        citiesList.add("内蒙古");
        citiesList.add("辽宁");
        citiesList.add("安徽");
        citiesList.add("北京");
        citiesList.add("湖南");
        citiesList.add("湖北");
        citiesList.add("浙江");
        citiesList.add("新疆");
        citiesList.add("云南");
        citiesList.add("西藏");
        citiesList.add("天津");
        citiesList.add("上海");
        citiesList.add("陕西");
        citiesList.add("四川");
	}

	public static void main(String[] args) {
//		String[] arr = { "刘刘", "李飞", "王五", "老三", "贝贝", "啊三","重庆" };
        List<String> list = new ArrayList<String>();
        list.add("重庆");
        list.add("福建");
        list.add("甘肃");
        list.add("广东");
        list.add("广西");
        list.add("贵州");
        list.add("海南");
        list.add("河北");
        list.add("江西");
        list.add("江苏");
        list.add("吉林");
        list.add("湖南");
        list.add("黑龙江");
        list.add("河南");
        list.add("山西");
        list.add("山东");
        list.add("青海");
        list.add("宁夏");
        list.add("内蒙古");
        list.add("辽宁");
        list.add("安徽");
        list.add("北京");
        list.add("湖南");
        list.add("湖北");
        list.add("浙江");
        list.add("新疆");
        list.add("云南");
        list.add("西藏");
        list.add("天津");
        list.add("上海");
        list.add("陕西");
        list.add("四川");

		System.out.println(sortByAlphabet(list));
	}

	/**
	 *  将参数拼成map的方法
	 * @param args
	 */
	public static Map<String,Object> getArgsMap(String[] args) {
		if(args.length<1) {
			System.out.println("没有传入参数");
			return null;
		}
		else {
			//记录两个必会附带指令是否附带了
			boolean hasLog = false;
			boolean hasOut = false;
			//定义变量保存传进来的参数
			String log = "";
			String out = "";
			String date = "";
			List<String> type = new ArrayList<String>();
			List<String> province = new ArrayList<String>();
			//循环参数，确定都有哪些参数
			for(int i=0; i<args.length; i++) {
				//第一个参数必须是list，否则报错
				if(!"list".equals(args[0])) {
					System.out.println("第一个参数必须是list");
					return null;
				}
				if(i>0) {
					//从第二个参数开始确认输入了哪些参数
					//一般都是第一个参数后一个为实际参数值，但是也有情况后面
					//跟着多个参数,多个参数用循环来控制	
					if("-log".equals(args[i])) {
						hasLog = true;
						log = args[i+1];
					} else if("-out".equals(args[i])) {
						hasOut = true;
						out = args[i+1];
					} else if("-date".equals(args[i])) {
						date = args[i+1];
					} else if("-type".equals(args[i])) {
						int j=1;
						while(args.length>i+j && !args[i+j].startsWith("-")) {
							type.add(args[i+j]);
							j++;
						}
					} else if("-province".equals(args[i])) {
						int j=1;
						while(args.length>i+j && !args[i+j].startsWith("-")) {
							province.add(args[i+j]);
							j++;
						}
					}
					
				}
			}
			//缺少了out 或者 log直接提示错误
			if(!hasLog || !hasOut) {
				System.out.println("请注意 -log -out 为必须含有的两个参数");
				return null;
			}
			//如果能执行到这里说明一切正常，拼接参数
			Map<String,Object> argsMap = new HashMap<String,Object>();	
			argsMap.put("log", log);
			argsMap.put("out", out);
			argsMap.put("date",date);
			argsMap.put("type",type);
			argsMap.put("province", province);
			return argsMap;
		}
	}

	/**
	 * 根据字母表给出现的城市排序
	 * @param cities
	 * @return listNewNew
	 */
	public static List<String> sortByAlphabet(List<String> cities) {

		List<String> listNew = new ArrayList<String>();
		List<String> listNewNew = new ArrayList<String>();
		for (String stringOld : cities) {
			listNew.add(stringOld);
		}

		Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
		Collections.sort(listNew, cmp);
		for(String stringNew : listNew) {
			listNewNew.add(stringNew);
		}
		return listNewNew;

	}

	/**
	 * 根据type 值获取对应的中文信息
	 * @return 患者类型中文信息
	 */
	public static String getNamebyType(String type) {
		String temp = "";
		if("ip".equals(type)) {
			temp = "感染患者";
		} else if("sp".equals(type)) {
			temp = "疑似患者";
		} else if("cure".equals(type)) {
			temp = "治愈";
		} else if("dead".equals(type)) {
			temp = "死亡";
		}
		return temp;
	}	
	
}
