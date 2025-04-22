package bto.util;

/**
 * These methods are related to the enquiry class.
 * The first method should be implemented to view every single enquiry created and stored
 * The second method should be implemented to create a response to already existing enquiries
 */
public interface enquiryInterface {
	 void viewEnquiriesAll();
	 void replyEnquiry(int enquiryId, String replyMessage);
}
