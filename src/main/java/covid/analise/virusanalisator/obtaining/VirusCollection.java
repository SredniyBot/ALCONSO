package covid.analise.virusanalisator.obtaining;

import org.springframework.stereotype.Component;
import java.util.HashSet;

@Component
public class VirusCollection {

    private final HashSet<VirusPrototype> viruses;
    private final HashSet<VirusPrototype> virusesWithoutN;

    VirusCollection(){
        viruses=new HashSet<>();
        virusesWithoutN=new HashSet<>();
    }

    public synchronized void addVirus(VirusPrototype v){
        viruses.add(v);
        if(!v.isNNN()){
            virusesWithoutN.add(v);
        }
    }

    public HashSet<VirusPrototype> getViruses() {
        return viruses;
    }

    public HashSet<VirusPrototype> getVirusesWithoutN() {
        return virusesWithoutN;
    }
}
