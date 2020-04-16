//package Yiqing;
import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * InfectStatistic
 * TODO
 *
 * @author zdc5511116_221701222
 * @version 1.0
 * @since 2020.02
 */
class InfectStatistic {

    Order order;

    public static void main(String[] args) {
        InfectStatistic infectStatistic = new InfectStatistic();
        try {
            if(args.length == 0){
                Lib.help();
                throw new Lib.Exit("请按照提示输入命令");
            }
            infectStatistic.takeOrder(args[0]);
            infectStatistic.placeOrder(args);
        }catch (Lib.Exit exit){
            System.out.println(exit.getMessage());
        }
    }

    /**
     * 判断命令类型并创建实例
     * @param cmd 传入的命令
     */
    public void takeOrder(String cmd) throws Lib.Exit {
        switch (cmd){
            case "list":
                order = new ListOrder();
                break;
            default:
                throw new Lib.Exit("\"" + cmd + "\"无效的命令");
        }
    }

    /**
     * 执行命令
     * @param args 参数列表
     */
    public void placeOrder(String[] args) throws Lib.Exit {
        order.execute(args);
    }

}

/**
 * 命令接口
 */
interface Order {

    void execute(String[] args) throws Lib.Exit;
}

/**
 * list命令类
 */
class ListOrder implements Order {

    /*一些常量*/
    private final String INFECTION_PATIENT = "感染患者";
    private final String SUSPECTED_PATIENT = "疑似患者";
    private final String DIAGNOSE = "确诊感染";
    private final String CURE = "治愈";
    private final String DEAD = "死亡";
    private final String INCREMENT = "新增";
    private final String EXCLUDE = "排除";
    private final String INFLOW = "流入";

    /*以下成员变量用于判断是否传入了相应参数*/
    private boolean hasLog;
    private boolean hasOut;
    private boolean hasDate;
    private boolean hasType;
    private boolean hasProvince;

    /*以下成员变量用于保存具体参数的参数值*/
    private String logParam;
    private String outParam;
    private String dateParam;
    private List<String> typeParams;

    private List<String> provinceParams;

    /*用于保存各省份疫情信息*/
    private final Map<String, List<Integer>> statistics;

    /*日志目录*/
    private File logDirectory;

    /*输出哪些类型数据和输出哪些省份*/
    private Map<String,Integer> outType;
    private List<String> outProvince;

    /*构造方法*/
    public ListOrder() {
        hasLog = false;
        hasOut = false;
        hasDate = false;
        hasType = false;
        hasProvince = false;
        typeParams = new ArrayList<>();
        provinceParams = new ArrayList<>();
        statistics = new LinkedHashMap<>();
        outType = new LinkedHashMap<>();   //默认输出全部类型数据
        outType.put(INFECTION_PATIENT,0);
        outType.put(SUSPECTED_PATIENT,1);
        outType.put(CURE,2);
        outType.put(DEAD,3);
        outProvince = new ArrayList<>();
        outProvince.add("全国");
        Lib.mapInit(statistics);
    }

    /**
     *解析参数，保存，设定默认值，并调用相应的方法
     * @param args 传递给main方法的参数
     */
    @Override
    public void execute(String[] args) throws Lib.Exit {
        if(args.length == 1){
            Lib.helpList();    //显示提示信息
            throw new Lib.Exit("请按照提示输入命令");
        }
        /*分离参数*/
        int i = 1;
        while (i < args.length) {
            switch (args[i]) {
                case "-log":
                    hasLog = true;
                    if (++i >= args.length) {  //如果-log后面没有给参数值
                        throw new Lib.Exit("-log参数缺少参数值");
                    }
                    logParam = args[i++];      //-log后面跟着的参数为-log的参数值
                    break;
                case "-out":
                    hasOut = true;
                    if (++i >= args.length) {  //如果-out后面没有给参数值
                        throw new Lib.Exit("-out参数缺少参数值");
                    }
                    outParam = args[i++];      //-out后面跟着的参数为-out的参数值
                    break;
                case "-date":
                    hasDate = true;
                    if (++i >= args.length) {  //如果-date后面没有给参数值
                        throw new Lib.Exit("-date参数缺少参数值");
                    }
                    dateParam = args[i++];     //-date后面跟着的参数为-date的参数值
                    break;
                case "-type":
                    hasType = true;
                    while (++i < args.length && !args[i].equals("-log") && !args[i].equals("-out") && !args[i].equals("-date")
                            && !args[i].equals("-province")) {   //-type的参数值范围
                        typeParams.add(args[i]);
                    }
                    break;
                case "-province":
                    hasProvince = true;
                    while (++i < args.length && !args[i].equals("-log") && !args[i].equals("-out") && !args[i].equals("-date")
                            && !args[i].equals("-type")) {       //-province的参数值范围
                        provinceParams.add(args[i]);
                    }
                    break;
                default:
                    throw new Lib.Exit("\"" + args[i] + "\"无法解析的参数");
            }
        }
        /*执行相应的方法*/
        if(!hasLog) {  //log必须有
            throw new Lib.Exit("缺少-log参数");
        }
        if(!hasOut) {  //out必须有
            throw new Lib.Exit("缺少-out参数");
        }
        if(!hasDate) {  //如果没有data参数
            dateParam=new SimpleDateFormat("yyyy-MM-dd").format(new Date()); //当前日期
        }
        doLog(logParam);    //读取日志路径
        doDate(dateParam);   //读取日志路径下相应日期的日志
        if(hasType) {
            doType(typeParams);   //需要输出的信息类型
        }
        if(hasProvince) {
            doProvince(provinceParams);   //需要输出的省份疫情信息
        }
        doOut(outParam);  //输出到指定的路径
    }

