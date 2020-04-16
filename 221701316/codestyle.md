 # 1、缩进
+ 作用域里的语句从第二行开始缩进1个Tab键，为4个空格
+ 左括号另起一行写
+ 举例如下
```
public static int GetInteger(String s)  
	{
		String ss = s.substring(0,s.length()-1);
		int ans=Integer.valueOf(ss); 
		return ans;
	}
```

# 2、变量命名
+ 变量第一个当次小写，后面单词大写头字母大写，如wordWord这样
+ 举例如下
```
static String pathString = "./log";
```

# 3、每行最多字符数
+ 每行最多字符数为40个
+ 举例如下
```
static String[] pString = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南",
    		"河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏",
    		"青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"
    };  
```

# 4、函数最大行数
+ 每个函数最大行数为100行
@ -16,6 +36,18 @@
# 5、函数、类命名
+ 函数命名每个单词的头字母大写
+ 类命每个单词的头字母大写
+ 举例如下
```
class InfectStatistic 
```
```
public static int GetInteger(String s)  
	{
		String ss = s.substring(0,s.length()-1);
		int ans=Integer.valueOf(ss); 
		return ans;
	}
```

# 6、常量
+ 常量为全部大写
@ -23,11 +55,63 @@
# 7、空行规则
+ 函数与函数间空行1行
+ 类中数据成员的声明也是空行1行
+ 举例如下
```
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
```
```
//初始化
	public InfectStatistic()   
	{
		for(int i = 0;i<32;i++)
			ip[i] =sp[i] = cure[i] = dead[i] = 0;
		
		pathString = "./log";
		
		outPathString = "./result/testOutput.txt";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = new Date();
		dateString = dateFormat.format(date); //日期参数值，初始值设为当前日期
		
		//将key和value一起放到hashmap中
	    map.put("-log",0);
	   	map.put("-out",1);
	   	map.put("-date",2);
	   	map.put("-type",3);
	   	map.put("-province",4);
		
	   	for(int i = 0;i<pString.length;i++)
		    provinceMap.put(pString[i],i);
	}
	
	//从字符串中提取数字
	public static int GetInteger(String s)  
	{
		String ss = s.substring(0,s.length() - 1);
		int ans = Integer.valueOf(ss); 
		return ans;
	}
```

# 8、注释规则
+ 一般单行注释为//
+ 如果单行注释不够，我会写成这样子/*zxcvbnm*/
+ 举例如下
```
//从字符串中提取数字
	public static int GetInteger(String s)  
	{
		String ss = s.substring(0,s.length()-1);
		int ans=Integer.valueOf(ss); 
		return ans;
	}
```

# 9、操作符前后空格
+ 所有操作符前空一格，后空一个
+举例如下
```
String ss = s.substring(0,s.length()-1);
```
