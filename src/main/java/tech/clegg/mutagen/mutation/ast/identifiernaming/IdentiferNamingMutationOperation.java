package tech.clegg.mutagen.mutation.ast.identifiernaming;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.mutation.ast.ASTMutant;
import tech.clegg.mutagen.mutation.ast.ASTVisitorMutationStrategy;
import tech.clegg.mutagen.mutation.ast.NodePatch;

import java.util.ArrayList;
import java.util.List;

public abstract class IdentiferNamingMutationOperation extends ASTVisitorMutationStrategy
{
    public IdentiferNamingMutationOperation(TargetSource target)
    {
        super(target);
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
                generateNamingMutants(declaration, new Class[]{
                        NameExpr.class
                });
            }

            @Override
            public void visit(MethodDeclaration declaration, Void v)
            {
                super.visit(declaration, v);
                generateNamingMutants(declaration, new Class[]{
                        MethodCallExpr.class
                });
            }
        };
    }

    protected void generateNamingMutants(Node declaration, Class[] usageNodeTypes)
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

    abstract List<String> nameReplacements(String original);
}
