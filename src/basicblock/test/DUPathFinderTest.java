package basicblock.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import basicblock.BasicBlock;
import basicblock.BasicBlockVisitor;
import basicblock.Helper;
import basicblock.algorithm.DUPathFinder;

public class DUPathFinderTest {

	private DUPathFinder finder = null;
	private BasicBlockVisitor bbv;

	@Before
	public void setUp() {
		bbv = new BasicBlockVisitor();
		String assignif1 = Helper.contentFromFixture("assignifelse");
		ASTNode node = Helper.getParser(assignif1).createAST(null);
		bbv.setRoot(node);
		bbv.start();

		List<BasicBlock> content = bbv.getBlocks();
		ASTNode def = content.get(0).getContent().get(0).getAstNode();
		ASTNode use = content.get(5).getContent().get(0).getAstNode();

		finder = new DUPathFinder(def, use);
	}

	@Test
	public void testFind() {
		finder.findPaths(bbv.getBlocks(), bbv.getBlocks().get(0), bbv
				.getBlocks().get(5));
		System.out.println(finder.getPaths());
		assertEquals(4, finder.getPaths().size());
	}

	@After
	public void tearDown() {
	}
}
