package Poke_University;

public class Game {
    private int nb_player;
    private String game_name;
    

    public Game(int n, String s){
        this.nb_player = n;
        this.game_name = s;
    }

    public String display(){
        return nb_player + " " + game_name + "\n";
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
