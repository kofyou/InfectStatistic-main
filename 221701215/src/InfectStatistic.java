import java.io.InputStream;

/**
 * InfectStatistic
 * TODO
@@ -6,9 +8,235 @@
 * @version xxx
 * @since xxx
 */
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.io.*;
public class InfectStatistic {
    public static void main(String[] args) {
    	for(int i = 0;i<args.length;i++) System.out.println(args[i]);
    	DocFormatter doc=new DocFormatter(args);
    	Excute excute=new Excute(doc);
    	excute.run();
        System.out.println("helloworld");
    }
}
class DocFormatter{
	private String[] cmd;
	private int logPathInd;
	private int outPathInd;
	private int dateInd;
	private int typeInd;
	private int provinceInd;
	DocFormatter(String[] arg){
       cmd=arg;
       exchange();
	}
	public void exchange() {
		dateInd=-1;
		typeInd=-1;
		provinceInd=-1;
		for(int i=0;i<cmd.length;i++) {
			if(cmd[i].equals("-log"))  logPathInd=++i;
			if(cmd[i].equals("-out"))  outPathInd=++i;
			if(cmd[i].equals("-date"))  dateInd=++i;
			if(cmd[i].equals("-province"))  provinceInd=++i;
			if(cmd[i].equals("-type"))  typeInd=++i;
		}
	}
	public String getlogPath() {
		return cmd[logPathInd];
	}
	public String getoutPath() {
		return cmd[outPathInd];
	}
	public String getDate() {
		if(dateInd==-1) return "Latest";
		return cmd[dateInd];
	}
	public Vector getType() {	
		Vector<String> type= new Vector<String>();
		if(typeInd!=-1) {
			int i=typeInd;
			while( i<cmd.length &&(cmd[i]!="-log"||cmd[i]!="-out"||cmd[i]!="-province"||cmd[i]!="-date")) {
				type.add(cmd[i]);
				i++;
			}	
		}
		return type;
	}
	public Vector<String> getPro() {
		Vector<String> vec= new Vector<String>();
		if(provinceInd!=-1) {
			int i=provinceInd;
			while( i<cmd.length &&(!cmd[i].equals("-log")||cmd[i]!="-out"||cmd[i]!="-type"||cmd[i]!="-date")) {
				vec.add(cmd[i]);
				i++;
			}	
		}
		return vec;
	}	
}
class Excute{
	private HashMap<String,Status> map;
	private DocFormatter Formatter;
	private boolean tyChecked[];
	private boolean TyChecked;
	private boolean ProChecked;
	private Vector<String> pro;
	public Excute(DocFormatter d) {
		Formatter =d;
		ProChecked=false;
		TyChecked=false;
		tyChecked= new boolean[]{false,false,false,false};
		pro=new Vector<>();
		map=new HashMap<>();
	}
	public void run() {
		for(int i=0;i<Formatter.getType().size();i++) {
			TyChecked=true;
			if(Formatter.getType().get(i).equals("ip")) tyChecked[0]=true;
			if(Formatter.getType().get(i).equals("sp")) tyChecked[1]=true;
			if(Formatter.getType().get(i).equals("dead")) tyChecked[2]=true;
			if(Formatter.getType().get(i).equals("cure")) tyChecked[3]=true;
		}
		for(int i=0;i<Formatter.getPro().size();i++) {
			ProChecked=true;
			pro.add(Formatter.getPro().get(i));
		}
		try{
			String date=Formatter.getDate();
			String source=Formatter.getlogPath();
			openFile();
		}
		catch(IOException e) {
			
		}
		try {
			String dest=Formatter.getoutPath();
			WriteFile(dest);
		}
		catch(IOException e) {
			
		}
	}
	private String openFile() throws IOException  {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("a.txt"),"UTF-8"));
		String line=null;
		while((line=br.readLine())!=null) {		
			String[] arr = line.split(" "); // ��,�ָ�
			
			
				if(!map.containsKey(arr[0]) ) {
					map.put(arr[0],new Status(arr[0]));
				}
				Status s=map.get(arr[0]);				
				if(arr[1].equals("����")) {
					if(arr[2].equals("��Ⱦ����"))  addIP(s,arr[3]);
					else  addSP(s,arr[3]);
				}
				else if(arr[1].equals("����")) {
					addDead(s,arr[2]);
				}
				else if(arr[1].equals("����")) {
					addCure(s,arr[2]);
				}
				else if(arr[1].equals("�ų�")) {
					delSp(s,arr[3]);
				}
				else if(arr[1].equals("���ƻ���")) {
					if(arr[2].equals("ȷ���Ⱦ"))  spToip(s,arr[3]);
					else {
						if(!map.containsKey(arr[3]) ) {
							map.put(arr[3],new Status(arr[3]));
						}
						Status t=map.get(arr[3]);
						spRunTo(s,t,arr[4]);
					} 
				}
				else if(arr[1].equals("��Ⱦ����")){   
					if(!map.containsKey(arr[3]) ) {
						map.put(arr[3],new Status(arr[3]));
					}
					Status t=map.get(arr[3]);
					ipRunTo(s,t,arr[4]);    }
				else {}
			
		}
		br.close();//�ر��ļ�
         return "";
	};
	private void WriteFile(String dest) throws IOException{
		OutputStream os = new FileOutputStream("output.txt");
		PrintWriter pw=new PrintWriter(os);
		for(String key : map.keySet()){
			Status status=map.get(key);
			if(ProChecked) {                        //����Ƿ���ָ�����
				if(!pro.contains(key)) continue;         //��ָ����ݣ�continue
			}
			
			if(TyChecked) {
				pw.print(key);
				if(tyChecked[0])   pw.print(" ��Ⱦ����"+status.ip +"��");
				if(tyChecked[1])   pw.print(" ���ƻ���"+status.sp +"��");
				if(tyChecked[2])   pw.print(" ����"+status.cure +"��");
				if(tyChecked[3])   pw.print(" ����"+status.dead +"��");
			}
			else
		        pw.print(key +" ��Ⱦ����"+status.ip+"�� ���ƻ���"+status.sp+
		 		   "�� ����"+status.cure+"�� ����"+status.dead+"�� \n");
		}
		pw.close();
	}
	private void addIP(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.ip+=num;
	}
	private void addSP(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.sp+=num;
	}
	private void ipRunTo(Status from,Status to,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		from.ip-=num;
		to.ip+=num;
	}
	private void spRunTo(Status from,Status to,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		from.sp-=num;
		to.sp+=num;
	}
	private void addDead(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.dead+=num;
		s.ip-=num;
	}
	private void addCure(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.cure+=num;
		s.ip-=num;
	}
	private void spToip(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.ip+=num;
		s.sp-=num;
	}
	private void delSp(Status s,String n) {
		int num=Integer.parseInt(n.substring(0, n.length()-1));
		s.sp-=num;
	}
	private void add(Status s,Status t) {
		
	}
	class Status{
		private String pro;
		private int ip;
		private int sp;
		private int cure;
		private int dead;
		public Status(String s) {
			pro=s;
			ip=sp=cure=dead=0;
		}
	}
}