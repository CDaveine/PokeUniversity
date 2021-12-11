package Poke_University;

import java.io.*;
import java.util.*;

import Poke_University.ServerTCP.ClientHandler;

public class World {
    private String map;

    public World(String s, ClientHandler joueur, Game game) {
        map = "map " + rows(read(s)) + " " + cols(read(s)) + "\n" + map_modified(read(s), joueur, game);
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String read(String s) {
        File map = new File(s);
        try {
            Scanner scan = new Scanner(map);
            String data = "";
            while (scan.hasNextLine()) {
                data += scan.nextLine() + "\n";

            }
            scan.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String map_modified(String map, ClientHandler moi, Game game) {
        char[][] tab = map_tab(map);
        for (int i = 0; i < game.getNb_player(); i++) {
            if (game.getPlayers(i).id == moi.id) {
                tab[moi.position_x][moi.position_y] = '0';
            } else {
                tab[game.getPlayers(i).position_x][game.getPlayers(i).position_y] =  Integer.toString(game.getPlayers(i).id).charAt(0);
            }
        }
        String new_map = tab_map(tab);
        return new_map;
    }

    public int rows(String s) {
        String[] rows = s.split("\n");
        return rows.length;
    }

    public int cols(String s) {
        String[] first_row = s.split("\n");
        return first_row[1].length();
    }

    public char[][] map_tab(String map) {
        String[] rows = map.split("\n");
        char[][] tab = new char[rows(map)][cols(map)];
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                tab[i][j] = rows[i].charAt(j);
            }
        }
        return tab;
    }

    public String tab_map(char[][] tab) {
        String map = "";
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                map += tab[i][j];
            }
            map += "\n";
        }
        return map;
    }

    public void print_map(char[][] map){
        for(int i = 0; i < map.length; i ++){
            for(int j = 0; j < map[i].length; j++){
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

}
