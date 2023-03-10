package Poke_University;

import Poke_University.ServerTCP.ClientHandler;

public class Game {
    public static String map_path;
    private int nb_player;
    private String game_name;
    private ClientHandler[] players;
    private World map;

    public Game(int n, String s, ClientHandler creator) {
        this.nb_player = n;
        this.game_name = s;
        players = new ClientHandler[4];
        players[0] = creator;
        map = new World(map_path, creator, this);
    }

    public World getMap(ClientHandler joueur) {
        return map = new World(map_path, joueur, this);
    }

    public void setMap(World map) {
        this.map = map;
    }

    public int getPlayersSize(){
        int nb = 0;
        for(int i = 0; i < players.length; i++){
            if(players[i] != null){
                nb++;
            }
        }
        return nb;
    }

    public ClientHandler getPlayers(int n) {
        return players[n];
    }

    public void setPlayers(ClientHandler player) {
        this.players[nb_player - 1] = player;
    }

    public void removePlayer(ClientHandler player){
        for(int i = 0; i < nb_player; i++){
            if(players[i].id == player.id){
                for(int j = i+1; j < nb_player; j++){
                    players[i] = players[j];
                }
            }
        }
    }

    public String display() {
        return nb_player + " " + game_name + "\n";
    }

    public String display_sans() {
        return nb_player + " " + game_name;
    }

    public int getNb_player() {
        return nb_player;
    }

    public void setNb_player(int nb_player) {
        this.nb_player = nb_player;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

}
