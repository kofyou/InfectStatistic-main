# InfectionStatistics-221701415
为了方便疫情统计,特开发次小工具,能够快速解析系统产生的(口胡)日志文件
### 快速开始
下载src下的InfectionStatistics.java和Lib.java,使用  
```
javac InfectionStatistics.java
```
来编译源文件,然后使用
```
java InfectionStatistics list
```
来运行软件

### 参数介绍
- -log 指定日志文件的**目录**, 日志文件名必须为`20xx-xx-xx.log.txt`, 若不为此格式可自行修改源代码, 此项必须
- -out 指定输出**文件**, 文件名可自行决定, 此项必须
- -date 指定日志的日期, 会自动处理该日期及之前的所有日志文件, 格式为`20xx-xx-xx`
- -type 指定输出时的格式,可选`ip 感染患者`, `sp 疑似患者`, `cure 治愈`, `dead 死亡`. 此项可选, 不提供则默认打印全部4种
- -province 指定输出省份, 中间用空格分隔. 输出文件中全国情况为第一行,其余省份按拼音排列. 此项为可选, 不提供则默认打印全部省份

### 作业连接
[作业](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)

### 博客连接
[博客](https://www.cnblogs.com/lunacia/p/12303922.html)
