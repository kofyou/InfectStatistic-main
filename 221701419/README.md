# InfectStatistic-221701419
## 项目简介

> 一个简单的Java疫情统计程序，能够对指定格式的txt文本中的各省份的疫情数据进行统计，并将结果输出。
>
> 输入文本示例：
>
> ```txt
> 福建 新增 感染患者 3人
> 福建 新增 疑似患者 6人
> 湖北 新增 感染患者 25人
> 湖北 新增 疑似患者 39人
> 湖北 感染患者 流入 福建 2人
> 湖北 疑似患者 流入 福建 5人
> 湖北 死亡 2人
> 湖北 治愈 1人
> 湖北 疑似患者 确诊感染 3人
> 福建 排除 疑似患者 2人
> 福建 治愈 1人
> // 该文档并非真实数据，仅供测试使用
> ```
>
> 输出文本示例：
>
> ```txt
> 全国 感染患者15人 疑似患者22人 治愈2人 死亡1人
> 福建 感染患者5人 疑似患者7人 治愈0人 死亡0人
> 湖北 感染患者10人 疑似患者15人 治愈2人 死亡1人
> // 该文档并非真实数据，仅供测试使用
> // 命令：list -log D:\log\ -out D:\ListOut1.txt -date 2020-01-22
> ```

# 如何运行

在src目录下打开命令提示符

![命令行提示符](https://images.cnblogs.com/cnblogs_com/hhhqqq/1646733/o_200214013938%E5%91%BD%E4%BB%A4%E8%A1%8C%E6%8F%90%E7%A4%BA%E7%AC%A6.png)

进入之后编译文件

![编译文件](https://images.cnblogs.com/cnblogs_com/hhhqqq/1646733/o_200214013744%E7%BC%96%E8%AF%91%E6%96%87%E4%BB%B6.png)

然后运行程序

![运行](https://images.cnblogs.com/cnblogs_com/hhhqqq/1646733/o_200214013758%E8%BF%90%E8%A1%8C.png)

# 命令行说明

```java
示例：
Java InfectStatistic list -log D:/log -out D:/result/ListOut3.txt -date 2020-01-22 -type sp -province 福建
```

## -log（必须指定）

>  指定日志目录的位置，示例：-log D:/log，里面的文件命名如下
>
> ![log](https://images.cnblogs.com/cnblogs_com/hhhqqq/1646733/o_200214013738log.png)

## -out（必须指定）

> 指定输出文件路径和文件名 

## -date（可无）

> 指定日期，不设置则默认为所提供日志最新的一天 ，程序会统计该日期以及该日期前的log文件中的数据

## -type（可无）

>可选参数：
>
>* **ip** (infection patients) : 感染患者 
>* **sp**( suspected patients ) :  疑似患者 
>* **cure**:  治愈 
>* **dead**:  死亡患者 
>
>示例：
>
>```txt
>-type sp
>福建 疑似患者3人
>
>-type sp ip
>福建 疑似患者3人 感染患者1人
>
>-type cure ip sp
>福建 治愈1人 感染患者3人 疑似患者3人
>
>//不指定-type类型，即命令行中没有"-type"参数名，也没有参数值，则按ip sp cure dead默认输出
>//如：list -log D:/log -out D:/result/ListOut3.txt -date 2020-01-22 -province 福建
>福建 感染患者5人 疑似患者7人 治愈0人 死亡0人
>
>/*特别说明：以上数据非真实数据，仅供示例用*/
>```

## -province（可无）

> 指定列出的省  如`-province 福建`，则只列出福建，`-province 全国 浙江`则只会列出全国、浙江 ，省份均按首字母排序输出，与参数指定顺序无关

# 作业链接

> <a href=" https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287#4 " target="_blank"> https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287#4 </a> 

# 博客链接

> <a href="https://www.cnblogs.com/hhhqqq/p/12306200.html" target="_blank"> https://www.cnblogs.com/hhhqqq/p/12306200.html </a>
