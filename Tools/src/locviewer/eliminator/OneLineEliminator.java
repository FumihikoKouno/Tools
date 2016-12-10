package locviewer.eliminator;

import java.util.ArrayList;
import java.util.List;

import utils.TokenFinder;
import utils.Tuple;

public class OneLineEliminator {
	private String prefix;
	private List<Tuple> comments;
	private int numOfEliminatedLine;
	private TokenFinder prefixFinder;

	public OneLineEliminator() {
		this.comments = new ArrayList<Tuple>();
	}

	public OneLineEliminator(String prefix) {
		this.comments = new ArrayList<Tuple>();
		this.setPrefix(prefix);
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
		this.prefixFinder = new TokenFinder(prefix);
	}

	public int getNumOfEliminatedLine() {
		return this.numOfEliminatedLine;
	}

	public List<String> eliminate(List<String> source) {
		if (prefix == null) {
			return source;
		}
		List<String> eliminatedSource = new ArrayList<String>(source);
		numOfEliminatedLine = 0;
		for (int i = 0; i < eliminatedSource.size();) {
			String line = eliminatedSource.get(i);
			int index = prefixFinder.indexOf(line);
			if (index >= 0) {
				this.comments.add(new Tuple(i + numOfEliminatedLine + 1, index, line.substring(index)));
				String beforeComment = line.substring(0, index);
				if (beforeComment.replace("\t", "").replace(" ", "").length() == 0) {
					numOfEliminatedLine++;
					eliminatedSource.remove(i);
					continue;
				} else {
					eliminatedSource.set(i, line.substring(0, index));
				}
			}
			i++;
		}
		return eliminatedSource;
	}

	public String toString() {
		String str = "prefix: " + this.prefix;
		for (Tuple commentInfo : this.comments) {
			str = str + "\n" + commentInfo.toString();
		}
		return str;
	}
}
