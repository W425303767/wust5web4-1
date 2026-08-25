package Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.json.JSONObject;
import org.junit.Test;

public class returndataTest {

	@Test
	public void numericFieldsDefaultToZero() {
		returndata data = new returndata();

		assertEquals(0, data.draw);
		assertEquals(0, data.start);
		assertEquals(0, data.length);
		assertEquals(0, data.recordTotal);
		assertEquals(0, data.recordsFiltered);
	}

	@Test
	public void dataArrayIsInitialisedAndMutable() {
		returndata data = new returndata();

		assertNotNull(data.data);
		assertEquals(0, data.data.length());

		JSONObject row = new JSONObject();
		row.put("num", "2017101");
		data.data.put(row);

		assertEquals(1, data.data.length());
		assertEquals("2017101", data.data.getJSONObject(0).getString("num"));
	}

	@Test
	public void eachInstanceOwnsItsDataArray() {
		returndata first = new returndata();
		returndata second = new returndata();

		first.data.put(new JSONObject());

		assertNotSame(first.data, second.data);
		assertEquals(0, second.data.length());
	}
}
