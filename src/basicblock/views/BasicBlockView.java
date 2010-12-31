package basicblock.views;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IEntityConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IGraphContentProvider;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import basicblock.BasicBlock;
import basicblock.BasicBlockVisitor;
import basicblock.Helper;

public class BasicBlockView extends ViewPart implements IZoomableWorkbenchPart {

	public static final String ID = "BasicBlock.basicblockview";

	private GraphViewer gViewer = null;

	public BasicBlockView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		List<BasicBlock> blocks = basicBlocksFromSource();

		gViewer = new GraphViewer(parent, SWT.BORDER);
		gViewer.setContentProvider(new BasicBlockContentProvider());
		gViewer.setLabelProvider(new BasicBlockLabelProvider());
		BasicBlockConnectionRelationshipProvider bbncp = new BasicBlockConnectionRelationshipProvider(
				blocks);
		gViewer.setInput(bbncp.getConnections());
		gViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		gViewer.applyLayout();
		fillToolBar();
	}

	private void fillToolBar() {
		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(
				this);
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().add(toolbarZoomContributionViewItem);
	}

	@Override
	public void setFocus() {
	}

	private List<BasicBlock> basicBlocksFromSource() {
		String source = sourceFromFile("ifelseblock3");
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

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return gViewer;
	}

}

/**
 * It is a simple relationship between two basic blocks, the main point is I
 * want to set the true/false label between two basic blocks.
 * 
 * @author chang
 * 
 */
class BBConnection {
	final String label;
	final BasicBlock source;
	final BasicBlock dest;

	public BBConnection(String label, BasicBlock source, BasicBlock destination) {
		this.label = label;
		this.source = source;
		this.dest = destination;
	}

	public String getLabel() {
		return label;
	}

	public BasicBlock getSource() {
		return source;
	}

	public BasicBlock getDestination() {
		return dest;
	}
}

class BasicBlockConnectionRelationshipProvider {
	private List<BBConnection> connections;

	// private List<BasicBlock> nodes;

	public BasicBlockConnectionRelationshipProvider(List<BasicBlock> nodes) {
		connections = new ArrayList<BBConnection>();
		// this.nodes = nodes;

		// create all relationships between all basic blocks

		if (nodes.size() == 1) {
			connections.add(new BBConnection("", nodes.get(0), null));
		} else if (nodes.size() > 1) {
			for (BasicBlock basicBlock : nodes) {
				if (basicBlock.getNextTrueBlock() != null) {
					BBConnection connect = new BBConnection("True", basicBlock,
							basicBlock.getNextTrueBlock());
					connections.add(connect);
				}
				if (basicBlock.getNextFalseBlock() != null) {
					BBConnection connect = new BBConnection("False",
							basicBlock, basicBlock.getNextFalseBlock());
					connections.add(connect);
				}
			}
		}
	}

	public List<BBConnection> getConnections() {
		return connections;
	}

	// public List<BasicBlock> getNodes() {
	// return nodes;
	// }
}

class BasicBlockContentProvider extends ArrayContentProvider implements
		IGraphContentProvider {

	@Override
	public Object getSource(Object rel) {
		if (rel instanceof BBConnection) {
			return ((BBConnection) rel).getSource();
		}
		return null;
	}

	@Override
	public Object getDestination(Object rel) {
		if (rel instanceof BBConnection) {
			return ((BBConnection) rel).getDestination();
		}
		return null;
	}
}

class BasicBlockLabelProvider extends LabelProvider implements
		IEntityConnectionStyleProvider {
	@Override
	public String getText(Object element) {
		if (element instanceof BasicBlock) {
			return "Block: " + ((BasicBlock) element).getID();
		}
		if (element instanceof BBConnection) {
			return ((BBConnection) element).getLabel();
		}
		return "";
	}

	@Override
	public int getConnectionStyle(Object src, Object dest) {
		return ZestStyles.CONNECTIONS_DIRECTED;
	}

	@Override
	public Color getColor(Object src, Object dest) {
		return null;
	}

	@Override
	public Color getHighlightColor(Object src, Object dest) {
		return null;
	}

	@Override
	public int getLineWidth(Object src, Object dest) {
		return -1;
	}

	@Override
	public IFigure getTooltip(Object entity) {
		return null;
	}
}