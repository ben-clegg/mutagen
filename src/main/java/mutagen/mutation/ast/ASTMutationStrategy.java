package mutagen.mutation.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import mutagen.TargetSource;

import java.util.ArrayList;
import java.util.List;

public class ASTMutationStrategy
{
    private TargetSource original;
    protected VoidVisitorAdapter visitor;
    private List<NodeMutant> nodeMutants;

    public ASTMutationStrategy(TargetSource target)
    {
        original = target;
        visitorSetup();
        nodeMutants = new ArrayList<NodeMutant>();
    }

    public void addNodeMutant(NodeMutant m)
    {
        nodeMutants.add(m);
    }

    public void run()
    {
        visitor.visit(original.getCompilationUnit(), null);
        System.out.println(nodeMutants);
    }

    protected void visitorSetup()
    {
        // Override
    }
}
