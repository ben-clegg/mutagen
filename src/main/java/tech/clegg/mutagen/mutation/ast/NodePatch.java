package tech.clegg.mutagen.mutation.ast;

import com.github.javaparser.ast.Node;

import java.util.Optional;

public class NodePatch
{
    private Node original;
    private Node mutated;

    public NodePatch(Node originalNode, Node mutatedNode)
    {
        original = originalNode;
        mutated = mutatedNode;
    }

    public Class<? extends Node> getNodeType()
    {
        return original.getClass();

    }

    public Optional<Node> getParent()
    {
        return original.getParentNode();
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
