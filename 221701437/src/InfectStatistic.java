import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author luckyzzzzc
 * @version 5.1
 * @since
 */
class InfectStatistic {
    public static void main(String[] args) throws IOException {
        CmdArgs cmdArgs = new CmdArgs(args);
        ProvinceData pd = ProvinceData.getInstance();
        AbstractDataHandle dataHandle = AbstractDataHandle.getChainOfDataHandle();
        FileProcess fileProcess = FileProcess.getInstance();
        fileProcess.FileInit(cmdArgs.argVals("-log").get(0),cmdArgs.argVals("-out").get(0));
        ArrayList<File> fileList = fileProcess.InputFileData();
        for(int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).getName().substring(0,10).compareTo(cmdArgs.argVals("-date").get(0)) <= 0) {
                ArrayList<String> data = fileProcess.getFileData(fileList.get(i));
                for (int j = 0; j < data.size(); j++)
                    dataHandle.dataProcessing(data.get(j));
            }
        }
        fileProcess.LogData(cmdArgs.argVals("-province"),cmdArgs.argVals("-type"));
    }
}

class ProvinceData{
    private static ProvinceData provinceData = new ProvinceData();

    private HashMap<String,int[]> AllData = new HashMap<>();

    private ProvinceData(){
        for(int i = 0; i < Constant.initStr.length; i++){
            int[] initArray = {0,0,0,0};
            AllData.put(Constant.initStr[i],initArray);
        }
    }

    public String logProcess(ArrayList<String> allData, ArrayList<String>typeList){
        String data = new String();
        int count = 0;
        for(int i = 0; i < AllData.size(); i++){
            if(allData.get(count).equals(Constant.initStr[i])) {
                int[] dataValue = AllData.get(Constant.initStr[i]);
                data = data + Constant.initStr[i] + " ";
                for (int j = 0; j < typeList.size(); j++) {
                    data = data + Constant.STATUSCHINESE.get(typeList.get(j)) + dataValue[Constant.STATUS.get(typeList.get(j))] + "人 ";
                }
                data += "\n";
                if(count < allData.size() - 1) {
                    count++;
                }else{
                    break;
                }

            }
        }
        return data;
    }

    public static ProvinceData getInstance(){
        return provinceData;
    }

    public void AddInfectPeople(String province,int num){
        int[] temp = AllData.get(province);
        temp[0]+=num;
        AllData.put(province,temp);
    }

    public void SubInfectPeople(String province,int num){
        int[] temp = AllData.get(province);
        temp[0]-=num;
        AllData.put(province,temp);
    }

    public void AddDoubtPeople(String province,int num){
        int[] temp = AllData.get(province);
        temp[1]+=num;
        AllData.put(province,temp);
    }

    public void SubDoubtPeople(String province,int num){
        int[] temp = AllData.get(province);
        temp[1]-=num;
        AllData.put(province,temp);
    }

    public void AddCurePeople(String province,int num){
        int[] temp = AllData.get(province);
        temp[2]+=num;
        AllData.put(province,temp);
    }

    public void AddDeadPeople(String province,int num){
        int[] temp = AllData.get(province);
        temp[3]+=num;
        AllData.put(province,temp);
    }
}

abstract class AbstractDataHandle{
    protected String model;
    protected AbstractDataHandle nextDataHandle;

    public void setNextDataHandle(AbstractDataHandle nextDataHandle){
        this.nextDataHandle = nextDataHandle;
    }

    public void dataProcessing(String str){
        Pattern pattern = Pattern.compile(model);
        Matcher matcher = pattern.matcher(str);
        boolean result = matcher.matches();
        if(result){
            processing(str);
        }else if(nextDataHandle != null){
            nextDataHandle.dataProcessing(str);
        }
    }

    public static AbstractDataHandle getChainOfDataHandle(){
        AbstractDataHandle addInfect = new AddInfectPeople(Constant.s1);
        AbstractDataHandle addDoubt = new AddDoubtPeople(Constant.s2);
        AbstractDataHandle addDead = new AddDeadPeople(Constant.s3);
        AbstractDataHandle addCure = new AddCurePeople(Constant.s4);
        AbstractDataHandle subDoubt = new SubDoubtPeople(Constant.s5);
        AbstractDataHandle sureDoubt = new SureInfectPeople(Constant.s6);
        AbstractDataHandle infectInflow = new InfectInflow(Constant.s7);
        AbstractDataHandle doubtInflow = new DoubtInflow(Constant.s8);

        addInfect.setNextDataHandle(addDoubt);
        addDoubt.setNextDataHandle(addDead);
        addDead.setNextDataHandle(addCure);
        addCure.setNextDataHandle(subDoubt);
        subDoubt.setNextDataHandle(sureDoubt);
        sureDoubt.setNextDataHandle(doubtInflow);
        doubtInflow.setNextDataHandle(infectInflow);

        return addInfect;
    }

    public abstract void processing(String str);
}

class AddInfectPeople extends AbstractDataHandle{
    public AddInfectPeople(String model){
        this.model = model;
    }

