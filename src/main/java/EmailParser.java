import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailParser {
    public static void main(String[] args) {
//        String login = "oleggmanager@yandex.ru";
//        String password = "testvasya6306";
//        String apppass = "rtuwmjdndodoavaw";
//        String host = "imap.yandex.ru";

            Properties props = new Properties();
            try (InputStream input = new FileInputStream("C:\\Users\\Albert\\IdeaProjects\\MTpdfparser\\src" +
                    "\\main\\resources\\config.properties")) {
                props.load(input);

            } catch (IOException io) {

                io.printStackTrace();
                System.out.println("Property file not found");

            }
            String host = props.getProperty("host");
            String login = props.getProperty("login");
            String apppass = props.getProperty("apppass");

//        props.forEach((k, v) -> System.out.println(k + ":" + v));

            Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(login,apppass);
                }
            });

            try {
                Store store = session.getStore();
                store.connect(host, login, apppass);
                //получаем папку с входящими сообщениями
                Folder inbox = store.getFolder("INBOX");
                //открываем её только для чтения
                inbox.open(Folder.READ_ONLY);
                System.out.println("Количество сообщений : " + String.valueOf(inbox.getMessageCount()));
                if (inbox.getMessageCount() == 0)
                    return;
                String attachFiles = "";
                String messageContent = "";
                //получаем последнее сообщение (самое старое будет под номером 1)
                Message message = inbox.getMessage(9);
                Multipart mp = (Multipart) message.getContent();
                int numberOfParts = mp.getCount();
                for (int partCount = 0; partCount < numberOfParts; partCount++) {
                    MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(partCount);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        // this part is attachment
                        String fileName = part.getFileName();
                        attachFiles += fileName + ", ";
                        part.saveFile("C:/gggggg.pdf");

                    } else {
                        // this part may be the message content
                        messageContent = part.getContent().toString();
                    }
                }
            }catch (NoSuchProviderException e) {
                System.err.println(e.getMessage());
            } catch (MessagingException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        PdfParser parser = new PdfParser();
        parser.parsePDF();



    }
}

