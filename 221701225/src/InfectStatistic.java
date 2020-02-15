import java.text.Collator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    //从命令行读取到的参数
    private String inputPath;
    private String outputPath;
    private LocalDate date=null;
    private ArrayList<String> types=null;
    private ArrayList<String> provinces=null;
    private Country country;
    //统计用的对象
    private LogList logList=new LogList();

    InfectStatistic(){
        country=Country.getInstance();
    }
    public static void main(String[] args) {
        String[] debugArgs=("-log C:\\Users\\62706\\Documents\\GitHub\\InfectStatistic-main\\221701225\\log -out ../result " +
                "-date 2020-01-22 -province 福建 河北").split(" ");
        InfectStatistic infectInfoOperator=new InfectStatistic();
        //从命令行读取参数到该类
        infectInfoOperator.readParameter(debugArgs);
        //
        infectInfoOperator.readLogs();
        //infectInfoOperator.country.getProvince("福建").printAllInfo();
        infectInfoOperator.output();
    }

    /**
     * 读取命令行参数到类属性
     * @param args 命令行参数
     */
    public void readParameter(String[] args){
        int inputPosition=-1;
        int outputPosition=-1;
        int datePosition=-1;
        int typesPosition=-1;
        int provincePosition=-1;

        for(int i=0;i<args.length;i++){
            if(args[i].equals("-log"))
                inputPosition=i;
            if(args[i].equals("-out"))
                outputPosition=i;
            if(args[i].equals("-date"))
                datePosition=i;
            if(args[i].equals("-type"))
                typesPosition=i;
            if(args[i].equals("-province"))
                provincePosition=i;
        }

        //读取各个参数到类属性中
        if(inputPosition!=-1) {
            inputPath = args[inputPosition + 1];
        }
        if(outputPosition!=-1) {
            outputPath = args[outputPosition + 1];
        }
        if(datePosition!=-1){
            date=LocalDate.parse(args[datePosition+1]);
        }
        if(typesPosition!=-1){
            types=new ArrayList<String>();
            int pos=typesPosition+1;
            int length=args.length;

            //第一个条件放在前面防止下标越界
            while(pos<length && !args[pos].contains("-")){
                types.add(args[pos]);
                pos++;
            }
        }
        if(provincePosition!=-1){
            provinces=new ArrayList<>();
            int pos=provincePosition+1;
            int length=args.length;

            while(pos<length && !args[pos].contains("-")){
                provinces.add(args[pos]);
                pos++;
            }
        }

        //System.out.println(this.toString());

    }

    /**
     * 从输入路径读取日志
     */
    public void readLogs(){
        logList.readLogsFromPath(inputPath);
    }

    /**
     * 根据获取到的参数的情况进行输出
     */
    public void output(){
        LocalDate beginDate;
        LocalDate endDate;
        String[] outputTypes=null;
        HashMap<String,DailyInfo> provinceDailyInfos;
        DailyInfo countryTotalInfo;

        //设置统计的起始时间、结束时间
        beginDate=logList.getBeginDate();
        if(date==null)
            endDate=logList.getEndDate();
        else
            endDate=date;

        if(types==null)
            outputTypes=DailyInfo.ALL_TYPES;
        else
            outputTypes=types.toArray(new String[types.size()]);

        //获取全国统计信息及各省统计信息
        countryTotalInfo=country.getCountryTotalInfo(beginDate,endDate);
        provinceDailyInfos=country.getAllProvincesInfo(beginDate,endDate);

        if(provinces==null) {
            //未指定省份时只打印全国和在日志中出现过的省份
            System.out.println("全国 " + countryTotalInfo.toString(outputTypes));

            for (String provinceName : Country.PROVINCES) {
                Province province = country.getProvince(provinceName);

                if (province.hasOccured == true) {
                    DailyInfo provinceInfo = provinceDailyInfos.get(provinceName);
                    System.out.println(provinceName + " " + provinceInfo.toString(outputTypes));
                }
            }
        }
        else{
            if(provinces.contains("全国")){
                System.out.println("全国 " + countryTotalInfo.toString(outputTypes));
                provinces.remove("全国");
            }

            Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);
            Collections.sort(provinces, CHINA_COMPARE);

            //遍历指定的省份
            for(String provinceName:provinces){
                Province province = country.getProvince(provinceName);

                //省份在日志出现过，就打印统计得到的数据，否则直接全输出0
                if (province.hasOccured == true) {
                    DailyInfo provinceInfo = provinceDailyInfos.get(provinceName);
                    System.out.println(provinceName + " " + provinceInfo.toString(outputTypes));
                }
                else{
                    DailyInfo emptyInfo=new DailyInfo(endDate);
                    System.out.println(provinceName+" "+emptyInfo.toString(outputTypes));
                }
            }
        }



    }
    @Override
    public String toString() {
        return "InfectStatistic{" +
                "inputPath='" + inputPath + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", date='" + date + '\'' +
                ", types=" + types +
                ", provinces=" + provinces +
                '}';
    }
}
