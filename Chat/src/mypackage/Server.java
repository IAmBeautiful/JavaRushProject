package mypackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by DNS on 13.03.2017.
 */
public class Server {
    public static void main(String[] args) throws IOException {
            int port = ConsoleHelper.readInt();
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Welcome to the Server side");
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                handler.start();
            }
        }
        catch (Exception e){
            serverSocket.close();
            System.out.println("Error connection");
        }
    }

    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<String, Connection>();

    public static void sendBroadcastMessage(Message message) {
        for (String a : connectionMap.keySet()){
            try {
                connectionMap.get(a).send(message);
            }
            catch (IOException e){
                System.out.println("Error send");
            }
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket){
            this.socket = socket;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException{
            while(true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message message = connection.receive();
                if (message.getType() == MessageType.USER_NAME){
                    if (!message.getData().isEmpty() && message.getData() != null && !connectionMap.containsKey(message.getData())){
                        connectionMap.put(message.getData(), connection);
                        connection.send(new Message(MessageType.NAME_ACCEPTED));
                        return  message.getData();
                    }
                }
            }
        }

        private void sendListOfUsers(Connection connection, String userName) throws IOException{
            for (String a : connectionMap.keySet()){
                if (!a.equals(userName)) {
                    Message message = new Message(MessageType.USER_ADDED, a);
                    connection.send(message);
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException{
            while(true){
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT){
                    Message message_new = new Message(MessageType.TEXT, userName + ": " + message.getData());
                    sendBroadcastMessage(message_new);
                }
                else{
                    System.out.println("Error command");
                }
            }
        }

        public void run(){
            String userName = null;
            try {
                System.out.println("Connection completed: " + socket.getRemoteSocketAddress());
                Connection connection = new Connection(socket);
                userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));
                sendListOfUsers(connection, userName);
                serverMainLoop(connection, userName);
                connectionMap.remove(userName);
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
            }
            catch (IOException e){
                System.out.println("Error " + e.getMessage());
                if (userName != null) {
                    connectionMap.remove(userName);
                    sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
                }
            }
            catch (ClassNotFoundException e){
                System.out.println("Error " + e.getMessage());
                if (userName != null) {
                    connectionMap.remove(userName);
                    sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
                }
            }

            System.out.println("Connection closed: " + socket.getRemoteSocketAddress());
        }
    }


}
