package bto;

public class BTOApplicationTest {
    public static void main(String[] args) {
        System.out.println("---- Testing BTOApplication ----");

        // Create a new application
        BTOapplication app = new BTOapplication("S1234567A", "PROJECT001");

        // Test 1: Initial status
        System.out.println("[Test 1] Initial Status: " + app.getStatus());               // Expect: Pending
        System.out.println("[Test 2] Is Bookable? " + app.isBookable());                 // Expect: false
        System.out.println("[Test 3] Is Booked? " + app.isBooked());                     // Expect: false

        // Test 2: Update to "Successful"
        app.updateStatus("Successful", "3-Room");
        System.out.println("[Test 4] Updated Status: " + app.getStatus());               // Expect: Successful
        System.out.println("[Test 5] Unit Type: " + app.getUnitType());                  // Expect: 3-Room
        System.out.println("[Test 6] Timestamp Exists? " + !app.getTimestamp().isEmpty());// Expect: true
        System.out.println("[Test 7] Is Bookable Now? " + app.isBookable());             // Expect: true

        // Test 3: Update to "Booked"
        app.updateStatus("Booked", "3-Room");
        System.out.println("[Test 8] Final Status: " + app.getStatus());                 // Expect: Booked
        System.out.println("[Test 9] Is Booked Now? " + app.isBooked());                 // Expect: true

        // Final info check
        System.out.println("[Test 10] Applicant NRIC: " + app.getUserID());       // Expect: S1234567A
        System.out.println("[Test 11] Project ID: " + app.getProjectId());               // Expect: PROJECT001

        System.out.println("---- BTOApplication Test Complete ----");
    }
}
