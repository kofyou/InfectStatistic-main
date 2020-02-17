import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Set;



/**
 * InfectStatistic
 * TODO
 *
 * @author 221701305_林琳
 * @version xxx
 * @since xxx
 */

class Province{
	String name;//省份名称(拼音）
    int ip;//感染患者
    int sp;//疑似患者
    int cure;//治愈
    int dead;//死亡
    
    public Province(String name) {
    	this.name=name;
    	this.ip=10;
    	this.sp=0;
    	this.cure=0;
    	this.dead=0;
    }
    
    public void deleteNum(String s) {
    	ip-=Integer.valueOf(s);
    }
    
    public void addNum(String s) {
    	ip+=Integer.valueOf(s);
    }
}

class InfectStatistic {

    //读文件
    public void readFile(String s) throws IOException{
    	
    	String[][] convert = {{"河北","HeBei"},{"山西","ShanXi"},{"辽宁","LiaoNing"},{"吉林","JiLin"},{"黑龙江",
    		"HeiLongJiang"},{"江苏","JiangSu"},{"浙江","ZheJiang"},{"安徽","AnHui"},{"福建","FuJian"},{"江西","JiangXi"},
    			{"山东","ShanDong"},{"河南","HeNan"},{"湖北","HuBei"},{"湖南","HuNan"},{"广东","Guangdong"},
    			{"海南","HaiNan"},{"四川","SiChuang"},{"贵州","GuiZhou"},{"云南","YunNan"},{"陕西","ShanXi"},
    			{"甘肃","GanSu"},{"青海","QingHai"}
    		};
    	
    			
    	
    	int position=0;
        String[] bufString=new String[1024];
        //打开读取的文件
        //BufferedReader br = new BufferedReader(new FileReader("F:\\test.txt"));
        BufferedReader br = new BufferedReader(new FileReader(s));
        String line=null;
        
        String[] splitString =new String[5];//字符串分割后存放于此
        Hashtable<String,Province> proHash = new Hashtable();//以省份汉字作为Key 以拼音作为value
        while((line=br.readLine())!=null) {//按行读取
        	bufString[position]=line;
        	
        	splitString=bufString[position].split(" ");
        	
        	position++;
        	
        	if(splitString.length==5) {//这种情况只有流入
        		if(proHash.containsKey(splitString[3])==false) {//如果province中没有这个省份则新建
        			String proName; 
        			for(int i=0;i<22;i++) {
        				if(convert[i][0].equals(splitString[3])) {
        					proName=convert[i][1];	
        				    proHash.put(convert[i][0], new Province(proName));
        					System.out.println("*****新建省"+proName);
        					break;
        				}
        			}	
        		}
        	}
        	
        	//ceshi
        	
        	
        	else if(splitString.length==3) {//治愈或死亡
        		proHash.put("湖北", new Province("HuBei"));
        		
        		
        		if(splitString[1].equals("死亡")) {
        			
        			proHash.get(splitString[0]).deleteNum(getNumber(splitString[2]));
        			System.out.println(splitString[0]+"dead"+getNumber(splitString[2]));
        		}
        		else if(splitString[1].equals("治愈")) {
        			proHash.get(splitString[0]).addNum(getNumber(splitString[2]));
        			System.out.println(splitString[0]+"zhiyu"+getNumber(splitString[2]));
        		}
        	}
        }
        br.close();//关闭文件
        for(int i=0;i<position;i++) {
        	System.out.println(bufString[i]);
        }
        
        
    }

    //写文件
    public void writeFile() throws IOException {
    	int number=1;
		OutputStream os = new FileOutputStream("F:\\output.txt");
		PrintWriter pw=new PrintWriter(os);
		for(int i=0;i<10;i++) {
			String s=""+number;
			pw.println(s);//每输入一个数据，自动换行，便于我们每一行每一行地进行读取
			//pw.print(s+",");//不会自动换行，必要时可以自己添加分隔符
			number++;
		}
		pw.close();
		os.close();

    }


    
    public String getNumber(String s) {//获取xx人中的数字
    	return  s.substring(0,s.length()-1);
    }

	
    public static void main(String[] args) throws IOException {
        
        InfectStatistic in = new InfectStatistic();
        
        //in.writeFile();
        in.readFile("F:\\InfectStatistic-main\\example\\log\\2020-01-22.log.txt");
        
        
      
    }
    
}
