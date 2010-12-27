package basicblock.action;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import basicblock.BasicBlock;
import basicblock.BasicBlockVisitor;
import basicblock.Helper;

public class FindBlockAction implements IEditorActionDelegate {

	ISelection selection;

	public FindBlockAction() {
		super();
	}

	@Override
	public void run(IAction action) {
		if (selection instanceof ITextSelection) {
			String selectedSource = ((ITextSelection) selection).getText();
			ASTNode root = (Statement) Helper.getParser(selectedSource)
					.createAST(null);

			char[] cs = { (char) 123, (char) 10, (char) 125, (char) 10 };
			char[] ts = root.toString().toCharArray();
			if (Arrays.equals(ts, cs)) {
				System.out
						.println("There is no block or the source code could not be parsed.");
				return;
			}

			BasicBlockVisitor bbv = new BasicBlockVisitor();
			bbv.setRoot(root);
			bbv.start();
			basicBlockPrinter(bbv.getBlocks());
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {

	}

	private void basicBlockPrinter(List<BasicBlock> blocks) {
		int bindex = 0;
		for (Iterator<BasicBlock> iter = blocks.iterator(); iter.hasNext();) {
			String title = "=========== Block " + (++bindex) + " ===========";
			System.out.println(title);
			System.out.print(iter.next());
			System.out.println("===============================");
			System.out.println();
		}
	}

}
