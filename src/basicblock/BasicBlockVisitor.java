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

	// records all the generated basic blocks in this list
	private List<BasicBlock> blocks = new ArrayList<BasicBlock>();

	private BasicBlock currentBasicBlock = null;

	private ASTNode root;

	public BasicBlockVisitor() {
		currentBasicBlock = createEmptyBasicBlock();
	}

	public BasicBlockVisitor(ASTNode root) {
		setRoot(root);
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
		// ASTParser always generates block as the root
		visitBlock(root);
	}

	/**
	 * visiting ASSIGNMENT node (such as assignment, initialization ...), here
	 * we just insert the node into current basic bloc, let its parent set
	 * true/fals refs
	 * 
	 * @param node
	 *            assignment node
	 * @return basic block the node belongs to
	 */
	private List<BasicBlock> visitAssignment(ASTNode node) {
		List<BasicBlock> unhandledBlocks = new ArrayList<BasicBlock>();
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
		unhandledBlocks.add(currentBasicBlock);
		return unhandledBlocks;
	}

	/**
	 * visiting IF statement node, insert IF node into current basic block, then
	 * create a new basic block, and set it as current basic block; we know next
	 * node must be in a new basic block, because its parent is a IF node
	 * 
	 * @param node
	 */
	private List<BasicBlock> visitIfStatement(ASTNode node) {
		List<BasicBlock> unhandledBlocks = new ArrayList<BasicBlock>();
		BasicBlock ifBlock = currentBasicBlock;
		ifBlock.addNode(new Node(node, node.getNodeType()));

		// reset current basic block to a new empty basic block
		// next true always points to its first child
		currentBasicBlock = createEmptyBasicBlock();
		ifBlock.setNextTrueBlock(currentBasicBlock);
		ASTNode thenStatement = ((IfStatement) node).getThenStatement();
		unhandledBlocks.addAll(visitChild(thenStatement));

		ASTNode elseStatement = ((IfStatement) node).getElseStatement();
		if (null != elseStatement) {
			currentBasicBlock = createEmptyBasicBlock();
			// if there is else statement, next false always points to it
			ifBlock.setNextFalseBlock(currentBasicBlock);
			unhandledBlocks.addAll(visitChild(elseStatement));
		} else {
			// if there is no else clause,
			// then leave IF statement's false ref to its parent
			unhandledBlocks.add(ifBlock);
		}
		return unhandledBlocks;
	}

	/**
	 * visiting WHILE statement node, insert WHILE node into current basic
	 * block, then create a new basic block, and set it as current basic block;
	 * we know next node must be in a new basic block, because its parent is a
	 * WHILE node
	 * 
	 * @param node
	 */
	private List<BasicBlock> visitWhileStatement(ASTNode node) {
		return visitLoopStatement(node);
	}

	/**
	 * visiting DO WHILE statement node, insert DO WHILE node into current basic
	 * block, then create a new basic block, and set it as current basic block;
	 * we know next node must be in a new basic block, because its parent is a
	 * DO WHILE node
	 * 
	 * @param node
	 */
	private List<BasicBlock> visitDoStatement(ASTNode node) {
		return visitLoopStatement(node);
	}

	/**
	 * visiting FOR statement node, insert FOR node into current basic block,
	 * then create a new basic block, and set it as current basic block; we know
	 * next node must be in a new basic block, because its parent is a FOR node
	 * 
	 * @param node
	 */
	private List<BasicBlock> visitForStatement(ASTNode node) {
		return visitLoopStatement(node);
	}

	/**
	 * visiting ENHANCED FOR statement node, insert FOR node into current basic
	 * block, then create a new basic block, and set it as current basic block;
	 * we know next node must be in a new basic block, because its parent is a
	 * ENHANCED FOR node
	 * 
	 * @param node
	 */
	private List<BasicBlock> visitEnhancedForStatement(ASTNode node) {
		return visitLoopStatement(node);
	}

	/**
	 * WHILE, DO WHILE, FOR, ENHANCED FOR have the same logic, so abstract it to
	 * LOOP control node
	 * 
	 * @param node
	 * @return
	 */
	private List<BasicBlock> visitLoopStatement(ASTNode node) {
		List<BasicBlock> unhandledBlocks = new ArrayList<BasicBlock>();
		BasicBlock loopBlock = currentBasicBlock;
		loopBlock.addNode(new Node(node, node.getNodeType()));
		currentBasicBlock = createEmptyBasicBlock();
		loopBlock.setNextTrueBlock(currentBasicBlock);

		ASTNode bodyStatements = null;
		switch (node.getNodeType()) {
		case ASTNode.WHILE_STATEMENT:
			bodyStatements = ((WhileStatement) node).getBody();
			break;
		case ASTNode.DO_STATEMENT:
			bodyStatements = ((DoStatement) node).getBody();
			break;
		case ASTNode.FOR_STATEMENT:
			bodyStatements = ((ForStatement) node).getBody();
			break;
		case ASTNode.ENHANCED_FOR_STATEMENT:
			bodyStatements = ((EnhancedForStatement) node).getBody();
			break;
		default:
			System.err.println("Unknown loop type!");
			return null;
		}

		unhandledBlocks = visitChild(bodyStatements);

		if (unhandledBlocks.size() > 0) {
			for (BasicBlock bb : unhandledBlocks) {
				// if the unhandled node is a control node,
				// we know that its next false ref should point to
				// the loop control node
				if (Helper.isControlNode(bb.lastNodeInList())) {
					if (null == bb.getNextFalseBlock()) {
						bb.setNextFalseBlock(loopBlock);
					}
				} else {
					// otherwise it reaches the basic block
					// only contains simple assignment(s)
					bb.setNextTrueBlock(loopBlock);
				}
			}
		}
		// clear the list, insert current loop node into it
		// because its false ref is unset
		unhandledBlocks.clear();
		unhandledBlocks.add(loopBlock);
		return unhandledBlocks;
	}

	/**
	 * visit BLOCK statement, in this case, we'll go to visit each child node
	 * inside the block by calling appropriate visit method
	 * 
	 * @param node
	 *            block node
	 */
	private List<BasicBlock> visitBlock(ASTNode node) {
		List<BasicBlock> unhandledBlocks = null;
		ASTNode prevNode = null, currNode = null;
		List<ASTNode> statements = ((Block) node).statements();
		for (int i = 0; i < statements.size(); i++) {
			currNode = statements.get(i);
			if (unhandledBlocks != null) {
				if (Helper.isControlNode(prevNode)) {
					currentBasicBlock = createEmptyBasicBlock();
				}
				for (BasicBlock bb : unhandledBlocks) {
					if (prevNode.getNodeType() == ASTNode.IF_STATEMENT) {
						if (Helper.isControlNode(bb.lastNodeInList())) {
							bb.setNextFalseBlock(currentBasicBlock);
						} else {
							bb.setNextTrueBlock(currentBasicBlock);
						}
					}
					if (Helper.isLoopControlNode(prevNode)) {
						bb.setNextFalseBlock(currentBasicBlock);
					}
				}
			}
			unhandledBlocks = visitChild(currNode);
			prevNode = currNode;
		}
		return unhandledBlocks;
	}

	/**
	 * because we don't know the child type, so find out, and visit it
	 * 
	 * @param child
	 */
	private List<BasicBlock> visitChild(ASTNode child) {
		switch (child.getNodeType()) {
		case ASTNode.IF_STATEMENT:
			return visitIfStatement(child);
		case ASTNode.WHILE_STATEMENT:
			return visitWhileStatement(child);
		case ASTNode.DO_STATEMENT:
			return visitDoStatement(child);
		case ASTNode.FOR_STATEMENT:
			return visitForStatement(child);
		case ASTNode.ENHANCED_FOR_STATEMENT:
			return visitEnhancedForStatement(child);
		case ASTNode.BLOCK:
			return visitBlock(child);
		default:
			return visitAssignment(child);
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
		basicblock.setID(generateBasicBlockID());
		blocks.add(basicblock);
		return basicblock;
	}

	/**
	 * create block ID based on the size of block list
	 * 
	 * @return
	 */
	private int generateBasicBlockID() {
		return blocks.size() + 1;
	}

}
