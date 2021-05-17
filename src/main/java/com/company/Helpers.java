package com.company;

import java.io.File;

public class Helpers {

    public static String noExtensionFileName(File file) {
        return file.getName().replaceFirst("[.][^.]+$", "");
    }
}
