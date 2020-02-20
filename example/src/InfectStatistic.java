import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import java.io.File;


/**
 * InfectStatistic
 * TODO
 *
 * @author 221701305_林琳
 * @version xxx
 * @since xxx
 */



class InfectStatistic {
	
	class Province{
		String name=null;//省份名称(拼音）
	    int ip=0;//感染患者
	    int sp=0;//疑似患者
	    int cure=0;//治愈
	    int dead=0;//死亡
	    
	    public Province(String name) {
	    	this.name=name;
	    	this.ip=0;
	    	this.sp=0;
	    	this.cure=0;
	    	this.dead=0;
	    }
	    
	    public Province(String name,String num,String type) {
	    	this.name=name;
	    	if(type=="ip") {
	    		this.ip=Integer.valueOf(num);
	    	}
	    	else if(type=="sp") {
	    		this.sp=Integer.valueOf(num);
	    	}
	    }
	    
	    public void deleteNum(String s,String type) {
	    	if(type=="ip")
	    		this.ip-=Integer.valueOf(s);
	    	else 
	    		this.sp-=Integer.valueOf(s);
	    }
	    
	    public void addNum(String s,String type) {
	    	switch(type) {
	    		case "ip":
    	    		this.ip+=Integer.valueOf(s);
    				break;
	    		case "sp":
    				this.sp+=Integer.valueOf(s);
    				break;
	    		case "cure":
    				this.cure+=Integer.valueOf(s);
    				break;
	    		case "dead":
    				this.dead+=Integer.valueOf(s);
    				break;
    			default:
    				break;
	    	}
    	}
	    	
	    
	    public int getIp() {
	    	return ip;
	    }
	    
	    public int getSp() {
	    	return sp;
	    }
	    
	    public int getCure() {
	    	return cure;
	    }
	    
	    public int getDead() {
	    	return dead;
	    }
	    
	    public String getName() {
	    	return name;
	    }
	    
	    
	}

	
	
	//获取省份名称的拼音
	/*public String getProvinceName(String s) {
		//按照拼音顺序排好的全国省份（除去香港、澳门和台湾省）
		String[][] convert = {{"安徽","AnHui"},{"北京","BeiJing"},{"重庆","ChongQing"},{"福建","FuJian"},{"甘肃","GanSu"},
				{"广东","Guangdong"},{"广西",""},{"贵州","GuiZhou"},{"海南","HaiNan"},{"河北","HeBei"},{"河南","HeNan"},{"黑龙江",
				"HeiLongJiang"},{"湖北","HuBei"},{"湖南","HuNan"},{"吉林","JiLin"},{"江苏","JiangSu"},{"江西","JiangXi"},{"辽宁","LiaoNing"},
				{"内蒙古","NeiMengGu"},{"宁夏","NingXia"},{"青海","QingHai"},{"山东","ShanDong"},{"山西","ShanXi"},{"陕西","ShanXi"},
				{"上海","ShangHai"},{"四川","SiChuang"},{"天津","TianJin"},{"西藏","XiZang"},{"新疆","XinJiang"},{"云南","YunNan"},
				{"浙江","ZheJiang"}
		};
		for(int i=0;i<31;i++) {
			if(convert[i][0]==s)
				return convert[i][1];
		}
		return "error";
	}*/
	
