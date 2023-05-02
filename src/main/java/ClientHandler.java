import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{


    // keep track of all our clients
    // broadcast messages to the multiple clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket; // this will be the socket that passed from server class. establish a connection between client and server
    private BufferedReader bufferedReader; // read data from input character stream
    private BufferedWriter bufferedWriter; // send/write data to character output stream
    private String clientUsername;


    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this); // this represents clientHandler object
            broadcastMessage("Server: " + clientUsername + " has entered the chat!");
        } catch (IOException e) {
            closeEveryThing(socket, bufferedReader, bufferedWriter);
        }

    }
    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEveryThing(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    private void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler: clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEveryThing(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat");
    }

    private void closeEveryThing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedReader.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

/**
 * In java byte stream ends with word stream(handle binary data such as images, audio or video).
 * character stream ends with word writer
 *
 * streams in java?
 * bufferedReader and bufferedWriter are also streams
 *
 * buffer means like storage
 * Buffers are required when producers and consumers operate at the different rates.
 *
 */
