import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * @author lenovo
 */
public class InfectStatistic {

    //文件置于工程目录下

    /**
     *
     */
    public final static int K=8;
    public String []pattern={"(\\W+) 新增 感染患者 (\\d+)人",
            "(\\W+) 新增 疑似患者 (\\d+)人",
            "(\\W+) 感染患者 流入 (\\W+) (\\d+)人",
            "(\\W+) 疑似患者 流入 (\\W+) (\\d+)人",
            "(\\W+) 死亡 (\\d+)人",
            "(\\W+) 治愈 (\\d+)人",
            "(\\W+) 疑似患者 确诊感染 (\\d+)人",
            "(\\W+) 排除 疑似患者 (\\d+)人"};
    String patterns="(\\W+) (\\W+)(\\d+)人 (\\W+)(\\d+)人" +
            "(\\W+)(\\d+)人 (\\W+)(\\d+)人";
    String []province={"安徽" ,"北京" ,"重庆" ,"福建" ,"甘肃" ,"广东" ,"广西" ,
            "贵州" ,"海南" ,"河北" ,"河南" ,"黑龙江" ,"湖北" ,"湖南",
            "吉林" ,"江苏" ,"江西", "辽宁", "内蒙古", "宁夏", "青海" ,"山东" ,"山西" ,
            "陕西" ,"上海" ,"四川" ,"天津" ,"西藏" ,"新疆" ,"云南" ,"浙江"};
     boolean []discovery= new boolean[31];
    String []type2={"感染患者","疑似患者","治愈","死亡"};
    String person="人";
    /**人数*/
    int [][]number=new int[31][4];

    boolean flag1 = false;
    boolean flag2 = false;

    private void dealLine(String line) {
        int num;
        for (int i=0;i<K;i++)
        {
            int flag=0;
            int flag1=0;
            int flag2=0;
            Pattern r=Pattern.compile(pattern[i]);
            Matcher m=r.matcher(line);
            if(m.find()){
                num=i;
                for (int i1=0;i1<province.length;i1++){
                    if (province[i1].equals(m.group(1))) {
                        flag=i1;
                        discovery[i1]=true;
                    }
                    boolean status=(num==2||num==3)&&province[i1].equals(m.group(2));
                    if(status){
                        discovery[i1]=true;
                        flag1=i1;
                    }
                    boolean status1=(num==2||num==3)&&province[i1].equals(m.group(1));
                    if(status1){
                        discovery[i1]=true;
                        flag2=i1;
                    }
                }
                if(num==2) {
                    //流入流出
                    number[flag1][0]+=Integer.parseInt(m.group(3));
                    number[flag2][0]-=Integer.parseInt(m.group(3));
                }
                else if(num==3){
                    number[flag1][1]+=Integer.parseInt(m.group(3));
                    number[flag2][1]-=Integer.parseInt(m.group(3));
                }
                else if (num==7){
                    //排除类型
                    number[flag][1]-=Integer.parseInt(m.group(2));
                }
                else if (num==5){
                    //治愈和死亡类型
                    number[flag][0]-=Integer.parseInt(m.group(2));
                    number[flag][2] +=Integer.parseInt(m.group(2));
                }
                else if (num==4){
                    number[flag][0]-=Integer.parseInt(m.group(2));
                    number[flag][3] +=Integer.parseInt(m.group(2));
                }
                else if (num==6){
                    //疑似->确认
                    number[flag][0]+=Integer.parseInt(m.group(2));
                    number[flag][1]-=Integer.parseInt(m.group(2));
                }
                else {
                    number[flag][num] +=Integer.parseInt(m.group(2));
                }
            }
        }

    }


    public void outputFile(String outfile, String dictionary, String date) throws IOException {
        List<File> fileList = getFiles(dictionary);
        File fout = new File(outfile);
        FileWriter fw = new FileWriter(fout);
        for (File f : fileList) {
            String filename;
            filename = f.getName().substring(0, f.getName().indexOf("."));

            if (date.compareTo(filename) >= 0) {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String line = br.readLine();
                while (line != null) {
                    dealLine(line);
                    line = br.readLine();
                }
                fr.close();
            }
        }
        String lines=getAllProvince();
        fw.append(lines).append("\n");
        for(int i = 0; i<number.length; i++) {
            if (discovery[i]) {
                String theline = province[i] + " " + type2[0] + number[i][0] + person + " "
                        + type2[1] + number[i][1] + person + " " + type2[2] + number[i][2] +
                        person + " " + type2[3] + number[i][3] + person;
                fw.append(theline).append("\n");
            }
        }
        fw.append("// 该文档并非真实数据，仅供测试使用\n");
        fw.close();
    }

    private String getAllProvince() {
        int allIp, allSp, allCure, allDead;
        allSp = allIp = allCure = allDead = 0;
        for (int[] ints : number) {
            allIp += ints[0];
            allSp += ints[1];
            allCure += ints[2];
            allDead += ints[3];
        }
        return "全国" + " " + type2[0] +allIp+person+ " " +type2[1] + allSp+person+ " "
                +type2[2]+ allCure+person+" "+type2[3]+ allDead+person;
    }

    public static List<File> getFiles(String path) {
        File root = new File(path);
        List<File> files = new ArrayList<>();
        if (!root.isDirectory()) {
            files.add(root);
        } else {
            File[] subFiles = root.listFiles();
            assert subFiles != null;
            for (File f : subFiles) {
                files.addAll(getFiles(f.getAbsolutePath()));
            }
        }
        return files;
    }



