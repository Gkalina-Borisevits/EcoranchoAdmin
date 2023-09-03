import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class OrdersFile {

  private final File file;
  private final boolean isIncome;
  private final String separator;

  /**
   *Конструктор принимающий параметры класса OrdersFile
   * @param fileName - имя файла, с которым работает программа
   * @param delimiter - сепаратор для разделения строки в файле на отдельные смысловые задачи
   * @throws IOException - обработка исключений, вызванная при выполнении операций ввода и вывода из файла
   */
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
    this.isIncome = true;
    this.separator = delimiter;
  }


  /**
   * Чтение полей класса Order из файла с помощью Scanner,
   * метод использует Order.parseFromCSVLine - метод для 'парсинга' строк из файла
   * @return TreeMap, которая собирается из LocalDate и ArrayList<>()
   * @throws FileNotFoundException - обработка исключений при чтении из файла, если метод не может найти читаемый файл
   */
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

  /**
   * Метод принимает на вход TreeMap и предназначен для записи с помощью BufferedWriter данных в файл
   * путем перебора пар ключ - значение, дальше вычленяем значение, заносим его в List благодаря
   * методу order.getCSVLine, которому на вход подается сепаратор для разделения значений
   * @param orders Метод принимает на вход TreeMap
   * @throws IOException - обработка исключений, вызванная при выполнении операций ввода и вывода из файла
   */
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

  /**
   *
   * @param order - параметр
   * @param isIncome
   * @throws IOException
   */
  public void writeOrder(Order order, boolean isIncome) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
      String csvLine = order.getCSVLine(separator);
      writer.write(csvLine);
      writer.newLine();
    } catch (IOException e) {
      System.out.println("Ошибка при сохранении заказа");
    }
  }
}


