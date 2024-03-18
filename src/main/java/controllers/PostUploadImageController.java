package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import services.S3Service;
import utils.annotations.Authorization;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@WebServlet(name = "PostUploadImageController", urlPatterns = "/product/upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100  // 100 MB
)
@Authorization(role = "", isPublic = true)
public class PostUploadImageController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/upload.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        String fileExt = fileName.split("\\.")[1];
        String contentType = filePart.getContentType();

        if(contentType == null || (!contentType.matches("image/jp(e)?g") && !contentType.equals("image/png"))){
            System.out.println("Wrong type");
        }

        InputStream inputStream = filePart.getInputStream();

        UUID uuid = UUID.randomUUID();
        String key = "images/product/" + uuid.toString() + "." + fileExt;
        S3Service.upload(key, inputStream);

        System.out.println(S3Service.getUrl(key));
    }
}
