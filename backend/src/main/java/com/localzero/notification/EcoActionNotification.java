package com.localzero.notification;

import com.localzero.model.*;
import com.localzero.model.enums.NotificationType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class EcoActionNotification extends BaseNotification {
    public EcoActionNotification(Map<String, Object> data) { super(data); }

    @Override
    protected void validateData() {
        getRequiredData("initiative");   // Initiative
        getRequiredData("performedBy");  // User
        getRequiredData("actionName");   // String
        getRequiredData("carbonKg");     // BigDecimal
        getRequiredData("date");         // LocalDate
    }

    @Override
    public Notification create() {
        Initiative initiative = getRequiredData("initiative");
        User performedBy      = getRequiredData("performedBy");
        String actionName     = getRequiredData("actionName");
        BigDecimal carbonKg   = getRequiredData("carbonKg");
        LocalDate date        = getRequiredData("date");

        Notification n = new Notification();
        n.setType(NotificationType.NEW_ECO_ACTION);
        n.setTitle("New eco-action in " + initiative.getTitle());
        n.setMessage(performedBy.getName() + " logged \"" + actionName + "\" (" + carbonKg + " kg CO₂) on " + date);
        n.setCreatedBy(performedBy);
        return n;
    }
}
