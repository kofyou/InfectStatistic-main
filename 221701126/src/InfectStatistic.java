import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.OutputDeviceAssigned;








/**
 * InfectStatistic
 * TODO
 *
 * @author 221701126_唐靖钧
 * @version 1.0
 * @since 2020/2/8
 */
class InfectStatistic {
	public static String inputPath = "C:\\Users\\Peter\\Documents\\GitHub\\InfectStatistic-main\\221701126\\log\\";
	public static String outputPath = "C:\\Users\\Peter\\Documents\\GitHub\\InfectStatistic-main\\221701126\\result\\out.txt";
	public static String targetDate = "";
	public static ArrayList<String> provinceArray = new ArrayList<String>();
	public static Map<String, Province> map = new HashMap<String, Province>();
	public static Province country = new Province();//用于统计全国人数
	public static boolean ip = false;
	public static boolean sp = false;
	public static boolean cure = false;
	public static boolean dead = false;
	public static ArrayList<String> typeItem = new ArrayList<String>();
	public static ArrayList<String> provinceItem = new ArrayList<String>();
	public static String[] province = {"全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南", "河北",
			"河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西",
			"上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"
	};
	
	//获取目录下日期最大的文件名
	public static String getMaxDate() {
		String maxDate = "0";
		File file = new File(inputPath);
		if(file.exists()) {
			String[] fileNames = file.list(); // 获得目录下的所有文件的文件名
			for(String fileName : fileNames) {
				//去后缀
				fileName = fileName.substring(0, fileName.indexOf('.'));
				//日期比较
				if(fileName.compareTo(maxDate) > 0) {
					maxDate = fileName;
				}
			}
		}
		
		return maxDate;
	}
	
