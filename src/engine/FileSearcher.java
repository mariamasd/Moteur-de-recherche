package engine;

import java.io.*;

public class FileSearcher {

    public static void search(File file, String pattern, java.util.function.Consumer<String> output)
            throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int lineNum = 1;

        while ((line = br.readLine()) != null) {
            if (PatternDispatcher.match(line, pattern)) {
                output.accept(lineNum + ": " + line);
            }
            lineNum++;
        }
        br.close();
    }
}