package Final;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ShipmentManager {
	    private static final String SHIPMENTS_FILE = "shipments.txt";
	    private List<Shipment> shipmentsDatabase = new ArrayList<>();

	    public ShipmentManager() {
	        loadShipmentsFromFile();
	    }

	    private void loadShipmentsFromFile() {
	        shipmentsDatabase.clear();
	        File file = new File(SHIPMENTS_FILE);
	        if (!file.exists()) return;

	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                if (line.trim().isEmpty()) continue;
	                String[] parts = line.split(",");
	                if (parts.length == 6) {
	                    shipmentsDatabase.add(new Shipment(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
	                }
	            }
	        } catch (IOException e) {
	            System.err.println("Error loading shipments.");
	        }
	    }

	    public void saveShipmentsToFile() {
	        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SHIPMENTS_FILE))) {
	            for (Shipment s : shipmentsDatabase) {
	                bw.write(s.toString());
	                bw.newLine();
	            }
	        } catch (IOException e) {
	            System.err.println("Error saving shipments.");
	        }
	    }

	    public void createShipment(String customer, String location, String items) {
	        String newId = "SHP" + System.currentTimeMillis(); // Simple unique ID generator
	        Shipment newShipment = new Shipment(newId, customer, location, items, "pending", "Unassigned");
	        shipmentsDatabase.add(newShipment);
	        saveShipmentsToFile();
	        SystemLogger.logAction("SHIPMENT CREATED: ID [" + newId + "] by Customer [" + customer + "]");
	        System.out.println("Shipment request created successfully. ID: " + newId);
	    }

	    public List<Shipment> getShipmentsByCustomer(String customerUsername) {
	        List<Shipment> result = new ArrayList<>();
	        for (Shipment s : shipmentsDatabase) {
	            if (s.getCustomerUsername().equals(customerUsername)) result.add(s);
	        }
	        return result;
	    }

	    public List<Shipment> getShipmentsByDriver(String driverUsername) {
	        List<Shipment> result = new ArrayList<>();
	        for (Shipment s : shipmentsDatabase) {
	            if (s.getAssignedDriver().equals(driverUsername)) result.add(s);
	        }
	        return result;
	    }

	    public List<Shipment> getAllShipments() {
	        return shipmentsDatabase;
	    }

	    public Shipment getShipmentById(String id) {
	        for (Shipment s : shipmentsDatabase) {
	            if (s.getId().equals(id)) return s;
	        }
	        return null;
	    }
	}