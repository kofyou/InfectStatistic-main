#JAVA编程规约
##（一）缩进
**【强制】**缩进采用4个空格，禁止使用tab字符。
> 说明：如果使用tab缩进，必须设置1个tab为4个空格。IDEA设置tab为4个空格时，请勿勾选Use tab character；而在eclipse中，必须勾选insert   spaces for tabs。

```
正例：（涉及1-5点）
public static void main(String args[]) {
	//缩进4个空格
	String say = "hello";
	//运算符的左右必须有一个空格
	int flag = 0;
	//关键词if与括号之间必须有一个空格，括号内的f与左括号，0与右括号不需要空格
	if (flag == 0) {
		System.out.println(say);
	}
	//左大括号前加空格且不换行；左大括号后换行
	if (flag == 1) {
		System.out.println("world");
		//右大括号前换行，右大括号后有else，不用换行
	} else {
		System.out.println("ok");
		//在右大括号后直接结束，则必须换行
	}
}
```

##（二）变量命名
1.**【强制】**代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束。
```
反例： _name / __name / $Object / name_ / name$ / Object$
```
2.**【强制】**代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式。
> 说明：正确的英文拼写和语法可以让阅读者易于理解，避免歧义。
> 注意，即使纯拼音命名方式也要避免采用。

```
反例： DaZhePromotion [打折] / getPingfenByName()  [评分] / int某变量 = 3
正例： alibaba / taobao / youku / hangzhou等国际通用的名称，可视同英文。
```
3.**【强制】**成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
```
正例： localValue / getHttpMessage() /  inputUserId
```
4.**【强制】**中括号是数组类型的一部分，数组定义如下：String[]   args;
```
反例：请勿使用String  args[]的方式来定义。
```
5.**【强制】**POJO类中布尔类型的变量，都不要加is，否则部分框架解析会引起序列化错误。
```
反例：定义为基本数据类型boolean isSuccess；的属性，
它的方法也是isSuccess()，RPC框架在反向解析的时候，“以为”对应的属性名称是success，导致属性获取不到，进而抛出异
常。
```
6.**【强制】**杜绝完全不规范的缩写，避免望文不知义。
```
反例： AbstractClass“缩写”命名成AbsClass；condition“缩写”命名成 condi，此类随意缩写严重降低了代码的可阅读性。
```

##（三）每行最多字符数
**【强制】**单行字符数限制不超过  120个，超出需要换行，换行时遵循如下原则：
* 第二行相对第一行缩进   4个空格，从第三行开始，不再继续缩进，参考示例。
* 运算符与下文一起换行。
* 方法调用的点符号与下文一起换行。
* 在多个参数超长，逗号后进行换行。
* 在括号前不要换行，见反例。
```
正例：
StringBuffer sb = new StringBuffer();
//超过120个字符的情况下，换行缩进4个空格，并且方法前的点符号一起换行
sb.append("zi").append("xin")...
	.append("huang")...
	.append("huang")...
	.append("huang");
反例：
StringBuffer sb = new StringBuffer();
//超过120个字符的情况下，不要在括号前换行
sb.append("zi").append("xin")...append
	("huang");
//参数很多的方法调用可能超过120个字符，不要在逗号前换行
method(args1, args2, args3, ...
	, argsX);
```

##（四）函数最大行数
**【强制】**一个函数的有效语句不超过100行，超出需要进行优化。有效语句不包括空行，单括号行，注释行等。

##（五）函数、类命名
1.**【强制】**代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束。
```
反例： _name / __name / $Object / name_ / name$ / Object$
```
2.**【强制】**代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式。
> 说明：正确的英文拼写和语法可以让阅读者易于理解，避免歧义。
> 注意，即使纯拼音命名方式也要避免采用。

```
反例： DaZhePromotion [打折] / getPingfenByName()  [评分] / int某变量 = 3
正例： alibaba / taobao / youku / hangzhou等国际通用的名称，可视同英文。
```

