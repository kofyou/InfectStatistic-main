import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
public class Log {
    LocalDate date;
    File logFile;
    Country country;
    //记录该日志
    HashMap<String,DailyInfo> dailyInfos;

    public Log(String filePath){
        logFile=new File(filePath);
        String logName=logFile.getName();
        date=LocalDate.parse(logName.substring(0,10));
        System.out.println("日志对象已创建，日期："+date);

        country=Country.getInstance();
        dailyInfos=new HashMap<>();
        for(String province:Country.PROVINCES){
            DailyInfo dailyInfo=new DailyInfo(date);
            dailyInfos.put(province,dailyInfo);
        }
//        date=LocalDate.parse(filePath.substring())
    }

    public void logData(){
        FileInputStream fis = null;

        System.out.println("开始处理日志");
        try {
            fis = new FileInputStream(logFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                //注释不处理
                if(line.contains("//"))
                    continue;

                //下面是日志中八种情况的处理
                if(line.contains("新增") && line.contains("感染")) {
                    addInfected(line);
                    continue;
                }
                if(line.contains("新增") && line.contains("疑似")) {
                    addSuspected(line);
                    continue;
                }
                if(line.contains("流入") && line.contains("感染")) {
                    flowInfected(line);
                    continue;
                }
                if(line.contains("流入") && line.contains("疑似")) {
                    flowSuspected(line);
                    continue;
                }
                if(line.contains("死亡")) {
                    addDead(line);
                    continue;
                }
                if(line.contains("治愈")) {
                    addCured(line);
                    continue;
                }
                if(line.contains("确诊")){
                    suspectedToInfected(line);
                    continue;
                }
                if(line.contains("排除")) {
                    suspectedToHealthy(line);
                    continue;
                }
            }

            br.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("读取日志文件"+logFile.getName()+"时出现错误！");
            e.printStackTrace();
        }



    }

    public void addInfected(String line){
        String[] words=line.split(" ");
        String province=words[0];

    }

    public void addSuspected(String line){
        System.out.println("新增疑似");

    }

    public void flowInfected(String line){

    }

    public void flowSuspected(String line){

    }

    public void addDead(String line){

    }

    public void addCured(String line){

    }

    public void suspectedToInfected(String line){

    }

    public void suspectedToHealthy(String line){

    }

}
