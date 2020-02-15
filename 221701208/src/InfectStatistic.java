import java.io.*;
import java.text.Collator;
import java.text.SimpleDateFormat;
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

    void countInfection(int number){
        infectionNumber += number;
    }


    void countSuspected(int number){
        suspectedNumber += number;
    }


    void countCure(int number){
        cureNumber += number;
    }


    void countDead(int number){
        deadNumber += number;
    }

    int getInfectionNumber(){
        return  infectionNumber;
    }


    int getSuspectedNumber(){
        return suspectedNumber;
    }

    int getCureNumber(){
        return  cureNumber;
    }


    int getDeadNumber(){
        return deadNumber;
    }

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




class MapKeyComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        Collator collator = Collator.getInstance();
        return collator.getCollationKey(str1).compareTo(collator.getCollationKey(str2));
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
        int number = Integer.valueOf(info[info.length-1].substring(0,info[info.length-1].length()-1));//获得人数
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
        int number =  Integer.valueOf(strNumber);//获得人数
        String province = info[0];
        String ip="感染患者",sp="疑似患者";
        if(info[1].equals("新增") && info[2].equals(ip)){//新增感染
            if(!result.containsKey(province)){//result 中还未出现过这个省的记录
                Record newRecord = new Record();
                newRecord.countInfection(number);
                result.put(province,newRecord);
            }
            else{
                Record aRecord = result.get(province);//获得这个省的数据记录
                aRecord.countInfection(number);
                result.put(province,aRecord);
            }

        }

        else if(info[1].equals("新增") && info[2].equals(sp)){//新增疑似
            if(!result.containsKey(province)){//result 中还未出现过这个省的记录
                Record newRecord = new Record();
                newRecord.countSuspected(number);
                result.put(province,newRecord);
            }
            else{
                Record aRecord = result.get(province);//获得这个省的数据记录
                aRecord.countSuspected(number);
                result.put(province,aRecord);
            }

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
     * @param info 一行数据
     * @param result 结果集
     */
    void countMovedIpAndSp(String[] info,Map<String,Record> result){
        int number = Integer.valueOf(info[info.length-1].substring(0,info[info.length-1].length()-1));//获得人数
        String sourceProvince = info[0];
        String aimProvince = info[3];
        String ip = "感染患者",sp = "疑似患者";
        if(info[1].equals(ip)){//感染患者流入
            if(!result.containsKey(aimProvince)){//未出现过的省份
                Record newRecord = new Record();//加上数据
                newRecord.countInfection(number);
                result.put(aimProvince,newRecord);
            }
            else {
                Record tmpRecord = result.get(aimProvince);//加上数据
                tmpRecord.countInfection(number);
                result.put(aimProvince,tmpRecord);
            }
            Record aRecord = result.get(sourceProvince);//减去数据
            aRecord.countInfection(-number);
            result.put(sourceProvince,aRecord);

        }
        else if(info[1].equals(sp)){//疑似患者流入
            if(!result.containsKey(aimProvince)){//未出现过的省份
                Record newRecord = new Record();//加上数据
                newRecord.countSuspected(number);
                result.put(aimProvince,newRecord);
            }
            else {
                Record tmpRecord = result.get(aimProvince);//加上数据
                tmpRecord.countSuspected(number);
                result.put(aimProvince,tmpRecord);
            }
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
                for(String i : Lib.allType){
                    for(String j : type){
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


    void partProvinceAllType(Record totalCountryNumber , Vector<String> province , FileOutputStream outFile){
        /* String record = "全国 " + Lib.allType[0] + totalCountryNumber.getInfectionNumber() + Lib.unit +
                Lib.allType[1] + totalCountryNumber.getSuspectedNumber() + Lib.unit +
                Lib.allType[2] + totalCountryNumber.getCureNumber() + Lib.unit +
                Lib.allType[3] + totalCountryNumber.getDeadNumber() + Lib.unit + "\n";
        try {
            outFile.write(record.getBytes());//写入全国数据
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        result.put("全国",totalCountryNumber);
        String record;

        for(String item : province){
            record = item + " ";
            if(!result.containsKey(item)){//查询无记录的省份则新建一个记录，数据都为0
                Record newRecord = new Record();
                result.put(item,newRecord);
            }

            record +=  Lib.toChinese(Lib.allType[0])+ result.get(item).getInfectionNumber() +  Lib.unit +
                    Lib.toChinese(Lib.allType[1]) + result.get(item).getSuspectedNumber() + Lib.unit +
                    Lib.toChinese(Lib.allType[2]) + result.get(item).getCureNumber() + Lib.unit +
                    Lib.toChinese(Lib.allType[3]) + result.get(item).getDeadNumber() + Lib.unit + "\n";
            try {
                outFile.write(record.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    void partProvincePartType(Record totalCountryNumber , Vector<String> province , FileOutputStream outFile,Vector<String> type){

         /*String record = "全国 ";
        for(String i : Lib.allType){
            for(String j : type){
                if(i.equals(j)){
                    record += i + totalCountryNumber.getNumber(j) + Lib.unit;
                }
            }
        }
        record += "\n";
        try {
            outFile.write(record.getBytes());//写入全国数据
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        result.put("全国",totalCountryNumber);
        String record;
        for(String item : province){
            if(!result.containsKey(item)){//查询无记录的省份则创建一个数据为空的记录
                Record newRecord = new Record();
                result.put(item,newRecord);
            }
            record = item +" ";
            for(String i : Lib.allType){
                for(String j : type){
                    if(i.equals(j)){
                        record +=  Lib.toChinese(j) + result.get(item).getNumber(j) + Lib.unit;
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



    public static void main(String[] ags) {
        String[] args = {"list","-log","E:/log/","-out","E:/out/output.txt","-date","2020-01-29","-type","cure","dead","ip","-province","全国","福建","河北"};
        InfectStatistic statistic = new InfectStatistic();
        int index=1; //args[0]=list,不用处理
        int startIndex,endIndex;
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

                default:System.out.println("参数错误");
            }
            index++;
        }

        if(statistic.strDate == null){
            Date d = new Date();
            System.out.println(d);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateNowStr = sdf.format(d);
            statistic.strDate = dateNowStr;

        }

        //....开始读入文件......
        String[] fileList = statistic.getFileList(statistic.logLocate);//获取文件夹下的文件列表
        statistic.statisticData(statistic.logLocate,statistic.strDate,fileList,statistic.result);//统计数据
        statistic.saveResult(statistic.result,statistic.outLocate,args,statistic.province,statistic.type);//保存结果


    }


}


