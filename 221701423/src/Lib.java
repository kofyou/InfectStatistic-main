import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lib
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
public class Lib {
    public List<InfectStatistic.Log> readLog(String filename) {
        List<InfectStatistic.Log> logList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                if (s.startsWith("//")) continue;
                String[] arr = s.split(" ");
                if (arr.length == 5) {//有流入
                    InfectStatistic.Log logOut = new InfectStatistic.Log();
                    int count = Integer.parseInt(arr[4].substring(0, arr[4].length() - 1));
                    logOut.setProvince(arr[0]);
                    logOut.setType(arr[1]);
                    logOut.setCount(-count);
                    logList.add(logOut);
                    InfectStatistic.Log log = new InfectStatistic.Log();
                    log.setProvince(arr[3]);
                    log.setType(arr[1]);
                    log.setCount(count);
                    logList.add(log);
                } else if (arr.length == 4) {//新增 确诊 排除
                    int count = Integer.parseInt(arr[3].substring(0, arr[3].length() - 1));
                    InfectStatistic.Log log = new InfectStatistic.Log();
                    switch (arr[1]) {
                        case "新增":
                            log.setProvince(arr[0]);
                            log.setType(arr[2]);
                            log.setCount(count);
                            break;
                        case "疑似患者":
                            InfectStatistic.Log logSub = new InfectStatistic.Log();
                            logSub.setProvince(arr[0]);
                            logSub.setType("疑似患者");
                            logSub.setCount(-count);
                            logList.add(logSub);
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
                    InfectStatistic.Log log = new InfectStatistic.Log();
                    int count = Integer.parseInt(arr[2].substring(0, arr[2].length() - 1));
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

}
