/**
Copyright (c) 2007, University of Pittsburgh

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.

    * Neither the name of the University of Pittsburgh nor the names
      of its contributors may be used to endorse or promote products
      derived from this software without specific prior written
      permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
**/

package pitt.search.semanticvectors;

import java.util.LinkedList;
import java.io.IOException;

/**
 * Command line utility for creating semantic vector indexes.
 */
public class BuildIndex{
    /* Change these and recompile if you want to play with them. */
    static int seedLength = 20;
    static int minFreq = 10;

    /**
     * Prints the following usage message: 
     * <code>
     * <br> BuildIndex class in package pitt.search.semanticvectors
     * <br> Usage: java pitt.search.semanticvectors.BuildIndex PATH_TO_LUCENE_INDEX
     * <br> BuildIndex creates files termvectors.bin and docvectors.bin in local directory.
     * <br> Other parameters that can be changed include vector length,
     * <br>     (number of dimensions), seed length (number of non-zero
     * <br>     entries in basic vectors), and minimum term frequency.
     * <br> To change these you need to edit BuildIndex.java or ObjectVector.java
     * <br>     and recompile.
     * </code>
     */
    public static void usage(){
	String usageMessage = "\nBuildIndex class in package pitt.search.semanticvectors"
	    + "\nUsage: java pitt.search.semanticvectors.BuildIndex PATH_TO_LUCENE_INDEX"
	    + "\nBuildIndex creates files termvectors.bin and docvectors.bin in local directory."
	    + "\nOther parameters that can be changed include vector length,"
	    + "\n    (number of dimensions), seed length (number of non-zero"
	    + "\n    entries in basic vectors), and minimum term frequency."
	    + "\nTo change these use the command line arguments "
	    + "\n  -d [number of dimensions]  and"
	    + "\n  -s [seed length]"
	    + "\n  -m [minimum term frequency";
	   
	System.out.println(usageMessage);
	System.exit(-1);
    }

    /**
     * Builds term vector and document vector stores from a Lucene index.
     * @param args See usage();
     */

    public static void main( String[] args ){
	 	boolean wellformed = false;
    if (args.length == 1) wellformed = true;
    else if (args.length % 2 ==0) wellformed = false;
    else 
    {for (int x = 0; x < args.length-1; x+=2)
    { String pa = args[x];
   
   
      String ar = args[x+1];

      if (pa.equalsIgnoreCase("-d")) 
      {try {ObjectVector.vecLength = Integer.parseInt(ar);	
            wellformed = true;
      } catch (NumberFormatException e) {System.out.println(ar + " is not a number"); usage();}}
      else if (pa.equalsIgnoreCase("-s")) 
      {try { seedLength = Integer.parseInt(ar);  
             if (seedLength > ObjectVector.vecLength) {System.out.println("Seed length cannot be greater than vector length"); usage();}
             else wellformed = true;
      } catch (NumberFormatException e) {System.out.println(ar + " is not a number"); usage();}}
      else if (pa.equalsIgnoreCase("-m")) 
      {try { minFreq = Integer.parseInt(ar);  
             if (minFreq < 0) {System.out.println("Minimum frequency cannot be less than zero"); usage();}
             else wellformed = true;
      } catch (NumberFormatException e) {System.out.println(ar + " is not a number"); usage();}}
  
    }
    }
    
    if (!wellformed){
	    usage();
	}

	String luceneIndex = args[args.length-1];
	String termFile = "termvectors.bin";
	String docFile = "docvectors.bin";
	System.out.println("seedLength = "+seedLength);
	System.out.println("Vector length = "+ObjectVector.vecLength);
	System.out.println("Minimum frequency = "+minFreq);
	try{
	    TermVectorsFromLucene vecStore = 
		new TermVectorsFromLucene(luceneIndex, seedLength, minFreq);
	    VectorStoreWriter vecWriter = new VectorStoreWriter();
	    System.err.println("Writing term vectors to " + termFile);
	    vecWriter.WriteVectors(termFile, vecStore);
	    DocVectors docVectors = new DocVectors(vecStore);
	    System.err.println("Writing doc vectors to " + docFile);
	    vecWriter.WriteVectors("docvectors.bin", docVectors);
	}
	catch( IOException e ){
	    e.printStackTrace();
	}
    }}
