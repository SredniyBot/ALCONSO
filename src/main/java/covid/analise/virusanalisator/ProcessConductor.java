package covid.analise.virusanalisator;

import covid.analise.virusanalisator.gui.ProcessInfo;
import covid.analise.virusanalisator.logger.Logger;
import covid.analise.virusanalisator.obtaining.Data;
import covid.analise.virusanalisator.obtaining.VirusCollection;
import covid.analise.virusanalisator.obtaining.VirusModel;
import covid.analise.virusanalisator.service.FilesCareService;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProcessConductor {

    private final Data data;
    private final CombineGenome combineGenome;
    private final ProcessInfo processInfo;
    private final Logger logger;

    private final FilesCareService filesCareService;
    ProcessConductor(Data data,
                     CombineGenome combineGenome,
                     ProcessInfo processInfo, Logger logger, FilesCareService filesCareService){
        this.data = data;
        this.combineGenome = combineGenome;this.processInfo = processInfo;
        this.logger = logger;
        this.filesCareService = filesCareService;
    }


    public void startWork(){
        filesCareService.startDownloading();
//        VirusCollection virusCollection= data.getVirusCollection();//Получение всех геномов из файлов
//
//        SequenceCollection sequenceCollection =fillSequenceCollection(virusCollection);
//
//        String result =combineGenome.analisePiecesAndGetResult(sequenceCollection.getBestSequencesAsMap());
//
//        recordResults(result);
//        processInfo.setStatus("Done!");
    }



    private SequenceCollection fillSequenceCollection(VirusCollection virusCollection){
        SequenceCollection sequenceCollection =new SequenceCollection(processInfo);
        HashSet<VirusModel> workSet;

        if(processInfo.isUseNGenomes()) workSet=virusCollection.getViruses();
        else workSet=virusCollection.getVirusesWithoutN();

        for(VirusModel virus:workSet){
                sequenceCollection.addSequences(VirusSlicer.sliceVirus(virus));
                processInfo.increaseNumberOfAnalysedGenomes();
        }
        return sequenceCollection;
    }

    private void recordResults(String res){
        try {
            String url=logger.getOutputDir().toString()+File.separator+"results.json";

            Path path = Files.createFile(Path.of(url));
            List<String> strings=res.lines().collect(Collectors.toList());
            Files.write(path, strings, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.addProgramError("Error 2: error writing results");
        }
    }

}
