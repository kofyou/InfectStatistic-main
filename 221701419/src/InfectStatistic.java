import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * InfectStatistic TODO
 *
 * @author HHQ
 * @version 2.0
 */
class InfectStatistic {

    /**
     * Province类
     * @author HHQ
     */
    public class Province {

        /** 省份名称 */
        String provinceName; 
        /** 感染患者 */
        int ip; 
        /** 疑似患者 */
        int sp;
        /** 治愈 */
        int cure;
        /** 死亡 */
        int dead;

        Province(String provinceName, int ip, int sp, int cure, int dead) {
            this.provinceName = provinceName;
            this.ip = ip;
            this.sp = sp;
            this.cure = cure;
            this.dead = dead;
        }

        /** 感染人数增加 */
        public void increaseIp(int newIpNum) {
            ip += newIpNum;
        }

        /** 感染人数减少 */
        public void decreaseIp(int ipNum) {
            ip -= ipNum;
        }

        /** 疑似患者增加 */
        public void increaseSp(int newSpNum) {
            sp += newSpNum;
        }
        
        /** 疑似患者减少 */
        public void decreaseSp(int spNum) {
            sp -= spNum;
        }

        /** 治愈增加 */
        public void increaseCure(int newCureNum) {
            cure += newCureNum;
        }

        /** 死亡增加 */
        public void increaseDead(int newDeadNum) {
            dead += newDeadNum;
        }
        
        public String getProvinceName() {
            return provinceName;
        }

        public int getIp() {
            return ip;
        }

        public int getSp() {
            return sp;
        }

        public int getCure() {
            return cure;
        }

        public int getDead() {
            return dead;
        }

        /**
         * description：打印全部统计的数据结果
         * @return resString 返回值为字符串
         */
        public String getAllResult() {
            String resString = provinceName + " " + "感染患者" + ip + "人" + " " + "疑似患者" + sp + "人" + " " + "治愈" + cure
                    + "人" + " " + "死亡" + dead + "人";
            return resString;
        }
        
        /**
         * description：按指定参数值要求给出结果
         * @param paramenterOf 一个保存着-type的参数值的数组
         * @return resString 返回值为字符串
         */
        public String getResultByRequest(String[] paramentersOfType) {
            String resString = provinceName + " ";
            for(int i=0; paramentersOfType[i] != null; i++) {
                switch (paramentersOfType[i]) {
                case "ip":
                    resString += "感染患者" + ip + "人" + " ";
                    break;
                case "sp":
                    resString += "疑似患者" + sp + "人" + " ";
                    break;
                case "cure":
                    resString += "治愈" + cure + "人" + " ";
                    break;
                case "dead":
                    resString += "死亡" + dead + "人" + " ";
                    break;
                default:
                    break;
                }
            }
            
            return resString;
        }
    }

    /**
     * description：静态的一个工具类，一些静态的方法
     * @author HHQ
     */
    static class ToolMethods {

        /**
         * description：将一个字符串以空格" "分割
         * @param string 传入的字符串
         * @return 返回值为分割后的数组长度
         */
        public static int numAfterSplit(String string) {
            String[] afterSplitStrings = string.split(" ");
            return afterSplitStrings.length;
        }

        /**
         * description：获取一个字符串前的数字
         * @param string 传入的字符串
         * @return 返回值为取得的数值int
         */
        public static int getNumber(String string) {
            for (int i = 0; i < string.length(); i++) {
                if (Character.isDigit(string.charAt(i))) {
                    ;
                } else {
                    string = string.substring(0, i);
                    break;
                }
            }

            return Integer.parseInt(string);
        }

        /**
         * description：得到要修改数据的省份名称modifyProvinceName
         * @param strings 分割后的字符串数组
         * @return 返回值为省份名称数组，只有一个省份时第二个为空
         */
        public static String[] getNeedModifyProvinceNames(String[] strings) {
            int len = strings.length;
            String[] resStrings = new String[2];
            if (len == 3 || len == 4) {
                resStrings[0] = strings[0];
                resStrings[1] = "";
            } else if (len == 5) {
                resStrings[0] = strings[0];
                resStrings[1] = strings[3];
            }
            return resStrings;
        }

