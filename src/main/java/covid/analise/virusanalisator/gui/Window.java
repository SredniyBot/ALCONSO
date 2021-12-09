package covid.analise.virusanalisator.gui;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Component
public class Window extends JFrame implements Observer{

    private final ProcessInfo processInfo;

    ArrayList<Runnable> startActivity=new ArrayList<>();

    Window(ProcessInfo processInfo){
        this.processInfo = processInfo;
    }

    private volatile boolean isWorking=true;
    private JLabel srcUrl;
    private JLabel outUrl;
    private JLabel numberOfGenomes;
    private JLabel numberOfNGenomes;
    private JLabel numberOfDownloadedGenomes;
    private JLabel numberOfAnalysedGenomes;
    private JLabel numberOfRightGenomes;
    private JLabel status;

    private JSlider chooseScatter;
    private JCheckBox useN;
    private JProgressBar downloadingOfGenomes;
    private JProgressBar analysedGenomes;
    private JProgressBar sortingOfGenomes;

    private final Color bgColor =new Color(80, 106, 128, 255);
    private final Color btnColor=new Color(47, 96, 134, 255);
    private final Color bottomColor=new Color(28, 49, 77, 255);

    private JButton start;
    private JButton sourceButton;
    private JButton destinationButton;

    private long startTime;

