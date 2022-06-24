package glasshousemanager.commands;

import glasshousemanager.GlassHouseControllerMBean;

public class WaterOff extends Command<GlassHouseControllerMBean, Boolean> {
    public Boolean execute(GlassHouseControllerMBean ghc, Boolean b){
        ghc.waterOff();
        return true;
    }
}
