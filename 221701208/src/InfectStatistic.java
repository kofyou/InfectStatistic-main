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
        result = new TreeMap<String,Record>();
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


    void statisticData(){

    }


    void saveResult(){

    }



    public static void main(String[] args) {
        //String[] args = {"list","-log","E:/log/","-out","E:/out/output.txt","-date","2020-01-22","-type","cure","dead","ip","-province","福建","河北",};
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
        statistic.statisticData();//统计数据
        statistic.saveResult();//保存结果


    }


}


