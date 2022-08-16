package covid.analise.virusanalisator;

import covid.analise.virusanalisator.gui.ProcessInfo;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class SequenceCollection {

    private final ConcurrentHashMap<String, Sequence> sequences;
    private final ProcessInfo processInfo;

    public SequenceCollection(ProcessInfo processInfo) {
        this.processInfo = processInfo;
        sequences=new ConcurrentHashMap<>();
    }

    private void addSequence(Sequence seq){
        if(sequences.containsKey(seq.getSequence()))
            sequences.get(seq.getSequence()).addSequenceQuantity(seq.getQuantity());
        else sequences.put(seq.getSequence(), seq);

    }

    public void addSequences(HashSet<Sequence> set){
        for (Sequence seq:set) addSequence(seq);
    }

    public String toString() {
        StringBuilder s= new StringBuilder();
        LinkedHashSet<Sequence> sorted =sequences.values().stream().sorted(Comparator.comparingInt(Sequence::getQuantity).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for(Sequence seq:sorted){
            s.append("{").append(seq.toString()).append("}\n");
        }
        return s.toString();
    }

    public ArrayList<Sequence> getBestSequences(){
        var reference = new Object() {
            double max = 0;
        };
        for(Sequence s:sequences.values()){
            if(s.getQuantity()> reference.max){
                reference.max =s.getQuantity();
            }
        }
        processInfo.setMaxSequenceQuantity((int) reference.max);
        processInfo.setMinSequenceQuantity((int) (reference.max-reference.max*processInfo.getScatterInResults()/100));

        ArrayList<Sequence> sorted = new ArrayList<>(sequences.values());
        double i= (reference.max- reference.max*processInfo.getScatterInResults()/100);
        sorted.removeIf(sequence -> sequence.getQuantity()< i);
        return sorted;
    }

    public HashMap<String, Sequence> getBestSequencesAsMap(){
        ArrayList<Sequence> sequences =getBestSequences();

        HashMap<String, Sequence> seqs = new HashMap<>();
        for(Sequence s:sequences) seqs.put(s.getSequence(),s);
        return seqs;
    }

}