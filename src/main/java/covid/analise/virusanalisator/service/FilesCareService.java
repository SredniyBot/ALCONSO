package covid.analise.virusanalisator.service;

import covid.analise.virusanalisator.gui.ProcessInfo;
import covid.analise.virusanalisator.logger.Logger;
import covid.analise.virusanalisator.logger.ProgramError;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilesCareService {
    private final ProcessInfo processInfo;
    private final Logger logger;
    private final FileService fileService;


    public FilesCareService(ProcessInfo processInfo, Logger logger, FileService fileService) {
        this.processInfo = processInfo;
        this.logger = logger;
        this.fileService = fileService;
    }

    public void startDownloading(){
        File source=new File(processInfo.getSourceUrl());
        if (source.exists()) {
            processInfo.setNumberOfGenomes(countFiles(source));
            for(File file: getFiles(source)) {
                fileService.pushFileToDB(file);
            }
            processInfo.setNumberOfGenomeErrors(logger.getNumberOfGenomeErrors());
            processInfo.setNumberOfGenomeWarnings(logger.getNumberOfGenomeWarnings());
        }else{
            logger.addProgramError(ProgramError.getProgramErrorById(6));
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

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

}
