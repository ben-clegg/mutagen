package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.Mutant;
import tech.clegg.mutagen.mutation.simple.SimpleMutant;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

public class ForSeparatorConfusion extends ASTVisitorMutationStrategy
{
    public ForSeparatorConfusion(TargetSource target)
    {
        super(target);
        setType(MutantType.FOR_SEPARATOR_CONFUSION);
        addFlag(MutantFlag.COMPILABILITY);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
    }

    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter()
        {
            @Override
            public void visit(ForStmt declaration, Object arg)
            {
                super.visit(declaration, arg);

                String initLine = declaration.toString().split("\n")[0];

                for (int i = 0; i < getOriginalLines().size(); i++)
                {
                    String l = getOriginalLines().get(i);
                    if (l.trim().equals(initLine))
                    {
                        Mutant m = new SimpleMutant(replaceSeparators(l),
                                                    i,
                                                    type,
                                                    getOriginal());
                        addMutant(m);
                    }
                }
            }

            // TODO also implement "for each" variant
        };
    }

    private String replaceSeparators(String original)
    {
        String l = original.replaceAll(";", ",");
        return l;
    }
}
