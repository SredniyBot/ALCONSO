package covid.analise.virusanalisator.obtaining;

import covid.analise.virusanalisator.gui.ProcessInfo;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;

public class VirusPrototype {

    private String name;
    private String fasta;

    private ArrayList<String> criticalLog;
    private String dataMessage;

    public boolean isNNN(){
        return fasta.contains("n");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFasta() {
        return fasta;
    }

    public void setFasta(String fasta) {
        criticalLog =new ArrayList<>();
        if(Strings.isBlank(fasta)){
            criticalLog.add("Fasta is empty");
            this.fasta="";
        }else {
            String f = fasta.replaceAll("a", "")
                    .replaceAll("t", "")
                    .replaceAll("g", "")
                    .replaceAll("c", "")
                    .replaceAll("n", "")
                    .replaceAll(" ", "");
            if (!f.equals("")){
                StringBuilder message= new StringBuilder();
                while (!f.equals("")){
                    fasta=fasta.replaceAll(String.valueOf(f.charAt(0)),"");
                    message.append(f.charAt(0));
                    f=f.replaceAll(String.valueOf(f.charAt(0)),"");
                }
                dataMessage ="The following characters have been replaced by n: "+ message;
            }else if ((fasta.length()<= ProcessInfo.getDefiningLength())) {
                criticalLog.add("Fasta is empty");
                this.fasta="";
            }else {
                this.fasta=fasta;
            }
        }
    }

    public String getCriticalError(){
        if(criticalLog.isEmpty()){
            return null;
        }else {
            StringBuilder msg = new StringBuilder("\t\t");
            for (String m:criticalLog){
                msg.append(m).append("\n\t\t");
            }
            return msg.toString();
        }
    }

    public String getDataMessage(){
        return dataMessage;
    }

}
