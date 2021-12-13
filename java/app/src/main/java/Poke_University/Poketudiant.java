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
        if (opponent.PV_current < 0) {
            opponent.PV_current = 0;
        }
    }

    protected boolean is_dead() {
        return ((PV_max - PV_current) == 0);
    }

    protected void experience(Poketudiant opponent, ClientHandler player) {
        double amount_due = opponent.xp * 0.1;
        int xp_recieve = (int) Math.round(amount_due / (player.dresseur.size_poke() * 1.0));
        for (int i = 0; i < player.dresseur.size_poke(); i++) {
            Poketudiant poke = player.dresseur.getPoketudiants(i);
            poke.xp += xp_recieve;
            if (poke.xp >= poke.xp_max && level < 10) {
                poke.level++;
                poke.xp = 0;
                poke.xp_max = (int) Math.round(500 * ((1 + level) / 2.0));
                poke.PV_current = poke.PV_max;
                if (poke.evolution != null) {
                    poke.evolves(poke.level, player, i);
                }
            }
        }
    }

    protected void evolves(int level, ClientHandler player, int index) {
        double luck = 0;
        Poketudiant poke = null;
        switch (level) {
            case 3:
                luck = 0.2;
                break;
            case 4:
                luck = 0.375;
                break;
            case 5:
                luck = 1;
                break;
        }
        double rand = Math.random() + 0.0000001;
        if (rand <= luck) {
            if (evolution.nom.contains("Rigolamor")) {
                poke = new Rigolamor();
            } else if (evolution.nom.contains("Nuidebou")) {
                poke = new Nuidebou();
            } else if (evolution.nom.contains("Promomajor")) {
                poke = new Promomajor();
            }
            player.dresseur.setPoketudiants(index, poke);
        }

    }

    protected boolean capture(Poketudiant opponent, ClientHandler player) {
        double chance = 2 * Math.max((1 / 2.0) - (opponent.PV_current / (opponent.PV_max * 1.0)), 0);
        System.out.println(chance + " " + opponent.PV_current + " " + opponent.PV_max);
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
                break;
            case -2:
                luck = 0.25;
                break;
            case -1:
                luck = 0.4;
                break;
            case 0:
                luck = 0.5;
                break;
            case 1:
                luck = 0.75;
                break;
            case 2:
                luck = 0.9;
                break;
            case 3:
                luck = 1;
                break;
            default:
                luck = 0.0;
                break;
        }
        double rand = Math.random() + 0.0000001;
        if (rand <= luck) {
            return true;
        }
        return false;
    }

    public void combat_sauvage(ClientHandler player, BufferedReader in, PrintWriter out) {
        Poketudiant poke_util = player.dresseur.getPoketudiants(0);
        try {
            String read;
            // genere pokemon sauvage
            Random rand = new Random();
            int rd = rand.nextInt(Poketudiants.values().length);
            Poketudiants poke = Poketudiants.values()[rd];
            Poketudiant sauvage;
            boolean fin = false;
            boolean gagne = false;
            boolean perdu = false;
            int nb_dead = 0;
            Random tmp = new Random();
            int xp = tmp.nextInt(110);
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
            sauvage.xp = xp;
            // envoit debut combat
            out.println("encounter new wild 1");
            // combat
            while (!fin) {
                int pv_percent_poke = (int) Math.round((poke_util.PV_current / (poke_util.PV_max * 1.0)) * 100);
                int pv_percent_sauvage = (int) Math.round((sauvage.PV_current / (sauvage.PV_max * 1.0)) * 100);
                out.println(
                        "encounter poketudiant player " + poke_util.nom + " " + poke_util.level + " " + pv_percent_poke
                                + " " + poke_util.attacks[0].getNom() + " " + poke_util.attacks[0].getType() + " "
                                + poke_util.attacks[1].getNom() + " " + poke_util.attacks[1].getType());
                out.println(
                        "encounter poketudiant opponent " + sauvage.nom + " " + sauvage.level + " "
                                + pv_percent_sauvage);
                out.println("encounter enter action");
                // read
                read = in.readLine();
                // attack1
                if (read.contains("encounter action attack1")) {
                    // attaquer
                    attack(sauvage, poke_util.attacks[0]);
                    // affiche maj
                    pv_percent_poke = (int) Math.round((poke_util.PV_current / (poke_util.PV_max * 1.0)) * 100);
                    pv_percent_sauvage = (int) Math.round((sauvage.PV_current / (sauvage.PV_max * 1.0)) * 100);
                    out.println("encounter poketudiant player " + poke_util.nom + " " + poke_util.level + " "
                            + pv_percent_poke + " " + poke_util.attacks[0].getNom() + " "
                            + poke_util.attacks[0].getType() + " " + poke_util.attacks[1].getNom() + " "
                            + poke_util.attacks[1].getType());
                    out.println("encounter poketudiant opponent " + sauvage.nom + " " + sauvage.level + " "
                            + pv_percent_sauvage);
                    // sauvage mort
                    if (pv_percent_sauvage <= 0) {
                        out.println("encounter KO opponent");
                        out.println("encounter win");
                        fin = true;
                        gagne = true;
                    }
                } else if (read.contains("encounter action attack2")) {
                    // attaque
                    attack(sauvage, poke_util.attacks[1]);
                    // affiche maj
                    pv_percent_poke = (int) Math.round((poke_util.PV_current / (poke_util.PV_max * 1.0)) * 100);
                    pv_percent_sauvage = (int) Math.round((sauvage.PV_current / (sauvage.PV_max * 1.0)) * 100);
                    out.println("encounter poketudiant player " + poke_util.nom + " " + poke_util.level + " "
                            + pv_percent_poke + " " + poke_util.attacks[0].getNom() + " "
                            + poke_util.attacks[0].getType() + " " + poke_util.attacks[1].getNom() + " "
                            + poke_util.attacks[1].getType());
                    out.println("encounter poketudiant opponent " + sauvage.nom + " " + sauvage.level + " "
                            + pv_percent_sauvage);
                    // sauvage mort
                    if (pv_percent_sauvage <= 0) {
                        out.println("encounter KO opponent");
                        out.println("encounter win");
                        fin = true;
                        gagne = true;
                    }
                    // switch
                } else if (read.contains("encounter action switch")) {
                    out.println("encounter enter poketudiant index");
                    read = in.readLine();
                    if (read.contains("encounter poketudiant index")) {
                        int index = Integer.parseInt(read.substring(28));
                        if (index >= player.dresseur.size_poke()) {
                            out.println("encounter invalid poketudiant index");
                        } else {
                            poke_util = player.dresseur.getPoketudiants(index);
                        }
                    }
                    // catch
                } else if (read.contains("encounter action catch")) {
                    System.out.println("ici");
                    if (capture(sauvage, player)) {
                        System.out.println("test");
                        out.println("encounter catch ok");
                        fin = true;
                    } else {
                        System.out.println("merde");
                        out.println("encounter catch fail");
                    }
                    // leave
                } else if (read.contains("encounter action leave")) {
                    if (leave(poke_util, sauvage)) {
                        out.println("encounter escape ok");
                        fin = true;
                    } else {
                        out.println("encounter escape fail");
                    }
                    // index
                } else {
                    out.println("encounter forbidden action");
                }
                // tour de sauvage
                Random r = new Random();
                int random = r.nextInt(2);
                attack(poke_util, sauvage.attacks[random]);
                pv_percent_poke = (int) Math.round((poke_util.PV_current / (poke_util.PV_max * 1.0)) * 100);
                pv_percent_sauvage = (int) Math.round((sauvage.PV_current / (sauvage.PV_max * 1.0)) * 100);
                out.println("encounter poketudiant player " + poke_util.nom + " " + poke_util.level + " "
                        + pv_percent_poke + " " + poke_util.attacks[0].getNom() + " "
                        + poke_util.attacks[0].getType() + " " + poke_util.attacks[1].getNom() + " "
                        + poke_util.attacks[1].getType());
                out.println("encounter poketudiant opponent " + sauvage.nom + " " + sauvage.level + " "
                        + pv_percent_sauvage);
                if (pv_percent_poke <= 0) {
                    nb_dead++;
                    // tous poketudiant morts
                    if (nb_dead == player.dresseur.size_poke()) {
                        System.out.println("test");
                        out.println("encounter lose");
                        fin = true;
                        perdu = true;
                    }
                    // un seul poktudiant mort
                    out.println("encounter KO player");
                    out.println("encounter enter poketudiant index");
                    read = in.readLine();
                    if (read.contains("encounter poketudiant index")) {
                        int index = Integer.parseInt(read.substring(28));
                        if (index >= player.dresseur.size_poke()) {
                            out.println("encounter invalid poketudiant index");
                        } else {
                            poke_util = player.dresseur.getPoketudiants(index);
                        }
                    }
                }
            }
            // Fin combat
            if (gagne) {
                experience(sauvage, player);
            } else if (perdu) {
                for (int i = 0; i < player.dresseur.size_poke(); i++) {
                    player.dresseur.getPoketudiants(i).xp = (int) Math
                            .round(player.dresseur.getPoketudiants(i).xp * 0.8);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*public void combat_player(ClientHandler player, int id_opponent, BufferedReader in, PrintWriter out) {
        ClientHandler opponent = player.game.getPlayers(id_opponent);
        Poketudiant poke_util_opponent = opponent.dresseur.getPoketudiants(0);
        Poketudiant poke_util = player.dresseur.getPoketudiants(0);
        try {
            BufferedReader in_opponent = new BufferedReader(
                    new InputStreamReader(opponent.socketaccept.getInputStream()));
            PrintWriter out_opponent = new PrintWriter(opponent.socketaccept.getOutputStream(), true);
            String read;
            String read_opponent;
            boolean fin = false;
            boolean gagne = false;
            boolean perdu = false;
            int nb_dead = 0;
            int nb_dead_opponent = 0;
            // envoit debut combat
            out.println("encounter new rival " + opponent.dresseur.size_poke());
            out_opponent.println("encounter new rival " + player.dresseur.size_poke());
            // combat
            while (!fin) {
                int pv_percent_poke = (int) Math.round((poke_util.PV_current / (poke_util.PV_max * 1.0)) * 100);
                int pv_percent_opponent = (int) Math
                        .round((poke_util_opponent.PV_current / (poke_util_opponent.PV_max * 1.0)) * 100);
                out.println(
                        "encounter poketudiant player " + poke_util.nom + " " + poke_util.level + " " + pv_percent_poke
                                + " " + poke_util.attacks[0].getNom() + " " + poke_util.attacks[0].getType() + " "
                                + poke_util.attacks[1].getNom() + " " + poke_util.attacks[1].getType());
                out.println(
                        "encounter poketudiant opponent " + poke_util_opponent.nom + " " + poke_util_opponent.level
                                + " "
                                + pv_percent_opponent);
                out.println("encounter enter action");
                out_opponent.println(
                        "encounter poketudiant player " + poke_util_opponent.nom + " " + poke_util_opponent.level + " "
                                + pv_percent_opponent
                                + " " + poke_util_opponent.attacks[0].getNom() + " "
                                + poke_util_opponent.attacks[0].getType() + " "
                                + poke_util_opponent.attacks[1].getNom() + " "
                                + poke_util_opponent.attacks[1].getType());
                out.println(
                        "encounter poketudiant opponent " + poke_util.nom + " " + poke_util.level + " "
                                + pv_percent_poke);
                out.println("encounter enter action");
                // read
                read = in.readLine();
                read_opponent = in_opponent.readLine();

                // attack1
                if (read.contains("encounter action attack1")) {
                    if (read_opponent.contains("encounter action attack1")) {
                        Random priority = new Random();
                        int prio = priority.nextInt(2);
                        if (prio == 0) {
                            attack(poke_util_opponent, poke_util.attacks[0]);
                            attack(poke_util, poke_util_opponent.attacks[0]);
                        } else if (prio == 1) {
                            attack(poke_util, poke_util_opponent.attacks[0]);
                            attack(poke_util_opponent, poke_util.attacks[0]);
                        }

                    } else if (read_opponent.contains("encounter action attack2")) {
                        Random priority = new Random();
                        int prio = priority.nextInt(2);
                        if (prio == 0) {
                            attack(poke_util_opponent, poke_util.attacks[0]);
                            attack(poke_util, poke_util_opponent.attacks[1]);
                        } else if (prio == 1) {
                            attack(poke_util, poke_util_opponent.attacks[1]);
                            attack(poke_util_opponent, poke_util.attacks[0]);
                        }
                    } else if (read_opponent.contains("encounter action switch")) {
                        out_opponent.println("encounter enter poketudiant index");
                        read_opponent = in_opponent.readLine();
                        if (read_opponent.contains("encounter poketudiant index")) {
                            int index = Integer.parseInt(read_opponent.substring(28));
                            if (index >= opponent.dresseur.size_poke()) {
                                out_opponent.println("encounter invalid poketudiant index");
                            } else {
                                poke_util_opponent = opponent.dresseur.getPoketudiants(index);
                            }
                        }
                        attack(poke_util_opponent, poke_util.attacks[0]);
                    }
                    // sauvage mort
                    if (pv_percent_poke <= 0) {
                        nb_dead++;
                        // tous poketudiant morts
                        if (nb_dead == player.dresseur.size_poke()) {
                            System.out.println("test");
                            out.println("encounter lose");
                            fin = true;
                            perdu = true;
                        }
                    }
                    if (pv_percent_opponent <= 0) {
                        nb_dead_opponent++;
                        // tous poketudiant morts
                        if (nb_dead_opponent == opponent.dresseur.size_poke()) {
                            out_opponent.println("encounter lose");
                            fin = true;
                        }
                    }
                    // affiche maj
                    pv_percent_poke = (int) Math.round((poke_util.PV_current / (poke_util.PV_max * 1.0)) * 100);
                    pv_percent_opponent = (int) Math
                            .round((poke_util_opponent.PV_current / (poke_util_opponent.PV_max * 1.0)) * 100);
                    out.println("encounter poketudiant player " + poke_util.nom + " " + poke_util.level + " "
                            + pv_percent_poke + " " + poke_util.attacks[0].getNom() + " "
                            + poke_util.attacks[0].getType() + " " + poke_util.attacks[1].getNom() + " "
                            + poke_util.attacks[1].getType());
                    out.println("encounter poketudiant opponent " + poke_util_opponent.nom + " "
                            + poke_util_opponent.level + " "
                            + pv_percent_opponent);
                    out_opponent.println("encounter poketudiant player " + poke_util.nom + " " + poke_util.level + " "
                            + pv_percent_poke + " " + poke_util.attacks[0].getNom() + " "
                            + poke_util.attacks[0].getType() + " " + poke_util.attacks[1].getNom() + " "
                            + poke_util.attacks[1].getType());
                    out_opponent.println("encounter poketudiant opponent " + poke_util_opponent.nom + " "
                            + poke_util_opponent.level + " "
                            + pv_percent_opponent);
                    
                } else if (read.contains("encounter action attack2")) {
                    if (read_opponent.contains("encounter action attack1")) {
                        Random priority = new Random();
                        int prio = priority.nextInt(2);
                        if (prio == 0) {
                            attack(poke_util_opponent, poke_util.attacks[1]);
                            attack(poke_util, poke_util_opponent.attacks[0]);
                        } else if (prio == 1) {
                            attack(poke_util, poke_util_opponent.attacks[0]);
                            attack(poke_util_opponent, poke_util.attacks[1]);
                        }

                    } else if (read_opponent.contains("encounter action attack2")) {
                        Random priority = new Random();
                        int prio = priority.nextInt(2);
                        if (prio == 0) {
                            attack(poke_util_opponent, poke_util.attacks[1]);
                            attack(poke_util, poke_util_opponent.attacks[1]);
                        } else if (prio == 1) {
                            attack(poke_util, poke_util_opponent.attacks[1]);
                            attack(poke_util_opponent, poke_util.attacks[1]);
                        }
                    } else if (read_opponent.contains("encounter action switch")) {
                        out_opponent.println("encounter enter poketudiant index");
                        read_opponent = in_opponent.readLine();
                        if (read_opponent.contains("encounter poketudiant index")) {
                            int index = Integer.parseInt(read_opponent.substring(28));
                            if (index >= opponent.dresseur.size_poke()) {
                                out_opponent.println("encounter invalid poketudiant index");
                            } else {
                                poke_util_opponent = opponent.dresseur.getPoketudiants(index);
                            }
                        }
                        attack(poke_util_opponent, poke_util.attacks[1]);
                    }
                    // affiche maj
                    pv_percent_poke = (int) Math.round((poke_util.PV_current / (poke_util.PV_max * 1.0)) * 100);
                    pv_percent_opponent = (int) Math
                            .round((poke_util_opponent.PV_current / (poke_util_opponent.PV_max * 1.0)) * 100);
                    out.println("encounter poketudiant player " + poke_util.nom + " " + poke_util.level + " "
                            + pv_percent_poke + " " + poke_util.attacks[0].getNom() + " "
                            + poke_util.attacks[0].getType() + " " + poke_util.attacks[1].getNom() + " "
                            + poke_util.attacks[1].getType());
                    out.println("encounter poketudiant opponent " + poke_util_opponent.nom + " "
                            + poke_util_opponent.level + " "
                            + pv_percent_opponent);
                    out_opponent.println("encounter poketudiant player " + poke_util.nom + " " + poke_util.level + " "
                            + pv_percent_poke + " " + poke_util.attacks[0].getNom() + " "
                            + poke_util.attacks[0].getType() + " " + poke_util.attacks[1].getNom() + " "
                            + poke_util.attacks[1].getType());
                    out_opponent.println("encounter poketudiant opponent " + poke_util_opponent.nom + " "
                            + poke_util_opponent.level + " "
                            + pv_percent_opponent);
                    // sauvage mort
                    if (pv_percent_poke <= 0) {
                        nb_dead++;
                        // tous poketudiant morts
                        if (nb_dead == player.dresseur.size_poke()) {
                            System.out.println("test");
                            out.println("encounter lose");
                            fin = true;
                            perdu = true;
                        }
                    }
                    if (pv_percent_opponent <= 0) {
                        nb_dead_opponent++;
                        // tous poketudiant morts
                        if (nb_dead_opponent == opponent.dresseur.size_poke()) {
                            out_opponent.println("encounter lose");
                            fin = true;
                        }
                    }
                    // switch
                } else if (read.contains("encounter action switch")) {
                    if (read_opponent.contains("encounter action attack1")) {

                    } else if (read_opponent.contains("encounter action attack2")) {

                    } else if (read_opponent.contains("encounter action switch")) {

                    }
                    out.println("encounter enter poketudiant index");
                    read = in.readLine();
                    if (read.contains("encounter poketudiant index")) {
                        int index = Integer.parseInt(read.substring(28));
                        if (index >= player.dresseur.size_poke()) {
                            out.println("encounter invalid poketudiant index");
                        } else {
                            poke_util = player.dresseur.getPoketudiants(index);
                        }
                    }
                }
                // leave
                else if (read.contains("encounter action leave")) {
                    if (leave(poke_util, poke_util_opponent)) {
                        out.println("encounter escape ok");
                        fin = true;
                    } else {
                        out.println("encounter escape fail");
                    }
                    // index
                } else {
                    out.println("encounter forbidden action");
                }
                // tour de sauvage
                Random r = new Random();
                int random = r.nextInt(2);
                attack(poke_util, sauvage.attacks[random]);
                pv_percent_poke = (int) Math.round((poke_util.PV_current / (poke_util.PV_max * 1.0)) * 100);
                pv_percent_sauvage = (int) Math.round((sauvage.PV_current / (sauvage.PV_max * 1.0)) * 100);
                out.println("encounter poketudiant player " + poke_util.nom + " " + poke_util.level + " "
                        + pv_percent_poke + " " + poke_util.attacks[0].getNom() + " "
                        + poke_util.attacks[0].getType() + " " + poke_util.attacks[1].getNom() + " "
                        + poke_util.attacks[1].getType());
                out.println("encounter poketudiant opponent " + sauvage.nom + " " + sauvage.level + " "
                        + pv_percent_sauvage);
                if (pv_percent_poke <= 0) {
                    nb_dead++;
                    // tous poketudiant morts
                    if (nb_dead == player.dresseur.size_poke()) {
                        System.out.println("test");
                        out.println("encounter lose");
                        fin = true;
                        perdu = true;
                    }
                    // un seul poktudiant mort
                    out.println("encounter KO player");
                    out.println("encounter enter poketudiant index");
                    read = in.readLine();
                    if (read.contains("encounter poketudiant index")) {
                        int index = Integer.parseInt(read.substring(28));
                        if (index >= player.dresseur.size_poke()) {
                            out.println("encounter invalid poketudiant index");
                        } else {
                            poke_util = player.dresseur.getPoketudiants(index);
                        }
                    }
                }
            }
            // Fin combat
            /*
             * if (gagne) {
             * experience(sauvage, player);
             * }else if(perdu){
             * for(int i = 0; i < player.dresseur.size_poke(); i ++){
             * player.dresseur.getPoketudiants(i).xp = (int)
             * Math.round(player.dresseur.getPoketudiants(i).xp * 0.8);
             * }
             * }
             

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
