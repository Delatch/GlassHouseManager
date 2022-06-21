package glasshousemanager;

import com.sun.jdmk.comm.HtmlAdaptorServer;
import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;

/**
 * Classe d'enregistrement de la station
 * Offre un connecteur RMI et un adpatateur HTML pour connection distante
 */
public class WeatherChannelAgent {
    private MBeanServer mbs;
    private static int interval = 5 * 1000;

    public WeatherChannelAgent(String city) {
        mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName name = new ObjectName("WeatherChannelAgent:name=" + city);
            WeatherChannel station = new WeatherChannel(interval, city);
            mbs.registerMBean(station, name);

            // Create an RMI connector and start it
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/weatherchannel");
            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);

            // Create an HTTP adapter and start it
            HtmlAdaptorServer adapter = new HtmlAdaptorServer();
            adapter.setPort(8088);
            name = new ObjectName("HtmlAdaptorServer:name=html,port=8088");
            mbs.registerMBean(adapter, name);

            adapter.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

//    public static void main(String[] args) throws InterruptedException {
//        String city = "Lorient";
//
//        try{
//            if(args[0] != null)
//                interval = Integer.parseInt(args[0]);
//        }
//        catch(Exception e){}
//
//        try{
//            if(args[1] != null)
//                city = args[1];
//        }
//        catch(Exception e){}
//
//        WeatherChannelAgent agent = new WeatherChannelAgent(city);
//
//        Thread.sleep(Integer.MAX_VALUE);
//    }
//}
