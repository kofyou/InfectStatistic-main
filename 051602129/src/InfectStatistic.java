import java.io.*;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @ClassName InfectStatistic
 * @Description 疫情统计程序
 * @Author mingll
 * @Date 2020/2/12 8:14 下午
 * @Version 1.0
 */

class InfectStatistic {

    private String command = "";
    private boolean list;
    private HashMap<Boolean,String> log;
    private HashMap<Boolean,String> out;
    private HashMap<Boolean,String> date;
    private HashMap<Boolean, ArrayList<String>> type;
    private HashMap<Boolean,ArrayList<String>> province;
    private HashMap<String,HashMap<String,Integer>> data;
    private String[] options = {"感染患者","疑似患者","治愈","死亡"};

    /**
     * 初始化参数
     * @param args
     */
    public InfectStatistic(String[] args){
        log = new HashMap<>();
        out = new HashMap<>();
        date = new HashMap<>();
        type = new HashMap<>();
        province = new HashMap<>();
        data = new HashMap<>();
        for(int i = 0; i<args.length;i++){
            command += args[i] + " ";
            switch (args[i]){
                case "list":
                    list = true;
                    break;
                case "-log":
                    log.put(true,args[i+1]);
                    break;
                case "-out":
                    out.put(true,args[i+1]);
                    break;
                case "-date":
                    if (i + 1 < args.length) {
                        if (this.checkTime(args[i + 1])){
                            date.put(true, args[i + 1]);
                        } else {
                            date.put(true,null);
                        }
                    }else {
                        date.put(true,null);
                    }
                    break;
                case "-type":
                    i++;
                    ArrayList<String> tmp = new ArrayList<>();
                    while (i<args.length&&!(args[i].substring(0,1).equals("-"))){
                        tmp.add(args[i]);
                        i++;
                    }
                    type.put(true,tmp);
                    i--;
                    break;
                case "-province":
                    i++;
                    ArrayList<String> pri = new ArrayList<>();
                    while (i<args.length&&!(args[i].substring(0,1).equals("-"))){
                        System.out.println(args[i]);
                        pri.add(args[i]);
                        i++;
                    }
                    province.put(true,pri);
                    i--;
                    break;
            }
        }
    }

    /**
     * 检查命名是否正确
     * @throws Exception
     */
    public void checkoutCommand() throws Exception{
        if(!list){
            throw new Exception("缺少list");
        }

        if (log.keySet().isEmpty()||log.values().isEmpty()){
            throw  new Exception("缺少log参数");
        }

        if (out.keySet().isEmpty()||out.values().isEmpty()){
            throw new Exception("date参数错误");
        }

        if (date.keySet().equals(true)){
            if (!this.checkTime(date.get(true))){
                throw new Exception("输入日期不正确");
            }
        }

        if (type.keySet().equals(true)){
            ArrayList<String> temps = new ArrayList<>(){{
                    add("ip");
                    add("sp");
                    add("cure");
                    add("dead");
                }};
            ArrayList<String> types = type.get(true);
            for (String str :types){
                if (!temps.contains(str)){
                    throw new Exception("type参数错误");
                }
            }

        }
    }

