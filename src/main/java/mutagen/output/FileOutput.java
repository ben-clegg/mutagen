package mutagen.output;

import mutagen.mutation.Mutant;
import org.testng.reporters.Files;

import java.io.File;
import java.util.List;

public class FileOutput
{
    private File directory;
    private String filename;

    public FileOutput(String path, String originalFilename)
    {
        directory = new File(path);
        filename = originalFilename;
    }

    public void writeMutants(List<Mutant> mutants)
    {
        for (Mutant m : mutants)
        {
            File f = new File(directory.getPath() +
                    File.pathSeparator +
                    m.getIdString() +
                    File.pathSeparator + filename);

            // TODO write m
        }
    }
}
