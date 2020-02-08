import java.io.*;
import java.util.*;

/**
 * InfectStatistic
 *
 * @author Xily9
 * @version 1.0
 * @since 1.0
 */
class InfectStatistic {
    public static final List<String> typeList = new ArrayList<>() {{
        add("感染患者");
        add("疑似患者");
        add("治愈");
        add("死亡");
    }};
    public static final Map<String, String> typeMap = new HashMap<>() {{
        put("ip", "感染患者");
        put("sp", "疑似患者");
        put("cure", "治愈");
        put("dead", "死亡");
    }};
    private static final String[] provinces = {"全国", "安徽", "澳门", "北京", "重庆", "福建", "甘肃", "广东", "广西",
            "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏",
            "青海", "山东", "山西", "陕西", "上海", "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};

    public static void main(String[] args) {
        Command command = readArgs(args);
        Map<String, Statistics> statisticsMap = new HashMap<>();
        List<String> files = Lib.getFiles(command.getLogDir());
        if (files.get(files.size() - 1).compareTo(command.getDate() + ".log.txt") < 0) {
            System.out.println("日期超出范围");
            return;
        }
        for (String file : files) {
            List<Log> logList = readLog(new File(command.getLogDir(), file));
            logList.forEach(log -> {
                if (!statisticsMap.containsKey(log.getProvince())) {
                    statisticsMap.put(log.getProvince(), new Statistics());
                }
                statisticsMap.get(log.getProvince()).setInfo(log.getType(), log.getCount());
            });
            if (file.compareTo(command.getDate()) >= 0) {
                break;
            }
        }
        if (command.getPrintProvinces().isEmpty()) {
            command.getPrintProvinces().add("全国");
            command.getPrintProvinces().addAll(new ArrayList<>(statisticsMap.keySet()));
        } else {
            command.getPrintProvinces().forEach(s -> {
                if (!statisticsMap.containsKey(s)) {
                    statisticsMap.put(s, new Statistics());
                }
            });
        }
        if (command.getPrintProvinces().contains("全国")) {
            Statistics countryStatistics = new Statistics();
            statisticsMap.forEach((s, statistics) -> {
                statistics.getInfos().forEach(countryStatistics::setInfo);
            });
            statisticsMap.put("全国", countryStatistics);
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(command.getOutFile()))) {
            for (String province : provinces) {
                if (command.getPrintProvinces().contains(province)) {
                    try {
                        bufferedWriter.write(province);
                        Map<String, Integer> infos = statisticsMap.get(province).getInfos();
                        command.getPrintTypes().forEach(s1 -> {
                            try {
                                bufferedWriter.write(" " + s1 + infos.get(s1) + "人");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        bufferedWriter.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            bufferedWriter.write("// 该文档并非真实数据，仅供测试使用");
            bufferedWriter.newLine();
            bufferedWriter.write("// 命令 java InfectStatistic " + String.join(" ", args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Command readArgs(String[] args) {
        String argType = "";
        Command.Builder builder = new Command.Builder();
        for (String arg : args) {
            if (arg.startsWith("-")) {
                argType = arg;
            } else {
                switch (argType) {
                    case "-log":
                        builder.setLogDir(arg);
                        break;
                    case "-out":
                        builder.setOutFile(arg);
                        break;
                    case "-date":
                        builder.setDate(arg);
                        break;
                    case "-type":
                        builder.addPrintType(arg);
                        break;
                    case "-province":
                        builder.addPrintProvince(arg);
                        break;
                }
            }
        }
        return builder.build();
    }

    public static List<Log> readLog(File file) {
        List<Log> logList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                if (s.startsWith("//")) continue;
                String[] arr = s.split(" ");
                if (arr.length == 5) {//有流入
                    Log logOut = new Log();
                    int count = Integer.parseInt(arr[4].substring(0, arr[4].length() - 1));
                    logOut.setProvince(arr[0]);
                    logOut.setType(arr[1]);
                    logOut.setCount(-count);
                    logList.add(logOut);
                    Log log = new Log();
                    log.setProvince(arr[3]);
                    log.setType(arr[1]);
                    log.setCount(count);
                    logList.add(log);
                } else if (arr.length == 4) {//新增 确诊 排除
                    int count = Integer.parseInt(arr[3].substring(0, arr[3].length() - 1));
                    Log log = new Log();
                    switch (arr[1]) {
                        case "新增":
                            log.setProvince(arr[0]);
                            log.setType(arr[2]);
                            log.setCount(count);
                            break;
                        case "疑似患者":
                            Log logSub = new Log();
                            logSub.setProvince(arr[0]);
                            logSub.setType("疑似患者");
                            logSub.setCount(-count);
                            logList.add(logSub);
                            log.setProvince(arr[0]);
                            log.setType("感染患者");
                            log.setCount(count);
                            break;
                        case "排除": {
                            log.setProvince(arr[0]);
                            log.setType("疑似患者");
                            log.setCount(-count);
                        }
                        break;
                    }
                    logList.add(log);
                } else {//死亡 治愈
                    int count = Integer.parseInt(arr[2].substring(0, arr[2].length() - 1));
                    Log logSub = new Log();
                    logSub.setProvince(arr[0]);
                    logSub.setType("感染患者");
                    logSub.setCount(-count);
                    logList.add(logSub);
                    Log log = new Log();
                    log.setProvince(arr[0]);
                    log.setType(arr[1]);
                    log.setCount(count);
                    logList.add(log);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logList;
    }

    public static class Command {
        private String logDir;
        private String outFile;
        private String date;
        private List<String> printTypes;
        private List<String> printProvinces;

        public Command(String logDir, String outFile, String date, List<String> printTypes, List<String> printProvinces) {
            this.logDir = logDir;
            this.outFile = outFile;
            this.date = date;
            this.printTypes = printTypes;
            this.printProvinces = printProvinces;
        }

        public String getLogDir() {
            return logDir;
        }

        public void setLogDir(String logDir) {
            this.logDir = logDir;
        }

        public String getOutFile() {
            return outFile;
        }

        public void setOutFile(String outFile) {
            this.outFile = outFile;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<String> getPrintTypes() {
            return printTypes;
        }

        public void setPrintTypes(List<String> printTypes) {
            this.printTypes = printTypes;
        }

        public List<String> getPrintProvinces() {
            return printProvinces;
        }

        public void setPrintProvinces(List<String> printProvinces) {
            this.printProvinces = printProvinces;
        }

        public static class Builder {
            private String logDir;
            private String outFile;
            private String date;
            private List<String> printTypes = new ArrayList<>();
            private List<String> printProvinces = new ArrayList<>();

            public Builder setLogDir(String logDir) {
                this.logDir = logDir;
                return this;
            }

            public Builder setOutFile(String outFile) {
                this.outFile = outFile;
                return this;
            }

            public Builder setDate(String date) {
                this.date = date;
                return this;
            }

            public Builder addPrintType(String printType) {
                printTypes.add(typeMap.get(printType));
                return this;
            }

            public Builder addPrintProvince(String printProvince) {
                printProvinces.add(printProvince);
                return this;
            }

            public Command build() {
                if (printTypes.isEmpty()) printTypes.addAll(typeList);
                return new Command(logDir, outFile, date, printTypes, printProvinces);
            }
        }
    }

    public static class Log {
        private String province;
        private String type;
        private int count;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "Log{" +
                    "province='" + province + '\'' +
                    ", type='" + type + '\'' +
                    ", count=" + count +
                    '}';
        }
    }

    public static class Statistics {
        private Map<String, Integer> infos = new HashMap<>();

        public Statistics() {
            typeList.forEach(s -> {
                infos.put(s, 0);
            });
        }

        public Map<String, Integer> getInfos() {
            return infos;
        }

        public void setInfo(String type, int count) {
            infos.put(type, infos.get(type) + count);
        }

        @Override
        public String toString() {
            return "Statistics{" +
                    "infos=" + infos +
                    '}';
        }
    }

}
