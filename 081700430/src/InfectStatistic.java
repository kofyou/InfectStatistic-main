import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * InfectStatistic
 * TODO
 *
 * @author PSF
 * @email 52260506@qq.com
 * @version 1.0
 * @since 2020/2/9
 */
class InfectStatistic {
    private String log, out, date;
    private String [] type, province;
    private final int typeNum = 4, provinceNum = 34;

    private void init(String[] args) {
        type = new String[typeNum];
        province = new String[provinceNum];
        if (args.length > 0 && args[0].equals("list")) {
            for (int i = 1, typeI = 0, provinceI = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-log" :
                        this.log = args[++i];
                        break;
                    case "-out" :
                        this.out = args[++i];
                        break;
                    case "-date" :
                        this.date = args[++i];
                        break;
                    case "-type" :
                        i++;
                        while (args[i] != null && args[i].charAt(0) != '-') {
                            if (typeI < typeNum) {
                                this.type[typeI++] = args[i++];
                            }
                        }
                        break;
                    case "-province" :
                        i++;
                        while (args[i] != null && args[i].charAt(0) != '-') {
                            if (provinceI < provinceNum) {
                                this.province[provinceI++] = args[i++];
                            }
                        }
                        break;
                }
            }
        }
        else {
            System.out.println("请输入正确的命令和选项");
        }
    }

/*    private void dealLogs() {
        File file = new File(log);
        if (file.isDirectory()) {

        }
    }

    private void showPara() {
        System.out.println(log+" "+out+" "+date);
        for (int i = 0; i < typeNum; i++) {

        }
    } */

    public static void main(String[] args) {
        InfectStatistic infect = new InfectStatistic();
        infect.init(args);
        System.out.println("helloworld");
    }

}
