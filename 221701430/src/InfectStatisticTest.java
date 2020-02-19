import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;


import junit.framework.TestCase;

class InfectStatisticTest {

	@Test
	void testMain() throws IOException {
		testCommandLineAnalysis test1 = new testCommandLineAnalysis();
		//手写一个命令行的ArrayList
		ArrayList<String> commandline_test = new ArrayList<String>();
        commandline_test.add("list");
        commandline_test.add("-log");
        commandline_test.add("D:\\InfectStatistic-main\\221701430\\log\\");
        commandline_test.add("-out");
        commandline_test.add("D:\\InfectStatistic-main\\221701430\\result\\test.txt");
        commandline_test.add("-type");
        commandline_test.add("ip");
        commandline_test.add("sp");
        commandline_test.add("dead");
        commandline_test.add("-province");
        commandline_test.add("全国");
        commandline_test.add("福建");
        commandline_test.add("-date");
        commandline_test.add("2020-02-04");
        test1.testanalysis(commandline_test);
        testCommandLineRun test2 = new testCommandLineRun();
        test2.testCommandLine_Run(test1.cmd_analysis.analysis(commandline_test));
	}
	class testCommandLineAnalysis extends TestCase{
		InfectStatistic.CommandLineAnalysis cmd_analysis = new InfectStatistic.CommandLineAnalysis();
		@Test
		public void testanalysis(ArrayList<String> commandline_test) {
			//analysis之后返回一个commandline类，使用test方法输出commandline类的各项参数
			cmd_analysis.analysis(commandline_test).test();
		}
	}
	class testCommandLineRun extends TestCase{
		InfectStatistic.CommandLineRun cmd_run;
		@Test
		public void testCommandLine_Run(InfectStatistic.CommandLine cmd) throws IOException {	
			cmd_run = new InfectStatistic.CommandLineRun(cmd);
			 for(int i = 0;i<cmd_run.province_list.size();i++) {
		        	System.out.println("省名：" + cmd_run.province_list.get(i).name + 
		        			" ip:" + cmd_run.province_list.get(i).ip + 
		        			" sp:" + cmd_run.province_list.get(i).sp + 
		        			" cure:" + cmd_run.province_list.get(i).cure + 
		        			" dead:" + cmd_run.province_list.get(i).dead);
		        }
		}
	}
}
