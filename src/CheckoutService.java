
import java.util.*;

public class CheckoutService {
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
