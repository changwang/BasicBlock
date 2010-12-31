package basicblock.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IEntityConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.viewers.IGraphContentProvider;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import basicblock.BasicBlock;
import basicblock.BasicBlockVisitor;
import basicblock.Helper;

public class BasicBlockView extends ViewPart implements IZoomableWorkbenchPart {

	public static final String ID = "BasicBlock.basicblockview";

	private GraphViewer gViewer = null;
	private List<BasicBlock> blocks = null;
	private GraphNode errorNode = null;

	public BasicBlockView() {
	}

	private ISelectionListener listener = new ISelectionListener() {

		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (part != BasicBlockView.this
					&& selection instanceof ITextSelection) {
				ITextSelection ts = (ITextSelection) selection;
				if (ts.getText().length() > 0) {
					blocks = basicBlocksFromSource(ts.getText());
					drawHierarchy(blocks);
				}
			}
		}
	};

	@Override
	public void createPartControl(Composite parent) {

		gViewer = new GraphViewer(parent, SWT.BORDER);

		gViewer.setContentProvider(new BasicBlockContentProvider());
		gViewer.setLabelProvider(new BasicBlockLabelProvider());
		gViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		gViewer.applyLayout();

		fillToolBar();

		getSite().getWorkbenchWindow().getSelectionService()
				.addPostSelectionListener(listener);
	}

	private void drawHierarchy(List<BasicBlock> blocks) {
		BasicBlockConnectionRelationshipProvider bbncp = new BasicBlockConnectionRelationshipProvider(
				blocks);
		if (errorNode != null) {
			errorNode.dispose();
		}
		if (bbncp.getConnections().size() == 0) {
			// no connection in it
			gViewer.setInput(null);
			errorNode = new GraphNode(gViewer.getGraphControl(), SWT.NONE);
			errorNode.setText("Selected source code cannot be parsed");
		} else {
			gViewer.setInput(bbncp.getConnections());
		}

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

	private List<BasicBlock> basicBlocksFromSource(String source) {
		ASTNode root = (Statement) Helper.getParser(source).createAST(null);
		BasicBlockVisitor bbv = new BasicBlockVisitor();
		bbv.setRoot(root);
		bbv.start();
		return bbv.getBlocks();
	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return gViewer;
	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService()
				.removePostSelectionListener(listener);
		super.dispose();
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

		// create all relationships between all basic blocks
		if (nodes.size() == 1) {
			// if the source code cannot be parsed, there is an empty basic
			// block with no node in it
			BasicBlock bb = nodes.get(0);
			if (bb.getContent().size() != 0) {
				connections.add(new BBConnection("", nodes.get(0), null));
			}

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
		IEntityConnectionStyleProvider, IEntityStyleProvider {
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
		if (entity instanceof BasicBlock) {
			return new ContentFigure(((BasicBlock) entity).getContent());
		}
		return null;
	}

	@Override
	public Color getNodeHighlightColor(Object entity) {
		return null;
	}

	@Override
	public Color getBorderColor(Object entity) {
		return null;
	}

	@Override
	public Color getBorderHighlightColor(Object entity) {
		return null;
	}

	@Override
	public int getBorderWidth(Object entity) {
		return 0;
	}

	@Override
	public Color getBackgroundColour(Object entity) {
		return null;
	}

	@Override
	public Color getForegroundColour(Object entity) {
		return null;
	}

	@Override
	public boolean fisheyeNode(Object entity) {
		return false;
	}
}