    //处理数据
    public void dealStatistic(String filePath,Hashtable<String,Province> hashtable) throws IOException{
    	
    	
    	int position=0;
        String[] bufString=new String[1024];
        //打开读取的文件
        //BufferedReader br = new BufferedReader(new FileReader("F:\\test.txt"));
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line=null;
        
        String[] splitString =new String[5];//字符串分割后存放于此
        
        while((line=br.readLine())!=null) {//按行读取
        	bufString[position]=line;
        	
        	splitString=bufString[position].split(" ");
        	
        	position++;
        	
        	if(!splitString[0].equals("//")){
        		if(splitString.length==4){//新增 确诊 排除 三种情况
         			if(splitString[1].equals("新增")) {
         				if(splitString[2].equals("感染患者")) {
         					if(hashtable.containsKey(splitString[0])==false) {//若哈希表中没有这个省份
         						hashtable.put(splitString[0],new Province(splitString[0],getNumber(splitString[3]),"ip"));
         						
         					}
         					else {
         						hashtable.get(splitString[0]).addNum(getNumber(splitString[3]),"ip");
         						
         					}

         				}
         				else if(splitString[2].equals("疑似患者")){//如果是疑似患者
         					
         					if(hashtable.containsKey(splitString[0])==false) {//若哈希表中没有这个省份
              				
         						hashtable.put(splitString[0],new Province(splitString[0],getNumber(splitString[3]),"sp"));
         					}
         					else {
         						hashtable.get(splitString[0]).addNum(getNumber(splitString[3]),"sp");
         					}
              		}
         			}//if新增结束
         			
         			else if(splitString[2].equals("确诊感染")) {//只有疑似患者会确诊？？？？？？不确定
         				//System.out.println("******ip"+splitString[0]+hashtable.get(splitString[0]).getSp());
         				hashtable.get(splitString[0]).addNum(getNumber(splitString[3]),"ip");
         				hashtable.get(splitString[0]).deleteNum(getNumber(splitString[3]),"sp");
         				//System.out.println("******ip"+splitString[0]+hashtable.get(splitString[0]).getSp());
         			}
          		
         			else if(splitString[1].equals("排除")){//排除 只有疑似患者能排除
         				//System.out.println("******ip"+splitString[0]+hashtable.get(splitString[0]).getSp());
         				hashtable.get(splitString[0]).deleteNum(getNumber(splitString[3]), "sp");
         				//System.out.println("******ip"+splitString[0]+hashtable.get(splitString[0]).getSp());
         			}
          			
            	}
            	
            	else if(splitString.length==5) {//这种情况只有流入一种情况
            		if(hashtable.containsKey(splitString[3])==false) {//如果province中没有这个省份则新建
            			String proName;
            			if(splitString[1].equals("感染患者")) {
            				
                			//proName=getProvinceName(splitString[3]);
                			//被流入的省份ip直接初始为化流入值
                			hashtable.put(splitString[3],new Province(splitString[3],getNumber(splitString[4]),"ip"));
                			//流出ip的省份减
                			hashtable.get(splitString[0]).deleteNum(getNumber(splitString[4]), "ip");
                			//System.out.println("*****新建省"+proName);
                			
                		}
            			else if(splitString[1].equals("疑似患者")){
                    		//proName=getProvinceName(splitString[3]);
                        	//被流入的省份sp直接初始为化流入值
                        	hashtable.put(splitString[3], new Province(splitString[3],getNumber(splitString[4]),"sp"));
                        	//流出sp省份减
                        	hashtable.get(splitString[0]).deleteNum(getNumber(splitString[4]), "sp");
                        	//System.out.println("*****新建省"+proName);
                        					
                        }
                	}
            		else{//如果已有这个省份
                		if(splitString[1].equals("感染患者")) {
                			
                			hashtable.get(splitString[3]).addNum(getNumber(splitString[4]),"ip");
               				hashtable.get(splitString[0]).deleteNum(getNumber(splitString[4]), "ip");
               				//System.out.println("******ip"+splitString[0]+hashtable.get(splitString[3]).getIp());
               			}
               			else {
               				
               				hashtable.get(splitString[3]).addNum(getNumber(splitString[4]),"sp");
               				hashtable.get(splitString[0]).deleteNum(getNumber(splitString[4]), "sp");
               				//System.out.println("******ip"+splitString[0]+hashtable.get(splitString[3]).getSp());
               			}
                			
               		}
            	}
            
               else{//splitString.length==3治愈或死亡 两种情况,并且死亡和治愈的一定是感染患者
            	    if(splitString[1].equals("死亡")) {
            		    hashtable.get(splitString[0]).deleteNum(getNumber(splitString[2]),"ip");
            		    hashtable.get(splitString[0]).addNum(getNumber(splitString[2]),"dead");
            		    //System.out.println("******ip"+splitString[0]+hashtable.get(splitString[0]).getIp());
           			}
           			else if(splitString[1].equals("治愈")) {
           				hashtable.get(splitString[0]).deleteNum(getNumber(splitString[2]),"ip");	
           				hashtable.get(splitString[0]).addNum(getNumber(splitString[2]),"cure");
           				//System.out.println("******ip"+splitString[0]+hashtable.get(splitString[0]).getIp());
           			}
           	   }
              
        	}
        	
        	
        }//while结束
        
        //打印文件内容
        for(int i=0;i<position;i++) {
        	System.out.println(bufString[i]);
        }
        br.close();//关闭文件
       
    }

