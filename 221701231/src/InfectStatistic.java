/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    static class Controller{
        static public String inputLocation;    // 输入文件夹位置
        static public String outputLocation;    // 输出文件夹位置
        static public String designatedProvince;  // 指定的省份
        static public String designatedDate;    // 指定日期
        static public int numberOfTypes = 0;    // 指定类型数量
        static public String designatedTypes[] =new String[4]; // 指定类型

        public static void GetParameters(String[] parameters){
            //获得输入信息
            for (int i0 = 0;i0<parameters.length;i0++){
                switch (parameters[i0]){
                    case ("-out"):
                        i0++;
                        Controller.outputLocation=parameters[i0];
                        break;
                    case ("-log"):
                        i0++;
                        Controller.inputLocation=parameters[i0];
                        break;
                    case ("-province"):
                        i0++;
                        Controller.designatedProvince=parameters[i0];
                        break;
                    case ("-date"):
                        i0++;
                        Controller.designatedDate=parameters[i0];
                        break;
                    case ("-type"):
                        i0++;
                        while (true){
                            if (parameters[i0].equals("ip")||parameters[i0].equals("sp")||
                                    parameters[i0].equals("cure")||parameters[i0].equals("dead")){
                                Controller.designatedTypes[Controller.numberOfTypes]=parameters[i0];
                                i0++;
                                Controller.numberOfTypes++;
                            }
                            else {
                                break;
                            }
                        }
                }
            }
        }

        public static void OutputParameters() {
            System.out.println(Controller.inputLocation);
            System.out.println(Controller.outputLocation);
            System.out.println(Controller.designatedDate);
            System.out.println(Controller.designatedProvince);
            for (int i0 = 0;i0 < Controller.numberOfTypes;i0++){
                System.out.print(Controller.designatedTypes[i0]+" ");
            }
        }
    }

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

        // 初始化区域类
        for (int i0 = 0;i0 < BasicInformation.nameOfAreas.length;i0++){
            BasicInformation.Areas[i0]=new AreaInformation(BasicInformation.nameOfAreas[i0]);
        }

        Controller.GetParameters(args);

        Controller.OutputParameters();

    }
}
