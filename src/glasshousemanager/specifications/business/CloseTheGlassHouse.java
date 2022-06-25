package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.All;
import glasshousemanager.specifications.And;
import glasshousemanager.specifications.Not;
import glasshousemanager.specifications.Or;

/**
 * On ferme la serre si
 * - Elle est ouverte
 * - On est au-dessous du seuil d'ouverture OU il pleut
 */
public class CloseTheGlassHouse extends All<GlassHouseControllerMBean> {
    public CloseTheGlassHouse() {
        this.add(new And<GlassHouseControllerMBean>(new Not<GlassHouseControllerMBean>(new IsClosed()),
                new Or<GlassHouseControllerMBean>(new IsRaining(), new TemperatureUnderOpeningFloor())));;
    }
}
