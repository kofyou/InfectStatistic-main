import java.io.IOException;
import java.util.zip.DataFormatException;

/**
 * InfectStatistic
 *
 * @author xjliang
 * @version 1.0.1
 * @since 2020-01-11
 */
class InfectStatistic {

    public static void main(String[] args) {
        try {
            Lib.run(args);
        } catch (IOException | DataFormatException e) {
            e.printStackTrace();
        }
    }
}
