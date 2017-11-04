package spittr.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 用户更新信息
 *
 * @author wq
 * repository：2016-11-19
 *
 */
@Entity
@Table(name="spittle")
public class Spittle  extends spittr.model.EntityObj {
	@Id
	@Column(name="id",nullable=false,length=4)	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="message")	
	private String message;
	
	@Column(name="time")	
	private Date time;
	
	@Column(name="latitude")	
	private Double latitude;
	
	@Column(name="longitude")	
	private Double longitude;

	//空构造函数用于thymeleaf参数传递
	public Spittle(){}
	
	public Spittle(String message,Date time)
	{
		this(message,time,null,null);
	}
	
	public Spittle(String message,Date time,Double longitude,Double latitude){
		this.id=null;
		this.message=message;
		this.time=time;
		this.longitude=longitude;
		this.latitude=latitude;
	}
	
	@Override
	public boolean equals(Object that) {
		return EqualsBuilder.reflectionEquals(this,that,"id","time");
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "id","time");
	}


	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Long getId() {
		return id;
	}
	public String getMessage() {
		return message;
	}
	public Date getTime() {
		return time;
	}
}
