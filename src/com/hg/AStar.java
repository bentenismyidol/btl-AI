
package com.hg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hg
 */
public class AStar {

    UAPP u;
    int c = 0;
    Set<Node> open;
    public boolean isSearching = false;

    public AStar(UAPP u) {
        this.u = u;
    }

    public List<Node> aStar(Node start, Node goal, boolean isSP) {
        isSearching = true;
        start.setValue(1);
        goal.setValue(1);
        c = 0;
        open = new HashSet<Node>();
        Set<Node> closed = new HashSet<Node>();

        start.g = 0;
        start.h = estimateDistance(start, goal);
        start.f = start.h;

        open.add(start);

        while (true) {
            Node current = null;

            if (open.size() == 0) {
                throw new RuntimeException("no route");
            }

            for (Node node : open) {
                if (current == null || node.f < current.f) {
                    current = node;
                }
            }

            if (current == goal) {
                break;
            }

            open.remove(current);
            closed.add(current);

            for (Node neighbor : getNeighbor(current, isSP)) {
                if (neighbor == null) {
                    continue;
                }

                int nextG = current.g + getDistanceNeighbor(current, neighbor);

                if (nextG < neighbor.g) {
                    open.remove(neighbor);
                    closed.remove(neighbor);
                }

                if (!open.contains(neighbor) && !closed.contains(neighbor)) {
                    neighbor.g = nextG;
                    neighbor.h = estimateDistance(neighbor, goal);
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = current;
                    open.add(neighbor);
                }
            }
        }

        List<Node> nodes = new ArrayList<Node>();
        Node current = goal;
        while (current.parent != null) {
            nodes.add(current);
            c = c + getDistanceNeighbor(current, current.parent);
            current = current.parent;
        }
        
        nodes.add(start);
        Collections.reverse(nodes);
        isSearching = false;
        return nodes;
    }

    public int estimateDistance(Node node1, Node node2) {
        return 10 * (Math.abs(node1.getRow() - node2.getRow()) + Math.abs(node1.getCol() - node2.getCol()));
    }

    public List<Node> getNeighbor(Node node, boolean isSP) {
        List<Node> list = u.getNeighbor(node);
        List<Node> result = new ArrayList();
        for (Node n : list) {
            if (n == null) {
                continue;
            }
            if (isSP) {
                if (n.getValue() == 1) {
                    result.add(n);
                }
            } else {
                if (n.getValue() == 0) {
                    result.add(n);
                }
            }

        }
        return list;
    }

    public int getDistanceNeighbor(Node nodeA, Node nodeB) {
        if (nodeA.getRow() == nodeB.getRow() || nodeA.getCol() == nodeB.getCol()) {
            return 10;
        } else {
            return 14;
        }
    }

    public List<Node> findNearestPoint(List<Node> setEndPoint, Node startPoint) {
        int cost = 1000000;
        List<Node> result = null;
        for (Node node : setEndPoint) {
            List<Node> list = this.aStar(startPoint, node, false);
            if (this.c < cost) {
                result = list;
            }

        }
        return result;
    }

    public Set<Node> getOpenNode() {
        synchronized (open) {
            return open;
        }
    }

    public Set<Node> getResultPath() {
        while (true) {
            if (isSearching) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AStar.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                return open;
            }
        }
    }
}
