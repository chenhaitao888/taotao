package com.taotao.exception;

public class TaoTaoException extends Exception{

	public TaoTaoException() {
		super();
	}

	public TaoTaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TaoTaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaoTaoException(String message) {
		super(message);
	}

	public TaoTaoException(Throwable cause) {
		super(cause);
	}

}