    //写文件
    public void writeFile(String path,Hashtable<String,Province> proHash,String[] type,String[] province) throws IOException {
    	
    	String[] sortedPro = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林",
    			"江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
    	
    	String content=null;
		FileWriter fileWriter;
		BufferedWriter bw;
		File file = new File(path);
		
		//-province
		if(province==null) {//不指定省份
			//System.out.println("全国 "+"感染患者"+proHash.get("全国").getIp()+"人 疑似患者 "+proHash.get("全国").getSp()+"人 治愈 "+proHash.get("全国").getCure()+"人 死亡 "+proHash.get("全国").getDead()+"人");
			System.out.println("province is null");
			try {	
				if(!file.exists()){
					file.createNewFile();
				}
				for(int i=0;i<32;i++) {
					if(proHash.containsKey(sortedPro[i])) {			
						String keys=sortedPro[i];
						//System.out.println(keys);
						content=proHash.get(keys).getName()+" 感染患者"+proHash.get(keys).getIp()+"人 疑似患者"+proHash.get(keys).getSp()+"人 治愈"+proHash.get(keys).getCure()+"人 死亡"+proHash.get(keys).getDead()+"人 \n";
						fileWriter = new FileWriter(file.getAbsoluteFile(),true);
						bw = new BufferedWriter(fileWriter);
						bw.write(content);
						//System.out.println(content);
						bw.close();
					}
				}
				
				/*Set set = proHash.keySet();
		        Iterator iterator = set.iterator();
		        while(iterator.hasNext()) {
		            Object keys = iterator.next();
		            
					
		        }*/
		    			    	
				//content = "全国 "+"感染患者"+proHash.get("全国").getIp()+"人 疑似患者 "+proHash.get("全国").getSp()+"人 治愈 "+proHash.get("全国").getCure()+"人 死亡 "+proHash.get("全国").getDead()+"人\n";
		    } 
			catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		else{//有指定省份
			for(int i=0;i<32;i++) {
				for(int j=0;i<province.length;j++) {
					if(proHash.containsKey(province[j])&&province[j]==sortedPro[i]) {
						String keys=sortedPro[i];
						System.out.println(keys);
						
						content=keys;
						
						for(int k=0;k<type.length;k++) {
							
							content+=type[k];
							System.out.println("***********");
						}
						content+="人\n";
						System.out.println(content);
						//content=proHash.get(keys).getName()+" 感染患者"+proHash.get(keys).getIp()+"人 疑似患者"+proHash.get(keys).getSp()+"人 治愈"+proHash.get(keys).getCure()+"人 死亡"+proHash.get(keys).getDead()+"人 \n";
						fileWriter = new FileWriter(file.getAbsoluteFile(),true);
						bw = new BufferedWriter(fileWriter);
						bw.write(content);
						bw.close();
					}
					else if(!proHash.containsKey(province[j])&&province[j]==sortedPro[i]){
						String keys=sortedPro[i];
						content=proHash.get(keys).getName()+" 感染患者0人 疑似患者0人 治愈0人 死亡0人\n";
					}
				}
			}
			
		}
		
		

    }

    //统计全国数据
    public void Sum(Hashtable<String,Province> hashtable) {
    	
    	Province nation = new Province("全国");
    	Set set = hashtable.keySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Object keys = iterator.next();
            nation.ip += hashtable.get(keys).getIp();
            nation.sp += hashtable.get(keys).getSp();
            nation.cure += hashtable.get(keys).getCure();
            nation.dead += hashtable.get(keys).getDead();
            //System.out.println(hashtable.get(keys).getName()+hashtable.get(keys).getIp()+" "+hashtable.get(keys).getSp()+" "+hashtable.get(keys).getCure()+hashtable.get(keys).getDead());
        }
    	hashtable.put("全国",nation);
    	
    }
    
    //获取xx人中的数字
    public String getNumber(String s) {
    	return  s.substring(0,s.length()-1);
    }

