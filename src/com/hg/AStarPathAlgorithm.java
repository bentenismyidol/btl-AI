package com.hg;

import static com.hg.UAPP.cost;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An implementation of A* path search algorithm.
 *
 * @author Kevin Wang
 *
 */
public class AStarPathAlgorithm {
    PictureFuzzySet PFS = new PictureFuzzySet();
    private AStarCostEvaluator evaluator;
    private Matrix matrix;

    private final BinaryHeap<Node> binaryHeap = new BinaryHeap<Node>(new Comparator<Node>() {

        public int compare(Node o1, Node o2) {
            return (o1.getCost() + o1.getHeuristic() - (o2.getCost() + o2.getHeuristic()));
        }
    });

    /**
     * {@inheritDoc}
     */
    public boolean searchPath(Node start, Node end) {
        try {
            end.setValue(1);
            matrix.buildGraphWithValue();
            matrix.clean();
            return search(start, end);
        } finally {
            binaryHeap.clear();
        }
    }

    /**
     * @param start
     * @param end
     * @return true if a path is found, false otherwise
     */
    private boolean search(Node start, Node end) {
        Node node = start;
        node.setVisited(true);
        node.setCost(0);

        
        binaryHeap.add(node);
        while (binaryHeap.size() > 0) {
            // take the node with the lowest cost, and check the nodes that are connected to
            // it.
            node = binaryHeap.remove();
            // the node has the lowest cost, close it so it won't be visited again.
            // even though a node has the lowest cost, it still can be not in the selected
            // path
            // in the end of the path search:
            // - if it has no connection to the destination, or its connection has a higher
            // cost so lost the battle in the competition, then it won't be in the selected
            // path
            // - if it remains as a link on the chain connecting the origin and the
            // destination,
            // then it will be in the selected path
            node.setOpen(false);
            Collection<Edge> edges = node.getEdges();
            // check the nodes connected by the edges
            for (Edge edge : edges) {
                Node candidate = (Node) edge.getOpposite(node);
                if (!candidate.isOpen()) {
                    continue;
                }
                int cost = evaluator.evaluateCost(candidate, edge, start, end);
                // if the node has never been visited since the search begins, add it
                // into the binary heap
                if (!candidate.isVisited()) {
                    candidate.setVisited(true);
                    candidate.setCost(cost);
                    candidate.setPredecessor(node);
                    binaryHeap.add(candidate);
                } // if the candidate node has been visited before, i.e. it can be reached
                // from the origin through another node, which is currently the predecessor
                // of the candidate node, but the cost is higher in comparison with that
                // if it takes the path through the "node" that is currently being tested.
                // so update the predecessor and cost.
                else if (cost < candidate.getCost()) {
                    candidate.setCost(cost);
                    candidate.setPredecessor(node);
                    // the removing and adding of the candidate is simply to put the candidate
                    // at a new place as its cost has changed.
                    binaryHeap.remove(candidate);
                    binaryHeap.add(candidate);
                }
            }
            // reached the destination, mark the path and stop
            if (binaryHeap.contains(end)) {
                end.setValue(0);
                //markPath(start, end);
                return true;
            }
        }
        end.setValue(0);
        return false;
    }

    private boolean searchWithValue(Node start, Node end) {

        Node node = start;
        node.setVisited(true);
        node.setCost(0);
        binaryHeap.add(node);
        UAPP uapp = new UAPP();
        while (binaryHeap.size() > 0) {
            // take the node with the lowest cost, and check the nodes that are connected to
            // it.
            node = binaryHeap.remove();
            // the node has the lowest cost, close it so it won't be visited again.
            // even though a node has the lowest cost, it still can be not in the selected
            // path
            // in the end of the path search:
            // - if it has no connection to the destination, or its connection has a higher
            // cost so lost the battle in the competition, then it won't be in the selected
            // path
            // - if it remains as a link on the chain connecting the origin and the
            // destination,
            // then it will be in the selected path
            node.setOpen(false);
            Collection<Edge> edges = node.getEdges();
            // check the nodes connected by the edges
            for (Edge edge : edges) {
                Node candidate = (Node) edge.getOpposite(node);
                if (!candidate.isOpen()) {
                    continue;
                }
                if (candidate != end) {
                    if (candidate.getValue() != 1) {
                        continue;
                    }
                }
                int cost = evaluator.evaluateCost(candidate, edge, start, end);
                // if the node has never been visited since the search begins, add it
                // into the binary heap
                if (!candidate.isVisited()) {
                    //candidate.setVisited(true);
                    candidate.setCost(cost);
                    candidate.setPredecessor(node);
                    binaryHeap.add(candidate);
                } // if the candidate node has been visited before, i.e. it can be reached
                // from the origin through another node, which is currently the predecessor
                // of the candidate node, but the cost is higher in comparison with that
                // if it takes the path through the "node" that is currently being tested.
                // so update the predecessor and cost.
                else if (cost < candidate.getCost()) {
                    candidate.setCost(cost);
                    candidate.setPredecessor(node);
                    // the removing and adding of the candidate is simply to put the candidate
                    // at a new place as its cost has changed.
                    binaryHeap.remove(candidate);
                    binaryHeap.add(candidate);
                }
            }
            // reached the destination, mark the path and stop
            if (binaryHeap.contains(end)) {
                //markPath(start, end);
                printTrace(start, end);
                return true;
            }
        }
        return false;
    }

