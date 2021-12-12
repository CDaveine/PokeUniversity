package Poke_University.Poketudiants;

import Poke_University.*;

public class Nuidebou extends Poketudiant{

    public Nuidebou(){
        nom = "Nuidebou";
        type = Type.Lazy;
        catchable = false;
        evolution = null;
        coef = rand_coef();
        attack = (int) Math.round(55 * coef);
        defense = (int) Math.round(85 * coef);
        PV_max = (int) Math.round(70 * coef);
        PV_current = PV_max;
        attacks[0] = Attacks.lazy_attacks[rand_attack(Attacks.lazy_attacks.length)];
        attacks[1] = other_type_attack(type);
        level = 1;
        xp = 0;
        xp_max = 500 * ((1 + level) / 2);
    }
    
}
