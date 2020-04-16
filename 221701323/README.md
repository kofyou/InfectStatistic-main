# InfectStatistic-学号
##疫情统计

###项目简介：
简单的Java程序统计分析指定日志文件疫情数据。，并将结果输出到指定的文件中。
日志文件可能如下：

>福建 新增 感染患者 23人
福建 新增 疑似患者 2人
浙江 感染患者 流入 福建 12人
湖北 疑似患者 流入 福建 2人
安徽 死亡 2人
新疆 治愈 3人
福建 疑似患者 确诊感染 2人
新疆 排除 疑似患者 5人
// 该文档并非真实数据，仅供测试使用

输出文件如下：
>全国 感染患者22人 疑似患者25人 治愈10人 死亡2人
福建 感染患者2人 疑似患者5人 治愈0人 死亡0人
浙江 感染患者3人 疑似患者5人 治愈2人 死亡1人
// 该文档并非真实数据，仅供测试使用

###运行
- 命令行（win+r cmd）cd到项目src下，之后输入命令进行编译，如图：
![编译图片](https://images.cnblogs.com/cnblogs_com/zhixinlin/1649880/o_200218095826%E7%A8%8B%E5%BA%8F%E7%BC%96%E8%AF%91%E6%88%AA%E5%9B%BE.png)

- 运行程序，如图：
![运行程序](https://images.cnblogs.com/cnblogs_com/zhixinlin/1649880/o_200218095833%E8%BF%90%E8%A1%8C%E6%88%AA%E5%9B%BE.png)

- 运行结果，如图：
![结果](https://images.cnblogs.com/cnblogs_com/zhixinlin/1649880/o_200218095801%E7%BB%93%E6%9E%9C%E6%88%AA%E5%9B%BE.png)


###功能简介

####-log
    指定日志文件存放的位置，该项必须有；
    例如：`-log D:/log`
####-out
    指定输出文件的位置，该项必须有：
    例如：`-out D:/result/out.txt`
####-date
    指定查询的日期，若无提供则默认为当天。指定查询日期为何时。参数唯一。
####-type
+ ip (infection patients) : 感染患者
+ sp( suspected patients ) : 疑似患者
+ cure: 治愈
+ dead: 死亡患者可以有多个参数。
    指定要查询的类型，默认为：四种类型都有。

####-province
    指定要查询的省份，默认包括全国各省。可以有多个参数。
    指定列出的省 如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
作业链接
[作业链接](https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287)
博客链接
[博客链接](https://www.cnblogs.com/zhixinlin/p/12341121.html)

