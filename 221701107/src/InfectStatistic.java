import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.zip.DataFormatException;
import org.junit.jupiter.api.Test;

/**
 * InfectStatistic
 *
 * @author xjliang
 * @version 1.0.0
 * @since 2020-01-11
 */
class InfectStatistic {

    public static void run(String[] args, String benchmarkFile) {
        CommandArgs commandArgs = ArgsParser.parse(args);
        System.out.println(commandArgs);

        Lib lib = new Lib();
        try {
            lib.parse(args);
        } catch (IOException | DataFormatException e) {
            e.printStackTrace();
        }

        if (benchmarkFile != null) {
            diff(commandArgs.getOptionValues("out").get(0), benchmarkFile);
        } else {
            System.out.println("no benchmark founded, you need to check result by yourself");
        }
    }

    public static void diff(String firstName, String secondName) {
        Scanner input1; // read first file
        Scanner input2; // read second file
        try {
            input1 = new Scanner(new File(firstName));
            input2 = new Scanner(new File(secondName));
            String first;
            String second;
            while (input1.hasNextLine() && input2.hasNextLine()) {
                while ((first = input1.nextLine()).startsWith("//")) {
                    if (!input1.hasNextLine()) {
                        first = "";
                        break;
                    }
                }
                while ((second = input2.nextLine()).startsWith("//")) {
                    if (! input2.hasNextLine()) {
                        second = "";
                        break;
                    }
                }

                if (!first.equals(second)) {
                    System.out.println("Differences found: " + "\n" + first + '\n' + second);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        run(args, null);
    }

    @Test
    public void test1() {
        String[] args = "list -log .\\log\\ -out .\\result\\ListOut1.mine.txt -date 2020-01-22"
            .split(" ");
        run(args, "./result/ListOut1.txt");
    }

    @Test
    public void test2() {
        String[] args = "list -log .\\log\\ -out .\\result\\ListOut2.mine.txt -date 2020-01-22 -province 福建 河北"
            .split(" ");
        run(args, "./result/ListOut2.txt");
    }

    @Test
    public void test3() {
        String[] args = "list -log .\\log\\ -out .\\result\\ListOut3.mine.txt -date 2020-01-23 -type cure dead ip -province 全国 浙江 福建"
            .split(" ");
        run(args, "./result/ListOut3.txt");
    }
}
