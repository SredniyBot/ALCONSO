package covid.analise.virusanalisator.obtaining;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import covid.analise.virusanalisator.gui.ProcessInfo;
import covid.analise.virusanalisator.logger.Logger;
import covid.analise.virusanalisator.logger.ProgramError;
import org.springframework.stereotype.Component;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class Data {

    private final ProcessInfo processInfo;
    private final VirusCollection virusCollection;
    private final Gson jsonDecoder = new Gson();
    private final Logger logger;

    Data(VirusCollection virusCollection, ProcessInfo processInfo, Logger logger) {
        this.virusCollection = virusCollection;
        this.processInfo = processInfo;
        this.logger = logger;
    }

    public VirusCollection getVirusCollection(){
        File source=new File(processInfo.getSourceUrl());
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        if (source.exists()) {
            processInfo.setNumberOfGenomes(countFiles(source));
            manageTasks(source,service);
            service.shutdown();
            try {
                if(service.awaitTermination(1000, TimeUnit.DAYS)){
                    System.out.println("Sorry, you were waiting for too long...");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            logger.addProgramError(ProgramError.getProgramErrorById(6));
        }
        return virusCollection;
    }

    private void manageTasks(File source, ExecutorService service){
        for(File currentFile: getFiles(source)) {
            Runnable runnable= () -> {
                try {
                    String content = getFileContent(currentFile);
                    VirusPrototype prototype=getVirusPrototypeFromJson(content,currentFile.getAbsolutePath());
                    virusCollection.addVirus(prototype);
                    fillProcessInfo(prototype);
                } catch (IOException e) {
                    logger.addDataError("File read error: "+currentFile.getAbsolutePath());
                } catch (JsonSyntaxException e){
                    logger.addDataError("Json format error: "+currentFile.getAbsolutePath());
                }
            };
            service.submit(runnable);
        }
    }

    private int countFiles(File source){
        if(source.isDirectory()){
            int answer =getFiles(source).size();
            if(answer==0)logger.addProgramError(ProgramError.getProgramErrorById(4));
            return answer;
        }else {
            logger.addProgramError(ProgramError.getProgramErrorById(3));
            return 0;
        }
    }

    private List<File> getFiles(File sourceFile){
        ArrayList<File> list =new ArrayList<>();
        if(sourceFile==null) return list;
        File[] source=sourceFile.listFiles();
        if(source==null) return list;
        for (File s:source) {
            if (s.isDirectory()){
                list.addAll(getFiles(s));
            } else if(getFileExtension(s).equals("json")){
                list.add(s);
            }
        }
        return list;
    }

    private synchronized void fillProcessInfo(VirusPrototype virusPrototype){
        processInfo.increaseNumberOfDownloadedGenomes();
        if(virusPrototype.isNNN()){
            processInfo.increaseNumberOfNGenomes();
        }
    }

    public VirusPrototype getVirusPrototypeFromJson(String json,String path) throws JsonSyntaxException {
        VirusJson virusJson=jsonDecoder.fromJson(json, (Type) VirusJson.class);
        VirusPrototype virusPrototype = new VirusPrototype();
        virusPrototype.setName(virusJson.dataset);
        virusPrototype.setFasta(virusJson.sequence.toLowerCase());
        if(virusPrototype.getCriticalError()!=null){
            logger.addDataError("Data format error (The genome is not taken into account):\n\t "+ path+
                    "\n\t\t"+virusPrototype.getCriticalError());
        }else {
            if (virusPrototype.getDataMessage() != null) {
                logger.addDataWarning("Data format warning \n\t " + path +
                        "\n\t\t" + virusPrototype.getDataMessage());
            }
        }
        return virusPrototype;
    }

    public String getFileContent(File source) throws IOException {
        return Files.readString(source.toPath());
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
