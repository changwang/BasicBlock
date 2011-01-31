package basicblock;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
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
		// parser.setProject(getProject());
		parser.setSource(source.toCharArray());
		parser.setResolveBindings(true);
		return parser;
	}

	public static IJavaProject getProject() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject[] projects = root.getProjects();
		for (IProject project : projects) {
			try {
				if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
					IJavaProject javaProject = JavaCore.create(project);
					return javaProject;
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}

		}
		return null;
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

	public static String contentFromFixture(String fixtureName) {
		BufferedReader br;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader(
					"/Users/chang/eclipse/workspace/BasicBlock/src/fixtures/"
							+ fixtureName));

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
