package basicblock.test;

import static org.junit.Assert.assertNotNull;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.Before;
import org.junit.Test;

import basicblock.DUVisitor;
import basicblock.Helper;

public class DUVisitorTest {

	private DUVisitor visitor;

	@Before
	public void setUp() {
		visitor = new DUVisitor();
	}

	@Test
	public void finddefs() {
		String assign = Helper.contentFromFixture("dutest");
		ASTNode node = Helper.getParser(assign).createAST(null);
		assertNotNull(node);
		node.accept(visitor);
		System.out.println(visitor.showDefs());
	}
}
