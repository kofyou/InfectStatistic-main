import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 * 注意排序及其他要求的功能  日期超出范围
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
	public static String[] commandStrings = { "-log", "-out", "-date", "-type", "-province" };

	// 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择
	public static String[] typeAbbreviationCommandStrings = { "ip", "sp", "cure", "dead" };
	public static HashMap<String, Integer> typeAbbreviationCommandHashMap = new HashMap<String, Integer>();
	// 存放患者的类型
	public static String[] typeCharCommondStrings = { "感染患者", "疑似患者", "治愈", "死亡" };

	public static String[] cureAndDeadStrings = { "治愈", "死亡" };
	public static String[] addAndExcludeAndDiagnosisStrings = { "新增", "排除", "确诊感染" };
	public static String inflowString = "流入";

	// 存放输入信息
	public static HashMap<String, String> inputHashMap = new HashMap<String, String>();
	// 存放省份与患者
	public static HashMap<String, HashMap<String, Long>> provinceHashMap = new HashMap<String, HashMap<String, Long>>();

	public static String logNameString = "";
	public static String outNameString = "";
	public static String dateString = "";
	public static String[] typeStrings;
	public static String[] provinceStrings;

	public static String[] logNameStrings;

	public static void main(String[] args) {
		init(args);
		try {
			if (readLogName()) {
				readLogContent();
				typeScreen();
				provinceScreen();
				writeOutPut();
			} else {
				System.out.println("日期超出范围");
			}
			// System.out.println(dateString);
			// System.out.println(provinceHashMap.get("全国").get("疑似患者") + "");
			// System.out.println(patientsHashMap.get("治愈") + "");
		} catch (NullPointerException e) {
			// TODO: handle exception
			System.out.println("文件名输入错误！文件不存在或已被移除");
			//e.printStackTrace();
		}catch (ParseException e) {
			// TODO 自动生成的 catch 块
			System.out.println("日期输入错误！格式应为：yyyy-mm-dd");
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			System.out.println("文件名输入错误！文件不存在或已被移除");
			//e.printStackTrace();
		}
	}

	public static void init(String[] args) {
		for (String string : commandStrings) {
			inputHashMap.put(string, "");
		}
		String temString = "";
		for (String string : args) {
			// System.out.println(i+":"+string.charAt(0));
			if (!string.equals("list")) {
				if (string.charAt(0) == '-') {
					temString = string;
				} else {
					inputHashMap.put(temString, inputHashMap.get(temString) + " " + string);
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

		initProvinceHashMap("全国");
		for (int i = 0; i < typeAbbreviationCommandStrings.length; i++) {
			typeAbbreviationCommandHashMap.put(typeAbbreviationCommandStrings[i], i);
		}
	}

	public static void initProvinceHashMap(String provinceString) {
		if (!provinceHashMap.containsKey(provinceString)) {
			// System.out.println("不包含");
			HashMap<String, Long> patientsHashMap = new HashMap<String, Long>();
			Long temLong = new Long(0);
			for (String string : typeCharCommondStrings) {
				patientsHashMap.put(string, temLong);
			}
			provinceHashMap.put(provinceString, patientsHashMap);
		}
	}

	public static boolean readLogName() throws ParseException,NullPointerException {
		File file = new File(logNameString);
		String[] fileList = file.list();
		String temString = "";
		if (dateString.length() != 0) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date inputDate = simpleDateFormat.parse(dateString);
			Date maxDate = simpleDateFormat.parse("0000-00-00");
			for (int i = 0; i < fileList.length; i++) {
				Date date = simpleDateFormat.parse(fileList[i].split("\\.")[0]);
				if (inputDate.compareTo(date) >= 0) {
					temString += " " + fileList[i];
					// System.out.println(simpleDateFormat.format(date));
				}
				if (maxDate.compareTo(date) < 0) {
					maxDate = date;
				}
			}
			if (maxDate.compareTo(inputDate) < 0) {
				return false;
			}
		} else {
			for (int i = 0; i < fileList.length; i++) {
				temString += " " + fileList[i];
			}
		}
		String[] temStrings = temString.split(" ");
		logNameStrings = new String[temStrings.length - 1];
		System.arraycopy(temStrings, 1, logNameStrings, 0, logNameStrings.length);
		return true;
	}

	public static void readLogContent() throws IOException {
		Charset.defaultCharset();
		for (String string : logNameStrings) {
			String pathString = logNameString + string;
			// String fileCharsetString = getFileCharset(pathString);
			// System.out.println("编码格式为" + fileCharsetString);
			File file = new File(pathString);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), "UTF-8"));

			String lineString;
			while ((lineString = bufferedReader.readLine()) != null) {
				lineString = lineString.trim();
				if (!lineString.startsWith("//")) {
					dealLogContent(lineString);
					// System.out.println(lineString);
				}
			}
		}
	}

	public static void dealLogContent(String lineString) {
		String[] inputStrings = lineString.split(" ");
		initProvinceHashMap(inputStrings[0]);
		if (inputStrings.length == 3) {
			if (Arrays.asList(cureAndDeadStrings).contains(inputStrings[1])) {
				cureAndDead(inputStrings);
				inputStrings[0] = "全国";
				cureAndDead(inputStrings);
			}
		} else if (inputStrings.length == 4) {
			int tem = -1;
			if (inputStrings[1].equals(addAndExcludeAndDiagnosisStrings[0])) {
				tem = 0;
			} else if (inputStrings[1].equals(addAndExcludeAndDiagnosisStrings[1])) {
				tem = 1;
			} else if (inputStrings[2].equals(addAndExcludeAndDiagnosisStrings[2])) {
				tem = 2;
			}
			addAndExcludeAndDiagnosis(inputStrings, tem);
			inputStrings[0] = "全国";
			addAndExcludeAndDiagnosis(inputStrings, tem);
		} else if (inputStrings.length == 5) {
			if (inflowString.equals(inputStrings[2])) {
				inflow(inputStrings);
			}
		}
	}

	public static void cureAndDead(String[] inputStrings) {
		Long originalLong = new Long(0);
		Long changesLong = new Long(0);
		originalLong = provinceHashMap.get(inputStrings[0]).get(typeCharCommondStrings[0]);
		changesLong = Long.valueOf(inputStrings[2].substring(0, inputStrings[2].length() - 1));
		originalLong -= changesLong;
		provinceHashMap.get(inputStrings[0]).put(typeCharCommondStrings[0], originalLong);
		originalLong = provinceHashMap.get(inputStrings[0]).get(inputStrings[1]);
		originalLong += changesLong;
		provinceHashMap.get(inputStrings[0]).put(inputStrings[1], originalLong);
	}

	public static void addAndExcludeAndDiagnosis(String[] inputStrings, int i) {
		Long originalLong = new Long(0);
		Long changesLong = new Long(0);
		changesLong = Long.valueOf(inputStrings[3].substring(0, inputStrings[3].length() - 1));
		if (i == 0 || i == 1) {
			originalLong = provinceHashMap.get(inputStrings[0]).get(inputStrings[2]);
			if (i == 0) {
				originalLong += changesLong;
			} else {
				originalLong -= changesLong;
			}
			provinceHashMap.get(inputStrings[0]).put(inputStrings[2], originalLong);
		} else if (i == 2) {
			originalLong = provinceHashMap.get(inputStrings[0]).get(inputStrings[1]);
			originalLong -= changesLong;
			provinceHashMap.get(inputStrings[0]).put(inputStrings[1], originalLong);
			originalLong = provinceHashMap.get(inputStrings[0]).get(typeCharCommondStrings[0]);
			originalLong += changesLong;
			provinceHashMap.get(inputStrings[0]).put(typeCharCommondStrings[0], originalLong);
		}
	}

	public static void inflow(String[] inputStrings) {
		initProvinceHashMap(inputStrings[3]);
		Long originalLong = new Long(0);
		Long changesLong = new Long(0);
		originalLong = provinceHashMap.get(inputStrings[0]).get(inputStrings[1]);
		changesLong = Long.valueOf(inputStrings[4].substring(0, inputStrings[4].length() - 1));
		originalLong -= changesLong;
		provinceHashMap.get(inputStrings[0]).put(inputStrings[1], originalLong);
		originalLong = provinceHashMap.get(inputStrings[3]).get(inputStrings[1]);
		originalLong += changesLong;
		provinceHashMap.get(inputStrings[3]).put(inputStrings[1], originalLong);
	}

	public static void typeScreen() {
		String[] temStrings = typeStrings;
		if (temStrings.length == 0) {
			typeStrings = new String[4];
			for (int i = 0; i < typeAbbreviationCommandStrings.length; i++) {
				typeStrings[i] = typeAbbreviationCommandStrings[i];
			}
			temStrings = typeStrings;
		}
		for (int i = 0; i < temStrings.length; i++) {
			typeStrings[i] = typeCharCommondStrings[typeAbbreviationCommandHashMap.get(temStrings[i])];
		}
	}

	public static void provinceScreen() {
		if (provinceStrings.length == 0) {
			Set set = provinceHashMap.keySet();
			Object[] temObject = set.toArray();
			provinceStrings = new String[temObject.length];
			for (int i = 0; i < temObject.length; i++) {
				provinceStrings[i] = (String) temObject[i];
			}
		} else {
			for (String string : provinceStrings) {
				initProvinceHashMap(string);
			}
		}
		sortProvinceStrings();
	}

	public static void sortProvinceStrings() {
		Comparator<Object> comparator = Collator.getInstance(java.util.Locale.CHINA);
		Arrays.sort(provinceStrings, comparator);
		if (Arrays.asList(provinceStrings).contains("全国")) {
			String temString = "全国";
			for (String string : provinceStrings) {
				if (!string.equals("全国")) {
					temString += " " + string;
				}
			}
			provinceStrings = temString.split(" ");
		}
	}

	public static void writeOutPut() throws IOException {
		File file = new File(outNameString);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		for (String string : provinceStrings) {
			String temString = "";
			// System.out.print(keyString + " ");
			temString += string + " ";
			HashMap<String, Long> patientsHashMap = provinceHashMap.get(string);
			for (String string1 : typeStrings) {
				temString += string1 + "" + patientsHashMap.get(string1) + "人 ";
				// System.out.print(string + ":" + patientsHashMap.get(string) + " ");
			}
			temString = temString.substring(0, temString.length() - 1);
			//System.out.println(temString);
			temString += "\n";
			bufferedWriter.write(temString);
		}
		bufferedWriter.write("// 该文档并非真实数据，仅供测试使用");
		bufferedWriter.close();
	}
	
	public static void returnToBegin() {
		typeAbbreviationCommandHashMap = new HashMap<String, Integer>();
		inputHashMap = new HashMap<String, String>();
		provinceHashMap = new HashMap<String, HashMap<String, Long>>();
		logNameString = "";
		outNameString = "";
		dateString = "";
		String[] temStrings=new String[0];
		typeStrings=temStrings;
		provinceStrings=temStrings;
		logNameStrings=temStrings;
	}
}