package covid.analise.virusanalisator.gui;

import java.io.File;
import java.util.Scanner;

public class Console implements Observer{

    private final ProcessInfo processInfo;

    private long startTime;
    private boolean first=true;

    public Console(ProcessInfo processInfo){
        this.processInfo=processInfo;
    }

    public void run(){
        processInfo.setSourceUrl(getDir());
        processInfo.setUseNGenomes(useN());
        processInfo.setScatterInResults(getScatter());
        startTime=System.currentTimeMillis();
    }

    private String getDir(){
        System.out.println("\n\n\nChoose input folder:");
        Scanner scanner =new Scanner(System.in);
        while (scanner.hasNextLine()){
            String res=scanner.nextLine();
            if(new File(res).exists()){
                return res;
            }
        }
        return null;
    }

    private boolean useN(){
        System.out.println("Include the genomes with ‘N’? (y/n)");
        Scanner scanner =new Scanner(System.in);
        String res=scanner.nextLine();
        return res.contains("y")||res.contains("Y");
    }

    private int getScatter(){
        System.out.println("\nSet scatter in results: "+processInfo.getMinScatterInResults()+"%-"
                +processInfo.getMaxScatterInResults()+"%   - default: "+processInfo.getScatterInResults()+"%");
        Scanner scanner =new Scanner(System.in);
        try {
            String res =scanner.nextLine();
            int i = Integer.parseInt(res.replace("%", ""));
            scanner.close();
            System.out.println("Scatter in results set as: " + i+"%");
            return i;
        }catch (Exception e){
            System.out.println("Scatter in results set as default: "+ processInfo.getScatterInResults());
        }
        return processInfo.getScatterInResults();
    }

    public String getDate(){
        long t = System.currentTimeMillis() - startTime;
        return t / 3600000 + ":" + t / 60000 % 60 + ":" + t / 1000 % 60;
    }


    private String getScale(double current,double max){
        int percent=(int)(current/(max)*100);
        String res;
        if(percent<5){
            res="|                    | "+percent+"% ";
        }else if(percent<10){
            res="|=                   | "+percent+"% ";
        }else if(percent<15){
            res="|==                  | "+percent+"% ";
        }else if(percent<20){
            res="|===                 | "+percent+"% ";
        }else if(percent<25){
            res="|====                | "+percent+"% ";
        }else if(percent<30){
            res="|=====               | "+percent+"% ";
        }else if(percent<35){
            res="|======              | "+percent+"% ";
        }else if(percent<40){
            res="|=======             | "+percent+"% ";
        }else if(percent<45){
            res="|========            | "+percent+"% ";
        }else if(percent<50){
            res="|=========           | "+percent+"% ";
        }else if(percent<55){
            res="|==========          | "+percent+"% ";
        }else if(percent<60){
            res="|===========         | "+percent+"% ";
        }else if(percent<65){
            res="|============        | "+percent+"% ";
        }else if(percent<70){
            res="|=============       | "+percent+"% ";
        }else if(percent<75){
            res="|==============      | "+percent+"% ";
        }else if(percent<80){
            res="|===============     | "+percent+"% ";
        }else if(percent<85){
            res="|================    | "+percent+"% ";
        }else if(percent<90){
            res="|=================   | "+percent+"% ";
        }else if(percent<95){
            res="|==================  | "+percent+"% ";
        }else if(percent<100){
            res="|=================== | "+percent+"% ";
        }else res="|====================| "+percent+"% ";
        return res;
    }


    @Override
    public void changeState(UpdateParam updateParam) {
        switch (updateParam) {
            case SOURCE -> consoleIt("Path '" + processInfo.getSourceUrl() + "' set as source path");
            case NUMBER_OF_GENOMES -> consoleIt(processInfo.getNumberOfGenomes() + " genomes found      " + getDate() + "\n");
            case NUMBER_OF_N_GENOMES, NUMBER_OF_DOWNLOADED_GENOMES -> {
                if (first) {
                    first = false;
                    consoleIt("Downloading of genomes have been started\n");
                }
                System.out.print(getScale(
                        processInfo.getNumberOfDownloadedGenomes(),
                        processInfo.getNumberOfGenomes()) + processInfo.getNumberOfDownloadedGenomes() + "/" + processInfo.getNumberOfGenomes() +
                        "     genomes with n: " +
                        processInfo.getNumberOfNGenomes() + "/" + processInfo.getNumberOfGenomes() + "    " +
                        getDate() +
                        "                          \r");
            }
            case NUMBER_OF_ANALYSED_GENOMES -> {
                if (!first) {
                    first = true;
                    consoleIt("\n\nAnalysing of genomes have been started\n");
                }
                if (processInfo.isUseNGenomes()) {
                    System.out.print(getScale(processInfo.getNumberOfAnalysedGenomes(), processInfo.getNumberOfGenomes()) +
                            processInfo.getNumberOfAnalysedGenomes() + "/" + processInfo.getNumberOfGenomes() +
                            "       " + getDate() +
                            "                          \r");

                } else {
                    System.out.print(getScale(processInfo.getNumberOfAnalysedGenomes(),
                            processInfo.getNumberOfGenomes() - processInfo.getNumberOfNGenomes()) +
                            processInfo.getNumberOfAnalysedGenomes() + "/" +
                            (processInfo.getNumberOfGenomes() - processInfo.getNumberOfNGenomes()) +
                            "       " + getDate() +
                            "                          \r");
                }
            }
            case NUMBER_OF_RIGHT_GENOMES -> {
                if (first) {
                    first = false;
                    consoleIt("\nGetting of right genomes have been started");
                }
                System.out.print("Number of right genomes: " + processInfo.getNumberOfRightGenomes() + "        " + getDate() + "                            \r");
            }
            case STATUS -> {
                consoleIt(processInfo.getStatus() + " " + getDate());
                if (processInfo.getLogs() != null) {
                    consoleIt("Logs:\n" + processInfo.getLogs());
                }
            }
            case NUMBER_OF_ERRORS -> System.out.println("Number of genome errors: " + processInfo.getNumberOfGenomeErrors());
            case NUMBER_OF_WARNINGS -> System.out.println("Number of genome warnings: " + processInfo.getNumberOfGenomeWarnings());
        }

    }


    private void consoleIt(String str){
        System.out.println(str);
    }
}
