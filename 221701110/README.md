本次作业用得是Java语言完成。  
在黑窗口进入InfectStatistic.所在目录下java目录下，  
先输入命令javac -encoding UTF-8 InfectStatistic.java后编译成功  
再输入类似指令java InfectStatistic list -date 2020-01-22 -log  
 D:/log/ -out D:/output.txt便可运行。  
 其中“java InfectStatistic”， ”list“，”log“，”out“必有，province，date，  
 type可选。  
 -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径  
 -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径  
 -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件  
 -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：  
 死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】  
 列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。  
 -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江  
 注：java InfectStatistic表示执行主类InfectStatistic，list为命令，-date代表该命令附带的参数，  
 -date后边跟着具体的参数值，如2020-01-22。-type 的多个参数值会用空格分离，每个命令参数都在上方给  
出了描述，每个命令都会携带一到多个命令参数
