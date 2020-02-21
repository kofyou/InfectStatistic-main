## 代码风格

* 缩进  
1. 缩进采用4个空格，禁止使用tab字符
> 说明：如果使用tab缩进，必须设置1个tab为4个空格。IDEA设置tab为4个空格时，请勿勾选Use tab character；而在eclipse中，必须勾选insert spaces for tabs

* 变量命名
1. 代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束。
> 反例： _name / __name / $Object / name_ / name$ / Object$
2. 用小写英文表示，每个单词用_隔开
> 正例：log_list、log_route；反例：logout、logneed
3. 严禁使用拼音与英文混合的方式，更不允许直接使用拼音的方式
> 正例：log_list、log_route；反例：wenjian_liebiao、log_xuyao
* 每行最多字符数  
1. 单行最多字符数不得超过100
* 函数最大行数
1. 单个函数最大函数不得超过200，若超过则需对函数进行拆解，重新封装，规则如下：
> 每一个循环体可以封装成一个函数；每一个条件体可以封装为一个函数;
* 函数、类命名
1. 第一个单词首字母小写后面的单词首字母大写
> 正例：outputFile、judgeCommandLine；反例：GetLogList、IsLegalDate
2.  严禁使用拼音与英文混合的方式，更不允许直接使用拼音的方式
> 正例：outputFile、judgeCommandLine；反例：shuchuWenJian、judgeMingLing
* 常量
1. 常量名全部大写，单词间用_隔开
> 正例： MAX_STOCK_COUNT;反例： MAX_COUNT
* 空行规则
1. 函数间、类间空一行
* 注释规则
1. 如果在代码上方采用/\*内容\*/
> 例如：
> /\*
> \* 判断是否是合法日期
> \*/
> public static boolean isLegalDate(String str)
1. 如果跟在代码后面使用//
> 例如：for (int i=0;i<log_list.length-1;i++){//将获取到的文件名进行排序
* 操作符前后空格
1. 不设空格
