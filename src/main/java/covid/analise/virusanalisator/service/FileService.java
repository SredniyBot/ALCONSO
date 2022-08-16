package covid.analise.virusanalisator.service;

import covid.analise.virusanalisator.entity.Organism;
import covid.analise.virusanalisator.logger.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.DataFormatException;

@Service
public class FileService {

    private final Logger logger;

    private final OrganismService organismService;

    public FileService(Logger logger, OrganismService organismService) {
        this.logger = logger;
        this.organismService = organismService;
    }

    @Async
    public void pushFileToDB(File source)  {
        try {
            Organism organism=organismService.getOrganismFromJson(getFileContent(source),source.getAbsolutePath());
            organismService.addOrganism(organism);
        } catch (DataFormatException e) {
            logger.addDataError(e.getMessage());
        } catch(OrganismService.OrganismAlreadyExistsException e) {
            logger.addDataError(e.getMessage()+" File "+source.getAbsolutePath()+" is ignored");
        } catch(IOException e) {
            logger.addDataError("Could not read the file: "+source.getAbsolutePath());
        }
    }
    private String getFileContent(File source) throws IOException {
        return Files.readString(source.toPath());
    }


}
