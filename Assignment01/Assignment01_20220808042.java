package Assignment01;
import java.util.ArrayList;
/**
 *@author Veysel Reşit Çaçan
 *@version 19.0.1
 *@since 19.03.2023
 **/

class Product{
    private String Id;
    private String Name;
    private int Quantity;
    private double Price;

    public Product(String Id, String Name,int Quantity, double Price) {
       this.Id = Id;
       this.Name = Name;
       this.Price = Price;
       this.Quantity = Quantity; 
    }
    public String getId() {
        return Id;
    }
    public void setId(String Id){
        this.Id = Id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String Name){
        this.Name = Name;
    }
    public double getPrice() {
        return Price;
    }
    public void setPrice(double Price){
        this.Price = Price;
    }
    public int remaining(){
        return Quantity;
    }
    public int addToInventory(int amount){
        if(amount > 0)
            Quantity += amount;
            return Quantity;
    }
    public double purchase(int amount){
        if(amount > 0){
            Quantity -= amount;
            return Quantity * Price;
        }
        return 0;
    }
    public String toString(){
        return "Product " + Name + " has " + Quantity + " remaining";
    }

    public boolean equals(Object o){
        if(o instanceof Product){
            Product p = (Product)o;
            if(Math.abs(p.Price - Price) <= 0.001)
            return true;
        }
        
        return false;
    }

}
class FoodProduct extends Product{
    private int Calories;
    private boolean Dairy;
    private boolean Eggs;
    private boolean Peanuts;
    private boolean Gluten;

    public FoodProduct(String Id, String Name, int Quantity, double Price, int Calories, boolean Dairy, boolean Eggs, boolean Peanuts, boolean Gluten) {
        super(Id, Name, Quantity, Price);
        this.Calories = Calories;
        this.Dairy = Dairy;
        this.Eggs = Eggs;
        this.Peanuts = Peanuts;
        this.Gluten = Gluten;
    }

    public int getCalories() {
        return Calories;
    }
    public void setCalories(int Calories) {
        this.Calories = Calories;
    }
    public boolean containsDairy() {
        return Dairy;
    }
    public boolean containsEggs() {
        return Eggs;
    }
    public boolean containsPeanuts() {
        return Peanuts;
    }
    public boolean containsGluten() {
        return Gluten;
    }

    
    
}
class CleaningProduct extends Product{
    private boolean Liquid;
    private String WhereToUse;

    public CleaningProduct(String Id, String Name, int Quantity, double Price, boolean Liquid, String WhereToUse) {
        super(Id, Name, Quantity, Price);
        this.Liquid = Liquid;
        this.WhereToUse = WhereToUse;
    }

    public String getWhereToUse(){
        return WhereToUse;
    }
    public void setWhereToUse(String size){
        this.WhereToUse = size;
    }
    public boolean isLiquid() {
        return Liquid;
    }
}
class Customer{
    private String Name;

    public Customer(String Name) {
        this.Name = Name;
    }
    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String toString(){
        return Name;
    }
}
class ClubCustomer extends Customer{
    private String Phone;
    private int Points;

    public ClubCustomer(String Name, String Phone) {
        super(Name);
        this.Phone = Phone;
        this.Points = 0;
    }
    public String getPhone() {
        return Phone;
    }
    public void setPhone(String Phone) {
        this.Phone = Phone;
    }
    public int getPoints() {
        return Points;
    }
    public void addPoints(int points){
        if (points > 0)
        this.Points += points;
    }
    public String toString(){
        return getName() + " has " + Points + " points";
    }
}
class Store{
    private String Name;
    private String Website;
    private ArrayList<Product> products;

    Store(String name, String website){
        this.Name = name;
        this.Website = website;
        ArrayList<Product> products = new ArrayList<Product>();
        this.products = products;
    }
    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getWebsite() {
        return Website;
    }
    public void setWebsite(String Website) {
        this.Website = Website;
    }
    public int getInventorySize(){
        return products.size();
    }
    public void addProduct(Product product, int index){
        if(index >= 0 && index < products.size()){
        products.add(index, product);}
        else
        products.add(product);
    }
    public void addProduct(Product product){
        products.add(product);
    }
    public Product getProduct(int index){
        if(index >= 0 && index < products.size()){
        return products.get(index);}
        else
        return null;
    }
    public int getProductIndex(Product p){
        if(products.contains(p)){
        return products.indexOf(p);}
        else
        return -1;
    }
}