3.**【强制】**类名使用UpperCamelCase风格，必须遵从驼峰形式，但以下情形例外：（领域模型的相关命名）DO / BO / DTO / VO等。
```
正例：MarcoPolo / UserDO / XmlService / TcpUdpDeal /   TaPromotion
反例：macroPolo / UserDo / XMLService / TCPUDPDeal /   TAPromotion
```
4.**【强制】**方法名、参数名都统一使用lowerCamelCase风格，必须遵从驼峰形式。
```
正例： localValue / getHttpMessage() /  inputUserId
```
5.**【强制】**抽象类命名使用Abstract或Base开头；异常类命名使用Exception结尾；测试类命名以它要测试的类的名称开始，以Test结尾。
6.**【强制】**包名统一使用小写，点分隔符之间有且仅有一个自然语义的英语单词。包名统一使用单数形式，但是类名如果有复数含义，类名可以使用复数形式。
```
正例：应用工具类包名为com.alibaba.open.util、类名为MessageUtils（此规则参考spring的框架结构）
```
7.**【强制】**杜绝完全不规范的缩写，避免望文不知义。
```
反例： AbstractClass“缩写”命名成AbsClass；condition“缩写”命名成 condi，此类随意缩写严重降低了代码的可阅读性。
```
8.*【推荐】*如果使用到了设计模式，建议在类名中体现出具体模式。
> 说明：将设计模式体现在名字中，有利于阅读者快速理解架构设计思想。

```
正例：public class OrderFactory;
public class LoginProxy;
public class ResourceObserver;
```
9.*【推荐】*接口类中的方法和属性不要加任何修饰符号（public也不要加），保持代码的简洁性，并加上有效的Javadoc注释。尽量不要在接口里定义变量，如果一定要定义变量，肯定是与接口方法相关，并且是整个应用的基础常量。
```
正例：接口方法签名：void f();
接口基础常量表示：String COMPANY = "alibaba";
反例：接口方法定义：public abstract void f();
说明：JDK8中接口允许有默认实现，那么这个default方法，是对所有实现类都有价值的默认实现。
```
10.接口和实现类的命名有两套规则：
* **【强制】**对于Service和DAO类，基于SOA的理念，暴露出来的服务一定是接口，内部的实现类用Impl的后缀与接口区别。
```
正例：CacheServiceImpl实现CacheService接口。
```
* *【推荐】*如果是形容能力的接口名称，取对应的形容词做接口名（通常是–able的形式）。
```
正例：AbstractTranslator实现 Translatable。
```
11.【参考】枚举类名建议带上Enum后缀，枚举成员名称需要全大写，单词间用下划线隔开。
> 说明：枚举其实就是特殊的常量类，且构造方法被默认强制是私有。

```
正例：枚举名字：DealStatusEnum，成员名称：SUCCESS / UNKNOWN_REASON。
```
12.【参考】各层命名规约：

* ####Service/DAO层方法命名规约

1. 获取单个对象的方法用get做前缀。
2. 获取多个对象的方法用list做前缀。
3. 获取统计值的方法用count做前缀。
4. 插入的方法用save（推荐）或insert做前缀。
5. 删除的方法用remove（推荐）或delete做前缀。
6. 修改的方法用update做前缀。

##（六）常量
1.**【强制】**常量命名全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。
```
正例： MAX_STOCK_COUNT
反例： MAX_COUNT
```
2.**【强制】**不允许出现任何魔法值（即未经定义的常量）直接出现在代码中。
```
反例： String key="Id#taobao_"+tradeId；
cache.put(key,  value);
```
3.**【强制】**long或者Long初始赋值时，必须使用大写的L，不能是小写的l，小写容易跟数字1混淆，造成误解。
> 说明：Long a = 2l;写的是数字的21，还是Long型的2?

4.*【推荐】*如果变量值仅在一个范围内变化用Enum类。如果还带有名称之外的延伸属性，必须使用Enum类，下面正例中的数字就是延伸信息，表示星期几。
```
正例：publicEnum{MONDAY(1),TUESDAY(2),WEDNESDAY(3),THURSDAY(4),FRIDAY(5),SATURDAY(6), SUNDAY(7);}
```
##（七）空行规则
*【推荐】*方法体内的执行语句组、变量的定义语句组、不同的业务逻辑之间或者不同的语义之间插入一个空行。相同业务逻辑和语义之间不需要插入空行。
> 说明：没有必要插入多行空格进行隔开。

##（八）注释规则
1.**【强制】**类、类属性、类方法的注释必须使用Javadoc规范，使用/*内容/格式，不得使用//xxx方式。
>说明：在IDE编辑窗口中，Javadoc方式会提示相关注释，生成Javadoc可以正确输出相应注释；在IDE中，工程调用方法时，不进入方法即可悬浮提示方法、参数、返回值的意义，提高阅读效率。

2.**【强制】**方法内部单行注释，在被注释语句上方另起一行，使用//注释。方法内部多行注释使用/ */注释，注意与代码对齐。

3.**【强制】**所有的枚举类型字段必须要有注释，说明每个数据项的用途。

