package com.hg;

import java.util.ArrayList;
import java.util.List;

public class ISS {

    Matrix matrix, storeMatrix;
    boolean isUp = false; // direction
    List<Node> cleanedNode = new ArrayList<>();
    List<Node> uncoveredArea = new ArrayList<>();
    static int cost = 0;
    static Node newStart;
    private volatile boolean searching = false;
    private int d = 1;  //0: up , 1: down, 2: left, 3: right
    List<Integer> indexOfDupNode = new ArrayList<>();
    private volatile boolean isStopping = false;

    public Matrix mainAlg() {
        matrix.clean();
        cleanedNode.clear();
        indexOfDupNode.clear();

        cleanedNode = new ArrayList<>();
        int j = 0;
        Node start = matrix.getValue(0, 0);
        start.setValue(1);
        cleanedNode.add(start);
        Node next;
        while (true) {
            if (isIsStopping()) {

                try {
                    System.out.println("Stop .");
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }

            } else {
                System.out.println("Current node : " + getCoor(start));
                next = getNextNode(start); // iss
                if (next == null) { // dead state
                    next = getNewStartPoint(cleanedNode.get(cleanedNode.size() - 1)); // a star
                    System.out.println("New start point: " + getCoor(next));
                    if (next == null) {
                        break;
                    }
                    next.setValue(1);
                    cleanedNode.add(next);
                } else {
                    next.setValue(1);
                    cleanedNode.add(next);
                }
                start = next;
                searching = true;
            }
        }
        searching = false;
        System.out.println("");
        System.out.println("Result : ");
        for (int i = 0; i < cleanedNode.size(); i++) {
            Node node = cleanedNode.get(i);
            System.out.print("[" + node.getRow() + ", " + node.getCol() + "] ;");
        }
        System.out.println("");
        printMatrix();
        matrix.clean();
        return matrix;
    }

    public List<Node> getListCleanedNode() {
        synchronized (cleanedNode) {
            return cleanedNode;
        }

    }

    public boolean isSearching() {
        return searching;
    }

    public static void print(Object o) {
        System.out.println(o);
    }

    public boolean isIsStopping() {
        return isStopping;
    }

    /**
     * @param isStopping the isStopping to set
     */
    public void setIsStopping(boolean isStopping) {
        this.isStopping = isStopping;
    }

   

