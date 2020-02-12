/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) {
        parameterOption parOpt=new parameterOption(args);
		parOpt.process();
    }
}

class parameterOption{
	private String[] myArgs;
	private Lib lib;
	private String log;
	private String out;
	private String date=null;
	private int[] province=null;
	private int[] type= null;
	
	
	public parameterOption(String []args) {
		//构造函数
		myArgs=args;
		//持有lib 对象
		lib=new Lib();
		
	}
	
	public void process() {
		if(myArgs.length==0) {
			//System.out.println("没有参数");
		}
		else {
			if(!myArgs[0].equals("list")) {
				System.out.println("不以list命令开头");
			}//if-end
			
			for(int i=1;i<myArgs.length;i++) {
				if(myArgs[i].startsWith("-")) {
					//System.out.println("包含命令段");
					if(myArgs[i].equals(lib.commands[0])) {
						if(i+1>=myArgs.length||myArgs[i+1].startsWith("-")) {//扫描之后的命令行(检查命令行数目)
							System.out.println("命令不符合规范");
							break;
						}
						else {
							//把后一条命令行当作日志文件路径
							log=myArgs[i+1];
						}	
					}//command-0-log
				  if(myArgs[i].equals(lib.commands[1])) {
					  if(i+1>=myArgs.length||myArgs[i+1].startsWith("-")) {//扫描之后的命令行(检查命令行数目)
							System.out.println("命令不符合规范");
							break;
						}
						else {
							//把后一条命令行当作日志文件路径
							out=myArgs[i+1];
						}	
				  }//command-1-out
				  if(myArgs[i].equals(lib.commands[2])){
					  if(i+1>=myArgs.length||myArgs[i+1].startsWith("-")) {//扫描之后的命令行(检查命令行数目)
							//可能是省略日期，则日期为最新的一天
						  date=null;
						}
					  else {
						  date=myArgs[i+1];
					  }
				  }//command-2-date
				  if(myArgs[i].equals(lib.commands[3])){
					  boolean flag=false;
					  type=new int[lib.types.length];
					  if(i+1>=myArgs.length||myArgs[i+1].startsWith("-")) {//扫描之后的命令行(检查命令行数目)
							//可能是省略，则type为所有
						  for(int t=0;t<type.length;t++) {
							  type[t]=1;
						  }
						}//if-end
					  else {
						  for(i++;i<myArgs.length;i++) {
							  if(myArgs[i].startsWith("-")) {
									flag=true;
									break;
								}
								for(int j=0;j<lib.types.length;j++) {
									//System.out.println("进入循环");
									if(myArgs[i].equals(lib.types[j])) {
										type[j]=1;
									}
								}//for-end		
							}//for-end
						  if(flag==true){
							  i--;
							  continue;
						  }
						  else {
							  break;
						  }
					  }//else-end
				  }//command-3-type的计数器
				  
				  if(myArgs[i].equals(lib.commands[4])){
					  boolean flag2=false;
					  province=new int[lib.provinces.length];
					  if(i+1>=myArgs.length||myArgs[i+1].startsWith("-")) {//扫描之后的命令行(检查命令行数目)
							//可能是省略，则province为所有
							  province[0]=1;
						}//if-end
					  else {
							for(i++;i<myArgs.length;i++) {
								for(int j=0;j<lib.provinces.length;j++) {
									if(myArgs[i].equals(lib.provinces[j])) {
										province[j]=1;
									}
								}//for-end	
								if(myArgs[i].startsWith("-")) {
									flag2=true;
									break;
								}
								
							}//for-end
							if(flag2==true){
								  i--;
								  continue;
							  }
							else {
								break;
							}
					  }//else-end
				  }//command-4-province
				}//start with "-"
			}//for loop -
			if(date==null) date="最新的一天";
			if(type==null) type= new int[]{1,1,1,1};
			if(province==null) {
				province=new int[] {1};
			}
			
		}
	}
	
}
	
	