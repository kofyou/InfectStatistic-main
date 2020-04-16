import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * InfectStatistic
 * TODO
 *
 * @author 221701231_朱鸿昊
 * @version 1.1.3
 * @since 2020/2/20 16:50
 */
class InfectStatistic {
    static class Controller{
        static public String inputLocation;    // 输入文件夹位置
        static public String outputLocation;    // 输出文件夹位置
        static public int numberOfDesignatedProvince = 0;    // 指定类型数量
        static public String designatedProvince[] = new String[32];  // 指定的省份
        static public String designatedDate="-1";    // 指定日期
        static public int numberOfTypes = 0;    // 指定类型数量
        static public String designatedTypes[] = new String[4]; // 指定类型

        public static void GetParameters(String[] parameters){
            //获得输入信息
            for (int i0 = 0,l = parameters.length;i0 < l;i0++){
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
                        Statistics.KeyOfOutput[Statistics.GetIndexOfArea(parameters[i0])] = 1;
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
            Statistics.outputFile=new File(Controller.outputLocation);
            try {
                Statistics.outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void OutputToFile() throws IOException {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Controller.outputLocation,true),"UTF-8"));

            for (int i0 = 0;i0 < 32;i0++){
                if (Statistics.KeyOfOutput[i0] == 1){
                    writer.write(Statistics.Areas[i0].Output()+"\r\n");
                }
            }
            writer.write("// 该文档并非真实数据，仅供测试使用\r\n");
            writer.flush();//刷新内存，将内存中的数据立刻写出。
            writer.close();
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
            String number;  // 用于记录数
            int num;
            int indexOfArea = 0;    // 每行最开始的地区的数组索引
            int indexOfTarge = 0;

            if (words[0].equals("//")){
                return false;   // 首先判断这行是不是注释内容
            }

            indexOfArea = Statistics.GetIndexOfArea(words[0]);    // 获得地区索引

            Statistics.KeyOfOutput[indexOfArea] = 1;  // 把该地区的输出key设为1

            // 开始按照后面的输入操作地区类
            for (int i0 = 1,l = words.length;i0 < l;i0++) {
                switch (words[i0]){
                    case ("新增"):
                        i0++;
                        switch (words[i0]){
                            case ("感染患者"):
                                i0++;
                                num = FileProcessor.GetNumber(words[i0]);
                                Statistics.AddInfectedPatients(num,indexOfArea);
                                break;
                            case ("疑似患者"):
                                i0++;
                                num = FileProcessor.GetNumber(words[i0]);
                                Statistics.AddSuspectedPatients(num,indexOfArea);
                                break;
                        }
                        break;
                    case ("治愈"):
                        i0++;
                        num = FileProcessor.GetNumber(words[i0]);
                        Statistics.AddCured(num,indexOfArea);
                        break;
                    case ("死亡"):
                        i0++;
                        num = FileProcessor.GetNumber(words[i0]);
                        Statistics.AddDeaths(num,indexOfArea);
                        break;
                    case ("排除"):
                        i0 += 2;
                        num = FileProcessor.GetNumber(words[i0]);
                        Statistics.Areas[0].numberOfSuspectedPatients -= num;
                        Statistics.Areas[indexOfArea].numberOfSuspectedPatients -= num;
                        break;
                    case ("感染患者"):
                        i0 += 2;
                        indexOfTarge = Statistics.GetIndexOfArea(words[i0]);
                        i0++;
                        num = FileProcessor.GetNumber(words[i0]);
                        Statistics.Areas[indexOfArea].numberOfInfectedPatients -= num;
                        Statistics.Areas[indexOfTarge].numberOfInfectedPatients += num;
                        break;
                    case ("疑似患者"):
                        i0++;
                        switch (words[i0]){
                            case ("确诊感染"):
                                i0++;
                                num = FileProcessor.GetNumber(words[i0]);
                                Statistics.AddInfectedPatients(num,indexOfArea);
                                Statistics.Areas[0].numberOfSuspectedPatients -= num;
                                Statistics.Areas[indexOfArea].numberOfSuspectedPatients -= num;
                                break;
                            case ("流入"):
                                i0 ++;
                                indexOfTarge = Statistics.GetIndexOfArea(words[i0]);
                                i0++;
                                num = FileProcessor.GetNumber(words[i0]);
                                Statistics.Areas[indexOfArea].numberOfSuspectedPatients -= num;
                                Statistics.Areas[indexOfTarge].numberOfSuspectedPatients += num;
                        }
                }
            }
            return true;
        }

