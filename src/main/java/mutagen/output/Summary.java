package mutagen.output;

import mutagen.mutation.Mutant;
import mutagen.mutation.simple.SimpleMutant;

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
        lines.append("ID,MutantType,MutatedLine,Change,Location\n");

        // Add each mutant as a line to CSV
        for (Mutant m : mutants)
        {
            if(m.getClass().equals(SimpleMutant.class))
            {
                SimpleMutant sm = (SimpleMutant)m;
                lines.append(sm.getIdString() + "," +
                        sm.getType() + "," +
                        String.format("%04d",sm.getLineNumber()) + ",\"" +
                        sm.getChange() + "\",\"" +
                        sm.getLocation() + "\"\n");
            }
            else
            {
                lines.append(m.getIdString() + "," +
                        m.getType() +
                        ",?," +
                        m.getChange() + "\",\"" +
                        m.getLocation() + "\"\n");
            }

        }
    }

    public String makeCSV()
    {
        return lines.toString();
    }
}
