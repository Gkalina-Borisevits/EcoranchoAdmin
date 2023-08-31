import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class OrdersFile {

  private final File file;
  private final String separator;

  public OrdersFile(String fileName, String delimiter) throws IOException {
    file = new File(fileName);
    if (!file.exists()) {
      if (!file.getParentFile().mkdirs() || !file.createNewFile()) {
        throw new RuntimeException("Не получилось создать файл: " + fileName);
      }
    }
    if (!(file.canRead() && file.canWrite())) {
      throw new IllegalArgumentException("Файл не доступен: " + fileName);
    }
    this.separator = delimiter;
  }

  public TreeMap<LocalDate, List<Order>> readOrder() throws FileNotFoundException {
    TreeMap<LocalDate, List<Order>> orders = new TreeMap<>();
    Scanner scanner = new Scanner(file);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
    }
    scanner.close();
    return orders;
  }
}
