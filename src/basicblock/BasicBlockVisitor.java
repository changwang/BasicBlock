package basicblock;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class BasicBlockVisitor {

	private List<BasicBlock> blocks = new ArrayList<BasicBlock>();
	private BasicBlock currentBasicBlock = new BasicBlock();

	private ASTNode root;

	public BasicBlockVisitor() {
		currentBasicBlock = createEmptyBasicBlock();
	}

	public BasicBlockVisitor(ASTNode root) {
		this.root = root;
		currentBasicBlock = createEmptyBasicBlock();
	}

	/**
	 * set the root entry
	 * 
	 * @param root
	 */
	public void setRoot(ASTNode root) {
		this.root = root;
	}

	/**
	 * return the block list
	 * 
	 * @return
	 */
	public List<BasicBlock> getBlocks() {
		return blocks;
	}

	/**
	 * begin to traverse the AST
	 */
	public void start() {
		if (null == root) {
			System.err.println("Please set root node to start parsing!");
			return;
		}
		visitBlock(root);
	}

	/**
	 * visiting ASSIGNMENT node (such as assignment, initialization ...), here
	 * we just insert the node into current basic bloc, let its parent set
	 * true/fals refs
	 * 
	 * @param node
	 *            assignment node
	 * @return
	 */
	private void visitAssignment(ASTNode node) {
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
	}

	/**
	 * visiting IF statement node, insert IF node into current basic block, then
	 * create a new basic block, and set it as current basic block; we know next
	 * node must be in a new basic block, because its parent is a IF node
	 * 
	 * @param node
	 */
	private void visitIfStatement(ASTNode node) {
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
		// reset current basic block to a new empty basic block
		currentBasicBlock = createEmptyBasicBlock();
		ASTNode thenStatement = ((IfStatement) node).getThenStatement();
		visitChild(thenStatement); // thenstatement cannot be null

		ASTNode elseStatement = ((IfStatement) node).getElseStatement();
		if (null != elseStatement) {
			currentBasicBlock = createEmptyBasicBlock();
			visitChild(elseStatement);
		}
	}

	/**
	 * visiting WHILE statement node, insert WHILE node into current basic
	 * block, then create a new basic block, and set it as current basic block;
	 * we know next node must be in a new basic block, because its parent is a
	 * WHILE node
	 * 
	 * @param node
	 */
	private void visitWhileStatement(ASTNode node) {
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
		currentBasicBlock = createEmptyBasicBlock();
		ASTNode bodyStatements = ((WhileStatement) node).getBody();
		visitChild(bodyStatements); // bodyStatements cannot be null
	}

	/**
	 * visiting DO WHILE statement node, insert DO WHILE node into current basic
	 * block, then create a new basic block, and set it as current basic block;
	 * we know next node must be in a new basic block, because its parent is a
	 * DO WHILE node
	 * 
	 * @param node
	 */
	private void visitDoStatement(ASTNode node) {
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
		currentBasicBlock = createEmptyBasicBlock();
		ASTNode bodyStatements = ((DoStatement) node).getBody();
		visitChild(bodyStatements); // bodyStatements cannot be null
	}

	/**
	 * visiting FOR statement node, insert FOR node into current basic block,
	 * then create a new basic block, and set it as current basic block; we know
	 * next node must be in a new basic block, because its parent is a FOR node
	 * 
	 * @param node
	 */
	private void visitForStatement(ASTNode node) {
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
		currentBasicBlock = createEmptyBasicBlock();
		ASTNode bodyStatements = ((ForStatement) node).getBody();
		visitChild(bodyStatements); // bodyStatements cannot be null
	}

	/**
	 * visiting ENHANCED FOR statement node, insert FOR node into current basic
	 * block, then create a new basic block, and set it as current basic block;
	 * we know next node must be in a new basic block, because its parent is a
	 * ENHANCED FOR node
	 * 
	 * @param node
	 */
	private void visitEnhancedForStatement(ASTNode node) {
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
		currentBasicBlock = createEmptyBasicBlock();
		ASTNode bodyStatements = ((EnhancedForStatement) node).getBody();
		visitChild(bodyStatements); // bodyStatements cannot be null
	}

	/**
	 * visit BLOCK statement, in this case, we'll go to visit each child node
	 * inside the block by calling appropriate visit method
	 * 
	 * @param node
	 *            block node
	 */
	private void visitBlock(ASTNode node) {
		List<ASTNode> statements = ((Block) node).statements();
		if (statements.size() < 1)
			return;
		for (ASTNode child : statements) {
			visitChild(child);
		}
	}

	/**
	 * because we don't know the child type, so find out, and visit it
	 * 
	 * @param child
	 */
	private void visitChild(ASTNode child) {
		switch (child.getNodeType()) {
		case ASTNode.IF_STATEMENT:
			visitIfStatement(child);
			break;
		case ASTNode.WHILE_STATEMENT:
			visitWhileStatement(child);
			break;
		case ASTNode.DO_STATEMENT:
			visitDoStatement(child);
			break;
		case ASTNode.FOR_STATEMENT:
			visitForStatement(child);
			break;
		case ASTNode.ENHANCED_FOR_STATEMENT:
			visitEnhancedForStatement(child);
		case ASTNode.BLOCK:
			visitBlock(child);
			break;
		default:
			visitAssignment(child);
			break;
		}
	}

	/**
	 * create an empty basic block, which will be used by currentBasicBlock,
	 * leave its next true/false refs unset
	 * 
	 * @return
	 */
	private BasicBlock createEmptyBasicBlock() {
		BasicBlock basicblock = new BasicBlock();
		basicblock.setID(getBasicBlockID());
		blocks.add(basicblock);
		return basicblock;
	}

	/**
	 * create block ID based on the size of block list
	 * 
	 * @return
	 */
	private int getBasicBlockID() {
		return blocks.size() + 1;
	}

}
