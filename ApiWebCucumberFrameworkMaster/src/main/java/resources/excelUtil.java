
package resources;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static base.basePageUI.getGlobalValue;

public final class excelUtil {

    private static final DataFormatter FORMATTER = new DataFormatter();

    // Thread-safe session mode
    private static final ThreadLocal<XSSFWorkbook> workbookHolder = new ThreadLocal<>();
    private static final ThreadLocal<FileInputStream> inputStreamHolder = new ThreadLocal<>();

    private excelUtil() {
    }

    private static Path getExcelPath() throws IOException {

        return Paths.get(
                System.getProperty("user.dir"),
                "src",
                "main",
                "java",
                "resources",
                "testData",
                getGlobalValue("excelName"));
    }

    // ============================================================
    // SESSION MODE (Large files / Parallel execution)
    // ============================================================

    public static void openExcelFile() throws IOException {

        if (workbookHolder.get() != null) {
            return;
        }

        FileInputStream fis = new FileInputStream(getExcelPath().toFile());
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        inputStreamHolder.set(fis);
        workbookHolder.set(workbook);
    }

    public static void closeExcelFile() {

        try {

            XSSFWorkbook workbook = workbookHolder.get();
            FileInputStream fis = inputStreamHolder.get();

            if (workbook != null) {
                workbook.close();
            }

            if (fis != null) {
                fis.close();
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to close Excel file.", e);
        } finally {
            workbookHolder.remove();
            inputStreamHolder.remove();
        }
    }

    // ============================================================
    // COMMON METHODS
    // ============================================================

    private static XSSFSheet getSheet(XSSFWorkbook workbook, String sheetName) {

        XSSFSheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            throw new IllegalArgumentException(
                    "Sheet '" + sheetName + "' does not exist.");
        }

        return sheet;
    }

    private static String getCellValue(XSSFRow row, int column) {

        if (row == null) {
            return "";
        }

        XSSFCell cell = row.getCell(column);

        if (cell == null) {
            return "";
        }

        return FORMATTER.formatCellValue(cell);
    }

    private static int findDataRow(XSSFSheet sheet, String dataKey) {

        int lastRow = sheet.getLastRowNum();

        for (int row = 1; row <= lastRow; row++) {

            String value = getCellValue(sheet.getRow(row), 0);

            if (value.equalsIgnoreCase(dataKey.trim())) {
                return row;
            }
        }

        return -1;
    }

    // ============================================================
    // STATELESS MODE (Default)
    // ============================================================

    public static int getRows(String sheetName) throws IOException {

        try (FileInputStream fis = new FileInputStream(getExcelPath().toFile());
                XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            return getSheet(workbook, sheetName).getLastRowNum() + 1;
        }
    }

    public static Map<String, String> getData(String dataKey,
            String sheetName) throws IOException {

        try (FileInputStream fis = new FileInputStream(getExcelPath().toFile());
                XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            return getData(dataKey, sheetName, workbook);
        }
    }

    public static String getValue(String dataKey,
            String sheetName,
            String columnName) throws IOException {

        return getData(dataKey, sheetName).get(columnName);
    }

    public static String getValue(String dataKey,
            String sheetName,
            int columnIndex) throws IOException {

        try (FileInputStream fis = new FileInputStream(getExcelPath().toFile());
                XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = getSheet(workbook, sheetName);

            int dataRow = findDataRow(sheet, dataKey);

            if (dataRow == -1) {
                throw new IllegalArgumentException(
                        "No data found for key: " + dataKey);
            }

            return getCellValue(sheet.getRow(dataRow), columnIndex);
        }
    }

    public static String getValue(int rowIndex,
            String sheetName,
            int columnIndex) throws IOException {

        try (FileInputStream fis = new FileInputStream(getExcelPath().toFile());
                XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = getSheet(workbook, sheetName);

            return getCellValue(sheet.getRow(rowIndex), columnIndex);
        }
    }

