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
    private String inputPath;
    private String outputPath;
    private String date=null;
    private ArrayList<String> types=null;
    private ArrayList<String> provinces=null;

    public static void main(String[] args) {
        InfectStatistic infectInfoOperator=new InfectStatistic();
//        System.out.println("helloworld");
        infectInfoOperator.readParameter(args);
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
        inputPath=args[inputPosition+1];
        outputPath=args[outputPosition+1];
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
