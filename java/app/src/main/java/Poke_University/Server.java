package Poke_University;

import Poke_University.ServerTCP.ClientHandler;

public class Server {
    // Game game1 = new Game(2, "bob");
    Game[] games;

    public Server() {
        games = new Game[4];
    }

    public int size_games(Game[] games) {
        int i = 0;
        while (games[i] != null) {
            i++;
        }
        return i;
    }

    public String message_game() {
        String s;
        if (size_games(games) == 0) {
            s = "number of games " + size_games(games);
        } else {
            s = "number of games " + size_games(games) + "\n";
            for (int i = 0; i < size_games(games); i++) {
                s += games[i].display();
            }
        }
        return s;
    }

    public boolean creation_game(String read, ClientHandler creator) {
        String titre = read.substring(12);
        if (size_games(games) == 0) {
            Game game = new Game(1, titre, creator);
            games[0] = game;
            return true;
        } else {
            for (int i = 0; i < size_games(games); i++) {
                if (!games[i].getGame_name().contains(titre) && size_games(games) < 4) {
                    Game game = new Game(1, titre, creator);
                    games[size_games(games)] = game;
                    return true;
                }
            }
            return false;
        }
    }

    public String join_game(String read, ClientHandler joueur) {
        String titre = read.substring(10);
        for (int i = 0; i < size_games(games); i++) {
            if (games[i].getGame_name().equals(titre)) {
                if (games[i].getNb_player() < 4) {
                    games[i].getPlayers(games[i].getNb_player());
                    games[i].setNb_player(games[i].getNb_player() + 1);
                    return titre;
                } else {
                    System.out.println("nb joueur max atteint");
                }
            } else {
                System.out.println("cette partie n'existe pas");
            }
        }
        return null;
    }

}
