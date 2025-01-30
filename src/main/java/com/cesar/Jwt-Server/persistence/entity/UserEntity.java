package com.cesar.Jwt-Server.persistence.entity;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String username;
	private String password;
	
	@Column(name = "is_enabled")
	private boolean isEnabled;
	
	@Column(name = "account_no_expired")
	private boolean accountNoExpired;
	
	@Column(name = "account_no_locked")
	private boolean accountNoLocked;
	
	@Column(name = "credential_no_expired")
	private boolean credentialNoExpired;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", 
		joinColumns = @JoinColumn(name = "user_id"), 
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	Set<RolesEntity> roles = new HashSet<>();
}