package basicblock;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * Basically, I want to associate the def or use node with its parent astnode,
 * which could be easily find in basic block.
 */
public class DUNode {

	/**
	 * This is the parent node which contains def node, because def node is
	 * subtler, could spend more time to find it from basic block.
	 */
	private ASTNode parentNode;

	/**
	 * This is the node we find could be a def.
	 */
	private SimpleName du;

	public DUNode(ASTNode parentNode, SimpleName du) {
		this.parentNode = parentNode;
		this.du = du;
	}

	public ASTNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(ASTNode parentNode) {
		this.parentNode = parentNode;
	}

	public SimpleName getDU() {
		return du;
	}

	public void setDU(SimpleName du) {
		this.du = du;
	}

	public String toString() {
		return parentNode.toString() + " -> " + du.toString();
	}

}