    /**
     *执行-log命令参数 读取log文件夹
     * @param logPath -log参数后面的log文件路径
     */
    private void doLog(String logPath) throws Lib.Exit {
        logDirectory = new File(logPath);  //读取路径
        if(!logDirectory.exists()) {
            throw new Lib.Exit("\"-log\" " + logDirectory + " 无法解析的路径");
        }
    }

    /**
     *执行-date命令参数 计算当日疫情状况
     * @param date -date参数后面的具体日期
     */
    private void doDate(String date) throws Lib.Exit {
        List<File> logList = Lib.getLogFiles(logDirectory);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date paramDate;
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {
            paramDate = dateFormat.parse(date);
            for (File log : logList) {
                Date logDate = dateFormat.parse(log.getName().substring(0, log.getName().indexOf('.')));
                if(logDate.compareTo(paramDate) > 0) {  //判断日志文件的日期是否小于等于给定日期
                    continue;
                }
                threadPool.submit(() -> {
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new InputStreamReader(new FileInputStream(log), StandardCharsets.UTF_8));
                        String dataRow;
                        while ((dataRow = reader.readLine()) != null) {
                            if (dataRow.startsWith("//")) { //忽略注释行
                                continue;
                            }
                            String[] data = dataRow.split(" ");  //分割数据行
                            if (!outProvince.contains(data[0])) {
                                outProvince.add(data[0]);
                            }
                            synchronized (statistics) {   //给数据加上锁
                                List<Integer> provinceData = statistics.get(data[0]);   //当前行的省份数据
                                List<Integer> destProvince;   //用于处理流入
                                switch (data[1]) {
                                    case INCREMENT:  //处理新增
                                        if (data[2].equals(INFECTION_PATIENT)) {  //新增感染
                                            increaseInf(provinceData, Lib.parseData(data[3]));
                                        } else {                                  //新增疑似
                                            increaseSus(provinceData, Lib.parseData(data[3]));
                                        }
                                        break;
                                    case EXCLUDE:  //处理排除疑似
                                        excludeSus(provinceData, Lib.parseData(data[3]));
                                        break;
                                    case CURE:  //处理治愈
                                        cure(provinceData, Lib.parseData(data[2]));
                                        break;
                                    case DEAD:  //处理死亡
                                        dead(provinceData, Lib.parseData(data[2]));
                                        break;
                                    case INFECTION_PATIENT:  //处理感染患者流入
                                        destProvince = statistics.get(data[3]);
                                        infInflow(provinceData, destProvince, Lib.parseData(data[4]));
                                        break;
                                    case SUSPECTED_PATIENT:
                                        if (data[2].equals(INFLOW)) {   //处理疑似患者流入
                                            destProvince = statistics.get(data[3]);
                                            susInflow(provinceData, destProvince, Lib.parseData(data[4]));
                                        } else if (data[2].equals(DIAGNOSE)) {  //处理确诊
                                            diagnose(provinceData, Lib.parseData(data[3]));
                                        }
                                        break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if(reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            threadPool.shutdown();
            while (!threadPool.isTerminated());   //等待所有线程执行完
        } catch (Exception e) {
            throw new Lib.Exit(e.getMessage());
        }
    }
    /**
     *执行-out命令参数
     * @param outPath -out参数后面的输出路径
     */
    private void doOut(String outPath) throws Lib.Exit {
        countNational(); //计算全国数据
        File outFile = new File(outPath);
        FileWriter writer = null;    //字符输出流
        try {
            writer = new FileWriter(outFile);
            for(String province : statistics.keySet()) {   //遍历统计数据
                if(!outProvince.contains(province)) {
                    continue;
                }
                List<Integer> data = statistics.get(province);
                writer.write(province + "    ");
                for(String type : outType.keySet()) {
                    writer.write(type + data.get(outType.get(type)) + "人    ");
                }
                writer.write("\n");
            }
            writer.write("//该文档并非真实数据，仅供测试使用\n");
            writer.flush();
        } catch (Exception e) {
            throw new Lib.Exit("\"out\" " + e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();   //关闭流
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

 

    /**
     *执行-type命令参数
     * @param types -type命令参数后面的具体参数值数组
     */
    private void doType(List<String> types) throws Lib.Exit {
        Map<String,Integer> newOutType = new LinkedHashMap<>();
        for (String key : statistics.keySet()) {
            List<Integer> oldData = statistics.get(key);
            List<Integer> newData = new ArrayList<>();
            int index = 0;
            for(String type : types) {
                switch (type) {
                    case "ip":
                        newData.add(oldData.get(0));
                        newOutType.put(INFECTION_PATIENT,index++);
                        break;
                    case "sp":
                        newData.add(oldData.get(1));
                        newOutType.put(SUSPECTED_PATIENT,index++);
                        break;
                    case "cure":
                        newData.add(oldData.get(2));
                        newOutType.put(CURE,index++);
                        break;
                    case "dead":
                        newData.add(oldData.get(3));
                        newOutType.put(DEAD,index++);
                        break;
                    default:
                        throw new Lib.Exit("\"-type\" 无法解析的类型 " + type);
                }
            }
            outType = newOutType;
            statistics.put(key,newData);
        }
    }

    /**
     *执行-province命令参数
     * @param provinces -province命令参数后面的具体参数值数组
     */
    private void doProvince(List<String> provinces) {
        outProvince = provinces;
    }

    /**
     *新增确诊患者的计算
     * @param provinceData 当前省份疫情
     * @param count 新增数量
     */
    private void increaseInf(List<Integer> provinceData,int count) {
        provinceData.set(0,provinceData.get(0) + count);
    }

    /**
     *新增疑似患者的计算
     * @param provinceData 当前省份疫情
     * @param count 新增人数
     */
    private void increaseSus(List<Integer> provinceData,int count) {
        provinceData.set(1,provinceData.get(1) + count);
    }

    /**
     *排除疑似患者的计算
     * @param provinceData 当前省份疫情
     * @param count 排除人数
     */
    private void excludeSus(List<Integer> provinceData,int count) {
        provinceData.set(1,provinceData.get(1) - count);
    }

    /**
     *新增治愈病例的计算
     * @param provinceData 当前省份疫情
     * @param count 治愈人数
     */
    private void cure(List<Integer> provinceData,int count) {
        provinceData.set(2,provinceData.get(2) + count);
        provinceData.set(0,provinceData.get(0) - count);
    }

    /**
     *新增死亡病例的计算
     * @param provinceData 当前省份疫情
     * @param count 死亡人数
     */
    private void dead(List<Integer> provinceData,int count) {
        provinceData.set(3,provinceData.get(3) + count);
        provinceData.set(0,provinceData.get(0) - count);
    }

    /**
     * 流入感染患者的计算
     * @param sourceProvince 感染者从那个省（市、自治区）流出
     * @param destProvince 感染者流入哪个省（市、自治区）
     * @param count 人数
     */
    private void infInflow(List<Integer> sourceProvince,List<Integer> destProvince,int count) {
        sourceProvince.set(0,sourceProvince.get(0) - count);
        destProvince.set(0,destProvince.get(0) + count);
    }

    /**
     * 流入疑似患者的计算
     * @param sourceProvince 感染者从那个省（市、自治区）流出
     * @param destProvince 感染者流入哪个省（市、自治区）
     * @param count 人数
     */
    private void susInflow(List<Integer> sourceProvince,List<Integer> destProvince,int count) {
        sourceProvince.set(1,sourceProvince.get(1) - count);
        destProvince.set(1,destProvince.get(1) + count);
    }

    /**
     *确诊病例的计算
     * @param provinceData 当前省份疫情
     * @param count 确诊人数
     */
    private void diagnose(List<Integer> provinceData,int count) {
        provinceData.set(0,provinceData.get(0) + count);
        provinceData.set(1,provinceData.get(1) -count);
    }

    /**
     * 计算全国数据
     */
    private void countNational() {
        List<Integer> national = statistics.get("全国");
        for (List<Integer> data : statistics.values()) {
            for (int i = 0 ; i < national.size() ; i ++){
                national.set(i,national.get(i) + data.get(i));
            }
        }
    }
}