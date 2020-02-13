import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
public class InfectStatistic {
    public  static  void  main(String [] args) throws IOException{
        //System.out.println(args[0]);
        String path = null,date = null,output = null;
        String request = args[0];
        if(request.equals("list")) {
        	//System.out.println(args[0]);
        	for(int i=0;i<args.length;i++) {
        		if(args[i].equals("-date")) 
                    date=args[i+1];
        		else if(args[i].equals("-log"))
        		    path="D:/log/";
        		else if(args[i].equals("-out"))
        			output="D:/output.txt";

        	}
        }
        else System.out.println("input the ritght request");  
        File f = new File("D:/log/");
        //File[] files=f.listFiles();
        String [] ss=f.list();
        for(int i=0;i<ss.length;i++){
         System.out.println(ss[i]);
        }
        System.out.println();
        FileInputStream fl = new FileInputStream ("D:/log/2020-02-01.log.txt");
        InputStreamReader reader = new InputStreamReader(fl,"UTF-8") ;
        BufferedReader bf = new BufferedReader (reader) ;
        BufferedReader bf2 = new BufferedReader (reader) ;
        String str=null;
        String[] line = new String[8] ;
        int num=0;
        while ( (str = bf.readLine() ) != null) {
        	line [num]=str;
        	num++;
        }
        System.out.println(line[0]);
        for(int i=1;i<num;i++)
        	System.out.println(line[i]);
        
    }
}