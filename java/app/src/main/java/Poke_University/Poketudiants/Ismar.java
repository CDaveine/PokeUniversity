package Poke_University.Poketudiants;

import Poke_University.*;

public class Ismar extends Poketudiant{
    
    public Ismar(){
        nom = "Ismar";
        type = Type.Noisy;
        catchable = true;
        Rigolamor rig = new Rigolamor();
        evolution = rig;
        coef = rand_coef();
        attack = (int) Math.round(50 * coef);
        defense = (int) Math.round(30 * coef);
        PV_max = (int) Math.round(40 * coef);
        PV_current = PV_max;
        attacks[0] = Attacks.noisy_attacks[rand_attack(Attacks.noisy_attacks.length)];
        attacks[1] = other_type_attack(type);
        level = 1;
        xp = 0;
        xp_max = 500 * ((1 + level) / 2);
    }

}
