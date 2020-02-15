/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic
{
	
	public String[] allType = {"感染患者，疑似患者，治愈，死亡患者"};
	
	public String[] allProvinces = {"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北"
									,"河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏"
									,"青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
	/*
	 * 读取日志文件的内容
	 * 输入参数：日志路径
	 * 返回值：无
	 */
	public void readLog(String filePath) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
        		new FileInputStream(new File(filePath)), "UTF-8"));
		String line = null;
		while((line = bufferedReader.readLine()) != null)
		{
			if(!line.startsWith("//"))
			{
				logHandle(line);
			}
		}
		bufferedReader.close();
	}
	
	/*
	 * 处理日志中的每一行
	 * 输入参数：未注释的日志行
	 * 返回值：无
	 */
	public void logHandle(String inputLine)
	{
		int patternNum=10;
		String[] patterns =
		{
			"(\\W+) 新增 感染患者 (\\d+)人",
			"(\\W+) 新增 疑似患者 (\\d+)人",
			"(\\W+) 感染患者 流入 (\\W+) (\\d+)人",
			"(\\W+) 疑似患者 流入 (\\W+) (\\d+)人",
			"(\\W+) 死亡 (\\d+)人",
			"(\\W+) 治愈 (\\d+)人",
			"(\\W+) 疑似患者 确诊感染 (\\d+)人",
			"(\\W+) 排除 疑似患者 (\\d+)人"
		};
		boolean[] matchPattern = new boolean[8];		
		for(int i = 0; i < 8; i++)
		{
			matchPattern[i] = false;
		}
		for(int i = 0; i < 8; i++)
		{
			matchPattern[i] = Pattern.matches(patterns[i], inputLine);
		}
		for(int i = 0; i < 8; i++)
		{
			if(matchPattern[i])
			{
				patternNum=i;
				break;
			}
		}
		switch(patternNum)
		{
			case '0' :
				
				break;
			case '1' :
				
				break;
			case '2' :
				
				break;
			case '3' :
	
				break;
			case '4' :
	
				break;
			case '5' :
	
				break;
			case '6' :
	
				break;
			case '7' :
				
				break;
			default :
				System.out.println("日志语句出错");
		}
	}
	
	public static void main(String args[]) throws IOException
	{
		
	}
}
