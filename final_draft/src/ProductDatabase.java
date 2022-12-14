import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ProductDatabase extends Database implements Public {
    private static ArrayList<Product> products = new ArrayList<>();

    // private static File file = new File("../Product.csv");
    private static File file = new File("Product.csv");
    private static boolean loaded = false;
    private static String header = "";

    // private ProductDatabase() {
    // loadProductDatabase();
    // }
    public Product getProduct() {

        if (this.checkCompatibility()) {

            Scanner sc = new Scanner(System.in);
            Product result = null;

            System.out.println("Enter product name");
            String productName = sc.nextLine();
            if (productName.equals("q")) {
                return null;
            } else {
                for (Product product : products) {
                    if (product.getName().equals(productName)) {
                        result = product;
                    }
                }
                return result;
            }
        } else {
            System.out.println("You don't have access to this functionality");
            return null;
        }

    }

    static void updateProductDatabase() {
        try {

            BufferedWriter out = new BufferedWriter(new FileWriter(file.getPath(), false));

            out.write(header + "\n");
            for (Product product : products) {
                out.write(product.toString() + "\n");
            }
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void loadProductDatabase() {
        if (loaded)
            return;
        try {

            Scanner sc = new Scanner(file);
            header = sc.nextLine();
            while (sc.hasNextLine()) {
                String[] productData = sc.nextLine().split(",");

                Product newProduct = new Product(productData);
                products.add(newProduct);
            }
            loaded = true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayAll() {

        if (!this.checkCompatibility()) {
            return;
        }
        loadProductDatabase();
        TableGenerator.printTable(file);
    }

    public void displayByCategory() {

        if (!this.checkCompatibility()) {
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter category: ");
        String input = sc.nextLine();
        ArrayList<Product> results = new ArrayList<Product>();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getCategory().equals(input) && products.get(i).getQuantity() > 0) {
                results.add(products.get(i));
            }
        }
        TableGenerator.printTable(results, header);
    }

    //

    public void displayByPrice() {

        if (!this.checkCompatibility()) {
            return;
        }
        loadProductDatabase();
        int numsProduct = products.size();
        Product[] sortPrice = new Product[numsProduct];

        for (int i = 0; i < numsProduct; i++) {
            sortPrice[i] = products.get(i);
        }
        // Sort product base on price
        Arrays.sort(sortPrice, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return (int) (p1.getPrice() - p2.getPrice());
            }
        });

        List<Product> list = Arrays.asList(sortPrice);

        // for (Product i : sortPrice) {
        // i.displayGeneralInfo();
        // }
        TableGenerator.printTable(list, header);
    }

    static void updateQuantity(HashMap<String, Integer> productQuantity) {
        for (String i : productQuantity.keySet()) {
            int index = Integer.parseInt(i.substring(1)) - 1;
            products.get(index).setQuantity(productQuantity.get(i));
        }
    }

    public void displaySpecific() {

        if (!this.checkCompatibility()) {
            return;
        }
        // consider doing index search to be more efficient later
        loadProductDatabase();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the product id: ");
        String id = input.nextLine();
        // input.close();
        for (Product product : products) {
            if (product.getID().equals(id)) {
                product.displayDetailInfo();
                return;
            }
        }
        System.out.println("Product with that id doesn't exist");

    }

    // For admin
    // addProduct
    public void addProduct() {

        if (!this.checkCompatibility()) {
            return;
        }
        loadProductDatabase();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter product name:");
        String name = input.nextLine();
        System.out.println("Enter product description:");
        String description = input.nextLine();
        System.out.println("Enter product category:");
        String category = input.nextLine();
        System.out.println("Enter product quantity:");
        String quantity = input.nextLine();
        System.out.println("Enter product price:");
        String price = input.nextLine();
        products.add(new Product(name, description, category, price, quantity));
        // Category.addCategory(category);
        System.out.println("Product added successfully");
        updateProductDatabase();
    }

    public void updatePrice() {

        if (!this.checkCompatibility()) {
            return;
        }
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the wanted update price product");
        String upPriceProduct = input.nextLine();

        // Add validation to the input later (MUST ADD);

        int wantedIndex = Integer.parseInt(upPriceProduct.substring(1)) - 1;
        Product wantedProduct = products.get(wantedIndex);
        System.out.println("Enter wanted price: ");
        double newPrice = input.nextDouble();
        wantedProduct.setPrice(newPrice);
        // input.close();
        updateProductDatabase();
    }

    @Override
    public ArrayList<Product> getList() {
        return products;
    }

    @Override
    public String getHeader() {
        return header;
    }

}
