package covid.analise.virusanalisator.gui;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ProcessInfo {

    private  Window window ;
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
        notifyWindow();
    }

    public String getDestinationUrl() {
        return destinationUrl;
    }

    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
        notifyWindow();
    }

    public int getNumberOfGenomes() {
        return numberOfGenomes;
    }

    public synchronized void setNumberOfGenomes(int number) {
        this.numberOfGenomes =number;
        notifyWindow();
    }

    public int getNumberOfNGenomes() {
        return numberOfNGenomes;
    }

    public synchronized void increaseNumberOfNGenomes() {
        this.numberOfNGenomes ++;
        notifyWindow();
    }

    public int getNumberOfDownloadedGenomes() {
        return numberOfDownloadedGenomes;
    }

    public synchronized void increaseNumberOfDownloadedGenomes() {
        this.numberOfDownloadedGenomes++;
        notifyWindow();
    }

    public int getDefiningLength() {
        return definingLength;
    }

    public void setDefiningLength(int definingLength) {
        this.definingLength = definingLength;
        notifyWindow();
    }

    public String getStatus() {
        return Status;
    }

    public synchronized void setStatus(String status) {
        Status = status;
        notifyWindow();
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
        notifyWindow();
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
    }

    public int getNumberOfRightGenomes() {
        return numberOfRightGenomes;
    }

    public void increaseNumberOfRightGenomes() {
        this.numberOfRightGenomes ++;
        notifyWindow();
    }

    @Autowired
    public void setWindow(Window window) {
        this.window = window;
    }

    public void notifyWindow(){
        window.rewriteData();
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
