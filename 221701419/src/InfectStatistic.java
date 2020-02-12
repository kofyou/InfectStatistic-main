
 /**
 * InfectStatistic
 * @author HHQ
 * @version 1.0
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
    
    }

    public static void main(String[] args) {
    }
}