        public static void ReadFiles(String path) {
            ArrayList<String> listFileName = new ArrayList<String>();
            GetAllFileName(path, listFileName);
            for (String name : listFileName) {
                if (FileProcessor.ComparingTheDate(name)) {
                    FileProcessor.ReadASingleFile(name);
                }
            }
        }

        public static void GetAllFileName(String path,ArrayList<String> listFileName){
            File file = new File(path);
            File [] files = file.listFiles();
            String [] names = file.list();
            if(names != null){
                String [] completNames = new String[names.length];
                for(int i=0;i<names.length;i++){
                    completNames[i]=path+names[i];
                }
                listFileName.addAll(Arrays.asList(completNames));
            }
            for(File a:files){
                if(a.isDirectory()){//如果文件夹下有子文件夹，获取子文件夹下的所有文件全路径。
                    GetAllFileName(a.getAbsolutePath()+"\\",listFileName);
                }
            }
        }

        public static int GetNumber(String number){
            return Integer.parseInt(number.substring(0,number.length()-1));
        }

        public static boolean ComparingTheDate(String name){
            int index=0;
            if (Controller.designatedDate.equals("-1")){
                return true;
            }
            else {
                for (int i0 = name.length()-1;i0 >= 0;i0--){
                    if (name.charAt(i0)=='/'){
                        index = i0+1;
                        break;
                    }
                }
                name = name.substring(index,name.length()-8);
                String dates[] = name.split("-");
                String designatedDate[] = Controller.designatedDate.split("-");
                for (int i0 = 0;i0 < 3;i0++){
                    if (Integer.valueOf(designatedDate[i0])<Integer.valueOf(dates[i0])){
                        return false;
                    }
                }

            }
            return true;
        }
    }

    static class Statistics {
        public static String[] nameOfAreas = {"全国","安徽", "北京","重庆","福建","甘肃", "广东", "广西", "贵州", "海南",
                "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海",
                "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};
        public static Map<String,Integer> indexOfAreas=new HashMap<>();
        public static AreaInformation[] Areas = new AreaInformation[32];
        public static int[] KeyOfOutput = new int[32];
        public static File outputFile;  // 输出文件

        public static int GetIndexOfArea(String nameOfArea){
            if (!Statistics.indexOfAreas.containsKey(nameOfArea)){
                return 0;
            }
            return Statistics.indexOfAreas.get(nameOfArea);
        }

        public static void AddInfectedPatients(int num,int indexOfArea){
            Statistics.Areas[0].numberOfInfectedPatients += num;
            Statistics.Areas[indexOfArea].numberOfInfectedPatients += num;
        }

        public static void AddSuspectedPatients(int num,int indexOfArea){
            Statistics.Areas[0].numberOfSuspectedPatients += num;
            Statistics.Areas[indexOfArea].numberOfSuspectedPatients += num;
        }

        public static void AddCured(int num,int indexOfArea){
            Statistics.Areas[0].numberOfPeopleCured += num;
            Statistics.Areas[0].numberOfInfectedPatients -=num;
            Statistics.Areas[indexOfArea].numberOfPeopleCured += num;
            Statistics.Areas[indexOfArea].numberOfInfectedPatients -= num;
        }

        public static void AddDeaths(int num,int indexOfArea){
            Statistics.Areas[0].numberOfDeaths += num;
            Statistics.Areas[0].numberOfInfectedPatients -=num;
            Statistics.Areas[indexOfArea].numberOfDeaths += num;
            Statistics.Areas[indexOfArea].numberOfInfectedPatients -= num;
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

        public String Output(){
            String res=nameOfArea;
            for (int i = 0;i < Controller.numberOfTypes;i++){
                switch (Controller.designatedTypes[i]){
                    case ("ip"):
                        res += " 感染患者"+numberOfInfectedPatients+"人";
                        break;
                    case ("sp"):
                        res += " 疑似患者"+numberOfSuspectedPatients+"人";
                        break;
                    case ("cure"):
                        res += " 治愈"+numberOfPeopleCured+"人";
                        break;
                    case ("dead"):
                        res += " 死亡"+numberOfDeaths+"人";
                        break;
                }
            }
            return res;
        }
    }


    public static void main(String[] args){

        // 初始化区域类
        for (int i0 = 0,l=Statistics.nameOfAreas.length; i0 < l; i0++){
            Statistics.Areas[i0] = new AreaInformation(Statistics.nameOfAreas[i0]);
            Statistics.indexOfAreas.put(Statistics.nameOfAreas[i0],i0);
        }
        Statistics.KeyOfOutput[0] = 1;
        Controller.GetParameters(args); // 获取输入的参数
        FileProcessor.CreateOutputFile();   // 创建输出文件
        FileProcessor.ReadFiles(Controller.inputLocation);
        try {
            FileProcessor.OutputToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
