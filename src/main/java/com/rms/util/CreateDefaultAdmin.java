package com.rms.util;

import com.rms.dao.AdminDAO;
import com.rms.model.Admin;

public class CreateDefaultAdmin {
public static void main(String[]args) {
	
Admin admin =new Admin();
admin.setName("SuperAdmin");
admin.setEmail("admin@gmail.com");
String password= PasswordHash.hashPassword("tanushree");
admin.setPassword(password);
	
AdminDAO obj= new AdminDAO();
boolean status= obj.addAdmin(admin);
System.out.println("Admin Creation successful"+status);
}

}
