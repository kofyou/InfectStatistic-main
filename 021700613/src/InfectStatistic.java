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
    
}

class ReadFile{

}

class InfoHandle{

}

class WriteFile{

}
