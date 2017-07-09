package memo.viewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import memo.StructuredMemo;
import memo.StructuredMemos;
import utils.Common;

public class MemoViewer extends JPanel {
	private static final long serialVersionUID = 1L;
	private List<String> titles;
	private List<String> tags;
	private List<String> keywords;
	private StructuredMemos memos;
	private JSplitPane splitPane;
	private Common common;
	private StructuredMemo currentMemo;

	public MemoViewer() {
		common = new Common();
		setMemos();
		setTabbedPanes();
	}

	private void initializeSplitPane(Component left) {
		splitPane = new JSplitPane();
		splitPane.setLeftComponent(left);
		setContents("", new ArrayList<String>());
	}

	private void setTabbedPanes() {
		this.removeAll();
		JTabbedPane tabbedPane = new JTabbedPane();
		for (String tag : tags) {
			tabbedPane.add(tag, createOneTagPane(memos.getMemosByTag(tag)));
		}
		initializeSplitPane(tabbedPane);
		this.add(addEditButton(splitPane));
	}

	private JPanel addEditButton(Component comp) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JButton editCurrentButton = new JButton("Edit Current Memo");
		editCurrentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditDialog(currentMemo).setVisible(true);
			}
		});
		JButton newMemoButton = new JButton("Create New Memo");
		newMemoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new EditDialog().setVisible(true);
			}
		});
		JPanel buttons = new JPanel();
		buttons.setLayout(new BorderLayout());
		buttons.add(editCurrentButton, BorderLayout.WEST);
		buttons.add(newMemoButton, BorderLayout.EAST);
		panel.add(buttons, BorderLayout.SOUTH);
		comp.setPreferredSize(new Dimension(common.windowSize.width, common.windowSize.height - 20));
		panel.add(comp, BorderLayout.CENTER);
		panel.setPreferredSize(common.windowSize);
		return panel;
	}

	private void setKeywordPanes() {
		this.removeAll();
		JTabbedPane tabbedPane = new JTabbedPane();
		for (String keyword : keywords) {
			tabbedPane.add(keyword, createOneTagPane(memos.getMemosByKeyword(keyword)));
		}
		initializeSplitPane(tabbedPane);
		this.add(addEditButton(splitPane));
	}

	private JScrollPane createOneTagPane(StructuredMemos memos) {
		JPanel titlePane = new JPanel();
		JScrollPane scrollPane = new JScrollPane(titlePane);
		titlePane.setLayout(new BoxLayout(titlePane, BoxLayout.Y_AXIS));
		for (StructuredMemo memo : memos) {
			JButton titleButton = new JButton(memo.getTitle());
			titleButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setContents(memo.getTitle(), memo.getContents());
					currentMemo = memo;
				}
			});
			titlePane.setLayout(new BoxLayout(titlePane, BoxLayout.Y_AXIS));
			titlePane.add(titleButton);
		}
		return scrollPane;
	}

	private void setContents(String title, List<String> contents) {
		JLabel titleLabel = new JLabel(title);
		JTextArea textArea = new JTextArea();
		String text = "";
		for (String content : contents) {
			text = text + content;
			text = text + System.getProperty("line.separator");
		}
		textArea.setText(text);
		JPanel rightComponent = new JPanel();
		rightComponent.setLayout(new BorderLayout());
		rightComponent.add(titleLabel, BorderLayout.NORTH);
		rightComponent.add(textArea, BorderLayout.CENTER);
		splitPane.setRightComponent(rightComponent);
	}

	private void setMemos() {
		titles = new ArrayList<String>();
		memos = new StructuredMemos();
		tags = new ArrayList<String>();
		keywords = new ArrayList<String>();
		for (StructuredMemo memo : memos) {
			titles.add(memo.getTitle());
			List<String> memoTags = memo.getTags();
			for (String memoTag : memoTags) {
				if (!tags.contains(memoTag)) {
					tags.add(memoTag);
				}
			}
			List<String> memoKeywords = memo.getKeywords();
			for (String memoKeyword : memoKeywords) {
				if (!keywords.contains(memoKeyword)) {
					keywords.add(memoKeyword);
				}
			}
		}
	}
}
