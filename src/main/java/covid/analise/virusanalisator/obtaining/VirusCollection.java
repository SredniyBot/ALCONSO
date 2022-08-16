package covid.analise.virusanalisator.obtaining;

import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class VirusCollection {

    private final HashSet<VirusModel> viruses;
    private final HashSet<VirusModel> virusesWithoutN;

    VirusCollection(){
        viruses=new HashSet<>();
        virusesWithoutN=new HashSet<>();
    }

    public synchronized void addVirus(VirusModel v){
        viruses.add(v);
        if(!v.isNNN()){
            virusesWithoutN.add(v);
        }

    }

    public HashSet<VirusModel> getViruses() {
        return viruses;
    }

    public HashSet<VirusModel> getVirusesWithoutN() {
        return virusesWithoutN;
    }

}
