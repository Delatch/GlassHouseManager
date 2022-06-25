package glasshousemanager;

import com.sun.jdmk.comm.HtmlAdaptorServer;
import glasshousemanager.utils.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Classe d'enregistrement de la station
 * Offre un connecteur RMI et un adpatateur HTML pour connection distante
 */
public class WeatherChannelAgent {
    private static int interval = 5 * 1000;

    /**
     * Agent chargé de l'instanciation et de l'enregistrement de la station météo pour accès distant
     * (Browser HTML, client/console RMI)
     * Permet la modification de la périodicité
     */
    public WeatherChannelAgent(String city) {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        try {
            ObjectName name = new ObjectName("WeatherChannelAgent:name=" + city);
            WeatherChannel station = new WeatherChannel(interval, city);
            mbs.registerMBean(station, name);

            // Create an RMI connector and start it
            Registry registry = LocateRegistry.createRegistry(9999);
            // Create an RMI connector and start it
            String localHost = InetAddress.getLocalHost().getHostAddress();
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + localHost +":9999/weatherchannel");
            Logger.log("Enregistrement de la station météo ici : " + "service:jmx:rmi:///jndi/rmi://" + localHost +":9999/weatherchannel");
            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
            cs.start();

            // Create an HTTP adapter and start it
            HtmlAdaptorServer adapter = new HtmlAdaptorServer();
            adapter.setPort(8088);
            name = new ObjectName("HtmlAdaptorServer:name=WeatherChannel,port=8088");
            mbs.registerMBean(adapter, name);

            adapter.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        String city = "Lorient";

        try{
            if(args[0] != null)
                interval = Integer.parseInt(args[0]);
        }
        catch(Exception e){}

        try{
            if(args[1] != null)
                city = args[1];
        }
        catch(Exception e){}

        new WeatherChannelAgent(city);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
