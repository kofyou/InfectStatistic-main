import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * InfectStatistic
 * TODO
 *
 * @author wys
 * @version v 1.0.0
 * @since 2020.2.11
 */
public class InfectStatistic {

    public static void main(String[] args) throws IOException {
        //调用方法，解析出各个参数
        Map<String,Object> argsMap = Lib.getArgsMap(args);
        //如果返回的是null，说明参数有误，直接return
        if(argsMap == null) {
            return;
        }
        String log = argsMap.get("log").toString();
        String out = argsMap.get("out").toString();
        String date = argsMap.get("date").toString();
        List<String> type = (List<String>)argsMap.get("type");
        List<String> province = (List<String>)argsMap.get("province");

        //1.根据log获取到最新的文件日期
        File fileDir = new File(log);
        if(!fileDir.isDirectory()) {
            System.out.println("log参数错误，未能获取到文件夹");
            return;
        } else {
            //获取到文件夹中的所有文件
            File[] files = fileDir.listFiles();
            if(files.length<1) {
                System.out.println("文件夹下没有任何文件");
                return;
            }

            //获取到文件夹中最大日期的文件名称。
            String maxDate = "";
            maxDate = getMaxDate(files);

            //如果date没有输入的话，默认成最新的日期
            if(date == "") {
                date = maxDate;
            }

            //用date跟maxDate比较，看date参数是否正确
            if((date.compareTo(maxDate)>0)) {
                System.out.println("date参数有问题，不能大于文件的最大时间");
                return;
            }

            //定义最后返回时候的变量
            Map<String,Map<String,Integer>> mapMap = new HashMap<String, Map<String,Integer>>();
            Map<String,Integer> map = new LinkedHashMap<String,Integer>();

            //map2一直是全国的数据，便于下面随时操作。
            Map<String,Integer> map2 = new LinkedHashMap<String,Integer>();
            map2.put(Lib.str1, 0);
            map2.put(Lib.str2, 0);
            map2.put(Lib.str3, 0);
            map2.put(Lib.str4,0);
            //mapMap.put("全国", map2);
            //根据date和log循环所有文文件，读入统计各个信息。然后计算全国信息
            for(int i=0; i<files.length; i++) {
                String fileName = files[i].getName();
                if(fileName.contains(".log.txt")) {
                    String fileNameDate = fileName.substring(0,fileName.indexOf(".log.txt"));

                    //遍历所有的
                    if(date.compareTo(fileNameDate)>=0) {
                        InputStreamReader isr = new InputStreamReader(new FileInputStream(files[i]), "UTF-8");
                        BufferedReader br = new BufferedReader(isr);
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            //每一行根据空格分割成数组,然后逐个判断计算
                            String[] arr = line.split("\\s{1,}");
                            //经过查看，每一行切割以后，长度最小是3  最大 为5,
                            //经过长度判断，去除掉部分有问题的数据
                            if(arr.length>2 && arr.length<6) {
                                //当第一组字符属于城市的一部分的时候，继续执行下面的部分
                                if(Lib.citiesList.contains(arr[0])) {
                                    if(mapMap.containsKey(arr[0])) {
                                        map = mapMap.get(arr[0]);
                                    } else {
                                        map = new LinkedHashMap<String,Integer>();
                                        map.put(Lib.str1, 0);
                                        map.put(Lib.str2, 0);
                                        map.put(Lib.str3, 0);
                                        map.put(Lib.str4,0);
                                        mapMap.put(arr[0], map);
                                    }

                                    //length为声明长度
                                    if(arr.length==3) {
                                        //此时状况为死亡，治愈两种情况，都是在对应的数上做加法；同时，感染患者减去这些；
                                        String tempNumStr = arr[2].substring(0,arr[2].length()-1);
                                        int tempNum = Integer.parseInt(tempNumStr);
                                        map.put(arr[1], map.get(arr[1])+tempNum);
                                        map.put(Lib.str1, map.get(Lib.str1)-tempNum);
                                        //计算上全国的数据
                                        map2.put(arr[1], map2.get(arr[1])+tempNum);
                                        map2.put(Lib.str1, map2.get(Lib.str1)-tempNum);
                                    } else if(arr.length==4) {
                                        if(arr[1].equals("新增")) {
                                            //新增，无论是感染新增，还是疑似新增，都是在原基础上做加法,同样的全国的变化也是如此
                                            String tempNumStr = arr[3].substring(0,arr[3].length()-1);
                                            int tempNum = Integer.parseInt(tempNumStr);
                                            map.put(arr[2], map.get(arr[2])+tempNum);

                                            //计算上全国的数据
                                            map2.put(arr[2], map2.get(arr[2])+tempNum);


                                        } else {
                                            if(arr[1].equals("疑似患者")) {
                                                if(arr[2].equals("确诊感染")) {
                                                    //在此情况下，疑似患者数量减少n ， 确诊患者数量增加 n,同样的全国的变化也是如此
                                                    //n 为此条数据最后的那个数字
                                                    String tempNumStr = arr[3].substring(0,arr[3].length()-1);
                                                    int tempNum = Integer.parseInt(tempNumStr);
                                                    map.put(arr[1], map.get(arr[1])-tempNum);
                                                    map.put(Lib.str1, map.get(Lib.str1)+tempNum);
                                                    map2.put(arr[1], map2.get(arr[1])-tempNum);
                                                    map2.put(Lib.str1, map2.get(Lib.str1)+tempNum);
                                                }
                                            } else if(arr[1].equals("排除")) {
                                                //此种情况下，该省排除相应人数，全国也排除相应人数
                                                String tempNumStr = arr[3].substring(0,arr[3].length()-1);
                                                int tempNum = Integer.parseInt(tempNumStr);
                                                map.put(arr[2], map.get(arr[2])-tempNum);
                                                map2.put(arr[2], map2.get(arr[2])-tempNum);
                                            }
                                        }
                                    } else if(arr.length==5) {
                                        //此种情况就是给省1减去对应的人给省二加上对应的人,对于全国来说没有变化
                                        //（1）对省一的操作
                                        String tempNumStr = arr[4].substring(0,arr[4].length()-1);
                                        int tempNum = Integer.parseInt(tempNumStr);
                                        map.put(arr[1], map.get(arr[1])-tempNum);
                                        String city2 = arr[3];
                                        if(mapMap.containsKey(city2)){
                                            map = mapMap.get(city2);
                                        } else {
                                            map = new LinkedHashMap<String,Integer>();
                                            map.put(Lib.str1, 0);
                                            map.put(Lib.str2, 0);
                                            map.put(Lib.str3, 0);
                                            map.put(Lib.str4,0);
                                            mapMap.put(city2, map);
                                        }
                                        //（2）对省二的操作
                                        map.put(arr[1], map.get(arr[1])+tempNum);
                                    }
                                }
                            }
                        }
                        //到这里一个文件处理完毕

                    }

                }
            }
            //到这里文件夹处理完毕

            //下面的工作：处理文件输出
            //写入out对应的文件中,先判断前面的文件夹是不是存在，不存在就创建
            String dir = out.substring(0,out.lastIndexOf("/"));
            File f = new File(dir);
            if(!f.exists() || !f.isDirectory()) {
                f.mkdirs();
            }

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(out)),
                    "UTF-8"));

            //遍历mapMap,按照特定的顺序排序
            List<String> keys = new ArrayList<String>();
            keys.addAll(mapMap.keySet());
            keys = Lib.sortByAlphabet(keys);

            //判断type
            if(type.size()>0 && province.size()<1) {
                //只有type
                StringBuffer sbf = new StringBuffer();
                for(int i=0; i<type.size(); i++) {
                    sbf.append(Lib.getNamebyType(type.get(i)) + map2.get(Lib.getNamebyType(type.get(i)))+"人  ");
                }
                bw.write("全国  " + sbf.toString());
                //先输出全国的

                //换行
                bw.newLine();

                //根据排好续的keys依次输出到txt中
                for(int i=0; i<keys.size(); i++) {
                    Map<String,Integer> tempMap = mapMap.get(keys.get(i));
                    StringBuffer sbf2 = new StringBuffer();
                    for(int j=0; j<type.size(); j++) {
                        sbf2.append(Lib.getNamebyType(type.get(j))+tempMap.get(Lib.getNamebyType(type.get(j)))+"人  ");
                    }
                    bw.write(keys.get(i)+"  "+sbf2.toString());
                    bw.newLine();
                }
                bw.close();
            } else if(type.size()>0 && province.size()>0) {
                //既有type，也有province  //先输出全国的
                if(province.contains("全国")) {
                    StringBuffer sbf = new StringBuffer();
                    for(int i=0; i<type.size(); i++) {
                        sbf.append(Lib.getNamebyType(type.get(i))+map2.get(Lib.getNamebyType(type.get(i)))+"人  ");
                    }
                    bw.write("全国  "+sbf.toString());
                    bw.newLine();
                }

                //全国输入进去以后，再看还有没有没输入的
                province = Lib.sortByAlphabet(province);
                for(String str: province) {
                    if(str.equals("全国")) {
                        continue;
                    }
                    Map<String,Integer> tempMap = mapMap.get(str);
                    StringBuffer sbf2 = new StringBuffer();
                    for(int j=0; j<type.size(); j++) {
                        sbf2.append(Lib.getNamebyType(type.get(j))+(tempMap==null? 0:tempMap.get(Lib.getNamebyType(type.get(j))))+"人  ");//???
                    }
                    bw.write(str+"  "+sbf2.toString());
                    bw.newLine();

                }

                bw.close();
                //不指定type，则列出所有情况
            } else if(type.size()<1 && province.size()>0) {
                if(province.contains("全国")) {
                    bw.write("全国  "+Lib.str1+map2.get(Lib.str1)+"人  "+Lib.str2+map2.get(Lib.str2)+"人  "
                            +Lib.str3+map2.get(Lib.str3)+"人  "+Lib.str4+map2.get(Lib.str4)+"人");
                    bw.newLine();
                } else {
                    province = Lib.sortByAlphabet(province);
                    for(String str:province){
                        int str1Num = 0;
                        int str2Num = 0;
                        int str3Num = 0;
                        int str4Num = 0;
                        if(mapMap.containsKey(str)) {
                            Map<String,Integer> tempMap = mapMap.get(str);
                            str1Num = tempMap.get(Lib.str1);
                            str2Num = tempMap.get(Lib.str2);
                            str3Num = tempMap.get(Lib.str3);
                            str4Num = tempMap.get(Lib.str4);
                        }
                        bw.write( str+"  "+Lib.str1+str1Num+"人  "+Lib.str2+str2Num+"人  "+Lib.str3+str3Num+"人  "+Lib.str4+str4Num+"人");
                        bw.newLine();
                    }
                }
                bw.close();
            } else {
                //既没有type也没有province
                //先输出全国的
                bw.write("全国  "+Lib.str1+map2.get(Lib.str1)+"人  "+Lib.str2+map2.get(Lib.str2)+"人  "
                        +Lib.str3+map2.get(Lib.str3)+"人  "+Lib.str4+map2.get(Lib.str4)+"人");
                bw.newLine();

                //根据排好续的keys依次输出到txt中
                for(int i=0; i<keys.size(); i++) {
                    Map<String,Integer> tempMap = mapMap.get(keys.get(i));
                    bw.write( keys.get(i)+"  "+Lib.str1+tempMap.get(Lib.str1)+"人  "+Lib.str2+tempMap.get(Lib.str2)
                            +"人  "+Lib.str3+tempMap.get(Lib.str3)+"人  "+Lib.str4+tempMap.get(Lib.str4)+"人");
                    bw.newLine();
                }
                bw.close();
            }
        }

    }
    /**
     *获取到文件夹中最大日期的文件名称
     * @param -log 参数下的目录文件
     * @return 文件最大日期
     */
    public static String getMaxDate(File[] files) {
        String maxDate = "";
        String tempDate = "";
        for(int i=0; i<files.length; i++) {
            String fileName = files[i].getName();
            if(fileName.contains(".log.txt")) {
                String fileNameDate = fileName.substring(0, fileName.indexOf(".log.txt"));
                if(i == 1) {
                    maxDate = fileNameDate;
                    tempDate = fileNameDate;
                } else {
                    tempDate = fileNameDate;
                    //两个日期格式的文件能直接这样比较大小
                    if(tempDate.compareTo(maxDate)>0) {
                        maxDate = tempDate;
                    }
                }
            }
        }
        return maxDate;
    }
}

