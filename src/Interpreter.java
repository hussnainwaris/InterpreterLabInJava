import java.io.IOException;
import java.util.ArrayList;



public class Interpreter {
	
	String path;

	static String[] lines ;				// store the data of file
	String[] splitter;						// array to store after splitterenizing single line
	String variableName;							// to store variableNameiable name
	int variableValue=0;						// to store variableValue of variableNameiable
	int error=0;							// error to help in checking that variableNameiable is already assigned or not
	int i=0, j=0;						// loop iterators
	String name;						
	int first=0;						// first operand
	int second=0;						// second operand
	int result=0;						// to store result after arithmetic operations 
	String temp;						
	int number=0;						
	
	ArrayList<Interpreter>  list = new ArrayList<Interpreter>() ;			//Array list to store key variableValue for variableNameiables and their variableValues
	
	//for storing key variableValue pair
	public Interpreter(String x, int y)
	{
		variableName=x;												
		variableValue=y;
	}
	
	// setting file path
	public Interpreter(String filePath)
    {
        path = filePath;
    }

	
    public void Interpret() throws IOException
    {
    	try
    	{
    		FileHandling file = new FileHandling(path);					//calling read file class
    		lines = file.OpenFile();							// storing file data in lines through calling function of FileHandling class
    		
    		//Interpreting the code i.e line by line
    		for(i=0; i<lines.length;i++)
    		{
    			splitter=lines[i].split(" ");						//splitting on the basis of space
    			
    			if(splitter[0].equals("Var"))						//checking variableName declarative to initialize
    			{	
    				//System.out.println("Initialize");
    				if(!("=".equals(splitter[2])))					// check if assignment operation is not present
    				{
    					System.out.println("Syntax Error at: "+(i+1) + "  = operator is missing");
    					continue;
    				}
    				
    				String name= splitter[1];						//variableNameiable name
    				int val= Integer.parseInt(splitter[3]);			//variableValue of variableNameiable
    				list.add(new Interpreter(name,val));		//storing variableNameiable name and its variableValue in key variableValue form in ArrayList
    			}

    			else if(splitter[0].equals("print"))					//checking print declarative to print variableValue
    			{
    				//System.out.println("Printing");
    				String name = splitter[1];						// variableNameialble name
    				
    				//printing immediate variableValue 
    				if(isInteger(splitter[1]))						
    				{
						System.out.println(splitter[1]);
    				}
    				
    				else
    				{
    					//traversing complete ArrayList to print variableValue of required variableNameiable
    					for(j=0; j<list.size();j++)
    					{
    					
    					
    						//printing if variableNameiable is initialized by checking its presence in ArrayList
    						if(name.contains(list.get(j).variableName))
    						{
    							//System.out.println(name);
    							System.out.println(list.get(j).variableValue);		
    							error=1;										//setting error 1 if variableNameiable is already initialized
    						}
    					}
    			
    					//if variableNameiable is not initialized so, its not present in the ArrayList
    					if(error==0)
    					{
    						System.out.println("Error at: "+(i+1)+" " + name +" is not intialized.");
    					}
    				
    					error=0;											//setting error back to zero
    				}
    			}
    			
    			else											//arithmetic operations statements
    			{
    				if(!("=".equals(splitter[1])))					// check if assignment operation is not present
    				{
    					System.out.println("Syntax Error at line: "+(i+1) + " = operator is missing");
    					continue;
    				}
    				
    				// L.H.S of = must be variableNameiable , it can't be number
    				if(isInteger(splitter[0]))						//checking L.H.S of = is integer or not
    				{
    					System.out.println("Syntax Error at: "+(i+1) + " variableValue can't be assigned to immediate variableValue");
    					continue;
    				}
    				else										// if L.H.S of = is variableNameiable , then checking is it initialized or not
    				{
    					if(variableNameiableInitialized(splitter[0],i)!=-1)
    					{
    						name = splitter[0];
    					}
    					
    				}

    				//variableNameiableInitialized(name);
    				
    				if(isInteger(splitter[2]))								//if first operand is just number
    				{
    					first=Integer.parseInt(splitter[2]);
    				}
    				else												// if first operand is variableNameiable
    				{
    					if((number=variableNameiableInitialized(splitter[2],i))!=-1)		//if variableNameiable then checking its existence in ArrayList
    					{
    						first = number;
    					}
    					
    				}
    				
    				if(isInteger(splitter[4]))								//if second operand is just number	
    	    		{
    					second = Integer.parseInt(splitter[4]);
    	    		}
    				else												// if second operand is variableNameiable
    				{
    					if((number=variableNameiableInitialized(splitter[4],i))!=-1)   	//if variableNameiable then checking its existence in ArrayList
    					{
    						second = number;
    					}
    				}
    				
    				
    				// checking and applying arithmetic operations
    				switch (splitter[3])								
    				{
    				case "+":
    					//System.out.println("addition");
    					result=first + second;
    					SetvariableValue(name , result);
    				
    					
    					break;
    			
    				case "-":
    					//System.out.println("subtraction");
    					result=first - second;
    					SetvariableValue(name , result);
    					
    					break;
    					
    				case "*":
    					//System.out.println("Multiplication");
    					result=first * second;
    					SetvariableValue(name , result);
    					
    					break;
    					
    				case "/":
    					//System.out.println("Division");
    					result=first / second;
    					SetvariableValue(name , result);
    					break;
    					
    				default:
    					System.out.println("Syntax Error at: "+(i+1)+" Invalid aritmetic operation");
    					break;
    				
    				}
    				
    			}
    		}
	
    	}
    	

        catch(IOException e){
            System.out.println( e.getMessage() );
        }

        
    }
    
    //checking if string is numeric or not
    public  boolean isInteger(String str)  
    {  
      try  
      {  
    	  Integer.parseInt(str);
    	  return true;
      }  
      catch(NumberFormatException e)  
      {  
        return false;  
      }  
     
    }

    //checking if variableNameiable is initialized or not by checking its presence in ArrayList
	
    public int variableNameiableInitialized(String name, int k)
    {
    	int num=0;
    	for(j=0; j<list.size();j++)
		{
			if(name.contains(list.get(j).variableName))
			{
				num=list.get(j).variableValue;
				error=1;								//setting error 1 if variableNameiable is already initialized
			}
		}
		
		if(error==0)									//if variableNameiable is not initialized so, its not present in the ArrayList
		{
			System.out.println("Error at: "+(k+1)+"  "  + name +" is not initialized");
			return -1;
		}
		error=0;
		return num;								// returning variableValue against variableNameiable name if its present
    }
    
    
    //Setting the variableValue against its variableNameiable name
    public void SetvariableValue(String name , int result)
    {
    	//traversing the ArrayList to store Value against its variableNameiable
    	for(j=0; j<list.size(); j++)
		{
			if(name.contains(list.get(j).variableName))
			{
				list.set(j, new Interpreter(name,result));
			}
		}
    }

	public static void main(String[] args) throws IOException 
	{
    	String fileName;
    	fileName = "SampleFile.txt";
    	
    	Interpreter newIr= new Interpreter(fileName);
    	newIr.Interpret();
    	
    	
    }


}
