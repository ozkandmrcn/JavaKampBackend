package com.etiya.recap.entities.concretes;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "rentals", "carImages" })
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "modelYear")
	private int modelYear;

	@Column(name = "dailyPrice")
	private double dailyPrice;

	@Column(name = "description")
	private String description;

	@Column(name = "findeksScore")
	private int findeksScore;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;

	@ManyToOne
	@JoinColumn(name = "color_id")
	private Color color;

	@OneToMany(mappedBy = "car")
	private List<Rental> rentals;

	@OneToMany(mappedBy = "car")
	private List<CarImages> carImages;

}
