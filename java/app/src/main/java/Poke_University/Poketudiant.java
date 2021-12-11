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

    public Poketudiant() {

    }

    protected double rand_coef() {
        return 0.9 + Math.random() * (1.1 - 0.9);
    }

    protected int rand_attack(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    protected Attack other_type_attack(Type type) {
        Random random = new Random();
        int rand = random.nextInt(Attacks.attacks.length);
        while (type.equals(Attacks.attacks[rand].getType())) {
            rand = random.nextInt(Attacks.attacks.length);
        }
        return Attacks.attacks[rand];
    }

    protected void display() {
        System.out.println("Voici un " + nom + " il est de type " + type + " sa puissance d'attaque est de " + attack
                + " sa defense est de " + defense + " de niveau " + level + "\n PV: " + PV_current + "/" + PV_max
                + "\n XP: " + xp + "/" + xp_max);
    }

    protected void attack(Poketudiant opponent, Attack att) {
        double rand = rand_coef();
        double dmg;
        if ((att.getType().equals(Type.Noisy) && opponent.type.equals(Type.Lazy))
                || (att.getType().equals(Type.Lazy) && opponent.type.equals(Type.Motivated))
                || (att.getType().equals(Type.Motivated) && opponent.type.equals(Type.Noisy))
                || att.getType().equals(Type.Teacher)) {
            dmg = rand * (this.attack / (opponent.defense * 1.0)) * (att.getPuissance() * 2);
        } else {
            dmg = rand * (this.attack / (opponent.defense * 1.0)) * att.getPuissance();
        }
        opponent.PV_current -= dmg;
    }

    protected boolean is_dead() {
        return ((this.PV_max - PV_current) == 0);
    }

    protected void experience(Poketudiant opponent, int nb_poketudiants) {
        double amount_due = opponent.xp * 0.1;
        int xp_recieve = (int) amount_due / nb_poketudiants;
        this.xp += xp_recieve;
        if (this.xp >= this.xp_max && level < 10) {
            this.level++;
            this.xp = 0;
            this.xp_max = (int) Math.round(500 * ((1 + level) / 2.0));
        }
    }

    protected void evolves() {

    }

}
