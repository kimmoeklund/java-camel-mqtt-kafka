package fi.ke.digitraffic.camel;

import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.main.Main;

public class CamelApp {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        PropertiesComponent pc = new PropertiesComponent();
        pc.addLocation("classpath:application.properties");
        main.bind("properties", pc);
        main.configure().addRoutesBuilder(new DigitrafficRouteBuilder());
        main.run(args);
    }
}
