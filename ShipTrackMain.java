package Final;

import java.util.List;
import java.util.Scanner;

public class ShipTrackMain {
	    private static Scanner scanner = new Scanner(System.in);
	    private static SecurityConfig config = new SecurityConfig();
	    private static AuthManager auth = new AuthManager(config);
	    private static ShipmentManager shipments = new ShipmentManager();

	    public static void main(String[] args) {
	        System.out.println("=== SkyRoute Solutions: ShipTrack ===");

	        if (!auth.hasUsers()) {
	            System.out.println("No users found. Creating initial System Admin.");
	            System.out.print("Admin Username: "); String aUser = scanner.nextLine();
	            System.out.print("Admin Password: "); String aPass = scanner.nextLine();
	            auth.registerUser(aUser, aPass, "System Admin", "Admin", "000", "000");
	        }

	        while (true) {
	            System.out.println("\n--- MAIN MENU ---");
	            System.out.println("1. Login");
	            System.out.println("2. Customer Registration");
	            System.out.println("3. Exit");
	            System.out.print("Select: ");
	            String choice = scanner.nextLine();

	            if (choice.equals("1")) {
	                System.out.print("Username: "); String user = scanner.nextLine();
	                System.out.print("Password: "); String pass = scanner.nextLine();
	                User loggedIn = auth.authenticate(user, pass);
	                if (loggedIn != null) routeUser(loggedIn);
	            } else if (choice.equals("2")) {
	                registerFlow("Customer");
	            } else if (choice.equals("3")) {
	                break;
	            }
	        }
	    }

	    private static void routeUser(User user) {
	        switch (user.getRole()) {
	            case "System Admin": adminDashboard(user); break;
	            case "Dispatcher": dispatcherDashboard(user); break;
	            case "Customer": customerDashboard(user); break;
	            case "Delivery": deliveryDashboard(user); break;
	        }
	    }

	    // --- DASHBOARDS ---

	    private static void adminDashboard(User user) {
	        while (true) {
	            System.out.println("\n=== SYSTEM ADMIN DASHBOARD ===");
	            System.out.println("1. Register New Dispatcher");
	            System.out.println("2. Remove Dispatcher/Delivery Personnel");
	            System.out.println("3. Configure Password Strength Requirements");
	            System.out.println("4. Configure Max Login Attempts");
	            System.out.println("5. Lock/Unlock User Accounts");
	            System.out.println("6. Logout");
	            System.out.print("Select: ");
	            String choice = scanner.nextLine();

	            if (choice.equals("1")) registerFlow("Dispatcher");
	            else if (choice.equals("2")) {
	                System.out.print("Enter username to remove: ");
	                auth.removeUser(scanner.nextLine());
	            } 
	            else if (choice.equals("3")) {
	                System.out.print("Min Length: "); config.minLength = Integer.parseInt(scanner.nextLine());
	                System.out.print("Min Uppercase: "); config.minUpper = Integer.parseInt(scanner.nextLine());
	                System.out.print("Min Lowercase: "); config.minLower = Integer.parseInt(scanner.nextLine());
	                System.out.print("Min Digits: "); config.minDigit = Integer.parseInt(scanner.nextLine());
	                System.out.print("Min Special: "); config.minSpecial = Integer.parseInt(scanner.nextLine());
	                config.saveConfig();
	                System.out.println("Password policies updated.");
	            }
	            else if (choice.equals("4")) {
	                System.out.print("Set max failed login attempts: ");
	                config.maxAttempts = Integer.parseInt(scanner.nextLine());
	                config.saveConfig();
	                System.out.println("Max attempts updated.");
	            }
	            else if (choice.equals("5")) {
	                System.out.print("Enter username: "); String targetUser = scanner.nextLine();
	                System.out.print("Lock (true) or Unlock (false)?: ");
	                boolean status = Boolean.parseBoolean(scanner.nextLine());
	                auth.lockUnlockUser(targetUser, status);
	            }
	            else if (choice.equals("6")) break;
	        }
	    }

	    private static void dispatcherDashboard(User user) {
	        while (true) {
	            System.out.println("\n=== DISPATCHER DASHBOARD ===");
	            System.out.println("1. Register New Delivery Personnel");
	            System.out.println("2. Assign Deliveries to Drivers");
	            System.out.println("3. Update Delivery Status");
	            System.out.println("4. View/Update My Personnel Info");
	            System.out.println("5. Logout");
	            System.out.print("Select: ");
	            String choice = scanner.nextLine();

	            if (choice.equals("1")) registerFlow("Delivery");
	            else if (choice.equals("2")) {
	                System.out.println("--- All Shipments ---");
	                for(Shipment s : shipments.getAllShipments()) {
	                    System.out.println(s.getId() + " | Customer: " + s.getCustomerUsername() + " | Driver: " + s.getAssignedDriver());
	                }
	                System.out.print("Enter Shipment ID: "); String sId = scanner.nextLine();
	                System.out.print("Enter Delivery Personnel Username: "); String dId = scanner.nextLine();
	                Shipment s = shipments.getShipmentById(sId);
	                if(s != null) {
	                    s.setAssignedDriver(dId);
	                    shipments.saveShipmentsToFile();
	                    SystemLogger.logAction("DISPATCH ACTION: Dispatcher [" + user.getUsername() + "] assigned Shipment [" + sId + "] to Driver [" + dId + "]");
	                    System.out.println("Driver assigned.");
	                } else { System.out.println("Shipment not found."); }
	            }
	            else if (choice.equals("3")) {
	                System.out.print("Enter Shipment ID: "); String sId = scanner.nextLine();
	                Shipment s = shipments.getShipmentById(sId);
	                if(s != null) {
	                    System.out.println("Select Status[cite: 63]: 1. pending, 2. in transit, 3. delivered");
	                    String statChoice = scanner.nextLine();
	                    if(statChoice.equals("1")) s.setStatus("pending");
	                    else if(statChoice.equals("2")) s.setStatus("in transit");
	                    else if(statChoice.equals("3")) s.setStatus("delivered");
	                    shipments.saveShipmentsToFile();
	                    SystemLogger.logAction("STATUS UPDATE: Shipment [" + sId + "] updated to [" + s.getStatus() + "] by [" + user.getUsername() + "]");
	                    System.out.println("Status updated.");
	                } else { System.out.println("Shipment not found!"); }
	            }
	            else if (choice.equals("4")) updatePersonalInfoFlow(user);
	            else if (choice.equals("5")) break;
	        }
	    }

