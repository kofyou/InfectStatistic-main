# 代码风格

+ 参考老师给的《码出高效_阿里巴巴Java开发手册》
+ 开发手册链接：https://github.com/chjw8016/alibaba-java-style-guide

# 1、缩进
+ 缩进1个Tab键，为4个空格
+ 左括号另起一行写
+ 举例如下
```
String[] splitString;
```

# 2、变量命名
+ 变量命名采用驼峰命名法，如helloWorld,第一个单词小写，后面的所有单词首字母大写
+ 举例如下
```
String[] splitString;
```

# 3、每行最多字符数
+ 每行最多字符数为40个


# 4、函数最大行数
+ 每个函数最大行数为200行

# 5、函数、类命名
+ 函数命名采用驼峰命名法
+ 类命名为每个单词首字母均大写
+ 举例如下
```
class InfectStatistic 
```
```
public int getIp() 
{
	return ip;
}
```

# 6、常量
+ 常量全部大写，并且采用下划线隔开单词
+ 举例如下
```
MAX_STOCK_COUNT
```

# 7、空行规则
+ 函数与函数间空行1行
+ 函数或类中不同的功能间空一行
+ 举例如下
```
class Province{
		String name=null;//省份名称(拼音）
	    int ip=0;//感染患者
	    int sp=0;//疑似患者
	    int cure=0;//治愈
	    int dead=0;//死亡
	    
	    public Province(String name) {
	    	this.name=name;
	    	this.ip=0;
	    	this.sp=0;
	    	this.cure=0;
	    	this.dead=0;
	    }
	    
	    public int getIp() {
	    	return ip;
	    }
	    
	    public int getSp() {
	    	return sp;
	    }
}
```

# 8、注释规则
+ 一般单行注释为//
+ 函数的功能或者函数内部一些功能块的注释写在上方，自己一行
+ 关于一些判断条件或比较长的单个语句的注释直接写在语句后面
+ 举例如下
```
//统计全国数据
public void Sum(Hashtable<String,Province> hashtable) {
    Province nation = new Province("全国");
	Set set = hashtable.keySet();
	Iterator iterator = set.iterator();
	
	//遍历哈希表
    while(iterator.hasNext()) {
        Object keys = iterator.next();
        nation.ip += hashtable.get(keys).getIp();
        nation.sp += hashtable.get(keys).getSp();
        nation.cure += hashtable.get(keys).getCure();
        nation.dead += hashtable.get(keys).getDead();
    }
    hashtable.put("全国",nation);//将全国放入哈希表
}
```

# 9、操作符前后空格
+ 所有操作符前后均不空格
+举例如下
```
String s=s.substring(0,s.length()-1);
```
