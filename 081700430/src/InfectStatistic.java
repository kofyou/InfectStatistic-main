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
 * @issue 1 xxx日期超出日志最大时间暂时没有报错，把目前的全部输出，后面需要添加约束
 */
class InfectStatistic {
    private String log, out;
    private String [] type, province;
    private Date date;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");         //用于定义日期格式，方便进行对比。
    private final int typeNum = 4, provinceNum = 34, ip = 0, sp = 1, cure = 2, dead = 3; //用于规定选项的可使用参数数量。
    private int[] internal = new int[4];                                                //用于统计全国的情况， 避免多次查找。感染 疑似 治愈 死亡
    private Map<String, int[]> status = new HashMap<>();                                //用于统计每个省份的情况。
    private String[] provinces = new String[]{"安徽", "北京" ,"重庆" ,"福建" ,"甘肃" ,"广东" ,"广西" ,"贵州" ,"海南" ,
                 "河北" ,"河南" ,"黑龙江" ,"湖北" ,"湖南" ,"吉林" ,"江苏" ,"江西" ,"辽宁" ,"内蒙古" ,"宁夏" ,"青海" ,
                 "山东" ,"山西" ,"陕西" ,"上海" ,"四川" ,"天津" ,"西藏" ,"新疆" ,"云南" ,"浙江"};
    private int solutions;                                                               //0: 无指定；1：指定省份；2：指定类型；3：都指定

    private void init(String[] args) {
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
                        type = new String[typeNum];
                        i++;
                        while (i < args.length && args[i].charAt(0) != '-') {
                            if (typeI < typeNum) {
                                this.type[typeI++] = args[i++];
                            }
                        }
                        i--;
                        break;
                    case "-province" :
                        province = new String[provinceNum];
                        i++;
                        while (i < args.length && args[i].charAt(0) != '-') {
                            if (provinceI < provinceNum) {
                                this.province[provinceI++] = args[i++];
                            }
                        }
                        i--;
                        break;
                }
            }

            for (String str : provinces) {
                status.put(str, new int[]{0, 0, 0, 0});
            }

            if (province == null && type == null) {
                solutions = 0;
            }
            else if (province == null && type != null) {
                solutions = 2;
            }
            else if (province != null && type == null) {
                solutions = 1;
            }
            else {
                solutions = 3;
            }
        }
        else {
            System.out.println("请输入正确的命令和选项");
        }
    }

    private void dealLogs() {
//        System.out.println("type:"+type[0]+" "+type[1]+" "+type[2]);
  //      System.out.println("province:"+province[0]+" "+province[1]+" "+province[2]);
   //     System.out.println(log + " " + out);/* dbg:调试查看读取文件是否正确 */
        File file = new File(log);
        if (file.isDirectory() && file.list() != null) {
            File[] files = file.listFiles();
            if (files != null)
            for (File aFile : files) {
                String[] splitName = aFile.getName().split("\\.");
                if (aFile.isFile() && splitName.length > 2 && splitName[1].equals("log")) {
                    //System.out.println(aFile.getName() + " " + splitName[0] + " " +splitName[1]+" "+splitName[2]);/* dbg:调试查看读取文件是否正确 */
                    if (date != null) {
                        try {
                            Date fileDate = sdf.parse(splitName[0]);
                            if (fileDate.before(date) || fileDate.equals(date)) {
                              //  System.out.println(aFile.getName() + " " + splitName[0] + " " +splitName[1]+" "+splitName[2]);
                                caculateLogs(aFile, solutions);
                            }
                        }
                        catch (ParseException pE) {
                        }
                    }
                    else {
                        //System.out.println(aFile.getName() + " " + splitName[0] + " " +splitName[1]+" "+splitName[2]);
                        caculateLogs(aFile, solutions);
                    }
                }
            }
        }
    }

    public void caculateLogs(File file, int solutions) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String dataLine;
            if (solutions == 0) {
                while ((dataLine = br.readLine()) != null) {
                    String[] words = dataLine.split("\\s+");
                    if (status.containsKey(words[0])) {
                        int[] tempStatus = status.get(words[0]);
                        if (words.length == 3) {
                            int num = Integer.parseInt(words[2].substring(0, words[2].indexOf("人")));
                            if (words[1].equals("死亡") && num > 0) {
                                tempStatus[ip] -= num;
                                tempStatus[dead] += num;
                                internal[ip] -= num;
                                internal[dead] += num;
                                status.put(words[0],tempStatus);
                            }
                            else if (words[1].equals("治愈") && num > 0) {
                                tempStatus[ip] -= num;
                                tempStatus[cure] += num;
                                internal[ip] -= num;
                                internal[cure] += num;
                                status.put(words[0],tempStatus);
                            }
                        }
                        else if (words.length == 4) {
                            int num = Integer.parseInt(words[3].substring(0, words[3].indexOf("人")));
                            if (words[1].equals("新增") && num > 0) {
                                if (words[2].equals("感染患者")) {
                                    tempStatus[ip] += num;
                                    internal[ip] += num;
                                    status.put(words[0],tempStatus);
                                }
                                else if (words[2].equals("疑似患者")) {
                                    tempStatus[sp] += num;
                                    internal[sp] += num;
                                    status.put(words[0],tempStatus);
                                }
                            }
                            else if (words[1].equals("疑似患者") && num > 0) {
                                tempStatus[sp] -= num;
                                tempStatus[ip] += num;
                                internal[sp] -= num;
                                internal[ip] += num;
                                status.put(words[0],tempStatus);
                            }
                            else if (words[1].equals("排除") && num > 0) {
                                tempStatus[sp] -= num;
                                internal[sp] -= num;
                                status.put(words[0],tempStatus);
                            }
                        }
                        else if (words.length == 5) {
                            int num = Integer.parseInt(words[4].substring(0, words[4].indexOf("人")));
                            if (words[1].equals("感染患者") && num > 0) {
                                tempStatus[ip] -= num;
                                status.put(words[0],tempStatus);
                                int[] tempStatus2 = status.get(words[3]);
                                tempStatus2[ip] += num;
                                status.put(words[3],tempStatus);
                            }
                            else if (words[1].equals("疑似患者") && num > 0) {
                                tempStatus[sp] -= num;
                                status.put(words[0],tempStatus);
                                int[] tempStatus2 = status.get(words[3]);
                                tempStatus2[sp] += num;
                                status.put(words[3],tempStatus);
                            }
                        }
                    }
                }
                br.close();
            }
            else if (solutions == 1) {
                while ((dataLine = br.readLine()) != null) {

                }
                br.close();
            }
            else if (solutions == 2) {
                while ((dataLine = br.readLine()) != null) {

                }
                br.close();
            }
            else if (solutions == 3){
                while ((dataLine = br.readLine()) != null) {

                }
                br.close();
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
