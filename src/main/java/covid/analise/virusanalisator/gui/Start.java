package covid.analise.virusanalisator.gui;

import covid.analise.virusanalisator.MainProcess;
import covid.analise.virusanalisator.VirusAnalisatorApplication;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class Start{

    private final ProcessInfo processInfo;
    private final MainProcess mainProcess;


    Start(ProcessInfo processInfo, MainProcess mainProcess){
        this.processInfo = processInfo;
        this.mainProcess = mainProcess;
    }
    
    @PostConstruct
    private void start(){
        if(VirusAnalisatorApplication.isConsoled()){
            Console console =new Console(processInfo);
            console.setOnStart(mainProcess::startWork);
            processInfo.addObserver(console);
            console.runConsole();
        }else {
            try {
                Window window=new Window(processInfo);
                window.setOnStart(mainProcess::startWork);
                processInfo.addObserver(window);
                window.createWindow();
            }catch (Exception e){
                System.out.println("Your system doesnt support java gui," +
                        " use 'consoled' parameter to start application in console");
            }
        }
    }

}
