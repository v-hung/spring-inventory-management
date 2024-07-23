package com.example.inventory_management.controller.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.inventory_management.core.annotation.NoAuthorityRequiredEvent;
import com.example.inventory_management.core.controller.CoreController;
import com.example.inventory_management.core.validator.CoreValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class LoginController extends CoreController<LoginForm> {

  private static final String EVENT_ID_INDEX = "login";

  private static final String EVENT_ID_LOGIN = "login_03";

  private final UserDetailsService userDetailsService;

  private final AuthenticationManager authenticationManager;

  @Override
	@ModelAttribute
	protected LoginForm getModelAttribute() {
		return new LoginForm();
	}

  
  @NoAuthorityRequiredEvent
  @RequestMapping(value = EVENT_ID_INDEX)
  public String doEventIndex(LoginForm form) {
    super.clearForm(form);

    return SUCCESS;
  }

  @NoAuthorityRequiredEvent
  @RequestMapping(value = EVENT_ID_LOGIN)
  public String doEventLogin(LoginForm form) {
    loginBase(form);

    return super.redirect(form.getFunctionId(), form.getEventId());
  }

  private void loginBase(LoginForm form) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(form.getEmail());
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, form.getPassword(), userDetails.getAuthorities());

    try {
      authenticationManager.authenticate(usernamePasswordAuthenticationToken);

      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    } catch (Exception e) {
      throw new RuntimeException("Authentication failed: " + e.getMessage());
    }
  }

  @Override
  protected void validate(LoginForm form) {
    if (EVENT_ID_LOGIN.equals(this.getEventId())) {
      CoreValidator.isEmail(this, form.getEmail(), "email", true);
    }
  }

  @Override
  protected void verifyStrictly(LoginForm form) {
    
  }
}
