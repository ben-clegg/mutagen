package mutagen.mutation.ast;

import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import mutagen.TargetSource;
import mutagen.mutation.Mutant;
import mutagen.mutation.simple.SimpleMutant;

public class ForSeparatorConfusion extends ASTVisitorMutationStrategy
{
    public ForSeparatorConfusion(TargetSource target)
    {
        super(target);
        setType("ForSeparatorConfusion");
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
