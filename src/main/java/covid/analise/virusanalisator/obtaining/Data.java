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
import java.util.zip.DataFormatException;

@Component
public class Data {
//
//    private final ProcessInfo processInfo;
//    private final VirusCollection virusCollection;
//    private final Logger logger;
//
//    Data(VirusCollection virusCollection, ProcessInfo processInfo, Logger logger) {
//        this.virusCollection = virusCollection;
//        this.processInfo = processInfo;
//        this.logger = logger;
//    }
//
//    public VirusCollection getVirusCollection(){
//        File source=new File(processInfo.getSourceUrl());
//        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        if (source.exists()) {
//            processInfo.setNumberOfGenomes(countFiles(source));
//            manageTasks(source,service);
//            service.shutdown();
//
//            processInfo.setNumberOfGenomeErrors(logger.getNumberOfGenomeErrors());
//            processInfo.setNumberOfGenomeWarnings(logger.getNumberOfGenomeWarnings());
//        }else{
//            logger.addProgramError(ProgramError.getProgramErrorById(6));
//        }
//        return virusCollection;
//    }
//
//
//    private int countFiles(File source){
//        if(source.isDirectory()){
//            int answer =getFiles(source).size();
//            if(answer==0)logger.addProgramError(ProgramError.getProgramErrorById(4));
//            return answer;
//        }else {
//            logger.addProgramError(ProgramError.getProgramErrorById(3));
//            return 0;
//        }
//    }
//
//    private List<File> getFiles(File sourceFile){
//        ArrayList<File> list =new ArrayList<>();
//        if(sourceFile==null) return list;
//        File[] source=sourceFile.listFiles();
//        if(source==null) return list;
//        for (File s:source) {
//            if (s.isDirectory()){
//                list.addAll(getFiles(s));
//            } else if(getFileExtension(s).equals("json")){
//                list.add(s);
//            }
//        }
//        return list;
//    }
//
//    private synchronized void fillProcessInfo(VirusModel virusModel){
//        processInfo.increaseNumberOfDownloadedGenomes();
//        if(virusModel.isNNN()){
//            processInfo.increaseNumberOfNGenomes();
//        }
//    }
//
//
//
//    private static String getFileExtension(File file) {
//        String fileName = file.getName();
//        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
//            return fileName.substring(fileName.lastIndexOf(".")+1);
//        else return "";
//    }
}
