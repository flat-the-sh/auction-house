package com.epam.auction.receiver.impl;

import com.epam.auction.command.RequestContent;
import com.epam.auction.dao.BidDAO;
import com.epam.auction.dao.impl.BidDAOImpl;
import com.epam.auction.db.DAOManager;
import com.epam.auction.entity.Bid;
import com.epam.auction.entity.Item;
import com.epam.auction.entity.ItemStatus;
import com.epam.auction.entity.User;
import com.epam.auction.exception.DAOException;
import com.epam.auction.exception.ReceiverException;
import com.epam.auction.receiver.BidReceiver;
import com.epam.auction.receiver.RequestConstant;
import com.epam.auction.util.MessageProvider;

import java.math.BigDecimal;
import java.util.Locale;

public class BidReceiverImpl implements BidReceiver {

    @Override
    public void makeBid(RequestContent requestContent) throws ReceiverException {
        User user = (User) requestContent.getSessionAttribute(RequestConstant.USER);
        if (user != null) {
            int itemId = Integer.valueOf(requestContent.getRequestParameter(RequestConstant.ITEM_ID)[0]);
            BigDecimal bidValue = new BigDecimal(requestContent.getRequestParameter(RequestConstant.BID_VALUE)[0]);

            BidDAO bidDAO = new BidDAOImpl();
            DAOManager daoManager = new DAOManager(true, bidDAO);

            daoManager.beginTransaction();
            try {
                MessageProvider messageProvider = new MessageProvider((Locale) requestContent.getSessionAttribute(RequestConstant.LOCALE));

                Bid winningBid = bidDAO.findWinning(itemId);
                if (winningBid == null || winningBid.getBidderId() != user.getId()) {
                    if (bidValue.compareTo(user.getBalance()) <= 0) {
                        Bid bid = new Bid(itemId, user.getId(), bidValue);
                        bidDAO.create(bid);
                        updateSessionItem(requestContent, bidValue);
                        requestContent.setSessionAttribute(RequestConstant.MESSAGE,
                                messageProvider.getMessage(MessageProvider.BID_MADE_SUCCESSFULLY));
                    } else {
                        requestContent.setSessionAttribute(RequestConstant.MESSAGE,
                                messageProvider.getMessage(MessageProvider.NOT_ENOUGH_MONEY));
                    }
                } else {
                    requestContent.setSessionAttribute(RequestConstant.MESSAGE,
                            messageProvider.getMessage(MessageProvider.YOUR_BID_IS_WINNING));
                }
                requestContent.setSessionAttribute(RequestConstant.WAS_SHOWN, false);

                daoManager.commit();
            } catch (DAOException e) {
                daoManager.rollback();
                throw new ReceiverException(e);
            } finally {
                daoManager.endTransaction();
            }
        }
    }

    private void updateSessionItem(RequestContent requestContent, BigDecimal bidValue) {
        Item item = (Item) requestContent.getSessionAttribute(RequestConstant.ITEM);
        item.setActualPrice(bidValue);
        if (bidValue.compareTo(item.getBlitzPrice()) >= 0) {
            item.setStatus(ItemStatus.SOLD);
        }
    }

}