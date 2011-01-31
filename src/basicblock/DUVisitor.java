package basicblock;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

public class DUVisitor extends ASTVisitor {

	Set<ASTNode> defs = new HashSet<ASTNode>();
	Set<ASTNode> uses = new HashSet<ASTNode>();

	enum DU {
		DEF, USE
	};

	public boolean visit(Assignment astnode) {
		ASTNode def = findDefOrUse(astnode, DU.DEF);
		if (null != def) {
			defs.add(def);
		}

		ASTNode use = findDefOrUse(astnode, DU.USE);
		if (null != use) {
			uses.add(use);
		}
		return false;
	}

	private ASTNode findDefOrUse(Assignment astnode, DU du) {

		if (du == DU.DEF) {
			Expression lhs = astnode.getLeftHandSide();

			// if left hand side isn't a simple name, it can't be a local
			// variable
			if (lhs.getNodeType() != ASTNode.SIMPLE_NAME)
				return null;

			SimpleName name = (SimpleName) lhs;
			IBinding nameBinding = name.resolveBinding();

			// if the binding cannot be resolved, ignore it
			if (null == nameBinding)
				return null;

			// if name isn't a variable reference, ignore it
			if (nameBinding.getKind() != IBinding.VARIABLE)
				return null;

			IVariableBinding varBinding = (IVariableBinding) nameBinding;
			// if variable reference refers to a field, ignore it
			if (varBinding.isField())
				return null;

			return astnode;
		} else if (du == DU.USE) {
			Expression rhs = astnode.getRightHandSide();

			// if left hand side isn't a simple name, it can't be a local
			// variable
			if (rhs.getNodeType() != ASTNode.SIMPLE_NAME)
				return null;

			SimpleName name = (SimpleName) rhs;
			IBinding nameBinding = name.resolveBinding();

			// if the binding cannot be resolved, ignore it
			if (null == nameBinding)
				return null;

			// if name isn't a variable reference, ignore it
			if (nameBinding.getKind() != IBinding.VARIABLE)
				return null;

			IVariableBinding varBinding = (IVariableBinding) nameBinding;
			// if variable reference refers to a field, ignore it
			if (varBinding.isField())
				return null;

			return astnode;
		} else {
			return null;
		}
	}

	public boolean visit(ForStatement astnode) {
		return false;
	}

	public String showDefs() {
		return defs.toString();
	}
}
