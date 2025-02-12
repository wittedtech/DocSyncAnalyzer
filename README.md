# DocSyncAnalyzer
---

**📄 DocSyncAnalyzer - Automated Document Parsing & OCR** 🚀 **DocSyncAnalyzer**  is a powerful document processing and OCR-based text extraction tool built using **Spring Boot, PDFBox, and Tesseract** . It efficiently scans PDFs, extracts relevant content, and processes large-scale documents for analysis.
 *(Optional: Add an image link if available)*

---

**📌 Features** ✔️ **Batch PDF Processing**  – Handles large PDF files with thousands of pages.
✔️ **OCR-based Text Extraction**  – Uses **Tesseract OCR**  for highly accurate text recognition.
✔️ **Multi-threaded Processing**  – Optimized for speed using parallel execution.
✔️ **Email Content Detection**  – Extracts emails from scanned PDFs based on header detection (`From`, `To`, `Subject`, `Sent`).
✔️ **Cross-Platform Support**  – Works on Windows, MacOS, and Linux.
✔️ **Logging & Error Handling**  – Integrated logging with proper exception handling.

---

**🚀 Tech Stack**  
- **Backend:**  Spring Boot 3.4.2
 
- **OCR Engine:**  Tesseract 5.7.0
 
- **PDF Processing:**  Apache PDFBox
 
- **Logging:**  SLF4J & Logback
 
- **Database (Optional for storing results):**  PostgreSQL (future integration planned)


---

**📥 Installation** **1️⃣ Prerequisites** 
Make sure you have the following installed:
 
- **Java 21**  (Temurin-21.0.5+11 recommended)
 
- **Maven**
 
- **Tesseract OCR**  
  - **Mac:**  `brew install tesseract`
 
  - **Linux:**  `sudo apt install tesseract-ocr`
 
  - **Windows:**  Download from [Tesseract GitHub Releases](https://github.com/tesseract-ocr/tesseract)
**2️⃣ Clone the Repository** 

```sh
git clone https://github.com/wittedtech/DocSyncAnalyzer.git
cd DocSyncAnalyzer
```
**3️⃣ Configure Tesseract Path** Make sure Tesseract is correctly set up in `PdfProcessorService.java`:

```java
String tessPath = new File("tesseract-bin/macos/").getAbsolutePath();  // Adjust for Windows/Linux
tesseract.setDatapath(tessPath);
tesseract.setLanguage("eng");
```
**4️⃣ Build the Project** 

```sh
mvn clean install
```
**5️⃣ Run the Application** 

```sh
mvn spring-boot:run
```


---

**📖 Usage** **Extract Emails from PDFs**  
1. Upload a **scanned PDF**  file using the API or CLI.
 
2. The tool will process each page and extract **emails**  containing: 
  - `From:`
 
  - `To:`
 
  - `Subject:`
 
  - `Sent:`

3. Results will be logged and stored in memory.
**Example API Call** 

```sh
curl -X POST -F "file=@sample.pdf" http://localhost:8080/upload
```
**Example Output** 

```json
{
  "extractedEmails": {
    "Page 10": "Sent: Mon, 12 Feb 2025 14:00",
    "Page 25": "Sent: Tue, 13 Feb 2025 16:45"
  }
}
```


---

**⚡ Performance Optimization** 🔹 **Multi-threading** : Improves PDF processing speed by leveraging concurrent execution.
🔹 **Custom DPI Settings** : Processes images at `150-300 DPI` for speed-accuracy tradeoff.
🔹 **Selective OCR Processing** : Instead of scanning the entire document, we apply regex-based filtering to detect relevant text faster.**📌 Future Enhancements** ✅ **Database Integration**  (PostgreSQL) for storing extracted data.
✅ **Support for More File Types**  (DOCX, PNG, TIFF).
✅ **Web UI for Uploading & Viewing Extracted Data.** 
✅ **Pre-trained AI Model for Better Text Classification.** 

---

**📜 Contributing** 
We welcome contributions! Follow these steps:
 
1. **Fork the repository**  🍴
 
2. **Create a feature branch**  (`git checkout -b feature-name`)
 
3. **Commit your changes**  (`git commit -m "Added new feature"`)
 
4. **Push to GitHub**  (`git push origin feature-name`)
 
5. **Create a Pull Request (PR)**  ✅


---

**📝 License** This project is licensed under the **MIT License** . See [LICENSE](https://chatgpt.com/c/LICENSE)  for details.

---

**📞 Contact & Support** 📧 **Email:**  [support@wittedtech.com]() 
🌐 **Website:**  [wittedtech.com](https://wittedtech.com/) 
💬 **GitHub Issues:**  [Create an Issue](https://github.com/wittedtech/DocSyncAnalyzer/issues) 

---
