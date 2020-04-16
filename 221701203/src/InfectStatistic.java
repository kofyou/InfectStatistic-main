import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.text.Collator;
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
    public final static int K = 8;
    public String[] pattern = new String[]{"(\\W+) 新增 感染患者 (\\d+)人",
            "(\\W+) 新增 疑似患者 (\\d+)人",
            "(\\W+) 感染患者 流入 (\\W+) (\\d+)人",
            "(\\W+) 疑似患者 流入 (\\W+) (\\d+)人",
            "(\\W+) 死亡 (\\d+)人",
            "(\\W+) 治愈 (\\d+)人",
            "(\\W+) 疑似患者 确诊感染 (\\d+)人",
            "(\\W+) 排除 疑似患者 (\\d+)人"};
    public static String[] province = {"安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西",
            "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南",
            "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西",
            "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};
    public static String[] type2 = {"感染患者", "疑似患者", "治愈", "死亡"};
    public static String person = "人";
    /**
     * 人数
     */
    int[][] number = new int[31][4];
    boolean flag1 = false;
    boolean flag2 = false;
    boolean[] discovery = new boolean[31];

    private void dealLine(String line) {
        int num;
        for (int i = 0; i < K; i++) {
            int flag = 0;
            int flag1 = 0;
            int flag2 = 0;
            Pattern r = Pattern.compile(pattern[i]);
            Matcher m = r.matcher(line);
            if (m.find()) {
                num = i;
                for (int i1 = 0; i1 < province.length; i1++) {
                    if (province[i1].equals(m.group(1))) {
                        flag = i1;
                        discovery[i1] = true;
                    }
                    boolean status = (num == 2 || num == 3) && province[i1].equals(m.group(2));
                    if (status) {
                        discovery[i1] = true;
                        flag1 = i1;
                    }
                    boolean status1 = (num == 2 || num == 3) && province[i1].equals(m.group(1));
                    if (status1) {
                        discovery[i1] = true;
                        flag2 = i1;
                    }
                }if (num == 2) {
                    //流入流出
                    number[flag1][0] += Integer.parseInt(m.group(3));
                    number[flag2][0] -= Integer.parseInt(m.group(3));
                } else if (num == 3) {
                    number[flag1][1] += Integer.parseInt(m.group(3));
                    number[flag2][1] -= Integer.parseInt(m.group(3));
                } else if (num == 7) {
                    //排除类型
                    number[flag][1] -= Integer.parseInt(m.group(2));
                } else if (num == 5) {
                    //治愈和死亡类型
                    number[flag][0] -= Integer.parseInt(m.group(2));
                    number[flag][2] += Integer.parseInt(m.group(2));
                } else if (num == 4) {
                    number[flag][0] -= Integer.parseInt(m.group(2));
                    number[flag][3] += Integer.parseInt(m.group(2));
                } else if (num == 6) {
                    //疑似->确认
                    number[flag][0] += Integer.parseInt(m.group(2));
                    number[flag][1] -= Integer.parseInt(m.group(2));
                } else {
                    number[flag][num] += Integer.parseInt(m.group(2));
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
        String lines = getAllProvince();
        fw.append(lines).append("\n");
        for (int i = 0; i < number.length; i++) {
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

    private void sortFilename(String dictionary, ArrayList<String> filenames) {
        List<File> fileList = getFiles(dictionary);
        for (File f : fileList) {

            String filename;
            filename = f.getName().substring(0, f.getName().indexOf("."));
            filenames.add(filename);

        }
        Collections.sort(filenames);
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
        String date=null;
        boolean status=false;
        String[] type = new String[4];
       // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //date=df.format(new Date());
        String[] provinces = new String[31];
        for (int i = 0; i < args.length; i++) {
            if ("-log".equals(args[i])) {
                dic = args[i + 1];
            } else if ("-out".equals(args[i])) {
                path = args[i + 1];
            } else if ("-date".equals(args[i])) {
                date = args[i + 1];
                status=true;
            } else if ("-type".equals(args[i])) {
                flag1 = true;
                int k = 0;
                for (int j = i + 1; j < args.length && args[j].toCharArray()[0] != a; j++) {
                    type[k++] = args[j];
                }
            } else if ("-province".equals(args[i])) {
                flag2 = true;
                int k = 0;
                for (int j = i + 1; j < args.length && args[j].toCharArray()[0] != a; j++) {
                    provinces[k++] = args[j];
                }
            }
        }

        if (isExistPath(path)) {

            ArrayList<String> filenames=new ArrayList<>();
            if(!status){
                sortFilename(dic,filenames);
                date=filenames.get(filenames.size()-2);
            }
            provinces = sortProvinces(provinces);
            type = deleteArrayNull(type);
            if (isValidDate(date, dic) && isValidProvince(provinces) || !flag2 && isValidDate(date, dic)) {
                outputFile(path, dic, date);
                dealFlag(type, provinces);
                System.out.println("// 该文档并非真实数据，仅供测试使用\n");
            } else {
                isValidDate(date, dic);
                if (!isValidDate(date, dic)) {
                    System.out.println("日期格式不合法！");
                } else if (!isValidProvince(provinces)) {
                    System.out.println("省份错误！");
                }
            }
        }
    }



    private String[] sortProvinces(String[] provinces) {
        provinces=deleteArrayNull(provinces);
        Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
        Arrays.sort(provinces, com);
        for (int i=0;i<provinces.length;i++){
            if ("全国".equals(provinces[i])){
                System.arraycopy(provinces, 0, provinces, 1, i);
                provinces[0]="全国";
            }
        }
        return provinces;
    }

    public static boolean isExistPath(String path) {

        if (null == path || "".equals(path.trim())) {
            System.out.println("创建文件路径错误！");
            return false;
        }


        return true;
    }

    public boolean isValidDate(String ymd, String dic) {
        ArrayList<String> filenames= new ArrayList<>();
        sortFilename(dic, filenames);
        if (ymd == null || ymd.length() == 0) {
            return false;
        }
        if (ymd.compareTo(filenames.get(0)) < 0 || ymd.compareTo(filenames.get(filenames.size() - 2)) > 0) {
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(ymd);
            if (!format.format(date).equals(ymd)) {
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
        ArrayList<String> strList = new ArrayList<>();
        Collections.addAll(strList, string);

        // step2: 删除list列表中所有的空值
        while (strList.remove(null)) { }
        while (strList.remove("")) { }

        // step3: 把list列表转换给一个新定义的中间数组，并赋值给它

        return strList.toArray(new String[strList.size()]);
    }

    private void dealProvince( String[] provinces) {

        if (isValidProvince(provinces)) {
            for (String s : provinces) {
                if ("全国".equals(s)){
                    System.out.println(getAllProvince());
                }
                for (int j = 0; j < province.length; j++) {
                    if (province[j].equals(s)) {
                        System.out.println(province[j] + " " + type2[0] + number[j][0] + person + " " + type2[1] +
                                number[j][1] + person + " "
                                + type2[2] + number[j][2] + person + " " + type2[3] + number[j][3] + person + " ");
                    }
                }
            }
        }
        if(!isValidProvince(provinces)){
            System.out.println("省份错误！");
        }
    }


    public void dealFlag(   String[] type, String[] provinces) {
        if (!flag1&&flag2) {
            dealProvince( provinces);
        }
        else if (flag1 && flag2) {
            dealTypeAndProvince( type, provinces);
        }
        if (flag1 && !flag2) {
            dealType(type);
        }
        else if(!flag2){
            dealNormal();
        }
    }

    private void dealNormal() {
        System.out.println(getAllProvince());
        for (int i=0;i<province.length;i++){
            if (discovery[i]) {
                System.out.print(province[i]+" ");
                System.out.println(type2[0] + number[i][0] + person + " " + type2[1] + number[i][1] + person + " "
                        + type2[2] + number[i][2] + person + " " + type2[3] + number[i][3] + person + " ");
            }
        }
    }


    private void dealType(String[] type) {
        System.out.println(getAllProvinceType(type));
        for (int i = 0; i < province.length; i++) {
            if (discovery[i]) {
                System.out.print(province[i]+" " );
                for (String s : type) {
                    if ("ip".equals(s)) {
                        System.out.print(type2[0] + number[i][0] + person + " ");
                    } else if ("sp".equals(s)) {
                        System.out.print(type2[0] + number[i][1] + person + " ");
                    } else if ("cure".equals(s)) {
                        System.out.print(type2[0] + number[i][2] + person + " ");
                    } else if ("dead".equals(s)) {
                        System.out.print(type2[0] + number[i][3] + person + " ");
                    }
                    else {
                        System.out.print("无该类患者");
                    }
                }
                System.out.print("\n");
            }
        }
    }

    private void dealTypeAndProvince(String[] type, String[] provinces) {
        provinces=deleteArrayNull(provinces);
        for (String s : provinces) {
            if ("全国".equals(s) && isValidType(type)) {
                System.out.println(getAllProvinceType(type));
            }
            for (int j = 0; j < province.length; j++) {
                if (s.equals(province[j])) {
                    System.out.print(province[j] + " ");
                    for (String t : type) {
                        if (t != null) {
                            switch (t) {
                                case "ip":
                                    System.out.print(type2[0] + number[j][0] + person + " ");
                                    break;
                                case "sp":
                                    System.out.print(type2[1] + number[j][1] + person + " ");
                                    break;
                                case "cure":
                                    System.out.print(type2[2] + number[j][2] + person + " ");
                                    break;
                                case "dead":
                                    System.out.print(type2[3] + number[j][3] + person + " ");
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

    private String getAllProvinceType(String[] type) {
        int allIp, allSp, allCure, allDead;
        StringBuilder massage= new StringBuilder();
        allSp = allIp = allCure = allDead = 0;
        for (int[] ints : number) {
            allIp += ints[0];
            allSp += ints[1];
            allCure += ints[2];
            allDead += ints[3];
        }
        for (String s : type) {
            if ("ip".equals(s)) {
                massage.append(type2[0]).append(allIp).append(person).append(" ");
            } else if ("sp".equals(s)) {
                massage.append(type2[0]).append(allSp).append(person).append(" ");
            } else if ("cure".equals(s)) {
                massage.append(type2[0]).append(allCure).append(person).append(" ");
            } else if ("dead".equals(s)) {
                massage.append(type2[0]).append(allDead).append(person).append(" ");
            }else {
                massage.append("无该类型患者 ");
            }
        }
        return "全国"+" "+ massage;
    }


    public static void main(String[] args) throws Exception {
        InfectStatistic t = new InfectStatistic();
        t.init(args);
    }
}

