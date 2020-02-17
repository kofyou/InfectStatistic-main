/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
import java.io.*;
class InfectStatistic {
    public static void main(String args[]) throws IOException {
    	for (int i=0;i<args.length;i++) {		//循环读取命令行参数
    		//System.out.println(args[i]);
			String logpath=null;				//日志文件路径
			String outpath=null;				//输入文件路径
			if (args[i].equals("-log")) {
				logpath=args[i+1];				//-log参数的下一个参数即为日志文件路径名
				System.out.println(logpath);
				LogProcess(logpath);			//对-log参数进行处理
			}
			if (args[i].equals("-out")) {
				outpath=args[i+1];				//-out参数的下一个参数即为输出文件路径名
			}
		}
    }
    public static void LogProcess(String logpath) throws IOException {
    	File logfilepath=new File(logpath);
		File list[]=logfilepath.listFiles();		//获取日志文件列表
		for (int logi=0;logi<list.length;logi++) {	//循环获得日志文件夹中的每一个日志文件
			//System.out.println(list[logi].getAbsolutePath());	//输出日志文件的绝对路径
			File logfile=new File(list[logi].getAbsolutePath());	//新建日志文件对象获得文件路径logfile
			//采用字符流读取文件内容
			BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(logfile),"UTF-8"));
			String lineText=null;
			while ((lineText=buff.readLine())!=null) {	//按行读取文本内容
				if(!lineText.startsWith("//"))		//行开头是"//"则不读取
					System.out.println(lineText);
			}
			buff.close();
		}
    }
}
