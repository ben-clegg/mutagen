package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.identifiernaming.NameReformatter;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.ArrayList;
import java.util.List;

public class ClassnameReplacement extends ASTVisitorMutationStrategy
{
    public ClassnameReplacement(TargetSource target)
    {
        super(target);
        setType(MutantType.CLASSNAME_REPLACEMENT);
        addFlag(MutantFlag.COMPILABILITY);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
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
                    ASTMutant m = new ASTMutant(getOriginal().getCompilationUnit(), declaration, mutated, type);
                    m.setPreMutation(declaration.getNameAsString());
                    m.setPostMutation(mutated.getNameAsString());
                    addMutant(m);
                }
            }
        };
    }

    private List<String> classnameReplacements(String original)
    {
        List<String> replacements = new ArrayList<String>();

        // Lowercase
        replacements.add(original.toLowerCase());

        // Character removal
        for (int i = 0; i < original.length(); i++)
        {
            replacements.add(original.substring(0, i) + original.substring(i+1));
        }

        // Name reformatting
        // TODO move to incorrect identifier naming?
        replacements.addAll(NameReformatter.generateMutants(original));

        // TODO other strategies
        // Character swapping

        return replacements;
    }
}
