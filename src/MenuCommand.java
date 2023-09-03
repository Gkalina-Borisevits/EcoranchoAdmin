import java.io.IOException;
import java.util.Scanner;

public enum MenuCommand {
  UNEXEPTED(10, "", ""),
  FINANCIAL(1, "ДОБАВИТЬ ФИНАНСОВЮ ОПЕРАЦИЮ", "FINANCIAL TRANSACTION"),
  OVERVIEWFINANCIAL(2, "ОБЗОР ФИНАНСОВЫХ ОПЕРАЦИЙ", "OVERVIEW TRANSACTIONS"),

  STATISTICS(4, "ПОСМОТРЕТЬ СТАТИСТИКУ", "STATISTICS"),
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
    System.out.println("welcome to ecorancho\n");
    for (MenuCommand command : values()) {

      if (command != UNEXEPTED) {
        System.out.println(
            command.num + ". " + command.displayNameRus + " / " + command.displayNameEng);
      }
    }
  }

  public static MenuCommand commandRead(Scanner scanner) throws IOException {
    boolean isRun = true;
    MenuCommand command = null;
    while (isRun) {
      printMenu();
      System.out.print("Выберите пункт меню: \n");

      String input = scanner.nextLine().toUpperCase();

      switch (input) {

        case "10":
        case "":
          System.out.println("Не корректный ввод");
          command = UNEXEPTED;
          break;

        case "1":
        case "ДОБАВИТЬ ФИНАНСОВЮ ОПЕРАЦИЮ":
        case "FINANCIAL TRANSACTION":
          AdministrativeResource.showMenuFinance(scanner);
          command = FINANCIAL;
          break;

        case "2":
        case "ОБЗОР ФИНАНСОВЫХ ОПЕРАЦИЙ":
        case "OVERVIEW TRANSACTIONS":
          AdministrativeResource.readOrders();

          command = OVERVIEWFINANCIAL;
          break;
        case "4":
        case "ПОСМОТРЕТЬ СТАТИСТИКУ":
        case "STATISTICS":
          AdministrativeResource.showMenuStatistic(scanner);
          return STATISTICS;

        case "5":
        case "УДАЛИТЬ ТРАНЗАКЦИЮ":
        case "DELETE TRANSACTION":
          AdministrativeResource.deleteOrder(scanner);
          command = DELETETRANSACTION;
          break;

        case "0":
        case "EXIT":
        case "ВЫХОД":
          return EXIT;

        default:
          command = UNEXEPTED;
          break;
      }

    }
    return command;
  }
}