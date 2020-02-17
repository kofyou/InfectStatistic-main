package InfectStatistic;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.Arrays;

import InfectStatistic.test.line;
/**
 * InfectStatistic
 * TODO
 *
 * @author 代铭杰
 * @version 1.0
 */
class InfectStatistic {
    static int count=0;//已有所有数据的条数
	static int selcount=0;//所筛选指定省份的个数
	static int seltypecount=0;////所筛选指定类型的个数
	static line[] all=new line[34];//初始化结果
	static line[] result=new line[34];//总的排序后结果
    static line[] proresult=new line[34];//筛选省份后的排序后结果
    static String topath=new String();//输出文档路径
    static String frompath=new String();//log文件路径
    static int index=0;//控制是否输入日期比日志最早一天还早，若是则值为-2
    static int isWrong=0;//输入日期是否出错（输入日期比最新的日志还晚）
     	public class Province {

            /** 省份名称 */
            String provinceName; 
            /** 感染患者 */
            int ip; 
            /** 疑似患者 */
            int sp;
            /** 治愈 */
            int cure;
            /** 死亡 */
            int dead;

            Province(String provinceName, int ip, int sp, int cure, int dead) {
                this.provinceName = provinceName;
                this.ip = ip;
                this.sp = sp;
                this.cure = cure;
                this.dead = dead;
            }

        }
	public static void main(String[] args) throws IOException {
    	if(args.length==0) {
    		System.out.println("输入命令行为空，请重新输入！");
    		return;
    	}
    	if(!args[0].equals("list")) {//命令错误
    		System.out.println("未输入命令‘list’，则不可以带参数，请重新输入！");
    		return;
    	}
	}
		static class cmdArgs {//获取使用命令
	    String[] args;
	    
	    	cmdArgs(String[] passargs) {
	    		args=passargs;
	    	}
	    	/*
	    	     功能：判断是否存在某命令
	    	     输入参数：需要查验的命令
	    	     返回值：该命令的索引int值，如果没有该命令则返回-1
	    	 */
	    	int hasParam(String key) {    	
	    		for(int i=0;i<args.length;i++) {
	    			if(args[i].equals(key)) {
	    				return i;
	    			}
	    		}
	    		return -1;
	    	}    
		}
		/*
          	功能：判断日期的合法性
          	输入参数：最新更新日志的时间，待验证日期字符串
          	返回值：true,false
         */
		static boolean isCorrectdate(String lastdate,String date) {
			return true;
		}
     	//处理日志文件
     	public void deal()
     	{	    
  
     	    }
     	//生成output.txt文件
     	public void output()
     	{

     	}

     	//处理"-date"命令

}