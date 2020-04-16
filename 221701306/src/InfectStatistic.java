import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.lang.String;

/**
 * InfectStatistic TODO
 *
 * @author 221701306高雨欣
 * @version 1.0
 * @since 2020-02-12
 */
public class InfectStatistic {
    public static void main(String[] args) {
        // 命令输入格式检查
        if (!ListCommand.checkInput(args)) {
            System.out.println("命令输入格式错误，退出程序");
            System.exit(1);
        }
        // 初始化命令和数据表
        ListCommand aListCommand = new ListCommand(args);
        Statistic aStatistic = new Statistic();
        // 检查命令是否符合要求
        aListCommand.checkCommand(aStatistic);
        // 初始化日志目录
        LogFiles aLogFiles = new LogFiles(aListCommand.arguments[0].value);
        // 读取日志
        aLogFiles.readFiles(aListCommand.arguments[2].value, aStatistic);
        // 输出统计结果
        aStatistic.outPutFile(aListCommand.arguments[1].value, (TypeArgument) aListCommand.arguments[3],
            (ProvinceArgument) aListCommand.arguments[4]);
        // 完成
        System.out.println("运行成功！统计数据存放在" + aListCommand.arguments[1].value);
    }

}

class ListCommand {
    String name;
    BaseArgument[] arguments;

    ListCommand(String[] strs) {
        name = strs[0];
        arguments = new BaseArgument[5];
        arguments[0] = new LogArgument("-log");
        arguments[1] = new OutArgument("-out");
        arguments[2] = new DateArgument("-date");
        arguments[3] = new TypeArgument("-type");
        arguments[4] = new ProvinceArgument("-province");

        // 分析命令行 赋值arguments
        for (int i = 1, k = 0; i < strs.length && k < 5; ++i) {
            int n = 1;
            // 经检查strs[1]一定是参数
            ++i;
            while (!strs[i].startsWith("-")) {
                ++n;
                ++i;
                if (i == strs.length) {
                    break;
                }
            }
            --i;
            // 创建赋值BaseArgument的string数组
            String[] argStrings = new String[n];
            for (int j = 0; j < n; ++j) {
                argStrings[j] = strs[i - n + 1 + j];
            }

            // 赋值BaseArgument[]
            switch (argStrings[0]) {
            case "-log":
                arguments[0] = new LogArgument(argStrings);
                break;
            case "-out":
                arguments[1] = new OutArgument(argStrings);
                break;
            case "-date":
                arguments[2] = new DateArgument(argStrings);
                break;
            case "-type":
                arguments[3] = new TypeArgument(argStrings);
                break;
            case "-province":
                arguments[4] = new ProvinceArgument(argStrings);
                break;
            default:
                ;
            }
        }
    }

    static boolean checkInput(String[] strs) {
        // 若命令不是list 则返回false
        if (!strs[0].equals("list")) {
            return false;
        }
        // list后紧跟参数
        if (!strs[1].startsWith("-")) {
            return false;
        }
        // 参数名正确：若参数不属于五种 则返回false
        for (int i = 1; i < strs.length; ++i) {
            if (strs[i].startsWith("-")) {
                if (!(strs[i].equals("-log") || strs[i].equals("-out") || strs[i].equals("-type")
                    || strs[i].equals("-date") || strs[i].equals("-province"))) {
                    return false;
                }
            }
        }
        // 若参数不包含-log和-out 则返回错误 //且-log只出现一次
        for (int i = 1; i < strs.length; ++i) {
            if (strs[i].equals("-log")) {
                for (int j = i + 1; j < strs.length; ++j) {
                    // -log出现多次
                    if (strs[j].equals("-log")) {
                        return false;
                    }
                }
                break;
            }
            // 没找到-log
            if (i == strs.length - 1) {
                return false;
            }
        }
        for (int i = 1; i < strs.length; ++i) {
            if (strs[i].equals("-out")) {
                for (int j = i + 1; j < strs.length; ++j) {
                    // -out出现多次
                    if (strs[j].equals("-log")) {
                        return false;
                    }
                }
                break;
            }
            // 没找到-out
            if (i == strs.length - 1) {
                return false;
            }
        }
        // 每种参数-date -type -province最多出现一次
        for (int i = 1; i < strs.length; ++i) {
            // 存在-date
            if (strs[i].equals("-date")) {
                for (int j = i + 1; j < strs.length; ++j) {
                    // -date出现多次
                    if (strs[j].equals("-date")) {
                        return false;
                    }
                }
                break;
            }
        }
        for (int i = 1; i < strs.length; ++i) {
            // 存在-type
            if (strs[i].equals("-type")) {
                for (int j = i + 1; j < strs.length; ++j) {
                    // -type出现多次
                    if (strs[j].equals("-type")) {
                        return false;
                    }
                }
                break;
            }
        }
        for (int i = 1; i < strs.length; ++i) {
            // 存在-province
            if (strs[i].equals("-province")) {
                for (int j = i + 1; j < strs.length; ++j) {
                    // -province出现多次
                    if (strs[j].equals("-province")) {
                        return false;
                    }
                }
                break;
            }
        }
        return true;
    }

