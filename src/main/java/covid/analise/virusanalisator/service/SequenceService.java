package covid.analise.virusanalisator.service;

import covid.analise.virusanalisator.entity.Sequence;
import covid.analise.virusanalisator.gui.ProcessInfo;
import covid.analise.virusanalisator.repos.SequenceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class SequenceService {

    private final SequenceRepository sequenceRepository;


    public SequenceService(SequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    public List<Long> sliceAndSave(String fasta){
        List<Long> ids=new ArrayList<>();
        for(Sequence sequence:sliceVirus(fasta)){
            Sequence currentSeq= sequenceRepository.findBySequence(sequence.getSequence());
            if(currentSeq==null){
                ids.add(sequenceRepository.save(sequence).getId());
            }else {
                currentSeq.setQuantity(currentSeq.getQuantity()+1);
                ids.add(sequenceRepository.save(currentSeq).getId());
            }
        }
        return ids;
    }

    public HashSet<Sequence> sliceVirus(String fasta){
        HashSet<Sequence> sequences=new HashSet<>();
        for(int i = 0; i<fasta.length()- ProcessInfo.getDefiningLength(); i++) {
            sequences.add(new Sequence(fasta.substring(i,i+ ProcessInfo.getDefiningLength())));
        }
        return sequences;
    }


}
