package basicblock;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;

public class Helper {

	// won't let user create any instance
	private Helper() {
	}

	/**
	 * create the parser based on the given source code.
	 * 
	 * @param source
	 *            the given source code
	 * @return
	 */
	public static ASTParser getParser(String source) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setSource(source.toCharArray());
		parser.setResolveBindings(true);
		return parser;
	}

	/**
	 * whether the AST node is a control node or not
	 * 
	 * @param node
	 *            current visiting node
	 * @return true if it is a control node, otherwise false
	 */
	public static boolean isControlNode(ASTNode node) {
		switch (node.getNodeType()) {
		case ASTNode.IF_STATEMENT:
		case ASTNode.DO_STATEMENT:
		case ASTNode.WHILE_STATEMENT:
		case ASTNode.FOR_STATEMENT:
		case ASTNode.ENHANCED_FOR_STATEMENT:
		case ASTNode.SWITCH_STATEMENT:
		case ASTNode.SWITCH_CASE:
		case ASTNode.TRY_STATEMENT:
		case ASTNode.CATCH_CLAUSE:
		case ASTNode.CONTINUE_STATEMENT:
		case ASTNode.BREAK_STATEMENT:
			return true;
		default:
			return false;
		}
	}

	/**
	 * whether the AST node is a loop control node or not
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isLoopControlNode(ASTNode node) {
		switch (node.getNodeType()) {
		case ASTNode.DO_STATEMENT:
		case ASTNode.WHILE_STATEMENT:
		case ASTNode.FOR_STATEMENT:
		case ASTNode.ENHANCED_FOR_STATEMENT:
			return true;
		default:
			return false;
		}
	}
}
