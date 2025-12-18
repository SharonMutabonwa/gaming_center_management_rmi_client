/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Odeth
 */

import model.Customer;
import model.User;

/**
 * SessionManager - Manages client-side session
 * Stores currently logged-in user and session token
 */
public class SessionManager {
    private static User currentUser;
    private static Customer currentCustomer;
    private static String sessionToken;

    /**
     * Set current user session
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
        if (user != null) {
            sessionToken = user.getSessionToken();
        }
    }

    /**
     * Get current logged-in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Set current customer (if user is a customer)
     */
    public static void setCurrentCustomer(Customer customer) {
        currentCustomer = customer;
    }

    /**
     * Get current customer
     */
    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * Get session token
     */
    public static String getSessionToken() {
        return sessionToken;
    }

    /**
     * Check if user is logged in
     */
    public static boolean isLoggedIn() {
        return currentUser != null && sessionToken != null;
    }

    /**
     * Clear session (logout)
     */
    public static void clearSession() {
        currentUser = null;
        currentCustomer = null;
        sessionToken = null;
    }

    /**
     * Get current username
     */
    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }

    /**
     * Get current user role
     */
    public static String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole().toString() : null;
    }

    /**
     * Check if current user is admin
     */
    public static boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole().toString());
    }

    /**
     * Check if current user is staff
     */
    public static boolean isStaff() {
        return currentUser != null && "STAFF".equals(currentUser.getRole().toString());
    }

    /**
     * Check if current user is customer
     */
    public static boolean isCustomer() {
        return currentUser != null && "CUSTOMER".equals(currentUser.getRole().toString());
    }

    /**
     * Get full name (if customer)
     */
    public static String getFullName() {
        if (currentCustomer != null) {
            return currentCustomer.getFullName();
        }
        return currentUser != null ? currentUser.getUsername() : "Guest";
    }
}
