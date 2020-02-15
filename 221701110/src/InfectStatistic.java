import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.io.FileWriter;

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

class Dates {
	int month;
	int day;

	public Dates(int mon, int da) {
		month = mon;
		day = da;
	}

}

@SuppressWarnings("unchecked")
public class InfectStatistic {
	//判断一个字符串是不是中文
	public static boolean isChinese(String string){
	    int n = 0;
	    for(int i = 0; i < string.length(); i++) {
	        n = (int)string.charAt(i);
	        if(!(19968 <= n && n <40869)) {
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
    public static Provin getcountry(Provin[] input) {
        Provin country=new Provin("全国");
    	for(int i=0;i<input.length;i++) {
    		country.ip+=input[i].ip;
    		country.sp+=input[i].sp;
    		country.cure+=input[i].cure;
    		country.death+=input[i].death;
    	}
    	return country;
    }
	public static void main(String[] args) throws IOException {
		// System.out.println(args[0]);
		String[] message=new String[35];//存放输出数据的数组
		String path = null, date = null, output = null,allctry=null;
		String[] prvin= new String[34];
		String[] types=new String[4];
		int pnum=0,tnum=0,pcout=0;//参数中省和类型的个数
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
					for(int k=i+1;k<args.length;k++) {
						if(isChinese(args[k])) {
							if(args[k].equals("全国")) 
								allctry=args[k];
							else {
								prvin[pnum]=args[k];
								pnum++;
							}
						}
					}
				}
				else if (args[i].equals("-type")) {
					for(int k=i+1;k<args.length;k++) {
						if(args[k].equals("ip")){
							types[tnum]="感染";
							tnum++;
						}
						else if(args[k].equals("sp")){
							types[tnum]="疑似";
							tnum++;
						}
						else if(args[k].equals("cure")){
							types[tnum]="治愈";
							tnum++;
						}
						else if(args[k].equals("death")){
							types[tnum]="死亡";
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
		// File[] files=f.listFiles();
		String[] ss = f.list();
		arraySort(ss);
		Dates[] Dates = new Dates[ss.length];
		for (int j = 0; j < ss.length; j++) {
			String s = ss[j].substring(5, 7);
			String s2 = ss[j].substring(8, 10);
			int j1 = Integer.parseInt(s);
			int j2 = Integer.parseInt(s2);
			Dates[j] = new Dates(j1, j2);
		}
		if (date != null) {
			int len=0;//统计是截止到哪一个日志文件
			//System.out.println(ss[0]);
			String str1=date + ".log.txt";
			String[] line = new String[2000];
			if(str1.compareTo(ss[0])>0||str1.compareTo(ss[ss.length-1])<0) {
				System.out.println("dates error!");
				System.exit(1);
			}
			else {
				for(int ji=0;ji<ss.length;ji++) {
					if(str1.compareTo(ss[ji])<0||str1.compareTo(ss[ji+1])>=0) {
						len=ji+1;
						break;
					}
				}
				for (int i = len,ki=0; i < ss.length; i++,ki++) {
					FileInputStream f1 = new FileInputStream(path + ss[ss.length - 1 - ki]);
					InputStreamReader reader = new InputStreamReader(f1, "UTF-8");
					BufferedReader bf = new BufferedReader(reader);
					BufferedReader bf2 = new BufferedReader(reader);
					String str = null;
					// String[] line = new String[8];
					// num = 0;
					while ((str = bf.readLine()) != null) {
						line[num] = str;
						num++;
					}
				}
				for (int k = 0; k < num; k++) {
					String[] as = line[k].split(" ");
					// String mes=as[0];
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
						} else if (as[2].equals("疑似患者")) {
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
					} else if (as[1].equals("死亡")) {
						String mes = as[0];
						int ki = 0;
						String coun = as[2].substring(0, as[2].length() - 1);
						int count = Integer.parseInt(coun);
						for (ki = 0; ki < 34; ki++) {
							if (provinces[ki].name.equals(mes)) {
								provinces[ki].death += count;
								provinces[ki].ip -=count;
								break;
							}
						}
					} else if (as[1].equals("治愈")) {
						String mes = as[0];
						int ki = 0;
						String coun = as[2].substring(0, as[2].length() - 1);
						int count = Integer.parseInt(coun);
						for (ki = 0; ki < 34; ki++) {
							if (provinces[ki].name.equals(mes)) {
								provinces[ki].cure += count;
								provinces[ki].ip -=count;
								break;
							}
						}
					} else if (as[1].equals("排除")) {
						String mes = as[0];
						int ki = 0;
						String coun = as[3].substring(0, as[3].length() - 1);
						int count = Integer.parseInt(coun);
						for (ki = 0; ki < 34; ki++) {
							if (provinces[ki].name.equals(mes)) {
								provinces[ki].sp -= count;
								break;
							}
						}
					} else if (as[1].equals("感染患者")) {
						String mes = as[0];
						String mes1 = as[3];
						int ki = 0, num1 = 0;
						String coun = as[4].substring(0, as[4].length() - 1);
						int count = Integer.parseInt(coun);
						for (ki = 0; ki < 34; ki++) {
							if (provinces[ki].name.equals(mes)) {
								provinces[ki].ip -= count;
								num1++;
							}
							if (provinces[ki].name.equals(mes1)) {
								provinces[ki].ip += count;
								num1++;
							}
							if (num1 == 2)
								break;
						}
					}
					else if (as[1].equals("疑似患者")) {
						if (as[2].equals("流入")) {
							String mes = as[0];
							String mes1 = as[3];
							int ki = 0, num1 = 0;
							String coun = as[4].substring(0, as[4].length() - 1);
							int count = Integer.parseInt(coun);
							for (ki = 0; ki < 34; ki++) {
								if (provinces[ki].name.equals(mes)) {
									provinces[ki].sp -= count;
									num1++;
								}
								if (provinces[ki].name.equals(mes1)) {
									provinces[ki].sp += count;
									num1++;
								}
								if (num1 == 2)
									break;
							}
						} 
						else if (as[2].equals("确诊感染")) {
							String mes = as[0];
							int ki = 0, num1 = 0;
							String coun = as[3].substring(0, as[3].length() - 1);
							int count = Integer.parseInt(coun);
							for (ki = 0; ki < 34; ki++) {
								if (provinces[ki].name.equals(mes)) {
									provinces[ki].ip += count;
									provinces[ki].sp -= count;
									break;
								}
							}
						}
					}
					//System.out.println(line[k]);
				}
				
				/*for (int j = 0; j < 34; j++)
					System.out.println(provinces[j].name + " 感染患者" + provinces[j].ip + "人 " + " 疑似患者" + provinces[j].sp
							+ "人 " + "治愈" + provinces[j].cure + "人 " + "死亡" + provinces[j].death + "人");*/
			}
		}
		else {
			num = 0;
			date=ss[0];
			String[] line = new String[2000];
			Dates begindate = Dates[ss.length - 1];
			for (int i = 0; i < ss.length; i++) {
				FileInputStream f1 = new FileInputStream(path + ss[ss.length - 1 - i]);
				InputStreamReader reader = new InputStreamReader(f1, "UTF-8");
				BufferedReader bf = new BufferedReader(reader);
				BufferedReader bf2 = new BufferedReader(reader);
				String str = null;
				// String[] line = new String[8];
				// num = 0;
				while ((str = bf.readLine()) != null) {
					line[num] = str;
					num++;
				}
			}
			for (int k = 0; k < num; k++) {
				String[] as = line[k].split(" ");
				// String mes=as[0];
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
					} else if (as[2].equals("疑似患者")) {
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
				} else if (as[1].equals("死亡")) {
					String mes = as[0];
					int ki = 0;
					String coun = as[2].substring(0, as[2].length() - 1);
					int count = Integer.parseInt(coun);
					for (ki = 0; ki < 34; ki++) {
						if (provinces[ki].name.equals(mes)) {
							provinces[ki].death += count;
							provinces[ki].ip -=count;
							break;
						}
					}
				} else if (as[1].equals("治愈")) {
					String mes = as[0];
					int ki = 0;
					String coun = as[2].substring(0, as[2].length() - 1);
					int count = Integer.parseInt(coun);
					for (ki = 0; ki < 34; ki++) {
						if (provinces[ki].name.equals(mes)) {
							provinces[ki].cure += count;
							provinces[ki].ip -=count;
							break;
						}
					}
				} else if (as[1].equals("排除")) {
					String mes = as[0];
					int ki = 0;
					String coun = as[3].substring(0, as[3].length() - 1);
					int count = Integer.parseInt(coun);
					for (ki = 0; ki < 34; ki++) {
						if (provinces[ki].name.equals(mes)) {
							provinces[ki].sp -= count;
							break;
						}
					}
				} else if (as[1].equals("感染患者")) {
					String mes = as[0];
					String mes1 = as[3];
					int ki = 0, num1 = 0;
					String coun = as[4].substring(0, as[4].length() - 1);
					int count = Integer.parseInt(coun);
					for (ki = 0; ki < 34; ki++) {
						if (provinces[ki].name.equals(mes)) {
							provinces[ki].ip -= count;
							num1++;
						}
						if (provinces[ki].name.equals(mes1)) {
							provinces[ki].ip += count;
							num1++;
						}
						if (num1 == 2)
							break;
					}
				}
				else if (as[1].equals("疑似患者")) {
					if (as[2].equals("流入")) {
						String mes = as[0];
						String mes1 = as[3];
						int ki = 0, num1 = 0;
						String coun = as[4].substring(0, as[4].length() - 1);
						int count = Integer.parseInt(coun);
						for (ki = 0; ki < 34; ki++) {
							if (provinces[ki].name.equals(mes)) {
								provinces[ki].sp -= count;
								num1++;
							}
							if (provinces[ki].name.equals(mes1)) {
								provinces[ki].sp += count;
								num1++;
							}
							if (num1 == 2)
								break;
						}
					} 
					else if (as[2].equals("确诊感染")) {
						String mes = as[0];
						int ki = 0, num1 = 0;
						String coun = as[3].substring(0, as[3].length() - 1);
						int count = Integer.parseInt(coun);
						for (ki = 0; ki < 34; ki++) {
							if (provinces[ki].name.equals(mes)) {
								provinces[ki].ip += count;
								provinces[ki].sp -= count;
								break;
							}
						}
					}
				}
				//System.out.println(line[k]);
			}
			/*for (int j = 0; j < 34; j++)
				System.out.println(provinces[j].name + " 感染患者" + provinces[j].ip + "人 " + " 疑似患者" + provinces[j].sp
						+ "人 " + "治愈" + provinces[j].cure + "人 " + "死亡" + provinces[j].death + "人");*/
		}
		//没有指明省份的情况下
		
		if(allctry==null&&pnum==0) {
			pcout=35;
		     Provin country=getcountry(provinces);
		     String p=country.name + " 感染患者" + country.ip + "人 " + "疑似患者" + country.sp
						+ "人 " + "治愈" + country.cure + "人 " + "死亡" + country.death + "人";
		     message[0]=p;
		     for(int j=0;j<34;j++) {
		    	 String p1=provinces[j].name + " 感染患者" + provinces[j].ip + "人 " + "疑似患者" + provinces[j].sp
							+ "人 " + "治愈" + provinces[j].cure + "人 " + "死亡" + provinces[j].death + "人";
		    	 message[j+1]=p1;
		     }
		     for(int j=0;j<message.length;j++)
		    	 System.out.println(message[j]);
		}
		else {
			pcout=0;
			if(allctry!=null) {
				 Provin country=getcountry(provinces);
			     String p=country.name + " 感染患者" + country.ip + "人 " + "疑似患者" + country.sp
							+ "人 " + "治愈" + country.cure + "人 " + "死亡" + country.death + "人";
			     message[pcout]=p;
			     pcout++;
			}
			for(int j=0;j<34;j++) {
				for(int ji=0;ji<pnum;ji++)
				{
					if(provinces[j].name.equals(prvin[ji])) {
						String p1=provinces[j].name + " 感染患者" + provinces[j].ip + "人 " + "疑似患者" + provinces[j].sp
								+ "人 " + "治愈" + provinces[j].cure + "人 " + "死亡" + provinces[j].death + "人";
			    	 message[pcout]=p1;
			    	 pcout++;
			    	 break;
					}
				}
		     }
			for(int j=0;j<pcout;j++)
		    	 System.out.println(message[j]);
		}
		//指明类型的情况下
		if(tnum!=0) {
			//boolean flag=true;
			String pr="";
			for(int j=0;j<pcout;j++) {
				String[] si = message[j].split(" ");
				pr="";
				pr=pr+si[0]+" ";
				for(int ji=1;ji<si.length;ji++) {
					boolean flag=true;
					for(int ki=0;ki<tnum;ki++) {
						if(si[ji].contains(types[ki])) {
							pr=pr+si[ji]+" ";
							break;
						}
					}
				}
				message[j]=pr;
			}
		}
		//
		String[] tofile=new String[pcout];//要写进文件的字符串数组
		for(int j=0;j<pcout;j++)
			tofile[j]=message[j];
		Comparator com = Collator.getInstance(Locale.CHINA);
		//Arrays.sort(line,com);
		if(allctry!=null) {
			String[] change=new String[pcout-1];
			for(int j=1;j<pcout;j++)
				change[j-1]=message[j];
			Arrays.sort(change,com);
			for(int j=0;j<pcout-1;j++)
				tofile[j+1]=change[j];
		}
		
		else Arrays.sort(tofile,com);
		FileWriter writer;
		// 在d盘上创建一个名为testfile的文本文件
		File fa = new File(output);
		// 用FileOutputSteam包装文件，并设置文件可追加
		OutputStream out = new FileOutputStream(fa, true);
		// 字符数组
		String pi2="日期 :"+date;
		String pi1="执行命令: ";
		for(int len=0;len<args.length;len++)
			pi1=pi1+" "+args[len];
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
	}
}