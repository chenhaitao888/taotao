package com.taotao.utils;

public class MaskCodeUtil {
	public MaskCodeUtil(String sourceCode, int beginIndex, int endIndex, char cover) {
		this.sourceCode = sourceCode;
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
		this.cover = cover;
	}

	private static final String[] cache = { "*", "**", "***", "****" };
	private static final char coverDefault = '*';
	/** 源字符串 */
	private String sourceCode;
	/** * 掩码起始位置 */
	private int beginIndex;
	/** * 掩码结束位置 */
	private int endIndex;
	private char cover = coverDefault;
	
	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public char getCover() {
		return cover;
	}

	public void setCover(char cover) {
		this.cover = cover;
	}

	public static String[] getCache() {
		return cache;
	}

	public static char getCoverdefault() {
		return coverDefault;
	}
	
	public static String getMaskSubWay(String sourceCode, int beginIndex, int endIndex, char cover) {
		if (null == sourceCode || "".equals(sourceCode) || "null".equals(sourceCode)) {
			return "";
		}
		if (beginIndex > endIndex) {
			beginIndex = beginIndex ^ endIndex;
			endIndex = endIndex ^ beginIndex;
			beginIndex = beginIndex ^ endIndex;
		}
		return sourceCode.substring(0, beginIndex - 1) + cover(beginIndex, endIndex, cover)
				+ sourceCode.substring(endIndex, sourceCode.length());
	}

	private static String cover(int beginIndex, int endIndex, char cover) {
		if (beginIndex < 0 || endIndex < 0) {
			return "";
		}
		if (endIndex - beginIndex < cache.length && cover == coverDefault) {
			return cache[endIndex - beginIndex];
		}
		StringBuilder sb = new StringBuilder(endIndex - beginIndex);
		for (; beginIndex <= endIndex; beginIndex++) {
			sb.append(cover);
		}
		return sb.toString();
	}
}
