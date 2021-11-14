public class ActionDeplacementEviter extends Action {
    private Agent agent;
    private Agent evite;

    public ActionDeplacementEviter(Environnement m, Agent a, Agent b){
        this.monde = m;
        this.agent = a;
        this.evite = b;
    }

    @Override
    public void executer() {
        int emplacement = super.monde.getEmplacementEvite(agent, evite);
        if(emplacement<0) super.monde.getDeplacementPossibleAleatoire(agent);
        super.monde.deplacer(agent, emplacement);
    }

    @Override
    public String toString() {
        return "Déplacement de " + agent + ", en évitant "+ (evite!=null?evite:"le sol") +".";
    }
}
