package covid.analise.virusanalisator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class VirusAnalisatorApplication {

    private static boolean consoled=false;
    public static void main(String[] args) {
        //TODO console separation

        SpringApplicationBuilder builder = new SpringApplicationBuilder(VirusAnalisatorApplication.class);
        builder.headless(false);
        builder.run(args);
    }

    public static boolean isConsoled(){
        return consoled;
    }

}
