import java.io.*;
import java.util.ArrayList;
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
    private int[][] patient;

    public InfectStatistic() {
        this.patient = new int[32][4];
    }

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
    }

    class CommandHandle {
        private String logPath;
        private String outputPath;
        private String endDate;
        private int[] patientTypeSign;
        private int[] provinceSign;

        public CommandHandle() {
            this.logPath = "";
            this.outputPath = "";
            this.endDate = "";
            this.patientTypeSign = new int[4];
            this.provinceSign = new int[32];
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

        public int[] getPatientTypeSign() {
            return patientTypeSign;
        }

        public int[] getProvinceSign() {
            return provinceSign;
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

        public void setPatientTypeSign(int[] patientTypeSign) {
            this.patientTypeSign = patientTypeSign;
        }

        public void setProvinceSign(int[] provinceSign) {
            this.provinceSign = provinceSign;
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
                        while (!args[i+1].startsWith("-")) {
                            i++;
                            if (args[i].equals("ip")) {
                                patientTypeSign[0] = 1;
                            }
                            else if (args[i].equals("sp")) {
                                patientTypeSign[1] = 1;
                            }
                            else if (args[i].equals("cure")) {
                                patientTypeSign[2] = 1;
                            }
                            else if (args[i].equals("dead")) {
                                patientTypeSign[3] = 1;
                            }
                            else {
                                return false;
                            }
                        }
                    }
                    else if (args[i].equals("-province")) {
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
        ArrayList<String> stringList;

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
                if (fileName.substring(1, 10).compareTo(endDate) <= 0) {
                    try {
                        FileReader fr = new FileReader(fileName);
                        BufferedReader bf = new BufferedReader(fr);
                        String str;
                        while ((str = bf.readLine()) != null) {
                            if(!str.startsWith("//")) {
                                this.stringList.add(str);
                            }
                        }
                        bf.close();
                        fr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    return false;
                }
            }
            return true;
        }
    }

    class DataHandle {
        private String[] provinceString = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃","广东", "广西", "贵州",
                "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林","江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海",
                "山东", "山西", "陕西", "上海","四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
        private int[][] patient = new int[32][4];

        public void dataProcess(ArrayList<String> stringList) {
            String pattern1 = "\\W+ 新增 感染患者 \\d+人";
            String pattern2 = "\\W+ 新增 疑似患者 \\d+人";
            String pattern3 = "\\W+ 感染患者 流入 \\W+ \\d+人";
            String pattern4 = "\\W+ 疑似患者 流入 \\W+ \\d+人";
            String pattern5 = "\\W+ 死亡 \\d+人";
            String pattern6 = "\\W+ 治愈 \\d+人";
            String pattern7 = "\\W+ 疑似患者 确诊感染 \\d+人";
            String pattern8 = "\\W+ 排除 疑似患者 \\d+人";
            for(int i = 0; i < stringList.size(); i++){
                String str = stringList.get(i);
                if (Pattern.matches(pattern1, str)) {
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
    }

}