    void checkCommand(Statistic sta) {
        for (int i = 0; i < 4; ++i) {
            if (!arguments[i].checkError()) {
                System.exit(1);
            }
        }
        ((ProvinceArgument) arguments[4]).checkError(sta);
    }

}

abstract class BaseArgument {
    String name;
    String value;

    BaseArgument(String name) {
        this.name = name;
        value = "";
    }

    BaseArgument(String[] strs) {
        name = strs[0];
        value = "";
    }

    abstract boolean checkError();
}

class LogArgument extends BaseArgument {

    LogArgument(String name) {
        super(name);
    }

    LogArgument(String[] strs) {
        super(strs);
        if (strs.length > 2) {
            value = "*";
        }
        value += strs[1];
    }

    boolean checkError() {
        // -log只有一个参数值
        if (value.startsWith("*")) {
            System.out.println("-log参数值错误，退出程序");
            return false;
        }
        // -log的参数值是一个目录的路径
        File alog = new File(value);
        if (!alog.isDirectory()) {
            return false;
        }
        return true;
    }
}

class OutArgument extends BaseArgument {

    OutArgument(String name) {
        super(name);
    }

    OutArgument(String[] strs) {
        super(strs);
        if (strs.length > 2) {
            value = "*";
        }
        value += strs[1];
    }

    boolean checkError() {
        // -out只有一个参数值
        if (value.startsWith("*")) {
            System.out.println("-out参数值错误，退出程序");
            return false;
        }
        // 若输入的生成文件路径没有后缀 或后缀不为".txt" 则修改为默认
        if (!value.endsWith(".txt")) {
            int x = value.lastIndexOf('.');
            if (x >= 0) {
                value = value.substring(0, x);
            }
            value += ".txt";
            System.out.println("输出文件格式有误，改为" + value);
        }
        return true;
    }
}

class DateArgument extends BaseArgument {

    DateArgument(String name) {
        super(name);
        value = "lastdate";
    }

    DateArgument(String[] strs) {
        super(strs);
        if (strs.length > 2) {
            value = "*";
        }
        value += strs[1];
    }

    boolean checkError() {
        // -date只有一个参数值
        if (value.startsWith("*")) {
            System.out.println("-date参数值错误，退出程序");
            return false;
        }
        // 日期格式检查,满足平年闰年的YYYY-MM-DD格式
        if (!value.matches(
            "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]"
                + "|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468]"
                + "[048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)   \r\n(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]"
                + "{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))"
                + "|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))"
                + "-02-29) \r\n")) {
            value = "lastdate";
            System.out.println("日期格式应为YYYY-MM-DD满足平闰，默认最新日期");
        }
        return true;
    }
}

class TypeArgument extends BaseArgument {

    String[] valueList;

    TypeArgument(String name) {
        super(name);
    }

    TypeArgument(String[] strs) {
        super(strs);
        value = "valuelist";
        int n = strs.length - 1;
        // 参数值只能为ip,sp,cure,dead,若不是就忽略该错误输入
        for (int i = 1; i < strs.length; ++i) {
            if (!(strs[i].equals("ip") || strs[i].equals("sp") || strs[i].equals("cure") || strs[i].equals("dead"))) {
                strs[i] = "";
                --n;
                System.out.println("-type参数值只能为ip,sp,cure,dead，忽略该错误参数值");
            }
        }
        // 若全部输入错误,则视为参数缺省
        if (n == 0) {
            valueList = new String[] { "ip", "sp", "cure", "dead" };
            return;
        }
        valueList = new String[n];
        for (int i = 0, j = 1; i < n; ++i) {
            while (strs[j].equals("")) {
                ++j;
            }
            valueList[i] = strs[j];
        }
    }

