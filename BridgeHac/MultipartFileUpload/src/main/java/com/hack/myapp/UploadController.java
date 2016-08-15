package com.hack.myapp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

		String fileName="";

		/*if(multipartFile!=null){
			fileName = multipartFile.getOriginalFilename();
			//do whatever you want
		}*/
		upImage((multipartFile.getName()+new Timestamp(0)),multipartFile);
		return new ModelAndView("FileUploadSuccess","fileName",fileName);
	}
	
	 @Override
	 protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
		throws ServletException {

		// Convert multipart object to byte[]
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());

	 }
	
	public static String upImage(String name, MultipartFile file) {
	     System.out.println("images name----------"+name); 
	     name=name.replace(" ", "");
	     name=name.replace("-", "_");
	     name=name.replace(":", "_");
	     name=name.replace(".", "_");
	     System.out.println("images name----------"+name); 
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "images/newImg");
                
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

    }
}
