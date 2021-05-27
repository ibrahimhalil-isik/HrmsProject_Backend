package kodlamaio.hrms.business.concretes;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kodlamaio.hrms.business.abstracts.AuthService;
import kodlamaio.hrms.business.abstracts.CandidateService;
import kodlamaio.hrms.business.abstracts.EmployerService;
import kodlamaio.hrms.business.abstracts.UserService;
import kodlamaio.hrms.business.abstracts.VerificationCodeService;
import kodlamaio.hrms.core.utilities.adapters.ValidationService;
import kodlamaio.hrms.core.utilities.results.ErrorResult;
import kodlamaio.hrms.core.utilities.results.Result;
import kodlamaio.hrms.core.utilities.results.SuccessResult;
import kodlamaio.hrms.core.utilities.verification.VerificationService;
import kodlamaio.hrms.entities.concretes.Candidate;
import kodlamaio.hrms.entities.concretes.Employer;
import kodlamaio.hrms.entities.concretes.VerificationCode;

@Service
public class AuthManager implements AuthService{	
	
	private CandidateService candidateService;
	private EmployerService employerService;
	private VerificationCodeService verificationCodeService;
	private VerificationService verificationService;
	private UserService userService;
	private ValidationService validationService;
	
	@Autowired
	public AuthManager(CandidateService candidateService, UserService userService, EmployerService employerService,
			VerificationService verificationService, ValidationService validationService,
			VerificationCodeService verificationCodeService) {
		super();
		this.userService = userService;
		this.employerService = employerService;
		this.candidateService = candidateService;
		this.verificationService = verificationService;
		this.validationService = validationService;
		this.verificationCodeService = verificationCodeService;
	}
	
	
	@Override
	public Result registerEmployer(Employer employer, String confirmPassword) {

		if (!checkIfNullInfoForEmployer(employer)) {

			return new ErrorResult("Eksik bilgi girdiniz. Lütfen bütün boşlukları doldurun.");
		}

		if (!checkIfEqualEmailAndDomain(employer.getEmail(), employer.getWebAddress())) {

			return new ErrorResult("Geçersiz e-posta adresi.");
		}

		if (!checkIfEmailExists(employer.getEmail())) {

			return new ErrorResult(employer.getEmail() + " zaten kayıtlı bir email girdiniz.");
		}

		if (!checkIfEqualPasswordAndConfirmPassword(employer.getPassword(), confirmPassword)) {

			return new ErrorResult("Parolalar uyuşmuyor.");
		}

		employerService.add(employer);
		//String code = verificationService.sendCode();;
		//verificationCodeRecord(code, employer.getId(), employer.getEmail());
		return new SuccessResult("Kayıt başarıyla tamamlandı.");

	}
	
	@Override
	public Result registerCandidate(Candidate candidate, String confirmPassword) {

		if (checkIfRealPerson(candidate.getIdentityNumber(), candidate.getFirstName(),
				candidate.getLastName(), candidate.getBirthYear()) == false) {
			return new ErrorResult("TCKN doğrulanamadı.");
		}

		if (!checkIfNullInfoForJobseeker(candidate, confirmPassword)) {

			return new ErrorResult("Eksik bilgi girdiniz. Lütfen bütün boşlukları doldurun.");
		}

		if (!checkIfExistsTcNo(candidate.getIdentityNumber())) {

			return new ErrorResult(candidate.getIdentityNumber() + " zaten kayıtlı bir TCKN girdiniz.");
		}

		if (!checkIfEmailExists(candidate.getEmail())) {

			return new ErrorResult(candidate.getEmail() + " zaten kayıtlı bir email girdiniz.");
		}
		if (!checkIfEqualPasswordAndConfirmPassword(candidate.getPassword(), confirmPassword)) {

			return new ErrorResult("Parolalar uyuşmuyor.");
		}

		
		candidateService.add(candidate);
		//String code = verificationService.sendCode();
		//verificationCodeRecord(code, candidate.getId(), candidate.getEmail());	
		return new SuccessResult("Kayıt başarıyla tamamlandı.");
	}

	
	
	
	// Validation for employer register ---START---

		private boolean checkIfNullInfoForEmployer(Employer employer) {

			if (employer.getCompanyName() != null && employer.getWebAddress() != null && employer.getEmail() != null
					&& employer.getPhoneNumber() != null && employer.getPassword() != null) {

				return true;
				
			}

			return false;
		}

		private boolean checkIfEqualEmailAndDomain(String email, String website) {
			String[] emailArr = email.split("@", 2);
			String domain = website.substring(4, website.length());

			if (emailArr[1].equals(domain)) {

				return true;
			}

			return false;
		}

		// Validation for employer register ---END---

		// Validation for candidate register ---START---
		
		private boolean checkIfNullInfoForJobseeker(Candidate candidate, String confirmPassword) {

			if (candidate.getFirstName() != null && candidate.getLastName() != null && candidate.getIdentityNumber() != null
					&& candidate.getBirthYear() != 0 && candidate.getPassword() != null && candidate.getEmail() != null
					&& confirmPassword != null) {

				return true;

			}

			return false;
		}

		private boolean checkIfExistsTcNo(String identityNumber) {

			if (this.candidateService.getByIdentityNumber(identityNumber).getData() == null) {
				return true;
			}
			return false;
		}

		private boolean checkIfRealPerson(String identityNumber, String firstName, String lastName, int birthYear) {

			if (validationService.validateByMernis(identityNumber, firstName, lastName, birthYear)) {
				return true;
			}
			return false;
		}

		// Validation for candidate register ---END---

		// Common Validation

		private boolean checkIfEmailExists(String email) {

			if (this.userService.getUserByEmail(email).getData() == null) {

				return true;
			}

			return false;
		}

		private boolean checkIfEqualPasswordAndConfirmPassword(String password, String confirmPassword) {

			if (!password.equals(confirmPassword)) {
				return false;
			}

			return true;
		}
		
		private void verificationCodeRecord(String code, int id, String email) {
			
			VerificationCode verificationCode = new VerificationCode(id, code, false, LocalDate.now());
			this.verificationCodeService.add(verificationCode);
			System.out.println("Doğrulama kodu " + email +" adresine gönderildi");
		
		}
}