    boolean checkError() {
        if (value == "") {
            return true;
        }
        // 参数值不能重复,若重复则删去
        int n = valueList.length;
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (valueList[i].equals(valueList[j])) {
                    valueList[j] = valueList[--n];
                }
            }
        }
        String[] temp = new String[n];
        for (int i = 0; i < n; ++i) {
            temp[i] = valueList[i];
        }
        valueList = temp;
        return true;
    }
}

class ProvinceArgument extends BaseArgument {

    String[] valueList;

    ProvinceArgument(String name) {
        super(name);
    }

    ProvinceArgument(String[] strs) {
        super(strs);
        value = "valuelist";
        valueList = new String[strs.length - 1];
        for (int i = 1; i < strs.length; ++i) {
            valueList[i - 1] = strs[i];
        }
    }

    boolean checkError() {
        return true;
    }

    boolean checkError(Statistic sta) {
        if (value == "") {
            return true;
        }
        // 参数值不能重复,若重复则删去
        int n = valueList.length;
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (valueList[i].equals(valueList[j])) {
                    valueList[j] = valueList[--n];
                }
            }
        }
        String[] temp = new String[n];
        for (int i = 0; i < n; ++i) {
            temp[i] = valueList[i];
        }
        valueList = temp;

        // 输入的省份存在,若不存在则列出所有
        for (int i = 0; i < valueList.length; ++i) {
            if (!sta.data.containsKey(valueList[i])) {
                value = "";
                valueList = new String[0];
                System.out.println("-province参数输入有误，默认列出所有省份数据");
            }
        }
        return true;
    }
}

class LogFiles {
    String lastDate;
    TreeSet<File> files;

    LogFiles(String path) {
        File logFile = new File(path);
        File[] temp = logFile.listFiles();
        files = new TreeSet<File>(new Comparator<File>() {
            @Override
            // 文件按日期排序 重写匿名内部类Comparator的compare()
            public int compare(File f0, File f1) {
                String name0 = f0.getName();
                String name1 = f1.getName();
                // 按年份月份日期依次比较时间前后
                if (Integer.parseInt(name0.substring(0, 4)) < Integer.parseInt(name1.substring(0, 4))) {
                    return -1;
                }
                if (Integer.parseInt(name0.substring(0, 4)) > Integer.parseInt(name1.substring(0, 4))) {
                    return 1;
                }
                if (Integer.parseInt(name0.substring(5, 7)) < Integer.parseInt(name1.substring(5, 7))) {
                    return -1;
                }
                if (Integer.parseInt(name0.substring(5, 7)) > Integer.parseInt(name1.substring(5, 7))) {
                    return 1;
                }
                if (Integer.parseInt(name0.substring(8, 10)) < Integer.parseInt(name1.substring(8, 10))) {
                    return -1;
                }
                if (Integer.parseInt(name0.substring(8, 10)) > Integer.parseInt(name1.substring(8, 10))) {
                    return 1;
                }
                return 0;
            }

        });
        for (int i = 0; i < temp.length; ++i) {
            files.add(temp[i]);
        }

        lastDate = files.last().getName().substring(0, 10);
    }

    void readFiles(String date, Statistic sta) {
        // 默认最新日期
        if (date.equals("lastdate")) {
            for (File f : files) {
                LogFiles.statisFile(f, sta);
            }
            return;
        }
        // 非默认日期
        for (File f : files) {
            if (LogFiles.dateCompare(date, f.getName().substring(0, 10))) {
                LogFiles.statisFile(f, sta);
                continue;
            }
            break;
        }

        // 各省数据统计到全国
        int[] all = { 0, 0, 0, 0 };
        for (String keytemp : sta.data.keySet()) {
            int[] valuetemp = sta.data.get(keytemp);
            for (int i = 0; i < 4; ++i) {
                all[i] += valuetemp[i];
            }
        }
        sta.data.put("全国", all);
    }

