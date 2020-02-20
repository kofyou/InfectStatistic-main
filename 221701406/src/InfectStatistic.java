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

/**
 * InfectStatistic
 * TODO
 *
 * @author zyt
 * @version 1.0
 * @since xxx
 */
class InfectStatistic {
	private static final int PROVINCE_NUM=32; //省份数
	private static final int TYPE_NUM=4;  //患者类型数
    String[] args;  //接收命令行参数
	
    //-log参数相关
    boolean hasLog=false;  //记录是否传入-log参数
	String logPath;  //日志文件路径
	
	//-out参数相关
	boolean hasOut=false;  //记录是否传入-out参数
	String resultPath;  //输出文件路径
	
	//-date参数相关
	boolean hasDate=false; //记录是否传入-date参数
	String date;  //指定日期
	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");  //-date参数的正确格式
	
	//-type参数相关
	boolean hasType=false;  //记录是否传入-type参数
	boolean[] type = new boolean[TYPE_NUM];//用于记录传入的-type参数
	String[] typeStrings = {"感染患者", "疑似患者", "治愈", "死亡"}; //具体患者类型
	
	//-province参数相关
	boolean hasProvince=false; //记录是否传入-province参数
	boolean[] province=new boolean[PROVINCE_NUM];  //记录-province相关参数值
	String[] provinceStrings = {"全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西",
		"贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", 
		"宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};  //具体省份
	int[] existProvince=new int[32];  //记录日志文档中是否有对应省份
	
	/*
	 * 使用二维数组存储疫情数据，一维代表省份，二维代表各省份患者类型数据
	 * 省份和provinceStrings数组中省份顺序相对应
	 * 患者类型按顺序分别为:ip,sp,cure,dead
	 */
	int[][] allStatistic=new int[PROVINCE_NUM][TYPE_NUM];
	
	/**
	 * 主函数
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		InfectStatistic infectStatistic = new InfectStatistic();
		infectStatistic.resolveCmd(args);
		infectStatistic.getFiles();
		infectStatistic.ouputFile();
    }
	
	/**
	 * 解析命令行参数
	 * @param args
	 */
	public void resolveCmd(String[] args) {
		if(!args[0].equals("list")) {  
		    System.out.println("输入的命令有误，开头不是list命令！");
		    System.exit(0);
		}
			
		for (int i=0; i<args.length; i++) {
			switch (args[i]) {
				case "-log":
					hasLog=true;
					if(args[i+1].matches("^[A-z]:\\\\(.+?\\\\)*$")) {
					    logPath=args[i+1];
					} else {
						System.out.println("-log选项的参数输入有误，请重新输入！");
						System.exit(0);
					}
					break;
				case "-out":
					hasOut=true;
					if(args[i+1].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$")) { 
					    resultPath=args[i+1];
					} else {
						System.out.println("-out选项的参数输入有误，请重新输入！");
						System.exit(0);
					}
					break;
				case "-date":					
				try {
					hasDate=true;
					date=args[i+1];
					format.setLenient(false);
					format.parse(date);					
				} catch (ParseException e) {
					e.printStackTrace();
					System.out.println("日期格式错误，请重新输入！");
					System.exit(0);
				}
					break;
				case "-type":  //将-type选项后面的参数存入type[]数组
					hasType=true;
					resolveType(i,args);
					break;
				case "-province":
					hasProvince=true;
					resolveProvince(i, args);
					break;
				default:
					break;
				}
			}
		//判断-log,-out选项是否存在
		if (!hasLog||!hasOut) {
			System.out.println("-log,-out选项必须存在，请重新输入命令！");
			System.exit(0);
		}
		
		
		//test
		/*System.out.println(date+"\n"+logPath+"\n"+resultPath);
		for (int k=0; k<type.length; k++) {
			System.out.println(type[k]);
		}
		for (int j=0; j<province.length; j++) {
			System.out.println(province[j]);
		}*/
			return;
	}
	
	/*
	 * 处理输入的-type参数
	 */
	private void resolveType(int i, String args[]){
    	while(i+1<args.length && !args[i+1].matches("-.*")){
    		switch(args[i+1]){
    			case "ip" :
    				type[0]=true;
    				break;
    			case "sp":
    				type[1]=true;
    				break;
    			case "cure":
    				type[2]=true;
    				break;
    			case "dead":
    				type[3]=true;
    				break;
    			default :
    				System.out.println("-type参数输入有误，请重新输入！");
    				System.exit(0);
    		}
    		i++;
    	}
    }
	
	/*
	 * 处理输入的-province参数
	 */
	public void resolveProvince(int i, String args[]) {
		while(i+1<args.length && !args[i+1].matches("-.*")){
    		for (int j=0; j<provinceStrings.length; j++) {
    			if (args[i+1].equals(provinceStrings[j])) {
    				province[j]=true;
    			}
    		}
    		i++;
    	}
	}
		
	/*
	 * 读取指定目录下文件
	 */
	 public void getFiles() {
	     File file = new File(logPath);
	     File[] allFile = file.listFiles();  //获取目录下的所有文件
         if (hasDate) {  //读取-date选项指定的日期及其之前的所有文件
        	 
        	//判断-date提供的日期是否早于日志最早一天的日期
        	 String earliestDate=new String(allFile[0].getName());                      
             if ((earliestDate.compareTo(date+".log.txt"))>0) {
                 System.out.println("日期早于最早提供日志的时间，请重新输入！");
                 return;
             }
             //判断-date提供的日期是否晚于日志最晚一天的日期
             String latestDate=new String(allFile[allFile.length-1].getName());                      
             if ((latestDate.compareTo(date+".log.txt"))<0) {
                 System.out.println("日期晚于最后更新的日志时间，请重新输入！");
                 return;
             }
             
        	 //读取文件
        	 for (int i=0; i<allFile.length; i++) {
        		 if (allFile[i].getName().compareTo(date+".log.txt")<=0) {
        			 //test
        			 //System.out.println(allFile[i].getName());
        			 readFile(logPath+allFile[i].getName());
        		 } 
        	 }
         } else {  //没有-date选项，读取所有文件
        	 for (int j=0; j<allFile.length; j++) {
        		 readFile(logPath+allFile[j].getName());
        	 }
         }
         //test
        /* for (int i=0; i<32; i++) {
 			for (int j=0; j<4; j++) {
 				System.out.print(allStatistic[i][j]);
 			}	
 		}*/
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
		        if (!line.substring(0,2).equals("//")) { //忽略注释行
		        	//System.out.println(line);
		            //按行处理文本
		        	handleFile(line);
		        } 
            }
		    br.close();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
    }
    
