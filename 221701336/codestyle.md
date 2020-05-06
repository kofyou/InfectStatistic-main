## C++代码风格

* 缩进

>缩进空格数为4个

* **变量命名**

>参照匈牙利记法，即
[作用范围域前缀] + [前缀] + 基本类型 + 变量名
其中:
前缀是可选项，以小写字母表示;
基本类型是必选项，以小写字母表示;
变量名是必选项，可多个单词(或缩写)合在一起，每个单词首字母大写。

* **每行最多字符数**

>每行字符数要小于等于80个字符，过长时需进行合理划分新行

* **函数最大行数**

>一百行以内，尽量一个函数只干一件事

* **函数、类命名**

>函数名以大写字母开头。命名尽量以英语单词组合，采用动宾短语

* **常量**

>常量采用全大写方式，并尽可能在程序头部进行注释

* **空行规则**

>1.函数之间应该用空行分开<br />
2.变量声明应尽可能靠近第一次使用处，避免一次性声明一组没有马上使用的变量<br />
3.用空行将代码按照逻辑片断划分<br />
4.每个类声明之后应该加入空格同其他代码分开<br />

* **注释规则**

>1.源文件头部进行注释，列出：作者、模块目的/功能、版本信息等<br />
2.函数头部进行促使，列出：函数的功能、输入参数、输出参数、返回值等<br />
3.注释位于函数（注释内容）的上方，位于单行语句的右方<br />
4.数据结构声明注释功能与内容

* **操作符前后空格**

>1.像 if、for、while 等关键字之后应留一个空格再跟左括号‘（ ’， 以突出关键字<br /> 
2.函数名之后不要留空格， 紧跟左括号’(’ ， 以与关键字区别<br /> 
3.‘( ’ 向后紧跟，‘ )’、‘ ，’、‘ ;’ 向前紧跟，紧跟处不留空格<br /> 
4.‘ ,’ 之后要留空格，如 Function(x, y, z)。如果‘ ;’ 不是一行的结束符号，其
后也要留空格，如 for (initialization; condition; update)<br /> 
5.值操作符、比较操作符、算术操作符、逻辑操作符、位域操作符，如“ =”、“ +=” 
“ >=”、“ <=”、“ +”、“ *”、“ %”、“ &&”、“ ||”、“ <<” 、“ ^” 等二元操作符 
的前后应当加空格<br /> 
6.一元操作符如“ !”、“ ~”、“ ++”、“ --”、“ &”（ 地址运算符） 等前后不加 
空格<br /> 
7.象“［ ］”、“ .”、“ ->” 这类操作符前后不加空格<br />

* **其他规则**

>分支语句（条件分支 （条件分支、循环语句等 、循环语句等）需编写注释<br />

>不要编写太复杂 、多用途的复合表达式 多用途的复合表达式<br />

>用括号明确表达式的操作顺序，避免使用默认优先级 <br />



