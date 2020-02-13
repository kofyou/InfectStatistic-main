import org.omg.PortableInterceptor.Interceptor;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author 会飞的大野鸡
 * @version 1
 * @since 2020-2-10
 */

class InfectStatistic {
    public static void main(String[] args) throws IOException {
//        int a = new CommandJudge().judge(args);
//        new Test().test();
//        new Test().files();
        new Test().toFile(new Test().files());
//        new Execute().log(args);
//        new InfectStatisticApplication().log(args);
    }
}

class Province{
    private String name;
    private int ip = 0;
    private int sp = 0;
    private int cure = 0;
    private int dead = 0;
    private boolean ipCheck =false;
    private boolean spCheck = false;
    private boolean cureCheck = false;
    private boolean deadCheck = false;

    public Province(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIp() {
        return ip;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public int getCure() {
        return cure;
    }

    public void setCure(int cure) {
        this.cure = cure;
    }

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public boolean isIpCheck() {
        return ipCheck;
    }

    public void setIpCheck(boolean ipCheck) {
        this.ipCheck = ipCheck;
    }

    public boolean isSpCheck() {
        return spCheck;
    }

    public void setSpCheck(boolean spCheck) {
        this.spCheck = spCheck;
    }

    public boolean isCureCheck() {
        return cureCheck;
    }

    public void setCureCheck(boolean cureCheck) {
        this.cureCheck = cureCheck;
    }

    public boolean isDeadCheck() {
        return deadCheck;
    }

    public void setDeadCheck(boolean deadCheck) {
        this.deadCheck = deadCheck;
    }
}


class Test{
    public void test(){
        int i = 1;
        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        hashMap.put(i++,"安徽"); hashMap.put(i++,"北京"); hashMap.put(i++,"重庆"); hashMap.put(i++,"福建"); hashMap.put(i++,"甘肃");
        hashMap.put(i++,"广东"); hashMap.put(i++,"广西"); hashMap.put(i++,"贵州"); hashMap.put(i++,"海南"); hashMap.put(i++,"河北");
        hashMap.put(i++,"河南"); hashMap.put(i++,"黑龙江"); hashMap.put(i++,"湖北"); hashMap.put(i++,"湖南"); hashMap.put(i++,"吉林");
        hashMap.put(i++,"江苏"); hashMap.put(i++,"江西"); hashMap.put(i++,"辽宁"); hashMap.put(i++,"内蒙古"); hashMap.put(i++,"宁夏");
        hashMap.put(i++,"青海"); hashMap.put(i++,"山东"); hashMap.put(i++,"山西"); hashMap.put(i++,"陕西"); hashMap.put(i++,"上海");
        hashMap.put(i++,"四川"); hashMap.put(i++,"天津"); hashMap.put(i++,"西藏"); hashMap.put(i++,"新疆"); hashMap.put(i++,"云南");
        hashMap.put(i++,"浙江");

        java.util.List<Province> provinceList = new LinkedList<Province>();
        for (int j = 0 ; j < hashMap.size() ; j++){
//            provinceList.set(j).setName(hashMap.get(j));
            provinceList.add(new Province(hashMap.get(j)));
        }

        for (int j = 0 ; j < provinceList.size() ; j++){
            System.out.println(provinceList.get(j).getName());
        }


//        System.out.println(hashMap.get());
//        System.out.println();
    }

    public String[] files(){
        File file = new File("/Users/a/Desktop/近期作业/InfectStatistic-main/example/log");
        File[] files = file.listFiles();
//        java.util.List<String> filesName = new LinkedList<>();
        String[] filesName = new String[files.length];
        for (int i = 0 ; i < files.length ; i++){
            filesName[i] = files[i].getName();
        }

        for (int i = 0; i < filesName.length; i++) {
            for (int j = 0; j < filesName.length - i - 1; j++) {
                if (filesName[j].compareTo(filesName[j + 1]) > 0) {
                    String s = filesName[j];
                    filesName[j] = filesName[j + 1];
                    filesName[j + 1] = s;
                }
            }
        }

        System.out.println(filesName[0].compareTo(filesName[1]));

        for (int i = 0 ; i < files.length ; i++){
            System.out.println(filesName[i]);
        }
        return filesName;
    }

    public void changeList(LinkedList<Province> provinces){
        provinces.get(0).setName("aaa");
    }

    public void toFile(String[] filesName ) throws IOException {
        int i = 0;
        HashMap<String , Integer> hashMap = new HashMap<String , Integer>();
        hashMap.put("安徽",i++); hashMap.put("北京",i++); hashMap.put("重庆",i++); hashMap.put("福建",i++); hashMap.put("甘肃",i++);
        hashMap.put("广东",i++); hashMap.put("广西",i++); hashMap.put("贵州",i++); hashMap.put("海南",i++); hashMap.put("河北",i++);
        hashMap.put("河南",i++); hashMap.put("黑龙江",i++); hashMap.put("湖北",i++); hashMap.put("湖南",i++); hashMap.put("吉林",i++);
        hashMap.put("江苏",i++); hashMap.put("江西",i++); hashMap.put("辽宁",i++); hashMap.put("内蒙古",i++); hashMap.put("宁夏",i++);
        hashMap.put("青海",i++); hashMap.put("山东",i++); hashMap.put("山西",i++); hashMap.put("陕西",i++); hashMap.put("上海",i++);
        hashMap.put("四川",i++); hashMap.put("天津",i++); hashMap.put("西藏",i++); hashMap.put("新疆",i++); hashMap.put("云南",i++);
        hashMap.put("浙江",i++);

        i = 0;
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(i++,"安徽"); map.put(i++,"北京"); map.put(i++,"重庆"); map.put(i++,"福建"); map.put(i++,"甘肃");
        map.put(i++,"广东"); map.put(i++,"广西"); map.put(i++,"贵州"); map.put(i++,"海南"); map.put(i++,"河北");
        map.put(i++,"河南"); map.put(i++,"黑龙江"); map.put(i++,"湖北"); map.put(i++,"湖南"); map.put(i++,"吉林");
        map.put(i++,"江苏"); map.put(i++,"江西"); map.put(i++,"辽宁"); map.put(i++,"内蒙古"); map.put(i++,"宁夏");
        map.put(i++,"青海"); map.put(i++,"山东"); map.put(i++,"山西"); map.put(i++,"陕西"); map.put(i++,"上海");
        map.put(i++,"四川"); map.put(i++,"天津"); map.put(i++,"西藏"); map.put(i++,"新疆"); map.put(i++,"云南");
        map.put(i++,"浙江");

        LinkedList<Province> provinceList = new LinkedList<Province>();
        for (int j = 0 ; j < map.size() ; j++){
//            provinceList.set(j).setName(hashMap.get(j));
            provinceList.add(new Province(map.get(j)));
        }

        System.out.println(map);

        System.out.println(hashMap);

        System.out.println(hashMap.get("福建"));

        System.out.println("省份个数:" + provinceList.size());

        for (int j = 0 ; j < provinceList.size() ; j++){
            System.out.println(provinceList.get(j).getName());
        }

//        changeList(provinceList);
//        System.out.println(provinceList.get(0).getName());

        java.util.List<Integer> infectProvinces = new LinkedList<>();

        String log1 = "(\\S+) 新增 感染患者 (\\d+)人";
        String log2 = "(\\S+) 新增 疑似患者 (\\d+)人";
        String log3 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
        String log4 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
        String log5 = "(\\S+) 死亡 (\\d+)人";
        String log6 = "(\\S+) 治愈 (\\d+)人";
        String log7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
        String log8 = "(\\S+) 排除 疑似患者 (\\d+)人";
        String regEx="[^0-9]";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        for (int j = 0 ; j < filesName.length ; j++) {
            fis = new FileInputStream("/Users/a/Desktop/Infectlog/" + filesName[j]);
            isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
            br = new BufferedReader(isr);
            String str = "";
            String str1 = "";
            while ((str = br.readLine()) != null) {
                str1 = "";
                str1 += str;
                System.out.println(str1);
                String[] provinces = str1.split(" ");
//                System.out.println(provinces.length);
                String s = provinces[provinces.length - 1].replaceAll(regEx , "");
//                System.out.println(s);
                int peopleNumber = 0;

                if (!s.isEmpty())
                    peopleNumber = Integer.parseInt(s);
//                System.out.println(peopleNumber);
                if (Pattern.matches(log1 , str1)) {
                    provinceList.get(hashMap.get(provinces[0])).setIp(provinceList.get(hashMap.get(provinces[0])).getIp() + peopleNumber);
                    if (!infectProvinces.contains(hashMap.get(provinces[0])))
                        infectProvinces.add(hashMap.get(provinces[0]));
                    System.out.println(1);
                }

                if (Pattern.matches(log2 , str1)) {
                    provinceList.get(hashMap.get(provinces[0])).setSp(provinceList.get(hashMap.get(provinces[0])).getSp() + peopleNumber);
                    if (!infectProvinces.contains(hashMap.get(provinces[0])))
                        infectProvinces.add(hashMap.get(provinces[0]));
                    System.out.println(2);
                }

                if (Pattern.matches(log3 , str1)) {
                    
                    System.out.println(3);
                }

                if (Pattern.matches(log4 , str1)) {
                    System.out.println(4);
                }

                if (Pattern.matches(log5 , str1)) {
                    System.out.println(5);
                }

                if (Pattern.matches(log6 , str1)) {
                    System.out.println(6);
                }

                if (Pattern.matches(log7 , str1)) {
                    System.out.println(7);
                }

                if (Pattern.matches(log8 , str1)) {
                    System.out.println(8);
                }

            }

        }

        System.out.println("被影响的省份个数" + infectProvinces.size());

        for (int j = 0 ; j < infectProvinces.size() ; j++){
            System.out.println(infectProvinces.get(j));
        }
        System.out.println("总省份个数" + provinceList.size());
        for (int j = 0 ; j < infectProvinces.size() ; j++){
            System.out.println(provinceList.get(infectProvinces.get(j)).getName());
        }

        fis.close();
        isr.close();
        br.close();
    }
}


/*
*
* 用于正则表达式
* @parameter  参数名
* @date 日期样式
*
* */
class InfectStatisticApplication{

  public void log(String[] args){
      int judgeNum = new CommandJudge().judge(args);
      if (judgeNum == 1){
          System.out.println("judgeNum: " + judgeNum);
          new ListCommand().Command(args);
      }
      if (judgeNum == 2){

      }

      if (judgeNum == 3){

      }
  }

}


class REUtil{
    public boolean checkParameter(String a){
        String parameter = "^\\-[a-z]+";
        return Pattern.matches(parameter , a);
    }

    public boolean checkDate(String a){
        String date = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-" +
                "(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|" +
                "(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|" +
                "((0[48]|[2468][048]|[3579][26])00))-02-29)(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|" +
                "[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|" +
                "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|" +
                "((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
        return Pattern.matches(date,a);
    }

    public boolean checkProvinces(String a){
        String province = "全国|北京|天津|上海|重庆|河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽|福建|江西|山东|" +
                "河南|湖北|湖南|广东|海南|四川|贵林|云南|陕西|甘肃|青海|台湾|内蒙古|广西|西藏|宁夏|新疆|香港|澳门";
        return Pattern.matches(province,a);
    }

    public boolean checkLogFile(String a){
        String logFile = "((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-" +
                "(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|" +
                "(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|" +
                "((0[48]|[2468][048]|[3579][26])00))-02-29)(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|" +
                "[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|" +
                "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|" +
                "((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\.log\\.txt";
        return Pattern.matches(logFile,a);
    }

    public boolean checkType(String a){
        String type = "ip|sp|cure|dead";
        return Pattern.matches(type ,a);
    }
}


//还差判断-out的路径是否能存在
class CommandJudge{

    public int judge(String[] args){
//      返回值0为语法错误，1为list语句，2为help,3为present
        java.util.List<Integer> integers = new LinkedList<Integer>();//存储参数的下标
        if (!args[0].equals("list") && !args[0].equals("help") && !args[0].equals("present")){
            System.out.println("Command " + args[0] + " not found");
            return 0;
        }
        else {
            /*
             * list语法参数
             * -log 必选  指定所需文件的目录
             * -out 必选  指定输出的文件
             * -type ip sp cure dead 感染患者 疑似患者 治愈 死亡患者
             * -date 2020-1-1
             * -province 福建 全国
             * */
            if (args[0].equals("list")){
                for (int i = 0 ; i < args.length ; i++){
                    if (new REUtil().checkParameter(args[i])){
                        integers.add(i);
                    }
                }
//              不允许不存在参数的情况
                if (integers.size() == 0){
                    System.out.println("请输入相应的参数及参数的选项");
                    return 0;
                }

//              list之后第一个必须是语法的参数
                if (integers.get(0) != 1){
                    System.out.println("Error Command1");
                    return 0;
                }

//              必须存在-out 和 -log 两个参数
                int ExistMust = 0;
                for (int i = 0 ; i < integers.size() ; i++){
                    if (args[integers.get(i)].equals("-log")){
                        ExistMust++;
                    }
                    if (args[integers.get(i)].equals("-out")){
                        ExistMust++;
                    }
                }
                if (ExistMust < 2){
                    System.out.println("必须存在参数-log和参数-out");
                    return 0;
                }

                for (int i = 0 ; i < integers.size() ; i++){
//                  两个参数之间必须有间隔
                    if (i != integers.size()-1){
                        if ((integers.get(i+1) - integers.get(i)) < 1 ){
                            System.out.println("Error Command2");
                            return 0;
                        }
                    }
                    else {
                        if (integers.get(i) >= args.length -1){
                            System.out.println("Error Command3");
                            return 0;
                        }
                    }
                }

//              参数需要特定的这几种形式
                for (int i = 0 ; i < integers.size() ; i++){
                    if (!args[integers.get(i)].equals("-log") && !args[integers.get(i)].equals("-out") &&
                            !args[integers.get(i)].equals("-type") && !args[integers.get(i)].equals("-date") &&
                            !args[integers.get(i)].equals("-province")){
                        System.out.println("Error Command4");
                        return 0;
                    }
                }

                int outJudge = 0, logJudge = 0, dateJudge = 0, typeJudge = 0, provinceJudge = 0;
//              具体的形式
                for (int i = 0 ; i < integers.size() ; i++){
                    /*
                     * -log 后面只能跟一个参数
                     *     参数必须是一个目录文件
                     *     目录文件下面必须存在有形如YYYY-mm-dd.log.txt的文件
                     * */
                    if (args[integers.get(i)].equals("-log")){
                        logJudge++;
                        String logInputPath;
                        if ((i+1) == integers.size()){
                            if (args.length-2 != integers.get(i)){
                                System.out.println("Error Command6");
                            }
                            File file = new File(args[integers.get(i)+1]);
                            if (!file.isDirectory()){
                                System.out.println("It should be a directory");
                            }
                            else {
                                File[] fileList = file.listFiles();
                                int judge = 0;
                                for (int j = 0 ; j < fileList.length ; j++){
                                    if (new REUtil().checkLogFile(fileList[j].getName())){
                                        judge++;
                                    }
                                }
                                if (judge == 0){
                                    System.out.println("File of log not found");
                                    return 0;
                                }
                            }
                        }
                        else {
                            if ((integers.get(i+1) - integers.get(i)) != 2){
                                System.out.println("Error Command5");
                                return 0;
                            }
                            File f = new File(args[integers.get(i)+1]);
                            if (!f.isDirectory()){
                                System.out.println("It should be a directory");
                                return 0;
                            }
                            else {
                                File[] files = f.listFiles();
                                int judge = 0;
                                for (int j = 0 ; j < files.length ; j++){
                                    if (new REUtil().checkLogFile(files[j].getName())){
                                        judge++;
                                    }
                                }
                                if (judge == 0){
                                    System.out.println("File of log not found");
                                    return 0;
                                }
                            }
                        }
                    }


                    if (args[integers.get(i)].equals("-out")){
                        outJudge++;
                        if ((i+1) == integers.size()){
                            if (args.length-2 != integers.get(i)){
                                System.out.println("Error Command7");
                            }
                            File f = new File(args[integers.get(i) + 1]);
                            if (f.isDirectory()){
                                System.out.println("It should be a file");
                                return 0;
                            }
                        }
                        else {
                            File f = new File(args[integers.get(i) + 1]);
                            if (f.isDirectory()){
                                System.out.println("It should be a file");
                                return 0;
                            }
                            if ((integers.get(i+1) - integers.get(i)) != 2){
                                System.out.println("Error Commad8");
                                return 0;
                            }
                        }
                    }

                    if (args[integers.get(i)].equals("-date")){
                        dateJudge++;
                        if ((i+1) == integers.size()){
                            if (args.length-2 != integers.get(i)){
                                System.out.println("Error Command9");
                                return 0;
                            }
                            if (!new REUtil().checkDate(args[integers.get(i)+1])){
                                System.out.println("Please input correct date");
                                return 0;
                            }
                        }
                        else {
                            if ((integers.get(i+1) - integers.get(i)) != 2){
                                System.out.println("Error Command10");
                                return 0;
                            }
                            if (!new REUtil().checkDate(args[integers.get(i)+1])){
                                System.out.println("Please input correct date");
                                return 0;
                            }
                        }
                    }

                    if (args[integers.get(i)].equals("-type")){
                        typeJudge++;
                        if ((i+1) == integers.size()){
                            String[] types = new String[args.length - integers.get(i) - 1];
                            for (int j = integers.get(i)+1 ; j < args.length ; j++){
                                types[j - (integers.get(i) + 1)] = args[j];
                            }
                            for (int j = 0 ; j < types.length ; j++){
//                              System.out.println(types[j]);
                                if (!new REUtil().checkType(types[j])){
                                    System.out.println("Please input correct type");
                                    return 0;
                                }
                            }
                        }
                        else {
                            if ((integers.get(i+1) - integers.get(i)) < 2){
                                System.out.println("Error Type");
                                return 0;
                            }
                            String[] types = new String[integers.get(i+1) - integers.get(i) - 1];
                            for (int j = integers.get(i) + 1 ; j < integers.get(i + 1) ; j++){
                                types[j - integers.get(i) - 1] = args[j];
                            }
                            for (int j = 0 ; j < types.length ; j++){
//                              System.out.println(types[j]);
                                if (!new REUtil().checkType(types[j])){
                                    System.out.println("Please input correct type");
                                    return 0;
                                }
                            }
                        }
                    }

                    if (args[integers.get(i)].equals("-province")){
                        provinceJudge++;
                        if ((i+1) == integers.size()){
                            String[] provinces = new String[args.length - integers.get(i) - 1];
                            for (int j = integers.get(i)+1 ; j < args.length ; j++){
                                provinces[j - (integers.get(i) + 1)] = args[j];
                            }
                            for (int j = 0 ; j < provinces.length ; j++){
//                              System.out.println(types[j]);
                                if (!new REUtil().checkProvinces(provinces[j])){
                                    System.out.println("Please input correct province");
                                    return 0;
                                }
                            }
                        }
                        else {
                            if ((integers.get(i+1) - integers.get(i)) < 2){
                                System.out.println("Error Type");
                                return 0;
                            }
                            String[] provinces = new String[integers.get(i+1) - integers.get(i) - 1];
                            for (int j = integers.get(i) + 1 ; j < integers.get(i + 1) ; j++){
                                provinces[j - integers.get(i) - 1] = args[j];
                            }
                            for (int j = 0 ; j < provinces.length ; j++){
//                              System.out.println(provinces[j]);
                                if (!new REUtil().checkProvinces(provinces[j])){
                                    System.out.println("Please input correct province");
                                    return 0;
                                }
                            }
                        }
                    }

                    if(outJudge > 1 || logJudge > 1 || dateJudge > 1 || typeJudge > 1 || provinceJudge > 1){
                        System.out.println("Parameter repeat");
                    }
                }
                return 1;
            }

            if (args[0].equals("help")){
                if (args.length > 1){
                    System.out.println("Command help don't need parameter");
                    return 0;
                }
                return 2;
            }

            /*
             * -log
             * -out
             * -type
             * -province
             *
             * */

            if (args[0].equals("present")){

            }
        }

        return 4;
    }


    public int foundSpecialIndex(String[] args , String special){
        for (int i = 0 ; i < args.length ; i++){
            if (args[i].equals(special)){
                return i;
            }
        }
        return 0;
    }



    public boolean foundSpecial(String[] args , String special){
        for (int i = 0 ; i < args.length ; i++){
            if (args[i].equals(special))
                return true;
        }
        return false;
    }

}


/*
*
* list命令类
*
* */
class ListCommand{
    public void Command(String[] args){
        int i = 0;
        HashMap<String , Integer> hashMap = new HashMap<String , Integer>();
        hashMap.put("安徽",i++); hashMap.put("北京",i++); hashMap.put("重庆",i++); hashMap.put("福建",i++); hashMap.put("甘肃",i++);
        hashMap.put("广东",i++); hashMap.put("广西",i++); hashMap.put("贵州",i++); hashMap.put("海南",i++); hashMap.put("河北",i++);
        hashMap.put("河南",i++); hashMap.put("黑龙江",i++); hashMap.put("湖北",i++); hashMap.put("湖南",i++); hashMap.put("吉林",i++);
        hashMap.put("江苏",i++); hashMap.put("江西",i++); hashMap.put("辽宁",i++); hashMap.put("内蒙古",i++); hashMap.put("宁夏",i++);
        hashMap.put("青海",i++); hashMap.put("山东",i++); hashMap.put("山西",i++); hashMap.put("陕西",i++); hashMap.put("上海",i++);
        hashMap.put("四川",i++); hashMap.put("天津",i++); hashMap.put("西藏",i++); hashMap.put("新疆",i++); hashMap.put("云南",i++);
        hashMap.put("浙江",i++);

        i = 0;
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(i++,"安徽"); map.put(i++,"北京"); map.put(i++,"重庆"); map.put(i++,"福建"); map.put(i++,"甘肃");
        map.put(i++,"广东"); map.put(i++,"广西"); map.put(i++,"贵州"); map.put(i++,"海南"); map.put(i++,"河北");
        map.put(i++,"河南"); map.put(i++,"黑龙江"); map.put(i++,"湖北"); map.put(i++,"湖南"); map.put(i++,"吉林");
        map.put(i++,"江苏"); map.put(i++,"江西"); map.put(i++,"辽宁"); map.put(i++,"内蒙古"); map.put(i++,"宁夏");
        map.put(i++,"青海"); map.put(i++,"山东"); map.put(i++,"山西"); map.put(i++,"陕西"); map.put(i++,"上海");
        map.put(i++,"四川"); map.put(i++,"天津"); map.put(i++,"西藏"); map.put(i++,"新疆"); map.put(i++,"云南");
        map.put(i++,"浙江");

        LinkedList<Province> provinceList = new LinkedList<Province>();
        for (int j = 0 ; j < map.size() ; j++){
//            provinceList.set(j).setName(hashMap.get(j));
            provinceList.add(new Province(map.get(j)));
        }


        String logPath = args[new CommandJudge().foundSpecialIndex(args , "-log") + 1];
        File file = new File(logPath);
        File[] files = file.listFiles();
        String[] filesName = new String[files.length];
        for (int j = 0 ; j < files.length ; j++){
            filesName[j] = files[j].getName();
        }
        sort(filesName);
        for (int j = 0 ; j < filesName.length ; j++){
            System.out.println(filesName[j]);
        }
        LinkedList<String> realfilesName = new LinkedList<>();
        if (new CommandJudge().foundSpecial(args , "-date")){
            for (int j = 0 ; j < filesName.length ; j++){
                if (args[new CommandJudge().foundSpecialIndex(args , "-date")+1].compareTo(filesName[j]) > 0)
                    realfilesName.add(filesName[j]);
            }
        }else {
            for (int j = 0 ; j < filesName.length ; j++){
                realfilesName.add(filesName[j]);
            }
        }

        if (realfilesName.size() == 0){
            System.out.println("太早了");
            return;
        }

        for (int j = 0 ; j < realfilesName.size() ; j++){
            System.out.println(realfilesName.get(j));
        }


    }

    public void sort(String[] filesName){
        for (int i = 0; i < filesName.length; i++) {
            for (int j = 0; j < filesName.length - i - 1; j++) {
                if (filesName[j].compareTo(filesName[j + 1]) > 0) {
                    String s = filesName[j];
                    filesName[j] = filesName[j + 1];
                    filesName[j + 1] = s;
                }
            }
        }
    }



}

/*
*
* 增加的功能，显示命令用法
* */
class Help{
    public void help(){

    }

    public void listHelp(){
        System.out.println("======================================================================");
        System.out.println("命令list支持参数");
        System.out.println("-log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径");
        System.out.println("-out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径");
        System.out.println("-date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件");
        System.out.println("-type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，" +
                "cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，" +
                "-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况");
        System.out.println("-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江");
        System.out.println("======================================================================");
    }

    public void presentHelp(){
        System.out.println("======================================================================");
        System.out.println("命令present支持参数");
        System.out.println("-log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径");
        System.out.println("-out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径");
        System.out.println("-type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，" +
                "cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，" +
                "-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况");
        System.out.println("-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江");
        System.out.println("======================================================================");
    }

}
/*
*
* 增加功能，得到现在实时的人数
* */
class Present{
}
