package bto;

public class User {
	
	private String userID;
	private String Password;
	public String Usertype;
	public String Filter;
	
	public User(String userID, String Password) {
        this.userID = userID;
        this.Password = Password;
	}
	
	
	
	
	boolean login() {
		while(false) {
			if(userID.equals() && Password.equals("password")) {
				return true;
			}else {
				//prompt failure then again
			}
		}
	}
	
	
}
