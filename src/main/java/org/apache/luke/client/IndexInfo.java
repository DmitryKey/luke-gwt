/**
 * 
 */
package org.apache.luke.client;

/**
 * Class for holding the index level information bits: index name, number of fields, number of documents etc
 * @author dmitrykan
 *
 */
public class IndexInfo {
	private String name;
	private int numberOfFields;
	private int numberOfDocuments;
	private int numberOfTerms;
	private boolean hasDeletions;
	private boolean isOptimised;
	private long indexVersion;
	private String indexFormat;
	private String directoryImplementation;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the numberOfFields
	 */
	public int getNumberOfFields() {
		return numberOfFields;
	}
	/**
	 * @param numberOfFields the numberOfFields to set
	 */
	public void setNumberOfFields(int numberOfFields) {
		this.numberOfFields = numberOfFields;
	}
	/**
	 * @return the numberOfDocuments
	 */
	public int getNumberOfDocuments() {
		return numberOfDocuments;
	}
	/**
	 * @param numberOfDocuments the numberOfDocuments to set
	 */
	public void setNumberOfDocuments(int numberOfDocuments) {
		this.numberOfDocuments = numberOfDocuments;
	}
	/**
	 * @return the numberOfTerms
	 */
	public int getNumberOfTerms() {
		return numberOfTerms;
	}
	/**
	 * @param numberOfTerms the numberOfTerms to set
	 */
	public void setNumberOfTerms(int numberOfTerms) {
		this.numberOfTerms = numberOfTerms;
	}
	/**
	 * @return the hasDeletions
	 */
	public boolean isHasDeletions() {
		return hasDeletions;
	}
	/**
	 * @param hasDeletions the hasDeletions to set
	 */
	public void setHasDeletions(boolean hasDeletions) {
		this.hasDeletions = hasDeletions;
	}
	/**
	 * @return the isOptimised
	 */
	public boolean isOptimised() {
		return isOptimised;
	}
	/**
	 * @param isOptimised the isOptimised to set
	 */
	public void setOptimised(boolean isOptimised) {
		this.isOptimised = isOptimised;
	}
	/**
	 * @return the indexVersion
	 */
	public long getIndexVersion() {
		return indexVersion;
	}
	/**
	 * @param indexVersion the indexVersion to set
	 */
	public void setIndexVersion(long indexVersion) {
		this.indexVersion = indexVersion;
	}
	/**
	 * @return the indexFormat
	 */
	public String getIndexFormat() {
		return indexFormat;
	}
	/**
	 * @param indexFormat the indexFormat to set
	 */
	public void setIndexFormat(String indexFormat) {
		this.indexFormat = indexFormat;
	}
	/**
	 * @return the directoryImplementation
	 */
	public String getDirectoryImplementation() {
		return directoryImplementation;
	}
	/**
	 * @param directoryImplementation the directoryImplementation to set
	 */
	public void setDirectoryImplementation(String directoryImplementation) {
		this.directoryImplementation = directoryImplementation;
	}
	
	
}
