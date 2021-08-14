package tech.clegg.mutagen.properties;

import java.util.ArrayList;
import java.util.List;

// TODO likely a better means than enums, e.g. reflection
public enum MutantType
{
    NONE(0),
    STUDENT(1),
    UNKNOWN_MUTANT(2),
    EQUALITY_CONFUSION(3),
    COMPARATOR_CONFUSION(4),
    SHORT_CIRCUIT_CONFUSION(5),
    FOR_SEPARATOR_CONFUSION(6),
    UNBALANCED_BRACKETS(7),
    CONSTANT_TO_VARIABLE(8),
    CLASSNAME_REPLACEMENT(9),
    INCORRECT_CALCULATION(10),
    INCORRECT_VALUES(11),
    INCOMPLETE_IMPLEMENTATION(12),
    POOR_INDENTATION(13),
    INCORRECT_IDENTIFIER_STYLE(14),
    POOR_IDENTIFIER_NAMING(15),
    LITERAL_REPETITION(16),
    LOGICAL_OPERATOR_REPLACEMENT(17),
    CONDITIONAL_OPERATOR_REPLACEMENT(18),
    RELATIONAL_OPERATOR_REPLACEMENT(19),
    SHIFT_OPERATOR_REPLACEMENT(20),
    STRING_MISSPELLING(21),
    BRANCH_EXTRACTION(22),
    INCORRECT_INPUT_VALIDATION(23),
    MULTIPLE_STATEMENT_DELETION(24),
    REMOVE_PUBLIC_ACCESS_MODIFIER(25),
    STATIC_MODIFIER_INTRODUCTION(26),
    BREAK_CONTINUE_DELETION(27),
    NEW_PARAMETER_CREATION(28),
    TARGETED_STATEMENT_DELETION_THROWS(29),
    TRY_STATEMENT_EXTRACTION(30),
    REMOVE_VARIABLE_INITIAL_VALUE(31),
    REMOVE_CASTS(32),
    STATEMENT_CREATION_ITERATOR_NEXT(33),
    TARGETED_EXTRACTION_BREAK_CONTINUE(34),
    STATEMENT_TRANSPOSE(35),
    ELSE_RELOCATION(36),
    BRANCH_NESTING(37),
    TARGETED_NESTING_BREAK_CONTINUE(38),
    VARIABLE_REINITIALISATION(39),
    COLLECTION_REMOVAL_DELETION(40),
    PARAMETER_REASSIGNMENT_DELETION(41),
    TRANSFORM_COPY_TO_REFERENCE(42),
    PARTIAL_IF_ELSE_BLOCK_SWITCH(43)
    ;

    MutantType(int index) {
        mutantIndex = index;
    }

    private int mutantIndex;

    public static List<String> nameList()
    {
        List<String> names = new ArrayList<>();
        for (MutantType t : MutantType.values())
        {
            names.add(t.name());
        }
        return names;
    }

    public static List<MutantType> listKnownMutants()
    {
        List<MutantType> mutantTypes = new ArrayList<>();
        for (MutantType t : MutantType.values())
        {
            switch (t)
            {
                case NONE: case STUDENT: case UNKNOWN_MUTANT:
                break;
                default:
                    mutantTypes.add(t);
                    break;
            }
        }
        return mutantTypes;
    }

    public static List<String> nameListKnownMutants()
    {
        List<String> names = new ArrayList<>();
        for (MutantType t : MutantType.listKnownMutants())
        {
            names.add(t.name());
        }
        return names;
    }

    public static List<Integer> indexListKnownMutants()
    {
        List<Integer> indexes = new ArrayList<>();
        for (MutantType t : MutantType.listKnownMutants())
        {
            indexes.add(t.mutantIndex);
        }
        return indexes;
    }
}
