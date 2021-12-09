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
}
