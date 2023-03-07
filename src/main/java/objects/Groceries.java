package objects;
import java.util.Date;
import java.time.LocalDate;

// child-class
public class Groceries extends Product {
    private LocalDate bestByDate;

    // Constructor
    public Groceries(String productDesignation, double purchasePrice, double sellingPrice, boolean ageRestriction, boolean bioRestriction, LocalDate bestByDate) {
      super(productDesignation, purchasePrice, sellingPrice, ageRestriction, bioRestriction);
      this.bestByDate = bestByDate;
    }

    // Get- & Set-Methods
    public LocalDate getBestByDate() {
        return bestByDate;
    }
    public void setBestByDate(LocalDate bestByDate) {
        this.bestByDate = bestByDate;
    }
}
