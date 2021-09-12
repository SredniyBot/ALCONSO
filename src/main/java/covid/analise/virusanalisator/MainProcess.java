package covid.analise.virusanalisator;

import covid.analise.virusanalisator.gui.ProcessInfo;
import covid.analise.virusanalisator.gui.Window;
import covid.analise.virusanalisator.obtaining.Data;
import covid.analise.virusanalisator.obtaining.VirusCollection;
import covid.analise.virusanalisator.obtaining.VirusPrototype;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class MainProcess {

    private final Data data;
    private final VirusSlicer virusSlicer;
    private final SequenceCollection sequenceCollection;
    private final CombineGenome combineGenome;
    private final Window window;
    private final ProcessInfo processInfo;
    private String destinationUrl;

    MainProcess(Window window, Data data, VirusSlicer virusSlicer,
                SequenceCollection sequenceCollection, CombineGenome combineGenome, ProcessInfo processInfo){
        this.data = data;
        this.virusSlicer = virusSlicer;
        this.sequenceCollection = sequenceCollection;
        this.combineGenome = combineGenome;
        this.window = window;
        this.processInfo = processInfo;
    }

    @PostConstruct
    public void init(){
        window.setOnStart(()->startWork());
    }

    public void startWork(){
        data.getResources();
        createDirectory();
        VirusCollection virusCollection=data.getVirusCollection();
        startProcessing(virusCollection);
        String result =combineGenome.analisePieces(sequenceCollection.getBestSequencesAsMap());
        System.out.println("write results");
        recordResults(result);
        processInfo.setStatus("Done!");
    }

    public void createDirectory(){
        destinationUrl=processInfo.getDestinationUrl();
        destinationUrl+="\\"+new Date().toString().replaceAll(" ","_").replaceAll(":","-");
        new File(destinationUrl).mkdir();
    }

    private void startProcessing(VirusCollection virusCollection){
        ExecutorService service= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        HashSet<VirusPrototype> workSet;

        if(processInfo.isUseNGenomes())workSet=virusCollection.getViruses();
        else workSet=virusCollection.getVirusesWithoutN();

        for(VirusPrototype virus:workSet){
            service.submit(() -> {
                sequenceCollection.addSequences(virusSlicer.sliceVirus(virus));
                processInfo.increaseNumberOfAnalysedGenomes();
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(2, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void recordResults(String res){
        try {
            System.out.println(destinationUrl);
            new File(destinationUrl+"\\results.json").createNewFile();
            Path path = Paths.get(destinationUrl+"\\results.json");
            List<String> strings=res.lines().collect(Collectors.toList());
            Files.write(path, strings, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
