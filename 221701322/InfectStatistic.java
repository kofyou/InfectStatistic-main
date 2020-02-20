package com.xzy;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.exit;

public class InfectStatistic {
	public static void main(String[] args) throws ParseException, IOException {
        Map<String, ArrayList<String>> map=get_params(args);
        String front_path=map.get("-log").get(0);
        ArrayList<String> filenames=get_input_data(map);//获得所有需要统计的数据的文件名称
        Map<String, Province> map1=init_date(filenames,front_path);//初始化map，并将数据写入
        out_put(map,map1);
	}

    private static void out_put(Map<String, ArrayList<String>> map, Map<String, Province> map1) throws IOException {

        String out_path=map.get("-out").get(0);
        ArrayList<String> type=map.get("-type");
        ArrayList<String> province=map.get("-province");
        if(province==null){
            out_put_as_type(map1,type,province,out_path);//按province顺序输出，若province为空，按默认顺序输出
        }
        else{
            Map<String,Province> map2=new HashMap<String, Province>();
            for (String pro:province
                 ) {
                map2.put(pro,map1.get(pro));
            }
            out_put_as_type(map2,type,province,out_path);
        }

        //System.out.println(province);

    }

    private static void out_put_as_type(Map<String, Province> map, ArrayList<String> type, ArrayList<String> province,String out_path) throws IOException {

        ArrayList<String> results=new ArrayList<String>();
	    if(province!=null){
            for (String pro:province
            ) {
                //System.out.println(pro);
                Province province1=map.get(pro);
                //province1.show_data();
                String result=province1.name+' ';
                if(type!=null){

                    for (String type1:type
                    ) {
                        result+=province1.get_result(type1);
                    }
                }
                else{
                    ArrayList<String> types=new ArrayList<String>(
                            Arrays.asList("ip","sp","cure","dead")
                    );
                    for (String str:types
                         ) {
                        result+=province1.get_result(str);
                    }
                }
                results.add(result);
                out_put_to_file(results,out_path);

                // System.out.println(result);
            }
        }

	    else{
            String province_name[]={"全国","安徽","北京", "重庆", "福建","甘肃","广东" ,"贵州" ,"海南" ,"河北" ,"湖北","湖南" ,"吉林" ,"辽宁" ,
                    "内蒙古", "宁夏", "山东", "山西" ,"陕西" ,"上海" ,"四川" ,"天津" ,"西藏" ,"新疆", "云南", "浙江"};
            for (String pro:province_name
            ) {
                //System.out.println(pro);
                Province province1=map.get(pro);
                //province1.show_data();
                String result=province1.name+' ';
                if(type!=null){

                    for (String type1:type
                    ) {
                        result+=province1.get_result(type1);
                    }
                }
                else{
                    ArrayList<String> types=new ArrayList<String>(
                            Arrays.asList("ip","sp","cure","dead")
                    );
                    for (String str:types
                    ) {
                        result+=province1.get_result(str);
                    }
                }
                results.add(result);
                //System.out.println(result);
                out_put_to_file(results,out_path);
            }
        }


    }

    private static void out_put_to_file(ArrayList<String> result, String out_path) throws IOException {
        FileWriter file = new FileWriter(out_path);
        BufferedWriter bw = new BufferedWriter(file);
        for (String str:result
             ) {
            bw.write(str);
            bw.newLine();
        }
        bw.write("// 该文档并非真实数据，仅供测试使用");
        bw.close();

    }

    private static Map<String, Province> init_date(ArrayList<String> filenames,String front_path) throws IOException {//初始化数据，并装入map中
	    String province_name[]={"全国","安徽","北京", "重庆", "福建","甘肃","广东" ,"贵州" ,"海南" ,"河北" ,"湖北","湖南" ,"吉林" ,"辽宁" ,
                "内蒙古", "宁夏", "山东", "山西" ,"陕西" ,"上海" ,"四川" ,"天津" ,"西藏" ,"新疆", "云南", "浙江"};
	    Province[] provinces=new Province[26];
	    for(int i=0;i<provinces.length;i++){
	        provinces[i]=new Province(province_name[i]);
        }
        Map<String, Province> map=new HashMap<String, Province>();
	    int i=0;
        for (String name:province_name
             ) {
            map.put(name,provinces[i++]);
        }


        for (String after_path:filenames
             ) {
            BufferedReader br=new BufferedReader(new FileReader(front_path+after_path));
            String data=br.readLine();
            while(data!=null) {
                if (!data.substring(0, 2).equals("//")) {
                    ArrayList<String> split_data = new ArrayList<String>();
                    for (String data1 : data.split("\\s+")//按空格分割字符串,每行一个ArrayList
                    ) {
                        data1=data1.replaceAll("\\s*", "");
                        split_data.add(data1);
                    }
                    data = br.readLine();


                    statistic_data(map,split_data);//统计数据，并加入map中
                }
                else
                    data = br.readLine();
            }
        }
        Province pro=provinces[0];
        for (int j=1;j<provinces.length;j++) {
            pro.setSp(provinces[j].sp);
            pro.setIp(provinces[j].ip);
            pro.setCure(provinces[j].cure);
            pro.setDead(provinces[j].dead);
        }


        //System.out.println(map.entrySet());
	    return map;
    }

