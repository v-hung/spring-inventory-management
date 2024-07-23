package com.example.inventory_management.core.utils;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

import com.example.inventory_management.core.controller.CoreController;

@Component
public class CoreUtils {
  public static String getFunctionId(CoreController<?> controller) {

		String name = controller.getClass().getPackage().getName();
		return name.substring(name.lastIndexOf(".") + 1);

	}

  public static String getScreenId(CoreController<?> controller) {

		String name = controller.getClass().getName();
		name = name.substring(name.lastIndexOf(".") + 1);
		name = name.replace("Controller", "").toLowerCase();

		if (name.indexOf("$") >= 0) {

			name = name.substring(0, name.indexOf("$"));

		}

		return name;

	}

  public static Method getMethod(Class<?> cls, String name) {

		try {

			Method[] methods = cls.getMethods();

			for (Method method : methods) {

				if (name.equals(method.getName())) {

					return method;

				}

			}

			methods = cls.getDeclaredMethods();

			for (Method method : methods) {

				if (name.equals(method.getName())) {

					return method;

				}

			}

			Class<?> superClass = cls.getSuperclass();

			if (superClass == Object.class) {

				throw new RuntimeException("Failed to get method " + name);

			}

			return getMethod(superClass, name);

		} catch (SecurityException e) {

			throw new RuntimeException("Failed to get method " + name);

		}

	}

  public static Object createForm(CoreController<?> actionController) {

		try {

			String clsName = actionController.getClass().getName();
			String formName = clsName.replace("Controller", "Form");

			if (formName.indexOf("$") >= 0) {

				formName = formName.substring(0, formName.indexOf("$"));

			}

			Class<?> cls = Class.forName(formName);

			return cls.getDeclaredConstructor().newInstance();

		} catch (SecurityException e) {

			throw new RuntimeException("CoreUtils-001 Formクラスの生成に失敗しました。", e);

		} catch (IllegalArgumentException e) {

			throw new RuntimeException("CoreUtils-002 Formクラスの生成に失敗しました。", e);

		} catch (ReflectiveOperationException e) {

			throw new RuntimeException("CoreUtils-003 Formクラスの生成に失敗しました。", e);

		}

	}

  public static String getModelAttributeName(CoreController<?> actionController) {

		String clsName = actionController.getClass().getSimpleName();
		String name = clsName.replace("Action", "Form");

		if (name.indexOf("$") >= 0) {

			name = name.substring(0, name.indexOf("$"));

		}

		name = name.substring(0, 1).toLowerCase() + name.substring(1);
		return name;

	}

  public static Object invokeMethod(Object obj, String name, Object ...args) {
    try {

      Class<?>[] parameterTypes = new Class<?>[args.length];
      for (int i = 0; i < args.length; i++) {
        parameterTypes[i] = args[i].getClass();
      }

      // Lấy method cần gọi
      Method method = obj.getClass().getDeclaredMethod(name, parameterTypes);
      method.setAccessible(true);

      // Gọi method và trả về kết quả
      return method.invoke(obj, args);

    } catch (Exception e) {
      throw new RuntimeException("Failed to invoke method");
    }
	}

  public static String getEventId(StringBuffer uri) {

		String eventId = uri.substring(uri.lastIndexOf("/") + 1);

		if (eventId.indexOf(";") > 0) {

			eventId = eventId.substring(0, eventId.indexOf(";"));

		}

		return eventId;

	}
}
