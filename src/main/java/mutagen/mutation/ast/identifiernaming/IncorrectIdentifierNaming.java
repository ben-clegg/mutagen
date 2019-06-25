package mutagen.mutation.ast.identifiernaming;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javassist.compiler.ast.Declarator;
import mutagen.TargetSource;
import mutagen.mutation.ast.ASTVisitorMutationStrategy;
import mutagen.mutation.ast.ASTMutant;
import mutagen.mutation.ast.NodePatch;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.List;

public class IncorrectIdentifierNaming extends ASTVisitorMutationStrategy
{
    public IncorrectIdentifierNaming(TargetSource target)
    {
        super(target);
        setType("IncorrectIdentifierNaming");
    }

    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter<Void>()
        {
            // TODO define for other types (e.g. class declaration)
            @Override
            public void visit(VariableDeclarator declaration, Void v)
            {
                super.visit(declaration, v);
                generateNamingMutant(declaration, new Class[]{
                        NameExpr.class
                });
            }

            @Override
            public void visit(MethodDeclaration declaration, Void v)
            {
                super.visit(declaration, v);
                generateNamingMutant(declaration, new Class[]{
                        MethodCallExpr.class
                });
            }
        };
    }

    private void generateNamingMutant(Node declaration, Class[] usageNodeTypes)
    {
        NodeWithSimpleName namedDeclaration = (NodeWithSimpleName) declaration;

        for (String name : nameReplacements(namedDeclaration.getNameAsString()))
        {
            List<NodePatch> nodePatches = new ArrayList<>();

            // Create patch for declarator
            NodeWithSimpleName mutated = (NodeWithSimpleName) declaration.clone();
            mutated.setName(name);
            NodePatch declNodePatch = new NodePatch(declaration, (Node) mutated);
            nodePatches.add(declNodePatch);

            // Add node patches for usages of the identifier
            nodePatches.addAll(generateIdentifierUsagePatches(namedDeclaration.getName(), name, usageNodeTypes));

            // Add full mutant
            ASTMutant m = new ASTMutant(getOriginal().getCompilationUnit(), nodePatches, type);
            m.setPreMutation(namedDeclaration.getNameAsString());
            m.setPostMutation(mutated.getNameAsString());
            addMutant(m);
        }
    }




    private List<String> nameReplacements(String original)
    {
        return NameReformatter.generateMutants(original);
    }
}
