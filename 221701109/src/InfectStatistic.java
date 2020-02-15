
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;  
public class InfectStatistic {
	public class Province{
		String name;
		int infected = 0;   //确诊病例
		int mayInfect = 0;  //疑似病例
		int cure = 0;       //治愈出院
		int dead = 0;       //死亡病例
		public Province(String name){
			this.name=name;
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException{
		/*各省直辖市及自治区*/
		InfectStatistic t = new InfectStatistic();
		String[] prs = new String[32];
		String[] pros = new String[]{"全国","安徽","北京","重庆","福建","甘肃","广东",
				"广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏",
				"江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川",
				"天津","西藏","新疆","云南","浙江"};
		Province[] provinces = new Province[32];
		for(int i=0;i<32;i++){
			provinces[i] = t.new Province(pros[i]);
			prs[i] = " ";
		}
		String cmd=args[0];
		String[] type = new String[]{" "," "," "," "};
		
		String local = "D:/log/",date = null,output = "D:/output.txt";
		//默认时间为当前时间
		Calendar calendar = Calendar.getInstance();
		date = ""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE)+"";
		if(cmd.equals("list")){
		for(int i=1;i<args.length;i++){
			if(args[i].equals("-date")){
				date=args[i+1];
			}
			else if(args[i].equals("-log")){
				local=args[i+1];
			}
			else if(args[i].equals("-out")){
				output=args[i+1];
			}
			else if(args[i].equals("-type")){
				for(int j=0;j<4;j++)
				if(!args[i+j+1].isEmpty) type[j] = args[i+j+1];       //记录type
				if(type[0].isEmpty()){
					System.out.println("请指定type!");
					System.exit(0);
				}                 
			}
			else if(args[i].equals("-province")){
				for(int j=0;j<args.length-i;j++)
				if(!args[i+j+1].isEmpty) prs[j] = args[i+j+1];       //记录省
				if(prs[0].isEmpty()){
					System.out.println("请指定省份!");
					System.exit(0);
				}
			}
	      }
		}
		else System.out.println("input right cmd.");
		int num=0,lines=0;
		//提取输入目录下的所有文件名
		String basePath=local;
		String[] list=new File(basePath).list();
		if(list[0].equals(null)) System.out.println("选定目录下无文件,请重新选择!");
		else{
		for(int j=0;j<list.length;j++){
		try {
			FileInputStream f = new FileInputStream(local+list[j]);
			InputStreamReader reader = new InputStreamReader(f, "UTF-8");
			BufferedReader bf = new BufferedReader(reader);
			BufferedReader bf2 = new BufferedReader(reader);
			String str = null,str0 = null;
			int a=0,b=0,sum=0;
			while ((str = bf.readLine())!= null){
				String[] line = str.split(" ");  ///以空格间隔提取内容
		    	for(int i=1;i<32;i++){
		    		if(pros[i].equals(line[0])) a=i;  //找到省份位置
		    		if(line[2].equals("流入")){
		    		if(pros[i].equals(line[3])) b=i;
		    		}
		    	}
		    	/*1、2新增感染或疑似*/
				if(line[1].equals("新增")){
			    	str0=line[3].substring(0,line[3].length()-1);
			    	sum = Integer.parseInt(str0);
					if(line[2].equals("感染患者")){
						provinces[a].infected += sum;
						provinces[0].infected += sum;
					}
					else if(line[2].equals("疑似患者")){
						provinces[a].mayInfect += sum;
						provinces[0].mayInfect += sum;
					}
				}
				/*3、4省流入省*/
				if(line[2].equals("流入")){
					str0=line[4].substring(0,line[4].length()-1);
			    	sum = Integer.parseInt(str0);
			 
					if(line[1].equals("感染患者")){
						provinces[a].infected -= sum;
						provinces[b].infected += sum;
					}
					else if(line[1].equals("疑似患者")) {
						provinces[a].mayInfect -= sum;
						provinces[b].mayInfect += sum;
					}
				}
				/*5、6治愈或死亡*/
				if(line[1].equals("死亡")||line[1].equals("治愈")){
					str0=line[2].substring(0,line[2].length()-1);
			    	sum = Integer.parseInt(str0);
			    	if(line[1].equals("死亡")){
			    		provinces[a].infected -= sum;
			    		provinces[a].dead += sum;
			    		provinces[0].infected -= sum;
			    		provinces[0].dead += sum;
			    	}
			    	else{
			    		provinces[a].infected -= sum;
			    		provinces[a].cure += sum;
			    		provinces[0].infected -= sum;
			    		provinces[0].cure += sum;
			    	}
				}
				/*7疑似确诊*/
				if(line[1].equals("疑似患者")){
					if(line[2].equals("确诊感染")){
						str0=line[3].substring(0,line[3].length()-1);
				    	sum = Integer.parseInt(str0);
						provinces[a].mayInfect -= sum;
						provinces[a].infected += sum;
						provinces[0].mayInfect -= sum;
						provinces[0].infected += sum;
					}
				}
				/*8疑似排除*/
				if(line[1].equals("排除")){
					str0=line[3].substring(0,line[3].length()-1);
			    	sum = Integer.parseInt(str0);
					if(line[2].equals("疑似患者")){
						provinces[a].mayInfect -= sum;
						provinces[0].mayInfect -= sum;
					}
				}
				num++;
			}
			bf.close();
			bf2.close();
			f.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	   }
		String[] print = new String[32];
		for(int i=0;i<32;i++){
			print[i]=provinces[i].name;
		}
		for(int i=0;i<type.length;i++){
			
			for (int j = 0; j < 32; j++) {  
							if(type[i].equals("ip"))
							    print[j] += "\t感染患者"+provinces[j].infected+"人\t";
							if(type[i].equals("sp"))
							    print[j] += " 疑似患者"+provinces[j].mayInfect+"人\t";
							if(type[i].equals("cure"))
								print[j] += " 治愈"+provinces[j].cure+"人\t";
							if(type[i].equals("dead"))
								print[j] += " 死亡"+provinces[j].dead+"人\t";
				}  
			
		}
		/*-type*/
		if(prs[0].equals(" "))
		for (int i = 0; i < 32; i++) { 
			
			  if(provinces[i].cure!=0||provinces[i].dead!=0
				||provinces[i].infected!=0||provinces[i].mayInfect!=0)
			  	System.out.println(print[i]);
			}
		/*-type -province*/
		else{
			for (int i = 0; i < 32; i++) {
				String[] s0 = print[i].split("\t"); 
				for(int j=0;j<prs.length;j++)
			  	if(prs[j].equals(s0[0])) System.out.println(print[i]);
			}
		}
		/*默认情况*/
		if(type[0].equals(" ")&&prs[0].equals(" "))
		for (int i = 0; i < 32; i++) {  
		   if(provinces[i].cure!=0||provinces[i].dead!=0
			||provinces[i].infected!=0||provinces[i].mayInfect!=0){
			   System.out.println(""+provinces[i].name+"\t感染患者"+provinces[i].infected+"人\t"
			   		+ "疑似患者"+provinces[i].mayInfect+"人\t治愈"+provinces[i].cure+"人\t死亡"+provinces[i].dead+"人");
		   }
		}  
	  }
   }

}
