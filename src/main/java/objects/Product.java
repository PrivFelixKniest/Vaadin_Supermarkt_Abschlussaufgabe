package objects;

// mother-class
public abstract class Product {
    public static int counter;
    private final String PRODUCTID;
    private String productDesignation;
    private double purchasePrice;
    private double sellingPrice;
    private boolean ageRestriction;
    private boolean bioRestriction;

    // Super-Constructor
    Product(String productDesignation, double purchasePrice, double sellingPrice, boolean fsk, boolean bio) {
        this.PRODUCTID = Integer.toString((counter++));
        this.productDesignation = productDesignation;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.ageRestriction = fsk;
        this.bioRestriction = bio;
    }

    // Get- & Set-Methods
    public String getProductId() { return PRODUCTID;}
    public String getProductDesignation() {
        return productDesignation;
    }

    public void setProductDesignation(String productDesignation) {
        this.productDesignation = productDesignation;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Product.counter = counter;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public boolean isAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(boolean ageRestriction) {
        this.ageRestriction = ageRestriction;
    }
    
    public boolean getBioRestriction() {
    	return bioRestriction;
    }
}
