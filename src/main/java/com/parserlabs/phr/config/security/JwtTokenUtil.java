package com.parserlabs.phr.config.security;

import static com.parserlabs.phr.constants.Constants.CLIENT_ID;
import static org.keycloak.util.TokenUtil.TOKEN_TYPE_REFRESH;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.parserlabs.phr.constants.Constants;
import com.parserlabs.phr.entity.PhrUserEntity;
import com.parserlabs.phr.model.UserDTO;
import com.parserlabs.phr.model.response.JwtResponse;
import com.parserlabs.phr.repository.UserRepository;
import com.parserlabs.phr.utils.GeneralUtils;
import com.parserlabs.phr.utils.PhrUtilits;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	private static final String TOKEN_TYPE = "typ";

	@Value("${jwt.user.token.validity.sec: 604800}") // Defaults to 7 days
	public long JWT_USER_TOKEN_VALIDITY_IN_SEC;

	@Value("${jwt.user.refresh.token.validity.sec: 630720000}")  // Defaults to 20 years
	public long JWT_USER_REFRESH_TOKEN_VALIDITY_IN_SEC;

	private static final String SYSTEM = "ABHA-A"; 
	private static final String KEY_PATH = "secret/";
	private static final String PUBLIC_KEY_PEM_FILE_NAME = "phr_public_key.pem";
	private static final String PRIVATE_KEY_PEM_FILE_NAME = "phr_private_key_pkcs8.pem";

	private static PrivateKey privateKey;

	public static String publicKeyContent;

	public static PublicKey publicKey;

	public static String privateKeyContent;
	
	@Autowired
	private UserRepository userRepository;


	static {
		try {
			loadKeys();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			log.error("An exception occured during loading keys:", e);
			System.exit(0);
		}
	}

	private static void loadKeys() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

		publicKeyContent = loadData(PUBLIC_KEY_PEM_FILE_NAME);
		privateKeyContent = loadData(PRIVATE_KEY_PEM_FILE_NAME);
		privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "");

		privateKeyContent = privateKeyContent.replaceAll(" ", ""); // Remove any white-spaces.
		privateKeyContent = privateKeyContent.replaceAll("(\\r\\n|\\n|\\r)", ""); // Remove any "\r\n".

		String publicKeyContentTrimmed = publicKeyContent.replaceAll("\\n", "")
				.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
		publicKeyContentTrimmed = publicKeyContentTrimmed.replaceAll(" ", ""); // Remove any white-spaces.
		publicKeyContentTrimmed = publicKeyContentTrimmed.replaceAll("(\\r\\n|\\n|\\r)", ""); // Remove any "\r\n".

		KeyFactory kf = KeyFactory.getInstance("RSA");

		try {
			X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(
					Base64.getDecoder().decode(publicKeyContentTrimmed));
			publicKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
			log.info("public Key Loaded Successfully");

			PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
			privateKey = kf.generatePrivate(keySpecPKCS8);
			log.info("private Key Loaded Successfully");

		} catch (Exception exp) {
			log.error("Exception occured:", exp);
		}
	}

	public static String loadData(String fileName) {
		String content = null;
		final String filePath = KEY_PATH + fileName;
		try {
			InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
			StringBuilder contentBuilder = new StringBuilder();
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(resourceAsStream));
			String line;
			while ((line = bufferReader.readLine()) != null) {
				contentBuilder.append(line + System.lineSeparator());
			}
			content = contentBuilder.toString();
			log.info("{} file loaded successfully", fileName);
		} catch (Exception e) {
			log.error("Exception occured while reading file: {} Error Msg : ", fileName, e.getMessage());
		}
		return content;
	}

	// Print structure of JWT
	public void printStructure(String token, PublicKey publicKey) {
		Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
		log.debug("Header     : " + parseClaimsJws.getHeader());
		log.debug("Body       : " + parseClaimsJws.getBody());
		log.debug("Signature  : " + parseClaimsJws.getSignature());
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieving any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody();
	}

	public String getSubjectFromToken(String token) {
		String subject = null;
		Claims claim = null;
		try {
			claim = Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException exp) {
			subject = exp.getClaims().getSubject();
			log.warn("token expired for id : {} ", subject);
		}
		return !StringUtils.isEmpty(subject) ? subject : claim.getSubject();
	}

	// generate token for user
