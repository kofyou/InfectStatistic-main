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
class InfectStatistic {

    public String get_name()// 获取特定时间的日志名
    {

        Date dd = new Date();

        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");

        String log_name = sim.format(dd);

        log_name += ".log";

        return log_name;

    }

    public boolean find_log_name(String log_name)// 寻找名称相应的日志
    {

        File path = new File("D:\\221701213\\example\\log");

        File[] list = path.listFiles();

        boolean find = false;

        for (File file : list) {

            if (log_name.equals(file.getName())) {

                find = true;

            }
        }
        return find;

    }

    public void process_log(String log_name) throws IOException
    {

        String result = new String ("D:\\221701213\\example\\log\\"+log_name);

        System.out.println(result);

        FileInputStream fstream = new FileInputStream(new File(result));

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream,"UTF-8"));

        String strLine;

        while((strLine = br.readLine()) != null)
        {

            System.out.println(strLine);

        }

        br.close();

    }



    public static void main(String[] args) throws IOException {

        InfectStatistic in = new InfectStatistic();

        String log_name = in.get_name()+".txt";

        boolean find = in.find_log_name(log_name);

        if(find == true)
        {

            in.process_log(log_name);

        }
        
    }
}
