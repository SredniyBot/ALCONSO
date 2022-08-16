package covid.analise.virusanalisator.repos;

import covid.analise.virusanalisator.entity.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceRepository extends JpaRepository<Sequence,Long> {

    Sequence findBySequence(String seq);

}
