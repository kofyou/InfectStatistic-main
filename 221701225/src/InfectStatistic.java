import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * InfectStatistic
 * 疫情统计程序主类
 * @author 陈启元
 * @since 2020-02-19
 */
class InfectStatistic {
    /**
     * 疫情统计程序所要用到的参数
     */
    private String inputPath;
    private String outputPath;
    private LocalDate date = null;
    private ArrayList<String> types = null;
    private ArrayList<String> provinces = null;

    /**
     * 统计时会用到的对象
     */
    private LogList logList = new LogList();
    private Country country;
    HashMap<String, Integer> provinceWeight;

    public static void main(String[] args) {
//        String[] debugArgs=("-log C:\\Users\\62706\\Documents\\GitHub\\InfectStatistic-main\\221701225\\log -out ..\\result\\ListOut.txt " +
//                "-date 2020-01-22 -province 福建 安徽 浙江 重庆 河北").split(" ");

        //使用命令行参数初始化InfectStatistic类（在这一步中获取各个参数）
        InfectStatistic infectInfoOperator = new InfectStatistic(args);
        infectInfoOperator.readLogs();
        infectInfoOperator.output();
    }

    InfectStatistic(String[] args) {
        int inputPosition = -1;
        int outputPosition = -1;
        int datePosition = -1;
        int typesPosition = -1;
        int provincePosition = -1;
        int weight = 0;
        this.country = Country.getInstance();
        provinceWeight = new HashMap<>();
        for (String provinceName : Country.PROVINCES) {
            provinceWeight.put(provinceName, weight);
            weight++;
        }

        for (int i = 0; i < args.length; i++) {
            if ("-log".equals(args[i])) {
                inputPosition = i;
            }
            if ("-out".equals(args[i])) {
                outputPosition = i;
            }
            if ("-date".equals(args[i])) {
                datePosition = i;
            }
            if ("-type".equals(args[i])) {
                typesPosition = i;
            }
            if ("-province".equals(args[i])) {
                provincePosition = i;
            }
        }

        //读取各个参数到类属性中
        if (inputPosition != -1) {
            inputPath = args[inputPosition + 1];
        }
        if (outputPosition != -1) {
            outputPath = args[outputPosition + 1];
        }
        if (datePosition != -1) {
            date = LocalDate.parse(args[datePosition + 1]);
        }
        if (typesPosition != -1) {
            types = new ArrayList<String>();
            int pos = typesPosition + 1;
            int length = args.length;

            //第一个条件放在前面防止下标越界
            while (pos < length && !args[pos].contains("-")) {
                types.add(args[pos]);
                pos++;
            }
        }
        if (provincePosition != -1) {
            provinces = new ArrayList<>();
            int pos = provincePosition + 1;
            int length = args.length;

            while (pos < length && !args[pos].contains("-")) {
                provinces.add(args[pos]);
                pos++;
            }
        }
    }

    /**
     * 从输入路径读取日志
     */
    public void readLogs() {
        logList.readLogsFromPath(inputPath);
    }

    /**
     * 根据获取到的参数的情况进行输出
     */
    public void output() {
        LocalDate endDate;
        String[] outputTypes = null;
        HashMap<String, DailyInfo> provinceDailyInfos;
        DailyInfo countryTotalInfo;

        //设置统计的起始时间、结束时间
        if (date == null) {
            endDate = logList.getEndDate();
        } else {
            endDate = date;
        }

        if (types == null) {
            outputTypes = DailyInfo.ALL_TYPES;
        } else {
            outputTypes = types.toArray(new String[types.size()]);
        }

        //获取全国统计信息及各省统计信息
        countryTotalInfo = country.getCountryTotalInfo(endDate);
        provinceDailyInfos = country.getAllProvincesInfo(endDate);

        try {
            File outputFile = new File(outputPath);
            File outputPath=outputFile.getParentFile();
            if(!outputPath.exists()) {
                outputPath.mkdir();
            }
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileWriter writer = new FileWriter(outputFile);

            if (provinces == null) {
                //未指定省份时只打印全国和在日志中出现过的省份
                writer.write("全国 " + countryTotalInfo.toString(outputTypes) + '\n');

                for (String provinceName : Country.PROVINCES) {
                    Province province = country.getProvince(provinceName);

                    if (province.hasOccurred == true) {
                        DailyInfo provinceInfo = provinceDailyInfos.get(provinceName);
                        writer.write(provinceName + " " + provinceInfo.toString(outputTypes) + '\n');
                    }
                }
            } else {
                if (provinces.contains("全国")) {
                    writer.write("全国 " + countryTotalInfo.toString(outputTypes) + '\n');
                    provinces.remove("全国");
                }

                //使用自定义比较函数对省份进行按拼音的排序
                Collections.sort(provinces, new Comparator<String>() {
                    @Override
                    public int compare(String s, String t1) {
                        int weight1 = provinceWeight.get(s);
                        int weight2 = provinceWeight.get(t1);

                        return weight1 - weight2;
                    }
                });

                //遍历指定的省份
                for (String provinceName : provinces) {
                    Province province = country.getProvince(provinceName);

                    //省份在日志出现过，就打印统计得到的数据，否则直接全输出0
                    if (province.hasOccurred == true) {
                        DailyInfo provinceInfo = provinceDailyInfos.get(provinceName);
                        writer.write(provinceName + " " + provinceInfo.toString(outputTypes) + '\n');
                    } else {
                        DailyInfo emptyInfo = new DailyInfo(endDate);
                        writer.write(provinceName + " " + emptyInfo.toString(outputTypes) + '\n');
                    }
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "InfectStatistic{" +
                "inputPath='" + inputPath + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", date='" + date + '\'' +
                ", types=" + types +
                ", provinces=" + provinces +
                '}';
    }
}
