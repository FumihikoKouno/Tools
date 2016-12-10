package memo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import utils.Config;

public class StructuredMemos implements Iterable<StructuredMemo> {
	private List<StructuredMemo> memos;

	public StructuredMemos() {
		memos = new ArrayList<StructuredMemo>();
		Map<String, Map<String, List<List<String>>>> config = new Config("memo").getConfig();
		for (String title : config.keySet()) {
			Map<String, List<List<String>>> oneInfo = config.get(title);
			StructuredMemo memo = new StructuredMemo(title, getInfo("Tag", oneInfo), getInfo("Keyword", oneInfo),
					getInfoWithSpace("Content", oneInfo));
			memos.add(memo);
		}
	}

	public StructuredMemos(List<StructuredMemo> memoList) {
		memos = memoList;
	}

	public StructuredMemos getMemosByTag(String tag) {
		List<StructuredMemo> ret = new ArrayList<StructuredMemo>();
		for (StructuredMemo memo : memos) {
			if (memo.hasTag(tag)) {
				ret.add(memo);
			}
		}
		return new StructuredMemos(ret);
	}

	public StructuredMemo getMemoByTitle(String title) {
		for (StructuredMemo memo : memos) {
			if (memo.hasTitle(title)) {
				return memo;
			}
		}
		return null;
	}

	public StructuredMemos getMemosByKeyword(String key) {
		List<StructuredMemo> ret = new ArrayList<StructuredMemo>();
		for (StructuredMemo memo : memos) {
			if (memo.hasKeyword(key)) {
				ret.add(memo);
			}
		}
		return new StructuredMemos(ret);
	}

	private List<String> getInfo(String key, Map<String, List<List<String>>> oneInfo) {
		List<String> ret = new ArrayList<String>();
		for (List<String> infos : oneInfo.get(key)) {
			for (String info : infos) {
				ret.add(info);
			}
		}
		return ret;
	}

	private List<String> getInfoWithSpace(String key, Map<String, List<List<String>>> oneInfo) {
		List<String> ret = new ArrayList<String>();
		for (List<String> infos : oneInfo.get(key)) {
			String str = "";
			for (String info : infos) {
				str = str + info;
				str = str + " ";
			}
			ret.add(str);
		}
		return ret;
	}

	@Override
	public Iterator<StructuredMemo> iterator() {
		return memos.iterator();
	}
}
