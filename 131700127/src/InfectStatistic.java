/**
 * 
 * @author wzzzq
 *
 */
import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.text.SimpleDateFormat;


public class InfectStatistic {
	public String logPath;		//保存日志路径
	public String outPath;		//保存输出路径
	public String dateStr;
	
	boolean typeIsExist;
	boolean provinceIsExist;
	
	public List<String> types = new ArrayList<String>();
	public List<String> provinces = new ArrayList<String>();
	
	public String[] args;		//保存命令
	public String[] typeStr = {"ip","sp","crue","dead"};            //保存类型
	public String[] provinceStr = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃",
			"广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
			"江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
			"四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};	//保存省份
	
	public LinkedHashMap<String,Integer> ip = new LinkedHashMap<String,Integer>();	//保存各省的感染患者人数
	public LinkedHashMap<String,Integer> sp = new LinkedHashMap<String,Integer>(); 	//保存各省的疑似患者人数
	public LinkedHashMap<String,Integer> cure = new LinkedHashMap<String,Integer>();	//保存各省的治愈人数
	public LinkedHashMap<String,Integer> dead = new LinkedHashMap<String,Integer>();	//保存各省的死亡人数
    
	public String addIp = "\\s*\\S+ 新增 感染患者 \\d+人\\s*";
    public String addSp = "\\s*\\S+ 新增 疑似患者 \\d+人\\s*";
    public String inflowIp = "\\s*\\S+ 感染患者 流入 \\S+ \\d+人\\s*";
    public String inflowSp = "\\s*\\S+ 疑似患者 流入 \\S+ \\d+人\\s*";
    public String addDead = "\\s*\\S+ 死亡 \\d+人\\s*";
    public String addCure = "\\s*\\S+ 治愈 \\d+人\\s*";
    public String spToIp = "\\s*\\S+ 疑似患者 确诊感染 \\d+人\\s*";
    public String reduceSp = "\\s*\\S+ 排除 疑似患者 \\d+人\\s*";
	
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	//设定日期格式
	Date d = new Date(System.currentTimeMillis());
	public String date = dateFormat.format(d);
	
	//构造函数初始化LinkedHashMap
	public InfectStatistic() {
		for(int i = 0;i < provinceStr.length;i++) {
			ip.put(provinceStr[i], 0);
			sp.put(provinceStr[i], 0);
			cure.put(provinceStr[i], 0);
			dead.put(provinceStr[i], 0);
		}
	}
	
	//检验参数函数
	public boolean inspectParameter(String [] argsStr) {
		int j;
		args = argsStr;
		if(args.length == 0) {
			System.out.print("No parameters entered.");
			return false;
		}
		if(!argsStr[0].equals("list")) {
			System.out.println("Command line format error.");
			return false;
		}
		for(j = 1;j < argsStr.length;j++) {
			switch(argsStr[j]) {
				case "-log":
					j = inspectLogPath(++j);
					if(j == -1) {
						System.out.println("Command line format error.('-log'parameter)");
						return false;
					}
					else{
						logPath = argsStr[j];
						break;
					}
				case "-date":
					j = inspectDate(++j);
					if(j == -1) {
						System.out.println("Command line format error.('-date'parameter)");
						return false;
					}
					else{
						dateStr = args[j];
						break;
					}
				case "-out":
					j = inspectOutPath(++j);
					if(j == -1) {
						System.out.println("Command line format error.('-outputFile'parameter)");
						return false;
					}
					else{
						outPath = argsStr[j];
						break;
					}
				case "-type":
					j = inspectType(++j);
					if(j == -1) {
						System.out.println("Command line format error.('-type'parameter)");
						return false;
					}
					else
						break;
				case "-province":
					j = inspectProvince(++j); 
					if(j == -1) {
						System.out.println("Command line format error.('-province'parameter)");
						return false;
					}
					else  
						break;
				 default:
					 System.out.println("Unknown error.");
					 return false;
			}
			
		}
		if (types.isEmpty()) {
            types.add("ip");
            types.add("sp");
            types.add("cure");
            types.add("dead");
        }
		return true;
	}
	
