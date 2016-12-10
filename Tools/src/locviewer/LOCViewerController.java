package locviewer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LOCViewerController extends JPanel {
	private static final long serialVersionUID = 1L;

	private LOCViewer viewer;
	private JTextField textField;

	public LOCViewerController() {

		setLayout(new BorderLayout());
		viewer = new LOCViewer(null);
		add(viewer, BorderLayout.CENTER);
		JPanel fileSelector = new JPanel();
		textField = new JTextField();
		JButton selectButton = new JButton("Select file");
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = selectFile();
				if (file != null) {
					textField.setText(file.getAbsolutePath());
				} else {
					textField.setText("");
				}
			}
		});
		JButton updateButton = new JButton("Update table");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				review();
			}
		});
		fileSelector.setLayout(new GridLayout(1, 3));
		fileSelector.add(new JLabel("Target Directory of File: "));
		fileSelector.add(textField);
		fileSelector.add(selectButton);
		JPanel fileUpdater = new JPanel();
		fileUpdater.setLayout(new GridLayout(2, 1));
		fileUpdater.add(fileSelector);
		fileUpdater.add(updateButton);

		add(fileUpdater, BorderLayout.NORTH);
	}

	public File selectFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.showOpenDialog(this);
		return chooser.getSelectedFile();
	}

	public void review() {
		remove(viewer);
		viewer = new LOCViewer(new File(textField.getText()));
		add(viewer, BorderLayout.EAST);
		revalidate();
		repaint();
	}

}
