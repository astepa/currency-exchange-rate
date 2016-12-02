package app.service;

public class CannotGetExchangeRateException extends Exception {

	private static final long serialVersionUID = 1L;

	CannotGetExchangeRateException() {
		super();
	}

	public CannotGetExchangeRateException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CannotGetExchangeRateException(String message) {
		super(message);
	}

}