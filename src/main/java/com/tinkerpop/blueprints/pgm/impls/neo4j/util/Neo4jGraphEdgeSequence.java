package com.tinkerpop.blueprints.pgm.impls.neo4j.util;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jEdge;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class Neo4jGraphEdgeSequence implements Iterator<Edge>, Iterable<Edge> {

    private Neo4jGraph graph;
    private Iterator<Node> nodes;
    private Iterator<Relationship> currentRelationships;
    private boolean complete = false;

    public Neo4jGraphEdgeSequence(final Iterable<Node> nodes, final Neo4jGraph graph) {
        this.graph = graph;
        this.nodes = nodes.iterator();
        this.complete = this.goToNextEdge();

    }

    public Edge next() {
        Edge edge = new Neo4jEdge(currentRelationships.next(), this.graph);
        this.complete = this.goToNextEdge();
        return edge;
    }

    public boolean hasNext() {
        return !complete;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private boolean goToNextEdge() {
        if (this.currentRelationships == null || !this.currentRelationships.hasNext()) {
            if (nodes.hasNext()) {
                this.currentRelationships = nodes.next().getRelationships(Direction.OUTGOING).iterator();
            } else {
                return true;
            }
        }

        if (this.currentRelationships.hasNext()) {
            return false;
        } else {
            return this.goToNextEdge();
        }
    }

    public Iterator<Edge> iterator() {
        return this;
    }
}
