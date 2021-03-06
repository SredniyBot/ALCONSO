package covid.analise.virusanalisator.gui;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProcessInfo {

    private final List<Observer> observers =new ArrayList<>();
    //@Value("${sourceURL}")
    private volatile String sourceUrl=getPath();
    private static int definingLength=40;
    @Value("${gapInResults}")
    private volatile int scatterInResults=7;
    private volatile int minScatterInResults=3;
    private volatile int maxScatterInResults=30;
    private volatile int numberOfGenomes;
    private volatile int numberOfNGenomes;
    private volatile int numberOfDownloadedGenomes;
    private volatile boolean useNGenomes=false;
    private volatile int numberOfAnalysedGenomes;
    private volatile int numberOfCombinedGenomes;
    private volatile int numberOfGenomeErrors;
    private volatile int numberOfGenomeWarnings;
    private volatile int numberOfRightGenomes;
    private volatile int maxSequenceQuantity;
    private volatile int minSequenceQuantity;

    private volatile String Status="Processing";

    private String logs;


    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        inform(UpdateParam.SOURCE);
    }

    public int getNumberOfGenomes() {
        return numberOfGenomes-numberOfGenomeErrors;
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

    public static int getDefiningLength() {
        return definingLength;
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
        if(scatterInResults<minScatterInResults)this.scatterInResults=minScatterInResults;
        else this.scatterInResults = Math.min(scatterInResults, maxScatterInResults);
    }

    public int getMinScatterInResults() {
        return minScatterInResults;
    }

    public void setMinScatterInResults(int minScatterInResults) {
        this.minScatterInResults = minScatterInResults;
    }

    public int getMaxScatterInResults() {
        return maxScatterInResults;
    }

    public void setMaxScatterInResults(int maxScatterInResults) {
        this.maxScatterInResults = maxScatterInResults;
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

    public synchronized void increaseNumberOfCombinedGenomes() {
        this.numberOfCombinedGenomes ++;
        inform(UpdateParam.NUMBER_OF_COMBINED_GENOMES);
    }

    public int getNumberOfRightGenomes() {
        return numberOfRightGenomes;
    }

    public synchronized void increaseNumberOfRightGenomes() {
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

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
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

    public int getNumberOfGenomeErrors() {
        return numberOfGenomeErrors;
    }

    public void setNumberOfGenomeErrors(int numberOfGenomeErrors) {
        this.numberOfGenomeErrors = numberOfGenomeErrors;
        inform(UpdateParam.NUMBER_OF_ERRORS);
    }

    public int getNumberOfGenomeWarnings() {
        return numberOfGenomeWarnings;
    }

    public void setNumberOfGenomeWarnings(int numberOfGenomeWarnings) {
        this.numberOfGenomeWarnings = numberOfGenomeWarnings;
        inform(UpdateParam.NUMBER_OF_WARNINGS);
    }

    public int getMaxSequenceQuantity() {
        return maxSequenceQuantity;
    }

    public void setMaxSequenceQuantity(int maxSequenceQuantity) {
        this.maxSequenceQuantity = maxSequenceQuantity;
    }

    public int getMinSequenceQuantity() {
        return minSequenceQuantity;
    }

    public void setMinSequenceQuantity(int minSequenceQuantity) {
        this.minSequenceQuantity = minSequenceQuantity;
    }
}
