package Final;

public class Shipment {

	    private String id;
	    private String customerUsername;
	    private String location;
	    private String items;
	    private String status;
	    private String assignedDriver;

	    public Shipment(String id, String customerUsername, String location, String items, String status, String assignedDriver) {
	        this.id = id;
	        this.customerUsername = customerUsername;
	        this.location = location;
	        this.items = items;
	        this.status = status;
	        this.assignedDriver = assignedDriver;
	    }

	    public String getId() { return id; }
	    public String getCustomerUsername() { return customerUsername; }
	    public String getLocation() { return location; }
	    public String getItems() { return items; }
	    public String getStatus() { return status; }
	    public void setStatus(String status) { this.status = status; }
	    public String getAssignedDriver() { return assignedDriver; }
	    public void setAssignedDriver(String assignedDriver) { this.assignedDriver = assignedDriver; }

	    @Override
	    public String toString() {
	        return id + "," + customerUsername + "," + location + "," + items + "," + status + "," + assignedDriver;
	    }
	}