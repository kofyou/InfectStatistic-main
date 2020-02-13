import java.util.ArrayList;

public class Province {
    String name;
    ArrayList<DailyInfo> dailyInfos;
    //标记是否在日志中出现过
    boolean hasOccured;

    Province(String name){
        this.name=name;
        dailyInfos=new ArrayList<>();
        hasOccured=false;
    }
}
