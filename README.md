# InfectStatistic
疫情统计-作业完成流程：


1. fork该仓库到你的仓库，在根目录新建目录，目录名为你的学号

2. 复制example下的目录结构到你新建的目录下：
    - 如果你使用c/c++或python，修改src下的文件后缀和文件内容对应到你使用的语言
    - src下源代码【仅】允许包括这`InfectStatistic、Lib`两个文件，Lib可以为空但必须存在，【c/c++或python可以修改后缀】，但确保文件名一致、区分大小写

3. 语言支持：
    - Java：Java8，推荐使用Java开发
    - c/c++：gcc/g++ 6.3，将`java InfectStatistic`替换为`InfectStatistic.exe`
    - python：3.7，将`java InfectStatistic`替换为`python InfectStatistic.py`
    - 换行使用'\n'，编码统一使用UTF-8
    - 仅允许使用语言自带的库，不允许使用第三方库

4. 请勿修改example下的文件

5. example/result下提供了三个测试用例的标准输出，对应的命令在文件尾部提供了，即对example/log下的日志，输入对应的命令应该会是相应的输出。

6. 为了使测试文件和输出文件不产生诱导性，要在日志文件/输出文件末尾加上`// 该文档并非真实数据，仅供测试使用`，建议读取日志时直接忽略以`//`开始的行

7. 除了示例仓库的给出的文件，其它自己产生的文件都应该在`.gitignore`忽略，如编译器生成的项目文件、输出文件、class、jar包、exe等

8. 代码每有更新就可以进行commit签入，然后push到github，至少进行10次以上的commit签入，并将最终程序以pr的方式提交到该仓库