    public Node getNextNode(Node node) {   // iss algorithm   
        try {
            Node next;
            next = moveForward(node);
            if (next == null || next.getValue() != 0) {
                next = rotate90(node);
                if (next == null || next.getValue() != 0) {

                    next = rotate270(node); // turn left if cannot turn right
                    if (next == null || next.getValue() == 1) {
                        next = rotate90(node);
                        if (next != null && next.getValue() == 2) {
                            return rollBack(node);
                        } else {
                            return null;
                        }
                    } else if (next.getValue() == 2) {
                        return rollBack(node);   //roll back
                    } else {
                        changeDirection270();
                        return next;
                    }
                } else {
                    changeDirection90();
                    return next;
                }
            } else {
                return next;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Oops !");
            return null;
        }
//		return null;
    }

    private void changeDirection() {
        boolean dirTmp = isUp;
        isUp = !dirTmp;
    }

    private boolean uncoveredAreaExist() {
        // TODO Auto-generated method stub
        return false;
    }

    private List<Node> getUncoveredArea() {
        List<Node> list = new ArrayList<>();
        cleanedNode.forEach(cleaned -> {
            getNeighbor(cleaned).forEach(node -> {
                if (node != null && node.getValue() == 0) {
                    if (!list.contains(node)) {
                        list.add(node);
                    }
                }

            });
        });
        return list;
    }

    private Node getNewStartPoint(Node node) {
        AStarPathAlgorithm alg = new AStarPathAlgorithm();
        AStarCostEvaluator eval = new AStarCostEvaluator();
        alg.setEvaluator(eval, matrix);
        cost = 10000000;
        newStart = null;
        List<Node> uncoveredArea = getUncoveredArea();
        if (uncoveredArea == null || uncoveredArea.isEmpty()) // mean all grid are cleaned
        {
            return null;
        }
        System.out.println("Size uncoverd area : " + uncoveredArea.size());
        List<Node> nearestNode = alg.findNearestNode(uncoveredArea, node, matrix);
        if (nearestNode.isEmpty()) {
            return null;
        } else {
            System.out.println("printTrace : " + nearestNode);
            for (int i = 0; i < nearestNode.size() - 1; i++) {
                indexOfDupNode.add(cleanedNode.size() + i);
            }
            newStart = nearestNode.get(nearestNode.size() - 1);
            nearestNode.remove(nearestNode.size() - 1);
            cleanedNode.addAll(nearestNode);
            System.out.println("indexOfDupNode : " + indexOfDupNode);
        }

        return newStart;
    }

    public ISS(Matrix matrix) {
        super();
        this.matrix = matrix;
    }

    public ISS() {

    }

    public List<Integer> getListDuplicateNode() {
        synchronized (indexOfDupNode) {
            return indexOfDupNode;
        }
        //return cleanedNode;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public void printMatrix() {
        System.out.println("Print matrix \n");
        int rows = matrix.getRows();
        int cols = matrix.getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node node = matrix.getValue(row, col);
                System.out.print("(" + node.getRow() + ", " + node.getCol() + ")" + node.getValue() + " ");
            }
            System.out.println("");
        }
    }

    private Node rollBack(Node node) {
        System.out.println("Roll back");
        Node next;
        List<Node> tmpNode = new ArrayList<>();

        if (d == 0) { // opposite direction
            int i = 1;
            while (i < matrix.getColumns()) {

                Node tmp = moveDown(node, i);     // "walk straight along reverse direction"
                tmpNode.add(tmp);
                if (tmp == null) {
                    i++;
                    continue;
                }
                if (tmp.getValue() != 1) {
                    return null; // over
                }
                next = turnRight(tmp);
                if (next == null) {
                    i++;
                } else if (next.getValue() != 0) {
                    next = turnLeft(tmp);
                    if (next == null || next.getValue() != 0) {
                        i++;
                    } else {
                        cleanedNode.addAll(tmpNode);
                        return next;
                    }
                } else {
                    cleanedNode.addAll(tmpNode);
                    return next;
                }
            }
            return null; // dead state
        } else if (d == 1) {
            int i = 1;
            while (i < matrix.getColumns()) {
                Node tmp = moveUp(node, i);
                tmpNode.add(tmp);
                if (tmp == null) {
                    i++;
                    continue;
                }
                if (tmp.getValue() != 1) {
                    return null;
                }
                next = turnRight(tmp);
                if (next == null) {
                    i++;
                } else if (next.getValue() != 0) {
                    next = turnLeft(tmp);
                    if (next == null || next.getValue() != 0) {
                        i++;
                    } else {
                        cleanedNode.addAll(tmpNode);
                        return next;
                    }
                } else {
                    cleanedNode.addAll(tmpNode);
                    return next;
                }
            }
            return null;
        } else if (d == 2) {
            int i = 1;
            while (i < matrix.getRows()) {
                Node tmp = turnRight(node, i);
                tmpNode.add(tmp);
                if (tmp == null) {
                    i++;
                    continue;
                }
                if (tmp.getValue() != 1) {
                    return null;
                }
                next = moveDown(tmp);
                if (next == null) {
                    i++;
                } else if (next.getValue() != 0) {
                    next = moveUp(tmp);
                    if (next == null || next.getValue() != 0) {
                        i++;
                    } else {
                        cleanedNode.addAll(tmpNode);
                        return next;
                    }
                } else {
                    cleanedNode.addAll(tmpNode);
                    return next;
                }
            }
            return null;
        } else {
            int i = 1;
            while (i < matrix.getRows()) {
                Node tmp = turnLeft(node, i);
                tmpNode.add(tmp);
                if (tmp == null) {
                    i++;
                    continue;
                }
                if (tmp.getValue() != 1) {
                    return null;
                }
                next = moveUp(tmp);
                if (next == null) {
                    i++;
                } else if (next.getValue() != 0) {
                    next = moveDown(tmp);
                    if (next == null || next.getValue() != 0) {
                        i++;
                    } else {
                        cleanedNode.addAll(tmpNode);
                        return next;
                    }
                } else {
                    cleanedNode.addAll(tmpNode);
                    return next;
                }
            }
            return null;
        }
    }

    public String getCoor(Node node) {
        if (node == null) {
            return "Not exist !";
        }
        return "(" + node.getRow() + ", " + node.getCol() + ")";
    }

    public Node turnLeft(Node node) {
        if (node == null) {
            return null;
        }
        return matrix.getValue(node.getRow(), node.getCol() - 1);
    }

    public Node turnRight(Node node) {
        if (node == null) {
            return null;
        }
        return matrix.getValue(node.getRow(), node.getCol() + 1);
    }

    public Node turnLeft(Node node, int i) {
        if (node == null) {
            return null;
        }
        return matrix.getValue(node.getRow(), node.getCol() - i);
    }

    public Node turnRight(Node node, int i) {
        if (node == null) {
            return null;
        }
        return matrix.getValue(node.getRow(), node.getCol() + i);
    }

    public Node moveUp(Node node) {
        if (node == null) {
            return null;
        }
        return matrix.getValue(node.getRow() - 1, node.getCol());
    }

    public Node moveDown(Node node) {
        if (node == null) {
            return null;
        }
        return matrix.getValue(node.getRow() + 1, node.getCol());
    }

    public Node moveUp(Node node, int i) {
        if (node == null) {
            return null;
        }
        return matrix.getValue(node.getRow() - i, node.getCol());
    }

    public Node moveDown(Node node, int i) {
        if (node == null) {
            return null;
        }
        return matrix.getValue(node.getRow() + i, node.getCol());
    }

    public List<Node> getNeighbor(Node node) {
        List<Node> list = new ArrayList<>();
        list.add(turnLeft(node));
        list.add(moveUp(turnLeft(node)));
        list.add(moveDown(turnLeft(node)));
        list.add(turnRight(node));
        list.add(moveUp(turnRight(node)));
        list.add(moveDown(turnRight(node)));
        list.add(moveUp(node));
        list.add(moveDown(node));
        return list;

    }

    public Node rotate90(Node node) {
        if (d == 0) {
            return turnLeft(node);
        } else if (d == 1) {
            return turnRight(node);
        } else if (d == 2) {
            return moveDown(node);
        } else {
            return moveUp(node);
        }
    }
    public void changeDirection90() {
        if (d == 0) {
            d = 2;
        } else if (d == 1) {
            d = 3;
        } else if (d == 2) {
            d = 1;
        } else {
            d = 0;
        }
    }
    public void changeDirection270() {
        if (d == 1) {
            d = 2;
        } else if (d == 0) {
            d = 3;
        } else if (d == 3) {
            d = 1;
        } else {
            d = 0;
        }
    }
    public Node rotate270(Node node) {
        if (d == 1) {
            return turnLeft(node);
        } else if (d == 0) {
            return turnRight(node);
        } else if (d == 3) {
            return moveDown(node);
        } else {
            return moveUp(node);
        }
    }

    public Node moveForward(Node node) {
        if (d == 0) {
            return moveUp(node);
        } else if (d == 1) {
            return moveDown(node);
        } else if (d == 2) {
            return turnLeft(node);
        } else {
            return turnRight(node);
        }
    }
}
