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
        new InfectStatisticApplication().log(args);
    }
}

class Province{
    private String name;
    private int ip = 0;
    private int sp = 0;
    private int cure = 0;
    private int dead = 0;

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
}

class InfectStatisticApplication{

  public void log(String[] args) throws IOException {
      int judgeNum = new CommandJudge().judge(args);
      if (judgeNum == 1){
//          System.out.println("judgeNum: " + judgeNum);
          new ListCommand().Command(args);
      }
      if (judgeNum == 2){
          new Help().help();
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

    public int txtType(String a){
        String log1 = "(\\S+) 新增 感染患者 (\\d+)人";
        String log2 = "(\\S+) 新增 疑似患者 (\\d+)人";
        String log3 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
        String log4 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
        String log5 = "(\\S+) 死亡 (\\d+)人";
        String log6 = "(\\S+) 治愈 (\\d+)人";
        String log7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
        String log8 = "(\\S+) 排除 疑似患者 (\\d+)人";

        if (Pattern.matches(log1 , a))
            return 1;
        if (Pattern.matches(log2 , a))
            return 2;
        if (Pattern.matches(log3 , a))
            return 3;
        if (Pattern.matches(log4 , a))
            return 4;
        if (Pattern.matches(log5 , a))
            return 5;
        if (Pattern.matches(log6 , a))
            return 6;
        if (Pattern.matches(log7 , a))
            return 7;
        if (Pattern.matches(log8 , a))
            return 8;
        return 0;
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
                                return 0;
                            }
                            File file = new File(args[integers.get(i)+1]);
                            if (!file.isDirectory()){
                                System.out.println("It should be a directory");
                                return 0;
                            } else {
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
                        } else {
                            if ((integers.get(i+1) - integers.get(i)) != 2){
                                System.out.println("Error Command5");
                                return 0;
                            }
                            File f = new File(args[integers.get(i)+1]);
                            if (!f.isDirectory()){
                                System.out.println("It should be a directory");
                                return 0;
                            } else {
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
                                return 0;
                            }
                            File f = new File(args[integers.get(i) + 1]);
                            if (f.isDirectory()){
                                System.out.println("It should be a file");
                                return 0;
                            }
                        } else {
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
                        } else {
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
                        } else {
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
                        } else {
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
                        return 0;
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

    public LinkedList<String> foundProvinces(String[] args){
        LinkedList<String> provincesList = new LinkedList<String>();
        for (int i = 0 ; i < args.length ; i++){
            if (args[i].equals("-province")){
                for (int j = i + 1 ; j < args.length ; j++){
                    if (args[j].equals("-out") || args[j].equals("-log") || args[j].equals("-date") || args[j].equals("-type"))
                        return provincesList;
                    provincesList.add(args[j]);
                }
                return provincesList;
            }
        }
        return provincesList;
    }

    public LinkedList<String> foundType(String[] args){
        LinkedList<String> provincesList = new LinkedList<String>();
        for (int i = 0 ; i < args.length ; i++){
            if (args[i].equals("-type")){
                for (int j = i + 1 ; j < args.length ; j++){
                    if (args[j].equals("-out") || args[j].equals("-log") || args[j].equals("-date") || args[j].equals("-province"))
                        return provincesList;
                    provincesList.add(args[j]);
                }
                return provincesList;
            }
        }
        return provincesList;
    }
}


/*
*
* list命令类
*
* */
class ListCommand{
    public void Command(String[] args) throws IOException {
        int i = 0;
        LinkedList<Province> provinceList = new LinkedList<Province>();
        HashMap<String , Integer> hashMap = new HashMap<String , Integer>();
        hashMap.put("全国",i++); hashMap.put("安徽",i++); hashMap.put("北京",i++); hashMap.put("重庆",i++); hashMap.put("福建",i++);
        hashMap.put("甘肃",i++); hashMap.put("广东",i++); hashMap.put("广西",i++); hashMap.put("贵州",i++); hashMap.put("海南",i++);
        hashMap.put("河北",i++); hashMap.put("河南",i++); hashMap.put("黑龙江",i++); hashMap.put("湖北",i++); hashMap.put("湖南",i++);
        hashMap.put("吉林",i++); hashMap.put("江苏",i++); hashMap.put("江西",i++); hashMap.put("辽宁",i++); hashMap.put("内蒙古",i++);
        hashMap.put("宁夏",i++); hashMap.put("青海",i++); hashMap.put("山东",i++); hashMap.put("山西",i++); hashMap.put("陕西",i++);
        hashMap.put("上海",i++); hashMap.put("四川",i++); hashMap.put("天津",i++); hashMap.put("西藏",i++); hashMap.put("新疆",i++);
        hashMap.put("云南",i++); hashMap.put("浙江",i++);

        i = 0;
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(i++,"全国"); map.put(i++,"安徽"); map.put(i++,"北京"); map.put(i++,"重庆"); map.put(i++,"福建");
        map.put(i++,"甘肃"); map.put(i++,"广东"); map.put(i++,"广西"); map.put(i++,"贵州"); map.put(i++,"海南");
        map.put(i++,"河北"); map.put(i++,"河南"); map.put(i++,"黑龙江"); map.put(i++,"湖北"); map.put(i++,"湖南");
        map.put(i++,"吉林"); map.put(i++,"江苏"); map.put(i++,"江西"); map.put(i++,"辽宁"); map.put(i++,"内蒙古");
        map.put(i++,"宁夏"); map.put(i++,"青海"); map.put(i++,"山东"); map.put(i++,"山西"); map.put(i++,"陕西");
        map.put(i++,"上海"); map.put(i++,"四川"); map.put(i++,"天津"); map.put(i++,"西藏"); map.put(i++,"新疆");
        map.put(i++,"云南"); map.put(i++,"浙江");


        for (int j = 0 ; j < map.size() ; j++){
            provinceList.add(new Province(map.get(j)));
        }
        LinkedList<Integer> infectProvinces = new LinkedList<Integer>();


        String logPath = args[new CommandJudge().foundSpecialIndex(args , "-log") + 1];
        File file = new File(logPath);
        File[] files = file.listFiles();
        String[] filesName = new String[files.length];
        for (int j = 0 ; j < files.length ; j++){
            filesName[j] = files[j].getName();
        }
        sort(filesName);

        LinkedList<String> realfilesName = new LinkedList<String>();
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


        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        for (int j = 0 ; j < realfilesName.size() ; j++){
            fis = new FileInputStream(logPath + "/" + realfilesName.get(j));
            isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
            br = new BufferedReader(isr);
            String str = "";
            String str1 = "";
            while ((str = br.readLine()) != null){
                str1 = "";
                str1 += str;
                getProvinceInfect(str1 , provinceList);
            }
        }
        br.close();
        isr.close();
        fis.close();

        if (new CommandJudge().foundSpecial(args , "-province")){
            infectProvinces.clear();
            LinkedList<String> list = new CommandJudge().foundProvinces(args);
//            System.out.println(list);
            for (int k = 0 ; k < list.size() ; k++){
//                System.out.println(list.get(k));
                int num = hashMap.get(list.get(k));
                infectProvinces.add(num);
            }
        }else {
            infectProvinces.clear();
            for (int k = 0 ; k < provinceList.size() ; k++){
                infectProvinces.add(k);
            }
        }

        for (int k = 0; k < infectProvinces.size(); k++) {
            for (int l = 0; l < infectProvinces.size() - k - 1; l++) {
                if (infectProvinces.get(l) > infectProvinces.get(l+1)) {
                    int a = infectProvinces.get(l);
                    infectProvinces.set(l , infectProvinces.get(l+1));
                    infectProvinces.set(l+1 , a);
                }
            }
        }

        FileOutputStream fos=new FileOutputStream(args[new CommandJudge().foundSpecialIndex(args , "-out")+1]);
        OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter  bw=new BufferedWriter(osw);

        if (new CommandJudge().foundSpecial(args , "-type")){
            LinkedList<String> types = new CommandJudge().foundType(args);
            for (int k = 0 ; k < infectProvinces.size() ; k++){
                String putIn = provinceList.get(infectProvinces.get(k)).getName();
                for (int p = 0 ; p < types.size() ; p++){
                    if (types.get(p).equals("ip"))
                        putIn += (" 感染患者" + provinceList.get(infectProvinces.get(k)).getIp() + "人");
                    if (types.get(p).equals("sp"))
                        putIn += (" 疑似患者" + provinceList.get(infectProvinces.get(k)).getSp() + "人");
                    if (types.get(p).equals("cure"))
                        putIn += (" 治愈" + provinceList.get(infectProvinces.get(k)).getCure() + "人");
                    if (types.get(p).equals("dead"))
                        putIn += (" 死亡" + provinceList.get(infectProvinces.get(k)).getDead() + "人");
                }
                String osName = System.getProperty("os.name");
                if (osName.startsWith("Mac os")) {
                    bw.write(putIn + "\n");
                }else if(osName.startsWith("Windows")){
                    bw.write(putIn + "\r\n");
                }else {
                    bw.write(putIn + "\r");
                }
            }
        }else{
            for (int k = 0 ; k < infectProvinces.size() ; k++){
                String putIn = provinceList.get(infectProvinces.get(k)).getName() + " 感染患者" + provinceList.get(infectProvinces.get(k)).getSp() + "人 疑似患者" + provinceList.get(infectProvinces.get(k)).getIp() + "人 治愈" +
                        provinceList.get(infectProvinces.get(k)).getCure() + "人 死亡" + provinceList.get(infectProvinces.get(k)).getDead() + "人";

                String osName = System.getProperty("os.name");
                if (osName.startsWith("Mac os")) {
                    bw.write(putIn + "\n");
                }else if(osName.startsWith("Windows")){
                    bw.write(putIn + "\r\n");
                }else {
                    bw.write(putIn + "\r");
                }
            }
        }

        bw.write("// 该文档并非真实数据，仅供测试使用");
        bw.close();
        osw.close();
        fos.close();

        /*测试代码
        for (int k = 0 ; k < infectProvinces.size() ; k++){
            System.out.println("==================================");
            System.out.println(provinceList.get(infectProvinces.get(k)).getName());
            System.out.println(provinceList.get(infectProvinces.get(k)).getIp());
            System.out.println(provinceList.get(infectProvinces.get(k)).getSp());
            System.out.println(provinceList.get(infectProvinces.get(k)).getCure());
            System.out.println(provinceList.get(infectProvinces.get(k)).getDead());
            System.out.println(provinceList.get(infectProvinces.get(k)).isIpCheck());
            System.out.println(provinceList.get(infectProvinces.get(k)).isSpCheck());
            System.out.println(provinceList.get(infectProvinces.get(k)).isCureCheck());
            System.out.println(provinceList.get(infectProvinces.get(k)).isDeadCheck());

            System.out.println("===================================");
        }
        */
    }

    public void getProvinceInfect(String input , LinkedList<Province> provinceList){
        int i = 0;
        HashMap<String , Integer> hashMap = new HashMap<String , Integer>();
        hashMap.put("全国",i++); hashMap.put("安徽",i++); hashMap.put("北京",i++); hashMap.put("重庆",i++); hashMap.put("福建",i++);
        hashMap.put("甘肃",i++); hashMap.put("广东",i++); hashMap.put("广西",i++); hashMap.put("贵州",i++); hashMap.put("海南",i++);
        hashMap.put("河北",i++); hashMap.put("河南",i++); hashMap.put("黑龙江",i++); hashMap.put("湖北",i++); hashMap.put("湖南",i++);
        hashMap.put("吉林",i++); hashMap.put("江苏",i++); hashMap.put("江西",i++); hashMap.put("辽宁",i++); hashMap.put("内蒙古",i++);
        hashMap.put("宁夏",i++); hashMap.put("青海",i++); hashMap.put("山东",i++); hashMap.put("山西",i++); hashMap.put("陕西",i++);
        hashMap.put("上海",i++); hashMap.put("四川",i++); hashMap.put("天津",i++); hashMap.put("西藏",i++); hashMap.put("新疆",i++);
        hashMap.put("云南",i++); hashMap.put("浙江",i++);
        String regEx="[^0-9]";
        String[] provinces = input.split(" ");

        String s = provinces[provinces.length - 1].replaceAll(regEx , "");
//                System.out.println(s);
        int peopleNumber = 0;

        if (!s.isEmpty())
            peopleNumber = Integer.parseInt(s);

        int inputType = new REUtil().txtType(input);
        if (inputType == 1) {
            provinceList.get(hashMap.get(provinces[0])).setIp(provinceList.get(hashMap.get(provinces[0])).getIp() + peopleNumber);
        }

        if (inputType == 2) {
            provinceList.get(hashMap.get(provinces[0])).setSp(provinceList.get(hashMap.get(provinces[0])).getSp() + peopleNumber);
        }

        if (inputType == 3) {
            provinceList.get(hashMap.get(provinces[0])).setIp(provinceList.get(hashMap.get(provinces[0])).getIp() - peopleNumber);
            provinceList.get(hashMap.get(provinces[3])).setIp(provinceList.get(hashMap.get(provinces[3])).getIp() + peopleNumber);
        }

        if (inputType == 4) {
            provinceList.get(hashMap.get(provinces[0])).setSp(provinceList.get(hashMap.get(provinces[0])).getSp() - peopleNumber);
            provinceList.get(hashMap.get(provinces[3])).setSp(provinceList.get(hashMap.get(provinces[3])).getSp() + peopleNumber);
        }

        if (inputType == 5) {
            provinceList.get(hashMap.get(provinces[0])).setDead(provinceList.get(hashMap.get(provinces[0])).getDead() + peopleNumber);
            provinceList.get(hashMap.get(provinces[0])).setIp(provinceList.get(hashMap.get(provinces[0])).getIp() - peopleNumber);
        }

        if (inputType == 6) {
            provinceList.get(hashMap.get(provinces[0])).setCure(provinceList.get(hashMap.get(provinces[0])).getCure() + peopleNumber);
            provinceList.get(hashMap.get(provinces[0])).setIp(provinceList.get(hashMap.get(provinces[0])).getIp() - peopleNumber);
        }

        if (inputType == 7) {
            provinceList.get(hashMap.get(provinces[0])).setSp(provinceList.get(hashMap.get(provinces[0])).getSp() - peopleNumber);
            provinceList.get(hashMap.get(provinces[0])).setIp(provinceList.get(hashMap.get(provinces[0])).getIp() + peopleNumber);
        }

        if (inputType == 8) {
            provinceList.get(hashMap.get(provinces[0])).setSp(provinceList.get(hashMap.get(provinces[0])).getSp() - peopleNumber);
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
        System.out.println("======================================================================");
        System.out.println("命令list支持参数");
        System.out.println("-log 指定日志目录的位置，该项必会附带");
        System.out.println("-out 指定输出文件路径和文件名，该项必会附带");
        System.out.println("-date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件");
        System.out.println("-type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，" +
                "cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，" +
                "-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况");
        System.out.println("-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江");
        System.out.println("======================================================================");
        System.out.println("命令present支持参数");
        System.out.println("-out 指定输出文件路径和文件名，该项必会附带");
        System.out.println("======================================================================");
    }
}
/*
*
* 增加功能，得到现在实时的人数
* */
class Present{
}
