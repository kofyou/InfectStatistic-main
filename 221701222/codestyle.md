## 代码风格

### 缩进

用4个空格来缩进代码,绝对不要用tab,也不要tab和空格混用,对于行连接要么垂直对齐换行元素,要么使用4个空格的悬挂式缩进‍‍

### 变量命名

1. 代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束
2. 代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式
3. 参数名，成员变量，局部变量采用aaaBbbCcc风格，也是遵从驼峰形式
4. 常量命名全部大写，单词用下划线间隔开，语义要表达清楚，比如MAX_VALUE
5. 枚举类名建议带上Enum后缀，枚举成员名称需要全大写，单词间用下划线隔开
6. 布尔类型的变量，都不要加is

### 每行最多字符数

每行最多不要尽量超过80个字符数，有时也可以是100以内

### 函数最大行数

尽量控制在100行以内，简洁明了

### 函数、类命名

1. 类名采用AaaBbbCcc风格，遵循驼峰形式
2. 函数名采用aaaBbbCcc风格，也是遵从驼峰形式

### 常量

常量命名全部大写，单词用下划线间隔开，语义要表达清楚，比如MAX_VALUE

### 空行规则

1. 规则一：定义变量后要空行。尽可能在定义变量的同时初始化该变量，即遵循就近原则。如果变量的引用和定义相隔比较远，那么变量的初始化就很容易被忘记。若引用了未被初始化的变量，就会导致程序出错。

2. 规则二：每个函数定义结束之后都要加空行。

3. 总规则：两个相对独立的程序块、变量说明之后必须要加空行。比如上面几行代码完成的是一个功能，下面几行代码完成的是另一个功能，那么它们中间就要加空行。这样看起来更清晰

### 注释规则

1. 类、类属性、类方法的注释必须使用 Javadoc 规范，使用/**内容*/格式，不得使用
   // xxx 方式。
2. 功能性，类和函数，接口等都要具体规定功能，参数，返回值等
3. 方法内部单行注释，在被注释语句上方另起一行，使用//注释。方法内部多行注释
   使用/ /注释，注意与代码对齐

### 操作符前后空格

通常运算符 ( = + - * / ) 前后需要添加空格

## 语句规则

简单语句的通用规则:

- 一条语句通常以分号作为结束符

## 对象规则

对象定义规则:

- 将左花括号与类名放在同一行。
- 冒号与属性值间有个空格。
- 字符串使用双引号，数字不需要。
- 最后一个属性-值对后面不要添加逗号。
- 将右花括号独立放在一行，并以分号作为结束符号


