import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Order {

  private LocalDate localDate;
  private String serviceName;
  private double price;
  private boolean isIncome;

  public Order(LocalDate localDate, String serviceName, double price, Boolean isIncome) {
    if (serviceName == null || serviceName.isEmpty()) {
      throw new IllegalArgumentException("Не корректный ввод" + serviceName);
    }
    if (localDate == null) {
      throw new IllegalArgumentException("Не корректный ввод" + null);
    }
    this.localDate = localDate;
    this.serviceName = serviceName;
    this.price = checkPrice(price);
    this.isIncome = isIncome;
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

  public boolean isIncome() {
    return isIncome;
  }

  public void setIncome(Boolean status) {
    this.isIncome = status;
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


  public static Order parseFromCSVLine(String s, String delimiter) {
    String[] cells = s.split(delimiter);
    try {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      LocalDate date = LocalDate.parse(cells[0], dateFormatter);

      return new Order(date, cells[1], Double.parseDouble(cells[2]),
          Boolean.parseBoolean(cells[3]));
    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
      throw new IllegalArgumentException("Некорректная строка: " + s);
    }
  }

  public String getCSVLine(String delimiter) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String formattedDate = localDate.format(dateFormatter);
    return String.join(delimiter, formattedDate, serviceName, Double.toString(price),
        Boolean.toString(isIncome));
  }


  @Override
  public String toString() {
    return "Order{" +
        "localDate=" + localDate +
        ", serviceName='" + serviceName + '\'' +
        ", price=" + price +
        ", isIncome=" + isIncome +
        '}';
  }
}
