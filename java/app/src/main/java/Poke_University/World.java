package Poke_University;

import java.io.*;
import java.util.*;

public class World {
    private String map;
    
    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public World(String s){
        map = "map " + rows(read(s)) + " " + cols(read(s)) + "\n" + read(s);
    }

    public String read(String s){
        File map = new File(s);
        try{
            Scanner scan = new Scanner(map);
            String data = "";
            while(scan.hasNextLine()){
                data += scan.nextLine()+"\n";

            }
            scan.close();
            return data;
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public int rows(String s){
        String[] rows = s.split("\n");
        return rows.length;
    }

    public int cols(String s){
        String[] first_row = s.split("\n");
        return first_row[1].length();
    }

}
