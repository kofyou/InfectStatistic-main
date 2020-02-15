import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Lib
 *
 * @author xjliang
 * @version 1.0.1
 * @since 2020-01-11
 */
public class Lib {

    private static final String NATION_NAME = "全国";
    private CommandArgs commandArgs;

    private Map<String, ProvinceStat> provinceStatMap;
    private String[] args;

    public static String run(String[] args)
        throws IOException, DataFormatException {
        CommandArgs commandArgs = ArgsParser.parse(args);
        System.out.println(commandArgs);

        Lib lib = new Lib();
        lib.parse(args);

        return lib.commandArgs.getOptionValues("out").get(0);
    }

    public static boolean fileEquals(String firstName, String secondName)
        throws FileNotFoundException {
        Scanner input1; // read first file
        Scanner input2; // read second file

        boolean equals = true;

        try {
            input1 = new Scanner(new File(firstName));
            input2 = new Scanner(new File(secondName));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        }

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
                if (!input2.hasNextLine()) {
                    second = "";
                    break;
                }
            }

            if (!first.equals(second)) {
                System.out.println("Differences found: " + "\n" + first + '\n' + second);
                equals = false;
                break;
            }
        }

        return equals;
    }

    public void parse(String[] args) throws IOException, DataFormatException {
        this.args = args;
        this.commandArgs = ArgsParser.parse(args);

        String logDir = commandArgs.getOptionValues("log").get(0);
        String logDeadline = "9999-99-99";
        if (commandArgs.containsOption("date")) {
            logDeadline = commandArgs.getOptionValues("date").get(0);
        }

        String[] logFiles = getOlderFiles(logDir, logDeadline);

        for (int i = 0; i < logFiles.length; i++) {
            logFiles[i] = logDir + '\\' + logFiles[i];
        }

        LogParser logParser = new LogParser();
        this.provinceStatMap = logParser.parse(logFiles);

        outputResult();
    }

    public static String[] getOlderFiles(String logDir, String logDeadline)
        throws NotDirectoryException, FileNotFoundException {
        File path = new File(logDir);
        if (!path.isDirectory()) {
            throw new NotDirectoryException("log path must be a directory");
        }

        final String finalLogDeadline = logDeadline;
        String[] logFiles = path.list((dir, name) -> {
            String logDate = name.substring(0, 10);
            return logDate.compareTo(finalLogDeadline) <= 0;
        });

        if (logFiles == null || logFiles.length == 0) {
            throw new FileNotFoundException("no log file older than deadline");
        }
        return logFiles;
    }

    private void statAll() {
        ProvinceStat nationStat = new ProvinceStat();

        Set<String> provinces = provinceStatMap.keySet();
        for (String province : provinces) {
            ProvinceStat provinceStat = provinceStatMap.get(province);
            nationStat.incrNumIP(provinceStat.getNumIP());
            nationStat.incrNumSP(provinceStat.getNumSP());
            nationStat.incrNumCure(provinceStat.getNumCure());
            nationStat.incrNumDead(provinceStat.getNumDead());
        }

        provinceStatMap.put(NATION_NAME, nationStat);
    }

    public void outputResult() throws IOException {
        List<String> province2stat;
        if (commandArgs.containsOption("province")) {
            province2stat = commandArgs.getOptionValues("province");
        } else {
            province2stat = new ArrayList<>(provinceStatMap.keySet());
            province2stat.add(0, NATION_NAME);
        }

        province2stat.sort(Comparator.reverseOrder());
        if (province2stat.contains(NATION_NAME)) {
            province2stat.remove(NATION_NAME);
            province2stat.add(0, NATION_NAME);
            statAll();
        }

        String outFilename = commandArgs.getOptionValues("out").get(0);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFilename))) {

//            Set<String> provinces = provinceStatMap.keySet();

            if (commandArgs.containsOption("type")) {
//                List<String> patientTypes = Arrays.asList("ip", "sp", "cure", "dead");
                List<String> patientTypes = commandArgs.getOptionValues("type");
                for (String province : province2stat) {
                    if (!provinceStatMap.containsKey(province)) {
                        provinceStatMap.put(province, new ProvinceStat());
                    }
                    ProvinceStat provinceStat = provinceStatMap.get(province);
                    bufferedWriter.write(province);
                    for (String type : patientTypes) {
                        switch (type) {
                            case "ip":
                                bufferedWriter.write(" 感染患者" + provinceStat.getNumIP() + "人");
                                break;
                            case "sp":
                                bufferedWriter.write(" 疑似患者" + provinceStat.getNumSP() + "人");
                                break;
                            case "cure":
                                bufferedWriter.write(" 治愈" + provinceStat.getNumCure() + "人");
                                break;
                            case "dead":
                                bufferedWriter.write(" 死亡" + provinceStat.getNumDead() + "人");
                                break;
                            default:
                                break;
                        }
                    }
                    bufferedWriter.newLine();
                }
            } else {
                for (String province : province2stat) {
                    if (!provinceStatMap.containsKey(province)) {
                        provinceStatMap.put(province, new ProvinceStat());
                    }
//                System.out.println(province + " " + provinceStatMap.get(province));
                    bufferedWriter.write(province + " " + provinceStatMap.get(province));
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.write("// 该文档并非真实数据，仅供测试使用\n");
            bufferedWriter.write("// 命令：" + String.join(" ", this.args) + "\n");

            System.out.println(">> write result to \"" + outFilename + "\" done.");
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}

class LibTest {

    @Test
    public void testArgsParseLog() {
        String[] args = "list -log .\\log\\ -out .\\result\\ListOut1.mine.txt -date 2020-01-22"
            .split(" ");

        CommandArgs commandArgs = ArgsParser.parse(args);
        Assert.assertEquals(".\\log\\", commandArgs.getOptionValues("log").get(0));
    }

    @Test
    public void testArgsParseOut() {
        String[] args = "list -log .\\log\\ -out .\\result\\ListOut1.mine.txt -date 2020-01-22"
            .split(" ");

        CommandArgs commandArgs = ArgsParser.parse(args);
        Assert.assertEquals(".\\result\\ListOut1.mine.txt", commandArgs.getOptionValues("out").get(0));
    }

    @Test
    public void testArgsParseProvince() {
        String[] args = "list -log .\\log\\ -out .\\result\\ListOut1.mine.txt -date 2020-01-22 -province 全国 福建"
            .split(" ");
        CommandArgs commandArgs = ArgsParser.parse(args);

        String[] expectedProvinces = {"全国", "福建"};
        Assert.assertArrayEquals(expectedProvinces, commandArgs.getOptionValues("province").toArray());
    }

    @Test
    public void testArgsParseType() {
        String[] args = "list -log .\\log\\ -out .\\result\\ListOut1.mine.txt -date 2020-01-22 -type ip cure dead"
            .split(" ");
        CommandArgs commandArgs = ArgsParser.parse(args);

        String[] expectedTypes = {"ip", "cure", "dead"};
        Assert.assertArrayEquals(expectedTypes, commandArgs.getOptionValues("type").toArray());
    }

    @Test
    public void testGetOlderLogFiles() {
        try {
            String[] actualFiles = Lib.getOlderFiles("./log", "2020-01-23");
            String[] expectedFiles = {"2020-01-22.log.txt", "2020-01-23.log.txt"};
            Assert.assertArrayEquals(expectedFiles, actualFiles);
        } catch (NotDirectoryException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegexGroup2() {
        String soap = "湖北 新增 感染患者 15人";
        String[] result = {"湖北", "15"};
        List<String> tokens = LogParser.regexGroup2(soap, LogParser.regex1);
        Assert.assertArrayEquals(result, tokens.toArray());
    }

    @Test
    public void testRegexGroup3() {
        String soap = "湖北 疑似患者 流入 福建 3人";
        String[] result = {"湖北", "福建", "3"};
        List<String> tokens = LogParser.regexGroup3(soap, LogParser.regex4);
        Assert.assertArrayEquals(result, tokens.toArray());
    }

    @Test
    public void testListOut1() {
        String[] args = "list -log .\\log\\ -out .\\result\\ListOut1.mine.txt -date 2020-01-22"
            .split(" ");
        try {
            String outFile = Lib.run(args);
            assertTrue(Lib.fileEquals(outFile, "./result/ListOut1.txt"));
            System.out.println("test passed");
        } catch (IOException | DataFormatException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testListOut2() {
        String[] args = "list -log .\\log\\ -out .\\result\\ListOut2.mine.txt -date 2020-01-22 -province 福建 河北"
            .split(" ");
        try {
            String outFile = Lib.run(args);
            assertTrue(Lib.fileEquals(outFile, "./result/ListOut2.txt"));
            System.out.println("test passed");
        } catch (IOException | DataFormatException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testListOut3() {
        String[] args = "list -log .\\log\\ -out .\\result\\ListOut3.mine.txt -date 2020-01-23 -type cure dead ip -province 全国 浙江 福建"
            .split(" ");
        try {
            String outFile = Lib.run(args);
            assertTrue(Lib.fileEquals(outFile, "./result/ListOut3.txt"));
            System.out.println("test passed");
        } catch (IOException | DataFormatException e) {
            fail(e.getMessage());
        }
    }
}

class ArgsParser {

    private static final String[] requiredOptions = {"log", "out"};

    public static boolean isOption(final String token) {
        return token.startsWith("-");
    }

    public static CommandArgs parse(final String[] args) {
        CommandArgs commandArgs = new CommandArgs();

        for (int i = 0; i < args.length; i++) {
            String token = args[i];
            if (isOption(token)) {
                String optionName = token.substring(1);

                boolean hasOptionValue = false;
                i++;
                // add all optionValues with optionName
                while (i < args.length) {
                    token = args[i];

                    if (isOption(token)) {
                        i--;
                        break;
                    }
                    i++;
                    commandArgs.appendOptionValue(optionName, token);
                    hasOptionValue = true;
                }

                if (!hasOptionValue) {
                    throw new IllegalArgumentException();
                }
            } else {
                // command
                commandArgs.appendCommand(token);
            }
        }

        for (String option : requiredOptions) {
            if (!commandArgs.containsOption(option)) {
                throw new IllegalArgumentException("option \"" + option + "\" is required");
            }
        }

        return commandArgs;
    }
}

class CommandArgs {

    private List<String> commandArgs = new ArrayList<>();

    private Map<String, List<String>> optionValuesMap = new HashMap<>();

    public boolean containsCommand(String commandName) {
        return commandArgs.contains(commandName);
    }

    public boolean containsOption(String optionName) {
        return optionValuesMap.containsKey(optionName);
    }

    public List<String> getOptionValues(String optionName) {
        return optionValuesMap.getOrDefault(optionName, null);
    }

    public void appendCommand(String command) {
        commandArgs.add(command);
    }

    public void appendOptionValue(String optionName, String optionValue) {
        if (!optionValuesMap.containsKey(optionName)) {
            optionValuesMap.put(optionName, new ArrayList<>());
        }
        optionValuesMap.get(optionName).add(optionValue);
    }

    @Override
    public String toString() {
        return "CommandArgs{" +
            "commandArgs=" + commandArgs +
            ", optionValuesMap=" + optionValuesMap +
            '}';
    }
}

class LogParser {

    final static public String regex1 = "(^\\W+) 新增 感染患者 (\\d+)人";
    final static public String regex2 = "(^\\W+) 新增 疑似患者 (\\d+)人";
    final static public String regex3 = "(^\\W+) 感染患者 流入 (\\W+) (\\d+)人";
    final static public String regex4 = "(^\\W+) 疑似患者 流入 (\\W+) (\\d+)人";
    final static public String regex5 = "(^\\W+) 死亡 (\\d+)人";
    final static public String regex6 = "(^\\W+) 治愈 (\\d+)人";
    final static public String regex7 = "(^\\W+) 疑似患者 确诊感染 (\\d+)人";
    final static public String regex8 = "(^\\W+) 排除 疑似患者 (\\d+)人";
    private Map<String, ProvinceStat> provinceStatMap;

    public static List<String> regexGroup2(String soap, String regex) {
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(soap);

        if (matcher.find()) {
            List<String> result = new ArrayList<>();
            result.add(matcher.group(1));
            result.add(matcher.group(2));
            return result;
        }
        return null;
    }

    public static List<String> regexGroup3(String soap, String regex) {
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(soap);
        if (matcher.find()) {
            List<String> result = new ArrayList<>();
            result.add(matcher.group(1));
            result.add(matcher.group(2));
            result.add(matcher.group(3));
            return result;
        }
        return null;
    }

    public static void testRegex() {
        String soap = "福建 新增 感染患者 23人\n"
            + "浙江 新增 感染患者 22人\n"
            + "福建 新增 疑似患者 2人\n"
            + "浙江 感染患者 流入 福建 12人\n"
            + "湖北 疑似患者 流入 福建 2人\n"
            + "安徽 死亡 2人\n"
            + "新疆 治愈 3人\n"
            + "福建 疑似患者 确诊感染 2人\n"
            + "新疆 排除 疑似患者 5人";

        System.out.println(regexGroup2(soap, regex1));
        System.out.println(regexGroup2(soap, regex2));
        System.out.println(regexGroup3(soap, regex3));
        System.out.println(regexGroup3(soap, regex4));
        System.out.println(regexGroup2(soap, regex5));
        System.out.println(regexGroup2(soap, regex6));
        System.out.println(regexGroup2(soap, regex7));
        System.out.println(regexGroup2(soap, regex8));
    }

    private ProvinceStat getProvinceStat(String provinceName) {
        if (!provinceStatMap.containsKey(provinceName)) {
            provinceStatMap.put(provinceName, new ProvinceStat());
        }
        return provinceStatMap.get(provinceName);
    }

    public Map<String, ProvinceStat> parse(String[] logFiles)
        throws IOException, DataFormatException {
        provinceStatMap = new HashMap<>();

        if (logFiles == null || logFiles.length == 0) {
            return provinceStatMap;
        }

        for (String logFile : logFiles) {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile));
            String line;
            System.out.println("parsing file \"" + logFile + "\"...");
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("//")) {
                    continue;
                }

                List<String> result;
                if ((result = regexGroup2(line, regex1)) != null) {
                    ProvinceStat provinceStat = getProvinceStat(result.get(0));
                    provinceStat.incrNumIP(Integer.parseInt(result.get(1)));
                } else if ((result = regexGroup2(line, regex2)) != null) {
                    ProvinceStat provinceStat = getProvinceStat(result.get(0));
                    provinceStat.incrNumSP(Integer.parseInt(result.get(1)));
                } else if ((result = regexGroup3(line, regex3)) != null) {
                    ProvinceStat source = getProvinceStat(result.get(0));
                    ProvinceStat target = getProvinceStat(result.get(1));
                    source.migrateIP(target, Integer.parseInt(result.get(2)));
                } else if ((result = regexGroup3(line, regex4)) != null) {
                    ProvinceStat source = getProvinceStat(result.get(0));
                    ProvinceStat target = getProvinceStat(result.get(1));
                    source.migrateSP(target, Integer.parseInt(result.get(2)));
                } else if ((result = regexGroup2(line, regex5)) != null) {
                    ProvinceStat provinceStat = getProvinceStat(result.get(0));
                    int num = Integer.parseInt(result.get(1));
                    provinceStat.incrNumDead(num);
                    provinceStat.decrNumIP(num);
                } else if ((result = regexGroup2(line, regex6)) != null) {
                    ProvinceStat provinceStat = getProvinceStat(result.get(0));
                    int num = Integer.parseInt(result.get(1));
                    provinceStat.incrNumCure(num);
                    provinceStat.decrNumIP(num);
                } else if ((result = regexGroup2(line, regex7)) != null) {
                    ProvinceStat provinceStat = getProvinceStat(result.get(0));
                    int num = Integer.parseInt(result.get(1));
                    provinceStat.incrNumIP(num);
                    provinceStat.decrNumSP(num);
                } else if ((result = regexGroup2(line, regex8)) != null) {
                    ProvinceStat provinceStat = getProvinceStat(result.get(0));
                    provinceStat.decrNumSP(Integer.parseInt(result.get(1)));
                } else {
                    // exception
                    System.out.println("exception");
                    throw new DataFormatException();
                }
            }
            bufferedReader.close();
        }
        return provinceStatMap;
    }
}

class ProvinceStat {

    private int numIP; // #infected

    private int numSP; // #suspected

    private int numCure; // #cured

    private int numDead; // #dead

    public ProvinceStat() {
        this.numIP = 0;
        this.numSP = 0;
        this.numCure = 0;
        this.numDead = 0;
    }

    public int getNumIP() {
        return numIP;
    }

    public int getNumSP() {
        return numSP;
    }

    public int getNumCure() {
        return numCure;
    }

    public int getNumDead() {
        return numDead;
    }

    public void incrNumIP(int variation) {
        this.numIP += variation;
    }

    public void decrNumIP(int variation) {
        this.numIP -= variation;
        if (this.numIP < 0) {
            throw new OutOfBoundException(String.valueOf(this.numIP), "0");
        }
    }

    public void incrNumSP(int variation) {
        this.numSP += variation;
    }

    public void decrNumSP(int variation) {
        this.numSP -= variation;
        if (this.numSP < 0) {
            throw new OutOfBoundException(String.valueOf(this.numSP), "0");
        }
    }

    public void incrNumCure(int variation) {
        this.numCure += variation;
    }

    public void decrNumCure(int variation) {
        this.numCure -= variation;
        if (this.numCure < 0) {
            throw new OutOfBoundException(String.valueOf(this.numCure), "0");
        }
    }

    public void incrNumDead(int variation) {
        this.numDead += variation;
    }

    public void decrNumDead(int variation) {
        this.numDead -= variation;
        if (this.numDead < 0) {
            throw new OutOfBoundException(String.valueOf(this.numDead), "0");
        }
    }

    public void migrateIP(ProvinceStat dest, int num) {
        this.numIP -= num;
        dest.numIP += num;
        if (this.numIP < 0) {
            throw new OutOfBoundException(String.valueOf(this.numIP), "0");
        }
    }

    public void migrateSP(ProvinceStat dest, int num) {
        this.numSP -= num;
        dest.numSP += num;
        if (this.numSP < 0) {
            throw new OutOfBoundException(String.valueOf(this.numSP), "0");
        }
    }

    @Override
    public String toString() {
        return "感染患者" + numIP + "人 "
            + "疑似患者" + numSP + "人 "
            + "治愈" + numCure + "人 "
            + "死亡" + numDead + "人";
    }
}

class OutOfBoundException extends RuntimeException {

    public OutOfBoundException(String number, String bound) {
        super(number + " out of bound " + bound);
    }
}
