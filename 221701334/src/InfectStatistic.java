import java.io.*;  
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
class Info implements Comparable<Info>{
	String province;
	int infected=0;
	int suspected=0;
	int dead=0;
	int cured=0;
	
	@Override
	public int compareTo(Info o) {
		// TODO Auto-generated method stub
		Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);  
		 return com.compare(this.province, o.province); 
	}
	public boolean equals(Object o) {
		Info t=(Info)o;
		return this.province.equals(t.province);
	}
}

class UnicodeReader extends Reader {   //工具类用于读取文件去除BOM
	  PushbackInputStream internalIn;  
	  InputStreamReader   internalIn2 = null;  
	  String              defaultEnc;  
	  
	  private static final int BOM_SIZE = 4;  
	  
	    
	  UnicodeReader(InputStream in, String defaultEnc) {  
	     internalIn = new PushbackInputStream(in, BOM_SIZE);  
	     this.defaultEnc = defaultEnc;  
	  }  
	  
	  public String getDefaultEncoding() {  
	     return defaultEnc;  
	  }  
	  
	    
	  public String getEncoding() {  
	     if (internalIn2 == null) return null;  
	     return internalIn2.getEncoding();  
	  }  
	  
	    
	  protected void init() throws IOException {  
	     if (internalIn2 != null) return;  
	  
	     String encoding;  
	     byte bom[] = new byte[BOM_SIZE];  
	     int n, unread;  
	     n = internalIn.read(bom, 0, bom.length);  
	  
	     if ( (bom[0] == (byte)0x00) && (bom[1] == (byte)0x00) &&  
	                 (bom[2] == (byte)0xFE) && (bom[3] == (byte)0xFF) ) {  
	        encoding = "UTF-32BE";  
	        unread = n - 4;  
	     } else if ( (bom[0] == (byte)0xFF) && (bom[1] == (byte)0xFE) &&  
	                 (bom[2] == (byte)0x00) && (bom[3] == (byte)0x00) ) {  
	        encoding = "UTF-32LE";  
	        unread = n - 4;  
	     } else if (  (bom[0] == (byte)0xEF) && (bom[1] == (byte)0xBB) &&  
	           (bom[2] == (byte)0xBF) ) {  
	        encoding = "UTF-8";  
	        unread = n - 3;  
	     } else if ( (bom[0] == (byte)0xFE) && (bom[1] == (byte)0xFF) ) {  
	        encoding = "UTF-16BE";  
	        unread = n - 2;  
	     } else if ( (bom[0] == (byte)0xFF) && (bom[1] == (byte)0xFE) ) {  
	        encoding = "UTF-16LE";  
	        unread = n - 2;  
	     } else {  
	        // Unicode BOM mark not found, unread all bytes  
	        encoding = defaultEnc;  
	        unread = n;  
	     }      
	     //System.out.println("read=" + n + ", unread=" + unread);  
	  
	     if (unread > 0) internalIn.unread(bom, (n - unread), unread);  
	  
	     // Use given encoding  
	     if (encoding == null) {  
	        internalIn2 = new InputStreamReader(internalIn);  
	     } else {  
	        internalIn2 = new InputStreamReader(internalIn, encoding);  
	     }  
	  }  
	  
	  public void close() throws IOException {  
	     init();  
	     internalIn2.close();  
	  }  
	  
	  public int read(char[] cbuf, int off, int len) throws IOException {  
	     init();  
	     return internalIn2.read(cbuf, off, len);  
	  }  
	  
	}  
