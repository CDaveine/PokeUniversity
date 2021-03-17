package Poke_University.Poketudiants;

import Poke_University.*;

public class Procrastino extends Poketudiant{
    
    public Procrastino(){
        nom = "Procrastino";
        type = Type.Lazy;
        catchable = true;
        evolution = null;
        coef = rand_coef();
        attack = (int) Math.round(40 * coef);
        defense = (int) Math.round(60 * coef);
        PV_max = (int) Math.round(60 * coef);
        PV_current = PV_max;
        attacks[0] = Attacks.lazy_attacks[rand_attack(Attacks.lazy_attacks.length)];
        attacks[1] = other_type_attack(type);
        level = 1;
        xp = 0;
        xp_max = 500 * ((1 + level) / 2);
    }

}
