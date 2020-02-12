
/**
 * InfectStatistic
 * @author HHQ
 * @version 1.2
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
         * @return 返回值为分割后的数组
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
    
    }

    public static void main(String[] args) {
        
    }
}
