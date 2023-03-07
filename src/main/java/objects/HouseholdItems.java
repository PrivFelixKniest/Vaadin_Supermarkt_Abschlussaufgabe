package objects;

// child-class
public class HouseholdItems extends Product {
    private double recycleProportion;

    // Constructor
    public HouseholdItems(String productDesignation, double purchasePrice, double sellingPrice, boolean ageRestriction, boolean bioRestriction, double recycleProportion) {
        super(productDesignation, purchasePrice, sellingPrice, ageRestriction, bioRestriction);
        this.recycleProportion = recycleProportion;
    }

    // Get- & Set-Methods
    public double getRecycleProportion() {
        return recycleProportion;
    }

    public void setRecycleProportion(double recycleProportion) {
        this.recycleProportion = recycleProportion;
    }
}
