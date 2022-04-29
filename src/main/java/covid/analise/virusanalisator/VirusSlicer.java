package covid.analise.virusanalisator;

import covid.analise.virusanalisator.gui.ProcessInfo;
import covid.analise.virusanalisator.obtaining.VirusPrototype;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class VirusSlicer {

    public HashSet<Sequence> sliceVirus(VirusPrototype virus){
        String fasta=virus.getFasta();
        HashSet<Sequence> sequences=new HashSet<>();
        for(int i = 0; i<fasta.length()- ProcessInfo.getDefiningLength(); i++) {
            sequences.add(new Sequence(fasta.substring(i,i+ ProcessInfo.getDefiningLength())));
        }
        return sequences;
    }

}