    //处理日期对象，比较日期大小，判断哪些文件需要处理
    private void dealDate(String filepath,int deep,String date,Hashtable<String,Province> hashtable,boolean flag) throws IOException{   
    	// 获得指定文件对象  
        File file = new File(filepath);   
        // 获得该文件夹内的所有文件   
        File[] childFile = file.listFiles();  

        for(int i=0;i<childFile.length;i++)
        {   
        	
            if(childFile[i].isFile())//如果是文件
            {   
            	if(date.compareTo(childFile[childFile.length-1].getName().substring(0,10))>0&&flag==false) {
            		System.out.println("超出日期范围！！！");
            		break;
            	}
            	
                //比较日期大小
                String fileName=childFile[i].getName().substring(0,10);
                if(date.compareTo(fileName)>=0) {
                	//System.out.println(childFile[i].getPath());
                	dealStatistic(childFile[i].getPath(),hashtable);     
                }
                
            }
        }   
    }   

    //解析命令行
    public void dealCmd(String[] test,Hashtable<String,Province> proHash) throws IOException {
    	Hashtable<String,Integer> cmdHash=new Hashtable<String,Integer>();
    	String[] type=new String[4];
    	String[] province=new String[32];
        for(int i=0;i<test.length;i++) {
        	cmdHash.put(test[i], i);
        }
    	//将文件名的日期分割后存放于此
    	String[] splitDate=new String[3];
    	boolean flagDate=false;
    	
    	//处理-date
        if(cmdHash.containsKey("-date")) {//如果给出-date
        	splitDate=test[cmdHash.get("-date")].split("-");
        	dealDate(test[cmdHash.get("-log")+1],0,test[cmdHash.get("-date")+1],proHash,flagDate); 
        }
        else {
        	//如果没有给出具体日期,获取当前系统日期传给date
        	flagDate=true;
        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate =   dateFormat.format(new Date());
            dealDate(test[cmdHash.get("-log")+1],0,currentDate,proHash,flagDate); 
        }
        Sum(proHash);
        System.out.println("ip:"+proHash.get("全国").ip+"   sp:"+proHash.get("全国").sp+"  cure:"+proHash.get("全国").cure+"  dead:"+proHash.get("全国").dead);
        
        //处理-province
        if(cmdHash.containsKey("-province")) {
        	int num=cmdHash.get("-province")+1;
        	for(int i=num,j=0;i<test.length;i++,j++) {
        		if(test[num].substring(0,1).equals("-")) {
        			break;
        		}
        		province[j]=test[num];
        		
        	}
        	
        }
        else
        	province=null;
        
        //处理-type
        if(cmdHash.containsKey("-type")) {
        	if(cmdHash.containsKey("ip")){
            	type[0]="\" 感染患者\"+proHash.get(keys).getIp()";
            }
            else if(cmdHash.containsKey("sp")) {
            	type[1]="\"人 疑似患者\"+proHash.get(keys).getSp()";
            }
            else if(cmdHash.containsKey("cure")) {
            	type[2]="\"人 治愈\"+proHash.get(keys).getCure()";
            }
            else if(cmdHash.containsKey("dead")) {
            	type[3]="\"人 死亡\"+proHash.get(keys).getDead()";
            }   
        }
        else
        	type=null;
        
        writeFile("F:/output.txt",proHash,type,province);
    }
    
    public static void main(String[] args) throws IOException {
        
        InfectStatistic in = new InfectStatistic();
        
        String[] test= {"list","-date","2020-01-22","-log","F:\\InfectStatistic-main\\example\\log\\","-out","D:/output.txt","-type","ip","-province","福建","湖北"};
        
        
        //args[1] -datae    args[2] xxxx-xx-xx  args[3] -log args[4] xxx args[5] -out  args[6] xxx
        Hashtable<String,Province> proHash = new Hashtable<String, Province>();//以省份汉字作为Key 以省类作为value
        in.dealCmd(test, proHash);
        
        
        
        //这是需要获取的文件夹路径  
      //  String path ="F:\\InfectStatistic-main\\example\\log";   
          
        
        
        
        //in.dealStatistic("F:\\InfectStatistic-main\\example\\log\\2020-01-23.log.txt",proHash);
        
        
       /* for(int i=0;i<test.length;i++) {
        	System.out.println(test[i]);
        }*/
        
      
        
      
    }
    
}
