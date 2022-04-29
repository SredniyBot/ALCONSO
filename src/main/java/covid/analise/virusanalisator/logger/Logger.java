package covid.analise.virusanalisator.logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

@Component
@Scope("singleton")
public class Logger {

    private ArrayList<String> programErrors;
    private ArrayList<String> dataErrors;
    private ArrayList<String> dataWarnings;
    private Path outputDir;
    private Path logFile;
    @Value("${isConsoled}")
    private boolean isConsoled;

    public void startLogger(){
        programErrors=new ArrayList<>();
        dataErrors=new ArrayList<>();
        dataWarnings=new ArrayList<>();
        createDirectory();
    }

    public void createDirectory(){
        String destinationUrl="results"+File.separator+
                new Date().toString().replaceAll(" ","_").replaceAll(":","-");
        try {
            outputDir=Files.createDirectories(Path.of(destinationUrl));
            logFile=Files.createFile(Path.of(destinationUrl+File.separator+"log.txt"));
        } catch (IOException e) {
            System.out.println(ProgramError.getProgramErrorById(0)+"\n"+destinationUrl);
            System.exit(0);
        }
    }

    public Path getOutputDir() {
        return outputDir;
    }

    public synchronized void addProgramError(String info){
        programErrors.add(info);
        writeData();
        System.out.println("Critical error, check log file");
        System.exit(0);
    }

    public synchronized void addDataError(String info){
        dataErrors.add(info);
        writeData();
    }

    public synchronized void addDataWarning(String info){
        dataWarnings.add(info);
        writeData();
    }

    public void writeData(){
        try {
            Files.writeString(logFile,this.toString());
        } catch (IOException e) {
            System.out.println(ProgramError.getProgramErrorById(5));
        }
    }

    @Override
    public String toString() {
        StringBuilder result=new StringBuilder();
        if(!programErrors.isEmpty()){
            result.append("Critical program execution errors:\n\n\t");
            for (String error:programErrors)
                result.append(error).append("\n\t");
            result.append("\n");
        }

        if(!dataErrors.isEmpty()){
            result.append("Critical data errors:\n\n\t");
            for (String error:dataErrors)
                result.append(error).append("\n\t");
            result.append("\n");
        }

        if(!dataWarnings.isEmpty()){
            result.append("Data warnings:\n\n\t");
            for (String error:dataWarnings)
                result.append(error).append("\n\t");
            result.append("\n");
        }

        return result.toString();
    }

    public void message(String message){
        if (isConsoled) {
            System.out.println(message);
        }else {
            JOptionPane.showConfirmDialog(null,message);
        }
    }


    public int getNumberOfGenomeErrors(){
        return dataErrors.size();
    }
    public int getNumberOfGenomeWarnings(){
        return dataWarnings.size();
    }
}
