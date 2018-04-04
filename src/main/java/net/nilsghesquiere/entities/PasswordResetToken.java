package net.nilsghesquiere.entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name ="verificationtokens")
@EqualsAndHashCode(exclude={"user"})
public class PasswordResetToken implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final int EXPIRATION = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String token;
	@OneToOne(targetEntity= User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	private Date expiryDate;

	public PasswordResetToken(){}
	
	public PasswordResetToken(String token, User user) {
		this.token = token;
		this.user= user;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}
	
	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}

	public void updateToken(String token) {
		this.token = token;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}
}
