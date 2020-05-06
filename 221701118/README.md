# InfectStatistic-221701118
疫情统计

描述你的项目，包括如何运行、功能简介、作业链接、博客链接等

* 项目描述
如何运行：命令行（win+r cmd）cd到项目src下，之后输入命令
java InfectStatistic list
后面的参数可以为：
>>-log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
-out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
-date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
-type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

命令中必须包含-log和-out，其余参数没有则取默认值，参数顺序不定

功能简介：通过已有的日志列出全国和各省在某日的感染情况

作业链接：https://github.com/ZhangJiaWeiDEV/InfectStatistic-main

博客链接：https://www.cnblogs.com/zjwblogs/p/SoftwareVacationWork2.html