import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Lib
 * 统计疫情数据会用到的一些类
 *
 * @author 陈启元
 * @since 2020-02-18
 */
public class Lib {

}

/**
 * 存有各省疫情信息的全国类
 * 使用了单例模式，该类的唯一实例由该类的getInstance()方法获得
 */
class Country {
    HashMap<String, Province> provincesMap;
    public static String[] PROVINCES = {"安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西",
            "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁",
            "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆",
            "云南", "浙江"};
    HashMap<String, DailyInfo> totalStatistics = null;
    private static final Country country = new Country();

    private Country() {
        provincesMap = new HashMap<>();

        //把代表各个省份的Province对象加入map中方便存取
        for (String provinceName : PROVINCES) {
            Province province = new Province(provinceName);
            provincesMap.put(provinceName, province);
        }
    }

    /**
     * 获取Country类的唯一实例
     *
     * @return Country类的唯一实例
     */
    public static Country getInstance() {
        return country;
    }

    /**
     * @param name 省份名称
     * @return 对应的Province对象
     */
    public Province getProvince(String name) {
        return provincesMap.get(name);
    }

    /**
     * 录入全国各省份的某一天中的疫情信息
     *
     * @param infosFromLog 由Log获取的一天中的各省疫情信息
     */
    public void logData(HashMap<String, DailyInfo> infosFromLog) {
        for (String provinceName : PROVINCES) {
            Province province = provincesMap.get(provinceName);
            DailyInfo info = infosFromLog.get(provinceName);
            province.addDailyInfo(info);
        }
    }

    /**
     * 获得全国截止到指定日期的疫情情况
     *
     * @param endDate 要指定的日期
     * @return 全国截止到endDate这一天的疫情情况
     */
    public DailyInfo getCountryTotalInfo(LocalDate endDate) {
        DailyInfo countryTotalInfo = new DailyInfo(endDate);
        HashMap<String, DailyInfo> allProvincesInfo = this.getAllProvincesInfo(endDate);

        for (String provinceName : Country.PROVINCES) {
            DailyInfo provinceInfo = allProvincesInfo.get(provinceName);
            countryTotalInfo.add(provinceInfo);
        }

        return countryTotalInfo;
    }


    /**
     * 使用各个省份的每日感染数据，计算出各省截止到指定日期的总疫情情况
     *
     * @param endDate 要指定的日期
     * @return 各省截止到指定日期的疫情情况
     */
    public HashMap<String, DailyInfo> getAllProvincesInfo(LocalDate endDate) {
        if (totalStatistics == null) {
            HashMap<String, DailyInfo> totalInfos = new HashMap<>();

            for (String provinceName : Country.PROVINCES) {
                Province province = provincesMap.get(provinceName);
                DailyInfo provinceStatistic = province.getStatistic(endDate);
                totalInfos.put(provinceName, provinceStatistic);
            }

            this.totalStatistics = totalInfos;
            return totalInfos;
        } else {
            return totalStatistics;
        }
    }


}


/**
 * 记录某一天的各项人数（总数或变化）
 */
class DailyInfo {
    LocalDate date;
    public static String[] ALL_TYPES = {"ip", "sp", "cure", "dead"};
    int infected;
    int suspected;
    int dead;
    int cured;

    DailyInfo(LocalDate date) {
        this.date = date;
        infected = 0;
        suspected = 0;
        dead = 0;
        cured = 0;
    }

    DailyInfo(LocalDate date, int ip, int sp, int dead, int cured) {
        this.date = date;
        infected = ip;
        suspected = sp;
        this.dead = dead;
        this.cured = cured;
    }

    public void changeInfected(int change) {
        infected += change;
    }

    public void changeSuspected(int change) {
        suspected += change;
    }

    public void changeDead(int change) {
        dead += change;
    }

    public void changeCured(int change) {
        cured += change;
    }

    public DailyInfo add(DailyInfo dailyInfo2) {
        //两份信息相加，以较晚的为相加后的日期
        if (this.date.isBefore(dailyInfo2.getDate())) {
            this.date = dailyInfo2.getDate();
        }

        this.infected += dailyInfo2.infected;
        this.suspected += dailyInfo2.suspected;
        this.dead += dailyInfo2.dead;
        this.cured += dailyInfo2.cured;

        return this;

    }

