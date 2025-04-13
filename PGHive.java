/*
HOW TO RUN THE APPLICATION:
--------------------------
1. Save this file as PGHive.java
2. Compile: javac PGHive.java
3. Run: java PGHive

LOGIN CREDENTIALS:
-----------------
Owner Login:
Email: owner@pg.com
Password: admin123

Sample Tenant Login:
Email: john@example.com
Password: password123
or
Email: jane@example.com
Password: password456

*/


import java.util.*;
import java.text.*;

/**
 * Interface for classes that need logging capability.
 * Provides a default implementation for logging messages.
 */
interface Loggable {
    /**
     * Logs a message to the console with a "LOG: " prefix.
     * @param message The message to be logged
     */
    default void log(String message) {
        System.out.println("LOG: " + message);
    }
}

/**
 * Custom exception class for handling room assignment errors.
 */
class RoomAssignmentException extends Exception {
    /**
     * Constructs a new RoomAssignmentException with the specified error message.
     * @param message The error message detailing the reason for the exception
     */
    public RoomAssignmentException(String message) {
        super(message);
    }
}

/**
 * Abstract base class for all users in the PG management system.
 * Provides authentication and authorization functionality.
 */
abstract class User {
    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected boolean loggedIn = false;
    protected int failedAttempts = 0;

    /**
     * Authenticates a user with email and password.
     * Implements account lockout after 3 failed attempts.
     * 
     * @param email The email address for authentication
     * @param password The password for authentication
     * @return true if login successful, false otherwise
     */
    public boolean login(String email, String password) {
        if (this.failedAttempts >= 3) {
            System.out.println("Account locked. Too many failed attempts.");
            return false;
        }

        if (this.email.equals(email) && this.password.equals(password)) {
            loggedIn = true;
            failedAttempts = 0;
            System.out.println("Login successful!");
            return true;
        } else {
            failedAttempts++;
            System.out.println("Invalid credentials! Attempts left: " + (3 - failedAttempts));
            return false;
        }
    }

    /**
     * Logs out the current user by setting loggedIn status to false.
     */
    public void logout() {
        loggedIn = false;
        System.out.println("Logged out successfully.");
    }

    /**
     * Changes the user's password if the current password matches.
     * 
     * @param currentPass The current password
     * @param newPass The new password to be set
     * @return true if password changed successfully, false otherwise
     */
    public boolean changePassword(String currentPass, String newPass) {
        if (this.password.equals(currentPass)) {
            this.password = newPass;
            System.out.println("Password changed successfully!");
            return true;
        }
        System.out.println("Current password incorrect!");
        return false;
    }

    /**
     * Abstract method to display the user-specific menu.
     * Must be implemented by subclasses.
     */
    public abstract void showMenu();
}

// PGOwner class with enhanced features
class PGOwner extends User {
    private List<Tenant> tenants = new ArrayList<>();
    private Map<String, Room> rooms = new HashMap<>();

