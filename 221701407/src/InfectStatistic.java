import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * InfectStatistic
 * TODO
 *
 * @author 221701407吕宇昕
 * @version 1.0.0
 * @since 2020.02
 */
class InfectStatistic {
	
    public static void main(String[] args) throws ParseException, IOException {
        ArrayList<String> list=new ArrayList<String>();
        for(String i:args) {
        	list.add(i);//把命令加入list
        }
        CommandAnalysis commandanalysis=new CommandAnalysis();//实例化命令行解析对象
		commandanalysis.CommandRun(list);//处理命令
        
    }
    static class Command{
    	//list命令
    	private boolean list;
    	//值为1表示包含此参数，反之则无
    	private boolean date;
    	private boolean type;
    	private boolean province;
    	private boolean log;
    	private boolean out;
    	//具体参数内容
    	private String date_argument;
    	private ArrayList<String> type_argument;//可能有多个参数
    	private ArrayList<String> province_argument;//可能有多个参数
    	private String log_argument;
    	private String out_argument;
    	
    	public Command() {
    		list=false;
    		date=false;
    		type=false;
    		province=false;
    		log=false;
    		out=false;
    		date_argument="";
    		log_argument="";
    		out_argument="";
    		type_argument=new ArrayList<String>();
    		province_argument=new ArrayList<String>();
    	}
    	
    	public void setList(boolean list) {
    		this.list=list;
    	}
    	
    	public void setDate(boolean date) {
    		this.date=date;
    	}
    	
    	public void setType(boolean type) {
    		this.type=type;
    	}
    	
    	public void setProvince(boolean province) {
    		this.province=province;
    	}
    	
    	public void setLog(boolean log) {
    		this.log=log;
    	}
    	
    	public void setOut(boolean out) {
    		this.out=out;
    	}
    	
    	public void setDateArgument(String argument) {
    		this.date_argument=argument;
    	}
    	public void setTypeArgument(ArrayList<String> argument) {
    		this.type_argument=argument;
    	}
    	public void setProvinceArgument(ArrayList<String> argument) {
    		this.province_argument=argument;
    	}
    	
    	public void setLogArgument(String argument) {
    		this.log_argument=argument;
    	}
    	
    	public void setOutArgument(String argument) {
    		this.out_argument=argument;
    	}
    }
    public static class LogControl {
    	
