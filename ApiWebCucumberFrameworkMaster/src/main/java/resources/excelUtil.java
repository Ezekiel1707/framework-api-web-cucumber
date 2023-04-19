package resources;
import base.BasePageUI;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class excelUtil extends BasePageUI {
    private static XSSFSheet excelSheet;
    private static XSSFWorkbook excelWorkbook;
    private static XSSFCell cell;

    private static void setExcelFile(String sheetName) throws IOException {
        String sheetPath = System.getProperty("user.dir")
                + "\\src\\main\\java\\resources\\TestData\\"
                + getGlobalValue("excelName");
        FileInputStream excelFile = new FileInputStream(sheetPath);
        excelWorkbook = new XSSFWorkbook(excelFile);

        for (int i = 0; i < excelWorkbook.getNumberOfSheets(); i++) {
            if (excelWorkbook.getSheetName(i).equalsIgnoreCase(sheetName)) {
                excelSheet = excelWorkbook.getSheet(excelWorkbook.getSheetName(i));
                break;
            }
        }
    }

    public static int getRows(String sheetName) throws Exception {
        setExcelFile(sheetName);
        return excelSheet.getLastRowNum();
    }

    private static int getDataRow(String dataKey) {
        int rowCount = excelSheet.getLastRowNum();
        for (int row = 0; row <= rowCount; row++) {
            if (excelUtil.getCellData(row, 0).equalsIgnoreCase(dataKey)) {
                return row;
            }
        }
        return 0;
    }

    private static String getCellData(int rowNumb, int colNumb) {
        cell = excelSheet.getRow(rowNumb).getCell(colNumb);

        if (cell.getCellType() == CellType.NUMERIC) {
            cell.setCellType(CellType.STRING);
        }
        return cell.getStringCellValue();
    }

    public static void setCellData(String result, int rowNumb, int colNumb, String sheetPath, String sheetName)
            throws Exception {
        try {
            XSSFRow row = excelSheet.getRow(rowNumb);
            cell = row.getCell(colNumb);
            if (cell == null) {
                cell = row.createCell(colNumb);
                cell.setCellValue(result);
            } else {
                cell.setCellValue(result);
            }

            FileOutputStream fileOut = new FileOutputStream(sheetPath + sheetName);
            excelWorkbook.write(fileOut);
            fileOut.flush();
            fileOut.close();

        } catch (Exception exp) {
        }
    }

    public static Map<String, String> getData(String dataKey, String sheetName) throws Exception {
        Map<String, String> dataMap = new HashMap<String, String>();
        setExcelFile(sheetName);

        int dataRow = getDataRow(dataKey.trim());

        if (dataRow == 0) {
            throw new Exception("NO DATA FOUND for dataKey: " + dataKey);
        } else {
            int columnCount = excelSheet.getRow(dataRow).getLastCellNum();
            for (int i = 0; i < columnCount; i++) {
                cell = excelSheet.getRow(dataRow).getCell(i);
                String cellData = null;
                if (cell != null) {
                    if (cell.getCellType() == CellType.NUMERIC) {
                        cell.setCellType(CellType.STRING);
                    }
                    cellData = cell.getStringCellValue();
                }
                dataMap.put(excelSheet.getRow(0).getCell(i).getStringCellValue(), cellData);
            }
        }
        return dataMap;
    }
}