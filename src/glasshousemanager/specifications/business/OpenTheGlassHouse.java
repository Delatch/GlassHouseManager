package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.All;
import glasshousemanager.specifications.Not;

/**
 * On ouvre la serre si
 * - Elle est fermée
 * - ET on est au-dessus du seuil d'ouverture
 * - ET il ne pleut pas
 */
public class OpenTheGlassHouse  extends All<GlassHouseControllerMBean> {
    public OpenTheGlassHouse(){
        this.add(new TemperatureAboveOrEqualsOpeningFloor())
                .add(new Not<GlassHouseControllerMBean>(new IsOpened()))
                .add(new Not<GlassHouseControllerMBean>(new IsRaining()));
    }
}
