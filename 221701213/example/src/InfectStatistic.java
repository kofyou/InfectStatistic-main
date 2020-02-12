import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * InfectStatistic
 *
 * 
 * @author xxx
 * @version xxx
 * @since xxx
 */
class province {

    protected int infected;   // 记录感染人数
    protected int suspected;  // 记录疑似人数
    protected int cure;   // 记录治愈人数
    protected int death;  // 记录死亡人数
    protected String pro_name;    // 记录省名字
    
    public province ()
    {
        
        this.infected = 0;
        
        this.suspected = 0;

        this.cure = 0;

        this.death = 0;
    }

    public void set_infected (int sum)
    {

        this.infected = sum;

    }

    public void set_suspected (int sum)
    {

        this.suspected = sum;

    }

    public void set_cure (int sum)
    {

        this.cure = sum;

    }

    public void set_death (int sum)
    {

        this.death = sum;

    }

    public void set_pro_name (String str)
    {

        this.pro_name = str;

    }

}

public class InfectStatistic {

    public void init_province(province[] pro , String[] str) //初始化各个省情况
    {

        for(int i = 0 ;i < 5 ;i++)
        {

            pro[i] = new province();

            pro[i].set_pro_name(str[i]);

        }

    }

    public String get_name()    // 获取特定时间的日志名
    {

        Date dd = new Date();

        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");

        String log_name = sim.format(dd);

        log_name += ".log";

        return log_name;

    }

    public boolean find_log_name(String log_name)   // 寻找名称相应的日志
    {

        File path = new File("D:\\221701213\\example\\log");

        File[] list = path.listFiles();

        boolean find = false;

        for (File file : list)
        {

            if (log_name.equals(file.getName())) {

                find = true;

            }
        }
        return find;

    }

    public void process_log(String log_name) throws IOException // 读取日志内容
    {

        String result = new String ("D:\\221701213\\example\\log\\"+log_name);

        FileInputStream fstream = new FileInputStream(new File(result));

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream,"UTF-8"));

        String strLine;

        while((strLine = br.readLine()) != null)
        {

            //System.out.println(strLine);

        }

        br.close();

    }

    public static void main(String[] args) throws IOException {

        InfectStatistic in = new InfectStatistic();

        String[] province_name = new String[] {"安徽","福建","甘肃","广东","广西",
        "贵州","海南","河北","河南","黑龙江","湖北","湖南","江西","吉林","江苏","辽宁","内蒙古",
        "宁夏","青海","山西","山东","陕西","四川","西藏","新疆","云南","浙江"};

        province[] pro;

        pro = new province[province_name.length];

        in.init_province(pro , province_name);

        String log_name = in.get_name()+".txt";

        boolean find = in.find_log_name(log_name);

        if(find == true)
        {

            in.process_log(log_name);

        }
        
    }
}
