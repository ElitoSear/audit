package edward.audit.audit;

import edward.audit.audit.queries.Monitor;

import java.io.File;
import java.nio.file.Path;

public class QueriesManager {
    public static final Path PATH = Path.of("C:/ProgramData/respair/queries.ini");

    public static void main(String[] args) {
        File fileToWatch = new File(PATH.toString());

        Monitor monitor = new Monitor(fileToWatch);
        monitor.start();
    }

}
