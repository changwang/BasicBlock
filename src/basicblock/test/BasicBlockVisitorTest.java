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
	}

	@Test
	public void ifStatement1() {
		String ifblock = contentFromFixture("ifblock1");
		ASTNode node = Helper.getParser(ifblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();
		assertEquals(2, bbv.getBlocks().size());
		assertEquals(1, bbv.getBlocks().get(0).getID());
		assertEquals(2, bbv.getBlocks().get(1).getID());
	}

	@Test
	public void ifStatement2() {
		String ifblock = contentFromFixture("ifblock2");
		ASTNode node = Helper.getParser(ifblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();
		assertEquals(2, bbv.getBlocks().size());
		assertEquals(1, bbv.getBlocks().get(0).getID());
		assertEquals(2, bbv.getBlocks().get(1).getID());
	}

	@Test
	public void ifStatement3() {
		String ifblock = contentFromFixture("ifblock3");
		ASTNode node = Helper.getParser(ifblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();
		assertEquals(3, bbv.getBlocks().size());
		assertEquals(1, bbv.getBlocks().get(0).getID());
		assertEquals(2, bbv.getBlocks().get(1).getID());
		assertEquals(3, bbv.getBlocks().get(2).getID());
	}

	@Test
	public void ifElseStatement1() {
		String ifelseblock = contentFromFixture("ifelseblock");
		ASTNode node = Helper.getParser(ifelseblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();
		assertEquals(3, bbv.getBlocks().size());
		assertEquals(1, bbv.getBlocks().get(0).getID());
		assertEquals(2, bbv.getBlocks().get(1).getID());
		assertEquals(3, bbv.getBlocks().get(2).getID());
	}

	@Test
	public void ifElseStatement2() {
		String ifelseblock = contentFromFixture("ifelseblock2");
		ASTNode node = Helper.getParser(ifelseblock).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();
		assertEquals(3, bbv.getBlocks().size());
		assertEquals(1, bbv.getBlocks().get(0).getID());
		assertEquals(2, bbv.getBlocks().get(1).getID());
		assertEquals(3, bbv.getBlocks().get(2).getID());
	}

	@Test
	public void whileStatement1() {
		String whileblock1 = contentFromFixture("whileblock1");
		ASTNode node = Helper.getParser(whileblock1).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();
		assertEquals(2, bbv.getBlocks().size());
		assertEquals(1, bbv.getBlocks().get(0).getID());
		assertEquals(2, bbv.getBlocks().get(1).getID());
	}

	@Test
	public void forStatement1() {
		String forblock1 = contentFromFixture("forblock1");
		ASTNode node = Helper.getParser(forblock1).createAST(null);
		assertNotNull(node);
		bbv.setRoot(node);
		bbv.start();
		assertEquals(2, bbv.getBlocks().size());
		assertEquals(1, bbv.getBlocks().get(0).getID());
		assertEquals(2, bbv.getBlocks().get(1).getID());
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
