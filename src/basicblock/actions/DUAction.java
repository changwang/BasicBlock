package basicblock.actions;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

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
			ASTNode root = Helper.getParser(selectedSource).createAST(null);

			DUVisitor visitor = new DUVisitor();
			root.accept(visitor);

			System.out.println(visitor.showDefs());
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
	}

}
