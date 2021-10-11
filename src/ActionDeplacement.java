public class ActionDeplacement extends Action{
    private Agent agent;

    public ActionDeplacement(Agent a){
        this.agent = a;
    }

    @Override
    public void executer() {
        int emplacement = super.monde.getDeplacementPossibleAleatoire(agent);
        super.monde.deplacer(agent, emplacement);
        System.out.println("- " + agent + " -> " + emplacement);
    }
}
