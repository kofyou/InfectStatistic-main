
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;



public class InfectStatisticTest {
    static InfectStatistic sta;

    @BeforeClass
    public static void BeforeClass() throws Exception {
        sta = new InfectStatistic();
    }

    @AfterClass
    public static void del() throws Exception {
    }

    @Test
    public void IfectStatisticTest() {
        String[] args = {"list","-log","E:/log/",
                "-out","E:/out/output.txt",
                "-date","2020-01-23",
                "-type","cure","dead","ip",
                "-province","全国","浙江","福建"};
        sta.main(args);
    }

















    @Test
    public void saveResult() {
    }
}
