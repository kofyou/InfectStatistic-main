import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * InfectStatistic
 * TODO
 *
 * @author zyt
 * @version 1.0
 * @since xxx
 */
class InfectStatistic {
    String[] args;  //接收命令行参数
	int[][] allStastic=new int[32][4];  //使用二维数组存储疫情数据，一维代表省份，二维代表各省份患者类型数据
	
	String logPath;  //日志文件路径
	String resultPath;  //输出文件路径
	
	//-date参数相关
	boolean hasDate=false; //记录是否记录-date参数
	String date;  //指定日期
	
	//-type参数相关
	boolean hastype=false;  //记录是否传入-type参数
	String[] type=new String[4]; //记录-type相关参数值
	String[] typeStrings = {"感染患者", "疑似患者", "治愈", "死亡"}; //具体患者类型
	
	//-province参数相关
	boolean hasprovince=false; //记录是否传入-province参数
	int[] province = new int[32];  //记录-province相关参数值
	String[] provinceStrings = {"全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西",
		"贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", 
		"宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};  //具体省份
	
	/**
	 * 主函数
	 * @param args
	 */
	public static void main(String[] args) {
		InfectStatistic infectStatistic = new InfectStatistic();
		infectStatistic.resolveCmd(args);
    }
	
	/**
	 * 解析命令行参数
	 * @param args
	 */
	public boolean resolveCmd(String[] args) {
		if(!args[0].equals("list")) {  
		    System.out.println("输入的命令有误！");
		    return false;
		}
			
		for (int i=1; i<args.length; i++) {
			switch (args[i]) {
				case "-log":
					logPath =args[i+1];
					break;
				case "-out":
					resultPath=args[i+1];
					break;
				case "-date":
					hasDate=true;
					date=args[i+1];
					break;
				case "-type":  //将-type选项后面的参数存入type[]数组
					hastype=true;
					for(int k=0;;i++,k++) {
						if (i+1<args.length) {
							if (!args[i+1].matches("-.*")) {
								type[k]=args[i+1];
							} else
								break;
						} else
							break;
					}
					break;
				case "-province":
					hasprovince=true;
					resolveProvince(i);
					break;
				}
			}
		//test
		System.out.println(date+"\n"+logPath+"\n"+resultPath);
		for (int k=0; k<type.length; k++) {
			System.out.println(type[k]);
		}
			return false;
	}
	
	/*
	 * 处理输入的省份参数
	 * 待完善
	 */
	public int resolveProvince(int i) {
		return i;
	}
		
	/*
	 * 读取指定目录下文件
	 */
	 public void getFiles() {
	     File file = new File(logPath);
	     File[] allFile = file.listFiles();  //获取目录下的所有文件
         if (hasDate) {
        	 for (int i=0; i<allFile.length; i++) {
        		 if (allFile[i].getName().compareTo(date+".log.txt")<=0) {
        			 readFile(logPath+allFile[i].getName());
        		 } 
        	 }
         } else {
        	 for (int j=0; j<allFile.length; j++) {
        		 readFile(logPath+allFile[j].getName());
        	 }
         }
	 }
	 
	 /*
	  * 读取文件具体内容
	  */
    public void readFile(String filePath) {
	    try {
		    String line=null;  //存储读取出的文件内容，按行读取日志文件
		    BufferedReader br=new BufferedReader
		        (new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"));
		    while ((line=br.readLine())!=null) {  //按行读取
		        if (line.substring(0,2).equals("//")) { //忽略注释行
				    continue;
		        } else {
		            //按行读取处理文本   		
		        }
            }
		    br.close();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
    }
	 
	 
}

	    

