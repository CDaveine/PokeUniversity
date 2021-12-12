package Poke_University.Poketudiants;

import Poke_University.*;

public class Parlfor extends Poketudiant{

    public Parlfor(){
        nom = "Parlfor";
        type = Type.Noisy;
        catchable = true;
        evolution = null;
        coef = rand_coef();
        attack = (int) Math.round(60 * coef);
        defense = (int) Math.round(40 * coef);
        PV_max = (int) Math.round(60 * coef);
        PV_current = PV_max;
        attacks[0] = Attacks.noisy_attacks[rand_attack(Attacks.noisy_attacks.length)];
        attacks[1] = other_type_attack(type);
        level = 1;
        xp = 0;
        xp_max = 500 * ((1 + level) / 2);
    }

}
