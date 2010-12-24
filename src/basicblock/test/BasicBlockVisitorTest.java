package basicblock.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import basicblock.BasicBlockVisitor;
import basicblock.Helper;

public class BasicBlockVisitorTest {
	private BasicBlockVisitor bbv;

	@Before
	public void setUp() throws Exception {
		bbv = new BasicBlockVisitor();
	}

	@Test
	public void assignmentBlock() {
		String assignmentBlock = contentFromFixture("assignmentBlock");
		ASTNode node = Helper.getParser(assignmentBlock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(1, bbv.getBlocks().size());

		assertEquals(1, bbv.getBlocks().get(0).getID());
		assertEquals(3, bbv.getBlocks().get(0).getContent().size());
		assertNull(bbv.getBlocks().get(0).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

	}

	@Test
	public void assignIf() {
		String assignif1 = contentFromFixture("assignif");
		ASTNode node = Helper.getParser(assignif1).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(3, bbv.getBlocks().size());

		assertEquals(1, bbv.getBlocks().get(0).getID());
		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertEquals(bbv.getBlocks().get(0).getNextFalseBlock(), bbv
				.getBlocks().get(2));

		assertEquals(2, bbv.getBlocks().get(1).getID());
		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertNull(bbv.getBlocks().get(1).getNextFalseBlock());

		assertEquals(3, bbv.getBlocks().get(2).getID());
		assertNull(bbv.getBlocks().get(2).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());

	}

	@Test
	public void assignIfElse() {
		String assignifelse = contentFromFixture("assignifelse");
		ASTNode node = Helper.getParser(assignifelse).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(8, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertEquals(bbv.getBlocks().get(0).getNextFalseBlock(), bbv
				.getBlocks().get(4));

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertEquals(bbv.getBlocks().get(1).getNextFalseBlock(), bbv
				.getBlocks().get(3));

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(7));
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(3).getNextTrueBlock(), bbv.getBlocks()
				.get(7));
		assertNull(bbv.getBlocks().get(3).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(4).getNextTrueBlock(), bbv.getBlocks()
				.get(5));
		assertEquals(bbv.getBlocks().get(4).getNextFalseBlock(), bbv
				.getBlocks().get(6));

		assertEquals(bbv.getBlocks().get(5).getNextTrueBlock(), bbv.getBlocks()
				.get(7));
		assertNull(bbv.getBlocks().get(5).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(6).getNextTrueBlock(), bbv.getBlocks()
				.get(7));
		assertNull(bbv.getBlocks().get(6).getNextFalseBlock());
	}

	@Test
	public void ifStatement1() {
		String ifblock = contentFromFixture("ifblock1");
		ASTNode node = Helper.getParser(ifblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(2, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(1).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(1).getNextFalseBlock());
	}

	@Test
	public void ifStatement2() {
		String ifblock = contentFromFixture("ifblock2");
		ASTNode node = Helper.getParser(ifblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(2, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(1).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(1).getNextFalseBlock());
	}

	@Test
	public void ifStatement3() {
		String ifblock = contentFromFixture("ifblock3");
		ASTNode node = Helper.getParser(ifblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(3, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertNull(bbv.getBlocks().get(1).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(2).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());
	}

	@Test
	public void ifStatement4() {
		String ifblock = contentFromFixture("ifblock4");
		ASTNode node = Helper.getParser(ifblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(5, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertEquals(bbv.getBlocks().get(0).getNextFalseBlock(), bbv
				.getBlocks().get(2));

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertNull(bbv.getBlocks().get(1).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(3));
		assertEquals(bbv.getBlocks().get(2).getNextFalseBlock(), bbv
				.getBlocks().get(4));

		assertEquals(bbv.getBlocks().get(3).getNextTrueBlock(), bbv.getBlocks()
				.get(4));
		assertNull(bbv.getBlocks().get(3).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(4).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(4).getNextFalseBlock());
	}

	@Test
	public void ifStatement5() {
		String ifblock = contentFromFixture("ifblock5");
		ASTNode node = Helper.getParser(ifblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(3, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertNull(bbv.getBlocks().get(1).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(2).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());
	}

	@Test
	public void ifStatement6() {
		String ifblock = contentFromFixture("ifblock6");
		ASTNode node = Helper.getParser(ifblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(4, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertEquals(bbv.getBlocks().get(1).getNextFalseBlock(), bbv
				.getBlocks().get(3));

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(3));
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(3).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(3).getNextFalseBlock());
	}

	@Test
	public void ifStatement7() {
		String ifblock = contentFromFixture("ifblock7");
		ASTNode node = Helper.getParser(ifblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(4, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertEquals(bbv.getBlocks().get(1).getNextFalseBlock(), bbv
				.getBlocks().get(3));

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(3));
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(3).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(3).getNextFalseBlock());
	}

	@Test
	public void ifElseStatement1() {
		String ifelseblock = contentFromFixture("ifelseblock");
		ASTNode node = Helper.getParser(ifelseblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(3, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertEquals(bbv.getBlocks().get(0).getNextFalseBlock(), bbv
				.getBlocks().get(2));

		assertNull(bbv.getBlocks().get(1).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(1).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(2).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());
	}

	@Test
	public void ifElseStatement2() {
		String ifelseblock = contentFromFixture("ifelseblock2");
		ASTNode node = Helper.getParser(ifelseblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(3, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertEquals(bbv.getBlocks().get(0).getNextFalseBlock(), bbv
				.getBlocks().get(2));

		assertNull(bbv.getBlocks().get(1).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(1).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(2).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());
	}

	@Test
	public void ifElseStatement3() {
		String ifelseblock = contentFromFixture("ifelseblock3");
		ASTNode node = Helper.getParser(ifelseblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(9, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertEquals(bbv.getBlocks().get(0).getNextFalseBlock(), bbv
				.getBlocks().get(5));

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertEquals(bbv.getBlocks().get(1).getNextFalseBlock(), bbv
				.getBlocks().get(3));

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(4));
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(3).getNextTrueBlock(), bbv.getBlocks()
				.get(4));
		assertNull(bbv.getBlocks().get(3).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(4).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(4).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(5).getNextTrueBlock(), bbv.getBlocks()
				.get(6));
		assertEquals(bbv.getBlocks().get(5).getNextFalseBlock(), bbv
				.getBlocks().get(7));

		assertEquals(bbv.getBlocks().get(6).getNextTrueBlock(), bbv.getBlocks()
				.get(8));
		assertNull(bbv.getBlocks().get(6).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(7).getNextTrueBlock(), bbv.getBlocks()
				.get(8));
		assertNull(bbv.getBlocks().get(7).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(8).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(8).getNextFalseBlock());
	}

	@Test
	public void whileStatement1() {
		String whileblock1 = contentFromFixture("whileblock1");
		ASTNode node = Helper.getParser(whileblock1).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(5, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertEquals(bbv.getBlocks().get(1).getNextFalseBlock(), bbv
				.getBlocks().get(0));

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(3));
		assertEquals(bbv.getBlocks().get(2).getNextFalseBlock(), bbv
				.getBlocks().get(1));

		assertEquals(bbv.getBlocks().get(3).getNextTrueBlock(), bbv.getBlocks()
				.get(4));
		assertEquals(bbv.getBlocks().get(3).getNextFalseBlock(), bbv
				.getBlocks().get(2));

		assertEquals(bbv.getBlocks().get(4).getNextTrueBlock(), bbv.getBlocks()
				.get(3));
		assertNull(bbv.getBlocks().get(4).getNextFalseBlock());
	}

	@Test
	public void whileStatement2() {
		String whileblock2 = contentFromFixture("whileblock2");
		ASTNode node = Helper.getParser(whileblock2).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(3, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertEquals(bbv.getBlocks().get(1).getNextFalseBlock(), bbv
				.getBlocks().get(0));

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());
	}

	@Test
	public void whileStatement3() {
		String whileblock3 = contentFromFixture("whileblock3");
		ASTNode node = Helper.getParser(whileblock3).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(4, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertEquals(bbv.getBlocks().get(1).getNextFalseBlock(), bbv
				.getBlocks().get(3));

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(3).getNextTrueBlock(), bbv.getBlocks()
				.get(0));
		assertNull(bbv.getBlocks().get(3).getNextFalseBlock());
	}

	@Test
	public void whileStatement4() {
		String whileblock4 = contentFromFixture("whileblock4");
		ASTNode node = Helper.getParser(whileblock4).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(6, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertEquals(bbv.getBlocks().get(1).getNextFalseBlock(), bbv
				.getBlocks().get(5));

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(3));
		assertEquals(bbv.getBlocks().get(2).getNextFalseBlock(), bbv
				.getBlocks().get(4));

		assertEquals(bbv.getBlocks().get(3).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertNull(bbv.getBlocks().get(3).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(4).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(5).getNextTrueBlock(), bbv.getBlocks()
				.get(0));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());
	}

	@Test
	public void forStatement1() {
		String forblock1 = contentFromFixture("forblock1");
		ASTNode node = Helper.getParser(forblock1).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(2, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(0));
		assertNull(bbv.getBlocks().get(1).getNextFalseBlock());
	}

	@Test
	public void forStatement2() {
		String forblock2 = contentFromFixture("forblock2");
		ASTNode node = Helper.getParser(forblock2).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(5, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertEquals(bbv.getBlocks().get(1).getNextFalseBlock(), bbv
				.getBlocks().get(3));

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(3).getNextTrueBlock(), bbv.getBlocks()
				.get(4));
		assertEquals(bbv.getBlocks().get(3).getNextFalseBlock(), bbv
				.getBlocks().get(0));

		assertEquals(bbv.getBlocks().get(4).getNextTrueBlock(), bbv.getBlocks()
				.get(3));
		assertNull(bbv.getBlocks().get(4).getNextFalseBlock());
	}

	@Test
	public void mix1() {
		String mix1 = contentFromFixture("mixblock1");
		ASTNode node = Helper.getParser(mix1).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(5, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(0).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertEquals(bbv.getBlocks().get(1).getNextFalseBlock(), bbv
				.getBlocks().get(3));

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertNull(bbv.getBlocks().get(2).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(3).getNextTrueBlock(), bbv.getBlocks()
				.get(4));
		assertNull(bbv.getBlocks().get(3).getNextFalseBlock());
	}

	@Test
	public void mix2() {
		String mix2 = contentFromFixture("mixblock2");
		ASTNode node = Helper.getParser(mix2).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();

		assertEquals(12, bbv.getBlocks().size());

		assertEquals(bbv.getBlocks().get(0).getNextTrueBlock(), bbv.getBlocks()
				.get(1));
		assertEquals(bbv.getBlocks().get(0).getNextFalseBlock(), bbv
				.getBlocks().get(6));

		assertEquals(bbv.getBlocks().get(1).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertEquals(bbv.getBlocks().get(1).getNextFalseBlock(), bbv
				.getBlocks().get(11));

		assertEquals(bbv.getBlocks().get(2).getNextTrueBlock(), bbv.getBlocks()
				.get(3));
		assertEquals(bbv.getBlocks().get(2).getNextFalseBlock(), bbv
				.getBlocks().get(1));

		assertEquals(bbv.getBlocks().get(3).getNextTrueBlock(), bbv.getBlocks()
				.get(4));
		assertEquals(bbv.getBlocks().get(3).getNextFalseBlock(), bbv
				.getBlocks().get(5));

		assertEquals(bbv.getBlocks().get(4).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertNull(bbv.getBlocks().get(4).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(5).getNextTrueBlock(), bbv.getBlocks()
				.get(2));
		assertNull(bbv.getBlocks().get(5).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(6).getNextTrueBlock(), bbv.getBlocks()
				.get(7));
		assertEquals(bbv.getBlocks().get(6).getNextFalseBlock(), bbv
				.getBlocks().get(11));

		assertEquals(bbv.getBlocks().get(7).getNextTrueBlock(), bbv.getBlocks()
				.get(8));
		assertEquals(bbv.getBlocks().get(7).getNextFalseBlock(), bbv
				.getBlocks().get(6));

		assertEquals(bbv.getBlocks().get(8).getNextTrueBlock(), bbv.getBlocks()
				.get(9));
		assertEquals(bbv.getBlocks().get(8).getNextFalseBlock(), bbv
				.getBlocks().get(10));

		assertEquals(bbv.getBlocks().get(9).getNextTrueBlock(), bbv.getBlocks()
				.get(7));
		assertNull(bbv.getBlocks().get(9).getNextFalseBlock());

		assertEquals(bbv.getBlocks().get(10).getNextTrueBlock(), bbv
				.getBlocks().get(7));
		assertNull(bbv.getBlocks().get(10).getNextFalseBlock());

		assertNull(bbv.getBlocks().get(11).getNextTrueBlock());
		assertNull(bbv.getBlocks().get(11).getNextFalseBlock());
	}

	@After
	public void tearDown() throws Exception {
	}

	private String contentFromFixture(String fixtureName) {
		BufferedReader br;
		StringBuilder sb = new StringBuilder();
		try {
			// br = new BufferedReader(new FileReader(
			// "C:\\eclipse\\workspace\\edu.wmich.basicblock\\src\\fixtures\\"
			// + fixtureName));

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
