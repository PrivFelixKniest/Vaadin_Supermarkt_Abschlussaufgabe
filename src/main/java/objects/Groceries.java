package objects;
import java.util.Date;
import java.time.LocalDate;

// child-class
public class Groceries extends Product {
    private LocalDate bestByDate;
    private boolean isBio;

    // Constructor
    public Groceries(String productDesignation, double purchasePrice, double sellingPrice, boolean ageRestriction, LocalDate bestByDate, boolean isBio) {
      super(productDesignation, purchasePrice, sellingPrice, ageRestriction);
      this.bestByDate = bestByDate;
      this.isBio = isBio;
    }

    // Get- & Set-Methods
    public LocalDate getBestByDate() {
        return bestByDate;
    }
    public void setBestByDate(LocalDate bestByDate) {
        this.bestByDate = bestByDate;
    }
    public boolean isBio() { return isBio; }
    public void setBio(boolean bio) { isBio = bio; }

}
