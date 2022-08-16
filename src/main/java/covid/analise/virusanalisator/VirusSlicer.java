package covid.analise.virusanalisator;

import covid.analise.virusanalisator.gui.ProcessInfo;
import covid.analise.virusanalisator.obtaining.VirusModel;

import java.util.HashSet;

public class VirusSlicer {

    public static HashSet<Sequence> sliceVirus(VirusModel virus){
        String fasta=virus.getSequence();
        HashSet<Sequence> sequences=new HashSet<>();
        for(int i = 0; i<fasta.length()- ProcessInfo.getDefiningLength(); i++) {
            sequences.add(new Sequence(fasta.substring(i,i+ ProcessInfo.getDefiningLength())));
        }
        return sequences;
    }

}
