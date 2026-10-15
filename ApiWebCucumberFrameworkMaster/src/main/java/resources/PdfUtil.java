package resources;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

public class PdfUtil {

    private PdfUtil() {}

    private static final Logger LOGGER = Logger.getLogger(PdfUtil.class.getName());
    public static List<Table> getTables(Path pdfPath) throws IOException {
        List<Table> allTables = new ArrayList<>();

        try (PDDocument pdDocument = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfPath.toFile()));
             ObjectExtractor extractor = new ObjectExtractor(pdDocument)) {

            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

            PageIterator pages = extractor.extract();

            while (pages.hasNext()) {
                Page page = pages.next();
                List<Table> tables = sea.extract(page);
                allTables.addAll(tables);
            }

        }
        return allTables;
    }

    public static List<Map<String, String>> tableToMap(Table table) {
        List<Map<String, String>> rows = new ArrayList<>();
        List<List<RectangularTextContainer>> rawRows = table.getRows();

        if (rawRows.size() < 2) {
            return rows;
        }

        List<String> headers = new ArrayList<>();
        Map<String, Integer> seen = new LinkedHashMap<>();

        for (int i = 0; i < rawRows.get(0).size(); i++) {
            String header = rawRows.get(0).get(i).getText().trim();

            if (header.isEmpty()) {
                header = "column_" + i;
                LOGGER.warning("Blank header at column index " + i
                        + "; renamed to \"" + header + "\".");
            }

            if (seen.containsKey(header)) {
                int count = seen.get(header) + 1;
                seen.put(header, count);
                String deduped = header + "_" + count;
                LOGGER.warning("Duplicate header \"" + header + "\" at column index " + i
                        + "; renamed to \"" + deduped + "\".");
                header = deduped;
            } else {
                seen.put(header, 0);
            }

            headers.add(header);
        }
        // Find the maximum number of columns across all data rows
        int maxCols = rawRows.stream()
                .skip(1)
                .mapToInt(List::size)
                .max()
                .orElse(headers.size());
        // If data rows have more columns than headers, generate extra header names
        if (maxCols > headers.size()) {
            LOGGER.warning("Header row has " + headers.size() + " columns but data rows have up to "
                    + maxCols + ". Extra columns will be named extra_col_N.");
            for (int i = headers.size(); i < maxCols; i++) {
                headers.add("extra_col_" + i);
            }
        }

        for (int i = 1; i < rawRows.size(); i++) {
            Map<String, String> rowMap = new LinkedHashMap<>();
            List<RectangularTextContainer> row = rawRows.get(i);

            for (int j = 0; j < headers.size(); j++) {
                String value = (j < row.size()) ? row.get(j).getText().trim() : "";
                rowMap.put(headers.get(j), value);
            }

            rows.add(rowMap);
        }

        return rows;
    }

    public static List<Map<String, String>> getTableByHeader(Path pdfPath, String header)
            throws IOException {

        List<List<Map<String, String>>> matches = getAllTablesByHeader(pdfPath, header);

        if (matches.isEmpty()) {
            throw new IllegalArgumentException("No table found with header: " + header);
        }
        if (matches.size() > 1) {
            throw new IllegalStateException(
                    matches.size() + " tables found with header " + header + ". "
                    + "Use getAllTablesByHeader() to retrieve all of them.");
        }

        return matches.get(0);
    }
    public static List<List<Map<String, String>>> getAllTablesByHeader(Path pdfPath, String header)
            throws IOException {

        List<List<Map<String, String>>> matches = new ArrayList<>();

        for (Table table : getTables(pdfPath)) {
            List<List<RectangularTextContainer>> rows = table.getRows();
            if (rows.isEmpty()) {
                continue;
            }
            for (RectangularTextContainer cell : rows.get(0)) {
                if (cell.getText().trim().equalsIgnoreCase(header)) {
                    matches.add(tableToMap(table));
                    break; // header found in this table, no need to check other cells
                }
            }
        }

        return matches;
    }
    public static String getCellValue(
            List<Map<String, String>> table,
            int rowIndex,
            String columnName) {

        if (rowIndex < 0 || rowIndex >= table.size()) {
            throw new IndexOutOfBoundsException(
                    "Row index " + rowIndex + " is out of range [0, " + (table.size() - 1) + "]");
        }

        String value = table.get(rowIndex).get(columnName);

        if (value == null) {
            throw new NoSuchElementException("Column not found: \"" + columnName + "\"");
        }

        return value;
    }

    public static String getCellValue(
            List<Map<String, String>> table,
            String searchColumn,
            String searchValue,
            String targetColumn) {

        return getRow(table, searchColumn, searchValue)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "No row found where " +
                                        searchColumn + "=" + searchValue))
                .get(targetColumn);
    }
    public static String getCellValue(
            Table table,
            int rowIndex,
            int columnIndex) {

        List<List<RectangularTextContainer>> rows = table.getRows();

        if (rowIndex < 0 || rowIndex >= rows.size()) {
            throw new IndexOutOfBoundsException(
                    "Row index " + rowIndex +
                            " out of range [0, " + (rows.size() - 1) + "]");
        }

        List<RectangularTextContainer> row = rows.get(rowIndex);

        if (columnIndex < 0 || columnIndex >= row.size()) {
            throw new IndexOutOfBoundsException(
                    "Column index " + columnIndex +
                            " out of range [0, " + (row.size() - 1) + "]");
        }

        String value = row.get(columnIndex).getText();

        return value != null ? value.trim() : "";
    }
    public static Optional<Map<String, String>> getRow(

            List<Map<String, String>> table,
            String searchColumn,
            String searchValue) {

        for (Map<String, String> row : table) {
            if (searchValue.equals(row.get(searchColumn))) {
                return Optional.of(row);
            }
        }
        return Optional.empty();
    }
    public static List<List<List<String>>> extractAllTablesAsStrings(Path pdfPath) throws IOException {

        List<Table> tables = getTables(pdfPath);

        List<List<List<String>>> result = new ArrayList<>();

        for (Table table : tables) {

            List<List<List<String>>> singleTable = new ArrayList<>();
            List<List<String>> normalizedTable = new ArrayList<>();

            List<List<RectangularTextContainer>> rows = table.getRows();

            for (List<RectangularTextContainer> row : rows) {

                List<String> normalizedRow = new ArrayList<>();

                for (RectangularTextContainer cell : row) {
                    String value = cell.getText();
                    normalizedRow.add(value == null ? "" : value.trim());
                }

                normalizedTable.add(normalizedRow);
            }

            result.add(normalizedTable);
        }

        return result;
    }
    public static void assertPdfTablesEqual(Path pdf1, Path pdf2) throws IOException {

        List<List<List<String>>> tables1 = extractAllTablesAsStrings(pdf1);
        List<List<List<String>>> tables2 = extractAllTablesAsStrings(pdf2);

        if (tables1.size() != tables2.size()) {
            throw new AssertionError(
                    "Table count mismatch. PDF1 table Size=" + tables1.size()
                            + ", PDF2 table Size=" + tables2.size());
        }

        for (int t = 0; t < tables1.size(); t++) {

            List<List<String>> table1 = tables1.get(t);
            List<List<String>> table2 = tables2.get(t);

            if (table1.size() != table2.size()) {
                throw new AssertionError("Row count mismatch in table " + t);
            }

            for (int r = 0; r < table1.size(); r++) {

                List<String> row1 = table1.get(r);
                List<String> row2 = table2.get(r);

                if (row1.size() != row2.size()) {
                    throw new AssertionError(
                            "Column count mismatch at table " + t + ", row " + r);
                }

                for (int c = 0; c < row1.size(); c++) {

                    String v1 = normalize(row1.get(c));
                    String v2 = normalize(row2.get(c));

                    if (!v1.equals(v2)) {
                        throw new AssertionError(
                                "Mismatch at table " + t +
                                        ", row " + r +
                                        ", col " + c +
                                        " | PDF1=" + v1 +
                                        " | PDF2=" + v2);
                    }
                }
            }
        }
    }
    public static boolean arePdfTablesEqual(Path pdf1, Path pdf2)
            throws IOException {

        List<List<List<String>>> tables1 = extractAllTablesAsStrings(pdf1);
        List<List<List<String>>> tables2 = extractAllTablesAsStrings(pdf2);

        return normalizeTables(tables1).equals(normalizeTables(tables2));
    }

    private static List<List<List<String>>> normalizeTables(
            List<List<List<String>>> tables) {

        List<List<List<String>>> normalizedTables = new ArrayList<>();

        for (List<List<String>> table : tables) {

            List<List<String>> normalizedTable = new ArrayList<>();

            for (List<String> row : table) {

                List<String> normalizedRow = new ArrayList<>();

                for (String cell : row) {
                    normalizedRow.add(normalize(cell));
                }

                normalizedTable.add(normalizedRow);
            }

            normalizedTables.add(normalizedTable);
        }

        return normalizedTables;
    }
    private static String normalize(String value) {

        if (value == null) return "";

        return value
                .replace("\u00A0", " ")   // non-breaking space
                .replaceAll("\\s+", " ")  // collapse spaces
                .trim();
    }
    public static String extractText(Path pdfPath) throws IOException {
        try (PDDocument document = Loader.loadPDF(
                new RandomAccessReadBufferedFile(pdfPath.toFile()))) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true); // left-to-right, top-to-bottom order
            return stripper.getText(document);

        }
    }

    public static String extractTextFromPage(Path pdfPath, int pageNumber) throws IOException {
        try (PDDocument document = Loader.loadPDF(
                new RandomAccessReadBufferedFile(pdfPath.toFile()))) {
            int total = document.getNumberOfPages();
            if (pageNumber < 1 || pageNumber > total) {
                throw new IllegalArgumentException(
                        "Page number " + pageNumber + " is out of range [1, " + total + "]");
            }
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setStartPage(pageNumber);
            stripper.setEndPage(pageNumber);
            return stripper.getText(document);
        }
    }

    public static String extractTextFromRegion(Path pdfPath, int pageNumber, Rectangle region)
            throws IOException {
        try (PDDocument document = Loader.loadPDF(
                new RandomAccessReadBufferedFile(pdfPath.toFile()))) {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            stripper.addRegion("target", region);
            PDPage page = document.getPage(pageNumber - 1); // 0-based index
            stripper.extractRegions(page);
            return stripper.getTextForRegion("target").trim();
        }
    }

    public static int getPageCount(Path pdfPath) throws IOException {
        try (PDDocument document = Loader.loadPDF(
                new RandomAccessReadBufferedFile(pdfPath.toFile()))) {
            return document.getNumberOfPages();
        }
    }
    public static Map<String, String> getMetadata(Path pdfPath) throws IOException {
        try (PDDocument document = Loader.loadPDF(
                new RandomAccessReadBufferedFile(pdfPath.toFile()))) {
            PDDocumentInformation info = document.getDocumentInformation();
            Map<String, String> metadata = new LinkedHashMap<>();
            metadata.put("Title",        info.getTitle());
            metadata.put("Author",       info.getAuthor());
            metadata.put("Subject",      info.getSubject());
            metadata.put("Keywords",     info.getKeywords());
            metadata.put("Creator",      info.getCreator());
            metadata.put("Producer",     info.getProducer());
            metadata.put("CreationDate", info.getCreationDate() != null
                    ? info.getCreationDate().getTime().toString() : null);
            metadata.put("ModifiedDate", info.getModificationDate() != null
                    ? info.getModificationDate().getTime().toString() : null);
            return metadata;
        }
    }
    public static BufferedImage renderPageAsImage(Path pdfPath, int pageNumber, float dpi)
            throws IOException {
        try (PDDocument document = Loader.loadPDF(
                new RandomAccessReadBufferedFile(pdfPath.toFile()))) {
            int total = document.getNumberOfPages();
            if (pageNumber < 1 || pageNumber > total) {
                throw new IllegalArgumentException(
                        "Page number " + pageNumber + " is out of range [1, " + total + "]");
            }
            PDFRenderer renderer = new PDFRenderer(document);
            return renderer.renderImageWithDPI(pageNumber - 1, dpi, ImageType.RGB);
        }
    }
}