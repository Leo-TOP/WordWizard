package wordwizard.service.export.exporters;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import wordwizard.models.Definition;
import wordwizard.models.Word;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ExcelExporter implements Exporter {
    private static final String[] HEADERS = {
            "Word", "Part of Speech", "Definition", "Source", "Example"
    };

    @Override
    public String getFormat() {
        return "excel";
    }

    @Override
    public byte[] export(Map<String, List<Word>> themedWords) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            for (var entry : themedWords.entrySet()) {
                Sheet sheet = workbook.createSheet(sanitizeSheetName(entry.getKey()));
                createHeaderRow(workbook, sheet);
                fillDataRows(sheet, entry.getValue());
                autoSizeColumns(sheet);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Excel file", e);
        }
    }

    private String sanitizeSheetName(String name) {
        return name.replaceAll("[\\[\\]*?:/\\\\]", "").substring(0, Math.min(31, name.length()));
    }

    private void createHeaderRow(Workbook workbook, Sheet sheet) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        Row header = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(style);
        }
    }

    private void fillDataRows(Sheet sheet, List<Word> words) {
        int rowNum = 1;
        for (Word word : words) {
            for (Definition def : word.definitions()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(word.word());
                row.createCell(1).setCellValue(def.partOfSpeech() != null ? def.partOfSpeech() : "");
                row.createCell(2).setCellValue(def.text());
                row.createCell(3).setCellValue(def.source() != null ? def.source() : "");
                row.createCell(4).setCellValue(def.example() != null ? def.example() : "");
            }
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
