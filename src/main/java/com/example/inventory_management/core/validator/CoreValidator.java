package com.example.inventory_management.core.validator;

import com.example.inventory_management.core.controller.CoreController;

public class CoreValidator {
  public static boolean isEmail(CoreController<?> controller, String value, String itemName, boolean isRequired) {
    if (isRequired) {
      if (value.isBlank()) {
        controller.addErrorMessage(itemName, "must be required ");

        return false;
      }
    }

    if (value != null) {
      if (!value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
        controller.addErrorMessage(itemName, "Invalidate");

        return false;
      }
    }

    return true;
  }
}
