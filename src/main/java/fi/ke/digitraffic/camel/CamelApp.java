package fi.ke.digitraffic.camel;

import org.apache.camel.main.Main;

public class CamelApp {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new DigitrafficRouteBuilder());
        main.run(args);
    }
}
