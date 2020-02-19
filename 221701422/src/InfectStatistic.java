import java.io.File;
import java.util.HashMap;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
	/**
	 * list的命令行参数的集合，类型为String[]
	 * -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
	 * -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
	 * -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
	 * -type 使用缩写选择如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序[sp, cure]列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
	 * -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
	 */
	static String[] commandStrings = { "-log", "-out", "-date", "-type", "-province" };

	// 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择
	static String[] provinceCommandStrings = { "ip", "sp", "cure", "dead" };

	// 存放输入信息
	private static String[] inputStrings = { "", "", "", "", "" };

	private static HashMap<String, String> inputHashMap = new HashMap<String, String>();

	private static String logNameString = "";
	private static String outNameString = "";
	private static String dateString = "";
	private static String[] typeStrings;
	private static String[] provinceStrings;

	public static void main(String[] args) {
		init(args);
		/*
		 * 
		 * for (String string : inputStrings) { if (string.length() != 0) { System.out.println(string); } }
		 */
		// System.out.println(typeStrings.length);
		System.out.println(dateString);
	}

	private static void init(String[] args) {
		for (String string : commandStrings) {
			inputHashMap.put(string, "");
		}
		String i = "";
		for (String string : args) {
			// System.out.println(i+":"+string.charAt(0));
			if (!string.equals("list")) {
				if (string.charAt(0) == '-') {
					i = string;
				} else {
					inputHashMap.put(i, inputHashMap.get(i) + " " + string);
				}
			}
		}
		String[] temStrings;
		temStrings = inputHashMap.get(commandStrings[0]).split(" ");
		logNameString = temStrings[1];
		temStrings = inputHashMap.get(commandStrings[1]).split(" ");
		outNameString = temStrings[1];
		temStrings = inputHashMap.get(commandStrings[2]).split(" ");
		if (temStrings.length > 1) {
			dateString = temStrings[1];
		}
		temStrings = inputHashMap.get(commandStrings[3]).split(" ");
		typeStrings = new String[temStrings.length - 1];
		System.arraycopy(temStrings, 1, typeStrings, 0, typeStrings.length);
		temStrings = inputHashMap.get(commandStrings[4]).split(" ");
		provinceStrings = new String[temStrings.length - 1];
		System.arraycopy(temStrings, 1, provinceStrings, 0, provinceStrings.length);
	}

}
