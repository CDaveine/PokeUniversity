package Poke_University;

public class Attacks {
    public static Attack[] noisy_attacks = new Attack[3];
    public static Attack[] lazy_attacks = new Attack[4];
    public static Attack[] motivated_attacks = new Attack[3];
    public static Attack[] teacher_attacks = new Attack[3];
    public static Attack[] attacks = new Attack[10];

    private Attack bavardage = new Attack("Bavardage", Type.Noisy, 10);
    private Attack groboucan = new Attack("Groboucan", Type.Noisy, 15);
    private Attack megaphone = new Attack("Mégaphone-vocal", Type.Noisy, 20);
    private Attack baillement = new Attack("Bâillement", Type.Lazy, 10);
    private Attack somme = new Attack("Pti'somme", Type.Lazy, 15);
    private Attack superdodo = new Attack("Superdodo", Type.Lazy, 20);
    private Attack rateletrain = new Attack("Rateletrain", Type.Lazy, 15);
    private Attack oboulo = new Attack("Oboulo", Type.Motivated, 10);
    private Attack maison = new Attack("Exo-maison", Type.Motivated, 15);
    private Attack reviz = new Attack("Max-reviz", Type.Motivated, 20);
    private Attack question = new Attack("Tit'question", Type.Teacher, 10);
    private Attack colle = new Attack("Poser-colle", Type.Teacher, 15);
    private Attack interro = new Attack("Fatal-interro", Type.Teacher, 20);

    public Attacks(){
        this.noisy_attacks[0] = bavardage;
        this.noisy_attacks[1] = groboucan;
        this.noisy_attacks[2] = megaphone;
        this.lazy_attacks[0] = baillement;
        this.lazy_attacks[1] = somme;
        this.lazy_attacks[2] = superdodo;
        this.lazy_attacks[3] = rateletrain;
        this.motivated_attacks[0] = oboulo;
        this.motivated_attacks[1] = maison;
        this.motivated_attacks[2] = reviz;
        this.teacher_attacks[0] = question;
        this.teacher_attacks[1] = colle;
        this.teacher_attacks[2] = interro;
    
        this.attacks[0] = bavardage;
        this.attacks[1] = groboucan;
        this.attacks[2] = megaphone;
        this.attacks[3] = baillement;
        this.attacks[4] = somme;
        this.attacks[5] = superdodo;
        this.attacks[6] = rateletrain;
        this.attacks[7] = oboulo;
        this.attacks[8] = maison;
        this.attacks[9] = reviz;
    
    
    }
}
