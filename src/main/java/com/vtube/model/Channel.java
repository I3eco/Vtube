package com.vtube.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "channels")
@Entity
public class Channel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "name")
	private String name;

	// One channel can have multiple videos
	@OneToMany(mappedBy = "owner")
	private Set<Video> ownedVideos;
	
	// One user can have multiple channels
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	private User owner;

	// Many users can subscribe to many channels
	@ManyToMany
	@JoinTable(
			name = "subscribed_channels", 
			joinColumns = @JoinColumn(name = "channel_id"), 
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> usersSubscribedToChannel;

}
