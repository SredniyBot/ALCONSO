package covid.analise.virusanalisator.obtaining;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class VirusCollection {

    private final HashSet<VirusPrototype> viruses;
    private final HashSet<VirusPrototype> virusesWithoutN;

    VirusCollection(){
        viruses=new HashSet<>();
        virusesWithoutN=new HashSet<>();
    }

    public synchronized void addVirus(VirusPrototype v){
        if (v.getCriticalError()==null){
            viruses.add(v);
            if(!v.isNNN()){
                virusesWithoutN.add(v);
            }
        }
    }

    public HashSet<VirusPrototype> getViruses() {
        return viruses;
    }

    public HashSet<VirusPrototype> getVirusesWithoutN() {
        return virusesWithoutN;
    }

    public void destroy(){
        viruses.clear();
        virusesWithoutN.clear();
    }

}
