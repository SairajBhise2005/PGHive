import java.util.*;
import java.text.*;

// Abstract User class with enhanced authentication
abstract class User {
    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected boolean loggedIn = false;
    protected int failedAttempts = 0;

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

    public void logout() {
        loggedIn = false;
        System.out.println("Logged out successfully.");
    }

    public boolean changePassword(String currentPass, String newPass) {
        if (this.password.equals(currentPass)) {
            this.password = newPass;
            System.out.println("Password changed successfully!");
            return true;
        }
        System.out.println("Current password incorrect!");
        return false;
    }

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
            assignRoom(rId, tenant);
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
    
    public List<Tenant> getTenants() {return this.tenants;}
    public void addTenant(Tenant tenant) { tenants.add(tenant); }
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

    public void assignRoom(String roomId, Tenant tenant) {
        Room room = rooms.get(roomId);
        if (room != null && !room.isOccupied()) {
            room.setTenant(tenant);
            tenant.setRoom(room);
            System.out.println("Room assigned successfully!");
        } else {
            System.out.println("Room not available or doesn't exist!");
        }
    }

    public void addRoom(Room room) { rooms.put(room.getRoomId(), room); }
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

// Base Tenant class modified to be more abstract
class Tenant extends User {
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

// Daily payment tenant
class DailyTenant extends Tenant {
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

// Weekly payment tenant
class WeeklyTenant extends Tenant {
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

// Fifteen day payment tenant
class FifteenDayTenant extends Tenant {
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

// Quarterly payment tenant
class QuarterlyTenant extends Tenant {
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

// Bi-yearly payment tenant
class BiYearlyTenant extends Tenant {
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

// Yearly payment tenant
class YearlyTenant extends Tenant {
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

// Room class with proper constant definition
class Room {
    // Define the constant first
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

    public Room(String roomId, double baseRent, double sizeSqft, 
               int amenityScore, String sharingType) {
        // Now we can safely use SHARING_TYPE_FACTORS
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

// Payment class
class Payment {
    private String paymentId;
    private double amount;
    private Date dueDate;
    private boolean paid;

    public Payment(String paymentId, double amount, Date dueDate) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.paid = false;
    }
    public String getPaymentId() { return paymentId; }
    public double getAmount() { return amount; }
    public Date getDueDate() { return dueDate; }
    public boolean isPaid() { return paid; }
    public void markAsPaid() { this.paid = true; }
}

class RentOptimizer {
    // Constants remain in RentOptimizer (better encapsulation)
    private static final Map<String, Double> SHARING_TYPE_FACTORS = Map.of(
        "Single", 1.8,
        "Double", 1.3,
        "Triple", 1.0,
        "Four", 0.8
    );

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

// Main application class
public class PGApp {
    private static Scanner scanner = new Scanner(System.in);
    private static PGOwner owner = new PGOwner("O001", "PG Owner", "owner@pg.com", "admin123");


    public static void main(String[] args) {
        initializeSampleData();
        showMainMenu();
    }

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
        owner.assignRoom("R101", tenant1);

        Tenant tenant2 = new Tenant("T002", "Jane Smith", "jane@example.com", "password456");
        tenant2.setContact("8765432109");
        tenant2.setMoveInDate(new Date());
        owner.addTenant(tenant2);
    }
}