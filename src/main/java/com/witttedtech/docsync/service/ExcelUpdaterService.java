package com.witttedtech.docsync.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class ExcelUpdaterService {

    public File updateExcel(String excelPath, Map<Integer, String> emailPages) throws IOException {
        FileInputStream fis = new FileInputStream(excelPath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        List<String> unmatchedEntries = new ArrayList<>();

        // Process each extracted email start page
        for (Map.Entry<Integer, String> entry : emailPages.entrySet()) {
            int pdfPageNumber = entry.getKey();
            String emailDateTime = entry.getValue();
            boolean isUpdated = false;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Cell dateCell = row.getCell(0);  // Assuming Date is in Column 0
                Cell timeCell = row.getCell(1);  // Assuming Time is in Column 1
                Cell pageNoCell = row.createCell(2, CellType.STRING); // Column for Page Number

                if (dateCell != null && timeCell != null) {
                    String sheetDate = dateCell.toString().trim();
                    String sheetTime = timeCell.toString().trim();
                    String sheetDateTime = sheetDate + " " + sheetTime;

                    if (sheetDateTime.equals(emailDateTime)) {
                        if (pageNoCell.getStringCellValue().isEmpty()) {
                            pageNoCell.setCellValue(pdfPageNumber);
                            isUpdated = true;
                            break;
                        }
                    }
                }
            }

            // If no match found in Excel, print it in the console
            if (!isUpdated) {
                unmatchedEntries.add(emailDateTime + " (Page: " + pdfPageNumber + ")");
            }
        }

        // Save the updated Excel file
        File updatedFile = new File("uploads/Updated_" + new File(excelPath).getName());
        FileOutputStream fos = new FileOutputStream(updatedFile);
        workbook.write(fos);
        workbook.close();
        fos.close();
        fis.close();

        // Print unmatched entries
        if (!unmatchedEntries.isEmpty()) {
            System.out.println("⚠️ Unmatched Entries Found:");
            for (String entry : unmatchedEntries) {
                System.out.println(entry);
            }
        }

        return updatedFile;
    }
}

