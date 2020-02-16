import com.sun.org.apache.bcel.internal.generic.DSTORE;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    private String []provinceList = {
        "安徽", "北京", "重庆","福建", "甘肃", "广东", "广西", "贵州", "海南", "河北", "河南",
            "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海",
            "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"
    };
    private HashMap<String, ProvanceInfo> provinceMap = new HashMap<>();
    private int infectTotalNum,suspectedTotalNum,cureTotalNum,diedTotalNum;
    public InfectStatistic(){
        for(String p : provinceList){
            provinceMap.put(p, new ProvanceInfo(p));
        }
        infectTotalNum = suspectedTotalNum = cureTotalNum = diedTotalNum = 0;
    }
    public static void main(String[] args) {
        List list = Arrays.asList(args);
        int dateIndex = list.indexOf("-date");//截止日期
        String dateString = args[dateIndex + 1];
        int dirIndex = list.indexOf("-log");//日志文件目录
        String dirPath = args[dirIndex + 1];
        int outputIndex = list.indexOf("-out");//输出文件目录
        String outputPath = args[outputIndex + 1];
        int provinceListIndex = list.indexOf("-province");//查询省份列表
        int provinceListLastIndex = findLastIndex(args,provinceListIndex);
        ArrayList<String> provinceList = new ArrayList<String >();
        for(int i = provinceListIndex; i <= provinceListLastIndex; i++){
            provinceList.add(args[i]);
        }
        int typeIndex = list.indexOf("-type");
        int typeLastIndex = findLastIndex(args,typeIndex);
        HashMap<String, Integer> typeMap = new HashMap<>();//查询的类别
        int t = typeIndex > 0 ? 0 : 1;
        typeMap.put("ip",t);
        typeMap.put("sp",t);
        typeMap.put("cure",t);
        typeMap.put("dead",t);
        if(t == 0){
            for(int i = typeIndex; i <= typeLastIndex; i++){
                typeMap.put(args[i], 1);
            }
        }

        File logDir = new File(dirPath);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        try{
            date = simpleDateFormat.parse(dateString);
        }catch (ParseException e){
            System.out.println("时间参数非法");
        }
        InfectStatistic infectStatistic = new InfectStatistic();
        infectStatistic.statistic(logDir,date);
        infectStatistic.output(outputPath, provinceList,typeMap);
    }

    public static int findLastIndex(String []list, int begin){
        int len = list.length;
        int index = begin + 1;
        while(index < len && list[index].charAt(0) != '-'){
            index++;
        }
        return index - 1;
    }


    public void statistic(File dir, Date date){//统计
        BufferedReader reader = null;
        String line;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        for(File file : dir.listFiles()){
            String fileDateString = file.getName().split("\\.")[0];//日志日期
            try{
                Date fileDate = simpleDateFormat.parse(fileDateString);//日志日期
                if(fileDate.compareTo(date) > 0){
                    break;
                }
                reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine())!= null){
                    if(line.charAt(0) != '/'){//该行不为注释
                        updateProvinceInfo(line);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void updateProvinceInfo(String line){
        String message[] = line.split(" ");
        ProvanceInfo province = provinceMap.get(message[0]);
        String lastMessage = message[message.length-1];
        int num = Integer.parseInt(lastMessage.substring(0,lastMessage.length()-1));//更新人数
        System.out.println(line);
        switch (message.length){
            case 3://死亡、治愈
                if(message[1].equals("死亡")){
                    province.diedNumAdd(num);
                    province.infectNumSub(num);
                    diedTotalNum += num;
                    infectTotalNum -= num;
                    System.out.print("  diedTotalNum" + num);
                }else{//治愈
                    province.cureNumAdd(num);
                    province.infectNumSub(num);
                    cureTotalNum += num;
                    infectTotalNum -= num;
                    System.out.print("  cureTotalNum" + num);
                }
                break;
            case 4://新增、确诊、排除
                if(message[1].equals("新增")){
                    if(message[2].equals("感染患者")){
                        province.infectNumAdd(num);
                        infectTotalNum += num;
                        System.out.print("  infectTotalNum" + num);
                    }else{//疑似患者
                        province.suspectedAdd(num);
                        suspectedTotalNum += num;
                        System.out.print("  suspectedTotalNum" + num);
                    }
                }else if(message[1].equals("排除")){//排除疑似患者
                    province.suspectedSub(num);
                    suspectedTotalNum -= num;
                    System.out.print("  suspectedTotalNum-" + num);
                }else{//确诊感染
                    province.suspectedSub(num);
                    province.infectNumAdd(num);
                    suspectedTotalNum -= num;
                    infectTotalNum += num;
                    System.out.print("  suspectedTotalNum-" + num + "  infectTotalNum" + num);
                }
                break;
            case 5://从A省流入B省
                ProvanceInfo provinceB = provinceMap.get(message[3]);
                if(message[1].equals("感染患者")){
                    province.infectNumSub(num);
                    provinceB.infectNumAdd(num);
                }else{//疑似患者
                    province.suspectedSub(num);
                    provinceB.suspectedAdd(num);
                }
                break;
        }
        System.out.println();
    }

    public void output(String outputPath, ArrayList<String>provinceList, HashMap<String, Integer> typeMap){
        try {
            Formatter writer = new Formatter(outputPath);
            int infectNum, suspectedNum, cureNum, diedNum;
            int infectDoesShow, suspectedDoesShow, cureDoesShow, diedDoesShow;
            infectNum = suspectedNum = cureNum = diedNum = 0;
            infectDoesShow = typeMap.get("ip");
            suspectedDoesShow = typeMap.get("sp");
            cureDoesShow = typeMap.get("cure");
            diedDoesShow = typeMap.get("dead");

            if(provinceList.contains("全国")){
                writer.format("全国  ");
                if(infectDoesShow > 0){
                    writer.format("感染患者:%d人  ", infectTotalNum);
                }
                if(suspectedDoesShow > 0){
                    writer.format("疑似患者%d人  ", suspectedTotalNum);
                }
                if(cureDoesShow > 0){
                    writer.format("治愈%d人  ", cureTotalNum);
                }
                if(diedDoesShow > 0){
                    writer.format("死亡%d人  ", diedTotalNum);
                }
                writer.format("\n");
            }

            for(ProvanceInfo p : provinceMap.values()){
                if(provinceList.contains(p.getName())){
                    writer.format(p.getName() + "  ");
                    if(infectDoesShow > 0){
                        writer.format("感染患者:%d人  ", p.getInfectNum());
                    }
                    if(suspectedDoesShow > 0){
                        writer.format("疑似患者%d人  ", p.getSuspectedNum());
                    }
                    if(cureDoesShow > 0){
                        writer.format("治愈%d人  ", p.getCureNum());
                    }
                    if(diedDoesShow > 0){
                        writer.format("死亡%d人  ", p.getDiedNum());
                    }
                    writer.format("\n");
                }
                infectNum += p.getInfectNum();
                suspectedNum += p.getSuspectedNum();
                cureNum += p.getCureNum();
                diedNum += p.getDiedNum();
            }
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class ProvanceInfo{
    private String name;//省名
    private int infectNum;//感染数
    private int suspectedNum;//疑似数
    private int diedNum;//死亡数
    private int cureNum;//治愈数

    public ProvanceInfo(String name){
        this.name = name;
    }

    public int getInfectNum() {
        return infectNum;
    }

    public void setInfectNum(int infectNum) {
        this.infectNum = infectNum;
    }

    public int infectNumAdd(int num){
        return infectNum += num;
    }

    public int infectNumSub(int num){
        return infectNum -= num;
    }

    public int getSuspectedNum() {
        return suspectedNum;
    }

    public void setSuspectedNum(int suspectedNum) {
        this.suspectedNum = suspectedNum;
    }

    public int suspectedAdd(int num){
        return suspectedNum += num;
    }

    public int suspectedSub(int num){
        return suspectedNum -= num;
    }

    public int getDiedNum() {
        return diedNum;
    }

    public void setDiedNum(int diedNum) {
        this.diedNum = diedNum;
    }

    public int diedNumAdd(int num){
        return diedNum += num;
    }

    public int diedNumSub(int num){
        return diedNum -= num;
    }

    public int getCureNum() {
        return cureNum;
    }

    public void setCureNum(int cureNum) {
        this.cureNum = cureNum;
    }

    public int cureNumAdd(int num){
        return cureNum += num;
    }

    public int cureNumSub(int num){
        return cureNum -= num;
    }

    public String getName() {
        return name;
    }
}