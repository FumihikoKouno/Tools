package locviewer.eliminator;

import java.util.ArrayList;
import java.util.List;

public class BlankLineEliminator {
	private List<Integer> blankline;
	private int numOfEliminatedLine;

	public BlankLineEliminator() {
		this.blankline = new ArrayList<Integer>();
	}

	public int getNumOfEliminatedLine() {
		return this.numOfEliminatedLine;
	}

	public List<String> eliminate(List<String> source) {
		List<String> eliminatedSource = new ArrayList<String>(source);
		numOfEliminatedLine = 0;
		for (int i = 0; i < eliminatedSource.size();) {
			String line = eliminatedSource.get(i);
			if (line.replace("\t", "").replace(" ", "").length() == 0) {
				numOfEliminatedLine++;
				eliminatedSource.remove(i);
				this.blankline.add(i + numOfEliminatedLine);
				continue;
			}
			i++;
		}
		return eliminatedSource;
	}

	public String toString() {
		String str = "";
		for (int lineNo : this.blankline) {
			str = str + "\n" + Integer.toString(lineNo);
		}
		return str;
	}
}
