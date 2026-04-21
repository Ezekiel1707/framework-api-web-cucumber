package resources;

import base.basePageUI;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class excelUtil extends basePageUI {
    private static XSSFSheet excelSheet;
    private static XSSFWorkbook excelWorkbook;
    private static FileInputStream excelFile;

    private static void setExcelFile(String sheetName) throws IOException {
        closeExcelFile();

        String sheetPath = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "resources", "testData",
                getGlobalValue("excelName")).toString();

        excelFile = new FileInputStream(sheetPath);
        excelWorkbook = new XSSFWorkbook(excelFile);

        for (int i = 0; i < excelWorkbook.getNumberOfSheets(); i++) {
            if (excelWorkbook.getSheetName(i).equalsIgnoreCase(sheetName)) {
                excelSheet = excelWorkbook.getSheet(excelWorkbook.getSheetName(i));
                break;
            }
        }

        if (excelSheet == null) {
            throw new IOException("Sheet '" + sheetName + "' not found in workbook");
        }
    }

    public static void closeExcelFile() {
        try {
            if (excelWorkbook != null) {
                excelWorkbook.close();
                excelWorkbook = null;
            }
            if (excelFile != null) {
                excelFile.close();
                excelFile = null;
            }
        } catch (IOException e) {
            System.err.println("Error closing Excel file: " + e.getMessage());
        }
    }

    public static int getRows(String sheetName) throws Exception {
        setExcelFile(sheetName);
        return excelSheet.getLastRowNum();
    }

    private static int getDataRow(String dataKey) {
        int rowCount = excelSheet.getLastRowNum();
        for (int row = 0; row <= rowCount; row++) {
            XSSFRow currentRow = excelSheet.getRow(row);
            if (currentRow != null) {
                XSSFCell currentCell = currentRow.getCell(0);
                if (currentCell != null && getCellData(row, 0).equalsIgnoreCase(dataKey)) {
                    return row;
                }
            }
        }
        return -1; // Return -1 instead of 0 to avoid confusion with row 0
    }

    private static String getCellData(int rowNumb, int colNumb) {
        XSSFRow row = excelSheet.getRow(rowNumb);
        if (row == null) {
            return "";
        }

        XSSFCell cell = row.getCell(colNumb);
        if (cell == null) {
            return "";
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            cell.setCellType(CellType.STRING);
        }
        return cell.getStringCellValue();
    }

    public static void setCellData(String result, int rowNumb, int colNumb, String sheetPath, String sheetName)
            throws Exception {
        XSSFRow row = excelSheet.getRow(rowNumb);
        if (row == null) {
            throw new Exception("Row " + rowNumb + " does not exist in sheet");
        }

        XSSFCell cell = row.getCell(colNumb);
        if (cell == null) {
            cell = row.createCell(colNumb);
        }
        cell.setCellValue(result);

        try (FileOutputStream fileOut = new FileOutputStream(sheetPath + sheetName)) {
            excelWorkbook.write(fileOut);
            fileOut.flush();
        } catch (IOException e) {
            throw new Exception("Error writing to Excel file: " + e.getMessage(), e);
        }
    }

    public static Map<String, String> getData(String dataKey, String sheetName) throws Exception {
        Map<String, String> dataMap = new HashMap<String, String>();
        setExcelFile(sheetName);

        int dataRow = getDataRow(dataKey.trim());

        if (dataRow == -1) {
            throw new Exception("NO DATA FOUND for dataKey: " + dataKey);
        }

        XSSFRow row = excelSheet.getRow(dataRow);
        if (row == null) {
            throw new Exception("Unable to read data row: " + dataRow);
        }

        int columnCount = row.getLastCellNum();
        XSSFRow headerRow = excelSheet.getRow(0);
        if (headerRow == null) {
            throw new Exception("Header row not found in sheet");
        }

        for (int i = 0; i < columnCount; i++) {
            XSSFCell cell = row.getCell(i);
            String cellData = null;
            if (cell != null) {
                if (cell.getCellType() == CellType.NUMERIC) {
                    cell.setCellType(CellType.STRING);
                }
                cellData = cell.getStringCellValue();
            }

            XSSFCell headerCell = headerRow.getCell(i);
            if (headerCell != null) {
                String columnHeader = headerCell.getStringCellValue();
                dataMap.put(columnHeader, cellData);
            }
        }
        return dataMap;
    }
}