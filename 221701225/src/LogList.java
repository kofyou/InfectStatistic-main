import java.io.File;
import java.util.ArrayList;

public class LogList {
    ArrayList<Log> logs;

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

        for(Log log:logs){
            log.logData();
        }
    }
}
