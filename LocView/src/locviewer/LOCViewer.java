package locviewer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import locviewer.eliminator.BlankLineEliminator;
import locviewer.eliminator.MultipleLinesEliminator;
import locviewer.eliminator.OneLineEliminator;
import utils.Config;

public class LOCViewer extends JPanel {
	private static final long serialVersionUID = 1L;
	private DefaultTableModel model;
	private Config config;

	public LOCViewer(File root) {
		if (root == null) {
			JTable table = createTable(new ArrayList<Object[]>());
			JScrollPane pane = new JScrollPane(table);
			add(pane);
			return;
		}
		config = new Config("comment");
		List<File> files = new ArrayList<>();

		if (root.isDirectory()) {
			for (File file : root.listFiles()) {
				files.add(file);
			}
		} else {
			files.add(root);
		}

		JTable table = createTable(getLOCInfo(files));
		JScrollPane pane = new JScrollPane(table);
		add(pane);
	}

	public JTable createTable(List<Object[]> locInfo) {
		String[] index = { "Directory", "File Name", "LOC", "One Line Comment", "Multiple Line Comment", "Blank Line" };
		model = new DefaultTableModel(index, 0);
		int loc = 0;
		int numOfOneLineComment = 0;
		int numOfMultipleLinesComment = 0;
		int numOfBlankLine = 0;
		for (Object[] info : locInfo) {
			if (info[2] instanceof Integer) {
				loc += (int) info[2];
			} else {
				continue;
			}
			if (info[3] instanceof Integer) {
				numOfOneLineComment += (int) info[3];
			} else {
				continue;
			}
			if (info[4] instanceof Integer) {
				numOfMultipleLinesComment += (int) info[4];
			} else {
				continue;
			}
			if (info[5] instanceof Integer) {
				numOfBlankLine += (int) info[5];
			} else {
				continue;
			}
			model.addRow(info);
		}
		String[] sum = { "Sum", "Sum", Integer.toString(loc), Integer.toString(numOfOneLineComment),
				Integer.toString(numOfMultipleLinesComment), Integer.toString(numOfBlankLine) };
		model.addRow(sum);
		JTable table = new JTable(model);
		table.setEnabled(false);
		return table;
	}

	public Object[] getOneFileLOC(File file) {
		String fileName = file.getName();
		int index = fileName.lastIndexOf(".");
		if (index < 0) {
			return new Object[] { file.getParent(), fileName, "No extention", "", "", "" };
		}
		String extention = fileName.substring(fileName.lastIndexOf(".") + 1);
		Map<String, List<List<String>>> fileConfig;
		try {
			fileConfig = config.getConfig(extention);
		} catch (NullPointerException e) {
			return new Object[] { file.getParent(), extention, "Setting file could not be found",
					e.getCause().toString(), "", "" };
		}
		if (fileConfig == null) {
			return new Object[] { file.getParent(), extention, "Setting could not be found", "", "", "" };
		}
		List<OneLineEliminator> oneLineEliminators = new ArrayList<OneLineEliminator>();
		List<MultipleLinesEliminator> multipleLinesEliminators = new ArrayList<MultipleLinesEliminator>();
		for (List<String> onePrefix : fileConfig.get("OneLineComment")) {
			oneLineEliminators.add(new OneLineEliminator(onePrefix.get(0)));
		}
		for (List<String> multiple : fileConfig.get("MultipleLinesComment")) {
			multipleLinesEliminators.add(new MultipleLinesEliminator(multiple.get(0), multiple.get(1)));
		}
		List<String> source = new ArrayList<String>();
		try {
			source = Files.readAllLines(Paths.get(file.getAbsolutePath()));
		} catch (IOException e) {
			return new Object[] { file.getParent(), fileName, "IOException", e.toString(), "", "" };
		}
		List<String> eliminatedSource = new ArrayList<String>();
		int numOfMultipleLinesComment = 0;
		for (MultipleLinesEliminator eliminator : multipleLinesEliminators) {
			if (eliminatedSource.size() == 0) {
				eliminatedSource = eliminator.eliminate(source);
			} else {
				eliminatedSource = eliminator.eliminate(eliminatedSource);
			}
			numOfMultipleLinesComment += eliminator.getNumOfEliminatedLine();
		}
		int numOfOneLineComment = 0;
		for (OneLineEliminator eliminator : oneLineEliminators) {
			eliminatedSource = eliminator.eliminate(eliminatedSource);
			numOfOneLineComment += eliminator.getNumOfEliminatedLine();
		}
		BlankLineEliminator blankLineEliminator = new BlankLineEliminator();
		blankLineEliminator.eliminate(source);
		eliminatedSource = blankLineEliminator.eliminate(eliminatedSource);
		int numOfBlankLine = blankLineEliminator.getNumOfEliminatedLine();

		System.out.println(fileName);
		for (String str : eliminatedSource) {
			System.out.println(str);
		}
		System.out.println();

		return new Object[] { file.getParent(), fileName, eliminatedSource.size(), numOfOneLineComment,
				numOfMultipleLinesComment, numOfBlankLine };
	}

	public List<Object[]> getLOCInfo(List<File> files) {
		List<Object[]> info = new ArrayList<Object[]>();
		for (File file : files) {
			if (file.isDirectory()) {
				List<File> fileList = new ArrayList<>();
				for (File child : file.listFiles()) {
					fileList.add(child);
				}
				List<Object[]> locInfo = getLOCInfo(fileList);
				for (Object[] loc : locInfo) {
					info.add(loc);
				}
			} else {
				info.add(getOneFileLOC(file));
			}
		}
		return info;
	}
}
