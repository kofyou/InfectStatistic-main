import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * InfectStatistic
 * @author HHQ
 * @version 1.4
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

    }

    /**
     * description：静态的一个工具类，一些静态的方法
     * @author HHQ
     */
    static class ToolMethods {
        
        /**
         * description：将一个字符串以空格" "分割
         * @param string 传入的字符串
         * @return 返回值为分割后的数组数量
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

//                    fileName.addAll(Arrays.asList(nameStrings));
                }
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
            String[] provinceNameStrings = ToolMethods.getNeedModifyProvinceNames(afterSplitStrings);
            int operateType = ToolMethods.getOperateType(afterSplitStrings);

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

    
    }

    public static void main(String[] args) {
        
    }
}
