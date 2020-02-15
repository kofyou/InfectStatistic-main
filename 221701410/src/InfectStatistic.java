import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
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
        }
        DataHandle dataHandle = infectStatistic.new DataHandle();
        dataHandle.dataProcess(logHandle.getStringList());
        dataHandle.output(commandHandle.getOutputPath(), commandHandle.getPatientTypeSign(),
                commandHandle.getProvinceList(), commandHandle.getProvinceSign(), commandHandle.getTypeSign());
    }

    class CommandHandle {
        private String logPath;
        private String outputPath;
        private String endDate;
        private boolean typeSign;
        private int[] patientTypeSign;
        private boolean provinceSign;
        private int[] provinceList;

        public CommandHandle() {
            this.logPath = "";
            this.outputPath = "";
            this.endDate = "";
            typeSign = false;
            this.patientTypeSign = new int[4];
            provinceSign = false;
            this.provinceList = new int[32];
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

        public int[] getPatientTypeSign() {
            return patientTypeSign;
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

        public boolean commandProcess(String[] args) {
            if (args[0].equals("list")) { //命令匹配
                for (int i = 1; i < args.length; i++) {
                    if (args[i].equals("-log")) {
                        i++;
                        this.setLogPath(args[i]);
                    }
                    else if (args[i].equals("-out")) {
                        i++;
                        this.setOutputPath(args[i]);
                    }
                    else if (args[i].equals("-date")) {
                        i++;
                        this.setEndDate(args[i]);
                    }
                    else if (args[i].equals("-type")) {
                        typeSign = true;
                        int j = 0;
                        while (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                            i++;
                            j++;
                            if (args[i].equals("ip")) {
                                patientTypeSign[0] = j;
                            }
                            else if (args[i].equals("sp")) {
                                patientTypeSign[1] = j;
                            }
                            else if (args[i].equals("cure")) {
                                patientTypeSign[2] = j;
                            }
                            else if (args[i].equals("dead")) {
                                patientTypeSign[3] = j;
                            }
                            else {
                                return false;
                            }
                        }
                    }
                    else if (args[i].equals("-province")) {
                        provinceSign = true;
                        while (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                            i++;
                            for (int j = 0; j < provinceString.length; j++) {
                                if (args[i].equals(provinceString[j])) {
                                    provinceList[j] = 1;
                                }
                            }
                        }
                    }
                }
            }
            else {
                return false;
            }
            return true;
        }
    }

    class LogHandle {
        private ArrayList<String> stringList;

        public LogHandle() {
            this.stringList = new ArrayList<>();
        }

        public ArrayList<String> getStringList() {
            return stringList;
        }

        public boolean readLogs(String endDate, String logPath) {
            File file = new File(logPath);
            File fileList[] = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                String fileName = fileList[i].getName();
                String fileNameWithout = fileName.substring(0, 10);
                if (endDate.compareTo(fileNameWithout) >= 0) {
                    try {
                        //FileReader fr = new FileReader(logPath + fileName);
                        //System.out.println(fileName);
                        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(
                                new File(logPath + fileName)),"UTF-8"));
                        String str;
                        while ((str = bf.readLine()) != null) {
                            if(!str.startsWith("//")) {
                                this.stringList.add(str);
                                //System.out.println(str);
                            }
                        }
                        bf.close();
                        //fr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    //return false;
                }
            }
            return true;
        }
    }

    class DataHandle {
        private int[][] patient = new int[32][4];
        private int[] influencedProvince = new int[32];

        public int[][] getPatient() {
            return patient;
        }

        public void setPatient(int[][] patient) {
            this.patient = patient;
        }

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
            for(int i = 0; i < stringList.size(); i++){
                //System.out.println(stringList.size());
                String str = stringList.get(i);
                //System.out.println(str);
                if (Pattern.matches(pattern1, str)) {
                    //System.out.println("1");
                    ipAdd(str);
                }
                else if (Pattern.matches(pattern2, str)) {
                    spAdd(str);
                }
                else if (Pattern.matches(pattern3, str)) {
                    ipFlow(str);
                }
                else if (Pattern.matches(pattern4, str)) {
                    spFlow(str);
                }
                else if (Pattern.matches(pattern5, str)) {
                    deadAdd(str);
                }
                else if (Pattern.matches(pattern6, str)) {
                    cureAdd(str);
                }
                else if (Pattern.matches(pattern7, str)) {
                    spToIp(str);
                }
                else if (Pattern.matches(pattern8, str)) {
                    spSub(str);
                }
            }

        }

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

        public void output(String outputPath, int[] patientTypeSign, int[] provinceList, boolean provinceSign, boolean typeSign) {
            try {
                FileWriter fileWriter = new FileWriter(outputPath);
                if (provinceSign) {
                    for (int i = 0; i < provinceString.length; i++) {
                        if (provinceList[i] == 1){
                            if (typeSign) {
                                fileWriter.write(provinceString[i]);
                                for (int j = 1; j <= patientType.length; j++) {
                                    for (int k = 0; k < patientType.length; k++) {
                                        if (patientTypeSign[k] == j) {
                                            fileWriter.write(" " + patientType[k] + patient[i][k] + "人");
                                            break;
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
                                for (int j = 1; j <= patientType.length; j++) {
                                    for (int k = 0; k < patientType.length; k++) {
                                        if (patientTypeSign[k] == j) {
                                            fileWriter.write(" " + patientType[k] + patient[i][k] + "人");
                                            break;
                                        }
                                    }
                                }
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
