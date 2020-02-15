import java.util.ArrayList;
import java.io.*;
/**
 * InfectStatistic
 */
public class InfectStatistic {
    static ArrayList<Province> provincesList = new ArrayList<Province>();
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
                                cmds.type.add(args[j]);
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
                                cmds.province.add(args[j]);
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
                        break;
                }
            }
            
            if (!cmds.type.isEmpty()) {
                
            }
        }
        String str;     //存放读取的每一行
        //FileUtil util = new FileUtil();
        BufferedReader bReader;
        if (!cmds.log.equals("")) 
            bReader = new BufferedReader(new FileReader(cmds.log));       
        else 
            bReader = new BufferedReader(new FileReader("c:\\Users\\13067\\Desktop\\test\\test\\2020-01-22.log.txt"));
        
        //BufferedWriter bWriter = new BufferedWriter(new FileWriter("output.txt"));
        while ((str = bReader.readLine()) != null) {

            String[] splitInfo = str.split(" ");
            int index;      //存放省列表下标
            if (provincesList.isEmpty()) {
                provincesList.add(new Province("全国"));
            }
            if ((index = FileUtil.isProvinceExit(splitInfo[0], provincesList)) == -1) {
                provincesList.add(new Province(splitInfo[0]));
                index = provincesList.size() - 1;
            }
            for (int i = 1; i < splitInfo.length; i++) {
                if (splitInfo[i].equals("新增")) {
                    if (splitInfo[i+1].equals("感染患者")) {
                        FileUtil.addNum("infect", splitInfo[i+2], provincesList, index);
                        break;
                    }
                    else if (splitInfo[i+1].equals("疑似患者")) {
                        FileUtil.addNum("suspect", splitInfo[i+2], provincesList, index);
                        break;
                    }
                }
                else if (splitInfo[i].equals("治愈")) {
                    FileUtil.addNum("cure", splitInfo[i+1], provincesList, index);
                    FileUtil.subNum("infect", splitInfo[i+1], provincesList, index);
                    break;
                }
                else if (splitInfo[i].equals("死亡")){
                    FileUtil.addNum("dead", splitInfo[i+1], provincesList, index);
                    FileUtil.subNum("infect", splitInfo[i+1], provincesList, index);
                    break;
                }
                else if (splitInfo[i].equals("排除")) {
                    FileUtil.subNum("suspect", splitInfo[i+2], provincesList, index);
                }
                else if (splitInfo[i].equals("确诊感染")){
                    FileUtil.subNum("suspect", splitInfo[i+1], provincesList, index);
                    FileUtil.addNum("infect", splitInfo[i+1], provincesList, index);
                }
                else if (splitInfo[i].equals("流入")) {
                    if (splitInfo[i-1].equals("感染患者")) {
                        FileUtil.subNum("infect", splitInfo[i+2], provincesList, index);   
                        if ((index = FileUtil.isProvinceExit(splitInfo[i+1], provincesList)) == -1) {
                            provincesList.add(new Province(splitInfo[i+1]));
                            index = provincesList.size() - 1;
                        }
                        FileUtil.addNum("infect", splitInfo[i+2], provincesList, index);
                    }    
                    else if (splitInfo[i-1].equals("疑似患者")) {
                        FileUtil.subNum("suspect", splitInfo[i+2], provincesList, index);   
                        if ((index = FileUtil.isProvinceExit(splitInfo[i+1], provincesList)) == -1) {
                            provincesList.add(new Province(splitInfo[i+1]));
                            index = provincesList.size() - 1;
                        }
                        FileUtil.addNum("suspect", splitInfo[i+2], provincesList, index);
                    }
                }
            }
        }
        //int n = provincesList.size();
        FileUtil.outPut(provincesList, cmds);
        // for (Province p : provincesList) {
        //     String message = p.toString();
        //     bWriter.write(message);
        //     bWriter.newLine();
        // }
        bReader.close();
        // bWriter.close();
        
    }

    
}