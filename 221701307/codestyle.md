## 代码风格

- 缩进
    > 缩进采用4个空格，禁止使用tab字符。
    > 如果使用tab缩进，必须设置1个tab为4个空格。IDEA设置tab为4个空格时，请勿勾选Use tab character；而在eclipse中，必须勾选insert spaces for tabs。
    
    </br>
- 变量命名 </br>
    1.代码中的命名均不以下划线_或美元符号$开始，也不能以下划线_或美元符号$结束
    - 正例：classnum / stuage </br> 
    - 反例：_classnum$ / classnum$ / stuage_ / $stuage 
    
    2.命名尽量使用英文单词，禁止使用中文
    > 变量的命名遵循见名知义的原则，力求简单清楚。</br>
    -  正例：'姓名'命名为'name'；'年龄'命名为'age' </br>
    -  反例：'姓名'命名为'a'/'b'/'姓名'；'年龄'命名为'a'/'b'/'年龄'
    
    3.对于变量命名，禁止取单个字符如'a'/'b'/'c'
    > 局部变量中可以采用通用命名的方式，仅限于n、i、j 、等作为循环使用。（一般习惯上用n、m、i、j、k等表示int 类型的变量；c、ch等表示 字符型变量；a、b等表示数组；p、pre等表示指针）；</br>
    - 正例：</br>
      ```java
      for ( i=0 ; i<100 ; i++ ) {...}
      p->next=pre->next; 
      ```
    
    4.使用驼峰命名法命名多个单词组成的变量名
    > 需要用到的变量命名命名要遵循首字母小写原则，如果名称是由多个单词组成，每个单词的首字母都要大写（除首个单词）使用尽量完整的单词组合来表达其意，即要做到"见名知意"</br>
    - 正例：stuMathScore / bookPrice </br>
    - 反例：speedcontroller / milkboxowner
    
    5.杜绝完全不规范的缩写，避免望文不知义
    - 正例：address缩写为addr；maximum缩写为max </br>
    - 反例：AbstractClass缩写命名成 AbsClass；condition缩写命名成 condi
    
    6.常量、宏和模板名采用全大写的方式，每个单词间用下划线_分隔
    > 力求语义表达完整清楚，不要嫌名字长 </br>
    - 正例：MAX_BOOK_COUNT / CACHE_EXPIRED_TIME </br>
    - 反例：MAX_COUNT / EXPIRED_TIME
    
    7.尽量避免名字中出现数字编号
    - 反例：textbox1 / button1
    
    8.枚举类型enum 常量应以大写字母开头或全部大写。
    - 正例：枚举名字为ProcessStatusEnum的成员名称：SUCCESS / UNKNOWN_REASON
    
    9.命名中若使用了特殊约定或缩写，则要有注释说明。
    > 应该在源文件的开始之处，对文件中所使用的缩写或约定，特别是特殊的缩写，进行必要的注释说明。
    
     </br>    
- 每行最多字符数</br>
    1.单行字符数限制不超过80个，超出需要换行 </br>
    
    2.在运算符前面进行折行处理 </br>
    
    3.高层折行优于低层折行。 </br>
    > 在考虑对代码进行折行处理的时候，需要注意代码的层次性。如某段代码涉及到混合四则运算，而四则运算又有明显的运算顺序，此时对代码进行折行时就最好能够在四则运算的关键顺序上进行折行处理。
    - 正例：</br>
    ``` java
        Mynum=mynum1*(mynum1+mynue2+mynum3-mynum4) 
                 +8*mynum5  
    ```
    - 反例：</br>
    ```java
       Mynum=mynum1*(mynum1+mynue2+mynum3  
                  -mynum4) +8*mynum5  
    ```
    
    4.如果代码行较长，在某个参数的逗号后面进行折行
    - 正例：
    ```java
    n7stat_str_compare((BYTE *) & stat_object,
                       (BYTE *) & (act_task_table[taskno].stat_object),
                       sizeof (_STAT_OBJECT));
    ```
    
    5.下一行的代码应该与其同等级的代码行左对齐
    > 当代码行两侧距离页边的距离比较大，看其来不怎么舒服时，可以在代码行中通过插入TAB键(会在代码行中连续插入8个字符)来提高代码的阅读性，让代码的缩进实现统一。

    </br>
- 函数最大行数 </br>
    以80、200、500为规定界限，超过80行的函数即为超大函数，即进行拆分，规定最大行数为80-200行，拆分时遵循如下原则：
    - 每一个代码块都可以封装为一个函数
    - 每一个循环体都可以封装为一个函数 </br>
    - 每一个条件体都可以封装为一个函数
      
    </br>
- 函数、类命名 </br>
    1.类名、接口名使用UpperCamelCase风格，必须遵从驼峰形式，但以下情形例外：DO/BO/DTO/VO/AO/PO/UID等。
    - 正例：UserLoginCheckService / UserDO 
    - 反例：userlogincheckservice / UserDo
      
    2.方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
    - 正例：userServiceImpl 
    - 反例：userserviceimpl
      
    3.如果模块、 接口、类、方法使用了设计模式，在命名时需体现出具体模式
    > 将设计模式体现在名字中，有利于阅读者快速理解架构设计理念。
    - 正例：public class OrderFactory ; public class LoginProxy;

    4.如果是形容能力的接口名称，取对应的形容词为接口名（通常是–able 的形容词）。
    - 正例：JDK中的Comparable接口
    
    5.接口类中的方法和属性不要加任何修饰符号（public也不要加），保持代码的简洁性，并加上有效的Javadoc注释。尽量不要在接口里定义变量，如果一定要定义变量，肯定是与接口方法相关，并且是整个应用的基础常量。
    
    6.抽象类命名使用Abstract或Base开头；异常类命名使用Exception结尾；测试类命名以它要测试的类的名称开始，以Test结尾。
    
    7.判断函数名采用谓-宾结构（动-名），且应反映函数执行什么操作以及返回什么内容。
    - 正例：if (NullBox(x))
    - 反例：if (CheckBox(x))
    
    </br>
