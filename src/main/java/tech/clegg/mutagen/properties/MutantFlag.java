package tech.clegg.mutagen.properties;

public enum MutantFlag
{
    COMPILABILITY,
    FUNCTIONALITY,
    STYLE,
    QUALITY,
    MUTAGEN_UNIQUE, // Unique to MutaGen
    MUTAGEN_UNIQUE_FUNCTIONALITY, // Unique to MutaGen - 2021-07 updated functionality operators from EndOfYear dataset
    MUTAGEN_THESIS_FINAL, // Unique to MutaGen - only operators that directly match to observed students' faults
                            // that are unique to MutaGen
    USES_MAJOR,
    USES_AST,
    USES_STRING_MANIPULATION
}
