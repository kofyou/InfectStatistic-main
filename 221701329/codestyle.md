## 代码风格

- 缩进  
Python是一个对缩进很敏感的语言（没有括号作为代码段的划分  
我使用Tab缩进(Pycharm中可以设置Tab空格数，我设置为4个)
- 变量命名  
  - 私有成员，使用小驼峰命名
  - 真私有成员，__private_小驼峰命名
  - 静态成员,大驼峰命名
- 每行最多字符数
    - 每行最大长度79，换行可以使用反斜杠，最好使用圆括号。换行点要在操作符的后边敲回车
- 函数最大行数
    - 不超过50行，多了尽量分离成多个函数
- 函数、类命名
    - 函数小驼峰命名，并且以V+n的形式出现
    - 类大驼峰命名
- 常量
    gl_开头，例:gl_filePath
- 空行规则
    - 类、函数之间空两行
    - 方法之间空一行
- 注释规则  
    - 注释函数说明、成员说明时不同
    - 成员说明：在需要注释的语句后空两格&ensp;&ensp;\# 这是注释  
    \#  这是注释  
    - 函数、类等说明：在语句上空格
- 空格规则
    - 所有操作符左右都留一个空格
    - 函数中，与参数之间要有一个空格
    - 流程控制语句与条件空一格
- 其他规则
    - 宇宙法则：装上Pylint
- ...