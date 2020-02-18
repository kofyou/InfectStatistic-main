import java.io.*;
import java.lang.String;


public class InfectStatistic {
	
	static Lib libs[];//用于存储某天的日志的数组
	static int count=0;//用于纪律当前已经存了多少天的日志的计数单位
	static int isIn[]=new int[35];//用于记录用户要求查询哪个省的数组
	
	public static void list(String log,String out,String date,
			String type,String province)
	{
		//读取文件
		GetFileList(log);
		//输出信息
		outListToFile(out,type,date,province);
	}
	//读文件的函数
	public static void GetFileList(String log)
	{
		File file = new File(log);
		File[] fileList = file.listFiles();
		//根据txt文件的数量创建对应容量的数组
		libs=new Lib[fileList.length];
		//开始读文件
		for (int i = 0; i < fileList.length; i++)
		{
			if (fileList[i].isFile())
			{
				//是文件就按行读取
				String fileName = fileList[i].getName();
				//根据文件名创建对应的lib元素，截取日期当作元素的date
				String substr=fileList[i].getName().substring(0,10);
				libs[count]=new Lib(substr);
				count=count+1;
				addYesterday();
				//读文件
				readFileByLines(log+"\\"+fileName,libs[count-1]);         
			} 
			if (fileList[i].isDirectory())
			{
				//是目录就递归读取
				String fileName = fileList[i].getName();
				GetFileList(log+"\\"+fileName);
            }
	    }
	}
	//按行读取文件
	public static void readFileByLines(String fileName,Lib lib)
	{  
		
		BufferedReader reader=null;
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
			reader = new BufferedReader(read);
			String line=null;
						
			while((line =reader.readLine()) != null){
				OpData(line,lib);						
			}			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}finally {
			if (reader !=null) {
				try {
					reader.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
		}
        
    }  
	//处理数据的函数
	public static void OpData(String data,Lib lib)
	{
		String[] strarray=data.split(" |人");
		if(strarray.length==5)
		{
			//人员流动的情况
			Flow(lib,strarray);
		}
		else if(strarray.length==4)
		{
			//确诊或者增长或者排除的情况
			SureOrIncreaseOrMove(lib,strarray);
		}
		else if(strarray.length==3)
		{
			//治愈或者死亡的情况
			CureOrDead(lib,strarray);
		}
	}
	
	//在全国的省份中找到对应省份的函数,lib是日期，y是省
	public static int Find(Lib lib,String[] strarray,int y)
	{
		for (int i=0;i<lib.provinces.size();i++)
		{
			Province temp=lib.provinces.get(i);
			if(temp.getName().contentEquals(strarray[y])==true)
			{
				//如果找到就返回下标,并标识出它是当天改变的省份
				temp.setIsinlog();
				return i;
			}
		}
		//找不到返回-1
		return -1;
	}
	//处理人口流动情况的函数
	public static void Flow(Lib lib,String[] strarray)
	{
		Province temp1,temp2;
		int i=Find(lib,strarray,0),j=Find(lib,strarray,3);
		if(i!=-1&&j!=-1)
		{
			temp1=lib.provinces.get(i);
			temp2=lib.provinces.get(j);
			if(strarray[1].contentEquals("感染患者")==true)
			{
				temp1.moveIp(Integer.parseInt(strarray[4]));
				temp2.addIp(Integer.parseInt(strarray[4]));
			}
			else
			{
				temp1.moveSp(Integer.parseInt(strarray[4]));
				temp2.addSp(Integer.parseInt(strarray[4]));
			}
			lib.provinces.remove(i);
			lib.provinces.insertElementAt(temp1,i);
			lib.provinces.remove(j);
			lib.provinces.insertElementAt(temp2,j);
		}
		else
		{	
			System.out.println("流入发生错误");
		}
	}
	//处理死亡或者治愈情况的函数
	public static void CureOrDead(Lib lib,String[] strarray)
	{
		int i=Find(lib,strarray,0);
		if(i!=-1)
		{
			Province temp1=lib.provinces.get(i);
			Province temp2=lib.provinces.get(0);
			if(strarray[1].contentEquals("死亡")==true)
			{
				temp1.moveIp(Integer.parseInt(strarray[2]));
				temp1.addDead(Integer.parseInt(strarray[2]));
				temp2.moveIp(Integer.parseInt(strarray[2]));
				temp2.addDead(Integer.parseInt(strarray[2]));
			}
			else
			{
				temp1.moveIp(Integer.parseInt(strarray[2]));
				temp1.addCure(Integer.parseInt(strarray[2]));
				temp2.moveIp(Integer.parseInt(strarray[2]));
				temp2.addCure(Integer.parseInt(strarray[2]));
			}
			lib.provinces.remove(i);
			lib.provinces.insertElementAt(temp1,i);
			lib.provinces.remove(0);
			lib.provinces.insertElementAt(temp2,0);
			
		}
		else
		{	
			System.out.println("治愈死亡发生错误");
		}
	}
	//处理确诊排除或者增长情况的函数
	public static void SureOrIncreaseOrMove(Lib lib,String[] strarray)
	{
		int i=Find(lib,strarray,0);
		if(i!=-1)
		{
			Province temp1=lib.provinces.get(i);
			Province temp2=lib.provinces.get(0);
			if(strarray[1].contentEquals("新增")==true)
			{
				if(strarray[2].contentEquals("感染患者")==true)
				{
					temp1.addIp(Integer.parseInt(strarray[3]));
					temp2.addIp(Integer.parseInt(strarray[3]));
				}
				else
				{
					temp1.addSp(Integer.parseInt(strarray[3]));
					temp2.addSp(Integer.parseInt(strarray[3]));
				}
			}
			else if(strarray[1].contentEquals("排除")==true)
			{
				temp1.moveSp(Integer.parseInt(strarray[3]));
				temp2.moveSp(Integer.parseInt(strarray[3]));
			}
			else
			{
				temp1.moveSp(Integer.parseInt(strarray[3]));
				temp1.addIp(Integer.parseInt(strarray[3]));
				temp2.moveSp(Integer.parseInt(strarray[3]));
				temp2.addIp(Integer.parseInt(strarray[3]));
			}
			lib.provinces.remove(i);
			lib.provinces.insertElementAt(temp1,i);
			lib.provinces.remove(0);
			lib.provinces.insertElementAt(temp2,0);
			
		}
		else
		{	
			System.out.println("确诊排除增加发生错误");
		}
	}
	//把昨天的数据加到今天的函数
	public static void addYesterday()
	{
		Province temp1,temp2;
		if(count>1)
		{
			for(int i=0;i<libs[count-2].provinces.size();i++)
			{
				temp1=libs[count-1].provinces.get(i);
				temp2=libs[count-2].provinces.get(i);
				temp1.addIp(temp2.getIp());
				temp1.addSp(temp2.getSp());
				temp1.addCure(temp2.getCure());
				temp1.addDead(temp2.getDead());
			}
		}
	}
	//从输入的province参数找到对应的省份
	public static void whatIn(int x,String province)
	{
		String[] strarray=province.split(" ");
		int i=0,find=0;
		do {
			if(libs[x].provinces.get(i).getName().contentEquals(strarray[find])==true)
			{
				isIn[i]=1;
				find=find+1;
			}
			i=i+1;
			if(i==libs[x].provinces.size())
			{
				i=0;
			}
		}while(find<strarray.length);
		
	}
	//根据输入的date参数找到对应的日期
	public static int findDate(String date)
	{
		if(date.contentEquals(" ")==true)
			return libs.length-1;
		else if(date.compareTo(libs[libs.length-1].getDate())>0)
		{
			return -1;
		}
		else if(date.compareTo(libs[0].getDate())<0)
		{
			return -1;
		}
		else
		{
			for(int i=0;i<libs.length;i++)
			{
				if(date.contentEquals(libs[i].getDate())==true)
					return i;
				else if(i+1<libs.length)
				{
					if(date.compareTo(libs[i].getDate())>0&&
							date.compareTo(libs[i+1].getDate())<0)
					{
						return i;
					}
				}
			}
		}
		return -1;
	}
	//把数据输出到控制台的函数，没什么用，要删除的
	public static void outList(String type,String date,String province)
	{
		int x=findDate(date);
		if(province.contentEquals(" ")==true)
		{
			libs[x].provinces.get(0).setIsinlog();
			for (int k=0;k<libs[x].provinces.size();k++)
			{
				Province temp=libs[x].provinces.get(k);
				if(temp.getIsinlog()==1)
				{
					if(type.contentEquals(" ")==true)
					{
						System.out.print(temp.ToString());
					}
					else
					{
						System.out.print(outType(type,temp));
					}
				}
			}
		}
		else
		{
			whatIn(x,province);
			for (int k=0;k<libs[x].provinces.size();k++)
			{
				Province temp=libs[x].provinces.get(k);
				if(isIn[k]==1)
				{
					if(type.contentEquals(" ")==true)
					{
						System.out.print(temp.ToString());
					}
					else
					{
						System.out.print(outType(type,temp));
					}
				}
			}
		}
		
	}
	
	public static String outType(String type,Province pro)
	{
		String[] strarray=type.split(" ");
		String raw=pro.getName();
		for(int i=0;i<strarray.length;i++)
		{
			raw=raw+" ";
			if(strarray[i].contentEquals("ip")==true)
			{
				raw=raw+pro.outIp();
			}
			else if(strarray[i].contentEquals("sp")==true)
			{
				raw=raw+pro.outSp();
			}
			else if(strarray[i].contentEquals("cure")==true)
			{
				raw=raw+pro.outCure();
			}
			else if(strarray[i].contentEquals("dead")==true)
			{
				raw=raw+pro.outDead();
			}
		}
		raw=raw+"\n";
		return raw;
	}
	
	public static void outListToFile(String out,String type,
			String date,String province)
	{
		 try {
			    File writename1 = new File(out);
	            //判断是否存在
	            if(!writename1.exists()) 
	            {
	                //不存在就创建
	                writename1.createNewFile();
	            }
	            //创建写入文件方式，true为追加写入，原内容不覆盖
	            //FileWriter fw = new FileWriter(writename1,true);
	            
	            FileWriter fileWriter = new FileWriter(writename1.getAbsoluteFile());

				BufferedWriter bw = new BufferedWriter(fileWriter);

				
	            
	            int x=findDate(date);
	            if(x==-1)
	            {
	            	//fw.append("日期超出范围\n");
	            	//fw.close();
	            	bw.write("日期超出范围\n");
	            	bw.close();
	            	return;
	            }
	    		if(province.contentEquals(" ")==true)
	    		{
	    			libs[x].provinces.get(0).setIsinlog();
	    			for (int k=0;k<libs[x].provinces.size();k++)
	    			{
	    				Province temp=libs[x].provinces.get(k);
	    				if(temp.getIsinlog()==1)
	    				{
	    					if(type.contentEquals(" ")==true)
	    					{
	    						//追加写入
	    	    				//fw.append(temp.ToString());
	    						bw.write(temp.ToString());
	    					}
	    					else
	    					{
	    						//追加写入
	    	    				//fw.append(outType(type,temp));
	    						bw.write(outType(type,temp));
	    					}
	    				}
	    				//刷新
	    	            //fw.flush();
	    			}
	    		}
	    		else
	    		{
	    			whatIn(x,province);
	    			for (int k=0;k<libs[x].provinces.size();k++)
	    			{
	    				Province temp=libs[x].provinces.get(k);
	    				if(isIn[k]==1)
	    				{
	    					if(type.contentEquals(" ")==true)
	    					{
	    						//追加写入
	    	    				//fw.append(temp.ToString());
	    						bw.write(temp.ToString());
	    					}
	    					else
	    					{
	    						//追加写入
	    	    				//fw.append(outType(type,temp));
	    						bw.write(outType(type,temp));
	    					}
	    				}
	    				//刷新
	    	            //fw.flush();
	    			}
	    		}
	            
	            //关闭资源
	            //fw.close();
	    		bw.close();
	        }
		 catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	public static String getParaments(String parament,String[] args)
	{
		int i=1;
		String result="";
		for(;i<args.length;i++)
		{
			if(args[i].contentEquals(parament)==true)
			{
				for(int j=i+1;j<args.length;j++)
				{
					if(result.contentEquals("")==true)
						result=args[j];
					else result=result+" "+args[j];
					if(j+1>=args.length)
					{
						break;
					}
					else if(args[j+1].contentEquals("-log")||
							args[j+1].contentEquals("-out")||
							args[j+1].contentEquals("-date")||
							args[j+1].contentEquals("-type")||
							args[j+1].contentEquals("-province"))
					{
						break;
					}
				}
			}
		}
		if(result.contentEquals("")==true)
		{
			return " ";
		}
		return result;
	}
	
	public static void main(String[] args){
		String log=getParaments("-log",args);
		log=log.substring(0,getParaments("-log",args).length()-1);
		String out=getParaments("-out",args);
		String date=getParaments("-date",args);
		String type=getParaments("-type",args);
		String province=getParaments("-province",args);
		if(log.contentEquals(" ")==false&&out.contentEquals(" ")==false)
		{
			list(log,out,date,type,province);
		}
		else System.out.println("指令错误");
	}

}