//	public String generateToken(PhrUserEntity transection) {
//		Map<String, Object> claims = new HashMap<>();
//		return doGenerateToken(claims, transection.getHealthIdNumber());
//	}

	public String generatePhrAddressToken(String phrAddress) {
		Optional<PhrUserEntity> phrUser=null;
		if(null!=phrAddress&&!phrAddress.isEmpty()) {
		   phrUser=userRepository.findByPhrAddress(phrAddress);
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put(Constants.PHR_ADDRESS, GeneralUtils.sanetizePhrAddress(phrAddress));
		claims.put(Constants.CLIENT_ID, PhrUtilits.clientId());
		claims.put(Constants.SYSTEM,SYSTEM);
		if(phrUser.isPresent()) {
			PhrUserEntity phruser=phrUser.get();
			Stream<String> words = Stream.of(phruser.getFirstName(), phruser.getMiddleName(),phruser.getLastName());
			String fullName=words.filter(x->x!=null && !x.isEmpty()).collect(Collectors.joining(" "));	
			claims.put(Constants.PHR_MOBILE, chekingNotNullValue(phruser.getMobile()));	
			claims.put(Constants.HEALTH_ID_NUMBER, chekingNotNullValue(phruser.getHealthIdNumber()));
			claims.put(Constants.DAY_OF_BIRTH, chekingNotNullValue(phruser.getDayOfBirth()));
			claims.put(Constants.MONTH_OF_BIRTH, chekingNotNullValue(phruser.getMonthOfBirth()));
			claims.put(Constants.YEAR_OF_BIRTH, chekingNotNullValue(phruser.getYearOfBirth()));
			claims.put(Constants.GENDER, chekingNotNullValue(phruser.getGender()));
			claims.put(Constants.FULL_NAME, chekingNotNullValue(fullName));
			claims.put(Constants.EMAIL, chekingNotNullValue(phruser.getEmail()));
			claims.put(Constants.ADDRESS_LINE, chekingNotNullValue(phruser.getAddress().getAddressLine()));
			claims.put(Constants.STATE_NAME, chekingNotNullValue(phruser.getAddress().getStateName()));
			claims.put(Constants.DISTRICT_NAME, chekingNotNullValue(phruser.getAddress().getDistrictName()));
			claims.put(Constants.PINCODE, chekingNotNullValue(phruser.getAddress().getPincode()));
			claims.put(Constants.MOBILE, chekingNotNullValue(phruser.getMobile()));
		}

		addClaims(claims);
		return doGenerateToken(claims, GeneralUtils.sanetizePhrAddress(phrAddress));
	}

	public JwtResponse generateTokens(UserDTO phrAuthTransactionEntity) {
		String accessToken = generatePhrAddressToken(phrAuthTransactionEntity);
		String refreshToken = generateRefreshToken(phrAuthTransactionEntity.getPhrAddress());
		return generateTokens(accessToken, refreshToken);
	}

	private JwtResponse generateTokens(String accessToken, String refreshToken) {
		return JwtResponse.builder().token(accessToken).refreshExpiresIn(JWT_USER_REFRESH_TOKEN_VALIDITY_IN_SEC)
				.refreshToken(refreshToken).expiresIn(JWT_USER_TOKEN_VALIDITY_IN_SEC).build();
	}

	public String generateRefreshToken(String healthIDNumber) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLIENT_ID, PhrUtilits.clientId());
		claims.put(TOKEN_TYPE, TOKEN_TYPE_REFRESH);
		claims.put(Constants.SYSTEM,SYSTEM);
		return generateRefreshToken(claims, healthIDNumber, JWT_USER_REFRESH_TOKEN_VALIDITY_IN_SEC);
	}

	private String generateRefreshToken(Map<String, Object> claims, String subject, long validaity) {
		return doGenerateToken(claims, subject, validaity * 1000);
	}

	

	// generate token for user
	public String generatePhrAddressToken(UserDTO userDto) {
		Stream<String> fullNameStream = Stream.of(userDto.getFirstName(), userDto.getMiddleName(),userDto.getLastName());
		String fullName=fullNameStream.filter(x->x!=null && !x.isEmpty()).collect(Collectors.joining(" "));
		Map<String, Object> claims = new HashMap<>();
		claims.put(Constants.TRANSECTION_ID, chekingNotNullValue(userDto.getAuthTransactionId()));
		claims.put(Constants.PHR_ADDRESS, GeneralUtils.sanetizePhrAddress(userDto.getPhrAddress()));
		claims.put(Constants.PHR_MOBILE, chekingNotNullValue(userDto.getMobile()));
		claims.put(Constants.CLIENT_ID, PhrUtilits.clientId());
		claims.put(Constants.SYSTEM,SYSTEM);	
		claims.put(Constants.HEALTH_ID_NUMBER, chekingNotNullValue(userDto.getHealthIdNumber()));
		claims.put(Constants.DAY_OF_BIRTH, chekingNotNullValue(userDto.getDayOfBirth()));
		claims.put(Constants.MONTH_OF_BIRTH, chekingNotNullValue(userDto.getMonthOfBirth()));
		claims.put(Constants.YEAR_OF_BIRTH, chekingNotNullValue(userDto.getYearOfBirth()));
		claims.put(Constants.GENDER, chekingNotNullValue(userDto.getGender()));
		claims.put(Constants.FULL_NAME, chekingNotNullValue(fullName));
		claims.put(Constants.EMAIL, chekingNotNullValue(userDto.getEmail()));
		claims.put(Constants.ADDRESS_LINE, chekingNotNullValue(userDto.getAddress().getAddressLine()));
		claims.put(Constants.STATE_NAME, chekingNotNullValue(userDto.getAddress().getStateName()));
		claims.put(Constants.DISTRICT_NAME, chekingNotNullValue(userDto.getAddress().getDistrictName()));
		claims.put(Constants.PINCODE, chekingNotNullValue(userDto.getAddress().getPincode()));
		claims.put(Constants.MOBILE, chekingNotNullValue(userDto.getMobile()));
		addClaims(claims);
		return doGenerateToken(claims, GeneralUtils.sanetizePhrAddress(userDto.getPhrAddress()));
	}


	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	// 3. According to JWS Compact
	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	// compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return doGenerateToken(claims, subject, JWT_USER_TOKEN_VALIDITY_IN_SEC * 1000);
	}

	private String doGenerateToken(Map<String, Object> claims, String subject, long duration) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + duration))
				.signWith(SignatureAlgorithm.RS512, privateKey).compact();
	}

	public Object getValuefromToken(String token, String key) {
		return getAllClaimsFromToken(token).get(key);
	}

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve username from jwt token
	public String getClientIDFromToken(String token) {
		Object clientID = getValueFromToken(token, CLIENT_ID);
		return Objects.nonNull(clientID) ? (String) clientID : null;
	}

	public Object getValueFromToken(String token, String key) {
		return getAllClaimsFromToken(token).get(key);
	}

	// validate token
	public Boolean validateToken(String token, UserDTO user) {
		final String phrAddress = getUsernameFromToken(token);
		return (phrAddress.equals(user.getPhrAddress()) && !isTokenExpired(token));
	}

	// check if the token has expired
	public boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	/**
	 * Get User token Expire time set in system.
	 * 
	 * @return user token expire time in seconds.
	 */
	public long getUserSessionExpiry() {
		return JWT_USER_TOKEN_VALIDITY_IN_SEC;

	}

	private void addClaims(Map<String, Object> claims) {
		addClaims(claims, Constants.REQUESTER_ID_HEADER, "PHR-WEB");
	}

	private void addClaims(Map<String, Object> claims, String claimName, Object claimValue) {
		if (Objects.nonNull(claimValue)) {
			claims.put(claimName, claimValue);
		}
	}

	private String chekingNotNullValue(String value)
	{
		return StringUtils.isNotBlank(value)?value:null;
	}
}
