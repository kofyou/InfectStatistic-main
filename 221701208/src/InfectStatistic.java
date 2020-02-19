import java.io.*;
import java.util.*;

class Record{
    int infectionNumber;//infection patients：感染患者
    int suspectedNumber;//suspected patients：疑似患者
    int cureNumber;//cure：治愈
    int deadNumber;//dead：死亡患者
    Record(){
        infectionNumber = 0;
        suspectedNumber = 0;
        cureNumber = 0;
        deadNumber = 0;
    }

    /**
     * @param number 累加数据
     */
    void countInfection(int number){
        infectionNumber += number;
    }


    /**
     * @param number 累加数据
     */
    void countSuspected(int number){
        suspectedNumber += number;
    }


    /**
     * @param number 累加数据
     */
    void countCure(int number){
        cureNumber += number;
    }


    /**
     * @param number 累加数据
     */
    void countDead(int number){
        deadNumber += number;
    }

    /**
     * @return 返回infectionNumber
     */
    int getInfectionNumber(){
        return  infectionNumber;
    }


    /**
     * @return 返回suspectedNumber
     */
    int getSuspectedNumber(){
        return suspectedNumber;
    }

    /**
     * @return 返回cureNumber
     */
    int getCureNumber() {
        return  cureNumber;
    }


    /**
     * @return  返回deadNumber
     */
    int getDeadNumber(){
        return deadNumber;
    }

    /**
     * @param name 类型
     * @return 返回类型所对应的数据
     */
    int getNumber(String name){
        if(name.equals("ip"))
            return infectionNumber;
        else if(name.equals("sp"))
            return suspectedNumber;
        else if(name.equals("cure"))
            return cureNumber;
        else
            return deadNumber;
    }

}


/**
 * 用于实现map键值比较的功能
 */
class MapKeyComparator implements Comparator<String> {
    static Map<String,Integer> provinceMap;

    public MapKeyComparator(){
        provinceMap = new TreeMap<String,Integer>();
        provinceMap.put("全国",0);
        provinceMap.put("安徽",1);
        provinceMap.put("北京",2);
        provinceMap.put("重庆",3);
        provinceMap.put("福建",4);
        provinceMap.put("甘肃",5);
        provinceMap.put("广东",6);
        provinceMap.put("广西",7);
        provinceMap.put("贵州",8);
        provinceMap.put("海南",9);
        provinceMap.put("河北",10);
        provinceMap.put("河南",11);
        provinceMap.put("黑龙江",12);
        provinceMap.put("湖北",13);
        provinceMap.put("湖南",14);
        provinceMap.put("吉林",15);
        provinceMap.put("江苏",16);
        provinceMap.put("江西",17);
        provinceMap.put("辽宁",18);
        provinceMap.put("内蒙古",19);
        provinceMap.put("宁夏",20);
        provinceMap.put("青海",21);
        provinceMap.put("山东",22);
        provinceMap.put("山西",23);
        provinceMap.put("陕西",24);
        provinceMap.put("上海",25);
        provinceMap.put("四川",26);
        provinceMap.put("天津",27);
        provinceMap.put("西藏",28);
        provinceMap.put("新疆",28);
        provinceMap.put("云南",30);
        provinceMap.put("浙江",31);
    }
    @Override
    public int compare(String str1, String str2) {
        return provinceMap.get(str1).compareTo(provinceMap.get(str2));
    }
}


/**
 * InfectStatistic
 * TODO
 *
 * @author baixuangezhu
 * @version xxx
 * @since xxx
 */
@SuppressWarnings("unchecked")
class InfectStatistic {
    String logLocate; // 必带
    String outLocate; //必带
    String strDate; //如果没带，则默认当前时间
    Vector type; // 可选择[ip：  感染患者，sp：  疑似患者，cure：治愈 ，dead：死亡患者] 不指定该项默认会列出所有情况。
    Vector province; //指定列出的省，如-province福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
    Map<String,Record> result;//统计结果
    public InfectStatistic(){
        type = new Vector();
        province = new Vector();
        result = new TreeMap<String,Record>(new MapKeyComparator());
    }


