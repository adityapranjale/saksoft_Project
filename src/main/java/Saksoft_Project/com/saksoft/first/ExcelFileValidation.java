package Saksoft_Project.com.saksoft.first;

import Saksoft_Project.com.saksoft.first.constant.MessagePropertyConstants;
import Saksoft_Project.com.saksoft.first.exception.TransferCostException;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.text.SimpleDateFormat;

@Slf4j
public class ExcelFileValidation {
    @Autowired
    private MessageSource messageSource;


    public void checkExcelFileValidation(String filePath, String category) throws IOException, TransferCostException {
        File file = new File("ExceptionMessagaes.properties");
        FileInputStream fileInput = new FileInputStream(file);
        FileInputStream fileInputStream = new FileInputStream(filePath);
        File fileExcel = new File(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        validateFile(fileExcel);
        checkMissingColumns(sheet);
        checkColumnOrder(sheet, category);
        checkEmptyRows(sheet);
        checkEmptyCells(sheet);
    }
    private void validateFile(File file) throws TransferCostException {
        String fileName = file.getName();
        if (!fileName.matches(MessagePropertyConstants.FILE_FORMAT_REGEX)) {
            System.out.println("Invalid file name format.");
            return;
        }
    }

    private void checkColumnOrder(Sheet sheet, String category) throws TransferCostException {
        String[] expectedColumnOrder = {};
        Row row = sheet.getRow(Integer.parseInt(MessagePropertyConstants.TARGET_ROW));
        if (category.equals(MessagePropertyConstants.VEH_TYPE_PC)) {
            expectedColumnOrder = new String[]{"Customer Center", "Type Class", "Order Date From", "Order Date To", "Transfer Cost"};
        } else if (category.equals(MessagePropertyConstants.VEH_TYPE_G_CLASS)) {
            expectedColumnOrder = new String[]{"Customer Center", "Type Class", "Baumuster", "Option code", "Order Date From", "Order Date To", "Transfer Cost"};
        } else if (category.equals(MessagePropertyConstants.VEH_TYPE_VAN)) {
            expectedColumnOrder = new String[]{"Plant Name", "Baumuster", "Order Date From", "Order Date To", "Transfer Cost"};
        }
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            String columnName = cell.getStringCellValue();
            if (!columnName.equals(expectedColumnOrder[i])) {
                throw new TransferCostException(
                        messageSource.getMessage(MessagePropertyConstants.ERRORCODE18, null, Locale.ENGLISH),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void checkMissingColumns(Sheet sheet) throws TransferCostException {
        Row headerRow = sheet.getRow(Integer.parseInt(MessagePropertyConstants.TARGET_ROW));
        Row dataRow = sheet.getRow(1);

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell headerCell = headerRow.getCell(i);
            Cell dataCell = dataRow.getCell(i);

            String headerValue = headerCell.getStringCellValue().trim();
            String dataValue = dataCell.getStringCellValue().trim();

            if (!headerValue.equals(dataValue)) {
                throw new TransferCostException(
                        messageSource.getMessage(String.format(MessagePropertyConstants.ERRORCODE19, headerValue), null, Locale.ENGLISH),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void checkEmptyRows(Sheet sheet) throws TransferCostException {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) {
                throw new TransferCostException(
                        messageSource.getMessage(MessagePropertyConstants.ERRORCODE20, null, Locale.ENGLISH),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void checkEmptyCells(Sheet sheet) throws TransferCostException {
        List<String> headerRow = getRowValues(sheet.getRow(1));

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            List<String> rowData = getRowValues(row);

            for (int j = 0; j < headerRow.size(); j++) {
                String headerValue = headerRow.get(j);
                String cellvalue = rowData.get(j);
                if (headerValue.equalsIgnoreCase("Order Date From") || headerValue.equalsIgnoreCase("Order Date To")) {
                    this.validateDateFormat(cellvalue);
                }
                if (cellvalue.isEmpty()) {
                    throw new TransferCostException(
                            messageSource.getMessage(String.format(MessagePropertyConstants.ERRORCODE19, headerValue), null, Locale.ENGLISH),
                            HttpStatus.BAD_REQUEST);
                }
            }
        }
    }

    private void validateDateFormat(String cellvalue) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD-MM-YYYY");
        String date = simpleDateFormat.format(cellvalue);

    }

    private static boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private static List<String> getRowValues(Row row) {
        List<String> values = new ArrayList<>();

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);

            values.add(cell.getStringCellValue().trim());
        }
        return values;
    }
}