	//将命令行参数转换成相应变量存储起来
	public static void solveArgs(String[] args) {
		int i = 0;
		int pos = 1;
		while(pos < args.length) {
			String arg = args[pos];
		//	System.out.println(pos + "-" + arg);
			if(arg.indexOf('-') == 0) {//这是命令
				
	    		if(arg.equals("-log")) {//处理输入路径
	    			inputPath = args[pos + 1] + "\\";
	    			pos+=2;
	    		}
	    		else if(arg.equals("-out")) {//处理输出路径
	    			outputPath = args[pos + 1] + "\\";
	    			pos+=2;
	    		}
	    		else if(arg.equals("-date")) {//处理日期
	    			targetDate = args[pos + 1];
	    			pos+=2;
	    		}
	    		else if(arg.equals("-type")) {
	    			for(i = pos + 1; i < args.length; i++) {
	    				String param = args[i];
	    				if(param.indexOf('-') != 0) {//这是参数
	    					typeItem.add(param);
		    			}
		    			else {
		    				pos = i;
		    				break;
		    			}
	    			}
	    		}
	    		else if(arg.equals("-province")) {//处理province命令
	    			for(i = pos + 1; i < args.length; i++) {
	    				String param = args[i];
	    				if(param.indexOf('-') != 0) {//这是参数
	    					provinceItem.add(param);
		    			}
		    			else {
		    				pos = i;
		    				break;
		    			}
	    			}
	    		}
	    		
//	    		for(i = pos + 1; i < args.length; i++) {
//	    			//System.out.println("I:" + i);
//	    			String newArg = args[i];
//	    			if(newArg.indexOf('-') != 0) {//这是参数
//	    				oneOrder.orderParams.add(newArg);
//	    			}
//	    			else {
//	    				pos = i;
//	    				break;
//	    			}
//	    		}
//	    		orders.add(oneOrder);
	    		if(i == args.length) {
	    			break;
	    		}
	    	}
//			
		}
		
	}
	
	
	//打印一个省的信息
	public static void printTheProvince(String provinceName, OutputStreamWriter osw) {
		try {
			
			osw.write(provinceName);
			System.out.print(provinceName);
			if(map.get(provinceName) != null) {
				Province province = map.get(provinceName);
				if(typeItem.size() != 0) {
					for(String item : typeItem) {
						switch (item) {
						case "ip":
							System.out.print(" 感染患者" + province.infect + "人");
							osw.write(" 感染患者" + province.infect + "人");
							break;
						case "sp":
							System.out.print(" 疑似患者" + province.seeming + "人");
							osw.write(" 疑似患者" + province.seeming + "人");
							break;
						case "cure":
							System.out.print(" 治愈" + province.cured + "人");
							osw.write(" 治愈" + province.cured + "人");
							break;
						case "dead":
							System.out.print(" 死亡" + province.dead + "人");
							osw.write(" 死亡" + province.dead + "人");
							break;
						default:
							break;
						}
					}
				}
				else {
					osw.write(" 感染患者" + province.infect + "人 疑似患者" + province.seeming + "人 治愈" + province.cured + "人 死亡" + province.dead + "人");
					System.out.print(" 感染患者" + province.infect + "人 疑似患者" + province.seeming + "人 治愈" + province.cured + "人 死亡" + province.dead + "人");
				}
			}
			else {
				if(typeItem.size() != 0) {
					for(String item : typeItem) {
						switch (item) {
						case "ip":
							System.out.print(" 感染患者0人");
							osw.write(" 感染患者0人");
							break;
						case "sp":
							System.out.print(" 疑似患者0人");
							osw.write(" 疑似患者0人");
							break;
						case "cure":
							System.out.print(" 治愈0人");
							osw.write(" 治愈0人");
							break;
						case "dead":
							System.out.print(" 死亡0人");
							osw.write(" 死亡0人");
							break;
						default:
							break;
						}
					}
				}
				else {
					System.out.print(" 感染患者0人 疑似患者0人 治愈0人 死亡0人");
					osw.write(" 感染患者0人 疑似患者0人 治愈0人 死亡0人");
				}
			}
			System.out.println();
			osw.write("\n");
//			fileWriter.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//测试打印结果
	public static void printResult() {
		try {
			File output = new File(outputPath);
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(output));
			if(provinceItem.size() == 0) {
				for(String provinceName : province) {
					if(map.get(provinceName) != null) {
						printTheProvince(provinceName, osw);
					}
				}
			}
			else {
				for(String provinceName : province) {
					if(provinceItem.contains(provinceName)) {
						printTheProvince(provinceName, osw);
					}
				}
			}
			
			osw.flush();
			osw.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//获取各行的人数
	public static int getNumber(String[] information) {
		//获取人数
		String numString = information[information.length - 1];
		int index = numString.indexOf("人");
		numString = numString.substring(0, index);
		int number = Integer.parseInt(numString);
		return number;
	}
	
	//处理待处理文件的每一个文件
	public static void solveEveryFile(Vector<String> toHandleDate) {
		
		
		StringBuffer sb = new StringBuffer();
		for(String dateFile : toHandleDate) {
			dateFile = inputPath + dateFile + ".log.txt";
			System.out.println(dateFile);
			try {
	    		File file = new File(dateFile);
	    		InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
	    		BufferedReader bf = new BufferedReader(inputReader);
	    		
	    		
	    		
	    		String str;
	    		while ((str = bf.readLine()) != null && str.indexOf("//") != 0) {
	    			//System.out.println(str);
	    			String[] information = str.split("\\s+");
	    			//System.out.println(information[0]);
	    			String province = information[0];//先取到省份
	    			int number = getNumber(information);//取出各行人数
	    			
	    			if(map.get(province) != null) {//省份已经出现过
	    				Province p = map.get(province);
	    				switch (information[1]) {
						case "新增":
							if(information[2].equals("感染患者")) {
								p.infect += number;
								country.infect += number;
								//System.out.println(num);
							}
							else {//疑似患者的情况
								p.seeming += number;
								country.seeming += number;
							}
							break;
						case "感染患者":
							String p2 = information[3];//取出流入的省份名称
							if(map.get(p2) != null) {//若该省份已经出现过
								Province anotherProvince = map.get(p2);
								anotherProvince.infect += number;
								p.infect -= number;
							}
							break;
						case "疑似患者":
							//判断是流入还是确诊
							if(information[2].equals("流入")) {
								String p3 = information[3];//取出流入的省份名称
								if(map.get(p3) != null) {//若该省份已经出现过
									Province anotherProvince = map.get(p3);
									anotherProvince.seeming += number;
									p.seeming -= number;
								}
							}
							else {//确诊
								p.infect += number;
								p.seeming -= number;
								country.infect += number;
								country.seeming -= number;
							}
							break;
						case "死亡":
							p.infect -= number;
							p.dead += number;
							country.infect -= number;
							country.dead += number;
							break;
						case "治愈":
							p.infect -= number;
							p.cured += number;
							country.infect -= number;
							country.cured += number;
							break;
						case "排除":
							p.seeming -= number;
							country.seeming -= number;
							break;
						default:
							break;
						}
	    			}
	    			else {//省份还未出现过
	    				Province p = new Province();
	    				p.name = province;
	    				switch (information[1]) {
						case "新增":
							if(information[2].equals("感染患者")) {
								p.infect += number;
								country.infect += number;
								//System.out.println(num);
							}
							else {//疑似患者的情况
								p.seeming += number;
								country.seeming += number;
							}
							break;
						case "感染患者":
							String p2 = information[3];//取出流入的省份名称
							if(map.get(p2) != null) {//若该省份已经出现过
								Province anotherProvince = map.get(p2);
								anotherProvince.infect += number;
								p.infect -= number;
							}
							break;
						case "疑似患者":
							//判断是流入还是确诊
							if(information[2].equals("流入")) {
								String p3 = information[3];//取出流入的省份名称
								if(map.get(p3) != null) {//若该省份已经出现过
									Province anotherProvince = map.get(p3);
									anotherProvince.seeming += number;
									p.seeming -= number;
								}
							}
							else {//确诊
								p.infect += number;
								p.seeming -= number;
								country.infect += number;
								country.seeming -= number;
							}
							break;
						case "死亡":
							p.infect -= number;
							p.dead += number;
							country.infect -= number;
							country.dead += number;
							break;
						case "治愈":
							p.infect -= number;
							p.cured += number;
							country.infect -= number;
							country.cured += number;
							break;
						case "排除":
							p.seeming -= number;
							country.seeming -= number;
							break;
						default:
							break;
						}
	    				map.put(province, p);
	    			}
	    		}			
	    		bf.close();		
	    	//	bw.close();
	    		inputReader.close();
	    	
			} 
	    	catch (IOException  e) {
				// TODO: handle exception
	    		e.printStackTrace();
			}
		}
		
	}
	
	//处理date命令
	public static void solveDateOrder(String targetDate) {
		String maxDate = getMaxDate();
		if(targetDate.compareTo(maxDate) > 0) {
			System.out.println("日期超出范围");
			return;
		}
		//获取输入路径下的所有文件
		File file = new File(inputPath);
		if(file.isDirectory()) {
			Vector<String> toHandleDate = new Vector<String>();//获取符合要求待处理的日期文件
			String[] fileNames = file.list(); // 获得目录下的所有文件的文件名
			for(String fileName : fileNames) {//截断后缀名
				fileName = fileName.substring(0, fileName.indexOf('.'));
				//日期比较
				if(fileName.compareTo(targetDate) <= 0) {
					toHandleDate.add(fileName);
					System.out.println(fileName);
				}
				else {
					break;
				}
				//System.out.println(fileName);
			}

			if(toHandleDate.size() > 0) {
				solveEveryFile(toHandleDate);
			}
			map.put("全国", country);
		}
		
	}
	
	public static void main(String[] args) {
	
    	country.name = "全国";
  
    	solveArgs(args);
    	if(targetDate.equals("")) {
        	targetDate = getMaxDate();
    	}

    	System.out.println("最大日期为" + targetDate);
    	System.out.println(inputPath);
    	System.out.println(outputPath);
    	solveDateOrder(targetDate);
    	printResult();

    }
}


class Province{
	String name;//省名
	int infect;//感染人数
	int seeming;//疑似人数
	int dead;//死亡人数
	int cured;//治愈人数
	
	public Province() {
		infect = seeming = dead = cured = 0;
	}
}
