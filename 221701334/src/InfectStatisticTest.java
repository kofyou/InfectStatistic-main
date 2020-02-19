import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class InfectStatisticTest {

	@Test
	void test() throws FileNotFoundException, IOException {
		String args[]= {"list", "-date", "2020-01-22", "-log", "D:/log/", "-out", "D:/output.txt"};
		InfectStatistic.main(args);
	}

}
