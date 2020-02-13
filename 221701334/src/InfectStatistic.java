import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
class Info implements Comparable<Info>{
	String province;
	int infected;
	int suspected;
	int dead;
	int cured;
	public boolean equals(Info o) {
		return this.province.equals(o.province);
	}
	@Override
	public int compareTo(Info o) {
		// TODO Auto-generated method stub
		Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);  
		 return com.compare(this.province, o.province); 
	}
}


class InfectStatistic {
    public static void main(String[] args) throws IOException,FileNotFoundException {
    	ArrayList<Info> list = new ArrayList<Info>(); 
        if(args[0].equals("list")) {
        	Info[] trip = new Info[4];
        	for (int i=0; i<trip.length; i++)
        		trip[i] = new Info();
        	System.out.println("start list");
        	String path=args[4];
        	File file=new File(path);
        	File[] tempList=file.listFiles();
        	for(int i=0;i<tempList.length;i++) {
        		if(tempList[i].isFile()) {
        			InputStreamReader read = new InputStreamReader(new FileInputStream(tempList[i]), "UTF-8");
                    BufferedReader reader = new BufferedReader(read);
        			System.out.println("文件:"+tempList[i]);
        			String str;
        			
        			while ((str = reader.readLine())!= null) {
        				String[] arrays = str.split("\\s+");
        				for(int j=0;j<arrays.length;j++) {
        					
        					switch(arrays[j]) {
        						 case "新增":
        				            System.out.print("0"); break;
        				         case "流入":
        				            System.out.print("1"); break;
        				         case "死亡":
        				            System.out.print("2"); break;
        				         case "治愈":
        				            System.out.print("3"); break;
        				         case "确诊感染":
         				            System.out.print("4"); break;
        				         case "排除":
         				            System.out.print("5"); break;
        				         default:
        				            System.out.print("default");
        					}
        					System.out.println(arrays[j]);
        				}
        			}
        			
        			reader.close();
        		}
        	}
        }
    }
}