    public LocalDate getDate() {
        return this.date;
    }


    public String toString(String[] types) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String type : types) {
            switch (type) {
                case "ip":
                    stringBuilder.append("感染患者" + infected + "人 ");
                    break;
                case "sp":
                    stringBuilder.append("疑似患者" + suspected + "人 ");
                    break;
                case "cure":
                    stringBuilder.append("治愈" + cured + "人 ");
                    break;
                case "dead":
                    stringBuilder.append("死亡" + dead + "人 ");
                    break;
                default:
                    System.out.println("\n输出时出现错误，类型参数只能指定为ip/sp/cure/dead");
            }
        }
        return stringBuilder.toString();
    }
}

class Log {
    LocalDate date;
    File logFile;
    Country country;
    /**
     * 记录该日志
     */
    HashMap<String, DailyInfo> dailyInfos;

    public Log(String filePath) {
        logFile = new File(filePath);
        String logName = logFile.getName();
        date = LocalDate.parse(logName.substring(0, 10));
        //System.out.println("日志对象已创建，日期："+date);

        country = Country.getInstance();
        dailyInfos = new HashMap<>();
        for (String provinceName : Country.PROVINCES) {
            DailyInfo dailyInfo = new DailyInfo(date);
            dailyInfos.put(provinceName, dailyInfo);
        }
//        date=LocalDate.parse(filePath.substring())
    }

    public void analyzeLog() {
        FileInputStream fis = null;

        //System.out.println("开始处理日志");
        try {
            fis = new FileInputStream(logFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                //注释不处理
                if (line.contains("//"))
                    continue;

                //下面是日志中八种情况的处理
                if (line.contains("新增") && line.contains("感染")) {
                    addInfected(line);
                    continue;
                }
                if (line.contains("新增") && line.contains("疑似")) {
                    addSuspected(line);
                    continue;
                }
                if (line.contains("流入") && line.contains("感染")) {
                    flowInfected(line);
                    continue;
                }
                if (line.contains("流入") && line.contains("疑似")) {
                    flowSuspected(line);
                    continue;
                }
                if (line.contains("死亡")) {
                    addDead(line);
                    continue;
                }
                if (line.contains("治愈")) {
                    addCured(line);
                    continue;
                }
                if (line.contains("确诊")) {
                    suspectedToInfected(line);
                    continue;
                }
                if (line.contains("排除")) {
                    suspectedToHealthy(line);
                    continue;
                }
            }

            br.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("读取日志文件" + logFile.getName() + "时出现错误！");
            e.printStackTrace();
        }

        country.logData(dailyInfos);


    }

    private void addInfected(String line) {
        String[] words = line.split(" ");
        String provinceName = words[0];
        String numberString = words[3];
        int number = Integer.parseInt(numberString.substring(0, numberString.length() - 1));

        dailyInfos.get(provinceName).changeInfected(number);
        country.getProvince(provinceName).hasOccurred = true;
    }

    private void addSuspected(String line) {
        String[] words = line.split(" ");
        String provinceName = words[0];
        String numberString = words[3];
        int number = Integer.parseInt(numberString.substring(0, numberString.length() - 1));

        dailyInfos.get(provinceName).changeSuspected(number);
        country.getProvince(provinceName).hasOccurred = true;
    }

    private void flowInfected(String line) {
        String[] words = line.split(" ");
        String province1Name = words[0];
        String province2Name = words[3];
        String numberString = words[4];
        int number = Integer.parseInt(numberString.substring(0, numberString.length() - 1));

        dailyInfos.get(province1Name).changeInfected(-number);
        dailyInfos.get(province2Name).changeInfected(number);
        country.getProvince(province1Name).hasOccurred = true;
        country.getProvince(province2Name).hasOccurred = true;
    }

    private void flowSuspected(String line) {
        String[] words = line.split(" ");
        String province1Name = words[0];
        String province2Name = words[3];
        String numberString = words[4];
        int number = Integer.parseInt(numberString.substring(0, numberString.length() - 1));

        dailyInfos.get(province1Name).changeSuspected(-number);
        dailyInfos.get(province2Name).changeSuspected(number);
        country.getProvince(province1Name).hasOccurred = true;
        country.getProvince(province2Name).hasOccurred = true;
    }

