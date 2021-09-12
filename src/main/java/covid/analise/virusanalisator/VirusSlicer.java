package covid.analise.virusanalisator;

import covid.analise.virusanalisator.gui.ProcessInfo;
import covid.analise.virusanalisator.obtaining.VirusPrototype;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class VirusSlicer {
    final ProcessInfo processInfo;

    public VirusSlicer(ProcessInfo processInfo) {
        this.processInfo = processInfo;
    }

    public HashSet<Sequence> sliceVirus(VirusPrototype virus){
        String fasta=virus.getFasta();
        HashSet<Sequence> seqs=new HashSet<>();

        for(int i=0;i<fasta.length()-processInfo.getDefiningLength();i++) {
            seqs.add(new Sequence(fasta.substring(i,i+processInfo.getDefiningLength())));
        }
        return seqs;
    }

}
