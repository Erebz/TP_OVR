public abstract class Action {
    protected Environnement monde;

    public void setEnvironnement(Environnement e){
        this.monde = e;
    }
    public abstract void executer();

    @Override
    public abstract String toString();
}
