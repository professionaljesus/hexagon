package hex.bots.JuanBot;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private ServerSocket serverSocket = null;
    private boolean listening = true;
    volatile Queue<String> requests;

    public Server(int port) {
        this.port = port;
        requests = new LinkedList<String>();
    }

    public void run(int mapSize, String method) throws IOException {
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + this.port);
            System.exit(1);
        }
        System.out.println("Listening on port: " + this.port);

        ExecutorService executor = Executors.newCachedThreadPool();
        while (listening) {
            try {
                Socket socket = serverSocket.accept();
                listening = false;
                System.out.println("Connection with client!");
                // run connection on new thread
                executor.submit(() -> {
                    if (method.equals("training")) return training(socket, mapSize);
                    return 0;
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        serverSocket.close();
    }

    /**
     * Train the network.
     * @param socket
     * @param mapSize
     * @return
     * @throws IOException
     */
    private Integer training(Socket socket, int mapSize) throws IOException {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            out.println("Map size: " + mapSize);
            while (true) {
                if (requests.peek() != null) {
                    String request = requests.poll();
                    System.out.println(request);
                    out.println(request);
                    String response = in.readLine();
                    System.out.println(response);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            in.close();
            socket.close();
        }
        return 1;
    }

    /**
     * Sending data to client.
     * @param request, json data
     */
    public void send(String request) {
        requests.offer(request);
    }

    public Queue<String> getRequests() {
        return requests;
    }

    public static void main(String[] args) throws IOException {
        Server s = new Server(8989);
        //s.run();
    }
}