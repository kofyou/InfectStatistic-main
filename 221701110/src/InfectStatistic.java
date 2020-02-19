import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.Collator;

class Provin {
	String name;// 省名
	int ip;// 确诊人数
	int sp;// 疑似人数
	int cure;// 治愈人数
	int death;// 死亡人数

	public Provin(String name) {
		this.name = name;
		ip = 0;
		sp = 0;
		cure = 0;
		death = 0;
	}
}

@SuppressWarnings("unchecked")
public class InfectStatistic {
	// 判断一个字符串是不是中文
	public static boolean isChinese(String string) {
		int n = 0;
		for (int i = 0; i < string.length(); i++) {
			n = (int) string.charAt(i);
			if (!(19968 <= n && n < 40869)) {
				return false;
			}
		}
		return true;
	}

	public static void arraySort(String[] input) {
		for (int i = 0; i < input.length - 1; i++) {
			for (int j = 0; j < input.length - i - 1; j++) {
				if (input[j].compareTo(input[j + 1]) < 0) {
					String temp = input[j];
					input[j] = input[j + 1];
					input[j + 1] = temp;
				}
			}

		}
	}

	public static Provin getCountry(Provin[] input) {
		Provin country = new Provin("全国");
		for (int i = 0; i < input.length; i++) {
			country.ip += input[i].ip;
			country.sp += input[i].sp;
			country.cure += input[i].cure;
			country.death += input[i].death;
		}
		return country;
	}

	public static void main(String[] args) throws IOException {
		// System.out.println(args[0]);
		String[] message = new String[35];// 存放输出数据的数组
		String path = null, date = null, output = null, allctry = null;
		String[] prvin = new String[34];
		String[] types = new String[4];
		int pnum = 0, tnum = 0, pcout = 0;// 参数中省和类型的个数
		String request = args[0];
		String[] provins = new String[] { "北京", "上海", "天津", "重庆", "黑龙江", "辽宁", "吉林", "河北", "河南", "湖北", "湖南", "山东", "山西",
				"陕西", "安徽", "浙江", "江苏", "福建", "广东", "海南", "四川", "云南", "贵州", "青海", "甘肃", "江西", "台湾", "内蒙古", "宁夏", "新疆",
				"西藏", "广西", "香港", "澳门" };
		Provin[] provinces = new Provin[34];
		for (int i = 0; i < 34; i++) {
			provinces[i] = new Provin(provins[i]);
		}
		int num = 0;
		if (request.equals("list")) {
			// System.out.println(args[0]);
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-date"))
					date = args[i + 1];
				else if (args[i].equals("-log"))
					path = args[i + 1];
				else if (args[i].equals("-out"))
					output = args[i + 1];
				else if (args[i].equals("-province")) {
					for (int k = i + 1; k < args.length; k++) {
						if (isChinese(args[k])) {
							if (args[k].equals("全国"))
								allctry = args[k];
							else {
								prvin[pnum] = args[k];
								pnum++;
							}
						}
					}
				} 
				else if (args[i].equals("-type")) {
					for (int k = i + 1; k < args.length; k++) {
						if (args[k].equals("ip")) {
							types[tnum] = "感染";
							tnum++;
						} 
						else if (args[k].equals("sp")) {
							types[tnum] = "疑似";
							tnum++;
						} 
						else if (args[k].equals("cure")) {
							types[tnum] = "治愈";
							tnum++;
						} 
						else if (args[k].equals("death")) {
							types[tnum] = "死亡";
							tnum++;
						}
					}
				}
			}
		} 
		else {
			System.out.println("input the ritght request");
			System.exit(1);
		}
		if (output == null || path == null) {
			System.out.println("input the ritght request");
			System.exit(1);
		}
		File f = new File(path);
		String[] ss = f.list();
		arraySort(ss);
		if(date==null) date=ss[0];
		num = 0;
		int len = 0;// 统计是截止到哪一个日志文件
		String str1 = date + ".log.txt";
		String[] line = new String[2000];
		if (str1.compareTo(ss[0]) > 0 || str1.compareTo(ss[ss.length - 1]) < 0) {
			System.out.println("dates error!");
			System.exit(1);
		} 
		else {
			for (int ji = 0; ji < ss.length; ji++) {
				if (str1.equals(ss[ji])) {
					len = ji;
				}
				if (str1.compareTo(ss[ji]) < 0 && str1.compareTo(ss[ji + 1]) >= 0) {
					len = ji + 1;
					break;
				}
			}
			for (int i = len, ki = 0; i < ss.length; i++, ki++) {
				FileInputStream f1 = new FileInputStream(path + ss[ss.length - 1 - ki]);
				InputStreamReader reader = new InputStreamReader(f1, "UTF-8");
				BufferedReader bf = new BufferedReader(reader);
				String str = null;
				while ((str = bf.readLine()) != null) {
					if(str.contains("//")) break;
					line[num] = str;
					num++;
				}
				bf.close();
			}

			for (int k = 0; k < num; k++) {
				String[] as = line[k].split(" ");
				if (as[1].equals("新增")) {
					String mes = as[0];
					if (as[2].equals("感染患者")) {
						int ki = 0;
						String coun = as[3].substring(0, as[3].length() - 1);
						int count = Integer.parseInt(coun);
						for (ki = 0; ki < 34; ki++) {
							if (provinces[ki].name.equals(mes)) {
								provinces[ki].ip += count;
								break;
							}
						}
					} 
					else if (as[2].equals("疑似患者")) {
						int ki = 0;
						String coun = as[3].substring(0, as[3].length() - 1);
						int count = Integer.parseInt(coun);
						for (ki = 0; ki < 34; ki++) {
							if (provinces[ki].name.equals(mes)) {
								provinces[ki].sp += count;
								break;
							}
						}
					}
				} 
				else if (as.length==3) {
					String coun = as[2].substring(0, as[2].length() - 1);
					int count = Integer.parseInt(coun);
					for (int ki = 0; ki < 34; ki++) {
						if (provinces[ki].name.equals(as[0])) {
							if(as[1].equals("死亡")) 
							    provinces[ki].death += count;
							else 
								provinces[ki].cure += count;
							provinces[ki].ip -= count;
							break;
						}
					}
				} 
				else if (as[1].equals("排除")) {
					String coun = as[3].substring(0, as[3].length() - 1);
					int count = Integer.parseInt(coun);
					for (int ki = 0; ki < 34; ki++) {
						if (provinces[ki].name.equals(as[0])) {
							provinces[ki].sp -= count;
							break;
						}
					}
				} 
				if (as[2].equals("流入")) {
					int num1 = 0;
					String coun = as[4].substring(0, as[4].length() - 1);
					int count = Integer.parseInt(coun);
					for (int ki = 0; ki < 34; ki++) {
						if (provinces[ki].name.equals(as[0])) {
							if (as[1].equals("感染患者"))
								provinces[ki].ip -= count;
							else
								provinces[ki].sp -= count;
							num1++;
						}
						if (provinces[ki].name.equals(as[3])) {
							if (as[1].equals("感染患者"))
								provinces[ki].ip += count;
							else
								provinces[ki].sp += count;
							num1++;
						}
						if (num1 == 2)
							break;
					}
				}
					
				if (as[2].equals("确诊感染")) {
					String coun = as[3].substring(0, as[3].length() - 1);
					int count = Integer.parseInt(coun);
					for (int ki = 0; ki < 34; ki++) {
						if (provinces[ki].name.equals(as[0])) {
							provinces[ki].ip += count;
							provinces[ki].sp -= count;
							break;
						}
					}
				}
			}

		}

