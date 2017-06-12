package mypackage.client;

import mypackage.Connection;
import mypackage.ConsoleHelper;
import mypackage.Message;
import mypackage.MessageType;
import java.io.IOException;
import java.net.Socket;


/**
 * Created by DNS on 14.03.2017.
 */
public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;

    protected String getServerAddress(){
        return ConsoleHelper.readString();
    }

    protected int getServerPort(){
        return ConsoleHelper.readInt();
    }

    protected String getUserName(){
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole(){
        return true;
    }

    protected SocketThread getSocketThread(){
        return new SocketThread();
    }

    protected void sendTextMessage(String text){
        try {
            Message message = new Message(MessageType.TEXT, text);
            connection.send(message);
        }
        catch (IOException e){
            clientConnected = false;
            System.out.println("Error");
        }
    }

    public class SocketThread extends Thread {
        protected void processIncomingMessage(String message){
            System.out.println(message);
        }

        protected void informAboutAddingNewUser(String userName){
            System.out.println(userName + " is connected");
        }

        protected void informAboutDeletingNewUser(String userName){
            System.out.println(userName +" has left");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected){
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this){
                Client.this.notify();
            }
        }

        protected void clientHandshake() throws IOException, ClassNotFoundException{
            while(true){
                Message message = connection.receive();
                if (message.getType() == MessageType.NAME_REQUEST){
                    connection.send(new Message(MessageType.USER_NAME, getUserName()));
                }
                else if (message.getType() == MessageType.NAME_ACCEPTED){
                    notifyConnectionStatusChanged(true);
                    return;
                }
                else
                    throw new IOException("Unexpected MessageType");
            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException{
            while(true){
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT){
                    processIncomingMessage(message.getData());
                }
                else if (message.getType() == MessageType.USER_ADDED){
                    informAboutAddingNewUser(message.getData());
                }
                else if (message.getType() == MessageType.USER_REMOVED){
                    informAboutDeletingNewUser(message.getData());
                }
                else
                    throw new IOException("Unexpected MessageType");
            }
        }

        public void run(){
            try {
                Socket socket = new Socket(getServerAddress(), getServerPort());
                connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            }
            catch (IOException e){
                notifyConnectionStatusChanged(false);
            }
            catch (ClassNotFoundException e){
                notifyConnectionStatusChanged(false);
            }
        }
    }

    public void run(){
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        try {
            synchronized (this){
                this.wait();
            }
        }
        catch (InterruptedException e){
            System.out.println("Error");
            return;
        }
        if (clientConnected){
            System.out.println("Соединение установлено. Для выхода наберите команду 'exit'.");
        }
        else{
            System.out.println("Произошла ошибка во время работы клиента.");
        }
        while(clientConnected){
            String text = ConsoleHelper.readString();
            if (text.equals("exit"))
                break;
            if (shouldSendTextFromConsole()){
                sendTextMessage(text);
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
