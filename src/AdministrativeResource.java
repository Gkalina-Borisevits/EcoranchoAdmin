import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Stream;

public class AdministrativeResource {

  private static TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
  private static String delimiter = "; ";
  public static OrdersFile ordersFile;


  public AdministrativeResource(TreeMap<LocalDate, List<Order>> orders) throws IOException {
    this.orders = orders;
    this.ordersFile = new OrdersFile("res/order.csv", delimiter);

  }

  public static void addIncomeOrderToFile(Scanner scanner) {
    try {

      LocalDate date = validDate(scanner);

      String serviceName = checkAddName(scanner);

      System.out.print("Введите стоимость услуги: ");
      double price = checkPriceDouble(scanner);

      System.out.println("Введите: 1(Доход) 2(Расход)");
      int statusNumber;
      while (true) {
        statusNumber = checkIntNumber(scanner);
        if (statusNumber == 1 || statusNumber == 2) {
          break;
        } else {
          System.out.println("no correct");
        }
      }
      OrdersFile ordersFile = new OrdersFile("res/order.csv", delimiter);

      if (statusNumber == 1) {
        Order newOrder = new Order(date, serviceName, price, true);
        orders.computeIfAbsent(date, k -> new ArrayList<>()).add(newOrder);
        ordersFile.writeOrder(newOrder, true);
      } else {
        Order newOrder = new Order(date, serviceName, price, false);
        orders.computeIfAbsent(date, k -> new ArrayList<>()).add(newOrder);
        ordersFile.writeOrder(newOrder, false);
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  public static void readOrders() throws IOException {
    OrdersFile ordersFile = new OrdersFile("res/order.csv", delimiter);
    TreeMap<LocalDate, List<Order>> orders = ordersFile.readOrder();

    List<Order> incomeOrders = new ArrayList<>();
    List<Order> expenseOrders = new ArrayList<>();

    for (LocalDate date : orders.keySet()) {
      List<Order> orderList = orders.get(date);
      for (Order o : orderList) {
        if (o.isIncome()) {
          incomeOrders.add(o);
        } else {
          expenseOrders.add(o);
        }
      }
    }

    System.out.println("Статьи доходов:");
    for (Order o : incomeOrders) {
      System.out.println(o);
    }
    System.out.println("Статьи расходов:");
    for (Order o : expenseOrders) {
      System.out.println(o);
    }
  }


  public static void showMenuStatistic(Scanner scanner) throws IOException {
    boolean isRun = true;
    OrdersFile ordersFile = new OrdersFile("res/order.csv", delimiter);
    TreeMap<LocalDate, List<Order>> orders = ordersFile.readOrder();

    while (isRun) {
      statisticMenu();
      int command = checkIntNumber(scanner);

      LocalDate startDate = null;
      LocalDate endDate = null;

      switch (command) {
        case 1:
          processDateInput(scanner, orders);

          break;
        case 2:
          processWeekInput(scanner, orders);

          break;
        case 3:
          processMonthInput(scanner, orders);

          break;
        case 0:
          isRun = false;
          break;
        default:
          System.out.println("Некорректный выбор. Попробуйте еще раз.");
          continue;
      }
    }
  }

  public static void statisticMenu() {
    System.out.println("Выберите период ценовой статистики:");
    System.out.println("1. За день");
    System.out.println("2. За неделю");
    System.out.println("3. За месяц");
    System.out.println("0. Вернуться в предыдущее меню");

  }

  private static double calculateTotalPrice(LocalDate startDate, LocalDate endDate,
      boolean isIncome,
      TreeMap<LocalDate, List<Order>> orders) {
    return orders.entrySet().stream()
        .filter(entry -> !entry.getKey().isBefore(startDate) && !entry.getKey().isAfter(endDate))
        .flatMap(entry -> entry.getValue().stream())
        .filter(order -> order.isIncome() == isIncome)
        .mapToDouble(Order::getPrice)
        .sum();
  }

  private static int calculateTotalOrderName(LocalDate startDate, LocalDate endDate,
      boolean isIncome, TreeMap<LocalDate, List<Order>> orders) {

    return orders.entrySet().stream()
        .filter(entry -> !entry.getKey().isBefore(startDate) && !entry.getKey().isAfter(endDate))
        .flatMap(entry -> entry.getValue().stream())
        .filter(order -> order.isIncome() == isIncome)


  }


  private static void processDateInput(Scanner scanner, TreeMap<LocalDate, List<Order>> orders) {

    LocalDate startDate = validDate(scanner);
    LocalDate endDate = startDate.plusDays(1);
    double totalIncome = calculateTotalPrice(startDate, endDate, true, orders);
    double totalExpenses = calculateTotalPrice(startDate, endDate, false, orders);
    double incomeMinusExpenses = totalIncome - totalExpenses;
    System.out.println(
        "Доходы на период с : " + startDate + " по " + endDate + " составляют " + totalIncome);
    System.out.println(
        "Расходы на период с : " + startDate + " по " + endDate + " составляют " + totalExpenses);
    System.out.println("Остаток: " + incomeMinusExpenses);
  }

  private static void processWeekInput(Scanner scanner, TreeMap<LocalDate, List<Order>> orders) {

    LocalDate weekStartDate = validDate(scanner);
    LocalDate endDate = weekStartDate.plusWeeks(1);
    double totalIncome = calculateTotalPrice(weekStartDate, endDate, true, orders);
    double totalExpenses = calculateTotalPrice(weekStartDate, endDate, false, orders);
    double incomeMinusExpenses = totalIncome - totalExpenses;
    System.out.println(
        "Доходы на период с : " + weekStartDate + " по " + endDate + " составляют " + totalIncome);
    System.out.println("Расходы на период с : " + weekStartDate + " по " + endDate + " составляют "
        + totalExpenses);
    System.out.println("Остаток: " + incomeMinusExpenses);
  }

  private static void processMonthInput(Scanner scanner, TreeMap<LocalDate, List<Order>> orders) {

    LocalDate monthStartDate = validDate(scanner);

    LocalDate endDate = monthStartDate.plusMonths(1);
    double totalIncome = calculateTotalPrice(monthStartDate, endDate, true, orders);
    double totalExpenses = calculateTotalPrice(monthStartDate, endDate, false, orders);
    double incomeMinusExpenses = totalIncome - totalExpenses;
    System.out.println(
        "Доходы на период с : " + monthStartDate + " по " + endDate + " составляют " + totalIncome);
    System.out.println("Расходы на период с : " + monthStartDate + " по " + endDate + " составляют "
        + totalExpenses);
    System.out.println("Остаток: " + incomeMinusExpenses);
  }

  public static void financeMenu() {
    System.out.println("Выберите тип операции:");
    System.out.println("1. Добавить услугу");
    System.out.println("0. Вернуться в предыдущее меню");
  }


  public static void showMenuFinance(Scanner scanner) {
    boolean isRun = true;

    while (isRun) {
      financeMenu();
      int command = checkIntNumber(scanner);

      switch (command) {
        case 1:
          addIncomeOrderToFile(scanner);
          break;

        case 0:
          isRun = false;
          break;
        default:
          System.out.println("Неверная команда.");
          break;
      }
    }
  }

  private static void removeOrdersByDate(TreeMap<LocalDate, List<Order>> orders,
      String targetDate) {
    LocalDate localDate;
    try {
      localDate = LocalDate.parse(targetDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    } catch (DateTimeParseException e) {
      System.out.println(" no correct data");
      return;
    }
    orders.remove(localDate);
  }


  private static void removeOrdersByService(TreeMap<LocalDate, List<Order>> orders,
      String serviceName) {
    for (Map.Entry<LocalDate, List<Order>> entry : orders.entrySet()) {
      List<Order> orderList = entry.getValue();
      orderList.removeIf(order -> order.getServiceName().equals(serviceName));
    }
  }

  public static void deleteOrder(Scanner scanner) throws IOException {
    OrdersFile ordersFile = new OrdersFile("res/order.csv", delimiter);
    TreeMap<LocalDate, List<Order>> orders = ordersFile.readOrder();

    System.out.print("Выберите тип удаления (1 - по дате, 2 - по названию): ");
    int operationType = checkIntNumber(scanner);

    if (operationType == 1) {
      System.out.print("Data: ");
      String date = validDate(scanner).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
      removeOrdersByDate(orders, date);

    } else if (operationType == 2) {
      System.out.println("name: ");
      String name = checkAddName(scanner);
      removeOrdersByService(orders, name);

    } else {
      System.out.println("no correct operation");
    }
    ordersFile.writeOrders(orders);
  }

  private static double checkPriceDouble(Scanner scanner) {
    double number;
    while (true) {
      if (scanner.hasNextLine()) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
          System.out.println("is empty");
          continue;
        }
        try {
          number = Double.parseDouble(input);

          if (number >= 0) {
            break;
          } else {
            System.out.println(
                "Обратите внимание на корректность ввода - число не может быть отрицательным");
            System.out.print("Введите корректное число:");
          }
        } catch (NumberFormatException e) {
          System.out.println("no correct " + input);
        }
      } else {
        String error = scanner.nextLine();
        System.out.println(
            "Некорректный ввод: " + error + "\nВведите корректное число");
      }
    }
    return number;
  }

  private static LocalDate validDate(Scanner scanner) {
    while (true) {
      System.out.print("Введите дату в формате (dd-MM-yyyy): ");
      String date = scanner.nextLine();
      if (date.isEmpty()) {
        System.out.println("Дата не может быть пустой");
        continue;
      }

      try {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        return localDate;
      } catch (DateTimeParseException e) {
        System.out.println("Некорректный формат даты. Используйте формат dd-MM-yyyy");
      }
    }
  }


  public static int checkIntNumber(Scanner scanner) {
    int number;
    while (true) {
      if (scanner.hasNextLine()) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
          System.out.println("is empty");
          continue;
        }
        try {
          number = Integer.parseInt(input);

          if (number >= 0) {
            break;
          } else {
            System.out.println(
                "Обратите внимание на корректность ввода - число не может быть отрицательным");
            System.out.print("Введите корректное число:");
          }
        } catch (NumberFormatException e) {
          System.out.println("no correct " + input);
        }
      } else {
        String error = scanner.nextLine();
        System.out.println(
            "Некорректный ввод: " + error + "\nВведите корректное число");
      }
    }
    return number;
  }

  public static String checkAddName(Scanner scanner) {
    System.out.print("Введите название: ");
    String name = scanner.nextLine().toUpperCase();
    while (name.isEmpty()) {
      System.out.println("Некорректное название: " + name);
      System.out.print("Введите корректное название: ");
      name = scanner.nextLine().toUpperCase();
    }
    return name;
  }
}