    public void init(String[] args) throws Exception {
        char a = '-';
        String dic = null;
        String path = null ;
        String date;
        String[] type = new String[4];
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date=df.format(new Date());
        String[] provinces = new String[31];
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-log":
                    dic = args[i + 1];
                    break;
                case "-out":
                    path = args[i + 1];
                    break;
                case "-date":
                    date = args[i + 1];
                    break;
                case "-type": {
                    flag1 = true;
                    int k = 0;
                    for (int j = i + 1; j < args.length && args[j].toCharArray()[0]!=a; j++) {
                        type[k++] = args[j];
                    }
                }
                break;
                case "-province": {
                    flag2 = true;
                    int k = 0;
                    for (int j = i + 1; j < args.length && args[j].toCharArray()[0]!=a; j++) {
                        provinces[k++] = args[j];
                    }
                }
                break;
                default:
                    break;
            }
        }
        if(isValidDate(date) && isValidProvince(provinces)||!flag2&&isValidDate(date)) {
            outputFile(path, dic, date);
            dealFlag(path, type, provinces);
            System.out.println("// 该文档并非真实数据，仅供测试使用\n");
        }
        else {
            isValidDate(date);
            if (!isValidDate(date)){
                System.out.println("日期格式不合法！");
            }
            else if (!isValidProvince(provinces)){
                System.out.println("省份错误！");
            }
        }
    }

    public static boolean isValidDate(String ymd) {
        if (ymd == null || ymd.length() == 0) {
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(ymd);
            if (!format.format(date).equals(ymd)) {
                return false;
            }
            if(format.format(new Date()).compareTo(ymd) < 0){
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
        return true;

    }
    public static boolean isValidType(String[] type){
        for (String s : type) {
            if (s==null){
                return false;
            }
            if("sp".equals(s)|| "cure".equals(s)|| "ip".equals(s)|| "dead".equals(s)){
                return true;
            }

        }
        return false;
    }
    private boolean isValidProvince(String[] provinces) {
        boolean flag=false;
        provinces=deleteArrayNull(provinces);
        for (String s : provinces) {

            flag = Arrays.asList(province).contains(s) || "全国".equals(s);


        }
        return flag;
    }

    /***
     * 去除String数组中的空值
     */
    private String[] deleteArrayNull(String[] string) {

        // step1: 定义一个list列表，并循环赋值
        ArrayList<String> strList = new ArrayList<String>();
        Collections.addAll(strList, string);

        // step2: 删除list列表中所有的空值
        while (strList.remove(null)) { }
        while (strList.remove("")) { }

        // step3: 把list列表转换给一个新定义的中间数组，并赋值给它

        return strList.toArray(new String[strList.size()]);
    }

    private void dealProvince(String line,String[] provinces) {
        Pattern r=Pattern.compile(patterns);
        Matcher m=r.matcher(line);
        if (m.find()&&isValidProvince(provinces)) {
            for (String s : province) {
                if (m.group(1).equals(s)) {
                    System.out.println(line);
                }
                else {
                    System.out.println(s+" "+type2[0]+person+" "+type2[1]+person+" "
                            +type2[2]+person+" "+type2[3]+person);
                }

            }
        }
        if(!isValidProvince(provinces)){
            System.out.println("省份错误！");
        }
    }


    public void dealFlag( String path,  String[] type, String[] provinces)throws Exception {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null&&!line.contains("/")) {
            if (flag1 && flag2) {
                dealTypeAndProvince(line, type, provinces);
            }
            else if (flag2) {
                dealProvince(line, provinces);
            }
            if (flag1 && !flag2) {
                dealType(line,type);
            }
            else if(!flag2){
                System.out.println(line);
            }
            line = br.readLine();
        }

    }


    private void dealType(String line,String[] type) {
        Pattern r=Pattern.compile(patterns);
        Matcher m=r.matcher(line);
        if(m.find()) {
            System.out.print(m.group(1) + " ");
            for (String s : type) {
                if (s != null) {
                    switch (s) {
                        case "ip":
                            System.out.print(type2[0] + m.group(3) + person );
                            break;
                        case "sp":
                            System.out.print(type2[1] + m.group(5) + person );
                            break;
                        case "cure":
                            System.out.print(type2[2] + m.group(7) +person);
                            break;
                        case "dead":
                            System.out.print(type2[3] + m.group(9)+person );
                            break;
                        default:
                            System.out.print("无该患者类型，请重新选择！");
                            break;
                    }
                    System.out.print(" ");
                }

            }  System.out.print("\n");
        }
    }

    private void dealTypeAndProvince(String line,String[] type, String[] provinces) {
        Pattern r=Pattern.compile(patterns);
        Matcher m=r.matcher(line);
        if(m.find() &&isValidType(type)) {
            for (String s : provinces) {
                if (Objects.equals(s, m.group(1))) {
                    System.out.print(m.group(1) + " ");
                    for (String t : type) {
                        if (s != null&&t!=null) {
                            switch (t) {
                                case "ip":
                                    System.out.print(type2[0] + m.group(3) + person + " ");
                                    break;
                                case "sp":
                                    System.out.print(type2[1] + m.group(5) + person + " ");
                                    break;
                                case "cure":
                                    System.out.print(type2[2] + m.group(7) + person + " ");
                                    break;
                                case "dead":
                                    System.out.print(type2[3] + m.group(9) + person + " ");
                                    break;
                                default:
                                    System.out.print("无该患者类型，请重新选择！");
                                    break;
                            }
                        }
                    }
                    System.out.print("\n");
                }
            }
        }
    }



    public static void main(String[] args) throws Exception {
        args = new String[]{"-log", "D:\\log\\", "-out", "D:/log/out.txt", "-date","2020-01-23"};

        InfectStatistic t = new InfectStatistic();
        t.init(args);
    }
}

