# InfectStatistic

针对统计网站**每天**提供的**一个**对应的日志文本，记录国内各省前一天的感染情况，如它 2020-01-23 发布文件名为 [2020-01-23.log.txt](https://github.com/xjliang/InfectStatistic-main/blob/master/221701107/log/2020-01-23.log.txt) 的日志文本。

本程序会根据指定的命令行参数列出全国和各省在某日的感染情况。

## 如何运行

命令行（win+r cmd）cd 到项目 src 下，之后输入命令：

```shell
$ java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
```

## 功能简介

list 命令 支持以下命令行参数：

- `-log` 指定日志目录的位置，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
- `-out` 指定输出文件路径和文件名，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
- `-date` 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有 log 文件
- `-type` 可选择 [ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 `-type ip` 表示只列出感染患者的情况，`-type sp cure` 则会按顺序[sp, cure] 列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
- `-province` 指定列出的省，如 `-province 福建`，则只列出福建，`-province 全国 浙江`则只会列出全国、浙江

注：java InfectStatistic 表示执行主类 InfectStatistic，list 为**命令**，-date 代表该命令附带的**参数**，-date 后边跟着具体的**参数值**，如 `2020-01-22`。-type 的多个参数值会用空格分离，每个命令参数都在上方给出了描述，每个命令都会携带一到多个命令参数

## 作业链接

https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287

## 博客链接

https://www.cnblogs.com/gnulxj/p/12322336.html