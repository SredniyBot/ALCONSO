package covid.analise.virusanalisator;

import covid.analise.virusanalisator.gui.ProcessInfo;
import covid.analise.virusanalisator.logger.Logger;
import covid.analise.virusanalisator.obtaining.Data;
import covid.analise.virusanalisator.obtaining.VirusCollection;
import covid.analise.virusanalisator.obtaining.VirusPrototype;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Component
public class ProcessConductor {

    private final Data data;
    private final VirusSlicer virusSlicer;
    private final SequenceCollection sequenceCollection;
    private final CombineGenome combineGenome;
    private final ProcessInfo processInfo;
    private final Logger logger;

    ProcessConductor(Data data,
                     VirusSlicer virusSlicer,
                     SequenceCollection sequenceCollection,
                     CombineGenome combineGenome,
                     ProcessInfo processInfo, Logger logger){
        this.data = data;
        this.virusSlicer = virusSlicer;
        this.sequenceCollection = sequenceCollection;
        this.combineGenome = combineGenome;this.processInfo = processInfo;
        this.logger = logger;
    }


    public void startWork(){

        VirusCollection virusCollection= data.getVirusCollection();



        startProcessing(virusCollection);

        String result =combineGenome.analisePiecesAndGetResult(sequenceCollection.getBestSequencesAsMap());

        recordResults(result);
        processInfo.setStatus("Done!");

    }



    private void startProcessing(VirusCollection virusCollection){
        ExecutorService service= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        HashSet<VirusPrototype> workSet;



        if(processInfo.isUseNGenomes()) workSet=virusCollection.getViruses();
        else workSet=virusCollection.getVirusesWithoutN();

        for(VirusPrototype virus:workSet){
            service.submit(() -> {
                sequenceCollection.addSequences(virusSlicer.sliceVirus(virus));
                processInfo.increaseNumberOfAnalysedGenomes();
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(10000, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void recordResults(String res){
        try {                               //TODO
            String url=logger.getOutputDir().toString()+File.separator+"results.json";

            Path path = Files.createFile(Path.of(url));
            List<String> strings=res.lines().collect(Collectors.toList());
            Files.write(path, strings, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.addProgramError("Error 2: error writing results");
        }
    }

}