    	public static ArrayList<String> SortLog(String date,String log,ArrayList<String> provin) throws IOException, ParseException {
    		
    		File file=new File(log);
    		File[] tempList=file.listFiles();
    		ArrayList<String> FileName=new ArrayList<String>();//存储文件名
    		ArrayList<String> OneFile=new ArrayList<String>();//单个log内容
    		ArrayList<String> AllFiles=new ArrayList<String>();//在date前所有log内容
    		ArrayList<String> Provinces=new ArrayList<String>();//log中每一行的省份
    		ArrayList<String> OutputText=new ArrayList<String>();//最终所有省份（包含全国）统计结果
    		String province="";
    		
    		for(int i=0;i<tempList.length;i++) {
    			if(tempList[i].isFile()) {
    				FileName.add(tempList[i].toString());
    			}
    		}
    		
    		for(int i=0;i<FileName.size();i++) {    			
    				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
					String FileNameDate=FileName.get(i).toString().split("\\\\")[2].split("\\.")[0];
    				if(date.equals("")) {
						date=df.format(new Date());
					}
					long date1=df.parse(date).getTime();
					long date2=df.parse(FileNameDate).getTime();
					//如果命令中指定的时间大于等于log文件的时间，即要处理
					if(date1>=date2) {
						BufferedReader breader=new BufferedReader(new FileReader(new File(log+FileNameDate+".log.txt")));
						String line=null;
						while((line=breader.readLine())!=null) {
							if(line.substring(0,2).equals("//")) {
								//注释语句忽略
								continue;
							}else {
								if(!line.equals("")) {
									OneFile.add(line);
								}
							}
						}
						breader.close();
						for (int j=0;j<OneFile.size();j++) {
							AllFiles.add(OneFile.get(j));
						}	
					}else {
						continue;
					}  			
    		}
    		//在Provinces中存入AllFiles里的省份（不重复）
    		for(int i=0;i<AllFiles.size();i++) {
    			boolean isExist=true;
    			if(Provinces.size()==0) {
    				Provinces.add(AllFiles.get(i).split(" ")[0]);//存入省份
    			}else {
    				for(int j=0;j<Provinces.size();j++) {
    					//判断下一行要存入的省份是否已存在Provinces中
    					if(AllFiles.get(i).split(" ")[0].equals(Provinces.get(j).split(" ")[0])) {
    						isExist=true;
    						break;
    					}else {
    						isExist=false;
    					}
    				}
    			}
    			if (!isExist) {
    				Provinces.add(AllFiles.get(i).split(" ")[0]);
				}
    		}
    		Integer total_ip=0,total_sp=0,total_cure=0,total_dead=0;//ip:感染，sp:疑似，cure:治愈，dead:死亡
    		for(int i=0;i<Provinces.size();i++) {
    			Integer ip=0,sp=0,cure=0,dead=0;
    			province=Provinces.get(i);//获得省份
    			for(int j=0;j<AllFiles.size();j++) {
    				String[] info=AllFiles.get(j).split(" ");
    				if(province.equals(info[0])) {
    					if(info.length==3) {
    						//情况1.<省> 死亡 n人  
    						//情况2.<省> 治愈 n人
    						String people=info[2];//people=n人
    						Integer n=Integer.parseInt(people.substring(0,people.length()-1));//获得人数
    						if(info[1].equals("死亡")) {
    							dead+=n;
    							ip-=n;
    						}else {
    							cure+=n;
    							ip-=n;
    						}
    					}else if(info.length==4) {
    						//情况1.<省> 新增     感染患者 n人
    						//情况2.<省> 新增     疑似患者 n人
    						//情况3.<省> 疑似患者 确诊感染 n人
    						//情况4.<省> 排除     疑似患者 n人
    						String people=info[3];
    						Integer n=Integer.parseInt(people.substring(0,people.length()-1));
    						if(info[1].equals("新增")&&info[2].equals("感染患者")) {
    							ip+=n;
    						}else if(info[1].equals("新增")&&info[2].equals("疑似患者")) {
    							sp+=n;
    						}else if(info[1].equals("疑似患者")) {
    							ip+=n;
    							sp-=n;
    						}else {
    							sp-=n;
    						}
    					}else {
    						//情况1.<省1> 感染患者 流入 <省2> n人
    						//情况2.<省1> 疑似患者 流入 <省2> n人
    						String people=info[4];
    						Integer n=Integer.parseInt(people.substring(0,people.length()-1));
    						if(info[1].equals("感染患者")) {
    							ip-=n;
    						}else {
    							sp-=n;
    						}
    					}
    				}else if(info.length==5) {
    					//外省流入本省的情况
    					String people=info[4];
    					Integer n=Integer.parseInt(people.substring(0,people.length()-1));
    					if(province.equals(info[3])) {
    						if(info[1].equals("感染患者")) {
    							ip+=n;
    						}else {
    							sp+=n;
    						}
    					}
    				}
    			}
    			total_ip+=ip;
    			total_sp+=sp;
    			total_cure+=cure;
    			total_dead+=dead;
    			OutputText.add(province+" 感染患者"+ip.toString()+"人 疑似患者"+sp.toString()+"人 治愈"+cure.toString()+"人 死亡"+dead.toString()+"人");
    		}
    		OutputText.add(0,"全国 感染患者"+total_ip.toString()+"人 疑似患者"+total_sp.toString()+"人 治愈"+total_cure.toString()+"人 死亡"+total_dead.toString()+"人");
    		
    		if(provin.size()>0) {
    			boolean isExist=true;
    			for(int i=0;i<provin.size();i++) {
    				for(int j=0;j<OutputText.size();j++) {
    					if(provin.get(i).equals(OutputText.get(j).split(" ")[0])) {
    						isExist=true;
    						break;
    					}else {
    						isExist=false;
    					}
    				}
    				if(!isExist) {
        				OutputText.add(provin.get(i).toString()+" 感染患者0人 疑似患者0人 治愈0人 死亡0人");
        			}
    			}
    		}
    		return OutputText;
    	}
    	
