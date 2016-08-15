package com.hack.myapp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.hack.aws.GeneratePreSignedUrl;
import com.hack.aws.UploadObjToAWS;
import com.hack.aws.WriteToFile;
import com.hack.pojo.FileUpload;

@SuppressWarnings("deprecation")
@Controller
public class UploadController extends SimpleFormController {
	@SuppressWarnings("deprecation")
	public UploadController(){
		setCommandClass(FileUpload.class);
		setCommandName("fileDetails");
	}
	
	@RequestMapping(value="saveNewRoom" , method=RequestMethod.POST)
	public ModelAndView uploadImage(HttpServletRequest req,@ModelAttribute("fileDetails") FileUpload fileDetails,
            BindingResult result){
		
		    
		FileUpload file = (FileUpload)fileDetails;


		
		MultipartFile multipartFile = file.getFile();
		File convFile = new File( multipartFile.getOriginalFilename());
		
		try {
			multipartFile.transferTo(convFile);
			
			//String filePath =convFile.getAbsolutePath();
			//filePath.replace("\\", "\\\\");
			
			
			//String fileName=convFile.getName()+ new Timestamp(0);
			//C://Users//inigam//Pictures//dataBridge//Presentation
			File folder = new File("C://Users//inigam//Pictures//dataBridge//image//");
			File[] listOfFiles = folder.listFiles();

			    for (int i = 0; i < listOfFiles.length; i++) {
			      if (listOfFiles[i].isFile()) {
			        System.out.println("File " + listOfFiles[i].getAbsolutePath());

					String filePath =listOfFiles[i].getAbsolutePath();
					filePath.replace("\\", "\\\\");
					
					
					String fileName=listOfFiles[i].getName()+ new Timestamp(0);
					
			        UploadObjToAWS.uploadToAws(filePath,fileName);
			        URL preSignedUri=GeneratePreSignedUrl.getPreSignedUrl(fileName);
			        
			        WriteToFile.dataToPrint(String.valueOf(preSignedUri));
			      } else if (listOfFiles[i].isDirectory()) {
			        System.out.println("Directory " + listOfFiles[i].getName());
			      }
			    }
			
			return new ModelAndView("home" , "status","Successfully uploaded!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ModelAndView("home" , "status","Uploaded failed!");
		}
		
	}
	
	 @Override
	 protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
		throws ServletException {

		// Convert multipart object to byte[]
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());

	 }
	
	 /*public static String upImage(String name, MultipartFile file) {
	       
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "images");
                
             //   File dir = new File(path + File.separator + "images");
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                return "You successfully uploaded file=" + name;
            } catch (Exception e) {
                
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name
                    + " because the file was empty.";
        }

    }*/
	 
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
