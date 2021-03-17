package Poke_University;

import java.lang.Math;
import java.util.Random;

public abstract class Poketudiant {
    protected String nom;
    protected Type type;
    protected boolean catchable;
    protected Poketudiant evolution;
    protected double coef;
    protected int attack, defense;
    protected int PV_max, PV_current;
    protected Attack[] attacks = new Attack[2];
    protected int xp, xp_max;
    protected int level;

    public Poketudiant(){
        
    }

    protected double rand_coef(){
        return 0.9 + Math.random() * (1.1 - 0.9);
    }

    protected int rand_attack(int max){
        Random random = new Random();
        return random.nextInt(max);
    }

    protected Attack other_type_attack(Type type){
        Random random = new Random();
        int rand = random.nextInt(Attacks.attacks.length);
        while(type.equals(Attacks.attacks[rand].getType())){
            rand = random.nextInt(Attacks.attacks.length);
        }
        return Attacks.attacks[rand];
    }

    protected void display(){
        System.out.println("Voici un " + nom + " il est de type " + type + " sa puissance d'attaque est de " + attack + " sa defense est de " + defense + "\n PV: " + PV_current + "/" + PV_max + "\n XP: " + xp + "/" + xp_max);
    }

}