    public void createWindow(){
        setSize(420,500);
        setMinimumSize(new Dimension(500,600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel mainPanel=new JPanel(new BorderLayout());
        JPanel startPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        JPanel infoPanel=new JPanel(new GridLayout(10,1,40,-10));


        srcUrl=new JLabel(processInfo.getSourceUrl());

        sourceButton=getFolderBtn("Choose input folder",true);
        infoPanel.add(
                getPanelWithParams("Input folder: ",srcUrl,sourceButton));


        outUrl=new JLabel(processInfo.getDestinationUrl());

        destinationButton=getFolderBtn("Choose output folder",false);
        infoPanel.add(
                getPanelWithParams("Output folder: ",outUrl,destinationButton));


        numberOfGenomes=new JLabel(String.valueOf(processInfo.getNumberOfGenomes()));
        numberOfNGenomes=new JLabel(String.valueOf(processInfo.getNumberOfNGenomes()));
        numberOfDownloadedGenomes=new JLabel(String.valueOf(processInfo.getNumberOfDownloadedGenomes()));
        numberOfAnalysedGenomes=new JLabel(String.valueOf(processInfo.getNumberOfAnalysedGenomes()));

        downloadingOfGenomes=new JProgressBar(0,0,processInfo.getNumberOfGenomes());
        downloadingOfGenomes.setBackground(btnColor);
        downloadingOfGenomes.setBorderPainted(false);

        chooseScatter =new JSlider(3,15,7);
        chooseScatter.setMinorTickSpacing(1);
        chooseScatter.setMajorTickSpacing(6);
        chooseScatter.setPaintTicks(true);
        chooseScatter.setPaintLabels(true);
        chooseScatter.addChangeListener(e->processInfo.setScatterInResults(chooseScatter.getValue()));
        chooseScatter.setBackground(bgColor);

        infoPanel.add(getPanelWithParams("Choose scatter in results(%):", chooseScatter));


        useN =new JCheckBox("Use genomes with n ");
        useN.addActionListener(e->processInfo.setUseNGenomes(!processInfo.isUseNGenomes()));
        useN.setBackground(bgColor);
        useN.setBorder(BorderFactory.createLineBorder(btnColor));


        infoPanel.add(getPanelWithParams("Number of genomes: ",numberOfGenomes));
        infoPanel.add(getPanelWithParams("Number of genomes with N: ",numberOfNGenomes,useN));
        infoPanel.add(getPanelWithParams("Downloaded genomes: ",numberOfDownloadedGenomes,downloadingOfGenomes));

        analysedGenomes =new JProgressBar(0,0,processInfo.getNumberOfGenomes());
        analysedGenomes.setBackground(btnColor);
        analysedGenomes.setBorderPainted(false);
        infoPanel.add(getPanelWithParams("Analysed genomes: ",numberOfAnalysedGenomes,analysedGenomes));


        numberOfRightGenomes=new JLabel(String.valueOf(processInfo.getNumberOfRightGenomes()));
        infoPanel.add(getPanelWithParams("Number of right genomes: ",numberOfRightGenomes));


        sortingOfGenomes =new JProgressBar(0,0,processInfo.getNumberOfGenomes());
        sortingOfGenomes.setBackground(btnColor);
        sortingOfGenomes.setBorderPainted(false);


        status=new JLabel(processInfo.getStatus());
        infoPanel.add(getPanelWithParams("Status: ",status));


        start=new JButton("Start");
        start.setBackground(btnColor);
        start.addActionListener(e -> startBeginningActivities());

        infoPanel.setBackground(bgColor);
        startPanel.add(start);
        startPanel.setBackground(bottomColor);
        mainPanel.add(infoPanel,BorderLayout.CENTER);
        mainPanel.add(startPanel,BorderLayout.SOUTH);

        add(mainPanel);

        setVisible(true);
    }

    private void startBeginningActivities(){
        closeInterface();
        startTime=System.currentTimeMillis();
        Thread timeThread = new Thread(() -> {
            while (isWorking) {
                long t = System.currentTimeMillis() - startTime;
                String date = t / 3600000 + ":" + t / 60000 % 60 + ":" + t / 1000 % 60;
                status.setText(processInfo.getStatus() + " " + date);
                try {
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

    public void setOnStart(Runnable runnable){
        startActivity.add(runnable);
    }

    private JButton getFolderBtn(String text,boolean isSource){
        JButton choose=new JButton(text);
        choose.setBackground(btnColor);
            choose.addActionListener(e -> {
            String src=chooseDirectory();
            if(src!=null)
            if(isSource)processInfo.setSourceUrl(src);
            else processInfo.setDestinationUrl(src);
        });
        return choose;
    }

    private JPanel getPanelWithParams(String comment, java.awt.Component ... components){
        JPanel panel =new JPanel(new FlowLayout( FlowLayout.LEFT));
        panel.add(new JLabel(comment));
        panel.setBackground(bgColor);
        for(java.awt.Component component:components){
            panel.add(component);
        }
        panel.revalidate();
        panel.repaint();
        return panel;
    }

    private String chooseDirectory(){
        JFileChooser chooseFile=new JFileChooser();
        chooseFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooseFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            return chooseFile.getSelectedFile().getPath();
        }
        return null;
    }

    private void closeInterface(){
        start.setEnabled(false);
        sourceButton.setEnabled(false);
        destinationButton.setEnabled(false);
        useN.setEnabled(false);
        chooseScatter.setEnabled(false);
    }

    @Override
    public void changeState(UpdateParam updateParam) {
        switch (updateParam){
            case SOURCE:
                srcUrl.setText(processInfo.getSourceUrl());
                break;
            case DESTINATION:
                outUrl.setText(processInfo.getDestinationUrl());
                break;
            case NUMBER_OF_GENOMES:
                numberOfGenomes.setText(String.valueOf(processInfo.getNumberOfGenomes()));
                break;
            case NUMBER_OF_N_GENOMES:
                numberOfNGenomes.setText(String.valueOf(processInfo.getNumberOfNGenomes()));
                break;
            case NUMBER_OF_DOWNLOADED_GENOMES:
                numberOfDownloadedGenomes.setText(String.valueOf(processInfo.getNumberOfDownloadedGenomes()));
                downloadingOfGenomes.setValue(processInfo.getNumberOfDownloadedGenomes());
                downloadingOfGenomes.setMaximum(processInfo.getNumberOfGenomes());
                break;
            case NUMBER_OF_ANALYSED_GENOMES:
                numberOfAnalysedGenomes.setText(String.valueOf(processInfo.getNumberOfAnalysedGenomes()));
                if(processInfo.isUseNGenomes()) {
                    analysedGenomes.setMaximum(processInfo.getNumberOfGenomes());
                }else {
                    analysedGenomes.setMaximum(processInfo.getNumberOfGenomes() - processInfo.getNumberOfNGenomes());
                }
                analysedGenomes.setValue(processInfo.getNumberOfAnalysedGenomes());
                break;
            case NUMBER_OF_RIGHT_GENOMES:
                numberOfRightGenomes.setText(String.valueOf(processInfo.getNumberOfRightGenomes()));
                sortingOfGenomes.setMaximum(processInfo.getNumberOfRightGenomes());
                break;
            case STATUS:
                isWorking=false;
                long t = System.currentTimeMillis() - startTime;
                String date = t / 3600000 + ":" + t / 60000 % 60 + ":" + t / 1000 % 60;
                status.setText(processInfo.getStatus() + " " + date);
                break;
        }

    }


}
