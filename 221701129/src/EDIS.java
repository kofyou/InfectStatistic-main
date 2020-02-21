import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.awt.List;
import java.io.*;
import java.io.File.*;
import java.util.*;
import java.util.regex.*;
public class EDIS {
	public static void main(String[] args){
		String ADN[] = 
		{
			    "全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北",
			    "河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏",
			    "青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"
		};
		int SIZE=0,esp=0,esp1=0;
		int Psize=0,Tsize=0;
		String[] list=new String[4];//记录type的类型
		int ADNSize[]=new int[32];
		String AddressOut=null;
		String AddressIn=null;
		ArrayList<String> filesNames = new ArrayList<String>();
		String Date=null;
		String condition1[]=null;
		int condition2[][]=null;
		for(int i=0;i<args.length;i++) 
		{
			switch(args[i])
			{
	        	case "-date":
		        	SIZE=1;
		        	
		        	//System.out.println(args[i]);
		            continue;
	        	case "-log":
		        	SIZE=2;		      
		        	//System.out.println(args[i]);
		        	continue;
	            case "-out":
		            SIZE=3;		   
		            //System.out.println(args[i]);
		            continue;
	            case "-type":
		            SIZE=4;
		            esp=1;
		            //System.out.println(i);
		            continue;
	            case "-province":
	            	SIZE=5;
	            	esp1=1;
	            	//System.out.println(args[i]);
	            	continue;
	        }      
			if(SIZE==1) 
			{
				Date=args[i];	
				//System.out.println(args[i]);
			}
			else if(SIZE==2)
			{
				int Flag1=i;
        		AddressIn=args[Flag1];
        		//System.out.println(args[i]);
			}
			else if(SIZE==3)
			{
				int Flag2=i;
	        	AddressOut=args[Flag2];
	        	//System.out.println(args[i]);
			}
			else if(SIZE==4)
			{
				for(;i<args.length;i++) 
				{
					if(!args[i].equals("-date")||!args[i].equals("-out")||!args[i].equals("-log")||!args[i].equals("-province"))
					{
						if(args[i].equals("ip"))
							list[0]="感染患者";
						else if(args[i].equals("sp"))
							list[1]="疑似患者";
						else if(args[i].equals("cure"))
							list[2]="治愈";
						else if(args[i].equals("dead"))
							list[3]="死亡";
						Tsize++;
					}
					else 
					{
						/*if(Tsize==0)
						{
							list[0]="感染患者";
							list[1]="疑似患者";
							list[2]="治愈";
							list[3]="死亡";
							Tsize=4;
						}*/
						i--;
						break;
					}	
				}
			}
			else if(SIZE==5) 
			{		
				for(int j=0;j<32;j++)
				{
					if(args[i].equals(ADN[j]))
					{
						ADNSize[Psize]=j;
						Psize++;
					}
				}
				Arrays.sort(ADNSize);
			}
			
		}
		
		if(AddressOut!=null&&AddressIn!=null)
		{
			
        		filesNames=getFilesName(AddressIn,Date);
        		//AddressIn=AddressIn+"\\"+Date+".log.txt";//
			if(esp==0||Tsize==0)
			{
				list[0]="感染患者";
				list[1]="疑似患者";
				list[2]="治愈";
				list[3]="死亡";
				Tsize=4;
			}
			if(esp1!=0)
			{	
				condition1=new String[Psize];
				for(int j=0;j<Psize;j++)
				{
					condition1[Psize-j-1]=ADN[ADNSize[31-j]];
				}
			}
			else {
				int LS[]=new int[32];
				for(int i=1;i<32;i++)
				{
					for(int j=0;j<filesNames.size();j++)
						if(Pnum(ADN[i],filesNames.get(j))==1)//
						{
							LS[Psize]=i;
							Psize++;
							break;
						}	
				}
				Psize++;
				Arrays.sort(LS);
				condition1=new String[Psize];
				for(int j=0;j<Psize;j++)
				{
					condition1[Psize-j-1]=ADN[LS[31-j]];
				}
				condition1[0]="全国";
			}
			condition2=new int[Psize][4];
			for(int i=0;i<filesNames.size();i++)
			{
				for(int j=0;j<Psize;j++)
				{
					Read(filesNames.get(i),AddressOut,condition1[j],condition2[j]);
				}
			}
			
			for(int j=0;j<Psize;j++)
			{
				FileIn(AddressOut,condition1[j]);
				for(int i=0;i<4;i++)
				{
					if(list[i]!=null)
					{
						String content=list[i]+condition2[j][i];
						FileIn(AddressOut,content);
					}
					
				}
				FileIn(AddressOut,"\n");
			}
			FileIn(AddressOut,"//该文档并非真实数据，仅供测试使用");
		}
		else 
		{
			System.out.println("没有输入地址");
		}
	}
	
