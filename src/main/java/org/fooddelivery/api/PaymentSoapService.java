package org.fooddelivery.api;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.fooddelivery.model.Payment;
import org.fooddelivery.repository.IOrderRepository;
import org.fooddelivery.repository.OrderRepository;
import org.fooddelivery.service.IPaymentService;
import org.fooddelivery.service.PaymentService;

@WebService(targetNamespace = "http://api.fooddelivery.org/")
public class PaymentSoapService {
    private final IPaymentService paymentService;
    private final IOrderRepository orderRepository;

    public PaymentSoapService() {
        this.paymentService = new PaymentService();
        this.orderRepository = new OrderRepository();
    }

    @WebMethod
    public Payment payOrder(
            @WebParam(name = "orderId") String orderId,
            @WebParam(name = "method") String method,
            @WebParam(name = "amount") double amount
    ) {
        double finalAmount = amount;
        if (finalAmount <= 0) {
            finalAmount = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId))
                    .getTotalPrice();
        }
        return paymentService.processPayment(orderId, method, finalAmount);
    }

    @WebMethod
    public Payment getPaymentByOrder(@WebParam(name = "orderId") String orderId) {
        return paymentService.getPaymentByOrder(orderId);
    }
}