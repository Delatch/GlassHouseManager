package glasshousemanager;

public interface GlassHouseControllerMBean {
    // Le temps qu'il fait
    double getTemperature();

    String getDescription();

    boolean getRainingState();

    // De quand datent les dernières infos ?
    String getLastUpdateTime();

    // température au-delà de laquelle il faut ouvrir
    int getOpeningTemperature();

    void setOpeningTemperature(int temp);

    String getOpeningState();

    // température au-delà de laquelle il faut faire une aspersion
    int getWateringTemperature();

    void setWateringTemperature(int temp);

    String getWateringState();

    // actions possibles
    void close();

    void open();

    void waterOn();

    void waterOff();

    // des erreurs ?
    String getLastActionError();

    String getLastActionErrorTime();
}
