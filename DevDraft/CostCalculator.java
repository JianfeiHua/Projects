package Projects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class CostCalculator
{
    public static void main(String[] args) throws IOException
    {
        BufferedReader in = new BufferedReader (new InputStreamReader(System.in));                
        int numTestCases = Integer.parseInt(in.readLine().trim());
                
        for (int i=0; i<numTestCases; i++)
        {            
            int basePrice = Integer.parseInt(in.readLine().trim());
            String addressString = in.readLine();
            Address addr = new Address(addressString);
            
            int taxAmount = TaxCalculator.calculateTax(basePrice, addr.getState());
            int shippingAmount = ShippingCalculator.calculateShipping(addr.getZipCode());
            
            System.out.println (basePrice + taxAmount + shippingAmount);
        }
    }
}

class Address
{    
    public String addressLine;
    public String[] parts;
    
    public Address (String addressLine)
    {
        this.addressLine = addressLine;
        parts = addressLine.split(",");
    }
    
    public String getStreetAddress()
    {   
        // no second street line, return the first part
        if (parts.length == 3) return parts[0].trim();
        // with second or more street lines, return all string parts 
        // except the last two. 
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length-2; i++) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(parts[i].trim());
        }
        return sb.toString();
    }
    
    public String getCityName()
    {
        //the city appears after the second last comma
        return parts[parts.length-2].trim();
    }
    
    public String getState()
    {
        //state appears in the last string part
        String stateLine = parts[parts.length-1].trim();
        return stateLine.split(" ")[0];
    }
    
    public int getZipCode()
    {
        // zipcode in the last string part
        String stateLine = parts[parts.length-1].trim();
        return Integer.parseInt(stateLine.split(" ")[1]);
    }
}
    
class TaxCalculator
{
    public static int calculateTax(int orderAmount, String state)
    {
        if (state.equals("Arizona") || state.equals("AZ"))
        {
            return orderAmount / 100 * 5;
        }
        if (state.equals("Washington") || state.equals("WA"))
        {
            return orderAmount / 100 * 9;
        }
        if (state.equals("California") || state.equals("CA"))
        {
            return orderAmount / 100 * 6;
        }
        if (state.equals("Delaware") || state.equals("DE"))
        {
            return 0;
        }
        return orderAmount / 100 * 7;            
    }
}

class ShippingCalculator
{
    public static int calculateShipping(int zipCode)
    {
        if (zipCode > 75000)
        {
            return 10;
        }
        else if (zipCode >= 25000)
        {
            return 20;
        }
        else
        {
            return 30;
        }
    }
}
