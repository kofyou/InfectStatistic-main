//package Yiqing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * InfectStatistic
 * TODO
 *程序执行过程中需要的一些静态方法
 *
 * @zdc5511116_221701222
 * @version 0.7
 * @since 2020.02
 */
public class Lib {
    /**
     * 初始化统计数据
     * @param statistics 统计数据
     */
    public static void mapInit(Map<String, List<Integer>> statistics){
        List<Integer> originData = new ArrayList<>();
        originData.add(0); //感染人数
        originData.add(0); //疑似人数
        originData.add(0); //治愈人数
        originData.add(0); //死亡人数
        statistics.put("全国",new ArrayList<>(originData));
        statistics.put("安徽",new ArrayList<>(originData));
        statistics.put("澳门",new ArrayList<>(originData));
        statistics.put("北京",new ArrayList<>(originData));
        statistics.put("重庆",new ArrayList<>(originData));
        statistics.put("福建",new ArrayList<>(originData));
        statistics.put("甘肃",new ArrayList<>(originData));
        statistics.put("广东",new ArrayList<>(originData));
        statistics.put("广西",new ArrayList<>(originData));
        statistics.put("贵州",new ArrayList<>(originData));
        statistics.put("河北",new ArrayList<>(originData));
        statistics.put("海南",new ArrayList<>(originData));
        statistics.put("河南",new ArrayList<>(originData));
        statistics.put("黑龙江",new ArrayList<>(originData));
        statistics.put("湖北",new ArrayList<>(originData));
        statistics.put("湖南",new ArrayList<>(originData));
        statistics.put("吉林",new ArrayList<>(originData));
        statistics.put("江苏",new ArrayList<>(originData));
        statistics.put("江西",new ArrayList<>(originData));
        statistics.put("辽宁",new ArrayList<>(originData));
        statistics.put("内蒙古",new ArrayList<>(originData));
        statistics.put("宁夏",new ArrayList<>(originData));
        statistics.put("青海",new ArrayList<>(originData));
        statistics.put("山东",new ArrayList<>(originData));
        statistics.put("山西",new ArrayList<>(originData));
        statistics.put("陕西",new ArrayList<>(originData));
        statistics.put("上海",new ArrayList<>(originData));
        statistics.put("四川",new ArrayList<>(originData));
        statistics.put("台湾",new ArrayList<>(originData));
        statistics.put("天津",new ArrayList<>(originData));
        statistics.put("香港",new ArrayList<>(originData));
        statistics.put("西藏",new ArrayList<>(originData));
        statistics.put("新疆",new ArrayList<>(originData));
        statistics.put("云南",new ArrayList<>(originData));
        statistics.put("浙江",new ArrayList<>(originData));
    }

    /**
     *帮助文档，当执行 “java 程序名”时显示的内容
     */
    public static void help(){
        System.out.println("用法：java <主类名> list [args...]");
        System.out.println("（执行list命令）");
    }

    /**
     *帮助文档，当执行  ”java 程序名 list“ 时显示的内容
     */
    public static void helpList(){
        System.out.println("list用法：java <主类名> list [args...]");
        System.out.println("args：-log <日志目录绝对路径>");
        System.out.println("（给定日志文件目录---必带参数）");
        System.out.println("或：-out <输出文件绝对路径>");
        System.out.println("（给定日志输出目录---必带参数）");
        System.out.println("或：-date <日期>");
        System.out.println("（统计指定日期之前（包括指定日期）的日志文件---可选参数 日期格式 xxxx-xx-xx 如 2020-01-1-01）");
        System.out.println("或：-type [ip sp cure dead]");
        System.out.println("（给定输出类型---可选参数 类型可选 ip sp cure dead 可多选）");
        System.out.println("或：-province [省1 省2 省3...]");
        System.out.println("（给定输出省份数据---可选参数 省份可多选 使用简称 如 福建省 输入 福建）");
    }

    /**
     * 解析日志文档里的数据，比如“10人”解析成整型数字”10“
     * @param dataNumber 需要解析的数据
     * @return 解析结果
     */
    public static int parseData(String dataNumber) throws Exit {
        try{
            return Integer.parseInt(dataNumber.substring(0,dataNumber.length() - 1));
        }catch (Exception e){
            throw new Lib.Exit(e.getMessage());
        }
    }

    /**
     * 从日志文件路径中提取日志文件
     * @param logDirectory 给定的日志文件路径
     * @return 日志文件夹下的所有日志文件list
     */
    public static List<File> getLogFiles(File logDirectory){
        List<File> fileList = new ArrayList<>();
        String regex = "\\d{4}-\\d{2}-\\d{2}.log.txt";   //日志文件名所遵循的格式
        for (File file : logDirectory.listFiles()){
            if(file.getName().matches(regex)){
                fileList.add(file);
            }
        }
        return fileList;
    }

    public static class Exit extends Exception{

        public Exit(){
            super();
        }

        public Exit(String msg){
            super(msg);
        }
    }
}