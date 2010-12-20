package poly_break.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import poly_break.algorithm.TriangulationAlgorithm;
import poly_break.algorithm.log.LogFrame;
import poly_break.model.Polygon;
import poly_break.ui.tools.BreakSegmentTool;
import poly_break.ui.tools.DeleteTool;
import poly_break.ui.tools.MoveTool;
import poly_break.ui.tools.NewTriangleTool;
import poly_break.ui.tools.Tool;

public class Main extends JFrame {
	private static final long serialVersionUID = 8832612085314033604L;

	private PolyDrawPanel polyDraw = new PolyDrawPanel();

	private JToolBar toolBar = new JToolBar();

	private JTextField fileNameField = new JTextField("1");

	private Action loadAction = new AbstractAction("Load") {
		private static final long serialVersionUID = 2766951600881568330L;

		public void actionPerformed(ActionEvent e) {
			String text = fileNameField.getText();
			try {
				Polygon poly = Polygon.load(new File("tests/data" + text
						+ ".txt"));
				polyDraw.setModel(poly);
				polyDraw.repaint();
			} catch (Throwable e1) {
				JOptionPane.showMessageDialog(Main.this, e1
						.getLocalizedMessage(), "Load error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	private Action saveAction = new AbstractAction("Save") {
		private static final long serialVersionUID = -1889708556563062997L;

		public void actionPerformed(ActionEvent e) {
			String text = fileNameField.getText();
			try {
				polyDraw.getModel()
						.save(new File("tests/data" + text + ".txt"));
			} catch (Throwable e1) {
				JOptionPane.showMessageDialog(Main.this, e1
						.getLocalizedMessage(), "Save error",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	};

	private Action newTriangleToolAction = new AbstractAction("New triangle") {
		private static final long serialVersionUID = -1542266458910687741L;

		public void actionPerformed(ActionEvent e) {
			changeTool(new NewTriangleTool(polyDraw));
		}
	};

	private Action breakSegmentToolAction = new AbstractAction("Break segment") {
		private static final long serialVersionUID = -8974516556658223519L;

		public void actionPerformed(ActionEvent e) {
			changeTool(new BreakSegmentTool(polyDraw));
		}
	};

	private Action moveToolAction = new AbstractAction("Move") {
		private static final long serialVersionUID = -1542266458910687741L;

		public void actionPerformed(ActionEvent e) {
			changeTool(new MoveTool(polyDraw));
		}
	};

	private Action deleteToolAction = new AbstractAction("Delete") {
		private static final long serialVersionUID = 6110668956065173038L;

		public void actionPerformed(ActionEvent e) {
			changeTool(new DeleteTool(polyDraw));
		}
	};

	private Action triangulateAction = new AbstractAction("Triangulate") {
		private static final long serialVersionUID = 0L;

		public void actionPerformed(ActionEvent e) {
			TriangulationAlgorithm alg = new TriangulationAlgorithm(polyDraw.getModel());
			LogFrame fr = LogFrame.openFrame();
			alg.setLog(fr.getLog());
			alg.compute();			
		}
		
	};

	public Main() {
		super("Polygon editor");
		init();
	}

	private void init() {
		try {
			polyDraw.setModel(Polygon.load(new File("tests/data6.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		polyDraw.setSize(500, 500);
		getContentPane().add(polyDraw);

		toolBar.add(fileNameField);

		JButton loadButton = new JButton(loadAction);
		loadButton.setMnemonic(KeyEvent.VK_F3);

		JButton saveButton = new JButton(saveAction);
		loadButton.setMnemonic(KeyEvent.VK_F2);

		JRadioButton newTriangleButton = new JRadioButton(newTriangleToolAction);
		JRadioButton breakSegmentButton = new JRadioButton(
				breakSegmentToolAction);
		JRadioButton moveToolButton = new JRadioButton(moveToolAction);
		JRadioButton deleteToolButton = new JRadioButton(deleteToolAction);
		
		JButton triangulateButton = new JButton(triangulateAction); 

		ButtonGroup toolGroup = new ButtonGroup();
		toolGroup.add(newTriangleButton);
		toolGroup.add(breakSegmentButton);
		toolGroup.add(moveToolButton);
		toolGroup.add(deleteToolButton);
		
		newTriangleButton.setSelected(true);
		newTriangleToolAction.actionPerformed(null);

		toolBar.add(loadButton);
		toolBar.add(saveButton);
		toolBar.add(newTriangleButton);
		toolBar.add(breakSegmentButton);
		toolBar.add(moveToolButton);
		toolBar.add(deleteToolButton);
		toolBar.add(triangulateButton);

		add(toolBar, BorderLayout.PAGE_START);
	}

	private void changeTool(Tool tool) {
		polyDraw.setTool(tool);
		polyDraw.repaint();
		
	}

	public static void main(String[] args) {
		Main m = new Main();
		m.setSize(new Dimension(500, 500));
		m.setVisible(true);
	}
}
