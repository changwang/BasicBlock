package basicblock.actions;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import basicblock.Activator;
import basicblock.DUVisitor;
import basicblock.Helper;

public class DUAction implements IEditorActionDelegate {

	ISelection selection;

	public DUAction() {
		super();
	}

	@Override
	public void run(IAction action) {
		if (selection instanceof ITextSelection) {
			String selectedSource = ((ITextSelection) selection).getText();

			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setResolveBindings(true);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setProject(Helper.getProject());
			parser.setSource(selectedSource.toCharArray());
			parser.setUnitName(getCompilationUnitPath());
			ASTNode root = parser.createAST(null);

			DUVisitor visitor = new DUVisitor();
			root.accept(visitor);

			System.out.println(visitor.showDefs());
		}
	}

	private String getCompilationUnitPath() {
		IEditorPart editorPart = Activator.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		return "/" + editorPart.getTitleToolTip();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
	}

}
