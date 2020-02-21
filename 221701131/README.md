# InfectStatistic-221701131
疫情统计

描述你的项目，包括如何运行、功能简介、作业链接、博客链接等
> [github地址/作业链接](https://github.com/SpringAlex/InfectStatistic-main)
> [博客链接]()
#### 如何运行
在命令行输入 命令 list 运行。统计路径下的文件信息。

#### 功能简介

list命令 支持以下命令行参数：

-log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
-out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
-date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
-type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
注：java InfectStatistic表示执行主类InfectStatistic，list为命令，-date代表该命令附带的参数，-date后边跟着具体的参数值，如2020-01-22。-type 的多个参数值会用空格分离，每个命令参数都在上方给出了描述，每个命令都会携带一到多个命令参数
#### 项目描述

统计网站每天都会提供一个对应的日志文本，记录国内各省前一天的感染情况，本项目使用java作为编码程序,对疫情进行统计，使用方法和功能在上文介绍了，下面介绍函数实现和各个参数列表。
##### 函数描述
| 函数名          | 对应功能                         |
| --------------- | -------------------------------- |
| LIb             | 存放关键数据                     |
| ParameterOption | 参数处理（读取并处理命令行数据） |
| Reader          | 读取相关文件                     |
| DataMessage     | 处理文件信息                     |

##### 参数表

| 类型                   | 名称        | 作用                     |
| ---------------------- | ----------- | ------------------------ |
| 字符串数组             | commands    | 记录命令名称             |
| 字符串数组             | types       | 记录类型名称             |
| 字符串数组             | outTypes    | 记录输出                 |
| 字符串数组             | provinces   | 记录按顺序记录省份       |
| 字符串                 | lastDayFlag | 表示当前日期为最新的一天 |
| 上述的为Lib            |             | 目录类                   |
| **InfectStatistic**    |             | 主类                     |
| 字符串数组             | Args        | 命令行参数               |
| **ParameterOption**    |             | 命令行参数处理类         |
| 字符串数组             | myArgs      | 记录命令行参数           |
| Lib类                  | lib         | 持有lib类对象            |
| 字符串                 | log         | 日志文件的路径           |
| 字符串                 | out         | 输出文件的路径           |
| 字符串                 | date        | 疫情统计日期             |
| int类型数组            | province    | 省份列表                 |
| int类型数组            | type        | 类型数组                 |
| **Reader**             |             |                          |
| 字符串                 | filePath    | 读取文件的路径           |
| 字符串                 | lastDate    | 统计日期                 |
| 字符串数组             | fileLists   | 所有文件列表             |
| ArrayList<String>      | files       | 需要读取的文件列表       |
| Lib类                  | lib         | 持有lib类对象            |
| **DateMessage**        |             | 处理省份信息             |
| 字符串                 | province    | 省份名                   |
| 整形                   | ip          | 感染患者                 |
| 整形                   | sp          | 疑似患者                 |
| 整形                   | cure        | 治愈                     |
| 整形                   | dead        | 死亡                     |
| **DateProcess**        |             | 对文件数据进行处理       |
| 字符串                 | outPath     | 输出文件路径             |
| ArrayList<DateMessage> | dm          | 各个省份的信息           |
| ArrayList<String>      | files       | 需要读取的文件列表       |
| 整形数组               | types       | 类型                     |
| Lib                    | lib         | 持有lib对象              |
| boolean                | all         | 是否包含全国数据         |
| DateMessage            | allDm       | 全国数据的信息           |
| 字符串                 | line        | 表示读取的数据行         |