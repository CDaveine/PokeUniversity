package Poke_University;

public class Attack {
    private String nom;
    private Type type;
    private int puissance;

    public Attack(String nom, Type type, int puissance){
        this.nom = nom;
        this.type = type;
        this.puissance = puissance;
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

    public int getPuissance() {
        return puissance;
    }

    public void setPuissance(int puissance) {
        this.puissance = puissance;
    }

    

}