    // 统计某个日志文件的数据
    static void statisFile(File f, Statistic sta) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                // 忽略'/'开头的行
                if (line.startsWith("/")) {
                    continue;
                }
                LogFiles.statisLine(line, sta);
            }
            br.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // 处理日志文件中的一行
    static void statisLine(String line, Statistic sta) {
        String[] strs = line.split(" ");
        // 省份存在
        if (!sta.data.containsKey(strs[0])) {
            return;
        }
        // 8类日志项
        switch (strs[1]) {
        case "新增":
            if (strs[2].equals("感染患者")) {
                sta.data.get(strs[0])[0] += Integer.parseInt(strs[3].replace("人", ""));
                break;
            }
            if (strs[2].equals("疑似患者")) {
                sta.data.get(strs[0])[1] += Integer.parseInt(strs[3].replace("人", ""));
            }
            break;
        case "死亡":
            sta.data.get(strs[0])[0] -= Integer.parseInt(strs[2].replace("人", ""));
            sta.data.get(strs[0])[3] += Integer.parseInt(strs[2].replace("人", ""));
            break;
        case "治愈":
            sta.data.get(strs[0])[0] -= Integer.parseInt(strs[2].replace("人", ""));
            sta.data.get(strs[0])[2] += Integer.parseInt(strs[2].replace("人", ""));
            break;
        case "排除":
            sta.data.get(strs[0])[1] -= Integer.parseInt(strs[3].replace("人", ""));
            break;
        case "疑似患者":
            if (strs[2].equals("流入")) {
                sta.data.get(strs[0])[1] -= Integer.parseInt(strs[4].replace("人", ""));
                sta.data.get(strs[3])[1] += Integer.parseInt(strs[4].replace("人", ""));
                break;
            }
            if (strs[2].equals("确诊感染")) {
                sta.data.get(strs[0])[1] -= Integer.parseInt(strs[3].replace("人", ""));
                sta.data.get(strs[0])[0] += Integer.parseInt(strs[3].replace("人", ""));
            }
            break;
        case "感染患者":
            if (strs[2].equals("流入")) {
                sta.data.get(strs[0])[0] -= Integer.parseInt(strs[4].replace("人", ""));
                sta.data.get(strs[3])[0] += Integer.parseInt(strs[4].replace("人", ""));
            }
            break;
        default:
            break;
        }
    }

    // 比较两个YYYY-MM-DD日期字符串 若date0大于等于date1 则返回true
    static boolean dateCompare(String date0, String date1) {
        // 按年份月份日期依次比较时间前后
        if (Integer.parseInt(date0.substring(0, 4)) < Integer.parseInt(date0.substring(0, 4))) {
            return false;
        }
        if (Integer.parseInt(date0.substring(0, 4)) > Integer.parseInt(date1.substring(0, 4))) {
            return true;
        }
        if (Integer.parseInt(date0.substring(5, 7)) < Integer.parseInt(date1.substring(5, 7))) {
            return false;
        }
        if (Integer.parseInt(date0.substring(5, 7)) > Integer.parseInt(date1.substring(5, 7))) {
            return true;
        }
        if (Integer.parseInt(date0.substring(8, 10)) < Integer.parseInt(date1.substring(8, 10))) {
            return false;
        }
        if (Integer.parseInt(date0.substring(8, 10)) > Integer.parseInt(date1.substring(8, 10))) {
            return true;
        }
        return true;
    }
}

class Statistic {
    Map<String, int[]> data;

