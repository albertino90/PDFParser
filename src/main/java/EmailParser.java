import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
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
            //получаем папку с входящими сообщениями
            Folder inbox = store.getFolder("INBOX");
            //открываем её только для чтения
            inbox.open(Folder.READ_ONLY);
            Message messages[] = inbox.search(new FlagTerm(new Flags(
                    Flags.Flag.SEEN), false));
//                for (int i = 012119; i < messages.length; i++) {
//                    System.out.println(messages[i].getMessageNumber());
//                }
            System.out.println("Общее количество сообщений : " + inbox.getMessageCount() + "\n"
                    + "Количество непрочитанных сообщений : " + messages.length + "\n");
            if (inbox.getMessageCount() == 0) {
                System.out.println("Сообщений нет");
            }
            System.out.println("Введите номер письма");
            int postNumber = Integer.parseInt(bufferedReader.readLine());
            //получаем последнее сообщение (самое старое будет под номером 1)
            Message message = inbox.getMessage(postNumber);
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
                    System.out.println("Отчет сформирован");
                }
            }
        } catch (NoSuchProviderException e) {
            System.err.println(e.getMessage());
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("C:\\Users\\Albert\\IdeaProjects\\MTpdfparser\\src" +
                "\\main\\resources\\config.properties")) {
            props.load(input);

        } catch (IOException io) {

            io.printStackTrace();
            System.err.println("\n" + "Property file not found" + "\n");

        }
        String host = props.getProperty("host");
        String login = props.getProperty("login");
        String password = props.getProperty("password");

        EmailParser parser = new EmailParser();
        parser.parsePDF(host, login, password, props);

    }
}
//    Flags seen = new Flags(Flags.Flag.RECENT);
//    FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
//messages = inbox.search(unseenFlagTerm);