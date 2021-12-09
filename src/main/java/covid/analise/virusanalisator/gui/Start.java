package covid.analise.virusanalisator.gui;

import covid.analise.virusanalisator.MainProcess;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Start{

    private final ProcessInfo processInfo;
    private final MainProcess mainProcess;
    private final Window window;
    private final Console console;

    @Value("${isConsoled}")
    private boolean isConsoled;

    Start(ProcessInfo processInfo, MainProcess mainProcess, Window window, Console console){
        this.processInfo = processInfo;
        this.mainProcess = mainProcess;
        this.window = window;
        this.console = console;
    }
    
    @PostConstruct
    private void start(){
        System.out.println("fgfgfg");
        if(isConsoled){
            console.setOnStart(mainProcess::startWork);
            processInfo.addObserver(console);
            console.runConsole();
        }else {
            window.setOnStart(mainProcess::startWork);
            processInfo.addObserver(window);
            window.createWindow();
        }
    }

}
