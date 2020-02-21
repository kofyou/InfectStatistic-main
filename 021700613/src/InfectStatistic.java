/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.*;
import java.awt.RenderingHints.Key;
import java.awt.print.Printable;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class InfectStatistic {
    public static void main(String[] args) {
        System.out.println("helloworld");
    }
}
/**
 * 
 * 感染地区及感染人数数据结构
 * 
 *
 * @author 021700613
 * @version 1.0
 * @since 2020.2.21
 */
class PeopleNum{
    public int infectedNum = 0;
    public int potentialNum = 0;
    public int curedNum = 0;
    public int deadNum = 0;  
}

class InfectedMap{
    //Key存储感染地区的名字，Value存储感染地区的人数信息
    HashMap<String, PeopleNum> map;
    
    public InfectedMap() {
        map = new HashMap<String, PeopleNum>();
        String wholeCountry = "全国";
        PeopleNum countryArea = new PeopleNum();
        map.put(wholeCountry, countryArea);
    }
    
    /*
     * 将map表中的数据根据key值进行排序
     */
    public void sortByProvince() {
        List<String> regulationOrder = Arrays.asList(
                "全国", "安徽", "北京",
                "重庆", "福建", "甘肃", "广东", "广西", "贵州",
                "海南", "河北", "河南", "黑龙江","湖北", "湖南",
                "吉林", "江苏", "江西", "辽宁", "内蒙古","宁夏",
                "青海", "山东", "山西", "陕西", "上海",  "四川", 
                "天津", "西藏", "新疆", "云南", "浙江" );
     
        Set<Entry<String,PeopleNum>> set = map.entrySet();
        List<Entry<String,PeopleNum>> list = new ArrayList<>();
        
        //把set加到list中去
        Iterator<Entry<String,PeopleNum>> it = set.iterator(); 
        while(it.hasNext()) {
            Entry<String,PeopleNum> entry = it.next();
            list.add(entry);
        }
        Collections.sort(list, new Comparator<Entry<String,PeopleNum>>() {
            @Override
            public int compare(Entry<String, PeopleNum> o1, Entry<String, PeopleNum> o2) {
                String value1 = o1.getKey();
                String value2 = o2.getKey();
                int index1 = regulationOrder.indexOf(value1);
                int index2 = regulationOrder.indexOf(value2);
                return index1-index2;
            }
        });
        //对list排序完成后放入LinkedHashMap即可。
        HashMap<String, PeopleNum> map2 = new LinkedHashMap<>();
        for (Entry<String, PeopleNum> entry : list) {
            map2.put(entry.getKey(), entry.getValue());
            map = map2;
        }
    }
}
/**
 * 
 * 命令行输入命令分析
 * 
 *
 * @author 021700613
 * @version 1.0
 * @since 2020.2.21
 */

class OrderHandle{
    //存放-log
    public String inPath = "";
    //存放-out
    public String outPath = "";
    //存放-date
    public String dateString = "";
    //存放-type
    public boolean hasType = false;
    public List<String> typeList = new ArrayList<String>();
    //存放-province
    public boolean hasProvince = false;
    public List<String> provinceList = new ArrayList<String>();
    
    public OrderHandle(String[] args) {
        for (int i = 1; i < args.length; i++){
            if ( args[i].equals("-log")) {
                i++;
                this.inPath = args[i];
            }else if (args[i].equals("-out")){
                i++;
                this.outPath = args[i];
            }else if (args[i].equals("-date")){
                i++;
                this.dateString = args[i];
            } else if (args[i].equals("-type")){
                i++;
                this.hasType = true;
                int index = i;
                for (int j = 0; j < args.length - index ; j++) {
                    this.typeList.add(args[i]);
                    if (args[i].equals("-province")) {
                        i --;
                        break;
                    }
                    i++;
                }
                
                if (typeList.size() == 4) {
                    hasType = false;
                }
            } else if (args[i].equals("-province")){
                //若含有province参数，则要对其后参数进行记录
                this.hasProvince = true;
                i++;
                int index = i;
                for (int j = 0; j < args.length - index; j++) {
                    this.provinceList.add(args[i]);
                    i++;
                }
            }                 
        }
    }   
}
/**
 * 
 * 命令执行类
 * 
 *
 * @author 021700613
 * @version 1.0
 * @since 2020.2.21
 */

class RunOrder{
    OrderHandle handle;
    boolean hasDate = false;
    boolean hasType = false;
    boolean hasProvince = false;
    
