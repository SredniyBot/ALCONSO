package covid.analise.virusanalisator.obtaining;

public class VirusPrototype {

    private String name;
    private String fasta;

    public boolean isNNN(){
        return fasta.contains("n");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFasta() {
        return fasta;
    }

    public void setFasta(String fasta) {
        this.fasta = fasta;
    }

}
