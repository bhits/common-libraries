package gov.samhsa.c2s.common.xdsbclient.exception;

/**
 * The Class DS4PException.
 */
public class DS4PException extends RuntimeException {

	/**
	 * Instantiates a new d s4 p exception.
	 */
	public DS4PException() {
		super();
	}

	/**
	 * Instantiates a new d s4 p exception.
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 */
	public DS4PException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Instantiates a new d s4 p exception.
	 *
	 * @param arg0 the arg0
	 */
	public DS4PException(String arg0) {
		super(arg0);
	}

	/**
	 * Instantiates a new d s4 p exception.
	 *
	 * @param arg0 the arg0
	 */
	public DS4PException(Throwable arg0) {
		super(arg0);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6820537577171759098L;

}
