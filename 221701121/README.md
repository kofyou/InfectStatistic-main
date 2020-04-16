

# InfectStatistic-2217011221

疫情统计
描述你的项目，包括如何运行、功能简介、作业链接、博客链接等

------
##项目具体描述
&emsp;&emsp;程序能够列出全国和各省在某日的感染情况，并将结果输出到指定文件下。

##如何运行
&emsp;&emsp;命令行cd到项目src下，之后输入命令运行程序，像$java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt会会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）

##功能简介
**list命令 支持以下命令行参数**
> * **-log** 指定日志目录的位置，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
> * **-out** 指定输出文件路径和文件名，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
> * **-date** 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件<br/>
> * **-type** 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 **-type ip** 表示只列出感染患者的情况，**-type sp cure**则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。<br/>
> * **-province** 指定列出的省，如**-province 福建**，则只列出福建，**-province 全国 浙江**则只会列出全国、浙江<br/>


##作业链接
[软工实践寒假作业（2/2） ](https://www.cnblogs.com/shenmw/p/12333049.html)

##博客链接
[我的博客](https://www.cnblogs.com/shenmw/)
欢迎访问