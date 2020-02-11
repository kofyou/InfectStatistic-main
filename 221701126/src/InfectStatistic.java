import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	private static String inputPath = "C://";
	private static String outputPath = "C://";
	 
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
	    			inputPath = args[pos + 1];
	    		}
	    		else if(arg.equals("-out")) {//处理输出路径
	    			outputPath = args[pos + 1];
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
    	printOrders(orders);
    	System.out.println(inputPath);
    	System.out.println(outputPath);
    	
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
