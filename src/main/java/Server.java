import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            // Listen for connections (Clients to connect) on port 1234
            while (!serverSocket.isClosed()) {
                // Will be closed in the Client Handler
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket); // accepts socket object
                // this class will also implement runnable interface

                // pass the class object that implement runnable
                Thread thread = new Thread(clientHandler);

                // The start method begins the execution of a thread.
                // When you call start() the run method is called.
                // The operating system schedules the threads.
                thread.start();
            }
        } catch (IOException e) {
            closedServerSocket();
        }
    }

    public void closedServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
// difference between ServerSocket and socket