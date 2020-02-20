# InfectStatistic-081700430
# 疫情统计
## 关于项目
#### 最近新型冠状病毒疫情严重，全国人民都感到担忧，迫切希望能够及时了解到病毒最新的情况，
#### 作为IT学子，大家请你帮忙开发一个疫情统计程序。

## 运行
在命令行下编译后使用java InfectStatistic list -date 2020-01-22 -log -out -type -province
-log 指定日志目录的位置，该项必会附带。

-out 指定输出文件路径和文件名，该项必会附带。

-date 指定日期，不设置则默认为所提供日志最新的一天。

-type 可选择[ip： 感染患者，sp：疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。

-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

## 功能简介
- 功能:假设有一家统计网站每天都会提供一个对应的日志文本，记录国内各省前一天的感染情况，用户输入命令可以处理日志数据并得到统计好的数据。

## 作业链接 
### [软工实践寒假作业（2/2）](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)

## 博客链接
### [点击进入我的博客](https://www.cnblogs.com/kylin773/p/12287008.html)
