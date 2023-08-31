import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

public class AdministrativeResource {
  private TreeMap<LocalDate, List<Order>> orders;

  public AdministrativeResource(TreeMap<LocalDate, List<Order>> orders) {
    this.orders = new TreeMap<>();
  }
}
