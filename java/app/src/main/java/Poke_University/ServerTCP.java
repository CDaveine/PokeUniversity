package Poke_University;

import java.io.*;
import java.net.*;

// Server class
class ServerTCP implements Closeable, Runnable {
    private final static int PORT = 9001;
    public ServerSocket socket;
    Socket client;
    Server serv = new Server();

    public ServerTCP() {
        try {
            socket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket accept() throws IOException {
        return socket.accept();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public void run() {
        // server is listening on port 1234
        // running infinite loop for getting
        // client request
        // while (true) {
        // socket object to receive incoming client
        // requests
        while (true) {
            try {
                client = socket.accept();
                // Displaying that new client is connected
                // to server
                // System.out.println("New client connected " +
                // client.getInetAddress().getHostAddress());
                // create a new thread object
                ClientHandler clientSock = new ClientHandler(); // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // ClientHandler class
    public class ClientHandler implements Runnable {
        Socket clientSocket;
        String read;
        PrintWriter out;
        BufferedReader in;
        Socket socketaccept = client;

        // Constructor
        public ClientHandler() {
            try {
                // client = socket.accept();
                System.out.println("Client Got connected ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (true) {
                try {
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    out = new PrintWriter(client.getOutputStream(), true);

                    while ((read = in.readLine()) != null) {
                        System.out.println("message recu : " + read);

                        // game list
                        if (read.contains("require game list")) {
                            System.out.print(serv.message_game());
                            out.println(serv.message_game());
                        } else

                        // creation game
                        if (read.contains("create game")) {
                            if (serv.creation_game(read, this)) {
                                out.println("game created");
                                World world = serv.games[serv.size_games(serv.games) - 1].getMap();
                                out.println(world.getMap());
                                System.out.println(world.getMap());
                            } else {
                                out.println("cannot create game");
                            }
                        } else

                        // rejoindre une game
                        if (read.contains("join game")) {
                            String titre;
                            World world;
                            if ((titre = serv.join_game(read, this)) != null) {
                                out.println("game joined");
                                for (int i = 0; i < serv.size_games(serv.games); i++) {
                                    if (serv.games[i].getGame_name().contains(titre)) {
                                        world = serv.games[i].getMap();
                                        out.println(world.getMap());
                                        System.out.println(world.getMap());
                                    }
                                }
                            } else {
                                out.println("cannot join game");
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*
                 * finally {
                 * try {
                 * if (out != null) {
                 * out.close();
                 * }
                 * if (in != null) {
                 * in.close();
                 * client.close();
                 * }
                 * }
                 * catch (IOException e) {
                 * e.printStackTrace();
                 * }
                 * }
                 */
            }
        }
    }

}