package covid.analise.virusanalisator.obtaining;

import covid.analise.virusanalisator.gui.ProcessInfo;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;

public class VirusModel {

    private String name;
    private String sequence;
    private String dataMessage;

    public boolean isNNN(){
        return sequence.contains("n");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) throws VirusModelException {
        if(Strings.isBlank(sequence)|| sequence.equals("")){
            throw new VirusModelException("Fasta is empty");
        }else {
            String f = sequence.replaceAll("a", "")
                    .replaceAll("t", "")
                    .replaceAll("g", "")
                    .replaceAll("c", "")
                    .replaceAll("n", "")
                    .replaceAll(" ", "");
            if (!f.equals("")){
                StringBuilder message= new StringBuilder();
                while (!f.equals("")){
                    sequence = sequence.replaceAll(String.valueOf(f.charAt(0)),"");
                    message.append(f.charAt(0));
                    f=f.replaceAll(String.valueOf(f.charAt(0)),"");
                }
                this.sequence = sequence;
                dataMessage ="The following characters have been replaced by n: "+ message;
            }else if ((sequence.length()<= ProcessInfo.getDefiningLength())) {
                throw new VirusModelException("Fasta is too small");
            }else {
                this.sequence = sequence;
            }
        }
    }



    public String getDataMessage(){
        return dataMessage;
    }

    static class VirusModelException extends IOException {
        public VirusModelException(String message) {
            super(message);
        }
    }
}
