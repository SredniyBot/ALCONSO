package covid.analise.virusanalisator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AlconsoApplication {


    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(AlconsoApplication.class);
        builder.headless(false);
        builder.run(args);
    }


}
