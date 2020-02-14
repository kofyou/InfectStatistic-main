import java.util.ArrayList;

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
    private String date=null;
    private ArrayList<String> types=null;
    private ArrayList<String> provinces=null;

    //统计用的对象
    private LogList logList=new LogList();
    private Country country=Country.getInstance();

    public static void main(String[] args) {
        InfectStatistic infectInfoOperator=new InfectStatistic();

        //从命令行读取参数到该类
        infectInfoOperator.readParameter(args);
        //
        //infectInfoOperator.readLogs();
    }

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
            date=args[datePosition+1];
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

        System.out.println(this.toString());

    }

    public void readLogs(){
        logList.readLogsFromPath(inputPath);
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
