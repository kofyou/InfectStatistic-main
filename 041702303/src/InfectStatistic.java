
/**
 * InfectStatistic
 * TODO
 *
 * @author wjchen
 * @version 1.0
 * @since 2020-02-15
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class infectStatistic {
	//省份数
	final int provinceNumber = 34;
	//类型数
	final int typeNumber = 4;
	//各省各类型统计数
	int StatisticsNumber[][];
	//省份参数
	int parameterProvince[];
	//类型参数
	int parameterType[];
	//日志日期
	Date date;
	//输入输出路径
	String logPath, outPath;
	//省份名字
	String provinceName[] = { "安徽", "澳门", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南",
			"吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "香港", "新疆", "台湾", "云南",
			"浙江" };
	//类型中文名
	String typeName[] = { "感染患者", "疑似患者", "治愈", "死亡" };
	//类型英文名
	String enTypeName[] = { "ip", "sp", "cure", "dead" };
	//改变的省份
	int ChangeProvince[] = new int[34];
	//参数存在标记
	boolean isExistParameterType = true;
	boolean isExistParameterProvince = true;
	//命令行
	String args[];

	//默认构造函数
	public infectStatistic() {
		// TODO Auto-generated constructor stub
	}

	//构造函数
	infectStatistic(String args[]) {
		this.args = args;
		StatisticsNumber = new int[provinceNumber][typeNumber];
		for (int i = 0; i < provinceNumber; i++) {
			ChangeProvince[i] = -1;
			Arrays.fill(StatisticsNumber[i], 0);
		}
		parameterProvince = new int[35];
		parameterType = new int[4];
		Arrays.fill(parameterProvince, -1);
		Arrays.fill(parameterType, -1);
	}

	//处理日志文件夹
	public void dealFolder() throws ParseException {
		File folder = new File(logPath);
		File[] validFiles = getFilesByDate(this.date, folder);
		for (File file : validFiles) {
			dealFile(file);
		}
	}

	//处理日志文件
	public void dealFile(File file) {
		try {
			if (!file.exists() && file.isDirectory()) {
				throw new FileNotFoundException();
			}
			InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(inputReader);
			String temp;
			while ((temp = br.readLine()) != null) {
				//跳过//
				if (temp.indexOf("//") != 0) {
					String temps[] = temp.split(" ");
					String number = temps[temps.length - 1].substring(0, temps[temps.length - 1].length() - 1);
					int changeNumber = Integer.parseInt(number);
					int firstProvinceIndex = getProvinceIndex(temps[0]);
					if (temps.length == 4) {
						if (temps[1].equals("新增")) {
							if (temps[2].equals(typeName[0])) {
								StatisticsNumber[firstProvinceIndex][0] += changeNumber;
							} else {
								StatisticsNumber[firstProvinceIndex][1] += changeNumber;
							}
						} else {
							StatisticsNumber[firstProvinceIndex][1] -= changeNumber;
							if (temps[1].equals("疑似患者")) {
								StatisticsNumber[firstProvinceIndex][0] += changeNumber;
							}
						}
					} else if (temps.length == 3) {
						if (temps[1].equals("死亡")) {
							StatisticsNumber[firstProvinceIndex][3] += changeNumber;
						} else {
							StatisticsNumber[firstProvinceIndex][2] += changeNumber;
						}
						StatisticsNumber[firstProvinceIndex][0] -= changeNumber;
					} else {
						int secondProvinceIndex = getProvinceIndex(temps[3]);
						if (temps[1].equals(typeName[0])) {
							StatisticsNumber[firstProvinceIndex][0] -= changeNumber;
							StatisticsNumber[secondProvinceIndex][0] += changeNumber;
						} else {
							StatisticsNumber[firstProvinceIndex][1] -= changeNumber;
							StatisticsNumber[secondProvinceIndex][1] += changeNumber;
						}
					}
					ChangeProvince[getProvinceIndex(temps[0])] = 1;
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//生成输出文件
	public void outResult() throws IOException {
		File file = new File(outPath);
		if (!file.exists()) {
			file.createNewFile();
		}
		OutputStreamWriter outputWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		int[] outTypeIndex = parameterType;
		int[] outProvinceIndex;
		if (isExistParameterProvince) {
			outProvinceIndex = parameterProvince;
		} else {
			outProvinceIndex = ChangeProvince;
		}
		BufferedWriter bw = new BufferedWriter(outputWriter);
		String allString = "";
		// 输出第一行全国
		if (parameterProvince[34] == 1) {
			allString = "全国 ";
			for (int i = 0; i < typeNumber; i++) {
				if (outTypeIndex[i] != -1) {
					allString += typeName[outTypeIndex[i]];
					int count = 0;
					for (int j = 0; j < provinceNumber; j++) {
						count += StatisticsNumber[j][outTypeIndex[i]];
					}
					if (i == typeNumber - 1 || outTypeIndex[i + 1] == -1) {
						allString = allString + count + "人";
					} else {
						allString = allString + count + "人 ";
					}
				}
			}
			allString += "\n";
		}
		// 判断参数省份
		for (int i = 0; i < provinceNumber; i++) {
			if (outProvinceIndex[i] == -1) {
				continue;
			} else if (outProvinceIndex[i] == 1) {
				allString += (provinceName[i] + " ");
				for (int j = 0; j < typeNumber; j++) {
					if (outTypeIndex[j] != -1) {
						allString += typeName[outTypeIndex[j]];
						allString += StatisticsNumber[i][outTypeIndex[j]];
						if (j == typeNumber - 1 || outTypeIndex[j + 1] == -1) {
							allString += "人";
						} else {
							allString += "人 ";
						}
					}
				}
				allString += "\n";
			}
		}
		allString += "// 该文档并非真实数据，仅供测试使用\n//命令：";
		for (int i = 0; i < args.length; i++) {
			allString += args[i];
			if (i != args.length - 1) {
				allString += " ";
			}
		}
		bw.write(allString);
		bw.close();
	}

	// 处理命令行参数并检测其合法性唯一性必要性
	public boolean dealCommand() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int parameterExist[] = { 0, 0, 0, 0, 0 };
		for (int i = 1; i < args.length; i++) {
			if (args[i].equals("-date")) {
				if (i != args.length - 1 && isValidDate(args[++i])) {
					date = dateFormat.parse(args[i]);
					parameterExist[0]++;
				} else {
					return false;
				}
			} else if (args[i].equals("-log")) {
				++i;
				if (i != args.length && args[i].matches("^[A-z]:\\\\(.+?\\\\)*$")
						|| (args[i] + "\\").matches("^[A-z]:\\\\(.+?\\\\)*$")) {
					logPath = args[i];
					parameterExist[1]++;
				} else {
					return false;
				}
			} else if (args[i].equals("-out")) {
				++i;
				String temp = args[i].substring(0, args[i].lastIndexOf("\\") + 1);
				if (i != args.length && temp.matches("^[A-z]:\\\\(.+?\\\\)*$")) {
					outPath = args[i];
					parameterExist[2]++;
				} else {
					return false;
				}
			} else if (args[i].equals("-province")) {
				while (i + 1 < args.length) {
					if (getProvinceIndex(args[++i]) != -1) {
						if (parameterProvince[getProvinceIndex(args[i])] == -1) {
							parameterProvince[getProvinceIndex(args[i])] = 1;
						} else {
							return false;
						}
					} else {
						--i;
						break;
					}
				}
				parameterExist[3]++;
			} else if (args[i].equals("-type")) {
				int j = 0;
				int type[] = { 0, 0, 0, 0 };
				while (i + 1 < args.length) {
					// 检测参数值正确与否，错误i--回退检测是否其他参数
					if (getTypeIndex(args[++i]) != -1) {
						// 判断参数值是否重复
						if (type[getTypeIndex(args[i])] == 0) {
							parameterType[j] = getTypeIndex(args[i]);
							type[getTypeIndex(args[i])] = 1;
							j++;
						} else {
							return false;
						}
					} else {
						--i;
						break;
					}
				}
				parameterExist[4]++;
			} else {
				return false;
			}
		}
		// 检测参数必要唯一
		if (parameterExist[1] == 1 && parameterExist[2] == 1 && parameterExist[0] == 1 && parameterExist[3] < 2
				&& parameterExist[4] < 2) {
			// 无type、province按默认输出
			if (parameterExist[3] == 0) {
				Arrays.fill(parameterProvince, 1);
				isExistParameterProvince = false;
			}
			if (parameterExist[4] == 0) {
				isExistParameterType = false;
				for (int i = 0; i < typeNumber; i++) {
					parameterType[i] = i;
				}
			}
			return true;
		}
		return false;
	}

	//获取文件名对应时间
	public Date getFileNameDate(File file) throws ParseException {
		String date = file.getName();
		int index = date.indexOf(".");
		date = date.substring(0, index);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.parse(date);
	}

	//通过日期参数获取合法日志文件
	public File[] getFilesByDate(Date parameterDate, File folder) throws ParseException {
		ArrayList<File> fileList = new ArrayList<File>();
		if (isExistValidDateFile(folder, parameterDate)) {
			File files[] = folder.listFiles();
			for (File file : files) {
				int result = getFileNameDate(file).compareTo(parameterDate);
				//判断日期，处理小于等于命令行日期的日志文件
				if (result < 0 || result == 0) {
					fileList.add(file);
				}
			}
		} else {
			System.out.println("基于日期参数，日志文件不合法");
		}
		return (File[]) fileList.toArray(new File[fileList.size()]);
	}

	//获取对应省份的下标
	public int getProvinceIndex(String province) {
		for (int i = 0; i < provinceNumber; i++) {
			if (province.equals("全国")) {
				return 34;
			}
			if (province.equals(provinceName[i])) {
				return i;
			}
		}
		return -1;
	}

	//获取对应类型的下标
	public int getTypeIndex(String type) {
		for (int i = 0; i < typeNumber; i++) {
			if (type.equals(enTypeName[i])) {
				return i;
			}
		}
		return -1;
	}

	//检测基于日期参数日志文件是否合法
	public boolean isExistValidDateFile(File folder, Date parameterDate) throws ParseException {
		File files[] = folder.listFiles();
		for (File file : files) {
			int result = getFileNameDate(file).compareTo(parameterDate);
			//判断存在大于日期参数的文件
			if (result > 0 || result == 0) {
				return true;
			}
		}
		return false;
	}

	//判断合法日期
	public static boolean isValidDate(String dateString) throws ParseException {
		char[] testDateArray = dateString.toCharArray();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//去除日期格式化
		if (testDateArray.length == 10 && testDateArray[4] == '-' && testDateArray[7] == '-') {
			for (int i = 0; i < testDateArray.length; i++) {
				if ((testDateArray[i] < '0' || testDateArray[i] > '9') && i != 4 && i != 7) {
					return false;
				}
			}
			//分离年月日
			String time[] = dateString.split("-");
			int year = Integer.parseInt(time[0]);
			int month = Integer.parseInt(time[1]);
			int day = Integer.parseInt(time[2]);
			//常规的日期合法性判断
			if (month > 12 || month < 1) {
				return false;
			} else {
				if (day > 31 || day < 1) {
					return false;
				} else {
					if ((month == 4 || month == 6 || month == 9 || month == 11)) {
						if (day > 30 || day < 0) {
							return false;
						}
					}
					if (year % 4 == 0 && month == 2 && day > 28) {
						return false;
					}
					long parameterDateTime = dateFormat.parse(dateString).getTime();
					//比较命令行日期和当前时间的合法性
					if (parameterDateTime < System.currentTimeMillis()) {
						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}

	//提示命令函数
	public static void validCommandTips() {
		System.out.println("reference:");
		System.out.println("	list<required and first>:");
		System.out.println("		-date<required>		must be valid date and lower now");
		System.out.println("		-log<required>		must be valid folder path");
		System.out.println("		-out<required>		must be valid file path");
		System.out.println("		-type<optional>		must be ip/sp/cure/dead");
		System.out.println("		-province<optional>	must be Chinese province(北京、上海...)/country");
	}

	//主函数
	public static void main(String args[]) throws ParseException, IOException {
		//检测命令行头部list
		if (args.length != 0 && args[0].equals("list") && args.length != 1) {
			infectStatistic test = new infectStatistic(args);
			if (test.dealCommand()) {
				test.dealFolder();
				test.outResult();
				System.out.println("生成数据成功，请查看相应文件");
			} else {
				System.out.println("命令行格式错误");
				infectStatistic.validCommandTips();
			}
		} else {
			System.out.println("命令行格式错误");
			infectStatistic.validCommandTips();
		}
	}
}