package tech.clegg.mutagen.properties;

public enum MutantFlag
{
    COMPILABILITY,
    FUNCTIONALITY,
    STYLE,
    QUALITY,
    MUTAGEN_UNIQUE, // Unique to MutaGen
    MUTAGEN_UNIQUE_FUNCTIONALITY, // Unique to MutaGen - 2021 updated functionality operators from EndOfYear dataset
    USES_MAJOR,
    USES_AST,
    USES_STRING_MANIPULATION
}