    /*
     * 文本处理
     * 该日志中出现以下几种情况：
     *   1、<省> 新增 感染患者 n人
     *   2、<省> 新增 疑似患者 n人
     *   3、<省1> 感染患者 流入 <省2> n人
     *   4、<省1> 疑似患者 流入 <省2> n人
     *   5、<省> 死亡 n人
     *   6、<省> 治愈 n人
     *   7、<省> 疑似患者 确诊感染 n人
     *   8、<省> 排除 疑似患者 n人
              *   感染患者：ip， 疑似患者：sp， 治愈：cure， 死亡：dead
     */
    public void handleFile(String line) {
    	String[] array=line.split(" ");
    	
    	//test
    	/*for (int i=0; i<array.length; i++) {
    		System.out.println(array[i]);
    	}*/
  	
    	switch (array[1]) {
    	    case "新增":
    		    if (array[2].equals("感染患者")) {			    
    		    	addIp(array[0],array[3]);  //array[0]所对应省ip+=n
    		    } else {		    	
    		    	addSp(array[0],array[3]);  //array[0]所对应的省sp+=n
    		    }  
    		    break;
    	    case "感染患者":
    	    	moveIp(array[0], array[3], array[4]);  //array[0]对应的省份ip-=n，array[3]对应的省ip+=n
    	        break;
    	    case "疑似患者":
    	    	if (array[2].equals("流入")) {
    	    		moveSp(array[0],array[3],array[4]);  //array[0]对应的省份sp-=n，array[3]对应的省sp+=n
    	    	} else { 	    		
    	    		changeToIp(array[0],array[3]);  //sp-=n,ip+=n
    	    	}
    	    	break;
    	    case "死亡":    	    	
    	    	addDead(array[0],array[2]);  //array[0]所对应省dead+=n,ip-=n
    	        break;
    	    case "治愈":    	    	
    	    	addCure(array[0],array[2]);  //array[0] cure+=n，ip-=n
    	        break;
    	    case "排除":    	    
    	    	removeSp(array[0],array[3]);  //sp-=n
    	    	break;
    	    default:
    	    	break;
    	}
    }
	
