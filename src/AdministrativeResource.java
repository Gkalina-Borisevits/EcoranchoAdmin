import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class AdministrativeResource {

  public static TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
  public Scanner scanner = new Scanner(System.in);
  public OrdersFile ordersFile;


  public AdministrativeResource(TreeMap<LocalDate, List<Order>> orders) throws IOException {
    this.orders = orders;
    this.ordersFile = new OrdersFile("res/orders.csv", "; ");
  }

  public static TreeMap<LocalDate, Order> interactive(Scanner scanner) {
    TreeMap<LocalDate, Order> orders = new TreeMap<>();

    while (true) {
      addIncomeOrderToFile(scanner);

      System.out.print("Введите 0 для завершения выбора услуг или любую цифру для продолжения:");

      while (!scanner.hasNextInt()) {
        System.out.println("Не корректный ввод: " + scanner.nextLine());
        System.out.print("Введите целое число: ");
      }

      int choice = scanner.nextInt();
      scanner.nextLine();

      if (choice == 0) {
        break;
      }
    }
    return orders;
  }

  public static void addIncomeOrderToFile(Scanner scanner) {
    System.out.print("Введите дату в формате (dd-MM-yyyy): ");
    String date = scanner.nextLine();

    if (date.isEmpty()) {
      System.out.println("Дата окончания не может быть пустой");
      return;
    }

    try {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      LocalDate localDate = LocalDate.parse(date, dateFormatter);

      System.out.print("Введите название: ");
      String serviceName = scanner.nextLine().toUpperCase();
      System.out.print("Введите стоимость услуги: ");
      double price;
      try {
        price = scanner.nextDouble();
      } catch (InputMismatchException e) {
        System.out.println("Некорректный ввод стоимости. Введите числовое значение.");
        return;
      }
      scanner.nextLine();
      Order newOrder = new Order(localDate, serviceName, price, true);

      orders.computeIfAbsent(localDate, k -> new ArrayList<>()).add(newOrder);

      OrdersFile ordersFile = new OrdersFile("res/orders.csv", "; ");
      ordersFile.writeOrders(orders);

    } catch (DateTimeParseException e) {
      System.out.println("Некорректный формат даты. Используйте формат dd-MM-yyyy");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void updateOrders(TreeMap<LocalDate, List<Order>> newOrders) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(ordersFile.toString()))) {
      for (Map.Entry<LocalDate, List<Order>> entry : newOrders.entrySet()) {
        LocalDate orderDate = entry.getKey();
        List<Order> ordersForDate = entry.getValue();

        if (orders.containsKey(orderDate)) {
          orders.get(orderDate).addAll(ordersForDate);
        } else {
          orders.put(orderDate, ordersForDate);
        }

        for (Order order : ordersForDate) {
          String csvLine = order.getCSVLine("; ");
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
