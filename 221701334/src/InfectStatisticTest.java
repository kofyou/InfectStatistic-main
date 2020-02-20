import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class InfectStatisticTest {
	@Test
	void test1() throws FileNotFoundException, IOException { //无参数情况
		String args[]= {"list",  "-log", "D:/log/", "-out", "D:/output1.txt"};
		InfectStatistic.main(args);
	}
	@Test
	void test2() throws FileNotFoundException, IOException { //省份参数无记录情况
		String args[]= {"list",  "-log", "D:/log/", "-out", "D:/output2.txt","-province","湖南"};
		InfectStatistic.main(args);
	}
	@Test
	void test3() throws FileNotFoundException, IOException { //带日期参数
		String args[]= {"list",  "-log", "D:/log/", "-out", "D:/output3.txt","-date", "2020-01-21"};
		InfectStatistic.main(args);
	}
	@Test
	void test4() throws FileNotFoundException, IOException { //带类型参数
		String args[]= {"list",  "-log", "D:/log/", "-out", "D:/output4.txt","-type","治愈"};
		InfectStatistic.main(args);
	}
	@Test
	void test5() throws FileNotFoundException, IOException { //带多个省份参数
		String args[]= {"list",  "-log", "D:/log/", "-out", "D:/output5.txt","-province","福建","全国"};
		InfectStatistic.main(args);
	}
	@Test
	void test6() throws FileNotFoundException, IOException { //同时带日期和类型参数
		String args[]= {"list", "-date", "2020-01-21", "-log", "D:/log/", "-out", "D:/output6.txt","-type","治愈"};
		InfectStatistic.main(args);
	}
	@Test
	void test7() throws FileNotFoundException, IOException { //同时带日期和省份参数
		String args[]= {"list", "-date", "2020-01-22", "-log", "D:/log/", "-out", "D:/output7.txt","-province","福建"};
		InfectStatistic.main(args);
	}
	@Test
	void test8() throws FileNotFoundException, IOException { //同时带日期、无记录省份、类型参数
		String args[]= {"list",  "-log", "D:/log/", "-out", "D:/output8.txt","-type","治愈","-province","福建","江苏"};
		InfectStatistic.main(args);
	}
	@Test
	void test9() throws FileNotFoundException, IOException { //同时带日期省份类型参数
		String args[]= {"list", "-date", "2020-01-22", "-log", "D:/log/", "-out", "D:/output9.txt","-type","疑似","-province","福建"};
		InfectStatistic.main(args);
	}
	@Test
	void test10() throws FileNotFoundException, IOException { //同时带日期多个类型多个省份参数
		String args[]= {"list", "-date", "2020-01-22", "-log", "D:/log/", "-out", "D:/output10.txt","-type","感染","死亡","-province","福建","全国"};
		InfectStatistic.main(args);
	}
}
