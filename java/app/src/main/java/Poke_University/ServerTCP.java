package Poke_University;

import java.io.*;
import java.net.*;

import Poke_University.Poketudiants.Alabourre;
import Poke_University.Poketudiants.Enseignant_dresseur;

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
        int position_x;
        int position_y;
        int id;
        Game game;
        Enseignant_dresseur dresseur;

        // Constructor
        public ClientHandler() {
            try {
                // client = socket.accept();
                System.out.println("Client Got connected ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*
         * public void send_map(Game game){
         * World world = game.getMap();
         * }
         */

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
                                dresseur = new Enseignant_dresseur();
                                String team = serv.team(this);
                                out.println(team);
                                World world = serv.games[serv.size_games(serv.games) - 1].getMap(this);
                                out.println(world.getMap());
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
                                dresseur = new Enseignant_dresseur();
                                String team = serv.team(this);
                                out.println(team);
                                for (int i = 0; i < serv.size_games(serv.games); i++) {
                                    if (serv.games[i].getGame_name().contains(titre)) {
                                        for (int j = 0; j < serv.games[i].getNb_player(); j++) {
                                            world = serv.games[i].getMap(serv.games[i].getPlayers(j));
                                            PrintWriter out_perso = new PrintWriter(
                                                    serv.games[i].getPlayers(j).socketaccept.getOutputStream(), true);
                                            out_perso.println(world.getMap());
                                        }
                                    }
                                }
                            } else {
                                out.println("cannot join game");
                            }
                        } else

                        // move sur la map
                        if (read.contains("map move")) {
                            String move = read.substring(9);
                            serv.move_to(move, this, this.game, in, out);
                            String team = serv.team(this);
                            out.println(team);
                            for (int i = 0; i < serv.size_games(serv.games); i++) {
                                for (int j = 0; j < serv.games[i].getNb_player(); j++) {
                                    World world = serv.games[i].getMap(serv.games[i].getPlayers(j));
                                    PrintWriter out_perso = new PrintWriter(
                                            serv.games[i].getPlayers(j).socketaccept.getOutputStream(), true);
                                    out_perso.println(world.getMap());
                                }
                            }
                        } else

                        //chat
                        if (read.contains("send message")) {
                            String msg = read.substring(13);
                            for (int i = 0; i < serv.size_games(serv.games); i++) {
                                for (int j = 0; j < serv.games[i].getNb_player(); j++) {
                                    if(serv.games[i].getPlayers(j).id != this.id){
                                    String send = "rival message " + this.socketaccept.getInetAddress().getHostAddress() + " " + msg;
                                    PrintWriter out_perso = new PrintWriter(serv.games[i].getPlayers(j).socketaccept.getOutputStream(), true);
                                    out_perso.println(send);
                                    }
                                }
                            }
                        }else

                        //reagencement team
                        if(read.contains("poketudiant")){
                            serv.team_changes(read, this);
                            String team = serv.team(this);
                            out.println(team);
                        }
                    }

                    // joueur part
                    game.removePlayer(this);
                    game.setNb_player(game.getNb_player() - 1);
                    if (game.getNb_player() == 0) {
                        System.out.println("bob");
                        serv.removeGame(game);
                    }
                    for (int i = 0; i < serv.size_games(serv.games); i++) {
                        for (int j = 0; j < serv.games[i].getNb_player(); j++) {
                            World world = serv.games[i].getMap(serv.games[i].getPlayers(j));
                            PrintWriter out_perso = new PrintWriter(
                                    serv.games[i].getPlayers(j).socketaccept.getOutputStream(), true);
                            out_perso.println(world.getMap());
                            System.out.println(world.getMap());

                        }
                    }
                    game = null;

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
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
                 * } catch (IOException e) {
                 * e.printStackTrace();
                 * }
                 * }
                 */

            }
        }

    }

}