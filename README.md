# InfectStatistic
疫情统计-作业完成


描述你的项目，包括如何运行、功能简介、作业链接、博客链接等

# 功能简介
一个疫情统计的小程序，可以在指定的目录下读取格式化的文件，并可根据输入参数调整输出内容
输出内容也可指定到某一个文本文件中

# 如何运行
1.编译``InfectStatistic.cpp``文件生成可执行文件``InfectStatistic.exe``
2.cmd或者终端下输入命令及参数例如:
``$ InfectStatistic.exe list -date 2020-01-22 -log D:/log/ -out D:/output.txt``
list命令 支持以下命令行参数：

``-log`` 指定日志目录的位置，该项必会附带
``-out`` 指定输出文件路径和文件名，该项必会附带
``-date`` 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
``-type`` 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
``-province`` 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

# 作业链接
[https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)
# 博客链接
[https://www.cnblogs.com/vegetablefriend/p/12342661.html](https://www.cnblogs.com/vegetablefriend/p/12342661.html)
