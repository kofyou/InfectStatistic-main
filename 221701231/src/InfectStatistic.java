import java.io.File;
import java.io.IOException;

/**
 * InfectStatistic
 * TODO
 *
 * @author 221701231_朱鸿昊
 * @version 1.0.4
 * @since 2020/2/16 17:01
 */
class InfectStatistic {
    static class Controller{
        static public String inputLocation;    // 输入文件夹位置
        static public String outputLocation;    // 输出文件夹位置
        static public int numberOfDesignatedProvince = 0;    // 指定类型数量
        static public String designatedProvince[] = new String[32];  // 指定的省份
        static public String designatedDate;    // 指定日期
        static public int numberOfTypes = 0;    // 指定类型数量
        static public String designatedTypes[] = new String[4]; // 指定类型

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
                        Controller.designatedProvince[numberOfDesignatedProvince]=parameters[i0];
                        numberOfDesignatedProvince++;
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
                                i0--;
                                break;
                            }
                        }
                        break;
                    case ("list"):
                        break;
                    default:
                        Controller.designatedProvince[numberOfDesignatedProvince]=parameters[i0];
                        numberOfDesignatedProvince++;
                }
            }

            // 处理没有输入-type的情况
            if (Controller.numberOfTypes == 0){
                Controller.numberOfTypes = 4;
                designatedTypes=new String[]{"ip","sp","cure","dead"};
            }

            // 处理没有输入-province的情况
            if (Controller.numberOfDesignatedProvince == 0){
                Controller.numberOfDesignatedProvince = 1;
                designatedProvince=new String[]{"全国"};
            }
        }

        public static void OutputParameters() {
            System.out.println("输入位置： " + Controller.inputLocation);
            System.out.println("输出位置： " + Controller.outputLocation);
            System.out.println("指定日期： " + Controller.designatedDate);
            System.out.print("指定区域： ");
            for (int i0 = 0;i0 < Controller.numberOfDesignatedProvince;i0++){
                System.out.print(Controller.designatedProvince[i0]+" ");
            }
            System.out.println();
            System.out.print("指定类型： ");
            for (int i0 = 0;i0 < Controller.numberOfTypes;i0++){
                System.out.print(Controller.designatedTypes[i0]+" ");
            }
        }
    }

    static class FileProcessor{
        public static void CreateOutputFile(){
            String path=Controller.outputLocation;
            String reversedFileName="";
            String fileName="";

            //获取输出文件名
            for(int i0 = Controller.outputLocation.length()-1;i0 > 0;i0--){
                if(Controller.outputLocation.charAt(i0)=='/'){
                    break;
                }
                reversedFileName += Controller.outputLocation.charAt(i0);
            }
            for(int i0 = reversedFileName.length()-1;i0 >= 0;i0--){
                fileName+=reversedFileName.charAt(i0);
            }

            //获取输出文件路径
            path=path.substring(0,path.length()-fileName.length());
            File pathOfFile=new File(path);

            if(!pathOfFile.exists())    //如果文件路径不存在则创建
            {
                pathOfFile.mkdirs();
            }

            //创建文件
            BasicInformation.outputFile=new File(Controller.outputLocation);
            try {
                BasicInformation.outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class BasicInformation{
        public static String[] nameOfAreas = {"全国","安徽", "北京","重庆","福建","甘肃", "广东", "广西", "贵州", "海南",
                "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海",
                "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};
        public static AreaInformation[] Areas=new AreaInformation[32];
        public static File outputFile;
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

        Controller.GetParameters(args); // 获取输入的参数
        Controller.OutputParameters();
        FileProcessor.CreateOutputFile();   // 创建输出文件
    }
}
