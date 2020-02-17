# InfectStatistic-221701407
##疫情统计

###如何运行
> * 在Github上fork项目到自己的仓库。

> * 打开命令提示符（win+r,输入cmd+回车），cd到本项目src下。
<div><img src="https://images.cnblogs.com/cnblogs_com/lyxblogaxi/1638320/o_2002171214591.PNG" width=80%/></div>

> * 编译文件（输入javac -encoding UTF-8 InfectStatistic.java）无报错则表示编译成功。
<div><img src="https://images.cnblogs.com/cnblogs_com/lyxblogaxi/1638320/o_2002171215112.PNG" width=80%/></div>

> * 根据自己的需要输入对应命令行运行程序

####本程序使用命令list，支持以下五个对应参数：
> -log（必须包含）：指定日志目录的位置。例如：-log ../log/
> -out（必须包含）：指定输出文件路径和文件名。例如：-out ../result/out.txt
> -date（可无）：指定日期，不设置则默认为所提供日志最新的一天。例如：-date 2020-01-22，若不设置则默认-date参数为提供日志最新的一天。
> -province（可以有多个参数）：指定列出的省。例如：-province 福建 湖北。
> -type（可以有多个参数）：可选择[ip:infection patients 感染患者，sp:suspected patients疑似患者，cure:治愈，dead:死亡患者]，使用缩写选择。例如：-type ip sp

综合举例说明：输入java -Dfile.encoding=UTF-8 InfectStatistic -date 2020-01-22 -province 福建 湖北 -type ip cure -log ../log/ -out ../result/out.txt
<div><img src="https://images.cnblogs.com/cnblogs_com/lyxblogaxi/1638320/o_2002171215173.PNG" width=80%/></div>
就会在对应的目录下生成输出文件，文件内容如下
<div><img src="https://images.cnblogs.com/cnblogs_com/lyxblogaxi/1638320/o_2002171215304.PNG" width=40%/></div>

###作业链接
[https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)
###博客链接
[https://www.cnblogs.com/lyxblogaxi/p/12287153.html](https://www.cnblogs.com/lyxblogaxi/p/12287153.html)



