import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;



/**
 * InfectStatistic
 * TODO
 *
 * @author 221701305_林琳
 * @version xxx
 * @since xxx
 */

class Province{
    int ip;//感染患者
    int sp;//疑似患者
    int cure;//治愈
    int dead;//死亡
}

class InfectStatistic {

    //读文件
    public void readFile(String s) throws IOException{
        /*int num=0;
        char[] buf=new char[1024];
        FileReader fr = new FileReader("F:\\test.txt");
      //取出字符存到buf数组中
		while((num=fr.read(buf))!=-1) {
			//String(char[] cbuf,a,b),从cbuf的位置a开始取出连续的b个char组成字符串
			System.out.println(new String(buf,0,num));;
		}
		//测试 一个字一个字读
		for(int i=0;i<10;i++) {
			System.out.print(buf[i]+" ");
			
		}*/
    	
    	int position=0;
        String[] bufstring=new String[1024];
        //打开带读取的文件
        //BufferedReader br = new BufferedReader(new FileReader("F:\\test.txt"));
        BufferedReader br = new BufferedReader(new FileReader(s));
        String line=null;
        while((line=br.readLine())!=null) {
        	bufstring[position]=line;
        	position++;
        }
        br.close();//关闭文件
        for(int i=0;i<position;i++) {
        	System.out.println(bufstring[i]);
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

          
    public static void main(String[] args) throws IOException {
        //System.out.println("hello world");
        InfectStatistic in = new InfectStatistic();
        
        in.writeFile();
        in.readFile("F:\\InfectStatistic-main\\example\\log\\2020-01-22.log.txt");
    }
}
