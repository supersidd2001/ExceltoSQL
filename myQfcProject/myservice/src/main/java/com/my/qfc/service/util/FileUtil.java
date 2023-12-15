package com.my.qfc.service.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtil {

    public static void moveFile(String sourcePath, String destinationPath) throws IOException {
        Path source = Path.of(sourcePath);
        Path destination = Path.of(destinationPath, source.getFileName().toString());

        Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }
}
