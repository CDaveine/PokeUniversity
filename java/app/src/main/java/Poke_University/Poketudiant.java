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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isCatchable() {
        return catchable;
    }

    public void setCatchable(boolean catchable) {
        this.catchable = catchable;
    }

    public Poketudiant getEvolution() {
        return evolution;
    }

    public void setEvolution(Poketudiant evolution) {
        this.evolution = evolution;
    }

    public double getCoef() {
        return coef;
    }

    public void setCoef(double coef) {
        this.coef = coef;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getPV_max() {
        return PV_max;
    }

    public void setPV_max(int pV_max) {
        PV_max = pV_max;
    }

    public int getPV_current() {
        return PV_current;
    }

    public void setPV_current(int pV_current) {
        PV_current = pV_current;
    }

    public Attack[] getAttacks() {
        return attacks;
    }

    public void setAttacks(Attack[] attacks) {
        this.attacks = attacks;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getXp_max() {
        return xp_max;
    }

    public void setXp_max(int xp_max) {
        this.xp_max = xp_max;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String display(){
        return nom + " " + type + " " + level + " " + xp + " " + xp_max + " " + PV_current + " " + PV_max + " " + attack + " " + defense + " " + attacks[0].getNom() + " " + attacks[0].getType() + " " + attacks[1].getNom() + " " + attacks[1].getType();
    }

    protected Attack other_type_attack(Type type) {
        Random random = new Random();
        int rand = random.nextInt(Attacks.attacks.length);
        while (type.equals(Attacks.attacks[rand].getType())) {
            rand = random.nextInt(Attacks.attacks.length);
        }
        return Attacks.attacks[rand];
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