    private void print() {
        int rows = matrix.getRows();
        int cols = matrix.getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node node = matrix.getValue(row, col);
                System.out.print(" " + (node.getValue() == 1 ? 1 : 0 ));
            }
            System.out.println("");
        }
    }

    private boolean searchWithValue111(Node start, Node end) {
        final Matrix tmp = matrix;
        rebuildMatrix(end);
        Node node = start;
        node.setVisited(true);
        node.setCost(0);
        binaryHeap.add(node);
        UAPP uapp = new UAPP();
        while (binaryHeap.size() > 0) {
            // take the node with the lowest cost, and check the nodes that are connected to
            // it.
            node = binaryHeap.remove();
            // the node has the lowest cost, close it so it won't be visited again.
            // even though a node has the lowest cost, it still can be not in the selected
            // path
            // in the end of the path search:
            // - if it has no connection to the destination, or its connection has a higher
            // cost so lost the battle in the competition, then it won't be in the selected
            // path
            // - if it remains as a link on the chain connecting the origin and the
            // destination,
            // then it will be in the selected path
            node.setOpen(false);
            Collection<Edge> edges = node.getEdges();
            // check the nodes connected by the edges
            for (Edge edge : edges) {
                Node candidate = (Node) edge.getOpposite(node);
                if (!candidate.isOpen()) {
                    continue;
                }
                int cost = evaluator.evaluateCost(candidate, edge, start, end);
                // if the node has never been visited since the search begins, add it
                // into the binary heap
                if (!candidate.isVisited()) {
                    //candidate.setVisited(true);
                    candidate.setCost(cost);
                    candidate.setPredecessor(node);
                    binaryHeap.add(candidate);
                } // if the candidate node has been visited before, i.e. it can be reached
                // from the origin through another node, which is currently the predecessor
                // of the candidate node, but the cost is higher in comparison with that
                // if it takes the path through the "node" that is currently being tested.
                // so update the predecessor and cost.
                else if (cost < candidate.getCost()) {
                    candidate.setCost(cost);
                    candidate.setPredecessor(node);
                    // the removing and adding of the candidate is simply to put the candidate
                    // at a new place as its cost has changed.
                    binaryHeap.remove(candidate);
                    binaryHeap.add(candidate);
                }
            }
            // reached the destination, mark the path and stop
            if (binaryHeap.contains(end)) {
                //markPath(start, end);
                printTrace(start, end);
                matrix.clean();
                return true;
            }
        }
        matrix = tmp;
        return false;
    }

    public List<Node> findNearestNode(List<Node> list, Node node, Matrix matrix) {
        List<Node> tmpList = new ArrayList();
        Collections.reverse(list);
        int cost = 1000000;
        for (Node end : list) {
            if (!searchPath(node, end)) {
                continue;
            }
            if (end.getCost() < cost && end.getCost() > 0) {
                cost = end.getCost();
                tmpList = printTrace(node, end);
            }
            matrix.clean();
        };
        return tmpList;
    }

    public List<Node> printTrace(Node start, Node end) {
        List<Node> list = new ArrayList<>();
        if (start == null || end == null) {
            return list;
        }
        Node node = end;
        while (node != start && node != null) {
            list.add(node);
            node = node.getPredecessor();
        }
        Collections.reverse(list);
        return list;
    }

    /**
     * Mark all the nodes that form the path as selected, from the last node
     * backwards through the predecessors, to the origin.
     *
     * @param start
     * @param end
     */
    private void markPath(Node start, Node end) {
        Node node = end;
        while (node != start) {
            node.setSelected(true);
            node = node.getPredecessor();
        }
        node.setSelected(true);
    }

    public AStarCostEvaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(AStarCostEvaluator evaluator, Matrix matrix) {
        this.evaluator = evaluator;
        this.matrix = matrix;
    }

    public boolean searchPathWithValue(Node start, Node end) {
        try {
            return searchWithValue(start, end);
        } finally {
            binaryHeap.clear();
        }
    }

    private void rebuildMatrix(Node end) {
        int rows = matrix.getRows();
        int cols = matrix.getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node node = matrix.getValue(row, col);
                if (node.getValue() != 1) {
                    node.setEnabled(false);
                }
            }
        }

        end.setVisited(true);
        end.setCost(0);
    }

}
