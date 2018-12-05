package mutagen.mutation.ast;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import mutagen.TargetSource;

import java.util.ArrayList;
import java.util.List;

public class ClassnameReplacementStrategy extends ASTMutationStrategy
{
    public ClassnameReplacementStrategy(TargetSource target)
    {
        super(target);
        setType("ClassnameReplacement");
        setType("ClassnameReplacement");
    }

    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter<Void>()
        {
            @Override
            public void visit(ClassOrInterfaceDeclaration declaration, Void v)
            {
                super.visit(declaration, v);
                //System.out.println("Classname: " + declaration.getName());

                for (String name : classnameReplacements(declaration.getNameAsString()))
                {
                    ClassOrInterfaceDeclaration mutated = declaration.clone();
                    mutated.setName(name);
                    addMutant(new NodeMutant(getOriginal().getCompilationUnit(), declaration, mutated, type));
                }
            }
        };
    }

    private List<String> classnameReplacements(String original)
    {
        List<String> replacements = new ArrayList<String>();

        // Lowercase
        replacements.add(original.toLowerCase());

        // TODO other strategies

        // Character removal

        // Character swapping

        return replacements;
    }
}
