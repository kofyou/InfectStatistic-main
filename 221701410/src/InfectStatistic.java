import java.io.*;
import java.util.ArrayList;

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

}
