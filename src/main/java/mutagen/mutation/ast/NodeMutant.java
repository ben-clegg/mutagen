package mutagen.mutation.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import mutagen.JavaSource;
import mutagen.mutation.Mutant;

public class NodeMutant extends Mutant
{
    private Node original;
    private Node mutated;
    private CompilationUnit originalCU;

    public NodeMutant(CompilationUnit originalCompilationUnit,
                      Node originalNode,
                      Node mutatedNode,
                      String mutantType)
    {
        super(mutantType);
        original = originalNode;
        mutated = mutatedNode;
        originalCU = originalCompilationUnit;
        setupMutatedJavaSource();
    }

    @Override
    protected void setupMutatedJavaSource()
    {
        CompilationUnit modifiedCU = originalCU.clone();
        modifiedCU.findAll(VariableDeclarator.class).stream()
                .filter(f -> f.equals(original))
                .forEach(c -> c.replace(mutated));
        modified = new JavaSource(modifiedCU.toString());
    }

    @Override
    public String toString()
    {
        return  getType() +
                "[" + getIdString() + "] (AST Mutant): " +
                getChange();
    }

    public Node getOriginal()
    {
        return original;
    }

    public Node getMutated()
    {
        return mutated;
    }
}
