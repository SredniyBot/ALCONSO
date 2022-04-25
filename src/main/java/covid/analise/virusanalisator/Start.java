package covid.analise.virusanalisator;

import covid.analise.virusanalisator.gui.Console;
import covid.analise.virusanalisator.gui.ProcessInfo;
import covid.analise.virusanalisator.gui.Window;
import covid.analise.virusanalisator.logger.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class Start{

    @Value("${isConsoled}")
    private boolean isConsoled;

    private final ProcessInfo processInfo;
    private final ProcessConductor processConductor;
    private final Logger logger;

    Start(ProcessInfo processInfo, ProcessConductor processConductor, Logger logger){
        this.processInfo = processInfo;
        this.processConductor = processConductor;
        this.logger = logger;
    }

    @PostConstruct
    private void start(){

        logger.startLogger();

        if(isConsoled){
            Console console =new Console(processInfo);
            console.setOnStart(processConductor::startWork);
            processInfo.addObserver(console);
            console.runConsole();
        }else {
            try {
                Window window=new Window(processInfo);
                window.setOnStart(processConductor::startWork);
                processInfo.addObserver(window);
                window.createWindow();
            }catch (Exception e){
                System.out.println("Your system doesnt support java gui," +
                        " use 'consoled' parameter to start application in console");
            }
        }
    }

}