    /**
     * @param locate log路径
     */
    void setLogLocate(String locate){
        logLocate = locate;
    }


    /**
     * @param locate 输出文件路径
     */
    void setOutLocate(String locate){
        outLocate = locate;
    }


    /**
     * @param date 日期（字符串）
     */
    void setDateString(String date){
        strDate = date;
    }


    /**
     * @param startIndex Type开始下标
     * @param endIndex Type结束下标
     */
    void setType(String[] arg,int startIndex , int endIndex){
        while(startIndex<endIndex){
            type.add(arg[startIndex]);
            startIndex++;
        }

    }


    /**
     * @param startIndex province开始下标
     * @param endIndex province 结束下标
     */
    void setProvince(String[] arg,int startIndex , int endIndex){
        while(startIndex<endIndex){
            province.add(arg[startIndex]);
            startIndex++;
        }

    }


    /**
     * @param path 文件夹路径
     * @return
     */
    String[] getFileList(String path){
        File file = new File(path);
        String[] list = file.list(); //获取文件夹内的所有文件名
        if (list != null) {
            for(int i=0;i<list.length;i++){//拼凑成完整路径
                list[i] = path + list[i];
            }
            return list;
        }

        else {
            System.out.println("文件路径错误，找不到文件夹");
            return null;
        }

    }


    /**
     * @param info 一行数据
     * @param result 结果集
     */
    void countDeadAndCure(String[] info,Map<String,Record> result){
        int number = Lib.getNumber(info[info.length-1].substring(0,info[info.length-1].length()-1));//获得人数

        String province = info[0];
        if(!result.containsKey(province)){ //result 中还未出现过这个省的记录
            Record newRecord = new Record();
            if (info[1].equals("死亡")) {
                newRecord.countDead(number);
                newRecord.countInfection(-number);
            }
            else if(info[1].equals("治愈")){
                newRecord.countCure(number);
                newRecord.countInfection(-number);
            }
            result.put(province,newRecord);
        }

        else { //result 中出现过这个省的记录
            Record aRecord = result.get(province);//获得这个省的数据记录
            if (info[1].equals("死亡")) {
                aRecord.countDead(number);//将数据加上去
                aRecord.countInfection(-number);
            }
            else if(info[1].equals("治愈")){
                aRecord.countCure(number);
                aRecord.countInfection(-number);
            }
            result.put(province,aRecord);
        }


    }


    /**
     * @param info 一行数据
     * @param result 结果集
     */
    void countIpAndSp(String[] info,Map<String,Record> result){
        String tmp = info[info.length-1];
        String strNumber=tmp.substring(0,tmp.length()-1);
        int number = Lib.getNumber(strNumber);
        String province = info[0];
        String ip="感染患者",sp="疑似患者";
        if(info[1].equals("新增") && info[2].equals(ip)){//新增感染
            infectProvince(province,number,result);
        }

        else if(info[1].equals("新增") && info[2].equals(sp)){//新增疑似
            suspectedProvince(province,number,result);
        }

        else if(info[1].equals("排除")){//排除疑似
            if(!result.containsKey(province)){//result 中还未出现过这个省的记录
                System.out.print("此省份无记录，此条数据有误： ");
                for(String str : info){
                    System.out.print(str);
                }
                System.out.print("\n");
            }
            else{
                Record aRecord = result.get(province);//获得这个省的数据记录
                aRecord.countSuspected(-number);
                result.put(province,aRecord);
            }

        }

        else if(info[2].equals("确诊感染")){
            Record aRecord = result.get(province);//获得这个省的数据记录
            aRecord.countSuspected(-number);//疑似减去
            aRecord.countInfection(number);//感染加上
            result.put(province,aRecord);
        }

        else {
            System.out.println("此条记录包含无法处理部分");
        }
    }


