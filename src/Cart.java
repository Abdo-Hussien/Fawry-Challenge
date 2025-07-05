
import java.util.*;

public class Cart {
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
