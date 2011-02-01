package basicblock.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;

import basicblock.BasicBlock;
import basicblock.Node;

/**
 * the algorithm used to find all possible paths between given def and use.
 */
public class DUPathFinder {

	/*
	 * the given def node, we could call it start node.
	 */
	private ASTNode def = null;
	/*
	 * the give use node, we could call it end node.
	 */
	private ASTNode use = null;

	/*
	 * a set used to put all paths, using set to eliminate duplication.
	 */
	private Set<ArrayList<BasicBlock>> paths = new HashSet<ArrayList<BasicBlock>>();
	/*
	 * back list used to track visited basic block in the graph.
	 */
	private ArrayList<BasicBlock> back = new ArrayList<BasicBlock>();
	/*
	 * circle list used to track circle paths.
	 */
	private ArrayList<BasicBlock> circle = new ArrayList<BasicBlock>();

	public DUPathFinder(ASTNode def, ASTNode use) {
		this.def = def;
		this.use = use;
	}

	public Set<ArrayList<BasicBlock>> getPaths() {
		return paths;
	}

	/**
	 * core of the algorithm, recursively visiting next basic block, until we
	 * reach the end node.
	 * 
	 * @param blocks
	 * @param start
	 * @param end
	 */
	public void findPaths(List<BasicBlock> blocks, BasicBlock start,
			BasicBlock end) {
		back.add(start);

		BasicBlock temp = start;
		/*
		 * find the end node in next true block.
		 */
		if (null != temp.getNextTrueBlock()) {
			/*
			 * if next true block has the end node, then one path is found
			 */
			temp = temp.getNextTrueBlock();
			if (hasASTNode(temp, this.use)) {
				ArrayList<BasicBlock> path = (ArrayList<BasicBlock>) back
						.clone();
				path.add(temp);
				paths.add(path);
			}
			/*
			 * find next true block of next true block.
			 */
			if (!back.contains(temp)) {
				findPaths(blocks, temp, end);
			} else {
				/*
				 * otherwise there is a circle.
				 */
				circle.add(temp);
			}
		}

		/*
		 * find the end node in next false block.
		 */
		temp = start;
		if (null != temp.getNextFalseBlock()) {
			/*
			 * if next false block has the end node, then one path is found
			 */
			temp = temp.getNextFalseBlock();
			if (hasASTNode(temp, this.use)) {
				ArrayList<BasicBlock> path = (ArrayList<BasicBlock>) back
						.clone();
				path.add(temp);
				paths.add(path);
			}
			/*
			 * find next false block of next false block.
			 */
			if (!back.contains(temp)) {
				findPaths(blocks, temp, end);
			} else {
				/*
				 * otherwise there is a circle.
				 */
				circle.add(temp);
			}
		}
		/*
		 * pops last visited basic block
		 */
		back.remove(start);
	}

	/**
	 * Whether the given basic block contains the given ASTNode
	 * 
	 * @param block
	 * @param astNode
	 * @return
	 */
	private boolean hasASTNode(BasicBlock block, ASTNode astNode) {
		for (Node node : block.getContent()) {
			if (node.getAstNode() == astNode) {
				return true;
			}
		}
		return false;
	}

}
