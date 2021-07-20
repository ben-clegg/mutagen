package tech.clegg.mutagen.conf;

import java.io.File;

public class Paths
{

    public static char SEP = File.separatorChar;
    private static String majorLocation = SEP + "usr" +
                                            SEP + "share" +
                                            SEP + "major" +
                                            SEP + "bin" +
                                            SEP + "javac";

    private static final String TOOLNAME = "MutaGen";

    // Experiment root
    public static File PARENT_DIR = new File(SEP + "var" + SEP + "tmp");
    public static File WORKING_DIR = new File(PARENT_DIR.getPath() + SEP + TOOLNAME);

    public static String getMajorLocation()
    {
        return majorLocation;
    }
}
