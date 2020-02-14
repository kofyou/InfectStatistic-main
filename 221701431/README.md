# InfectStatistic-221701431

###1.项目简介
&nbsp;&nbsp;该项目读取用户所输入的文件路径，获得日志，根据其他的-type,-province,-date来做筛选，最后输出到用户指定的文件中

###2.运行
&nbsp;&nbsp;用户到src文件中，输入命令:

> 一个命令实例：java InfectStatistic -log /Users/a/... -out /Users/... -date ...  
> 当然使用前需要编译文件 javac InfectStatistic.java

###3.功能简介
- list命令  
&nbsp;&nbsp;参数:-log , -out , -date , -province , -type  
    * -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
    * -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
    * -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件
    * -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
    * -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

- help命令  
&nbsp;&nbsp;无参数，显示所有命令的功能

- present命令
&nbsp;&nbsp;参数-out
    *-out 指定输出文件路径和文件名，必须存在。将现在的真实数据显示到该文件中。

