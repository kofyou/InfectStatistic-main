import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Lib
 *
 * @author Xily9
 * @version 1.0
 * @since 1.0
 */
public class Lib {
    public static List<String> getFiles(String dir) {
        File file = new File(dir);
        File[] files = file.listFiles();
        if (files != null) {
            return Stream.of(files).map(File::getName).sorted().collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


}
