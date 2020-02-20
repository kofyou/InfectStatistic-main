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
 * @notes 1 vvv solutions 放在输出文件的时候处理，而不是每次查询文件的每行都进行判断，
 *            有效加快速度。及把所有数据都统计起来，到输出的时候再依据情况输出。
 */
class InfectStatistic {
    private String log, out;
    private int[] type;
    private String [] province;
    private Date date;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");         //用于定义日期格式，方便进行对比。
    private final int typeNum = 4, provinceNum = 34, ip = 0, sp = 1, cure = 2, dead = 3; //用于规定选项的可使用参数数量。
    private int[] internal = new int[typeNum];                                           //用于统计全国的情况， 避免多次查找。感染 疑似 治愈 死亡
    private Map<String, int[]> status = new HashMap<>();                                //用于统计每个省份的情况。
    private String[] provinces = new String[]{"全国", "安徽", "北京" ,"重庆" ,"福建" ,"甘肃" ,"广东" ,"广西" ,"贵州" ,"海南" ,
                 "河北" ,"河南" ,"黑龙江" ,"湖北" ,"湖南" ,"吉林" ,"江苏" ,"江西" ,"辽宁" ,"内蒙古" ,"宁夏" ,"青海" ,
                 "山东" ,"山西" ,"陕西" ,"上海" ,"四川" ,"天津" ,"西藏" ,"新疆" ,"云南" ,"浙江"};
    private int solutions;                                                               //0: 无指定；1：指定省份；2：指定类型；3：都指定

