# InfectStatistic-221701331
疫情统计的小脚本

- 运行方式  
```
cd 221701331/src/
python InfectStatistic.py
```
list命令支持以下参数：  
`-log` 指定日志目录的位置【必填】  
`-out` 指定输出文件路径和文件名【必填】  
`-date` 指定日期，不设置则默认为所提供日志最新的一天  
`-type` 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。  
`-province` 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江  

- 作业链接：https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287

- 博客链接：https://www.cnblogs.com/llddcc/p/12341703.html