    	public void OutLog(String log,String out,String date,ArrayList<String> province,ArrayList<String> type,ArrayList<String> OutputText) throws IOException {
    		String OutputContent="";
    		
    		for(int i=0;i<OutputText.size();i++) {
    			if(province.size()<=0&&type.size()<=0) {
    				OutputContent+=OutputText.get(i).toString()+"\n";
    			}else if(type.size()<=0) {
    				for(int j=0;j<province.size();j++) {
    					if(OutputText.get(i).toString().split(" ")[0].equals(province.get(j).toString())) {
    						OutputContent+=OutputText.get(i).toString()+"\n";
    					}
    				}
    			}else if(province.size()<=0) {
    				OutputContent+=OutputText.get(i).split(" ")[0];
    				for(int j=0;j<type.size();j++) {
    					OutputContent+=" ";
    					if(type.get(j).equals("ip")) {
    						OutputContent+=OutputText.get(i).split(" ")[1];
    					}else if(type.get(j).equals("sp")) {
    						OutputContent+=OutputText.get(i).split(" ")[2];
    					}else if(type.get(j).equals("cure")) {
    						OutputContent+=OutputText.get(i).split(" ")[3];
    					}else {
    						OutputContent+=OutputText.get(i).split(" ")[4];
    					}
    				}
    				OutputContent+="\n";
    			}else {
    				for(int j=0;j<province.size();j++) {
    					if(OutputText.get(i).toString().split(" ")[0].equals(province.get(j).toString())) {
    						OutputContent+=OutputText.get(i).split(" ")[0];
    	    				for(int k=0;k<type.size();k++) {
    	    					OutputContent+=" ";
    	    					if(type.get(k).equals("ip")) {
    	    						OutputContent+=OutputText.get(i).split(" ")[1];
    	    					}else if(type.get(k).equals("sp")) {
    	    						OutputContent+=OutputText.get(i).split(" ")[2];
    	    					}else if(type.get(k).equals("cure")) {
    	    						OutputContent+=OutputText.get(i).split(" ")[3];
    	    					}else {
    	    						OutputContent+=OutputText.get(i).split(" ")[4];
    	    					}
    	    				}
    	    				OutputContent+="\n";
    					}
    				}
    			}
    		}
    		
    		OutputContent+="// 该文档并非真实数据，仅供测试使用";
    		File file=new File(out);
    		FileOutputStream fileoutputstream=new FileOutputStream(file);
    		if(!file.exists()) {
    			file.createNewFile();
    			file=new File(out);//重新实例化
    		}
    		fileoutputstream.write(OutputContent.getBytes());
    		fileoutputstream.flush();
    		fileoutputstream.close();
    	}
    }
    
    public static class CommandAnalysis {
    	
    	public void CommandRun(ArrayList<String> list) throws IOException, ParseException{
    		//规格化command
    		Command command=new Command();
    		command=SetCommand(list);
    		//整理日志数据并输出
    		String log=command.log_argument;
    		String out=command.out_argument;
    		String date=command.date_argument;
    		ArrayList<String> province=command.province_argument;
    		ArrayList<String> type=command.type_argument;
    		
    		LogControl logcontrol=new LogControl();
    		if(log.equals("")||out.equals("")) {
    		}else {
        		ArrayList<String> OutputText=logcontrol.SortLog(date,log,province);
        		logcontrol.OutLog(log, out, date, province, type,OutputText);
    		}
    			
    	}

		private Command SetCommand(List<String> list) {
			Command command=new Command();
			for(int i=0;i<list.size();i++) {
				String a=list.get(i);
				//获取command
				switch(a) {
				case "list":
					command.setList(true);//命令行包含list命令
					break;
				case "-date":
					command.setDate(true);
					command.setDateArgument(list.get(i+1));
					break;
				case "-type":
					command.setType(true);
					ArrayList<String> typ=new ArrayList<String>();
					for(int j=i+1;j<list.size();j++) {
						String type=list.get(j);
						if(!type.substring(0,1).equals("-")) {
							typ.add(type);//直到搜索到后一个参数截止
						}else {
							break;
						}
					}
					command.setTypeArgument(typ);					
					break;
				case "-province":
					command.setProvince(true);
					ArrayList<String> prov=new ArrayList<String>();
					for(int j=i+1;j<list.size();j++) {
						String province=list.get(j);
						if(!province.substring(0,1).equals("-")) {
							prov.add(province);
						}else {
							break;
						}
					}
					command.setProvinceArgument(prov);
					break;
				case "-log":
					command.setLog(true);
					command.setLogArgument(list.get(i+1));
					break;
				case "-out":
					command.setOut(true);
					command.setOutArgument(list.get(i+1));
					break;				
				}
			}		 
			return command;
		}
    }
}

