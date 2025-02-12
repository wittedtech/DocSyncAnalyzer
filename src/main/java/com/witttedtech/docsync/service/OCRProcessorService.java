package com.witttedtech.docsync.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.rendering.ImageType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class OCRProcessorService {

    private final ITesseract tesseract;

    public OCRProcessorService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("C:/Tesseract-OCR/tessdata"); // Set your Tesseract OCR data path
        tesseract.setLanguage("eng");
    }

    public String extractPageNumber(String pdfPath, int pageIndex) throws IOException {
        PDDocument document = PDDocument.load(new File(pdfPath));
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        // Convert PDF page to an image
        BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 300, ImageType.RGB);
        document.close();

        // Crop the top-right corner where page number is located
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int cropX = (int) (imageWidth * 0.75); // Start cropping from 75% width
        int cropY = (int) (imageHeight * 0.02); // Crop top 2% height
        int cropWidth = (int) (imageWidth * 0.20);
        int cropHeight = (int) (imageHeight * 0.08);
        BufferedImage croppedImage = image.getSubimage(cropX, cropY, cropWidth, cropHeight);

        try {
            // Extract text using OCR
            String result = tesseract.doOCR(croppedImage);
            return result.replaceAll("[^0-9]", "").trim(); // Extract only numbers
        } catch (TesseractException e) {
            e.printStackTrace();
            return "Error: OCR Failed!";
        }
    }
}
