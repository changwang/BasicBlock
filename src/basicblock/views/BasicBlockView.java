package basicblock.views;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import basicblock.BasicBlock;
import basicblock.BasicBlockVisitor;
import basicblock.Helper;

public class BasicBlockView extends ViewPart {

	public static final String ID = "BasicBlock.basicblockview";

	public BasicBlockView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Graph graph = new Graph(parent, SWT.NONE);
		List<BasicBlock> blocks = basicBlocksFromSource();
		List<GraphNode> nodes = new ArrayList<GraphNode>();
		for (BasicBlock basicBlock : blocks) {
			GraphNode node = new GraphNode(graph, SWT.NONE);
			node.setText("Node: " + basicBlock.getID());
			node.setTooltip(new ContentFigure(basicBlock.getContent()));
			nodes.add(node);
		}

		for (BasicBlock bb : blocks) {
			if (bb.getNextTrueBlock() != null) {
				GraphConnection gc = new GraphConnection(graph,
						ZestStyles.CONNECTIONS_DIRECTED,
						nodes.get(bb.getID() - 1), nodes.get(bb
								.getNextTrueBlock().getID() - 1));
				gc.setText("True");
			}
			if (bb.getNextFalseBlock() != null) {
				GraphConnection gc = new GraphConnection(graph,
						ZestStyles.CONNECTIONS_DIRECTED,
						nodes.get(bb.getID() - 1), nodes.get(bb
								.getNextFalseBlock().getID() - 1));
				gc.setText("False");
			}
		}

		graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
	}

	@Override
	public void setFocus() {
	}

	private List<BasicBlock> basicBlocksFromSource() {
		String source = sourceFromFile("mixblock2");
		ASTNode root = (Statement) Helper.getParser(source).createAST(null);
		BasicBlockVisitor bbv = new BasicBlockVisitor();
		bbv.setRoot(root);
		bbv.start();
		return bbv.getBlocks();
	}

	private String sourceFromFile(String filename) {
		BufferedReader br;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader(
					"/Users/chang/eclipse/workspace/BasicBlock/src/fixtures/"
							+ filename));

			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
