package Poke_University;

import java.util.Random;

public class World {
    private int size;
    private Case[][] map;
    
    public World(int size){
        int r, ri, rj;
        Random alea = new Random();
        this.size = size;

        ri = alea.nextInt(size);
        rj = alea.nextInt(size);

        map[ri][rj] = new Heal();

        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                if(i != ri || j != rj){
                    r = alea.nextInt(100);
                    if (r < 75) {
                        map[i][j] = new Floor();
                    }
                    else{
                        map[i][j] = new Bush();
                    }
                }
            }
        }
    }

    public int getSize() {
        return this.size;
    }

    public Case getCase(int i, int j) {
        return this.map[i][j];
    }

    public void printMap() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(map[i][j].getClass() == Floor.class){
                    System.out.print("F ");
                }
                else if(map[i][j].getClass() == Bush.class){
                    System.out.print("B ");
                }
                else{
                    System.out.print("H ");
                }
            }
            System.out.print("\n");
        }
    }
}
