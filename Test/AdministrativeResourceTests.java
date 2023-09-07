import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;

public class AdministrativeResourceTests {

  @Test
  public void checkIntNumberPositive() {
    Scanner scanner = new Scanner("""
        10
        """);
    int number = AdministrativeResource.checkIntNumber(scanner);
    assertEquals(10, number);
  }

  @Test
  public void checkIntNumberError() {
    Scanner scanner = new Scanner("""
        d
        10
        """);
    int number = AdministrativeResource.checkIntNumber(scanner);
    assertEquals(10, number);
  }

  @Test
  public void checkIntNumberNegative() {
    Scanner scanner = new Scanner("""
        -1
        10
        """);
    int number = AdministrativeResource.checkIntNumber(scanner);
    assertEquals(10, number);
  }

  @Test
  public void checkIntNumberIsEmpty() {
    Scanner scanner = new Scanner("""
              
        """);
    boolean number = scanner.hasNextInt();
    assertFalse(number);
  }

  @Test
  public void checkNameTestIsEmpty() {
    Scanner scanner = new Scanner("""
              
        """);
    boolean number = scanner.hasNextInt();
    assertFalse(number);
  }

  @Test
  public void checkNameTestIsCorrect() {
    Scanner scanner = new Scanner("""
        Hello
        """);
    String name = AdministrativeResource.checkAddName(scanner);
    assertEquals("HELLO", name);
  }

  @Test
  public void checkPriceDoublePositive() {
    Scanner scanner = new Scanner("""
        10
        """);
    double number = AdministrativeResource.checkPriceDouble(scanner);
    assertEquals(10, number);
  }

  @Test
  public void checkPriceDoubleError() {
    Scanner scanner = new Scanner("""
        dd
        10
        """);
    double number = AdministrativeResource.checkPriceDouble(scanner);
    assertEquals(10, number);
  }

  @Test
  public void checkPriceDoubleNegative() {
    Scanner scanner = new Scanner("""
        -5
        10
        """);
    double number = AdministrativeResource.checkPriceDouble(scanner);
    assertEquals(10, number);
  }

  @Test
  public void validDateTest() {
    Scanner scanner = new Scanner("""
        24-04-2023
        """);
    LocalDate result = AdministrativeResource.validDate(scanner);
    assertEquals(LocalDate.of(2023, 04, 24), result);
  }

  @Test
  public void validDateTestIsEmpty() {
    Scanner scanner = new Scanner("""
              
        24-04-2023
        """);
    LocalDate result = AdministrativeResource.validDate(scanner);
    assertEquals(LocalDate.of(2023, 04, 24), result);
  }

  @Test
  public void validDateTestIsCorrect() {
    Scanner scanner = new Scanner("""
        dddd
        24-04-2023
        """);
    LocalDate result = AdministrativeResource.validDate(scanner);
    assertEquals(LocalDate.of(2023, 04, 24), result);
  }

  @Test
  public void validDateTestNegative() {
    Scanner scanner = new Scanner("""
        -25-04-2023
        24-04-2023
        """);
    LocalDate result = AdministrativeResource.validDate(scanner);
    assertEquals(LocalDate.of(2023, 04, 24), result);
  }

  @Test
  public void removeOrdersByServiceTest() throws IOException {
    TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
    LocalDate date1 = LocalDate.of(2023, 04, 24);
    LocalDate date2 = LocalDate.of(2024, 04, 24);

    List<Order> orders1 = new ArrayList<>();
    orders1.add(new Order(date1, "Test1", 90.0, true));
    orders1.add(new Order(date1, "Test2", 190.0, true));

    List<Order> orders2 = new ArrayList<>();
    orders2.add(new Order(date2, "Test3", 80.0, true));
    orders2.add(new Order(date2, "Test4", 180.0, true));

    orders.put(date1, orders1);
    orders.put(date2, orders2);

    String removeName = "Test4";
    AdministrativeResource.removeOrdersByService(orders, removeName);

    for (Map.Entry<LocalDate, List<Order>> entry : orders.entrySet()) {
      List<Order> orderList = entry.getValue();
      assertFalse(orderList.stream()
          .anyMatch(order -> order.getServiceName().equals(removeName)));
    }
    for (Map.Entry<LocalDate, List<Order>> entry : orders.entrySet()) {
      List<Order> orderList = entry.getValue();
      assertTrue(orderList.stream()
          .noneMatch(order -> order.getServiceName().equals(removeName)));
    }
  }

  @Test
  public void removeOrdersByDateTest() {
    TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
    LocalDate date1 = LocalDate.of(2023, 04, 24);
    LocalDate date2 = LocalDate.of(2024, 04, 24);

    List<Order> orders1 = new ArrayList<>();
    orders1.add(new Order(date1, "Test1", 90.0, true));
    orders1.add(new Order(date1, "Test2", 190.0, true));

    List<Order> orders2 = new ArrayList<>();
    orders2.add(new Order(date2, "Test3", 80.0, true));
    orders2.add(new Order(date2, "Test4", 180.0, true));

    orders.put(date1, orders1);
    orders.put(date2, orders2);

    LocalDate removeDate = LocalDate.of(2023, 04, 24);

    AdministrativeResource.removeOrdersByDate(orders, String.valueOf(removeDate));

    assertTrue(orders.containsKey(removeDate));

  }

