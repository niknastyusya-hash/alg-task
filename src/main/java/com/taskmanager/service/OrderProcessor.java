package com.taskmanager.service;

import com.taskmanager.model.Order;
import com.taskmanager.storage.OrderStorage;
import com.taskmanager.validation.OrderValidator;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class OrderProcessor {

    private static final Logger logger = Logger.getLogger(OrderProcessor.class.getName());
    private final OrderStorage storage;
    private final OrderValidator validator;

    public OrderProcessor(OrderStorage storage) {
        this.storage = storage;
        this.validator = new OrderValidator();
    }

    public boolean processOrder(Order order, String processorName) {
        if (order == null) {
            return false;
        }

        try {
            if (!validator.validate(order)) {
                logger.warning(String.format("[%s] Order %s validation failed",
                        processorName, order.getId().substring(0, 8)));
                order.setStatus(Order.Status.FAILED);
                return false;
            }

            order.setStatus(Order.Status.PROCESSING);

            int processingTime = order.isUrgent() ? 500 : 1000;
            Thread.sleep(processingTime);

            order.setStatus(Order.Status.COMPLETED);
            order.setProcessedAt(LocalDateTime.now());
            order.setProcessedBy(processorName);

            storage.storeProcessedOrder(order);

            logger.info(String.format("[%s] Processed order: %s | %s | Time: %dms",
                    processorName, order.getId().substring(0, 8),
                    order.getProduct(), processingTime));

            return true;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            order.setStatus(Order.Status.FAILED);
            logger.warning(String.format("[%s] Interrupted while processing order %s",
                    processorName, order.getId()));
            return false;
        } catch (Exception e) {
            order.setStatus(Order.Status.FAILED);
            logger.severe(String.format("[%s] Error processing order: %s",
                    processorName, e.getMessage()));
            return false;
        }
    }
}