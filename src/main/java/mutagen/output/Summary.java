package mutagen.output;

import mutagen.mutation.simple.SimpleMutant;

import java.util.List;

public class Summary
{
    private List<SimpleMutant> mutantList;
    private StringBuilder lines;

    public Summary(List<SimpleMutant> mutants)
    {
        // Initialise variables
        lines = new StringBuilder();
        mutantList = mutants;

        // TODO add type
        // CSV Header
        lines.append("ID,MutatedLine,Replacement,Location\n");

        // TODO support mutant types

        // Add each mutant as a line to CSV
        for (SimpleMutant m : mutants)
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
