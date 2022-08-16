package covid.analise.virusanalisator.repos;

import covid.analise.virusanalisator.entity.Organism;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganismRepository extends JpaRepository<Organism,Long> {
    Organism findBySequence(String sequence);
}
