import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.licensekey.LicenseKey;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PdfParser {
    private String fileName;
    private String fileNameDest;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileNameDest(String fileNameDest) {
        this.fileNameDest = fileNameDest;
    }

    public void parsePDF() {
        LicenseKey.loadLicenseFile("C:/Keys/itextkey1570533739943_0.xml");
//        PdfReader reader;
//        PdfDocument document;
        try (PdfReader reader = new PdfReader(fileName);
            PdfDocument document = new PdfDocument(reader);
            FileWriter writer = new FileWriter(fileNameDest)){
            String textFromPage = PdfTextExtractor.getTextFromPage(document.getPage(1));
            ArrayList<String> listTemp = new ArrayList<String>();
            Scanner scanner = new Scanner(textFromPage);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                listTemp.add(line);
            }
            scanner.close();
            ArrayList<String>list = new ArrayList<>();
            list.add(listTemp.get(5));
            list.add(listTemp.get(8));
            list.add(listTemp.get(33));
            list.add(listTemp.get(35));
            for(String e : list){
                    writer.write(e + System.lineSeparator());
                }

        }catch (Exception e){
            e.printStackTrace();
        }
//        try {
//            reader = new PdfReader(fileName);
//            document = new PdfDocument(reader);
//            String textFromPage = PdfTextExtractor.getTextFromPage(document.getPage(1));
//            ArrayList<String> listTemp = new ArrayList<String>();
//            Scanner scanner = new Scanner(textFromPage);
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                listTemp.add(line);
//            }
//            scanner.close();
//            ArrayList<String>list = new ArrayList<>();
//            list.add(listTemp.get(5));
//            list.add(listTemp.get(8));
//            list.add(listTemp.get(33));
//            list.add(listTemp.get(35));
//
//            try (FileWriter writer = new FileWriter(fileNameDest)){
//                for(String e : list){
//                    writer.write(e + System.lineSeparator());
//                }
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            reader.close();
//            document.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
