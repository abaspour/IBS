package com.nicico.ibs.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table( schema = "MAINPARTS", name = "TBL_USER")
public class User implements UserDetails {
	private static final long serialVersionUID = 6311364761937265306L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	@SequenceGenerator(name = "user_seq", sequenceName = "FB.SEQ_INQUIRY", allocationSize = 1)
	private Long id;

	@Column(name = "USERNAME", length = 50)
	private String username;

	@Column(name = "PASSWORD", length = 50)
	private String password;

	@Column(name = "FIRST_NAME", length = 50)
	private String firstName;

	@Column(name = "LAST_NAME", length = 100)
	private String lastName;

	@Column(name = "password2", length = 50)
	private String password2;

	@Column(name = "FILTER_REQUESTER")
	private String filterRequester;

	@Column(name = "FILTER_PLACE")
	private String filterPlace;

	@Column(name = "FILTER_CITY")
	private String filterCity;

	@Column(name = "FILTER_REQUNIT")
	private String filterRequnit;

	@Column(name = "EN_LAST_NAME")
	private String enLastName;

	@Column(name = "EN_FIRST_NAME")
	private String enFirstName;

	@Column(name = "ENABLED")
	private boolean enabled;

	@Column(name = "USER_TYPE")
	private String userType;

	@Column(name = "SUPPLIERID")
	private Long supplierId;

	@Column(name = "CURRENCYID")
	private Long currencyId;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(schema = "MAINPARTS",name = "TBL_USER_ROLE",
			joinColumns        = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "ROLE_ID",  referencedColumnName = "ID")}
	)
	private Set<Role> roles;


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		if(this.getRoles() != null && this.getRoles().size()>0) {
			for(Role tblRole : this.getRoles()) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(tblRole.getRolename());
				authorities.add(authority);
			}
		}
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		//return true = account is valid / not expired
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		//return true = account is not locked
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		//return true = password is valid / not expired
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}


}