		// ，没有指明省的情况之下
		if (allctry == null && pnum == 0) {
			pnum = 34;
			allctry = "全国";
			for(int ki=0;ki<34;ki++) {
				prvin[ki]=provins[ki];
			}
		}
		if (allctry != null) {
			Provin country = getCountry(provinces);
			String p = country.name + " 感染患者" + country.ip + "人 " + "疑似患者" + country.sp + "人 " + "治愈" + country.cure
					+ "人 " + "死亡" + country.death + "人";
			message[pcout] = p;
			pcout++;
		}
		for (int j = 0; j < 34; j++) {
			for (int ji = 0; ji < pnum; ji++) {
				if (provinces[j].name.equals(prvin[ji])) {
					String p1 = provinces[j].name + " 感染患者" + provinces[j].ip + "人 " + "疑似患者" + provinces[j].sp + "人 "
							+ "治愈" + provinces[j].cure + "人 " + "死亡" + provinces[j].death + "人";
					message[pcout] = p1;
					pcout++;
					break;
				}
			}
		}

		if(tnum==0) {
			tnum=4;
			types[0]="感染";
			types[1]="疑似";
			types[2]="治愈";
			types[3]="死亡";
		}
		String pr = "";
		for (int j = 0; j < pcout; j++) {
			String[] si = message[j].split(" ");
			pr = "";
			pr = pr + si[0] + " ";
			for (int ji = 1; ji < si.length; ji++) {
				for (int ki = 0; ki < tnum; ki++) {
					if (si[ji].contains(types[ki])) {
						pr = pr + si[ji] + " ";
						break;
					}
				}
			}
			message[j] = pr;
		}
		String[] tofile = new String[pcout];// 要写进文件的字符串数组
		for (int j = 0; j < pcout; j++)
			tofile[j] = message[j];
		Comparator com = Collator.getInstance(Locale.CHINA);
        if (allctry != null) {
			String[] change = new String[pcout - 1];
			for (int j = 1; j < pcout; j++)
				change[j - 1] = message[j];
			Arrays.sort(change, com);
			for (int j = 0; j < pcout - 1; j++)
				tofile[j + 1] = change[j];
		}

		else
			Arrays.sort(tofile, com);
		File fa = new File(output);
		// 用FileOutputSteam包装文件，并设置文件可追加
		OutputStream out = new FileOutputStream(fa, true);
		// 字符数组
		String pi2 = "日期 :" + date;
		String pi1 = "执行命令: ";
		for (int len1 = 0; len1 < args.length; len1++)
			pi1 = pi1 + " " + args[len1];
		for (int ji = 0; ji < pcout; ji++) {
			System.out.println(tofile[ji]);
			out.write(tofile[ji].getBytes()); // 向文件中写入数据
			out.write('\r'); // \r\n表示换行
			out.write('\n');
		}
		out.write(pi2.getBytes()); // 向文件中写入数据
		out.write('\r'); // \r\n表示换行
		out.write('\n');
		out.write(pi1.getBytes());
		out.write('\r'); // \r\n表示换行
		out.write('\n');
		pi1="// 该文档并非真实数据，仅供测试使用";
		out.write(pi1.getBytes());
		out.write('\r'); // \r\n表示换行
		out.write('\n');
		out.close();
	}
}