package io.snplab.gsdk.common.util;

import java.util.Arrays;
import java.util.List;

public class FileExtension {

    public static final List<String> EXCEL_FILE_EXTENSION = Arrays.asList("xls", "xlsx", "csv");

    public static boolean isValidExcelExtension(String extension) {
        return EXCEL_FILE_EXTENSION.contains(extension);
    }
}
