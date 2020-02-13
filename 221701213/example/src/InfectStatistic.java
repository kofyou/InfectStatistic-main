import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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

    public int get_infected ()
    {

        return this.infected;

    }

    public void set_suspected (int sum)
    {

        this.suspected = sum;

    }

    public int get_suspected ()
    {

        return this.suspected;

    }

    public void set_cure (int sum)
    {

        this.cure = sum;

    }

    public int get_cure ()
    {

        return this.cure;

    }

    public void set_death (int sum)
    {

        this.death = sum;

    }

    public int get_death ()
    {

        return this.death;

    }

    public void set_pro_name (String str)
    {

        this.pro_name = str;

    }

    public String get_pro_name ()
    {

        return this.pro_name;

    }

}

public class InfectStatistic {

    public void init_province(province[] pro , String[] str) //初始化各个省情况
    {

        for(int i = 0 ;i < str.length ;i++)
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

    public int get_num(String str)  //提取字符串中的数字
    {

        str=str.trim();

        String str2 = new String();

        for(int i = 0 ;i < str.length() ;i++)
        {

            if(str.charAt(i) >= '0' && str.charAt(i) <= '9')
            {

                str2 += str.charAt(i);

            }
        
        }
        
        return Integer.parseInt(str2);

    }

    public void process_string(String strLine , province[] pro)  //处理字符串
    {

        String [] spString = strLine.split("\\s+");

        if(spString[0].equals("//"))    //如果以“//”开头表示是注释，则忽略
        {

            return; 

        }

        for(int i = 0 ;i < pro.length ;i++)
        {

            if(spString[0].equals(pro[i].get_pro_name()))
            {

                if(spString[1].equals("新增"))
                {

                    if(spString[2].equals("感染患者"))
                    {

                        int add = this.get_num(spString[3]);

                        int new_infected = pro[i].get_infected() + add;

                        pro[i].set_infected(new_infected);

                    }

                    else if(spString[2].equals("疑似患者"))
                    {

                        int add = this.get_num(spString[3]);

                        int new_suspected = pro[i].get_suspected() + add;

                        pro[i].set_suspected(new_suspected);

                    }

                    break;

                }

                else if(spString[1].equals("死亡"))
                {

                    int add = this.get_num(spString[2]);

                    int new_infected = pro[i].get_infected() - add;

                    int new_death = pro[i].get_death() + add;

                    pro[i].set_infected(new_infected);

                    pro[i].set_death(new_death);

                    break;

                }

                else if(spString[1].equals("治愈"))
                {

                    int add = this.get_num(spString[2]);

                    int new_infected = pro[i].get_infected() - add;

                    int new_cure = pro[i].get_cure() + add;

                    pro[i].set_infected(new_infected);

                    pro[i].set_cure(new_cure);

                    break;

                }

                else if(spString[1].equals("疑似患者"))
                {

                    if(spString[2].equals("确诊感染"))
                    {

                        int add = this.get_num(spString[3]);

                        int new_infected = pro[i].get_infected() + add;

                        int new_suspected = pro[i].get_suspected() - add;

                        pro[i].set_infected(new_infected);

                        pro[i].set_suspected(new_suspected);

                    }

                    else if(spString[2].equals("流入"))
                    {

                        for(int x = 0 ; x <pro.length ; x++)
                        {

                            if(spString[3].equals(pro[x].get_pro_name()))
                            {

                                int add = this.get_num(spString[4]);

                                int new_suspected_i = pro[i].get_suspected() - add;

                                int new_suspected_x = pro[x].get_suspected() + add;

                                pro[i].set_suspected(new_suspected_i);

                                pro[x].set_suspected(new_suspected_x);

                                break;

                            }
                        
                        }
                    }

                    break;

                }

                else if(spString[1].equals("排除"))
                {

                    if(spString[2].equals("疑似患者"))
                    {

                        int add = this.get_num(spString[3]);

                        int new_suspected = pro[i].get_suspected() - add;

                        pro[i].set_suspected(new_suspected);

                    }

                    break;

                }
                else if(spString[1].equals("感染患者"))
                {

                    if(spString[2].equals("流入"))
                    {

                        for(int x = 0 ; x < pro.length ; x++)
                        {

                            if(spString[3].equals(pro[x].get_pro_name()))
                            {

                                int add = this.get_num(spString[4]);
                                
                                int new_infected_i = pro[i].get_infected() - add;
                                
                                int new_infected_x = pro[x].get_infected() + add;

                                pro[i].set_infected(new_infected_i);

                                pro[x].set_infected(new_infected_x);

                                break;

                            }
                        }
                    }

                    break;

                }
                
            }

        }
    }

    public void process_country(province[] pro) // 统计全国疫情感染情况
    {

        for(int i = 0 ;i < pro.length ;i++)
        {

            if(pro[i].get_cure() != 0 ||pro[i].get_death() != 0 ||pro[i].get_infected() != 0 ||pro[i].get_suspected() != 0)
            {

                pro[0].set_infected(pro[0].get_infected()+pro[i].get_infected());
                pro[0].set_suspected(pro[0].get_suspected()+pro[i].get_suspected());
                pro[0].set_cure(pro[0].get_cure()+pro[i].get_cure());
                pro[0].set_death(pro[0].get_death()+pro[i].get_death());

            }

        }

    }

    public void process_log(String log_name , province[] pro) throws IOException // 读取日志并统计日志内疫情情况
    {

        String result = new String ("D:\\221701213\\example\\log\\"+log_name);

        FileInputStream fstream = new FileInputStream(new File(result));

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream,"UTF-8"));

        String strLine;

        while((strLine = br.readLine()) != null)
        {

            this.process_string(strLine , pro); // 处理日志中每一行的疫情统计情况

        }

        this.process_country(pro);  // 统计全国疫情情况

        br.close();

    }

    public void output(province[] pro) throws IOException   // 将统计结果输出到output.txt
    {

        String path = new String("D:\\221701213\\example\\result\\output.txt");

        FileWriter fwriter = new FileWriter(path);

        for(int i = 0 ; i < pro.length ; i++)
        {

            String word = new String();

            if(pro[i].get_cure() != 0 || pro[i].get_death() != 0 || pro[i].get_infected() != 0 || pro[i].get_suspected() != 0)
            {

                word += pro[i].get_pro_name() + " 感染患者" + pro[i].get_infected() + " 疑似患者" + 
                pro[i].get_suspected() + " 治愈" + pro[i].get_cure() + " 死亡" + pro[i].get_death() + "\n";

                fwriter.write(word);

                word = "";

            }

        }

        fwriter.flush();

        fwriter.close();

    }

    public static void main(String[] args) throws IOException {

        InfectStatistic in = new InfectStatistic();

        String[] province_name = new String[] {"全国","安徽","福建","甘肃","广东","广西",
        "贵州","海南","河北","河南","黑龙江","湖北","湖南","江西","吉林","江苏","辽宁","内蒙古",
        "宁夏","青海","山西","山东","陕西","四川","西藏","新疆","云南","浙江"};

        province[] pro;

        pro = new province[province_name.length];

        in.init_province(pro , province_name);

        String log_name = in.get_name()+".txt";

        boolean find = in.find_log_name(log_name);

        if(find == true)
        {

            in.process_log(log_name , pro);

            in.output(pro);

        }

        in.print(pro);
        
    }
}
