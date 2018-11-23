package mutagen.output;

import mutagen.mutation.Mutant;

import java.util.ArrayList;
import java.util.List;

public class Summary
{
    private List<Mutant> mutantList;
    private StringBuilder lines;

    public Summary(List<Mutant> mutants)
    {
        // Initialise variables
        lines = new StringBuilder();
        mutantList = mutants;

        // CSV Header
        lines.append("ID,MutatedLine,Replacement,Location\n");

        // Add each mutant as a line to CSV
        for (Mutant m : mutants)
        {
            lines.append(m.getIdString() + "," +
                        String.format("%04d",m.getLineNumber()) + ",\"" +
                        m.getReplacement() + "\",\"" +
                        m.getLocation() + "\"\n");
        }
    }

    public String makeCSV()
    {
        return lines.toString();
    }
}
