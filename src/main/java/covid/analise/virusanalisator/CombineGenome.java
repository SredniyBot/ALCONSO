package covid.analise.virusanalisator;

import covid.analise.virusanalisator.gui.ProcessInfo;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class CombineGenome {

    private final ProcessInfo processInfo;

    public CombineGenome(ProcessInfo processInfo) {
        this.processInfo = processInfo;
    }

    public String analisePiecesAndGetResult(HashMap<String, Sequence> map) {
        StringBuilder result = new StringBuilder();
        ArrayList<Sequence> array = collect(getSortedList(map));
        result.append("{\n\"definingLength\": \"")
                .append(ProcessInfo.getDefiningLength()).append("\",\n")
                .append(" \"scatterInResults\": \"")
                .append(processInfo.getScatterInResults())
                .append("\",\n").append(" \"numberOfGenomes\": \"")
                .append(processInfo.getNumberOfGenomes())
                .append("\",\n").append(" \"numberOfNGenomes\": \"")
                .append(processInfo.getNumberOfNGenomes())
                .append("\",\n").append(" \"useNGenomes\": \"")
                .append(processInfo.isUseNGenomes()).append("\",\n").append(" \"numberOfAnalysedGenomes\": \"");
        if(processInfo.isUseNGenomes()){
            result.append(processInfo.getNumberOfDownloadedGenomes()).append("\",\n");
        }else {
            result.append(processInfo.getNumberOfDownloadedGenomes() - processInfo.getNumberOfNGenomes()).append("\",\n");
        }

        result.append(" \"genomes\": [\n");

        for (Sequence seq : array) {
            result.append(seq).append(",\n");
        }
        result.deleteCharAt(result.length()-1);
        result.append("\n]\n}");
        return result.toString();
    }

    private ArrayList<LinkedHashSet<Sequence>> getSortedList(HashMap<String, Sequence> map) {
        ArrayList<LinkedHashSet<Sequence>> sequenceArray = new ArrayList<>();
        while (!map.isEmpty()) {
            LinkedHashSet<Sequence> sequenceLinkedSet = new LinkedHashSet<>();
            List<Sequence> currentList = new ArrayList<>(map.values());

            for (Sequence currentSeq : currentList) {
                map.remove(currentSeq.getSequence());
                Sequence rightSeq = getSeqFromMap(map, currentSeq.getSequence().substring(1));
                Sequence leftSeq = getSeqFromMap(map, currentSeq.getSequence().substring(0, currentSeq.getLength() - 1));
                if (rightSeq != null || leftSeq != null) {
                    sequenceLinkedSet.add(currentSeq);

                    while (rightSeq != null) {
                        sequenceLinkedSet.add(rightSeq);
                        map.remove(rightSeq.getSequence());
                        rightSeq = getSeqFromMap(map, rightSeq.getSequence().substring(1));
                    }
                    while (leftSeq != null) {
                        LinkedHashSet<Sequence> temp = new LinkedHashSet<>(sequenceLinkedSet);
                        sequenceLinkedSet.clear();
                        sequenceLinkedSet.add(leftSeq);
                        sequenceLinkedSet.addAll(temp);
                        map.remove(leftSeq.getSequence());
                        leftSeq = getSeqFromMap(map, leftSeq.getSequence().substring(0, leftSeq.getLength() - 1));
                    }
                    sequenceArray.add(sequenceLinkedSet);
                    break;
                }
            }
            processInfo.increaseNumberOfRightGenomes();
        }
        return sequenceArray;
    }

    private ArrayList<Sequence> collect(ArrayList<LinkedHashSet<Sequence>> sourceList){
        ArrayList<Sequence> trueSeq=new ArrayList<>();
        for(LinkedHashSet<Sequence> sequenceSet:sourceList){
            boolean first=true;
            Sequence resultSequence = null;
            for(Sequence sequence:sequenceSet){
                if(first){
                    first=false;
                    resultSequence=sequence;
                    continue;
                }
                resultSequence.wideLeftSequence(sequence);
                processInfo.increaseNumberOfCombinedGenomes();
            }
            trueSeq.add(resultSequence);
        }
        return trueSeq;
    }


    private Sequence getSeqFromMap(HashMap<String, Sequence> list, String searchStr) {
        return list.values().stream()
                .filter(sequence ->
                        sequence.getSequence().contains(searchStr)).findFirst().orElse(null);
    }

}