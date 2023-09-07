import java.io.IOException;
import java.util.Scanner;

public enum MenuCommand {
  UNEXEPTED(10, "", ""),
  FINANCIAL(1, "ДОБАВИТЬ ФИНАНСОВЮ ОПЕРАЦИЮ", "FINANCIAL TRANSACTION"),
  OVERVIEWFINANCIAL(2, "ОБЗОР ФИНАНСОВЫХ ОПЕРАЦИЙ", "OVERVIEW TRANSACTIONS"),
  SERVICESTATISTICS(3, "СТАТИСТИКА УСЛУГ", "SERVICE STATISTICS"),
  MONEYSTATISTICS(4, "СТАТИСТИКА ТРАНЗАКЦИЙ", "MONEY STATISTICS"),
  DELETETRANSACTION(5, "УДАЛИТЬ ТРАНЗАКЦИЮ", "DELETE TRANSACTION"),
  EXIT(0, "ВЫХОД", "EXIT");

  private final int num;
  private final String displayNameRus;
  private final String displayNameEng;


  MenuCommand(int num, String displayNameRus, String displayNameEng) {
    this.num = num;
    this.displayNameRus = displayNameRus;
    this.displayNameEng = displayNameEng;

  }

  public static void printMenu() {
    System.out.println("\nwelcome to ecorancho\n");
    for (MenuCommand command : values()) {

      if (command != UNEXEPTED) {
        System.out.println(
            command.num + ". " + command.displayNameRus + " / " + command.displayNameEng);
      }
    }
  }

  public static MenuCommand commandRead(Scanner scanner) throws IOException {

    printMenu();
    System.out.print("Выберите пункт меню: \n");
    if (!scanner.hasNext()) {
      throw new RuntimeException("Необходимо ввести команду");
    }

    String input = scanner.next().toUpperCase();
    scanner.nextLine();

    switch (input) {

      case "1":
      case "ДОБАВИТЬ ФИНАНСОВЮ ОПЕРАЦИЮ":
      case "FINANCIAL TRANSACTION":
        return FINANCIAL;

      case "2":
      case "ОБЗОР ФИНАНСОВЫХ ОПЕРАЦИЙ":
      case "OVERVIEW TRANSACTIONS":
        return OVERVIEWFINANCIAL;

      case "3":
      case "СТАТИСТИКА УСЛУГ":
      case "STATISTICNAME":
        return SERVICESTATISTICS;

      case "4":
      case "СТАТИСТИКА ТРАНЗАКЦИЙ":
      case "STATISTICS":
        return MONEYSTATISTICS;

      case "5":
      case "УДАЛИТЬ ТРАНЗАКЦИЮ":
      case "DELETE TRANSACTION":
        return DELETETRANSACTION;

      case "0":
      case "EXIT":
      case "ВЫХОД":
        return EXIT;

      default:
        return UNEXEPTED;
    }
  }

  public static MenuCommand menuCommand() {
    Scanner scanner = new Scanner(System.in);
    boolean isRun = true;
    try {
      while (isRun) {
        MenuCommand command = MenuCommand.commandRead(scanner);

        switch (command) {

          case UNEXEPTED:
            System.out.println("Не корректная команда");
            break;

          case FINANCIAL:
            AdministrativeResource.showMenuFinance(scanner);
            break;
          case OVERVIEWFINANCIAL:
            AdministrativeResource.readOrders();
            break;
          case SERVICESTATISTICS:
            AdministrativeResource.showMenuNameStatistic(scanner);
            break;
          case MONEYSTATISTICS:
            AdministrativeResource.showMenuPriceStatistic(scanner);
            break;
          case DELETETRANSACTION:
            AdministrativeResource.deleteOrder(scanner);
            break;

          case EXIT:
            isRun = false;
            System.out.println("");
            break;

        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      scanner.close();
    }
    return null;
  }
}