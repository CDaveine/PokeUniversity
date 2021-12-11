package Poke_University.Poketudiants;

import Poke_University.*;

public class Enseignant_dresseur extends Poketudiant {
    private Poketudiant[] poketudiants = new Poketudiant[3];

    public Poketudiant getPoketudiants(int n) {
        return poketudiants[n];
    }

    public void setPoketudiants(Poketudiant[] poketudiants) {
        this.poketudiants = poketudiants;
    }

    public Enseignant_dresseur() {
        nom = "Enseignant_dresseur";
        type = Type.Teacher;
        catchable = false;
        evolution = null;
        coef = rand_coef();
        attack = (int) Math.round(100 * coef);
        defense = (int) Math.round(100 * coef);
        PV_max = (int) Math.round(100 * coef);
        PV_current = PV_max;
        attacks[0] = Attacks.teacher_attacks[rand_attack(Attacks.teacher_attacks.length)];
        attacks[1] = other_type_attack(type);
        level = 1;
        xp = 0;
        xp_max = 500 * ((1 + level) / 2);
        poketudiants[0] = this;
        poketudiants[1] = null;
        poketudiants[2] = null;
    }

    public int size_poke() {
        int size = 0;
        for (int i = 0; i < poketudiants.length; i++) {
            if (poketudiants[i] != null) {
                size++;
            }
        }
        return size;
    }

}
