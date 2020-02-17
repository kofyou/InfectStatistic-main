/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) throws IOException {
    	for (int i=0;i<args.length;i++) {		//循环读取命令行参数
			String logpath=null;				//日志文件路径
			String outpath=null;				//输入文件路径
			if (args[i]=="-log") {
				logpath=args[i+1];				//-log参数的下一个参数即为日志文件路径名
			}
			if (args[i]=="-out") {
				outpath=args[i+1];				//-out参数的下一个参数即为输出文件路径名
			}
			File logfilepath=new File(logpath);
			File list[]=logfilepath.listFiles();		//获取日志文件列表
			for (int logi=0;logi<list.length;logi++) {	//循环获得日志文件夹中的每一个日志文件
				//System.out.println(list[logi].getAbsolutePath());	//输出日志文件的绝对路径
				File logfile=new File(list[logi].getAbsolutePath());	//新建日志文件对象获得文件路径
				FileReader log=null;					//使用FileReader类读取日志文件
				log = new FileReader(logfile);			
				char a[]=new char[10000];
				log.read(a);							//读取文件内容
				for (char c : a) {
					System.out.print(c); // 输出文件
					log.close();
				}
			}
		}
    }
}
