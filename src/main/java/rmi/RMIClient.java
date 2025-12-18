/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi;

/**
 *
 * @author Odeth
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rmi.RemoteService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMIClient - Singleton RMI Connection Manager
 * Manages connection to RMI server
 */
public class RMIClient {
    private static final Logger logger = LoggerFactory.getLogger(RMIClient.class);
    
    // Server configuration
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 3500;
    private static final String SERVICE_NAME = "GamingCenterService";
    
    // Singleton instance
    private static RemoteService service;
    private static boolean connected = false;

    /**
     * Get RemoteService instance (Singleton)
     */
    public static RemoteService getService() {
        if (service == null) {
            connect();
        }
        return service;
    }

    /**
     * Connect to RMI server
     */
    public static boolean connect() {
        try {
            logger.info("Connecting to RMI server at {}:{}...", SERVER_HOST, SERVER_PORT);
            
            Registry registry = LocateRegistry.getRegistry(SERVER_HOST, SERVER_PORT);
            service = (RemoteService) registry.lookup(SERVICE_NAME);
            
            // Test connection
            service.getDashboardStats(); // Test call
            
            connected = true;
            logger.info("✅ Successfully connected to RMI server");
            return true;
            
        } catch (Exception e) {
            connected = false;
            logger.error("❌ Failed to connect to RMI server: {}", e.getMessage());
            service = null;
            return false;
        }
    }

    /**
     * Check if connected to server
     */
    public static boolean isConnected() {
        if (!connected || service == null) {
            return false;
        }
        
        try {
            // Test connection
            service.getDashboardStats();
            return true;
        } catch (Exception e) {
            connected = false;
            service = null;
            return false;
        }
    }

    /**
     * Disconnect from server
     */
    public static void disconnect() {
        service = null;
        connected = false;
        logger.info("Disconnected from RMI server");
    }

    /**
     * Get server host
     */
    public static String getServerHost() {
        return SERVER_HOST;
    }

    /**
     * Get server port
     */
    public static int getServerPort() {
        return SERVER_PORT;
    }

    /**
     * Reconnect to server
     */
    public static boolean reconnect() {
        disconnect();
        return connect();
    }
}
