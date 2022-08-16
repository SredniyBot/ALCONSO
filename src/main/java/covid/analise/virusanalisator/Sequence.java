package covid.analise.virusanalisator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
public class Sequence implements Comparable<Sequence>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
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

    public String getMessage(double max){
        return "{\n\"conservatism\": \"" +
                Math.round((minQuantity/max*10000))/100.0+"%-"+
                Math.round((maxQuantity/max*10000))/100.0+"%\""+
                ",\n \"minQuantity\": \"" +
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


    @Override
    public int compareTo(Sequence o) {
        return Integer.compare(quantity,o.getQuantity());
    }
}