    /**
     * @param province 省份
     * @param number 数量
     * @param result 结果集
     */
    void infectProvince(String province,int number,Map<String,Record> result){
        if(!result.containsKey(province)){//未出现过的省份
            Record newRecord = new Record();//加上数据
            newRecord.countInfection(number);
            result.put(province,newRecord);
        }
        else {
            Record tmpRecord = result.get(province);//加上数据
            tmpRecord.countInfection(number);
            result.put(province,tmpRecord);
        }

    }


    /**
     * @param province 省份
     * @param number 数量
     * @param result 结果集
     */
    void suspectedProvince(String province,int number,Map<String,Record> result){
        if(!result.containsKey(province)){//未出现过的省份
            Record newRecord = new Record();//加上数据
            newRecord.countInfection(number);
            result.put(province,newRecord);
        }
        else {
            Record tmpRecord = result.get(province);//加上数据
            tmpRecord.countSuspected(number);
            result.put(province,tmpRecord);
        }
    }




    /**
     * @param info 一行数据
     * @param result 结果集
     */
    void countMovedIpAndSp(String[] info,Map<String,Record> result){
        int number = Lib.getNumber(info[info.length-1].substring(0,info[info.length-1].length()-1));//获得人数
        String sourceProvince = info[0];
        String aimProvince = info[3];
        String ip = "感染患者",sp = "疑似患者";
        if(info[1].equals(ip)){//感染患者流入
            infectProvince(aimProvince,number,result);
            Record aRecord = result.get(sourceProvince);//减去数据
            aRecord.countInfection(-number);
            result.put(sourceProvince,aRecord);
        }
        else if(info[1].equals(sp)){//疑似患者流入
            suspectedProvince(aimProvince,number,result);
            Record aRecord = result.get(sourceProvince);//减去数据
            aRecord.countSuspected(-number);
            result.put(sourceProvince,aRecord);
        }
        else {
            System.out.print("此纪录无法处理： ");
            for(String str:info)
                System.out.print(str);
            System.out.print("\n");
        }
    }


