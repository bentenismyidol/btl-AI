package com.hg;



/**
 * An implementation for a graph in matrix form.
 * 
 * @author Kevin Wang
 *
 */
public class AStarCostEvaluator {
	PictureFuzzySet PFS = new PictureFuzzySet();
	volatile boolean enabled = true;
	final int factor = 10;
	//final int factor = (int) PFS.FuzzyLogic();
	public int evaluateWeight(Edge edge) {
                Node nodeA =  edge.getNodeA();
		Node nodeB =  edge.getNodeB();
		if (nodeA.getRow() == nodeB.getRow() || nodeA.getCol() == nodeB.getCol()) {
			return factor*1;
		}
		else {
			return (int) (factor*1.4);
		}
	}

	
	public int evaluateHeuristic(Node node, Node start, Node end) {
		return factor*(Math.abs(( node).getRow() - ( end).getRow()) + Math.abs(( node).getCol() - ( end).getCol()));
	}

	
	public int evaluateCost(Node candidate, Edge edge, Node start, Node end) {
		if (isEnabled()) {
			return edge.getOpposite(candidate).getCost() + edge.getWeight();
		}
                return -1;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
