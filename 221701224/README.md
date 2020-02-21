## 疫情统计程序
### 项目简介
> 该程序可以统计指定目录下的日志文件，根据你的需求，输出相应的各省的各种患者的感染情况到你指定的文件目录下
### 如何运行
命令行（win+r cmd）cd到项目src下，之后输入命令，举例如下：
`$ java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt`
会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出
> 注：
> -log 指定日志目录的位置，该项必须指定
> -out 指定输出文件路径和文件名，该项指定
> -date 指定日期，不设置则默认为所提供日志最新的一天。
> type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
> -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
### [作业链接](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)
### [博客链接](https://www.cnblogs.com/esinSha/p/12291388.html)
