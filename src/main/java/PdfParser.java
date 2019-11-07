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
//        LicenseKey.loadLicenseFile("C:/Keys/itextkey1570533739943_0.xml");
//        PdfReader reader;
//        PdfDocument document;
        try (PdfReader reader = new PdfReader(fileName);
             PdfDocument document = new PdfDocument(reader);
             FileWriter writer = new FileWriter(fileNameDest)) {
//          Текст с переносами
            String textFromPage = PdfTextExtractor.getTextFromPage(document.getPage(1));
            String tempTextFromPage = textFromPage.replaceAll("\n", " ");
            ArrayList<String> list = new ArrayList<>();
//          Проверка текста на валидность
            if (tempTextFromPage.matches(".*\\bЗа услуги связи\\b.*")) {
                ArrayList<String> listTemp = new ArrayList<String>();
                Scanner scanner = new Scanner(textFromPage);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    listTemp.add(line);
                }
                scanner.close();
//                Лист со значениями

                String[] billNumber = listTemp.get(5).split(" ");
//                1 - Номер счер фактуры
                list.add(billNumber[4]);
//                2 - Дата создания
                list.add(billNumber[6]);
//                3 - Организация - ПАО "МТС"
                list.add("ПАО \"МТС\"");
//                4 - Основной договор
                list.add("Основной договор");
//                5,6,7,8,9 - За услуги связи, суммы
                String[] summy = listTemp.get(33).split(" ");
                list.add("За услуги связи");
                list.add(summy[8]);
                list.add(summy[10]);
                list.add(summy[11]);
                list.add(summy[12]);
//                По просьбе бухгалтера еще два пункта 10 и 11 ?
                list.add(billNumber[4]);
                list.add(billNumber[6]);

                for (String e : list) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
