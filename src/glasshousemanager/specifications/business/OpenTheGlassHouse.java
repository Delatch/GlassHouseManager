package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.All;
import glasshousemanager.specifications.And;
import glasshousemanager.specifications.Not;

/**
 * On ouvre la serre si
 * - Elle est fermée
 * - ET on est au-dessus du seuil d'ouverture
 * - ET il ne pleut pas
 */
public class OpenTheGlassHouse  extends All<GlassHouseControllerMBean> {
    public OpenTheGlassHouse(){
        this.add(new And<GlassHouseControllerMBean>(
                    new And<GlassHouseControllerMBean>(new TemperatureAboveOrEqualsOpeningFloor(),
                                                    new Not<GlassHouseControllerMBean>(new IsOpened())),
                new Not<GlassHouseControllerMBean>(new IsRaining())));
    }
}

