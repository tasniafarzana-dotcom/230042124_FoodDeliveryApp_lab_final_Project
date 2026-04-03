package org.fooddelivery.api;

import java.util.List;

import org.fooddelivery.model.RiderAssignment;
import org.fooddelivery.service.IRiderAssignmentService;
import org.fooddelivery.service.RiderAssignmentService;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(targetNamespace = "http://api.fooddelivery.org/")
public class RiderTaskService {

    private final IRiderAssignmentService assignmentService;

    public RiderTaskService() {
        this.assignmentService = new RiderAssignmentService();
    }

    @WebMethod
    public List<RiderAssignment> getPendingTasksForRider(
            @WebParam(name = "riderId") String riderId) {
        return assignmentService.getPendingTasksForRider(riderId);
    }

    @WebMethod
    public List<RiderAssignment> getAcceptedTasksForRider(
            @WebParam(name = "riderId") String riderId) {
        return assignmentService.getAcceptedTasksForRider(riderId);
    }

    @WebMethod
    public void respondToAssignment(
            @WebParam(name = "assignmentId") String assignmentId,
            @WebParam(name = "response") String response) {
        assignmentService.respondToAssignment(assignmentId, response);
    }
}