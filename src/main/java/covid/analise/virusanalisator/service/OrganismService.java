package covid.analise.virusanalisator.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import covid.analise.virusanalisator.entity.Organism;
import covid.analise.virusanalisator.gui.ProcessInfo;
import covid.analise.virusanalisator.logger.Logger;
import covid.analise.virusanalisator.obtaining.VirusJson;
import covid.analise.virusanalisator.repos.OrganismRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.zip.DataFormatException;

@Service
public class OrganismService {

    private final Logger logger;
    private final OrganismRepository organismRepository;

    private final ProcessInfo processInfo;
    private final SequenceService sequenceService;

    public OrganismService(Logger logger, OrganismRepository organismRepository, ProcessInfo processInfo, SequenceService sequenceService) {
        this.logger = logger;
        this.organismRepository = organismRepository;
        this.processInfo = processInfo;
        this.sequenceService = sequenceService;
    }

    public Organism getOrganismFromJson(String json, String path) throws DataFormatException {
        VirusJson virusJson=null;
        try {
            virusJson=new Gson().fromJson(json, (Type) VirusJson.class);
        }catch (JsonSyntaxException e){
            logger.addDataError("Json format error: "+path);
        }
        if(virusJson==null){
            throw new DataFormatException("Data format error (The genome is not taken into account):\n\t "+
                    path+ "\n\t\t"+"Fasta is empty");
        }
        String seq =virusJson.sequence.replaceAll(" ", "").toLowerCase();
        String f = seq.replaceAll("a", "")
                .replaceAll("t", "")
                .replaceAll("g", "")
                .replaceAll("c", "")
                .replaceAll("n", "");

        if (processInfo.isUseNGenomes()&&seq.contains("n")){
            throw new DataFormatException("Genome contains 'N' (The genome is not taken into account):\n\t "+ path);
        }

        if(Strings.isBlank(seq)|| seq.equals("")){
            throw new DataFormatException("Data format error (The genome is not taken into account):\n\t "+
                    path+ "\n\t\t"+"Fasta is empty");
        }

        if ((seq.length()-f.length()<= ProcessInfo.getDefiningLength())) {
            throw new DataFormatException("Data format error (The genome is not taken into account):\n\t "+
                    path+ "\n\t\t"+"Fasta is too small");
        }

        Organism organism=new Organism();
        organism.setDeletedChars(f);
        if (!f.equals("")){
            StringBuilder message= new StringBuilder();
            while (!f.equals("")){
                seq = seq.replaceAll(String.valueOf(f.charAt(0)),"");
                message.append(f.charAt(0));
                f=f.replaceAll(String.valueOf(f.charAt(0)),"");
            }
            logger.addDataWarning("Data format warning \n\t "+path+ "\n\t\t"+
                    "The following characters have been replaced by n: "+message);
        }
        organism.setSequence(seq.toLowerCase());
        organism.setDataset(virusJson.dataset);
        return organism;
    }

    public void addOrganism(Organism organism) throws IOException {
        if(organismRepository.findBySequence(organism.getSequence())!=null)
            throw new OrganismAlreadyExistsException("The organism: " + organism.getDataset() + " has already been injected");
        organism.setSequencesIds(sequenceService.sliceAndSave(organism.getSequence()));
        organismRepository.save(organism);
    }

    public static class OrganismAlreadyExistsException extends IOException{
        public OrganismAlreadyExistsException(String message) {
            super(message);
        }
    }

}
