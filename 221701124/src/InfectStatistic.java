import java.util.Date;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public String targetPath; //目标路径
    public String originPath; //原路径
    public int var=0;
    public int preMark=0;
    /*
     * 获取当前时间
     * 将默认日期设为当前时间
     */
    static Date date = new Date();
    static SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
    public static String acqTime = dateFormat.format(date);
    /*
     * 设置省份列表
     * 0代表未列出，1代表列出
     */
    public int [] area = {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public String[] areaStr = new String[]{"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北","湖南",
    		"吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"};
    /*
     * 设置患者类型（ip,sp,cure,dead）
     * 0代表无需列出，1代表感染，2代表疑似，3代表治愈，4代表死亡
     * 默认排序为1,2,3,4
     */
    public int [] patients = {1,2,3,4};
    public String[] patientsStr = new String[] {"感染患者","疑似患者","治愈","死亡"};
    /*
     * 设置全国和各地各个患者类型的人数情况
     * 数组的一维代表地区（包括全国），二维代表患者类型（ip,sp,cure,dead）
     */
    public int [][] totalNumber = new int [32][4];
    

    
    public static void main(String[] args) {
        System.out.println("helloworld");
        System.out.println( acqTime);
        int a=0;
        int d=a++;
        int c=++a;
        System.out.println(c);


    }
}
