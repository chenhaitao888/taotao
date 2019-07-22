package com.taotao.enums;

public enum MaskLevelEnum {
	DEFAULTLEVEL(0, 0), LEVEL1(4, 7), LEVEL2(2, Integer.MAX_VALUE), LEVEL3(12, 17), CARDLEVEL(-1,
			0), LARGELEVEL(Integer.MAX_VALUE, Integer.MAX_VALUE);
	private Integer beginIndex;
	private Integer endIndex;

	private MaskLevelEnum(Integer beginIndex, Integer endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}

	public Integer getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(Integer beginIndex) {
		this.beginIndex = beginIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}
}