    @Override
    public void processing(String str) {
        ProvinceData provinceData = ProvinceData.getInstance();
        Pattern pattern = Pattern.compile(Constant.PICKUPDIGIT);
        Matcher matcher = pattern.matcher(str);

        int num = Integer.valueOf(matcher.replaceAll("").trim());
        String[] province = str.split("\\s");

        provinceData.AddInfectPeople(province[0],num);
        provinceData.AddInfectPeople("全国",num);
    }
}

class AddDoubtPeople extends AbstractDataHandle{
    public AddDoubtPeople(String model){
        this.model = model;
    }

    @Override
    public void processing(String str) {
        ProvinceData provinceData = ProvinceData.getInstance();
        Pattern pattern = Pattern.compile(Constant.PICKUPDIGIT);
        Matcher matcher = pattern.matcher(str);

        int num = Integer.valueOf(matcher.replaceAll("").trim());
        String[] province = str.split("\\s");

        provinceData.AddDoubtPeople(province[0],num);
        provinceData.AddDoubtPeople("全国",num);
    }
}

class SubDoubtPeople extends AbstractDataHandle{
    public SubDoubtPeople(String model){
        this.model = model;
    }

    @Override
    public void processing(String str) {
        ProvinceData provinceData = ProvinceData.getInstance();
        Pattern pattern = Pattern.compile(Constant.PICKUPDIGIT);
        Matcher matcher = pattern.matcher(str);

        int num = Integer.valueOf(matcher.replaceAll("").trim());
        String[] province = str.split("\\s");

        provinceData.SubDoubtPeople(province[0],num);
        provinceData.SubDoubtPeople("全国",num);
    }
}

class AddCurePeople extends AbstractDataHandle{
    public AddCurePeople(String model){
        this.model = model;
    }

    @Override
    public void processing(String str) {
        ProvinceData provinceData = ProvinceData.getInstance();
        Pattern pattern = Pattern.compile(Constant.PICKUPDIGIT);
        Matcher matcher = pattern.matcher(str);

        int num = Integer.valueOf(matcher.replaceAll("").trim());
        String[] province = str.split("\\s");

        provinceData.AddCurePeople(province[0],num);
        provinceData.SubInfectPeople(province[0],num);
        provinceData.AddCurePeople("全国",num);
        provinceData.SubInfectPeople("全国",num);
    }
}

class AddDeadPeople extends AbstractDataHandle{
    public AddDeadPeople(String model){
        this.model = model;
    }

    @Override
    public void processing(String str) {
        ProvinceData provinceData = ProvinceData.getInstance();
        Pattern pattern = Pattern.compile(Constant.PICKUPDIGIT);
        Matcher matcher = pattern.matcher(str);

        int num = Integer.valueOf(matcher.replaceAll("").trim());
        String[] province = str.split("\\s");

        provinceData.AddDeadPeople(province[0],num);
        provinceData.SubInfectPeople(province[0],num);
        provinceData.AddDeadPeople("全国",num);
        provinceData.SubInfectPeople("全国",num);
    }
}

class DoubtInflow extends AbstractDataHandle{
    public DoubtInflow(String model){
        this.model = model;
    }

    @Override
    public void processing(String str) {
        ProvinceData provinceData = ProvinceData.getInstance();
        Pattern pattern = Pattern.compile(Constant.PICKUPDIGIT);
        Matcher matcher = pattern.matcher(str);

        int num = Integer.valueOf(matcher.replaceAll("").trim());
        String[] province = str.split("\\s");

        provinceData.SubDoubtPeople(province[0],num);
        provinceData.AddDoubtPeople(province[3],num);
    }
}

class InfectInflow extends AbstractDataHandle{
    public InfectInflow(String model){
        this.model = model;
    }

    @Override
    public void processing(String str) {
        ProvinceData provinceData = ProvinceData.getInstance();
        Pattern pattern = Pattern.compile(Constant.PICKUPDIGIT);
        Matcher matcher = pattern.matcher(str);

        int num = Integer.valueOf(matcher.replaceAll("").trim());
        String[] province = str.split("\\s");

        provinceData.SubInfectPeople(province[0],num);
        provinceData.AddInfectPeople(province[3],num);
    }
}

class SureInfectPeople extends AbstractDataHandle{
    public SureInfectPeople(String model){
        this.model = model;
    }

    @Override
    public void processing(String str) {
        ProvinceData provinceData = ProvinceData.getInstance();
        Pattern pattern = Pattern.compile(Constant.PICKUPDIGIT);
        Matcher matcher = pattern.matcher(str);

        int num = Integer.valueOf(matcher.replaceAll("").trim());
        String[] province = str.split("\\s");

        provinceData.AddInfectPeople(province[0],num);
        provinceData.SubDoubtPeople(province[0],num);
        provinceData.AddInfectPeople("全国",num);
        provinceData.SubDoubtPeople("全国",num);
    }
}

class FileProcess{
    private final static String GETFLENAME = "(\\d{4})-(\\d{2})-(\\d{2})\\.log\\.txt";
    private static FileProcess fileProcess = new FileProcess();
    private File file;
    private File outfile;

    private FileProcess(){};

    public static FileProcess getInstance(){
        return fileProcess;
    }

