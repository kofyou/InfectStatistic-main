/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    static class BasicInformation{
        public static String[] nameOfAreas = {"全国","安徽", "北京","重庆","福建","甘肃", "广东", "广西", "贵州", "海南",
                "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海",
                "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};
        public static AreaInformation[] Areas=new AreaInformation[32];
    }

    static class AreaInformation{
        String nameOfArea;    // 区域名
        int numberOfInfectedPatients;   // 感染人数
        int numberOfSuspectedPatients;  // 疑似人数
        int numberOfPeopleCured;    // 被治愈人数
        int numberOfDeaths;    // 死亡人数

        public AreaInformation(String nameOfArea){
            this.nameOfArea = nameOfArea;
            numberOfInfectedPatients = 0;
            numberOfPeopleCured = 0;
            numberOfSuspectedPatients = 0;
            numberOfDeaths = 0;
        }

        public void SetTheNumberOfInfectedPatients(int numberOfInfectedPatients){
            this.numberOfInfectedPatients=numberOfInfectedPatients;
        }

        public void SetThenNmberOfSuspectedPatients(int numberOfSuspectedPatients){
            this.numberOfSuspectedPatients=numberOfSuspectedPatients;
        }

        public void SetTheNumberOfPeopleCured(int numberOfPeopleCured){
            this.numberOfPeopleCured=numberOfPeopleCured;
        }

        public String OutputInformation(){
            String res=nameOfArea+" 感染患者"+numberOfInfectedPatients+"人 疑似患者"+
                    numberOfSuspectedPatients+"人 治愈"+numberOfPeopleCured+"人 死亡"+
                    numberOfDeaths+"人";

            return res;
        }
    }
    public static void main(String[] args){
        for (int i0 = 0;i0 < BasicInformation.nameOfAreas.length;i0++){
            BasicInformation.Areas[i0]=new AreaInformation(BasicInformation.nameOfAreas[i0]);
        }

    }
}
