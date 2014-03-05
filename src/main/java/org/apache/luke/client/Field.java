package org.apache.luke.client;

/**
 * A simple data type that represents a displayable Lucene index field
 * @author dmitrykan
 *
 */
public class Field {
  private final String name;
  private final String termCount;
  private final String decoder;

  public Field(String name, String termCount, String decoder) {
    this.name = name;
    this.termCount = termCount;
    this.decoder = decoder;
  }

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the termCount
	 */
	public String getTermCount() {
		return termCount;
	}

	/**
	 * @return the decoder
	 */
	public String getDecoder() {
		return decoder;
	}
}

