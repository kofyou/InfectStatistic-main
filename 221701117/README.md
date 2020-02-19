# InfectStatistic-221701117
疫情统计

|这个作业属于哪个课程|[福大20春软工S班](https://edu.cnblogs.com/campus/fzu/2020SPRINGS)|
|:--:    |:--:    |
|这个作业要求在哪里|[软工实践寒假作业（2/2）](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)|
|这个作业的目标|新型冠状病毒疫情统计|
|作业正文|[221701117余嘉宸作业（2/2）](https://www.cnblogs.com/yjchen/p/12298469.html)|
|其他参考文献|《Java程序设计》 |
***
**InfectStatistic-疫情统计**
### 1. 项目描述
&emsp;&emsp;最近新型冠状病毒疫情严重，全国人民都感到担忧，迫切希望能够及时了解到病毒最新的情况，要求开发一个疫情统计程序。该程序实现读取日志文件并输出要求输出的省份的疑似患者人数,感染患者人数,治愈人数,死亡人数。  
&emsp;&emsp;同时，希望武汉小朋友早日克服困难，青山一道同风雨，明月何曾是两乡。武汉加油！  
### 2. 如何运行
&emsp;&emsp;下载并用cmd打开(cd)到221701117/src文件下，根据用户的需求输入代码。例如，  
```
java InfectStatistic list -log G:\\java\\eclipse\\eclipse-workspace\\hw2_2\\src\\ -out G:\\java\\eclipse\\eclipse-workspace\\hw2_2\\src\\output.txt -date 2020-02-22 -type ip sp dead -province 福建 全国 湖南
```
&emsp;&emsp;将在相应目录下生成output。txt输出结果。
<div><img width="100%" src="https://images.cnblogs.com/cnblogs_com/yjchen/1645851/o_200218091302%E6%96%87%E4%BB%B6.png"/></div>  

```
全国	感染患者152人	疑似患者290人	死亡22人	
福建	感染患者39人	疑似患者11人	死亡2人	
湖南	感染患者33人	疑似患者48人	死亡2人	
```
### 3. 功能简介 
list命令 支持以下命令行参数：

* -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
* -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
* -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
* -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
* -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

注：java InfectStatistic表示执行主类InfectStatistic，list为命令，-date代表该命令附带的参数，-date后边跟着具体的参数值，如2020-01-22。-type 的多个参数值会用空格分离，每个命令参数都在上方给出了描述，每个命令都会携带一到多个命令参数