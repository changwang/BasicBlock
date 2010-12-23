package basicblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class BasicBlockVisitor {

	private List<BasicBlock> blocks = new ArrayList<BasicBlock>();
	// this list always maintains the basicblocks which are the tail of its
	// parent node
	// private List<BasicBlock> tailBlocks;
	private Stack<List<BasicBlock>> tails = new Stack<List<BasicBlock>>();
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
	 * @return basic block the node belongs to
	 */
	private BasicBlock visitAssignment(ASTNode node) {
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
		return currentBasicBlock;
	}

	/**
	 * visiting IF statement node, insert IF node into current basic block, then
	 * create a new basic block, and set it as current basic block; we know next
	 * node must be in a new basic block, because its parent is a IF node
	 * 
	 * @param node
	 */
	private BasicBlock visitIfStatement(ASTNode node) {
		List<BasicBlock> tailBlocks = new ArrayList<BasicBlock>();
		BasicBlock ifBlock = currentBasicBlock;
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));

		// reset current basic block to a new empty basic block
		BasicBlock newBasicBlock = createEmptyBasicBlock();
		// next true always points to its first child
		ifBlock.setNextTrueBlock(newBasicBlock);
		currentBasicBlock = newBasicBlock;
		ASTNode thenStatement = ((IfStatement) node).getThenStatement();
		BasicBlock tailBlock = visitChild(thenStatement);
		if (null != tailBlocks && null != tailBlock) {
			tailBlocks.add(tailBlock);
		}
		tails.push(tailBlocks);

		ASTNode elseStatement = ((IfStatement) node).getElseStatement();
		if (null != elseStatement) {
			newBasicBlock = createEmptyBasicBlock();
			// if there is else statement, next false always points to it
			ifBlock.setNextFalseBlock(newBasicBlock);
			currentBasicBlock = newBasicBlock;
			tailBlocks = tails.pop();
			//for (BasicBlock bb : tailBlocks) {
			//	bb.setNextTrueBlock(currentBasicBlock);
			//}
			tailBlock = visitChild(elseStatement);

			tailBlocks.add(tailBlock);
			tails.push(tailBlocks);
		}
		return ifBlock;
	}

	/**
	 * visiting WHILE statement node, insert WHILE node into current basic
	 * block, then create a new basic block, and set it as current basic block;
	 * we know next node must be in a new basic block, because its parent is a
	 * WHILE node
	 * 
	 * @param node
	 */
	private BasicBlock visitWhileStatement(ASTNode node) {
		BasicBlock whileBlock = currentBasicBlock;
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
		BasicBlock newBasicBlock = createEmptyBasicBlock();
		whileBlock.setNextTrueBlock(newBasicBlock);
		currentBasicBlock = newBasicBlock;
		ASTNode bodyStatements = ((WhileStatement) node).getBody();
		visitChild(bodyStatements); // bodyStatements cannot be null
		return whileBlock;
	}

	/**
	 * visiting DO WHILE statement node, insert DO WHILE node into current basic
	 * block, then create a new basic block, and set it as current basic block;
	 * we know next node must be in a new basic block, because its parent is a
	 * DO WHILE node
	 * 
	 * @param node
	 */
	private BasicBlock visitDoStatement(ASTNode node) {
		BasicBlock doBlock = currentBasicBlock;
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
		BasicBlock newBasicBlock = createEmptyBasicBlock();
		doBlock.setNextTrueBlock(newBasicBlock);
		currentBasicBlock = newBasicBlock;
		ASTNode bodyStatements = ((DoStatement) node).getBody();
		visitChild(bodyStatements); // bodyStatements cannot be null
		return doBlock;
	}

	/**
	 * visiting FOR statement node, insert FOR node into current basic block,
	 * then create a new basic block, and set it as current basic block; we know
	 * next node must be in a new basic block, because its parent is a FOR node
	 * 
	 * @param node
	 */
	private BasicBlock visitForStatement(ASTNode node) {
		BasicBlock forBlock = currentBasicBlock;
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
		BasicBlock newBasicBlock = createEmptyBasicBlock();
		forBlock.setNextTrueBlock(newBasicBlock);
		currentBasicBlock = newBasicBlock;
		ASTNode bodyStatements = ((ForStatement) node).getBody();
		visitChild(bodyStatements); // bodyStatements cannot be null
		return forBlock;
	}

	/**
	 * visiting ENHANCED FOR statement node, insert FOR node into current basic
	 * block, then create a new basic block, and set it as current basic block;
	 * we know next node must be in a new basic block, because its parent is a
	 * ENHANCED FOR node
	 * 
	 * @param node
	 */
	private BasicBlock visitEnhancedForStatement(ASTNode node) {
		BasicBlock forBlock = currentBasicBlock;
		currentBasicBlock.addNode(new Node(node, node.getNodeType()));
		BasicBlock newBasicBlock = createEmptyBasicBlock();
		forBlock.setNextTrueBlock(newBasicBlock);
		currentBasicBlock = newBasicBlock;
		ASTNode bodyStatements = ((EnhancedForStatement) node).getBody();
		visitChild(bodyStatements); // bodyStatements cannot be null
		return forBlock;
	}

	/**
	 * visit BLOCK statement, in this case, we'll go to visit each child node
	 * inside the block by calling appropriate visit method
	 * 
	 * @param node
	 *            block node
	 */
	private BasicBlock visitBlock(ASTNode node) {
		ASTNode prevNode = null, currNode = null;
		BasicBlock prevBasicBlock = null, currBasicBlock = null;
		List<ASTNode> statements = ((Block) node).statements();
		if (statements.size() < 1)
			return null;
		for (int i = 0; i < statements.size(); i++) {
			currNode = statements.get(i);
			if (null == prevNode) { // first time iterates the children
				currBasicBlock = visitChild(currNode);
			} else {
				if (Helper.isControlNode(prevNode)) {
					currentBasicBlock = createEmptyBasicBlock();
					currBasicBlock = visitChild(currNode);
					if (prevBasicBlock.getNextFalseBlock() == null) {
						prevBasicBlock.setNextFalseBlock(currBasicBlock);
					}
				} else {
					currBasicBlock = visitChild(currNode);
				}

				while (!tails.empty()) {
					List<BasicBlock> tbs = tails.pop();
					for (BasicBlock bb : tbs) {
						switch (prevNode.getNodeType()) {
						case ASTNode.IF_STATEMENT:
							if (null == bb.getNextTrueBlock()) {
								bb.setNextTrueBlock(currBasicBlock);
							}
							if (null == bb.getNextFalseBlock()
									&& Helper
											.isControlNode(bb.lastNodeInList())) {
								bb.setNextFalseBlock(currBasicBlock);
							}
							continue;
						case ASTNode.WHILE_STATEMENT:
						case ASTNode.DO_STATEMENT:
						case ASTNode.FOR_STATEMENT:
						case ASTNode.ENHANCED_FOR_STATEMENT:
							bb.setNextTrueBlock(prevBasicBlock);
							continue;
						}
					}
				}
			}
			prevNode = currNode;
			prevBasicBlock = currBasicBlock;
		}
		return currBasicBlock;
	}

	/**
	 * Find out the basic block which contains the given ASTNode
	 * 
	 * @param node
	 *            given ASTNode
	 * @return
	 */
	private BasicBlock findBasicBlockHasGivenNode(ASTNode node) {
		if (null == node) {
			return null;
		}
		for (BasicBlock bb : blocks) {
			if (bb.hasAstNode(node)) {
				return bb;
			}
		}
		return null;
	}

	/**
	 * because we don't know the child type, so find out, and visit it
	 * 
	 * @param child
	 */
	private BasicBlock visitChild(ASTNode child) {
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
