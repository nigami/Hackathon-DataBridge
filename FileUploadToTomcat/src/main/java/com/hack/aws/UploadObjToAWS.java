package com.hack.aws;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class UploadObjToAWS {

    public static void uploadToAws(String filePath, String keyName) throws Exception {
        String existingBucketName = "thedatabridge2";
     //   String keyName            = "test@123";
      
        
        TransferManager tm = new TransferManager(new ProfileCredentialsProvider()); 
        
        
        System.out.println("Hello");
        // TransferManager processes all transfers asynchronously, 
        // so this call will return immediately.
       
        Upload upload = tm.upload(
        		existingBucketName, keyName, new File(filePath.replace("\\", "\\\\")));
        System.out.println("Hello2");

        try {
        	// Or you can block and wait for the upload to finish
        	upload.waitForCompletion();
        	System.out.println("Upload complete.");
        } catch (AmazonClientException amazonClientException) {
        	System.out.println("Unable to upload file, upload was aborted.");
        	amazonClientException.printStackTrace();
        }
    }
    
    
    public static long chunkSize = 800000;

    public static void split(String filename,File f,File dir) throws FileNotFoundException, IOException
    	{
    	// open the file
    	BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
    	
    	// get the file length
    	//File f = new File(filename);
    	long fileSize = f.length();
    	
    	// loop for each full chunk
    	

    	int subfile;
    	for (subfile = 0; subfile < fileSize / chunkSize; subfile++)
    		{
    		// open the output file
    		
    		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dir.getAbsolutePath() + File.separator +filename + "." + subfile));
    		
    		// write the right amount of bytes
    		for (int currentByte = 0; currentByte < chunkSize; currentByte++)
    			{
    			// load one byte from the input file and write it to the output file
    			out.write(in.read());
    			
    			}
    			
    		// close the file
    		out.close();
    		}
    	
    	// loop for the last chunk (which may be smaller than the chunk size)
    	if (fileSize != chunkSize * (subfile - 1))
    		{
    		// open the output file

    		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dir.getAbsolutePath() + File.separator +filename + "." + subfile));
    		
    		// write the rest of the file
    		int b;
    		while ((b = in.read()) != -1)
    			out.write(b);
    			
    		// close the file
    		out.close();			
    		}
    	
    	// close the file
    	in.close();
    	}
}