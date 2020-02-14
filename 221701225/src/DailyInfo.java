import java.time.LocalDate;

//记录每日各项人数的变化
public class DailyInfo {
    LocalDate date;
    int infected;
    int suspected;
    int dead;
    int cured;

    DailyInfo(LocalDate date){
        this.date=date;
        infected=0;
        suspected=0;
        dead=0;
        cured=0;
    }

    public void changeInfected(int change){
        infected+=change;
    }

    public void changeSuspected(int change){
        suspected+=change;
    }

    public void changeDead(int change){
        dead+=change;
    }

    public void changeCured(int change){
        cured+=change;
    }

    @Override
    public String toString() {
        return "日期" + date +
                "\n新增感染" + infected +
                "\n新增疑似" + suspected +
                "\n新增死亡" + dead +
                "\n新增治愈" + cured;
    }
}
