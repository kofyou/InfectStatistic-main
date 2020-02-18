## 代码风格

- 缩进
> 缩进采用四个空格，禁止使用tab缩进，以防造成tab和空格的混用

- 变量命名
> 小写字母，单词之间使用'\_'分割，如  `lowercase_with_underscores`

- 每行最多字符数
> 根据PEP8规范，每行限制为79个字符，若超出则使用换行符'\\'换行
```python
示例：
print('qwertyuiop...\
    zxcvbnm')
```
> 换行符断行的位置应在操作符前
```python
示例：
if color == 'white' or color == 'black'\
    or color == 'red':
    do_something(color)
```

- 函数最大行数
> 函数最大行数不超过200行

- 函数、类命名
> **函数名** 小写字母及下划线'\_'，如  `lowercase_with_underscores`
> **类名** 采用驼峰拼写法，首字母大写，如  `CamelCase`

- 常量
> 大写字母及下划线'\_'，如  `UPPERCASE_WITH_UNDERSCORES`

- 空行规则
> 输入空行时，一般遵循以下原则：
>（1）在import不同种类的模块间加空行；
>（2）顶层函数和类的定义之间加空行；
>（3）在类与类的定义之间加空行；
>（4）在函数与函数的定义之间加空行；
>（5）在class定义行和其第一个方法定义之间加空行；
>（6）在函数中的逻辑段落间加空行，即把相关的代码紧凑写在一起，作为一个逻辑段落，段落间以空行分隔

```python
示例：
def hello(name):
    print 'Hello %s!' % name
    

def goodbye(name):
    print 'See you %s.' % name
    
    
class MyClass(object):
    'This is a simple docstring'
    
    def __init__(self,name):
        self.name = name
        
    def get_annoy_name(self):
        return self.name.upper()
```
- 注释规则
> **单行注释** 在注释的开头使用#
> **多行注释** 使用三个单引号" ''' "
```python
示例：

#这是单行注释

'''
这是多行注释
这是多行注释
'''
```

- 操作符前后空格
> 在二元运算符周围使用空格
```python
示例：

是：

i = i + 1
sum += 1
x = x*2 - 1
sum = x*x + y*y
c = (a+b) * (a-b)

否：

i=i+1
sum +=1
x = x * 2 - 1
sum = x * x + y * y
c = (a + b) * (a - b)
```

- 引号
> 除特殊情况以外（如字符串中带有单引号），字符串统一使用单引号
> 

- 分支与循环
> 各种分支与循环语句不要写成一行
```python
示例：

是：

if !flg:
    pass
for i in range(10):
    print(i)

否：

if !flg: pass
for i in range(10): print(10)

```

- 头部
> 所有python文件头部需要标上如下语句
```python
#-*- coding:utf8 -*-
#!/usr/bin/python
```