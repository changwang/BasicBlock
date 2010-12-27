package basicblock;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

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

	/**
	 * make the basic block readable in console
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		//sb.append(this.getID());
		List<Node> nodes = this.getContent();
		for (Node node : nodes) {
			switch (node.getType()) {
			case ASTNode.IF_STATEMENT:
				sb.append("If Statement with expression: "
						+ ((IfStatement) node.getAstNode()).getExpression()
								.toString() + "\n");
				break;
			case ASTNode.WHILE_STATEMENT:
				sb.append("While Statement with expression: "
						+ ((WhileStatement) node.getAstNode()).getExpression()
								.toString() + "\n");
				break;
			case ASTNode.DO_STATEMENT:
				sb.append("While Statement with expression: "
						+ ((DoStatement) node.getAstNode()).getExpression()
								.toString() + "\n");
				break;
			case ASTNode.FOR_STATEMENT:
				sb.append("For Statement with expression: "
						+ ((ForStatement) node.getAstNode()).getExpression()
								.toString() + "\n");
				break;
			case ASTNode.ENHANCED_FOR_STATEMENT:
				sb.append("For Statement with expression: "
						+ ((EnhancedForStatement) node.getAstNode())
								.getExpression().toString() + "\n");
				break;
			default:
				sb.append("Statement: " + node.getAstNode().toString());
				break;
			}
		}
		if (null == getNextTrueBlock()) {
			sb.append("There is no next true block ref\n");
		} else {
			sb.append("Next True Block is: Block " + getNextTrueBlock().getID()
					+ "\n");
		}

		if (null == getNextFalseBlock()) {
			sb.append("There is no next true block ref\n");
		} else {
			sb.append("Next False Block is: Block " + getNextFalseBlock().getID()
					+ "\n");
		}
		return sb.toString();
	}
}