    /**
     * @param path 日志文件的路径
     * @param date 统计日期
     * @param fileList 日志文件夹下的文件列表
     * @param result 结果集
     */
    void statisticData(String path,String date,String[] fileList,Map<String,Record> result){
        date = path + date + ".log.txt";
        for(String fileName : fileList){
            if(date.compareTo(fileName) >= 0){//只统计date之前的数据
                try {
                    FileReader fr = new FileReader(fileName);
                    BufferedReader bf = new BufferedReader(fr);
                    String line;
                    while ((line = bf.readLine()) != null) { // 按行读取字符串并统计信息
                        if(line.startsWith("//"))//忽略文件里的注释
                            continue;
                        String info[] = line.split(" "); //以空格将一行分割
                        switch (info.length){
                            case 3:{
                                countDeadAndCure(info,result);//统计治愈和死亡
                            };break;
                            case 4:{
                                countIpAndSp(info,result);//统计新增加的感染患者和疑似患者，确诊的，排除的患者
                            };break;
                            case 5:{
                                countMovedIpAndSp(info,result);//统计流动的感染患者和疑似患者
                            };break;
                            default:System.out.println(line+"： 存在格式错误，此条记录无法处理");
                        }
                    }
                    bf.close();
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @param result 全部数据的集合
     * @return 总的数据——全国数据
     */
    Record statisticTotalNumber(Map<String,Record> result){
        Record totalCountryNumber = new Record();
        for (Record value:result.values()){//统计全国数据相加
            totalCountryNumber.countInfection(value.getInfectionNumber());
            totalCountryNumber.countSuspected(value.getSuspectedNumber());
            totalCountryNumber.countCure(value.getCureNumber());
            totalCountryNumber.countDead(value.getDeadNumber());
        }
        return  totalCountryNumber;
    }


    /**
     * @param totalCountryNumber 全国数据
     * @param outFile 输出流
     */
    void allProvinceAllType(Record totalCountryNumber , FileOutputStream outFile){
        String record = "全国 " + Lib.toChinese(Lib.allType[0])+ totalCountryNumber.getInfectionNumber() + Lib.unit +
                Lib.toChinese(Lib.allType[1]) + totalCountryNumber.getSuspectedNumber() + Lib.unit +
                Lib.toChinese(Lib.allType[2]) + totalCountryNumber.getCureNumber() + Lib.unit +
                Lib.toChinese(Lib.allType[3]) + totalCountryNumber.getDeadNumber() + Lib.unit + "\n";
        try {
            outFile.write(record.getBytes());
            for (Map.Entry<String, Record> m : result.entrySet()) {//写入地方数据
                record = m.getKey()+" " +
                        Lib.toChinese(Lib.allType[0]) + m.getValue().getInfectionNumber() + Lib.unit +
                        Lib.toChinese(Lib.allType[1]) + m.getValue().getSuspectedNumber() + Lib.unit +
                        Lib.toChinese(Lib.allType[2]) + m.getValue().getCureNumber() + Lib.unit +
                        Lib.toChinese(Lib.allType[3]) + m.getValue().getDeadNumber() + Lib.unit + "\n";
                outFile.write(record.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param totalCountryNumber 全国数据
     * @param outFile 输出流
     * @param type type数组
     */
    void allProvincePartType(Record totalCountryNumber , FileOutputStream outFile,Vector<String> type){
        String record = "全国 ";
        for(String i : Lib.allType){
            for(String j : type){
                if(i.equals(j)){
                    record += Lib.toChinese(i) + totalCountryNumber.getNumber(j) + Lib.unit;
                }
            }
        }
        record += "\n";
        try {
            outFile.write(record.getBytes());//写入全国数据
            for (Map.Entry<String, Record> m : result.entrySet()) {//写入地方数据
                record = m.getKey()+" ";
                for(String j : type){
                    for(String i : Lib.allType){
                        if(i.equals(j)){
                            record += Lib.toChinese(i) + m.getValue().getNumber(j) + Lib.unit;
                        }
                    }
                }
                record += "\n";
                outFile.write(record.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param totalCountryNumber 全国数据
     * @param province 省份数组
     * @param outFile 输出流
     */
    void partProvinceAllType(Record totalCountryNumber , Vector<String> province , FileOutputStream outFile){
        result.put("全国",totalCountryNumber);
        String record;

        for(String item : province){
            if(!result.containsKey(item)){//查询无记录的省份则创建一个数据为空的记录
                Record newRecord = new Record();
                result.put(item,newRecord);
            }
        }

        if(province.contains("全国")){//如果province里面包含全国则要写在文件的第一行
            record = "全国" + " ";
            record +=  Lib.toChinese(Lib.allType[0])+ result.get("全国").getInfectionNumber() +  Lib.unit +
                    Lib.toChinese(Lib.allType[1]) + result.get("全国").getSuspectedNumber() + Lib.unit +
                    Lib.toChinese(Lib.allType[2]) + result.get("全国").getCureNumber() + Lib.unit +
                    Lib.toChinese(Lib.allType[3]) + result.get("全国").getDeadNumber() + Lib.unit + "\n";
            try {
                outFile.write(record.getBytes());//将全国数据写入
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        for(String key : result.keySet()){
            for(int k=0;k<province.size();k++){
                if(province.get(k).equals(key) && !key.equals("全国")){//若不是全国，则写入
                    record = key +" ";
                    record +=  Lib.toChinese(Lib.allType[0])+ result.get(key).getInfectionNumber() +  Lib.unit +
                            Lib.toChinese(Lib.allType[1]) + result.get(key).getSuspectedNumber() + Lib.unit +
                            Lib.toChinese(Lib.allType[2]) + result.get(key).getCureNumber() + Lib.unit +
                            Lib.toChinese(Lib.allType[3]) + result.get(key).getDeadNumber() + Lib.unit + "\n";
                    try {
                        outFile.write(record.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    /**
     * @param totalCountryNumber 全国数据
     * @param province 省份数组
     * @param outFile 输出流
     * @param type type 数组
     */
    void partProvincePartType(Record totalCountryNumber , Vector<String> province , FileOutputStream outFile,Vector<String> type){
        result.put("全国",totalCountryNumber);
        String record;
        for(String item : province){
            if(!result.containsKey(item)){//查询无记录的省份则创建一个数据为空的记录
                Record newRecord = new Record();
                result.put(item,newRecord);
            }
        }
        if(province.contains("全国")){
            record = "全国" + " ";
            for(String j : type){
                for(String i : Lib.allType){
                    if(i.equals(j)){
                        record +=  Lib.toChinese(j) + result.get("全国").getNumber(j) + Lib.unit;
                    }
                }
            }
            record += "\n";
            try {
                outFile.write(record.getBytes());//将全国数据写入
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        for(String key : result.keySet()){
            for(int k=0;k<province.size();k++){
                if(province.get(k).equals(key) && !key.equals("全国")){//若不是全国，则写入
                    record = key +" ";
                    for(String j : type){
                        for(String i : Lib.allType){
                            if(i.equals(j)){
                                record +=  Lib.toChinese(j) + result.get(key).getNumber(j) + Lib.unit;
                            }
                        }
                    }
                    record += "\n";
                    try {
                        outFile.write(record.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    /**
     * @param result 结果集
     * @param outLocate 输出文件路径
     * @param command 此次命令行给出的命令
     * @param province 省份数组
     * @param type type数组
     */
    void saveResult(Map<String,Record> result,String outLocate,String[] command,Vector<String> province,Vector<String> type){
        Record totalCountryNumber = statisticTotalNumber(result);
        String record;
        FileOutputStream outFile = null;
        try {//创建文件夹和文件
            Lib.creatFile(outLocate);
            outFile = new FileOutputStream(outLocate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(province.size() == 0){// 全部省份
            if(type.size() == 0){//全部省份全部类型
                allProvinceAllType(totalCountryNumber,outFile);
            }
            else{// 全部省份部分类型
                allProvincePartType(totalCountryNumber,outFile,type);
            }

        }
        else{// 部分省份
            if(type.size() == 0){//部分省份全部类型
                partProvinceAllType(totalCountryNumber,province,outFile);
            }
            else{// 部分省份部分类型
                partProvincePartType(totalCountryNumber,province,outFile,type);
            }

        }

        try {
            outFile.write(Lib.explain.getBytes());
            for(String str : command)
                outFile.write((str + " ").getBytes());
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        /*String[] args = {"list","-log","E:/log/",
                "-out","E:/out/output.txt",
                "-date","2020-01-22",
                "-type","cure","dead","ip",
                "-province","全国","浙江","福建"};//*/
        InfectStatistic statistic = new InfectStatistic();
        int index=1; //args[0]=list,只会出现一个命令，所以不用处理
        int startIndex,endIndex;
        //......读取并设置参数......
        while(index < args.length){
            switch (args[index]){
                case "-log" : statistic.setLogLocate(args[++index]);break;

                case "-out" : statistic.setOutLocate(args[++index]);break;

                case "-date" : statistic.setDateString(args[++index]);break;

                case  "-type" : {
                    startIndex = ++index;
                    while(index < args.length && args[index].charAt(0) != '-')
                        index++;
                    endIndex = index;
                    statistic.setType(args,startIndex,endIndex);
                    index--;//因为外层每个循环index都要自增，而现在的index已经是一个选项的下标，所以要先减去一
                }break;

                case  "-province" : {
                    startIndex = ++index;
                    while(index < args.length && args[index].charAt(0) != '-')
                        index++;
                    endIndex = index;
                    statistic.setProvince(args,startIndex,endIndex);
                    index--;
                }break;

                default:System.out.println("命令参数错误");
            }
            index++;
        }
        //....如果参数中没有日期则设为当前日期
        if(statistic.strDate == null)
            Lib.setDate(statistic);
        //....开始读入文件列表......
        String[] fileList = statistic.getFileList(statistic.logLocate);//获取文件夹下的文件列表
        //.....计数据............
        statistic.statisticData(statistic.logLocate,statistic.strDate,fileList,statistic.result);
        //......保存结果..........
        statistic.saveResult(statistic.result,statistic.outLocate,args,statistic.province,statistic.type);
    }

}


//嘿，终于写完