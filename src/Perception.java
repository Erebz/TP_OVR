public class Perception {
    private Agent observeur;
    private Agent dessous;
    private boolean estPousse;
    private boolean estLibre;

    public Perception(Agent observeur, Agent dessous, boolean estPousse, boolean estLibre) {
        this.observeur = observeur;
        this.dessous = dessous;
        this.estPousse = estPousse;
        this.estLibre = estLibre;
    }

    public Agent getObserveur() {
        return observeur;
    }

    public Agent getDessous() {
        return dessous;
    }

    public boolean estPousse() {
        return estPousse;
    }

    public boolean estLibre() {
        return estLibre;
    }
}
