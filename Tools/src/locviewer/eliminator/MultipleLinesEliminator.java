package locviewer.eliminator;

import java.util.ArrayList;
import java.util.List;

import utils.TokenFinder;
import utils.Tuple;

public class MultipleLinesEliminator {
	private String prefix;
	private String postfix;
	private List<Tuple> comments;
	private int numOfEliminatedLine;
	private TokenFinder prefixFinder;
	private TokenFinder postfixFinder;

	public MultipleLinesEliminator() {
		this.comments = new ArrayList<Tuple>();
	}

	public MultipleLinesEliminator(String prefix, String postfix) {
		this.comments = new ArrayList<Tuple>();
		this.setPrefix(prefix);
		this.setPostfix(postfix);
	}

	public int getNumOfEliminatedLine() {
		return this.numOfEliminatedLine;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
		this.prefixFinder = new TokenFinder(prefix);
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
		this.postfixFinder = new TokenFinder(postfix);
	}

	public List<String> eliminate(List<String> source) {
		if (prefix == null || postfix == null) {
			return source;
		}
		List<String> eliminatedSource = new ArrayList<String>(source);
		boolean inComment = false;
		Tuple startLine = new Tuple();
		String commentStr = "";
		numOfEliminatedLine = 0;
		for (int i = 0; i < eliminatedSource.size();) {
			String line = eliminatedSource.get(i);
			if (inComment) {
				int index = postfixFinder.indexOf(line);
				if (index >= 0) {
					inComment = false;
					commentStr = commentStr + "\n" + line.substring(0, index);
					this.comments.add(new Tuple(startLine, new Tuple(i + numOfEliminatedLine + 1, index), commentStr));
					String afterComment = line.substring(index + this.postfix.length());
					if (afterComment.replace("\t", "").replace(" ", "").length() == 0) {
						numOfEliminatedLine++;
						eliminatedSource.remove(i);
						continue;
					} else {
						eliminatedSource.set(i, line.substring(index + this.postfix.length()));
					}
					i++;
				} else {
					commentStr = commentStr + "\n" + line;
					if (i == eliminatedSource.size() - 1) {
						this.comments.add(new Tuple(startLine, new Tuple(i + numOfEliminatedLine + 1, line.length()),
								commentStr));
					}
					numOfEliminatedLine++;
					eliminatedSource.remove(i);
				}
			} else {
				int index = prefixFinder.indexOf(line);
				if (index >= 0) {
					startLine = new Tuple(i + numOfEliminatedLine + 1, index);
					String beforeComment = line.substring(0, index);
					String afterComment = line.substring(index + this.prefix.length());
					int postIndex = postfixFinder.indexOf(afterComment);
					if (postIndex >= 0) {
						this.comments.add(new Tuple(startLine,
								new Tuple(i + numOfEliminatedLine + 1, index + this.prefix.length() + postIndex),
								line.substring(index,
										index + this.prefix.length() + postIndex + this.postfix.length())));
						String exceptComment = beforeComment
								+ afterComment.substring(postIndex + this.postfix.length());
						if (exceptComment.replace("\t", "").replace(" ", "").length() == 0) {
							numOfEliminatedLine++;
							eliminatedSource.remove(i);
						} else {
							eliminatedSource.set(i, exceptComment);
							i++;
						}
						continue;
					}
					inComment = true;
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
		}
		return eliminatedSource;
	}

	public String toString() {
		String str = "prefix: " + this.prefix;
		str = str + "\npostfix: " + this.postfix;
		for (Tuple commentInfo : this.comments) {
			str = str + "\n" + commentInfo.toString();
		}
		return str;
	}
}