	    private static void customerDashboard(User user) {
	        while (true) {
	            System.out.println("\n=== CUSTOMER DASHBOARD ===");
	            System.out.println("1. Create Shipment Request");
	            System.out.println("2. Track Package Status");
	            System.out.println("3. View/Update Personal Info");
	            System.out.println("4. Logout");
	            System.out.print("Select: ");
	            String choice = scanner.nextLine();

	            if (choice.equals("1")) {
	                System.out.print("Enter Delivery Location: "); String loc = scanner.nextLine();
	                System.out.print("Enter Items (comma separated): "); String items = scanner.nextLine();
	                shipments.createShipment(user.getUsername(), loc, items);
	            }
	            else if (choice.equals("2")) {
	                List<Shipment> myShipments = shipments.getShipmentsByCustomer(user.getUsername());
	                System.out.println("--- My Shipments ---");
	                for (Shipment s : myShipments) {
	                    System.out.println("ID: " + s.getId() + " | Status: " + s.getStatus() + " | Driver: " + s.getAssignedDriver());
	                }
	            }
	            else if (choice.equals("3")) updatePersonalInfoFlow(user);
	            else if (choice.equals("4")) break;
	        }
	    }

	    private static void deliveryDashboard(User user) {
	        while (true) {
	            System.out.println("\n=== DELIVERY PERSONNEL DASHBOARD ===");
	            System.out.println("1. View Assigned Deliveries");
	            System.out.println("2. Update Delivery Status");
	            System.out.println("3. Logout");
	            System.out.print("Select: ");
	            String choice = scanner.nextLine();

	            if (choice.equals("1")) {
	                List<Shipment> myJobs = shipments.getShipmentsByDriver(user.getUsername());
	                SystemLogger.logAction("DATA ACCESSED: Delivery driver [" + user.getUsername() + "] viewed their assigned deliveries.");
	                System.out.println("--- Assigned Deliveries ---");
	                for (Shipment s : myJobs) {
	                    System.out.println("ID: " + s.getId() + " | Loc: " + s.getLocation() + " | Status: " + s.getStatus());
	                }
	            }
	            else if (choice.equals("2")) {
	                System.out.print("Enter Shipment ID to update: "); String sId = scanner.nextLine();
	                Shipment s = shipments.getShipmentById(sId);
	                if(s != null && s.getAssignedDriver().equals(user.getUsername())) {
	                    System.out.println("Select Status[cite: 73]: 1. picked up, 2. in transit, 3. delivered");
	                    String statChoice = scanner.nextLine();
	                    if(statChoice.equals("1")) s.setStatus("picked up");
	                    else if(statChoice.equals("2")) s.setStatus("in transit");
	                    else if(statChoice.equals("3")) s.setStatus("delivered");
	                    shipments.saveShipmentsToFile();
	                    SystemLogger.logAction("STATUS UPDATE: Shipment [" + sId + "] updated to [" + s.getStatus() + "] by [" + user.getUsername() + "]");
	                    System.out.println("Status updated.");
	                } else { System.out.println("Shipment not found or not assigned to you!"); }
	            }
	            else if (choice.equals("3")) break;
	        }
	    }

	    // Helper: Registration
	    private static void registerFlow(String role) {
	        System.out.println("--- Registering " + role + " ---");
	        System.out.print("Username: "); String u = scanner.nextLine();
	        System.out.print("Password: "); String p = scanner.nextLine();
	        System.out.print("Full Name: "); String n = scanner.nextLine();
	        System.out.print("ID Number: "); String id = scanner.nextLine();
	        System.out.print("Contact Number: "); String c = scanner.nextLine();
	        auth.registerUser(u, p, role, n, id, c);
	    }

	    // Helper: Update Personal Info
	    private static void updatePersonalInfoFlow(User user) {
	        System.out.println("--- Current Information ---");
	        System.out.println("Name: " + user.getFullName());
	        System.out.println("ID Number: " + user.getIdNumber());
	        System.out.println("Contact: " + user.getContactNumber());
	        System.out.println("---------------------------");
	        System.out.print("Enter new Name (or press Enter to skip): ");
	        String n = scanner.nextLine();
	        if (!n.isEmpty()) user.setFullName(n);

	        System.out.print("Enter new ID Number (or press Enter to skip): ");
	        String id = scanner.nextLine();
	        if (!id.isEmpty()) user.setIdNumber(id);

	        System.out.print("Enter new Contact (or press Enter to skip): ");
	        String c = scanner.nextLine();
	        if (!c.isEmpty()) user.setContactNumber(c);

	        auth.saveUsersToFile(); // Live save
	        SystemLogger.logAction("PROFILE UPDATE: User [" + user.getUsername() + "] modified their personal information.");
	        System.out.println("Information updated.");
	    }
	}