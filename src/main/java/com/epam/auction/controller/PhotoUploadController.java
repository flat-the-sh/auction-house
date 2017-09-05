package com.epam.auction.controller;

import com.epam.auction.command.AbstractCommand;
import com.epam.auction.command.PageGuide;
import com.epam.auction.command.TransferMethod;
import com.epam.auction.receiver.RequestConstant;
import com.epam.auction.command.CommandFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides service for uploading photos to server.
 */
@WebServlet(name = "PhotoUploadController", urlPatterns = {"/upload"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 5,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 20)
public class PhotoUploadController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Collects files from request into {@link RequestContent#files}.
     *
     * @param request  request
     * @param response response
     * @throws ServletException if servlet exception occurred
     * @throws IOException      if IO exception occurred
     * @see MainController#processRequest(HttpServletRequest, HttpServletResponse)
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestContent requestContent = new RequestContent();

        requestContent.extractValues(request);

        CommandFactory commandFactory = new CommandFactory();
        AbstractCommand command = commandFactory.initCommand(requestContent);

        List<InputStream> files = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getSubmittedFileName() != null && part.getSize() > 0) {
                files.add(part.getInputStream());
            }
        }
        if (!files.isEmpty()) {
            requestContent.setFiles(files);
        }

        PageGuide pageGuide = command.execute(requestContent);

        requestContent.setSessionAttribute(RequestConstant.CURRENT_PAGE, pageGuide.getPageAddress());

        requestContent.insertAttributes(request);

        if (TransferMethod.FORWARD.equals(pageGuide.getTransferMethod())) {
            request.getRequestDispatcher(pageGuide.getPageAddress()).forward(request, response);
        } else {
            response.sendRedirect(pageGuide.getPageAddress());
        }
    }

}