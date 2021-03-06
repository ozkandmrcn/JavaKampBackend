package com.etiya.recap.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etiya.recap.business.abstracts.RentalService;
import com.etiya.recap.business.constants.Messages;
import com.etiya.recap.core.utilities.business.BusinessRules;
import com.etiya.recap.core.utilities.results.DataResult;
import com.etiya.recap.core.utilities.results.ErrorResult;
import com.etiya.recap.core.utilities.results.Result;
import com.etiya.recap.core.utilities.results.SuccessDataResult;
import com.etiya.recap.core.utilities.results.SuccessResult;
import com.etiya.recap.dataAccess.abstracts.CarDao;
import com.etiya.recap.dataAccess.abstracts.CustomerDao;
import com.etiya.recap.dataAccess.abstracts.RentalDao;
import com.etiya.recap.entities.concretes.Car;
import com.etiya.recap.entities.concretes.Customer;
import com.etiya.recap.entities.concretes.Rental;
import com.etiya.recap.entities.requests.CreateRentalRequest;

@Service
public class RentalManager implements RentalService {

	private RentalDao rentalDao;
	private CarDao carDao;
	private CustomerDao customerDao;

	@Autowired
	public RentalManager(RentalDao rentalDao, CarDao carDao, CustomerDao customerDao) {
		super();
		this.rentalDao = rentalDao;
		this.carDao = carDao;
		this.customerDao = customerDao;
	}

	@Override
	public DataResult<List<Rental>> getAll() {
		return new SuccessDataResult<List<Rental>>(this.rentalDao.findAll(), Messages.GetAll);
	}

	@Override
	public Result add(CreateRentalRequest createRentalRequest) {

		Car car = new Car();
		car.setId(createRentalRequest.getCarId());
		car.setFindeksScore(this.carDao.getFindeksScoreByCarId(car.getId()));

		Customer customer = new Customer();
		customer.setCustomerId(createRentalRequest.getCustomerId());
		customer.setFindeksScore(this.customerDao.getFindeksScoreByCustomerId(customer.getCustomerId()));

		Rental rental = new Rental();
		rental.setRentDate(createRentalRequest.getRentDate());
		rental.setReturnDate(createRentalRequest.getReturnDate());
		rental.setCar(car);
		rental.setCustomer(customer);

		var result = BusinessRules.run(checkCarIsReturned(car.getId()), checkFindeksScore(car, customer));

		if (result != null) {
			return result;
		}

		//System.out.println(rental.getReturnDate());
		this.rentalDao.save(rental);
		return new SuccessResult(true, Messages.Add);
	}

	@Override
	public DataResult<Rental> getById(int id) {
		return new SuccessDataResult<Rental>(this.rentalDao.getById(id), Messages.GetById);
	}

	@Override
	public Result delete(CreateRentalRequest createRentalRequest) {

		Rental rental = new Rental();
		rental.setId(createRentalRequest.getId());

		this.rentalDao.delete(rental);
		return new SuccessResult(true, Messages.Delete);
	}

	@Override
	public Result update(CreateRentalRequest createRentalRequest) {

		Car car = new Car();
		car.setId(createRentalRequest.getCarId());

		Customer customer = new Customer();
		customer.setCustomerId(createRentalRequest.getCustomerId());

		Rental rental = new Rental();
		rental.setId(createRentalRequest.getId());
		rental.setRentDate(createRentalRequest.getRentDate());
		rental.setReturnDate(createRentalRequest.getReturnDate());

		rental.setCar(car);
		rental.setCustomer(customer);

		this.rentalDao.save(rental);
		return new SuccessResult(true, Messages.Update);
	}

	private Result checkCarIsReturned(int carId) {
		List<Rental> checkRentalReturnDate = this.rentalDao.getByCar_id(carId);
		for (Rental rental2 : checkRentalReturnDate) {
			if (rental2.getReturnDate() == null) {
				return new ErrorResult(Messages.ErrorIfCarIsNotAvailable);
			}
		}
		
		return new SuccessResult();
	}

	private Result checkFindeksScore(Car car, Customer customer) {
		if (customer.getFindeksScore() < car.getFindeksScore()) {
			return new ErrorResult(Messages.ErrorFindeksScore);
		}
		return new SuccessResult();
	}

}
