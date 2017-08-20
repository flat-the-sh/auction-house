package com.epam.auction.receiver.impl;

import com.epam.auction.constant.RequestConstant;
import com.epam.auction.content.RequestContent;
import com.epam.auction.db.DAOManager;
import com.epam.auction.dao.UserDAO;
import com.epam.auction.dao.impl.UserDAOImpl;
import com.epam.auction.entity.User;
import com.epam.auction.exception.DAOLayerException;
import com.epam.auction.exception.ReceiverLayerException;
import com.epam.auction.receiver.UserReceiver;
import com.epam.auction.util.Encoder;
import com.epam.auction.validator.UserValidator;

public class UserReceiverImpl implements UserReceiver {

    @Override
    public void signIn(RequestContent requestContent) throws ReceiverLayerException {
        User user = new User(
                requestContent.getRequestParameter(RequestConstant.USERNAME)[0],
                Encoder.encode(requestContent.getRequestParameter(RequestConstant.PASSWORD)[0]));

        UserDAO userDAO = new UserDAOImpl();

        try (DAOManager daoManager = new DAOManager(userDAO)) {
            if (userDAO.isExist(user)) {
                requestContent.setSessionAttribute(RequestConstant.USER, user);
            } else {
                requestContent.setRequestAttribute(RequestConstant.WRONG_USERNAME_PASSWORD, true);
                requestContent.setRequestAttribute(RequestConstant.OPEN_SIGN_IN, true);
            }
        } catch (DAOLayerException e) {
            throw new ReceiverLayerException(e.getMessage(), e);
        }
    }

    @Override
    public void signUp(RequestContent requestContent) throws ReceiverLayerException {
        User user = new User(
                requestContent.getRequestParameter(RequestConstant.USERNAME)[0],
                requestContent.getRequestParameter(RequestConstant.PASSWORD)[0],
                requestContent.getRequestParameter(RequestConstant.LAST_NAME)[0],
                requestContent.getRequestParameter(RequestConstant.MIDDLE_NAME)[0],
                requestContent.getRequestParameter(RequestConstant.FIRST_NAME)[0],
                requestContent.getRequestParameter(RequestConstant.PHONE_NUMBER)[0],
                requestContent.getRequestParameter(RequestConstant.EMAIL)[0]);

        UserValidator validator = new UserValidator();
        if (validator.validateSignUpParam(user)) {
            user.setPassword(Encoder.encode(user.getPassword()));

            UserDAO userDAO = new UserDAOImpl();
            DAOManager daoManager = new DAOManager(true, userDAO);

            boolean result = false;

            daoManager.beginTransaction();
            try {
                if (userDAO.isUsernameAlreadyExist(user.getUsername())) {
                    requestContent.setRequestAttribute(RequestConstant.USERNAME_ALREADY_EXIST, true);
                } else if (userDAO.isEmailAlreadyExist(user.getEmail())) {
                    requestContent.setRequestAttribute(RequestConstant.EMAIL_ALREADY_EXIST, true);
                } else {
                    result = userDAO.create(user);
                    daoManager.commit();
                }
            } catch (DAOLayerException e) {
                daoManager.rollback();
                throw new ReceiverLayerException(e.getMessage(), e);
            } finally {
                daoManager.endTransaction();
            }

            if (result) {
                requestContent.setRequestAttribute(RequestConstant.SUCCESSFUL_REGISTRATION, true);
            } else {
                requestContent.setRequestAttribute(RequestConstant.OPEN_SIGN_UP, true);
            }

        } else {
            throw new ReceiverLayerException(validator.getValidationMessage());
        }
    }

    @Override
    public void logOut(RequestContent requestContent) {
        requestContent.destroySessionAttributes();
    }

}