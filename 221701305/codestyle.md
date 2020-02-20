## 代码风格

//参考老师给的《码出高效_阿里巴巴Java开发手册》
//开发手册链接：https://github.com/chjw8016/alibaba-java-style-guide

- 缩进
- 变量命名  **【强制】**类名使用UpperCamelCase风格，必须遵从驼峰形式，但以下情形例外：（领域模型的相关命名）DO / BO / DTO / VO等。

正例：MarcoPolo / UserDO / XmlService / TcpUdpDeal /   TaPromotion
反例：macroPolo / UserDo / XMLService / TCPUDPDeal /   TAPromotion
4.**【强制】**方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。

正例： localValue / getHttpMessage() /  inputUserId
5.**【强制】**常量命名全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。

正例： MAX_STOCK_COUNT
反例： MAX_COUNT
6.**【强制】**抽象类命名使用Abstract或Base开头；异常类命名使用Exception结尾；测试类命名以它要测试的类的名称开始，以Test结尾。
- 每行最多字符数
- 函数最大行数
- 函数、类命名
- 常量
- 空行规则
- 注释规则
- 操作符前后空格
- 其他规则
- ...
