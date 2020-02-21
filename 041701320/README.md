# InfectStatistic-041701320

疫情统计
|这个作业属于哪个课程|[2020春S班 (福州大学)](https://edu.cnblogs.com/campus/fzu/2020SPRINGS)|
:--|:--|
|这个作业要求在哪里|[软工实践寒假作业（2/2）](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)|
|这个作业的目标|1.疫情统计程序<br/>2.github初使用<br/>3.代码规范制定<br/>|
|作业正文|[软工实践寒假作业（2/2）](https://www.cnblogs.com/fzu-yxj/p/12341776.html)|
|其他参考文献|CSDN、百度、博客园、缪雪峰的git教程|

## 1.项目描述  

&emsp;&emsp;最近新型冠状病毒疫情严重，全国人民都感到担忧，迫切希望能够及时了解到病毒最新的情况，作为IT学子，开发一个疫情统计程序。该程序实现读取日志文件并输出要求输出的省份的疑似患者人数,感染患者人数,治愈人数,死亡人数。

## 2.如何运行  

 1. 命令行（win+r cmd）cd到项目src下
 2. 输入命令：javac Infectstatistic.java
 3. 输入命令：java Infectstatistic list （+各个参数）

## 3.功能简介

list命令 支持以下命令行参数：

* -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
* -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
* -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
* -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
* -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

注：java InfectStatistic表示执行主类InfectStatistic，list为命令，-date代表该命令附带的参数，-date后边跟着具体的参数值，如2020-01-22。-type 的多个参数值会用空格分离，每个命令参数都在上方给出了描述，每个命令都会携带一到多个命令参数

实例：
>java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt

&emsp;&emsp;会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省**（日志文件中有提到）**的情况（全国总是排第一个，别的省按拼音先后排序）
