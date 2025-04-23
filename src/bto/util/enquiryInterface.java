package bto.util;

/**
 * These methods are related to the enquiry class.
 * The first method should be implemented to view every single enquiry created and stored
 * The second method should be implemented to create a response to already existing enquiries
 */
public interface enquiryInterface {
	
	/**
	 * Uses default methods as a means of handling when interface is not implemented to prevent error
	 */
	default void viewEnquiriesAll() {
		System.out.println("Error");
	}
	 
	/**
	 * Uses default methods as a means of handling when interface is not implemented to prevent error
	 * 
	 * @param enquiryId of enquiry
	 * @param replyMessage response
	 */
	default void replyEnquiry(int enquiryId, String replyMessage) {
		System.out.println("Error");
	}
}