    public static void setCellData(String value,
            int rowNum,
            int colNum,
            String sheetName) throws IOException {

        String filePath = getExcelPath().toString();

        try (FileInputStream fis = new FileInputStream(filePath);
                XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = getSheet(workbook, sheetName);

            XSSFRow row = sheet.getRow(rowNum);

            if (row == null) {
                row = sheet.createRow(rowNum);
            }

            XSSFCell cell = row.getCell(colNum);

            if (cell == null) {
                cell = row.createCell(colNum);
            }

            cell.setCellValue(value);

            fis.close();

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }

    // ============================================================
    // SESSION MODE METHODS
    // ============================================================

    public static Map<String, String> getDataFromOpenWorkbook(String dataKey,
            String sheetName) {

        XSSFWorkbook workbook = workbookHolder.get();

        if (workbook == null) {
            throw new IllegalStateException(
                    "Workbook not open. Call openExcelFile() first.");
        }

        return getData(dataKey, sheetName, workbook);
    }

    public static String getValueFromOpenWorkbook(String dataKey,
            String sheetName,
            String columnName) {

        return getDataFromOpenWorkbook(dataKey, sheetName)
                .get(columnName);
    }

    public static String getValueFromOpenWorkbook(String dataKey,
            String sheetName,
            int columnIndex) {

        XSSFWorkbook workbook = workbookHolder.get();

        if (workbook == null) {
            throw new IllegalStateException(
                    "Workbook not open. Call openExcelFile() first.");
        }

        XSSFSheet sheet = getSheet(workbook, sheetName);

        int dataRow = findDataRow(sheet, dataKey);

        if (dataRow == -1) {
            throw new IllegalArgumentException(
                    "No data found for key: " + dataKey);
        }

        return getCellValue(sheet.getRow(dataRow), columnIndex);
    }

    public static String getValueFromOpenWorkbook(int rowIndex,
            String sheetName,
            int columnIndex) {

        XSSFWorkbook workbook = workbookHolder.get();

        if (workbook == null) {
            throw new IllegalStateException(
                    "Workbook not open. Call openExcelFile() first.");
        }

        XSSFSheet sheet = getSheet(workbook, sheetName);

        return getCellValue(sheet.getRow(rowIndex), columnIndex);
    }

    public static int getRowsFromOpenWorkbook(String sheetName) {

        XSSFWorkbook workbook = workbookHolder.get();

        if (workbook == null) {
            throw new IllegalStateException(
                    "Workbook not open. Call openExcelFile() first.");
        }

        return getSheet(workbook, sheetName).getLastRowNum() + 1;
    }

    public static void setCellDataInOpenWorkbook(String value,
            int rowNum,
            int colNum,
            String sheetName) throws IOException {

        XSSFWorkbook workbook = workbookHolder.get();

        if (workbook == null) {
            throw new IllegalStateException(
                    "Workbook not open. Call openExcelFile() first.");
        }

        XSSFSheet sheet = getSheet(workbook, sheetName);

        XSSFRow row = sheet.getRow(rowNum);

        if (row == null) {
            row = sheet.createRow(rowNum);
        }

        XSSFCell cell = row.getCell(colNum);

        if (cell == null) {
            cell = row.createCell(colNum);
        }

        cell.setCellValue(value);

        try (FileOutputStream fos = new FileOutputStream(getExcelPath().toFile())) {
            workbook.write(fos);
        }
    }

    // ============================================================
    // INTERNAL DATA EXTRACTION
    // ============================================================

    private static Map<String, String> getData(String dataKey,
            String sheetName,
            XSSFWorkbook workbook) {

        Map<String, String> dataMap = new HashMap<>();

        XSSFSheet sheet = getSheet(workbook, sheetName);

        int dataRow = findDataRow(sheet, dataKey);

        if (dataRow == -1) {
            throw new IllegalArgumentException(
                    "No data found for key: " + dataKey);
        }

        XSSFRow headerRow = sheet.getRow(0);
        XSSFRow row = sheet.getRow(dataRow);

        int columnCount = headerRow.getLastCellNum();

        for (int col = 0; col < columnCount; col++) {

            String header = getCellValue(headerRow, col);
            String value = getCellValue(row, col);

            dataMap.put(header, value);
        }

        return dataMap;
    }
}
