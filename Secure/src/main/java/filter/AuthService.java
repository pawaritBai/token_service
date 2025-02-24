package filter;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

import dao.CustomerDAO;
import model.Customer;

public class AuthService {
	public boolean authenticate(String authCredentials) {
	    if (authCredentials == null || authCredentials.isEmpty()) {
	        return false;
	    }

	    final String encodedUserPassword = authCredentials.replaceFirst("Basic ", ""); // แก้ไขช่องว่าง
	    String usernameAndPassword = null;

	    try {
	        byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
	        usernameAndPassword = new String(decodedBytes, "UTF-8");
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }

	    final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
	    if (tokenizer.countTokens() < 2) {
	        return false; // ป้องกันกรณีไม่มี ":" หรือข้อมูลไม่ครบ
	    }

	    final String username = tokenizer.nextToken();
	    final String password = tokenizer.nextToken();

	    CustomerDAO c = new CustomerDAO();
	    Customer cus = c.findCustomer(username);

	    if (cus == null) { // ✅ ป้องกัน NullPointerException
	        return false;
	    }

	    return cus.getUsername().equals(username) && cus.getPwd().equals(password);
	}

}
