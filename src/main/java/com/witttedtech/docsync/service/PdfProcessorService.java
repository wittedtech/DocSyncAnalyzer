package com.witttedtech.docsync.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class PdfProcessorService {

    private final ITesseract tesseract;

    public PdfProcessorService() {
        tesseract = new Tesseract();

        // Detect OS and set Tesseract path dynamically
        String os = System.getProperty("os.name").toLowerCase();
        String tessPath;

        if (os.contains("win")) {
            tessPath = new File("tesseract-bin/windows/").getAbsolutePath();
        } else if (os.contains("mac")) {
            tessPath = new File("tesseract-bin/macos/").getAbsolutePath();
        } else {
            tessPath = new File("tesseract-bin/linux/").getAbsolutePath();
        }

        tesseract.setDatapath(tessPath);
        String tessDataPath = new File(tessPath + "/tessdata").getAbsolutePath();
        tesseract.setVariable("TESSDATA_PREFIX", tessDataPath); // Required for Mac/Linux
        tesseract.setLanguage("eng");

        // Explicitly set JNA to find the correct Tesseract native library
        System.setProperty("jna.library.path", "/opt/homebrew/lib/");

        System.out.println("Using Tesseract from: " + tessPath);
        System.out.println("TESSDATA_PREFIX: " + System.getenv("TESSDATA_PREFIX"));
    }

    public Map<Integer, String> extractEmailPages(String pdfFilePath) throws IOException {
        Map<Integer, String> emailPages = new LinkedHashMap<>();

        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                try {
                    String text = tesseract.doOCR(image);

                    if (text.contains("From:") && text.contains("To:") && text.contains("Subject:") && text.contains("Sent:")) {
                        String[] lines = text.split("\n");
                        for (String line : lines) {
                            if (line.startsWith("Sent:")) {
                                String emailDateTime = line.replace("Sent:", "").trim();
                                emailPages.put(page + 1, emailDateTime);
                                break;
                            }
                        }
                    }
                } catch (TesseractException e) {
                    System.err.println("OCR Error on page " + (page + 1) + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing PDF file: " + e.getMessage());
            throw e;
        }

        return emailPages;
    }
}
