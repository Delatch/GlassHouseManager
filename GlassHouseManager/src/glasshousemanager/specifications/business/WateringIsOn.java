package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.SpecificationI;

// ça arrose ?
public class WateringIsOn implements SpecificationI<GlassHouseControllerMBean> {
    @Override
    public boolean isSatisfiedBy(GlassHouseControllerMBean ghc) {
        return ghc.getWateringState().equals("On");
    }
}
