package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import utilities.PdfOperations;

public class App {
   public static void main(String[] args) {
      if(args.length==0 || isNullOrEmpty(args[0])){
         System.out.println("no argument passed");
         return;
      }
      String fileName = null;
      String password = null;
      String targetFile = null;
      int startingPageNumber = -1;
      int endingPageNumber = -1;

      if (args.length >= 2) {
         fileName = args[0];
         String temp = args[1];
         if (isPagesRangeArgument(temp)) {
            String[] pageNumbers = temp.split("-");
            startingPageNumber = Integer.valueOf(pageNumbers[0]);
            endingPageNumber = pageNumbers.length == 2 ? Integer.valueOf(pageNumbers[1]) : 100000;
         } else {
            password = temp;
         }
      }

      if (args.length >= 3) {
         String temp = args[2];
         if (isPagesRangeArgument(temp)) {
            String[] pageNumbers = temp.split("-");
            startingPageNumber = Integer.valueOf(pageNumbers[0]);
            endingPageNumber = pageNumbers.length == 2 ? Integer.valueOf(pageNumbers[1]) : 100000;
         } else {
            targetFile = temp;
         }
      }

      if (args.length == 4) {
         targetFile = args[3];
      }

      if (!isNullOrEmpty(fileName) && !fileName.contains(File.separator)) {
         fileName = addFilePath(fileName);
      }

      if (!isNullOrEmpty(targetFile) && !targetFile.contains(File.separator)) {
         targetFile = addFilePath(targetFile);
      }

      try {
         if (startingPageNumber != -1 && endingPageNumber != -1 && !isNullOrEmpty(password)) {
            if (!isNullOrEmpty(targetFile)) {
               PdfOperations.unlockAndSplitPdf(fileName, password, startingPageNumber, endingPageNumber, targetFile);
            } else {
               PdfOperations.unlockAndSplitPdf(fileName, password, startingPageNumber, endingPageNumber);
            }
         } else if (!isNullOrEmpty(password)) {
            if (!isNullOrEmpty(targetFile)) {
               PdfOperations.unlockPdf(fileName, password, targetFile);
            } else {
               PdfOperations.unlockPdf(fileName, password);
            }
         } else {
            if (!isNullOrEmpty(targetFile)) {
               PdfOperations.splitPdf(fileName, startingPageNumber, endingPageNumber, targetFile);
            } else {
               PdfOperations.splitPdf(fileName, startingPageNumber, endingPageNumber);
            }
         }
      } catch (InvalidPasswordException ioException){
         System.out.println("Operation failed as supplied password is either empty or wrong");
      }catch (IOException ioException){
         System.out.println("Operation failed with Error :\n" + ioException.getLocalizedMessage());
      }


   }

   private static String addFilePath(String fileName) {
      String filepath = Paths.get("").toAbsolutePath().toString();
      return filepath + File.separator + fileName;
   }

   private static boolean isPagesRangeArgument(String input) {
      String pattern = "\\d+-\\d+";
      return input.matches(pattern);
   }

   private static boolean isNullOrEmpty(String input) {
      return input == null || input.equals("");
   }
}
