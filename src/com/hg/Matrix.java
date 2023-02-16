package com.hg;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Matrix extends XMatrix<Node> {
    PictureFuzzySet PFS = new PictureFuzzySet();
    private AStarCostEvaluator evaluator = new AStarCostEvaluator();

    private Node start;
    private Node end;

    @Override
    public void setDimension(int rows, int cols) {
        super.setDimension(rows, cols);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node node = new Node();
                node.setRow(row);
                node.setCol(col);
                setValue(row, col, node);

            }
        }
    }

    public void buildGraph() {
        int rows = getRows();
        int cols = getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                buildEdges(row, col);
//                build4Edges(row, col);
            }
        }
    }
    public void buildGraphWithValue() {
        int rows = getRows();
        int cols = getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
               // buildEdges(row, col);
                build4Edges(row, col);
            }
        }
    }

    public void reset() {
        int rows = getRows();
        int cols = getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node node = getValue(row, col);
                node.reset();
            }
        }
    }

    public void clean() {
        int rows = getRows();
        int cols = getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node node = getValue(row, col);
                node.clean();
            }
        }
    }

    public int getObstacles() {
        int count = 0;
        int rows = getRows();
        int cols = getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node node = getValue(row, col);
                if (node.getValue() == 2) {
                    count++;
                }
            }
        }
        return count;
    }

    public void evaluateHeuristic() {

        int rows = getRows();
        int cols = getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node node = getValue(row, col);
                int heuristic = evaluator.evaluateHeuristic(node, start, end);
                node.setHeuristic(heuristic);
            }
        }
    }

    private void buildEdges(int row, int col) {
        Node node = getValue(row, col);
        node.getEdges().clear();
        if (!node.isEnabled()) {
            return;
        }
        // check 8 neighors
        int rows = getRows();
        int cols = getColumns();
        for (int i = -1; i <= 1; i++) {
            int y = row + i;
            if (y < 0 || y >= rows) {
                continue;
            }
            for (int j = -1; j <= 1; j++) {
                int x = col + j;
                // outside the matrix
                if (x < 0 || x >= cols) {
                    continue;
                }
                // it is the node itself
                if (y == row && x == col) {
                    continue;
                }
                // do not do cross cut
                if (i == -1 && j == -1) {
                    Node neighbor1 = getValue(row, col - 1);
                    Node neighbor2 = getValue(row - 1, col);
                    if (!neighbor1.isEnabled() && !neighbor2.isEnabled()) {
                        continue;
                    }
                }
                if (i == -1 && j == 1) {
                    Node neighbor1 = getValue(row, col + 1);
                    Node neighbor2 = getValue(row - 1, col);
                    if (!neighbor1.isEnabled() && !neighbor2.isEnabled()) {
                        continue;
                    }
                }
                if (i == 1 && j == -1) {
                    Node neighbor1 = getValue(row, col - 1);
                    Node neighbor2 = getValue(row + 1, col);
                    if (!neighbor1.isEnabled() && !neighbor2.isEnabled()) {
                        continue;
                    }
                }
                if (i == 1 && j == 1) {
                    Node neighbor1 = getValue(row, col + 1);
                    Node neighbor2 = getValue(row + 1, col);
                    if (!neighbor1.isEnabled() && !neighbor2.isEnabled()) {
                        continue;
                    }
                }
                Node neighbor = getValue(y, x);
                // the disabled node
                if (!neighbor.isEnabled()) {
                    continue;
                }
                Edge edge = new Edge();
                edge.setNodeA(node);
                edge.setNodeB(neighbor);
                node.addEdge(edge);
                edge.setWeight(evaluator.evaluateWeight(edge));
                //edge.setWeight((int) PFS.FuzzyLogic());
            }
        }
    }

    private void setEdge(Node node, Node neighbor) {
        if (node == null || neighbor == null || node.getValue() != 1 || neighbor.getValue() != 1) {
            return;
        }
        Edge edge = new Edge();
        edge.setNodeA(node);
        edge.setNodeB(neighbor);
        node.addEdge(edge);
        //edge.setWeight(evaluator.evaluateWeight(edge));
        edge.setWeight((int) PFS.FuzzyLogic());
    }

    private void build4Edges(int row, int col) {
        Node node = getValue(row, col);
        node.getEdges().clear();
        setEdge(node, moveUp(node));
        setEdge(node, moveDown(node));
        setEdge(node, turnLeft(node));
        setEdge(node, turnRight(node));

    }

    public AStarCostEvaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(AStarCostEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public Node turnLeft(Node node) {
        if (node == null) {
            return null;
        }
        return getValue(node.getRow(), node.getCol() - 1);
    }

    public Node turnRight(Node node) {
        if (node == null) {
            return null;
        }
        return getValue(node.getRow(), node.getCol() + 1);
    }

    public Node moveUp(Node node) {
        if (node == null) {
            return null;
        }
        return getValue(node.getRow() - 1, node.getCol());
    }

    public Node moveDown(Node node) {
        if (node == null) {
            return null;
        }
        return getValue(node.getRow() + 1, node.getCol());
    }

}
