InfectStatistic-221701215
疫情统计

项目简介

  该程序可以读取指定文件夹下的日志文件并计算各省的感染情况，然后把结果输出到指定文件。


运行方式

  在命令行下进入InfectStatistic.java文件所在路径，使用命令行命令运行程序。例如：java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt就是执行list命令。
该命令会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）。


功能简介

  list命令 支持以下命令行参数：
    -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
    -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
    -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
    -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
    -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

作业链接:  [点我](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)



博客链接： [点我](https://www.cnblogs.com/yangmingwei969/p/12298734.html)
