import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
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

    public static void main(String[] args) {
        Command command = readArgs("list -log ./221701423/log -out D:\\ListOut7.txt -date 2020-01-23 -type cure dead ip -province 全国 浙江 福建".split(" "));
        Map<String, Statistics> statisticsMap = new HashMap<>();
        List<String> files = Lib.getFiles(command.getLogDir());
        if (files.get(files.size() - 1).compareTo(command.getDate() + ".log.txt") < 0) {
            System.out.println("日期超出范围");
            System.exit(1);
        }
        for (String file : files) {
            List<Log> logList = readLog(new File(command.getLogDir(), file));
            logList.forEach(log -> {
                System.out.println(log.toString());
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
        Collator collator = Collator.getInstance(Locale.CHINA);
        command.getPrintProvinces().sort((o1, o2) -> {
            if (o1.equals("全国")) return -1;
            else if (o2.equals("全国")) return 1;
            else return collator.compare(o1, o2);
        });
        if (command.getPrintProvinces().contains("全国")) {
            Statistics countryStatistics = new Statistics();
            statisticsMap.forEach((s, statistics) -> {
                statistics.getInfos().forEach(countryStatistics::setInfo);
            });
            statisticsMap.put("全国", countryStatistics);
        }
        command.getPrintProvinces().forEach(s -> {
            System.out.print(s);
            Map<String, Integer> infos = statisticsMap.get(s).getInfos();
            command.getPrintTypes().forEach(s1 -> {
                System.out.print(" " + s1 + infos.get(s1) + "人");
            });
            System.out.println();
        });
        System.out.println("// 该文档并非真实数据，仅供测试使用");
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
    }

}
