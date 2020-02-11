import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * InfectStatistic
 * TODO
 *
 * @author 会飞的大野鸡
 * @version 1
 * @since 2020-2-10
 */

class InfectStatistic {
    public static void main(String[] args) {
        int a = new Execute().judge(args);
//        new Test().test(args);
//        new Execute().log(args);
    }
}

class Test{
    public void test(String[] a){
        String pro = "福建|全国";
        for(int i = 0 ; i < a.length ; i++){
            if (Pattern.matches(pro , a[i])){
                System.out.println(a[i]);
            }
        }
    }

}


/*
*
* 用于正则表达式
* @parameter  参数名
* @date 日期样式
*
* */
class Execute{
  static String parameter = "^\\-[a-z]+";
  static String date = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-" +
          "(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|" +
          "(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|" +
          "((0[48]|[2468][048]|[3579][26])00))-02-29)(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|" +
          "[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|" +
          "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|" +
          "((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
  static String province = "全国|北京|天津|上海|重庆|河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽|福建|江西|山东|" +
          "河南|湖北|湖南|广东|海南|四川|贵林|云南|陕西|甘肃|青海|台湾|内蒙古|广西|西藏|宁夏|新疆|香港|澳门";
  static String logFile = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-" +
          "(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|" +
          "(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|" +
          "((0[48]|[2468][048]|[3579][26])00))-02-29)(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|" +
          "[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|" +
          "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|" +
          "((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29).log.txt";

  public void log(String[] args){

  }



  public int judge(String[] args){
//      返回值0为语法错误，1为list语句，2为help,3为present
      java.util.List<Integer> integers = new LinkedList<Integer>();
      if (!args[0].equals("list") && !args[0].equals("help") && !args[0].equals("present")){
          System.out.println("Command " + args[0] + " not found");
          return 0;
      }
      else {
          /*
          * list语法参数
          * -log 必选  指定所需文件的目录
          * -out 必选  指定输出的文件
          * -type ip sp cure dead 感染患者 疑似患者 治愈 死亡患者
          * -date 2020-1-1
          * -province 福建 全国
          * */
          if (args[0].equals("list")){

              for (int i = 0 ; i < args.length ; i++){
                  if (Pattern.matches(parameter , args[i])){
                      integers.add(i);
                  }
              }
              if (integers.size() == 0){
                  System.out.println("请输入相应的参数及参数的选项");
                  return 0;
              }
              if (integers.get(0) != 1){
                  System.out.println("Error Command");
                  return 0;
              }

              int ExistMust = 0;
              for (int i = 0 ; i < integers.size() ; i++){
                  if (args[integers.get(i)].equals("-log")){
                      ExistMust++;
                  }
                  if (args[integers.get(i)].equals("-log")){
                      ExistMust++;
                  }
              }
              if (ExistMust != 2){
                  System.out.println("必须存在参数-log和参数-out");
                  return 0;
              }




              for (int i = 0 ; i < integers.size() ; i++){

                  if (args[integers.get(i)].equals("-log")){

                      String logInputPath;
                      if ((i+1) == integers.size()){
                          File file = new File(args[integers.get(i)+1]);
                          if (!file.isDirectory()){
                              System.out.println("The Directory is empty");
                          }
                          else {
                              File[] fileList = file.listFiles();
                              for (int j = 0 ; j < fileList.length ; j++){
                                  if (Pattern.matches(logFile , fileList[j].getName())){
                                      break;
                                  }
                              }
                          }
                      }
                      else {

                      }
                  }


                  if (args[integers.get(i)].equals("-out")){

                  }

                  if (args[integers.get(i)].equals("-type")){

                  }

                  if (args[integers.get(i)].equals("-date")){

                  }

                  if (args[integers.get(i)].equals("-province")){

                  }
              }

          }

          if (args[0].equals("help")){
              if (args.length > 1){
                  System.out.println("Comman error");
                  return 0;
              }
              return 2;
          }

          /*
          * -log
          * -out
          * -type
          * -province
          *
          * */

          if (args[0].equals("present")){

          }
      }

      return 4;
  }

}


/*
*
* list命令类
*
* */
class List{
}

/*
*
* 增加的功能，显示命令用法
* */
class Help{
    public void helpList(){

    }

    public void help(String command){

    }

    public void listHelp(){
        System.out.println("======================================================================");
        System.out.println("命令list支持参数");
        System.out.println("-log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径");
        System.out.println("-out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径");
        System.out.println("-date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件");
        System.out.println("-type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，" +
                "cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，" +
                "-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况");
        System.out.println("-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江");
        System.out.println("======================================================================");
    }

    public void presentHelp(){
        System.out.println("======================================================================");
        System.out.println("命令present支持参数");
        System.out.println("-log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径");
        System.out.println("-out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径");
        System.out.println("-type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，" +
                "cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，" +
                "-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况");
        System.out.println("-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江");
        System.out.println("======================================================================");
    }

}
/*
*
* 增加功能，得到现在实时的人数
* */
class Present{

}
