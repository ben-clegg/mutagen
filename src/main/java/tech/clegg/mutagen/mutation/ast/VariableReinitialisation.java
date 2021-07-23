package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import tech.clegg.mutagen.TargetSource;
import tech.clegg.mutagen.properties.MutantFlag;
import tech.clegg.mutagen.properties.MutantType;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class VariableReinitialisation extends ASTVisitorMutationStrategy
{
    public VariableReinitialisation(TargetSource target)
    {
        super(target);
        setType(MutantType.VARIABLE_REINITIALISATION);
        addFlag(MutantFlag.FUNCTIONALITY);
        addFlag(MutantFlag.USES_AST);
        addFlag(MutantFlag.MUTAGEN_UNIQUE);
        addFlag(MutantFlag.MUTAGEN_UNIQUE_FUNCTIONALITY);
    }

    @Override
    protected void visitorSetup()
    {
        visitor = new VoidVisitorAdapter()
        {
            @Override
            public void visit(NameExpr n, Object arg)
            {
                super.visit(n, arg);
                generateMutant(n);
            }


        };
    }

    private void generateMutant(NameExpr varCall)
    {
        // Search for declarator of variable
        AtomicReference<Node> decl = new AtomicReference<>();
        varCall.walk(Node.TreeTraversal.PREORDER, n -> matchingVariableDeclarator(n, decl, varCall));
        if (decl.get() == null)
        {
            // Node immediately found, try again from the CompilationUnit
            getOriginal().getCompilationUnit().walk(Node.TreeTraversal.PREORDER, n -> matchingVariableDeclarator(n, decl, varCall));
        }
        if (decl.get() == null)
            return;
        VariableDeclarator varDecl = (VariableDeclarator) decl.get();

        // Get original initializer value / method / expr
        if (!varDecl.getInitializer().isPresent())
            return; // TODO Support case where later defined
        Expression originalInitializer = varDecl.getInitializer().get();

        // Generate a Node to set the variable to original value
        AssignExpr assignExpr = new AssignExpr(varCall.clone(), originalInitializer, AssignExpr.Operator.ASSIGN);
        ExpressionStmt injectAssignStmt = new ExpressionStmt(assignExpr);


        // Find BlockStmt that contains original call
        BlockStmt originalBlockStmt = getClosestBlockStatementAbove(varCall);
        if (originalBlockStmt == null)
            return;
        BlockStmt modifiedBlockStmt = originalBlockStmt.clone();

        // Inject new assignment into the BlockStmt that contains the original call, immediately before the call
        Optional<Statement> ancestorOfCall = modifiedBlockStmt.getStatements().stream()
                .filter(n -> leadsToSearched(n, varCall))
                .findFirst();
        if (!ancestorOfCall.isPresent())
            return;
        int callIndex = modifiedBlockStmt.getStatements().indexOf(ancestorOfCall.get());
        modifiedBlockStmt.addStatement(callIndex, injectAssignStmt);

        // Check if in same BlockStmt and between modified index & original declaration
        // If so, it may be equivalent, so skip
        Optional<Statement> varDeclAncestor = modifiedBlockStmt.getStatements().stream()
                .filter(n -> leadsToSearched(n, varDecl))
                .findFirst();
        if (varDeclAncestor.isPresent())
        {
            // BlockStmt contains varDecl
            int varDeclIndex = modifiedBlockStmt.getStatements().indexOf(varDeclAncestor.get());
            callIndex = modifiedBlockStmt.getStatements().indexOf(ancestorOfCall.get());
            int injectionIndex = modifiedBlockStmt.getStatements().indexOf(injectAssignStmt);
            if (injectionIndex > varDeclIndex && injectionIndex < callIndex)
                return; // Could be equivalent
        }

        // Generate a mutant
        addMutant(new ASTMutant(
                this.getOriginal().getCompilationUnit(),
                originalBlockStmt,
                modifiedBlockStmt,
                this.getType()
        ));
    }

    private void matchingVariableDeclarator(Node n, AtomicReference<Node> storage, NameExpr targetName)
    {
        // Skip
        if (storage.get() != null)
            return;

        if (n instanceof VariableDeclarator)
        {
            VariableDeclarator result = (VariableDeclarator) n;
            if (result.getName().equals(targetName.getName()))
                storage.set(result);
        }
    }


}
