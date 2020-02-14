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

    public DailyInfo add(DailyInfo dailyInfo2){
        //两份信息相加，以较晚的为相加后的日期
        if(this.date.isBefore(dailyInfo2.getDate()))
            this.date=dailyInfo2.getDate();

        this.infected+=dailyInfo2.infected;
        this.suspected+=dailyInfo2.suspected;
        this.dead+=dailyInfo2.dead;
        this.cured+=dailyInfo2.cured;

        return this;

    }

    public LocalDate getDate(){
        return this.date;
    }
    @Override
    public String toString() {
        return "感染患者" + infected + "人 疑似患者" + suspected + "人 治愈" + cured + "人 死亡" + dead + "人";
    }
}
