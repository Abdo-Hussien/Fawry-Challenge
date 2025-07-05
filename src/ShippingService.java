
import java.util.*;

public class ShippingService {
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
