import java.util.ArrayList;

 /**
 * InfectStatistic
 * @author HHQ
 * @version 1.1
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
    
    }

    public static void main(String[] args) {
        
    }
}
