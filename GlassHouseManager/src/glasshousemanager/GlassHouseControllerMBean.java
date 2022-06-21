package glasshousemanager;

public interface GlassHouseControllerMBean {
    // Le temps qu'il fait
    public double getTemperature();
    public boolean getRainingState();

    // température au-delà de laquelle il faut ouvrir
    public int getOpeningTemperature();
    public void setOpeningTemperature(int temp);

    public String getOpeningState();

    // température au-delà de laquelle il faut faire une aspersion
    public int getWateringTemperature();
    public void setWateringTemperature(int temp);

    public String getWateringState();

    // actions possibles
    public void close();
    public void open();
    public void waterOn();
    public void waterOff();

    // des erreurs ?
    public String getLastActionError();
    public String getLastActionErrorTime();
}
