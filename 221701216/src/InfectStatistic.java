import java.util.ArrayList;
import java.util.List;

class InfectStatistic {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("使用方式 java InfectStatistic list ");
            return;
        }
        String action = args[0];
        if (!"list".equals(action)) {
            System.out.println("暂不支持其他操作");
            return;
        }
        String inDir = "";
        String outDir = "";
        String date = "";
        List<String> types = new ArrayList<>();
        List<String> provinces = new ArrayList<>();
        int curType = 0;
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                curType = 0;
                if ("-log".equals(args[i])) {
                    inDir = args[i + 1];
                    i++;
                } else if ("-out".equals(args[i])) {
                    outDir = args[i + 1];
                    i++;
                } else if ("-date".equals(args[i])) {
                    date = args[i + 1];
                    i++;
                } else if ("-type".equals(args[i])) {
                    curType = 1;
                } else if ("-province".equals(args[i])) {
                    curType = 2;
                }
            } else {
                if (curType == 1) {
                    types.add(args[i]);
                } else if (curType == 2) {
                    provinces.add(args[i]);
                }
            }
        }
        Lib lib = new Lib();
        lib.loadData(inDir, date);
        lib.Print(outDir, types, provinces);
    }
}
