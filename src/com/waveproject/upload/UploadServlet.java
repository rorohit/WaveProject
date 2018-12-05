package com.waveproject.upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIRECTORY = "upload";
	private static final int THRESHOLD_SIZE     = 1024 * 1024 * 3;  // 3MB
	private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

    /**
     * handles file upload via HTTP POST method
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // checks if the request actually contains upload file
        if (!ServletFileUpload.isMultipartContent(request)) {
            PrintWriter writer = response.getWriter();
            writer.println("Request does not contain upload data");
            writer.flush();
            return;
        }
         
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
         
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
         
        // constructs the directory path to store upload file
        String uploadPath = getServletContext().getRealPath("")
            + File.separator + UPLOAD_DIRECTORY;
        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
         
        try {
            // parses the request's content to extract file data
            List formItems = upload.parseRequest(request);
            Iterator iter = formItems.iterator();
            String fileName = null;
            String filePath = null;
            // iterates over form's fields
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                // processes only fields that are not form fields
                if (!item.isFormField()) {
                    fileName = new File(item.getName()).getName();
                    filePath = uploadPath + File.separator + fileName;
                    File storeFile = new File(filePath);
                     
                    // saves the file on disk
                    item.write(storeFile);
                }
            }
            File f = new File(filePath);
            FileInputStream is = new FileInputStream(f);
            BodyContentHandler contenthandler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
            //System.out.println(metadata.get(Metadata.CONTENT_TYPE));
            Parser parser = new AutoDetectParser();
            // OOXMLParser parser = new OOXMLParser();
            parser.parse(is, contenthandler, metadata);
/*            System.out.println("Mime: " + metadata.get(Metadata.CONTENT_TYPE));
            System.out.println("Title: " + metadata.get(Metadata.TITLE));
            System.out.println("Author: " + metadata.get(Metadata.AUTHOR));
            System.out.println("content: " + contenthandler.toString());*/
            Path file1 = Paths.get(filePath);
            final Tika tika = new Tika();
            String fileContentDetect = tika.detect(file1.toFile());
            String message = null;
            if(!fileContentDetect.equals(MimeTypes.OCTET_STREAM)) {
            	//System.out.println("Whaaat lool-: "+fileContentDetect);
            	if(fileContentDetect.toLowerCase().contains("csv")||fileContentDetect.toLowerCase().contains("text/html")) {
            		//File Type confirmed as CSV
            		message = ReadDataCSV.readAllDataAtOnce(filePath);
            	}
            }
            //String type =  file1.probeContentType(filePath);
            FileReader fr = new FileReader(filePath);
            
            BufferedReader br = new BufferedReader(fr);
            if(message == null)
            	request.setAttribute("message", "Upload has been done successfully!");
            else 
            	request.setAttribute("message", message);
        } catch (Exception ex) {
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }
        getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
    }
}