    /*
     * <省> 新增 感染患者 n人
     */
    public void addIp(String str1, String n) {
    	n=n.substring(0, n.length()-1);  //去掉字符串n的最后一位字符“人”
        for (int i=0; i<provinceStrings.length; i++) {  
        	if (str1.equals(provinceStrings[i])) {  //匹配省份
        		allStatistic[0][0]+=Integer.parseInt(n);  //全国感染患者增加
        		allStatistic[i][0]+=Integer.parseInt(n);  //对应省份的感染患者增加
        		
        		existProvince[i]=1;  //记录该省份存在疫情相关情况
        	}
        }
    }
    
    /*
     * <省> 新增 疑似患者 n人
     */
    public void addSp(String str1, String n) {
    	n=n.substring(0, n.length()-1);  //去掉字符串n的最后一位字符“人”
        for (int i=0; i<provinceStrings.length; i++) {  
        	if (str1.equals(provinceStrings[i])) {  //匹配省份
        		allStatistic[0][1]+=Integer.parseInt(n);  //全国感染患者增加
        		allStatistic[i][1]+=Integer.parseInt(n);  //对应省份的感染患者增加
        		
        		existProvince[i]=1;  //记录该省份存在疫情相关情况
        	}
        }
    }
    
    /*
     * <省1> 感染患者 流入 <省2> n人
     */
    public void moveIp(String str1, String str2, String n) {
    	n=n.substring(0, n.length()-1);  //去掉字符串n的最后一位字符“人”
        for (int i=0; i<provinceStrings.length; i++) {  
        	if (str1.equals(provinceStrings[i])) {  //匹配省份一
        		allStatistic[i][0]-=Integer.parseInt(n);  //对应省份的感染患者减少
        	}
        	if (str2.equals(provinceStrings[i])) {  //匹配省份二
        	    allStatistic[i][0]+=Integer.parseInt(n);  //流入省份感染患者增加
        	    
        	    existProvince[i]=1;  //记录该省份存在疫情相关情况
        	}
        }
    }
	 
    /*
     * <省1> 疑似患者 流入 <省2> n人
     */
    public void moveSp(String str1, String str2,String n) {
    	n=n.substring(0, n.length()-1);  //去掉字符串n的最后一位字符“人”
        for (int i=0; i<provinceStrings.length; i++) {  
        	if (str1.equals(provinceStrings[i])) {  //匹配省份一
        		allStatistic[i][1]-=Integer.parseInt(n);  //对应省份的疑似患者减少
        	}
        	if (str2.equals(provinceStrings[i])) {
        	    allStatistic[i][1]+=Integer.parseInt(n);  //流入省份疑似患者增加
        	    
        	    existProvince[i]=1;  //记录该省份存在疫情相关情况
        	}
        }
    }
    
    /*
     * <省> 疑似患者 确诊感染 n人
     */
    public void changeToIp(String str1, String n) {
    	n=n.substring(0, n.length()-1);  //去掉字符串n的最后一位字符“人”
        for (int i=0; i<provinceStrings.length; i++) {  
        	if (str1.equals(provinceStrings[i])) {  //匹配省份   		
        		allStatistic[0][0]+=Integer.parseInt(n);  //全国感染患者增加
        		allStatistic[i][0]+=Integer.parseInt(n);  //对应省份的感染患者增加
        		
        		allStatistic[0][1]-=Integer.parseInt(n);  //全国疑似患者减少
        		allStatistic[i][1]-=Integer.parseInt(n);  //对应省份疑似患者减少
        		
        		existProvince[i]=1;  //记录该省份存在疫情相关情况
        	}
        }
    }
    
