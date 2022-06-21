package glasshousemanager.specifications.business;

import glasshousemanager.GlassHouseControllerMBean;
import glasshousemanager.specifications.SpecificationI;

// ça n'arrose pas ?
public class WateringIsOff implements SpecificationI<GlassHouseControllerMBean> {
    @Override
    public boolean isSatisfiedBy(GlassHouseControllerMBean ghc) {
        return ghc.getWateringState().equals("Off");
    }
}
