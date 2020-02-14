import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class LogList {
    ArrayList<Log> logs;
    LocalDate beginDate;
    LocalDate endDate;

    public LogList(){
        logs=new ArrayList<>();
    }

    public void readLogsFromPath(String path){
        File logFiles=new File(path);
        String[] subPaths=logFiles.list();

        //使用日志路径创建Log对象列表
        for(int i=0;i<subPaths.length;i++){
            System.out.println(path+'/'+subPaths[i]);
            Log log=new Log(path+'/'+subPaths[i]);
            logs.add(log);
        }

        //获取日志列表的最早时间和最晚时间
        if(!logs.isEmpty()){
            LocalDate firstDate=logs.get(0).date;
            LocalDate lastDate=logs.get(0).date;

            for(Log log:logs){
                LocalDate logDate=log.date;
                if(logDate.isBefore(firstDate))
                    firstDate=logDate;
                if(logDate.isAfter(lastDate))
                    lastDate=logDate;
            }
        }
    }

    public void analyzeLogs(){
        for(Log log:logs){
            log.analyzeLog();
        }
    }

    public LocalDate getBeginDate(){
        return beginDate;
    }

    public LocalDate getEndDate(){
        return endDate;
    }
}