4.*【推荐】*代码修改的同时，注释也要进行相应的修改，尤其是参数、返回值、异常、核心逻辑等的修改。
>说明：代码与注释更新不同步，就像路网与导航软件更新不同步一样，如果导航软件严重滞后，就失去了导航的意义。

5.*【参考】*注释掉的代码尽量要配合说明，而不是简单的注释掉。
>说明：代码被注释掉有两种可能性：
1）后续会恢复此段代码逻辑。
2）永久不用。
前者如果没有备注信息，难以知晓注释动机。后者建议直接删掉（代码仓库保存了历史代码）。

6.**【强制】**所有的覆写方法，必须加@Override注解。
>反例：getObject()与get0bject()的问题。一个是字母的O，一个是数字的0，加@Override可以准确判断是否覆盖成功。
另外，如果在抽象类中对方法签名进行修改，其实现类会马上编译报错。

##（九）操作符前后空格
1.**【强制】**方法参数在定义和传入时，多个参数逗号后边必须加空格。
```
正例：下例中实参的"a",后边必须要有一个空格。
method("a", "b", "c");
```
2.**【强制】**任何运算符左右必须加一个空格。
> 说明：运算符包括赋值运算符=、逻辑运算符&&、加减乘除符号、三目运行符等。

3.**【强制】**大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行；如果是非空代码块则：
* 左大括号前不换行。
* 左大括号后换行。
* 右大括号前换行。
* 右大括号后还有else等代码则不换行；表示终止右大括号后必须换行。

4.**【强制】**左括号和后一个字符之间不出现空格；同样，右括号和前一个字符之间也不出现空格。
5.**【强制】**if/for/while/switch/do等保留字与左右括号之间都必须加空格。

```
正例：（涉及2-5点）
public static void main(String args[]) {
	String say = "hello";
	//运算符的左右必须有一个空格
	int flag = 0;
	//关键词if与括号之间必须有一个空格，括号内的f与左括号，0与右括号不需要空格
	if (flag == 0) {
		System.out.println(say);
	}
	//左大括号前加空格且不换行；左大括号后换行
	if (flag == 1) {
		System.out.println("world");
		//右大括号前换行，右大括号后有else，不用换行
	} else {
		System.out.println("ok");
		//在右大括号后直接结束，则必须换行
	}
}
```
##（十）其他规则
1.*【推荐】*没有必要增加若干空格来使某一行的字符与上一行的相应字符对齐。
```
正例：
int a = 3;
long b = 4L;
float c = 5F;
StringBuffer sb = new StringBuffer();
说明：增加sb这个变量，如果需要对齐，则给a、b、c都要增加几个空格，在变量比较多的
```
2.*【推荐】*当一个类有多个构造方法，或者多个同名方法，这些方法应该按顺序放置在一起，便于阅读。
3.*【推荐】*setter方法中，参数名称与类成员变量名称一致，this.成员名=参数名。在getter/setter方法中，尽量不要增加业务逻辑，增加排查问题的难度。
```
反例：
public Integer getData(){
	if(true)  {
		return data + 100;
	} else {
		return data - 100;
	}
}
```
4.**【强制】**在一个switch块内，每个case要么通过break/return等来终止，要么注释说明程序将继续执行到哪一个case为止；在一个switch块内，都必须包含一个default语句并且放在最后，即使它什么代码也没有。

5.**【强制】**在if/else/for/while/do语句中必须使用大括号，即使只有一行代码，避免使用下面的形式：if (condition) statements;

6.*【推荐】*推荐尽量少用else， if-else的方式可以改写成：
```
if(condition){
	...
	return obj;
}
//接着写else的业务逻辑代码;
说明：如果非得使用if()...else if()...else...方式表达逻辑，【强制】请勿超过 3层，
超过请使用状态设计模式。
正例：逻辑上超过 3层的if-else代码可以使用卫语句，或者状态模式来实现。
```
7.*【推荐】*除常用方法（如 getXxx/isXxx）等外，不要在条件判断中执行其它复杂的语句，将复杂逻辑判断的结果赋值给一个有意义的布尔变量名，以提高可读性。
>说明：很多 if语句内的逻辑相当复杂，阅读者需要分析条件表达式的最终结果，才能明确什么样的条件执行什么样的语句，那么，如果阅读者分析逻辑表达式错误呢？

```
正例：
//伪代码如下
boolean existed = (file.open(fileName, "w") != null) && (...) || (...);
if (existed) {
...
}
反例：
if ((file.open(fileName, "w") != null) && (...) || (...)) {
...
}
```

本文参考[《阿里巴巴JAVA开发手册》][1]

[1]:https://github.com/GaogaoHub/alibaba-java-style-guide