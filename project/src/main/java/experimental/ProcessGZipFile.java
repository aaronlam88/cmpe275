package experimental;

import java.io.*;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

public class ProcessGZipFile {

    public static void main(String[] args) throws IOException {
        String filename = "/Users/aaronlam/Desktop/test_data/3.mesowest.out.gz";
        InputStream fileStream = new FileInputStream(filename);
        InputStream gzipStream = new GZIPInputStream(fileStream);
        Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
        BufferedReader buffered = new BufferedReader(decoder);
        int i = 0;
        for (String line = buffered.readLine(); i < 10 && line != null; ++i, line = buffered.readLine()) {
            line = line.trim();
            String[] tokens = line.split(" +");
            if (tokens.length == 16) {
                System.out.println(Arrays.toString(tokens));
            }
        }
    }
}
