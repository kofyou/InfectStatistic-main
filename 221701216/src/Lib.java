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
    private Pattern[] p = {//匹配正则表达式模式
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
    private Set<String> provinceSet = new HashSet<>();//哈希表存储

    public void loadData(String path, String endDate) {//读取截止日期前的文本
        File file = new File(path);
        File[] files = file.listFiles();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");//日期格式
        Date eDate;
        try {
            eDate = fmt.parse(endDate);//解析截止日期
        } catch (ParseException e) {
            eDate = new Date();

        }
        if (files != null) {
            for (File f : files) {
                String filename = f.getName();
                if (f.isFile() && filename.endsWith(".txt")) {//索引
                    String shortName = filename.substring(filename.lastIndexOf(File.separator) + 1);
                    String dateStr = shortName.substring(0, shortName.indexOf("."));
                    try {
                        Date curDate = fmt.parse(dateStr);//解析当前日期
                        if (curDate.getTime() <= eDate.getTime()) {
                            loadFile(f);//当前日期小于截止日期则读取文本
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void Print(String outFile, List<String> types, List<String> provinces) {//输出统计文本
        boolean showNational = false;
        if (types.size() == 0) {
            types.add("ip");
            types.add("sp");
            types.add("cure");
            types.add("dead");
        }//添加类型
        if (provinces.size() == 0) {
            showNational = true;
            provinces.addAll(provinceSet);
        } else {
            if (provinces.contains("全国")) {
                showNational = true;
                provinces.remove("全国");
            }//省份不包括全国
        }
        provinces.sort(Collator.getInstance(java.util.Locale.CHINA));
        try {
            File f = new File(outFile);
            PrintWriter pw = new PrintWriter(f);
            if (showNational) {
                StringBuilder n = new StringBuilder("全国");
                for (String t : types) {//选择显示类型
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
            for (String p : provinces) {//选择显示省份
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
            pw.println("//该文档并非真实数据，仅供测试使用");
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

    private void process(String str) {//疫情变化情况统计
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

    private void operate(int type, String province, int num) {//患者身体变化情况
        int ip, sp, cure, dead;
        provinceSet.add(province);
        if (type == 0) {//感染患者增加
            ip = statIp.getOrDefault(province, 0);
            statIp.put(province, ip + num);
            totalIp += num;
        } else if (type == 1) {//疑似患者增加
            sp = statSp.getOrDefault(province, 0);
            statSp.put(province, sp + num);
            totalSp += num;
        } else if (type == 4) {//感染患者死亡
            ip = statIp.getOrDefault(province, 0);
            statIp.put(province, ip - num);
            totalIp -= num;
            dead = statDead.getOrDefault(province, 0);
            statDead.put(province, dead + num);
            totalDead += num;
        } else if (type == 5) {//感染患者治愈
            ip = statIp.getOrDefault(province, 0);
            statIp.put(province, ip - num);
            totalIp -= num;
            cure = statCure.getOrDefault(province, 0);
            statCure.put(province, cure + num);
            totalCure += num;
        } else if (type == 6) {//疑似患者确诊
            ip = statIp.getOrDefault(province, 0);
            statIp.put(province, ip + num);
            totalIp += num;
            sp = statSp.getOrDefault(province, 0);
            statSp.put(province, sp - num);
            totalSp -= num;
        } else if (type == 7) {//疑似患者排除
            sp = statSp.getOrDefault(province, 0);
            statSp.put(province, sp - num);
            totalSp -= num;
        }
    }

    private void operate(int type, String outProvince, String inProvince, int num) {//患者流动情况
        int ip, sp;
        provinceSet.add(inProvince);
        provinceSet.add(outProvince);
        if (type == 2) {//感染患者
            ip = statIp.getOrDefault(outProvince, 0);
            statIp.put(outProvince, ip - num);
            ip = statIp.getOrDefault(inProvince, 0);
            statIp.put(inProvince, ip + num);
        } else if (type == 3) {//疑似患者
            sp = statSp.getOrDefault(outProvince, 0);
            statSp.put(outProvince, sp - num);
            sp = statSp.getOrDefault(inProvince, 0);
            statSp.put(inProvince, sp + num);
        }
    }
}
