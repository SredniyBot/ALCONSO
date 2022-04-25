package covid.analise.virusanalisator;

import java.util.Objects;

public class Sequence {

    private String sequence;
    private int quantity;
    private int length;
    private int minQuantity;
    private int maxQuantity;

    public Sequence(String sequence) {
        this.sequence = sequence.toLowerCase();
        length = sequence.length();
        quantity = 1;
        minQuantity=quantity;
        maxQuantity=quantity;
    }

    public synchronized void addSequenceQuantity(int l) {
        quantity += l;
        minQuantity=quantity;
        maxQuantity=quantity;
    }

    public String getSequence() {
        return sequence;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getLength() {
        return length;
    }

    public void wideLeftSequence(Sequence sequence){
        if(sequence.getMinQuantity()<minQuantity){
            minQuantity=sequence.getMinQuantity();
        }
        if(sequence.getMaxQuantity()>maxQuantity){
            maxQuantity=sequence.getMaxQuantity();
        }
        this.sequence+=sequence.sequence.substring(sequence.length-1);
        length++;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    @Override
    public String toString() {
        return
                "{\n\"minQuantity\": \"" +
                minQuantity +"\""+
                ",\n \"maxQuantity\": \"" +
                maxQuantity +"\""+
                ",\n \"length\": \"" +
                length +"\""+
                ",\n \"sequence\": \"" +
                sequence +"\"\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sequence sequence1 = (Sequence) o;
        return sequence.equals(sequence1.sequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequence);
    }

}