    private void init(String[] args) {
        if (args.length > 0 && args[0].equals("list")) {
            for (int i = 1, typeI = 0, provinceI = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-log" :
                        if (i + 1 < args.length) {
                            this.log = args[++i];
                        }
                        break;
                    case "-out" :
                        if (i + 1 < args.length) {
                            this.out = args[++i];
                        }
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
                        type = new int[]{-1, -1, -1, -1};
                        i++;
                        while (i < args.length && args[i].charAt(0) != '-') {
                            if (typeI < typeNum) {
                                if (args[i].equals("ip")) {
                                    this.type[typeI++] = ip;
                                }
                                else if (args[i].equals("sp")) {
                                    this.type[typeI++] = sp;
                                }
                                else if (args[i].equals("cure")) {
                                    this.type[typeI++] = cure;
                                }
                                else if (args[i].equals("dead")) {
                                    this.type[typeI++] = dead;
                                }
                            }
                            i++;
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
        }
        else {
            System.out.println("请输入正确的命令和选项");
        }
    }

    private void dealLogs() {
       /* int k = 0;
        while (province[k] != null) {
            System.out.println(province[k++]);
        } */
     //   System.out.println(log + " " + out);/* dbg:调试查看读取文件是否正确 */
        if (log == null) {
            System.out.println("请输入正确的日志地址！");
            System.exit(-1);
        }
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
                            //    System.out.println(aFile.getName() + " " + splitName[0] + " " +splitName[1]+" "+splitName[2]);
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
        else {
            System.out.println("请输入正确的日志地址！");
            System.exit(-1);
        }
    }

    private void caculateLogs(File file) {
        try {
            //System.out.println("a1:");  //xx:test
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String dataLine;
            while ((dataLine = br.readLine()) != null) {
                //System.out.println("a2:");  //xx:test
                String[] words = dataLine.split("\\s+");
                //System.out.println("test words:"+words[0]);  //xx:test
                if (status.containsKey(words[0])) {
                    int[] tempStatus = status.get(words[0]);
                    //System.out.println(words[0]+":" + tempStatus[ip] + " "+tempStatus[sp]+" "+
                    //        tempStatus[cure]+" "+tempStatus[dead]);  //xx:test
                   // System.out.println("test words:"+words[0]+" "+words[1]+" "+words[2]);
                    if (words.length == 3) {
                        int num = Integer.parseInt(words[2].substring(0, words[2].indexOf("人")));
                        if (words[1].equals("死亡") && num > 0) {
                            tempStatus[ip] -= num;
                            tempStatus[dead] += num;
                            internal[ip] -= num;
                            internal[dead] += num;
                            status.put(words[0], tempStatus);
                        }
                        else if (words[1].equals("治愈") && num > 0) {
                            tempStatus[ip] -= num;
                            tempStatus[cure] += num;
                            internal[ip] -= num;
                            internal[cure] += num;
                            status.put(words[0], tempStatus);
                        }
                        //System.out.println("num:"+num);
                        //System.out.println(words[0]+":" + tempStatus[ip] + " "+tempStatus[sp]+" "+
                          //      tempStatus[cure]+" "+tempStatus[dead]);  //xx:test
                       // System.out.println("test num:"+num+" inter: " + internal[ip] + " "+internal[sp]+" "+
                        //        internal[cure]+" "+internal[dead]);  //xx:test
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
                        //System.out.println("num:"+num);
                      //  System.out.println(words[0]+":" + tempStatus[ip] + " "+tempStatus[sp]+" "+
                        //        tempStatus[cure]+" "+tempStatus[dead]);  //xx:test
                       // System.out.println("test num:"+num+" inter: " + internal[ip] + " "+internal[sp]+" "+
                        //        internal[cure]+" "+internal[dead]);  //xx:test
                    }
                    else if (words.length == 5) {
                        int num = Integer.parseInt(words[4].substring(0, words[4].indexOf("人")));
                        if (words[1].equals("感染患者") && num > 0) {
                            tempStatus[ip] -= num;
                            status.put(words[0],tempStatus);
                            int[] tempStatus2 = status.get(words[3]);
                            tempStatus2[ip] += num;
                            status.put(words[3],tempStatus2);
                        }
                        else if (words[1].equals("疑似患者") && num > 0) {
                            tempStatus[sp] -= num;
                            status.put(words[0],tempStatus);
                            int[] tempStatus2 = status.get(words[3]);
                            tempStatus2[sp] += num;
                            status.put(words[3],tempStatus2);
                        }
                       // System.out.println("num:"+num);
                       // System.out.println(words[0]+":" + tempStatus[ip] + " "+tempStatus[sp]+" "+
                         //       tempStatus[cure]+" "+tempStatus[dead]);  //xx:test

                       // System.out.println("test num:"+num+" inter: " + internal[ip] + " "+internal[sp]+" "+
                        //        internal[cure]+" "+internal[dead]);  //xx:test
                    }
                }
            }
            br.close();
        }
        catch (IOException ioE) {
            System.out.println("日志统计出错！");
            System.exit(-1);
        }
    }

    private void outFile() {
        try {
            if (out == null) {
                System.out.println("请输入正确的输出地址！");
                System.exit(-1);
            }
            File file = new File(out);
            status.put("全国", internal);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            String dataLine;
            if (province == null && type == null) {
               // System.out.println("arrive solition0");
                for (String provin : provinces) {
                    int[] tempStatus = status.get(provin);
                    dataLine = String.format("%s 感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人\n", provin,
                            tempStatus[ip], tempStatus[sp], tempStatus[cure], tempStatus[dead]);
                    bw.write(dataLine);
                }
                bw.close();
            }
            else if (province != null && type == null) {
                //System.out.println("arrive solition1");
                int i = 0;
                while (province[i] != null) {
                 //   System.out.println("test p:"+province[i]); //xxx
                    if (status.containsKey(province[i])) {
                        int[] tempStatus = status.get(province[i]);
                        dataLine = String.format("%s 感染患者%d人 疑似患者%d人 治愈%d人 死亡%d人\n", province[i],
                                tempStatus[ip], tempStatus[sp], tempStatus[cure], tempStatus[dead]);
                        bw.write(dataLine);
                    }
                    i++;
                }
               // System.out.println("test arrive here?"); //xxx
                bw.close();
            }
            else if (province == null) {
                int needTypes = 0;
                for (int tempType : type) {
                    if (tempType >= 0) {
                        needTypes++;
                    }
                }
               // System.out.println("arrive solition2");
                for (String provin : provinces) {
                    int[] tempStatus = status.get(provin);
                    String[] intToSring = new String[]{"感染患者" + tempStatus[ip] + "人", "疑似患者" + tempStatus[sp]
                            + "人", "治愈" + tempStatus[cure] + "人", "死亡" + tempStatus[dead] + "人"};
                    if (needTypes == 1) {
                        dataLine = String.format("%s %s\n", provin, intToSring[type[0]]);
                    }
                    else if (needTypes == 2) {
                        dataLine = String.format("%s %s %s\n", provin, intToSring[type[0]], intToSring[type[1]]);
                    }
                    else if (needTypes == 3) {
                        dataLine = String.format("%s %s %s %s\n", provin, intToSring[type[0]], intToSring[type[1]],
                                intToSring[type[2]]);
                    }
                    else if (needTypes == 4) {
                        dataLine = String.format("%s %s %s %s %s\n", provin, intToSring[type[0]], intToSring[type[1]],
                                intToSring[type[2]], intToSring[type[3]]);
                    }
                    else {
                        dataLine = "";
                    }
                   // System.out.println(dataLine);
                    bw.write(dataLine);
                }
                bw.close();
            }
            else {
                int needTypes = 0;
                for (int tempType : type) {
                    if (tempType >= 0) {
                        needTypes++;
                    }
                }
               // System.out.println("arrive solition3");
                int i = 0;
                while (province[i] != null) {
                    //System.out.println("test p:"+province[i]); //xxx
                    if (status.containsKey(province[i])) {
                        int[] tempStatus = status.get(province[i]);
                        String[] intToSring = new String[]{"感染患者" + tempStatus[ip] + "人", "疑似患者" + tempStatus[sp]
                                + "人", "治愈" + tempStatus[cure] + "人", "死亡" + tempStatus[dead] + "人"};
                        if (needTypes == 1) {
                            dataLine = String.format("%s %s\n", province[i], intToSring[type[0]]);
                        }
                        else if (needTypes == 2) {
                            dataLine = String.format("%s %s %s\n", province[i], intToSring[type[0]], intToSring[type[1]]);
                        }
                        else if (needTypes == 3) {
                            dataLine = String.format("%s %s %s %s\n", province[i], intToSring[type[0]], intToSring[type[1]],
                                    intToSring[type[2]]);
                        }
                        else if (needTypes == 4) {
                            dataLine = String.format("%s %s %s %s %s\n", province[i], intToSring[type[0]], intToSring[type[1]],
                                    intToSring[type[2]], intToSring[type[3]]);
                        }
                        else {
                            dataLine = "";
                        }
                        bw.write(dataLine);
                    }
                    i++;
                   // System.out.println("test data:"+dataLine); //xxx
                }
                bw.close();
            }
        }
        catch (Exception e) {
            System.out.println("请输入正确的输出地址！");
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        InfectStatistic infect = new InfectStatistic();
        infect.init(args);
        infect.dealLogs();
        infect.outFile();
    }

}
