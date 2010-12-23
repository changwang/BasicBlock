package basicblock;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

public class BasicBlock {

	private int ID;
	private List<Node> content = new ArrayList<Node>();
	private BasicBlock nextTrueBlock = null;
	private BasicBlock nextFalseBlock = null;

	public BasicBlock() {
	}

	public BasicBlock(Node aNode) {
		content.add(aNode);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public List<Node> getContent() {
		return content;
	}

	public void setContent(List<Node> content) {
		this.content = content;
	}

	public void addNode(Node aNode) {
		content.add(aNode);
	}

	public BasicBlock getNextTrueBlock() {
		return nextTrueBlock;
	}

	public void setNextTrueBlock(BasicBlock nextTrueBlock) {
		this.nextTrueBlock = nextTrueBlock;
	}

	public BasicBlock getNextFalseBlock() {
		return nextFalseBlock;
	}

	public void setNextFalseBlock(BasicBlock nextFalseBlock) {
		this.nextFalseBlock = nextFalseBlock;
	}

	/**
	 * Whether the list contains the given ASTNode
	 * 
	 * @param node
	 * @return true if it has, otherwise false
	 */
	public boolean hasAstNode(ASTNode node) {
		List<Node> nodes = this.getContent();
		for (Node n : nodes) {
			if (n.getAstNode() == node) {
				return true;
			}
		}
		return false;
	}

	/**
	 * get the last ast node in the content list
	 * 
	 * @return
	 */
	public ASTNode lastNodeInList() {
		if (content.size() > 0) {
			return content.get(content.size() - 1).getAstNode();
		}
		return null;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Block:\t");
		sb.append(this.getID());
		// List<Node> nodes = this.getContent();
		// for (Node node : nodes) {
		// switch (node.getType()) {
		// case ASTNode.IF_STATEMENT:
		// sb.append("If Statement with expression: "
		// + ((IfStatement) node.getAstNode()).getExpression()
		// .toString() + "\n");
		// break;
		// case ASTNode.WHILE_STATEMENT:
		// sb.append("While Statement with expression: "
		// + ((WhileStatement) node.getAstNode()).getExpression()
		// .toString() + "\n");
		// break;
		// case ASTNode.DO_STATEMENT:
		// sb.append("While Statement with expression: "
		// + ((DoStatement) node.getAstNode()).getExpression()
		// .toString() + "\n");
		// break;
		// case ASTNode.FOR_STATEMENT:
		// sb.append("For Statement with expression: "
		// + ((ForStatement) node.getAstNode()).getExpression()
		// .toString() + "\n");
		// break;
		// case ASTNode.ENHANCED_FOR_STATEMENT:
		// sb.append("For Statement with expression: "
		// + ((EnhancedForStatement) node.getAstNode())
		// .getExpression().toString() + "\n");
		// break;
		// default:
		// sb.append("Statement: " + node.getAstNode().toString());
		// break;
		// }
		// }
		// sb.append("Next True Block is: " + getNextTrueID() + "\n");
		// sb.append("Next False Block is: " + getNextFalseID() + "\n");
		return sb.toString();
	}
}
