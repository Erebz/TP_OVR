public class ActionDeplacementCibler extends Action {
    private Agent agent;
    private Agent cible;

    public ActionDeplacementCibler(Environnement m, Agent a, Agent b){
        this.monde = m;
        this.agent = a;
        this.cible = b;
    }

    @Override
    public void executer() {
        int emplacement = super.monde.getEmplacementCible(agent, cible);
        if(emplacement<0) super.monde.getDeplacementPossibleAleatoire(agent);
        super.monde.deplacer(agent, emplacement);
    }
    @Override
    public String toString() {
        return "Déplacement de " + agent + ", en ciblant "+ (cible!=null?cible:"le sol") +".";
    }
}
