import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Order {

  private LocalDate localDate;
  private String serviceName;
  private double price;
  private double totalCost;
  private boolean income;

  public Order(LocalDate localDate, String serviceName, double price, double totalCost) {
    if (serviceName == null || serviceName.isEmpty()) {
      throw new IllegalArgumentException("Не корректный ввод" + serviceName);
    }
    if (localDate == null || localDate.isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("Не корректный ввод" + localDate);
    }
    this.localDate = localDate;
    this.serviceName = serviceName;
    this.price = price;
    this.totalCost = totalCost;
    this.income = income;
  }

  public Order(LocalDate localDate, String serviceName, double price, Boolean income) {
    this.localDate = localDate;
    this.serviceName = serviceName;
    this.price = price;
    this.totalCost = totalCost;
    this.income = income;
  }

  public LocalDate getLocalDate() {
    return localDate;
  }

  public String getServiceName() {
    return serviceName;
  }

  public double getPrice() {
    return price;
  }

  public void setLocalDate(LocalDate localDate) {
    this.localDate = localDate;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  private double checkPrice(double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Стоимость не может быть отрицательной");
    }
    return price;
  }

  private static double checkPriceDouble() {
    Scanner scanner = new Scanner(System.in);
    while (!scanner.hasNextDouble()) {
      System.out.print("Не корректный ввод стоимости: " + scanner.nextLine());
      System.out.print("Введите корректную стоимость: ");
    }

    double number = scanner.nextDouble();
    scanner.nextLine();
    return number;
  }

  private double validatePrice() {
    double price = checkPriceDouble();
    return checkPrice(price);
  }

  public static Order parseFromCSVLine(String s, String delimiter) {
    String[] cells = s.split(delimiter);
    try {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      LocalDate date = LocalDate.parse(cells[0], dateFormatter);
      return new Order(date, cells[1], Double.parseDouble(cells[2]), Double.parseDouble(cells[3]));
    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
      throw new IllegalArgumentException("Некорректная строка: " + s);
    }
  }

  public String getCSVLine(String delimiter) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String formattedDate = localDate.format(dateFormatter);
    return String.join(delimiter, formattedDate, serviceName, Double.toString(price),
        Double.toString(totalCost));
  }
}
