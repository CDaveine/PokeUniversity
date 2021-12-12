package Poke_University;

import java.lang.Math;
import java.util.Random;
import java.io.*;
import java.net.*;

import Poke_University.Poketudiants.*;
import Poke_University.ServerTCP.ClientHandler;

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

    public enum Poketudiants {
        Alabourre,
        Belmention,
        Buchafon,
        Couchtar,
        Ismar,
        Nuidebou,
        Parlfor,
        Procrastino,
        Promomajor,
        Rigolamor
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

    public String display() {
        return nom + " " + type + " " + level + " " + xp + " " + xp_max + " " + PV_current + " " + PV_max + " " + attack
                + " " + defense + " " + attacks[0].getNom() + " " + attacks[0].getType() + " " + attacks[1].getNom()
                + " " + attacks[1].getType();
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
        return ((PV_max - PV_current) == 0);
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

    protected boolean capture(Poketudiant opponent, ClientHandler player) {
        double chance = 2 * Math.max((1 / 2) - (opponent.PV_current / opponent.PV_max), 0);
        double rand = Math.random();
        if (rand <= chance && player.dresseur.size_poke() < 3) {
            player.dresseur.setPoketudiants(player.dresseur.size_poke(), opponent);
            return true;
        }
        return false;
    }

    protected boolean leave(Poketudiant player, Poketudiant opponent) {
        int levels = player.level - opponent.level;
        double luck;
        switch (levels) {
            case -3:
                luck = 0;
            case -2:
                luck = 0.25;
            case -1:
                luck = 0.4;
            case 0:
                luck = 0.5;
            case 1:
                luck = 0.75;
            case 2:
                luck = 0.9;
            case 3:
                luck = 1;
            default:
                luck = 0.0;
        }
        double rand = Math.random() + 0.00000001;
        if (rand <= luck) {
            return true;
        }
        return false;
    }

    public void combat_sauvage(ClientHandler player) {
        Poketudiant poke_util = player.dresseur.getPoketudiants(0);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(player.socketaccept.getInputStream()));
            PrintWriter out = new PrintWriter(player.socketaccept.getOutputStream(), true);
            String read;
            out.println("encouter new wild 1");
            // genere pokemon sauvage
            Random rand = new Random();
            int rd = rand.nextInt(Poketudiants.values().length);
            Poketudiants poke = Poketudiants.values()[rd];
            Poketudiant sauvage;
            switch (poke) {
                case Alabourre:
                    sauvage = new Alabourre();
                    break;
                case Belmention:
                    sauvage = new Belmention();
                    break;
                case Buchafon:
                    sauvage = new Buchafon();
                    break;
                case Couchtar:
                    sauvage = new Couchtar();
                    break;
                case Ismar:
                    sauvage = new Ismar();
                    break;
                case Nuidebou:
                    sauvage = new Nuidebou();
                    break;
                case Parlfor:
                    sauvage = new Parlfor();
                    break;
                case Procrastino:
                    sauvage = new Procrastino();
                    break;
                case Promomajor:
                    sauvage = new Promomajor();
                    break;
                case Rigolamor:
                    sauvage = new Rigolamor();
                    break;
                default:
                    sauvage = null;
                    break;
            }

            while ((read = in.readLine()) != null) {
                if (read.contains("encounter action attack1")) {
                    attack(sauvage, poke_util.attacks[0]);
                    if (sauvage.is_dead()) {
                        out.println("encounter KO opponent");
                        out.println("encounter win");
                    } else if (poke_util.is_dead()) {
                        out.println("encounter KO player");
                    }
                } else if (read.contains("encounter action attack2")) {
                    attack(sauvage, poke_util.attacks[1]);
                } else if (read.contains("encounter action switch")) {
                    out.println("encounter enter poketudiant index");
                } else if (read.contains("encounter action catch")) {
                    if (capture(sauvage, player)) {
                        out.println("encounter catch ok");
                    } else {
                        out.println("encounter catch fail");
                    }
                } else if (read.contains("encounter action leave")) {
                    if (leave(poke_util, sauvage)) {
                        out.println("encounter escape ok");
                    } else {
                        out.println("encounter escape fail");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void combat_player(ClientHandler player, int id_opponent) {

    }

}
