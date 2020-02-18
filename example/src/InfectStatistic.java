import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;


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
		String name;//省份名称(拼音）
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
	    
	    /*public void setIp(String ip) {
	    	
	    	this.ip=Integer.valueOf(ip);
	    }*/
	}

	
	
	//获取省份名称的拼音
	public String getProvinceName(String s) {
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
	}
	
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
         						hashtable.put(splitString[0],new Province(getProvinceName(splitString[0]),getNumber(splitString[3]),"ip"));
         						
         					}
         					else {
         						hashtable.get(splitString[0]).addNum(getNumber(splitString[3]),"ip");
         						
         					}

         				}
         				else if(splitString[2].equals("疑似患者")){//如果是疑似患者
         					
         					if(hashtable.containsKey(splitString[0])==false) {//若哈希表中没有这个省份
              				
         						hashtable.put(splitString[0],new Province(getProvinceName(splitString[0]),getNumber(splitString[3]),"sp"));
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
            				
                			proName=getProvinceName(splitString[3]);
                			//被流入的省份ip直接初始为化流入值
                			hashtable.put(splitString[3],new Province(proName,getNumber(splitString[4]),"ip"));
                			//流出ip的省份减
                			hashtable.get(splitString[0]).deleteNum(getNumber(splitString[4]), "ip");
                			//System.out.println("*****新建省"+proName);
                			
                		}
            			else if(splitString[1].equals("疑似患者")){
                    		proName=getProvinceName(splitString[3]);
                        	//被流入的省份sp直接初始为化流入值
                        	hashtable.put(splitString[3], new Province(proName,getNumber(splitString[4]),"sp"));
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
    public void writeFile() throws IOException {
    	int number=1;
		OutputStream os = new FileOutputStream("F:\\output.txt");
		PrintWriter pw=new PrintWriter(os);
		System.out.println("当日情况：");
		for(int i=0;i<10;i++) {
			//String s=""+number;
			System.out.println("全国 "+"感染患者"+"疑似患者"+"治愈"+"死亡");
			pw.println(s);//每输入一个数据，自动换行，便于我们每一行每一行地进行读取
			//pw.print(s+",");//不会自动换行，必要时可以自己添加分隔符
			number++;
		}
		pw.close();
		os.close();

    }

    //统计全国数据
    public void Sum(Hashtable<String,Province> hashtable) {
    	
    	Province nation = new Province("nation");
    	Set set = hashtable.keySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Object keys = iterator.next();
            nation.ip += hashtable.get(keys).getIp();
            nation.sp += hashtable.get(keys).getSp();
            nation.cure += hashtable.get(keys).getCure();
            nation.dead += hashtable.get(keys).getDead();
           // System.out.println(hashtable.get(keys).getName()+hashtable.get(keys).getIp()+" "+hashtable.get(keys).getSp()+" "+hashtable.get(keys).getCure()+hashtable.get(keys).getDead());
        }
    	hashtable.put("全国",nation);
    	
    }
    
    //获取xx人中的数字
    public String getNumber(String s) {
    	return  s.substring(0,s.length()-1);
    }

	
    public static void main(String[] args) throws IOException {
        
        InfectStatistic in = new InfectStatistic();
        Hashtable<String,Province> proHash = new Hashtable<String, Province>();//以省份汉字作为Key 以省类作为value
        in.dealStatistic("F:\\InfectStatistic-main\\example\\log\\2020-01-23.log.txt",proHash);
        in.Sum(proHash);
        //System.out.println("ip:"+proHash.get("全国").ip+"   sp:"+proHash.get("全国").sp+"  cure:"+proHash.get("全国").cure+"  dead:"+proHash.get("全国").dead);
        
    
        	
        
       // System.out.println(in.getProvinceName("新疆"));
      
    }
    
}
