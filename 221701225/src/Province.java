import java.time.LocalDate;
import java.util.ArrayList;

public class Province {
    String name;
    ArrayList<DailyInfo> dailyInfos;
    //标记是否在日志中出现过
    boolean hasOccured;
    DailyInfo totalInfo=null;


    Province(String name){
        this.name=name;
        dailyInfos=new ArrayList<>();
        hasOccured=false;
    }

    public void addDailyInfo(DailyInfo dailyInfo){
        dailyInfos.add(dailyInfo);
    }

    public void printAllInfo(){
        for(DailyInfo info:dailyInfos){
            System.out.println(info.toString());
        }
    }

    public DailyInfo getStatistic(LocalDate beginDate,LocalDate endDate){
        //未进行统计就计算一遍
        if(totalInfo==null) {
            DailyInfo totalInfo = new DailyInfo(endDate);

            for (DailyInfo info : dailyInfos) {
                if (info.getDate().isBefore(endDate) ||info.getDate().isEqual(endDate)) {
                    totalInfo.add(info);
                }
            }

            this.totalInfo = totalInfo;
            return totalInfo;
        }
        else
            return this.totalInfo;
    }
}