        /**
         * description：判断操作类型
         * @param strings 分割后的字符串数组
         * @return 返回值操作类型ID（1~8）
         */
        public static int getOperateType(String[] strings) {
            int len = strings.length;
            int res = 0;
            if (len == 3) {
                if (strings[1].equals("死亡")) {
                    res = 1;
                } else if (strings[1].equals("治愈")) {
                    res = 2;
                }
            } else if (len == 4) {
                if (strings[1].equals("新增")) {
                    if (strings[2].equals("感染患者")) {
                        res = 3;
                    } else if (strings[2].equals("疑似患者")) {
                        res = 4;
                    }
                } else if (strings[1].equals("排除")) {
                    res = 5;
                } else {
                    res = 6;
                }
            } else {
                if (strings[1].equals("感染患者")) {
                    res = 7;
                } else {
                    res = 8;
                }
            }
            return res;
        }

        /**
         * description：简单判断该行是注释行，仅判断前两个字符"//"
         * @param string 传入一行字符串
         * @return 布尔值
         */
        public static boolean isAnnotation(String lineString) {
            if (lineString.charAt(0) == '/' && lineString.charAt(1) == '/') {
                return true;
            } else {
                return false;
            }
        }

        /**
         * description：取得所有log中最大的日期
         * @param nameStrings 传入的文件名数组
         * @return 最大的日期，类型：Date
         */
        public static Date getMaxDate(String[] nameStrings) {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            String maxDateString = "2000-01-01";
            Date maxDate = null;
            try {
                maxDate = dFormat.parse(maxDateString);
                for(int i=0; i<nameStrings.length; i++) {
                    Date tmpDate = dFormat.parse(nameStrings[i]);
                    if(tmpDate.getTime() >= maxDate.getTime()) {
                        maxDate = tmpDate;
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return maxDate;
        }

        /** description：取得今天的日期 */
        public static String getToday() {
            Date todayDate = new Date();
            SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String todayString = sdfDateFormat.format(todayDate);
            return todayString;
        }
        
        /**
         * description：获取文件夹下指定日期前的所有文件文件名
         * @param path 文件夹路径
         * @param date 指定的日期
         * @param fileName 获得的文件名列表
         */
        public static void getBeforeDateFileName(String path, String date, ArrayList<String> fileName) {
            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
            File file = new File(path);
            File[] files = file.listFiles();
            String[] nameStrings = file.list();
            Date maxDate = getMaxDate(nameStrings);
            if (nameStrings != null) {
                for (int i = 0; i < nameStrings.length; i++) {
                    String dateOfFileNameString = nameStrings[i].substring(0, nameStrings[i].indexOf('.'));
                    try {
                        Date dateOfFileNameDate = dFormat.parse(dateOfFileNameString);
                        Date limitDate = dFormat.parse(date);
                        if(limitDate.getTime() > maxDate.getTime()) {
                            System.out.println("日期超出范围");
                        }else {
                            if (dateOfFileNameDate.getTime() <= limitDate.getTime()) {
                                fileName.add(nameStrings[i]);
                            }
                        }
                        
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * description：根据省份和操作类型ID执行相应的操作
         * @param province1 省份1
         * @param province2 省份2，有可能为空
         * @param operateType 操作类型ID（1~8）
         * @param number 执行相应修改的 人数
         */
        public static void executeOperate(Province province1, Province province2, int operateType, int number) {
            switch (operateType) {
            case 1:
                province1.increaseDead(number);
                province1.decreaseIp(number);
                break;
            case 2:
                province1.increaseCure(number);
                province1.decreaseIp(number);
                break;
            case 3:
                province1.increaseIp(number);
                break;
            case 4:
                province1.increaseSp(number);
                break;
            case 5:
                province1.decreaseSp(number);
                break;
            case 6:
                province1.decreaseSp(number);
                province1.increaseIp(number);
                break;
            case 7:
                province1.decreaseIp(number);
                province2.increaseIp(number);
                break;
            case 8:
                province1.decreaseSp(number);
                province2.increaseSp(number);
                break;
            default:
                break;
            }
        }

        /**
         * description：写入文件
         * @param hashtable 保存着所有参与统计的省份
         * @param fileOutputStream 输出文件流
         * @param paramenterOfType -type的参数值
         * @param paramenterOfProvice -province的参数值
         * @param commandLineStrings 命令行数组 argv
         */
        public static void writeFile(Hashtable<String, Province> hashtable, FileOutputStream fileOutputStream, 
            String[] paramentersOfType, String[] paramentersOfProvince,String[] commandLineStrings) {
            String endLineString = "// 该文档并非真实数据，仅供测试使用";
            String commandLineString = "// 命令：";
            for(int i=0; i<commandLineStrings.length; i++) {
                commandLineString = commandLineString + commandLineStrings[i] + " ";
            }
            InfectStatistic infectStatistic = new InfectStatistic();
            Province wholeNation = hashtable.get("全国");
            try {
               
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF8");
                
                if(paramentersOfProvince[0].equals("null")) {   //没有指定省份
                    Set set = hashtable.keySet();
                    Iterator iterator = set.iterator();
                    List<Map.Entry<String,Province>> list = sortByHeadAlphabet(hashtable);       //排序
                    for (Map.Entry entry : list){
                        Province province = (Province) entry.getValue();
                        
                        if(paramentersOfType[0].equals("null")) {   //没有指定输出类型
                            outputStreamWriter.write(province.getAllResult() + "\r\n");
                            outputStreamWriter.flush();
                        }else {
                            outputStreamWriter.write(province.getResultByRequest(paramentersOfType) + "\r\n");
                            outputStreamWriter.flush();
                        }
                    }
                    outputStreamWriter.write(endLineString + "\r\n" + commandLineString);
                    outputStreamWriter.flush();
                }else { //指定省份
                    Hashtable<String, Province> requestProvinceHashtable = new Hashtable<String, InfectStatistic.Province>();
//                    for(int i=0; i<paramentersOfProvince.length; i++) {   // 别用.length，指定的省的个数不一定等于数组的大小
                    for(int i=0; paramentersOfProvince[i] != null; i++) {
                        if(!hashtable.containsKey(paramentersOfProvince[i])) {  //哈希表中不存在
                            Province province = infectStatistic.new Province(paramentersOfProvince[i], 0, 0, 0, 0);
                            requestProvinceHashtable.put(paramentersOfProvince[i], province);
                        }else { //哈希表中存在
                            Province province = hashtable.get(paramentersOfProvince[i]);
                            requestProvinceHashtable.put(paramentersOfProvince[i], province);
                        }
                    }
                   
                    List<Map.Entry<String,Province>> list = sortByHeadAlphabet(requestProvinceHashtable);       //排序

                    for (Map.Entry entry : list){
                        Province province = (Province) entry.getValue();
                        
                        if(paramentersOfType[0].equals("null")) {   //没有指定输出类型
                            outputStreamWriter.write(province.getAllResult() + "\r\n");
                            outputStreamWriter.flush();
                        }else {
                            outputStreamWriter.write(province.getResultByRequest(paramentersOfType) + "\r\n");
                            outputStreamWriter.flush();
                        }
                    }
                    outputStreamWriter.write(endLineString + "\r\n" + commandLineString);
                    outputStreamWriter.flush();
                }
                
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        /**
         * description：统计省份数据
         * @param lineString 一行字符串
         * @param hashtable 保存参与统计的省份
         */
        public static void calcProvince(String lineString, Hashtable<String, Province> hashtable) {
            InfectStatistic infectStatistic = new InfectStatistic();
            String[] afterSplitStrings = lineString.split(" ");
            int numAfterSplit = afterSplitStrings.length; // 切割后数量
            int number = ToolMethods.getNumber(afterSplitStrings[numAfterSplit - 1]); // 一行信息中涉及的人数
            String[] provinceNameStrings = ToolMethods.getNeedModifyProvinceNames(afterSplitStrings);   //需要修改数据的省份名称
            int operateType = ToolMethods.getOperateType(afterSplitStrings);    // 获得操作类型

            if (provinceNameStrings[1].equals("")) { // 只有一个省
                if (!hashtable.containsKey(provinceNameStrings[0])) { // 哈希表中没有该省
                    Province province = infectStatistic.new Province(provinceNameStrings[0], 0, 0, 0, 0);
                    ToolMethods.executeOperate(province, province, operateType, number);
                    hashtable.put(province.getProvinceName(), province);
                } else {
                    Province province = hashtable.get(provinceNameStrings[0]);
                    ToolMethods.executeOperate(province, province, operateType, number);
                }
            } else if (!provinceNameStrings[1].equals("")) { // 有两个省
                Province province1 = null;
                Province province2 = null;
                if (hashtable.containsKey(provinceNameStrings[0]) && hashtable.containsKey(provinceNameStrings[1])) {
                    province1 = hashtable.get(provinceNameStrings[0]);
                    province2 = hashtable.get(provinceNameStrings[1]);
                } else if (hashtable.containsKey(provinceNameStrings[0])
                        && !hashtable.containsKey(provinceNameStrings[1])) {
                    province1 = hashtable.get(provinceNameStrings[0]);
                    province2 = infectStatistic.new Province(provinceNameStrings[1], 0, 0, 0, 0);
                    hashtable.put(provinceNameStrings[1], province2);
                } else if (!hashtable.containsKey(provinceNameStrings[0])
                        && hashtable.containsKey(provinceNameStrings[1])) {
                    province1 = infectStatistic.new Province(provinceNameStrings[0], 0, 0, 0, 0);
                    hashtable.put(provinceNameStrings[0], province1);
                    province2 = hashtable.get(provinceNameStrings[1]);
                } else if (!hashtable.containsKey(provinceNameStrings[0])
                        && !hashtable.containsKey(provinceNameStrings[1])) {
                    province1 = infectStatistic.new Province(provinceNameStrings[0], 0, 0, 0, 0);
                    province2 = infectStatistic.new Province(provinceNameStrings[1], 0, 0, 0, 0);
                    hashtable.put(provinceNameStrings[0], province1);
                    hashtable.put(provinceNameStrings[1], province2);

                }
                ToolMethods.executeOperate(province1, province2, operateType, number);
            }

        }

        /**
         * description：统计全国的数据
         * @param hashtable 保存着所有参与统计的省份
         */
        public static void calcWholeNation(Hashtable<String, Province> hashtable) {
            InfectStatistic infectStatistic = new InfectStatistic();
            Province wholeNation = infectStatistic.new Province("全国", 0, 0, 0, 0);
            Set set = hashtable.keySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Object keyObject = iterator.next();
                wholeNation.ip += hashtable.get(keyObject).getIp();
                wholeNation.sp += hashtable.get(keyObject).getSp();
                wholeNation.cure += hashtable.get(keyObject).getCure();
                wholeNation.dead += hashtable.get(keyObject).getDead();
            }
            hashtable.put("全国", wholeNation);
        }
        
        /**
         * description：HashMap根据value获取key
         * @param map 要反转的HashMap
         * @param value map的值
         * @return 返回获得的key值，如果没找到返回-1
         */
        public static int getKey(HashMap<Integer, String> map, String value) {
            int res = -1;
            for(int getKey:map.keySet()) {
                if(map.get(getKey).equals(value)) {
                    res = getKey;
                }
            }
            return res;
        }

        /**
         * description：按城市首字母排序，“全国”置顶
         * @param hashtable 保存着所有参与统计的省份，是要排序的对象
         * @return 返回排序后的lis集合
         */
        public static List<Map.Entry<String,Province>> sortByHeadAlphabet(Hashtable<String, Province> hashtable) {
            Hashtable<String, String> alphabetOfProvince = new Hashtable<String, String>(35);
            alphabetOfProvince.put("全国", "AAAQG");
            alphabetOfProvince.put("北京", "BJ");
            alphabetOfProvince.put("天津", "TJ");
            alphabetOfProvince.put("上海", "SH");
            alphabetOfProvince.put("重庆", "CQ");
            alphabetOfProvince.put("河北", "HB");
            alphabetOfProvince.put("山西", "SX");
            alphabetOfProvince.put("辽宁", "LN");
            alphabetOfProvince.put("吉林", "JL");
            alphabetOfProvince.put("黑龙江", "HLJ");
            alphabetOfProvince.put("江苏", "JS");
            alphabetOfProvince.put("浙江", "ZJ");
            alphabetOfProvince.put("安徽", "AH");
            alphabetOfProvince.put("福建", "FJ");
            alphabetOfProvince.put("江西", "JX");
            alphabetOfProvince.put("山东", "SD");
            alphabetOfProvince.put("河南", "HN");
            alphabetOfProvince.put("湖北", "HB");
            alphabetOfProvince.put("湖南", "HN");
            alphabetOfProvince.put("广东", "GD");
            alphabetOfProvince.put("海南", "HN");
            alphabetOfProvince.put("四川", "SC");
            alphabetOfProvince.put("贵州", "GZ");
            alphabetOfProvince.put("云南", "YN");
            alphabetOfProvince.put("陕西", "SX");
            alphabetOfProvince.put("甘肃", "GS");
            alphabetOfProvince.put("青海", "QH");
            alphabetOfProvince.put("台湾", "TW");
            alphabetOfProvince.put("内蒙古自治区", "NMG");
            alphabetOfProvince.put("广西壮族自治区", "GXZZ");
            alphabetOfProvince.put("西藏自治区", "XZZZQ");
            alphabetOfProvince.put("宁夏回族自治区", "NX");
            alphabetOfProvince.put("新疆维吾尔自治区", "XZ");
            alphabetOfProvince.put("香港", "XG");
            alphabetOfProvince.put("澳门", "AM");        
            
            List<Map.Entry<String,Province>> list = new ArrayList<>(hashtable.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String, Province>>() {
                @Override
                public int compare(Map.Entry<String, Province> o1, Map.Entry<String, Province> o2) {
                    return alphabetOfProvince.get(o1.getKey()).compareTo(alphabetOfProvince.get(o2.getKey()));
                }
            });
            
            return list;
        }
        
        /**
         * description：初始化参数名哈希表
         * @return 返回一个包含所有参数名的哈希表
         */
        public static HashMap<Integer, String> initParamentHashMap() {
            HashMap<Integer, String> paramenterHashMap = new HashMap<Integer, String>(5);
            paramenterHashMap.put(1, "-log");
            paramenterHashMap.put(2, "-out");
            paramenterHashMap.put(3, "-date");
            paramenterHashMap.put(4, "-type");
            paramenterHashMap.put(5, "-province");
            
            return paramenterHashMap;
        }
    
    
    }

    public static void main(String[] args) {
        
        HashMap<Integer, String> paramenterHashMap = ToolMethods.initParamentHashMap(); //一个包含所有参数名的哈希表
        
        String[] paramenterStrings = new String[args.length - 1];   //存储传入的参数名、参数值
        for(int i=1; i<args.length; i++) {
            paramenterStrings[i-1] = args[i];
        }
        
        int[] indexOfParamenterStrings = {-1, -1, -1, -1, -1, -1};
        //找到参数名，并记录位置
        for(int i=0; i<paramenterStrings.length; i++) {
            int key = ToolMethods.getKey(paramenterHashMap, paramenterStrings[i]);
            if( key != -1) {   //是参数名
                indexOfParamenterStrings[key] = i;   //key对应的参数名在patamenterStrings的i下标位置,值为-1则代表无此参数名
            }
        }
        
        String directoryString = "./log";   // log 日志文件目录
        String outputFileNameString = "./result/testOutput.txt";    //输出路径/文件名
        String toDateString = ToolMethods.getToday(); //统计到哪一天
        String[] paramentersOfType = new String[10];;  //type的参数值
        String[] paramentersOfProvince = new String[25];  //province的参数值
        paramentersOfType[0] = "null";
        paramentersOfProvince[0] = "null";
        
        //接着处理每个参数名对应的参数值
        for(int i=1; i<=5; i++) {
            if(indexOfParamenterStrings[i] != -1) { //传入了该参数名
                if(i == 1) {    // -log
                    directoryString = paramenterStrings[indexOfParamenterStrings[i] + 1];    //配置log路径
                }else if(i == 2) {  //-out
                    outputFileNameString = paramenterStrings[indexOfParamenterStrings[i] + 1];      //配置输出文件路径
                }else if(i == 3) {  //-date
                    toDateString = paramenterStrings[indexOfParamenterStrings[i] + 1];  //统计到哪一天
                }else if(i == 4) {  //-type 可能会有多个参数
                    String[] paramenterValues = new String[20]; //记录所有参数值
                    int cnt = 0;
                    //取得参数值，直到找到下一个参数名时停止，   当前参数名 参数值1 参数值2 ... 下一个参数名
                     for(int j=indexOfParamenterStrings[i]+1; 
                        j<paramenterStrings.length && ToolMethods.getKey(paramenterHashMap, paramenterStrings[j])==-1; j++) { 
                        paramenterValues[cnt++] = paramenterStrings[j];
                        paramentersOfType = paramenterValues;
                    }
                }else if(i == 5) {  //-province
                    String[] paramenterValues = new String[20];
                    int cnt = 0;
                    //取得参数值，直到找到下一个参数名时停止，   当前参数名 参数值1 参数值2 ... 下一个参数名
                     for(int j=indexOfParamenterStrings[i]+1; 
                        j<paramenterStrings.length && ToolMethods.getKey(paramenterHashMap, paramenterStrings[j])==-1; j++) { 
                        paramenterValues[cnt++] = paramenterStrings[j];
                        paramentersOfProvince = paramenterValues;
                    }
                }
            }
        }
        
        InfectStatistic infectStatistic = new InfectStatistic();
        Hashtable<String, Province> hashtable = new Hashtable<String, Province>(40);    //存储省份的哈希表
        ArrayList<String> listFileNameArrayList = new ArrayList<String>();      //用来保存一个文件夹下的文件夹名的数组
        ToolMethods.getBeforeDateFileName(directoryString, toDateString, listFileNameArrayList);    //初始化listFileNameArrayList
        
        try {
            File file = null;
            File outputFile = new File(outputFileNameString);
            FileOutputStream fileOutputStream = null;
            InputStreamReader reader = null;
            for (int cnt = 0; cnt < listFileNameArrayList.size(); cnt++) {
                String filePathString = "./log/" + listFileNameArrayList.get(cnt);
                file = new File(filePathString);
                
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }
                if (file.isFile() && file.exists()) {
                    reader = new InputStreamReader(new FileInputStream(file), "UTF8");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    fileOutputStream = new FileOutputStream(outputFileNameString);

                    String lineString = null;
                    while ((lineString = bufferedReader.readLine()) != null) {
                        if (!ToolMethods.isAnnotation(lineString)) { // 不是注释行
                            ToolMethods.calcProvince(lineString, hashtable); // 进行统计
                        } else { // 是注释行，不执行任何操作
                            ;
                        }
                    }
                }
            }
            
            ToolMethods.calcWholeNation(hashtable);
            ToolMethods.writeFile(hashtable, fileOutputStream, paramentersOfType, paramentersOfProvince, args);
            reader.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }


    }
}
