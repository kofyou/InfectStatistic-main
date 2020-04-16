import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import java.text.Collator;
import java.util.Locale;

/**
 * InfectStatistic
 * TODO
 *
 * @author 枣子今天不吃枣
 * @version 1.0
 * @since 2020-02-10
 */
public class InfectStatistic {
    static ArrayList<Province> provincesList = new ArrayList<Province>();
    static String[] provincesRefered = {"全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", 
    "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", 
    "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", 
    "天津", "西藏", "新疆", "云南", "浙江"}; 
    public static void main(String[] args) throws Exception{
        Commands cmds = new Commands();
        if (args[0].equals("list")) {
            for (int i = 1; i < args.length; i++) {
        
                switch (args[i]) {
                    case "-log":
                        cmds.log = args[i+1];
                        ++i;
                        break;
                    case "-out":
                        cmds.out = args[i+1];
                        ++i;
                        break;
                    case "-date":
                        cmds.date = args[i+1];
                        ++i;
                        break;
                    case "-type":
                        for(int j = i + 1; j < args.length; j++) {
                            if (!args[j].substring(0, 1).equals("-")) {
                                if(args[j].equals("ip") || args[j].equals("sp") || args[j].equals("cure") || args[j].equals("dead"))
                                    cmds.type.add(args[j]);
                                else
                                    System.out.println(args[j] + " 不在查询范围内，将查询剩余的类型。");
                                i = j;
                            }
                            else {
                                i = j - 1;
                                break;
                            }
                        }
                        break;
                    case "-province":
                        for(int j = i + 1; j < args.length; j++) {
                            if (!args[j].substring(0, 1).equals("-")) {
                                if (Arrays.asList(provincesRefered).contains(args[j]))
                                    cmds.province.add(args[j]);
                                else
                                    System.out.println(args[j] + " 不在查询范围内，将查询剩余的省份。");
                                i = j;
                            }
                            else {
                                i = j - 1;
                                break;
                            }
                        }
                        // System.out.println(cmds.province.size());
                        break;
                    default:
                        System.out.println("\n存在错误的命令行参数！");
                        System.exit(0);
                }
            }
            if (cmds.log.equals("")) {
                System.out.println("未指定-log参数！");
                System.exit(0);
            }
            if (cmds.out.equals("")) {
                System.out.println("未指定-out参数！");
                System.exit(0);
            }
        }
        else {
            System.out.println("不存在 " + args[0] + " 指令！");
            System.exit(0);
        }

        FileUtil fUtil = new FileUtil();
        String str;     //存放读取的每一行
        //FileUtil util = new FileUtil();
        BufferedReader bReader;
        String dir = "";
        if (!cmds.log.equals("")) 
            dir = cmds.log;
            //bReader = new BufferedReader(new FileReader(cmds.log));       
            //bReader = new BufferedReader(new FileReader("c:\\Users\\13067\\Desktop\\test\\test\\2020-01-22.log.txt"));
        String[] files = fUtil.readLog(cmds);
        // for (String string : files) {
        //     System.out.println(string);
        // }
        
        for (String name : files) {
            //BufferedWriter bWriter = new BufferedWriter(new FileWriter("output.txt"));
            String path = dir + "\\" + name;
        
            bReader = new BufferedReader(new FileReader(path));
            while ((str = bReader.readLine()) != null && !str.contains("//")) {

                String[] splitInfo = str.split(" ");
                int index;      //存放省列表下标
                if (provincesList.isEmpty()) {
                    provincesList.add(new Province("全国"));
                }
                if ((index = fUtil.isProvinceExit(splitInfo[0], provincesList)) == -1) {
                    provincesList.add(new Province(splitInfo[0]));
                    index = provincesList.size() - 1;
                }
                for (int i = 1; i < splitInfo.length; i++) {
                    if (splitInfo[i].equals("新增")) {
                        if (splitInfo[i+1].equals("感染患者")) {
                            fUtil.addNum("infect", splitInfo[i+2], provincesList, index);
                            break;
                        }
                        else if (splitInfo[i+1].equals("疑似患者")) {
                            fUtil.addNum("suspect", splitInfo[i+2], provincesList, index);
                            break;
                        }
                    }
                    else if (splitInfo[i].equals("治愈")) {
                        fUtil.addNum("cure", splitInfo[i+1], provincesList, index);
                        fUtil.subNum("infect", splitInfo[i+1], provincesList, index);
                        break;
                    }
                    else if (splitInfo[i].equals("死亡")){
                        fUtil.addNum("dead", splitInfo[i+1], provincesList, index);
                        fUtil.subNum("infect", splitInfo[i+1], provincesList, index);
                        break;
                    }
                    else if (splitInfo[i].equals("排除")) {
                        fUtil.subNum("suspect", splitInfo[i+2], provincesList, index);
                    }
                    else if (splitInfo[i].equals("确诊感染")){
                        fUtil.subNum("suspect", splitInfo[i+1], provincesList, index);
                        fUtil.addNum("infect", splitInfo[i+1], provincesList, index);
                    }
                    else if (splitInfo[i].equals("流入")) {
                        if (splitInfo[i-1].equals("感染患者")) {
                            fUtil.subNum("infect", splitInfo[i+2], provincesList, index);   
                            if ((index = fUtil.isProvinceExit(splitInfo[i+1], provincesList)) == -1) {
                                provincesList.add(new Province(splitInfo[i+1]));
                                index = provincesList.size() - 1;
                            }
                            fUtil.addNum("infect", splitInfo[i+2], provincesList, index);
                        }    
                        else if (splitInfo[i-1].equals("疑似患者")) {
                            fUtil.subNum("suspect", splitInfo[i+2], provincesList, index);   
                            if ((index = fUtil.isProvinceExit(splitInfo[i+1], provincesList)) == -1) {
                                provincesList.add(new Province(splitInfo[i+1]));
                                index = provincesList.size() - 1;
                            }
                            fUtil.addNum("suspect", splitInfo[i+2], provincesList, index);
                        }
                    }
                }
            }
            bReader.close();
        }
        for (String p1 : cmds.province) {
            boolean flag = false;
            for (Province p2 : provincesList) {
                if (p1.equals(p2.getName())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                provincesList.add(new Province(p1));
            }
        }
        
        //int n = provincesList.size();
        fUtil.outPut(provincesList, cmds);
        // for (Province p : provincesList) {
        //     String message = p.toString();
        //     bWriter.write(message);
        //     bWriter.newLine();
        // }
        
        // bWriter.close();
        
    }

    /**
     * FileUtil
     */
    public static class FileUtil {
        
        public int isProvinceExit(String name, ArrayList<Province> list) {
            if (list.isEmpty()) {
                return -1;
            }
            else {
                for (Province province : list) {
                    if (province.getName().equals(name)) {                  
                        return list.indexOf(province);
                    }
                }
            }
            return -1;
        }

        
        public void outPut(ArrayList<Province> list,Commands cmds) throws Exception {
        
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(cmds.out));
            
            //将"全国"数据复制到national中，从列表中删除并将列表排序后，再插入到列表第一的位置
            Province national = list.get(0);
            list.remove(0);
            list.sort(Province::compareByName);
            list.add(0, national);
            //遍历列表 输出
            for (Province p : list) {
                String message = p.toString(cmds);
                if (!message.equals("")) {
                    bWriter.write(message);
                    bWriter.newLine();
                }
            }
            bWriter.write("// 该文档并非真实数据，仅供测试使用");
            
            bWriter.close();
        }

        //提取字符串中的数字
        public String saveDigit(String a) {
            String regEx="[^0-9]";  
            Pattern p = Pattern.compile(regEx);  
            Matcher m = p.matcher(a);  
            return m.replaceAll("");           
        }

        public void addNum(String type, String number, ArrayList<Province> list, int index) {
            int num = Integer.parseInt(saveDigit(number));
            if (type.equals("infect")) {
                list.get(index).addNumInfect(num);
                list.get(0).addNumInfect(num);
            }
            else if (type.equals("suspect")) {
                list.get(index).addNumSuspect(num);
                list.get(0).addNumSuspect(num);
            }
            else if (type.equals("cure")) {
                list.get(index).addNumCure(num);
                list.get(0).addNumCure(num);
            }
            else {
                list.get(index).addNumDead(num);
                list.get(0).addNumDead(num);
            }
        }

        public void subNum(String type, String number, ArrayList<Province> list, int index) {
            int num = Integer.parseInt(saveDigit(number));
            if (type.equals("infect")) {
                list.get(index).subNumInfect(num);
                list.get(0).subNumInfect(num);
            }
            else if (type.equals("suspect")) {
                list.get(index).subNumSuspect(num);
                list.get(0).subNumSuspect(num);
            }
            // else if (type.equals("cure")) {
            //     list.get(index).subNumCure(num);
            // }
            // else {
            //     list.get(index).subNumDead(num);
            // }
        }

        public String[] readLog(Commands cmds) throws Exception {
            
            File file = new File(cmds.log);
            //BufferedReader bReader = new BufferedReader(new FileReader("fileName"))
            FilenameFilter filter = new FilenameFilter(){
            
                @Override
                public boolean accept(File dir, String name) {
                    File currFile = new File(dir, name);

                    if (currFile.isFile() && name.endsWith(".log.txt")) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            };
            if (file.exists()) {
                //ArrayList <String> lists = new ArrayList<>();
                
                String[] filter_lists = file.list(filter);
                int index;
                if (!cmds.date.equals("")) {
                    if (!Pattern.matches("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]", cmds.date)) {
                        System.out.println("日期格式错误！");
                        System.exit(-1);
                    }
                    for (int i = 0; i < filter_lists.length; i++) {
                        
                            if (filter_lists[i].substring(0, 10).equals(cmds.date)) {
                                index = i;
                                String[] lists = new String[index + 1];
                                System.arraycopy(filter_lists, 0, lists, 0, index + 1);
                                return lists;
                            }
                            else if (filter_lists[i].substring(0, 10).compareTo(cmds.date) >= 1) {
                                index = i;
                                String[] lists = new String[index];
                                System.arraycopy(filter_lists, 0, lists, 0, index);
                                return lists;
                            }
                    }
                    System.out.println("未找到 " + cmds.date + " 的日志文件！");
                    System.exit(-1);
                }
                return filter_lists;
                
            }
            else {
                System.out.println("-log文件路径不存在！");
                System.exit(0);
            }
            return null;
        }


    }

    /**
     * province
     */
    public static class Province {

        private String name;
        private int num_infect;
        private int num_cure;
        private int num_dead;
        private int num_suspect;

        public Province(String name) {
            this.name = name;
            this.num_cure = 0;
            this.num_dead = 0;
            this.num_infect = 0;
            this.num_suspect = 0;
        }
        public Province(String name, int num_cure, int num_dead, int num_infect, int num_suspect) {
            this.name = name;
            this.num_cure = num_cure;
            this.num_dead = num_dead;
            this.num_infect = num_infect;
            this.num_suspect = num_suspect;
        }

        public String toString(Commands cmds) {
            FileUtil fUtil = new FileUtil();
            String message = "";
            if (cmds.province.isEmpty()) {
                message = this.name;
                if (cmds.type.isEmpty()) {
                    message += " 感染患者 " + this.num_infect + "人 疑似患者 " + this.num_suspect
                    + "人 治愈 " + this.num_cure + "人 死亡 " + this.num_dead + "人";
                }
                else {
                    for (String s : cmds.type) {
                        if (s.equals("ip")) {
                            message += " 感染患者 " + this.num_infect + "人 ";
                        }
                        else if (s.equals("sp")) {
                            message += " 疑似患者 " + this.num_suspect + "人 ";
                        }
                        else if (s.equals("cure")) {
                            message += " 治愈 " + this.num_cure + "人 ";
                        }
                        else {
                            message += " 死亡 " + this.num_dead + "人 ";
                        }
                    }
                    String a = fUtil.saveDigit(message);
                    int b = Integer.valueOf(a);
                    if (b == 0) {
                        message = "";
                    }
                }            
            }
            else {
                if (cmds.type.isEmpty()) {
                    for (String p : cmds.province) {
                        if (this.name.equals(p)) {
                            //message = this.name;
                            message = this.name + " 感染患者 " + this.num_infect + "人 疑似患者 " + this.num_suspect
                                + "人 治愈 " + this.num_cure + "人 死亡 " + this.num_dead + "人"; 
                            break;
                        }
                    }
                }
                else {
                    for (String p : cmds.province) {
                        if (this.name.equals(p)) {
                            message = this.name;
                            for (String s : cmds.type) {
                                if (s.equals("ip")) {
                                    message += " 感染患者 " + this.num_infect + "人 ";
                                }
                                else if (s.equals("sp")) {
                                    message += " 疑似患者 " + this.num_suspect + "人 ";
                                }
                                else if (s.equals("cure")) {
                                    message += " 治愈 " + this.num_cure + "人 ";
                                }
                                else {
                                    message += " 死亡 " + this.num_dead + "人 ";
                                }
                            }
                            break;
                        }  
                    }           
                }
            }       
            return message;
        }

        //按拼音排序
        public int compareByName(Province p) {
            Collator c = Collator.getInstance(Locale.CHINA);
            int numForName = c.compare(this.getName(), p.getName());
            return numForName;
        }

        public void setName (String name) {
            this.name = name;
        }
        public void addNumInfect (int num) {
            this.num_infect += num;
        }
        public void addNumSuspect (int num) {
            this.num_suspect += num;
        }
        public void addNumCure (int num) {
            this.num_cure += num;
        }
        public void addNumDead (int num) {
            this.num_dead += num;
        }
        public void subNumInfect (int num) {
            this.num_infect -= num;
        }
        public void subNumSuspect (int num) {
            this.num_suspect -= num;
        }
        public void subNumCure (int num) {
            this.num_cure -= num;
        }
        public void subNumDead (int num) {
            this.num_dead -= num;
        }
        public void setNumInfect (int num) {
            this.num_infect = num;
        }
        public void setNumSuspect (int num) {
            this.num_suspect = num;
        }
        public void setNumCure (int num) {
            this.num_cure = num;
        }
        public void setNumDead (int num) {
            this.num_dead = num;
        }

        public String getName() {
            return this.name;
        }
        public int getNumInfect() {
            return this.num_infect;
        }
        public int getNumSuspect() {
            return this.num_suspect;
        }
        public int getNumCure() {
            return this.num_cure;
        }
        public int getNumDead() {
            return this.num_dead;
        }

        
        
        
    }
    /**
     * Commands
     */
    public static class Commands {

        String log = "";
        String out = "";
        String date = "";
        ArrayList<String> type = new ArrayList<>();
        ArrayList<String> province = new ArrayList<>();
        
        
    }
    
}

