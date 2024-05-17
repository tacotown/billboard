package org.mtk.arcade.billboard;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.mtk.util.BitmapBuffer;

public class BillboardApp extends JFrame {

	JOptionPane optPane;
	JTabbedPane tabbedPane;
	final BillboardApp billboard;
	ColorMenu cm;
	JDialog jd;
	JButton colorButton;
	ColorBrick cb;
	final String romloc;
	BitmapBuffer s1;
	public static final int DEFAULT_MAG = 4;
	BillboardEditor primaryEdit;
	BillboardEditor secondaryEdit;
	BillboardEditor shrunkPrimaryEdit;
	BillboardEditor shrunkSecondaryEdit;
	BillboardLayer layer;
	boolean mini;

	int tabIndex;
	String board;

	public BillboardApp(String romloc, String board, String size)
			throws IOException {
		super("Billboard - Pole Position Billboard Editor");
		this.romloc = romloc;
		this.board = board;
		if ("mini".equals(size)) {
			mini = true;
		}
		setSize(600, 300);
		billboard = this;
		init();
	}

	public void init() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		optPane = new JOptionPane();
		optPane.setWantsInput(true);

		Container c = this.getContentPane();
		// build menu
		JMenuBar menuBar = new JMenuBar();

		// File
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Import GIF");
		OpenAction open = new OpenAction("Import GIF", null, null, 'O');
		openItem.addActionListener(open);
		fileMenu.add(openItem);
		fileMenu.addSeparator();
		JMenuItem saveItem = new JMenuItem("Save ROMS");
		SaveAction save = new SaveAction("Save ROMS", null, null, 'S');
		saveItem.addActionListener(save);
		saveItem.setEnabled(true);
		JMenuItem saveGifItem = new JMenuItem("Save GIF");
		SaveGifAction saveGif = new SaveGifAction("Save GIF", null, null, 'S');
		saveGifItem.addActionListener(saveGif);
		saveGifItem.setEnabled(true);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(saveGifItem);
		fileMenu.addSeparator();
		fileMenu.add(new ExitAction("Exit", null, null));

		menuBar.add(fileMenu);

		this.setJMenuBar(menuBar);

		// get board
		layer = BoardFactory.getLayer(board, romloc, mini);
		primaryEdit = new BillboardEditor(layer, BillboardConstants.PRIMARY,
				DEFAULT_MAG, false, true);
		secondaryEdit = new BillboardEditor(layer, BillboardConstants.SECONDARY,
				DEFAULT_MAG, false, true);
		if (!mini) {
			shrunkPrimaryEdit = new BillboardEditor(layer,
					BillboardConstants.PRIMARY, DEFAULT_MAG, true, false);
			shrunkSecondaryEdit = new BillboardEditor(layer,
					BillboardConstants.SECONDARY, DEFAULT_MAG, true, false);
		}
		// JToolBar
		JToolBar toolBar = new JToolBar();
		JButton incMag = new JButton("+");
		JButton decMag = new JButton("-");
		JButton clearButton = new JButton("CLEAR");
		toolBar.add(incMag);
		toolBar.add(decMag);
		cm = new ColorMenu(primaryEdit, secondaryEdit);
		primaryEdit.setColor(cm.getColor());
		jd = new JDialog(billboard, "Colors", true);
		jd.setContentPane(cm);
		jd.setSize(cm.getPreferredSize());
		jd.setLocation(50, 100);
		cm.addPropertyChangeListener("click", e -> {
			ColorMenu colorMenu = (ColorMenu) e.getSource();
			cb.setColor(colorMenu.getColor());
			if (tabIndex % 2 == 0) {
				primaryEdit.setColor(cb.getColor());
			}
			else {
				secondaryEdit.setColor(cb.getColor());
			}
			jd.setVisible(false);
		});

		colorButton = new JButton();
		cb = new ColorBrick();
		cb.setColor(cm.getColor());
		colorButton.add(cb);
		colorButton.addActionListener(ae -> jd.setVisible(true));
		toolBar.add(colorButton);

		clearButton.addActionListener(e -> {
			if (tabIndex % 2 == 0) {
				primaryEdit.clear();
			}
			else {
				secondaryEdit.clear();
			}

		});
		toolBar.add(clearButton);
		incMag.addActionListener(e -> {
			primaryEdit.increaseMagnification();
			secondaryEdit.increaseMagnification();
		});
		decMag.addActionListener(e -> {
			primaryEdit.decreaseMagnification();
			secondaryEdit.decreaseMagnification();
		});
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Primary", primaryEdit);
		tabbedPane.addTab("Secondary", secondaryEdit);
		if (!mini) {
			tabbedPane.addTab("ShrunkenPrimary", shrunkPrimaryEdit);
			tabbedPane.addTab("ShrunkenSecondary", shrunkSecondaryEdit);
		}
		tabbedPane.setVisible(true);
		//add the Listener
		tabbedPane.addChangeListener(e -> {
			tabIndex = tabbedPane.getSelectedIndex();
			cm.update(tabIndex % 2);
			cb.setColor(cm.getColor());
			primaryEdit.setColor(cm.getColor());
			secondaryEdit.setColor(cm.getColor());
		});

