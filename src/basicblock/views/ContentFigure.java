package basicblock.views;

import java.util.List;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.swt.graphics.Color;

import basicblock.Node;

public class ContentFigure extends Figure {

	private static Color backgroundColor = new Color(null, 255, 255, 206);

	public ContentFigure(List<Node> nodes) {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(false);
		layout.setSpacing(2);
		setLayoutManager(layout);
		setBackgroundColor(backgroundColor);
		setOpaque(true);

		for (Node n : nodes) {
			ASTNode astNode = n.getAstNode();
			switch (astNode.getNodeType()) {
			case ASTNode.IF_STATEMENT:
				add(new StatementFigure("IF statement with expression: "
						+ ((IfStatement) astNode).getExpression() + "\n"));
				break;
			case ASTNode.WHILE_STATEMENT:
				add(new StatementFigure("WHILE statement with expression: "
						+ ((WhileStatement) astNode).getExpression() + "\n"));
				break;
			case ASTNode.DO_STATEMENT:
				add(new StatementFigure("DO WHILE statement with expression: "
						+ ((DoStatement) astNode).getExpression() + "\n"));
				break;
			case ASTNode.FOR_STATEMENT:
				add(new StatementFigure("FOR statement with expression: "
						+ ((ForStatement) astNode).getExpression() + "\n"));
				break;
			case ASTNode.ENHANCED_FOR_STATEMENT:
				add(new StatementFigure("IF statement with expression: "
						+ ((EnhancedForStatement) astNode).getExpression()
						+ "\n"));
				break;
			default:
				add(new StatementFigure("Statement: " + astNode.toString()));
			}
		}
	}
}

class StatementFigure extends Figure {
	public StatementFigure(String content) {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(false);
		layout.setSpacing(3);
		setLayoutManager(layout);
		setBorder(new StatementFigureBorder());
		add(new Label(content));
	}

	public class StatementFigureBorder extends AbstractBorder {

		@Override
		public Insets getInsets(IFigure figure) {
			return new Insets(1, 0, 0, 0);
		}

		@Override
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(),
					tempRect.getTopRight());
		}
	}
}