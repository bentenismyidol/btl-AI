package com.hg;

import com.hg.Node;


public class Edge {

	private Node nodeA;
	private Node nodeB;
	private int weight;

	/**
	 * {@inheritDoc}
	 */
	
	public Node getNodeA() {
		return this.nodeA;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public Node getNodeB() {
		return this.nodeB;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public Node getOpposite(Node node) {
		if (node == this.nodeA) {
			return this.nodeB;
		}
		if (node == this.nodeB) {
			return this.nodeA;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public int getWeight() {
		return this.weight;
	}
	
	/**
	 * Set the weight of the edge.
	 * 
	 * @param weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * Set node A.
	 * 
	 * @param nodeA
	 */
	public void setNodeA(Node nodeA) {
		this.nodeA = nodeA;
	}

	/**
	 * Set node B.
	 * 
	 * @param nodeB
	 */
	public void setNodeB(Node nodeB) {
		this.nodeB = nodeB;
	}

}
