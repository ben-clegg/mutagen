package mutagen.mutation.ast;

import com.github.javaparser.ast.Node;

public class NodeMutant
{
    private Node original;
    private Node mutated;

    public NodeMutant(Node originalNode, Node mutatedNode)
    {
        original = originalNode;
        mutated = mutatedNode;
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
