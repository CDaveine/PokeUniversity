package Poke_University.Poketudiants;

import Poke_University.*;

public class Couchtar extends Poketudiant{
    
    public Couchtar(){
        nom = "Couchtar";
        type = Type.Lazy;
        catchable = true;
        Nuidebou nui = new Nuidebou();
        evolution = nui;
        coef = rand_coef();
        attack = (int) Math.round(30 * coef);
        defense = (int) Math.round(50 * coef);
        PV_max = (int) Math.round(40 * coef);
        PV_current = PV_max;
        attacks[0] = Attacks.lazy_attacks[rand_attack(Attacks.lazy_attacks.length)];
        attacks[1] = other_type_attack(type);
        level = 1;
        xp = 0;
        xp_max = 500 * ((1 + level) / 2);
    }
}
