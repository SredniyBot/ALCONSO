package covid.analise.virusanalisator.obtaining;

import com.google.gson.Gson;
import covid.analise.virusanalisator.gui.ProcessInfo;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class Data {
    private final ProcessInfo processInfo;
    private final VirusCollection virusCollection;
    private final Gson jsonDecoder = new Gson();

    Data(VirusCollection virusCollection, ProcessInfo processInfo) {
        this.virusCollection = virusCollection;
        this.processInfo = processInfo;
    }


    public void getResources(){
        File source=new File(processInfo.getSourceUrl());
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        if (source.exists()) {
            processInfo.setNumberOfGenomes(countFiles(source));
            manageTasks(source,service);
            service.shutdown();
            try {
                service.awaitTermination(7, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null,"NO GENOME FOLDER");
        }
    }

    private void manageTasks(File source, ExecutorService service){
        for(File currentFile: getFiles(Arrays.asList(source.listFiles()))) {
            Runnable runnable= () -> {
                String content=getFileContent(currentFile);
                VirusPrototype prototype=getVirusPrototypeFromJson(content);
                virusCollection.addVirus(prototype);
                fillProcessInfo(prototype);
            };
            service.submit(runnable);
        }
    }


    private int countFiles(File source){
        if(source.isDirectory()){
            return getFiles(Arrays.asList(source.listFiles())).size();
        }
        return 0;
    }

    private List<File> getFiles(List<File> source){
        ArrayList<File> list =new ArrayList<>();
        for (File s:source) {
            if (s.isDirectory()){
                if (s.listFiles() != null) {
                    list.addAll(getFiles(Arrays.asList(s.listFiles())));
                }
            } else list.add(s);
        }
        return list;
    }

    private synchronized void fillProcessInfo(VirusPrototype virusPrototype){
        processInfo.increaseNumberOfDownloadedGenomes();
        if(virusPrototype.isNNN()){
            processInfo.increaseNumberOfNGenomes();
        }
    }

    public VirusPrototype getVirusPrototypeFromJson(String json){
        VirusJson virusJson=null;
        try {
            virusJson=jsonDecoder.fromJson(json, (Type) VirusJson.class);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error in json format","Error message", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return decodeJson(virusJson);
    }

    public String getFileContent(File source){
        InputStream is = null;
        try {
            is = new FileInputStream(source);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Error of reading file","Error message", JOptionPane.ERROR_MESSAGE);
        }
        assert is != null;
        Scanner sc = new Scanner(is, StandardCharsets.UTF_8);
        StringBuilder content = new StringBuilder();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().toLowerCase();
            content.append(line);
        }
        return content.toString();
    }

    private VirusPrototype decodeJson(VirusJson v){
        VirusPrototype virusPrototype = new VirusPrototype();
        virusPrototype.setName(v.dataset);
        virusPrototype.setFasta(v.sequence);
        return virusPrototype;
    }

    public VirusCollection getVirusCollection() {
        return virusCollection;
    }

}