	//检验路径
	public int inspectLogPath(int j) {
		if (j != args.length && args[j].matches("^[A-z]:\\\\(.+?\\\\)*$")
				|| (args[j] + "\\").matches("^[A-z]:\\\\(.+?\\\\)*$")) 
				return j;
			else
				return -1;
	}
	
	//检验输出路径
	public int inspectOutPath(int j) {
		if (j != args.length && args[j].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$"))
			return j;
		else return -1;
	}
	
	//检验日期
	public int inspectDate(int j) {
		if(j < args.length) {
			if(isValidDate(args[j])) {
				if(date.compareTo(args[j]) >= 0) 
					return j;
				else 
					return -1;
			}
			else
				return -1;
		}
		else
			return -1;
	}
	
	//检验类型
	public int inspectType(int j) {
		typeIsExist = true;
		if(j < args.length) {
			for(int k = 0; k < typeStr.length && j<args.length; k++) {
				if(args[j].equals(typeStr[k])) { 
					types.add(args[j]); 
					j++;
				}
				else 
					break;
			}
		}
		return (j - 1);
	}
	
	//检验省份
	public int inspectProvince(int j) {
		int k, n = j;
		if(j < args.length){
			while(j<args.length) {
				for(k = 0; k < provinceStr.length; k++) {
					if(args[j].equals(provinceStr[k])) { 
						provinces.add(args[j]); 
						j++;
						break;
					}
				}
			}
			provinceIsExist = true;
			provinces = sort();
		}
		if(n == j) 
			return -1;
		return (j - 1); 
	}
	
	//对省份进行排序
	private List<String> sort() {
        List<String> list = new ArrayList<String>();
        int size = provinceStr.length;
        for (int i = 0; i < size; i++)
            if (provinces.contains(provinceStr[i]))
                list.add(provinceStr[i]);
        return list;
    }
	
	//判断日期是否合法
	public boolean isValidDate(String dateStr) {
        try {
            dateFormat.setLenient(false);
            dateFormat.parse(dateStr);
            String[] sArray = dateStr.split("-");
            for (String s : sArray) {
                boolean isNum = s.matches("[0-9]+");
                if (!isNum)
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
	
	//获取日志文件并处理
	public void execLog() throws Exception{
		File f = new File(logPath);
		String[] logFiles = f.list();
		int l = logFiles.length;
		List<String> legalFiles = new ArrayList<String>();
		for(int i = 0;i < l;i++) {
			String  fileDate = logFiles[i].substring(0, 10);
			String suffix = logFiles[i].substring(logFiles[i].lastIndexOf(".") + 1);
			if(suffix.matches("txt") && dateStr.compareTo(fileDate) >=0)
				legalFiles.add(logFiles[i]);
		}
		l = legalFiles.size();
		if(l == 0)
			throw new IllegalException("Error, no legal log file exists in the log directory");
		logFiles = new String[l];
		legalFiles.toArray(logFiles);
		Arrays.sort(logFiles);
		for(int i = 0;i < l;i++) {
			execFile(logPath + "/" + logFiles[i]);
		}
		int ipSum = 0;
		int spSum = 0;
		int cureSum = 0;
		int deadSum = 0;
		for(Integer i : ip.values())
			ipSum += i;
		ip.put("全国", ipSum);
		for(Integer i : sp.values())
			spSum += i;
		sp.put("全国", spSum);
		for(Integer i : cure.values())
			cureSum += i;
		cure.put("全国", cureSum);
		for(Integer i : dead.values())
			deadSum += i;
		dead.put("全国", deadSum);
		 FileOutputStream outFile = new FileOutputStream(outPath);
	        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outFile, "utf-8"));
	        if (typeIsExist) {
	            if (!provinceIsExist) {
	                List<String> list = new ArrayList<String>();
	                int size = provinceStr.length;
	                for (int i = 0; i < size; i++) {
	                    if (ip.get(provinceStr[i]) == 0 && sp.get(provinceStr[i]) == 0
	                            && cure.get(provinceStr[i]) == 0 && dead.get(provinceStr[i]) == 0)
	                        continue;
	                    else
	                        list.add(provinceStr[i]);
	                }
	                outputFile(writer, list);
	            }
	            else {
	                outputFile(writer, provinces);
	            }
	        }
	      else {
	            if (provinceIsExist) {
	                for (String province : provinces) {
	                    writer.write(province + " 感染患者" + ip.get(province) + "人 疑似患者" + sp.get(province) + "人 治愈"
	                            + cure.get(province) + "人 死亡" + dead.get(province) + "人\n");
	                }

	            } 
	            else {
	                Integer[] ipProvincesAmount = new Integer[ip.size()];
	                ip.values().toArray(ipProvincesAmount);
	                Integer[] spProvincesAmount = new Integer[sp.size()];
	                sp.values().toArray(spProvincesAmount);
	                Integer[] cureProvincesAmount = new Integer[cure.size()];
	                cure.values().toArray(cureProvincesAmount);
	                Integer[] deadProvincesAmount = new Integer[dead.size()];
	                dead.values().toArray(deadProvincesAmount);
	                int size = provinceStr.length;
	                for (int i = 0; i < size; i++) {
	                    if (ipProvincesAmount[i] == 0 && spProvincesAmount[i] == 0 && cureProvincesAmount[i] == 0
	                            && deadProvincesAmount[i] == 0)
	                        continue;
	                    else
	                        writer.write(
	                                provinceStr[i] + " 感染患者" + ipProvincesAmount[i] + "人 疑似患者" + spProvincesAmount[i]
	                                        + "人 治愈" + cureProvincesAmount[i] + "人 死亡" + deadProvincesAmount[i] + "人\n");
	                }

	            }
	        }
	        writer.write("//该文档并非真实数据，仅供测试使用");
	        writer.close();
	}
	
	//输出函数
	private void outputFile(BufferedWriter writer, List<String> provinces) throws Exception {
        for (String province : provinces) {
            writer.write(province);
            int size = types.size();
            String[] needTypes = new String[size];
            types.toArray(needTypes);
            if (size == 1) {
                if (needTypes[0].equals("ip"))
                    writer.write(" 感染患者" + ip.get(province) + "人\n");
                else if (needTypes[0].equals("sp"))
                    writer.write(" 疑似患者" + sp.get(province) + "人\n");
                else if (needTypes[0].equals("cure"))
                    writer.write(" 治愈" + cure.get(province) + "人\n");
                else
                    writer.write(" 死亡" + dead.get(province) + "人\n");
                continue;
            }
            if (needTypes[0].equals("ip"))
                writer.write(" 感染患者" + ip.get(province));
            else if (needTypes[0].equals("sp"))
                writer.write(" 疑似患者" + sp.get(province));
            else if (needTypes[0].equals("cure"))
                writer.write(" 治愈" + cure.get(province));
            else
                writer.write(" 死亡" + dead.get(province));

            for (int i = 1; i < size - 1; i++) {
                if (needTypes[i].equals("ip"))
                    writer.write("人 感染患者" + ip.get(province));
                else if (needTypes[i].equals("sp"))
                    writer.write("人 疑似患者" + sp.get(province));
                else if (needTypes[i].equals("cure"))
                    writer.write("人 治愈" + cure.get(province));
                else
                    writer.write("人 死亡" + dead.get(province));
            }
            if (needTypes[size - 1].equals("ip"))
                writer.write("人 感染患者" + ip.get(province) + "人\n");
            else if (needTypes[size - 1].equals("sp"))
                writer.write("人 疑似患者" + sp.get(province) + "人\n");
            else if (needTypes[size - 1].equals("cure"))
                writer.write("人 治愈" + cure.get(province) + "人\n");
            else
                writer.write("人 死亡" + dead.get(province) + "人\n");
        }
    }

	//处理日志文件
	public void execFile(String path) throws Exception{
		FileInputStream fs = new FileInputStream(new File(path));
		BufferedReader br = new BufferedReader(new InputStreamReader(fs, "UTF-8"));
		String strLine;
		while((strLine = br.readLine()) != null) {
			if(strLine.matches(addIp)) {
				int index = strLine.indexOf(" 新增 感染患者");
                String province = strLine.substring(0, index);
                province.replace(" ", "");
                int sum = getAmount(strLine);
                sum += ip.get(province);
                ip.put(province, sum);	
			}else if (strLine.matches(addSp)) {
                int index = strLine.indexOf(" 新增 疑似患者");
                String province = strLine.substring(0, index);
                province.replace(" ", "");
                int sum = getAmount(strLine);
                sum += sp.get(province);
                sp.put(province, sum);
            }
			else if (strLine.matches(inflowIp)) {
                int index = strLine.indexOf(" 感染患者 流入");
                String outProvince = strLine.substring(0, index);
                outProvince.replace(" ", "");
                int sum = getAmount(strLine);
                index = strLine.indexOf(Integer.toString(sum));
                String inProvince = strLine.substring(strLine.lastIndexOf("流入") + 3, index - 1);
                ip.put(outProvince, ip.get(outProvince) - sum);
                ip.put(inProvince, ip.get(inProvince) + sum);
            } else if (strLine.matches(inflowSp)) {
                int index = strLine.indexOf(" 疑似患者 流入");
                String outProvince = strLine.substring(0, index);
                outProvince.replace(" ", "");
                int sum = getAmount(strLine);
                index = strLine.indexOf(Integer.toString(sum));
                String inProvince = strLine.substring(strLine.lastIndexOf("流入") + 3, index - 1);
                sp.put(outProvince, sp.get(outProvince) - sum);
                sp.put(inProvince, sp.get(inProvince) + sum);
            } else if (strLine.matches(addDead)) {
                int index = strLine.indexOf(" 死亡");
                String province = strLine.substring(0, index);
                province.replace(" ", "");
                int deadSum = getAmount(strLine);
                int ipSum = ip.get(province);
                ip.put(province, ipSum - deadSum);
                deadSum += dead.get(province);
                dead.put(province, deadSum);
            } else if (strLine.matches(addCure)) {
                int index = strLine.indexOf(" 治愈");
                String province = strLine.substring(0, index);
                province.replace(" ", "");
                int cureSum = getAmount(strLine);
                int ipSum = ip.get(province);
                ip.put(province, ipSum - cureSum);
                cureSum += cure.get(province);
                cure.put(province, cureSum);
            } else if (strLine.matches(spToIp)) {
                int index = strLine.indexOf(" 疑似患者 确诊感染");
                String province = strLine.substring(0, index);
                province.replace(" ", "");
                int ipSum = getAmount(strLine);
                sp.put(province, sp.get(province) - ipSum);
                ip.put(province, ip.get(province) + ipSum);
            } else if (strLine.matches(reduceSp)) {
                int index = strLine.indexOf(" 排除 疑似患者");
                String province = strLine.substring(0, index);
                province.replace(" ", "");
                int excludeSum = getAmount(strLine);
                sp.put(province, sp.get(province) - excludeSum);
            }
		}
		br.close();
		fs.close();
	}
	
	//获取数目
	public int getAmount(String s) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        m.find();
        return Integer.parseInt(s.substring(m.start(), m.end()));
    }
	
	//异常类
	class IllegalException extends Exception{
		private String message;
		public  IllegalException(String tMessage) {
			message =tMessage;
		}
		public String toString() {
			return message;
		}
	}
	
	//主函数
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InfectStatistic s = new InfectStatistic();
		if(s.inspectParameter(args)) {
			try {
				s.execLog();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else 
			return;
	}

}
