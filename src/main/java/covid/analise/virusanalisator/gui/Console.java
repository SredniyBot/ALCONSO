package covid.analise.virusanalisator.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Console implements Observer{

    private final ProcessInfo processInfo;
    ArrayList<Runnable> startActivity=new ArrayList<>();

    private long startTime;
    private volatile boolean isWorking=true;
    private String date="0";

    private boolean first=true;

    public Console(ProcessInfo processInfo){
        this.processInfo=processInfo;
    }

    public void runConsole(){
        processInfo.setSourceUrl(getDir("\n\n\nChoose input folder:"));
        processInfo.setDestinationUrl(getDir("Choose output folder:"));
        processInfo.setUseNGenomes(useN());
        startBeginningActivities();
    }


    private String getDir(String message){
        System.out.println(message);
        Scanner scanner =new Scanner(System.in);
        String res="";
        while (scanner.hasNextLine()){
            res=scanner.nextLine();
            if(new File(res).exists()){
                return res;
            }
        }
        return null;
    }

    private boolean useN(){
        System.out.println("Choose genomes with N? (y/n)");
        Scanner scanner =new Scanner(System.in);
        String res=scanner.nextLine();
        scanner.close();
        return res.contains("y")||res.contains("Y");
    }

    public void setOnStart(Runnable runnable){
        startActivity.add(runnable);
    }

    private void startBeginningActivities(){
        startTime=System.currentTimeMillis();
        Thread timeThread = new Thread(() -> {
            while (isWorking) {
                long t = System.currentTimeMillis() - startTime;
                date = t / 3600000 + ":" + t / 60000 % 60 + ":" + t / 1000 % 60;
                try {                                                               //TODO
                    Thread.sleep(900);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timeThread.start();
        for(Runnable runnable:startActivity){
            Thread thread=new Thread(runnable);
            thread.start();
        }
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
        switch (updateParam){
            case SOURCE:
                consoleIt("Path '"+processInfo.getSourceUrl()+"' set as source path");
                break;
            case DESTINATION:
                consoleIt("Path '"+processInfo.getDestinationUrl()+"' set as destination path");
                break;
            case NUMBER_OF_GENOMES:
                consoleIt(processInfo.getNumberOfGenomes()+ " genomes found      " +date+"\n");
                break;
            case NUMBER_OF_N_GENOMES:
            case NUMBER_OF_DOWNLOADED_GENOMES:
                if(first){
                    first=false;
                    consoleIt("Downloading of genomes have been started\n");
                }
                System.out.print(getScale(processInfo.getNumberOfDownloadedGenomes(),processInfo.getNumberOfGenomes())+
                        processInfo.getNumberOfDownloadedGenomes()+"/"+processInfo.getNumberOfGenomes()+
                        "     genomes with n: "+
                        processInfo.getNumberOfNGenomes()+"/"+processInfo.getNumberOfGenomes()+"    "+
                        date+
                        "                          \r");
                break;
            case NUMBER_OF_ANALYSED_GENOMES:
                if(!first){
                    first=true;
                    consoleIt("\n\nAnalysing of genomes have been started\n");
                }
                if(processInfo.isUseNGenomes()) {
                    System.out.print(getScale(processInfo.getNumberOfAnalysedGenomes(),processInfo.getNumberOfGenomes())+
                            processInfo.getNumberOfAnalysedGenomes()+"/"+processInfo.getNumberOfGenomes()+
                            "       "+date+
                            "                          \r");

                }else {
                    System.out.print(getScale(processInfo.getNumberOfAnalysedGenomes(),
                            processInfo.getNumberOfGenomes() - processInfo.getNumberOfNGenomes())+
                            processInfo.getNumberOfAnalysedGenomes()+"/"+
                            (processInfo.getNumberOfGenomes() - processInfo.getNumberOfNGenomes())+
                            "       "+date+
                            "                          \r");
                }
                break;
            case NUMBER_OF_RIGHT_GENOMES:
                if(first){
                    first=false;
                    consoleIt("\nGetting of right genomes have been started");
                }
                System.out.print("Number of right genomes: "+processInfo.getNumberOfRightGenomes()+"        "+date+"                            \r");
                break;
            case STATUS:
                isWorking=false;
                long t = System.currentTimeMillis() - startTime;
                date = t / 3600000 + ":" + t / 60000 % 60 + ":" + t / 1000 % 60;
                consoleIt(processInfo.getStatus() + " " + date);
                if(processInfo.getLogs()!=null){
                    consoleIt("Logs:\n"+processInfo.getLogs());
                }
                break;
        }

    }


    private void consoleIt(String str){
        System.out.println(str);
    }
}
