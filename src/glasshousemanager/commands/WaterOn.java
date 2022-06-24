package glasshousemanager.commands;

import glasshousemanager.GlassHouseControllerMBean;

public class WaterOn extends Command<GlassHouseControllerMBean, Boolean> {
    public Boolean execute(GlassHouseControllerMBean ghc, Boolean b){
        ghc.waterOn();
        return true;
    }
}
