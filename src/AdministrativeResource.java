import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class AdministrativeResource {

  private static TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
  private static String delimiter = "; ";
  public static OrdersFile ordersFile;


  /**
   * Конструктор класса AdministrativeResource, полями которого являются: TreeMap<LocalDate,
   * List<Order>> orders, OrdersFile ordersFile
   *
   * @param orders - параметром инициализирована TreeMap<LocalDate, List<Order>>
   * @throws IOException - обработка общего типа исключения при работе с файлами
   */
  public AdministrativeResource(TreeMap<LocalDate, List<Order>> orders) throws IOException {
    this.orders = orders;
    this.ordersFile = new OrdersFile("res/order.csv", delimiter);

  }

  /**
   * Метод добавления данных в файл. Этот метод позволяет пользователю вводить данные: дата,
   * название услуги, стоимость оказания услуги, тип услуги - доход или расход, а затем записывает
   * эту информацию в файл. Внутри метода используется Map, где данные хранятся по дате. Метод
   * использует для записи в файл метод класса OrdersFile.writeOrder
   *
   * @param scanner - Scanner используется для ввода данных пользователем
   */
  protected static void addIncomeOrderToFile(Scanner scanner) {
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
          System.out.println("Не корректный ввод");
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

  /**
   * Метод предназначен для чтения данных из файла и формирования списков статей доходов и расходов
   * на основе данных из файла
   *
   * @throws IOException - общее исключение при работе с файлами
   */
  protected static void readOrders() throws IOException {
    TreeMap<LocalDate, List<Order>> orders = readOrdersFile();

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

  /**
   * Метод, который использует оператор switch для вывода статистики по стоимости услуг за
   * определенный интервал времени
   *
   * @param scanner - на вход подается Scanner для взаимодействия с пользователем
   * @throws IOException - общее исключение, которое может возникнуть при работе с файлами
   */
  public static void showMenuPriceStatistic(Scanner scanner) throws IOException {
    boolean isRun = true;
    TreeMap<LocalDate, List<Order>> orders = readOrdersFile();

    while (isRun) {
      statisticPriceMenu();
      int command = checkIntNumber(scanner);

      switch (command) {
        case 1:
          processDayInput(scanner, orders);

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

  /**
   * Метод для передачи пользователю информации по каким параметрам можно произвести ценовую
   * статистику
   */
  public static void statisticPriceMenu() {
    System.out.println("Выберите период ценовой статистики:");
    System.out.println("1. За день");
    System.out.println("2. За неделю");
    System.out.println("3. За месяц");
    System.out.println("0. Вернуться в предыдущее меню");

  }

  /**
   * Метод, который использует оператор switch для вывода статистики по названию услуги за
   * определенный пользователем интервал времени
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   * @throws IOException - общее исключение, которое может возникнуть при работе с файлами
   */
  protected static void showMenuNameStatistic(Scanner scanner) throws IOException {
    boolean isRun = true;
    TreeMap<LocalDate, List<Order>> orders = readOrdersFile();

    while (isRun) {
      statisticPriceMenu();
      int command = checkIntNumber(scanner);

      switch (command) {
        case 1:
          processDateNameDayInput(scanner, orders);

          break;
        case 2:
          processDateNameWeekInput(scanner, orders);

          break;
        case 3:
          processDateNameMonthInput(scanner, orders);

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

  /**
   * Метод для расчета ценовой статистики исходя из переменной isIncome, которая определяет к какому
   * источнику определить информацию - к доходу или расходу. .entrySet возвращает набор элементов
   * ключ (LocalDate) - значение (List<Order>). .stream - преобразовывает в поток элементов .filter
   * - фильтрует поток по заданным элементам и исключает из условия элементы до startDate и после
   * endDate .flatMap - объединяет полученные элементы в один поток .filter - фильтрует значение по
   * переменной isIncome .mapToDouble - получает значение getPrice .sum = выводит сумму полученных
   * из потока элементов
   *
   * @param startDate - задаваемая дата для статистики
   * @param endDate   - задаваемая дополнительным методом дата
   * @param isIncome  - переменная которая распределяет информацию на доход и расход
   * @param orders    - TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение
   *                  (List<Order>)
   * @return - метод возвращает в формате double сумму стоимости в заданном диапазоне дат
   */
  protected static double calculateTotalPrice(LocalDate startDate, LocalDate endDate,
      boolean isIncome,
      TreeMap<LocalDate, List<Order>> orders) {
    return orders.entrySet().stream()
        .filter(entry -> !entry.getKey().isBefore(startDate) && !entry.getKey().isAfter(endDate))
        .flatMap(entry -> entry.getValue().stream())
        .filter(order -> order.isIncome() == isIncome)
        .mapToDouble(Order::getPrice)
        .sum();
  }

  /**
   * Метод для расчета статистики оказанных услуг исходя из переменной isIncome, которая определяет
   * к какому источнику определить информацию - к доходу или расходу. .entrySet возвращает набор
   * элементов ключ (LocalDate) - значение (List<Order>). .stream - преобразовывает в поток
   * элементов .filter - фильтрует поток по заданным элементам и исключает из условия элементы до
   * startDate и после endDate .flatMap - объединяет полученные элементы в один поток .filter -
   * фильтрует значение по переменной isIncome .collect - группирует элементы потока по
   * getServiceName Collectors.counting() - коллектор, подсчитывает количество элементов потока
   *
   * @param startDate - задаваемая дата для статистики
   * @param endDate   - задаваемая дополнительным методом дата
   * @param isIncome  - переменная которая распределяет информацию на доход и расход
   * @param orders    - TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение
   *                  (List<Order>)
   * @return - метод возвращает Map<String, Long>, где ключ - название услуги, значение - количество
   * раз использования услуги в заданном диапазоне дат
   */
  protected static Map<String, Long> calculateServiceUsage(LocalDate startDate, LocalDate endDate,
      boolean isIncome, TreeMap<LocalDate, List<Order>> orders) {
    return orders.entrySet().stream()
        .filter(entry -> !entry.getKey().isBefore(startDate) && !entry.getKey().isAfter(endDate))
        .flatMap(entry -> entry.getValue().stream())
        .filter(order -> order.isIncome() == isIncome)
        .collect(Collectors.groupingBy(Order::getServiceName, Collectors.counting()));
  }


  /**
   * Метод ввода пользователем исходной даты для вывода статистической информации по оказанным
   * услугам и их количеству, за выбранный интервал времени (сутки)
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   * @param orders  - - TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение
   *                (List<Order>)
   */
  protected static void processDateNameDayInput(Scanner scanner,
      TreeMap<LocalDate, List<Order>> orders) {
    LocalDate startDate = validDate(scanner);
    LocalDate endDate = startDate.plusDays(1);
    processDateNameInput(orders, startDate, endDate);
  }


  /**
   * Метод ввода пользователем исходной даты для вывода статистической информации по оказанным
   * услугам и их количеству, за выбранный интервал времени(неделя)
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   * @param orders- TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение
   *                (List<Order>)
   */
  protected static void processDateNameWeekInput(Scanner scanner,
      TreeMap<LocalDate, List<Order>> orders) {
    LocalDate weekStartDate = validDate(scanner);
    LocalDate endDate = weekStartDate.plusWeeks(1);
    processDateNameInput(orders, weekStartDate, endDate);
  }


  /**
   * Метод ввода пользователем исходной даты для вывода статистической информации по оказанным
   * услугам и их количеству, за выбранный интервал времени (месяц)
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   * @param orders- TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение
   *                (List<Order>)
   */
  protected static void processDateNameMonthInput(Scanner scanner,
      TreeMap<LocalDate, List<Order>> orders) {

    LocalDate monthStartDate = validDate(scanner);
    LocalDate endDate = monthStartDate.plusMonths(1);
    processDateNameInput(orders, monthStartDate, endDate);

  }

  /**
   * Метод для вывода пользователю статистической информации согласно его запросу по временному
   * интервалу, об оказанных услугах и их количеству
   *
   * @param orders-   TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение *
   *                  (List<Order>)
   * @param startDate - вводимая пользователем начальная дата расчета
   * @param endDate   - выводимая методом конечная дата временного интервала
   */
  protected static void processDateNameInput(
      TreeMap<LocalDate, List<Order>> orders, LocalDate startDate, LocalDate endDate) {
    Map<String, Long> outputTrueName = calculateServiceUsage(startDate, endDate, true, orders);
    Map<String, Long> outputFalseName = calculateServiceUsage(startDate, endDate, false,
        orders);
    System.out.println(
        "Предоставленные услуги на период с : " + startDate + " по " + endDate + " : "
            + outputTrueName
            + "\n");
    System.out.println(
        "Статьи расходов на период с : " + startDate + " по " + endDate + " : " + outputFalseName
            + "\n");

  }

  /**
   * Метод для ввода пользователем исходной даты для дальнейшего выведения списка статей доходов и
   * расходов за заданный интервал времени (сутки)
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   * @param orders- TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение
   *                (List<Order>)
   */
  protected static void processDayInput(Scanner scanner, TreeMap<LocalDate, List<Order>> orders) {

    LocalDate startDate = validDate(scanner);
    LocalDate endDate = startDate.plusDays(1);
    processDateInput(orders, startDate, endDate);
  }


  /**
   * Метод для ввода пользователем исходной даты для дальнейшего выведения списка статей доходов и
   * расходов за заданный интервал времени (неделя)
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   * @param orders  - TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение
   *                (List<Order>)
   */
  protected static void processWeekInput(Scanner scanner, TreeMap<LocalDate, List<Order>> orders) {

    LocalDate weekStartDate = validDate(scanner);
    LocalDate endDate = weekStartDate.plusWeeks(1);
    processDateInput(orders, weekStartDate, endDate);
  }


  /**
   * Метод для ввода пользователем исходной даты для дальнейшего выведения списка статей доходов и
   * расходов за заданный интервал времени (месяц)
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   * @param orders  - TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение *
   *                (List<Order>)
   */
  protected static void processMonthInput(Scanner scanner, TreeMap<LocalDate, List<Order>> orders) {

    LocalDate monthStartDate = validDate(scanner);
    LocalDate endDate = monthStartDate.plusMonths(1);
    processDateInput(orders, monthStartDate, endDate);

  }


  /**
   * Метод для вывода расчетов списка доходов и расходов за выбранный пользователем интервал
   * времени.
   *
   * @param orders    - TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение
   *                  (List<Order>)
   * @param startDate - вводимая пользователем начальная дата расчета
   * @param endDate   - выводимая методом конечная дата временного интервала
   */
  protected static void processDateInput(TreeMap<LocalDate, List<Order>> orders,
      LocalDate startDate, LocalDate endDate) {
    double totalIncome = calculateTotalPrice(startDate, endDate, true, orders);
    double totalExpenses = calculateTotalPrice(startDate, endDate, false, orders);
    double incomeMinusExpenses = totalIncome - totalExpenses;
    System.out.println(
        "Доходы на период с : " + startDate + " по " + endDate + " составляют " + totalIncome);
    System.out.println(
        "Расходы на период с : " + startDate + " по " + endDate + " составляют " + totalExpenses);
    System.out.println("Остаток: " + incomeMinusExpenses);
  }


  /**
   * Метод перечисления для вывода пользователю возможных действий
   */
  public static void financeMenu() {
    System.out.println("Выберите тип операции:");
    System.out.println("1. Добавить услугу");
    System.out.println("0. Вернуться в предыдущее меню");
  }


  /**
   * Метод для вывода пользователю меню с возможностью добавления услуг
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   */
  public static void showMenuFinance(Scanner scanner) {
    boolean isRun = true;

    while (isRun) {
      financeMenu();
      int command = checkIntNumber(scanner);

      if (command == 1) {
        addIncomeOrderToFile(scanner);
      } else if (command == 0) {
        isRun = false;
      } else {
        System.out.println("Неверная команда.");
      }
    }
  }


  /**
   * Метод для удаления элемента по ключу (LocalDate). При выборе данного метода происходит удаление
   * всего перечня услуг по введенной пользователем дате.
   *
   * @param orders     - TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение
   *                   (List<Order>)
   * @param removeDate - localDate - дата, по которой необходимо удалить значения, значения
   *                   удаляются все, соответствующие выбранной дате
   */
  protected static void removeOrdersByDate(TreeMap<LocalDate, List<Order>> orders,
      String removeDate) {

    LocalDate localDate;
    try {
      localDate = LocalDate.parse(removeDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    } catch (DateTimeParseException e) {
      System.out.println("Не корректная дата");
      return;
    }
    orders.remove(localDate);
  }


  /**
   * Метод для удаления из списка элемента как по дате так и по названию услуги.
   *
   * @param orders     - TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение
   *                   (List<Order>)
   * @param removeDate - вводимая пользователем дата, по которой необходимо произвести удаление
   * @param name       - название услуги, которую необходимо удалить
   */
  protected static void removeOrdersByDateAndService(TreeMap<LocalDate, List<Order>> orders,
      String removeDate, String name) {
    LocalDate localDate;
    try {
      localDate = LocalDate.parse(removeDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    } catch (DateTimeParseException e) {
      System.out.println(" no correct data");
      return;
    }
    for (List<Order> ordersOnDate : orders.values()) {
      ordersOnDate.removeIf(order -> order.getServiceName().equals(name));
    }
    orders.entrySet().removeIf(entry -> entry.getValue().isEmpty());
  }


  /**
   * Метод удаления из TreeMap по значению(названию услуги), после удаления значения - удаляются все
   * ключи соответствующие этому значению. В результате - происходит удаление конкретно заданной
   * услуги, не зависимо от даты.
   *
   * @param orders - TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение
   *               (List<Order>)
   * @param name
   */
  protected static void removeOrdersByService(TreeMap<LocalDate, List<Order>> orders, String name) {
    for (List<Order> ordersOnDate : orders.values()) {
      ordersOnDate.removeIf(order -> order.getServiceName().equals(name));
    }
    orders.entrySet().removeIf(entry -> entry.getValue().isEmpty());
  }


  /**
   * Метод для выбора по какому параметру должно произойти удаление элемента в TreeMap. При вызове
   * метода removeOrdersByDate - происходит удаление по ключу. При вызове метода
   * removeOrdersByService - удаление по значению. При вызове метода removeOrdersByDateAndService -
   * происходит удаление и по ключу и по значению
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   * @throws IOException - обработка общих исключений при работе с файлами
   */
  protected static void deleteOrder(Scanner scanner) throws IOException {
    OrdersFile ordersFile = new OrdersFile("res/order.csv", delimiter);
    TreeMap<LocalDate, List<Order>> orders = readOrdersFile();

    System.out.print(
        "Выберите тип удаления (1 - по дате, 2 - по названию, 3- по дате и по названию): ");
    int operationType = checkIntNumber(scanner);

    if (operationType == 1) {
      String date = validDate(scanner).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
      removeOrdersByDate(orders, date);

    } else if (operationType == 2) {
      String name = checkAddName(scanner);
      removeOrdersByService(orders, name);
    } else if (operationType == 3) {
      String date = validDate(scanner).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
      String name = checkAddName(scanner);
      removeOrdersByDateAndService(orders, date, name);

    } else {
      System.out.println("Не корректный ввод");
    }
    ordersFile.writeOrders(orders);
  }


  /**
   * Метод проверки ввода данных типа double
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   * @return - возвращает тип данных double
   */
  protected static double checkPriceDouble(Scanner scanner) {
    double number;
    while (true) {
      if (scanner.hasNextLine()) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
          System.out.println("Это поле не может быть пустым");
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

  /**
   * Метод проверки даты на корректный ввод
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   * @return - возвращает дату
   */
  protected static LocalDate validDate(Scanner scanner) {
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

  /**
   * Метод проверки ввода данных типа int
   *
   * @param scanner - на вход подается Scanner для считывания информации с консоли
   * @return - возвращает тип данных int
   */
  protected static int checkIntNumber(Scanner scanner) {
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

  /**
   * Метод, проверяющий на корректность вводимого имени на ввод пустой строки, .toUpperCase() -
   * добавлена для приведения вводимых данных к одному регистру
   *
   * @param scanner - на ввод подается Scanner в виде параметра
   * @return - на вывод - String name
   */
  protected static String checkAddName(Scanner scanner) {
    System.out.print("Введите название: ");
    String name = scanner.nextLine().toUpperCase();
    while (name.isEmpty()) {
      System.out.println("Некорректное название: " + name);
      System.out.print("Введите корректное название: ");
      name = scanner.nextLine().toUpperCase();
    }
    return name;
  }

  /**
   * Метод для чтения данных из файла с помощью включенного метода readOrder
   *
   * @return - TreeMap, в котором хранится набор элементов ключ (LocalDate) - значение (List<Order>)
   * @throws IOException - обработка общего типа исключений при работе с файлом
   */
  protected static TreeMap<LocalDate, List<Order>> readOrdersFile() throws IOException {
    OrdersFile ordersFile = new OrdersFile("res/order.csv", delimiter);
    return ordersFile.readOrder();
  }
}

