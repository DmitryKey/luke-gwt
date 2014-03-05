/**
 * 
 */
package org.apache.luke.client.data;

import java.util.Arrays;
import java.util.List;
import org.apache.luke.client.Field;

/**
 * @author dmitrykan
 *
 */
public final class FieldsDummyData {
	
	// The list of data to display.
	// Name, Term count, Decoder
	public static final List<Field> Fields = Arrays.asList(new Field("id",
			"17321", "string utf8"), new Field("query", "7127", "string utf8"),
			new Field("timestamp", "15020", "string utf8"), new Field("user",
					"3186", "string utf8"));
}
