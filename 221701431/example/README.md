##InfectStatistic-221701432
疫情统计

通过读取日志的方式，统计截至到某一天各地感染情况
- 项目链接：https://github.com/BeJame/InfectStatistic-main
- 博客链接：https://www.cnblogs.com/bejame/p/12328051.html
- 作业链接：https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287


##日志要求
日志文件的命名遵守对应的规范： 年-月-日.log.txt ，如2020-01-22.log.txt

日志内容可以有以下几种情况

```
1、<省> 新增 感染患者 n人
2、<省> 新增 疑似患者 n人
3、<省1> 感染患者 流入 <省2> n人
4、<省1> 疑似患者 流入 <省2> n人
5、<省> 死亡 n人
6、<省> 治愈 n人
7、<省> 疑似患者 确诊感染 n人
8、<省> 排除 疑似患者 n人
```

如：
```
福建 新增 感染患者 23人
福建 新增 疑似患者 2人
浙江 感染患者 流入 福建 12人
湖北 疑似患者 流入 福建 2人
安徽 死亡 2人
新疆 治愈 3人
福建 疑似患者 确诊感染 2人
新疆 排除 疑似患者 5人
// 该文档并非真实数据，仅供测试使用
```

##运行方式
在src目录下运行
```
python InfectStatistic.py list -out 输出目录 -log 日志目录
```

- -log 指定日志目录的位置
- -out 指定输出文件路径和文件名
- -date 指定日期，可选项，不设置则默认为所提供日志最新的一天
- -type 可选择
ip： infection patients 感染患者，
sp： suspected patients 疑似患者，
cure：治愈
dead：死亡患者
使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况
不指定该项默认会列出所有情况。
- -province 可选项，指定列出的省
