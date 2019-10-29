import org.w3c.dom.ls.LSOutput;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class EmailParser {

    public void parsePDF(String host, String login, String password, Properties props) {

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password);
            }
        });

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
             Store store = session.getStore()) {
            store.connect(host, login, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(new FlagTerm(new Flags(
                    Flags.Flag.SEEN), false));

            if (messages.length == 0){
                System.out.println("Необработанных сообщений нет");
                return;
            }
            StringBuilder builder = new StringBuilder();
            System.out.print("Ждите");
            for (int i = 0; i < 3; i++) {
                Thread.sleep(900);
                System.out.print(".");
            }
            System.out.println();

            for (int i = inbox.getMessageCount() - messages.length+1, j = inbox.getMessageCount(); i <= j ; j--) {
                Message message = inbox.getMessage(j);
                //Return the content as a Java object
                Multipart mp = (Multipart) message.getContent();
                //Return the number of enclosed BodyPart objects
                int numberOfParts = mp.getCount();
//                System.out.println("number of parts "+numberOfParts);
                for (int partCount = 0; partCount < numberOfParts; partCount++) {
                    MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(partCount);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        //дата получения
                        Date messageDate = message.getReceivedDate();
                        SimpleDateFormat strFormat = new SimpleDateFormat("MM.dd_HH.mm.ss");
                        String receiptTime = strFormat.format(messageDate);
                        String filePath = "C:/Dir/оригPDF/" + receiptTime + ".pdf";
                        //сохранение PDf на диск
                        part.saveFile(filePath);
                        //адресс отчета
                        String reportFilepath = "C:/Dir/Отчет/" + receiptTime + ".txt";
                        //парсим
                        PdfParser parser = new PdfParser();
                        parser.setFileName(filePath);
                        parser.setFileNameDest(reportFilepath);
                        parser.parsePDF();
                        System.out.println("Отчет по письму "+ receiptTime +" сформирован");
                    }
                }
            }
        } catch (NoSuchProviderException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }catch (InterruptedException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("C:\\PDF parser\\config.properties")) {
            props.load(input);

        } catch (IOException io) {
            System.err.println("\n" + "Файл с настройками не найден, убедитесь в том что в папке с программой присутствует файл config.properties" + "\n");
            return;

        }
        String host = props.getProperty("host");
        String login = props.getProperty("login");
        String password = props.getProperty("password");

        EmailParser parser = new EmailParser();
        parser.parsePDF(host, login, password, props);
    }
}