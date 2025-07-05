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

class CartProd {
    Product product;
    int quantity;

    public CartProd(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
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
