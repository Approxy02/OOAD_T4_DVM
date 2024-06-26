package org.DVM.Control.Communication;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.function.Function;

public class JsonServer {
    private int port;

    public  SocketService service;

    public JsonServer(int port) {
        this.port = port;
    }

//    public Consumer<Message> callback;

    public void startServer(MessageCallback callback) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server Listening On Port : " + port);


            while (true) {

                Socket clientSocket = serverSocket.accept();

                service = new JsonSocketService(clientSocket);

                service.start();
                // 클라이언트로부터 메시지를 받습니다.
//                Message received = service.receiveMessage(Message.class);
                service.handleClient(clientSocket, callback);

                service.stop();

            }
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        service.sendMessage(message);
    }

    public void stopServer(){
        service.stop();
    }
}
