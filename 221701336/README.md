# InfectStatistic-221701336

## **疫情统计**

* **项目描述**

>使用c++语言设计并开发一个疫情统计程序，通过命令行操作可以读取日志内容，进行归纳整理并输出到对应文本，列出全国及各省市的病况。

* **运行方式**

>命令行（win+r cmd）cd到项目src下，编译代码InfectStatistic.cpp后生成可执行文件InfectStatistic.exe，之后输入相关命令，比如 InfectStatistic.exe list -date 2020-01-22 -log D:/log/ -out D:/output.txt 会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）

* **功能简介**

>通过list命令后添加参数来进行各项操作<br/>
list命令 支持以下命令行参数：<br/>
-log 指定日志目录的位置，该项是必须参数<br/>
-out 指定输出文件路径和文件名，该项是必须参数<br/>
-date 指定日期，不设置则默认为所提供日志最新的一天，格式形如“2020-01-23”<br/>
-type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。<br/>
-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江<br/>

* **作业链接**

>github仓库<https://github.com/FreeHqq/InfectStatistic-main>

* **博客链接**

>博客园链接<https://www.cnblogs.com/hqq1031196651/p/12336531.html>
