package com.campus.service;

import com.campus.repository.CampusRepository;
import com.campus.model.User;

public class AuthenticationService {
    private CampusRepository<User> userRepository;
    private User currentUser;

    public AuthenticationService(CampusRepository<User> userRepository){
        setUserRepository(userRepository);
    }

    public User login(String username, String password){
        boolean isFound = false;

        for (int i = 0; i < userRepository.getSize(); i++){
            if (userRepository.get(i).getUsername().equals(username)){
                isFound = true;
                if (userRepository.get(i).authenticate(password)){
                    currentUser = userRepository.get(i);
                    return currentUser;
                } else {
                    System.out.println("Incorrect password.");
                }
            }
        }

        if (!isFound){
            System.out.println("Username not found.");
        }

        return null;
    }

    public void logout(){
        if (currentUser != null){
            currentUser = null;
            System.out.println("Logged out.");
        } else {
            System.out.println("Already logged out.");
        }
    }

    public void setUserRepository(CampusRepository<User> userRepository) {
        if (userRepository != null){
            this.userRepository = userRepository;
        } else {
            System.out.println("User repository cannot be null.");
            this.userRepository = new CampusRepository<>();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getCurrentRole(){
        if (currentUser != null){
            return currentUser.getRole();
        } else {
            System.out.println("Logged out.");
            return "N/A";
        }
    }

    public boolean hasAccess(String requiredRole){
        if (currentUser == null || requiredRole == null || requiredRole.isEmpty()) return false;

        if (currentUser.getRole().equalsIgnoreCase("ADMIN")) return true;

        if (currentUser.getRole().equalsIgnoreCase(requiredRole)) return true;

        return false;
    }
}
