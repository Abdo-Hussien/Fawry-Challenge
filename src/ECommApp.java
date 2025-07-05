import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ECommApp {
    public static void main(String[] args) {
        LocalDate localFutureDate = LocalDate.now().plusDays(2); // to make it not expired
        Date future = Date.from(localFutureDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Product cheese = new Cheese("Cheese", 100, 5, future, 0.2);
        // Product cheese = new Cheese("Cheese", 9999, 1, future, 0.4);
        Product biscuits = new Biscuits("Biscuits", 150, 2, future, 0.7);
        Product tv = new TV("TV", 300, 3, 5.0);
        Product scratchCard = new MobileScratchCard("Scratch Card", 50, 10);

        Customer customer = new Customer("John", 1000);

        Cart cart = new Cart();
        cart.add(cheese, 2);
        cart.add(biscuits, 1);
        cart.add(tv, 1);
        cart.add(scratchCard, 1);

        new CheckoutService().checkout(customer, cart);
    }
}

abstract class Product {
    protected String name;
    protected double price;
    protected int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity(int amount) {
        if (amount <= quantity)
            quantity -= amount;
    }

    public boolean isExpired() {
        return false;
    }
}

interface Expirable {
    boolean isExpired();
}

interface Shippable {
    String getName();

    double getWeight();
}

class Cheese extends Product implements Expirable, Shippable {
    private Date expiryDate;
    private double weight;

    public Cheese(String name, double price, int quantity, Date expiryDate, double weight) {
        super(name, price, quantity);
        this.expiryDate = expiryDate;
        this.weight = weight;
    }

    // public boolean expired() {
    // return expiryDate.before(new Date());
    // }

    @Override
    public boolean isExpired() {
        return expiryDate.before(new Date());
    }

    @Override
    public double getWeight() {
        return weight;
    }
}

class Biscuits extends Product implements Shippable, Expirable {
    private Date expiryDate;
    private double weight;

    public Biscuits(String name, double price, int quantity, Date expiryDate, double weight) {
        super(name, price, quantity);
        this.expiryDate = expiryDate;
        this.weight = weight;
    }

    @Override
    public boolean isExpired() {
        return expiryDate.before(new Date());
    }

    public double getWeight() {
        return weight;
    }
}

class TV extends Product implements Shippable {
    private double weight;

    public TV(String name, double price, int quantity, double weight) {
        super(name, price, quantity);
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}

class Mobile extends Product {
    public Mobile(String name, double price, int quantity) {
        super(name, price, quantity);
    }
}

class MobileScratchCard extends Product {
    public MobileScratchCard(String name, double price, int quantity) {
        super(name, price, quantity);
    }
}

class Customer {
    private String name;
    private double balance;

    public Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void subtractFromBalance(double amount) {
        balance -= amount;
    }

    public String getName() {
        return name;
    }
}

class CartProd {
    Product product;
    int quantity;

    public CartProd(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}

class Cart {
    private List<CartProd> items = new ArrayList<>();

    public void add(Product product, int quantity) {
        if (quantity > product.getQuantity()) {
            System.out.println("quantity exceeds available stock.");
            return;
        }
        items.add(new CartProd(product, quantity));
    }

    public List<CartProd> getItems() {
        return items;
    }

    public boolean isCartEmpty() {
        return items.isEmpty();
    }

    public void clearCart() {
        items.clear();
    }
}

class ShippingService {
    public void ship(List<Shippable> shippables, Map<String, Integer> counts) {
        System.out.println("** Shipment notice **");
        double totalWeight = 0;
        for (Shippable item : shippables) {
            String name = item.getName();
            int count = counts.get(name);
            double weight = item.getWeight();
            System.out.printf("%dx %s %.0fg\n", count, name, weight * 1000);
            totalWeight += weight * count;
        }
        System.out.printf("Total package weight %.1fkg\n", totalWeight);
    }
}

class CheckoutService {
    private static final double SHIPPIN_RATE_PER_KG = 30;

    public void checkout(Customer customer, Cart cart) {
        if (cart.isCartEmpty()) {
            System.out.println("Cart is empty. Cannot checkout.");
            return;
        }

        List<CartProd> items = cart.getItems();
        double subtotal = 0;
        double totalShippingWeight = 0;
        List<Shippable> toShip = new ArrayList<>();
        Map<String, Integer> shipCount = new HashMap<>();

        for (CartProd item : items) {
            Product p = item.product;
            int qty = item.quantity;

            if (p instanceof Expirable && ((Expirable) p).isExpired()) {
                System.out.printf("%s is expired. Cannot proceed.\n", p.getName());
                return;
            }

            if (p.getQuantity() < qty) {
                System.out.printf("%s is out of stock.\n", p.getName());
                return;
            }

            subtotal += p.getPrice() * qty;

            if (p instanceof Shippable) {
                toShip.add((Shippable) p);
                shipCount.put(p.getName(), qty);
                totalShippingWeight += ((Shippable) p).getWeight() * qty;
            }
        }

        double shippingFee = totalShippingWeight > 0 ? SHIPPIN_RATE_PER_KG : 0;
        double total = subtotal + shippingFee;

        if (customer.getBalance() < total) {
            System.out.println("Insufficient balance.");
            return;
        }

        if (!toShip.isEmpty()) {
            new ShippingService().ship(toShip, shipCount);
        }
        System.out.println("** Checkout receipt **");

        for (CartProd item : items) {
            System.out.printf("%dx %s %.0f\n", item.quantity, item.product.getName(),
                    item.product.getPrice() * item.quantity);
            item.product.reduceQuantity(item.quantity);
        }
        System.out.println("----------------------");
        System.out.printf("Subtotal %.0f\n", subtotal);
        System.out.printf("Shipping %.0f\n", shippingFee);
        System.out.printf("Amount %.0f\n", total);

        customer.subtractFromBalance(total);
        System.out.printf("Customer balance after payment: %.2f\n", customer.getBalance());

        cart.clearCart();
    }
}

// private static Date getFutureDate(int days) {
// return
// Date.from(LocalDate.now().plusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant());
// }

// private static Date getPastDate(int days) {
// return
// Date.from(LocalDate.now().minusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant());
// }
