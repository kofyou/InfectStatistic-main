import java.io.*;

/**
 * InfectStatistic
 * TODO
 *
 * @author 221701231_朱鸿昊
 * @version 1.0.5
 * @since 2020/2/16 19:28
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
                        BasicInformation.KeyOfOutput[BasicInformation.GetIndexOfArea(parameters[i0])] = 1;
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
        // 创建输出文件方法
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

        // 读取单个文件方法
        public static void ReadASingleFile(String path){
            File file = new File(path);
            StringBuilder result = new StringBuilder();
            try{
                //构造一个BufferedReader类来读取文件
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file),"UTF-8"));
                String line = null;
                while((line = reader.readLine())!=null){
                    if (!FileProcessor.ReadASingleLine(line)){
                        break;
                    }
                }
                reader.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        // 读取单行方法
        public static boolean ReadASingleLine(String line){
            String words[] = line.split(" ");
            String word = words[0];
            String number;  // 用于记录数字
            int indexOfArea = 0;    // 每行最开始的地区的数组索引

            // 首先判断这行是不是注释内容
            if (word.equals("//")){
                return false;
            }

            indexOfArea = BasicInformation.GetIndexOfArea(word);    // 获得地区索引

            BasicInformation.KeyOfOutput[indexOfArea] = 1;  // 把该地区的输出key设为1

            // 开始按照后面的输入操作地区类
            for (int i0 = 1;i0 < words.length;i0++) {
                switch (words[i0]){
                    case ("新增"):
                        i0++;
                        switch (words[i0]){
                            case ("感染患者"):
                                i0++;
                                number = words[i0].substring(0,words[i0].length()-1);
                                BasicInformation.Areas[0].numberOfInfectedPatients += Integer.parseInt(number);
                                BasicInformation.Areas[indexOfArea].numberOfInfectedPatients += Integer.parseInt(number);
                                break;
                            case ("疑似患者"):
                                i0++;
                                number = words[i0].substring(0,words[i0].length()-1);
                                BasicInformation.Areas[0].numberOfSuspectedPatients += Integer.parseInt(number);
                                BasicInformation.Areas[indexOfArea].numberOfSuspectedPatients += Integer.parseInt(number);
                                break;
                        }
                        break;
                    case ("治愈"):
                        i0++;
                        number = words[i0].substring(0,words[i0].length()-1);
                        BasicInformation.Areas[0].numberOfPeopleCured += Integer.parseInt(number);
                        BasicInformation.Areas[indexOfArea].numberOfPeopleCured += Integer.parseInt(number);
                        break;
                    case ("死亡"):
                        i0++;
                        number = words[i0].substring(0,words[i0].length()-1);
                        BasicInformation.Areas[0].numberOfDeaths += Integer.parseInt(number);
                        BasicInformation.Areas[indexOfArea].numberOfDeaths += Integer.parseInt(number);
                        break;
                }
            }
            return true;
        }
    }

    static class BasicInformation{
        public static String[] nameOfAreas = {"全国","安徽", "北京","重庆","福建","甘肃", "广东", "广西", "贵州", "海南",
                "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海",
                "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};
        public static AreaInformation[] Areas = new AreaInformation[32];
        public static int[] KeyOfOutput = new int[32];
        public static File outputFile;  // 输出文件

        public static int GetIndexOfArea(String nameOfArea){
            int res=0;
            for(int i0 = 0;i0 < 32;i0++){
                if (BasicInformation.nameOfAreas[i0].equals(nameOfArea)){
                    res = i0;
                    break;
                }
            }
            return res;
        }
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
            BasicInformation.Areas[i0] = new AreaInformation(BasicInformation.nameOfAreas[i0]);
            BasicInformation.KeyOfOutput[i0] = 0;
        }
        BasicInformation.KeyOfOutput[0] = 1;

        Controller.GetParameters(args); // 获取输入的参数
        //FileProcessor.CreateOutputFile();   // 创建输出文件
        System.out.println();
        FileProcessor.ReadASingleFile(Controller.inputLocation+"2020-01-22.log.txt");

        for (int i0 = 0;i0 < 32;i0++){
            if (BasicInformation.KeyOfOutput[i0] == 1){
                System.out.println(BasicInformation.Areas[i0].OutputInformation());
            }
        }

    }
}
