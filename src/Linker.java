import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Linker {
	static ObjectInInterface object1 = new ObjectIn();
	static ObjectInInterface object2 = new ObjectIn(); 
	static ObjectInInterface object3 = new ObjectIn();
	static ObjectInInterface object4 = new ObjectIn();
	static ObjectInInterface object5 = new ObjectIn();
	
	//object to hold the different objectArrays
	static private ArrayList<ArrayList<ArrayList<String>>> objectArrays = new ArrayList<ArrayList<ArrayList<String>>>(5);

	//linker error output
	static private File LinkerUserReport = new File("LinkerUserReport.txt");
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		//create global variables
		ArrayList<ArrayList<String>> objectArray;
		boolean validObject = true;
		LinkerTableInterface GST = new LinkerTable();
		Integer moduleLength = 0;
		ConverterInterface converter = new Converter();
		
		

		//create printWriter for linkerFile
		PrintWriter UserOut = new PrintWriter (new BufferedWriter(new FileWriter(LinkerUserReport)));
		
		//begin loop for multiple files
		for(int inc = 0; inc < args.length && inc < 2; inc++)
		{
		
			//import object files into object arrays
			//class object imports
			ObjectInInterface objectFile = new ObjectIn();
			
			//make objecFileName from args
			File objectFileName = new File(args[inc]);
			
			//Read object file
			objectFile.readObjectFile(objectFileName);
		
			//create object array
			objectArray = objectFile.outputObjectArray();
			
			//set validObject boolean
			validObject = checkObjectValidity(objectArray, UserOut);
			
			//if the object is valid, then
			if (validObject)
			{
				
				//store in appropriate ObjectIn global var	
				if(inc == 0)
				{
					object1 = objectFile;
				}
				else if(inc == 1)
				{
					object2 = objectFile;
				}
				else if(inc == 2)
				{
					object3 = objectFile;
				}
				else if(inc == 3)
				{
					object4 = objectFile;
				}
				else if(inc == 4)
				{
					object5 = objectFile;
				}
				
				
				
			}
			
			//else, output error 
			else
			{
				
			}
					
			//store in objectArrays
			objectArrays.add(inc, objectArray);
			
		//end for loop for multiple files
		}
		
		//populate symbol table
		System.out.println(">>>>>>>> Popluating Symbol Table <<<<<<<<");
		populateSymbolTable(GST);
		
		
		
		
		//things that need to be done outside of loop
		//they need all objects to be processed before they can be done
		
		
		//create linker file
		System.out.println(">>>>>>>> Building Linker Load Module <<<<<<<<");
		
		
			//create file itself
		File linkerFile = new File("LoadModule.ld");
		
		//create printWriter for linkerFile
		PrintWriter out = new PrintWriter (new BufferedWriter(new FileWriter(linkerFile)));
		
		//Set up the date and time for printing.		
		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd|HH:mm:ss");
        Date date = new Date();
        
        //set module length
        moduleLength = GST.getLength();
        
        //convert moduleLength to a string and format it
        String moduleLen = converter.decimalToHex(moduleLength.toString());
        while(moduleLen.length() < 4)
        {
        	moduleLen = "0" + moduleLen;
        }
		
		//create header record
		//print "LH|" execStartAddress "|"
		out.print("LH|" + objectArrays.get(0).get(0).get(8) + "|");
		//print moduleName "|"
		out.print(objectArrays.get(0).get(0).get(1) + "|");
		//print moduleLength in hex followed by a "\"
		out.print(moduleLen + "|");
		//print load address followed by "|"
		out.print(objectArrays.get(0).get(0).get(3) + "|");
		//print date "|" time "|SAL-LINK|
		out.print(dateFormat.format(date) + "|SAL-LINK|");
		//print version# "|" 
		out.print(objectArrays.get(0).get(0).get(10) + "|");
		//print revision# "|" 
		out.print(objectArrays.get(0).get(0).get(11) + "|");
		//print module name
		out.println(objectArrays.get(0).get(0).get(1));
		
		
		
			//create text records
		int textRecordCount = buildLinkerFile(out, GST);
		
		//create variable to hold total number of records. It is equal to textRecordCOunt + 2
		String recordCount = Integer.toString(textRecordCount + 2);
		
		//format recordCount
		recordCount = converter.decimalToHex(recordCount);
		while(recordCount.length() < 4)
		{
			recordCount = "0" + recordCount;
		}
		
		
			//create end record
		//print "LE|" total records "|"
		out.print("LE|" + recordCount + "|");
		//print module name
		out.println(objectArrays.get(0).get(0).get(1));		
		
		
		//finish
		out.close();
		UserOut.close();
		System.out.println(">>>>>>>> Linking Complete <<<<<<<<");
		

	}
	
	/**This method will check if the object file is valid.
	 * 
	 * @param objectArray ArrayList<ArrayList<String>> containing tokenized object file
	 * @return true if object is valid
	 */
	static boolean checkObjectValidity(ArrayList<ArrayList<String>> objectArray, PrintWriter out)
	{
		
		//create converter
		ConverterInterface converter = new Converter();
		
		// declaring global variables to use throughout the method
	    boolean isValid = true;

	    // a converter used to convert hex values into decimal for validation
	    // purposes
	    ConverterInterface conv = new Converter();

	    // boolean value to check first line to see if it is a header record, as
	    // it should be;
	    // after first check, acts as a swtich telling the method whether or not
	    // a program has begun
	    boolean hasHeader = false;

	    // boolean acting as a switch, telling the method whether or not a
	    // program has ended
	    boolean hasEnded = true;

	    // keeps track of the total number of text records and linking records
	    // in the object file
	    int textRecs = 0;
	    int linkRecs = 0;

	    // boolean values act as switch for whether or not the number of text or
	    // linking records are correct
	    boolean textWrong = false;
	    boolean linkWrong = false;

	    // create arraylist that holds the first record of the object file
	    ArrayList<String> firstRec = new ArrayList<String>(objectArray.get(0));

	    // output intermediate message for testing / validation reasons
	    System.out.println("Now validating record:");

	    for (int j = 0; j < firstRec.size() - 1; j++) {
	        System.out.print(firstRec.get(j));
	        System.out.print("|");
	    }
	    System.out.println(firstRec.get(firstRec.size() - 1));

        // output record to user report file
        for (int j = 0; j < firstRec.size() - 1; j++) {
            out.print(firstRec.get(j));
            out.print("|");
        }
        out.println(firstRec.get(firstRec.size() - 1));
        
	    // the program name to be stored; should be consistent throughout the
	    // object file
	    // [linker-adjusted loading address to use for overall linking file
	    // address]
	    String linkLoadAddr = firstRec.get(3);

	    // [execution start address for program]
	    String execStartAddr = firstRec.get(8);

	    // [program length]
	    String pgmLen = firstRec.get(2);

	    // check for header

	    // holds value of the first character in what should be the header
	    // record
	    Character head = firstRec.get(0).charAt(0);

	    /*
	     * if the first character isn't an 'H' or 'h', or if the length of the
	     * header file then report invalid header file error and abort.
	     */
	    if (!(head.equals('H') && firstRec.size() == 13)) {
	        // errors put into linking file??
	        out.println("Error at record 0: INVALID HEADER RECORD! ABORTING LINKING PROCESS");
	        return hasHeader;
	    }

	    // otherwise, dig through the header record

	    // first things first, setting the switches correctly. This object file
	    // has a header record
	    // and has not run into an end record yet.
	    hasHeader = true;
	    hasEnded = false;

	    // save the name of the program
	    String pgmName = firstRec.get(1);

	    // check module name in second field with program name in 12th field
	    if (!(pgmName.equals(firstRec.get(12)))) {
	        out.println("Error at record 0: module name and program name fields do not match");
	        isValid = false;
	    }

	    // check validity of each hex field, then verify the length

	    // if the program length is not in valid hex OR is too long ...
	    if (!(conv.isValidHex(pgmLen) && pgmLen.length() <= 4)) {

	        // throw the error and declare invalidity
	        out.println("Error at record 0: program length field is not in valid syntax: "
	                + " \"hhhh\" where h is a valid hexadecimal number");
	        isValid = false;
	    }

	    // if the assembler assigned program load address is not in valid hex OR
	    // is too long ...
	    if (!(conv.isValidHex(linkLoadAddr) && linkLoadAddr.length() <= 4)) {

	        // throw the error and declare invalidity
	        out.println("Error at record 0: assembler assigned program load address"
	                + " field is not in valid syntax: \"hhhh\" where h is a valid hexadecimal number");
	        isValid = false;
	    }

	    // if the execution start address is not in valid hex OR is too long ...
	    if (!(conv.isValidHex(execStartAddr) && execStartAddr.length() <= 4)) {

	        // throw the error and declare invalidity
	        out.println("Error at record 0: execution start address field is not in"
	                + " valid syntax: \"hhhh\" where h is a valid hexadecimal number");
	        isValid = false;
	    }

	    // if the number of text records is not in valid hex OR is too long ...
	    if (!(conv.isValidHex(firstRec.get(7)) && firstRec.get(7).length() <= 4)) {

	        // throw the error and declare invalidity
	        out.println("Error at record 0: number of text records field is not in"
	                + " valid syntax: \"hhhh\" where h is a valid hexadecimal number");
	        isValid = false;
	    } else {

	        // save the value for the number of text records expected
	        textRecs = Integer.parseInt(conv.hexToDec(firstRec.get(7)));
	    }

	    // if the number of linking records is not in valid hex OR is too long
	    // ...
	    if (!(conv.isValidHex(firstRec.get(6)) && firstRec.get(6).length() <= 4)) {

	        // throw the error and declare invalidity
	        out.println("Error at record 0: number of linking records field is not in"
	                + " valid syntax: \"hhhh\" where h is a valid hexadecimal number");
	        isValid = false;
	    } else {

	        // save the value for the number of linking records expected
	        linkRecs = Integer.parseInt(firstRec.get(6));
	    }

	    // --------check the date field for proper syntax and values--------- //

	    // string for the year portion of the date field
	    String year = firstRec.get(4).substring(0, 4);

	    // if the year is too far in the future ...
	    if (Integer.parseInt(year) > 2010) {

	        // give a [funny ;} ] warning
	        out.println("Warning: given year is too far in the future!"
	                + "\nRecommended action: give Marty McFly his time machine back.");
	    }

	    // if the year is too far in the past ...
	    else if (Integer.parseInt(year) < 2010) {

	        // give a [funny ;} ] warning
	        out.println("Warning: given year is too far in the past!"
	                + "\nRecommended action: borrow Marty McFly's time machine and go back to the future.");
	    }

	    // string for the day portion of the date field
	    String day = firstRec.get(4).substring(5);

	    // check bounds for the day; anything else that should be checked ???
	    if (!(Integer.parseInt(day) >= 0 && Integer.parseInt(day) <= 365)) {

	        // give a [funny ;} ] warning
	        out.println("Warning: given day is outside reasonable bounds for the day."
	                + "\nRecommended action: check syntax, or if in a leap year, please try again tomorrow! =)");
	    }

	    // check the time field for proper syntax
	    String time = firstRec.get(5);

	    // if the time markings aren't in proper syntax and range ...
	    if (!(time.length() == 8 && Integer.parseInt(time.substring(0, 2)) < 24
	            && Integer.parseInt(time.substring(3, 5)) < 60 && Integer
	            .parseInt(time.substring(6)) < 60)) {

	        // give a [funny ;} ] warning
	        out.println("Warning: given time is not in proper syntax or range: hh:mm:ss where hh == hours, mm == minutes, and ss == seconds."
	                + "\nRecommended action: check syntax, or buy your assembler a watch! =P");
	    }

	    System.out.println(">>>>>>>>>>>>>>>>Record validation complete");
	    // iteration to go through each next line to validate the object file
	    for (int i = 1; i < objectArray.size(); i++) {

	        // create arraylist that holds the next record of the object file
	        ArrayList<String> nextRec = new ArrayList<String>(
	                objectArray.get(i));

	        // output intermediate message for testing / validation reasons
	        System.out.print("Now validating record:");

	        for (int j = 0; j < nextRec.size() - 1; j++) {
	            System.out.print(nextRec.get(j));
	            System.out.print("|");
	        }
	        System.out.println(nextRec.get(nextRec.size() - 1));

	        // output record to user report file
	        for (int j = 0; j < nextRec.size() - 1; j++) {
	            out.print(nextRec.get(j));
	            out.print("|");
	        }
	        out.println(nextRec.get(nextRec.size() - 1));
	        
	        // distinguish the type of record by the letter of the first field
	        Character recType = nextRec.get(0).charAt(0);

	        if (recType.equals('L')) {

	            /*
	             * if a linking record is encountered, it should be between a
	             * header and an end record. thus, hasHeader should be true and
	             * hasEnded should be false
	             */

	            // if the above requirements isn't true ...
	            if (!(hasHeader && !hasEnded)) {

	                // throw the error and "devalidate" the object file
	                out.println("Error at record " + i
	                        + ":misplaced linking record");
	                isValid = false;
	            }

	            // if the number of linking records given in the header record
	            // is exceeded,
	            // and the error switch is not thrown yet ...
	            if (linkRecs <= 0 && !(linkWrong)) {

	                // turn the switch, throw the error, and "devalidate" the
	                // object file
	                linkWrong = true;
	                out.println("Error at record " + i
	                        + ":number of linking records given by header "
	                        + "record is exceeded.");
	                isValid = false;
	            } else {

	                // otherwise, decrement the number of linking records to
	                // find and validate the linking record
	                linkRecs--;
	            }

	            // throw error and devalidate the object file

	            // store the address of the name of the entry in the object file
	            String entryAddress = nextRec.get(2);

	            // if the address is not in proper syntax or if the memory
	            // bounds are violated ...
	            if (!(conv.isValidHex(entryAddress))) {

	                // throw the error and "devalidate" the object file
	                out.println("Error at record "
	                        + i
	                        + ":address of linking record entry is not in proper syntax");
	                isValid = false;
	            } else if (!(Integer.parseInt(conv.hexToDec(entryAddress)) <= 65535)) {

	                // throw the error and "devalidate" the object file
	                out.println("Error at record "
	                        + i
	                        + ":address of linking record entry is outside memory bounds");
	                isValid = false;
	            }

	            // store the value of the type of the entry in the object file
	            String entryType = nextRec.get(3);

	            // if the type of the entry does not equal 'start' or 'ent' ...
	            if (!(entryType.equals("start") || entryType.equals("ent"))) {

	                // throw the invalid entry type error and "devalidate" the
	                // object file
	                out.println("Error at record " + i + ":invalid entry type");
	                isValid = false;
	            }

	            // if the program name in the linking record doesn't match the
	            // given program name in the header record ...
	            if (!(nextRec.get(nextRec.size()-1).equals(pgmName))) {

	                // throw the mismatched program name error and "devalidate"
	                // the object file
	                out.println("Error at record "
	                        + i
	                        + ":program name in linking record does not match program"
	                        + " name in corresponding header record");
	                isValid = false;
	            }
	        }

	        else if (recType.equals('T')) {

	            /*
	             * if a text record is encountered, it should be between a
	             * header and an end record. thus, hasHeader should be true and
	             * hasEnded should be false
	             */

	            // if the above requirements isn't true ...
	            if (!(hasHeader && !hasEnded)) {

	                // throw the error and "devalidate" the object file
	                out.println("Error at record " + i
	                        + ":misplaced text record");
	                isValid = false;
	            }

	            // if the number of text records given in the header record is
	            // exceeded,
	            // and the error switch is not thrown yet ...
	            if (textRecs <= 0 && !(textWrong)) {
	                // turn the switch, throw the error, and "devalidate" the
	                // object file
	                textWrong = true;
	                out.println("Error at record " + i
	                        + ":number of text records given by header "
	                        + "record is exceeded.");
	                isValid = false;
	            } else {
	                // otherwise, decrement the number of linking records to
	                // find and validate the text record
	                textRecs--;
	            }


	            /*
	             * field for address in hex validate for four hex digits
	             */

	            // string for holding the text address in hex
	            String textAddress = nextRec.get(1);

	            // if the address is not in proper syntax or if the memory
	            // bounds are violated ...
	            if (!(conv.isValidHex(textAddress) && textAddress.length() == 4)) {

	                // throw the error and "devalidate" the object file
	                out.println("Error at record "
	                        + i
	                        + ":address of text record entry is not in proper syntax");
	                isValid = false;
	            }

	            /*
	             * field for debug code validate for Y or N
	             */
	            String debugCode = nextRec.get(2);

	            if (!(debugCode.equalsIgnoreCase("Y") || debugCode
	                    .equalsIgnoreCase("N"))) {
	                // throw the error and "devalidate" the object file
	                out.println("Error at record " + i + ":invalid debug code.");
	                isValid = false;
	            }

	            /*
	             * field for data word validate for eight hex digits and within
	             * mem bounds
	             */
	            String dataWord = nextRec.get(3);

	            // if the address is not in proper syntax or if the memory
	            // bounds are violated ...
	            if (!(conv.isValidHex(dataWord) && dataWord.length() == 8)) {

	                // throw the error and "devalidate" the object file
	                out.println("Error at record " + i
	                        + ":word of data field is not in proper syntax");
	                isValid = false;
	            } else if (!(Integer.parseInt(conv.hexToDec(dataWord)) <= 65535)) {

	                // throw the error and "devalidate" the object file
	                out.println("Error at record " + i
	                        + ":word of data field is outside memory bounds");
	                isValid = false;
	            }

	            /*
	             * field for number of adj's validate for integer between 0 and
	             * 4 inclusive
	             * 
	             * sub-iteration for loop going from i=0 to n
	             * 
	             * if A, then nothing and nothing
	             * 
	             * if R or E, then check for + or -, then for possible label
	             * 
	             * else adj operand is invalid
	             */

	            // integer value for the number of adjustments that need to be
	            // made
	            int adjNum = Integer.parseInt(nextRec.get(4));

	            // if the number of adjustments is not within allowed bounds ...
	            if (!(0 <= adjNum && adjNum <= 4)) {
	                // throw the adjustments out of bounds error and
	                // "devalidate" the object file
	                out.println("Error at record " + i
	                        + ":number of adjustments is outside bounds -->"
	                        + "integer n where 0<=n<=4");
	                isValid = false;
	            }

	            // integer to help with the checking for all proper syntax and
	            // format of files
	            int aug = 5;

	            // sub-iteration to check all adjustments
	            for (int k = 0; k < adjNum; k++) {
	                // String for the type of adjustment
	                String adjType = nextRec.get(aug);

	                // check for the type of adjustment
	                if (adjType.equalsIgnoreCase("A")) {
	                    k = adjNum;
	                } else if (adjType.equalsIgnoreCase("R")) {
	                    if (!(nextRec.get(aug + 1).equals("+") || nextRec.get(
	                            aug + 1).equals("-"))) {

	                        // throw the invalid augmentation operand error and
	                        // "devalidate" the object file
	                        out.println("Error at record " + i
	                                + ":invalid augmentation operand");
	                        isValid = false;
	                    }
	                } else if (adjType.equalsIgnoreCase("E")) {
	                    if (!(nextRec.get(aug + 1).equals("+") || nextRec.get(
	                            aug + 1).equals("-"))) {

	                        // throw the invalid augmentation operand error and
	                        // "devalidate" the object file
	                        out.println("Error at record " + i
	                                + ":invalid augmentation operand");
	                        isValid = false;
	                    }

	                } else {

	                    // throw the invalid adjustment type error and
	                    // "devalidate" the object file
	                    out.println("Error at record " + i
	                            + ":invalid adjustment type");
	                    isValid = false;
	                }

	                // aug will be increased to perform the format check of the
	                // next adjustment
	                aug = aug + 3;
	            }

	            String prog = nextRec.get(nextRec.size() - 1);
	            // if the program name in the end record doesn't match the given
	            // program name in the header record ...
	            if (!((nextRec.get(nextRec.size() - 1)).equals(pgmName))) {

	                // throw the mismatched program name error and "devalidate"
	                // the object file
	                out.println("Error at record "
	                        + i
	                        + ":program name in text record does not match program"
	                        + " name in corresponding header record");
	                isValid = false;
	            }

	            if (!isValid) {
	                // replace invalid text record with a nop and set isValid to
	                // true for next record
	                isValid = true;

	                ArrayList<String> newRec = new ArrayList<String>(0);
	                // nop for invalid text record
	                newRec.add("T");
	                newRec.add(textAddress);
	                newRec.add(debugCode);
	                newRec.add("00800000");
	                newRec.add("0");
	                newRec.add(prog);

	                objectArray.set(i, newRec);

	            }
	        }

	        else if (recType.equals('E')) {


	            /*
	             * if an end record is encountered, it should be after a header
	             * record. thus, hasHeader should be true and hasEnded should be
	             * false at this point
	             */

	            // if the above requirements isn't true ...
	            if (!(hasHeader && !hasEnded)) {

	                // throw the error and "devalidate" the object file
	                out.println("Error at record " + i
	                        + ":misplaced end record or missing header record");
	                isValid = false;
	            }

	            // if the above requirements are true ...
	            else {

	                // change the boolean values to reflect what should be the
	                // end of the object file
	                hasHeader = false;
	                hasEnded = true;
	            }
	            // if the hex field for total number of records isn't in proper
	            // hex ...
	            if (!(conv.isValidHex(nextRec.get(1)) && nextRec.get(1)
	                    .length() == 4)) {

	                // throw the error and "devalidate" the object file
	                out.println("Error at record "
	                        + i
	                        + ": \"total number of records\" field is not in proper syntax");
	                isValid = false;
	            }
	            else
	            {
	            // create and store value for total number of records for
	            // validation
	            int recNum = Integer.parseInt(converter.hexToDec((nextRec.get(1))));
	            
	            // of the total number of records given by the end record is not
	            // equal to
	            // the literal number of records in the entire object file ...
	            if (!(objectArray.size() == recNum)) {
	                // throw the error and "devalidate" the object file
	                out.println("Error at record "
	                        + i
	                        + ":total number of records given by the end record is "
	                        + "not equal to the total number of records in the object file!");
	                isValid = false;
	            }
	            }
	            // if the program name in the end record doesn't match the given
	            // program name in the header record ...
	            if (!(nextRec.get(2).equals(pgmName))) {

	                // throw the mismatched program name error and "devalidate"
	                // the object file
	                out.println("Error at record "
	                        + i
	                        + ":program name in end record does not match program"
	                        + " name in corresponding header record");
	                isValid = false;
	            }

	        }

	        else if (recType.equals('H')) {

	            // boolean value to check first line to see if it is a header record, as
	            // it should be;
	            // after first check, acts as a swtich telling the method whether or not
	            // a program has begun
	            hasHeader = true;

	            // boolean acting as a switch, telling the method whether or not a
	            // program has ended
	            hasEnded = false;

	            // throw an error for duplicate header record
	            out.println("Error at record " + i
	                    + ": another header record detected in object file.");
	            isValid = false;

	            // the program name to be stored; should be consistent
	            // throughout the object file
	            // [linker-adjusted loading address to use for overall linking
	            // file address]
	            String linkLoadAddrs = nextRec.get(3);

	            // [execution start address for program]
	            String execStartAddrs = nextRec.get(8);

	            // [program length]
	            String pgmLens = nextRec.get(2);

	            // first things first, setting the switches correctly. This
	            // object file has a header record
	            // and has not run into an end record yet.
	            hasHeader = true;
	            hasEnded = false;

	            // save the name of the program
	            pgmName = nextRec.get(1);

	            // check module name in second field with program name in 12th
	            // field
	            if (!(pgmName.equals(nextRec.get(12)))) {
	                out.println("Error at record "
	                        + i
	                        + ": module name and program name fields do not match");
	                isValid = false;
	            }

	            // check validity of each hex field, then verify the length

	            // if the program length is not in valid hex OR is too long ...
	            if (!(conv.isValidHex(pgmLens) && pgmLens.length() <= 4)) {

	                // throw the error and declare invalidity
	                out.println("Error at record " + i
	                        + ": program length field is not in valid syntax: "
	                        + " \"hhhh\" where h is a valid hexadecimal number");
	                isValid = false;
	            }

	            // if the assembler assigned program load address is not in
	            // valid hex OR is too long ...
	            if (!(conv.isValidHex(linkLoadAddrs) && linkLoadAddrs.length() <= 4)) {

	                // throw the error and declare invalidity
	                out.println("Error at record "
	                        + i
	                        + ": assembler assigned program load address"
	                        + " field is not in valid syntax: \"hhhh\" where h is a valid hexadecimal number");
	                isValid = false;
	            }

	            // if the execution start address is not in valid hex OR is too
	            // long ...
	            if (!(conv.isValidHex(execStartAddrs) && execStartAddrs
	                    .length() <= 4)) {

	                // throw the error and declare invalidity
	                out.println("Error at record "
	                        + i
	                        + ": execution start address field is not in"
	                        + " valid syntax: \"hhhh\" where h is a valid hexadecimal number");
	                isValid = false;
	            }

	            // if the number of text records is not in valid hex OR is too
	            // long ...
	            if (!(conv.isValidHex(nextRec.get(7)) && nextRec.get(7)
	                    .length() <= 4)) {

	                // throw the error and declare invalidity
	                out.println("Error at record "
	                        + i
	                        + ": number of text records field is not in"
	                        + " valid syntax: \"hhhh\" where h is a valid hexadecimal number");
	                isValid = false;
	            } else {

	                // save the value for the number of text records expected
	                textRecs = Integer.parseInt(conv.hexToDec(nextRec.get(7)));
	            }

	            // if the number of linking records is not in valid hex OR is
	            // too long ...
	            if (!(conv.isValidHex(nextRec.get(6)) && nextRec.get(6)
	                    .length() <= 4)) {

	                // throw the error and declare invalidity
	                out.println("Error at record "
	                        + i
	                        + ": number of linking records field is not in"
	                        + " valid syntax: \"hhhh\" where h is a valid hexadecimal number");
	                isValid = false;
	            } else {

	                // save the value for the number of linking records expected
	                linkRecs = Integer.parseInt(nextRec.get(6));
	            }

	            // --------check the date field for proper syntax and
	            // values--------- //

	            // string for the year portion of the date field
	            String years = nextRec.get(4).substring(0, 4);

	            // if the year is too far in the future ...
	            if (Integer.parseInt(years) > 2010) {

	                // give a [funny ;} ] warning
	                out.println("Warning: given year is too far in the future!"
	                        + "\nRecommended action: give Marty McFly his time machine back.");
	            }

	            // if the year is too far in the past ...
	            else if (Integer.parseInt(years) < 2010) {

	                // give a [funny ;} ] warning
	                out.println("Warning: given year is too far in the past!"
	                        + "\nRecommended action: borrow Marty McFly's time machine and go back to the future.");
	            }

	            // string for the day portion of the date field
	            String days = nextRec.get(4).substring(5);

	            // check bounds for the day; anything else that should be
	            // checked ???
	            if (!(Integer.parseInt(days) >= 0 && Integer.parseInt(days) <= 365)) {

	                // give a [funny ;} ] warning
	                out.println("Warning: given day is outside reasonable bounds for the day."
	                        + "\nRecommended action: check syntax, or if in a leap year, please try again tomorrow! =)");
	            }

	            // check the time field for proper syntax
	            String times = nextRec.get(5);

	            // if the time markings aren't in proper syntax and range ...
	            if (!(times.length() == 8
	                    && Integer.parseInt(times.substring(0, 2)) < 24
	                    && Integer.parseInt(times.substring(3, 5)) < 60 && Integer
	                    .parseInt(times.substring(6)) < 60)) {

	                // give a [funny ;} ] warning
	                out.println("Warning: given time is not in proper syntax or range: hh:mm:ss where hh == hours, mm == minutes, and ss == seconds."
	                        + "\nRecommended action: check syntax, or buy your assembler a watch! =P");
	            }

	        } else {


	            out.println("Error at record "
	                    + i
	                    + ":invalid record ");
	        }
	        System.out.println(">>>>>>>>>>>>>>>>Record validation complete");
	    }

	    // if the validation has not aborted by this point, the object file is
	    // good enough to link
	    // therefore the method will return true if it makes it to this point
	    
	    System.out.println("Object file validation complete");
	    return isValid;
	}

	/**This method takes an ObjectIn object file and output the correct parts to the linker
	 * file.
	 * 
	 * @param linkerFile = the file to output to
	 * @param symbolTable = the object containing the linker's global symbol table
	 * @param objectfile = the ObjectIn containing the object file to be converted
	 * @returns int record count
	 * @throws IOException 
	 */
	static int buildLinkerFile(PrintWriter out, LinkerTableInterface symbolTable ) throws IOException
	{
		
		//variables for main scope		
		int recordCount = 0;
		int startLocation = 0; //holds start location of program
		Integer loadAddress = 0; //holds the current address to load at
		int diffOfLoc = 0; //holds the difference between assembler assigned start and linker assigned
		ConverterInterface converter = new Converter();
		

		
		//set startLocation from current objects header 
		startLocation += Integer.parseInt(converter.hexToDec(objectArrays.get(0).get(0).get(3)));
		
		//increment through object arrays
		for (int i = 0; i < objectArrays.size(); i++)
		{
			
			//set diffOfLoc for program 
			//diffOfLoc equals startLocation minus prog load address
			if (objectArrays.get(i).size() > 0)
			{
				diffOfLoc = startLocation
						- Integer.parseInt(converter.hexToDec(objectArrays
								.get(i).get(0).get(3)));
			}
			
			
			//increment through object array and convert text records to linker records
			for (int inc = 0; inc < objectArrays.get(i).size(); inc++)
			{

				if (objectArrays.get(i).get(inc).get(0).equalsIgnoreCase("T"))
				{

					//store hex code as string
					String code = objectArrays.get(i).get(inc).get(3);
					Integer intCode = Integer.parseInt(converter.hexToDec(code));

					// mod code if it is R, or E type
					int j = 5;

					while (objectArrays.get(i).get(inc).get(j)
							.equalsIgnoreCase("R")
							|| objectArrays.get(i).get(inc).get(5)
									.equalsIgnoreCase("E")) {

						if (objectArrays.get(i).get(inc).get(5)
								.equalsIgnoreCase("R")) {

							// if R- type subtract startLocation from
							// address section of code
							if (objectArrays.get(i).get(inc).get(j + 1)
									.equalsIgnoreCase("-")) {
								intCode -= startLocation;
							}

							// if R+ type add startLocation to address
							// section of code
							else if (objectArrays.get(i).get(inc).get(j + 1)
									.equalsIgnoreCase("-")) {
								intCode += startLocation;
							}

							// increment j by 2
							j += 2;

						}

						// if E type
						if (objectArrays.get(i).get(inc).get(5)
								.equalsIgnoreCase("E")) {

							
							//check if reference variable is defined
							if (symbolTable.isDefined(objectArrays.get(i)
									.get(inc).get(j + 2)))
							{
								
								// if - subtract address of label from intCode
								if (objectArrays.get(i).get(inc).get(j + 1)
										.equalsIgnoreCase("-")) {

									intCode -= symbolTable
											.getLocation(objectArrays.get(j)
													.get(inc).get(j + 2));
								}

								// if + subtract address of label from intCode
								else if (objectArrays.get(i).get(inc)
										.get(j + 1).equalsIgnoreCase("+")) {
									intCode += symbolTable
											.getLocation(objectArrays.get(i)
													.get(inc).get(j + 2));
								}

								// increment j by 3 to get to next possible 'E'
								j += 3;
							}

							//if ext var is not defined, print error to console
							else {
								System.out.println("Linking error: " + objectArrays.get(i)
										.get(inc).get(j + 2) + "does not have an entry point.");
							}

						}

					}
					
					//convert code back to hex
					code = converter.decimalToHex(intCode.toString());
					
					//fill missing leading zeros in code
					while (code.length() < 8)
					{
						code = "0" + code;
					}
					
					
					//get loadAddress
					loadAddress = Integer.parseInt(converter.hexToDec(objectArrays.get(i).get(inc).get(1)));
					
					//modify loadAddress
					loadAddress += diffOfLoc;
					
					//format load address
					String loadAddr = converter.decimalToHex(loadAddress.toString());
					while(loadAddr.length() < 4)
					{
						loadAddr = "0" + loadAddr;
					}
					
					//add to linkerfile here
					out.println("LT|" + objectArrays.get(i).get(0).get(2) + "|" + loadAddr + 
							"|" + code + "|" + objectArrays.get(i).get(0).get(1));
					
					recordCount++;
					

				}
				

			}
			
			
			//set startLocation to end of this program
			if (objectArrays.get(i).size() > 0)
			{
				startLocation += Integer.parseInt(converter.hexToDec(objectArrays.get(i).get(0).get(2)));
			}
			
			
		}
		
		//return total number of records
		return recordCount;
		
		
		
		
	}

	/**This method takes an ObjectIn and and gets the linker records and inputs the needed
	 * data into the LinkerTable(also know as the global symbol table).
	 * 
	 * @param symbolTable
	 * @param objectFile
	 * @returns boolean true if successful, false if length exceeds memory
	 */
	static boolean populateSymbolTable(LinkerTableInterface symbolTable)
	{
		
		//create converter class
		ConverterInterface converter = new Converter();
		
		//storage variables
		int startLocation = 0; //this will hold the beginning address of the program
		
		//this variable holds the difference between the prog's mem load address and the linker's
		int diffOfLoc = 0; 
		
		
		
		
		
		
		//populate from objects
		
		for (int i = 0; i < objectArrays.size(); i++) {
			//get start location
			startLocation = Integer.parseInt(converter.hexToDec(objectArrays.get(i)
					.get(0).get(3)));
			//check startLocation does not exceed length
			if (startLocation >= 65536) {
				//print error message and return false
				System.out.println("Length of linked programs exceeds memory.");
				return false;
			}
			if (objectArrays.get(i).size() > 0) {
				//populate from object 
				//set diffOfLoc for program 
				//diffOfLoc equals startLocation minus prog load address
				diffOfLoc = startLocation
						- Integer.parseInt(converter.hexToDec(objectArrays.get(i).get(
								0).get(3)));
				//increment through object1Array and find the linker files
				for (int inc = 0; inc < objectArrays.get(i).size(); inc++) {

					//check record type. If 'L' continue
					if (objectArrays.get(i).get(inc).get(0).equals("L")) {

						//get name
						String name = objectArrays.get(i).get(inc).get(1);

						//get type
						String type = objectArrays.get(i).get(inc).get(3);

						//get location
						int location = Integer.parseInt(converter
								.hexToDec(objectArrays.get(i).get(inc).get(2)));

						//modify location due to load address differences
						location = location + diffOfLoc;

						//add to symbol table
						symbolTable.add(name, type, location);
					}

				}
				//modify start location to the beginning of the second program
				startLocation = startLocation + Integer.parseInt(converter
						.hexToDec(objectArrays.get(i).get(0).get(2)));
				
				//add program length to module length in GST
				symbolTable.increaseLength(Integer.parseInt(converter
						.hexToDec(objectArrays.get(i).get(0).get(2))));
			}
		}
		
		
		
		//if successful return true
		return true;
		
		
		
		
	}
	
	
	
	
	
}
