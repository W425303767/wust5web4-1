package testsupport;

import java.io.PrintWriter;
import java.io.StringWriter;

/** Captures whatever a servlet writes to the response. */
public final class ServletTestSupport {

	private final StringWriter buffer = new StringWriter();
	private final PrintWriter writer = new PrintWriter(buffer);

	public PrintWriter writer() {
		return writer;
	}

	public String output() {
		writer.flush();
		return buffer.toString();
	}
}
