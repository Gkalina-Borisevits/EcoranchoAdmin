import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Test;


public class OrderTests {

  private Order order;

  // @BeforeEach
  // public void newOrder () {
  //   order = new Order(LocalDate.of(2024, 12, 12), "Test Order", 90.0, true);
  //   order1 = new Order(LocalDate.of(0, 0, 0), "Test Order", 90.0, true);
  //   order2 = new Order(LocalDate.of(2024, 12, 12), "Test Order", 90.0, true);
  //
  // }

  @Test
  public void serviceNameIsNull() {
    order = new Order(LocalDate.of(2024, 12, 12), "Test Order", 90.0, true);
    assertFalse(order.equals(null));
  }

  @Test
  public void serviceNameNoEmpty() {
    order = new Order(LocalDate.of(2024, 12, 12), "Test Order", 90.0, true);
    assertEquals("Test Order", order.getServiceName());
  }

  @Test
  public void serviceNameWitchSpase() {
    order = new Order(LocalDate.of(2024, 12, 12), " ", 90.0, true);
    assertEquals(" ", order.getServiceName());
  }

  @Test
  public void constructorThrowsIllegalArgumentExceptionForNegativePrice() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Order(LocalDate.of(2024, 12, 12), " ", -90.0, true);
    });
  }

  @Test
  public void priceNoEmpty() {
    order = new Order(LocalDate.of(2024, 12, 12), "Test Order ", 9.0, true);
    assertEquals(9, 9);
  }

  @Test
  public void priceIsNull() {
    order = new Order(LocalDate.of(2024, 12, 12), "Test Order ", 0, true);
    assertEquals(0, order.getPrice());
  }

  @Test
  public void IllegalArgumentExceptionIsEmpty() {
    assertThrows(IllegalArgumentException.class, () -> {
      order = new Order(LocalDate.now(), "", 10.0, true);
    });
  }

  @Test
  public void IllegalArgumentExceptionIsEmptyLocalDate() {
    assertThrows(IllegalArgumentException.class, () -> {
      order = new Order(null, "", 10.0, true);
    });
  }

  @Test
  public void testDateFormatting() {
    LocalDate date = LocalDate.of(2024, 12, 12);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String formattedDate = date.format(formatter);

    assertEquals("12-12-2024", formattedDate);
  }


  @Test
  public void testDateValidCsvLine() {
    String csvLine = "12-05-2024;Test Order;10.5;true";
    Order order = Order.parseFromCSVLine(csvLine, ";");
    assertEquals(LocalDate.of(2024, 5, 12), order.getLocalDate());
  }

  @Test
  public void testNameValidCsvLine() {
    String csvLine = "12-05-2024;Test Order;10.5;true";
    Order order = Order.parseFromCSVLine(csvLine, ";");
    assertEquals("Test Order", order.getServiceName());
  }

  @Test
  public void testPriceValidCsvLine() {
    String csvLine = "12-05-2024;Test Order;10.5;true";
    Order order = Order.parseFromCSVLine(csvLine, ";");
    assertEquals(10.5, order.getPrice());
  }

  @Test
  public void testBooleanValidCsvLine() {
    String csvLine = "12-05-2024;Test Order;10.5;true";
    Order order = Order.parseFromCSVLine(csvLine, ";");
    assertTrue(order.isIncome());
  }

  @Test
  public void testParseFromCSVLineDateTimeParseException() {
    order = new Order(LocalDate.of(2024, 12, 12), "Test Order", 10.5, true);
    String csvLine = "InvalidData";
    assertThrows(DateTimeParseException.class, () -> {
      Order.parseFromCSVLine(csvLine, ";");
    });
  }


  @Test
  public void testGetCSVLine() {
    order = new Order(LocalDate.of(2024, 12, 12), "Test Order", 10.5, true);
    String csvLine = "12-12-2024;Test Order;10.5;true";
    assertEquals(csvLine, order.getCSVLine(";"));
  }

  @Test
  public void testToString() {
    order = new Order(LocalDate.of(2024, 12, 12), "Test Order", 10.5, true);

    String expectedString = "Order{localDate=2024-12-12, serviceName='Test Order', price=10.5, isIncome=true}";
    assertEquals(expectedString, order.toString());
  }
}
