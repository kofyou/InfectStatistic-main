# InfectStatistic-221701128
疫情统计

1.项目简介：该项目会通过分析统计疫情日志文件，将统计信息输出到指定的文件中，用以了解全国各省的疫情情况。

2.运行：
> 进入到该项目的src下进行cmd操作。
1.若没有进行第一次编译，请输入javac InfectStatistic.java -encoding UTF-8
2.之后再输入java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt 这样就会输出2020-01-22日的疫情情况

3.功能简介：
> 含有命令list，list会列出疫情情况list拥有5个参数： 
1. -log 必会附带，指定日志目录的位置
2. -out 必会附带，指定输出文件路径和文件名
3. -date 指定日期，不设置则默认为所提供日志最新的一天的疫情情况，格式按xxxx-xx-xx
4. -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择
5. -province 指定列出的省 如：-province 全国 浙江则只会列出全国、浙江，不带参数值就列出全国加日志涉及到的省份

4.作业链接：https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287#1

5.博客链接：https://www.cnblogs.com/bkmemory/p/12326568.html