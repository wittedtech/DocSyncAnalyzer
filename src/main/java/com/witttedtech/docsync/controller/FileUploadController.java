package com.witttedtech.docsync.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.witttedtech.docsync.service.ExcelUpdaterService;
import com.witttedtech.docsync.service.PdfProcessorService;

@Controller
public class FileUploadController {

    @Autowired
    private PdfProcessorService pdfProcessorService;

    @Autowired
    private ExcelUpdaterService excelUpdaterService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads"; 

    @GetMapping("/")
    public String showUploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("pdfFile") MultipartFile pdfFile,
                                   @RequestParam("excelFile") MultipartFile excelFile,
                                   Model model) {

        if (pdfFile.isEmpty() || excelFile.isEmpty()) {
            model.addAttribute("message", "Please upload both files!");
            return "upload";
        }

        try {
            // Ensure "uploads" directory exists with an absolute path
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Define file paths
            Path pdfPath = Paths.get(UPLOAD_DIR, pdfFile.getOriginalFilename());
            Path excelPath = Paths.get(UPLOAD_DIR, excelFile.getOriginalFilename());

            // Copy uploaded files safely (Avoids issues with Spring Boot's Tomcat temp storage)
            Files.copy(pdfFile.getInputStream(), pdfPath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(excelFile.getInputStream(), excelPath, StandardCopyOption.REPLACE_EXISTING);

            // Check if files exist before processing
            if (!Files.exists(pdfPath) || !Files.exists(excelPath)) {
                model.addAttribute("message", "File upload failed! Please try again.");
                return "upload";
            }

            // Process PDF and Excel
            Map<Integer, String> emailPages = pdfProcessorService.extractEmailPages(pdfPath.toString());
            File updatedExcel = excelUpdaterService.updateExcel(excelPath.toString(), emailPages);

            model.addAttribute("downloadLink", "/download?file=" + updatedExcel.getName());
            model.addAttribute("message", "Files processed successfully!");

        } catch (IOException e) {
            model.addAttribute("message", "Error processing files: " + e.getMessage());
            e.printStackTrace();
        }

        return "upload";
    }
}
