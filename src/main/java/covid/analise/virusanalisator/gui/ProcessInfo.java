package covid.analise.virusanalisator.gui;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProcessInfo {

    private List<Observer> observers =new ArrayList<>();
    //@Value("${sourceURL}")
    private volatile String sourceUrl=getPath();
    //@Value("${URLout}")
    private volatile String destinationUrl=getPath();
    @Value("${length}")
    private int definingLength;
    @Value("${gapInResults}")
    private volatile int scatterInResults;
    private volatile int numberOfGenomes;
    private volatile int numberOfNGenomes;
    private volatile int numberOfDownloadedGenomes;
    private volatile boolean useNGenomes=false;
    private volatile int numberOfAnalysedGenomes;
    private volatile int numberOfCombinedGenomes;
    private volatile int numberOfRightGenomes;

    private volatile String Status="Processing";



    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        inform(UpdateParam.SOURCE);
    }

    public String getDestinationUrl() {
        return destinationUrl;
    }

    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
        inform(UpdateParam.DESTINATION);
    }

    public int getNumberOfGenomes() {
        return numberOfGenomes;
    }

    public synchronized void setNumberOfGenomes(int number) {
        this.numberOfGenomes =number;
        inform(UpdateParam.NUMBER_OF_GENOMES);
    }

    public int getNumberOfNGenomes() {
        return numberOfNGenomes;
    }

    public synchronized void increaseNumberOfNGenomes() {
        this.numberOfNGenomes ++;
        inform(UpdateParam.NUMBER_OF_N_GENOMES);
    }

    public int getNumberOfDownloadedGenomes() {
        return numberOfDownloadedGenomes;
    }

    public synchronized void increaseNumberOfDownloadedGenomes() {
        this.numberOfDownloadedGenomes++;
        inform(UpdateParam.NUMBER_OF_DOWNLOADED_GENOMES);
    }

    public int getDefiningLength() {
        return definingLength;
    }

    public void setDefiningLength(int definingLength) {
        this.definingLength = definingLength;
//        inform();
    }

    public String getStatus() {
        return Status;
    }

    public synchronized void setStatus(String status) {
        Status = status;
        inform(UpdateParam.STATUS);
    }

    public int getNumberOfAnalysedGenomes() {
        return numberOfAnalysedGenomes;
    }

    public int getScatterInResults() {
        return scatterInResults;
    }

    public void setScatterInResults(int scatterInResults) {
        this.scatterInResults = scatterInResults;
    }

    public synchronized void increaseNumberOfAnalysedGenomes() {
        this.numberOfAnalysedGenomes ++;
        inform(UpdateParam.NUMBER_OF_ANALYSED_GENOMES);
    }

    public boolean isUseNGenomes() {
        return useNGenomes;
    }

    public void setUseNGenomes(boolean useNGenomes) {
        this.useNGenomes = useNGenomes;
    }

    public int getNumberOfCombinedGenomes() {
        return numberOfCombinedGenomes;
    }

    public void increaseNumberOfCombinedGenomes() {
        this.numberOfCombinedGenomes ++;
        inform(UpdateParam.NUMBER_OF_COMBINED_GENOMES);
    }

    public int getNumberOfRightGenomes() {
        return numberOfRightGenomes;
    }

    public void increaseNumberOfRightGenomes() {
        this.numberOfRightGenomes ++;
        inform(UpdateParam.NUMBER_OF_RIGHT_GENOMES);
    }


    public void addObserver(Observer observer){
        observers.add(observer);
    }

    private void inform(UpdateParam updateParam){
        for(Observer observer:observers){
            observer.changeState(updateParam);
        }
    }

    private String  getPath(){
        String path=ProcessInfo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if(path.contains("file:/"))
            path=path.replaceAll("file:/","");
        else path=path.substring(1);
        path=path.replace('/',File.separatorChar);

        while (path.contains("!")){
            path=path.substring(0,path.lastIndexOf("!"));
            path=path.substring(0,path.lastIndexOf(File.separatorChar));
        }
        return path;
    }
}