    public void runCommand() {
        try {
            this.checkoutCommand();
            this.readFileNameFromDir();
            ArrayList<String> provinceNames = this.getProvince();
            ArrayList<String> types = this.getType();
            this.outFile(provinceNames,types);
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 检查是否存在文件夹
     * @return 文件夹名称
     * @throws Exception
     */
    public String isDir() throws Exception{
        String path = log.get(true);
        File dir = new File(path);
        if (!dir.isDirectory()){
            throw new Exception("文件夹不存在");
        }

       return path;
    }

    public boolean checkTime(String time){

        SimpleDateFormat check = new SimpleDateFormat("yyyy-MM-dd");
        try {
            check.setLenient(true);
            check.parse(time);

        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 读取文件夹下面的符合要求文件
     */
    public void readFileNameFromDir(){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String path = this.isDir();
            File dir = new File(path);
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.toString().endsWith(".log.txt");
                }
            });
            for (File file : files) {
               if (date.keySet().contains(true)&(date.get(true)!=null)){
                   Date logDate = format.parse(date.get(true));
                   Date fileDate = format.parse(file.getName().substring(0,10));
                   int result = fileDate.compareTo(logDate);
                   if (result<1){
                       this.readFileContent(file.getName());
                   }
               }else {
                   this.readFileContent(file.getName());
               }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public void readFileContent(String fileName){
        String[] type1 ={
                "(\\W+) 新增 感染患者 (\\d+)人",
                "(\\W+) 新增 疑似患者 (\\d+)人",
                "(\\W+) 感染患者 流入 (\\W+) (\\d+)人",
                "(\\W+) 疑似患者 流入 (\\W+) (\\d+)人",
                "(\\W+) 死亡 (\\d+)人",
                "(\\W+) 治愈 (\\d+)人",
                "(\\W+) 疑似患者 确诊感染 (\\d+)人",
                "(\\W+) 排除 疑似患者 (\\d+)人"};

        try {
            BufferedReader reader = new BufferedReader(new FileReader(log.get(true)+'/'+fileName));
            String str;
            while ((str =reader.readLine()) != null){
                for (String a:type1){
                    if (str.matches(a)){
                        this.matchingString(a,str);
                    }
                }
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void matchingString(String pattern, String line){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (m.find()){
            this.statisticalData(m);
        }
    }

    public void statisticalData(Matcher matcher){
        int count = matcher.groupCount();
        String temp = matcher.group(0);
        String[] content = temp.split(" ");
        for (int i = 1; i<3; i++){
            switch (content[i]){
                case "新增":
                    processIncreasedData(matcher,content[i+1],true);
                    break;
                case "死亡":
                    processIncreasedData(matcher,"死亡",false);
                    break;
                case "治愈":
                    processIncreasedData(matcher,"治愈",false);
                    break;
                case "流入":
                case "确诊感染":
                    processFlowData(matcher,content[i-1]);
                    break;
                case "排除":
                    processIncreasedData(matcher,"疑似患者",false);
                    break;
            }
        }
    }

    public void processIncreasedData(Matcher matcher, String type,boolean sub){
        String provinceName = matcher.group(1);
        if (sub) {
            if (data.containsKey(provinceName)) {
                HashMap<String, Integer> temp = data.get(provinceName);
                int num = temp.get(type);
                num += Integer.parseInt(matcher.group(2));
                temp.put(type, num);
            } else {
                HashMap<String, Integer> temp = new HashMap<>();
                temp.put("感染患者",0);
                temp.put("疑似患者",0);
                temp.put("治愈",0);
                temp.put("死亡",0);
                int num = Integer.parseInt(matcher.group(2));
                temp.put(type, num);
                data.put(provinceName,temp);
            }
        }else {
            if (type.equals("疑似患者")) {
                HashMap<String, Integer> temp = data.get(provinceName);
                int num = temp.get(type);
                num -= Integer.parseInt(matcher.group(2));
                temp.put(type, num);
            }else {
                HashMap<String, Integer> temp = data.get(provinceName);
                int num = temp.get(type);
                num += Integer.parseInt(matcher.group(2));
                temp.put(type, num);
                int ipNum = temp.get("感染患者");
                ipNum -= Integer.parseInt(matcher.group(2));
                temp.put("感染患者",ipNum);
            }
        }

    }

    public void processFlowData(Matcher matcher,String type){
        if (matcher.groupCount()==2){
            String provinceName = matcher.group(1);
            HashMap<String,Integer> temp = data.get(provinceName);
            int spNum = temp.get(type);
            int num = Integer.parseInt(matcher.group(2));
            spNum -= num;
            temp.put(type,spNum);
            int ipNum = temp.get("感染患者");
            ipNum += num;
            temp.put("感染患者",ipNum);
        }else {
            String provinceNameOut = matcher.group(1);
            String provinceNameIn = matcher.group(2);
            HashMap<String,Integer> out = data.get(provinceNameOut);
            HashMap<String,Integer> in = data.get(provinceNameIn);
            int outNum = out.get(type);
            int inNum = in.get(type);
            int num = Integer.parseInt(matcher.group(3));
            outNum -= num;
            inNum += num;
            out.put(type,outNum);
            in.put(type,inNum);
        }
    }

    public String processCountryData(){
        String result = new String();
        int[] num = new int[options.length];
        Set<String> provinceNames = data.keySet();
        for (String provinceName : provinceNames){
            HashMap<String,Integer> temp = data.get(provinceName);
            for (int i =0; i <options.length; i++){
                num[i] += temp.get(options[i]);
            }
        }
        result = "全国";
        for (int i = 0; i<options.length; i++){
            result += " " + options[i] + num[i] + "人";
        }
        result += "\n";
        return result;
    }

    public ArrayList sortByProvinceName(ArrayList<String> list) {
        ArrayList<String> provinceName = new ArrayList<>();
        for (String s :list){
            provinceName.add(s);
        }
        Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
        Collections.sort(provinceName,com);
        return provinceName;
    }

    public void outFile(ArrayList<String> provinceNames,ArrayList<String> types){
        String outFileName = out.get(true);
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(outFileName));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"utf8");
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            if (provinceNames.contains("全国")) {
                writer.write(processCountryData());
            }
            Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
            ArrayList<String> sortNames = this.sortByProvinceName(provinceNames);
            System.out.println(sortNames);
            for(String provinceName : sortNames){
                HashMap<String,Integer> temp = data.get(provinceName);
                String result = provinceName;
                for (String string:types){
                    int num = temp.get(string);
                    result += " " + string + num + "人";
                }
                writer.write(result+"\n");
            }
            writer.write("// 该文档并非真实数据，仅供测试使用\n");
            writer.write("// 命令 "+command);

            writer.close();;
            outputStreamWriter.close();
            outputStream.close();
        }catch (Exception e){
            System.exit(-1);
        }
    }

    public ArrayList getType(){
        if (type.keySet().contains(true)){
            ArrayList<String> types = type.get(true);
            ArrayList<String> temp = new ArrayList<>();
            for (String str : types){
                switch (str){
                    case "ip":
                        temp.add(options[0]);
                        break;
                    case "sp":
                        temp.add(options[1]);
                        break;
                    case "cure":
                        temp.add(options[2]);
                        break;
                    case "dead":
                        temp.add(options[3]);
                        break;
                }
            }
            return temp;
        }
        return new ArrayList<String>(Arrays.asList(options));
    }

    public ArrayList getProvince(){
        if (province.keySet().contains(true)){
            return province.get(true);
        }
        return new ArrayList(data.keySet());
    }

    public static void main(String[] args) {
        InfectStatistic infectStatistic = new InfectStatistic(args);
        infectStatistic.runCommand();
    }
}