    private void addDead(String line) {
        String[] words = line.split(" ");
        String provinceName = words[0];
        String numberString = words[2];
        int number = Integer.parseInt(numberString.substring(0, numberString.length() - 1));

        dailyInfos.get(provinceName).changeDead(number);
        dailyInfos.get(provinceName).changeInfected(-number);
        country.getProvince(provinceName).hasOccurred = true;
    }

    private void addCured(String line) {
        String[] words = line.split(" ");
        String provinceName = words[0];
        String numberString = words[2];
        int number = Integer.parseInt(numberString.substring(0, numberString.length() - 1));

        dailyInfos.get(provinceName).changeInfected(-number);
        dailyInfos.get(provinceName).changeCured(number);
        country.getProvince(provinceName).hasOccurred = true;
    }

    private void suspectedToInfected(String line) {
        String[] words = line.split(" ");
        String provinceName = words[0];
        String numberString = words[3];
        int number = Integer.parseInt(numberString.substring(0, numberString.length() - 1));

        dailyInfos.get(provinceName).changeInfected(number);
        dailyInfos.get(provinceName).changeSuspected(-number);
        country.getProvince(provinceName).hasOccurred = true;
    }

    private void suspectedToHealthy(String line) {
        String[] words = line.split(" ");
        String provinceName = words[0];
        String numberString = words[3];
        int number = Integer.parseInt(numberString.substring(0, numberString.length() - 1));

        dailyInfos.get(provinceName).changeSuspected(-number);
        country.getProvince(provinceName).hasOccurred = true;
    }

}

/**
 * 存放日志列表，以及日志列表中日志的最早/最晚时间
 */
class LogList {
    ArrayList<Log> logs;
    LocalDate beginDate;
    LocalDate endDate;

    public LogList() {
        logs = new ArrayList<>();
    }

    public void readLogsFromPath(String path) {
        File logFiles = new File(path);
        String[] subPaths = logFiles.list();

        //使用日志路径创建Log对象列表
        for (int i = 0; i < subPaths.length; i++) {
            //System.out.println(path+'/'+subPaths[i]);
            Log log = new Log(path + '/' + subPaths[i]);
            logs.add(log);
        }

        //获取日志列表的最早时间和最晚时间
        if (!logs.isEmpty()) {
            LocalDate firstDate = logs.get(0).date;
            LocalDate lastDate = logs.get(0).date;

            for (Log log : logs) {
                LocalDate logDate = log.date;
                if (logDate.isBefore(firstDate))
                    firstDate = logDate;
                if (logDate.isAfter(lastDate))
                    lastDate = logDate;
            }

            beginDate = firstDate;
            endDate = lastDate;
        }

        analyzeLogs();
    }

    public void analyzeLogs() {
        for (Log log : logs) {
            log.analyzeLog();
        }
    }


    public LocalDate getEndDate() {
        return endDate;
    }
}

/**
 * 代表各省的类
 */
class Province {
    String name;
    ArrayList<DailyInfo> dailyInfos;
    /**
     * 标记是否在日志中出现过
     */
    boolean hasOccurred;
    DailyInfo totalInfo = null;


    Province(String name) {
        this.name = name;
        dailyInfos = new ArrayList<>();
        hasOccurred = false;
    }

    public void addDailyInfo(DailyInfo dailyInfo) {
        dailyInfos.add(dailyInfo);
    }

//    public void printAllInfo(){
//        for(DailyInfo info:dailyInfos){
//            System.out.println(info.toString());
//        }
//    }

    public DailyInfo getStatistic(LocalDate endDate) {
        //未进行统计就计算一遍
        if (totalInfo == null) {
            DailyInfo totalInfo = new DailyInfo(endDate);

            for (DailyInfo info : dailyInfos) {
                //只处理指定日期当天以及之前的日志
                if (info.getDate().isBefore(endDate) || info.getDate().isEqual(endDate)) {
                    totalInfo.add(info);
                }
            }

            this.totalInfo = totalInfo;
            return totalInfo;
        } else {
            return this.totalInfo;
        }
    }
}

