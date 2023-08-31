import java.time.LocalDate;

public class Order {

  private LocalDate localDate;
  private String serviceName;
  private double price;
  private double totalCost;
  private String income;
  private String expense;

  public Order(LocalDate localDate, String serviceName, double price, double totalCost,
      String income,
      String expense) {
    this.localDate = localDate;
    this.serviceName = serviceName;
    this.price = price;
    this.totalCost = totalCost;
    this.income = income;
    this.expense = expense;
  }

  public Order(LocalDate localDate, String serviceName, double price) {
    this.localDate = localDate;
    this.serviceName = serviceName;
    this.price = price;
    this.totalCost = totalCost;
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

  public String getIncome() {
    return income;
  }

  public String getExpense() {
    return expense;
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

  public void setIncome(String income) {
    this.income = income;
  }

  public void setExpense(String expense) {
    this.expense = expense;
  }
}
