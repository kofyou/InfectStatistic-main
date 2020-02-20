import java.io.*;
import java.net.URL;
import java.net.URLConnection;
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
    private boolean web;
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
                    if (i + 1 < args.length) {
                        log.put(true, args[i + 1]);
                    }else {
                        log.put(true,null);
                    }
                    break;
                case "-out":
                    if(i + 1 < args.length) {
                        out.put(true, args[i + 1]);
                    }else {
                        out.put(true,null);
                    }
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
                        pri.add(args[i]);
                        i++;
                    }
                    province.put(true,pri);
                    i--;
                    break;
                case "-web":
                    web = true;
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

        if (date.containsKey(true)&&date.get(true)!=null){
            if (!this.checkTime(date.get(true))){
                throw new Exception("输入日期不正确");
            }
        }

        if (type.containsKey(true)){
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

    /**
     * 运行命令行
     */
    public void runCommand() {
        try {
            this.checkoutCommand();
            this.readFileNameFromDir();
            ArrayList<String> provinceNames = this.getProvince();
            ArrayList<String> types = this.getType();
            this.outFile(provinceNames,types);
            if (web){
                this.getWebData();
                this.writerWebFileFoot();
            }
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

    /**
     * 检查时间是否符合格式
     * @param time 日期时间格式的字符串
     * @return Boolean变量 正确为true 错误为false
     */
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

    /**
     * 读取文件中的每一行信息，匹配字符串
     * 字符串匹配信息为
     * "(\\W+) 新增 感染患者 (\\d+)人",
     * "(\\W+) 新增 疑似患者 (\\d+)人",
     * "(\\W+) 感染患者 流入 (\\W+) (\\d+)人",
     * "(\\W+) 疑似患者 流入 (\\W+) (\\d+)人",
     * "(\\W+) 死亡 (\\d+)人",
     * "(\\W+) 治愈 (\\d+)人",
     * "(\\W+) 疑似患者 确诊感染 (\\d+)人",
     * "(\\W+) 排除 疑似患者 (\\d+)人"};
     * @param fileName 读取的文件名称
     */
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

    /**
     * 匹配读取到的内容与匹配字符串做匹配
     * @param pattern 正则表达式
     * @param line 需要匹配的内容
     */
    public void matchingString(String pattern, String line){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (m.find()){
            this.statisticalData(m);
        }
    }

    /**
     * 处理匹配到的信息
     * @param matcher 匹配到的匹配器
     */
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

    /**
     * 处理 新增 治愈 死亡 排除的数据
     * @param matcher 匹配到的匹配器
     * @param type 患者类型
     * @param sub 数据加减判断
     */
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

    /**
     * 处理 流入 确诊感染数据
     * @param matcher 匹配到的匹配器
     * @param type 患者类型
     */
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

    /**
     * 计算全国的累计数
     * @return
     */
    public String processCountryData(ArrayList<String> types){
        String result = new String();
        int[] num = new int[types.size()+1];
        Set<String> provinceNames = data.keySet();
        for (String provinceName : provinceNames){
            HashMap<String,Integer> temp = data.get(provinceName);
            int i = 0;
            for (String type :types){
                num[i] += temp.get(type);
                i++;
            }
        }
        result = "全国";
        int i = 0;
        for (String type :types){
            result += " " + type + num[i] + "人";
            i++;
        }
        result += "\n";
        return result;
    }

    /**
     * 将省份按照首字母进行排序
     * @param list
     * @return
     */
    public ArrayList sortByProvinceName(ArrayList<String> list) {
        ArrayList<String> provinceName = new ArrayList<>();
        for (String s :list){
            if (s.equals("重庆")){
                provinceName.add("冲庆");
            }else {
                provinceName.add(s);
            }
        }
        Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
        Collections.sort(provinceName,com);
        System.out.println(provinceName);
        return provinceName;
    }

    /**
     * 将结果输出到文件中
     * @param provinceNames 省份名称
     * @param types 患者类型
     */
    public void outFile(ArrayList<String> provinceNames,ArrayList<String> types){
        String outFileName = out.get(true);
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(outFileName));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"utf8");
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            if (provinceNames.contains("全国")) {
                writer.write(processCountryData(types));
                provinceNames.remove("全国");
            }
            Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
            ArrayList<String> sortNames = this.sortByProvinceName(provinceNames);
            for(String provinceName : sortNames){
                if (provinceName.equals("冲庆")){
                    provinceName = "重庆";
                }
                String result = provinceName;
                if(data.containsKey(provinceName)) {
                    HashMap<String, Integer> temp = data.get(provinceName);
                    for (String string : types) {
                        int num = temp.get(string);
                        result += " " + string + num + "人";
                    }
                    writer.write(result + "\n");
                } else {
                    for (String string : types) {
                        result += " " + string + 0 + "人";
                    }
                    writer.write(result);
                }
            }
            writer.write("// 该文档并非真实数据，仅供测试使用\n");
            writer.write("// 命令 "+command);

            writer.close();;
            outputStreamWriter.close();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 处理是否传入要查询到类型
     * @return 返回查询类型 若无-type参数,返回所有类型
     */
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

    /**
     * 处理是否传入要查询省份名称
     * @return 返回查询类型 若无-province参数,返回所有省份
     */
    public ArrayList getProvince(){
        if (province.keySet().contains(true)){
            return province.get(true);
        }
        ArrayList re = new ArrayList(data.keySet());
        re.add("全国");
        return re;
    }

    /**
     * 从网页中获取数据并写入log目录下
     */
    public void getWebData(){
        String result = "";
        BufferedReader in = null;
        try {
            String urlString = "https://ncov.dxy.cn/ncovh5/view/pneumonia";
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            String webData = this.getWebProvinceData(result);
            this.processWebData(webData);
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 读取网页中有用的信息
     * @param webContent 整个网页
     * @return 各省的数据
     */
    public String getWebProvinceData(String webContent) {
        String reg= "window.getAreaStat = (.*?)}(?=catch)";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(webContent);
        if (matcher.find()){
            return matcher.group(1);
        }else {
            return null;
        }
    }

    /**
     * 分析网页中的数据
     * @param webData 各省数据
     */
    public void processWebData(String webData){
        String reg = "\"provinceShortName\":\"(\\W+)\"," +
                "\"currentConfirmedCount\":(\\d+)," +
                "\"confirmedCount\":(\\d+)," +
                "\"suspectedCount\":(\\d+)," +
                "\"curedCount\":(\\d+)," +
                "\"deadCount\":(\\d+)";
        String [] contents = webData.split("cities");
        for (String content : contents){
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()){
                ArrayList<String> datas = new ArrayList<>(){{
                    add(matcher.group(1) + " 新增 感染患者 " + matcher.group(3) + "人");
                    add(matcher.group(1) + " 治愈 " + matcher.group(5) + "人");
                    add(matcher.group(1) + " 死亡 " + matcher.group(6) + "人");
                }};
                this.writerWebDataToFile(datas);
            }
        }
    }

    /**
     * 将数据写入文件
     * @param data 从网页中获取的数据
     */
    public void writerWebDataToFile(ArrayList<String> data){
        try {
            String outFilePath = log.get(true);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = sdf.format(date);
            String outFileName = outFilePath + "/" + nowDate + ".log.txt";
            BufferedWriter writer = this.linkWriterFile(outFileName,true);
            for (String str : data){
                writer.write(str + "\n");
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }

    }

    /**
     * 连接写入文件
     * @param fileName 要写入的文件名
     * @param append 是否覆盖文件写
     * @return 返回BufferedWriter 写入器
     */
    public BufferedWriter linkWriterFile(String fileName , boolean append){
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(fileName),append);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"utf8");
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            return writer;
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    /**
     * 写入文件的读取信息
     */
    public void writerWebFileFoot(){
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = sdf.format(date);
            String outFileName = log.get(true) + "/" + nowDate + ".log.txt";
            BufferedWriter writer = this.linkWriterFile(outFileName,true);
            writer.write("//本文档从https://ncov.dxy.cn/ncovh5/view/pneumonia获取");
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }


    }

    public static void main(String[] args) {
        InfectStatistic infectStatistic = new InfectStatistic(args);
        infectStatistic.runCommand();
    }
}