    /*
     * <省> 死亡 n人
     */
    public void addDead(String str1, String n) {
    	n=n.substring(0, n.length()-1);  //去掉字符串n的最后一位字符“人”
        for (int i=0; i<provinceStrings.length; i++) {  
        	if (str1.equals(provinceStrings[i])) {  //匹配省份
        		allStatistic[0][3]+=Integer.parseInt(n);  //全国死亡人数增加
        		allStatistic[i][3]+=Integer.parseInt(n);  //对应省份的死亡人数增加
        		
        		allStatistic[0][0]-=Integer.parseInt(n);  //全国感染患者减少
        		allStatistic[i][0]-=Integer.parseInt(n);  //对应省份的感染患者减少
        		
        		existProvince[i]=1;  //记录该省份存在疫情相关情况
        	}
        }
    }

    /*
     * <省> 治愈 n人
     */
	public void addCure(String str1, String n) {
		n=n.substring(0, n.length()-1);  //去掉字符串n的最后一位字符“人”
        for (int i=0; i<provinceStrings.length; i++) {  
        	if (str1.equals(provinceStrings[i])) {  //匹配省份
        		allStatistic[0][2]+=Integer.parseInt(n);  //全国治愈人数增加
        		allStatistic[i][2]+=Integer.parseInt(n);  //对应省份的治愈人数增加
        		
        		allStatistic[0][0]-=Integer.parseInt(n);  //全国感染患者减少
        		allStatistic[i][0]-=Integer.parseInt(n);  //对应省份的感染患者减少
        		
        		existProvince[i]=1;  //记录该省份存在疫情相关情况
        	}
        }
	}

    /*
     * <省> 排除 疑似患者 n人
     */
	public void removeSp(String str1, String n) {
		n=n.substring(0, n.length()-1);  //去掉字符串n的最后一位字符“人”
        for (int i=0; i<provinceStrings.length; i++) {  
        	if (str1.equals(provinceStrings[i])) {  //匹配省份       		
        		allStatistic[0][1]-=Integer.parseInt(n);  //全国疑似患者减少
        		allStatistic[i][1]-=Integer.parseInt(n);  //对应省份的疑似患者减少
        		
        		existProvince[i]=1;  //记录该省份存在疫情相关情况
        	}
        }
	}
	
	/*
	 * 按要求输出到指定文件
	 */
	public void ouputFile() throws IOException, FileNotFoundException {
		existProvince[0]=1;  //将全国存在疫情情况置为1
		BufferedWriter bw=new BufferedWriter
			(new OutputStreamWriter(new FileOutputStream(resultPath), "UTF-8"));
		if (!hasProvince) {
			for (int i=0; i<provinceStrings.length; i++) {
				if (existProvince[i]==1) {
					bw.write(provinceStrings[i]+" ");
			        if (!hasType) {  //输出所有的疫情信息
			            for (int j=0; j<typeStrings.length; j++) {
			            	bw.write(typeStrings[j]+allStatistic[i][j]+"人 ");
			            }
			        } else {  //按传入的Type参数输出相对应类型
				        for (int k=0; k<type.length; k++) {
				        	if (type[k]==true) {
				        		bw.write(typeStrings[k]+allStatistic[i][k]+"人 ");
				        	}
				        }
			        }
			        bw.write("\n");
				}	
			}
			
		} else {
			for (int i=0; i<province.length; i++) {
				if (province[i]==true) {
					bw.write(provinceStrings[i]+" ");
					if (!hasType) {  //输出相应省份全部类型患者数据
						for (int j=0; j<typeStrings.length; j++) {
							bw.write(typeStrings[j]+allStatistic[i][j]+"人 ");
						}
					} else {  //输出相应省份相应类型患者数据
						for (int k=0; k<type.length; k++) {
							if (type[k]==true) {
								bw.write(typeStrings[k]+allStatistic[i][k]+"人 ");
							}
						}
					}
					bw.write("\n");
				}
			}	
		}
		bw.write("// 该文档并非真实数据，仅供测试使用");
		bw.flush();
		bw.close();
	}
}
