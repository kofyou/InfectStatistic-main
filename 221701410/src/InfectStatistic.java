import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * @author hujh4779
 * @version 1.0
 * @since 2020.2.14
 */
class InfectStatistic {

    private String[] provinceString = {"全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南",
            "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东",
            "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};
    private String[] patientType = {"感染患者", "疑似患者", "治愈", "死亡"};

    public static void main(String[] args) {
        InfectStatistic infectStatistic = new InfectStatistic();
        CommandHandle commandHandle = infectStatistic.new CommandHandle();
        commandHandle.commandProcess(args);
        LogHandle logHandle = infectStatistic.new LogHandle();
        if (!logHandle.readLogs(commandHandle.getEndDate(), commandHandle.getLogPath())) {
            System.out.println("日期超出范围");
            return;
        }
        DataHandle dataHandle = infectStatistic.new DataHandle();
        dataHandle.dataProcess(logHandle.getStringList());
        dataHandle.output(commandHandle.getOutputPath(), commandHandle.getTypeList(), commandHandle.getProvinceList(),
                commandHandle.getProvinceSign(), commandHandle.getTypeSign());
    }

    /**
     * CommandHandle
     * @author hujh4779
     * @version 1.0
     * @since 2020.2.15
     */
    class CommandHandle {

        private String logPath;
        private String outputPath;
        private String endDate;
        private boolean typeSign;
        private ArrayList<String> typeList;
        private boolean provinceSign;
        private int[] provinceList;

        public CommandHandle() {
            this.logPath = "";
            this.outputPath = "";
            this.endDate = "";
            typeSign = false;
            this.typeList = new ArrayList<>();
            provinceSign = false;
            this.provinceList = new int[provinceString.length];
        }

        public String getLogPath() {
            return logPath;
        }

        public String getOutputPath() {
            return outputPath;
        }

        public String getEndDate() {
            return endDate;
        }

        public boolean getTypeSign() {
            return typeSign;
        }

        public ArrayList<String> getTypeList() {
            return typeList;
        }

        public boolean getProvinceSign() {
            return provinceSign;
        }

        public int[] getProvinceList() {
            return provinceList;
        }

        public void setLogPath(String logPath) {
            this.logPath = logPath;
        }

        public void setOutputPath(String outputPath) {
            this.outputPath = outputPath;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        /**
         * commandProcess
         * @description 根据传入的命令行参数，设置类变量
         * @param args 命令行参数
         */
        public void commandProcess(String[] args) {
            if (args[0].equals("list")) { //命令匹配
                for (int i = 1; i < args.length; i++) {
                    switch (args[i]) {
                        case "-log":
                            i++;
                            this.setLogPath(args[i]);
                            break;
                        case "-out":
                            i++;
                            this.setOutputPath(args[i]);
                            break;
                        case "-date":
                            i++;
                            this.setEndDate(args[i]);
                            break;
                        case "-type":
                            typeSign = true;
                            while (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                                i++;
                                switch (args[i]) {
                                    case "ip":
                                        typeList.add(patientType[0]);
                                        break;
                                    case "sp":
                                        typeList.add(patientType[1]);
                                        break;
                                    case "cure":
                                        typeList.add(patientType[2]);
                                        break;
                                    case "dead":
                                        typeList.add(patientType[3]);
                                        break;
                                    default:
                                        return;
                                }
                            }
                            break;
                        case "-province":
                            provinceSign = true;
                            while (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                                i++;
                                for (int k = 0; k < provinceString.length; k++) {
                                    if (args[i].equals(provinceString[k])) {
                                        provinceList[k] = 1;
                                    }
                                }
                            }
                            break;
                        default:
                            return;
                    }
                }
            }
        }
    }

    /**
     * LogHandle
     * @author hujh4779
     * @version 1.0
     * @since 2020.2.15
     */
    class LogHandle {

        private ArrayList<String> stringList;

        public LogHandle() {
            this.stringList = new ArrayList<>();
        }

        public ArrayList<String> getStringList() {
            return stringList;
        }

        /**
         * readLogs
         * @description 根据传入的最迟日期和日志目录逐行读取日志，并添加至stringList变量中
         * @param endDate 读取日志最迟日期
         * @param logPath 读取日志目录
         */
        public boolean readLogs(String endDate, String logPath) {
            try {
                File file = new File(logPath);
                File[] fileList = file.listFiles();
                assert fileList != null;
                String biggestDate = "";
                String fileNameOut;
                String fileNameWithoutOut;
                for (File value : fileList) {
                    fileNameOut = value.getName();
                    fileNameWithoutOut = fileNameOut.substring(0, 10);
                    if (biggestDate.compareTo(fileNameWithoutOut) < 0) {
                        biggestDate = fileNameWithoutOut;
                    }
                }
                if (endDate.compareTo(biggestDate) > 0) {
                    return false;
                }
                else {
                    String fileName;
                    String fileNameWithout;
                    for (File value : fileList) {
                        fileName = value.getName();
                        fileNameWithout = fileName.substring(0, 10);
                        if (endDate.compareTo(fileNameWithout) >= 0) {
                            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(
                                    new File(logPath + fileName)), "UTF-8"));
                            String str;
                            while ((str = bf.readLine()) != null) {
                                if (!str.startsWith("//")) {
                                    this.stringList.add(str);
                                }
                            }
                            bf.close();
                        }
                    }
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * DataHandle
     * @author hujh4779
     * @version 1.0
     * @since 2020.2.15
     */
    class DataHandle {

        private int[][] patient = new int[provinceString.length][patientType.length];
        private int[] influencedProvince = new int[provinceString.length];

        public int[][] getPatient() {
            return patient;
        }

        public void setPatient(int[][] patient) {
            this.patient = patient;
        }

        /**
         * dataProcess
         * @description 将传入的字符串数组匹配正则表达式模式，并调用其他方法进行处理
         * @param stringList 传入日志中的每一行字符串
         */
        public void dataProcess(ArrayList<String> stringList) {
            String pattern1 = "\\W+ 新增 感染患者 \\d+人";
            String pattern2 = "\\W+ 新增 疑似患者 \\d+人";
            String pattern3 = "\\W+ 感染患者 流入 \\W+ \\d+人";
            String pattern4 = "\\W+ 疑似患者 流入 \\W+ \\d+人";
            String pattern5 = "\\W+ 死亡 \\d+人";
            String pattern6 = "\\W+ 治愈 \\d+人";
            String pattern7 = "\\W+ 疑似患者 确诊感染 \\d+人";
            String pattern8 = "\\W+ 排除 疑似患者 \\d+人";
            influencedProvince[0] = 1;
            for (String str : stringList) {
                if (Pattern.matches(pattern1, str)) {
                    ipAdd(str);
                } else if (Pattern.matches(pattern2, str)) {
                    spAdd(str);
                } else if (Pattern.matches(pattern3, str)) {
                    ipFlow(str);
                } else if (Pattern.matches(pattern4, str)) {
                    spFlow(str);
                } else if (Pattern.matches(pattern5, str)) {
                    deadAdd(str);
                } else if (Pattern.matches(pattern6, str)) {
                    cureAdd(str);
                } else if (Pattern.matches(pattern7, str)) {
                    spToIp(str);
                } else if (Pattern.matches(pattern8, str)) {
                    spSub(str);
                }
            }
        }

        /**
         * ipAdd
         * @description 根据单条日志记录修改patient和influencedProvince
         * @param str 传入单条日志记录
         */
        public void ipAdd(String str) {
            String pattern = "(\\W+) 新增 感染患者 (\\d+)人";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            String province = "";
            int ip = 0;
            if (m.find( )) {
                province = m.group(1);
                ip = Integer.parseInt(m.group(2));
            } else {
                System.out.println("NO MATCH");
            }
            for (int i = 0; i < provinceString.length; i++) {
                if (provinceString[i].equals(province)) {
                    patient[i][0] += ip;
                    patient[0][0] += ip;
                    influencedProvince[i] = 1;
                }
            }
        }

        /**
         * spAdd
         * @description 根据单条日志记录修改patient和influencedProvince
         * @param str 传入单条日志记录
         */
        public void spAdd(String str) {
            String pattern = "(\\W+) 新增 疑似患者 (\\d+)人";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            String province = "";
            int sp = 0;
            if (m.find( )) {
                province = m.group(1);
                sp = Integer.parseInt(m.group(2));
            } else {
                System.out.println("NO MATCH");
            }
            for (int i = 0; i < provinceString.length; i++) {
                if (provinceString[i].equals(province)) {
                    patient[i][1] += sp;
                    patient[0][1] += sp;
                    influencedProvince[i] = 1;
                }
            }
        }

        /**
         * ipFlow
         * @description 根据单条日志记录修改patient和influencedProvince
         * @param str 传入单条日志记录
         */
        public void ipFlow(String str) {
            String pattern = "(\\W+) 感染患者 流入 (\\W+) (\\d+)人";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            String province1 = "";
            String province2 = "";
            int ip = 0;
            if (m.find( )) {
                province1 = m.group(1);
                province2 = m.group(2);
                ip = Integer.parseInt(m.group(3));
            } else {
                System.out.println("NO MATCH");
            }
            for (int i = 0; i < provinceString.length; i++) {
                if (provinceString[i].equals(province1)) {
                    patient[i][0] -= ip;
                    influencedProvince[i] = 1;
                }
                else if (provinceString[i].equals(province2)) {
                    patient[i][0] += ip;
                    influencedProvince[i] = 1;
                }
            }
        }

        /**
         * spFlow
         * @description 根据单条日志记录修改patient和influencedProvince
         * @param str 传入单条日志记录
         */
        public void spFlow(String str) {
            String pattern = "(\\W+) 疑似患者 流入 (\\W+) (\\d+)人";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            String province1 = "";
            String province2 = "";
            int sp = 0;
            if (m.find( )) {
                province1 = m.group(1);
                province2 = m.group(2);
                sp = Integer.parseInt(m.group(3));
            } else {
                System.out.println("NO MATCH");
            }
            for (int i = 0; i < provinceString.length; i++) {
                if (provinceString[i].equals(province1)) {
                    patient[i][1] -= sp;
                    influencedProvince[i] = 1;
                }
                else if (provinceString[i].equals(province2)) {
                    patient[i][1] += sp;
                    influencedProvince[i] = 1;
                }
            }
        }

        /**
         * deadAdd
         * @description 根据单条日志记录修改patient和influencedProvince
         * @param str 传入单条日志记录
         */
        public void deadAdd(String str) {
            String pattern = "(\\W+) 死亡 (\\d+)人";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            String province = "";
            int dead = 0;
            if (m.find( )) {
                province = m.group(1);
                dead = Integer.parseInt(m.group(2));
            } else {
                System.out.println("NO MATCH");
            }
            for (int i = 0; i < provinceString.length; i++) {
                if (provinceString[i].equals(province)) {
                    patient[i][3] += dead;
                    patient[0][3] += dead;
                    patient[i][0] -= dead;
                    patient[0][0] -= dead;
                    influencedProvince[i] = 1;
                }
            }
        }

        /**
         * cureAdd
         * @description 根据单条日志记录修改patient和influencedProvince
         * @param str 传入单条日志记录
         */
        public void cureAdd(String str) {
            String pattern = "(\\W+) 治愈 (\\d+)人";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            String province = "";
            int cure = 0;
            if (m.find( )) {
                province = m.group(1);
                cure = Integer.parseInt(m.group(2));
            } else {
                System.out.println("NO MATCH");
            }
            for (int i = 0; i < provinceString.length; i++) {
                if (provinceString[i].equals(province)) {
                    patient[i][2] += cure;
                    patient[0][2] += cure;
                    patient[i][0] -= cure;
                    patient[0][0] -= cure;
                    influencedProvince[i] = 1;
                }
            }
        }

        /**
         * spToIp
         * @description 根据单条日志记录修改patient和influencedProvince
         * @param str 传入单条日志记录
         */
        public void spToIp(String str) {
            String pattern = "(\\W+) 疑似患者 确诊感染 (\\d+)人";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            String province = "";
            int ip = 0;
            if (m.find( )) {
                province = m.group(1);
                ip = Integer.parseInt(m.group(2));
            } else {
                System.out.println("NO MATCH");
            }
            for (int i = 0; i < provinceString.length; i++) {
                if (provinceString[i].equals(province)) {
                    patient[i][0] += ip;
                    patient[0][0] += ip;
                    patient[i][1] -= ip;
                    patient[0][1] -= ip;
                    influencedProvince[i] = 1;
                }
            }
        }

        /**
         * spSub
         * @description 根据单条日志记录修改patient和influencedProvince
         * @param str 传入单条日志记录
         */
        public void spSub(String str) {
            String pattern = "(\\W+) 排除 疑似患者 (\\d+)人";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            String province = "";
            int sp = 0;
            if (m.find( )) {
                province = m.group(1);
                sp = Integer.parseInt(m.group(2));
            } else {
                System.out.println("NO MATCH");
            }
            for (int i = 0; i < provinceString.length; i++) {
                if (provinceString[i].equals(province)) {
                    patient[i][1] -= sp;
                    patient[0][1] -= sp;
                    influencedProvince[i] = 1;
                }
            }
        }

        /**
         * output
         * @description 根据传入的参数，将类变量格式化输出
         * @param outputPath 输出的日志目录
         * @param typeList 命令行参数的类型列表
         * @param provinceList 省份输出标志
         * @param provinceSign 命令行参数是否有-province
         * @param typeSign 命令行参数是否有-type
         */
        public void output(String outputPath, ArrayList<String> typeList, int[] provinceList,
                           boolean provinceSign, boolean typeSign) {
            try {
                File file = new File(outputPath);
                String dir = file.getParent();
                File dirFile = new File(dir);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }
                FileWriter fileWriter = new FileWriter(outputPath);
                if (provinceSign) {
                    for (int i = 0; i < provinceString.length; i++) {
                        if (provinceList[i] == 1){
                            if (typeSign) {
                                fileWriter.write(provinceString[i]);
                                for (String s : typeList) {
                                    for (int k = 0; k < patientType.length; k++) {
                                        if (s.equals(patientType[k])) {
                                            fileWriter.write(" " + patientType[k] + patient[i][k] + "人");
                                        }
                                    }
                                }
                                fileWriter.write("\n");
                            }
                            else {
                                fileWriter.write(provinceString[i] + " 感染患者" + patient[i][0] + "人 疑似患者"
                                        + patient[i][1] + "人 治愈" + patient[i][2] + "人 死亡" + patient[i][3] + "人\n");
                            }
                        }
                    }
                }
                else {
                    for (int i = 0; i < provinceString.length; i++) {
                        if (influencedProvince[i] == 1){
                            if (typeSign) {
                                fileWriter.write(provinceString[i]);
                                for (String s : typeList) {
                                    for (int k = 0; k < patientType.length; k++) {
                                        if (s.equals(patientType[k])) {
                                            fileWriter.write(" " + patientType[k] + patient[i][k] + "人");
                                        }
                                    }
                                }
                                fileWriter.write("\n");
                            }
                            else {
                                fileWriter.write(provinceString[i] + " 感染患者" + patient[i][0] + "人 疑似患者"
                                        + patient[i][1] + "人 治愈" + patient[i][2] + "人 死亡" + patient[i][3] + "人\n");
                            }
                        }
                    }
                }
                fileWriter.write("// 该文档并非真实数据，仅供测试使用");
                fileWriter.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
