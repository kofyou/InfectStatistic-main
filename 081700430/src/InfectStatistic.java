import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * InfectStatistic
 * TODO
 *
 * @author PSF
 * @email 52260506@qq.com
 * @version 1.0
 * @since 2020/2/9
 */
class InfectStatistic {
    private String log, out;
    private String [] type, province;
    private Date date;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //用于定义日期格式，方便进行对比。
    private final int typeNum = 4, provinceNum = 34;          //用于规定选项的可使用参数数量。
    private int[] internal = new int[4];                      //用于统计全国的情况， 避免多次查找。
    private Map<String, Integer[]> status = new HashMap<>(); //用于统计每个省份的情况。
    private String[] provinces = new String[]{"安徽", "北京" ,"重庆" ,"福建" ,"甘肃" ,"广东" ,"广西" ,"贵州" ,"海南" ,
                 "河北" ,"河南" ,"黑龙江" ,"湖北" ,"湖南" ,"吉林" ,"江苏" ,"江西" ,"辽宁" ,"内蒙古" ,"宁夏" ,"青海" ,
                 "山东" ,"山西" ,"陕西" ,"上海" ,"四川" ,"天津" ,"西藏" ,"新疆" ,"云南" ,"浙江"};

    private void init(String[] args) {
        type = new String[typeNum];
        province = new String[provinceNum];
        if (args.length > 0 && args[0].equals("list")) {
            for (int i = 1, typeI = 0, provinceI = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-log" :
                        this.log = args[++i];
                        break;
                    case "-out" :
                        this.out = args[++i];
                        break;
                    case "-date" :
                        try {
                            this.date = sdf.parse(args[++i]);
                        }
                        catch (ParseException pE) {
                            System.out.println("请输入正确的日期！");
                            System.exit(-1);
                        }
                        break;
                    case "-type" :
                        i++;
                        while (args[i] != null && args[i].charAt(0) != '-') {
                            if (typeI < typeNum) {
                                this.type[typeI++] = args[i++];
                            }
                        }
                        break;
                    case "-province" :
                        i++;
                        while (args[i] != null && args[i].charAt(0) != '-') {
                            if (provinceI < provinceNum) {
                                this.province[provinceI++] = args[i++];
                            }
                        }
                        break;
                }
            }
        }
        else {
            System.out.println("请输入正确的命令和选项");
        }
        for (String str : provinces) {
            status.put(str, new Integer[]{0, 0, 0, 0});
        }

    }

    private void dealLogs() {
        System.out.println(log + " ");/* dbg:调试查看读取文件是否正确 */
        File file = new File(log);
        if (file.isDirectory() && file.list() != null) {
            File[] files = file.listFiles();
            if (files != null)
            for (File aFile : files) {
                String[] splitName = aFile.getName().split("\\.");
                if (aFile.isFile() && splitName.length > 2 && splitName[1].equals("log")) {
                  //  System.out.println(aFile.getName() + " " + splitName[0] + " " +splitName[1]+" "+splitName[2]);/* dbg:调试查看读取文件是否正确 */
                    if (date != null) {
                        try {
                            Date fileDate = sdf.parse(splitName[0]);
                            if (fileDate.before(date) || fileDate.equals(date)) {
                                //System.out.println(aFile.getName() + " " + splitName[0] + " " +splitName[1]+" "+splitName[2]);
                                caculateLogs(aFile);
                            }
                        }
                        catch (ParseException pE) {
                        }
                    }
                    else {
                        //System.out.println(aFile.getName() + " " + splitName[0] + " " +splitName[1]+" "+splitName[2]);
                        caculateLogs(aFile);
                    }
                }
            }
        }
    }

    public void caculateLogs(File file) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String dataLine;
            while ((dataLine = br.readLine()) != null) {

            }
        }
        catch (IOException ioE) {

        }
    }

    public void outFile() {
        File file = new File(out);
        try {
            if (file.exists() && file.isFile()) {

            }
        }
        catch (Exception e) {
        }

    }

    private void showPara() {
        System.out.println(log+" "+out+" "+date);
        for (int i = 0; i < typeNum; i++) {

        }
    }

    public static void main(String[] args) {
        InfectStatistic infect = new InfectStatistic();
        infect.init(args);
        infect.dealLogs();
    }

}
