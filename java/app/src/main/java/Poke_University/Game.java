package Poke_University;

import Poke_University.ServerTCP.ClientHandler;

public class Game {
    private int nb_player;
    private String game_name;
    private ClientHandler[] players;
    private World map;

    public Game(int n, String s, ClientHandler creator) {
        this.nb_player = n;
        this.game_name = s;
        players = new ClientHandler[4];
        players[0] = creator;
        map = new World("/home/mint/Documents/2021/Res/projet-bd/java/app/src/main/java/Poke_University/world.map", creator, this);
    }

    public World getMap(ClientHandler joueur) {
        return map = new World("/home/mint/Documents/2021/Res/projet-bd/java/app/src/main/java/Poke_University/world.map", joueur, this);
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