	public static ArrayList<String> getFilesName(String filepath,String DATE) {
		ArrayList<String> files = new ArrayList<String>();
		String LS=null;
		File file = new File(filepath);
		File[] tempLists = file.listFiles();		
		if(DATE!=null) 
		{
			LS=filepath+"\\"+DATE+".log.txt";
		}
		else 
		{
			LS=tempLists[tempLists.length-1].toString();
			
		}
		for (int i = 0; i < tempLists.length; i ++) 
		{
			if (tempLists[i].isFile()&&tempLists[i].toString().compareTo(LS)<=0) 
			{
				files.add(tempLists[i].toString());
			}
		}
		return files;
	}
	public static String Read(String address1,String address2,String condition1,int condition2[]){
        File file = new File(address1);
        StringBuilder result = new StringBuilder();
        //int size=0;
        String ZS="//.*";
        String pattern[]=new String[10];
        if(condition1.equals("全国"))
        {
        	//String pattern[]=new String[6];
        	pattern[0] = ".*新增.*感染患者.*";
        	pattern[1] = ".*新增.*疑似患者.*";   
        	pattern[2] = ".*死亡.*";
        	pattern[3] = ".*治愈.*";
        	pattern[4] = ".*疑似患者.*确诊感染.*";
        	pattern[5] = ".*排除.*疑似患者.*";
        }
        else 
        {
        	//String pattern[]=new String[10];
        	pattern[0] = condition1+".*新增.*感染患者.*";
        	pattern[1] = condition1+".*新增.*疑似患者.*";
        	pattern[2] = condition1+".*感染患者.*流入.*";
        	pattern[3] = condition1+".*疑似患者.*流入.*";
        	pattern[4] = condition1+".*死亡.*";
        	pattern[5] = condition1+".*治愈.*";
        	pattern[6] = condition1+".*疑似患者.*确诊感染.*";
        	pattern[7] = condition1+".*排除.*疑似患者.*";
        	pattern[8] = ".*感染患者.*流入.*"+condition1+".*";
        	pattern[9] = ".*疑似患者.*流入.*"+condition1+".*";
		}
        try
        {        
        	FileInputStream fr=new FileInputStream(file);
        	InputStreamReader fsr=new InputStreamReader(fr,"UTF-8");
            BufferedReader br = new BufferedReader(fsr);//构造一个BufferedReader类来读取文件 
            String s = null;  
            int j=condition2.length;
            int m=0;
            while((s = br.readLine())!=null)
            {
            	if(Pattern.matches(ZS,s))
            		continue;
            	if(condition1.equals("全国"))
            	{
                	for(int k=0;k<6;k++)
                	{
                		if(Pattern.matches(pattern[k],s))
                		{
                			String num="[^0-9]";  
                		    Pattern p = Pattern.compile(num);  
                		    Matcher numString = p.matcher(s);  
                		    int NUM=Integer.parseInt(numString.replaceAll("").trim());//提取整数
                			if(k==0)
                				condition2[0]+=NUM;
                			else if(k==1)
                				condition2[1]+=NUM;
                			else if(k==2)
                			{
                				condition2[3]+=NUM;
                				condition2[0]-=NUM;
                			}		
                			else if(k==3)
                			{
                				condition2[0]-=NUM;
                				condition2[2]+=NUM;
                			}
                			else if(k==4)
                			{
                				condition2[1]-=NUM;
                				condition2[0]+=NUM;
                			}
                			else if(k==5)
                			{
                				condition2[1]-=NUM;
                			}   
                			break;
                		}
                	}
            	}
            	else 
            	{
            		
            	
            	
            	for(int k=0;k<10;k++)
            	{
            		//System.out.println(pattern[k]);
            		if(Pattern.matches(pattern[k],s))
            		{
            			String num="[^0-9]";  
            		    Pattern p = Pattern.compile(num);  
            		    Matcher numString = p.matcher(s);  
            		    int NUM=Integer.parseInt(numString.replaceAll("").trim());//提取整数
            		    //System.out.println(NUM);
            			if(k==0||k==8)
            				condition2[0]+=NUM;
            			else if(k==1||k==9)
            				condition2[1]+=NUM;
            			else if(k==2)
            				condition2[0]-=NUM;
            			else if(k==3)
            				condition2[1]-=NUM;
            			else if(k==4)
            			{
            				condition2[3]+=NUM;
            				condition2[0]-=NUM;
            			}
            			else if(k==5)
            			{
            				condition2[0]-=NUM;
            				condition2[2]+=NUM;
            			}           				
            			else if(k==6)
            			{
            				condition2[0]+=NUM;
            				condition2[1]-=NUM;
            			}
            			else
            				condition2[1]-=NUM;
            			break;
            		}
            	}
            	}
            }
            br.close();
            fr.close();
            fsr.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result.toString();
	}
	public static void FileIn(String address,String content)
	{
		File f = new File(address);
        FileWriter fw;
        try
        {
        	fw=new FileWriter(f,true);
        	if(content!="\n")
        		fw.write(content+" ");//将字符串写入到指定的路径下的文件中  
        	else 
        		fw.write(content);
            fw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
	}
	public static int Pnum(String Province,String address1)
	{
		File file = new File(address1);
        StringBuilder result = new StringBuilder();
        int m=0;
        try
        {        
        	FileInputStream fr=new FileInputStream(file);
        	InputStreamReader fsr=new InputStreamReader(fr,"UTF-8");
            BufferedReader br = new BufferedReader(fsr);//构造一个BufferedReader类来读取文件 
            String s = null;  
            while((s = br.readLine())!=null)
            {
            	
            	String pattern=".*"+Province+".*";
            	if(Pattern.matches(pattern,s))
            	{
            		m=1;
            		return m;
            	}
            	
            }
            br.close();
            fsr.close();
            fr.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
		return m;
	}
}
