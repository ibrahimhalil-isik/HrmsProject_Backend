package kodlamaio.hrms.mernisService;

public class FakeMernisService {
	
	public boolean ValidateByPersonalInfo(String identityNumber, String firstName, String lastName, int birthYear)
	{
		System.out.println(firstName + " " + lastName + " geçerli kişi.");
		return true;
	}

}
