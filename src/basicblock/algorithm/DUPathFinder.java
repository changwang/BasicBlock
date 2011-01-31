package basicblock.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;

import basicblock.BasicBlock;
import basicblock.Node;

public class DUPathFinder {

	private ASTNode def = null;
	private ASTNode use = null;

	private Set<ArrayList<BasicBlock>> paths = new HashSet<ArrayList<BasicBlock>>();
	private ArrayList<BasicBlock> back = new ArrayList<BasicBlock>();

	public DUPathFinder(ASTNode def, ASTNode use) {
		this.def = def;
		this.use = use;
	}

	public Set<ArrayList<BasicBlock>> getPaths() {
		return paths;
	}

	public void findPaths(List<BasicBlock> blocks, BasicBlock start,
			BasicBlock end) {
		back.add(start);

		BasicBlock temp = start;
		if (null != temp.getNextTrueBlock()) {
			temp = temp.getNextTrueBlock();
			if (hasASTNode(temp, this.use)) {
				ArrayList<BasicBlock> path = (ArrayList<BasicBlock>) back
						.clone();
				path.add(temp);
				paths.add(path);
			}
			if (!back.contains(temp)) {
				findPaths(blocks, temp, end);
			}
		}

		temp = start;
		if (null != temp.getNextFalseBlock()) {
			temp = temp.getNextFalseBlock();
			if (hasASTNode(temp, this.use)) {
				ArrayList<BasicBlock> path = (ArrayList<BasicBlock>) back
						.clone();
				path.add(temp);
				paths.add(path);
			}
			if (!back.contains(temp)) {
				findPaths(blocks, temp, end);
			}
		}
		back.remove(start);
	}

	private boolean hasASTNode(BasicBlock block, ASTNode astNode) {
		for (Node node : block.getContent()) {
			if (node.getAstNode() == astNode) {
				return true;
			}
		}
		return false;
	}

	private BasicBlock blockHasASTNode(List<BasicBlock> blocks, ASTNode astnode) {
		for (BasicBlock bb : blocks) {
			if (hasASTNode(bb, astnode)) {
				return bb;
			}
		}
		return null;
	}

}
