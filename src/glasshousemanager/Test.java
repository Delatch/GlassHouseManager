package glasshousemanager;


public class Test {
    public static void main(String args[]) throws InterruptedException {
        String city = "Lorient";
        int interval = 2000;

        try {
            if (args[0] != null)
                interval = Integer.parseInt(args[0]);
        } catch (Exception e) {
        }

        try {
            if (args[1] != null)
                city = args[1];
        } catch (Exception e) {
        }

        WeatherChannelAgent agent = new WeatherChannelAgent(city);
        GlassHouseControllerAgent agent2 = new GlassHouseControllerAgent("localhost", "Lorient");
        GlassHouseDevices ghs = new GlassHouseDevices();

        Thread.sleep(Integer.MAX_VALUE);
    }
}
