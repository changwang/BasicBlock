package basicblock;

import org.eclipse.jdt.core.dom.ASTNode;

public class Node {

	private ASTNode astNode = null; // the actual node from AST
	private int type;

	public Node(ASTNode aNode, int type) {
		assert aNode != null;
		this.astNode = aNode;
		this.type = aNode.getNodeType();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ASTNode getAstNode() {
		return astNode;
	}

	public void setAstNode(ASTNode astNode) {
		this.astNode = astNode;
	}

	public String toString() {
		return astNode.toString();
	}
}
