import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;

/*
@author PSF(52260506@qq.com)
@data 2020/2/19 19:16
*/public class InfectStatisticTest extends TestCase {
    /* 以下进行正确性输出单元测试 */
    @Test
    public void test1 () throws Exception {
        String[] args = { "list", "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt",
                "-log", "F:\\GitRepository\\081700430\\081700430\\log"};
        InfectStatistic.main(args);
    }
    @Test
    public void test2 () throws Exception {
        String[] args = { "list", "-log", "F:\\GitRepository\\081700430\\081700430\\log",
                 "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt"};
        InfectStatistic.main(args);
    }
    @Test
    public void test3() throws Exception {
        String[] args = { "list", "-log", "F:\\GitRepository\\0817\\log",
                "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt"};
        InfectStatistic.main(args);
    }
    @Test
    public void test4() throws Exception {
        String[] args = { "list", "-out" , "-log"};
        InfectStatistic.main(args);
    }
    @Test
    public void test5() throws Exception {
        String[] args = { "list", "-log"};
        InfectStatistic.main(args);
    }
    @Test
    public void test6 () throws Exception {
        String[] args = { "list", "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt",
                "-log", "F:\\GitRepository\\081700430\\081700430\\log", "-date", "2020-01-24" };
        InfectStatistic.main(args);
    }
    @Test
    public void test7 () throws Exception {

        String[] args = { "list", "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt",
                "-log", "F:\\GitRepository\\081700430\\081700430\\log", "-province", "福建", "浙江", "全国", "湖北" };
        InfectStatistic.main(args);
    }
    @Test
    public void test8 () throws Exception {

        String[] args = { "list", "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt",
                "-log", "F:\\GitRepository\\081700430\\081700430\\log", "-type", "dead", "ip", "sp" };
        InfectStatistic.main(args);
    }
    @Test
    public void test9 () throws Exception {
        String[] args = { "list", "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt", "-log",
                "F:\\GitRepository\\081700430\\081700430\\log", "-province", "江苏", "全国", "福建", "湖北", "-type",
                "sp", "dead", "cure"};
        InfectStatistic.main(args);
    }
    @Test
    public void test10() throws Exception {
        String[] args = { "list", "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt", "-log",
                "F:\\GitRepository\\081700430\\081700430\\log", "-province", "山东", "全国", "福建", "湖北",
                "-type", "sp", "ip", "cure", "-date", "2020-01-23"};
        InfectStatistic.main(args);
    }
    @Test
    public void test11() throws Exception {
        String[] args = { "list", "-province", "山东", "全国", "福建", "湖北", "-type", "sp", "ip",
                "-date", "2020-01-23" , "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt", "-log",
                "F:\\GitRepository\\081700430\\081700430\\log",};
        InfectStatistic.main(args);
    }
    @Test
    public void test12() throws Exception {
        String[] args = { "list","-type", "sp", "ip", "-date", "2020-01-22", "-out" ,
                "F:\\GitRepository\\081700430\\081700430\\src\\test.txt", "-log",
                "F:\\GitRepository\\081700430\\081700430\\log"};
        InfectStatistic.main(args);
    }
    public void test13() throws Exception {
        String[] args = { "list", "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt", "-log",
                "F:\\GitRepository\\081700430\\081700430\\log", "-province", "山东", "全国", "福建", "湖北",
                 "-date", "2020-01-21"};
        InfectStatistic.main(args);
    }
    /* 容错处理 */
    public void test14() throws Exception {
        String[] args = { "list", "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt", "-log",
                "F:\\GitRepository\\081700430\\081700430\\log", "-province", "山", "全国", "福", "湖北",
                "-date", "2020-02-20"};
        InfectStatistic.main(args);
    }
    public void test15() throws Exception {
        String[] args = { "list", "-out" , "F:\\GitRepository\\081700430\\081700430\\src\\test.txt", "-log",
                "F:\\GitRepository\\081700430\\081700430\\log", "-province", "tiand", "全国", "福建", "湖北",
                "-type", "sdfx", "ddd", "ip", "sp", "-date", "2020-02-21"};
        InfectStatistic.main(args);
    }

}