import java.io.*;
import java.util.ArrayList;
//import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FileUtil
 */
public class FileUtil {
    
    public static int isProvinceExit(String name, ArrayList<Province> list) {
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

    // public static void outPut(ArrayList<Province> list) throws Exception {
    //     BufferedWriter bWriter = new BufferedWriter(new FileWriter("output.txt"));
    //     for (Province p : list) {
    //         String message = p.toString();
    //         bWriter.write(message);
    //         bWriter.newLine();
    //     }
        
    //     bWriter.close();
    // }

    
    public static void outPut(ArrayList<Province> list,Commands cmds) throws Exception {
        BufferedWriter bWriter;
        if (!cmds.out.equals("")) 
            bWriter = new BufferedWriter(new FileWriter(cmds.out));
        else 
            bWriter = new BufferedWriter(new FileWriter("output.txt"));
      
            
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
        
        bWriter.close();
    }

    //提取字符串中的数字
    public static String saveDigit(String a) {
        String regEx="[^0-9]";  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(a);  
        return m.replaceAll("");           
    }

    public static void addNum(String type, String number, ArrayList<Province> list, int index) {
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

    public static void subNum(String type, String number, ArrayList<Province> list, int index) {
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

    public static String[] readLog(Commands cmds) throws Exception {
        
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
                            //filter_lists = (String[])lists.toArray(new String[lists.size()]);
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