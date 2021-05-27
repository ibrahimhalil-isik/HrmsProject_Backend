package kodlamaio.hrms.core.utilities.adapters;

import org.springframework.stereotype.Service;

import kodlamaio.hrms.mernisService.FakeMernisService;

@Service
public class MernisServiceAdapter implements ValidationService {

	@Override
	public boolean validateByMernis(String identityNumber, String firstName, String lastName, int birthYear) {

		FakeMernisService fakeMernisService = new FakeMernisService();
		
		boolean result = true;
		try {
			result = fakeMernisService.ValidateByPersonalInfo(identityNumber, firstName, lastName, birthYear);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
}
