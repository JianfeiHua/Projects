package Projects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DevDraftECommerce {
    
}    
    
/*    class CostCalculator
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
        
        public Address (String addressLine)
        {
            this.addressLine = addressLine;
        }
        
        public String getStreetAddress()
        {
            //take everything before the first comma
            return addressLine.split(",")[0].trim();
        }
        
        public String getCityName()
        {
            //the city appears after the first comma
            return addressLine.split(",")[1].trim();
        }
        
        public String getState()
        {
            //state appears after 2 commas
            String stateLine = addressLine.split(",")[2].trim();
            return stateLine.split(" ")[0].trim();
        }
        
        public int getZipCode()
        {
            int consecutiveDigits = 0;
            
            //search for something that matches 5 consecutive digits
            for (int i=0; i<addressLine.length(); i++)
            {
                char c = addressLine.charAt(i);
                if (Character.isDigit(c))
                {
                    consecutiveDigits++;
                    if (consecutiveDigits == 5)
                    {
                        return Integer.parseInt(addressLine.substring(i-4, i+1));
                    }
                }
                else
                {
                    consecutiveDigits = 0;
                }
            }
            
            //should never happen
            return 0;
        }
    }
        
    class TaxCalculator
    {
        public static int calculateTax(int orderAmount, String state)
        {
            if (state.equals("Arizona"))
            {
                return orderAmount / 100 * 5;
            }
            if (state.equals("Washington"))
            {
                return orderAmount / 100 * 9;
            }
            if (state.equals("California"))
            {
                return orderAmount / 100 * 6;
            }
            if (state.equals("Delaware"))
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
            if (zipCode >= 75000)
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
*/