    public RunOrder(OrderHandle handle) {
        if (!handle.dateString.equals("")) {
            this.hasDate = true;
        }
        if (handle.hasType) {
            this.hasType = true;
        }
        if (handle.hasProvince) {
            this.hasProvince = true;
        }
        this.handle = handle;
    }
    public void Run() {
        InfectedMap map = new InfectedMap();
        ReadFile reader = new ReadFile();
        WriteFile writer = new WriteFile();
        
        try {
            reader.handleFile(handle.inPath, map, hasDate, handle.dateString);
            map.sortByProvince();
            writer.writeFile(handle.outPath, map, hasType, handle.typeList, hasProvince, handle.provinceList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
/**
 * 
 * 文件读取类
 * 
 *
 * @author 021700613
 * @version 1.0
 * @since 2020.2.22
 */

class ReadFile{
    /*
     * 对传入log文件夹目录下的所有文件进行处理
     */
    public void handleFile(String dirPath, InfectedMap map, boolean hasDate, String dateString) throws IOException{
        //获取文件目录
        File dirFile = new File(dirPath);
        //获取目录下所有*.log.txt文件
        File[] logFiles = dirFile.listFiles();
        //存放所有的文件名
        List<String> filesName = new ArrayList<String>();
        List<File> filesList = Arrays.asList(logFiles);

        //对filesList根据文件名按照时间进行排序
        //覆写compare方法
        Collections.sort(filesList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (int i = 0; i < filesList.size() ;i++) {
                filesName.add(filesList.get(i).toString());
            }
        String inPath = "";
        //改成绝对路径
        String absolutePath = "";
        if (hasDate) {
            absolutePath = dirPath + dateString + ".log.txt";

        }
        String lastFile = filesName.get(filesName.size() - 1);
        int len = lastFile.length();
        String lastDateString = lastFile.substring(len - 18, len -8 );
        if (lastDateString.compareTo(dateString) < 0) {
            System.out.println("！错误：输入date参数比最新log文件更新！");
        }
        for (int i = 0; i<filesName.size(); i++) {
            inPath = filesName.get(i);
            //若命令中有 -date参数
            if(hasDate) {
                if (inPath.compareTo(absolutePath) > 0) {
                    break;
                }
            }
            inputFile(inPath, map);
        }
    }
    /*
     * 解析每个*.log.txt文件的内容
     */
    public void inputFile(String inPath, InfectedMap map) throws IOException {
        InputStream inStream = new FileInputStream(inPath);
        String line; 
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        line = reader.readLine(); 
        Infohandle infohandle = new Infohandle();
        while (line != null) { 
            if (!line.matches("//(.*)") && !line.equals("")) {
                infohandle.parseInfo(line, map);
            }     
            line = reader.readLine(); 
        }
        reader.close();
        inStream.close();
    }
 
}
/**
 * 
 * 处理文件信息
 * 
 *
 * @author 021700613
 * @version 1.0
 * @since 2020.2.22
 */

class InfoHandle{
    public void parseInfo(String line, InfectedMap infectedMap){
        //将传入的信息按空格进行切割
        String cutString = " ";
        String[] newLine = line.split(cutString);
        
        //获取人数信息
        int len = newLine.length;
        String regEx="[^0-9]";  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(newLine[len-1]); 
        String numString= m.replaceAll("").trim();
        int num = Integer.valueOf(numString);
        
        //查找infectedMap中是否含有该地区
        boolean existed = infectedMap.map.containsKey(newLine[0]);
        
        //若无该地区，则先创建
        if (!existed){
            PeopleNum thisArea = new PeopleNum();
            infectedMap.map.put(newLine[0], thisArea);
        }
        
        PeopleNum countryTmp = infectedMap.map.get("全国");
        PeopleNum provinceTmp = infectedMap.map.get(newLine[0]); 
       
        //用正则表达式进行处理
        if (line.matches("(.*)新增 感染患者(.*)")){
            countryTmp.infectedNum += num;
            provinceTmp.infectedNum += num;           
        }else if (line.matches("(.*)新增 疑似患者(.*)")){
            countryTmp.potentialNum += num;
            provinceTmp.potentialNum += num;
        }else if (line.matches("(.*)死亡(.*)")){
            countryTmp.deadNum += num;
            provinceTmp.deadNum += num;
            countryTmp.infectedNum -= num;
            provinceTmp.infectedNum -= num;
        }else if (line.matches("(.*)治愈(.*)")){
            countryTmp.curedNum += num;
            provinceTmp.curedNum += num;
            countryTmp.infectedNum -= num;
            provinceTmp.infectedNum -= num;
        }else if (line.matches("(.*)疑似患者 确诊感染(.*)")){
            countryTmp.infectedNum += num;
            provinceTmp.infectedNum += num;
            countryTmp.potentialNum -= num;
            provinceTmp.potentialNum -= num;
        }else if (line.matches("(.*)排除 疑似患者(.*)")){
            countryTmp.potentialNum -= num;
            provinceTmp.potentialNum -= num;
        }else if (line.matches("(.*)流入(.*)")){
            boolean existed2 = infectedMap.map.containsKey(newLine[3]);
            if(!existed2){
                PeopleNum thisArea = new PeopleNum();
                infectedMap.map.put(newLine[3], thisArea);
            }
            PeopleNum province2Tmp = infectedMap.map.get(newLine[3]); 
            if (line.matches("(.*)疑似患者 流入(.*)")){
                provinceTmp.potentialNum -= num;
                province2Tmp.potentialNum += num;
            }else if (line.matches("(.*)感染患者 流入(.*)")) {
                provinceTmp.infectedNum -= num;
                province2Tmp.infectedNum += num;
            }
            infectedMap.map.replace(newLine[3], province2Tmp);
        }
        
        infectedMap.map.replace(newLine[0], countryTmp);
        infectedMap.map.replace(newLine[0], provinceTmp);
    }
    
  
}
/**
 * 
 * 文件输出的工具类
 * 
 *
 * @author 021700613
 * @version 1.0
 * @since 2020.2.22
 */

class WriteFile{
    public void writeFile(String outPath, InfectedMap map,
                boolean hasType, List<String> typeList, boolean hasProvince ,
                List<String> provinceList) throws IOException {    
        OutputStream outStream = new FileOutputStream(outPath);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outStream));
        
        if (hasProvince && !hasType) {
            String provinceName = "";
            for (int i = 0; i < provinceList.size(); i++) {
                provinceName = provinceList.get(i);
                if (map.map.get(provinceName) != null) {
                    PeopleNum province = map.map.get(provinceName);
                    writeAll(provinceName, bufferedWriter, province);
                }else {
                    bufferedWriter.write(provinceName);
                    bufferedWriter.write(" 暂无信息：感染患者0人");
                    bufferedWriter.write(" 疑似0人");
                    bufferedWriter.write(" 治愈0人");
                    bufferedWriter.write(" 死亡0人\n");
                }
            }
           
        }else if (!hasProvince && hasType) {
            for (HashMap.Entry<String, PeopleNum> entry : map.map.entrySet()) {
                String keyString = entry.getKey();
                bufferedWriter.write(keyString);
                if(typeList.contains("ip")) {
                    bufferedWriter.write(" 感染患者" + map.map.get(keyString).infectedNum + "人");
                }
                if(typeList.contains("sp")) {
                    bufferedWriter.write(" 疑似患者" + map.map.get(keyString).potentialNum + "人");
                }
                if(typeList.contains("cure")) {
                    bufferedWriter.write(" 治愈" + map.map.get(keyString).curedNum + "人");
                }
                if(typeList.contains("dead")) {
                    bufferedWriter.write(" 死亡" + map.map.get(keyString).deadNum + "人");
                }
                bufferedWriter.write("\n");  
               }
        }else if (hasProvince && hasType) {
            String provinceName = "";
            for (int i = 0; i < provinceList.size(); i++) {
                provinceName = provinceList.get(i);
                if (map.map.get(provinceName) != null) {
                    PeopleNum province = map.map.get(provinceName);
                    String keyString = provinceName;
                    bufferedWriter.write(keyString);
                    if(typeList.contains("ip")) {
                        bufferedWriter.write(" 感染患者" + province.infectedNum + "人");
                    }
                    if(typeList.contains("sp")) {
                        bufferedWriter.write(" 疑似患者" + province.potentialNum + "人");
                    }
                    if(typeList.contains("cure")) {
                        bufferedWriter.write(" 治愈" + province.curedNum + "人");
                    }
                    if(typeList.contains("dead")) {
                        bufferedWriter.write(" 死亡" + province.deadNum + "人");
                    }
                    bufferedWriter.write("\n");  
                }else {
                    System.out.println(provinceName + "暂无疫情信息");
                }
            }
        }else {
            for (HashMap.Entry<String, PeopleNum> entry : map.map.entrySet()) {
                String keyString = entry.getKey();
                writeAll(keyString, bufferedWriter, map.map.get(keyString));
               }
        }
        
        bufferedWriter.write("//该数据并非真实数据，仅供测试程序使用\n"); 
        bufferedWriter.close();
        outStream.close();
    }
    /*
     * 来向文件写信息
     */
    public void writeAll(String keyString, BufferedWriter bufferedWriter, PeopleNum PeopleNum) {
        try {
            bufferedWriter.write(keyString);
            if(PeopleNum.infectedNum > 0) {
                bufferedWriter.write(" 感染患者" + PeopleNum.infectedNum + "人");
            }
            if(PeopleNum.potentialNum > 0) {
                bufferedWriter.write(" 疑似患者" + PeopleNum.potentialNum + "人");
            }
            if(PeopleNum.curedNum > 0) {
                bufferedWriter.write(" 治愈" + PeopleNum.curedNum + "人");
            }
            if(PeopleNum.deadNum > 0) {
                bufferedWriter.write(" 死亡" + PeopleNum.deadNum + "人");
            }
            bufferedWriter.write("\n");  
        } catch (IOException e) {
            e.printStackTrace();
        }            
          
    }

}
