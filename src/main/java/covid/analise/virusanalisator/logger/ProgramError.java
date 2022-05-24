package covid.analise.virusanalisator.logger;

public class ProgramError {

    public static String getProgramErrorById(int id){
        switch (id){
            case 0 -> {
                return "Directory creation error";
            }
            case 1 -> {
                return "1";
            }
            case 2 -> {
                return "Waiting limit exceeded";
            }
            case 3 -> {
                return "Chosen file is not directory";
            }
            case 4 -> {
                return "No json file in directory";
            }
            case 5 -> {
                return "Error writing log file";
            }
            case 6 -> {
                return "Source folder doesnt exists";
            }
            default->{
                return "Unknown error";
            }
        }
    }
}
