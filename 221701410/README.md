# InfectStatistic-221701410
疫情统计
描述你的项目，包括如何运行、功能简介、作业链接、博客链接等

|这个作业属于哪个课程| [2020春S班 (福州大学)](https://edu.cnblogs.com/campus/fzu/2020SPRINGS) |
|--    |--    |
|这个作业要求在哪里| [软工实践寒假作业（2/2）](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287) |
|这个作业的目标|1.学习github使用<br /> 2.制定代码规范<br /> 3.学习PSP表格规划<br /> 4.对简单项目进行项目需求分析并编程实现<br /> 5.学习使用单元测试和性能测试工具 |
|作业正文| [hujh的软工实践寒假作业（2/2）](https://www.cnblogs.com/hujh/p/12318524.html) |
|其他参考文献|[《码出高效_阿里巴巴Java开发手册》](https://github.com/chjw8016/alibaba-java-style-guide) |

## 1. Github仓库地址
[https://github.com/hujh4779/InfectStatistic-main](https://github.com/hujh4779/InfectStatistic-main)

## 2. 开发工具
- java8
- IntelliJ IDEA 2019.3.2 x64

## 3. 功能简介
假设有一家统计网站每天都会提供一个对应的日志文本，记录国内各省前一天的感染情况，如它2020-01-23发布的日志文本可能长这样：
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
撰写程序，读取传入的log目录下的所有日志，来实现需求:命令行cd到项目src下，之后输入命令：
`$ java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt`
会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）。
