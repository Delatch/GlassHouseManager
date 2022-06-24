package glasshousemanager;

public interface GlassHouseControllerMBean {
    // Le temps qu'il fait
    double getTemperature();
    String getDescription();
    // Pleut-il ?
    boolean getRainingState();

    // De quand datent les dernières infos ?
    String getLastUpdateTime();

    // température au-delà de laquelle il faut ouvrir
    int getOpeningTemperature();
    void setOpeningTemperature(int temp);
    // la serre est-elle ouverte ? Fermée ?
    String getOpeningState();

    // température au-delà de laquelle il faut faire une aspersion
    int getWateringTemperature();
    void setWateringTemperature(int temp);
    // L'irrigation est-elle en marche ? Arrêtée ?
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
