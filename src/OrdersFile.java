import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class OrdersFile {

  private final File file;
  private final String separator;

  public OrdersFile(String fileName, String delimiter) throws IOException {
    file = new File(fileName);
    if (!file.exists()) {
      if (!file.getParentFile().mkdirs() || !file.createNewFile()) {
        throw new RuntimeException("Не получилось создать файл: " + fileName);
      }
    }
    if (!(file.canRead() && file.canWrite())) {
      throw new IllegalArgumentException("Файл не доступен: " + fileName);
    }
    this.separator = delimiter;
  }

  public TreeMap<LocalDate, List<Order>> readOrder() throws FileNotFoundException {
    TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
    Scanner scanner = new Scanner(file);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      Order order = Order.parseFromCSVLine(line, separator);
      LocalDate orderDate = order.getLocalDate();
      List<Order> ordersDate = orders.getOrDefault(orderDate, new ArrayList<>());
      ordersDate.add(order);
      orders.put(orderDate, ordersDate);
    }
    scanner.close();
    return orders;
  }

  public void writeOrders(TreeMap<LocalDate, List<Order>> orders) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      for (Map.Entry<LocalDate, List<Order>> entry : orders.entrySet()) {
        LocalDate orderDate = entry.getKey();
        List<Order> ordersForDate = entry.getValue();

        for (Order order : ordersForDate) {
          String csvLine = order.getCSVLine(separator);
          writer.write(csvLine);
          writer.newLine();
        }
      }
    } catch (IOException e) {
      System.out.println("Ошибка при сохранении заказов");
      e.printStackTrace();
    }
  }
}

