import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.licensekey.LicenseKey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PdfParser {
    private static final String FILE_NAME = "/tmp/itext.pdf";

    public void parsePDF() {
        LicenseKey.loadLicenseFile("C:/Keys/itextkey1570533739943_0.xml");
        PdfReader reader;
        PdfDocument document;
        try {
            reader = new PdfReader("C:/testdoc.pdf");
            document = new PdfDocument(reader);
            String textFromPage = PdfTextExtractor.getTextFromPage(document.getPage(1));
            ArrayList<String> list = new ArrayList<String>();
            Scanner scanner = new Scanner(textFromPage);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                list.add(line);
                // process the line
            }
            scanner.close();
//            for(String e : list){
//                System.out.println(e);
//            }
//            System.out.println(list.get(5) + "\n" + list.get(8)+"\n"+list.get(33)+ "\n"+list.get(35));

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