    public PGOwner(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (loggedIn) {
            System.out.println("\n=== OWNER DASHBOARD ===");
            System.out.println("1. Add Tenant");
            System.out.println("2. Edit Tenant");
            System.out.println("3. Delete Tenant");
            System.out.println("4. Add Room");
            System.out.println("5. Assign Room");
            System.out.println("6. View All Tenants");
            System.out.println("7. View All Rooms");
            System.out.println("8. Generate Reports");
            System.out.println("9. Generate Bulk Payments");
            System.out.println("10. Change Password");
            System.out.println("11. Optimize Rent Prices");
            System.out.println("12. Logout");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addTenantUI(scanner);
                    break;
                case 2:
                    editTenantUI(scanner);
                    break;
                case 3:
                    deleteTenantUI(scanner);
                    break;
                case 4:
                    addRoomUI(scanner);
                    break;
                case 5:
                    assignRoomUI(scanner);
                    break;
                case 6:
                    viewAllTenants();
                    break;
                case 7:
                    viewAllRooms();
                    break;
                case 8:
                    generateReport();
                    break;
                case 9:
                    generateBulkPaymentsUI(scanner);
                    break;
                case 10:
                    changePasswordUI(scanner);
                    break;
                case 11:
                    suggestOptimizedRents();
                    break;
                case 12:
                    logout();
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void addTenantUI(Scanner scanner) {
        System.out.println("\n--- Add New Tenant ---");
        System.out.print("Enter Tenant ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String pass = scanner.nextLine();
        System.out.print("Enter Contact: ");
        String contact = scanner.nextLine();
        System.out.print("Enter Move-in Date (yyyy-mm-dd): ");
        Date moveIn = parseDate(scanner.nextLine());
        System.out.print("Enter Move-out Date (yyyy-mm-dd): ");
        Date moveOut = parseDate(scanner.nextLine());
        
        System.out.println("\nSelect Tenant Type:");
        System.out.println("1. Daily");
        System.out.println("2. Weekly");
        System.out.println("3. Fifteen Day");
        System.out.println("4. Quarterly");
        System.out.println("5. Bi-Yearly");
        System.out.println("6. Yearly");
        System.out.print("Enter choice (1-6): ");
        
        int tenantTypeChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        Tenant tenant;
        switch (tenantTypeChoice) {
            case 1:
                tenant = new DailyTenant(id, name, email, pass);
                break;
            case 2:
                tenant = new WeeklyTenant(id, name, email, pass);
                break;
            case 3:
                tenant = new FifteenDayTenant(id, name, email, pass);
                break;
            case 4:
                tenant = new QuarterlyTenant(id, name, email, pass);
                break;
            case 5:
                tenant = new BiYearlyTenant(id, name, email, pass);
                break;
            case 6:
                tenant = new YearlyTenant(id, name, email, pass);
                break;
            default:
                System.out.println("Invalid choice! Creating default tenant type.");
                tenant = new Tenant(id, name, email, pass);
        }

        tenant.setContact(contact);
        tenant.setMoveInDate(moveIn);
        tenant.setMoveOutDate(moveOut);
        addTenant(tenant);
        System.out.println("Tenant added successfully!");
    }

    private void editTenantUI(Scanner scanner) {
        System.out.println("\n--- Edit Tenant ---");
        System.out.print("Enter Tenant ID to edit: ");
        String tid = scanner.nextLine();
        System.out.print("Enter New Name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter New Contact: ");
        String newContact = scanner.nextLine();
        System.out.print("Enter New Move-in Date (yyyy-mm-dd): ");
        Date newMoveIn = parseDate(scanner.nextLine());
        System.out.print("Enter New Move-out Date (yyyy-mm-dd): ");
        Date newMoveOut = parseDate(scanner.nextLine());
        
        editTenant(tid, newName, newContact, newMoveIn, newMoveOut);
    }

    private void deleteTenantUI(Scanner scanner) {
        System.out.println("\n--- Delete Tenant ---");
        System.out.print("Enter Tenant ID to delete: ");
        String deleteId = scanner.nextLine();
        deleteTenant(deleteId);
    }

    private void addRoomUI(Scanner scanner) {
        System.out.println("\n--- Add New Room ---");
        System.out.print("Enter Room ID: ");
        String roomId = scanner.nextLine();
        System.out.print("Enter Base Rent: ");
        double rent = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Size (sqft): ");
        double size = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Amenity Score (1-10): ");
        int amenities = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Sharing Type (Single/Double/Triple/Four): ");
        String sharingType = scanner.nextLine();
        
        try {
            addRoom(new Room(roomId, rent, size, amenities, sharingType));
            System.out.println("Room added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void assignRoomUI(Scanner scanner) {
        System.out.println("\n--- Assign Room ---");
        System.out.print("Enter Room ID: ");
        String rId = scanner.nextLine();
        System.out.print("Enter Tenant ID: ");
        String tId = scanner.nextLine();
        
        Tenant tenant = findTenant(tId);
        if (tenant != null) {
            try {
                assignRoom(rId, tenant);
            } catch (RoomAssignmentException e) {
                System.out.println("Error assigning room: " + e.getMessage());
            }
        } else {
            System.out.println("Tenant not found!");
        }
    }

    private void generateBulkPaymentsUI(Scanner scanner) {
        System.out.println("\n--- Generate Bulk Payments ---");
        System.out.print("Enter number of months to generate payments: ");
        int months = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Start Date (yyyy-mm-dd): ");
        Date startDate = parseDate(scanner.nextLine());
        generateBulkPayments(months, startDate);
    }

    private void changePasswordUI(Scanner scanner) {
        System.out.println("\n--- Change Password ---");
        System.out.print("Enter current password: ");
        String current = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();
        changePassword(current, newPass);
    }

    private void viewAllTenants() {
        System.out.println("\n--- All Tenants ---");
        if (tenants.isEmpty()) {
            System.out.println("No tenants found.");
            return;
        }
        
        for (Tenant t : tenants) {
            System.out.println("ID: " + t.getUserId() + " | Name: " + t.getName() + 
                             " | Email: " + t.getEmail() + " | Room: " + 
                             (t.getRoom() != null ? t.getRoom().getRoomId() : "Not assigned"));
        }
    }

    private void viewAllRooms() {
        System.out.println("\n--- All Rooms ---");
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
            return;
        }
        
        for (Room r : rooms.values()) {
            System.out.println("Room ID: " + r.getRoomId() + " | Rent: ₹" + r.getRent() + 
                              " | Status: " + (r.isOccupied() ? "Occupied" : "Vacant"));
        }
    }

    private Tenant findTenant(String tenantId) {
        for (Tenant t : tenants) {
            if (t.getUserId().equals(tenantId)) {
                return t;
            }
        }
        return null;
    }

    public class RoomValidator {
        public boolean isValid(Room room) {
            return room != null && room.getRoomId() != null;
        }
    }
    
    public List<Tenant> getTenants() {return this.tenants;}
    public void addTenant(Tenant tenant) { tenants.add(tenant); }
    private RoomValidator validator= new RoomValidator();
    public void editTenant(String tenantId, String name, String contact, Date moveIn, Date moveOut) {
        for (Tenant t : tenants) {
            if (t.getUserId().equals(tenantId)) {
                t.setName(name);
                t.setContact(contact);
                t.setMoveInDate(moveIn);
                t.setMoveOutDate(moveOut);
                System.out.println("Tenant details updated.");
                return;
            }
        }
        System.out.println("Tenant not found.");
    }

    public void deleteTenant(String tenantId) {
        if (tenants.removeIf(t -> t.getUserId().equals(tenantId))) {
            System.out.println("Tenant deleted successfully.");
        } else {
            System.out.println("Tenant not found.");
        }
    }

    public void assignRoom(String roomId, Tenant tenant) throws RoomAssignmentException {
        if (roomId == null) {
            throw new RoomAssignmentException("Room ID cannot be null");
        }
        Room room = rooms.get(roomId);
        if (room != null && !room.isOccupied()) {
            room.setTenant(tenant);
            tenant.setRoom(room);
        }
    }

    public void addRoom(Room room) {
        if (validator.isValid(room)) {
            rooms.put(room.getRoomId(), room);
        }
    }
    
    // Overloaded version
    public void addRoom(String roomId, double rent) {
        addRoom(new Room(roomId, rent, 0, 0, "Single"));
    }

    public void generateReport() {
        System.out.println("\n--- PG STATUS REPORT ---");
        System.out.println("Total Tenants: " + tenants.size());
        long occupied = rooms.values().stream().filter(Room::isOccupied).count();
        System.out.println("Occupied Rooms: " + occupied);
        System.out.println("Vacant Rooms: " + (rooms.size() - occupied));
        System.out.println("Occupancy Rate: " + (rooms.size() > 0 ? 
                              (occupied * 100 / rooms.size()) + "%" : "N/A"));
    }
    public void generateBulkPayments(int months, Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        for (Tenant tenant : tenants) {
            Room room = tenant.getRoom();
            if (room != null) {
                for (int i = 0; i < months; i++) {
                    String paymentId = tenant.getUserId() + "-M" + (i+1);
                    Payment payment = new Payment(paymentId, room.getRent(), cal.getTime());
                    tenant.addPayment(payment);
                    cal.add(Calendar.MONTH, 1);
                }
                cal.setTime(startDate);
            }
        }
        System.out.println("Payment records generated for all tenants.");
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Using today's date.");
            return new Date();
        }
    }

    public void suggestOptimizedRents() {
        double occupancyRate = calculateOccupancyRate();
        
        System.out.println("\n=== RENT OPTIMIZATION REPORT ===");
        System.out.printf("Current Occupancy: %.1f%%\n", occupancyRate * 100);
        System.out.println("Sharing Type | Room | Current Rent | Suggested Rent | Change");
        System.out.println("----------------------------------------------------------");
        
        // Define the order of sharing types
        List<String> sharingOrder = Arrays.asList("Single", "Double", "Triple", "Four");
        
        // Sort rooms by sharing type
        rooms.values().stream()
            .sorted((r1, r2) -> {
                int index1 = sharingOrder.indexOf(r1.getSharingType());
                int index2 = sharingOrder.indexOf(r2.getSharingType());
                return Integer.compare(index1, index2);
            })
            .forEach(room -> {
                double current = room.getRent();
                double suggested = RentOptimizer.calculateOptimizedRent(room, occupancyRate);
                double change = ((suggested - current) / current) * 100;
                
                System.out.printf("%-12s | %-4s | ₹%-11.0f | ₹%-13.0f | %+.1f%%\n",
                    room.getSharingType(),
                    room.getRoomId(),
                    current,
                    suggested,
                    change);
            });
    }

    private double calculateOccupancyRate() {
        long occupied = rooms.values().stream().filter(Room::isOccupied).count();
        return rooms.isEmpty() ? 0 : (double) occupied / rooms.size();
    }    

}

/**
 * Represents a tenant who pays rent on a daily basis.
 */
class DailyTenant extends Tenant {
    /**
     * Creates a new tenant with daily payment schedule.
     * 
     * @param userId Unique identifier for the tenant
     * @param name Name of the tenant
     * @param email Email address of the tenant
     * @param password Password for tenant's account
     */
    public DailyTenant(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.paymentPeriodDays = 1;
    }

    @Override
    protected double calculatePaymentAmount(Room room) {
        if (room == null) return 0;
        return room.getRent() / 30; // Daily rate
    }
}

/**
 * Represents a tenant who pays rent on a weekly basis.
 */
class WeeklyTenant extends Tenant {
    /**
     * Creates a new tenant with weekly payment schedule.
     * 
     * @param userId Unique identifier for the tenant
     * @param name Name of the tenant
     * @param email Email address of the tenant
     * @param password Password for tenant's account
     */
    public WeeklyTenant(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.paymentPeriodDays = 7;
    }

    @Override
    protected double calculatePaymentAmount(Room room) {
        if (room == null) return 0;
        return (room.getRent() * 7) / 30; // Weekly rate
    }
}

/**
 * Represents a tenant who pays rent every fifteen days.
 */
class FifteenDayTenant extends Tenant {
    /**
     * Creates a new tenant with fifteen-day payment schedule.
     * 
     * @param userId Unique identifier for the tenant
     * @param name Name of the tenant
     * @param email Email address of the tenant
     * @param password Password for tenant's account
     */
    public FifteenDayTenant(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.paymentPeriodDays = 15;
    }

    @Override
    protected double calculatePaymentAmount(Room room) {
        if (room == null) return 0;
        return room.getRent() / 2; // Half-monthly rate
    }
}

/**
 * Represents a tenant who pays rent on a quarterly basis (every three months).
 */
class QuarterlyTenant extends Tenant {
    /**
     * Creates a new tenant with quarterly payment schedule.
     * 
     * @param userId Unique identifier for the tenant
     * @param name Name of the tenant
     * @param email Email address of the tenant
     * @param password Password for tenant's account
     */
    public QuarterlyTenant(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.paymentPeriodDays = 90;
    }

    @Override
    protected double calculatePaymentAmount(Room room) {
        if (room == null) return 0;
        return room.getRent() * 3; // Three months rent
    }
}

/**
 * Represents a tenant who pays rent every six months.
 */
class BiYearlyTenant extends Tenant {
    /**
     * Creates a new tenant with bi-yearly payment schedule.
     * 
     * @param userId Unique identifier for the tenant
     * @param name Name of the tenant
     * @param email Email address of the tenant
     * @param password Password for tenant's account
     */
    public BiYearlyTenant(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.paymentPeriodDays = 180;
    }

    @Override
    protected double calculatePaymentAmount(Room room) {
        if (room == null) return 0;
        return room.getRent() * 6; // Six months rent
    }
}

/**
 * Represents a tenant who pays rent on a yearly basis.
 */
class YearlyTenant extends Tenant {
    /**
     * Creates a new tenant with yearly payment schedule.
     * 
     * @param userId Unique identifier for the tenant
     * @param name Name of the tenant
     * @param email Email address of the tenant
     * @param password Password for tenant's account
     */
    public YearlyTenant(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.paymentPeriodDays = 365;
    }

    @Override
    protected double calculatePaymentAmount(Room room) {
        if (room == null) return 0;
        return room.getRent() * 12; // Full year rent
    }
}

// Base Tenant class modified to be more abstract
class Tenant extends User implements Loggable {
    private String contact;
    private Room room;
    protected List<Payment> payments = new ArrayList<>();
    private Set<String> documents = new HashSet<>();
    private Date moveInDate, moveOutDate;
    protected int paymentPeriodDays; // Number of days between payments

    public Tenant(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        log("New tenant created.");
    }

    // Overloaded constructors for different scenarios
    public Tenant(String userId, String name, String email) {
        this(userId, name, email, "default-pass");
    }

    public Tenant(String userId) {
        this(userId, "Unknown", "no-email", "default-pass");
    }

    public Tenant(String userId, String name) {
        this(userId, name, "no-email", "default-pass");
    }


    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (loggedIn) {
            System.out.println("\n=== TENANT DASHBOARD ===");
            System.out.println("1. View My Details");
            System.out.println("2. View Rent History");
            System.out.println("3. Upload Document");
            System.out.println("4. View Documents");
            System.out.println("5. Change Password");
            System.out.println("6. Logout");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewDetails();
                    break;
                case 2:
                    viewRentHistory();
                    break;
                case 3:
                    uploadDocumentUI(scanner);
                    break;
                case 4:
                    viewDocuments();
                    break;
                case 5:
                    changePasswordUI(scanner);
                    break;
                case 6:
                    logout();
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void viewDetails() {
        System.out.println("\n--- MY DETAILS ---");
        System.out.println("ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Contact: " + contact);
        System.out.println("Room: " + (room != null ? room.getRoomId() : "Not assigned"));
        System.out.println("Move-in Date: " + formatDate(moveInDate));
        System.out.println("Move-out Date: " + formatDate(moveOutDate));
    }

    private void uploadDocumentUI(Scanner scanner) {
        System.out.print("\nEnter document name to upload: ");
        String doc = scanner.nextLine();
        uploadDocument(doc);
    }

    private void changePasswordUI(Scanner scanner) {
        System.out.println("\n--- CHANGE PASSWORD ---");
        System.out.print("Enter current password: ");
        String current = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();
        changePassword(current, newPass);
    }

    private String formatDate(Date date) {
        if (date == null) return "Not set";
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    // Existing methods
    public String getName() {return this.name;} // name is inherited from User class
    public String getEmail() {return this.email;}  // email is inherited from User class
    public void setName(String name) {this.name = name;}  // Updates the name field inherited from User
    public String getUserId() { return userId; }
    public void setContact(String contact) { this.contact = contact; }
    public void setMoveInDate(Date date) { this.moveInDate = date; }
    public void setMoveOutDate(Date date) { this.moveOutDate = date; }
    public void uploadDocument(String doc) {
        documents.add(doc);
        System.out.println("Document uploaded: " + doc);
    }
    public void viewDocuments() {
        System.out.println("\n--- MY DOCUMENTS ---");
        if (documents.isEmpty()) {
            System.out.println("No documents uploaded.");
            return;
        }
        for (String doc : documents) {
            System.out.println("- " + doc);
        }
    }
    public void addPayment(Payment payment) { payments.add(payment); }
    public void setRoom(Room room) { this.room = room; }
    public Room getRoom() { return room; }
    public void viewRentHistory() {
        System.out.println("\n--- RENT HISTORY ---");
        if (payments.isEmpty()) {
            System.out.println("No payment records found.");
            return;
        }
        for (Payment p : payments) {
            System.out.println("Payment ID: " + p.getPaymentId() + 
                             " | Amount: ₹" + p.getAmount() + 
                             " | Due: " + formatDate(p.getDueDate()) + 
                             " | Status: " + (p.isPaid() ? "Paid" : "Pending"));
        }
    }

    protected double calculatePaymentAmount(Room room) {
        if (room == null) return 0;
        return (room.getRent() * paymentPeriodDays) / 30; // Base calculation using monthly rent
    }
}

/**
 * Represents a room in the PG accommodation.
 * Handles room details, occupancy status, and rent calculations based on various factors.
 */
class Room {
    /**
     * Mapping of sharing types to their corresponding rent multiplier factors.
     * Single rooms have highest factor, decreasing through Double, Triple, and Four sharing.
     */
    private static final Map<String, Double> SHARING_TYPE_FACTORS = Map.of(
        "Single", 1.8,
        "Double", 1.3,
        "Triple", 1.0,
        "Four", 0.8
    );

    private String roomId;
    private boolean occupied;
    private double baseRent;
    private double sizeSqft;
    private int amenityScore;
    private String sharingType;
    private Tenant tenant;

    /**
     * Creates a new Room with specified parameters.
     * 
     * @param roomId Unique identifier for the room
     * @param baseRent Base monthly rent for the room
     * @param sizeSqft Size of the room in square feet
     * @param amenityScore Score representing the quality of amenities (1-10)
     * @param sharingType Type of sharing arrangement (Single/Double/Triple/Four)
     * @throws IllegalArgumentException if sharing type is invalid
     */
    public Room(String roomId, double baseRent, double sizeSqft, 
               int amenityScore, String sharingType) {
        if (!SHARING_TYPE_FACTORS.containsKey(sharingType)) {
            throw new IllegalArgumentException("Invalid sharing type. Must be Single/Double/Triple/Four");
        }
        this.roomId = roomId;
        this.baseRent = baseRent;
        this.sizeSqft = sizeSqft;
        this.amenityScore = amenityScore;
        this.sharingType = sharingType;
        this.occupied = false;
    }

    // Getters
    public static Map<String, Double> getSharingTypeFactors() {
        return SHARING_TYPE_FACTORS;
    }
    
    public double getRent() { return baseRent; }
    public String getRoomId() { return roomId; }
    public boolean isOccupied() { return occupied; }
    public double getBaseRent() { return baseRent; }
    public double getSizeSqft() { return sizeSqft; }
    public int getAmenityScore() { return amenityScore; }
    public String getSharingType() { return sharingType; }
    public Tenant getTenant() { return tenant; }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
        this.occupied = (tenant != null);
    }
}

/**
 * Represents a payment record for a tenant.
 * Tracks payment details including amount, due date, and payment status.
 */
class Payment {
    private String paymentId;
    private double amount;
    private Date dueDate;
    private boolean paid;

    /**
     * Creates a new Payment record.
     * 
     * @param paymentId Unique identifier for the payment
     * @param amount Amount to be paid
     * @param dueDate Date by which payment should be made
     */
    public Payment(String paymentId, double amount, Date dueDate) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.paid = false;
    }

    /**
     * Gets the unique identifier of the payment.
     * @return Payment ID string
     */
    public String getPaymentId() { return paymentId; }

    /**
     * Gets the payment amount.
     * @return Amount to be paid
     */
    public double getAmount() { return amount; }

    /**
     * Gets the payment due date.
     * @return Due date of the payment
     */
    public Date getDueDate() { return dueDate; }

    /**
     * Checks if the payment has been made.
     * @return true if paid, false otherwise
     */
    public boolean isPaid() { return paid; }

    /**
     * Marks the payment as paid.
     */
    public void markAsPaid() { this.paid = true; }
}

/**
 * Utility class for optimizing room rents based on various factors.
 * Calculates suggested rent prices considering room characteristics and occupancy rates.
 */
class RentOptimizer {
    /**
     * Multiplier factors for different sharing types to adjust base rent.
     */
    private static final Map<String, Double> SHARING_TYPE_FACTORS = Map.of(
        "Single", 1.8,
        "Double", 1.3,
        "Triple", 1.0,
        "Four", 0.8
    );

    /**
     * Calculates the optimized rent for a room based on multiple factors.
     * 
     * @param room The room to calculate optimized rent for
     * @param occupancyRate Current occupancy rate of the PG
     * @return Optimized rent amount
     * @throws IllegalArgumentException if room is null
     */
    public static double calculateOptimizedRent(Room room, double occupancyRate) {
        if (room == null) throw new IllegalArgumentException("Room cannot be null");
        
        double optimizedRent = room.getBaseRent(); // Changed from getRent() to getBaseRent()
        
        optimizedRent += room.getSizeSqft() * 8;
        optimizedRent *= 1 + (0.04 * room.getAmenityScore());
        optimizedRent *= SHARING_TYPE_FACTORS.get(room.getSharingType()); // Remove getOrDefault()
        
        if (occupancyRate > 0.75) optimizedRent *= 1.1;
        else if (occupancyRate < 0.4) optimizedRent *= 0.9;
        
        return Math.round(optimizedRent);
    }
}

/**
 * Main application class for the PG (Paying Guest) Management System.
 * Handles user authentication and provides the main menu interface.
 */
public class PGHive {
    private static Scanner scanner = new Scanner(System.in);
    private static PGOwner owner = new PGOwner("O001", "PG Owner", "owner@pg.com", "admin123");

    /**
     * Entry point of the application.
     * Initializes sample data and displays the main menu.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        initializeSampleData();
        showMainMenu();
    }

    /**
     * Helper method to safely assign a room to a tenant.
     * Wraps the room assignment process in exception handling.
     * 
     * @param owner The PG owner making the assignment
     * @param roomId ID of the room to be assigned
     * @param tenant Tenant to whom the room will be assigned
     */
    private static void assignRoomWrapper(PGOwner owner, String roomId, Tenant tenant) {
        try {
            owner.assignRoom(roomId, tenant);
        } catch (RoomAssignmentException e) {
            System.out.println("Error assigning room: " + e.getMessage());
        }
    }
    
    /**
     * Displays and handles the main menu of the application.
     * Provides options for owner login, tenant login, and exit.
     */
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== PG MANAGEMENT SYSTEM ===");
            System.out.println("1. Owner Login");
            System.out.println("2. Tenant Login");
            System.out.println("3. Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    ownerLogin();
                    break;
                case 2:
                    tenantLogin();
                    break;
                case 3:
                    System.out.println("Exiting system... Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    /**
     * Handles the owner login process.
     * Prompts for email and password, and shows the owner menu upon successful login.
     */
    private static void ownerLogin() {
        System.out.println("\n--- OWNER LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        if (owner.login(email, password)) {
            owner.showMenu();
        }
    }

    /**
     * Handles the tenant login process.
     * Searches for the tenant by email and validates their credentials.
     * Shows the tenant menu upon successful login.
     */
    private static void tenantLogin() {
        System.out.println("\n--- TENANT LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
    
        // Get all tenants from the owner
        List<Tenant> allTenants = owner.getTenants();
        
        // Search through all registered tenants
        for (Tenant tenant : allTenants) {
            if (tenant.getEmail().equals(email)) {
                if (tenant.login(email, password)) {
                    tenant.showMenu();
                    return;
                } else {
                    System.out.println("Invalid password!");
                    return;
                }
            }
        }
        System.out.println("Tenant not found! Please check your email or contact the owner.");
    }

    /**
     * Initializes the system with sample data for testing purposes.
     * Creates sample rooms and tenants with default values.
     */
    private static void initializeSampleData() {
        // Add sample rooms
        owner.addRoom(new Room("R101", 6000, 180, 7, "Single"));
        owner.addRoom(new Room("R102", 4500, 220, 6, "Double"));
        owner.addRoom(new Room("R103", 3500, 250, 5, "Triple"));
        owner.addRoom(new Room("R104", 3000, 300, 4, "Four"));
        
        // Add sample tenants
        Tenant tenant1 = new Tenant("T001", "John Doe", "john@example.com", "password123");
        tenant1.setContact("9876543210");
        tenant1.setMoveInDate(new Date());
        owner.addTenant(tenant1);
        try {
            owner.assignRoom("R101", tenant1);
        } catch (RoomAssignmentException e) {
            System.out.println("Error assigning room: " + e.getMessage());
        }

        Tenant tenant2 = new Tenant("T002", "Jane Smith", "jane@example.com", "password456");
        tenant2.setContact("8765432109");
        tenant2.setMoveInDate(new Date());
        owner.addTenant(tenant2);
    }
}