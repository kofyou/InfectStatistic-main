import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;

public class Log {
    LocalDate date;
    File logFile;
    HashMap<String,DailyInfo> dailyInfos;

    public Log(String filePath){
        logFile=new File(filePath);
        String logName=logFile.getName();
        date=LocalDate.parse(logName.substring(0,10));
        System.out.println("日志对象已创建，日期："+date);
//        date=LocalDate.parse(filePath.substring())
    }


}