class InfectStatistic {
    public static void main(String[] args) throws IOException,FileNotFoundException {
    	ArrayList<Info> list = new ArrayList<Info>(); 
        if(args[0].equals("list")) {
        	Info all=new Info();
        	all.province="全国";
        	String path="";
        	String out="";
        	String date="";
        	ArrayList<String> type = new ArrayList<String>(); 
        	ArrayList<String> province = new ArrayList<String>(); 
        	for(int i=0;i<args.length;i++) {
        		if(args[i].equals("-log"))
            	path=args[i+1];
            	if(args[i].equals("-out"))
            	out=args[i+1];
            	if(args[i].equals("-date"))
                date=args[i+1];
            	if(args[i].equals("-type")) {
            		i++;
            		while(i<args.length&&args[i].charAt(0)!='-') {
            			type.add(args[i]);
            			i++;
            		}
            		i--;
            	}
            	if(args[i].equals("-province")) {
            		i++;
            		while(i<args.length&&args[i].charAt(0)!='-') {
            			province.add(args[i]);
            			i++;
            		}
            		i--;
            	}
        	}
        	File file=new File(path);
        	File[] tempList=file.listFiles();
        	for(int i=0;i<tempList.length;i++) {    		
        		if(tempList[i].isFile()) {
        			BufferedReader br = new BufferedReader(  
        				     new UnicodeReader(  
        				     new FileInputStream(tempList[i]),   
        				     "UTF-8")); 
        			InputStreamReader read = new InputStreamReader(new FileInputStream(tempList[i]), "UTF-8");
                    BufferedReader reader = new BufferedReader(read);
        			String str;
        			
        			while ((str = br.readLine())!= null) {
        				String[] arrays = str.split("\\s+");
        				for(int j=0;j<arrays.length;j++) {
        					Info t=new Info();
        					
        					switch(arrays[j]) {
        						 case "新增":
        							 t.province=arrays[j-1];
        							 
        							 if(list.contains(t)) {
        								 if(arrays[j+1].equals("感染患者")) {
        									 list.get(list.indexOf(t)).infected+=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));
        									 all.infected+=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));
        								 }
        								 else {
        									 list.get(list.indexOf(t)).suspected+=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));
        									 all.suspected+=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));
        								 }
        								 
        							 }
        							 else {
        								 if(arrays[j+1].equals("感染患者")) {
        									 t.infected=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));  
        									 all.infected+=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));
        								 }
        								 else {
        									 t.suspected=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));  
        									 all.suspected+=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));
        								 }  
        								 
        								 list.add(t);
        							 }
        				            break;
        				         case "流入":
        				        	 Info from=new Info();
        				        	 Info to=new Info();
        				        	 from.province=arrays[j-2];
        				        	 to.province=arrays[j+1];
        							 if(list.contains(to)) {
        								 if(arrays[j-1].equals("感染患者")) {
        									 list.get(list.indexOf(from)).infected-=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));
        									 list.get(list.indexOf(to)).infected+=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1)); 
        								 }
        								 else {
        									 list.get(list.indexOf(from)).suspected-=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));
        									 list.get(list.indexOf(to)).suspected+=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));  
        								 }     								 
        							 }
        							 else {
        								 if(arrays[j-1].equals("感染患者")) {
        									 list.get(list.indexOf(from)).infected-=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));
        									 to.infected=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));  
        								 }
        								 else {
        									 list.get(list.indexOf(from)).infected-=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));
        									 to.suspected=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1));  
        								 }  
        								 
        								 list.add(to);
        							 }
        				            break;
        				         case "死亡":
        							 t.province=arrays[j-1];
        							 list.get(list.indexOf(t)).infected-=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1)); 
        							 list.get(list.indexOf(t)).dead+=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1)); 
        							 all.infected-=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1)); 
        							 all.dead+=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1)); 
        				            break;
        				         case "治愈":
        				        	 t.province=arrays[j-1];
        				        	 list.get(list.indexOf(t)).infected-=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1)); 
        							 list.get(list.indexOf(t)).cured+=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1)); 
        							 all.infected-=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1)); 
        							 all.cured+=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1)); 
        				            break;
        				         case "确诊感染":
        				        	 t.province=arrays[j-2];
        				        	 list.get(list.indexOf(t)).suspected-=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1)); 
        							 list.get(list.indexOf(t)).infected+=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1));   
        							 all.suspected-=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1)); 
        							 all.infected+=Integer.parseInt(arrays[j+1].substring(0,arrays[j+1].length()-1)); 
         				            break;
        				         case "排除":
        				        	 t.province=arrays[j-1];
        				        	 list.get(list.indexOf(t)).suspected-=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1)); 
        				        	 all.suspected-=Integer.parseInt(arrays[j+2].substring(0,arrays[j+2].length()-1)); 
         				            break;
        				         default:
        				            break;
        					}

        				}
        			}
        			
        			reader.close();
        			br.close();
        		}
        		if(date.equals(tempList[i].getName().toString().substring(0,10)))
        		break;
        	}     	

        	File fr=new File(out);    
			if(!fr.exists())    
			{    
				try {    
    				file.createNewFile();    
    			} catch (IOException e) {    
    				// TODO Auto-generated catch block    
    			e.printStackTrace();    
    			}    
			}  
			FileWriter fw = new FileWriter(out);
			BufferedWriter fout = new BufferedWriter(fw);
        	if(province.size()>0) {
				if(type.size()>0) {
					for(int k=0;k<province.size();k++) {
						Info t=new Info();
						t.province=province.get(k);
						if(t.province.equals("全国")) {
		    				System.out.print(all.province+" ");
        					String string=all.province+" ";
        					if(type.contains("感染")) {
        						System.out.print("感染患者:"+all.infected+" ");
        						string+="感染患者:"+all.infected+" ";
        					}
        					if(type.contains("疑似")) {
        						System.out.print("疑似患者:"+all.suspected+" ");
        						string+="疑似患者:"+all.suspected+" ";
        					}
        					if(type.contains("治愈")) {
        						System.out.print("治愈患者:"+all.cured+" ");
        						string+="治愈患者:"+all.cured+" ";
        					}
        					if(type.contains("死亡")) {
        						System.out.print("死亡患者:"+list.get(list.indexOf(t)).dead+"");
        						string+="死亡患者:"+all.dead+"";
        					}
        					System.out.print("\n");
        					string+="\r\n";
        					fout.write(string);
		    				continue;
						}
        				if(list.contains(t)) {
        					System.out.print(list.get(list.indexOf(t)).province+" ");
        					String string=list.get(list.indexOf(t)).province+" ";
        					if(type.contains("感染")) {
        						System.out.print("感染患者:"+list.get(list.indexOf(t)).infected+" ");
        						string+="感染患者:"+list.get(list.indexOf(t)).infected+" ";
        					}
        					if(type.contains("疑似")) {
        						System.out.print("疑似患者:"+list.get(list.indexOf(t)).suspected+" ");
        						string+="疑似患者:"+list.get(list.indexOf(t)).suspected+" ";
        					}
        					if(type.contains("治愈")) {
        						System.out.print("治愈患者:"+list.get(list.indexOf(t)).cured+" ");
        						string+="治愈患者:"+list.get(list.indexOf(t)).cured+" ";
        					}
        					if(type.contains("死亡")) {
        						System.out.print("死亡患者:"+list.get(list.indexOf(t)).dead+"");
        						string+="死亡患者:"+list.get(list.indexOf(t)).dead+"";
        					}
        					System.out.print("\n");
        					string+="\r\n";
        					fout.write(string);
        				}
        				else {
        					System.out.print(t.province+" ");
        					String string="";
        					if(type.contains("感染")) {
        						System.out.print("感染患者:"+"0"+" ");
        						string+="感染患者:"+"0"+" ";
        					}
        					if(type.contains("疑似")) {
        						System.out.print("疑似患者:"+"0"+" ");
        						string+="疑似患者:"+"0"+" ";
        					}
        					if(type.contains("治愈")) {
        						System.out.print("治愈患者:"+"0"+" ");
        						string+="治愈患者:"+"0"+" ";
        					}
        					if(type.contains("死亡")) {
        						System.out.print("死亡患者:"+"0"+"");
        						string+="死亡患者:"+"0"+"";
        					}
        					System.out.print("\n");
        					string+="\r\n";
        					fout.write(string);
        				}
        			}
				}
				else {
					for(int k=0;k<province.size();k++) {
						Info t=new Info();
						t.province=province.get(k);
						if(t.province.equals("全国")) {
							System.out.print(all.province+" ");
		    				System.out.print("感染患者:"+all.infected+" ");
		    				System.out.print("疑似患者:"+all.suspected+" ");
		    				System.out.print("治愈患者:"+all.cured+" ");
		    				System.out.print("死亡患者:"+all.dead+"\n");	
		    				String string=all.province+" "+"感染患者:"+all.infected+" "+"疑似患者:"+
									all.suspected+" "+"治愈患者:"+all.cured+" "+"死亡患者:"+all.dead+"\r\n";
		    				fout.write(string);
		    				continue;
						}
        				if(list.contains(t)) {
        					System.out.print(list.get(list.indexOf(t)).province+" ");
            				System.out.print("感染患者:"+list.get(list.indexOf(t)).infected+" ");
            				System.out.print("疑似患者:"+list.get(list.indexOf(t)).suspected+" ");
            				System.out.print("治愈患者:"+list.get(list.indexOf(t)).cured+" ");
            				System.out.print("死亡患者:"+list.get(list.indexOf(t)).dead+"\n");
            				String string=list.get(list.indexOf(t)).province+" "+"感染患者:"+list.get(list.indexOf(t)).infected+" "+"疑似患者:"+list.get(list.indexOf(t)).suspected+" "+
            						"治愈患者:"+list.get(list.indexOf(t)).cured+" "+"死亡患者:"+list.get(list.indexOf(t)).dead+"\r\n";
            				fout.write(string);
        				}
        				else {
        					System.out.print(t.province+" ");
        					System.out.print("感染患者:"+"0"+" ");
        					System.out.print("疑似患者:"+"0"+" ");
        					System.out.print("治愈患者:"+"0"+" ");
        					System.out.print("死亡患者:"+"0"+"\n");
        					String string=t.province+" "+"感染患者:"+"0"+" "+"疑似患者:"+"0"+" "+"治愈患者:"+"0"+" "+"死亡患者:"+"0"+"\r\n";
        					fout.write(string);
        				}
        			}
				}
			}
			else {
				if(type.size()>0) {
					for(int k=0;k<list.size();k++) {				
        				System.out.print(list.get(k).province+" ");
        				String string=list.get(k).province+" ";
        				if(type.contains("感染")) {
        					System.out.print("感染患者:"+list.get(k).infected+" ");
        					string+="感染患者:"+list.get(k).infected+" ";
        				}
        				if(type.contains("疑似")) {
        					System.out.print("疑似患者:"+list.get(k).suspected+" ");
        					string+="疑似患者:"+list.get(k).suspected+" ";
        				}
        				if(type.contains("治愈")) {
        					System.out.print("治愈患者:"+list.get(k).cured+" ");
        					string+="治愈患者:"+list.get(k).cured+" ";
        				}
        				if(type.contains("死亡")) {
        					System.out.print("死亡患者:"+list.get(k).dead+"\n");
        					string+="死亡患者:"+list.get(k).dead+" ";
        				}
        				fout.write(string);
        			}
				}
				else {
					String string=all.province+" "+"感染患者:"+all.infected+" "+"疑似患者:"+
							all.suspected+" "+"治愈患者:"+all.cured+" "+"死亡患者:"+all.dead+"\r\n";
					System.out.print(all.province+" ");
    				System.out.print("感染患者:"+all.infected+" ");
    				System.out.print("疑似患者:"+all.suspected+" ");
    				System.out.print("治愈患者:"+all.cured+" ");
    				System.out.print("死亡患者:"+all.dead+"\n");	
    				fout.write(string);
					for(int k=0;k<list.size();k++) {
	    				System.out.print(list.get(k).province+" ");
	        			System.out.print("感染患者:"+list.get(k).infected+" ");
	        			System.out.print("疑似患者:"+list.get(k).suspected+" ");
	        			System.out.print("治愈患者:"+list.get(k).cured+" ");
	        			System.out.print("死亡患者:"+list.get(k).dead+"\n");		
	        			string=list.get(k).province+" "+"感染患者:"+list.get(k).infected+" "+"疑似患者:"+list.get(k).suspected+" "+
	        					"治愈患者:"+list.get(k).cured+" "+"死亡患者:"+list.get(k).dead+"\r\n";
	        			fout.write(string);
	    			}
				}
				
			}
        	fout.close();
        }
    }
}
