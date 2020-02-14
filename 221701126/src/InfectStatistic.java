import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.OutputDeviceAssigned;

import sun.nio.cs.ext.IBM037;






/**
 * InfectStatistic
 * TODO
 *
 * @author 221701126_唐靖钧
 * @version 1.0
 * @since 2020/2/8
 */
class InfectStatistic {
	private static String inputPath = "C://";
	private static String outputPath = "C://";
	private static String targetDate = "";
	private static ArrayList<String> provinceArray = new ArrayList<String>();
	private static Map<String, Province> map = new HashMap<String, Province>();
	 
	//将命令行参数转换成对象存储起来
	private static void solveArgs(String[] args, Vector<Order> orders) {
		int i = 0;
		int pos = 1;
		while(pos < args.length) {
			String arg = args[pos];
		//	System.out.println(pos + "-" + arg);
			if(arg.indexOf('-') == 0) {//这是命令
				
	    		Order oneOrder = new Order();
	    		oneOrder.orderName = arg;
	    		if(arg.equals("-log")) {//处理输入路径
	    			inputPath = args[pos + 1] + "\\";
	    		}
	    		else if(arg.equals("-out")) {//处理输出路径
	    			outputPath = args[pos + 1] + "\\";
	    		}
	    		else if(arg.equals("-date")) {//处理日期
	    			targetDate = args[pos + 1];
	    		}
	    		for(i = pos + 1; i < args.length; i++) {
	    			//System.out.println("I:" + i);
	    			String newArg = args[i];
	    			if(newArg.indexOf('-') != 0) {//这是参数
	    				oneOrder.orderParams.add(newArg);
	    			}
	    			else {
	    				pos = i;
	    				break;
	    			}
	    		}
	    		orders.add(oneOrder);
	    		if(i == args.length) {
	    			break;
	    		}
	    	}
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}
	
	//打印命令对象数组
	private static void printOrders(Vector<Order> orders) {
		for(Order order : orders) {
			System.out.println("命令名为" + order.orderName);
			System.out.println("命令参数为");
			for(String param : order.orderParams) {
				System.out.println(param + " ");
			}
		}
	}
	
	//测试打印结果
	private static void printResult() {
		if(map.get("福建") != null) {
			Province province  = map.get("福建");
			System.out.println("福建感染人数" + province.infect);
			System.out.println("福建疑似人数" + province.seeming);
			System.out.println("福建治愈人数" + province.cured);
			System.out.println("福建死亡人数" + province.dead);
		}
		if(map.get("湖北") != null) {
			Province province  = map.get("湖北");
			System.out.println("湖北感染人数" + province.infect);
			System.out.println("湖北疑似人数" + province.seeming);
			System.out.println("湖北治愈人数" + province.cured);
			System.out.println("湖北死亡人数" + province.dead);
		}
	}
	
	//获取各行的人数
	private static int getNumber(String[] information) {
		//获取人数
		String numString = information[information.length - 1];
		int index = numString.indexOf("人");
		numString = numString.substring(0, index);
		int number = Integer.parseInt(numString);
		return number;
	}
	
	//处理待处理文件的每一个文件
	private static void solveEveryFile(Vector<String> toHandleDate) {
		
		
		StringBuffer sb = new StringBuffer();
		for(String dateFile : toHandleDate) {
			dateFile = inputPath + dateFile + ".log.txt";
			try {
	    		File file = new File(dateFile);
	    		InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
	    		BufferedReader bf = new BufferedReader(inputReader);
	    		
//	    		File output = new File(outputPath + ".output.txt");
//	    		if(!output.exists()){
//	    			output.createNewFile();
//	    		}
//	    		FileWriter fileWriter = new FileWriter(output.getAbsoluteFile());
//	    		BufferedWriter bw = new BufferedWriter(fileWriter);
	    		
	    		
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
								//System.out.println(num);
							}
							else {//疑似患者的情况
								p.seeming += number;
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
							}
							break;
						case "死亡":
							p.infect -= number;
							p.dead += number;
							break;
						case "治愈":
							p.infect -= number;
							p.cured += number;
							break;
						case "排除":
							p.seeming -= number;
							break;
						default:
							break;
						}
	    			}
	    			else {//省份还未出现过
	    				Province p = new Province();
	    				switch (information[1]) {
						case "新增":
							if(information[2].equals("感染患者")) {
								p.infect += number;
								//System.out.println(num);
							}
							else {//疑似患者的情况
								p.seeming += number;
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
							}
							break;
						case "死亡":
							p.infect -= number;
							p.dead += number;
							break;
						case "治愈":
							p.infect -= number;
							p.cured += number;
							break;
						case "排除":
							p.seeming -= number;
							break;
						default:
							break;
						}
	    				map.put(province, p);
	    			}
	    			//System.out.println(str);
	    			//bw.write(str);
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
	private static void solveDateOrder(String targetDate) {
		//获取输入路径下的所有文件
		File file = new File(inputPath);
		if(file.isDirectory()) {
			Vector<String> toHandleDate = new Vector<String>();//获取符合要求待处理的日期文件
			String[] fileNames = file.list(); // 获得目录下的所有文件的文件名
			boolean flag = false;
			for(String fileName : fileNames) {//截断后缀名
				fileName = fileName.substring(0, fileName.indexOf('.'));
				//日期比较
				if(fileName.compareTo(targetDate) <= 0) {
					toHandleDate.add(fileName);
				}
				else {
					flag = true;
					break;
				}
				//System.out.println(fileName);
			}
			if(flag == false) {
				System.out.println("日期超出范围");
				toHandleDate.clear();
			}
			else {
				flag = false;
			}
//			for(String aString : toHandleDate) {
//				System.out.println(aString);
//			}
			if(toHandleDate.size() > 0) {
				solveEveryFile(toHandleDate);
			}
		}
		
	}
	
	public static void main(String[] args) {
	
    	Vector<Order> orders = new Vector<Order>();
//    	try {
//    		if(args.length != 0 && !args[0].equals("list")) {
//        		System.out.println("请输入正确的命令(list)");
//        		return;
//        	}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
    	
    	solveArgs(args, orders);
//    	try {
//    		File file = new File("../log/2020-01-22.log.txt");
//
//    		InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
//    		BufferedReader bf = new BufferedReader(inputReader);
//    		
//    		String str;
//    		while ((str = bf.readLine()) != null) {
//    			System.out.println(str);	
//    		}			
//    		bf.close();			
//    		inputReader.close();
//    	
//		} 
//    	catch (IOException  e) {
//			// TODO: handle exception
//    		e.printStackTrace();
//		}

//		try {
//
//				String content = "a dog will be write in file";
//
//				File file = new File("test_appendfile2.txt");
//
//				if(!file.exists()){
//
//					file.createNewFile();
//
//				}
//
//				FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
//
//				BufferedWriter bw = new BufferedWriter(fileWriter);
//
//				bw.write(content);
//
//				bw.close();
//
//				System.out.println("finish");
//
//		    } catch (IOException e) {
//
//		        e.printStackTrace();
//
//		    }


    	printOrders(orders);
    	System.out.println(inputPath);
    	System.out.println(outputPath);
    	solveDateOrder(targetDate);
    	printResult();
    	//setVariable(orders);
    }
}
class Order{
	String orderName;//命令名
	Vector<String> orderParams;//该命令的参数
	 Order() {
		 orderParams =  new Vector<String>();
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
