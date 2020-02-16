/*
* FileName:InfectStatistic.java
* Author:zyl
* Version:1.0
* Date:2020-2-16
*/

import java.io.File;

class InfectStatistic 
{
    public static void main(String[] args) 
    {
    	String pathOfLog="";  //用于记录日志目录的位置
    	String pathOfOutput="";  //用于记录输出文件路径和文件名
    	String dateString="";  //用于记录日期
    	String[] paramentersOfType = new String[4];  //用于记录参数type的参数值
    	String[] paramentersOfProvince = new String[34];  //用于记录参数province的参数值
    	
    	//记录list命令各参数是否出现，分别对应参数log、out、date、type、province,出现后相应元素值置为1
        int commandArgsFlag[] = {0,0,0,0,0};
        
        for (int i = 0;i < args.length;i++)
        {
        	if (args[i].equals("-log"))
        	{
        		commandArgsFlag[0]++;
        		pathOfLog = args[i + 1];
        	}
        	else if (args[i].equals("-out"))
        	{
        		commandArgsFlag[1]++;
        		pathOfOutput = args[i + 1];
        	}
        	else if (args[i].equals("-date"))
            {
                commandArgsFlag[2]++;
                dateString = args[i + 1];
            }
        	else if (args[i].equals("-type"))
            {
                commandArgsFlag[3]++;
                
                int cnt = 0;
                for (int j = i + 1;j < args.length && args[j].startsWith("-") == false;j++)
                {
                	paramentersOfType[cnt++] = args[j];
                }
                
            }
        	else if (args[i].equals("-province"))
            {
                commandArgsFlag[4]++;

                int count = 0;
                for (int j = i + 1;j < args.length && args[j].startsWith("-") == false;j++)
                {
                	paramentersOfProvince[count++] = args[j];
                }
            }
        }
    }
}