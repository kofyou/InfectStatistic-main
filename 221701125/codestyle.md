## 代码风格

- 缩进

  > ​		缩进采用4个空格，禁止使用tab字符。如果使用tab缩进，必须设置1个tab为4个空格。IDEA设置tab为4个空格时，请勿勾选Use tab character；而在eclipse中，必须勾选insert spaces for tabs。
  >
  > 

- 变量命名

  > 代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束。
  >
  > > ```
  > > 反例： _name / __name / $Object / name_ / name$ / Object$
  > > ```
  >
  > 代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式。
  >
  > > ```
  > > 反例： DaZhePromotion [打折] / getPingfenByName()  [评分] / int某变量 = 3
  > > 正例： alibaba / taobao / youku / hangzhou等国际通用的名称，可视同英文。
  > > ```
  >
  > 方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
  >
  > > 正例： localValue / getHttpMessage() /  inputUserId
  >
  > 中括号是数组类型的一部分，数组定义如下：String[] args;
  >
  > > ```
  > > 反例：请勿使用String  args[]的方式来定义。
  > > ```
  >
  > 包名统一使用小写，点分隔符之间有且仅有一个自然语义的英语单词。包名统一使用单数形式，但是类名如果有复数含义，类名可以使用复数形式。
  >
  > > ```
  > > 正例：应用工具类包名为com.alibaba.open.util、类名为MessageUtils（此规则参考spring的框架结构）
  > > ```
  >
  > 如果是形容能力的接口名称，取对应的形容词做接口名（通常是–able的形式）。
  >
  > > ```
  > > 正例：AbstractTranslator实现 Translatable。
  > > ```
  >
  > 枚举类名建议带上Enum后缀，枚举成员名称需要全大写，单词间用下划线隔开。
  >
  > > ```
  > > 正例：枚举名字：DealStatusEnum，成员名称：SUCCESS / UNKNOWN_REASON。
  > > ```

- 每行最多字符数

  > 单行字符数限制不超过 120个，超出需要换行，换行时遵循如下原则：
  >
  >      		1. 第二行相对第一行缩进 4个空格，从第三行开始，不再继续缩进
  >      		2. 运算符与下文一起换行。
  >      		3. 方法调用的点符号与下文一起换行。
  >      		4. 在多个参数超长，逗号后进行换行。
  >      		5. 在括号前不要换行
  >
  > > 正例：
  > > StringBuffer sb = new StringBuffer();
  > > //超过120个字符的情况下，换行缩进4个空格，并且方法前的点符号一起换行
  > > sb.append("zi").append("xin")...
  > > 	.append("huang")...
  > > 	.append("huang")...
  > > 	.append("huang");
  > > 反例：
  > > StringBuffer sb = new StringBuffer();
  > > //超过120个字符的情况下，不要在括号前换行
  > > sb.append("zi").append("xin")...append
  > > 	("huang");
  > > //参数很多的方法调用可能超过120个字符，不要在逗号前换行
  > > method(args1, args2, args3, ...
  > > 	, argsX);

- 函数最大行数

  > 函数最大30行，若超过30行，应该考虑拆分为多个函数

- 函数、类命名

  > 类名使用UpperCamelCase风格，必须遵从驼峰形式，但以下情形例外：（领域模型的相关命名）DO / BO / DTO / VO等。
  >
  > > 正例：MarcoPolo / UserDO / XmlService / TcpUdpDeal /   TaPromotion
  > > 反例：macroPolo / UserDo / XMLService / TCPUDPDeal /   TAPromotion
  >
  > 函数命名使用lowerCamelCase风格
  >
  > 抽象类命名使用Abstract或Base开头；异常类命名使用Exception结尾；测试类命名以它要测试的类的名称开始，以Test结尾。

- 常量

  > 常量命名全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。
  >
  > > 正例： MAX_STOCK_COUNT
  > > 反例： MAX_COUNT
  >
  > long或者Long初始赋值时，必须使用大写的L，不能是小写的l，小写容易跟数字1混淆，造成误解。
  >
  > > 说明：Long a = 2l;写的是数字的21，还是Long型的2?
  >
  > 不要使用一个常量类维护所有常量，应该按常量功能进行归类，分开维护。如：缓存相关的常量放在类：CacheConsts下；系统配置相关的常量放在类：ConfigConsts下。
  >
  > 常量的复用层次有五层：跨应用共享常量、应用内共享常量、子工程内共享常量、包内共享常量、类内共享常量。
  >
  > > 跨应用共享常量：放置在二方库中，通常是client.jar中的constant目录下。
  > > 应用内共享常量：放置在一方库的modules中的constant目录下。
  > > 子工程内部共享常量：即在当前子工程的constant目录下。
  > > 包内共享常量：即在当前包下单独的constant目录下。
  > > 类内共享常量：直接在类内部private static final定义。

- 空行规则

  > 方法体内的执行语句组、变量的定义语句组、不同的业务逻辑之间或者不同的语义之间插入一个空行。相同业务逻辑和语义之间不需要插入空行。

- 注释规则

  > 与其“半吊子”英文来注释，不如用中文注释把问题说清楚。专有名词与关键字保持英文原文即可。
  >
  > 类、类属性、类方法要使用注释说明作用；get、set方法可不注释
  >
  > 所有的枚举类型字段必须要有注释，说明每个数据项的用途。
  >
  > 注释掉的代码尽量要配合说明，而不是简单的注释掉。
  >
  > 对于注释的要求：
  >
  > > 第一、能够准确反应设计思想和代码逻辑；
  > > 第二、能够描述业务含义，使别的程序员能够迅速了解到代码背后的信息。完全没有注释的大段代码对于阅读者形同天书，注释是给自己看的，即使隔很长时间，也能清晰理解当时的思路；注释也是给继任者看的，使其能够快速接替自己的工作。
  >
  > 好的命名、代码结构是自解释的，注释力求精简准确、表达到位。避免出现注释的一个极端：过多过滥的注释，代码的逻辑一旦修改，修改注释是相当大的负担。

- 操作符前后空格

  > 赋值运算符=、逻辑运算符&&、三目运行符左右必须加一个空格。

- 控制语句

  > 在一个switch块内，每个case要么通过break/return等来终止，要么注释说明程序将继续执行到哪一个case为止；在一个switch块内，都必须包含一个default语句并且放在最后，即使它什么代码也没有。
  >
  > 在if/else/for/while/do语句中必须使用大括号，即使只有一行代码，避免使用下面的形式：if (condition) statements;
  >
  > 循环体中的语句要考量性能，以下操作尽量移至循环体外处理，如定义对象、变量、获取数据库连接，进行不必要的try-catch操作（这个try-catch是否可以移至循环体外）。

- 其他规则

  > 大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行；如果是非空代码块则：
  >
  > > 表示函数开始、循环开始、条件语句、try、catch开始的左大括号前换行。
  > > 表示函数开始、循环开始、条件语句、try、catch开始的左大括号后换行。
  > > 表示函数开始、循环开始、条件语句、try、catch结束的右大括号前换行。
  > > 表示函数开始、循环开始、条件语句、try、catch结束的右大括号后换行。
  >
  > if/for/while/switch/do等保留字与左括号之间都必须加空格。
  >
  > 避免通过一个类的对象引用访问此类的静态变量或静态方法，无谓增加编译器解析成本，直接用类名来访问即可。
  >
  > 当一个类有多个构造方法，或者多个同名方法，这些方法应该按顺序放置在一起，便于阅读。
  >
  > 使用entrySet遍历Map类集合KV，而不是keySet方式进行遍历。
  >