    private static void statistic_data(Map<String, Province> map, ArrayList<String> split_data) {
	    int length=split_data.size();
	    switch (length){
            case 3:
                if(split_data.get(1).equals("死亡")){
                    int str_length=split_data.get(length-1).toString().length();//获得xx人
                    long num=Long.parseLong(split_data.get(length-1).toString().substring(0,str_length-1));
                    map.get(split_data.get(0)).setDead(num);
                    map.get(split_data.get(0)).setIp(-num);
                }
                else{//治愈的情况
                    int str_length=split_data.get(length-1).toString().length();
                    long num=Long.parseLong(split_data.get(length-1).toString().substring(0,str_length-1));
                    map.get(split_data.get(0)).setCure(num);
                    map.get(split_data.get(0)).setIp(-num);
                }
                break;
            case 4:
                if(split_data.get(1).equals("新增")){
                    if(split_data.get(2).equals("感染患者")){
                        int str_length=split_data.get(length-1).length();
                        long num=Long.parseLong(split_data.get(length-1).substring(0,str_length-1));
                        map.get(split_data.get(0)).setIp(num);
                    }
                    else{
                         //新增疑似
                        int str_length=split_data.get(length-1).toString().length();
                        long num=Long.parseLong(split_data.get(length-1).toString().substring(0,str_length-1));
                        map.get(split_data.get(0)).setSp(num);
                    }
                }
                else if(split_data.get(1).equals("疑似患者")&&split_data.get(2).equals("确诊感染")){
                    int str_length=split_data.get(length-1).toString().length();
                    long num=Long.parseLong(split_data.get(length-1).toString().substring(0,str_length-1));
                    map.get(split_data.get(0)).setIp(num);
                    map.get(split_data.get(0)).setSp(-num);
                }
                else//排除的情况{
                {
                    int str_length=split_data.get(length-1).toString().length();
                    long num=Long.parseLong(split_data.get(length-1).toString().substring(0,str_length-1));
                    map.get(split_data.get(0)).setSp(-num);
                }
                break;
            case 5://流入
                if(split_data.get(1).equals("疑似患者")){

                    int str_length=split_data.get(length-1).toString().length();
                    long num=Long.parseLong(split_data.get(length-1).toString().substring(0,str_length-1));
                    map.get(split_data.get(0)).setSp(-num);
                    map.get(split_data.get(3)).setSp(num);

                }
                else{
                    int str_length=split_data.get(length-1).toString().length();
                    long num=Long.parseLong(split_data.get(length-1).toString().substring(0,str_length-1));
                    map.get(split_data.get(0)).setIp(-num);
                    map.get(split_data.get(3)).setIp(num);
                }
                break;
        }
    }

    static Map<String, ArrayList<String>> get_params(String[] args){
        Map<String, ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
        String[] params={"-log","-out","-date","-type","-province"};
        for (String param:params
        ) {
            map.put(param,null);
        }
        for (int i=1;i<args.length;) {

            if (map.containsKey(args[i])) {

                String param = args[i];
                ArrayList<String> values = new ArrayList<String>();
                int j = 0;
                while (++i<args.length&&map.containsKey(args[i]) == false) {

                    values.add(args[i]) ;
                    j++;
                }
                map.put(param,values);
                //System.out.println(map);
            } else
                i++;
        }
        return map;
    }

	static ArrayList<String>  get_input_data(Map<String, ArrayList<String>> map) throws ParseException {
	    String input_path=map.get("-log").get(0);
        File file = new File(input_path);
	    String files_name[]=file.list();
	    ///////////////////
        SimpleDateFormat sqf=new SimpleDateFormat("yyyy-MM-dd");
        Date point_date; //point_date为目标日期，需统计该日期前的数据
        Date max_date=get_max_date(files_name);
        if(map.get("-date").get(0)!=null){
            if(max_date.getTime()>=(sqf.parse(map.get("-date").get(0))).getTime())
            point_date=sqf.parse(map.get("-date").get(0));
            else
                point_date=max_date;
        }
        else{
            point_date=max_date;
        }
        ArrayList<String> filenames=get_file_need(point_date,files_name);//获得所有需要统计的日期文件名
        return filenames;
    }

    private static ArrayList<String> get_file_need(Date point_date, String [] filelist) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> need_date=new ArrayList<String>();
	    for (String filename:filelist
             ) {
            Date date1=sdf.parse(filename);
            if(point_date.getTime()>=date1.getTime()){
                need_date.add(filename);
            }
        }
        /*for (String str:need_date
        ) {
            System.out.println(str);

        }*/
	    return need_date;
    }

    private static Date get_max_date(String []filelist) throws ParseException {//获得最大日期
        SimpleDateFormat sqf=new SimpleDateFormat("yyyy-MM-dd");
        Date max_date=sqf.parse("2010-01-01");
	    for (String date:filelist
             ) {
            if(sqf.parse(date).after(max_date)){
                max_date=sqf.parse(date);
            }
        }

        return max_date;
    }

    public static class Province {
	    String name;
	    long ip=0;
	    long sp=0;
	    long cure=0;
	    long dead=0;
	    boolean flag;
	    public Province(String name){
	        this.name=name;
        }

        public void setIp(long ip) {
            this.ip += ip;
        }

        public void setSp(long sp) {
            this.sp += sp;
        }

        public void setCure(long cure) {
            this.cure += cure;
        }

        public void setDead(long dead) {
            this.dead += dead;
        }
        public void show_data(){
            System.out.println("感染"+ip+"疑似"+sp+"治愈"+cure+"死亡"+dead);
        }
        public String get_result(String type){
	        String result="";
	        if(type!=null){
	            if(type.equals("ip"))
	                result += "感染患者" + " " + ip + "人" + " ";
	            else if(type.equals("sp"))
	                result += "疑似患者" + " " + sp + "人" + " ";
	            else if(type.equals("cure"))
	                result +="治愈" + " " + cure + "人" + " ";
	            else
	                result+= "死亡" + " " + dead + "人" + " ";
            }
	        return result;
        }
    }
}
