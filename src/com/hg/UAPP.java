package com.hg;


import java.util.ArrayList;
import java.util.List;

public class UAPP {

    /**
     * @return the isStopping
     */
    public boolean isIsStopping() {
        return isStopping;
    }

    /**
     * @param isStopping the isStopping to set
     */
    public void setIsStopping(boolean isStopping) {
        this.isStopping = isStopping;
    }
    private volatile boolean isStopping = false;
    Matrix matrix, storeMatrix;
    boolean isUp = false; // direction
    List<Node> cleanedNode = new ArrayList<>();
    List<Integer> indexOfDupNode = new ArrayList<>();
    static int cost = 0;
    static Node newStart;
    private volatile boolean searching = false;
    AStar a;

    public Matrix mainAlg() {
//        MyAStar m = new MyAStar(this);
//        print(m.aStar(matrix.getValue(0, 0), matrix.get))
        a = new AStar(this);
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
                next = getNextNode(start); // u turn
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
        //return cleanedNode;
    }

    public List<Integer> getListDuplicateNode() {
        synchronized (indexOfDupNode) {
            return indexOfDupNode;
        }
        //return cleanedNode;
    }

    public boolean isSearching() {
        return searching;
    }

    public static void print(Object o) {
        System.out.println(o);
    }

    public Node getNextNode(Node node) {   // u-turn algorithm   
        try {
            Node next;
            if (!isUp) {
                next = moveDown(node);
            } else {
                next = moveUp(node);
            }
            if (next == null || next.getValue() != 0) {
                next = turnRight(node); // turn right first
                if (next == null || next.getValue() != 0) {
                    if (next == null || next.getValue() == 1) {
                        next = turnLeft(node); // turn left if cannot turn right
                        if (next == null || next.getValue() == 1) {
                            return null; // dead state
                        } else if (next.getValue() == 2) {
                            return rollBack(node);   //roll back
                        } else {
                            changeDirection();
                            return next;
                        }
                    } else if (next.getValue() == 2) { // traverse obstacle -> roll back
                        return rollBack(node);
                    } else {
                        changeDirection();
                        return next;
                    }
                } else {
                    changeDirection();
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
        matrix.buildGraphWithValue();
        List<Node> nearestNode = alg.findNearestNode(uncoveredArea, node, matrix);
        //List<Node> list1 = a.findNearestPoint(uncoveredArea, node);
        if (nearestNode.isEmpty()) {
            System.out.println("qweqwewqewqeqwes");
            return null;
        } else {
            System.out.println("printTrace original: " + nearestNode);
            //System.out.println("printTrace myastar: " + list1);
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

    public UAPP(Matrix matrix) {
        super();
        this.matrix = matrix;
        //this.matrix.reset();
    }

    public UAPP() {

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

        if (isUp) { // opposite direction
            int i = 1;
            while (i < matrix.getRows()) {

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
                        changeDirection();
                        return next;
                    }
                } else {
                    cleanedNode.addAll(tmpNode);
                    changeDirection();
                    return next;
                }
            }
            return null; // dead state
        } else {
            int i = 1;
            while (i < matrix.getRows()) {
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
                        changeDirection();
                        return next;
                    }
                } else {
                    cleanedNode.addAll(tmpNode);
                    changeDirection();
                    return next;
                }
            }
            return null;
        } // dead state
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
}
