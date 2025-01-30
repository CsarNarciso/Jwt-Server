package com.cesar.Jwt-Server.persistence.entity;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private RoleEnum name;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "roles_permissions", 
		joinColumns = @JoinColumn(name = "role_id"), 
		inverseJoinColumns = @JoinColumn(name = "permission_id"))
	Set<PermissionEntity> permissions = new HashSet<>();
}