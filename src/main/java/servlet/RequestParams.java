package servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * Helpers for reading request parameters. Missing or malformed values raise an
 * IllegalArgumentException instead of failing with a NullPointerException or
 * silently producing a nonsense value, so that callers can answer with
 * HTTP 400 and a message that says what is wrong.
 */
public final class RequestParams {

	private RequestParams() {
	}

	/**
	 * @return the value of the parameter, never null nor empty
	 * @throws IllegalArgumentException if the parameter is missing or empty
	 */
	public static String require(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value == null || value.trim().length() == 0)
			throw new IllegalArgumentException("Missing required parameter '" + name + "'");
		return value.trim();
	}

	/**
	 * @return the parameter parsed as a non negative int, or defaultValue when it is
	 *         missing or empty
	 * @throws IllegalArgumentException if the parameter is not a non negative number
	 */
	public static int optInt(HttpServletRequest request, String name, int defaultValue) {
		String value = request.getParameter(name);
		if (value == null || value.trim().length() == 0)
			return defaultValue;
		return parseNonNegative(name, value.trim());
	}

	/**
	 * @return the parameter parsed as a non negative int
	 * @throws IllegalArgumentException if the parameter is missing or is not a non
	 *         negative number
	 */
	public static int requireInt(HttpServletRequest request, String name) {
		return parseNonNegative(name, require(request, name));
	}

	private static int parseNonNegative(String name, String value) {
		int parsed;
		try {
			parsed = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Parameter '" + name + "' is not a number: '" + value + "'", e);
		}
		if (parsed < 0)
			throw new IllegalArgumentException("Parameter '" + name + "' must not be negative: " + parsed);
		return parsed;
	}

}
