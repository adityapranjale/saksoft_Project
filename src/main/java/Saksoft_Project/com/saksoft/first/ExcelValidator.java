package Saksoft_Project.com.saksoft.first;

import Saksoft_Project.com.saksoft.first.constant.MessagePropertyConstants;
import Saksoft_Project.com.saksoft.first.exception.TransferCostException;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExcelValidator {
    public  void checkExcelValidaton() {
        String filePath = "path/to/your/excel/file.xlsx";

        // Step 0: Check if the file exists
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        // Step 1: Validate file name format
        String fileName = file.getName();
        if (!fileName.matches("(PC|GC)_\\d{2}-\\d{2}-\\d{4}\\.xlsx")) {
            System.out.println("Invalid file name format.");
            return;
        }

        // Step 2: Extract and validate the date from the file name
        String dateString = fileName.substring(3, 13);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate fileDate = LocalDate.parse(dateString, formatter);
        LocalDate currentDate = LocalDate.now();
        if (!fileDate.equals(currentDate)) {
            System.out.println("File date does not match the current date.");
            return;
        }

        // Step 3: Extract and validate the ProductGroup from the file name
        String productGroup = fileName.substring(0, 2);
        if (!productGroup.equals("PC") && !productGroup.equals("GC")) {
            System.out.println("Invalid ProductGroup specified in the file name.");
            return;
        }

        // Step 4: Read and validate the Excel file
        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming we're working with the first sheet

            // Iterate over rows
            for (Row row : sheet) {
                if (isEmptyRow(row)) {
                    System.out.println("Empty row found: " + (row.getRowNum() + 1));
                    return;
                }

                // Iterate over cells in the row
                for (Cell cell : row) {
                    if (isMissingColumn(cell)) {
                        System.out.println("Missing column found: " + cell.getColumnIndex());
                        return;
                    }

                    if (isMandatoryFieldEmpty(cell)) {
                        System.out.println("Mandatory field is empty: Row " + (row.getRowNum() + 1) +
                                ", Column " + (cell.getColumnIndex() + 1));
                        return;
                    }
                }
            }

            System.out.println("Validation successful.");
        } catch (Exception e) {
            System.out.println("Error reading the Excel file: " + e.getMessage());
        }
    }

    private static boolean isEmptyRow(Row row) {
        for (Cell cell : row) {
            if (cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private static boolean isMissingColumn(Cell cell) {
        return cell.getCellType() == CellType.BLANK;
    }

    private static boolean isMandatoryFieldEmpty(Cell cell) {
        return cell.getCellType() == CellType.BLANK;
    }
}
