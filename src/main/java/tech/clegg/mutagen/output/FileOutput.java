package tech.clegg.mutagen.output;

import tech.clegg.mutagen.mutation.Mutant;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileOutput
{
    private File directory;
    private String relativeFilePath;

    public FileOutput(String path, String originalFilename)
    {
        directory = new File(path);
        relativeFilePath = originalFilename;
    }

    public void setMutantLocation(Mutant m)
    {
        m.setLocation(new File(directory.getPath() +
                File.separatorChar +
                m.getIdString() + "_" + m.getType().toString().trim()), relativeFilePath);
    }

    public void writeMutant(Mutant m)
    {
        // Set mutant's location to a subdirectory of its ID.
        setMutantLocation(m);

        // Write the mutant to a file
        //System.out.println(m.getFullyQualifiedFile());
        m.getLocation().getParentFile().mkdirs();
        try
        {
            FileUtils.writeStringToFile(m.getLocation(), m.getModifiedLines().toString(), "UTF-8");
        }
        catch (IOException ioEx)
        {
            ioEx.printStackTrace();
            System.err.println("Failed to save mutant to " +
                    m.getLocation());
        }
    }

    public void writeMutants(List<Mutant> mutants)
    {
        System.out.println("Writing mutants to " + directory.getPath() + "...");
        for (Mutant m : mutants)
        {
            writeMutant(m);
        }
        System.out.println("Mutants written!");
    }

    public void writeSummary(List<Mutant> mutants)
    {
        Summary summary = new Summary(mutants);
        File csvLocation = new File(directory.getPath() + File.separatorChar +
                                    "mutantIndex.csv");
        try
        {
            FileUtils.writeStringToFile(csvLocation, summary.makeCSV(), "UTF-8");
        }
        catch (IOException ioEx)
        {
            ioEx.printStackTrace();
            System.err.println("Failed to write mutant index CSV to " +
                                csvLocation);
        }
    }
}
