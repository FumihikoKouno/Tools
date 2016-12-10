package memo.viewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import memo.StructuredMemo;
import utils.Common;

public class EditDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private StructuredMemo memo;
	private String initTitle;
	private boolean update;
	private String shareString;
	private int shareIndex;
	private JTextArea textArea;
	
	public EditDialog() {
		List<String> tags = new ArrayList<String>();
		tags.add("Tag");
		List<String> keywords = new ArrayList<String>();
		keywords.add("Keyword");
		List<String> contents = new ArrayList<String>();
		contents.add("Content");
		memo = new StructuredMemo("title", tags, keywords, contents);
		setView();
	}

	private void setView() {
		Common common = new Common();
		this.setLayout(new BorderLayout());
		this.add(createTitlePanel(), BorderLayout.NORTH);
		this.add(createExitPanel(), BorderLayout.SOUTH);
		this.add(createInputPanel(), BorderLayout.WEST);
		if(textArea == null){
			textArea = new JTextArea();
			for(String str : memo.getContents()){
				textArea.append(str + "\n");
			}
			this.add(textArea, BorderLayout.CENTER);
		}
		this.setPreferredSize(common.windowSize);
		pack();
	}

	public EditDialog(StructuredMemo memo) {
		this.memo = memo;
		setView();
	}

	private String inputDialog(String index, String initValue) {
		JDialog dialog = new JDialog(this, true);
		shareString = initValue;
		JLabel indexLabel = new JLabel("Input " + index);
		JTextField textField = new JTextField(initValue);
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();
				shareString = textField.getText();				
			}
		});
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				shareString = textField.getText();
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.add(okButton);
		buttons.add(cancelButton);
		dialog.setLayout(new BorderLayout());
		dialog.add(indexLabel, BorderLayout.NORTH);
		dialog.add(textField, BorderLayout.CENTER);
		dialog.add(buttons, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setVisible(true);
		return shareString;
	}
	
	private void okEvent() {
		writeFile();
		dispose();		
	}

	private JPanel createListPanel(String index, List<String> list) {
		JPanel ret = new JPanel();
		ret.setLayout(new BorderLayout());
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		for(String item : list) {
			JButton editButton = new JButton("Edit");
			editButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String tag = inputDialog("Tag", item);
					if(tag.equals(item)) {
						return;
					}
					if(list.contains(tag)) {
						list.remove(tag);
					}else{
						list.set(list.indexOf(item), tag);
					}
					setView();
				}
			});
			JPanel line = new JPanel();
			line.setLayout(new BoxLayout(line, BoxLayout.X_AXIS));
			line.add(new JLabel(item));
			line.add(editButton);
			listPanel.add(line);
		}
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input = inputDialog("Tag", "");
				if(!input.equals("")){
					list.add(input);
					setView();
				}
			}
		});
		ret.add(new JLabel(index), BorderLayout.NORTH);
		ret.add(new JScrollPane(listPanel), BorderLayout.CENTER);
		ret.add(addButton, BorderLayout.SOUTH);
		return ret;
	}

	private JPanel createInputPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(createListPanel("Tags", memo.getTags()));
		panel.add(createListPanel("Keywords", memo.getKeywords()));
		return panel;
	}

	private JPanel createTitlePanel() {
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.add(new JLabel("Title: " + memo.getTitle()), BorderLayout.CENTER);
		JButton titleButton = new JButton("Edit title");
		titleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				memo.setTitle(inputDialog("Title", memo.getTitle()));
				setView();
			}
		});
		titlePanel.add(titleButton, BorderLayout.EAST);
		return titlePanel;
	}

	private boolean confirmWindow() {
		update = false;
		JDialog dialog = new JDialog(this, true);
		JLabel exist = new JLabel("File " + memo.getTitle() + " already exists.");
		JLabel confirm = new JLabel("Are you sure to over write?");
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update = true;
				dialog.dispose();
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update = false;
				dialog.dispose();
			}
		});
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.add(exist);
		messagePanel.add(confirm);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		dialog.setLayout(new BorderLayout());
		dialog.add(messagePanel, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setVisible(true);
		return update;
	}

	private void write(File file) {
		String[] lines = textArea.getText().split("\n");
		List<String> contents = new ArrayList<String>();
		for(String line : lines) {
			contents.add(line);
		}
		memo.setContents(contents);
		try (BufferedWriter writer = 
				new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(file), "UTF-8"))) {
			writer.write(memo.toString());
		} catch (IOException e) {
			JDialog dialog = new JDialog(this, true);
			dialog.setLayout(new BorderLayout());
			dialog.add(new JLabel("IOException in writing file " + file.getName()), BorderLayout.CENTER);
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					dialog.dispose();
				}
			});
			dialog.add(okButton, BorderLayout.SOUTH);
			dialog.setVisible(true);
		}
	}

	private void writeFile() {
		File file = new File("./memo/" + memo.getTitle());
		if (file.exists()) {
			if (confirmWindow()) {
				write(file);
			}
		} else {
			try {
				file.createNewFile();
				write(file);
			} catch (IOException e) {
				JDialog dialog = new JDialog();
				dialog.add(new JLabel("IOException occured in create new file."));
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dialog.dispose();
					}
				});
				return;
			}
		}
	}

	private JPanel createExitPanel() {
		JPanel exitPanel = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okEvent();
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writeFile();
			}
		});
		exitPanel.setLayout(new BoxLayout(exitPanel, BoxLayout.X_AXIS));
		exitPanel.add(okButton);
		exitPanel.add(cancelButton);
		exitPanel.add(applyButton);
		return exitPanel;
	}
}
