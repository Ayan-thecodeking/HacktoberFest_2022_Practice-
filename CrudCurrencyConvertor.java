package Currencypakage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
public class MainClass {

    Currency currency=new Currency();
    public static void main(String[] args) {
        DataBaseMethods db=new DataBaseMethods();
        db.connect();
        Recursion(db);
    }

    public static void Recursion(DataBaseMethods db){

        Scanner scan=new Scanner(System.in);
        System.out.println("What you want to do: \n" +
                "1.Create table \n" +
                "2.Insert And Save Currency \n" +
                "3.Update Currency\n" +
                "4.Drop Table \n" +
                "5.See currency Data\n" +
                "6.Convert currency in to Dollar\n"+
                "7.Stop Program\n"+
                "Enter Choice:");
        int choice=scan.nextInt();
        switch (choice){
            case 1:
                db.createCurrencyTable();
                Recursion(db);
                break;
            case 2:
                db.insertCurrenyTable(getCurrency());
                Recursion(db);
                break;
            case 3:
                System.out.println("What you want to update \n 1.name_of_currency " +
                        "\n 2.amount_of_currency");
                int updateChoice=scan.nextInt();
                System.out.println("Enter id");
                int id=scan.nextInt();
                if(updateChoice==1)
                {
                    System.out.println("Enter new name");
                    String newUpdateName=scan.next();
                    db.updateCurrencyName(id,newUpdateName);
                }
                else if (updateChoice==2)
                {
                    System.out.println("Enter new amout of curreny");
                    int newAmountOfcurrency=scan.nextInt();
                    db.updateCurrencyAmount(id,newAmountOfcurrency);
                }
                Recursion(db);
                break;
            case 4:
                db.dropCurrencyTable();
                Recursion(db);
                break;
            case 5:
                db.retriveCurrencyTable();
                Recursion(db);
                break;
            case 6:
                System.out.println("How many dollars you have:");
                int numberOfDollars= scan.nextInt();
                for(Calculation calculation:db.retriveCurrencyCalculation())
                {
                    calculation.Calcuation(numberOfDollars);
                }
                Recursion(db);
                break;

            default:
                break;
        }
    }

    public static List<Currency> getCurrency() {
        List<Currency> currencyList= new ArrayList<>();
        System.out.println("how many currencies you have?");
        Scanner scan = new Scanner(System.in);

        int numberOfCurrency = scan.nextInt();
        for (int i = 0; i < numberOfCurrency; i++) {
            Currency currency = new Currency();
            System.out.println("Name Of Currency");
            currency.nameOfCurrency = scan.next();

            System.out.println("Amount of currency equal to one dollar:");
            currency.amountOfCurrency = scan.nextInt();
            currencyList.add(currency);
        }
        return currencyList;
    }
}

class DataBaseMethods {
    private Connection connect=null;
    public Statement statement;
    String dbname="Currency";
    public void connect(){
        try{
            Class.forName("org.postgresql.Driver");
            String user="postgres";
            String password="ali";

            this.connect= DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname,user,password);
            if(this.connect!=null)
            {
                System.out.println("Connection Established");
            }
            else{
                System.out.println("Connection Failed");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public List<Calculation> retriveCurrencyCalculation() {
        ResultSet result;
        List<Calculation> calculateList=new ArrayList<>();
        try {
            String Query = "select * from tbl_currency;";
            statement = connect.createStatement();
            result=statement.executeQuery(Query);
            while(result.next())
            {
                Calculation calculation=new Calculation();

                calculation.nameOfCurrency=result.getString("name_of_currency");
                calculation.amountOfcurrency=result.getInt("amount_of_currency");
                calculateList.add(calculation);
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        return calculateList;
    }


    public void createCurrencyTable() {
        Statement statement;
        try {
            String createQuery = "Create table tbl_currency (id Integer generated always as identity,name_of_currency varchar(200)," +
                    "amount_of_currency Integer," +
                    "primary key (id));";
            statement = this.connect.createStatement();
            statement.executeUpdate(createQuery);
            System.out.println("Table Created");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void insertCurrenyTable(List<Currency> currencyList){
        try {
            String insertQuery = "Insert into public.tbl_currency (name_of_currency,amount_of_currency)" +
                    " values ('%s',%s);";
            for(Currency currency:currencyList) {
                String Query=String.format(insertQuery,currency.nameOfCurrency,currency.amountOfCurrency);
                this.statement = this.connect.createStatement();
                this.statement.executeUpdate(Query);
                System.out.println("Row Inserted");
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void updateCurrencyName(int id,String nameofcurrency){
        try{


            String updateQuery="Update tbl_currency set name_of_currency='%s' where id=%s ;";
            String Query=String.format(updateQuery,nameofcurrency,id);
            this.statement=this.connect.createStatement();
            this.statement.executeUpdate(Query);
            System.out.println("Updated");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void updateCurrencyAmount(int id,int newAmountOfcurrency){
        try{


            String updateQuery="Update tbl_currency set amount_of_currency=%s where id=%s ;";
            String Query=String.format(updateQuery,newAmountOfcurrency,id);
            statement=connect.createStatement();
            statement.executeUpdate(Query);
            System.out.println("Updated");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void dropCurrencyTable() {
        try {
            String dropQuery = "Drop table tbl_currency;";
            this.statement = this.connect.createStatement();
            this.statement.executeUpdate(dropQuery);
            System.out.println("Table Dropped");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void retriveCurrencyTable() {
        ResultSet result;
        try {
            String Query = "select * from tbl_currency;";
            statement = connect.createStatement();
            result=statement.executeQuery(Query);
            while(result.next())
            {
                System.out.print("  "+result.getString("id"));
                System.out.print("  "+result.getString("name_of_currency"));
                System.out.println("  "+result.getString("amount_of_currency"));
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }
}

class Currency {
    String nameOfCurrency;
    int amountOfCurrency;
}

class  Calculation{
    String nameOfCurrency;
    int amountOfcurrency;

    public void Calcuation(int numberOfDollars){
        System.out.println(nameOfCurrency + "=" + numberOfDollars * amountOfcurrency);
    }
}

