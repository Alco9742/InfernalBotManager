package net.nilsghesquiere.registration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.nilsghesquiere.entities.User;

import org.springframework.context.ApplicationEvent;

@Data
@EqualsAndHashCode(callSuper=false)
public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	private String appUrl;
	private User user;
 
	public OnRegistrationCompleteEvent(User user, String appUrl) {super(user);
		this.user = user;
		this.appUrl = appUrl;
	}
}
