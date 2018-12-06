package mutagen.mutation.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import mutagen.JavaSource;
import mutagen.mutation.Mutant;

public class NodeMutant extends Mutant
{
    private Node original;
    private Node mutated;
    private CompilationUnit originalCU;

    private String preMutation = "";
    private String postMutation = "";

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

    public void setPreMutation(String preMutation)
    {
        this.preMutation = preMutation;
    }

    public void setPostMutation(String postMutation)
    {
        this.postMutation = postMutation;
    }

    public String getChange()
    {
        if(postMutation.equals(""))
        {
            return "";
        }
        else
        {
            if(preMutation.equals(""))
            {
                return postMutation;
            }
            else
            {
                return preMutation + " -> " + postMutation;
            }
        }
    }

    @Override
    protected void setupMutatedJavaSource()
    {
        // TODO may need to modify to consider compilation unit
        modified = new JavaSource(mutated.toString());
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
