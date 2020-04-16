# InfectStatistic-221701236
疫情统计

- 项目简介<br/><br/>
&emsp;&emsp;该程序可以读取指定文件夹下的日志文件并计算各省的感染情况，然后把结果输出到指定文件。<br/><br/>

- 运行方式<br/><br/>
&emsp;&emsp;在命令行下进入InfectStatistic.java文件所在路径，使用命令行命令运行程序。例如：java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt就是执行list命令。<br/>
该命令会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）。<br/><br/>

- 功能简介<br/><br/>
&emsp;&emsp;list命令 支持以下命令行参数：<br/>
&emsp;&emsp;&emsp;&emsp;-log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径<br/>
&emsp;&emsp;&emsp;&emsp;-out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径<br/>
&emsp;&emsp;&emsp;&emsp;-date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件<br/>
&emsp;&emsp;&emsp;&emsp;-type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。<br/>
&emsp;&emsp;&emsp;&emsp;-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江<br/>
     
    

- 作业链接<br/><br/>
&emsp;&emsp;[https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)<br/><br/>
- 博客链接<br/><br/>
&emsp;&emsp;[https://www.cnblogs.com/bzzd/p/12310813.html](https://www.cnblogs.com/bzzd/p/12310813.html)<br/><br/>
