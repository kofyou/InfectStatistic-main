/**
 * InfectStatistic
 * TODO
 *
 * @author 杨鑫杰
 * @version 1.0
 * @since 2020.02.17
 */

import java.io.*;
import java.util.regex.*;
import java.text.SimpleDateFormat;
import java.util.Date;


class InfectStatistic{
	
	public String path_in;//读取日志目录
	public String path_out;//输出日志目录
	
	//获取格式化日期
	Date dt = new Date(System.currentTimeMillis());
    String strDateFormat = "yyyy-MM-dd";
    SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
    String date=sdf.format(dt);
	
	public String[] province = {
			"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃","广东", "广西",
			"贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
			"江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", 
			"上海","四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
	/*二维数组
	 * 一维表示省份
	 * 二维分别表示{是否需要列出,ip,sp,cure,dead}*/
	public int[][] situation=new int[35][5];
	
	//类型输出顺序，默认ip,sp,cure,dead
	public int type_order[]= {1,2,3,4};
	public String[] type= {"感染患者", "疑似患者", "治愈", "死亡"};
	
    public static void main(String[] args) {
    	
    }	
}
