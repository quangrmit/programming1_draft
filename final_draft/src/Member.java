import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Member extends User {
    private String customerId;
    private String userName;
    private String password;
    private String fullName;
    private int phoneNumber;
    private double spending;
    private double discount = 0;
    private final long SILVER_MEM = 5_000_000;
    private final long GOLD_MEM = 10_000_000;
    private final long DIAMOND_MEM = 25_000_000;
    private static int count = 0;
    private static boolean loggedIn = false;
    public static Member currentUser = null;

    public Member() {

    }

    public Member(String userName, String password, String fullName, int phoneNumber) throws IOException {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.spending = 0;
        count++;
        customerId = "u" + count;

        File f = new File("Customer.csv");
        Scanner file = new Scanner(f);
        while (file.hasNextLine()) {
            String fileLine = file.nextLine();
            String[] fileLineArray = fileLine.split(",");
            if (fileLine != "") {
                if (fileLineArray[1].equals(userName)) {
                    System.out.println("User Name already exists!");
                    return;
                }
            }

        }

        System.out.println("Create successfully!");
        BufferedWriter out = new BufferedWriter(new FileWriter("Customer.csv", true));
        out.write(this.toString() + "\n");
        out.close();
    }

    public Member(String[] data) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Field[] f = this.getClass().getDeclaredFields();

        for (int i = 0; i < data.length; i++) {
            if (!(f[i].getType().equals(((Object) data[i]).getClass()))) {

                Object wrapped = f[i].get(this);
                Class<?> c = wrapped.getClass();

                Method m = (c.getMethod("valueOf", ((Object) data[i]).getClass()));
                Object j = m.invoke(null, data[i]);
                f[i].set(this, j);
            } else {
                f[i].set(this, data[i]);
            }
        }
    }

    public static Member createMember() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Username: ");
        String username = sc.nextLine();
        System.out.println("Password: ");
        String pass = sc.nextLine();
        System.out.println("Full Name: ");
        String fullName = sc.nextLine();
        System.out.println("Phone number: ");
        try {
            int phoneNum = sc.nextInt();
            sc.nextLine();
            return new Member(username, pass, fullName, phoneNum);
        } catch (Exception e) {
            System.out.println("Invalid input");
            createMember();
        }

        return null;
    }

    public static boolean loginMember() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Username: ");
        String username = sc.nextLine();
        System.out.println("Password: ");
        String pass = sc.nextLine();

        File f = new File("Customer.csv");
        Scanner file = new Scanner(f);
        while (file.hasNextLine()) {
            String fileLine = file.nextLine();
            String[] fileLineArray = fileLine.split(",");
            if (fileLine != "") {
                if (fileLineArray[1].equals(username) && fileLineArray[2].equals(pass)) {
                    System.out.println("Log in Success");
                    System.out.println("-".repeat(20));
                    System.out.println("Hello " + username + ".");
                    // currentUser = username;
                    loggedIn = true;
                    return loggedIn;
                }
            }

        }
        System.out.println("Wrong username or pass");
        return false;
    }

    public void displayInfo() throws FileNotFoundException {
        // File f = new File("Customer.csv");
        // Scanner file = new Scanner(f);
        // while (file.hasNextLine()) {
        // String fileLine = file.nextLine();
        // String[] fileLineArray = fileLine.split(",");
        // if (fileLine != "") {
        // if (fileLineArray[1].equals(currentUser.getUserName())) {
        // return "Member{" +
        // "customerId=" + fileLineArray[0] +
        // ", userName='" + fileLineArray[1] + '\'' +
        // ", password='" + fileLineArray[2] + '\'' +
        // ", fullName='" + fileLineArray[3] + '\'' +
        // ", phoneNumber=" + fileLineArray[4] +
        // ", spending=" + fileLineArray[5] +
        // '}';
        // }
        // }
        // }
        // return null;
        if (loggedIn) {
            (new PersonalMemberDatabase()).displayAll();
        }
    }

    public void viewAllProducts() {
        if (loggedIn) {
            (new ProductDatabase()).displayAll();
        }
    }

    public void searchProductByCategory() {
        if (loggedIn) {
            (new ProductDatabase()).displayByCategory();
            ;
        }
    }

    public void displayByPrice() throws FileNotFoundException {
        if (loggedIn) {
            (new ProductDatabase()).displayByPrice();
        }
    }

    public void viewOrderByID() {
        if (loggedIn) {
            (new PersonalOrderDatabase()).viewOrderByID();
        }
    }

    public void checkUpgrade() {
        if (this.spending > DIAMOND_MEM) {
            this.discount = 0.15;
        } else if (this.spending > GOLD_MEM) {
            this.discount = 0.1;
        } else if (this.spending > SILVER_MEM) {
            this.discount = 0.05;
        }
    }

    public void buy(Order order) {
        this.spending = (this.getSpending() + order.getBill());
        this.checkUpgrade();
    }

    public double getDiscount() {
        return this.discount;
    }

    public double getSpending() {
        return this.spending;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void register(String userName, String password) {

    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return customerId + "," + userName + "," + password + "," + fullName + "," + phoneNumber + "," + spending;
    }
}