- 常量 </br>
    1.在long或者Long赋值时，数值后使用大写的 L，不能是小写的 l，小写容易跟数字 1 混淆，造成误解。
    > 说明：Long a = 2l；写的是数字的 21，还是 Long 型的 2？
    
    2.不允许任何魔法值（即未经预先定义的常量）直接出现在代码中  
    - 正例：</br>
    ```java
     public static final ORDER_REDIS_KEY_PREFIX = "orderId_"; 
     String orderRedisKey = ORDER_REDIS_KEY_PREFIX + orderId; 
     ```
    - 反例：
    ```java
    String redisKey = "orderId_" + orderId;
    ```
   </br>  
- 空行规则 </br>
  1.函数之间应该用空行分开 
  
  2.变量声明应尽可能靠近第一次使用处，避免一次性声明一组没有马上使用的变量 
  
  3.用空行将代码按照逻辑片断划分 
  
  4.每个类声明之后应该加入空格同其他代码分开 
  - 正例：
  ```java
  public int nullBox(int milknum)
  { 
       ... // program code
  }
  
  public void toString(char[] c)
  { 
       ... // program code
  }
  
  repssn_ind = ssn_data[index].repssn_index;
  repssn_ni = ssn_data[index].ni;
  ```
  - 反例：
  ```java
  public int nullBox(int milknum)
  { 
       ... // program code
  }
  public void toString(char[] c)
  { 
       ... // program code
  }
  repssn_ind = ssn_data[index].repssn_index;
  repssn_ni = ssn_data[index].ni;
  ```
  </br>
- 注释规则 </br>
  1.源文件头部应进行注释，列出：生成日期、作者、模块目的/功能等
  - 正例：
  ```java
  /************************************************************
  FileName: test.cpp
  Author: Version : Date:
  Description: // 模块描述
  Version: // 版本信息
  Function List: // 主要函数及其功能
  1. -------
  History: // 历史修改记录
  <author> <time> <version > <desc>
  David 96/10/12 1.0 build this moudle
  ***********************************************************/
  ```
  
  2.函数头部应进行注释，列出：函数的目的/功能、输入参数、输出参数、返回值等。
  ```java
  /*************************************************
  Description: // 函数功能、性能等的描述
  Input: // 输入参数说明，包括每个参数的作
  // 用、取值说明及参数间关系。
  Output: // 对输出参数的说明。
  Return: // 函数返回值的说明
  Others: // 其它说明
  *************************************************/
  ```
  
  3.对于所有有物理含义的变量、常量，如果其命名不是充分自注释的，在声明时都必须加以注释，说明其物理含义。
  ```java
  /*************************************************
  #define MAX_ACT_TASK_NUMBER 1000
  #define MAX_ACT_TASK_NUMBER 1000 /* active statistic task number */
  *************************************************/
  ```
  
  4.注释的主要目的应该是解释为什么这么做，而不是正在做什么。 
  
  5.某些分支语句（条件分支、循环语句等）需编写注释。
  > 这些语句往往是程序实现某一特定功能的关键，对于维护人员来说，良好的注释帮助更好的理解程序，有时甚至优于看设计文档.
  
  6.所有的枚举类型字段必须要有注释，说明每个数据项的用途。 
  
  7.注释时，要求能够准确反应设计思想和代码逻辑；能够描述业务含义，使别的程序员能够迅速了解到代码背后的信息。完全没有注释的大段代码对于阅读者形同天书，注释是给自己看的，即使隔很长时间，也能清晰理解当时的思路；注释也是给继任者看的，使其能够快速接替自己的工作。


  </br>
- 操作符前后空格 </br>
  1.值操作符、比较操作符、算术操作符、逻辑操作符、位域操作符，如 = + >= <= + * % && || << ^ 等二元操作符的前后应当加空格
  - 正例：for (i = 10; i <= 100; i++)
  - 反例：for (i=10;i<=100;i++)
  
  2.一元操作符如 !、 ~ 、 ++ 、 -- 、 &（ 地址运算符） 等前后不加空格
  - 正例：if (!nullBox(milknum))
  - 反例：if ( !nullBox(milknum))
  
  3.像'[]' '.' '->' 这类操作符前后不加空格
  - 正例：p->next.stuname = Stunum[8];
  - 反例：p ->next. stuname = Stunum [8];
  
- 其他规则 </br>
  1.关键字之后要留空格
  > if、for、while 等关键字之后应留一个空格再跟左括号'（ '， 以突出关键字
  - 正例：if (NullBox(x))
  - 反例：if(NullBox(x))
  
  2.函数名之后不要留空格， 紧跟左括号'('， 以与关键字区别。
  - 正例：findStudent(age)
  - 反例：findStudent (age)
  
  3.'(' 向后紧跟，')' 、'，' 、';' 向前紧跟， 紧跟处不留空格。
   - 正例：for (i = 10; i < 100; i++)
   - 反例：for ( i = 10;i < 100;i++ )
   
  5.尽量使用interfaces，不要使用abstract类。
  
  6.尽量不要在vm中加入变量声明、逻辑运算符，更不要在vm模板中加入任何复杂的逻辑。

  7.任何数据结构的构造或初始化，都应指定大小，避免数据结构无限增长吃光内存。

  8.对于“明确停止使用的代码和配置”，如方法、变量、类、配置文件、动态配置属性等要坚决从程序中清理出去，避免造成过多垃圾。
