import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

public class MenuCommandTests {

  @Test
  public void testPrintMenu() {
    assertDoesNotThrow(() -> MenuCommand.printMenu());
  }

  @Test
  public void testReadExit() throws IOException {
    Scanner scanner = new Scanner("""
        0""");
    MenuCommand command = MenuCommand.commandRead(scanner);
    assertEquals(MenuCommand.EXIT, command);
  }

  @Test
  public void testCommandReadRegister() throws IOException {
    Scanner scanner = new Scanner("Exit");
    MenuCommand result = MenuCommand.commandRead(scanner);
    MenuCommand expected = MenuCommand.EXIT;
    assertEquals(expected, result);
  }
}
