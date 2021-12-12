package Poke_University.Poketudiants;


import Poke_University.Attacks;
import Poke_University.Poketudiant;
import Poke_University.Type;

public class Alabourre extends Poketudiant{

    public Alabourre(){
        nom = "Alabourre";
        type = Type.Lazy;
        catchable = true;
        evolution = null;
        coef = rand_coef();
        attack = (int) Math.round(75 * coef);
        defense = (int) Math.round(95 * coef);
        PV_max = (int) Math.round(65 * coef);
        PV_current = PV_max;
        attacks[0] = Attacks.lazy_attacks[rand_attack(Attacks.lazy_attacks.length)];
        attacks[1] = other_type_attack(type);
        level = 1;
        xp = 0;
        xp_max = 500 * ((1 + level) / 2);
    }

    
}
