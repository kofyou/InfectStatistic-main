import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import org.junit.Test;

import com.sun.javafx.scene.EnteredExitedHandler;

import java.lang.String;

/**
 * InfectStatistic TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
public class InfectStatistic {
    public static void main(String[] args) {
        ListCommand aListCommand=new ListCommand(args);
        Statistic aStatistic =new Statistic();
        aListCommand.checkCommand(aStatistic);
        LogFiles aLogFiles=new LogFiles(aListCommand.arguments[0].value);
        System.out.println("helloworld");
    }

}

class ListCommand {
    String name;
    BaseArgument[] arguments;

    ListCommand(String[] strs) {
        // TODO Auto-generated constructor stub
        name = strs[0];
        arguments = new BaseArgument[5];
        arguments[0]=new LogArgument("-log");
        arguments[1]=new OutArgument("-out");
        arguments[2]=new DateArgument("-date");
        arguments[3]=new TypeArgument("-type");
        arguments[4]=new ProvinceArgument("-province");

        //命令输入格式检查
        if(!checkInput(strs)) {
            System.out.println("命令输入格式错误，退出程序");
            System.exit(1);
        }
        
        //分析命令行 赋值arguments
        for (int i = 1, k = 0; i < strs.length && k < 5; ++i) {
            int n = 1;
            //经检查strs[1]一定是参数
            ++i;
            while (!strs[i].startsWith("-")) {
                ++n;
                ++i;
            }
            --i;
            // 创建赋值BaseArgument的string数组
            String[] argStrings = new String[n];
            for (int j = 0; j < n; ++j) {
                argStrings[j] = strs[i - n + 1 + j];
            }
            
            //赋值BaseArgument[]
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
            default:;
            }
        }
    }

    boolean checkInput(String[] strs) {
        //若命令不是list 则返回false
        if(!name.equals("list")) {
            return false;
        }
        //list后紧跟参数
        if(!strs[1].startsWith("-")) {
            return false;
        }
        // 参数名正确：若参数不属于五种 则返回false
        for(int i=1;i<strs.length;++i) {
            if(strs[i].startsWith("-")) {
                if(!(strs[i].equals("-log")||strs[i].equals("-out")||strs[i].equals("-type")||strs[i].equals("-date")||strs[i].equals("-province"))) {
                    return false;
                }
            }
        }
        //若参数不包含-log和-out 则返回错误 //且-log只出现一次
        for(int i=1;i<strs.length;++i) {
            if(strs[i].equals("-log")) {
                for(int j=i+1;j<strs.length;++j) {
                    //-log出现多次
                    if(strs[j].equals("-log")) {
                        return false;
                    }
                }
                break;
            }
            //没找到-log
            if(i==strs.length-1) {
                return false;
            }
        }
        for(int i=1;i<strs.length;++i) {
            if(strs[i].equals("-out")) {
                for(int j=i+1;j<strs.length;++j) {
                    //-out出现多次
                    if(strs[j].equals("-log")) {
                        return false;
                    }
                }
                break;
            }
            //没找到-out
            if(i==strs.length-1) {
                return false;
            }
        }
        //每种参数-date -type -province最多出现一次
        for(int i=1;i<strs.length;++i) {
            //存在-date
            if(strs[i].equals("-date")) {
                for(int j=i+1;j<strs.length;++j) {
                    //-date出现多次
                    if(strs[j].equals("-date")) {
                        return false;
                    }
                }
                break;
            }
        }
        for(int i=1;i<strs.length;++i) {
            //存在-type
            if(strs[i].equals("-type")) {
                for(int j=i+1;j<strs.length;++j) {
                    //-type出现多次
                    if(strs[j].equals("-type")) {
                        return false;
                    }
                }
                break;
            }
        }
        for(int i=1;i<strs.length;++i) {
            //存在-province
            if(strs[i].equals("-province")) {
                for(int j=i+1;j<strs.length;++j) {
                    //-province出现多次
                    if(strs[j].equals("-province")) {
                        return false;
                    }
                }
                break;
            }
        }
        return true; 
    }

    void checkCommand(Statistic sta) {
        for(int i=0;i<4;++i) {
            if(!arguments[0].checkError()) {
                System.exit(1);
            }
        }
        ((ProvinceArgument)arguments[4]).checkError(sta);
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
        // TODO Auto-generated constructor stub
        super(name);
    }
    
    LogArgument(String[] strs) {
        super(strs);
        // TODO Auto-generated constructor stub
        if (strs.length > 2) {
            value = "*";
        }
        value = strs[1];
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
        // TODO Auto-generated constructor stub
        super(name);
    }
    
    OutArgument(String[] strs) {
        super(strs);
        // TODO Auto-generated constructor stub
        if (strs.length > 2) {
            value = "*";
        }
        value = strs[1];
    }

    boolean checkError() {
        // -out只有一个参数值
        if (value.startsWith("*")) {
            System.out.println("-out参数值错误，退出程序");
            return false;
        }
        // 若输入的生成文件路径没有后缀 或后缀不为".txt" //则修改为默认
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
        // TODO Auto-generated constructor stub
        super(name);
    }
    
    DateArgument(String[] strs) {
        super(strs);
        // TODO Auto-generated constructor stub
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
        // 日期格式检查 满足平年闰年的YYYY-MM-DD格式
        if (!value.matches(
                "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]"
                        + "|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468]"
                        + "[048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)   \r\n(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]"
                        + "{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))"
                        + "|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))"
                        + "-02-29) \r\n")) {
            value = "lastdate";
            System.out.println("日期格式有误，改为默认最新日期");
        }
        return true;
    }
}

class TypeArgument extends BaseArgument {

    String[] valueList;

    TypeArgument(String name) {
        // TODO Auto-generated constructor stub
        super(name);
    }
    
    TypeArgument(String[] strs) {
        super(strs);
        // TODO Auto-generated constructor stub
        value = "valueList";
        int n = strs.length - 1;
        // 参数值只能为ip,sp,cure,dead 若不是就忽略该错误输入
        for (int i = 1; i < strs.length; ++i) {
            if (!(strs[i].equals("ip") || strs[i].equals("sp") || strs[i].equals("cure") || strs[i].equals("dead"))) {
                strs[i] = "";
                --n;
                System.out.println("-type参数值只能为ip,sp,cure,dead，忽略该错误参数值");
            }
        }
        // 若全部输入错误 则视为参数缺省
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
        // 参数值不能重复 若重复则删去
        int n = valueList.length;
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (valueList[i].equals(valueList[j])) {
                    valueList[j] = valueList[n--];
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
        // TODO Auto-generated constructor stub
        super(name);
    }
    
    ProvinceArgument(String[] strs) {
        super(strs);
        // TODO Auto-generated constructor stub
        value = "valuelist";
        valueList = new String[strs.length - 1];
        for (int i = 1; i < strs.length; ++i) {
            valueList[i - 1] = strs[i];
        }
    }

    boolean checkError(){
        return true;
    }
    
    boolean checkError(Statistic sta) {
        //输入的省份存在，若不存在则列出所有
        for(int i=0;i<valueList.length;++i) {
            if(!sta.data.containsKey(valueList[i])) {
                value="all";
                valueList=new String[0];
                System.out.println("-province参数输入有误，默认列出所有省份数据");
            }
        }
        return true;
    }
}

class LogFiles {
    String lastDate;
    File[] files;

    LogFiles(String path) {
        File logFile = new File(path);
        files = logFile.listFiles();
    }

    void readFiles(String str) {

    }

    // 统计第i个文件的数据
    void statisFile(int i) {

    }

    // 处理日志文件中的一行 被statisFile()调用
    static void statisLine(String s) {

    }
}

class Statistic {
    Map<String, int[]> data;

    public Statistic() {
        // TODO Auto-generated constructor stub
        data = new HashMap<String, int[]>();
        data.put("全国", new int[] { 0, 0, 0, 0 });
        data.put("安徽", new int[] { 0, 0, 0, 0 });
        data.put("北京", new int[] { 0, 0, 0, 0 });
        data.put("重庆", new int[] { 0, 0, 0, 0 });
        data.put("福建", new int[] { 0, 0, 0, 0 });
        data.put("甘肃", new int[] { 0, 0, 0, 0 });
        data.put("广东", new int[] { 0, 0, 0, 0 });
        data.put("广西", new int[] { 0, 0, 0, 0 });
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

    void outPutFile(String str) {

    }
}