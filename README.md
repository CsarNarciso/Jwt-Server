First, we create a spring boot project with spring security dependency

Then, create a class for spring secutiry config with the following annotations
@configuration
@EnableWebSecurity

Inside, we have the following beans methods
SecurityFilterChain
AuthenticationManager
AuthenticationProvider - DaoAuthenticationProvider for DB integration 
UserDetailsService
PasswordEncoder - NoOpPasswordEncoder for no encryptation (just for tests! Don't do this in prod)
				BCryptPasswordEncoder for encrypted passwords.

We include auth0 java-jwt dependency (from JWT.io guides for java integration), following 
the github link to the oficial documentation for more details.

Generate encrypted secret for our jwt utils class. Put it in .properties or yml file
The same for the issuer token generator name: you can use the name of your app/backend
(who generates the token) here
Create /util/JwtUtils class

and create the following methods

createToken
validate
extractUsername
getSpecificClaim
getAllClaims

Create /security/filter/JwtTokenValidator class wich extends from OncePreRequestFilter
Override the doFilterInternal method:
check if token is on request header
extract it withou the bearer prefix
validate it with JwtUtils class
if valid, you get decodedtoken
extract username and authorities as string from JwtUtils methods
convert authorities as string to Collection of grantedAuthority objects with 
AuthorityUtils.commaSeparatedStringToAuthorityList
get secutirtycontext from contextholder
create authentication object from usernamepasswordauthenticationtoken(username, null password and authorities)
set autehntication on securitycontext
set context on securityContextHolder to authenticate user
continue filter chain with filterchain.dofilter(request, response)
if not token it will fail next filter (BasicAuthenticationFilter, wich verifies if user is already placed
on the SecurityContextHolder -authenticated-)
If not valid, it will break from there due to the JWTValidationException threw on the JwtUtils validate method
Add our new jwt filter to the filter chain (before the BasicAuthenticationFilter). On the securityConfig class,
on the securityFilterchain bean method, over the httpsecurity, we add 
.addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class) so that it validates, on each
request, first the jwt toekn sent on the bearer request header, if valid, it autehnticates the user on the 
security context holder, then, the BasicAuthenticationFilter verifies this and can give succesfful access to the
next filters because the user is already authenticated. 

Create authController class for auth operations (log in and sign up)
POST login() receives as request body the user credentials (username, password), the response contains jwt token if
successful auth
we call service method for login (we can place it in userdetailserviceimpl class)
login method receives the credentials and with them calls authenticate by username and password method (we can place
it in the same service too), wich receives the credentials, search the user in the db (with the already overrided
loadbyusernameandpassword method) and if user exists and decoded encoded password (from the user detials object)
matchs the sent credentials password, it's successful authentication and we authenticate the user in the security 
context holder, then return the authentication object 
(usernamePasswordAuthenticationToken(username, null password, user.getPermissions()))
If successful auth, login method calls jwtUtils.createToken(received auth object) and return the jwt token
controller method returns this token
if not succesfful auth (no existent user, bad credentials), throw BadCredentialsException("message")
Configure in security config auth controller operations as permitall


