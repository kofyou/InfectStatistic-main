# InfectStatistic-221701423
疫情统计

* 如何运行
  
  ```
  cd ./src/
  javac InfectStatistic.java && java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
  ```
  list命令 支持以下命令行参数：
  
  * ``-log`` 指定日志目录的位置，该项必填
  * ``-out`` 指定输出文件路径和文件名，该项必填
  * ``-date`` 指定日期，不设置则默认为所提供日志最新的一天
  * ``-type`` 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 ``-type ip`` 表示只列出感染患者的情况，``-type sp cure``则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
  * ``-province`` 指定列出的省，如``-province 福建``，则只列出福建，``-province 全国 浙江``则只会列出全国、浙江
* 功能简介

  将每天的疫情统计情况放在文件夹中,程序可帮助你统计到目前为止的疫情信息
  
  该日志中出现以下几种情况：
  * <省> 新增 感染患者 n人
  * <省> 新增 疑似患者 n人
  * <省1> 感染患者 流入 <省2> n人
  * <省1> 疑似患者 流入 <省2> n人
  * <省> 死亡 n人
  * <省> 治愈 n人
  * <省> 疑似患者 确诊感染 n人
  * <省> 排除 疑似患者 n人
  
* 作业链接

  https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287

* 博客链接

  https://www.cnblogs.com/Xily9/p/12284708.html
