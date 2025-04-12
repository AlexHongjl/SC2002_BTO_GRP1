package bto;

public class UserPerson {
	private String name;
	private String NRIC;
	private int age;
	private String maritalStatus;
	private String password;
	private String userType;
	
	public UserPerson(String name, String NRIC, int age, String maritalStatus, String password, String userType) {
		this.name = name;
		this.NRIC = NRIC;
		this.age = age;
		this.maritalStatus = maritalStatus;
		this.password = password;
		this.userType = userType;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getNRIC() {
		return this.NRIC;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public String getMaritalStatus() {
		return this.maritalStatus;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getUserType() {
		return this.userType;
	}
	
	public void changePassword(String newPassword) {
		this.password = newPassword;
	}
	
	
}
