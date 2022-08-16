package covid.analise.virusanalisator.entity;

import covid.analise.virusanalisator.Sequence;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Organism {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sequence;

    private String dataset;

    private String deletedChars;

    @ElementCollection
    private List<Long> sequencesIds;

}
