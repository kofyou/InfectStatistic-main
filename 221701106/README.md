# InfectStatistic-221701106
疫情统计

+ 如何运行

  在cmd下进入InfectStatistic.java文件所在的文件夹

  随后输入javac -encoding utf8 InfectStatistic.java进行编译

  最后即可使用以下命令行参数

  - `-log` 指定日志目录的位置，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
  - `-out` 指定输出文件路径和文件名，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
  - `-date` 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
  - `-type` 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 `-type ip` 表示只列出感染患者的情况，`-type sp cure`则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
  - `-province` 指定列出的省，如`-province 福建`，则只列出福建，`-province 全国 浙江`则只会列出全国、浙江

  例：java InfectStatistic list -log D:\game\221701106\example\log -out D:\output.txt -date 2020-02-25 -type dead ip cure -province 福建 武汉

+ 功能简介

  可以输出指定日期（若没有指定则为系统日期）累计的感染患者人数、疑似患者人数、治愈人数和死亡患者人数。可以通过-date、-type、-province的任意组合得到想要的省份、日期或类型的情况。

+ 作业链接

  [软工实践寒假作业（2/2）](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)

+ 博客链接

  [https://www.cnblogs.com/221701106-lxy/p/12340000.html](https://www.cnblogs.com/221701106-lxy/p/12340000.html)