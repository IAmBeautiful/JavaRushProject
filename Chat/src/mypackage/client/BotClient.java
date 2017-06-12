package mypackage.client;

import mypackage.Message;
import mypackage.MessageType;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by DNS on 15.03.2017.
 */
public class BotClient extends Client {
    public class BotSocketThread extends SocketThread{
        @Override
        public void clientMainLoop() throws IOException, ClassNotFoundException{
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message){
            System.out.println(message);
            if (message.contains(": ")) {
            String[] data = message.split(": ");
            SimpleDateFormat format;
            Calendar calendar = new GregorianCalendar();
                if (data[1].equals("дата")) {
                    format = new SimpleDateFormat("d.MM.YYYY");
                    sendTextMessage("Информация для " + data[0] + ": " + format.format(new GregorianCalendar().getTime()));
                } else if (data[1].equals("время")) {
                    format = new SimpleDateFormat("H:mm:ss");
                    sendTextMessage("Информация для " + data[0] + ": " + format.format(new GregorianCalendar().getTime()));
                } else if (data[1].equals("день")) {
                    format = new SimpleDateFormat("d");
                    sendTextMessage("Информация для " + data[0] + ": " + format.format(new GregorianCalendar().getTime()));
                } else if (data[1].equals("месяц")){
                    format = new SimpleDateFormat("MMMM");
                    sendTextMessage("Информация для " + data[0] + ": " + format.format(new GregorianCalendar().getTime()));
                } else if (data[1].equals("год")){
                    format = new SimpleDateFormat("YYYY");
                    sendTextMessage("Информация для " + data[0] + ": " + format.format(new GregorianCalendar().getTime()));
                }
                else if (data[1].equals("час")){
                    format = new SimpleDateFormat("H");
                    sendTextMessage("Информация для " + data[0] + ": " + format.format(new GregorianCalendar().getTime()));
                }
                else if (data[1].equals("минуты")){
                    format = new SimpleDateFormat("m");
                    sendTextMessage("Информация для " + data[0] + ": " + format.format(new GregorianCalendar().getTime()));
                }
                else if (data[1].equals("секунды")){
                    format = new SimpleDateFormat("s");
                    sendTextMessage("Информация для " + data[0] + ": " + format.format(new GregorianCalendar().getTime()));
                }
            }
        }
    }

    @Override
    protected SocketThread getSocketThread(){
        BotSocketThread botSocketThread = new BotSocketThread();
        return botSocketThread;
    }

    @Override
    protected boolean shouldSendTextFromConsole(){
        return false;
    }

    @Override
    protected String getUserName(){
        return "date_bot_" + (int) (Math.random() * 100);
    }

    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        botClient.run();
    }
}
