>  编程代码规范

 * 缩进：
  统一缩进4格大小

 * 命名:
  1.使用纯英文单词
  2.多个单词用下划线隔开,如student_num,find_max_num() 
  3.常量,宏,模板,枚举类型常量采取全大写形式
  4.除循环变量外不用单字母作为变量名
  5.类和函数命名首字母大写
  6.代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束。
  7.代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式。
  8.常量命名全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。
  9.中括号是数组类型的一部分，数组定义如下：String[] args;
 10.方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。

* 每行最多字符数:80个

* 函数编写规则:
  1.函数的行数尽量限制在100行以内
  2.一个函数完成一个功能
  3.禁止编写的函数依赖于其他函数内部所实现的功能
  4.尽量重写类的构造函数

* 空行:每个函数,类,结构体,以及某些程序块之间空一行以表示分离关系

* 注释规则:
  1.在源文件头部应该列出:生成日期,作者,版权,代码功能/目的等信息
  2.应该函数程序块前编写注释表明函数功能以及一些必要信息
  3.重要变量定义需编写注释
  4.类、类属性、类方法的注释必须使用Javadoc规范，使用/*内容/格式，不得使用//xxx方式。

* 操作符前后空格:
  1.值操作符、比较操作符、算术操作符、逻辑操作符、位域操作符，如“ =”、“ +=”
  “ >=”、“ <=”、“ +”、“ *”、“ %”、“ &&”、“ ||”、“ <<” 、“ ^” 等二元操作符
 的前后应当加空格
  2.一元操作符如“ !”、“ ~”、“ ++”、“ --”、“ &”（ 地址运算符） 等前后不加空格
  3.像“［ ］”、“ .”、“ ->” 这类操作符前后不加空格。
  4.大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行；如果是非空代码块则：
    左大括号前不换行。
    左大括号后换行。
    右大括号前换行。
    右大括号后还有else等代码则不换行；表示终止右大括号后必须换行。

* 其他规则:
  1.尽量使用const,避免使用宏
  2.尽可能局部声明变量