    public Statistic() {
        data = new HashMap<String, int[]>();
        data.put("全国", new int[] { 0, 0, 0, 0 });
        data.put("安徽", new int[] { 0, 0, 0, 0 });
        data.put("北京", new int[] { 0, 0, 0, 0 });
        data.put("重庆", new int[] { 0, 0, 0, 0 });
        data.put("福建", new int[] { 0, 0, 0, 0 });
        data.put("甘肃", new int[] { 0, 0, 0, 0 });
        data.put("广东", new int[] { 0, 0, 0, 0 });
        data.put("广西", new int[] { 0, 0, 0, 0 });
        data.put("贵州", new int[] { 0, 0, 0, 0 });
        data.put("海南", new int[] { 0, 0, 0, 0 });
        data.put("河北", new int[] { 0, 0, 0, 0 });
        data.put("河南", new int[] { 0, 0, 0, 0 });
        data.put("黑龙江", new int[] { 0, 0, 0, 0 });
        data.put("湖北", new int[] { 0, 0, 0, 0 });
        data.put("湖南", new int[] { 0, 0, 0, 0 });
        data.put("江西", new int[] { 0, 0, 0, 0 });
        data.put("吉林", new int[] { 0, 0, 0, 0 });
        data.put("江苏", new int[] { 0, 0, 0, 0 });
        data.put("辽宁", new int[] { 0, 0, 0, 0 });
        data.put("内蒙古", new int[] { 0, 0, 0, 0 });
        data.put("宁夏", new int[] { 0, 0, 0, 0 });
        data.put("青海", new int[] { 0, 0, 0, 0 });
        data.put("山西", new int[] { 0, 0, 0, 0 });
        data.put("山东", new int[] { 0, 0, 0, 0 });
        data.put("陕西", new int[] { 0, 0, 0, 0 });
        data.put("上海", new int[] { 0, 0, 0, 0 });
        data.put("四川", new int[] { 0, 0, 0, 0 });
        data.put("天津", new int[] { 0, 0, 0, 0 });
        data.put("西藏", new int[] { 0, 0, 0, 0 });
        data.put("新疆", new int[] { 0, 0, 0, 0 });
        data.put("云南", new int[] { 0, 0, 0, 0 });
        data.put("浙江", new int[] { 0, 0, 0, 0 });
    }

    void outPutFile(String path, TypeArgument types, ProvinceArgument provinces) {
        try {
            // 打开out文件
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            String outline;
            // 处理type参数值 用boolean[]分别表示是否输出四种类型
            boolean[] typetemp = { false, false, false, false };
            if (types.value.equals("valuelist")) {
                for (String temp : types.valueList) {
                    switch (temp) {
                    case "ip":
                        typetemp[0] = true;
                        break;
                    case "sp":
                        typetemp[1] = true;
                        break;
                    case "cure":
                        typetemp[2] = true;
                        break;
                    case "dead":
                        typetemp[3] = true;
                        break;
                    default:
                        break;
                    }
                }
            }
            if (types.value.equals("")) {
                typetemp[0] = true;
                typetemp[1] = true;
                typetemp[2] = true;
                typetemp[3] = true;
            }
            // 若输出所有省份数据 遍历数据,并按省份拼音排序写入文件
            if (provinces.value.equals("")) {
                String[] provincesort = { "全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江",
                    "湖北", "湖南", "江西", "吉林", "江苏", "辽宁", "内蒙古", "宁夏", "青海", "山西", "山东", "陕西", "上海", "四川", "天津", "西藏",
                    "新疆", "云南", "浙江" };
                for (int i = 0; i < provincesort.length; ++i) {
                    String keytemp = provincesort[i];
                    int[] valuetemp = data.get(keytemp);
                    outline = keytemp;
                    if (typetemp[0]) {
                        outline = outline + " 感染患者" + valuetemp[0] + "人";
                    }
                    if (typetemp[1]) {
                        outline = outline + " 疑似患者" + valuetemp[1] + "人";
                    }
                    if (typetemp[2]) {
                        outline = outline + " 治愈" + valuetemp[2] + "人";
                    }
                    if (typetemp[3]) {
                        outline = outline + " 死亡" + valuetemp[3] + "人";
                    }
                    bw.write(outline);
                    bw.newLine();
                }
            }
            // 若只输出-province参数值省份数据
            if (provinces.value.equals("valuelist")) {
                for (String provincetemp : provinces.valueList) {
                    int[] valuetemp = data.get(provincetemp);
                    outline = provincetemp;
                    if (typetemp[0]) {
                        outline = outline + " 感染患者" + valuetemp[0] + "人";
                    }
                    if (typetemp[1]) {
                        outline = outline + " 疑似患者" + valuetemp[1] + "人";
                    }
                    if (typetemp[2]) {
                        outline = outline + " 治愈" + valuetemp[2] + "人";
                    }
                    if (typetemp[3]) {
                        outline = outline + " 死亡" + valuetemp[3] + "人";
                    }
                    bw.write(outline);
                    bw.newLine();
                }
            }
            bw.write("// 该文档并非真实数据，仅供测试使用");
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}