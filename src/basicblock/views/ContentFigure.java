package basicblock.views;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Color;

import basicblock.Node;

public class ContentFigure extends Figure {

	// TODO: change the background color and add border line to each node
	private static Color backgroundColor = new Color(null, 111, 123, 134);

	public ContentFigure(List<Node> nodes) {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(false);
		layout.setSpacing(2);
		setLayoutManager(layout);
		setBackgroundColor(backgroundColor);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setOpaque(true);

		for (Node n : nodes) {
			add(new Label(n.getAstNode().toString()));
		}

	}
}
