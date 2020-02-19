# InfectStatistic-221701422
疫情统计

本项目使用HashMap作为存储的核心，基本的存储结构为HashMap<String, HashMap<String, Long>> provinceHashMap = new HashMap<String,HashMap<String, Long>>();
其中，以省份为key，每个省份下的病人情况HashMap<String, Long>为value，病人情况的HashMap又以病人种类为key，数量为value。

##如何运行

win+r  然后输入cmd
cd到src文件夹，
javac -encoding UTF-8 InfectStatistic.java
java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt

##功能简介

可以接受文件夹下的日志文件，并按要求处理并输出

##作业链接

[软工实践寒假作业（2/2）](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)

##博客链接

[我的软工实践寒假作业（2/2）](https://www.cnblogs.com/hjsblog/p/12330806.html)
