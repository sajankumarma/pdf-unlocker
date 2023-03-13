package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfOperations {

   private static final String UNLOCKED_PREFIX = "unlocked-";
   private static final String PAGES_PREFIX = "PAGES-";

   private PdfOperations() {

   }

   public static void unlockAndSplitPdf(String inputFilePath, String password, int startingPageNumber, int endingPageNumber) throws IOException {
      String unlockedPdfName = generateNameForUnlockPdf(inputFilePath);
      unlockPdf(inputFilePath, password);
      splitPdf(unlockedPdfName, startingPageNumber, endingPageNumber);
   }

   public static void unlockAndSplitPdf(String inputFilePath, String password, int startingPageNumber, int endingPageNumber, String targetFilePath) throws IOException {
      String unlockedPdfName = generateNameForUnlockPdf(inputFilePath);
      unlockPdf(inputFilePath, password);
      splitPdf(unlockedPdfName, startingPageNumber, endingPageNumber, targetFilePath);
      Files.delete(Paths.get(unlockedPdfName));
   }

   public static void unlockPdf(String inputFilePath, String password) throws IOException {
      unlockPdf(inputFilePath, password, generateNameForUnlockPdf(inputFilePath));
   }

   public static void unlockPdf(String inputFilePath, String password, String targetFilePath) throws IOException {
      File inputFile = new File(inputFilePath);
      File targetFile = new File(targetFilePath);
      try (PDDocument pdd = PDDocument.load(inputFile, password)) {
         pdd.setAllSecurityToBeRemoved(true);
         pdd.save(targetFile);
      }
   }

   public static void splitPdf(String inputFilePath, int startingPageNumber, int endingPageNumber, String targetFilePath) throws IOException {

      try (PDDocument pageDocument = new PDDocument();) {
         File file = new File(inputFilePath);
         PDDocument document = PDDocument.load(file);
         for (int i = startingPageNumber - 1; i < endingPageNumber; i++) {
            pageDocument.addPage(document.getPage(i));
         }
         pageDocument.save(targetFilePath);
      }
   }

   public static void splitPdf(String inputFilePath, int startingPageNumber, int endingPageNumber) throws IOException {
      splitPdf(inputFilePath, startingPageNumber, endingPageNumber, generateNameForSplitPdf(inputFilePath, startingPageNumber, endingPageNumber));
   }

   private static String filePathWithPrefix(String filePath, String prefix) {
      return replaceLast(filePath, File.separator, File.separator + prefix);
   }

   private static String replaceLast(String string, String toReplace, String replacement) {
      int pos = string.lastIndexOf(toReplace);
      if (pos > -1) {
         return string.substring(0, pos)
               + replacement
               + string.substring(pos + toReplace.length());
      } else {
         return string;
      }
   }

   private static String generateNameForUnlockPdf(String fileName) {
      return filePathWithPrefix(fileName, UNLOCKED_PREFIX);
   }

   private static String generateNameForSplitPdf(String fileName, int startingPageNumber, int endingPageNumber) {
      return filePathWithPrefix(fileName, PAGES_PREFIX + startingPageNumber + "-" + endingPageNumber + "-");
   }

}
