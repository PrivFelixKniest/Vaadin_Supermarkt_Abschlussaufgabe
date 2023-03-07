package objects;

// child-class
public class GeneralItems extends Product {

    // Constructor
    public GeneralItems(String productDesignation, double purchasePrice, double sellingPrice, boolean ageRestriction, boolean bioRestriction) {
        super(productDesignation, purchasePrice, sellingPrice, ageRestriction, bioRestriction);
    }
}
