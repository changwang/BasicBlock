package basicblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * The visitor class used to find all defs and uses.
 */
public class DUVisitor extends ASTVisitor {

	/*
	 * use array to hold defs, duplication is tolerant, because later def will
	 * over lay previous def, and the order is meaningful.
	 */
	List<DUNode> defs = new ArrayList<DUNode>();

	/*
	 * use map to hold the def/use relationship, the key is the index of def
	 * which is in the defs array, the value is the actual use node
	 */
	Map<Integer, DUNode> uses = new HashMap<Integer, DUNode>();

	/**
	 * So far, we only focus on finding def and use.
	 */
	enum DU {
		DEF, USE
	};

	/**
	 * visits the assignment ( Expression AssignmentOperator Expression )
	 */
	public boolean visit(Assignment astnode) {
		SimpleName def = findDefOrUse(astnode, DU.DEF);
		/*
		 * if it contains a def, simply put it into the defs array.
		 */
		if (null != def) {
			defs.add(new DUNode(astnode, def));
		}

		/*
		 * if astnode contains a use, we will reversely search the defs, because
		 * the lastest should be its def.
		 */
		SimpleName use = findDefOrUse(astnode, DU.USE);
		if (null != use) {
			setAssociatedDefAndUse(astnode, use);
		}
		return false;
	}

	/**
	 * reversely find the def which a use is associated with, because the later
	 * one overlaies previous one.
	 * 
	 * @param parentNode
	 *            the astnode contains the use
	 * @param useName
	 */
	private void setAssociatedDefAndUse(ASTNode parentNode, SimpleName useName) {
		List<DUNode> defsCopy = new ArrayList<DUNode>(defs);
		Collections.reverse(defsCopy);
		for (int i = 0; i < defsCopy.size(); i++) {
			SimpleName name = defsCopy.get(i).getDU();
			if (name.resolveBinding().getName() == useName.resolveBinding()
					.getName()) {
				uses.put(i, new DUNode(parentNode, useName));
			}
		}
	}

	/**
	 * visit the prefix expression ( PrefixOperator Expression ), like ++i, --j.
	 * In this case, we know that the operand could be a use.
	 * 
	 * @param astnode
	 * @return
	 */
	private SimpleName findFromPrefixExpression(PrefixExpression astnode) {
		Expression operand = astnode.getOperand();
		if (operand.getNodeType() == ASTNode.SIMPLE_NAME
				&& couldFindBinding((SimpleName) operand))
			return (SimpleName) operand;
		else
			return null;
	}

	/**
	 * visit the postfix expression (Expression PostfixOperator), like i++, j--.
	 * In this case, we know that the operand could be a use.
	 * 
	 * @param astnode
	 * @return
	 */
	private SimpleName findFromPostfixExpression(PostfixExpression astnode) {
		Expression operand = astnode.getOperand();
		if (operand.getNodeType() == ASTNode.SIMPLE_NAME
				&& couldFindBinding((SimpleName) operand))
			return (SimpleName) operand;
		else
			return null;
	}

	/**
	 * find the def and use from assignment.
	 * 
	 * @param astnode
	 * @param du
	 *            def or use
	 * @return
	 */
	private SimpleName findFromAssignment(Assignment astnode, DU du) {
		Expression exp = null;
		switch (du) {
		case DEF:
			exp = astnode.getLeftHandSide();
			break;
		case USE:
			exp = astnode.getRightHandSide();
			break;
		}

		if (null == exp)
			return null;

		// if left hand side isn't a simple name, it can't be a local
		// variable
		if (exp.getNodeType() != ASTNode.SIMPLE_NAME)
			return null;

		SimpleName name = (SimpleName) exp;
		if (couldFindBinding(name))
			return name;
		return null;
	}

	/**
	 * whether the binding information could be solved, if not, it is not a def
	 * or use.
	 * 
	 * @param astnode
	 * @return
	 */
	private boolean couldFindBinding(SimpleName astnode) {
		IBinding nameBinding = astnode.resolveBinding();

		// if the binding cannot be resolved, ignore it
		if (null == nameBinding)
			return false;

		// if name isn't a variable reference, ignore it
		if (nameBinding.getKind() != IBinding.VARIABLE)
			return false;

		// if variable reference refers to a field, ignore it
		IVariableBinding varBinding = (IVariableBinding) nameBinding;
		if (varBinding.isField())
			return false;
		return true;
	}

	/**
	 * find the def or use from given ASTNode
	 * 
	 * @param astnode
	 * @param du
	 * @return
	 */
	private SimpleName findDefOrUse(ASTNode astnode, DU du) {
		switch (astnode.getNodeType()) {
		case ASTNode.ASSIGNMENT:
			return findFromAssignment((Assignment) astnode, du);
		case ASTNode.PREFIX_EXPRESSION:
			return findFromPrefixExpression((PrefixExpression) astnode);
		case ASTNode.POSTFIX_EXPRESSION:
			return findFromPostfixExpression((PostfixExpression) astnode);
		default:
			return null;
		}
	}

	/**
	 * visit the for statement ( for([ForInit]; [Expression]; [ForUpdate]) ) in
	 * ForInit, normally the expression is a def, in ForUpdate, the expression
	 * could be a use.
	 */
	public boolean visit(ForStatement astnode) {
		/*
		 * find defs from initializer
		 */
		List<Expression> inits = astnode.initializers();
		for (Expression init : inits) {
			SimpleName defName = findDefOrUse(init, DU.DEF);
			if (null != defName) {
				defs.add(new DUNode(astnode, defName));
			}
		}

		/*
		 * find uses from updater
		 */
		List<Expression> updaters = astnode.updaters();
		for (Expression updater : updaters) {
			SimpleName useName = findDefOrUse(updater, DU.USE);
			if (null != useName) {
				setAssociatedDefAndUse(astnode, useName);
			}
		}
		return true;
	}

	/**
	 * print out the defs array.
	 * 
	 * @return
	 */
	public String showDefs() {
		return defs.toString();
	}
}