		this.setPreferredSize(new Dimension(600, 400));
		add(toolBar, BorderLayout.NORTH);
		add(tabbedPane, BorderLayout.CENTER);
		revalidate();
		repaint();
		pack();
		setVisible(true);
	}

	class SaveAction extends AbstractAction {
		public SaveAction(String text, Icon icon, String description,
				char accelerator) {
			super(text, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				layer.save(romloc);
				JOptionPane.showMessageDialog(billboard, "ROMS saved");
			}
			catch (IOException ioe) {
				JOptionPane.showMessageDialog(billboard,
						"Could not save: " + ioe.getMessage());
			}
		}
	}

	class SaveGifAction extends AbstractAction {
		public SaveGifAction(String text, Icon icon, String description,
				char accelerator) {
			super(text, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save As");
			fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.isDirectory() || f.getName().toLowerCase()
							.endsWith(".gif");
				}

				@Override
				public String getDescription() {
					return "Graphics Interchange Format (*.gif)";
				}
			});

			int userSelection = fileChooser.showSaveDialog(BillboardApp.this);

			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();
				String filePath = fileToSave.getAbsolutePath();
				if (!filePath.toLowerCase().endsWith(".gif")) {
					filePath += ".gif";
				}
				try {
					BufferedImage image = layer.convertToBufferedImage(
							tabIndex % 2);
					ImageIO.write(image, "GIF", new File(filePath));
					JOptionPane.showMessageDialog(BillboardApp.this,
							"Saved Image File");
				}
				catch (IOException ioe) {
					JOptionPane.showMessageDialog(BillboardApp.this,
							"Failed to save the image: " + ioe.getMessage());
				}
			}
		}
	}

	class OpenAction extends AbstractAction {
		public OpenAction(String text, Icon icon, String description,
				char accelerator) {
			super(text, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"GIF files", "gif");
			fileChooser.setFileFilter(filter);
			// Show open dialog
			int returnValue = fileChooser.showOpenDialog(BillboardApp.this);

			// If the user selects a file, process it
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				BufferedImage image;
				// Read the GIF file
				try {
					image = ImageIO.read(selectedFile);
				}
				catch (IOException ioException) {
					JOptionPane.showMessageDialog(billboard,
							"Unable to open file");
					return;
				}
				try {
					if (tabIndex == 0) {
						primaryEdit.importImage(image);
					}
					else {
						secondaryEdit.importImage(image);
					}
				}
				catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(billboard, ex.getMessage());
				}
			}
		}
	}

	static class ExitAction extends AbstractAction {

		public ExitAction(String text, Icon icon, String description) {
			super(text, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	public static void main(String[] args) throws IOException {
		Map<String, String> inputs = parseArgs(args);
		String romloc = inputs.get("--romloc");
		String type = inputs.get("--editor");
		String size = inputs.get("--mini");
		BillboardApp billboard = new BillboardApp(romloc, type, size);
		billboard.setVisible(true);
	}

	private static Map<String, String> parseArgs(String[] args) {
		Map<String, String> inputs = new HashMap<>();
		for (int i = 0, n = args.length; i < n; ++i) {
			switch (args[i]) {
				case "--romloc":
				case "--editor":
					++i;
					if (i < n) {
						inputs.put(args[i - 1], args[i]);
					}
					else {
						System.out.println(usage());
						System.exit(0);
					}
					break;
				case "--version":
					System.out.println(version());
					System.exit(0);
					break;
				case "--mini":
					inputs.put("--mini", "mini");
					break;
				default:
					System.out.println(usage());
					System.exit(0);
					break;
			}
		}
		for (String required : new String[] { "--romloc", "--editor" }) {
			if (!inputs.containsKey(required)) {
				System.out.println(usage());
				System.exit(0);
			}
		}
		return inputs;

	}

	public static String version() {
		String version;
		Class<?> clazz = BillboardApp.class;
		String className = clazz.getSimpleName() + ".class";
		String classPath = clazz.getResource(className).toString();
		if (!classPath.startsWith("jar")) {
			// Class not from JAR
			String relativePath =
					clazz.getName().replace('.', File.separatorChar) + ".class";
			String classFolder = classPath.substring(0,
					classPath.length() - relativePath.length() - 1);
			String manifestPath = classFolder + "/META-INF/MANIFEST.MF";
			version = readVersionFrom(manifestPath);
		}
		else {
			String manifestPath =
					classPath.substring(0, classPath.lastIndexOf("!") + 1)
							+ "/META-INF/MANIFEST.MF";
			version = readVersionFrom(manifestPath);
		}
		return version;
	}

	private static String readVersionFrom(String manifestPath) {
		String version;
		try {
			Manifest manifest = new Manifest(
					new URL(manifestPath).openStream());
			Attributes attrs = manifest.getMainAttributes();

			String implementationVersion = attrs.getValue(
					"Implementation-Version");

			version = implementationVersion;
		}
		catch (Exception e) {
			version = "Manifest error!";
		}
		return version;
	}

	public static String oldversion() {

		URLClassLoader cl = (URLClassLoader) BillboardApp.class.getClassLoader();
		try {
			URL url = cl.findResource("META-INF/MANIFEST.MF");
			Manifest manifest = new Manifest(url.openStream());
			// do stuff with it
			Attributes a = manifest.getMainAttributes();
			return a.getValue("Implementation-Version");
		}
		catch (IOException E) {
			// handle
			return "Manifest error";
		}
	}

	private static String usage() {
		StringBuilder sb = new StringBuilder(
				"Usage: billboard [OPTIONS} --romloc <romset location> --editor <digdug|centipede|namco|marboro|martini|pepsi>\n");
		sb.append("OPTIONS:\n");
		sb.append("\t--version: outputs the current executable version.\n");
		sb.append("\t--mini: edit the smaller version of the billboard.");
		return sb.toString();

	}

	static class ColorBrick extends JPanel {
		protected Color m_c = Color.BLACK;

		ColorBrick() {
			setBackground(m_c);
		}

		public Color getColor() {
			return m_c;
		}

		public void setColor(Color c) {
			m_c = c;
			setBackground(c);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(15, 15);
		}

		@Override
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}

		@Override
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}
	}

}
