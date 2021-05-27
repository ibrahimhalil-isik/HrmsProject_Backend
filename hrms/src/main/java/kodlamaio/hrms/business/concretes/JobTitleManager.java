package kodlamaio.hrms.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kodlamaio.hrms.business.abstracts.JobTitleService;
import kodlamaio.hrms.core.utilities.results.DataResult;
import kodlamaio.hrms.core.utilities.results.ErrorResult;
import kodlamaio.hrms.core.utilities.results.Result;
import kodlamaio.hrms.core.utilities.results.SuccessDataResult;
import kodlamaio.hrms.core.utilities.results.SuccessResult;
import kodlamaio.hrms.dataAccess.abstracts.JobTitleDao;
import kodlamaio.hrms.entities.concretes.Employee;
import kodlamaio.hrms.entities.concretes.JobTitle;
import kodlamaio.hrms.entities.concretes.User;

@Service
public class JobTitleManager implements JobTitleService {

	private JobTitleDao jobTitleDao;
	
	@Autowired
	public JobTitleManager(JobTitleDao jobTitleDao) {
		super();
		this.jobTitleDao = jobTitleDao;
	}


	@Override
	public DataResult<List<JobTitle>> getAll() {		
		return new SuccessDataResult<List<JobTitle>>(this.jobTitleDao.findAll());
	}


	@Override
	public Result add(JobTitle jobTitle) {
		
		if(!this.isTitleExist(jobTitle.getTitle())) {
			
			return new ErrorResult("Hata : Zaten mevcut bir iş pozisyonu girdiniz. Yeni iş pozisyonu eklenemedi!");
		}
		else 
		{
			this.jobTitleDao.save(jobTitle);
		    return new SuccessResult("Yeni iş pozisyonu eklendi.");
		}		
	}
	
	private boolean isTitleExist(String titleName) {
		if(this.jobTitleDao.findByTitle(titleName) != null) {
			return false;
		}
		return true;
		
	}

}