package com.hg;

import java.util.ArrayList;
import java.util.Collection;

public class Node {

    private boolean open = true;
    private boolean visited = false;
    private boolean selected = false;
    private boolean enabled = true;
    private final Collection<Edge> edges = new ArrayList<Edge>();
    private Node predecessor;
    private int cost = 0;
    private int heuristic = 0;
    private int value = 0; // value of node in uapp
    private int col;
    private int row;
    
    
    int f;
    int g;
    int h;
    Node parent;
    int c;

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * reset to the original values.
     */
    public void clean() {
        predecessor = null;
        cost = 0;
        open = true;
        visited = false;
        selected = false;
        
    }
    public void reset() {
        predecessor = null;
        cost = 0;
        open = true;
        visited = false;
        selected = false;
        if (this.value != 2) {
            this.value = 0;
        }
    }
    

    /**
     * {@inheritDoc}
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * {@inheritDoc}
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * {@inheritDoc}
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * {@inheritDoc}
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean addEdge(Edge edge) {
        return this.edges.add(edge);
    }

    public boolean removeEdge(Edge edge) {
        return this.edges.remove(edge);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Edge> getEdges() {
        return this.edges;
    }

    /**
     * {@inheritDoc}
     */
    public Node getPredecessor() {
        return predecessor;
    }

    /**
     * {@inheritDoc}
     */
    public void setPredecessor(Node node) {
        this.predecessor = node;
    }

    public int getCost() {
        return this.cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getHeuristic() {
        return heuristic;
    }

    /**
     * {@inheritDoc}
     */
    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * {@inheritDoc}
     */
    public String toStringValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(heuristic + cost);
        sb.append("(");
        sb.append(cost);
        sb.append(",");
        sb.append(heuristic);
        sb.append(")");;
        return sb.toString();
    }
    public String toString() {
        UAPP u = new UAPP();
        return u.getCoor(this);
    }
}
