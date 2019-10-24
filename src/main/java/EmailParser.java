import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailParser {
    public static void main(String[] args) {

            Properties props = new Properties();
            try (InputStream input = new FileInputStream("C:\\Users\\Albert\\IdeaProjects\\MTpdfparser\\src" +
                    "\\main\\resources\\config.properties")) {
                props.load(input);

            } catch (IOException io) {

                io.printStackTrace();
                System.err.println("\n"+"Property file not found"+"\n");

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
                Message messages[] = inbox.search(new FlagTerm(new Flags(
                        Flags.Flag.SEEN), false));
                for (int i = 0; i < messages.length; i++) {
                    System.out.println(messages[i].getMessageNumber());
                }
                System.out.println();
                System.out.println("Общее количество сообщений : " + inbox.getMessageCount()+"\n"
                        +"Количество непрочитанных сообщений : " + messages.length);
                if (inbox.getMessageCount() == 0){
                    System.out.println("Сообщений нет");
                }
                String attachFiles = "";
                String messageContent = "";
                //получаем последнее сообщение (самое старое будет под номером 1)
                Message message = inbox.getMessage(9);
                //Return the content as a Java object
                Multipart mp = (Multipart) message.getContent();
                //Return the number of enclosed BodyPart objects
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

//        PdfParser parser = new PdfParser();
//        parser.parsePDF();



    }
}

//    Flags seen = new Flags(Flags.Flag.RECENT);
//    FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
//messages = inbox.search(unseenFlagTerm);