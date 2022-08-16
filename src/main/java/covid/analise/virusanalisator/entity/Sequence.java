package covid.analise.virusanalisator.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Sequence implements Comparable<Sequence>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String sequence;
    private int quantity;

    public Sequence(String sequence) {
        this.sequence = sequence.toLowerCase();
        quantity = 1;
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
