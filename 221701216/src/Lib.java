import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lib {
    private Pattern[] p = {
            Pattern.compile("(.+) 新增 感染患者 (\\d+)人"),
            Pattern.compile("(.+) 新增 疑似患者 (\\d+)人"),
            Pattern.compile("(.+) 感染患者 流入 (.*) (\\d+)人"),
            Pattern.compile("(.+) 疑似患者 流入 (.*) (\\d+)人"),
            Pattern.compile("(.+) 死亡 (\\d+)人"),
            Pattern.compile("(.+) 治愈 (\\d+)人"),
            Pattern.compile("(.+) 疑似患者 确诊感染 (\\d+)人"),
            Pattern.compile("(.+) 排除 疑似患者 (\\d+)人"),
    };

    private HashMap<String, Integer> statIp = new HashMap<>(), statSp = new HashMap<>(), statCure = new HashMap<>(), statDead = new HashMap<>();
    private int totalIp = 0, totalSp = 0, totalCure = 0, totalDead = 0;
    private Set<String> provinceSet = new HashSet<>();

    public void loadData(String path, String endDate) {
        File file = new File(path);
        File[] files = file.listFiles();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date eDate;
        try {
            eDate = fmt.parse(endDate);
        } catch (ParseException e) {
            eDate = new Date();

        }
        if (files != null) {
            for (File f : files) {
                String filename = f.getName();
                if (f.isFile() && filename.endsWith(".txt")) {
                    String shortName = filename.substring(filename.lastIndexOf(File.separator) + 1);
                    String dateStr = shortName.substring(0, shortName.indexOf("."));
                    try {
                        Date curDate = fmt.parse(dateStr);
                        if (curDate.getTime() <= eDate.getTime()) {
                            loadFile(f);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void Print(String outFile, List<String> types, List<String> provinces) {
        boolean showNational = false;
        if (types.size() == 0) {
            types.add("ip");
            types.add("sp");
            types.add("cure");
            types.add("dead");
        }
        if (provinces.size() == 0) {
            showNational = true;
            provinces.addAll(provinceSet);
        } else {
            if (provinces.contains("全国")) {
                showNational = true;
                provinces.remove("全国");
            }
        }
        provinces.sort(Collator.getInstance(java.util.Locale.CHINA));
        try {
            File f = new File(outFile);
            PrintWriter pw = new PrintWriter(f);
            if (showNational) {
                StringBuilder n = new StringBuilder("全国");
                for (String t : types) {
                    switch (t) {
                        case "ip":
                            n.append(" 感染患者").append(totalIp).append("人");
                            break;
                        case "sp":
                            n.append(" 疑似患者").append(totalSp).append("人");
                            break;
                        case "cure":
                            n.append(" 治愈").append(totalCure).append("人");
                            break;
                        case "dead":
                            n.append(" 死亡").append(totalDead).append("人");
                            break;
                    }
                }
                pw.println(n.toString());
            }
            for (String p : provinces) {
                StringBuilder n = new StringBuilder(p);
                for (String t : types) {
                    switch (t) {
                        case "ip":
                            n.append(" 感染患者").append(statIp.getOrDefault(p, 0)).append("人");
                            break;
                        case "sp":
                            n.append(" 疑似患者").append(statSp.getOrDefault(p, 0)).append("人");
                            break;
                        case "cure":
                            n.append(" 治愈").append(statCure.getOrDefault(p, 0)).append("人");
                            break;
                        case "dead":
                            n.append(" 死亡").append(statDead.getOrDefault(p, 0)).append("人");
                            break;
                    }
                }
                pw.println(n.toString());
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadFile(File f) {
        try {
            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                String l = s.nextLine();
                process(l);
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void process(String str) {
        for (int i = 0; i < p.length; i++) {
            Matcher m = p[i].matcher(str);
            if (m.matches()) {
                String province = m.group(1);
                String province2 = "";
                int num = 0;
                if (i == 2 || i == 3) {
                    province2 = m.group(2);
                    num = Integer.valueOf(m.group(3));
                    operate(i, province, province2, num);
                } else {
                    num = Integer.valueOf(m.group(2));
                    operate(i, province, num);
                }
            }
        }
    }

    private void operate(int type, String province, int num) {
        int ip, sp, cure, dead;
        provinceSet.add(province);
        if (type == 0) {
            ip = statIp.getOrDefault(province, 0);
            statIp.put(province, ip + num);
            totalIp += num;
        } else if (type == 1) {
            sp = statSp.getOrDefault(province, 0);
            statSp.put(province, sp + num);
            totalSp += num;
        } else if (type == 4) {
            ip = statIp.getOrDefault(province, 0);
            statIp.put(province, ip - num);
            totalIp -= num;
            dead = statDead.getOrDefault(province, 0);
            statDead.put(province, dead + num);
            totalDead += num;
        } else if (type == 5) {
            ip = statIp.getOrDefault(province, 0);
            statIp.put(province, ip - num);
            totalIp -= num;
            cure = statCure.getOrDefault(province, 0);
            statCure.put(province, cure + num);
            totalCure += num;
        } else if (type == 6) {
            ip = statIp.getOrDefault(province, 0);
            statIp.put(province, ip + num);
            totalIp += num;
            sp = statSp.getOrDefault(province, 0);
            statSp.put(province, sp - num);
            totalSp -= num;
        } else if (type == 7) {
            sp = statSp.getOrDefault(province, 0);
            statSp.put(province, sp - num);
            totalSp -= num;
        }
    }

    private void operate(int type, String outProvince, String inProvince, int num) {
        int ip, sp;
        provinceSet.add(inProvince);
        provinceSet.add(outProvince);
        if (type == 2) {
            ip = statIp.getOrDefault(outProvince, 0);
            statIp.put(outProvince, ip - num);
            ip = statIp.getOrDefault(inProvince, 0);
            statIp.put(inProvince, ip + num);
        } else if (type == 3) {
            sp = statSp.getOrDefault(outProvince, 0);
            statSp.put(outProvince, sp - num);
            sp = statSp.getOrDefault(inProvince, 0);
            statSp.put(inProvince, sp + num);
        }
    }
}