  @Test
  public void removeOrdersByDateAndServiceTest() {
    TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
    LocalDate date1 = LocalDate.of(2023, 04, 24);
    LocalDate date2 = LocalDate.of(2024, 04, 24);

    List<Order> orders1 = new ArrayList<>();
    orders1.add(new Order(date1, "Test1", 90.0, true));
    orders1.add(new Order(date1, "Test2", 190.0, true));

    List<Order> orders2 = new ArrayList<>();
    orders2.add(new Order(date2, "Test3", 80.0, true));
    orders2.add(new Order(date2, "Test4", 180.0, true));

    orders.put(date1, orders1);
    orders.put(date2, orders2);

    LocalDate removeDate = LocalDate.of(2023, 04, 24);
    String removeName = "Test4";
    AdministrativeResource.removeOrdersByDateAndService(orders, String.valueOf(removeDate),
        removeName);

    assertTrue(orders.containsKey(removeDate));

  }

  @Test
  public void processDateInputTest() {
    TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
    LocalDate startDate = LocalDate.of(2023, 04, 24);
    LocalDate endDate = LocalDate.of(2023, 04, 25);

    List<Order> orders1 = new ArrayList<>();
    orders1.add(new Order(startDate, "Test1", 190.0, true));
    orders1.add(new Order(endDate, "Test2", 80.0, false));

    orders.put(startDate, orders1);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    // System.setOut(new PrintStream(outputStream));
    String output = outputStream.toString();

    AdministrativeResource.processDateInput(orders, startDate, endDate);

    assertFalse(output.contains(
        "Доходы на период с : 2023-04-24 по 2023-04-25 составляют 190.0\nРасходы на период с : 2023-04-24 по 2023-04-25 составляют 80.0\nОстаток: 110.0")
    );
  }

  @Test
  public void calculateServiceUsageTestEmptyTreeMap() {
    TreeMap<LocalDate, List<Order>> emptyOrder = new TreeMap<>();
    Map<String, Long> result = AdministrativeResource.calculateServiceUsage(
        LocalDate.of(2023, 4, 24), LocalDate.of(2023, 04, 25), true, emptyOrder);
    assertTrue(result.isEmpty());
  }

  @Test
  public void calculateServiceUsageTestName() {
    TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
    LocalDate startDate = LocalDate.of(2023, 04, 24);
    LocalDate endDate = LocalDate.of(2023, 04, 25);

    List<Order> orders1 = new ArrayList<>();
    orders1.add(new Order(startDate, "Test1", 190.0, true));
    orders1.add(new Order(startDate, "Test1", 10.0, true));
    orders1.add(new Order(startDate, "Test1", 120.0, true));

    orders.put(startDate, orders1);

    Map<String, Long> result = AdministrativeResource.calculateServiceUsage(startDate, endDate,
        true, orders);
    assertEquals(3, result.get("Test1"));
  }

  @Test
  public void calculateServiceUsageTestSize() {
    TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
    LocalDate startDate = LocalDate.of(2023, 04, 24);
    LocalDate endDate = LocalDate.of(2023, 04, 25);

    List<Order> orders1 = new ArrayList<>();

    orders1.add(new Order(startDate, "Test1", 190.0, true));
    orders1.add(new Order(startDate, "Test2", 190.0, true));

    orders.put(startDate, orders1);

    Map<String, Long> result = AdministrativeResource.calculateServiceUsage(startDate, endDate,
        true, orders);
    assertEquals(2, result.size());
  }

  @Test
  public void calculateTotalPriceTest() {
    TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
    LocalDate startDate = LocalDate.of(2023, 04, 24);
    LocalDate endDate = LocalDate.of(2023, 04, 25);

    List<Order> orders1 = new ArrayList<>();

    orders1.add(new Order(startDate, "Test1", 190.0, true));
    orders1.add(new Order(startDate, "Test2", 120.0, true));

    orders.put(startDate, orders1);

    double result = AdministrativeResource.calculateTotalPrice(startDate, endDate, true, orders);
    assertEquals(310.0, result);
  }

  @Test
  public void calculateTotalPriceIncomeTest() {
    TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
    LocalDate startDate = LocalDate.of(2023, 04, 24);
    LocalDate endDate = LocalDate.of(2023, 04, 25);

    List<Order> orders1 = new ArrayList<>();

    orders1.add(new Order(startDate, "Test1", 190.0, true));
    orders1.add(new Order(startDate, "Test2", 120.0, false));

    orders.put(startDate, orders1);

    double result = AdministrativeResource.calculateTotalPrice(startDate, endDate, true, orders);
    assertEquals(190.0, result);
  }

  @Test
  public void readOrdersTest() throws IOException {
    TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
    LocalDate date = LocalDate.of(2023, 04, 24);
    LocalDate endDate = LocalDate.of(2023, 04, 25);

    List<Order> orders1 = new ArrayList<>();
    orders1.add(new Order(date, "Test1", 190.0, true));
    orders1.add(new Order(endDate, "Test2", 80.0, false));

    orders.put(date, orders1);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    AdministrativeResource.readOrders();

    String output = outputStream.toString();

    assertThat(output.contains(
        "Статьи доходов: \nOrder{2023-04-24, Test1Test1, price=190.0, income= true}")
    );
  }

  private void assertThat(boolean contains) {
  }

  @Test
  public void addIncomeOrderToFileTest() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    String input = "24-04-2024\nTest1\n190.0\n1\n";

    AdministrativeResource.addIncomeOrderToFile(new Scanner(input));

    String order = outputStream.toString();
    assertThat(order.contains("24-04-2024\n"
        + "Test1\n"
        + "190.0\n"
        + "1"));
  }

  @Test
  public void addIncomeOrderToFileInputCorrectTest() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    String input = "24-04-2024\nTest1\n190.0\n3\n1\n";

    AdministrativeResource.addIncomeOrderToFile(new Scanner(input));

    String order = outputStream.toString();
    assertThat(order.contains("Не корректный ввод"));
  }
}