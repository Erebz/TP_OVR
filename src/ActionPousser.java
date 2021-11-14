public class ActionPousser extends Action{
    private Agent agent;

    public ActionPousser(Agent a){
        this.agent = a;
    }

    @Override
    public void executer() {
        super.monde.pousser(agent);
        //System.out.println("- "+agent+" pousse au dessus...");
    }
    @Override
    public String toString() {
        return "" + agent + " pousse.";
    }
}