import java.io.*;
import java.util.*;
import java.lang.String;


public class InfectStatistic {
	
	static Lib libs[];
	static int count=0;
	static int isIn[]=new int[35];
	
	public static void list(String log,String out,String date,
			String type,String province)
	{
		//读取文件
		GetFileList(log);
		outList(type,date,province);
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
				//根据文件名创建对应的lib元素
				String substr=fileList[i].getName().substring(0,10);
				libs[count]=new Lib(substr);
				count=count+1;
				addYesterday();
				//读文件
				readFileByLines(log+"\\"+fileName,i);         
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
	public static void readFileByLines(String fileName,int order) 
	{  
        File file = new File(fileName);  
        BufferedReader reader = null;  
        try {    
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;  
            //读行  
            while ((tempString = reader.readLine()) != null)
            {  
                //System.out.println(tempString);
            	//处理文件中的数据
                OpData(tempString,order);
            }
            reader.close();  
        } 
        catch (IOException e)
        {  
            e.printStackTrace();  
        }
        finally
        {  
            if (reader != null)
            {  
                try {  
                    reader.close();  
                } 
                catch (IOException e1) {}  
            }  
        }  
    }  
	//处理数据的函数
	public static void OpData(String data,int x)
	{
		String[] strarray=data.split(" |人");
		if(strarray.length==5)
		{
			//人员流动的情况
			Flow(x,strarray);
		}
		else if(strarray.length==4)
		{
			//确诊或者增长或者排除的情况
			SureOrIncreaseOrMove(x,strarray);
		}
		else if(strarray.length==3)
		{
			//治愈或者死亡的情况
			CureOrDead(x,strarray);
		}
	}
	
	//在全国的省份中找到对应省份的函数
	public static int Find(int x,String[] strarray,int y)
	{
		int i=0;
		for (;i<libs[x].provinces.size();i++)
		{
			Province temp=libs[x].provinces.get(i);
			if(temp.getName().contentEquals(strarray[y])==true)
			{
				//如果找到就返回下标
				return i;
			}
		}
		//找不到返回-1
		return -1;
	}
	//处理人口流动情况的函数
	public static void Flow(int x,String[] strarray)
	{
		Province temp1,temp2;
		int i=Find(x,strarray,0),j=Find(x,strarray,3);
		if(i==-1||j==-1)
		{
			System.out.println("发生错误");
		}
		else
		{	
			temp1=libs[x].provinces.get(i);
			temp2=libs[x].provinces.get(j);
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
			libs[x].provinces.remove(i);
			libs[x].provinces.insertElementAt(temp1,i);
			libs[x].provinces.remove(j);
			libs[x].provinces.insertElementAt(temp2,j);
		}
	}
	//处理死亡或者治愈情况的函数
	public static void CureOrDead(int x,String[] strarray)
	{
		int i=Find(x,strarray,0);
		if(i==-1)
		{
			System.out.println("发生错误");
		}
		else
		{	
			Province temp1=libs[x].provinces.get(i);
			Province temp2=libs[x].provinces.get(0);
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
			libs[x].provinces.remove(i);
			libs[x].provinces.insertElementAt(temp1,i);
			libs[x].provinces.remove(0);
			libs[x].provinces.insertElementAt(temp2,0);
		}
	}
	//处理确诊排除或者增长情况的函数
	public static void SureOrIncreaseOrMove(int x,String[] strarray)
	{
		int i=Find(x,strarray,0);
		if(i==-1)
		{
			System.out.println("发生错误");
		}
		else
		{	
			Province temp1=libs[x].provinces.get(i);
			Province temp2=libs[x].provinces.get(0);
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
			libs[x].provinces.remove(i);
			libs[x].provinces.insertElementAt(temp1,i);
			libs[x].provinces.remove(0);
			libs[x].provinces.insertElementAt(temp2,0);
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
	
	public static int findDate(String date)
	{
		for(int i=0;i<libs.length;i++)
		{
			if(date.contentEquals(libs[i].getDate())==true)
				return i;
		}
		return -1;
	}
	
	public static void outList(String type,String date,String province)
	{
		int x=findDate(date);
		whatIn(x,province);
		for (int k=0;k<libs[x].provinces.size();k++)
		{
			if(isIn[k]==1)
			{
				outType(type,libs[x].provinces.get(k));
			}
		}
	}
	
	public static void outType(String type,Province pro)
	{
		String[] strarray=type.split(" ");
		System.out.print(pro.getName()+" ");
		for(int i=0;i<strarray.length;i++)
		{
			if(strarray[i].contentEquals("ip")==true)
			{
				System.out.print(pro.outIp()+" ");
			}
			else if(strarray[i].contentEquals("sp")==true)
			{
				System.out.print(pro.outSp()+" ");
			}
			else if(strarray[i].contentEquals("cure")==true)
			{
				System.out.print(pro.outCure()+" ");
			}
			else if(strarray[i].contentEquals("dead")==true)
			{
				System.out.print(pro.outDead()+" ");
			}
		}
		System.out.print("\n");
	}
	
	public static void main(String[] args) {
//		for(int i=0;i<args.length;i++)
//		{
//			System.out.println(args[i]);
//		}
		
		Scanner s = new Scanner(System.in); 
		
		System.out.print("输入log\n");
		String log=s.nextLine();
		System.out.print("输入out\n");
		String out=s.nextLine();
		System.out.print("输入date\n");
		String date=s.nextLine();
		System.out.print("输入type\n");
		String type=s.nextLine();
		System.out.print("输入province\n");
		String province=s.nextLine();
		System.out.print("\n");
		list(log,out,date,type,province);
		
	}

	
}
