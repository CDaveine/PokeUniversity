package Poke_University;

import java.util.HashMap;

public abstract class Poketudiant {
    private String nom;
    private Type type;
    private boolean catchable;
    private Poketudiant evolution;
    private int attack;
    private int defense;
    private int PV_max;
    private int PV_current;
    private Attack[] attacks;
    private int xp;

    public Poketudiant(){
        
    }
}