    public void FileInit(String in, String out){
        file = new File(in);
        outfile = new File(out);
    }

    public ArrayList<File> InputFileData(){
        File[] tempList = file.listFiles();
        ArrayList<File> fileArrayList = new ArrayList<File>();
        for(int i = 0; i < tempList.length; i++){
            if(tempList[i].isFile()){
                Pattern pattern = Pattern.compile(GETFLENAME);
                Matcher matcher = pattern.matcher(tempList[i].getName());
                boolean result = matcher.matches();
                if(result){
                    fileArrayList.add(tempList[i]);
                }
            }
        }
        Collections.sort(fileArrayList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return fileArrayList;
    }

    public ArrayList<String> getFileData(File file) throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
        BufferedReader bf = new BufferedReader(inputReader);
        String str;
        while ((str = bf.readLine()) != null) {
            arrayList.add(str);
        }
        bf.close();
        inputReader.close();
        return arrayList;
    }

    public void LogData(ArrayList<String> provinceList, ArrayList<String>typeList) throws IOException {
        ProvinceData provinceData = ProvinceData.getInstance();
        PrintStream stream=null;
        stream=new PrintStream(outfile);
        if(typeList == null || typeList.isEmpty()){
            if(typeList == null){
                typeList = new ArrayList<>();
            }
            typeList.add("ip");
            typeList.add("sp");
            typeList.add("cure");
            typeList.add("dead");
        }
        String data = new String();
        if(provinceList == null) {
            provinceList = new ArrayList<>();
            for(int i = 0; i < Constant.initStr.length; i++){
                provinceList.add(Constant.initStr[i]);
            }
            data = provinceData.logProcess(provinceList,typeList);
        }else{
            data = provinceData.logProcess(provinceList,typeList);
        }
        System.out.println(data);
        stream.print(data);
    }
}

class CmdArgs{
    private String[] args;
    private HashMap<String, ArrayList<String>> paramToValue = new HashMap<>();

    CmdArgs(String[] args){
        this.args = args;
        argsProcess();
    }

    String getCmd(){
        return this.args[0];
    }

    private void argsProcess(){
        ArrayList<String> tempValue = new ArrayList<>();
        String tempKey = new String();
        int index = 0;
        for(int i = 1; i < args.length; i++){
            if(args[i].contains("-") && !HasDigit(args[i]) && index == 0){
                tempKey = args[i];
            }else if(args[i].contains("-") && index == 1){
                ArrayList<String> tempForValue = (ArrayList<String>) tempValue.clone();
                paramToValue.put(tempKey,tempForValue);
                tempKey = args[i];
                tempValue.clear();
            }else{
                tempValue.add(args[i]);
                index = 1;
            }
        }
        paramToValue.put(tempKey,tempValue);
    }

    public ArrayList<String> argVals(String key){
        return paramToValue.get(key);
    }

    public boolean hasParam(String key){
        if(paramToValue.containsKey(key)){
            return true;
        }else {
            return false;
        }
    }

    public boolean HasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }
}

class Constant{
    public static final String[] initStr = {
            "全国","安徽","澳门","北京","重庆","福建","甘肃","广东","广西",
            "贵州", "海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏",
            "江西", "辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海",
            "四川", "台湾","天津","西藏","香港","新疆","云南","浙江"
    };

    public final static String s1 = "^[\\u4e00-\\u9fa5]*\\s(新增)\\s(感染患者)\\s(\\d+)人?";
    public final static String s2 = "^[\\u4e00-\\u9fa5]*\\s(新增)\\s(疑似患者)\\s(\\d+)人?";
    public final static String s3 = "^[\\u4e00-\\u9fa5]*\\s(死亡)\\s(\\d+)人?";
    public final static String s4 = "^[\\u4e00-\\u9fa5]*\\s(治愈)\\s(\\d+)人?";
    public final static String s5 = "^[\\u4e00-\\u9fa5]*\\s(排除)\\s(疑似患者)\\s(\\d+)人?";
    public final static String s6 = "^[\\u4e00-\\u9fa5]*\\s(疑似患者)\\s(确诊感染)\\s(\\d+)人?";
    public final static String s7 = "^[\\u4e00-\\u9fa5]*\\s(感染患者)\\s(流入)\\s[\\u4e00-\\u9fa5]*\\s(\\d+)人?";
    public final static String s8 = "^[\\u4e00-\\u9fa5]*\\s(疑似患者)\\s(流入)\\s[\\u4e00-\\u9fa5]*\\s(\\d+)人?";
    public final static String PICKUPDIGIT = "[^0-9]";

    public final static HashMap<String,Integer> STATUS = new HashMap<>();

    public final static HashMap<String,String> STATUSCHINESE = new HashMap<>();

    static {
        STATUS.put("ip", 0);
        STATUS.put("sp", 1);
        STATUS.put("cure", 2);
        STATUS.put("dead", 3);
        STATUSCHINESE.put("ip","感染患者");
        STATUSCHINESE.put("sp","疑似患者");
        STATUSCHINESE.put("cure","治愈");
        STATUSCHINESE.put("dead","死亡");
    }
}