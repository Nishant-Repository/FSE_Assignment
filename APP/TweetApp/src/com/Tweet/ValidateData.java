package com.Tweet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateData {

	public String validateCredential(String crdential, String type) {

		String errMessage = "Validated";

		if (crdential.equals("")) {
			errMessage = "Error! " + type + " cannot be empty";
		} else if (crdential.length() < 8 && !(type.equals("fname") || (type.equals("lname")))) {
			errMessage = "Error! " + type + " cannot be less than 8 characters";
		} else if (crdential.length() > 30) {
			errMessage = "Error! " + type + " cannot be more than 30 characters";
		}

		if (type == "password") {
			String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";

			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(crdential);

			if (!m.matches()) {
				errMessage = "Error! " + type + " should contains numbers, letters, and special characters.";
			}
		}

		return errMessage;
	}
}
