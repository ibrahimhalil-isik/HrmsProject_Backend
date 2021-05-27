package kodlamaio.hrms.core.utilities.adapters;

public interface ValidationService {
	
	boolean validateByMernis(String identityNumber, String firstName, String lastName, int birthYear);
}
