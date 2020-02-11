import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lib
 * TODO
 *
 * @author xjliang
 * @version 1.0.0
 * @since 2020-01-11
 */
public class Lib {

    public static void main(String[] args) {
        CommandArgs commandArgs = ArgsParser.parse(args);
        System.out.println(commandArgs);
    }
}

class ArgsParser {

    private static final String[] requiredOptions = {"log", "out"};

    public static boolean isOption(String token) {
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
// TODO
}

class ProvinceStat {
// TODO
}
