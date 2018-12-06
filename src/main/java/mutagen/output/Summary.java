package mutagen.output;

import mutagen.mutation.Mutant;
import mutagen.mutation.ast.NodeMutant;
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

        // TODO add type
        // CSV Header
        lines.append("ID,MutatedLine,Replacement,Location\n");

        // TODO support mutant types

        // Add each mutant as a line to CSV
        for (Mutant m : mutants)
        {
            if(m.getClass().equals(SimpleMutant.class))
            {
                SimpleMutant sm = (SimpleMutant)m;
                lines.append(sm.getIdString() + "," +
                        String.format("%04d",sm.getLineNumber()) + ",\"" +
                        sm.getReplacement() + "\",\"" +
                        sm.getLocation() + "\"\n");
            }
            else if (m.getClass().equals(NodeMutant.class))
            {
                NodeMutant nm = (NodeMutant) m;
                lines.append(nm.getIdString() + ",?," +
                        nm.getChange() + "\",\"" +
                        nm.getLocation() + "\"\n");
            }

        }
    }

    public String makeCSV()
    {
        return lines.toString();
    }
}
