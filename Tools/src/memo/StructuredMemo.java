package memo;

import java.util.List;

public class StructuredMemo {
	private String title;
	private List<String> tags;
	private List<String> keywords;
	private List<String> contents;

	public StructuredMemo(String title, List<String> tags, List<String> keywords, List<String> contents) {
		this.title = title;
		this.tags = tags;
		this.keywords = keywords;
		this.contents = contents;
	}

	public boolean hasTitle(String title) {
		return this.title.equals(title);
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	public boolean hasKeyword(String keyword) {
		return keywords.contains(keyword);
	}

	public String getTitle() {
		return title;
	}

	public List<String> getTags() {
		return tags;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public List<String> getContents() {
		return contents;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	
	public void setContents(List<String> contents) {
		this.contents = contents;
	}

	public String toString() {
		String str = "";
		for(String tag : tags) {
			str = str + "Tag " + tag + "\n";
		}
		for(String keyword : keywords) {
			str = str + "Keyword " + keyword + "\n";
		}
		for(String content : contents) {
			str = str + "Content " + content + "\n";
		}
		return str;
	}
}
